package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

  public static final String ADMIN = "ROLE_ADMIN";

  public static final String USER = "ROLE_USER";

  public static final String PUBLISHER = "ROLE_PUBLISHER";

  public static final String ANONYMOUS = "ROLE_ANONYMOUS";

  public static final String DATA_PROVIDER = "ROLE_DATA_PROVIDER";
  
  public static final String TASK_USER = "ROLE_TASK_USER";

  private AuthoritiesConstants() {}
}
