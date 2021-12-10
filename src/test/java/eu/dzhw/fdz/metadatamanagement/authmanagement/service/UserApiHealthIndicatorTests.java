package eu.dzhw.fdz.metadatamanagement.authmanagement.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserApiHealthIndicatorTests extends AbstractUserApiTests {

  private final UserApiHealthIndicator healthIndicator;

  public UserApiHealthIndicatorTests(
      @Value("${metadatamanagement.authmanagement.server.endpoint}")
      final String authServerEndpoint,
      @Autowired final UserApiService userApiService,
      @Autowired final UserApiHealthIndicator healthIndicator
  ) {
    super(authServerEndpoint, userApiService);

    this.healthIndicator = healthIndicator;
  }

  @Test
  public void doHealthCheck_Success() throws Exception {
    addUserJsonApiPath()
        .withSuccess()
        .addToServer();

    var builder = new Health.Builder();
    healthIndicator.doHealthCheck(builder);

    var health = builder.build();

    assertTrue(health.getStatus().equals(Status.UP));
  }

  @Test
  public void doHealthCheck_Fail() throws Exception {
    addUserJsonApiPath()
      .withServerError()
      .addToServer();

    var builder = new Health.Builder();
    healthIndicator.doHealthCheck(builder);

    var health = builder.build();

    assertTrue(health.getStatus().equals(Status.DOWN));
  }
}
