package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;

/**
 * The 'referenced' Projection of a dataSet domain object.
 * 'referenced' means only some attributes will be
 * displayed.
 */
@Projection(name = "referenced", types = DataSet.class)
public interface ReferencedDataSetProjection {
  String getId();
  
  I18nString getDescription();
}
