package eu.dzhw.fdz.metadatamanagement.domain;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.domain.validation.ValidAtomicQuestionType;
import eu.dzhw.fdz.metadatamanagement.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.domain.validation.ValidIdName;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Atomic Question.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "atomic_questions")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
@CompoundIndex(def = "{name: 1, questionnaireId: 1}", unique = true)
@ValidIdName(message = "{error.atomicQuestion.validIdName}")
public class AtomicQuestion extends AbstractRdcDomainObject {

  /* Domain model attributes */
  @Id
  @NotEmpty(message = "{error.atomicQuestion.id.isEmpty}")
  @Size(max = StringLengths.MEDIUM, message = "{error.atomicQuestion.id.size}")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, 
      message = "{error.atomicQuestion.id.pattern}")
  private String id;

  @ValidAtomicQuestionType(message = 
      "{error.atomicQuestion.type.atomicQuestionTypeConsistence}")
  private I18nString type;

  @NotEmpty(message = "{error.atomicQuestion.name.isEmpty}")
  @Size(max = StringLengths.SMALL, message = "{error.atomicQuestion.name.size}")
  private String name;

  @NotEmpty(message = "{error.atomicQuestion.compositeQuestionName.isEmpty}")
  @Size(max = StringLengths.SMALL, message = "{error.atomicQuestion.compositeQuestionName.size}")
  private String compositeQuestionName;

  @I18nStringSize(max = StringLengths.LARGE, message = "{error.atomicQuestion.footnote.size}")
  private I18nString footnote;

  @I18nStringSize(max = StringLengths.LARGE, message = "{error.atomicQuestion.questionText.size}")
  private I18nString questionText;

  @I18nStringSize(max = StringLengths.LARGE, message = "{error.atomicQuestion.instruction.size}")
  private I18nString instruction;

  @I18nStringSize(max = StringLengths.LARGE, message = "{error.atomicQuestion.introduction.size}")
  private I18nString introduction;

  @I18nStringSize(max = StringLengths.MEDIUM, message = "{error.atomicQuestion.sectionHeader.size}")
  private I18nString sectionHeader;

  
  /* Foreign Keys */
  @NotEmpty(message = "{error.dataAcquisitionProject.id.isEmpty}")  
  private String dataAcquisitionProjectId;

  @NotEmpty(message = "{error.questionnaire.id.isEmpty}")
  private String questionnaireId;

  @NotEmpty(message = "{error.variable.id.isEmpty}")
  private String variableId;

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return this.id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("type", type)
      .add("name", name)
      .add("compositeQuestionName", compositeQuestionName)
      .add("footnote", footnote)
      .add("questionText", questionText)
      .add("instruction", instruction)
      .add("introduction", introduction)
      .add("sectionHeader", sectionHeader)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .add("questionnaireId", questionnaireId)
      .add("variableId", variableId)
      .toString();
  }

  /* GETTER / SETTER */
  public I18nString getType() {
    return type;
  }

  public void setType(I18nString type) {
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

  public I18nString getFootnote() {
    return footnote;
  }

  public void setFootnote(I18nString footnote) {
    this.footnote = footnote;
  }

  public I18nString getQuestionText() {
    return questionText;
  }

  public void setQuestionText(I18nString questionText) {
    this.questionText = questionText;
  }

  public I18nString getInstruction() {
    return instruction;
  }

  public void setInstruction(I18nString instruction) {
    this.instruction = instruction;
  }

  public I18nString getIntroduction() {
    return introduction;
  }

  public void setIntroduction(I18nString introduction) {
    this.introduction = introduction;
  }

  public I18nString getSectionHeader() {
    return sectionHeader;
  }

  public void setSectionHeader(I18nString sectionHeader) {
    this.sectionHeader = sectionHeader;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public String getQuestionnaireId() {
    return questionnaireId;
  }

  public void setQuestionnaireId(String questionnaireId) {
    this.questionnaireId = questionnaireId;
  }

  public String getVariableId() {
    return variableId;
  }

  public void setVariableId(String variableId) {
    this.variableId = variableId;
  }

  public void setId(String id) {
    this.id = id;
  }
}
