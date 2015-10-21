package eu.dzhw.fdz.metadatamanagement.web.common;

import org.springframework.hateoas.Link;

/**
 * Base interface which defines the getters for the I18n links.
 * 
 * @author René Reitmann
 */
public interface InternationalizedResource {
  
  /**
   * @return The url of the german version of a page.
   */
  Link getGermanLink();

  /**
   * @return The url of the english version of a page.
   */
  Link getEnglishLink();
}
