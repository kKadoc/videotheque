package fr.mmtech.web.rest;

import fr.mmtech.Application;
import fr.mmtech.domain.ConfigField;
import fr.mmtech.repository.ConfigFieldRepository;

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
 * Test class for the ConfigFieldResource REST controller.
 *
 * @see ConfigFieldResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ConfigFieldResourceTest {

    private static final String DEFAULT_KEY = "SAMPLE_TEXT";
    private static final String UPDATED_KEY = "UPDATED_TEXT";
    private static final String DEFAULT_CONTENT = "SAMPLE_TEXT";
    private static final String UPDATED_CONTENT = "UPDATED_TEXT";

    @Inject
    private ConfigFieldRepository configFieldRepository;

    private MockMvc restConfigFieldMockMvc;

    private ConfigField configField;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConfigFieldResource configFieldResource = new ConfigFieldResource();
        ReflectionTestUtils.setField(configFieldResource, "configFieldRepository", configFieldRepository);
        this.restConfigFieldMockMvc = MockMvcBuilders.standaloneSetup(configFieldResource).build();
    }

    @Before
    public void initTest() {
        configField = new ConfigField();
        configField.setKey(DEFAULT_KEY);
        configField.setContent(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createConfigField() throws Exception {
        // Validate the database is empty
        assertThat(configFieldRepository.findAll()).hasSize(0);

        // Create the ConfigField
        restConfigFieldMockMvc.perform(post("/api/configFields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(configField)))
                .andExpect(status().isCreated());

        // Validate the ConfigField in the database
        List<ConfigField> configFields = configFieldRepository.findAll();
        assertThat(configFields).hasSize(1);
        ConfigField testConfigField = configFields.iterator().next();
        assertThat(testConfigField.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testConfigField.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void getAllConfigFields() throws Exception {
        // Initialize the database
        configFieldRepository.saveAndFlush(configField);

        // Get all the configFields
        restConfigFieldMockMvc.perform(get("/api/configFields"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(configField.getId().intValue()))
                .andExpect(jsonPath("$.[0].key").value(DEFAULT_KEY.toString()))
                .andExpect(jsonPath("$.[0].content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getConfigField() throws Exception {
        // Initialize the database
        configFieldRepository.saveAndFlush(configField);

        // Get the configField
        restConfigFieldMockMvc.perform(get("/api/configFields/{id}", configField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(configField.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfigField() throws Exception {
        // Get the configField
        restConfigFieldMockMvc.perform(get("/api/configFields/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigField() throws Exception {
        // Initialize the database
        configFieldRepository.saveAndFlush(configField);

        // Update the configField
        configField.setKey(UPDATED_KEY);
        configField.setContent(UPDATED_CONTENT);
        restConfigFieldMockMvc.perform(put("/api/configFields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(configField)))
                .andExpect(status().isOk());

        // Validate the ConfigField in the database
        List<ConfigField> configFields = configFieldRepository.findAll();
        assertThat(configFields).hasSize(1);
        ConfigField testConfigField = configFields.iterator().next();
        assertThat(testConfigField.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testConfigField.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void deleteConfigField() throws Exception {
        // Initialize the database
        configFieldRepository.saveAndFlush(configField);

        // Get the configField
        restConfigFieldMockMvc.perform(delete("/api/configFields/{id}", configField.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ConfigField> configFields = configFieldRepository.findAll();
        assertThat(configFields).hasSize(0);
    }
}
