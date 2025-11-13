package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class AnalysisPackageVersionsResourceTest extends AbstractTest {
  private static final String API_ANALYSISPACKAGE_URI = "/api/analysis-packages";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private AnalysisPackageRepository analysisPackageRepository;

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
    analysisPackageRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  public void testCreateAnalysisPackageAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());

    // create the analysis package with the given id
    mockMvc.perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // read the analysis package versions
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId() + "/versions"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(equalTo(1))))
        .andExpect(jsonPath("$[0].id", is(analysisPackage.getId())))
        .andExpect(jsonPath("$[0].title.de", is(analysisPackage.getTitle().getDe())))
        .andExpect(
            jsonPath("$[0].authors.length()", is(equalTo(analysisPackage.getAuthors().size()))))
        .andExpect(jsonPath("$[0].authors[0].firstName",
            is(analysisPackage.getAuthors().get(0).getFirstName())));
  }

  @Test
  public void testEditAnalysisPackageAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());

    String firstTitle = analysisPackage.getTitle().getDe();

    // create the analysis package with the given id
    mockMvc.perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    analysisPackage.setVersion(0L);
    // update the analysis package with the given id
    analysisPackage.setTitle(new I18nString("hurzDe2", "hurzEn2"));
    mockMvc.perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

    analysisPackage.setVersion(1L);
    // update the analysis package again with the given id
    analysisPackage.setTitle(new I18nString("hurzDe3", "hurzEn3"));
    mockMvc.perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

    // read the dataPackage versions
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId() + "/versions"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(equalTo(3))))
        .andExpect(jsonPath("$[0].id", is(analysisPackage.getId())))
        .andExpect(jsonPath("$[0].title.de", is("hurzDe3")))
        .andExpect(jsonPath("$[0].version", is(equalTo(2))))
        .andExpect(jsonPath("$[0].authors.length()",
            is(equalTo(analysisPackage.getAuthors().size()))))
        .andExpect(jsonPath("$[0].authors[0].firstName",
            is(analysisPackage.getAuthors().get(0).getFirstName())))
        .andExpect(jsonPath("$[1].id", is(analysisPackage.getId())))
        .andExpect(jsonPath("$[1].title.de", is("hurzDe2")))
        .andExpect(jsonPath("$[1].version", is(equalTo(1))))
        .andExpect(jsonPath("$[1].authors.length()",
            is(equalTo(analysisPackage.getAuthors().size()))))
        .andExpect(jsonPath("$[1].authors[0].firstName",
            is(analysisPackage.getAuthors().get(0).getFirstName())))
        .andExpect(jsonPath("$[2].id", is(analysisPackage.getId())))
        .andExpect(jsonPath("$[2].version", is(equalTo(0))))
        .andExpect(jsonPath("$[2].title.de", is(firstTitle)))
        .andExpect(jsonPath("$[2].authors.length()",
            is(equalTo(analysisPackage.getAuthors().size()))))
        .andExpect(jsonPath("$[2].authors[0].firstName",
            is(analysisPackage.getAuthors().get(0).getFirstName())));

    // read the first two analysis package versions
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId() + "/versions?skip=1"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(equalTo(2))))
        .andExpect(jsonPath("$[0].id", is(analysisPackage.getId())))
        .andExpect(jsonPath("$[0].title.de", is("hurzDe2")))
        .andExpect(jsonPath("$[0].version", is(equalTo(1))))
        .andExpect(jsonPath("$[0].authors.length()",
            is(equalTo(analysisPackage.getAuthors().size()))))
        .andExpect(jsonPath("$[0].authors[0].firstName",
            is(analysisPackage.getAuthors().get(0).getFirstName())))
        .andExpect(jsonPath("$[1].id", is(analysisPackage.getId())))
        .andExpect(jsonPath("$[1].version", is(equalTo(0))))
        .andExpect(jsonPath("$[1].title.de", is(firstTitle)))
        .andExpect(jsonPath("$[1].authors.length()",
            is(equalTo(analysisPackage.getAuthors().size()))))
        .andExpect(jsonPath("$[1].authors[0].firstName",
            is(analysisPackage.getAuthors().get(0).getFirstName())));
  }

  @Test
  public void testAnalysisPackageVersionsNotFound() throws IOException, Exception {
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/spaß/versions")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(0)));
  }
}
