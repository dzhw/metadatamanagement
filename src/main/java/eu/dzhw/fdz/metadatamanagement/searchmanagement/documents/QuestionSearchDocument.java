package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;

/**
 * Representation of an question which is stored in elasticsearch.
 */
public class QuestionSearchDocument extends Question {
  
  public QuestionSearchDocument(Question question) {
    super(question);
  }
}
