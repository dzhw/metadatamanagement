package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.helper.InstrumentShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingStartedEvent;

/**
 * Service which generates shadow copies of all instruments of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class InstrumentShadowCopyService extends ShadowCopyHelper<Instrument> {
  public InstrumentShadowCopyService(InstrumentShadowCopyDataSource instrumentShadowCopyDataSource) {
    super(instrumentShadowCopyDataSource);
  }

  /**
   * Create shadow copies of current master instruments on project release.
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
