/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImageType;
import eu.dzhw.fdz.metadatamanagement.common.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionTypes;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;


public class QuestionResourceTest extends AbstractTest {
  private static final String API_QUESTIONS_URI = "/api/questions";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private QuestionRepository questionRepository;
  
  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private MockMvc mockMvc;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
    elasticsearchAdminService.recreateAllIndices();
  }

  @After
  public void cleanUp() {
    this.dataAcquisitionProjectRepository.deleteAll();
    this.questionRepository.deleteAll();
    this.surveyRepository.deleteAll();
    this.elasticsearchUpdateQueueService.clearQueue();
  }

  @Test
  public void testCreateQuestion() throws Exception {

    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
      .buildQuestion(project.getId(), 123, "instrument-Id", "SurveyId");
    question.setImageType(ImageType.PNG);
    // Act and Assert
    // create the Question with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)))
      .andExpect(status().isCreated());

    elasticsearchUpdateQueueService.processQueue();

    // check that there is one question documents
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1.0));

    // check that auditing attributes have been set
    mockMvc.perform(get(API_QUESTIONS_URI + "/" + question.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.createdDate", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.lastModifiedDate", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.createdBy", is("system")))
      .andExpect(jsonPath("$.lastModifiedBy", is("system")));

    // call toString for test coverage :-|
    question.toString();
  }

  @Test
  public void testUpdateQuestion() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
      .buildQuestion(project.getId(), 123, "instrument-Id", "SurveyId");
    question.setImageType(ImageType.PNG);
    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)))
      .andExpect(status().isCreated());
    
      question.setQuestionText(new I18nStringBuilder().withDe("Angepasst")
          .withEn("Different Value")
          .build());
      
    // update the Question with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)))
      .andExpect(status().is2xxSuccessful());

    // read the updated Question and check the version
    mockMvc
      .perform(get(API_QUESTIONS_URI + "/" + question.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(question.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.questionText.de", is("Angepasst")));

    elasticsearchUpdateQueueService.processQueue();

    // check that there is one question documents
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1.0));
  }

  // add this test when validation is on
  @Test
  public void testUpdateWithWrongTypeQuestion() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
      .buildQuestion(project.getId(), 123, "instrument-Id", "SurveyId");

    // Act and Assert
    // create the question with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)))
      .andExpect(status().isCreated());

    // set inconsistent type
    question.setType(new I18nStringBuilder().withDe(QuestionTypes.OPEN.getDe())
      .withEn("Bad Value")
      .build());

    // update the Question with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void testUpdateQuestionWithWrongNumber() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
      .buildQuestion(project.getId(), 123, "instrument-Id", "SurveyId");

    // Act and Assert
    // create the question with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)))
      .andExpect(status().isCreated());

    // set inconsistent type
    question.setNumber("123456789.123456789123456789123456789123456789");

    // update the Question with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void testCreateQuestionWithoutType() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
        .buildQuestion(project.getId(), 123, "instrument-Id", "SurveyId");
    question.setType(null);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateQuestionWithoutImageType() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
        .buildQuestion(project.getId(), 123, "instrument-Id", "SurveyId");
    question.setImageType(null);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateQuestionWithoutNumber() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
        .buildQuestion(project.getId(), 123, "instrument-Id", "SurveyId");
    question.setNumber(null);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateQuestionWithoutInstrumentId() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
        .buildQuestion(project.getId(), 123, "instrument-Id", "SurveyId");
    question.setInstrumentId(null);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testDeletingProjectDeletesQuestion() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
        .buildQuestion(project.getId(), 123, "instrument-Id", "SurveyId");

    // Act and Assert
    // create the Question with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)))
      .andExpect(status().isCreated());

    mockMvc.perform(delete("/api/data-acquisition-projects/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the Question has been deleted
    mockMvc.perform(get(API_QUESTIONS_URI + "/" + question.getId()))
      .andExpect(status().isNotFound());

    // check that there are no question documents anymore
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0.0));
  }  
}
