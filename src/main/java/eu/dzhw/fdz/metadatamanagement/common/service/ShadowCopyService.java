package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;

/**
 * Create shadow copies of domain objects provided by {@link ShadowCopyDataProvider}s.
 * 
 * @param <T> The domain object to be copied.
 */
@Service
public class ShadowCopyService<T extends AbstractShadowableRdcDomainObject> {

  private static final Logger LOG = LoggerFactory.getLogger(ShadowCopyService.class);

  private static final String MASTER_DELETED_SUCCESSOR_ID = "DELETED";

  /**
   * Create shadow copies of the master domain objects of a project returned by
   * {@link ShadowCopyDataProvider}.
   * @param dataAcquisitionProjectId Id of released project
   * @param releaseVersion           Version of released project
   */
  public void createShadowCopies(String dataAcquisitionProjectId, String releaseVersion,
                                 ShadowCopyDataProvider<T> shadowCopyDataProvider) {

    List<T> cache = new ArrayList<>();

    try (Stream<T> masters = shadowCopyDataProvider
        .getMasters(dataAcquisitionProjectId)) {

      masters.map(master -> shadowCopyDataProvider.createShadowCopy(master, releaseVersion))
          .forEach(shadowCopy -> {
            cache.add(shadowCopy);
            if (cache.size() >= 100) {
              List<T> updatedSuccessors =
                  updateSuccessorIdsOfPredecessorShadowCopies(dataAcquisitionProjectId, cache,
                      shadowCopyDataProvider);

              cache.addAll(updatedSuccessors);
              shadowCopyDataProvider.saveShadowCopies(new ArrayList<>(cache));
              cache.clear();
            }
          });

      if (!cache.isEmpty()) {
        List<T> updatedSuccessors =
            updateSuccessorIdsOfPredecessorShadowCopies(dataAcquisitionProjectId, cache,
                shadowCopyDataProvider);

        cache.addAll(updatedSuccessors);
        shadowCopyDataProvider.saveShadowCopies(new ArrayList<>(cache));
      }
    }
  }

  /**
   * Set successor id to a deleted reference for latest shadow copies provided by
   * {@link ShadowCopyDataProvider}.
   * @param dataAcquisitionProjectId Data acquisition project id
   */
  public void writeDeletedSuccessorIdToShadowCopiesWithoutSuccessorId(
      String dataAcquisitionProjectId,
      ShadowCopyDataProvider<T> shadowCopyDataProvider) {

    List<T> cache = new ArrayList<>();

    try (Stream<T> shadowCopiesStream =
             shadowCopyDataProvider.getLastShadowCopies(dataAcquisitionProjectId)) {

      shadowCopiesStream.forEach(shadow -> {
        shadow.setSuccessorId(MASTER_DELETED_SUCCESSOR_ID);
        cache.add(shadow);
        if (cache.size() >= 100) {
          shadowCopyDataProvider.saveShadowCopies(new ArrayList<T>(cache));
          cache.clear();
        }
      });

      if (!cache.isEmpty()) {
        shadowCopyDataProvider.saveShadowCopies(new ArrayList<>(cache));
      }
    }
  }

  private List<T> updateSuccessorIdsOfPredecessorShadowCopies(
      String dataAcquisitionProjectId,
      List<T> masterShadowCopies,
      ShadowCopyDataProvider<T> dataProvider) {

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
          warnAboutMissingMaster(predecessor.getId(), predecessor.getMasterId());
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

  private static void warnAboutMissingMaster(String shadowId, String masterId) {
    LOG.warn("Master for " + shadowId + " with id " + masterId + " is missing unexpectedly."
        + "It was probably deleted during the creation of shadow copies. The successor of the"
        + "shadow copy will be set to " + MASTER_DELETED_SUCCESSOR_ID);
  }

}
