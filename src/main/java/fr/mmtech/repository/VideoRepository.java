package fr.mmtech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.mmtech.domain.Video;

/**
 * Spring Data JPA repository for the Video entity.
 */
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("select video from Video video left join fetch video.videoTypes where video.id =:id")
    Video findOneWithEagerRelationships(@Param("id") Long id);

    Video findOneByImdbId(String imdbId);
}
