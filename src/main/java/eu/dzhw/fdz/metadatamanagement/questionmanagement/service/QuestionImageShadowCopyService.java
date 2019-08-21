package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingStartedEvent;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionImageMetadata;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.helper.QuestionImageShadowCopyDataSource;

/**
 * Service which generates shadow copies of all question images of a project, when the project has
 * been released.
 * 
 * @author René Reitmann
 */
@Service
public class QuestionImageShadowCopyService extends ShadowCopyHelper<QuestionImageMetadata> {
  public QuestionImageShadowCopyService(
      QuestionImageShadowCopyDataSource shadowCopyDataSource) {
    super(shadowCopyDataSource);
  }

  /**
   * Create shadow copies of current master question images on project release.
   * 
   * @param shadowCopyingStartedEvent Emitted by {@link ShadowCopyQueueItemService}
   */
  @EventListener
  public void onShadowCopyingStarted(ShadowCopyingStartedEvent shadowCopyingStartedEvent) {
    super.createShadowCopies(shadowCopyingStartedEvent.getDataAcquisitionProjectId(),
        shadowCopyingStartedEvent.getRelease(),
        shadowCopyingStartedEvent.getPreviousReleaseVersion());
  }
}
