package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

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

import com.icegreen.greenmail.store.FolderException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.rest.filter.LegacyUrlsFilter;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestUserManagementUtils;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.ShadowCopyQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
public class DataPackagePublicListResourceControllerTest extends AbstractTest {
  private static final String API_DATAPACKAGE_URI = "/api/data-packages";

  private static final String API_LEGACY_URI = "/api/studies";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private DataPackageRepository dataPackageRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private JaversService javersService;

  @Autowired
  private LegacyUrlsFilter legacyUrlsFilter;

  @Autowired
  private ShadowCopyQueueItemService shadowCopyQueueItemService;

  @Autowired
  private ShadowCopyQueueItemRepository shadowCopyQueueItemRepository;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(legacyUrlsFilter).build();
    elasticsearchAdminService.recreateAllIndices();
  }

  @AfterEach
  public void cleanUp() throws FolderException {
    dataAcquisitionProjectRepository.deleteAll();
    shadowCopyQueueItemRepository.deleteAll();
    dataPackageRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
    greenMail.purgeEmailFromAllMailboxes();
  }

  @Test
  public void testEmptyList() throws Exception {
    // get the empty list
    mockMvc.perform(get(API_DATAPACKAGE_URI)).andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(0)));

    // get the empty list of pinned data packages
    mockMvc.perform(get(API_DATAPACKAGE_URI + "?pinned=true")).andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(0)));

    // get the empty list from the LEGACY URI
    mockMvc.perform(get(API_LEGACY_URI)).andExpect(status().isOk())
        .andExpect(forwardedUrl(API_DATAPACKAGE_URI));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUnreleasedNotPubliclyVisible() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());

    // create the dataPackage with the given id
    mockMvc.perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(dataPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // ensure list contains no items cause not yet released
    UnitTestUserManagementUtils.logout();
    mockMvc.perform(get(API_DATAPACKAGE_URI)).andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(0)));
    // ensure that there are still no pinned data packages
    mockMvc.perform(get(API_DATAPACKAGE_URI + "?pinned=true")).andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testReleasedDataPackageIsPubliclyVisible() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());

    // create the dataPackage with the given id
    mockMvc.perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(dataPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // release the project
    project.setRelease(UnitTestCreateDomainObjectUtils.buildRelease());
    mockMvc.perform(put("/api/data-acquisition-projects" + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isNoContent());
    shadowCopyQueueItemService.executeShadowCopyActions();

    UnitTestUserManagementUtils.logout();
    // ensure that there are still no pinned data packages
    mockMvc.perform(get(API_DATAPACKAGE_URI + "?pinned=true")).andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(0)));
    // ensure list contains one item after release
    mockMvc.perform(get(API_DATAPACKAGE_URI)).andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(1)))
        .andExpect(jsonPath("$.content[0].masterId", is(dataPackage.getId())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testReleasedDataPackageIsPinned() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());

    // create the dataPackage with the given id
    mockMvc.perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(dataPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // release the project
    project.setRelease(UnitTestCreateDomainObjectUtils.buildRelease());
    project.getRelease().setPinToStartPage(true);
    mockMvc.perform(put("/api/data-acquisition-projects" + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isNoContent());
    shadowCopyQueueItemService.executeShadowCopyActions();

    UnitTestUserManagementUtils.logout();
    // ensure that there is one pinned data package
    mockMvc.perform(get(API_DATAPACKAGE_URI + "?pinned=true")).andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(1)))
        .andExpect(jsonPath("$.content[0].masterId", is(dataPackage.getId())));

    // now release a second data package and pin it
    UnitTestUserManagementUtils.login("admin", "admin");
    project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject("testproject2");
    dataAcquisitionProjectRepository.save(project);

    dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());

    // create the dataPackage with the given id
    mockMvc.perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(dataPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // release the project
    project.setRelease(UnitTestCreateDomainObjectUtils.buildRelease());
    project.getRelease().setPinToStartPage(true);
    mockMvc.perform(put("/api/data-acquisition-projects" + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isNoContent());
    shadowCopyQueueItemService.executeShadowCopyActions();

    UnitTestUserManagementUtils.logout();
    // ensure that there are two pinned data packages and the first one is the newest
    mockMvc.perform(get(API_DATAPACKAGE_URI + "?pinned=true&size=2")).andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(2)))
        .andExpect(jsonPath("$.content[0].masterId", is(dataPackage.getId())));
  }
}
