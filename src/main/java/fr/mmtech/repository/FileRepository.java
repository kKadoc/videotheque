package fr.mmtech.repository;

import fr.mmtech.domain.File;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the File entity.
 */
public interface FileRepository extends JpaRepository<File,Long> {

}
