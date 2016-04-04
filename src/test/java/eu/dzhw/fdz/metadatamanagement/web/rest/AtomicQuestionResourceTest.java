/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.rest;

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

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.JsonSyntaxException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.domain.AtomicQuestion;
import eu.dzhw.fdz.metadatamanagement.domain.AtomicQuestionTypes;
import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.domain.Questionnaire;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.repository.AtomicQuestionRepository;
import eu.dzhw.fdz.metadatamanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.QuestionnaireRepository;
import eu.dzhw.fdz.metadatamanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * @author Daniel Katzberg
 *
 */
public class AtomicQuestionResourceTest extends AbstractTest {
  private static final String API_ATOMICQUESTIONS_URI = "/api/atomic_questions";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private VariableRepository variableRepository;

  @Autowired
  private QuestionnaireRepository questionnaireRepository;

  @Autowired
  private AtomicQuestionRepository atomicQuestionRepository;

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
    this.variableRepository.deleteAll();
    this.atomicQuestionRepository.deleteAll();
    this.questionnaireRepository.deleteAll();
  }

  @Test
  public void testCreateAtomicQuestion() throws Exception {

    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Variable variable = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), null);
    this.variableRepository.save(variable);

    Questionnaire questionnaire =
        UnitTestCreateDomainObjectUtils.buildQuestionnaire(project.getId());
    this.questionnaireRepository.save(questionnaire);

    AtomicQuestion atomicQuestion = UnitTestCreateDomainObjectUtils
      .buildAtomicQuestion(project.getId(), questionnaire.getId(), variable.getId());

    // Act and Assert
    // create the AtomicQuestion with the given id
    mockMvc.perform(put(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId())
      .content(TestUtil.convertObjectToJsonBytes(atomicQuestion)))
      .andExpect(status().isCreated());

    // check that auditing attributes have been set
    mockMvc.perform(get(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.createdAt", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.lastModifiedAt", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.createdBy", is("system")))
      .andExpect(jsonPath("$.lastModifiedBy", is("system")));

    // call toString for test coverage :-)
    atomicQuestion.toString();
  }
  
  @Test
  public void deleteDataSet() throws JsonSyntaxException, IOException, Exception {

    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Variable variable = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), null);
    this.variableRepository.save(variable);

    Questionnaire questionnaire =
        UnitTestCreateDomainObjectUtils.buildQuestionnaire(project.getId());
    this.questionnaireRepository.save(questionnaire);

    AtomicQuestion atomicQuestion = UnitTestCreateDomainObjectUtils
      .buildAtomicQuestion(project.getId(), questionnaire.getId(), variable.getId());

    // create the AtomicQuestion with the given id
    mockMvc.perform(put(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId())
      .content(TestUtil.convertObjectToJsonBytes(atomicQuestion)))
      .andExpect(status().isCreated());

    // delete the AtomicQuestion
    mockMvc.perform(delete(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the AtomicQuestion has been deleted
    mockMvc.perform(get(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId()))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateAtomicQuestion() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Variable variable = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), null);
    this.variableRepository.save(variable);

    Questionnaire questionnaire =
        UnitTestCreateDomainObjectUtils.buildQuestionnaire(project.getId());
    this.questionnaireRepository.save(questionnaire);

    AtomicQuestion atomicQuestion = UnitTestCreateDomainObjectUtils
      .buildAtomicQuestion(project.getId(), questionnaire.getId(), variable.getId());

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId())
      .content(TestUtil.convertObjectToJsonBytes(atomicQuestion)))
      .andExpect(status().isCreated());

    atomicQuestion.getFootnote()
      .setDe("Angepasst.");
    atomicQuestion.setType(AtomicQuestionTypes.SINGLE_CHOICE);

    // update the AtomicQuestion with the given id
    mockMvc.perform(put(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId())
      .content(TestUtil.convertObjectToJsonBytes(atomicQuestion)))
      .andExpect(status().is2xxSuccessful());

    // read the updated AtomicQuestion and check the version
    mockMvc
      .perform(get(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(atomicQuestion.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.footnote.de", is("Angepasst.")))
      .andExpect(jsonPath("$.type.de", is("single-choice")));
  }
  
  @Test
  public void testUpdateWithWrongTypeAtomicQuestion() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Variable variable = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), null);
    this.variableRepository.save(variable);

    Questionnaire questionnaire =
        UnitTestCreateDomainObjectUtils.buildQuestionnaire(project.getId());
    this.questionnaireRepository.save(questionnaire);

    AtomicQuestion atomicQuestion = UnitTestCreateDomainObjectUtils
      .buildAtomicQuestion(project.getId(), questionnaire.getId(), variable.getId());

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId())
      .content(TestUtil.convertObjectToJsonBytes(atomicQuestion)))
      .andExpect(status().isCreated());

    //set inconsistent type
    atomicQuestion.setType(new I18nStringBuilder().withDe(AtomicQuestionTypes.OPEN.getDe())
        .withEn("Bad Value")
        .build());

    // update the AtomicQuestion with the given id
    mockMvc.perform(put(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId())
      .content(TestUtil.convertObjectToJsonBytes(atomicQuestion)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void testCreateAtomicQuestionWithoutType() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Variable variable = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), null);
    this.variableRepository.save(variable);

    Questionnaire questionnaire =
        UnitTestCreateDomainObjectUtils.buildQuestionnaire(project.getId());
    this.questionnaireRepository.save(questionnaire);

    AtomicQuestion atomicQuestion = UnitTestCreateDomainObjectUtils
      .buildAtomicQuestion(project.getId(), questionnaire.getId(), variable.getId());
    atomicQuestion.setType(null);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId())
      .content(TestUtil.convertObjectToJsonBytes(atomicQuestion)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testDeletingProjectDeletesAtomicQuestion() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Variable variable = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), null);
    // create the variable with the given id
    mockMvc.perform(put("/api/variables/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    Questionnaire questionnaire =
        UnitTestCreateDomainObjectUtils.buildQuestionnaire(project.getId());
    this.questionnaireRepository.save(questionnaire);

    AtomicQuestion atomicQuestion = UnitTestCreateDomainObjectUtils
      .buildAtomicQuestion(project.getId(), questionnaire.getId(), variable.getId());

    // Act and Assert
    // create the AtomicQuestion with the given id
    mockMvc.perform(put(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId())
      .content(TestUtil.convertObjectToJsonBytes(atomicQuestion)))
      .andExpect(status().isCreated());

    mockMvc.perform(delete("/api/data_acquisition_projects/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the AtomicQuestion has been deleted
    mockMvc.perform(get(API_ATOMICQUESTIONS_URI + "/" + atomicQuestion.getId()))
      .andExpect(status().isNotFound());

    // check that there are no variable search documents anymore
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0.0));
  }
}
