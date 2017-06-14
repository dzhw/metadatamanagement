package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Subset of instrument attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 * 
 * @author René Reitmann
 */
public interface InstrumentSubDocumentProjection  {
  
  String getId();
  
  String getDataAcquisitionProjectId();
  
  I18nString getTitle();
  
  I18nString getSubtitle();

  I18nString getDescription();

  Integer getNumber();

  List<String> getSurveyIds();
  
  I18nString getAnnotations();
}
