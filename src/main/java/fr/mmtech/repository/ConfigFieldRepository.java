package fr.mmtech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.mmtech.domain.ConfigField;

/**
 * Spring Data JPA repository for the ConfigField entity.
 */
public interface ConfigFieldRepository extends JpaRepository<ConfigField, Long> {

    @Query("select content from ConfigField where key='VIDEOTHEQUE_PATH'")
    String getPath();

    @Query("select content from ConfigField where key='GARBAGE_PATH'")
    String getGarbagePath();
}
