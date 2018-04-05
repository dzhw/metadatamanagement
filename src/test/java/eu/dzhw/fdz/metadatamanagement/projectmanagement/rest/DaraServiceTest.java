/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * @author Daniel Katzberg
 *
 */
@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class DaraServiceTest extends AbstractTest {

  @Autowired
  private DaraService daraService;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private JaversService javersService;

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    studyRepository.deleteAll();
    javersService.deleteAll();
  }

  @Test
  public void testHealthCheck() throws Exception {

    //ASSERT
    RestTemplate restTemplate = this.daraService.getRestTemplate();
    MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    mockServer
      .expect(requestTo(this.daraService.getApiEndpoint() + DaraService.IS_ALiVE_ENDPOINT))
      .andRespond(withSuccess());

    //ACT
    boolean health = this.daraService.isDaraHealthy();

    //ASSERT
    assertThat(health, is(true));
  }

  @Test
  public void testRelease() throws Exception {

    //ASSERT
    RestTemplate restTemplate = this.daraService.getRestTemplate();
    MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    mockServer
      .expect(requestTo(this.daraService.getApiEndpoint() + DaraService.REGISTRATION_ENDPOINT + "?registration=true"))
      .andRespond(withStatus(HttpStatus.CREATED));
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    Release release = UnitTestCreateDomainObjectUtils.buildRelease();
    project.setRelease(release);
    dataAcquisitionProjectRepository.save(project);
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    this.studyRepository.save(study);

    mockMvc.perform(post("/api/data-acquisition-projects/" + project.getId() + "/release")
        .content(TestUtil.convertObjectToJsonBytes(project))
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isCreated());
  }
}
