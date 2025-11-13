package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class DataPackageVersionsResourceTest extends AbstractTest {
  private static final String API_DATAPACKAGE_URI = "/api/data-packages";

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

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @AfterEach
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    dataPackageRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  public void testCreateDataPackageAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());

    // create the dataPackage with the given id
    mockMvc.perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataPackage)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // read the dataPackage versions
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getId() + "/versions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(1))))
      .andExpect(jsonPath("$[0].id", is(dataPackage.getId())))
      .andExpect(jsonPath("$[0].title.de", is(dataPackage.getTitle().getDe())))
      .andExpect(jsonPath("$[0].projectContributors.length()",
        is(equalTo(dataPackage.getProjectContributors().size()))))
      .andExpect(jsonPath("$[0].projectContributors[0].firstName",
        is(dataPackage.getProjectContributors().get(0).getFirstName())));
  }

  @Test
  public void testEditDataPackageAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    String firstTitle = dataPackage.getTitle().getDe();

    // create the dataPackage with the given id
    mockMvc.perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataPackage)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    dataPackage.setVersion(0L);
    // update the dataPackage with the given id
    dataPackage.setTitle(new I18nString("hurzDe2", "hurzEn2"));
    mockMvc.perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataPackage)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    dataPackage.setVersion(1L);
    // update the dataPackage again with the given id
    dataPackage.setTitle(new I18nString("hurzDe3", "hurzEn3"));
    mockMvc.perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataPackage)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // read the dataPackage versions
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getId() + "/versions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(3))))
      .andExpect(jsonPath("$[0].id", is(dataPackage.getId())))
      .andExpect(jsonPath("$[0].title.de", is("hurzDe3")))
      .andExpect(jsonPath("$[0].version", is(equalTo(2))))
      .andExpect(jsonPath("$[0].projectContributors.length()",
        is(equalTo(dataPackage.getProjectContributors().size()))))
      .andExpect(jsonPath("$[0].projectContributors[0].firstName",
        is(dataPackage.getProjectContributors().get(0).getFirstName())))
      .andExpect(jsonPath("$[1].id", is(dataPackage.getId())))
      .andExpect(jsonPath("$[1].title.de", is("hurzDe2")))
      .andExpect(jsonPath("$[1].version", is(equalTo(1))))
      .andExpect(jsonPath("$[1].projectContributors.length()",
        is(equalTo(dataPackage.getProjectContributors().size()))))
      .andExpect(jsonPath("$[1].projectContributors[0].firstName",
        is(dataPackage.getProjectContributors().get(0).getFirstName())))
      .andExpect(jsonPath("$[2].id", is(dataPackage.getId())))
      .andExpect(jsonPath("$[2].version", is(equalTo(0))))
      .andExpect(jsonPath("$[2].title.de", is(firstTitle)))
      .andExpect(jsonPath("$[2].projectContributors.length()",
        is(equalTo(dataPackage.getProjectContributors().size()))))
      .andExpect(jsonPath("$[2].projectContributors[0].firstName",
        is(dataPackage.getProjectContributors().get(0).getFirstName())));

    // read the first two dataPackage versions
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getId() + "/versions?skip=1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(2))))
      .andExpect(jsonPath("$[0].id", is(dataPackage.getId())))
      .andExpect(jsonPath("$[0].title.de", is("hurzDe2")))
      .andExpect(jsonPath("$[0].version", is(equalTo(1))))
      .andExpect(jsonPath("$[0].projectContributors.length()",
        is(equalTo(dataPackage.getProjectContributors().size()))))
      .andExpect(jsonPath("$[0].projectContributors[0].firstName",
        is(dataPackage.getProjectContributors().get(0).getFirstName())))
      .andExpect(jsonPath("$[1].id", is(dataPackage.getId())))
      .andExpect(jsonPath("$[1].version", is(equalTo(0))))
      .andExpect(jsonPath("$[1].title.de", is(firstTitle)))
      .andExpect(jsonPath("$[1].projectContributors.length()",
        is(equalTo(dataPackage.getProjectContributors().size()))))
      .andExpect(jsonPath("$[1].projectContributors[0].firstName",
        is(dataPackage.getProjectContributors().get(0).getFirstName())));
  }

  @Test
  public void testDataPackageVersionsNotFound() throws IOException, Exception {
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/spaß/versions")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(0)));
  }
}
