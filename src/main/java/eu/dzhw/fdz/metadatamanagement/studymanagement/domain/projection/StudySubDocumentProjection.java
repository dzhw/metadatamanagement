package eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;

/**
 * Subset of study attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 * 
 * @author René Reitmann
 */
public interface StudySubDocumentProjection {
  String getId();

  String getDataAcquisitionProjectId();

  I18nString getSurveySeries();

  I18nString getTitle();

  I18nString getDescription();

  I18nString getInstitution();

  I18nString getSponsor();

  List<Person> getAuthors();
  
  String getDoi();
}
