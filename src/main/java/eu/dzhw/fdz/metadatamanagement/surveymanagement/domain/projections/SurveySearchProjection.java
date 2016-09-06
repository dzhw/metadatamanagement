package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

/**
 * The 'surveyTextOnly' Projection of a survey domain object.
 * 'surveyTextOnly' means only some attributes will be
 * displayed.
 */
@Projection(name = "surveyTextOnly", types = Survey.class)
public interface SurveySearchProjection {
  String getId();
  
  I18nString getTitle();
}
