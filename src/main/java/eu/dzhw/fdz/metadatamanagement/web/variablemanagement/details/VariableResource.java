package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import org.springframework.hateoas.ResourceSupport;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * Build and Wrap a VariableDocument resource from a VariableDocument object.
 * 
 * @author Amine Limouri
 */
public class VariableResource extends ResourceSupport {

  private VariableDocument variableDocument;

  public VariableResource(VariableDocument variableDocument) {
    this.variableDocument = variableDocument;
  }

  public VariableDocument getVariableDocument() {
    return variableDocument;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result =
        prime * result + ((this.variableDocument == null) ? 0 : this.variableDocument.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    VariableResource other = (VariableResource) obj;
    if (this.variableDocument == null) {
      if (other.variableDocument != null) {
        return false;
      }
    } else if (!this.variableDocument.equals(other.variableDocument)) {
      return false;
    }
    return true;
  }
}
