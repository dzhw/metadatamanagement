/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;

/**
 * @author Daniel Katzberg
 *
 */
@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DaraServiceTest extends AbstractTest {

  @Autowired
  private DaraService daraService;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private DataPackageRepository dataPackageRepository;

  @Autowired
  private AnalysisPackageRepository analysisPackageRepository;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private JaversService javersService;

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    dataPackageRepository.deleteAll();
    analysisPackageRepository.deleteAll();
    surveyRepository.deleteAll();
    javersService.deleteAll();
  }

  @Test
  public void testHealthCheck() throws Exception {

    // ASSERT
    RestTemplate restTemplate = this.daraService.getRestTemplate();
    MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    mockServer.expect(requestTo(this.daraService.getApiEndpoint() + DaraService.IS_ALiVE_ENDPOINT))
        .andRespond(withSuccess());

    // ACT
    boolean health = this.daraService.isDaraHealthy();

    // ASSERT
    assertThat(health, is(true));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testReleaseDataPackage() throws Exception {

    // ASSERT
    RestTemplate restTemplate = this.daraService.getRestTemplate();
    MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    mockServer.expect(requestTo(this.daraService.getApiEndpoint()
        + DaraService.REGISTRATION_ENDPOINT + "?registration=true"))
        .andRespond(withStatus(HttpStatus.CREATED));
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    Release release = UnitTestCreateDomainObjectUtils.buildRelease();
    project.setRelease(release);
    dataAcquisitionProjectRepository.save(project);
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    this.dataPackageRepository.save(dataPackage);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);

    mockMvc.perform(post("/api/data-acquisition-projects/" + project.getId() + "/release")
        .content(TestUtil.convertObjectToJsonBytes(project))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testReleaseAnalysisPackage() throws Exception {

    // ASSERT
    RestTemplate restTemplate = this.daraService.getRestTemplate();
    MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    mockServer.expect(requestTo(this.daraService.getApiEndpoint()
        + DaraService.REGISTRATION_ENDPOINT + "?registration=true"))
        .andRespond(withStatus(HttpStatus.CREATED));
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    Release release = UnitTestCreateDomainObjectUtils.buildRelease();
    project.setRelease(release);
    dataAcquisitionProjectRepository.save(project);
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());
    this.analysisPackageRepository.save(analysisPackage);

    mockMvc.perform(post("/api/data-acquisition-projects/" + project.getId() + "/release")
        .content(TestUtil.convertObjectToJsonBytes(project))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testReleaseShadowThrowsError() throws Exception {
    // fake a shadow project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    Release release = UnitTestCreateDomainObjectUtils.buildRelease();
    project.setRelease(release);
    project.setId(project.getId() + "-1.0.0");
    dataAcquisitionProjectRepository.save(project);

    // assert that the shadow cannot be send to dara
    mockMvc
        .perform(post("/api/data-acquisition-projects/" + project.getMasterId() + "/release")
            .content(TestUtil.convertObjectToJsonBytes(project))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            is("project-management.error.shadow-copy-release-to-dara-not-allowed")));
  }
}
