package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import java.util.stream.Stream;

/**
 * Types of documents stored in elasticsearch. For us: type == index. Since property mappings are
 * shared between all types in the index, we use one index per type.
 * 
 * @author René Reitmann
 */
public enum ElasticsearchType {
  variables, surveys, data_sets, questions, studies, related_publications, instruments;

  /**
   * Return the names of the types as array.
   * 
   * @return the strings of this enum
   */
  public static String[] names() {
    return Stream.of(ElasticsearchType.values())
      .map(ElasticsearchType::name)
      .toArray(String[]::new);
  }
}
