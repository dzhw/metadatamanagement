package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.AtomicQuestion;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import io.searchbox.annotations.JestId;

/**
 * Representation of an atomic question which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
public class AtomicQuestionSearchDocument {
  @JestId
  private String id;

  private String type;

  private String name;

  private String compositeQuestionName;

  private String footnote;

  private String questionText;

  private String instruction;

  private String introduction;

  private String sectionHeader;

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public AtomicQuestionSearchDocument(AtomicQuestion question, ElasticsearchIndices index) {
    this.id = question.getId();
    this.name = question.getName();
    this.compositeQuestionName = question.getCompositeQuestionName();
    createI18nAttributes(question, index);
  }

  private void createI18nAttributes(AtomicQuestion question, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        type = question.getType() != null ? question.getType()
          .getDe() : null;
        footnote = question.getFootnote() != null ? question.getFootnote()
          .getDe() : null;
        questionText = question.getQuestionText() != null ? question.getQuestionText()
          .getDe() : null;
        instruction = question.getInstruction() != null ? question.getInstruction()
          .getDe() : null;
        introduction = question.getIntroduction() != null ? question.getIntroduction()
          .getDe() : null;
        sectionHeader = question.getSectionHeader() != null ? question.getSectionHeader()
          .getDe() : null;
        break;
      case METADATA_EN:
        type = question.getType() != null ? question.getType()
          .getEn() : null;
        footnote = question.getFootnote() != null ? question.getFootnote()
          .getEn() : null;
        questionText = question.getQuestionText() != null ? question.getQuestionText()
          .getEn() : null;
        instruction = question.getInstruction() != null ? question.getInstruction()
          .getEn() : null;
        introduction = question.getIntroduction() != null ? question.getIntroduction()
          .getEn() : null;
        sectionHeader = question.getSectionHeader() != null ? question.getSectionHeader()
          .getEn() : null;
        break;
      default:
        throw new RuntimeException("Unknown index:" + index);
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCompositeQuestionName() {
    return compositeQuestionName;
  }

  public void setCompositeQuestionName(String compositeQuestionName) {
    this.compositeQuestionName = compositeQuestionName;
  }

  public String getFootnote() {
    return footnote;
  }

  public void setFootnote(String footnote) {
    this.footnote = footnote;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public String getInstruction() {
    return instruction;
  }

  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }

  public String getIntroduction() {
    return introduction;
  }

  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }

  public String getSectionHeader() {
    return sectionHeader;
  }

  public void setSectionHeader(String sectionHeader) {
    this.sectionHeader = sectionHeader;
  }
}
