package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.common.service.DomainObjectChangesProvider;
import eu.dzhw.fdz.metadatamanagement.common.service.util.ListUtils;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;

/**
 * Remember the previous version of a related publication per request in order to update
 * elasticsearch correctly.
 * 
 * @author René Reitmann
 */
@Component
@RequestScope
public class RelatedPublicationChangesProvider
    extends DomainObjectChangesProvider<RelatedPublication> {
  
  /**
   * Get the list of dataPackageIds which need to be updated.
   * 
   * @return a list of dataPackageIds
   */
  public List<String> getAffectedDataPackageIds(String relatedPublicationId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldDomainObjects.get(relatedPublicationId) != null) {
      oldIds = oldDomainObjects.get(relatedPublicationId).getDataPackageIds() != null
          ? oldDomainObjects.get(relatedPublicationId).getDataPackageIds()
          : new ArrayList<>();
    }
    if (newDomainObjects.get(relatedPublicationId) != null) {
      newIds = newDomainObjects.get(relatedPublicationId).getDataPackageIds() != null
          ? newDomainObjects.get(relatedPublicationId).getDataPackageIds()
          : new ArrayList<>();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }

  /**
   * Detect if changes need to be send to Dara.
   * 
   * @param relatedPublicationId The id of the related publication
   * @return true if the changes need to be send to dara
   */
  public boolean hasChangesRelevantForDara(String relatedPublicationId) {
    if (newDomainObjects.get(relatedPublicationId) == null
        || oldDomainObjects.get(relatedPublicationId) == null) {
      return true;
    }
    return !oldDomainObjects.get(relatedPublicationId).getSourceReference()
        .equals(newDomainObjects.get(relatedPublicationId).getSourceReference());
  }

  /**
   * Get the list of dataPackage ids which have been removed from the publication.
   * 
   * @param relatedPublicationId the id of the publication
   * @return list of dataPackage ids which have been removed from the publications
   */
  public List<String> getDeletedDataPackageIds(String relatedPublicationId) {
    if (oldDomainObjects.get(relatedPublicationId) == null
        || oldDomainObjects.get(relatedPublicationId).getDataPackageIds() == null) {
      return new ArrayList<>();
    }
    if (newDomainObjects.get(relatedPublicationId) == null
        || newDomainObjects.get(relatedPublicationId).getDataPackageIds() == null) {
      return oldDomainObjects.get(relatedPublicationId).getDataPackageIds();
    }
    List<String> deletedDataPackageIds =
        new ArrayList<>(oldDomainObjects.get(relatedPublicationId).getDataPackageIds());
    deletedDataPackageIds.removeAll(newDomainObjects.get(relatedPublicationId).getDataPackageIds());
    return deletedDataPackageIds;
  }

  /**
   * Get the list of dataPackage ids which have been added to the publication.
   * 
   * @param relatedPublicationId the id of the publication
   * @return list of dataPackage ids which have been added to the publications
   */
  public List<String> getAddedDataPackageIds(String relatedPublicationId) {
    if (newDomainObjects.get(relatedPublicationId) == null
        || newDomainObjects.get(relatedPublicationId).getDataPackageIds() == null) {
      return new ArrayList<>();
    }
    if (oldDomainObjects.get(relatedPublicationId) == null
        || oldDomainObjects.get(relatedPublicationId).getDataPackageIds() == null) {
      return newDomainObjects.get(relatedPublicationId).getDataPackageIds();
    }
    List<String> addedDataPackageIds =
        new ArrayList<>(newDomainObjects.get(relatedPublicationId).getDataPackageIds());
    addedDataPackageIds.removeAll(oldDomainObjects.get(relatedPublicationId).getDataPackageIds());
    return addedDataPackageIds;
  }
}
