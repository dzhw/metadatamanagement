package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.helper.InstrumentAttachmentShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingStartedEvent;

/**
 * Service which generates shadow copies of all instrument attachments of a project, when the
 * project has been released.
 * 
 * @author René Reitmann
 */
@Service
public class InstrumentAttachmentShadowCopyService
    extends ShadowCopyHelper<InstrumentAttachmentMetadata> {
  public InstrumentAttachmentShadowCopyService(
      InstrumentAttachmentShadowCopyDataSource shadowCopyDataSource) {
    super(shadowCopyDataSource);
  }

  /**
   * Create shadow copies of current master instrument attachments on project release.
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
