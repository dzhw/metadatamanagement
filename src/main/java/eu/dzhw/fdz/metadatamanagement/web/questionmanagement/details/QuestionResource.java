package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details;

import org.springframework.hateoas.ResourceSupport;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;

/**
 * This {@link QuestionResource} wraps a {@link QuestionDocument}.
 * 
 * @author Daniel Katzberg
 *
 */
public class QuestionResource extends ResourceSupport {
  
  /**
   * A reference to a question document.
   */
  private QuestionDocument questionDocument;
  
  /**
   * @param questionDocument A QuestionDocument which will be wraped by this QuestionResource.
   */
  public QuestionResource(QuestionDocument questionDocument) {
    this.questionDocument = questionDocument;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.hateoas.ResourceSupport#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), questionDocument);
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.hateoas.ResourceSupport#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      if (!super.equals(object)) {
        return false;
      }
      QuestionResource that = (QuestionResource) object;
      return Objects.equal(this.questionDocument, that.questionDocument);
    }
    return false;
  }

  /* GETTER / SETTER */
  public QuestionDocument getQuestionDocument() {
    return questionDocument;
  }
}
