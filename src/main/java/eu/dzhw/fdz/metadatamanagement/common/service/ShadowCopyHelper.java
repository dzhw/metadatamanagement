package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
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
   * @param releaseVersion The version of the shadow copies being created
   * @param previousVersion The previous version of the project or {@code null} if this
   */
  public void createShadowCopies(String dataAcquisitionProjectId, String releaseVersion,
      String previousVersion) {
    boolean hasPreviousVersion = StringUtils.hasText(previousVersion);

    // we might release with the same version, therefore delete all existing shadows
    shadowCopyDataSource.deleteExistingShadowCopies(dataAcquisitionProjectId, releaseVersion);

    try (Stream<T> masters = shadowCopyDataSource.getMasters(dataAcquisitionProjectId)) {

      masters.map(master -> shadowCopyDataSource.createShadowCopy(master, releaseVersion))
          .forEach(shadowCopy -> {
            if (hasPreviousVersion) {
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

    try (Stream<T> shadowCopies =
        shadowCopyDataSource.findShadowCopiesWithDeletedMasters(dataAcquisitionProjectId, previousVersion)) {

      shadowCopies.forEach(shadowCopy -> {
        shadowCopy.setSuccessorId(MASTER_DELETED_SUCCESSOR_ID);
        shadowCopyDataSource.updatePredecessor(shadowCopy);
      });
    }
  }
}
