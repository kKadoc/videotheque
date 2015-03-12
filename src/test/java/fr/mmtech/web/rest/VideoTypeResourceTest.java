package fr.mmtech.web.rest;

import fr.mmtech.Application;
import fr.mmtech.domain.VideoType;
import fr.mmtech.repository.VideoTypeRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VideoTypeResource REST controller.
 *
 * @see VideoTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VideoTypeResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private VideoTypeRepository videoTypeRepository;

    private MockMvc restVideoTypeMockMvc;

    private VideoType videoType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VideoTypeResource videoTypeResource = new VideoTypeResource();
        ReflectionTestUtils.setField(videoTypeResource, "videoTypeRepository", videoTypeRepository);
        this.restVideoTypeMockMvc = MockMvcBuilders.standaloneSetup(videoTypeResource).build();
    }

    @Before
    public void initTest() {
        videoType = new VideoType();
        videoType.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createVideoType() throws Exception {
        // Validate the database is empty
        assertThat(videoTypeRepository.findAll()).hasSize(0);

        // Create the VideoType
        restVideoTypeMockMvc.perform(post("/api/videoTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(videoType)))
                .andExpect(status().isCreated());

        // Validate the VideoType in the database
        List<VideoType> videoTypes = videoTypeRepository.findAll();
        assertThat(videoTypes).hasSize(1);
        VideoType testVideoType = videoTypes.iterator().next();
        assertThat(testVideoType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllVideoTypes() throws Exception {
        // Initialize the database
        videoTypeRepository.saveAndFlush(videoType);

        // Get all the videoTypes
        restVideoTypeMockMvc.perform(get("/api/videoTypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(videoType.getId().intValue()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getVideoType() throws Exception {
        // Initialize the database
        videoTypeRepository.saveAndFlush(videoType);

        // Get the videoType
        restVideoTypeMockMvc.perform(get("/api/videoTypes/{id}", videoType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(videoType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVideoType() throws Exception {
        // Get the videoType
        restVideoTypeMockMvc.perform(get("/api/videoTypes/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideoType() throws Exception {
        // Initialize the database
        videoTypeRepository.saveAndFlush(videoType);

        // Update the videoType
        videoType.setName(UPDATED_NAME);
        restVideoTypeMockMvc.perform(put("/api/videoTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(videoType)))
                .andExpect(status().isOk());

        // Validate the VideoType in the database
        List<VideoType> videoTypes = videoTypeRepository.findAll();
        assertThat(videoTypes).hasSize(1);
        VideoType testVideoType = videoTypes.iterator().next();
        assertThat(testVideoType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteVideoType() throws Exception {
        // Initialize the database
        videoTypeRepository.saveAndFlush(videoType);

        // Get the videoType
        restVideoTypeMockMvc.perform(delete("/api/videoTypes/{id}", videoType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<VideoType> videoTypes = videoTypeRepository.findAll();
        assertThat(videoTypes).hasSize(0);
    }
}
