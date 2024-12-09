package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidRegistrationService.VariableMetadata;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.boot.actuate.metrics.AutoTimer;
import org.springframework.boot.actuate.metrics.web.client.MetricsRestTemplateCustomizer;
import org.springframework.boot.actuate.metrics.web.client.RestTemplateExchangeTagsProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A client service that's solely responsible for
 * communication with the da|ra PID Service.
 */
@Service
@Slf4j
public class DaraPidClientService {

  static final String PATH_REGISTER = "/variable/register";
  static final String PATH_VERIFY = "/variable/verify";
  static final String PATH_JOB_STATUS = "/variable/status/job/";

  private final MetadataManagementProperties config;
  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  public DaraPidClientService(
    MetadataManagementProperties config,
    MeterRegistry registry,
    RestTemplateExchangeTagsProvider provider
  ) {
    this.config = config;
    this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    new MetricsRestTemplateCustomizer(registry, provider, "dara.pid.client.requests", AutoTimer.ENABLED)
      .customize(restTemplate);
    this.objectMapper = new ObjectMapper();
    this.objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
  }

  /**
   * Returns the fully expanded registration Endpoint URI.
   * @return the fully expanded registration Endpoint URI
   */
  String getRegistationEndpoint() {
    return this.config.getDaraPid().getEndpoint() + PATH_REGISTER;
  }

  /**
   * Polls the registration endpoint in order to
   * determine whether the service is reachable.
   * @return true if the service is reachable, false otherwise
   * @throws IOException if an IO error occurrs, in which case the service is also likely not reachable
   */
  boolean serviceIsReachable() throws IOException {

    var authHash = Base64.encodeBase64(String.format("%s:%s",
      this.config.getDaraPid().getUsername(),
      this.config.getDaraPid().getPassword()
    ).getBytes(StandardCharsets.UTF_8));

    var uri = this.getRegistationEndpoint();
    var request = this.restTemplate.getRequestFactory().createRequest(URI.create(uri), HttpMethod.GET);
    request.getHeaders().add("Authorization", "Basic " + new String(authHash, StandardCharsets.UTF_8));

    try (var response = request.execute()) {
      return response.getRawStatusCode() == 405;
    }
  }

  /**
   * Registers the provided list of variables with the da|ra PID service.
   * @param variables the list of variables that should be registered
   * @return the API response with a Job ID
   * @throws RegistrationException if the registration wasn't successful (IO error or unexpected response)
   * @see RegistrationResponseException
   * @see RegistrationClientException
   * @see RegistrationResponseParsingException
   */
  RegistrationResponse register(List<VariableMetadata> variables) throws RegistrationException {

    var authHash = Base64.encodeBase64(String.format("%s:%s",
      this.config.getDaraPid().getUsername(),
      this.config.getDaraPid().getPassword()
    ).getBytes(StandardCharsets.UTF_8));

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    headers.add("Authorization", "Basic " + new String(authHash, StandardCharsets.UTF_8));

    var entity = new HttpEntity<>(new RegistrationRequestBody(variables), headers);
    try {
      log.debug(this.objectMapper.writeValueAsString(new RegistrationRequestBody(variables)));
      // verify variables
      var response = this.restTemplate.postForEntity(
        this.config.getDaraPid().getEndpoint() + PATH_VERIFY, entity, String.class);
      var responseNode = this.objectMapper.readTree(response.getBody());
      if (responseNode.path("constraintViolation").isArray()) {
        var violations = Stream.of((ArrayNode) responseNode.path("constraintViolation"))
          .map(JsonNode::toPrettyString).toList();
        throw new VerificationException(violations);
      }
      // register variables
      response = this.restTemplate.postForEntity(this.getRegistationEndpoint(), entity, String.class);
      if (!response.getStatusCode().is2xxSuccessful()) {
        throw new RegistrationResponseException(response.getStatusCodeValue(), response.getBody());
      }
      var responseBody = this.objectMapper.readValue(response.getBody(), RegistrationResponse.class);
      log.debug("PID Registration request was successful. Status can be queried using Job-ID: " + responseBody.jobId());
      return responseBody;
    } catch (RestClientException e) {
      throw new RegistrationClientException(e);
    } catch (JsonProcessingException e) {
      throw new RegistrationResponseParsingException(e);
    }
  }

