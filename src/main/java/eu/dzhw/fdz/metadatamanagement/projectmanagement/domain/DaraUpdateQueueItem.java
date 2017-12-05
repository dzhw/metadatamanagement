package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Dara entries will be updated asynchronously this using these items.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "dara_update_queue_item")
@CompoundIndexes({
    @CompoundIndex(def = "{updateStartedAt: 1, updateStartedBy: 1, createdDate: 1}")
    })
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DaraUpdateQueueItem extends AbstractRdcDomainObject {
  
  /* Domain Object Attributes */
  @Id
  private String id;

  @NotEmpty
  @Indexed(unique = true)
  private String projectId;

  private LocalDateTime updateStartedAt;

  private String updateStartedBy;
  
  /**
   * Construct a dara queue item with a project id.
   * @param projectId The id of a study.
   */
  public DaraUpdateQueueItem(String projectId) {
    this.projectId = projectId;
  }

}
