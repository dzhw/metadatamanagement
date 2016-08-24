package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;

/**
 * 
 * @author Daniel Katzberg
 *
 */
public class StudyTest { //extends AbstractTest {
  private static final String API_STUDY_URI = "/api/studies";
  
  @Autowired
  private WebApplicationContext wac;
  
  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private StudyRepository studyRepository;

  private MockMvc mockMvc;

//  @Before
//  public void setup() {
//    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
//      .build();
//  }
//
//  @After
//  public void cleanUp() {
//    dataAcquisitionProjectRepository.deleteAll();
//    studyRepository.deleteAll();
//  }
//  
//  @Test
//  public void testCreateStudy() throws IOException, Exception {
//    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
//    dataAcquisitionProjectRepository.save(project);
//    
//    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
//    
//    // create the project with the given id
//    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
//      .content(TestUtil.convertObjectToJsonBytes(study)))
//      .andExpect(status().isCreated());
//
//    // read the project under the new url
//    mockMvc.perform(get(API_STUDY_URI + "/" + project.getId()))
//      .andExpect(status().isOk());
//  }

}
