package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;

/**
 * The 'questionTextOnly' Projection of a question domain object.
 * 'questionTextOnly' means only some attributes will be
 * displayed.
 */
@Projection(name = "questionTextOnly", types = Question.class)
public interface QuestionSearchProjection {
  String getId();
  
  String getNumber();
  
  I18nString getQuestionText();
}
