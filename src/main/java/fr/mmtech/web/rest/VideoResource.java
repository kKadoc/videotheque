package fr.mmtech.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import fr.mmtech.domain.Video;
import fr.mmtech.repository.VideoRepository;
import fr.mmtech.service.VideoService;
import fr.mmtech.web.rest.dto.GuessDTO;
import fr.mmtech.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Video.
 */
@RestController
@RequestMapping("/api")
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
    public ResponseEntity<List<Video>> getAll(@RequestParam(value = "page", required = false) Integer offset, @RequestParam(value = "per_page", required = false) Integer limit)
	    throws URISyntaxException {
	Page<Video> page = videoRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
	HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/videos", offset, limit);
	return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
    public void play(@PathVariable Long id) {
	log.debug("REST request to play Video : {}", id);
	videoService.play(id);
    }

    /**
     * GET /guess/:filename -> guess the title from the "filename".
     */
    @RequestMapping(value = "/guess", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GuessDTO> guess(@RequestBody String fileName) {
	log.debug("REST request to guess File : {}", fileName);

	return videoService.guess(fileName, null);
    }

    /**
     * POST /uploadVideo -> upload a video file with the imdbId associated.
     */
    @RequestMapping(value = "/uploadVideo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String uploadVideo(@RequestParam("videoFile") MultipartFile videoFile, @RequestParam(value = "subFile", required = false) MultipartFile subFile, @RequestParam("imdbId") String imdbId) {
	log.debug("REST request to uploadVideo : {}", videoFile, subFile, imdbId);

	try {
	    return videoService.createVideo(videoFile, subFile, imdbId);
	} catch (Exception e) {
	    log.error("Error lors de la creation de la vidéo", e);
	    return e.getMessage();
	}
    }
}
