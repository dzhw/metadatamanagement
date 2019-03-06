package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Represents a queued shadow copy task of a project.
 */
@Document(collection = "shadow_copy_queue_item")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShadowCopyQueueItem {

  @Id
  private String id;
  @NotNull
  private String dataAcquisitionProjectId;
  @NotNull
  private String shadowCopyVersion;
  @NotNull
  private String username;
  @CreatedDate
  private LocalDateTime createdDate;

  private LocalDateTime updateStartedAt;
}
