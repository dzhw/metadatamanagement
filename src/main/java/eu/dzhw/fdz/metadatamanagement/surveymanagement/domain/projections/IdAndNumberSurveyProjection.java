package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;

/**
 * Get only the survey id and the survey number of a survey.
 *
 * @author René Reitmann
 */
public interface IdAndNumberSurveyProjection 
    extends IdAndVersionProjection {
  Integer getNumber();
}
