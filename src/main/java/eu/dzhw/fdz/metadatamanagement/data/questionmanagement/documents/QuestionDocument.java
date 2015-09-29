package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.elasticsearch.annotations.Document;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * This is a representation of a question. All fields describe the attributes of the question, for
 * example the possible answers, the questionnaire or the study.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(
    indexName = "#{'" + AbstractDocument.METADATA_INDEX + "_'"
        + "+T(org.springframework.context.i18n.LocaleContextHolder).getLocale().getLanguage()}",
    type = "questions")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders")
public class QuestionDocument extends AbstractDocument {

  /**
   * The question survey. It has some information about the survey which has this question.
   */
  @Valid
  @NotNull
  private QuestionSurvey questionSurvey;

  /**
   * The question as a String.
   */
  @Size(max = 256)
  @NotBlank
  private String question;

  /**
   * A list of all variables which are depending to this question.
   */
  @Valid
  private List<VariableDocument> variableDocuments;

  /**
   * Creates a question with empty survey and list of variable documents.
   */
  public QuestionDocument() {
    super();
    this.questionSurvey = new QuestionSurvey();
    this.variableDocuments = new ArrayList<>();
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("super", super.toString())
        .add("questionSurvey", questionSurvey).add("question", question)
        .add("variableDocuments", variableDocuments).toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), questionSurvey, question, variableDocuments);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      if (!super.equals(object)) {
        return false;
      }  
      QuestionDocument that = (QuestionDocument) object;
      return Objects.equal(this.questionSurvey, that.questionSurvey)
          && Objects.equal(this.question, that.question)
          && Objects.equal(this.variableDocuments, that.variableDocuments);
    }
    return false;
  }
  
  /* GETTER / SETTER */
  public QuestionSurvey getQuestionSurvey() {
    return questionSurvey;
  }

  public void setQuestionSurvey(QuestionSurvey questionSurvey) {
    this.questionSurvey = questionSurvey;
  }

  public List<VariableDocument> getVariableDocuments() {
    return variableDocuments;
  }

  public void setVariableDocuments(List<VariableDocument> variableDocuments) {
    this.variableDocuments = variableDocuments;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }
}
