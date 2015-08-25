package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;

/**
 * Resource holding the links for the edit version of the modify page.
 * 
 * @author René Reitmann
 */
public class VariableEditResource extends AbstractVariableModifyResource {

  /**
   * Create the create resource for the variable modify template with self link and validate link as
   * well as the navigation links.
   * 
   * @param factory the {@link ControllerLinkBuilderFactory}
   */
  public VariableEditResource(ControllerLinkBuilderFactory factory, String variableId) {
    super(factory, Optional.of(variableId));
    // create links for edit mode
    this.add(
        factory
            .linkTo(methodOn(VariableEditController.class,
                LocaleContextHolder.getLocale().getLanguage()).edit(variableId))
        .withRel(Link.REL_SELF));
    this.add(factory
        .linkTo(methodOn(VariableEditController.class,
            LocaleContextHolder.getLocale().getLanguage(), variableId).save(null, null, null))
        .withRel("save"));
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      this.add(
          factory.linkTo(methodOn(VariableEditController.class, supportedLocale).edit(variableId))
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
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify.AbstractVariableModifyResource#
   * addValidateLink(org.springframework.hateoas.mvc.ControllerLinkBuilderFactory)
   */
  @Override
  protected void addValidateLink(ControllerLinkBuilderFactory factory,
      Optional<String> variableId) {
    this.add(factory.linkTo(methodOn(VariableEditController.class,
        LocaleContextHolder.getLocale().getLanguage(), variableId.get()).validate(null, null, null))
        .withRel("validate"));
  }
}
