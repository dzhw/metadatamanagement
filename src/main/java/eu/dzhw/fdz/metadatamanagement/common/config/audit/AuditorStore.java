package eu.dzhw.fdz.metadatamanagement.common.config.audit;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * A Store the name of the auditor who is creating or updating
 * an entity.
 */
@Getter
@Setter
public class AuditorStore {

  private String auditor;

  /**
   * Build and set the default auditor to the currently logged-in user.
   */
  public AuditorStore() {
    // Default the auditor to the current user.
    this.auditor = SecurityUtils.getCurrentUserLogin();
  }

  /**
   * clear the name of the current auditor.
   */
  public void clear() {
    this.auditor = null;
  }
}
