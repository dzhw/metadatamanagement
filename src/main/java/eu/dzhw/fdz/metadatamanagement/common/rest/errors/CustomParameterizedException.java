package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

/**
 * Custom, parameterized exception, which can be translated on the client side. For example:
 * 
 * <pre>
 * throw new CustomParameterizedException(
 * &quot;myCustomError&quot;, &quot;hello&quot;, &quot;world&quot;);
 * </pre>
 * Can be translated with:
 * 
 * <pre>
 * "error.myCustomError" :  "The server says {{params[0]}} to {{params[1]}}"
 * </pre>
 */
public class CustomParameterizedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String message;
  private final String[] params;

  /**
   * Create the {@link RuntimeException}.
   */
  public CustomParameterizedException(String message, String... params) {
    super(message);
    this.message = message;
    this.params = params;
  }

  /**
   * Get the {@link ParameterizedErrorDto}.
   */
  public ParameterizedErrorDto getErrorDto() {
    return new ParameterizedErrorDto(message, params);
  }

}
