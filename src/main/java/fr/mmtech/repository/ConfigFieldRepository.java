package fr.mmtech.repository;

import fr.mmtech.domain.ConfigField;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ConfigField entity.
 */
public interface ConfigFieldRepository extends JpaRepository<ConfigField,Long> {

	@Query("select content from ConfigField where key='VIDEOTHEQUE_PATH'")
    String getPath();
}
