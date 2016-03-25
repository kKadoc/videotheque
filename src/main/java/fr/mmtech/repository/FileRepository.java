package fr.mmtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.mmtech.domain.File;

/**
 * Spring Data JPA repository for the File entity.
 */
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByPath(String path);

    List<File> findAllByType(String ignoreFlag);
}
