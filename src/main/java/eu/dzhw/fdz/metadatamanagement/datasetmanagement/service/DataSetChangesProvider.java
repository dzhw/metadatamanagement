package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.common.service.DomainObjectChangesProvider;
import eu.dzhw.fdz.metadatamanagement.common.service.util.ListUtils;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;

/**
 * Remember the previous version of a data set per request in order to update elasticsearch
 * correctly.
 * 
 * @author René Reitmann
 */
@Component
@RequestScope
public class DataSetChangesProvider extends DomainObjectChangesProvider<DataSet> {

  /**
   * Get the list of surveyIds which need to be updated.
   * 
   * @return a list of surveyIds
   */
  public List<String> getAffectedSurveyIds(String dataSetId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldDomainObjects.get(dataSetId) != null) {
      oldIds = oldDomainObjects.get(dataSetId).getSurveyIds();
    }
    if (newDomainObjects.get(dataSetId) != null) {
      newIds = newDomainObjects.get(dataSetId).getSurveyIds();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }
}
