package eu.dzhw.fdz.metadatamanagement.authmanagement.rest.dto;

import eu.dzhw.fdz.metadatamanagement.authmanagement.common.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.common.dto.UserWithRolesDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A DTO which acts as a wrapper for the "data" field in the User API response.
 */
@Getter
@Setter
public class UserApiResponseDto<T extends UserDto> {

  private List<T> data;

  /**
   * Convenience wrapper class for user API responses that should convert the Response Body to
   * a {@link UserDto}.
   */
  public static final class Users extends UserApiResponseDto<UserDto> {}

  /**
   * Convenience wrapper class for user API responses that should convert the Response Body to
   * a {@link UserWithRolesDto}.
   */
  public static final class UsersWithRoles extends UserApiResponseDto<UserWithRolesDto> {}
}
