package fr.mmtech.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.mmtech.domain.VideoType;
import fr.mmtech.repository.VideoTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VideoType.
 */
@RestController
@RequestMapping("/api")
public class VideoTypeResource {

    private final Logger log = LoggerFactory.getLogger(VideoTypeResource.class);

    @Inject
    private VideoTypeRepository videoTypeRepository;

    /**
     * POST  /videoTypes -> Create a new videoType.
     */
    @RequestMapping(value = "/videoTypes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody VideoType videoType) throws URISyntaxException {
        log.debug("REST request to save VideoType : {}", videoType);
        if (videoType.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new videoType cannot already have an ID").build();
        }
        videoTypeRepository.save(videoType);
        return ResponseEntity.created(new URI("/api/videoTypes/" + videoType.getId())).build();
    }

    /**
     * PUT  /videoTypes -> Updates an existing videoType.
     */
    @RequestMapping(value = "/videoTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody VideoType videoType) throws URISyntaxException {
        log.debug("REST request to update VideoType : {}", videoType);
        if (videoType.getId() == null) {
            return create(videoType);
        }
        videoTypeRepository.save(videoType);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /videoTypes -> get all the videoTypes.
     */
    @RequestMapping(value = "/videoTypes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<VideoType> getAll() {
        log.debug("REST request to get all VideoTypes");
        return videoTypeRepository.findAll();
    }

    /**
     * GET  /videoTypes/:id -> get the "id" videoType.
     */
    @RequestMapping(value = "/videoTypes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VideoType> get(@PathVariable Long id) {
        log.debug("REST request to get VideoType : {}", id);
        return Optional.ofNullable(videoTypeRepository.findOne(id))
            .map(videoType -> new ResponseEntity<>(
                videoType,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /videoTypes/:id -> delete the "id" videoType.
     */
    @RequestMapping(value = "/videoTypes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete VideoType : {}", id);
        videoTypeRepository.delete(id);
    }
}
