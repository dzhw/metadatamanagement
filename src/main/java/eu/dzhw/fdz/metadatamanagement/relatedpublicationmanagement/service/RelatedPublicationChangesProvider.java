package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

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
      oldIds = oldPublications.get(relatedPublicationId).getStudyIds();
    }
    if (newPublications.get(relatedPublicationId) != null) {
      newIds = newPublications.get(relatedPublicationId).getStudyIds();
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
}
