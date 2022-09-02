package eu.dzhw.fdz.metadatamanagement.common.config.audit;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import lombok.Setter;

/**
 * A Store the name of the auditor who is creating or updating
 * an entity.
 */
@Setter
public class AuditorStore {

  private String auditor;

  /**
   * Get the current auditor.
   *
   * Defaults to the current user via authentication.
   *
   * @return the current auditor
   */
  public String getAuditor() {
    if (this.auditor != null) {
      return this.auditor;
    }

    return SecurityUtils.getCurrentUserLogin();
  }

  /**
   * clear the name of the current auditor.
   */
  public void clear() {
    this.auditor = null;
  }
}
