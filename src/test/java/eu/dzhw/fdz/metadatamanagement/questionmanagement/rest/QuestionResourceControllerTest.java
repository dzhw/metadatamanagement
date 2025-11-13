/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionTypes;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class QuestionResourceControllerTest extends AbstractTest {
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

  @Autowired
  private JaversService javersService;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @AfterEach
  public void cleanUp() {
    this.dataAcquisitionProjectRepository.deleteAll();
    this.questionRepository.deleteAll();
    this.surveyRepository.deleteAll();
    this.elasticsearchUpdateQueueService.clearQueue();
    this.elasticsearchAdminService.recreateAllIndices();
    this.javersService.deleteAll();
  }

  @Test
  public void testCreateQuestion() throws Exception {

    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
      .buildQuestion(project.getId(), 123, "instrument-Id");
    // Act and Assert
    // create the Question with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one question documents
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));

    // check that auditing attributes have been set
    mockMvc.perform(get(API_QUESTIONS_URI + "/" + question.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.createdDate", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.lastModifiedDate", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.createdBy", is("user")))
      .andExpect(jsonPath("$.lastModifiedBy", is("user")));

    // call toString for test coverage :-|
    question.toString();
  }

  @Test
  public void testCreateQuestionWithPost() throws Exception {

    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question =
        UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 123, "instrument-Id");
    // Act and Assert
    // create the Question with the given id
    mockMvc.perform(post(API_QUESTIONS_URI).content(TestUtil.convertObjectToJsonBytes(question))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one question documents
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));

    // check that auditing attributes have been set
    mockMvc.perform(get(API_QUESTIONS_URI + "/" + question.getId())).andExpect(status().isOk())
        .andExpect(jsonPath("$.createdDate", not(isEmptyOrNullString())))
        .andExpect(jsonPath("$.lastModifiedDate", not(isEmptyOrNullString())))
        .andExpect(jsonPath("$.createdBy", is("user")))
        .andExpect(jsonPath("$.lastModifiedBy", is("user")));
  }

  @Test
  public void testDeleteQuestion() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question =
        UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 123, "instrument-Id");

    // create the Question with the given id
    mockMvc.perform(post(API_QUESTIONS_URI).content(TestUtil.convertObjectToJsonBytes(question))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // assert that the question exists
    mockMvc.perform(get(API_QUESTIONS_URI + "/" + question.getId())).andExpect(status().isOk());

    // delete the question
    mockMvc.perform(delete(API_QUESTIONS_URI + "/" + question.getId())).andExpect(status().isNoContent());

    // assert that the question does not exist anymore
    mockMvc.perform(get(API_QUESTIONS_URI + "/" + question.getId()))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateQuestion() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
      .buildQuestion(project.getId(), 123, "instrument-Id");
    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

      question.setQuestionText(I18nString.builder().de("Angepasst")
          .en("Different Value")
          .build());

    question.setVersion(0L);
    // update the Question with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful());

    // read the updated Question and check the version
    mockMvc
      .perform(get(API_QUESTIONS_URI + "/" + question.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(question.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.questionText.de", is("Angepasst")));

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one question documents
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));
  }

  // add this test when validation is on
  @Test
  public void testUpdateWithWrongTypeQuestion() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
      .buildQuestion(project.getId(), 123, "instrument-Id");

    // Act and Assert
    // create the question with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // set inconsistent type
    question.setType(I18nString.builder().de(QuestionTypes.OPEN.getDe())
      .en("Bad Value")
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
      .buildQuestion(project.getId(), 123, "instrument-Id");

    // Act and Assert
    // create the question with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)).contentType(MediaType.APPLICATION_JSON))
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
        .buildQuestion(project.getId(), 123, "instrument-Id");
    question.setType(null);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateQuestionWithoutNumber() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
        .buildQuestion(project.getId(), 123, "instrument-Id");
    question.setNumber(null);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateQuestionWithoutInstrumentId() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
        .buildQuestion(project.getId(), 123, "instrument-Id");
    question.setInstrumentId(null);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testDeletingProjectDeletesQuestion() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Question question = UnitTestCreateDomainObjectUtils
        .buildQuestion(project.getId(), 123, "instrument-Id");

    // Act and Assert
    // create the Question with the given id
    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
      .content(TestUtil.convertObjectToJsonBytes(question)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    mockMvc.perform(delete("/api/data-acquisition-projects/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the Question has been deleted
    mockMvc.perform(get(API_QUESTIONS_URI + "/" + question.getId()))
      .andExpect(status().isNotFound());

    // check that there are no question documents anymore
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0L));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopyQuestion() throws Exception {
    Question question = UnitTestCreateDomainObjectUtils
        .buildQuestion("issue1991", 123, "instrument-Id");
    question.setId(question.getId() + "-1.0.0");

    mockMvc.perform(post(API_QUESTIONS_URI)
        .content(TestUtil.convertObjectToJsonBytes(question)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUpdateShadowCopyQuestion() throws Exception {
    Question question = UnitTestCreateDomainObjectUtils
        .buildQuestion("issue1991", 123, "instrument-Id");
    question.setId(question.getId() + "-1.0.0");
    questionRepository.save(question);

    mockMvc.perform(put(API_QUESTIONS_URI + "/" + question.getId())
        .content(TestUtil.convertObjectToJsonBytes(question)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testDeleteShadowCopyQuestion() throws Exception {
    Question question = UnitTestCreateDomainObjectUtils
        .buildQuestion("issue1991", 123, "instrument-Id");
    question.setId(question.getId() + "-1.0.0");
    questionRepository.save(question);

    mockMvc.perform(delete(API_QUESTIONS_URI + "/" + question.getId()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }
}
