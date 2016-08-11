package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * Service for creating and updating questions. Used for updating questions in mongo
 * and elasticsearch.
 */
@Service
@RepositoryEventHandler
public class QuestionService {

  @Inject
  private QuestionRepository questionRepository;

  @Inject
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Inject
  private ImageService imageService;
  
  public static final String CONTENT_TYPE_IMAGE = "image/png";

  /**
   * Delete all atomic questions when the dataAcquisitionProject was deleted.
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
   * @return List of deleted Questions
   */
  public List<Question> deleteQuestionsByProjectId(String dataAcquisitionProjectId) {
    List<Question> deletedQuestions = this.questionRepository
        .deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    deletedQuestions.forEach(question -> {
      imageService.deleteQuestionImage(question.getId());
      elasticsearchUpdateQueueService.enqueue(
          question.getId(), 
          ElasticsearchType.questions, 
          ElasticsearchUpdateQueueAction.DELETE);      
    });
    return deletedQuestions;
  }
  
  /**
   * Enqueue deletion of question search document when the question is deleted.
   * 
   * @param question the deleted question.
   */
  @HandleAfterDelete
  public void onQuestionDeleted(Question question) {
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
}
