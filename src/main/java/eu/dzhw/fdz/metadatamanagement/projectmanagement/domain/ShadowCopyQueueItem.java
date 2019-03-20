package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@EqualsAndHashCode(callSuper = false, of = "id")
public class ShadowCopyQueueItem extends AbstractRdcDomainObject {

  /**
   * Queue item id.
   */
  @Id
  private String id;

  /**
   * Project id for which a shadow copy should be created.
   */
  @NotNull
  private String dataAcquisitionProjectId;

  /**
   * The version that should be created.
   */
  @NotNull
  private String shadowCopyVersion;

  /**
   * Start time of the copy process.
   */
  private LocalDateTime updateStartedAt;
}
