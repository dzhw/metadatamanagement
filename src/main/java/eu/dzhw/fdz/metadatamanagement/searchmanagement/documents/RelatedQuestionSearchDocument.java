package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;

/**
 * Representation of an related questions which is stored in elasticsearch.
 */
public class RelatedQuestionSearchDocument {
 
  
  private String questionId;
  
  private String questionNumber;
  
  private String instrumentId;
  
  private String instrumentNumber;

  private String relatedQuestionStrings;
   
  
  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public RelatedQuestionSearchDocument(RelatedQuestion relatedQuestion,
      ElasticsearchIndices index) {
    this.questionId = relatedQuestion.getQuestionId();
    this.questionNumber = relatedQuestion.getQuestionNumber();
    this.instrumentId = relatedQuestion.getInstrumentId();
    this.setInstrumentNumber(relatedQuestion.getInstrumentNumber());
    createI18nAttributes(relatedQuestion, index);
  }

  private void createI18nAttributes(RelatedQuestion relatedQuestion, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        setRelatedQuestionStrings(relatedQuestion.getRelatedQuestionStrings() != null
            ? relatedQuestion.getRelatedQuestionStrings().getDe() : null);
        break;
      case METADATA_EN:
        setRelatedQuestionStrings(relatedQuestion.getRelatedQuestionStrings() != null 
            ? relatedQuestion.getRelatedQuestionStrings().getEn() : null);
        break;
      default:
        throw new RuntimeException("Unknown index:" + index);
    }
  }

  public String getQuestionId() {
    return questionId;
  }

  public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }

  public String getQuestionNumber() {
    return questionNumber;
  }

  public void setQuestionNumber(String questionNumber) {
    this.questionNumber = questionNumber;
  }

  public String getInstrumentId() {
    return instrumentId;
  }

  public void setInstrumentId(String instrumentId) {
    this.instrumentId = instrumentId;
  }

  public String getInstrumentNumber() {
    return instrumentNumber;
  }

  public void setInstrumentNumber(String instrumentNumber) {
    this.instrumentNumber = instrumentNumber;
  }

  public String getRelatedQuestionStrings() {
    return relatedQuestionStrings;
  }

  public void setRelatedQuestionStrings(String relatedQuestionStrings) {
    this.relatedQuestionStrings = relatedQuestionStrings;
  }
}
