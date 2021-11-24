package eu.dzhw.fdz.metadatamanagement.authmanagement.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * A wrapper that represents the body of a User Api response.
 */
@Getter
@ToString
public class UserApiResponse {
  private final List<Error> errors;

  /**
   * Build a representation of the responses body.
   *
   * @param errors the "errors" field of the response's JSON Body.
   */
  public UserApiResponse(
      @JsonProperty("errors") final List<Error> errors
  ) {
    this.errors = errors;
  }

  /**
   * Whether the response return without errors.
   *
   * @return was the request successful?
   */
  public boolean isSuccessful() {
    return this.errors == null || this.errors.size() == 0;
  }

  /**
   * A wrapper for an error JSON object in the "errors" field of the
   * User API response's body.
   */
  @Getter
  public static class Error {
    private final String detail;

    /**
     * Build an error using only the error's "detail" field.
     *
     * @param detail a message describing the error in detail
     */
    public Error(
        @JsonProperty("detail") final String detail
    ) {
      this.detail = detail;
    }
  }
}
