package fr.mmtech.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.mmtech.domain.File;
import fr.mmtech.repository.FileRepository;
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
 * REST controller for managing File.
 */
@RestController
@RequestMapping("/api")
public class FileResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    @Inject
    private FileRepository fileRepository;

    /**
     * POST  /files -> Create a new file.
     */
    @RequestMapping(value = "/files",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody File file) throws URISyntaxException {
        log.debug("REST request to save File : {}", file);
        if (file.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new file cannot already have an ID").build();
        }
        fileRepository.save(file);
        return ResponseEntity.created(new URI("/api/files/" + file.getId())).build();
    }

    /**
     * PUT  /files -> Updates an existing file.
     */
    @RequestMapping(value = "/files",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody File file) throws URISyntaxException {
        log.debug("REST request to update File : {}", file);
        if (file.getId() == null) {
            return create(file);
        }
        fileRepository.save(file);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /files -> get all the files.
     */
    @RequestMapping(value = "/files",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<File> getAll() {
        log.debug("REST request to get all Files");
        return fileRepository.findAll();
    }

    /**
     * GET  /files/:id -> get the "id" file.
     */
    @RequestMapping(value = "/files/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<File> get(@PathVariable Long id) {
        log.debug("REST request to get File : {}", id);
        return Optional.ofNullable(fileRepository.findOne(id))
            .map(file -> new ResponseEntity<>(
                file,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /files/:id -> delete the "id" file.
     */
    @RequestMapping(value = "/files/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete File : {}", id);
        fileRepository.delete(id);
    }
}
