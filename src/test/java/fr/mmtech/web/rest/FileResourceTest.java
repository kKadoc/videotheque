package fr.mmtech.web.rest;

import fr.mmtech.Application;
import fr.mmtech.domain.File;
import fr.mmtech.repository.FileRepository;

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
 * Test class for the FileResource REST controller.
 *
 * @see FileResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FileResourceTest {

    private static final String DEFAULT_PATH = "SAMPLE_TEXT";
    private static final String UPDATED_PATH = "UPDATED_TEXT";
    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";

    @Inject
    private FileRepository fileRepository;

    private MockMvc restFileMockMvc;

    private File file;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FileResource fileResource = new FileResource();
        ReflectionTestUtils.setField(fileResource, "fileRepository", fileRepository);
        this.restFileMockMvc = MockMvcBuilders.standaloneSetup(fileResource).build();
    }

    @Before
    public void initTest() {
        file = new File();
        file.setPath(DEFAULT_PATH);
        file.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createFile() throws Exception {
        // Validate the database is empty
        assertThat(fileRepository.findAll()).hasSize(0);

        // Create the File
        restFileMockMvc.perform(post("/api/files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(file)))
                .andExpect(status().isCreated());

        // Validate the File in the database
        List<File> files = fileRepository.findAll();
        assertThat(files).hasSize(1);
        File testFile = files.iterator().next();
        assertThat(testFile.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testFile.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void getAllFiles() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get all the files
        restFileMockMvc.perform(get("/api/files"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(file.getId().intValue()))
                .andExpect(jsonPath("$.[0].path").value(DEFAULT_PATH.toString()))
                .andExpect(jsonPath("$.[0].type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get the file
        restFileMockMvc.perform(get("/api/files/{id}", file.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(file.getId().intValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFile() throws Exception {
        // Get the file
        restFileMockMvc.perform(get("/api/files/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Update the file
        file.setPath(UPDATED_PATH);
        file.setType(UPDATED_TYPE);
        restFileMockMvc.perform(put("/api/files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(file)))
                .andExpect(status().isOk());

        // Validate the File in the database
        List<File> files = fileRepository.findAll();
        assertThat(files).hasSize(1);
        File testFile = files.iterator().next();
        assertThat(testFile.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testFile.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get the file
        restFileMockMvc.perform(delete("/api/files/{id}", file.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<File> files = fileRepository.findAll();
        assertThat(files).hasSize(0);
    }
}
