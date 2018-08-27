package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Base class for all documents used as sub documents in elasticsearch.
 * 
 * @author René Reitmann
 */
public abstract class AbstractSubDocument extends AbstractRdcDomainObject {
  public abstract I18nString getCompleteTitle();
}
