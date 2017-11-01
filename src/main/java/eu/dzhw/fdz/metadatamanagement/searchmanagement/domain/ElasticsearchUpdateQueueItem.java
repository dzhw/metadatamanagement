package eu.dzhw.fdz.metadatamanagement.searchmanagement.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Elasticsearch indices are updated asynchronously by processing these queue items.
 * 
 * @author René Reitmann
 */
@Document(collection = "elasticsearch_update_queue_item")
@CompoundIndexes({
    @CompoundIndex(def = "{documentType: 1, documentId: 1, action: 1}",
        unique = true),
    @CompoundIndex(def = "{updateStartedAt: 1, updateStartedBy: 1, createdDate: 1}"),
    @CompoundIndex(def = "{updateStartedAt: 1, updateStartedBy: 1, documentType: 1,"
        + " createdDate: 1}", name = "locked_items_per_type")})
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class ElasticsearchUpdateQueueItem extends AbstractRdcDomainObject {

  /* Domain Object Attributes */
  @Id
  private String id;

  @NotNull
  private ElasticsearchType documentType;

  @NotEmpty
  private String documentId;

  private LocalDateTime updateStartedAt;

  private String updateStartedBy;

  @NotNull
  private ElasticsearchUpdateQueueAction action;
  
  /**
   * Construct a queue item with the mandatory params.
   * @param documentId The id of the document to update or delete
   * @param documentType The type (e.g. "variables")
   * @param action The action
   */
  public ElasticsearchUpdateQueueItem(String documentId, ElasticsearchType documentType,
      ElasticsearchUpdateQueueAction action) {
    this.documentId = documentId;
    this.documentType = documentType;
    this.action = action;
  }
}
