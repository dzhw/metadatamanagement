package eu.dzhw.fdz.metadatamanagement.web.common.exceptions;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.web.common.NavigatablePageResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify.VariableEditController;
import eu.dzhw.fdz.metadatamanagement.web.welcome.WelcomeController;

/**
 * The Resource handles the {@link DocumentNotFoundException}. An id is part of the url at the
 * detail and edit page. If an id is unknown, the url is not valid. In this case throws the
 * DocumentNotFoundException and uses this resource for a document not found error page. *
 * 
 * @author Daniel Katzberg
 */
public class DocumentNotFoundResource extends NavigatablePageResource<VariableEditController> {

  /**
   * The id of the not found document.
   */
  private String unknownId;

  /**
   * Constructor of the DocumentNotFoundResource.
   * 
   * @param unknownId The id of the not found element.
   */
  public DocumentNotFoundResource(String unknownId, Class<WelcomeController> pageController,
      ControllerLinkBuilderFactory factory) {
    super();
    this.unknownId = unknownId;

    // TODO DKatzberg: Use WelcomeController -> Change!! Own get Method in this Handler or separated
    // Controller
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(factory.linkTo(methodOn(pageController, supportedLocale).get())
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
    return Objects.hashCode(super.hashCode(), unknownId);
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
      DocumentNotFoundResource that = (DocumentNotFoundResource) object;
      return Objects.equal(this.unknownId, that.unknownId);
    }
    return false;
  }

  /* GETTER / SETTER */
  public String getUnknownId() {
    return this.unknownId;
  }
}
