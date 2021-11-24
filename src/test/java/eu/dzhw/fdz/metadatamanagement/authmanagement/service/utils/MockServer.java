package eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriTemplateHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * A wrapper class which makes working with a {@link MockRestServiceServer} easier for the
 * metadatamanagement project.
 */
public class MockServer {
  private static final String RESPONSE_TEMPLATE = """
        {
          "data": [%s]
        }""";

  private final List<User> users = new ArrayList<>();

  private final UriTemplateHandler uriHandler;
  private final MockRestServiceServer server;

  /**
   * Build the MockServer with the required parameters.
   *
   * @param rootUri the root of the URI the mock requests will be mocking
   * @param restTemplate the RestTemplate that will make all requests
   */
  public MockServer(final String rootUri, final RestTemplate restTemplate) {
    DefaultUriBuilderFactory uriFactory = new DefaultUriBuilderFactory(rootUri);
    uriFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);
    uriHandler = uriFactory;

    server = MockRestServiceServer
        .bindTo(restTemplate)
        .ignoreExpectOrder(true)
        .build();
  }

  /**
   * Add Users which will be used to generate the responses to a mock request.
   *
   * @param users a collection of Users
   */
  public void users(final User... users) {
    this.users.addAll(List.of(users));
  }

  /**
   * Add a mock GET request which will be expected by the {@link MockRestServiceServer}.
   *
   * @param url the API path (NOTE: only path is needed the root should have already been added)
   * @param predicate the condition(s) that will be used to see which {@link User}s will be
   *                  included in the response body
   * @param params the parameters that will be used to replace parameters in the provided url
   * @return the current instance of MockServer
   */
  public MockServer request(
      final String url,
      final Predicate<User> predicate,
      final Object... params
  ) {
    return multipleRequests(url, predicate, 1, params);
  }

  public MockServer request(
      final String url,
      final HttpMethod method,
      final Predicate<User> predicate,
      final Object... params
  ) {
    return multipleRequests(url, method, predicate, 1, params);
  }

  /**
   * Add multiple of the same mock requests which will be expected by the
   * {@link MockRestServiceServer}.
   *
   * @param url the API path (NOTE: only path is needed the root should have already been added)
   * @param predicate the condition(s) that will be used to see which {@link User}s will be
   *                  included in the response body
   * @param expectedRequests the number of times the request will be expected
   * @param params the parameters that will be used to replace parameters in the provided url
   * @return the current instance of MockServer
   */
  public MockServer multipleRequests(
    final String url,
    final Predicate<User> predicate,
    final int expectedRequests,
    final Object... params
  ) {
    return multipleRequests(url, HttpMethod.GET, predicate, expectedRequests, params);
  }

  /**
   * Add multiple of the same mock requests which will be expected by the
   * {@link MockRestServiceServer}.
   *
   * @param url the API path (NOTE: only path is needed the root should have already been added)
   * @param method the expected HTTP Method used by the HTTP request
   * @param predicate the condition(s) that will be used to see which {@link User}s will be
   *                  included in the response body
   * @param expectedRequests the number of times the request will be expected
   * @param params the parameters that will be used to replace parameters in the provided url
   * @return the current instance of MockServer
   */
  public MockServer multipleRequests(
      final String url,
      final HttpMethod method,
      final Predicate<User> predicate,
      final int expectedRequests,
      final Object... params
  ) {
    server.expect(
        ExpectedCount.times(expectedRequests),
        requestTo(uriHandler.expand(url, params))
    )
      .andExpect(method(method))
    .andRespond(
        withSuccess()
            .body(generateBody(predicate))
            .contentType(new MediaType("application", "vnd.api+json"))
    );

    return this;
  }

  /**
   * Verify that all expected requests where run.
   */
  public void verify() {
    server.verify();
  }

  private String generateBody(Predicate<User> predicate) {
    if (predicate == null) {
      return  String.format(
        RESPONSE_TEMPLATE,
        ""
      );
    }

    return String.format(
      RESPONSE_TEMPLATE,
      MockServer.this.users.stream()
          .filter(predicate)
          .map(User::toString)
          .collect(Collectors.joining(","))
    );
  }
}
