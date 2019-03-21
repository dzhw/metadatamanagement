package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class StudyVersionsResourceTest extends AbstractTest {
  private static final String API_STUDY_URI = "/api/studies";
  
  @Autowired
  private WebApplicationContext wac;
  
  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;
  
  @Autowired
  private StudyRepository studyRepository;
  
  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;
  
  @Autowired
  private JaversService javersService;
  
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
    elasticsearchUpdateQueueItemRepository.deleteAll();
    javersService.deleteAll();
  }
  
  @Test
  public void testCreateStudyAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    
    // create the study with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // read the study versions
    mockMvc.perform(get(API_STUDY_URI + "/" + study.getId() + "/versions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(1))))
      .andExpect(jsonPath("$[0].id", is(study.getId())))
      .andExpect(jsonPath("$[0].title.de", is(study.getTitle().getDe())))
      .andExpect(jsonPath("$[0].authors.length()", is(equalTo(study.getAuthors().size()))))
      .andExpect(jsonPath("$[0].authors[0].firstName", is(study.getAuthors().get(0).getFirstName())));
  }
  
  @Test
  public void testEditStudyAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    String firstTitle = study.getTitle().getDe();
    
    // create the study with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    study.setVersion(0L);
    // update the study with the given id
    study.setTitle(new I18nString("hurzDe2", "hurzEn2"));
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    study.setVersion(1L);
    // update the study again with the given id
    study.setTitle(new I18nString("hurzDe3", "hurzEn3"));
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // read the study versions
    mockMvc.perform(get(API_STUDY_URI + "/" + study.getId() + "/versions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(3))))
      .andExpect(jsonPath("$[0].id", is(study.getId())))
      .andExpect(jsonPath("$[0].title.de", is("hurzDe3")))
      .andExpect(jsonPath("$[0].version", is(equalTo(2))))
      .andExpect(jsonPath("$[0].authors.length()", is(equalTo(study.getAuthors().size()))))
      .andExpect(jsonPath("$[0].authors[0].firstName", is(study.getAuthors().get(0).getFirstName())))
      .andExpect(jsonPath("$[1].id", is(study.getId())))
      .andExpect(jsonPath("$[1].title.de", is("hurzDe2")))
      .andExpect(jsonPath("$[1].version", is(equalTo(1))))
      .andExpect(jsonPath("$[1].authors.length()", is(equalTo(study.getAuthors().size()))))
      .andExpect(jsonPath("$[1].authors[0].firstName", is(study.getAuthors().get(0).getFirstName())))
      .andExpect(jsonPath("$[2].id", is(study.getId())))
      .andExpect(jsonPath("$[2].version", is(equalTo(0))))
      .andExpect(jsonPath("$[2].title.de", is(firstTitle)))
      .andExpect(jsonPath("$[2].authors.length()", is(equalTo(study.getAuthors().size()))))
      .andExpect(jsonPath("$[2].authors[0].firstName", is(study.getAuthors().get(0).getFirstName())));
    
    // read the first two study versions
    mockMvc.perform(get(API_STUDY_URI + "/" + study.getId() + "/versions?skip=1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(2))))
      .andExpect(jsonPath("$[0].id", is(study.getId())))
      .andExpect(jsonPath("$[0].title.de", is("hurzDe2")))
      .andExpect(jsonPath("$[0].version", is(equalTo(1))))
      .andExpect(jsonPath("$[0].authors.length()", is(equalTo(study.getAuthors().size()))))
      .andExpect(jsonPath("$[0].authors[0].firstName", is(study.getAuthors().get(0).getFirstName())))
      .andExpect(jsonPath("$[1].id", is(study.getId())))
      .andExpect(jsonPath("$[1].version", is(equalTo(0))))
      .andExpect(jsonPath("$[1].title.de", is(firstTitle)))
      .andExpect(jsonPath("$[1].authors.length()", is(equalTo(study.getAuthors().size()))))
      .andExpect(jsonPath("$[1].authors[0].firstName", is(study.getAuthors().get(0).getFirstName())));
  }
}
