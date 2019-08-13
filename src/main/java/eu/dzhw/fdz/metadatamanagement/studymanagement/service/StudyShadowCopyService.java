package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingEndedEvent;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingStartedEvent;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.helper.StudyShadowCopyDataSource;

/**
 * Service which generates shadow copies of all studies of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class StudyShadowCopyService extends ShadowCopyHelper<Study> {
  public StudyShadowCopyService(StudyShadowCopyDataSource studyShadowCopyDataSource) {
    super(studyShadowCopyDataSource);
  }

  /**
   * Create shadow copies of current master studies on project release.
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

