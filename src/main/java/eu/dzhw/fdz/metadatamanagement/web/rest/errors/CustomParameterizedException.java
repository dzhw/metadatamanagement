package eu.dzhw.fdz.metadatamanagement.web.rest.errors;

/**
 * Custom, parameterized exception, which can be translated on the client side. For example:
 * 
 * <pre>
 * throw new CustomParameterizedException(&quot;myCustomError&quot;, 
 * &quot;hello&quot;, &quot;world&quot;);
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
   * Constructor for custom parameter exceptions.
   * 
   * @param message a given message of the exception.
   * @param params given parameters of the exception.
   */
  public CustomParameterizedException(String message, String... params) {
    super(message);
    this.message = message;
    this.params = params;
  }

  public ParameterizedErrorDto getErrorDto() {
    return new ParameterizedErrorDto(message, params);
  }

}
