package fr.mmtech.service;

import java.io.IOException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mmtech.domain.Video;
import fr.mmtech.repository.ConfigFieldRepository;
import fr.mmtech.repository.VideoRepository;

@Service
@Transactional
public class VideoService {

	@Inject
    private VideoRepository videoRepository;
	
	@Inject
	private ConfigFieldRepository configRepository;
	
    private final Logger log = LoggerFactory.getLogger(VideoService.class);

	public void play(Long id) {
		Video video = videoRepository.findOneWithEagerRelationships(id);
		
		Runtime runtime = Runtime.getRuntime();
		String path = configRepository.getPath();
		System.out.println(path);
		
        String cmd = "cmd /c start \"\" \""+path+video.getVideoFile().getPath();
        log.debug("running cmd : "+cmd);
        try {
			runtime.exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
