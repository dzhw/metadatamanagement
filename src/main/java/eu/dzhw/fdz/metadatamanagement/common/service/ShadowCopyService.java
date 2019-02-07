package eu.dzhw.fdz.metadatamanagement.common.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Create shadow copies of domain objects provided by {@link ShadowCopyDataProvider}s.
 */
@Service
public class ShadowCopyService {

  private static final Logger LOG = LoggerFactory.getLogger(ShadowCopyService.class);

  private static final String MASTER_DELETED_SUCCESSOR_ID = "DELETED";

  /**
   * Create shadow copies of the master domain objects of a project returned by
   * {@link ShadowCopyDataProvider}.
   * @param dataAcquisitionProjectId Id of released project
   * @param releaseVersion Version of released project
   */
  public void createShadowCopies(String dataAcquisitionProjectId, String releaseVersion,
                                 ShadowCopyDataProvider shadowCopyDataProvider) {

    List<AbstractShadowableRdcDomainObject> shadowCopies =
        createShadowCopiesOfCurrentMasters(dataAcquisitionProjectId, releaseVersion,
            shadowCopyDataProvider);

    List<AbstractShadowableRdcDomainObject> updatedPredecessors =
        updateSuccessorsOfPredecessorShadowCopies(dataAcquisitionProjectId, shadowCopies,
            shadowCopyDataProvider);

    shadowCopies.addAll(updatedPredecessors);
    shadowCopyDataProvider.saveShadowCopies(shadowCopies);
  }

  /**
   * Set successor id to a deleted reference for latest shadow copies provided by
   * {@link ShadowCopyDataProvider}.
   * @param dataAcquisitionProjectId Data acquisition project id
   */
  public void writeDeletedSuccessorIdToShadowCopiesWithoutSuccessorId(
      String dataAcquisitionProjectId,
      ShadowCopyDataProvider shadowCopyDataProvider) {

    List<? extends AbstractShadowableRdcDomainObject> shadows = shadowCopyDataProvider
        .getLastShadowCopies(dataAcquisitionProjectId);

    shadows.forEach(shadow -> shadow.setSuccessorId(MASTER_DELETED_SUCCESSOR_ID));
    shadowCopyDataProvider.saveShadowCopies(shadows);
  }

  private List<AbstractShadowableRdcDomainObject> createShadowCopiesOfCurrentMasters(
      String dataAcquisitionProjectId, String version,
      ShadowCopyDataProvider shadowCopyDataProvider) {

    List<? extends AbstractShadowableRdcDomainObject> masters = shadowCopyDataProvider
        .getMasters(dataAcquisitionProjectId);

    return masters.stream().map(master -> shadowCopyDataProvider.createShadowCopy(master, version))
        .collect(Collectors.toList());
  }

  private List<AbstractShadowableRdcDomainObject> updateSuccessorsOfPredecessorShadowCopies(
      String dataAcquisitionProjectId,
      List<? extends AbstractShadowableRdcDomainObject> masterShadowCopies,
      ShadowCopyDataProvider dataProvider) {

    Map<String, ? extends AbstractShadowableRdcDomainObject> masterShadowCopiesMap =
        groupByMasterId(masterShadowCopies);
    List<? extends AbstractShadowableRdcDomainObject> predecessors = dataProvider
        .getLastShadowCopies(dataAcquisitionProjectId);

    return predecessors.stream().peek(predecessor -> {
      AbstractShadowableRdcDomainObject latestShadowCopy = masterShadowCopiesMap
          .get(predecessor.getMasterId());

      if (latestShadowCopy != null) {
        predecessor.setSuccessorId(latestShadowCopy.getId());
      } else {
        warnAboutMissingMaster(predecessor.getId(), predecessor.getMasterId());
        predecessor.setSuccessorId(MASTER_DELETED_SUCCESSOR_ID);
      }
    }).collect(Collectors.toList());
  }

  private static Map<String, AbstractShadowableRdcDomainObject> groupByMasterId(
      List<? extends AbstractShadowableRdcDomainObject> items) {

    Map<String, AbstractShadowableRdcDomainObject> map = new HashMap<>(items.size());
    items.forEach(item -> map.put(item.getMasterId(), item));
    return map;
  }

  private static void warnAboutMissingMaster(String shadowId, String masterId) {
    LOG.warn("Master for " + shadowId + " with id " + masterId + " is missing unexpectedly."
        + "It was probably deleted during the creation of shadow copies. The successor of the"
        + "shadow copy will be set to " + MASTER_DELETED_SUCCESSOR_ID);
  }

}
