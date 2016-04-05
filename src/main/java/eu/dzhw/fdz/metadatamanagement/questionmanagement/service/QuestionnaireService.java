package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Questionnaire;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionnaireRepository;

/**
 * The service for the questionnaires. This service handels delete events.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
@RepositoryEventHandler
public class QuestionnaireService {

  @Inject
  private QuestionnaireRepository questionnaireRepository;

  @Inject
  private ApplicationEventPublisher eventPublisher;

  /**
   * Listener, which will be activate by a deletion of a data acquisition project.
   * 
   * @param dataAcquisitionProject A reference to the data acquisition project.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    List<Questionnaire> deletedQuestionnaires =
        questionnaireRepository.deleteByDataAcquisitionProjectId(dataAcquisitionProject.getId());
    deletedQuestionnaires
      .forEach(questionnaire -> eventPublisher.publishEvent(new AfterDeleteEvent(questionnaire)));
  }
}
