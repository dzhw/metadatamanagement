package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

/**
 * The 'referenced' Projection of a survey domain object.
 * 'referenced' means only some attributes will be
 * displayed.
 */
@Projection(name = "referenced", types = Survey.class)
public interface ReferencedSurveyProjection {
  String getId();
  
  I18nString getTitle();
}
