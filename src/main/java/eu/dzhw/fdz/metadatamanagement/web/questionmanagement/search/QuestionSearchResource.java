package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.search;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details.QuestionResource;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.search.dto.QuestionSearchFilter;

/**
 * Resource for the {@link QuestionSearchController}.
 * 
 * @author Daniel Katzberg
 *
 */
public class QuestionSearchResource extends NavigatablePageResource<QuestionSearchController> {

  private PagedResources<QuestionResource> page;

  /**
   * Create the resource with navigation and i18n links.
   * 
   * @param page The page of variable resources
   * @param pageController The controller for this resource
   * @param factory The {@link ControllerLinkBuilderFactory}
   * @param pageable the page request request parameter
   */
  public QuestionSearchResource(PagedResources<QuestionResource> page,
      Class<QuestionSearchController> pageController, ControllerLinkBuilderFactory factory, 
      QuestionSearchFilter questionSearchFilter, Pageable pageable) {
    super();
    this.page = page;
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(factory.linkTo(methodOn(pageController, supportedLocale).get(null,
          questionSearchFilter, pageable, null)).withRel(supportedLocale.getLanguage()));
    }
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.hateoas.ResourceSupport#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), page);
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
      QuestionSearchResource that = (QuestionSearchResource) object;
      return Objects.equal(this.page, that.page);
    }
    return false;
  }

  /* GETTER / SETTER */
  public PagedResources<QuestionResource> getPage() {
    return page;
  }
  
}
