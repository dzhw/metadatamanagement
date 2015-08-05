package eu.dzhw.fdz.metadatamanagement.web.common;

import org.springframework.hateoas.Link;

/**
 * Base interface which defines the getters for the I18n links.
 * 
 * @author René Reitmann
 */
public interface InternationalizedResource {
  Link getGermanLink();

  Link getEnglishLink();
}
