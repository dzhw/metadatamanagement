package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.common.service.DomainObjectChangesProvider;
import eu.dzhw.fdz.metadatamanagement.common.service.util.ListUtils;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;

/**
 * Remember the previous version of a related publication per request
 * in order to update elasticsearch correctly.
 *  
 * @author René Reitmann
 */
@Component
@RequestScope
public class RelatedPublicationChangesProvider extends DomainObjectChangesProvider<RelatedPublication> {
  /**
   * Get the list of surveyIds which need to be updated.
   * @return a list of surveyIds
   */
  public List<String> getAffectedSurveyIds(String relatedPublicationId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldDomainObjects.get(relatedPublicationId) != null) {
      oldIds = oldDomainObjects.get(relatedPublicationId).getSurveyIds();
    }
    if (newDomainObjects.get(relatedPublicationId) != null) {
      newIds = newDomainObjects.get(relatedPublicationId).getSurveyIds();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }
  
  /**
   * Get the list of studyIds which need to be updated.
   * @return a list of studyIds
   */
  public List<String> getAffectedStudyIds(String relatedPublicationId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldDomainObjects.get(relatedPublicationId) != null) {
      oldIds = oldDomainObjects.get(relatedPublicationId).getStudyIds() != null 
          ? oldDomainObjects.get(relatedPublicationId).getStudyIds()
          : new ArrayList<>();
    }
    if (newDomainObjects.get(relatedPublicationId) != null) {
      newIds = newDomainObjects.get(relatedPublicationId).getStudyIds() != null
          ? newDomainObjects.get(relatedPublicationId).getStudyIds()
          : new ArrayList<>();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }
  
  /**
   * Get the list of dataSetIds which need to be updated.
   * @return a list of dataSetIds
   */
  public List<String> getAffectedDataSetIds(String relatedPublicationId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldDomainObjects.get(relatedPublicationId) != null) {
      oldIds = oldDomainObjects.get(relatedPublicationId).getDataSetIds();
    }
    if (newDomainObjects.get(relatedPublicationId) != null) {
      newIds = newDomainObjects.get(relatedPublicationId).getDataSetIds();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }

  /**
   * Get the list of variableIds which need to be updated.
   * @return a list of variableIds
   */
  public List<String> getAffectedVariableIds(String relatedPublicationId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldDomainObjects.get(relatedPublicationId) != null) {
      oldIds = oldDomainObjects.get(relatedPublicationId).getVariableIds();
    }
    if (newDomainObjects.get(relatedPublicationId) != null) {
      newIds = newDomainObjects.get(relatedPublicationId).getVariableIds();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }
  
  /**
   * Get the list of instrumentIds which need to be updated.
   * @return a list of instrumentIds
   */
  public List<String> getAffectedInstrumentIds(String relatedPublicationId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldDomainObjects.get(relatedPublicationId) != null) {
      oldIds = oldDomainObjects.get(relatedPublicationId).getInstrumentIds();
    }
    if (newDomainObjects.get(relatedPublicationId) != null) {
      newIds = newDomainObjects.get(relatedPublicationId).getInstrumentIds();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }
  
  /**
   * Get the list of questionIds which need to be updated.
   * @return a list of questionIds
   */
  public List<String> getAffectedQuestionIds(String relatedPublicationId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldDomainObjects.get(relatedPublicationId) != null) {
      oldIds = oldDomainObjects.get(relatedPublicationId).getQuestionIds();
    }
    if (newDomainObjects.get(relatedPublicationId) != null) {
      newIds = newDomainObjects.get(relatedPublicationId).getQuestionIds();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }
  
  /**
   * Detect if changes need to be send to Dara.
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
   * Get the list of study ids which have been removed from the publication.
   * @param relatedPublicationId the id of the publication
   * @return list of study ids which have been removed from the publications
   */
  public List<String> getDeletedStudyIds(String relatedPublicationId) {
    if (oldDomainObjects.get(relatedPublicationId) == null 
        || oldDomainObjects.get(relatedPublicationId).getStudyIds() == null) {
      return new ArrayList<>();
    }
    if (newDomainObjects.get(relatedPublicationId) == null
        || newDomainObjects.get(relatedPublicationId).getStudyIds() == null) {
      return oldDomainObjects.get(relatedPublicationId).getStudyIds();
    }
    List<String> deletedStudyIds = new ArrayList<>(oldDomainObjects.get(relatedPublicationId)
        .getStudyIds());
    deletedStudyIds.removeAll(newDomainObjects.get(relatedPublicationId).getStudyIds());
    return deletedStudyIds;
  }
  
  /**
   * Get the list of study ids which have been added to the publication.
   * @param relatedPublicationId the id of the publication
   * @return list of study ids which have been added to the publications
   */
  public List<String> getAddedStudyIds(String relatedPublicationId) {
    if (newDomainObjects.get(relatedPublicationId) == null 
        || newDomainObjects.get(relatedPublicationId)
        .getStudyIds() == null) {
      return new ArrayList<>();
    }
    if (oldDomainObjects.get(relatedPublicationId) == null
        || oldDomainObjects.get(relatedPublicationId).getStudyIds() == null) {
      return newDomainObjects.get(relatedPublicationId).getStudyIds();
    }
    List<String> addedStudyIds = new ArrayList<>(newDomainObjects.get(relatedPublicationId)
        .getStudyIds());
    addedStudyIds.removeAll(oldDomainObjects.get(relatedPublicationId)
        .getStudyIds());
    return addedStudyIds;
  }
}
