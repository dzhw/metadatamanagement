package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;

public class DeleteAllDataPackagesResourceControllerTest extends AbstractTest {

  private static final String API_DELETE_ALL_DATAPACKAGES_URI = "/api/data-acquisition-projects";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private MockMvc mockMvc;

  @Autowired
  private DataPackageRepository dataPackageRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private JaversService javersService;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    dataPackageRepository.deleteAll();
    javersService.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllDataPackagesOfShadowCopyProject() throws Exception {
    String masterProjectId = "issue1991";
    String shadowProjectId = masterProjectId + "-1.0.0";
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(masterProjectId);
    dataPackage.setId(dataPackage.getId() + "-1.0.0");
    dataPackage.setDataAcquisitionProjectId(shadowProjectId);
    dataPackageRepository.save(dataPackage);

    mockMvc
        .perform(delete(API_DELETE_ALL_DATAPACKAGES_URI + "/" + shadowProjectId + "/data-packages"))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-delete-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllDataPackagesOfProject() throws Exception {
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage("projectId");
    dataPackageRepository.save(dataPackage);

    // assert that it is available
    mockMvc.perform(get("/api/data-packages/" + dataPackage.getId())).andExpect(status().isOk());

    // delete all data packages of the project
    mockMvc
        .perform(delete(API_DELETE_ALL_DATAPACKAGES_URI + "/"
            + dataPackage.getDataAcquisitionProjectId() + "/data-packages"))
        .andExpect(status().isNoContent());

    // assert that it is gone
    mockMvc.perform(get("/api/data-packages/" + dataPackage.getId()))
        .andExpect(status().isNotFound());
  }
}
