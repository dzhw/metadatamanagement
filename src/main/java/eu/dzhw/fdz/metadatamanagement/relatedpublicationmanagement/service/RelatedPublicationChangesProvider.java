package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

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
public class RelatedPublicationChangesProvider {
  private Map<String, RelatedPublication> oldPublications = new HashMap<>();
  private Map<String, RelatedPublication> newPublications = new HashMap<>();
  
  /**
   * Get the list of surveyIds which need to be updated.
   * @return a list of surveyIds
   */
  public List<String> getAffectedSurveyIds(String relatedPublicationId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldPublications.get(relatedPublicationId) != null) {
      oldIds = oldPublications.get(relatedPublicationId).getSurveyIds();
    }
    if (newPublications.get(relatedPublicationId) != null) {
      newIds = newPublications.get(relatedPublicationId).getSurveyIds();
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
    if (oldPublications.get(relatedPublicationId) != null) {
      oldIds = oldPublications.get(relatedPublicationId).getStudyIds() != null 
          ? oldPublications.get(relatedPublicationId).getStudyIds()
          : new ArrayList<>();
    }
    if (newPublications.get(relatedPublicationId) != null) {
      newIds = newPublications.get(relatedPublicationId).getStudyIds() != null
          ? newPublications.get(relatedPublicationId).getStudyIds()
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
    if (oldPublications.get(relatedPublicationId) != null) {
      oldIds = oldPublications.get(relatedPublicationId).getDataSetIds();
    }
    if (newPublications.get(relatedPublicationId) != null) {
      newIds = newPublications.get(relatedPublicationId).getDataSetIds();
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
    if (oldPublications.get(relatedPublicationId) != null) {
      oldIds = oldPublications.get(relatedPublicationId).getVariableIds();
    }
    if (newPublications.get(relatedPublicationId) != null) {
      newIds = newPublications.get(relatedPublicationId).getVariableIds();
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
    if (oldPublications.get(relatedPublicationId) != null) {
      oldIds = oldPublications.get(relatedPublicationId).getInstrumentIds();
    }
    if (newPublications.get(relatedPublicationId) != null) {
      newIds = newPublications.get(relatedPublicationId).getInstrumentIds();
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
    if (oldPublications.get(relatedPublicationId) != null) {
      oldIds = oldPublications.get(relatedPublicationId).getQuestionIds();
    }
    if (newPublications.get(relatedPublicationId) != null) {
      newIds = newPublications.get(relatedPublicationId).getQuestionIds();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }

  protected void put(RelatedPublication newPublication, RelatedPublication oldPublication) {
    if (newPublication != null) {      
      newPublications.put(newPublication.getId(), newPublication);
    }
    if (oldPublication != null) {      
      oldPublications.put(oldPublication.getId(), oldPublication);
    }
  }
  
  /**
   * Detect if changes need to be send to Dara.
   * @param relatedPublicationId The id of the related publication
   * @return true if the changes need to be send to dara
   */
  public boolean hasChangesRelevantForDara(String relatedPublicationId) {
    if (newPublications.get(relatedPublicationId) == null 
        || oldPublications.get(relatedPublicationId) == null) {
      return true;
    }
    return !oldPublications.get(relatedPublicationId).getSourceReference()
        .equals(newPublications.get(relatedPublicationId).getSourceReference());
  }
  
  /**
   * Get the list of study ids which have been removed from the publication.
   * @param relatedPublicationId the id of the publication
   * @return list of study ids which have been removed from the publications
   */
  public List<String> getDeletedStudyIds(String relatedPublicationId) {
    if (oldPublications.get(relatedPublicationId) == null 
        || oldPublications.get(relatedPublicationId).getStudyIds() == null) {
      return new ArrayList<>();
    }
    if (newPublications.get(relatedPublicationId) == null) {
      return oldPublications.get(relatedPublicationId).getStudyIds();
    }
    List<String> deletedStudyIds = new ArrayList<>(oldPublications.get(relatedPublicationId)
        .getStudyIds());
    deletedStudyIds.removeAll(newPublications.get(relatedPublicationId).getStudyIds());
    return deletedStudyIds;
  }
  
  /**
   * Get the list of study ids which have been added to the publication.
   * @param relatedPublicationId the id of the publication
   * @return list of study ids which have been added to the publications
   */
  public List<String> getAddedStudyIds(String relatedPublicationId) {
    if (newPublications.get(relatedPublicationId) == null 
        || newPublications.get(relatedPublicationId)
        .getStudyIds() == null) {
      return new ArrayList<>();
    }
    if (oldPublications.get(relatedPublicationId) == null) {
      return newPublications.get(relatedPublicationId).getStudyIds();
    }
    List<String> addedStudyIds = new ArrayList<>(newPublications.get(relatedPublicationId)
        .getStudyIds());
    addedStudyIds.removeAll(oldPublications.get(relatedPublicationId)
        .getStudyIds());
    return addedStudyIds;
  }
}
