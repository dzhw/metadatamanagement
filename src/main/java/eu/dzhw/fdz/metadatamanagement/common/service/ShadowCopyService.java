package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectVersionsService;

/**
 * Create shadow copies of domain objects provided by {@link ShadowCopyDataSource}s.
 * @param <T> The domain object to be copied.
 */
@Service
public class ShadowCopyService<T extends AbstractShadowableRdcDomainObject> {

  private static final String MASTER_DELETED_SUCCESSOR_ID = "DELETED";

  private DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

  public ShadowCopyService(DataAcquisitionProjectVersionsService
                               dataAcquisitionProjectVersionsService) {
    this.dataAcquisitionProjectVersionsService = dataAcquisitionProjectVersionsService;
  }

  /**
   * Create shadow copies of the master domain objects of a project returned by
   * {@link ShadowCopyDataSource}.
   * @param dataAcquisitionProject Domain object's {@link DataAcquisitionProject}
   * @param previousVersion        The previous version of the project or {@code null} if this
   */
  public void createShadowCopies(DataAcquisitionProject dataAcquisitionProject,
      String previousVersion, ShadowCopyDataSource<T> shadowCopyDataSource) {

    String version = getVersion(dataAcquisitionProject);
    String projectId = dataAcquisitionProject.getId();
    boolean hasPreviousVersion = StringUtils.hasText(previousVersion);

    // we might release with the same version, therefore delete all existing shadows
    shadowCopyDataSource.deleteExistingShadowCopies(projectId, version);
    
    try (Stream<T> masters = shadowCopyDataSource.getMasters(projectId)) {

      masters.map(master -> shadowCopyDataSource.createShadowCopy(master, version))
          .forEach(shadowCopy -> {
            if (hasPreviousVersion) {
              Optional<T> opt = shadowCopyDataSource
                  .findPredecessorOfShadowCopy(shadowCopy, previousVersion);
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
        .findShadowCopiesWithDeletedMasters(projectId, previousVersion)) {

      shadowCopies.forEach(shadowCopy -> {
        shadowCopy.setSuccessorId(MASTER_DELETED_SUCCESSOR_ID);
        shadowCopyDataSource.updatePredecessor(shadowCopy);
      });
    }
  }

  private String getVersion(DataAcquisitionProject dataAcquisitionProject) {
    Release release = dataAcquisitionProject.getRelease();
    String version;
    if (release != null) {
      String currentVersion = release.getVersion();
      if (StringUtils.hasText(currentVersion)) {
        version = currentVersion;
      } else {
        version = loadVersionFromHistory(dataAcquisitionProject.getMasterId());
      }
    } else {
      version = loadVersionFromHistory(dataAcquisitionProject.getMasterId());
    }

    if (StringUtils.hasText(version)) {
      return version;
    } else {
      throw new IllegalStateException(dataAcquisitionProject + " has currently no set version and "
          + "it could not be found in the project's history. Unable to create a shadow copy for "
          + "this project.");
    }
  }

  private String loadVersionFromHistory(String dataAcquisitionProjectId) {
    Release release = dataAcquisitionProjectVersionsService
        .findLastRelease(dataAcquisitionProjectId);
    if (release != null) {
      return release.getVersion();
    } else {
      return null;
    }
  }
}
