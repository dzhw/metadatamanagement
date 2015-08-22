package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify.VariableEditController;


/**
 * Transform a {@link VariableDocument} into a {@link VariableResource}.
 * 
 * @author Amine Limouri
 */
@Component
public class VariableResourceAssembler
    extends ResourceAssemblerSupport<VariableDocument, VariableResource> {

  private ControllerLinkBuilderFactory factory;

  @Autowired
  public VariableResourceAssembler(ControllerLinkBuilderFactory factory) {
    super(VariableDetailsController.class, VariableResource.class);
    this.factory = factory;
  }

  @Override
  public VariableResource toResource(VariableDocument variableDocument) {
    VariableResource resource;
    resource = createResourceWithId(variableDocument.getId(), variableDocument,
        LocaleContextHolder.getLocale().getLanguage());
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      resource.add(linkTo(
          methodOn(VariableDetailsController.class, supportedLocale).get(variableDocument.getId()))
              .withRel(supportedLocale.getLanguage()));
    }
    resource.add(factory.linkTo(
        methodOn(VariableEditController.class, LocaleContextHolder.getLocale().getLanguage())
            .edit(resource.getVariableDocument().getId()))
        .withRel("edit"));
    return resource;
  }

  @Override
  protected VariableResource instantiateResource(VariableDocument variableDocument) {
    return new VariableResource(variableDocument);
  }
}
