package eu.dzhw.fdz.metadatamanagement.questionmanagementold.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.questionmanagementold.domain.validation.UniqueAtomicQuestionName;
import eu.dzhw.fdz.metadatamanagement.questionmanagementold.domain.validation.ValidAtomicQuestionIdName;
import eu.dzhw.fdz.metadatamanagement.questionmanagementold.domain.validation.ValidAtomicQuestionType;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Atomic Question.
 *
 * @author Daniel Katzberg
 *
 */
@Document(collection = "atomic_questions")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.builders")
@CompoundIndex(def = "{name: 1, questionnaireId: 1}", unique = true)
@ValidAtomicQuestionIdName(
    message = "question-management.error.question.valid-atomic-question-id-name")
@UniqueAtomicQuestionName(message = "question-management.error.question.unique-question-name")
public class AtomicQuestion extends AbstractRdcDomainObject {

  /* Domain model attributes */
  @Id
  @NotEmpty(message = "question-management.error.question.id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "question-management.error.question.id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "question-management.error.question.id.pattern")
  private String id;

  @NotNull(message = "question-management.error.question.type.not-null")
  @ValidAtomicQuestionType(message =
      "question-management.error.question.type.question-type-consistence")
  private I18nString type;

  @NotEmpty(message = "question-management.error.question.name.not-empty")
  @Size(max = StringLengths.SMALL, message = "question-management.error.question.name.size")
  private String name;

  @NotEmpty(message = "question-management.error.questioncomposite-question-namenot-empty")
  @Size(max = StringLengths.SMALL,
      message = "question-management.error.questioncomposite-question-namesize")
  private String compositeQuestionName;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.question.footnote.i18n-string-size")
  private I18nString footnote;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.question.question-text.i18n-string-size")
  private I18nString questionText;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.question.instruction.i18n-string-size")
  private I18nString instruction;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.question.introduction.i18n-string-size")
  private I18nString introduction;

  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "question-management.error.question.section-header.i18n-string-size")
  private I18nString sectionHeader;


  /* Foreign Keys */
  @Indexed
  @NotEmpty(message = "question-management.error.question.dataAcquisitionProject.id.not-empty")
  private String dataAcquisitionProjectId;

  @NotEmpty(message = "question-management.error.question.questionnaire.id.not-empty")
  private String questionnaireId;

  @NotEmpty(message = "question-management.error.question.variable.id.not-empty")
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
