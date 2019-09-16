package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.helper.QuestionShadowCopyDataSource;

/**
 * Service which generates shadow copies of all questions of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class QuestionShadowCopyService extends ShadowCopyHelper<Question> {
  public QuestionShadowCopyService(QuestionShadowCopyDataSource questionShadowCopyDataSource) {
    super(questionShadowCopyDataSource);
  }
}
