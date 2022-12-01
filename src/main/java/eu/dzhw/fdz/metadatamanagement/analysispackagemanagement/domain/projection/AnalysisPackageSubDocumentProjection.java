package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.projection;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.Sponsor;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;

/**
 * Subset of analysis package attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 *
 * @author René Reitmann
 */
public interface AnalysisPackageSubDocumentProjection
    extends AbstractRdcDomainObjectProjection {
  String getDataAcquisitionProjectId();

  I18nString getTitle();

  List<I18nString> getInstitutions();

  List<Sponsor> getSponsors();

  List<Person> getAuthors();

  String getMasterId();

  String getSuccessorId();

  boolean isShadow();
}
