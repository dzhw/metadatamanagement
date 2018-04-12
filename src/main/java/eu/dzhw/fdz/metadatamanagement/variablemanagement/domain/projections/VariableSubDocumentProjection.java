package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;

/**
 * Subset of variable attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 * 
 * @author René Reitmann
 */
public interface VariableSubDocumentProjection 
    extends AbstractRdcDomainObjectProjection {
  String getDataAcquisitionProjectId();
  
  String getName();

  I18nString getLabel();

  String getDataSetId();

  Integer getDataSetNumber();
}
