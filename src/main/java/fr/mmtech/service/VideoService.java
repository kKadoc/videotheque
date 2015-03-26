package fr.mmtech.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import fr.mmtech.domain.File;
import fr.mmtech.domain.Video;
import fr.mmtech.domain.VideoType;
import fr.mmtech.repository.ConfigFieldRepository;
import fr.mmtech.repository.FileRepository;
import fr.mmtech.repository.VideoRepository;
import fr.mmtech.repository.VideoTypeRepository;
import fr.mmtech.service.util.IMDBUtil;
import fr.mmtech.web.rest.dto.GuessDTO;
import fr.mmtech.web.rest.dto.VideoImdbDTO;

@Service
@Transactional
public class VideoService {

    @Inject
    private VideoRepository videoRepository;

    @Inject
    private FileRepository fileRepository;

    @Inject
    private VideoTypeRepository typeRepository;

    @Inject
    private ConfigFieldRepository configRepository;

    private final Logger log = LoggerFactory.getLogger(VideoService.class);

    /**
     * Lance la lecture de la video
     * 
     * @param id
     * @throws Exception
     */
    public void play(Long id) throws Exception {
	Video video = videoRepository.findOneWithEagerRelationships(id);

	Runtime runtime = Runtime.getRuntime();
	String path = configRepository.getPath();
	System.out.println(path);

	String cmd = "cmd /c start \"\" \"" + path + video.getVideoFile().getPath();
	log.debug("running cmd : " + cmd);

	runtime.exec(cmd);

    }

    /**
     * Interroge IMDB pour tenter de deviner la vidéo du fichier
     * 
     * @param fileName
     * @return
     * @throws Exception
     */
    public List<GuessDTO> guess(String fileName, String keyword) throws Exception {
	String finalKeyword = null;
	if (keyword == null || keyword.isEmpty()) {
	    // pas d'indication supplémentaire, on utilise le nom du fichier
	    // on remplace les caractères spéciaux par des espace pour séparer
	    // les différents mots
	    fileName = fileName.replaceAll("[._']", " ");
	    // on récupère le plus long mot
	    String[] words = fileName.split(" ");
	    finalKeyword = "";
	    for (String w : words) {
			if (w.length() > finalKeyword.length()) {
			    finalKeyword = w;
		    }
		}
	} else {
	    finalKeyword = keyword;
	}

	IMDBUtil imdb = new IMDBUtil();
	List<GuessDTO> list = null;
	list = imdb.getKeywordGuess(finalKeyword);

	return list;
    }

