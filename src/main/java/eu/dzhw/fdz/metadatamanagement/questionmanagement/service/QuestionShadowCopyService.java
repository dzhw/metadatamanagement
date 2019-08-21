package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingEndedEvent;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingStartedEvent;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.helper.QuestionShadowCopyDataSource;

/**
 * Service which generates shadow copies of all questions of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class QuestionShadowCopyService extends ShadowCopyHelper<Question> {
  public QuestionShadowCopyService(QuestionShadowCopyDataSource questionShadowCopyDataSource) {
    super(questionShadowCopyDataSource);
  }

  /**
   * Create shadow copies of current master questions on project release.
   * 
   * @param shadowCopyingStartedEvent Emitted by {@link ShadowCopyQueueItemService}
   */
  @EventListener
  public void onShadowCopyingStarted(ShadowCopyingStartedEvent shadowCopyingStartedEvent) {
    super.createShadowCopies(shadowCopyingStartedEvent.getDataAcquisitionProjectId(),
        shadowCopyingStartedEvent.getRelease(),
        shadowCopyingStartedEvent.getPreviousReleaseVersion());
  }
  
  /**
   * Update elasticsearch (both predecessors and current shadows).
   * 
   * @param shadowCopyingEndedEvent Emitted by {@link ShadowCopyQueueItemService}
   */
  @EventListener
  public void onShadowCopyingEnded(ShadowCopyingEndedEvent shadowCopyingEndedEvent) {
    super.updateElasticsearch(shadowCopyingEndedEvent.getDataAcquisitionProjectId(),
        shadowCopyingEndedEvent.getRelease().getVersion(),
        shadowCopyingEndedEvent.getPreviousReleaseVersion());
  }
}
