package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Subset of question attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 * 
 * @author René Reitmann
 */
public interface QuestionSubDocumentProjection {

  String getId();

  String getDataAcquisitionProjectId();

  String getInstrumentId();

  Integer getInstrumentNumber();

  String getNumber();

  I18nString getQuestionText();

  I18nString getTopic();

}
