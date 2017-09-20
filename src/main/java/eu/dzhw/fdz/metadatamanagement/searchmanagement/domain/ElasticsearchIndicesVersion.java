package eu.dzhw.fdz.metadatamanagement.searchmanagement.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;

/**
 * MongoDB Document holding the current version of our elasticsearch indices.
 * The version is checked on container start and when they differ the container
 * will automatically recreate all elasticsearch indices.
 *  
 * @author René Reitmann
 */
@Document(collection = "elasticsearch_indices_version")
public class ElasticsearchIndicesVersion extends AbstractRdcDomainObject {

  //the version string of our elasticsearch indices
  private String id;

  public ElasticsearchIndicesVersion() {
    super();
  }
  
  public ElasticsearchIndicesVersion(String id) {
    super();
    this.id = id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  @Override
  public String getId() {
    return id;
  }
}
