package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.common.service.DomainObjectChangesProvider;
import eu.dzhw.fdz.metadatamanagement.common.service.util.ListUtils;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Remember the previous version of a variable per request in order to update elasticsearch
 * correctly.
 * 
 * @author René Reitmann
 */
@Component
@RequestScope
public class VariableChangesProvider extends DomainObjectChangesProvider<Variable> {
  /**
   * Get the list of surveyIds which need to be updated.
   * 
   * @return a list of surveyIds
   */
  public List<String> getAffectedSurveyIds(String variableId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldDomainObjects.get(variableId) != null) {
      oldIds = oldDomainObjects.get(variableId).getSurveyIds();
    }
    if (newDomainObjects.get(variableId) != null) {
      newIds = newDomainObjects.get(variableId).getSurveyIds();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }

  /**
   * Get the list of instrumentIds which need to be updated.
   * 
   * @return a list of instrumentIds
   */
  public List<String> getAffectedInstrumentIds(String variableId) {
    List<String> oldIds = new ArrayList<>();
    List<String> newIds = new ArrayList<>();
    if (oldDomainObjects.get(variableId) != null
        && oldDomainObjects.get(variableId).getRelatedQuestions() != null) {
      for (RelatedQuestion question : oldDomainObjects.get(variableId).getRelatedQuestions()) {
        if (!oldIds.contains(question.getInstrumentId())) {
          oldIds.add(question.getInstrumentId());
        }
      }
    }
    if (oldDomainObjects.get(variableId) != null
        && oldDomainObjects.get(variableId).getRelatedQuestions() != null) {
      for (RelatedQuestion question : oldDomainObjects.get(variableId).getRelatedQuestions()) {
        if (!newIds.contains(question.getInstrumentId())) {
          newIds.add(question.getInstrumentId());
        }
      }
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }

  /**
   * Get the list of questionIds which need to be updated.
   * 
   * @return a list of questionIds
   */
  public List<String> getAffectedQuestionIds(String variableId) {
    List<String> oldIds = new ArrayList<>();
    List<String> newIds = new ArrayList<>();
    if (oldDomainObjects.get(variableId) != null
        && oldDomainObjects.get(variableId).getRelatedQuestions() != null) {
      for (RelatedQuestion question : oldDomainObjects.get(variableId).getRelatedQuestions()) {
        oldIds.add(question.getQuestionId());
      }
    }
    if (newDomainObjects.get(variableId) != null
        && newDomainObjects.get(variableId).getRelatedQuestions() != null) {
      for (RelatedQuestion question : newDomainObjects.get(variableId).getRelatedQuestions()) {
        newIds.add(question.getQuestionId());
      }
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }
}
