package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.context.event.EventListener;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingEndedEvent;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingStartedEvent;
import lombok.RequiredArgsConstructor;

/**
 * Create shadow copies of domain objects provided by {@link ShadowCopyDataSource}s.
 *
 * @param <T> The domain object to be copied.
 */
@RequiredArgsConstructor
public class ShadowCopyHelper<T extends AbstractShadowableRdcDomainObject> {

  private static final String MASTER_DELETED_SUCCESSOR_ID = "DELETED";

  private final ShadowCopyDataSource<T> shadowCopyDataSource;

  /**
   * Create shadow copies of the master domain objects of a project returned by
   * {@link ShadowCopyDataSource}.
   *
   * @param dataAcquisitionProjectId id of the project being shadow copied
   * @param release The release object containing the version of the shadow copies being created
   * @param previousVersion The previous version of the project or {@code null} if this
   */
  public void createShadowCopies(String dataAcquisitionProjectId, Release release,
      String previousVersion) {
    boolean hasPreviousVersion = StringUtils.hasText(previousVersion);

    // we might release with the same version, therefore delete all existing shadows
    shadowCopyDataSource.deleteExistingShadowCopies(dataAcquisitionProjectId, release.getVersion());

    try (Stream<T> masters = shadowCopyDataSource.getMasters(dataAcquisitionProjectId)) {

      masters.map(master -> shadowCopyDataSource.createShadowCopy(master, release))
          .forEach(shadowCopy -> {
            // add a successorId to previous versions if the current release is not a pre-release
            if (hasPreviousVersion && !release.getIsPreRelease()) {
              Optional<T> opt =
                  shadowCopyDataSource.findPredecessorOfShadowCopy(shadowCopy, previousVersion);
              if (opt.isPresent()) {
                T predecessor = opt.get();
                predecessor.setSuccessorId(shadowCopy.getId());
                shadowCopyDataSource.updatePredecessor(predecessor);
              }
            }
            shadowCopyDataSource.saveShadowCopy(shadowCopy);
          });
    }

    try (Stream<T> shadowCopies = shadowCopyDataSource
        .findShadowCopiesWithDeletedMasters(dataAcquisitionProjectId, previousVersion)) {

      shadowCopies.forEach(shadowCopy -> {
        shadowCopy.setSuccessorId(MASTER_DELETED_SUCCESSOR_ID);
        shadowCopyDataSource.updatePredecessor(shadowCopy);
      });
    }
  }

  private void updateElasticsearch(String dataAcquisitionProjectId, String releaseVersion,
      String previousVersion) {
    shadowCopyDataSource.updateElasticsearch(dataAcquisitionProjectId, releaseVersion,
        previousVersion);
  }

  private void hideExistingShadowCopies(String dataAcquisitionProjectId, String releaseVersion) {
    shadowCopyDataSource.hideExistingShadowCopies(dataAcquisitionProjectId, releaseVersion);
  }

  private void unhideExistingShadowCopies(String dataAcquisitionProjectId, String version) {
    shadowCopyDataSource.unhideExistingShadowCopies(dataAcquisitionProjectId, version);
  }

  private void deleteExistingShadowCopies(String dataAcquisitionProjectId, String version) {
    shadowCopyDataSource.deleteExistingShadowCopies(dataAcquisitionProjectId, version);
  }

  /**
   * Create, hide or unhide shadow copies of current master domain objects on project release.
   *
   * @param shadowCopyingStartedEvent Emitted by {@link ShadowCopyQueueItemService}
   */
  @EventListener
  public void onShadowCopyingStarted(ShadowCopyingStartedEvent shadowCopyingStartedEvent) {
    switch (shadowCopyingStartedEvent.getAction()) {
      case CREATE:
        this.createShadowCopies(shadowCopyingStartedEvent.getDataAcquisitionProjectId(),
            shadowCopyingStartedEvent.getRelease(),
            shadowCopyingStartedEvent.getPreviousReleaseVersion());
        break;
      case HIDE:
        this.hideExistingShadowCopies(shadowCopyingStartedEvent.getDataAcquisitionProjectId(),
            shadowCopyingStartedEvent.getRelease().getVersion());
        break;
      case UNHIDE:
        this.unhideExistingShadowCopies(shadowCopyingStartedEvent.getDataAcquisitionProjectId(),
            shadowCopyingStartedEvent.getRelease().getVersion());
        break;
      case DELETE:
        this.deleteExistingShadowCopies(shadowCopyingStartedEvent.getDataAcquisitionProjectId(),
            shadowCopyingStartedEvent.getRelease().getVersion());
        break;
      default:
        throw new IllegalArgumentException(
            shadowCopyingStartedEvent.getAction() + " has not been implemented yet!");
    }
  }

  /**
   * Update elasticsearch (both predecessors and current shadows).
   *
   * @param shadowCopyingEndedEvent Emitted by {@link ShadowCopyQueueItemService}
   */
  @EventListener
  public void onShadowCopyingEnded(ShadowCopyingEndedEvent shadowCopyingEndedEvent) {
    this.updateElasticsearch(shadowCopyingEndedEvent.getDataAcquisitionProjectId(),
        shadowCopyingEndedEvent.getRelease().getVersion(),
        shadowCopyingEndedEvent.getPreviousReleaseVersion());
  }
}
