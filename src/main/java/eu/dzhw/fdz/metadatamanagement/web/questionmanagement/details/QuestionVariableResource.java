package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details;

import org.springframework.hateoas.ResourceSupport;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionVariable;

/**
 *  This {@link QuestionVariableResource} wraps a {@link QuestionVariable}.
 *  
 * @author Amine limouri
 *
 */
public class QuestionVariableResource extends ResourceSupport{
  
  private QuestionVariable questionVariable;
  
  /**
   * @param questionVariable which will be wraped by this QuestionVariableResource.
   * 
   */
  public QuestionVariableResource(QuestionVariable questionVariable) {
    this.questionVariable = questionVariable;
  }

  /*
   * @return the questionVariable
   */
  public QuestionVariable getQuestionVariable() {
    return questionVariable;
  }
  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.hateoas.ResourceSupport#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), questionVariable);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.hateoas.ResourceSupport#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      if (!super.equals(object)) {
        return false;
      }
      QuestionVariableResource that = (QuestionVariableResource) object;
      return Objects.equal(this.questionVariable, that.questionVariable);
    }
    return false;
  }

}
