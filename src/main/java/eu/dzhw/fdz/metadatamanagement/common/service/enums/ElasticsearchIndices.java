package eu.dzhw.fdz.metadatamanagement.common.service.enums;

/**
 * This enumerations holds all index names of the metadata management.
 * @author Daniel Katzberg
 *
 */
public enum ElasticsearchIndices {
  
  METADATA_DE("metadata_de"),
  METADATA_EN("metadata_en");
  
  /** Holds the correct index name of an elasticsearch index. */
  private String indexName;
  
  /**
   *  Constructor for the indices. 
   */
  private ElasticsearchIndices(String indexName) {
    this.indexName = indexName;
  }

  /* GETTER / SETTER */
  public String getIndexName() {
    return indexName;
  }
}
