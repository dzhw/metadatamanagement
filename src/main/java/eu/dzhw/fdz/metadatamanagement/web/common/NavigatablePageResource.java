package eu.dzhw.fdz.metadatamanagement.web.common;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.VariableSearchController;
import eu.dzhw.fdz.metadatamanagement.web.welcome.WelcomeController;

/**
 * Base resource fall all pages. Adds default links for the navigation bar and provides getters for
 * the I18n links.
 * 
 * @param <T> the page controller
 * @author René Reitmann
 */
public abstract class NavigatablePageResource<T> extends ResourceSupport
    implements InternationalizedResource {

  public static final String VARIABLES_REL = "variables";
  public static final String HOME_REL = "home";
  public static final String GERMAN_REL = Locale.GERMAN.getLanguage();
  public static final String ENGLISH_REL = Locale.ENGLISH.getLanguage();

  /**
   * Create the page resource and add static navigation bar links.
   */
  public NavigatablePageResource() {
    this.add(linkTo(
        methodOn(WelcomeController.class, LocaleContextHolder.getLocale().getLanguage()).get())
            .withRel(HOME_REL));
    this.add(linkTo(
        methodOn(VariableSearchController.class, LocaleContextHolder.getLocale().getLanguage())
            .get(null, null)).withRel(VARIABLES_REL));
  }

  public abstract void addInternationalizationLinks(Class<T> pageController,
      ControllerLinkBuilderFactory factory, Object... params);

  public Link getHomeLink() {
    return this.getLink(HOME_REL);
  }

  public Link getVariablesLink() {
    return this.getLink(VARIABLES_REL);
  }

  @Override
  public Link getEnglishLink() {
    return this.getLink(ENGLISH_REL);
  }

  @Override
  public Link getGermanLink() {
    return this.getLink(GERMAN_REL);
  }
}
