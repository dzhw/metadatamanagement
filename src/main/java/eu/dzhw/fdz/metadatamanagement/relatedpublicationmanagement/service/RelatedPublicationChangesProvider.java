package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
    return getAffectedIds(relatedPublicationId, RelatedPublication::getDataPackageIds);
  }

  /**
   * Get the list of analysisPackageIds which need to be updated.
   * 
   * @return a list of analysisPackageIds
   */
  public List<String> getAffectedAnalysisPackageIds(String relatedPublicationId) {
    return getAffectedIds(relatedPublicationId, RelatedPublication::getAnalysisPackageIds);
  }

  private List<String> getAffectedIds(String relatedPublicationId,
      Function<RelatedPublication, List<String>> idsGetter) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldDomainObjects.get(relatedPublicationId) != null) {
      oldIds = idsGetter.apply(oldDomainObjects.get(relatedPublicationId)) != null
          ? idsGetter.apply(oldDomainObjects.get(relatedPublicationId))
          : new ArrayList<>();
    }
    if (newDomainObjects.get(relatedPublicationId) != null) {
      newIds = idsGetter.apply(newDomainObjects.get(relatedPublicationId)) != null
          ? idsGetter.apply(newDomainObjects.get(relatedPublicationId))
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
    return getDeletedIds(relatedPublicationId, RelatedPublication::getDataPackageIds);
  }

  /**
   * Get the list of analysisPackage ids which have been removed from the publication.
   * 
   * @param relatedPublicationId the id of the publication
   * @return list of analysisPackage ids which have been removed from the publications
   */
  public List<String> getDeletedAnalysisPackageIds(String relatedPublicationId) {
    return getDeletedIds(relatedPublicationId, RelatedPublication::getAnalysisPackageIds);
  }

  private List<String> getDeletedIds(String relatedPublicationId,
      Function<RelatedPublication, List<String>> idsGetter) {
    if (oldDomainObjects.get(relatedPublicationId) == null
        || idsGetter.apply(oldDomainObjects.get(relatedPublicationId)) == null) {
      return new ArrayList<>();
    }
    if (newDomainObjects.get(relatedPublicationId) == null
        || idsGetter.apply(newDomainObjects.get(relatedPublicationId)) == null) {
      return idsGetter.apply(oldDomainObjects.get(relatedPublicationId));
    }
    List<String> deletedIds =
        new ArrayList<>(idsGetter.apply(oldDomainObjects.get(relatedPublicationId)));
    deletedIds.removeAll(idsGetter.apply(newDomainObjects.get(relatedPublicationId)));
    return deletedIds;
  }

  /**
   * Get the list of dataPackage ids which have been added to the publication.
   * 
   * @param relatedPublicationId the id of the publication
   * @return list of dataPackage ids which have been added to the publications
   */
  public List<String> getAddedDataPackageIds(String relatedPublicationId) {
    return getAddedIds(relatedPublicationId, RelatedPublication::getDataPackageIds);
  }
  
  /**
   * Get the list of analysisPackage ids which have been added to the publication.
   * 
   * @param relatedPublicationId the id of the publication
   * @return list of analysisPackage ids which have been added to the publications
   */
  public List<String> getAddedAnalysisPackageIds(String relatedPublicationId) {
    return getAddedIds(relatedPublicationId, RelatedPublication::getAnalysisPackageIds);
  }

  private List<String> getAddedIds(String relatedPublicationId,
      Function<RelatedPublication, List<String>> idsGetter) {
    if (newDomainObjects.get(relatedPublicationId) == null
        || idsGetter.apply(newDomainObjects.get(relatedPublicationId)) == null) {
      return new ArrayList<>();
    }
    if (oldDomainObjects.get(relatedPublicationId) == null
        || idsGetter.apply(oldDomainObjects.get(relatedPublicationId)) == null) {
      return idsGetter.apply(newDomainObjects.get(relatedPublicationId));
    }
    List<String> addedIds =
        new ArrayList<>(idsGetter.apply(newDomainObjects.get(relatedPublicationId)));
    addedIds.removeAll(idsGetter.apply(oldDomainObjects.get(relatedPublicationId)));
    return addedIds;
  }
}
