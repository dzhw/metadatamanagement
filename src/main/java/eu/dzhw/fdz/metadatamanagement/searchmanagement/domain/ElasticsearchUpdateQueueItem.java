package eu.dzhw.fdz.metadatamanagement.searchmanagement.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Elasticsearch indices are updated asynchronously by processing these queue items.
 * 
 * @author René Reitmann
 */
@Document(collection = "elasticsearch_update_queue_item")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.builders")
@CompoundIndexes({
    @CompoundIndex(def = "{documentType: 1, documentId: 1, action: 1, createdDate: 1}",
        unique = true),
    @CompoundIndex(def = "{updateStartedAt: 1, updateStartedBy: 1}")})
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

  public ElasticsearchUpdateQueueItem() {
    // default constructor for spring data
  }
  
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

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ElasticsearchType getDocumentType() {
    return documentType;
  }

  public void setDocumentType(ElasticsearchType documentType) {
    this.documentType = documentType;
  }

  public String getDocumentId() {
    return documentId;
  }

  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public LocalDateTime getUpdateStartedAt() {
    return updateStartedAt;
  }

  public void setUpdateStartedAt(LocalDateTime updateStartedAt) {
    this.updateStartedAt = updateStartedAt;
  }

  public ElasticsearchUpdateQueueAction getAction() {
    return action;
  }

  public void setAction(ElasticsearchUpdateQueueAction action) {
    this.action = action;
  }

  public String getUpdateStartedBy() {
    return updateStartedBy;
  }

  public void setUpdateStartedBy(String updateStartedBy) {
    this.updateStartedBy = updateStartedBy;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("documentType", documentType)
      .add("documentId", documentId)
      .add("updateStartedAt", updateStartedAt)
      .add("action", action)
      .add("updateStartedBy", updateStartedBy)
      .toString();
  }

}
