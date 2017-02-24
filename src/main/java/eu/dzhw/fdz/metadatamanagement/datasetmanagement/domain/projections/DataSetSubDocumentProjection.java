package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Subset of data set attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 * 
 * @author René Reitmann
 */
public interface DataSetSubDocumentProjection {

  String getId();

  String getDataAcquisitionProjectId();

  I18nString getDescription();

  Integer getNumber();

  I18nString getFormat();

}
