package eu.dzhw.fdz.metadatamanagement.web.common;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.RelatedVariable;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableDetailsController;

/**
 * Convert a {@link RelatedVariable} into a {@link RelatedVariableResource}.
 * 
 * @author Amine limouri
 *
 */
@Component
public class RelatedVariableResourceAssembler
    extends ResourceAssemblerSupport<RelatedVariable, RelatedVariableResource> {


  public RelatedVariableResourceAssembler() {
    super(VariableDetailsController.class, RelatedVariableResource.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.hateoas.ResourceAssembler#toResource(java.lang.Object)
   */
  @Override
  public RelatedVariableResource toResource(RelatedVariable relatedVariable) {
    RelatedVariableResource resource;
    resource = createResourceWithId(relatedVariable.getId(), relatedVariable,
        LocaleContextHolder.getLocale().getLanguage());
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      resource.add(linkTo(
          methodOn(VariableDetailsController.class, supportedLocale).get(relatedVariable.getId()))
              .withRel(supportedLocale.getLanguage()));
    }
    return resource;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.hateoas.mvc.ResourceAssemblerSupport#instantiateResource(java.lang.Object)
   */
  @Override
  protected RelatedVariableResource instantiateResource(RelatedVariable relatedVariable) {
    return new RelatedVariableResource(relatedVariable);
  }
}
