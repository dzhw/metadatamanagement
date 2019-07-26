package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingEndedEvent;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingStartedEvent;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper.SurveyShadowCopyDataSource;

/**
 * Service which generates shadow copies of all surveys of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class SurveyShadowCopyService extends ShadowCopyHelper<Survey> {
  public SurveyShadowCopyService(SurveyShadowCopyDataSource surveyShadowCopyDataSource) {
    super(surveyShadowCopyDataSource);
  }

  /**
   * Create shadow copies of current master surveys on project release.
   * 
   * @param shadowCopyingStartedEvent Emitted by {@link ShadowCopyQueueItemService}
   */
  @EventListener
  public void onShadowCopyingStarted(ShadowCopyingStartedEvent shadowCopyingStartedEvent) {
    super.createShadowCopies(shadowCopyingStartedEvent.getDataAcquisitionProjectId(),
        shadowCopyingStartedEvent.getReleaseVersion(),
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
        shadowCopyingEndedEvent.getReleaseVersion(),
        shadowCopyingEndedEvent.getPreviousReleaseVersion());
  }
}
