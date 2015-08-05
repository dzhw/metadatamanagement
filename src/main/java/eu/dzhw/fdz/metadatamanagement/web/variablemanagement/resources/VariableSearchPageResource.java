package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources;

import org.springframework.hateoas.PagedResources;

import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.VariableSearchController;

/**
 * Resource for the {@link VariableSearchController}.
 * 
 * @author René Reitmann
 */
public class VariableSearchPageResource extends NavigatablePageResource {

  private PagedResources<VariableResource> page;

  public VariableSearchPageResource(PagedResources<VariableResource> page) {
    super();
    this.page = page;
  }

  public PagedResources<VariableResource> getPage() {
    return this.page;
  }
}
