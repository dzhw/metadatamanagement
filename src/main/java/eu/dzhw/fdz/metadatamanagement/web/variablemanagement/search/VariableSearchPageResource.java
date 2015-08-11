package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableResource;

/**
 * Resource for the {@link VariableSearchController}.
 * 
 * @author René Reitmann
 */
public class VariableSearchPageResource extends NavigatablePageResource<VariableSearchController> {

  private PagedResources<VariableResource> page;

  /**
   * Create the resource with navigation and i18n links.
   * 
   * @param page The page of variable resources
   * @param pageController The controller for this resource
   * @param factory The {@link ControllerLinkBuilderFactory}
   * @param query the query request param
   * @param pageable the pagerequest request param
   */
  public VariableSearchPageResource(PagedResources<VariableResource> page,
      Class<VariableSearchController> pageController, ControllerLinkBuilderFactory factory,
      String query, Pageable pageable) {
    super();
    this.page = page;
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(factory.linkTo(methodOn(pageController, supportedLocale).get(query, pageable))
          .withRel(supportedLocale.getLanguage()));
    }
  }

  public PagedResources<VariableResource> getPage() {
    return this.page;
  }
}
