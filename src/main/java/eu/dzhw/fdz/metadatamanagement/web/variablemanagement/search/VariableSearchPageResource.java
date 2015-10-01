package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFilter;

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
   * @param variableSearchFormDto the data transfer object of the search form
   * @param pageable the pagerequest request param
   */
  public VariableSearchPageResource(PagedResources<VariableResource> page,
      Class<VariableSearchController> pageController, ControllerLinkBuilderFactory factory,
      VariableSearchFilter variableSearchFormDto, Pageable pageable) {
    super();
    this.page = page;
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(factory.linkTo(methodOn(pageController, supportedLocale).get(null,
          variableSearchFormDto, null, pageable, null)).withRel(supportedLocale.getLanguage()));
    }
    this.add(factory
        .linkTo(
            methodOn(pageController, LocaleContextHolder.getLocale().getLanguage()).suggest(null))
        .withRel("suggest"));
  }

  public PagedResources<VariableResource> getPage() {
    return this.page;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.hateoas.ResourceSupport#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), page);
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
      VariableSearchPageResource that = (VariableSearchPageResource) object;
      return Objects.equal(this.page, that.page);
    }
    return false;
  }

  public Link getSuggestLink() {
    return this.getLink("suggest");
  }

}
