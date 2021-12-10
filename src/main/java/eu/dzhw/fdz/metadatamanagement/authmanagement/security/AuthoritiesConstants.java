package eu.dzhw.fdz.metadatamanagement.authmanagement.security;

import java.util.Locale;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

  public static final String ADMIN = "ROLE_ADMIN";

  public static final String USER = "ROLE_USER";

  public static final String PUBLISHER = "ROLE_PUBLISHER";

  public static final String ANONYMOUS = "ROLE_ANONYMOUS";

  public static final String DATA_PROVIDER = "ROLE_DATA_PROVIDER";

  public static final String RELEASE_MANAGER = "ROLE_RELEASE_MANAGER";

  public static final String TASK_USER = "ROLE_TASK_USER";

  private AuthoritiesConstants() {}

  /**
   * Convert a "role" (usually with ROLE_ prefix and all uppercase) to be used in a Drupal user API
   * search (all lowercase without the ROLE_ prefix).
   *
   * NOTE: This works best with the roles set in this class.
   *
   * @param role the role which will be the search parameter
   * @return the role as a Drupal user API role search parameter
   */
  public static String toSearchValue(final String role) {
    return role.toLowerCase(Locale.ROOT);

  }
}
