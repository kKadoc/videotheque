package fr.mmtech.service.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mmtech.domain.File;
import fr.mmtech.domain.Video;
import fr.mmtech.domain.VideoType;
import fr.mmtech.repository.FileRepository;
import fr.mmtech.repository.VideoRepository;
import fr.mmtech.repository.VideoTypeRepository;
import fr.mmtech.service.VideoService;

public class ReloadUtil {

    private VideoRepository videos;
    private FileRepository files;
    private VideoTypeRepository typeRepo;

    private final Logger log = LoggerFactory.getLogger(VideoService.class);

    public ReloadUtil(VideoRepository videos, FileRepository files, VideoTypeRepository types) {
	this.videos = videos;
	this.files = files;
	this.typeRepo = types;
    }

    public void go() {
	try {

	    List<String> lignes = Files.readAllLines(Paths.get("C:\\tmp\\file.txt"));
	    for (String l : lignes) {
		String[] i = l.split(",");
		log.debug(i[1] + " " + i[2]);

		File f = new File();
		f.setPath(i[1]);
		f.setType(i[2]);
		files.saveAndFlush(f);

	    }

	    lignes = Files.readAllLines(Paths.get("C:\\tmp\\video.txt"));
	    for (String l : lignes) {
		String[] i = l.split(",");
		log.debug(l);

		Video v = new Video();
		v.setImdbId(i[1]);
		v.setTitle(i[2]);
		v.setYear(Integer.decode(i[3]));
		v.setRate(buildRate(i[4]));
		v.setDuration(buildDuration(i[5]));
		v.setFavoris(i[6] == "1");
		v.setAddDate(new DateTime(Long.decode(i[7])));
		videos.saveAndFlush(v);
		v.setVideoFile(files.findOne(Long.decode(i[8])));

		v = videos.findOneByImdbId(i[1]);
		if (i[9] != null && !i[9].isEmpty() && !i[9].trim().equals("NULL")) {
		    v.setImgFile(files.findOne(Long.decode(i[9])));
		}
		if (i[10] != null && !i[10].isEmpty() && !i[10].trim().equals("NULL")) {
		    v.setSubFile(files.findOne(Long.decode(i[10])));
		}
		videos.saveAndFlush(v);

	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
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
	    Optional<VideoType> type = typeRepo.findOneByName(strType);
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
