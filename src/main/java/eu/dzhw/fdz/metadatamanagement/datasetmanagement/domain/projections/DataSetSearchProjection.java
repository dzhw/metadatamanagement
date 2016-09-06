package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;

/**
 * The 'dataSetTextOnly' Projection of a dataSet domain object.
 * 'dataSetTextOnly' means only some attributes will be
 * displayed.
 */
@Projection(name = "dataSetTextOnly", types = DataSet.class)
public interface DataSetSearchProjection {
  String getId();
  
  I18nString getDescription();
}
