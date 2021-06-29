package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a queued shadow copy task of a project.
 */
@Document(collection = "shadow_copy_queue_item")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
public class ShadowCopyQueueItem extends AbstractRdcDomainObject {

  private static final long serialVersionUID = -6343565339183648173L;

  /**
   * Queue item id.
   */
  @Id
  private String id;

  /**
   * Project id for which a shadow copy should be created or hidden or unhidden.
   */
  @NotNull
  private String dataAcquisitionProjectId;

  /**
   * The release object of the project which has been released.
   */
  @NotNull
  private Release release;

  /**
   * Start time of the copy process.
   */
  private LocalDateTime updateStartedAt;
  
  /**
   * The action which will be performed for the shadow copies.
   */
  @NotNull
  private Action action;
  
  /**
   * The action which will be performed for the shadows. 
   */
  public enum Action {
    CREATE, HIDE, UNHIDE, DELETE
  }
}
