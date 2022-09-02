package eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils;

import lombok.Getter;

/**
 * A wrapper which represents a JSON "error" object in a User API response.
 */
@Getter
public class MockError {
  private static final String TEMPLATE = """
    {
      "detail": "%s"
    }""";

  private final String detail;

  /**
   * Build an error.
   *
   * @param detail a detailed message describing what caused the error
   */
  public MockError(final String detail) {
    this.detail = detail;
  }

  @Override
  public String toString() {
    return String.format(
        TEMPLATE,
        detail
    );
  }
}
