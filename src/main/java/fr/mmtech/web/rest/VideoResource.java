package fr.mmtech.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.mmtech.domain.Video;
import fr.mmtech.repository.VideoRepository;
import fr.mmtech.service.VideoService;

/**
 * REST controller for managing Video.
 */
@RestController
@RequestMapping("/api")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class VideoResource {

    private final Logger log = LoggerFactory.getLogger(VideoResource.class);

    @Inject
    private VideoRepository videoRepository;

    @Inject
    private VideoService videoService;

    /**
     * POST /videos -> Create a new video.
     */
    @RequestMapping(value = "/videos", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Video video) throws URISyntaxException {
	log.debug("REST request to save Video : {}", video);
	if (video.getId() != null) {
	    return ResponseEntity.badRequest().header("Failure", "A new video cannot already have an ID").build();
	}
	videoRepository.save(video);
	return ResponseEntity.created(new URI("/api/videos/" + video.getId())).build();
    }

    /**
     * PUT /videos -> Updates an existing video.
     */
    @RequestMapping(value = "/videos", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Video video) throws URISyntaxException {
	log.debug("REST request to update Video : {}", video);
	if (video.getId() == null) {
	    return create(video);
	}
	videoRepository.save(video);
	return ResponseEntity.ok().build();
    }

    /**
     * GET /videos -> get all the videos.
     */
    @RequestMapping(value = "/videos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity getAll() throws URISyntaxException {
	log.debug("REST request to get all Videos : {}");
	return new ResponseEntity(videoRepository.findAll(), HttpStatus.OK);
    }

    /**
     * GET /videos/:id -> get the "id" video.
     */
    @RequestMapping(value = "/videos/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Video> get(@PathVariable Long id) {
	log.debug("REST request to get Video : {}", id);
	return Optional.ofNullable(videoRepository.findOneWithEagerRelationships(id)).map(video -> new ResponseEntity<>(video, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /videos/:id -> delete the "id" video.
     */
    @RequestMapping(value = "/videos/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
	log.debug("REST request to delete Video : {}", id);
	videoRepository.delete(id);
    }

    /**
     * GET /play/:id -> play the "id" video.
     */
    @RequestMapping(value = "/play/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity play(@PathVariable Long id) {
	log.debug("REST request to play Video : {}", id);
	try {
	    videoService.play(id);
	} catch (Exception e) {
	    log.error("Error lors de la lecture de la vidéo", e);
	    return new ResponseEntity(e.getClass().getName() + ":" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * GET /refreshImdb/:id -> reload from imdb for the "id" video.
     */
    @RequestMapping(value = "/refreshImdb/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity refreshImdb(@PathVariable Long id) {
	log.debug("REST request to refreshImdb Video : {}", id);
	try {
	    videoService.refreshImdb(id);
	} catch (Exception e) {
	    log.error("Error lors de la mise à jour de la vidéo", e);
	    return new ResponseEntity(e.getClass().getName() + ":" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * POST /guess -> guess the title from the "filename".
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/guess", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity guess(@RequestParam(value = "f", required = false) String fileName, @RequestParam(value = "k", required = false) String keyword) throws Exception {
	log.debug("REST request to guess File : {}", fileName, keyword);

	try {
	    return new ResponseEntity(videoService.guess(fileName, keyword), HttpStatus.OK);
	} catch (Exception e) {
	    log.error("Error lors de la récupération des suppositions", e);
	    return new ResponseEntity(e.getClass().getName() + ":" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    /**
     * POST /eraseFile ->erase the file "filename".
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/eraseFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity eraseFile(@RequestParam(value = "file", required = true) String fileName) throws Exception {
	log.debug("REST request to erase File : {}", fileName);

	try {
	    videoService.eraseFile(fileName);
	    return new ResponseEntity(HttpStatus.OK);
	} catch (Exception e) {
	    log.error("Error lors de la suppression du fichier", e);
	    return new ResponseEntity(e.getClass().getName() + ":" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    /**
     * POST /ignoreFile ->add the file "filename" to the ignore list.
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/ignoreFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity ignoreFile(@RequestParam(value = "file", required = true) String fileName) throws Exception {
	log.debug("REST request to ignore File : {}", fileName);

	try {
	    videoService.ignoreFile(fileName);
	    return new ResponseEntity(HttpStatus.OK);
	} catch (Exception e) {
	    log.error("Error lors de l'ajout à la liste des fichiers ignorés", e);
	    return new ResponseEntity(e.getClass().getName() + ":" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    /**
     * GET /scan -> scan the directories to find new video files
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/scan", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity scan() throws Exception {
	log.debug("REST request to scan : {}");
	try {
	    return new ResponseEntity(videoService.scan(), HttpStatus.OK);
	} catch (Exception e) {
	    log.error("Error lors du scan des répertoires", e);
	    return new ResponseEntity(e.getClass().getName() + ":" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    /**
     * POST /uploadVideo -> upload a video file with the imdbId associated.
     */
    @RequestMapping(value = "/createVideo", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<String> createVideo(@RequestParam("videoFile") String videoFile, @RequestParam(value = "subFile", required = false) String subFile, @RequestParam("imdbId") String imdbId) {
	log.debug("REST request to uploadVideo : {}", videoFile, subFile, imdbId);

	try {
	    return new ResponseEntity<String>(videoService.createVideo(videoFile, subFile, imdbId), HttpStatus.OK);
	} catch (Exception e) {
	    log.error("Error lors de la creation de la vidéo", e);
	    return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    /**
     * GET /clearAppDir -> reload dat from imdb for the "id" video.
     */
    @RequestMapping(value = "/clearAppDir", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<String> clearAppDir() {
	log.debug("REST request to clearAppDir : {}");
	try {
	    videoService.clearAppDir();
	} catch (Exception e) {
	    log.error("Error lors du nettoyage du répertoire de la vidéothèque", e);
	    return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<String>(HttpStatus.OK);
    }

    // @RequestMapping(value = "/reload", method = RequestMethod.GET, produces =
    // MediaType.APPLICATION_JSON_VALUE)
    // @Timed
    // public void reload() {
    // ReloadUtil r = new ReloadUtil(videoRepository, fileRepository,
    // typeRepository);
    // r.go();
    // }
}
