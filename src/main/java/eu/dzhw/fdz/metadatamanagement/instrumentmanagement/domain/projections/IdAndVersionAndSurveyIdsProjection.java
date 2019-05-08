package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections;

import java.util.List;

/**
 * Projection for returning an instrument with id and version and survey ids.
 * 
 * @author René Reitmann
 */
public interface IdAndVersionAndSurveyIdsProjection extends IdAndVersionInstrumentProjection {
  List<String> getSurveyIds();
  
  String getStudyId();
}