    /**
     * Reçoit un fichier à ajouter dans la videotheque
     * 
     * @param subFile
     * @param file
     * @param imdbId
     * @return
     * @throws Exception
     * @throws IOException
     */
    public String createVideo(MultipartFile dlVideoFile, MultipartFile dlSubFile, String imdbId) throws IOException, Exception {
	String msg = "";

	Video video = new Video();
	IMDBUtil imdb = new IMDBUtil();
	// on rempli avec les infos de imdb
	VideoImdbDTO videoImdb = imdb.getVideo(imdbId);
	log.debug("Video from imdb : " + video);

	if (videoImdb == null) {
	    throw new Exception("Impossible de récupérer les données depuis cet id (" + imdbId + ". Création annulée.");
	}

	video.setImdbId(imdbId);
	video.setTitle(videoImdb.getTitle());
	video.setYear(Integer.decode(videoImdb.getYear()));
	video.setDuration(buildDuration(videoImdb.getDuration()));
	video.setRate(buildRate(videoImdb.getRate()));
	video.setFavoris(false);
	video.setAddDate(new DateTime());
	video.setVideoTypes(buildTypes(videoImdb.getType()));

	// on va ranger les fichiers
	// dossier (titre)
	// fichier video(titre + extension video)
	// poster (titre + extension img)
	// [soustitres] (titre + extension sub)
	String title = buildTitleForFilename(video);

	String basePath = configRepository.getPath();
	// on crée le dossier
	String dirName = java.io.File.separatorChar + title;
	log.debug("dirName : " + dirName);
	java.io.File dir = new java.io.File(basePath + dirName);
	if (dir.exists()) {
	    log.debug("The directory " + dirName + " already exists");
	} else if (!dir.mkdir()) {
	    throw new Exception("Impossible de créer le répertoire " + dirName + ". Création annulée.");
	}

	// on déplace et renome le fichier vidéo
	String newMovieFileName = dirName + java.io.File.separatorChar + title + dlVideoFile.getOriginalFilename().substring(dlVideoFile.getOriginalFilename().lastIndexOf("."));
	log.debug("newMovieFileName : " + newMovieFileName);
	java.io.File movieFile = new java.io.File(basePath + newMovieFileName);
	if (!movieFile.createNewFile()) {
	    throw new Exception("Impossible de créer le fichier vidéo de " + newMovieFileName + ". Création annulée.");
	}

	FileOutputStream fos = new FileOutputStream(movieFile);
	fos.write(dlVideoFile.getBytes());
	fos.close();

	// on associe le fichier à la vidéo
	video.setVideoFile(new File(newMovieFileName, File.VIDEO_FLAG));

	if (dlSubFile != null) {
	    // on récupère e fichier sous titres
	    String newSubFileName = dirName + java.io.File.separatorChar + title + dlSubFile.getOriginalFilename().substring(dlSubFile.getOriginalFilename().lastIndexOf("."));
	    log.debug("newSubFileName : " + newSubFileName);
	    java.io.File subFile = new java.io.File(basePath + newSubFileName);
	    if (!subFile.createNewFile()) {
		log.error("Impossible de créer le fichier de sous-titres de " + newSubFileName + ".");
		msg += "Impossible de créer le fichier de sous-titres de " + newSubFileName + ".<br />";
	    }

	    fos = new FileOutputStream(subFile);
	    fos.write(dlSubFile.getBytes());
	    fos.close();

	    // on associe le fichier à la vidéo
	    video.setSubFile(new File(newSubFileName, File.SUBS_FLAG));
	}

	// on télécharge l'image
	// on vérifie que le nom récupéré depuis imdb est un fichier (extension)
	if (videoImdb.getImg().contains(".")) {
	    String newImgFileName = dirName + java.io.File.separatorChar + title + videoImdb.getImg().substring(videoImdb.getImg().lastIndexOf("."));
	    log.debug("newImgFileName : " + newImgFileName);
	    java.io.File newImgFile = new java.io.File(basePath + newImgFileName);
	    if (newImgFile.exists()) {
		log.debug(newImgFileName + " already exists");
		// le poster existe déjà -> pas besoin de la télécharger
		video.setImgFile(new File(newImgFileName, File.IMG_FLAG));
	    } else {
		// on télécharge le poster
		if (!downloadFile(videoImdb.getImg(), basePath + newImgFileName)) {
		    video.setImgFile(null);
		} else {
		    video.setImgFile(new File(newImgFileName, File.IMG_FLAG));
		}
	    }
	} else {
	    video.setImgFile(null);
	}

	videoRepository.save(video);

	msg += "Vidéo ajoutée avec succès.";
	return msg;

    }

    /**
     * Crée un nom pour un fichier basé sur le titre de la vidéo
     * 
     * @param video
     * @return
     */
    private String buildTitleForFilename(Video video) {
	String title = video.getTitle();
	// on supprime tous les caractères interdits
	title = title.replaceAll("[<>:\"/\\|?*]", "");
	while (title.endsWith(".")) {
	    title = title.substring(0, title.length() - 2);
	}

	return title;
    }

    /**
     * Télécharge le fichier pour le déposer à la destination indiquée
     * 
     * @param source
     * @param destination
     * @return
     */
    private boolean downloadFile(String source, String destination) {
	InputStream input = null;
	FileOutputStream writeFile = null;

	try {
	    URL url = new URL(source);
	    URLConnection connection = url.openConnection();
	    int fileLength = connection.getContentLength();

	    if (fileLength == -1) {
		log.error("Can't download file " + source);
		return false;
	    }

	    input = connection.getInputStream();
	    writeFile = new FileOutputStream(destination);
	    byte[] buffer = new byte[1024];
	    int read;

	    while ((read = input.read(buffer)) > 0)
		writeFile.write(buffer, 0, read);
	    writeFile.flush();
	} catch (IOException e) {
	    log.error("Error while trying to download the file " + source, e);
	} finally {
	    try {
		writeFile.close();
		input.close();
	    } catch (Exception e) {

	    }
	}
	return true;
    }

