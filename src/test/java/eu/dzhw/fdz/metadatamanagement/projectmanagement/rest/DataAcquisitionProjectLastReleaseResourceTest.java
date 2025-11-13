package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;

@Disabled
public class DataAcquisitionProjectLastReleaseResourceTest extends AbstractTest {
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
  private JaversService javersService;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  public void testFindReleases() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project = dataAcquisitionProjectRepository.save(project);

    // assert that there are no releases yet
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "/releases"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));

    // fake a release
    project.setRelease(UnitTestCreateDomainObjectUtils.buildRelease());
    dataAcquisitionProjectRepository.save(project);

    // fake shadow
    DataAcquisitionProject shadow = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    shadow.setRelease(UnitTestCreateDomainObjectUtils.buildRelease());
    shadow.setId(project.getId() + "-" + project.getRelease().getVersion());
    dataAcquisitionProjectRepository.save(shadow);

    // assert that there is one release
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "/releases"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));
  }

  @Test
  public void testReleasesNotFound() throws IOException, Exception {
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/spaß/releases"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }
}
