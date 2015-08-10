package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;


/**
 * Transform a VariableDocument Page into a PagedResources by creating the necessary PageMetadata.
 * 
 * @author Amine Limouri
 */
@Component
public class VariableResourceAssembler
    extends ResourceAssemblerSupport<VariableDocument, VariableResource> {

  public VariableResourceAssembler() {
    super(VariableDetailsController.class, VariableResource.class);
  }

  @Override
  public VariableResource toResource(VariableDocument variableDocument) {
    VariableResource resource;
    try {
      resource = createResourceWithId(
          UriUtils.encode(variableDocument.getId(), StandardCharsets.UTF_8.name()),
          variableDocument, LocaleContextHolder.getLocale().getLanguage());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      resource.add(linkTo(methodOn(VariableDetailsController.class, supportedLocale)
          .get(variableDocument.getId(), null)).withRel(supportedLocale.getLanguage()));
    }
    return resource;
  }

  @Override
  protected VariableResource instantiateResource(VariableDocument variableDocument) {
    return new VariableResource(variableDocument);
  }
}
