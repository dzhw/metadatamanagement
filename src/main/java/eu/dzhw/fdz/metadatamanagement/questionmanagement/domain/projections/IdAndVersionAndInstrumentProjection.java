package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;

/**
 * Projection for returning a question with id and version and instrument id.
 * 
 * @author René Reitmann
 */
public interface IdAndVersionAndInstrumentProjection extends IdAndVersionProjection {
  String getInstrumentId();
  
  String getStudyId();
}
