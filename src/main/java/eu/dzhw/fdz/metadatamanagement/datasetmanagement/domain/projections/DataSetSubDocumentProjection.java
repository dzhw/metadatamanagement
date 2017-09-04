package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;

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
  
  I18nString getType();

  Integer getNumber();

  I18nString getFormat();
  
  List<SubDataSet> getSubDataSets();
}
