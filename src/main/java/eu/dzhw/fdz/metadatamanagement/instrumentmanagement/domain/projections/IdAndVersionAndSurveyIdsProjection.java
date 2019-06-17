package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;

/**
 * Projection for returning an instrument with id and version and survey ids.
 * 
 * @author René Reitmann
 */
public interface IdAndVersionAndSurveyIdsProjection extends IdAndVersionProjection {
  List<String> getSurveyIds();
  
  String getStudyId();
}
