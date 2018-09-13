package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Base class for all documents used as NESTED sub documents in elasticsearch. We currently use
 * these for nested aggregations showing the filter options.
 * 
 * @author René Reitmann
 */
public abstract class AbstractNestedSubDocument extends AbstractRdcDomainObject {
  public abstract I18nString getCompleteTitle();
}
