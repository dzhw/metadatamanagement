package eu.dzhw.fdz.metadatamanagement.common.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Create shadow copies of domain objects provided by {@link ShadowCopyDataSource}s.
 * 
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
   */
  public void createShadowCopies(DataAcquisitionProject dataAcquisitionProject,
                                 ShadowCopyDataSource<T> shadowCopyDataSource) {

    String version = dataAcquisitionProject.getRelease().getVersion();
    String projectId = dataAcquisitionProject.getId();

    List<T> cache = new ArrayList<>();

    try (Stream<T> masters = shadowCopyDataSource.getMasters(projectId)) {

      masters.map(master -> shadowCopyDataSource.createShadowCopy(master, version))
          .forEach(shadowCopy -> {
            cache.add(shadowCopy);
            if (cache.size() >= 100) {
              List<T> updatedSuccessors =
                  updateSuccessorIdsOfPredecessorShadowCopies(projectId, cache,
                      shadowCopyDataSource);

              cache.addAll(updatedSuccessors);
              List<T> persisted = shadowCopyDataSource.saveShadowCopies(new ArrayList<>(cache));
              persisted.forEach(item -> applicationEventPublisher
                  .publishEvent(new AfterSaveEvent(item)));
              cache.clear();
            }
          });

      if (!cache.isEmpty()) {
        List<T> updatedSuccessors =
            updateSuccessorIdsOfPredecessorShadowCopies(projectId, cache,
                shadowCopyDataSource);

        cache.addAll(updatedSuccessors);
        List<T> persisted = shadowCopyDataSource.saveShadowCopies(new ArrayList<>(cache));
        persisted.forEach(item -> applicationEventPublisher
            .publishEvent(new AfterSaveEvent(item)));
      }
    }
  }

  private List<T> updateSuccessorIdsOfPredecessorShadowCopies(
      String dataAcquisitionProjectId,
      List<T> masterShadowCopies,
      ShadowCopyDataSource<T> dataProvider) {

    Map<String, T> masterShadowCopiesMap =
        groupByMasterId(masterShadowCopies);

    try (Stream<T> predecessors = dataProvider
        .getLastShadowCopies(dataAcquisitionProjectId)) {

      return predecessors.peek(predecessor -> {
        T latestShadowCopy = masterShadowCopiesMap
            .get(predecessor.getMasterId());

        if (latestShadowCopy != null) {
          predecessor.setSuccessorId(latestShadowCopy.getId());
        } else {
          predecessor.setSuccessorId(MASTER_DELETED_SUCCESSOR_ID);
        }
      }).collect(Collectors.toList());
    }
  }

  private Map<String, T> groupByMasterId(
      List<T> items) {

    Map<String, T> map = new HashMap<>(items.size());
    items.forEach(item -> map.put(item.getMasterId(), item));
    return map;
  }

}
