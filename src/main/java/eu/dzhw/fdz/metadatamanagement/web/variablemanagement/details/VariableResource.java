package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import org.springframework.hateoas.ResourceSupport;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * Build and Wrap a {@link VariableDocument} resource from a {@link VariableDocument} object.
 * 
 * @author Amine Limouri
 */
public class VariableResource extends ResourceSupport {

  private VariableDocument variableDocument;

  /**
   * Create a resource from the given {@link VariableDocument}.
   * 
   * @param variableDocument A {@link VariableDocument}
   */
  public VariableResource(VariableDocument variableDocument) {
    this.variableDocument = variableDocument;
  }

  public
          VariableDocument getVariableDocument() {
    return variableDocument;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.hateoas.ResourceSupport#hashCode()
   */
  @Override
  public
          int hashCode() {
    return Objects.hashCode(super.hashCode(), variableDocument);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.hateoas.ResourceSupport#equals(java.lang.Object)
   */
  @Override
  public
          boolean equals(
                  Object object) {
    if (object != null && getClass() == object.getClass()) {
      if (!super.equals(object)) {
        return false;
      }
      VariableResource that = (VariableResource) object;
      return Objects.equal(this.variableDocument, that.variableDocument);
    }
    return false;
  }


}
