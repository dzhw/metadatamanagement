package eu.dzhw.fdz.metadatamanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.domain.AtomicQuestion;
import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.repository.AtomicQuestionRepository;

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
  private ApplicationEventPublisher eventPublisher;

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
    deletedAtomicQuestions
      .forEach(atomicQuestion -> eventPublisher.publishEvent(new AfterDeleteEvent(atomicQuestion)));
    return deletedAtomicQuestions;
  }
}
