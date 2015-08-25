package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;

/**
 * The create resource for the variable modify template.
 */
public class VariableCreateResource extends AbstractVariableModifyResource {

  /**
   * Create the create resource for the variable modify template with self link and validate link as
   * well as the navigation links.
   * 
   * @param factory the {@link ControllerLinkBuilderFactory}
   */
  public VariableCreateResource(ControllerLinkBuilderFactory factory) {
    super(factory, Optional.empty());
    // create links for create mode
    this.add(
        factory
            .linkTo(methodOn(VariableCreateController.class,
                LocaleContextHolder.getLocale().getLanguage()).create(null, null))
        .withRel(Link.REL_SELF));
    this.add(factory.linkTo(
        methodOn(VariableCreateController.class, LocaleContextHolder.getLocale().getLanguage())
            .save(null, null))
        .withRel("save"));
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(factory
          .linkTo(methodOn(VariableCreateController.class, supportedLocale).create(null, null))
          .withRel(supportedLocale.getLanguage()));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify.VariableModifyResource#
   * isCreateMode()
   */
  @Override
  public boolean isCreateMode() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify.AbstractVariableModifyResource#
   * addValidateLink(org.springframework.hateoas.mvc.ControllerLinkBuilderFactory,
   * java.util.Optional)
   */
  @Override
  protected void addValidateLink(ControllerLinkBuilderFactory factory,
      Optional<String> variableId) {
    this.add(
        factory
            .linkTo(methodOn(VariableCreateController.class,
                LocaleContextHolder.getLocale().getLanguage()).validate(null, null, null))
        .withRel("validate"));
  }
}
