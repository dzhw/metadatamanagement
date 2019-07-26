package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingEndedEvent;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingStartedEvent;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.helper.VariableShadowCopyDataSource;

/**
 * Service which generates shadow copies of all variables of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class VariableShadowCopyService extends ShadowCopyHelper<Variable> {
  public VariableShadowCopyService(VariableShadowCopyDataSource variableShadowCopyDataSource) {
    super(variableShadowCopyDataSource);
  }

  /**
   * Create shadow copies of current master variables on project release.
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
