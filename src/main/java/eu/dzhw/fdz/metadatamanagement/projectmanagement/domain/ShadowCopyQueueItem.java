package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Represents a queued shadow copy task of a project.
 */
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
  @CreatedDate
  private LocalDateTime createdDate;
}
