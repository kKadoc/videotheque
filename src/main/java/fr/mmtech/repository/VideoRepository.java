package fr.mmtech.repository;

import fr.mmtech.domain.Video;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Video entity.
 */
public interface VideoRepository extends JpaRepository<Video,Long> {

    @Query("select video from Video video left join fetch video.videoTypes where video.id =:id")
    Video findOneWithEagerRelationships(@Param("id") Long id);

}
