package eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriTemplateHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;

/**
 * A wrapper class which makes working with a {@link MockRestServiceServer} easier for the
 * metadatamanagement project.
 */
public class MockServer {
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

  public MockRequest request(String url, Object... params) {
    return new MockRequest(url, params);
  }

  /**
   * Verify that all expected requests where run.
   */
  public void verify() {
    server.verify();
  }

  /**
   * Reset all expectations.
   */
  public void reset() {
    server.reset();
  }

  public class MockRequest {
    private final URI uri;

    private HttpMethod httpMethod = HttpMethod.GET;
    private ExpectedCount expectedCount = ExpectedCount.once();

    public MockRequest(String url, Object... params) {
      this.uri = MockServer.this.uriHandler.expand(url, params);
    }

    public MockRequest httpMethod(final HttpMethod httpMethod) {
      this.httpMethod = httpMethod;

      return this;
    }

    public MockRequest expectedCount(final int count) {
      this.expectedCount = ExpectedCount.times(count);

      return this;
    }

    public MockResponse withSuccess() {
      return new MockResponse(
          this,
          MockRestResponseCreators.withSuccess()
              .contentType(new MediaType("application", "vnd.api+json"))
      );
    }

    public MockResponse withBadRequest() {
      return new MockResponse(this, MockRestResponseCreators.withBadRequest());
    }

    public MockResponse withUnauthorizedRequest() {
      return new MockResponse(this, MockRestResponseCreators.withUnauthorizedRequest());
    }

    public MockResponse withNotFound() {
      return new MockResponse(this, MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));
    }

    public MockResponse withServerError() {
      return new MockResponse(this, MockRestResponseCreators.withServerError());
    }
  }

  public class MockResponse {
    private static final String SUCCESSFUL_RESPONSE_TEMPLATE = """
        {
          "data": [%s]
        }""";

    private static final String ERROR_RESPONSE_TEMPLATE = """
      {
        "errors": [%s]
      }""";

    private final DefaultResponseCreator responseCreator;
    private final MockRequest request;

    public MockResponse(
        MockRequest request,
        DefaultResponseCreator responseCreator
    ) {
      this.request = request;
      this.responseCreator = responseCreator;
    }

    public MockResponse mediaType(MediaType mediaType) {
      this.responseCreator.contentType(mediaType);

      return this;
    }

    public MockResponse body() {
      responseCreator.body("{}");

      return this;
    }

    public MockResponse body(Predicate<User> predicate) {
      assert predicate != null;

      responseCreator.body(String.format(
          SUCCESSFUL_RESPONSE_TEMPLATE,
          MockServer.this.users.stream()
              .filter(predicate)
              .map(User::toString)
              .collect(Collectors.joining(","))
      ));

      return this;
    }

    public MockResponse body(MockError... errors) {
      responseCreator.body(String.format(
          ERROR_RESPONSE_TEMPLATE,
          Arrays.stream(errors)
              .map(MockError::toString)
              .collect(Collectors.joining(","))
      ));

      return this;
    }

    public void addToServer() {
      MockServer.this.server.expect(request.expectedCount, requestTo(request.uri))
        .andExpect(method(request.httpMethod))
        .andRespond(responseCreator);
    }
  }
}
