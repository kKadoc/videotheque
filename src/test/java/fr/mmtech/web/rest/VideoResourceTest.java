package fr.mmtech.web.rest;

import fr.mmtech.Application;
import fr.mmtech.domain.Video;
import fr.mmtech.repository.VideoRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VideoResource REST controller.
 *
 * @see VideoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VideoResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_IMDB_ID = "SAMPLE_TEXT";
    private static final String UPDATED_IMDB_ID = "UPDATED_TEXT";
    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";

    private static final Integer DEFAULT_YEAR = 0;
    private static final Integer UPDATED_YEAR = 1;

    private static final BigDecimal DEFAULT_RATE = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_RATE = BigDecimal.ONE;

    private static final Integer DEFAULT_DURATION = 0;
    private static final Integer UPDATED_DURATION = 1;

    private static final Boolean DEFAULT_FAVORIS = false;
    private static final Boolean UPDATED_FAVORIS = true;

    private static final DateTime DEFAULT_ADD_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_ADD_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_ADD_DATE_STR = dateTimeFormatter.print(DEFAULT_ADD_DATE);

    @Inject
    private VideoRepository videoRepository;

    private MockMvc restVideoMockMvc;

    private Video video;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VideoResource videoResource = new VideoResource();
        ReflectionTestUtils.setField(videoResource, "videoRepository", videoRepository);
        this.restVideoMockMvc = MockMvcBuilders.standaloneSetup(videoResource).build();
    }

    @Before
    public void initTest() {
        video = new Video();
        video.setImdbId(DEFAULT_IMDB_ID);
        video.setTitle(DEFAULT_TITLE);
        video.setYear(DEFAULT_YEAR);
        video.setRate(DEFAULT_RATE);
        video.setDuration(DEFAULT_DURATION);
        video.setFavoris(DEFAULT_FAVORIS);
        video.setAddDate(DEFAULT_ADD_DATE);
    }

    @Test
    @Transactional
    public void createVideo() throws Exception {
        // Validate the database is empty
        assertThat(videoRepository.findAll()).hasSize(0);

        // Create the Video
        restVideoMockMvc.perform(post("/api/videos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(video)))
                .andExpect(status().isCreated());

        // Validate the Video in the database
        List<Video> videos = videoRepository.findAll();
        assertThat(videos).hasSize(1);
        Video testVideo = videos.iterator().next();
        assertThat(testVideo.getImdbId()).isEqualTo(DEFAULT_IMDB_ID);
        assertThat(testVideo.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVideo.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testVideo.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testVideo.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testVideo.getFavoris()).isEqualTo(DEFAULT_FAVORIS);
        assertThat(testVideo.getAddDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_ADD_DATE);
    }

    @Test
    @Transactional
    public void getAllVideos() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get all the videos
        restVideoMockMvc.perform(get("/api/videos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(video.getId().intValue()))
                .andExpect(jsonPath("$.[0].imdbId").value(DEFAULT_IMDB_ID.toString()))
                .andExpect(jsonPath("$.[0].title").value(DEFAULT_TITLE.toString()))
                .andExpect(jsonPath("$.[0].year").value(DEFAULT_YEAR))
                .andExpect(jsonPath("$.[0].rate").value(DEFAULT_RATE.intValue()))
                .andExpect(jsonPath("$.[0].duration").value(DEFAULT_DURATION))
                .andExpect(jsonPath("$.[0].favoris").value(DEFAULT_FAVORIS.booleanValue()))
                .andExpect(jsonPath("$.[0].addDate").value(DEFAULT_ADD_DATE_STR));
    }

    @Test
    @Transactional
    public void getVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get the video
        restVideoMockMvc.perform(get("/api/videos/{id}", video.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(video.getId().intValue()))
            .andExpect(jsonPath("$.imdbId").value(DEFAULT_IMDB_ID.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.intValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.favoris").value(DEFAULT_FAVORIS.booleanValue()))
            .andExpect(jsonPath("$.addDate").value(DEFAULT_ADD_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingVideo() throws Exception {
        // Get the video
        restVideoMockMvc.perform(get("/api/videos/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Update the video
        video.setImdbId(UPDATED_IMDB_ID);
        video.setTitle(UPDATED_TITLE);
        video.setYear(UPDATED_YEAR);
        video.setRate(UPDATED_RATE);
        video.setDuration(UPDATED_DURATION);
        video.setFavoris(UPDATED_FAVORIS);
        video.setAddDate(UPDATED_ADD_DATE);
        restVideoMockMvc.perform(put("/api/videos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(video)))
                .andExpect(status().isOk());

        // Validate the Video in the database
        List<Video> videos = videoRepository.findAll();
        assertThat(videos).hasSize(1);
        Video testVideo = videos.iterator().next();
        assertThat(testVideo.getImdbId()).isEqualTo(UPDATED_IMDB_ID);
        assertThat(testVideo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVideo.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testVideo.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testVideo.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testVideo.getFavoris()).isEqualTo(UPDATED_FAVORIS);
        assertThat(testVideo.getAddDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_ADD_DATE);
    }

    @Test
    @Transactional
    public void deleteVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get the video
        restVideoMockMvc.perform(delete("/api/videos/{id}", video.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Video> videos = videoRepository.findAll();
        assertThat(videos).hasSize(0);
    }
}
