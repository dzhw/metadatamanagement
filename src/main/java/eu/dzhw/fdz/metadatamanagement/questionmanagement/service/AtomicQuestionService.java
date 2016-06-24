package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.AtomicQuestion;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.AtomicQuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * Service for creating and updating atomic questions. Used for updating atomic questions in mongo
 * and elasticsearch.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
@RepositoryEventHandler
public class AtomicQuestionService {

  @Inject
  private AtomicQuestionRepository atomicQuestionRepository;

  @Inject
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  /**
   * Delete all atomic questions when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAtomicQuestionsByProjectId(dataAcquisitionProject.getId());
  }
  
  /**
   * A service method for deletion of atomicQuestions within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   * @return List of deleted AtomicQuestions
   */
  public List<AtomicQuestion> deleteAtomicQuestionsByProjectId(String dataAcquisitionProjectId) {
    List<AtomicQuestion> deletedAtomicQuestions = this.atomicQuestionRepository
        .deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    deletedAtomicQuestions.forEach(atomicQuestion -> {
      elasticsearchUpdateQueueService.enqueue(
          atomicQuestion.getId(), 
          ElasticsearchType.atomic_questions, 
          ElasticsearchUpdateQueueAction.DELETE);      
    });
    return deletedAtomicQuestions;
  }
  
  /**
   * Enqueue deletion of atomicQuestion search document when the atomicQuestion is deleted.
   * 
   * @param atomicQuestion the deleted atomicQuestion.
   */
  @HandleAfterDelete
  public void onAtomicQuestionDeleted(AtomicQuestion atomicQuestion) {
    elasticsearchUpdateQueueService.enqueue(
        atomicQuestion.getId(), 
        ElasticsearchType.atomic_questions, 
        ElasticsearchUpdateQueueAction.DELETE);
  }
  
  /**
   * Enqueue update of question search document when the atomicQuestion is updated.
   * 
   * @param atomicQuestion the updated or created question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onAtomicQuestionSaved(AtomicQuestion atomicQuestion) {
    elasticsearchUpdateQueueService.enqueue(
        atomicQuestion.getId(), 
        ElasticsearchType.atomic_questions, 
        ElasticsearchUpdateQueueAction.UPSERT);
  }
}
