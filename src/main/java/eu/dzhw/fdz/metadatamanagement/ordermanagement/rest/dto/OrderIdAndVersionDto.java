package eu.dzhw.fdz.metadatamanagement.ordermanagement.rest.dto;

import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;

/**
 * Transfer object for id and version of an {@link Order}.
 */
public class OrderIdAndVersionDto {

  private String id;
  private Long version;

  public OrderIdAndVersionDto(String id, Long version) {
    this.id = id;
    this.version = version;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
