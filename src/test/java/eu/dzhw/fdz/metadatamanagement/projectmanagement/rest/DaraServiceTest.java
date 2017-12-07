/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * @author dkatzberg
 *
 */
@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class DaraServiceTest extends AbstractTest{
  
  @Autowired
  private DaraService daraService;
    
  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;
  
  @Autowired
  private StudyRepository studyRepository;

  @After
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    studyRepository.deleteAll();
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
    dataAcquisitionProjectRepository.save(project);    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    this.studyRepository.save(study);
    
    //ACT
    HttpStatus isRegistered = this.daraService.registerOrUpdateProjectToDara(project.getId()); 
    
    //ASSERT
    assertThat(isRegistered, is(HttpStatus.CREATED));
  }

}
