package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception.InvalidUserApiResponseException;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils.User;
import eu.dzhw.fdz.metadatamanagement.common.config.audit.AuditorStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;

public class AuditorServiceTests extends AbstractUserApiTests {
  private static final String LOGIN = "user";

  private final AuditorService auditorService;

  private final AuditorStore auditorStore;

  public AuditorServiceTests(
      @Value("${metadatamanagement.authmanagement.server.endpoint}")
      final String authServerEndpoint,
      @Autowired final UserApiService userApiService,
      @Autowired final AuditorService auditorService,
      @Autowired final AuditorStore auditorStore
  ) {
    super(authServerEndpoint, userApiService);

    this.auditorService = auditorService;
    this.auditorStore = auditorStore;

    this.mockServer.users(
        new User(
            "1234",
            LOGIN,
            "user@local",
            "de",
            false,
            AuthoritiesConstants.USER
        )
    );
  }

  @Test
  public void findAndSetOnBehalfAuditorTest_EmptyLogin() {
    assertNull(auditorService.findAndSetOnBehalfAuditor(null));
    assertNull(auditorService.findAndSetOnBehalfAuditor(""));
    assertNull(auditorService.findAndSetOnBehalfAuditor(" "));
  }

  @Test
  public void findAndSetOnBehalfAuditorTest_MissingUser() {
    final var login = "missing_user";
    this.addFindOneByLoginRequest(login);

    var message = assertThrows(
        IllegalArgumentException.class,
        () -> auditorService.findAndSetOnBehalfAuditor(login)
    ).getMessage();

    assertEquals("User not found: " + login, message);
  }

  @Test
  public void findAndSetOnBehalfAuditorTest_ResponseError() {
    this.startFindOneByLoginRequest(LOGIN)
        .withServerError()
        .addToServer();

    assertThrows(
      InvalidUserApiResponseException.class,
      () -> auditorService.findAndSetOnBehalfAuditor(LOGIN)
    );
  }

  @Test
  public void findAndSetOnBehalfAuditorTest_Success() {
    this.addFindOneByLoginRequest(LOGIN);

    assertNull(auditorStore.getAuditor());

    try(auditorService) {
      auditorService.findAndSetOnBehalfAuditor(LOGIN);

      assertEquals(LOGIN, auditorStore.getAuditor());
    }

    assertNull(auditorStore.getAuditor());
  }
}
