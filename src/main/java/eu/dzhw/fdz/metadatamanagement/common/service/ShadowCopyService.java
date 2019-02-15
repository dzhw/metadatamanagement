package eu.dzhw.fdz.metadatamanagement.common.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterCreateEvent;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeCreateEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Create shadow copies of domain objects provided by {@link ShadowCopyDataSource}s.
 * @param <T> The domain object to be copied.
 */
@Service
public class ShadowCopyService<T extends AbstractShadowableRdcDomainObject> {

  private static final String MASTER_DELETED_SUCCESSOR_ID = "DELETED";

  private ApplicationEventPublisher applicationEventPublisher;

  public ShadowCopyService(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  /**
   * Create shadow copies of the master domain objects of a project returned by
   * {@link ShadowCopyDataSource}.
   * @param dataAcquisitionProject Domain object's {@link DataAcquisitionProject}
   * @param previousVersion        The previous version of the project or {@code null} if this
   */
  public void createShadowCopies(DataAcquisitionProject dataAcquisitionProject,
      String previousVersion, ShadowCopyDataSource<T> shadowCopyDataSource) {

    String version = dataAcquisitionProject.getRelease().getVersion();
    String projectId = dataAcquisitionProject.getId();
    boolean hasPreviousVersion = StringUtils.hasText(previousVersion);

    try (Stream<T> masters = shadowCopyDataSource.getMasters(projectId)) {

      masters.map(master -> shadowCopyDataSource.createShadowCopy(master, version))
          .forEach(shadowCopy -> {
            if (hasPreviousVersion) {
              Optional<T> opt = shadowCopyDataSource
                  .findPredecessorOfShadowCopy(shadowCopy, previousVersion);
              if (opt.isPresent()) {
                T predecessor = opt.get();
                predecessor.setSuccessorId(shadowCopy.getId());
                applicationEventPublisher.publishEvent(new BeforeSaveEvent(predecessor));
                shadowCopyDataSource.updatePredecessor(predecessor);
                applicationEventPublisher.publishEvent(new AfterSaveEvent(predecessor));
              }
            }

            if (shadowCopy.getVersion() == null) {
              applicationEventPublisher.publishEvent(new BeforeCreateEvent(shadowCopy));
              shadowCopyDataSource.saveShadowCopy(shadowCopy);
              applicationEventPublisher.publishEvent(new AfterCreateEvent(shadowCopy));
            } else {
              applicationEventPublisher.publishEvent(new BeforeSaveEvent(shadowCopy));
              shadowCopyDataSource.saveShadowCopy(shadowCopy);
              applicationEventPublisher.publishEvent(new AfterSaveEvent(shadowCopy));
            }
          });
    }

    try (Stream<T> shadowCopies = shadowCopyDataSource
        .findShadowCopiesWithDeletedMasters(projectId, previousVersion)) {

      shadowCopies.forEach(shadowCopy -> {
        shadowCopy.setSuccessorId(MASTER_DELETED_SUCCESSOR_ID);
        applicationEventPublisher.publishEvent(new BeforeSaveEvent(shadowCopy));
        shadowCopyDataSource.updatePredecessor(shadowCopy);
        applicationEventPublisher.publishEvent(new AfterSaveEvent(shadowCopy));
      });
    }
  }
}
