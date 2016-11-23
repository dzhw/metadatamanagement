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

  private final Error error;

  /**
   * Create the {@link RuntimeException}.
   */
  public CustomParameterizedException(String message, String entity, String invalidValue, 
      String property) {
    super(message);
    Error error = new Error(entity, message, invalidValue, property);
    this.error = error;
  }

  /**
   * Get the {@link ParameterizedErrorDto}.
   */
  public Error getErrorDto() {
    return this.error;
  }

}
