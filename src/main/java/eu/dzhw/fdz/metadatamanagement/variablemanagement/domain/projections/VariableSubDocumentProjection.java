package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Subset of variable attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 * 
 * @author René Reitmann
 */
public interface VariableSubDocumentProjection {
  
  String getId();
  
  String getDataAcquisitionProjectId();
  
  String getName();

  I18nString getLabel();

  I18nString getAnnotations();

  String getDataSetId();

  Integer getDataSetNumber();
}
