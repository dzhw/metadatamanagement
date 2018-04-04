package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.common.service.util.ListUtils;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;

/**
 * Remember the previous version of a data set per request
 * in order to update elasticsearch correctly.
 *  
 * @author René Reitmann
 */
@Component
@RequestScope
public class DataSetChangesProvider {
  
  private Map<String, DataSet> oldDataSets = new HashMap<>();
  private Map<String, DataSet> newDataSets = new HashMap<>();
  
  /**
   * Get the list of surveyIds which need to be updated.
   * @return a list of surveyIds
   */
  public List<String> getAffectedSurveyIds(String dataSetId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldDataSets.get(dataSetId) != null) {
      oldIds = oldDataSets.get(dataSetId).getSurveyIds();
    }
    if (newDataSets.get(dataSetId) != null) {
      newIds = newDataSets.get(dataSetId).getSurveyIds();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }
  
  protected void put(DataSet newDataSet, DataSet oldDataSet) {
    if (newDataSet != null) {      
      newDataSets.put(newDataSet.getId(), newDataSet);
    }
    if (oldDataSet != null) {      
      oldDataSets.put(oldDataSet.getId(), oldDataSet);
    }
  }
}
