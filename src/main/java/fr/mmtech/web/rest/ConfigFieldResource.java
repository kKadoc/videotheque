package fr.mmtech.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.mmtech.domain.ConfigField;
import fr.mmtech.repository.ConfigFieldRepository;
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
 * REST controller for managing ConfigField.
 */
@RestController
@RequestMapping("/api")
public class ConfigFieldResource {

    private final Logger log = LoggerFactory.getLogger(ConfigFieldResource.class);

    @Inject
    private ConfigFieldRepository configFieldRepository;

    /**
     * POST  /configFields -> Create a new configField.
     */
    @RequestMapping(value = "/configFields",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody ConfigField configField) throws URISyntaxException {
        log.debug("REST request to save ConfigField : {}", configField);
        if (configField.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new configField cannot already have an ID").build();
        }
        configFieldRepository.save(configField);
        return ResponseEntity.created(new URI("/api/configFields/" + configField.getId())).build();
    }

    /**
     * PUT  /configFields -> Updates an existing configField.
     */
    @RequestMapping(value = "/configFields",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody ConfigField configField) throws URISyntaxException {
        log.debug("REST request to update ConfigField : {}", configField);
        if (configField.getId() == null) {
            return create(configField);
        }
        configFieldRepository.save(configField);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /configFields -> get all the configFields.
     */
    @RequestMapping(value = "/configFields",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ConfigField> getAll() {
        log.debug("REST request to get all ConfigFields");
        return configFieldRepository.findAll();
    }

    /**
     * GET  /configFields/:id -> get the "id" configField.
     */
    @RequestMapping(value = "/configFields/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ConfigField> get(@PathVariable Long id) {
        log.debug("REST request to get ConfigField : {}", id);
        return Optional.ofNullable(configFieldRepository.findOne(id))
            .map(configField -> new ResponseEntity<>(
                configField,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /configFields/:id -> delete the "id" configField.
     */
    @RequestMapping(value = "/configFields/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ConfigField : {}", id);
        configFieldRepository.delete(id);
    }
}
