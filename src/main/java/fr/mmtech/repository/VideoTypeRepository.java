package fr.mmtech.repository;

import fr.mmtech.domain.VideoType;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the VideoType entity.
 */
public interface VideoTypeRepository extends JpaRepository<VideoType,Long> {

}
