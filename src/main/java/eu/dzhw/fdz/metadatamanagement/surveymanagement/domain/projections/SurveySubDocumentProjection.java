package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Period;

/**
 * Subset of survey attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 * 
 * @author René Reitmann
 */
public interface SurveySubDocumentProjection {
  
  String getId();
  
  String getDataAcquisitionProjectId();
  
  I18nString getTitle();

  I18nString getPopulation();

  I18nString getSurveyMethod();

  Integer getNumber();
  
  Period getFieldPeriod();
  
  I18nString getSample();
  
  I18nString getAnnotations();

}
