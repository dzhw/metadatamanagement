package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

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
public class VariableChangesProvider {
  private Map<String, Variable> oldVariables = new HashMap<>();
  private Map<String, Variable> newVariables = new HashMap<>();

  /**
   * Get the list of surveyIds which need to be updated.
   * 
   * @return a list of surveyIds
   */
  public List<String> getAffectedSurveyIds(String variableId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldVariables.get(variableId) != null) {
      oldIds = oldVariables.get(variableId).getSurveyIds();
    }
    if (newVariables.get(variableId) != null) {
      newIds = newVariables.get(variableId).getSurveyIds();
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
    if (oldVariables.get(variableId) != null
        && oldVariables.get(variableId).getRelatedQuestions() != null) {
      for (RelatedQuestion question : oldVariables.get(variableId).getRelatedQuestions()) {
        if (!oldIds.contains(question.getInstrumentId())) {
          oldIds.add(question.getInstrumentId());
        }
      }
    }
    if (newVariables.get(variableId) != null
        && newVariables.get(variableId).getRelatedQuestions() != null) {
      for (RelatedQuestion question : newVariables.get(variableId).getRelatedQuestions()) {
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
    if (oldVariables.get(variableId) != null
        && oldVariables.get(variableId).getRelatedQuestions() != null) {
      for (RelatedQuestion question : oldVariables.get(variableId).getRelatedQuestions()) {
        oldIds.add(question.getQuestionId());
      }
    }
    if (newVariables.get(variableId) != null
        && newVariables.get(variableId).getRelatedQuestions() != null) {
      for (RelatedQuestion question : newVariables.get(variableId).getRelatedQuestions()) {
        newIds.add(question.getQuestionId());
      }
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }
  
  protected void put(Variable newVariable, Variable oldVariable) {
    if (newVariable != null) {      
      newVariables.put(newVariable.getId(), newVariable);
    }
    if (oldVariable != null) {      
      oldVariables.put(oldVariable.getId(), oldVariable);
    }
  }
}
