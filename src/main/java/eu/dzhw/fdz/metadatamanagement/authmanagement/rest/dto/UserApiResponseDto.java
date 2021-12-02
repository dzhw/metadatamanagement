package eu.dzhw.fdz.metadatamanagement.authmanagement.rest.dto;

import eu.dzhw.fdz.metadatamanagement.authmanagement.service.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.dto.UserWithRolesDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A DTO which acts as a wrapper for the "data" field in the User API response.
 *
 * @param <T> the class Jackson JSON will use to try to create interpret the data
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
