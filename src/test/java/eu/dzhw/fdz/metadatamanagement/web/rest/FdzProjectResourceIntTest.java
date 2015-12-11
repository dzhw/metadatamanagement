package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;
import eu.dzhw.fdz.metadatamanagement.service.FdzProjectService;


/**
 * Test class for the FdzProjectResource REST controller.
 *
 * @author Daniel Katzberg
 * @author JHipster
 *
 * @see FdzProjectResource
 */
public class FdzProjectResourceIntTest extends AbstractTest {

  private static final String DEFAULT_NAME = "AAAAA";
  private static final String DEFAULT_SUF_DOI = "AAAAA";
  private static final String UPDATED_SUF_DOI = "BBBBB";
  private static final String DEFAULT_CUF_DOI = "AAAAA";
  private static final String UPDATED_CUF_DOI = "BBBBB";

  @Inject
  private FdzProjectRepository fdzProjectRepository;

  @Inject
  private FdzProjectService fdzProjectService;

  @Inject
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Inject
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  private MockMvc restFdzProjectMockMvc;

  private FdzProject fdzProject;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    FdzProjectResource fdzProjectResource = new FdzProjectResource();
    ReflectionTestUtils.setField(fdzProjectResource, "fdzProjectService", fdzProjectService);
    this.restFdzProjectMockMvc = MockMvcBuilders.standaloneSetup(fdzProjectResource)
      .setCustomArgumentResolvers(pageableArgumentResolver)
      .setMessageConverters(jacksonMessageConverter)
      .build();
  }

  @Before
  public void initTest() {
    fdzProjectRepository.deleteAll();
    fdzProject = new FdzProject();
    fdzProject.setName(DEFAULT_NAME);
    fdzProject.setSufDoi(DEFAULT_SUF_DOI);
    fdzProject.setCufDoi(DEFAULT_CUF_DOI);
  }

  @Test
  public void createFdzProject() throws Exception {
    int databaseSizeBeforeCreate = fdzProjectRepository.findAll()
      .size();

    // Create the FdzProject

    restFdzProjectMockMvc
      .perform(post("/api/fdzProjects").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(fdzProject)))
      .andExpect(status().isCreated());

    // Validate the FdzProject in the database
    List<FdzProject> fdzProjects = fdzProjectRepository.findAll();
    assertThat(fdzProjects).hasSize(databaseSizeBeforeCreate + 1);
    FdzProject testFdzProject = fdzProjects.get(fdzProjects.size() - 1);
    assertThat(testFdzProject.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testFdzProject.getSufDoi()).isEqualTo(DEFAULT_SUF_DOI);
    assertThat(testFdzProject.getCufDoi()).isEqualTo(DEFAULT_CUF_DOI);
  }

  @Test
  public void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = fdzProjectRepository.findAll()
      .size();
    // set the field null
    fdzProject.setName(null);

    // Create the FdzProject, which fails.

    restFdzProjectMockMvc
      .perform(post("/api/fdzProjects").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(fdzProject)))
      .andExpect(status().isBadRequest());

    List<FdzProject> fdzProjects = fdzProjectRepository.findAll();
    assertThat(fdzProjects).hasSize(databaseSizeBeforeTest);
  }

  @Test
  public void getAllFdzProjects() throws Exception {
    // Initialize the database
    fdzProjectRepository.save(fdzProject);

    // Get all the fdzProjects
    restFdzProjectMockMvc.perform(get("/api/fdzProjects?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
      .andExpect(jsonPath("$.[*].sufDoi").value(hasItem(DEFAULT_SUF_DOI.toString())))
      .andExpect(jsonPath("$.[*].cufDoi").value(hasItem(DEFAULT_CUF_DOI.toString())));
  }

  @Test
  public void getAllFdzProjectsWithoutPageable() throws Exception {
    // Arrange
    this.fdzProjectRepository.save(fdzProject);

    // Act and Assert
    this.restFdzProjectMockMvc.perform(get("/api/fdzProjects").param("getAll", "true"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
      .andExpect(jsonPath("$.[*].sufDoi").value(hasItem(DEFAULT_SUF_DOI.toString())))
      .andExpect(jsonPath("$.[*].cufDoi").value(hasItem(DEFAULT_CUF_DOI.toString())));
  }

  @Test
  public void getFdzProject() throws Exception {
    // Initialize the database
    fdzProjectRepository.save(fdzProject);

    // Get the fdzProject
    restFdzProjectMockMvc.perform(get("/api/fdzProjects/{name}", fdzProject.getName()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
      .andExpect(jsonPath("$.sufDoi").value(DEFAULT_SUF_DOI.toString()))
      .andExpect(jsonPath("$.cufDoi").value(DEFAULT_CUF_DOI.toString()));
  }

  @Test
  public void getNonExistingFdzProject() throws Exception {
    // Get the fdzProject
    restFdzProjectMockMvc.perform(get("/api/fdzProjects/{name}", Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  public void updateFdzProject() throws Exception {
    // Initialize the database
    fdzProjectRepository.save(fdzProject);

    int databaseSizeBeforeUpdate = fdzProjectRepository.findAll()
      .size();

    // Update the fdzProject
    fdzProject.setSufDoi(UPDATED_SUF_DOI);
    fdzProject.setCufDoi(UPDATED_CUF_DOI);

    restFdzProjectMockMvc
      .perform(put("/api/fdzProjects").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(fdzProject)))
      .andExpect(status().isOk());

    // Validate the FdzProject in the database
    List<FdzProject> fdzProjects = fdzProjectRepository.findAll();
    assertThat(fdzProjects).hasSize(databaseSizeBeforeUpdate);
    FdzProject testFdzProject = fdzProjects.get(fdzProjects.size() - 1);
    assertThat(testFdzProject.getSufDoi()).isEqualTo(UPDATED_SUF_DOI);
    assertThat(testFdzProject.getCufDoi()).isEqualTo(UPDATED_CUF_DOI);
  }

  @Test
  public void deleteFdzProject() throws Exception {
    // Initialize the database
    fdzProjectRepository.save(fdzProject);

    int databaseSizeBeforeDelete = fdzProjectRepository.findAll()
      .size();

    // Get the fdzProject
    restFdzProjectMockMvc.perform(delete("/api/fdzProjects/{name}", fdzProject.getName())
      .accept(TestUtil.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk());

    // Validate the database is empty
    List<FdzProject> fdzProjects = fdzProjectRepository.findAll();
    assertThat(fdzProjects).hasSize(databaseSizeBeforeDelete - 1);
  }
}
