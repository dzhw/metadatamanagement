package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.data.domain.PageRequest;
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

  public VariableSearchPageResource(PagedResources<VariableResource> page) {
    super();
    this.page = page;
  }

  public PagedResources<VariableResource> getPage() {
    return this.page;
  }

  @Override
  public void addInternationalizationLinks(Class<VariableSearchController> pageController,
      ControllerLinkBuilderFactory factory, Object... params) {
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(factory.linkTo(methodOn(pageController, supportedLocale).get((String) params[0],
          (PageRequest) params[1])).withRel(supportedLocale.getLanguage()));
    }

  }
}
