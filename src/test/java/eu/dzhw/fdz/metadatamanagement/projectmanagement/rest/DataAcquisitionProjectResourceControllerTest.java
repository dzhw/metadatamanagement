package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.hamcrest.core.AnyOf;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.AssigneeGroup;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * Test the REST API for {@link DataAcquisitionProject}s.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Disabled
@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DataAcquisitionProjectResourceControllerTest extends AbstractTest {
  private static final String API_DATA_ACQUISITION_PROJECTS_URI = "/api/data-acquisition-projects";

  private static final String DATA_PROVIDER_USERNAME = "dataProvider";

  private static final String PUBLISHER_USERNAME = "publisher";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository rdcProjectRepository;

  private MockMvc mockMvc;

  @Autowired
  private JaversService javersService;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
  public void cleanUp() {
    rdcProjectRepository.deleteAll();
    javersService.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateDataAcquisitionProject() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());

    // read the project under the new url
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
        .andExpect(status().isOk());

    // call toString for test coverage :-)
    project.toString();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void shouldFailToCreateProjectsForAnalysisPackagesAndDataPackages()
      throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.getConfiguration().getRequirements().setAnalysisPackagesRequired(true);

    AnyOf<String> matcher = anyOf(
        containsString("data-acquisition-project-management.error.configuration.requirements."
            + "publications-required-for-analysis-packages"),
        containsString("data-acquisition-project-management.error.configuration.requirements."
            + "either-data-packages-or-analysis-packages-required"));
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message", matcher))
        .andExpect(jsonPath("$.errors[1].message", matcher));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void shouldCreateProjectsForAnalysisPackages()
      throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.getConfiguration().getRequirements().setAnalysisPackagesRequired(true);
    project.getConfiguration().getRequirements().setDataPackagesRequired(false);
    project.getConfiguration().getRequirements().setPublicationsRequired(true);

    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());

    // read the project under the new url
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateDataAcquisitionProjectWithPost() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // create the project with the given id
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_URI).contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());

    // read the project under the new url
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
        .andExpect(status().isOk());
  }


  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateDataAcquisitionProjectWithTooLongMasterId() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // Collections.emptyList()));
    project.setId("thisidistoolongandshouldproduceanerror");
    project.setMasterId("thisidistoolongandshouldproduceanerror");
    // create the project with the given id
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().is4xxClientError()).andExpect(jsonPath("$.errors[0].message",
            containsString("error.data-acquisition-project.master-id.size")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteDataAcquisitionProject() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // delete not created project
    mockMvc.perform(delete(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());

    // delete the project under the new url
    mockMvc.perform(delete(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
  }

  @Test
  public void testCompleteProjectionContainsId() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());

    // load the project with the projection
    mockMvc
        .perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(project.getId())))
        .andExpect(jsonPath("$.version", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateProject() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();

    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());
    project.setVersion(0L);

    // update the project
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().is2xxSuccessful());

    // load the project with the complete projection
    mockMvc
        .perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(project.getId())))
        .andExpect(jsonPath("$.version", is(1)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateProjectToSetHasBeenReleasedBackToFalse() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setHasBeenReleasedBefore(true);

    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());

    // update the project
    project.setHasBeenReleasedBefore(false);
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().is4xxClientError());

    // load the project with the complete projection
    mockMvc
        .perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(project.getId())))
        .andExpect(jsonPath("$.hasBeenReleasedBefore", is(true)))
        .andExpect(jsonPath("$.version", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testIdIsMandatory() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setId(null);

    AnyOf<String> matcher = anyOf(
        containsString(
            "data-acquisition-project-management" + ".error.data-acquisition-project.id.not-empty"),
        containsString(
            "data-acquisition-project-management" + ".error.data-acquisition-project.id.pattern"));

    // create the project without id
    mockMvc
        .perform(post(API_DATA_ACQUISITION_PROJECTS_URI).contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message", matcher))
        .andExpect(jsonPath("$.errors[1].message", matcher));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER, username = DATA_PROVIDER_USERNAME)
  public void testFindByIdLikeOrderByIdAsc() throws Exception {
    Configuration shouldBeFoundConfiguration = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProjectConfiguration(Collections.singletonList("aPublisherId"),
            Collections.singletonList(DATA_PROVIDER_USERNAME));

    DataAcquisitionProject shouldBeFound =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    shouldBeFound.setId("testid");
    shouldBeFound.setMasterId("testid");
    shouldBeFound.setConfiguration(shouldBeFoundConfiguration);

    Configuration shouldNotBeFoundConfiguration = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProjectConfiguration(Collections.singletonList("bPublisherId"),
            Collections.singletonList("anotherProviderId"));

    DataAcquisitionProject shouldNotBeFound =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    shouldNotBeFound.setId("shouldnotbefoundid");
    shouldNotBeFound.setMasterId("shouldnotbefoundid");
    shouldNotBeFound.setConfiguration(shouldNotBeFoundConfiguration);

    rdcProjectRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

    mockMvc
        .perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/search/findByIdLikeOrderByIdAsc")
            .param("id", "TES"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", equalTo(2)))
        .andExpect(jsonPath("$.dataAcquisitionProjects[0].id", equalTo(shouldBeFound.getId())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = PUBLISHER_USERNAME)
  public void testFindByIdLikeOrderByIdAsc_asPublisher() throws Exception {

    Configuration configurationA = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProjectConfiguration(Collections.singletonList(PUBLISHER_USERNAME),
            Collections.singletonList(PUBLISHER_USERNAME));
    DataAcquisitionProject projectA = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    projectA.setId("a");
    projectA.setMasterId("a");
    projectA.setConfiguration(configurationA);

    Configuration configurationB =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectConfiguration(
            Collections.singletonList("completelyDifferentPublisherId"),
            Collections.singletonList("someDataProvider"));
    DataAcquisitionProject projectB = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    projectB.setId("b");
    projectB.setMasterId("b");
    projectB.setConfiguration(configurationB);

    rdcProjectRepository.saveAll(Arrays.asList(projectA, projectB));

    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/search/findByIdLikeOrderByIdAsc"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.dataAcquisitionProjects.length()", equalTo(2)))
        .andExpect(jsonPath("$.dataAcquisitionProjects[0].id", equalTo(projectA.getId())))
        .andExpect(jsonPath("$.dataAcquisitionProjects[1].id", equalTo(projectB.getId())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER, username = DATA_PROVIDER_USERNAME)
  public void testFindById() throws Exception {
    Configuration configuration = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProjectConfiguration(Collections.singletonList("aPublisherId"),
            Collections.singletonList(DATA_PROVIDER_USERNAME));

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setConfiguration(configuration);

    rdcProjectRepository.save(project);

    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", equalTo(project.getId())));
  }

  /**
   * test the user implemented parts of save project
   *
   * @throws Exception
   */
  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testSaveProjectWithEmptyPublishers() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // create the project with the given id
    rdcProjectRepository.insert(project);
    Configuration invalidConf = new Configuration();
    project.setConfiguration(invalidConf);
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER)
  public void testSaveProjectWithInvalidConfigAsProvider() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.getConfiguration().setDataProviders(Arrays.asList("dataprovider"));
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().isBadRequest());
    // create the project with the given id
    rdcProjectRepository.insert(project);
    new Configuration();
    Configuration invalidConf =
        Configuration.builder().publishers(project.getConfiguration().getPublishers())
            .dataProviders(Collections.emptyList()).build();
    project.setConfiguration(invalidConf);
    // update with invalid conf -- no dataprovider
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().isBadRequest());
    invalidConf = new Configuration();
    project.setConfiguration(invalidConf);
    // update with invalid conf -- no publisher
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = PUBLISHER_USERNAME)
  public void testUpdateRequiredObjectTypes() throws Exception {
    Configuration configuration =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectConfiguration(
            Collections.singletonList(PUBLISHER_USERNAME), Collections.emptyList());

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setConfiguration(configuration);

    rdcProjectRepository.save(project);

    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .content(TestUtil.convertObjectToJsonBytes(project))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = PUBLISHER_USERNAME)
  public void testUpdateRequiredObjectTypes_badRequest() throws Exception {
    Configuration configuration =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectConfiguration(
            Collections.singletonList("differentPublisher"), Collections.emptyList());

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setConfiguration(configuration);

    project = rdcProjectRepository.save(project);

    project.getConfiguration().getRequirements().setDataSetsRequired(true);

    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .content(TestUtil.convertObjectToJsonBytes(project))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER, username = DATA_PROVIDER_USERNAME)
  public void testUpdatDataAcquisitionProject_valid_assignee_group() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setAssigneeGroup(AssigneeGroup.DATA_PROVIDER);
    project.setLastAssigneeGroupMessage("test");

    project = rdcProjectRepository.save(project);

    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .content(TestUtil.convertObjectToJsonBytes(project))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = PUBLISHER_USERNAME)
  public void testUpdatDataAcquisitionProject_override_as_publisher() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.getConfiguration().setPublishers(Collections.singletonList(PUBLISHER_USERNAME));
    project.setAssigneeGroup(AssigneeGroup.DATA_PROVIDER);

    project = rdcProjectRepository.save(project);

    project.setAssigneeGroup(AssigneeGroup.PUBLISHER);

    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .content(TestUtil.convertObjectToJsonBytes(project))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER, username = DATA_PROVIDER_USERNAME)
  public void testUpdatDataAcquisitionProject_invalid_assignee_group() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setAssigneeGroup(AssigneeGroup.PUBLISHER);
    project.setLastAssigneeGroupMessage("test");

    project = rdcProjectRepository.save(project);

    project.setAssigneeGroup(AssigneeGroup.DATA_PROVIDER);

    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .content(TestUtil.convertObjectToJsonBytes(project))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = PUBLISHER_USERNAME)
  public void testCreateShadowCopyProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setId(project.getId() + "-1.0.0");

    mockMvc
        .perform(post(API_DATA_ACQUISITION_PROJECTS_URI)
            .content(TestUtil.convertObjectToJsonBytes(project))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = PUBLISHER_USERNAME)
  public void testUpdateShadowCopyProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setId(project.getId() + "-1.0.0");
    project = rdcProjectRepository.save(project);

    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .content(TestUtil.convertObjectToJsonBytes(project))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = PUBLISHER_USERNAME)
  public void testDeleteShadowCopyProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setId(project.getId() + "-1.0.0");
    project = rdcProjectRepository.save(project);

    mockMvc.perform(delete(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-delete-not-allowed")));
  }
}
