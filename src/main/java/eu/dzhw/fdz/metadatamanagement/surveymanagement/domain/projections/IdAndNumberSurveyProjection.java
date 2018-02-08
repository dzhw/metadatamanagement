package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections;

/**
 * Get only the survey id and the survey number of a survey.
 *
 * @author René Reitmann
 */
public interface IdAndNumberSurveyProjection {
  String getId();
  
  Integer getNumber();
}
