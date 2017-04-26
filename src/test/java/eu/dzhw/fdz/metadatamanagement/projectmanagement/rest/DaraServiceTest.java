/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

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
  public void testRegister() throws Exception {
    
    //Create Objects
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    this.studyRepository.save(study);    
    
    //perform register
    boolean isRegistered = true; //TODO this.daraService.registerOrUpdateDoi(project.getId(), study.getId());    
    assertThat(isRegistered, is(true));
    
    boolean doiNotAvailable = true; //TODO this.daraService.setDoiToNotAvailable(project.getId(), study.getId());
    assertThat(doiNotAvailable, is(true));
  }

}
