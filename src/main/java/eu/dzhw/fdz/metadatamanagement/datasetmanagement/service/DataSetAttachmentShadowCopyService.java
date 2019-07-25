package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper.DataSetAttachmentShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingStartedEvent;

/**
 * Service which generates shadow copies of all dataSet attachments of a project, when the project
 * has been released.
 * 
 * @author René Reitmann
 */
@Service
public class DataSetAttachmentShadowCopyService extends ShadowCopyHelper<DataSetAttachmentMetadata> {
  public DataSetAttachmentShadowCopyService(DataSetAttachmentShadowCopyDataSource shadowCopyDataSource) {
    super(shadowCopyDataSource);
  }
  
  /**
   * Create shadow copies of current master dataSet attachments on project release.
   * 
   * @param shadowCopyingStartedEvent Emitted by {@link ShadowCopyQueueItemService}
   */
  @EventListener
  public void onShadowCopyingStarted(ShadowCopyingStartedEvent shadowCopyingStartedEvent) {
    super.createShadowCopies(shadowCopyingStartedEvent.getDataAcquisitionProjectId(),
        shadowCopyingStartedEvent.getReleaseVersion(),
        shadowCopyingStartedEvent.getPreviousReleaseVersion());
  }
}
