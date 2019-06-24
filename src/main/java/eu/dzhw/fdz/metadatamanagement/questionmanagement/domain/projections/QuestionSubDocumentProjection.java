package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;

/**
 * Subset of question attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 * 
 * @author René Reitmann
 */
public interface QuestionSubDocumentProjection 
    extends AbstractRdcDomainObjectProjection {
  String getDataAcquisitionProjectId();

  String getInstrumentId();

  Integer getInstrumentNumber();

  String getNumber();

  I18nString getQuestionText();

  I18nString getTopic();

  String getStudyId();
  
  List<String> getConceptIds();

  String getMasterId();
  
  String getSuccessorId();
  
  boolean isShadow();
}
