package eu.dzhw.fdz.metadatamanagement.searchmanagement.domain;

/**
 * Actions for {@link ElasticsearchUpdateQueueItem}s.
 * 
 * @author René Reitmann
 */
public enum ElasticsearchUpdateQueueAction {
  //update or insert the document
  UPSERT, 
  //delete the document
  DELETE
}
