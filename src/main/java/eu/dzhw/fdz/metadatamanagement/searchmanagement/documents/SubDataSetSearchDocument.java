package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import io.searchbox.annotations.JestId;

/**
 * Representation of a subDataSet which is stored in elasticsearch.
 *
 */
public class SubDataSetSearchDocument {
  @JestId
  private String name;
  
  private String description;
  
  private String accessWay;
  
  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public SubDataSetSearchDocument(SubDataSet subDataSet, ElasticsearchIndices index) {
    this.name = subDataSet.getName();
    this.accessWay = subDataSet.getAccessWay();
    createI18nAttributes(subDataSet, index);
  }

  private void createI18nAttributes(SubDataSet subDataSet, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        setDescription(subDataSet.getDescription() != null ? subDataSet.getDescription()
            .getDe() : null);
        break;
      case METADATA_EN:
        setDescription(subDataSet.getDescription() != null ? subDataSet.getDescription()
            .getEn() : null);
        break;
      default:
        throw new RuntimeException("Unknown index:" + index);
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAccessWay() {
    return accessWay;
  }

  public void setAccessWay(String accessWay) {
    this.accessWay = accessWay;
  }
}
