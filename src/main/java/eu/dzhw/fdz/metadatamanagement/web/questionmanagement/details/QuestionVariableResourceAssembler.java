package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionVariable;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableDetailsController;

/**
 * Convert a {@link QuestionVariable} into a {@link QuestionVariableResource}.
 * 
 * @author Amine limouri
 *
 */
@Component
public class QuestionVariableResourceAssembler extends
    ResourceAssemblerSupport<QuestionVariable, QuestionVariableResource> {


  public QuestionVariableResourceAssembler() {
    super(VariableDetailsController.class, QuestionVariableResource.class);
  }

  @Override
  public QuestionVariableResource toResource(QuestionVariable questionVariable) {
    QuestionVariableResource resource;
    resource =
        createResourceWithId(questionVariable.getId(), questionVariable, LocaleContextHolder
            .getLocale().getLanguage());
    for (Locale supportedLocale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      resource.add(linkTo(
          methodOn(VariableDetailsController.class, supportedLocale).get(questionVariable.getId()))
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
  protected QuestionVariableResource instantiateResource(QuestionVariable questionVariable) {
    return new QuestionVariableResource(questionVariable);
  }
}
