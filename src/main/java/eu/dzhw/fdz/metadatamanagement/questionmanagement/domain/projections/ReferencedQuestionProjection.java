package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;

/**
 * The 'referenced' Projection of a question domain object.
 * 'referenced' means only some attributes will be
 * displayed.
 */
@Projection(name = "referenced", types = Question.class)
public interface ReferencedQuestionProjection {
  String getId();
  
  String getNumber();
  
  I18nString getQuestionText();
}
