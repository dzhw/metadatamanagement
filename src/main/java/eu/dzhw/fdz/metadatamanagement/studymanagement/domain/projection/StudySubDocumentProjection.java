package eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;

/**
 * Subset of study attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 *
 * @author René Reitmann
 */
public interface StudySubDocumentProjection
    extends AbstractRdcDomainObjectProjection {
  String getDataAcquisitionProjectId();

  I18nString getStudySeries();

  I18nString getTitle();

  List<I18nString> getInstitutions();

  I18nString getSponsor();

  List<Person> getProjectContributors();

  I18nString getSurveyDesign();

  I18nString getDataAvailability();

  String getMasterId();

  String getSuccessorId();

  boolean isShadow();
}
