package fr.mmtech.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.mmtech.domain.VideoType;

/**
 * Spring Data JPA repository for the VideoType entity.
 */
public interface VideoTypeRepository extends JpaRepository<VideoType, Long> {

    Optional<VideoType> findOneByName(String name);
}
