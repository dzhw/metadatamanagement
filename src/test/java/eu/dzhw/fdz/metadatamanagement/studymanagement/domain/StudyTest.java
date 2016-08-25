package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;

/**
 * 
 * @author Daniel Katzberg
 *
 */
public class StudyTest extends AbstractTest {
  private static final String API_STUDY_URI = "/api/studies";
  
  @Autowired
  private WebApplicationContext wac;
  
  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private StudyRepository studyRepository;

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
  }
  
  @Test
  public void testCreateStudy() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    
    // create the project with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().isCreated());

    // read the project under the new url
    mockMvc.perform(get(API_STUDY_URI + "/" + study.getId()))
      .andExpect(status().isOk());
  }

}
