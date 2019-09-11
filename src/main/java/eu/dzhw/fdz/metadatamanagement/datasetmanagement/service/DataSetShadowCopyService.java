package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper.DataSetShadowCopyDataSource;

/**
 * Service which generates shadow copies of all dataSets of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class DataSetShadowCopyService extends ShadowCopyHelper<DataSet> {
  public DataSetShadowCopyService(DataSetShadowCopyDataSource dataSetShadowCopyDataSource) {
    super(dataSetShadowCopyDataSource);
  }
}
