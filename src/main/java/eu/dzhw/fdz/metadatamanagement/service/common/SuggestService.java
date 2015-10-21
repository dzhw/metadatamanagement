package eu.dzhw.fdz.metadatamanagement.service.common;

import java.util.List;

/**
 * This interface is for the services which are offer suggest-as-you.typing.
 * 
 * @author Daniel Katzberg
 *
 */
public interface SuggestService {

  /**
   * Suggest search terms for the last word of the query string.
   * 
   * @param query the query string as given by the user containing multiple words
   * @return A list of suggested terms (the first words remain constant)
   */
  List<String> suggest(String query);

}
