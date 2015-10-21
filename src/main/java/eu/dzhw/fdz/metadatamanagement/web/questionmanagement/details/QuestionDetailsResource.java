package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;

/**
 * Resource holding the links for the details page of the question search results.
 * 
 * @author Daniel Katzberg
 *
 */
public class QuestionDetailsResource extends NavigatablePageResource<QuestionDetailsController> {

  private QuestionResource questionResource;

  /**
   * Create the details resource for the question details template with navigation and edit links.
   * 
   * @param controllerLinkBuilderFactory the {@link ControllerLinkBuilderFactory}
   * @param questionResource the document as resource
   */
  public QuestionDetailsResource(ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      QuestionResource questionResource) {
    super();
    this.questionResource = questionResource;
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(controllerLinkBuilderFactory
          .linkTo(methodOn(QuestionDetailsController.class, supportedLocale)
              .get(questionResource.getQuestionDocument().getId()))
          .withRel(supportedLocale.getLanguage()));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.hateoas.ResourceSupport#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), questionResource);
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
      QuestionDetailsResource that = (QuestionDetailsResource) object;
      return Objects.equal(this.questionResource, that.questionResource);
    }
    return false;
  }

  /* GETTER / SETTER */
  public QuestionResource getQuestionResource() {
    return questionResource;
  }

  public void setQuestionResource(QuestionResource questionResource) {
    this.questionResource = questionResource;
  }
}
