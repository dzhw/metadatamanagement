package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Tags;

/**
 * Subset of concept attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 * 
 * @author René Reitmann
 */
public interface ConceptSubDocumentProjection 
    extends AbstractRdcDomainObjectProjection {
  I18nString getTitle();
  
  Tags getTags();
  
  String getDoi();
  
  List<Person> getAuthors();
}