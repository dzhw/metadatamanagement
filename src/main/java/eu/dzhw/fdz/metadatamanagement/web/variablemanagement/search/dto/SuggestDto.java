package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto;

import java.util.List;

/**
 * Data Transfer Object for returning the list of suggestions to the browser.
 * 
 * @author René Reitmann
 */
public class SuggestDto {
  private List<String> suggestions;
  private boolean empty;

  public SuggestDto(List<String> suggestions) {
    this.suggestions = suggestions;
    this.empty = suggestions.isEmpty();
  }

  public List<String> getSuggestions() {
    return this.suggestions;
  }

  public boolean isEmpty() {
    return this.empty;
  }
}