    /**
     * Met à jour les données de la vidéo depuis imdb
     * 
     * @param id
     * @throws Exception
     */
    public void refreshImdb(Long id) throws Exception {
	// on recup l'ancienne vidéo
	Video video = videoRepository.getOne(id);

	// on la met à jour avec les infos imdb
	IMDBUtil imdb = new IMDBUtil();
	VideoImdbDTO videoImdb = imdb.getVideo(video.getImdbId());
	log.debug("Video from imdb : " + video);

	if (videoImdb == null) {
	    throw new Exception("Impossible de récupérer les données depuis cet id (" + video.getImdbId() + ".");
	}

	video.setTitle(videoImdb.getTitle());
	video.setYear(Integer.decode(videoImdb.getYear()));
	video.setDuration(buildDuration(videoImdb.getDuration()));
	video.setRate(buildRate(videoImdb.getRate()));
	video.setVideoTypes(buildTypes(videoImdb.getType()));

	// maj de la nouvelle image
	String title = buildTitleForFilename(video);

	String basePath = configRepository.getPath();
	// on crée le dossier
	String dirName = java.io.File.separatorChar + title;

	File imgFile = video.getImgFile();
	if (imgFile != null) {
	    // on supprime le fichier
	    java.io.File oldImgFile = new java.io.File(basePath + imgFile.getPath());
	    oldImgFile.delete();

	    fileRepository.delete(imgFile);
	}

	// on télécharge l'image
	// on vérifie que le nom récupéré depuis imdb est un fichier (extension)
	if (videoImdb.getImg().contains(".")) {
	    String newImgFileName = dirName + java.io.File.separatorChar + title + videoImdb.getImg().substring(videoImdb.getImg().lastIndexOf("."));
	    log.debug("newImgFileName : " + newImgFileName);
	    java.io.File newImgFile = new java.io.File(basePath + newImgFileName);
	    if (newImgFile.exists()) {
		log.debug(newImgFileName + " already exists");
		// le poster existe déjà -> pas besoin de la télécharger
		video.setImgFile(new File(newImgFileName, File.IMG_FLAG));
	    } else {
		// on télécharge le poster
		if (!downloadFile(video.getImgFile().getPath(), basePath + newImgFileName)) {
		    video.setImgFile(null);
		} else {
		    video.setImgFile(new File(newImgFileName, File.IMG_FLAG));
		}
	    }
	} else {
	    video.setImgFile(null);
	}

	videoRepository.save(video);
    }

    /**
     * Parse la durée (string) pour la convertir en entier
     * 
     * @param str
     * @return
     */
    private Integer buildDuration(String str) {
	// on vire tous les charactères
	str = str.replaceAll("\\D", "");
	Integer res = Integer.decode(str);
	return res;
    }

    /**
     * Parse la notation (string) pour la convertir en BigDecimal
     * 
     * @param str
     * @return
     */
    private BigDecimal buildRate(String str) {
	try {
	    return new BigDecimal(str);
	} catch (Exception e) {
	    return BigDecimal.ZERO;
	}
    }

    /**
     * Parse les types (string) pour la convertir en liste de VideoType
     * 
     * @param str
     * @return
     */
    private Set<VideoType> buildTypes(String types) {
	Set<VideoType> set = new HashSet<VideoType>();
	String[] strTypes = types.split(",");
	for (String strType : strTypes) {
	    strType = strType.trim();
	    Optional<VideoType> type = typeRepository.findOneByName(strType);
	    if (type.isPresent()) {
		set.add(type.get());
	    } else {
		VideoType newType = new VideoType();
		newType.setName(strType);
		set.add(newType);
	    }
	}

	return set;
    }
}
