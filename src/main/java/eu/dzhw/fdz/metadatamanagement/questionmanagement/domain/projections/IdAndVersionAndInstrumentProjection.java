package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections;

/**
 * Projection for returning a question with id and version and instrument id.
 * 
 * @author René Reitmann
 */
public interface IdAndVersionAndInstrumentProjection extends IdAndVersionQuestionProjection {
  String getInstrumentId();
  
  String getStudyId();
}
