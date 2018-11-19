package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test the REST API for {@link DataAcquisitionProject}s.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
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
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    rdcProjectRepository.deleteAll();
    javersService.deleteAll();
  }

  @Test
  public void testCreateDataAcquisitionProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isCreated());

    // read the project under the new url
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
      .andExpect(status().isOk());

    // call toString for test coverage :-)
    project.toString();
  }
  
  
  @Test
  public void testCreateDataAcquisitionProjectWithTooLongId() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setId("thisidistoolongandshouldproduceanerror");
    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().is4xxClientError()).andExpect(jsonPath("$.errors[0].message", containsString("error.data-acquisition-project.id.size")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testDeleteDataAcquisitionProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isCreated());

    // delete the project under the new url
    mockMvc.perform(delete(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testCompleteProjectionContainsId() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isCreated());

    // load the project with the projection
    mockMvc.perform(
        get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(project.getId())))
      .andExpect(jsonPath("$.version", is(0)));
  }

  @Test
  public void testUpdateProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();

    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isCreated());

    // update the project
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().is2xxSuccessful());

    // load the project with the complete projection
    mockMvc.perform(
        get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(project.getId())))
      .andExpect(jsonPath("$.version", is(1)));
  }
  
  @Test
  public void testUpdateProjectToSetHasBeenReleasedBackToFalse() throws  Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setHasBeenReleasedBefore(true);

    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isCreated());

    // update the project
    project.setHasBeenReleasedBefore(false);
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().is4xxClientError());

    // load the project with the complete projection
    mockMvc.perform(
        get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(project.getId())))
      .andExpect(jsonPath("$.hasBeenReleasedBefore", is(true)))
      .andExpect(jsonPath("$.version", is(0)));
  }

  @Test
  public void testIdIsMandatory() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setId(null);

    // create the project without id
    mockMvc.perform(
        post(API_DATA_ACQUISITION_PROJECTS_URI).content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isBadRequest())
      .andExpect(
          jsonPath("$.errors[0].message", 
              containsString("data-acquisition-project-management"
                  + ".error.data-acquisition-project.id.not-empty")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER, username = DATA_PROVIDER_USERNAME)
  public void testFindByIdLikeOrderByIdAsc_asDataProvider() throws Exception {
    Configuration shouldBeFoundConfiguration = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectConfiguration(
      Collections.singletonList("aPublisherId"),
      Collections.singletonList(DATA_PROVIDER_USERNAME)
    );

    DataAcquisitionProject shouldBeFound = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    shouldBeFound.setId("testid");
    shouldBeFound.setConfiguration(shouldBeFoundConfiguration);

    Configuration shouldNotBeFoundConfiguration = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectConfiguration(
      Collections.singletonList("bPublisherId"),
      Collections.singletonList("anotherProviderId")
    );

    DataAcquisitionProject shouldNotBeFound = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    shouldNotBeFound.setId("shouldnotbefoundid");
    shouldNotBeFound.setConfiguration(shouldNotBeFoundConfiguration);

    dataAcquisitionProjectRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/search/findByIdLikeOrderByIdAsc").param("id", "TES"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", equalTo(1)))
      .andExpect(jsonPath("$[0].id", equalTo(shouldBeFound.getId())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = PUBLISHER_USERNAME)
  public void testFindByIdLikeOrderByIdAsc_asPublisher() throws Exception {

    Configuration configurationA = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectConfiguration(
        Collections.singletonList(PUBLISHER_USERNAME),
        Collections.singletonList(PUBLISHER_USERNAME)
    );
    DataAcquisitionProject projectA = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    projectA.setId("a");
    projectA.setConfiguration(configurationA);

    Configuration configurationB = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectConfiguration(
      Collections.singletonList("completelyDifferentPublisherId"),
      Collections.singletonList("someDataProvider")
    );
    DataAcquisitionProject projectB = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    projectB.setId("b");
    projectB.setConfiguration(configurationB);

    dataAcquisitionProjectRepository.saveAll(Arrays.asList(projectA, projectB));

    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/search/findByIdLikeOrderByIdAsc"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", equalTo(2)))
      .andExpect(jsonPath("$[0].id", equalTo(projectA.getId())))
      .andExpect(jsonPath("$[1].id", equalTo(projectB.getId())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER, username = DATA_PROVIDER_USERNAME)
  public void testFindById() throws Exception {
    Configuration configuration = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectConfiguration(
      Collections.singletonList("aPublisherId"),
      Collections.singletonList(DATA_PROVIDER_USERNAME)
    );

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setConfiguration(configuration);

    dataAcquisitionProjectRepository.save(project);

    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", equalTo(project.getId())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER, username = DATA_PROVIDER_USERNAME)
  public void testFindById_404() throws Exception {
    Configuration configuration = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectConfiguration(
      Collections.singletonList("aPublisherId"),
      Collections.singletonList("anotherDataProviderId")
    );

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setConfiguration(configuration);

    dataAcquisitionProjectRepository.save(project);

    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
      .andExpect(status().isNotFound());
  }
}
