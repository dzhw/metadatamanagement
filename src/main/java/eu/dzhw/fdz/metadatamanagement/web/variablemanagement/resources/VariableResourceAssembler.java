package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.config.i18n.PathLocaleResolver;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.VariableSearchController;


/**
 * Transform a VariableDocument Page into a PagedResources by creating the necessary PageMetadata.
 * 
 * @author Amine Limouri
 */
@Component
public class VariableResourceAssembler extends
    ResourceAssemblerSupport<VariableDocument, VariableResource> {

  public VariableResourceAssembler() {
    super(VariableSearchController.class, VariableResource.class);
  }

  @Override
  public VariableResource toResource(VariableDocument variableDocument) {
    VariableResource resource =
        createResourceWithId(variableDocument.getName(), variableDocument,
            PathLocaleResolver.LOCALE_ATTRIBUTE);
    // resource.removeLinks();

    /*
     * resource.add(linkTo(methodOn(VariableSearchController.class).showVariableSearch()).withRel(
     * "edit"));
     */
    return resource;
  }

  @Override
  protected VariableResource instantiateResource(VariableDocument variableDocument) {
    return new VariableResource(variableDocument);
  }
}
