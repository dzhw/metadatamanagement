package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import eu.dzhw.fdz.metadatamanagement.common.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;

/**
 * 
 * @author Daniel Katzberg
 *
 */
public class StudyResourceTest extends AbstractTest {
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
    
    // create the study with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().isCreated());

    // read the study under the new url
    mockMvc.perform(get(API_STUDY_URI + "/" + study.getId()))
      .andExpect(status().isOk());
  }
  
  @Test
  public void testCreateStudyWithTooSurveySeries() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
      study.setSurveySeries(new I18nStringBuilder()
          .withDe("Zufallsstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewgn")
          .withEn("Randomstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewgn")
          .build());
      
    // create the project with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().is4xxClientError());
  }
   
  @Test
  public void testCreateStudyWithWrongId() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    study.setId("hurz");
      
    // create the study with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().is4xxClientError());
  }
  
  @Test
  public void testUpdateStudy() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    
    // create the study with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().isCreated());
    
    study.setAuthors("AnotherAuthor");
    
    // create the study with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().is2xxSuccessful());

    // read the study under the new url
    mockMvc.perform(get(API_STUDY_URI + "/" + study.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(study.getId())))
      .andExpect(jsonPath("$.authors", is("AnotherAuthor")));
  }
  
  @Test
  public void testDeleteStudy() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    
    // create the project with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().isCreated());

    // delete the project under the new url
    mockMvc.perform(delete(API_STUDY_URI + "/" + study.getId()))
      .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_STUDY_URI + "/" + study.getId()))
      .andExpect(status().isNotFound());
  }
  
  @Test
  public void testUpdateStudyWithWrongId() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    study.setId("ThisIdIsWrong.");
    
    // Try to put into mongo db
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().is4xxClientError());
  }
  
}