  /**
   * Returns the status of a registration job.
   * @param jobId the UUID that was returned for creating a registration job
   * @return the status entry for each variable included in the registration request
   * @throws JobStatusException when fetching the job status failed (IO error or unexpected response)
   * @see DaraPidClientService#register(List)
   */
  VariableStatus[] getJobStatus(String jobId) throws JobStatusException {

    var authHash = Base64.encodeBase64(String.format("%s:%s",
      this.config.getDaraPid().getUsername(),
      this.config.getDaraPid().getPassword()
    ).getBytes(StandardCharsets.UTF_8));

    var uri = this.config.getDaraPid().getEndpoint() + PATH_JOB_STATUS + jobId;
    ClientHttpRequest request;
    try {
      request = this.restTemplate.getRequestFactory().createRequest(URI.create(uri), HttpMethod.GET);
    } catch (IOException e) {
      throw new JobStatusException(String.format("Unable to create job status request using this URI: '%s'", uri));
    }
    request.getHeaders().add("Authorization", "Basic " + new String(authHash, StandardCharsets.UTF_8));

    try (var response = request.execute()) {
      var body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
      log.debug("Job status response for {}", jobId);
      log.debug(body);
      if (response.getStatusCode().is2xxSuccessful()) {
        return this.objectMapper.readValue(body, VariableStatus[].class);
      } else {
        throw new JobStatusResponseException(response.getRawStatusCode(), body);
      }
    } catch (JsonMappingException | JsonParseException e) {
      log.error("An error occurred while parsing the JSON response", e);
      throw new JobStatusException("An error occurred while parsing the JSON response", e);
    } catch (IOException e) {
      log.error("An IO error has occurred while performing the request", e);
      throw new JobStatusException("An IO error has occurred while performing the request", e);
    }
  }

  /**
   * This exception is thrown when verification of variable metadata fails.
   */
  @Getter
  public static class VerificationException extends RegistrationException implements DaraPidApiException {
    private final ArrayList<String> violations;
    public VerificationException(List<String> violations) {
      super("Verification of variable metadata has failed");
      this.violations = new ArrayList<>(violations);
    }
  }

  /**
   * A wrapper type that provides the expected request body
   * JSON structure for the registration API call .
   */
  private record RegistrationRequestBody(List<VariableMetadata> variables) {}

  /** The expected response type when a registration API call was successful. */
  record RegistrationResponse(String jobId) {}

  /**
   * A generic interface to pass all custom exceptions around with a single argument type.
   * @see eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService#sendDaraPidRegistrationErrorEmail(User, List, DataAcquisitionProject, DataPackage, DaraPidApiException)
   */
  public interface DaraPidApiException {}

  /**
   * An abstract exception type used as a catch-all for the register API call.
   */
  public static abstract class RegistrationException extends Exception implements DaraPidApiException {
    public RegistrationException(String message) {
      super(message);
    }
    public RegistrationException(String message, Throwable t) {
      super(message, t);
    }
  }

  /**
   * This exception is thrown when the response status code
   * is not in the success category (200, 201, etc.).
   */
  @Getter
  public static class RegistrationResponseException extends RegistrationException {
    private final int statusCode;
    private final String body;
    public RegistrationResponseException(int statusCode, String body) {
      super("Registration was not successful");
      this.statusCode = statusCode;
      this.body = body;
    }
  }

  /**
   * This exception is thrown when the JSON data included in the response
   * couldn't be processed or mapped to the expected result type.
   */
  public static class RegistrationResponseParsingException extends RegistrationException {
    public RegistrationResponseParsingException(JsonProcessingException e) {
      super("An error occurred while processing the JSON response", e);
    }
  }

  /**
   * This exception is thrown when the Spring REST client encounters
   * an IO error such as connection closed or a timeout.
   */
  public static class RegistrationClientException extends RegistrationException {
    public RegistrationClientException(RestClientException e) {
      super("A REST client error has occurred while registering variables", e);
    }
  }

  /**
   * Details on which constraints were violated when metadata verification fails.
   * @see <a href="https://labs.da-ra.de/nfdi/api/swagger-ui/index.html#/Registration/verifyVariables">PID Metadata Verification Endpoint</a>
   */
  public record ConstraintViolation(
    String message,
    String locationInfo
  ) {}

  /**
   * Variable registration status types.
   * @see <a href="https://labs.da-ra.de/nfdi/api/swagger-ui/index.html">da|ra PID API</a>
   */
  enum VariableStatusType {
    FAILED,
    PENDING,
    FINISHED
  }

  /**
   * Per variable item type in a response for the status of a batch registration job.
   * @see <a href="https://labs.da-ra.de/nfdi/api/swagger-ui/index.html">da|ra PID API</a>
   */
  public record VariableStatus(
    int position,
    String validationStatus,
    List<ConstraintViolation> validationErrors,
    String pid,
    String registrationResult,
    VariableStatusType status,
    String lastUpdate // format: 2024-12-07 21:00:55 --> needs special converter in Jackson for it to work with LocalDateTime
  ) {}

  /**
   * This exception is generally thrown when an IO error occurrs
   * (connection closed, JSON response parsing failed, etc.).
   */
  public static class JobStatusException extends Exception implements DaraPidApiException {
    public JobStatusException(String message) {
      super(message);
    }
    public JobStatusException(String message, Throwable t) {
      super(message, t);
    }
  }

  /**
   * This exception is thrown when the response status code
   * is not in the success category (200, 201, etc.).
   */
  @Getter
  public static class JobStatusResponseException extends JobStatusException {
    private final int statusCode;
    private final String body;
    public JobStatusResponseException(int statusCode, String body) {
      super("The polling of the job status was unsuccessful");
      this.statusCode = statusCode;
      this.body = body;
    }
  }
}
