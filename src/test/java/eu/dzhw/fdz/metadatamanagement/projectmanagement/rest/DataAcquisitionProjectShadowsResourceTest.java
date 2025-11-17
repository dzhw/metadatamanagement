package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataCiteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.icegreen.greenmail.store.FolderException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestUserManagementUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem.Action;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowHidingNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.ShadowCopyQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DataAcquisitionProjectShadowsResourceTest extends AbstractTest {
  private static final String API_DATA_ACQUISITION_PROJECTS_URI = "/api/data-acquisition-projects";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private ShadowCopyQueueItemService shadowCopyQueueItemService;

  @Autowired
  private ShadowCopyQueueItemRepository shadowCopyQueueItemRepository;

  @Autowired
  private JaversService javersService;

  @MockBean
  private DataCiteService dataCiteService;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
  public void cleanUp() throws FolderException {
    dataAcquisitionProjectRepository.deleteAll();
    shadowCopyQueueItemRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
    greenMail.purgeEmailFromAllMailboxes();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testGetShadowsNotFound() throws Exception {
    String projectId = createProject();
    this.mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + projectId + "/shadows"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.content.length()", is(0)));

    this.mockMvc
        .perform(
            post(API_DATA_ACQUISITION_PROJECTS_URI + "/" + projectId + "/shadows/1.0.0/hidden"))
        .andExpect(status().isNotFound());

    this.mockMvc
        .perform(
            delete(API_DATA_ACQUISITION_PROJECTS_URI + "/" + projectId + "/shadows/1.0.0/hidden"))
        .andExpect(status().isNotFound());

    this.mockMvc
        .perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + projectId + "/shadows/1.0.0/action"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testOneReleasedShadowFound() throws Exception {
    String projectId = createProject();
    releaseProject(projectId, "1.0.0");

    this.mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + projectId + "/shadows"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.content.length()", is(1)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testHideSingleReleasedShadowIsForbidden() throws Exception {
    String projectId = createProject();
    releaseProject(projectId, "1.0.0");

    this.mockMvc
        .perform(
            post(API_DATA_ACQUISITION_PROJECTS_URI + "/" + projectId + "/shadows/1.0.0/hidden"))
        .andExpect(status().isBadRequest()).andExpect(result -> assertTrue(
            result.getResolvedException() instanceof ShadowHidingNotAllowedException));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testHideAndUnhideReleasedShadow() throws Exception {
    String projectId = createProject();
    releaseProject(projectId, "1.0.0");
    unreleaseProject(projectId);
    releaseProject(projectId, "1.0.1");

    // hide version 1.0.0
    this.mockMvc
        .perform(
            post(API_DATA_ACQUISITION_PROJECTS_URI + "/" + projectId + "/shadows/1.0.0/hidden"))
        .andExpect(status().isAccepted());

    // check if hiding is in progress
    this.mockMvc
        .perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + projectId + "/shadows/1.0.0/action"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.action", is(Action.HIDE.name())));

    // speed up hiding
    shadowCopyQueueItemService.executeShadowCopyActions();
    UnitTestUserManagementUtils.login("admin", "admin");

    // ensure that dara gets updated
    verify(dataCiteService).registerOrUpdateProjectToDataCite(projectId + "-1.0.0");

    // assert that hiding has finished
    this.mockMvc
        .perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + projectId + "/shadows/1.0.0/action"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(0)));

    // unhide version 1.0.0
    this.mockMvc
        .perform(
            delete(API_DATA_ACQUISITION_PROJECTS_URI + "/" + projectId + "/shadows/1.0.0/hidden"))
        .andExpect(status().isAccepted());

    // check if unhiding is in progress
    this.mockMvc
        .perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + projectId + "/shadows/1.0.0/action"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.action", is(Action.UNHIDE.name())));

    // speed up unhiding
    shadowCopyQueueItemService.executeShadowCopyActions();
    UnitTestUserManagementUtils.login("admin", "admin");

    // ensure that dara gets updated a second time
    verify(dataCiteService, times(2)).registerOrUpdateProjectToDataCite(projectId + "-1.0.0");
  }

  private String createProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();

    this.mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());

    return project.getId();
  }

  private void releaseProject(String projectId, String version) throws Exception {
    DataAcquisitionProject project = dataAcquisitionProjectRepository.findById(projectId).get();
    project.setRelease(UnitTestCreateDomainObjectUtils.buildRelease());
    project.getRelease().setVersion(version);;
    this.mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isNoContent());
    shadowCopyQueueItemService.executeShadowCopyActions();
    // shadow copy thread authenticates itself therefore we need to login again
    UnitTestUserManagementUtils.login("admin", "admin");
  }

  private void unreleaseProject(String projectId) throws Exception {
    DataAcquisitionProject project = dataAcquisitionProjectRepository.findById(projectId).get();
    project.setRelease(null);
    this.mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isNoContent());
    shadowCopyQueueItemService.executeShadowCopyActions();
    // shadow copy thread authenticates itself therefore we need to login again
    UnitTestUserManagementUtils.login("admin", "admin");
  }
}
