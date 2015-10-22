package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details;

import org.springframework.hateoas.ResourceSupport;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.RelatedVariable;

/**
 * This {@link RelatedVariableResource} wraps a {@link RelatedVariable}.
 * 
 * @author Amine limouri
 *
 */
public class RelatedVariableResource extends ResourceSupport {

  /**
   * A reference to a question variable. A question variable is the reference to a variable from a
   * question.
   */
  private RelatedVariable relatedVariable;

  /**
   * @param relatedVariable which will be wraped by this RelatedVariableResource.
   * 
   */
  public RelatedVariableResource(RelatedVariable relatedVariable) {
    this.relatedVariable = relatedVariable;
  }

  /*
   * @return the relatedVariable
   */
  public RelatedVariable getRelatedVariable() {
    return relatedVariable;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.hateoas.ResourceSupport#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), relatedVariable);
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
      RelatedVariableResource that = (RelatedVariableResource) object;
      return Objects.equal(this.relatedVariable, that.relatedVariable);
    }
    return false;
  }

}
