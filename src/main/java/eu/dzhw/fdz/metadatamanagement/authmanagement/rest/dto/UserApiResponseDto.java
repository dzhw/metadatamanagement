package eu.dzhw.fdz.metadatamanagement.authmanagement.rest.dto;

import eu.dzhw.fdz.metadatamanagement.authmanagement.common.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A DTO which acts as a wrapper for the "data" field in the User API response.
 */
@Getter
@Setter
public class UserApiResponseDto {

  private List<UserDto> data;
}
