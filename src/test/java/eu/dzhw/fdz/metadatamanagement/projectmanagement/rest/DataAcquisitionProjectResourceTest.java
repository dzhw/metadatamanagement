package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Requirements;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test the REST API for {@link DataAcquisitionProject}s.
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DataAcquisitionProjectResourceTest extends AbstractTest {
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
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @After
  public void cleanUp() {
    rdcProjectRepository.deleteAll();
    javersService.deleteAll();
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
  public void testCreateDataAcquisitionProjectWithTooLongId() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // Collections.emptyList()));
    project.setId("thisidistoolongandshouldproduceanerror");
    // create the project with the given id
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().is4xxClientError()).andExpect(jsonPath("$.errors[0].message",
        containsString("error.data-acquisition-project.id.size")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteDataAcquisitionProject() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    //delete not created project
    mockMvc.perform(delete(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    
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
        .perform(
            get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "?projection=complete")
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

    // update the project
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().is2xxSuccessful());

    // load the project with the complete projection
    mockMvc
        .perform(
            get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "?projection=complete")
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
        .perform(
            get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "?projection=complete")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(project.getId())))
        .andExpect(jsonPath("$.hasBeenReleasedBefore", is(true)))
        .andExpect(jsonPath("$.version", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testIdIsMandatory() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setId(null);

    // create the project without id
    mockMvc
        .perform(post(API_DATA_ACQUISITION_PROJECTS_URI).contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().isBadRequest()).andExpect(
        jsonPath("$.errors[0].message", containsString("data-acquisition-project-management"
            + ".error.data-acquisition-project.id.not-empty")));
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
    shouldBeFound.setConfiguration(shouldBeFoundConfiguration);

    Configuration shouldNotBeFoundConfiguration = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProjectConfiguration(Collections.singletonList("bPublisherId"),
            Collections.singletonList("anotherProviderId"));

    DataAcquisitionProject shouldNotBeFound =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    shouldNotBeFound.setId("shouldnotbefoundid");
    shouldNotBeFound.setConfiguration(shouldNotBeFoundConfiguration);

    dataAcquisitionProjectRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

    mockMvc
        .perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/search/findByIdLikeOrderByIdAsc")
            .param("id", "TES"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", equalTo(1)))
        .andExpect(jsonPath("$[0].id", equalTo(shouldBeFound.getId())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = PUBLISHER_USERNAME)
  public void testFindByIdLikeOrderByIdAsc_asPublisher() throws Exception {

    Configuration configurationA = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProjectConfiguration(Collections.singletonList(PUBLISHER_USERNAME),
            Collections.singletonList(PUBLISHER_USERNAME));
    DataAcquisitionProject projectA = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    projectA.setId("a");
    projectA.setConfiguration(configurationA);

    Configuration configurationB =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectConfiguration(
            Collections.singletonList("completelyDifferentPublisherId"),
            Collections.singletonList("someDataProvider"));
    DataAcquisitionProject projectB = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    projectB.setId("b");
    projectB.setConfiguration(configurationB);

    dataAcquisitionProjectRepository.saveAll(Arrays.asList(projectA, projectB));

    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/search/findByIdLikeOrderByIdAsc"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", equalTo(2)))
        .andExpect(jsonPath("$[0].id", equalTo(projectA.getId())))
        .andExpect(jsonPath("$[1].id", equalTo(projectB.getId())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER, username = DATA_PROVIDER_USERNAME)
  public void testFindById() throws Exception {
    Configuration configuration = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProjectConfiguration(Collections.singletonList("aPublisherId"),
            Collections.singletonList(DATA_PROVIDER_USERNAME));

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setConfiguration(configuration);

    dataAcquisitionProjectRepository.save(project);

    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", equalTo(project.getId())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER, username = DATA_PROVIDER_USERNAME)
  public void testFindById_404() throws Exception {
    Configuration configuration = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProjectConfiguration(Collections.singletonList("aPublisherId"),
            Collections.singletonList("anotherDataProviderId"));

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setConfiguration(configuration);

    dataAcquisitionProjectRepository.save(project);

    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
        .andExpect(status().isNotFound());
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
    dataAcquisitionProjectRepository.insert(project);
    Configuration invalidConf = new Configuration();
    project.setConfiguration(invalidConf);
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("cannot clear publishers")));
  }
  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER)
  public void testSaveProjectWithInvalidConfigAsProvider() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.getConfiguration().setDataProviders(Arrays.asList("dataprovider"));
    // create the project with the given id
    dataAcquisitionProjectRepository.insert(project);
    Configuration invalidConf = new Configuration(
        project.getConfiguration().getPublishers(), Collections.emptyList(), new Requirements()
    );
    project.setConfiguration(invalidConf);
    //update with invalid conf -- no dataprovider
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string(containsString("not authorized to clear data providers")));
    invalidConf = new Configuration();
    project.setConfiguration(invalidConf);
    //update with invalid conf -- no publisher
    mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string(containsString("not authorized to remove publisher")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = PUBLISHER_USERNAME)
  public void testUpdateRequiredObjectTypes() throws Exception {
    Configuration configuration = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectConfiguration(
        Collections.singletonList(PUBLISHER_USERNAME),
        Collections.emptyList()
    );

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setConfiguration(configuration);

    dataAcquisitionProjectRepository.save(project);

    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .content(TestUtil.convertObjectToJsonBytes(project))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = PUBLISHER_USERNAME)
  public void testUpdateRequiredObjectTypes_forbidden() throws Exception {
    Configuration configuration = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectConfiguration(
        Collections.singletonList("differentPublisher"),
        Collections.emptyList()
    );

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setConfiguration(configuration);

    project = dataAcquisitionProjectRepository.save(project);

    project.getConfiguration().getRequirements().setDataSetsRequired(true);

    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .content(TestUtil.convertObjectToJsonBytes(project))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }
}
