package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Service for creating and updating questions. Used for updating questions in mongo
 * and elasticsearch.
 */
@Service
@RepositoryEventHandler
public class QuestionService {

  @Autowired
  private QuestionRepository questionRepository;
  
  @Autowired
  private InstrumentRepository instrumentRepository;
 
  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Autowired
  private QuestionImageService imageService;

  /**
   * Delete all questions when the dataAcquisitionProject was deleted.
   *
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteQuestionsByProjectId(dataAcquisitionProject.getId());
  }

  /**
   * A service method for deletion of questions within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteQuestionsByProjectId(String dataAcquisitionProjectId) {
    List<Question> deletedQuestions = this.questionRepository
        .deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    deletedQuestions.forEach(question -> {
      imageService.deleteQuestionImage(question.getId());
      elasticsearchUpdateQueueService.enqueue(
          question.getId(),
          ElasticsearchType.questions,
          ElasticsearchUpdateQueueAction.DELETE);
    });
  }

  /**
   * Enqueue deletion of question search document when the question is deleted.
   *
   * @param question the deleted question.
   */
  @HandleAfterDelete
  public void onQuestionDeleted(Question question) {
    imageService.deleteQuestionImage(question.getId());
    elasticsearchUpdateQueueService.enqueue(
        question.getId(),
        ElasticsearchType.questions,
        ElasticsearchUpdateQueueAction.DELETE);
  }

  /**
   * Enqueue update of question search document when the question is updated.
   *
   * @param question the updated or created question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onQuestionSaved(Question question) {
    elasticsearchUpdateQueueService.enqueue(
        question.getId(),
        ElasticsearchType.questions,
        ElasticsearchUpdateQueueAction.UPSERT);
  }
  
  /**
   * Enqueue update of question search documents when the study is changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onStudyChanged(Study study) {
    List<Question> questions = questionRepository.findByStudyId(study.getId());
    questions.forEach(question -> {
      elasticsearchUpdateQueueService.enqueue(
          question.getId(), 
          ElasticsearchType.questions, 
          ElasticsearchUpdateQueueAction.UPSERT);      
    });    
  }
  
  /**
   * Enqueue update of question search documents when the instrument is changed.
   * 
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    List<Question> questions = questionRepository.findByInstrumentId(instrument.getId());
    questions.forEach(question -> {
      elasticsearchUpdateQueueService.enqueue(
          question.getId(), 
          ElasticsearchType.questions, 
          ElasticsearchUpdateQueueAction.UPSERT);      
    });    
  }
  
  /**
   * Enqueue update of question search documents when the survey is changed.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    List<Instrument> instruments = instrumentRepository.findBySurveyIdsContaining(survey.getId());
    instruments.forEach(instrument -> {
      List<Question> questions = questionRepository.findByInstrumentId(instrument.getId());
      questions.forEach(question -> {
        elasticsearchUpdateQueueService.enqueue(
            question.getId(), 
            ElasticsearchType.questions, 
            ElasticsearchUpdateQueueAction.UPSERT);      
      });          
    });
  }

  /**
   * Enqueue update of question search document when the variable is changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    if (variable.getRelatedQuestions() != null) {
      List<String> questionIds = variable.getRelatedQuestions().stream()
          .map(RelatedQuestion::getQuestionId).collect(Collectors.toList());
      List<Question> questions = questionRepository.findByIdIn(questionIds);
      questions.forEach(question -> {
        elasticsearchUpdateQueueService.enqueue(question.getId(),
            ElasticsearchType.questions, ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }  
  
  /**
   * Enqueue update of question search document when the related publication 
   * is changed.
   * 
   * @param relatedPublication the updated, created or deleted publication.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    if (relatedPublication.getQuestionIds() != null) {
      List<Question> questions = questionRepository.findByIdIn(relatedPublication
          .getQuestionIds());
      questions.forEach(question -> {
        elasticsearchUpdateQueueService.enqueue(question.getId(),
            ElasticsearchType.questions, ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
}
