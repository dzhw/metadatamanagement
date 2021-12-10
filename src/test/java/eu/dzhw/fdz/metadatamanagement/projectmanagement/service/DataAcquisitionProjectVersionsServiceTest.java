package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.Set;

import com.icegreen.greenmail.store.FolderException;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AbstractUserApiTests;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.UserApiService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.test.context.support.WithMockUser;

import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.ShadowCopyQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;

import javax.mail.MessagingException;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DataAcquisitionProjectVersionsServiceTest extends AbstractUserApiTests {
  private static final String PUBLISHER_EMAIL = "publisher@local";
  private static final String PUBLISHER_LOGIN = "defaultPublisher";

  private final DataAcquisitionProjectVersionsService versionsService;
  private final ShadowCopyQueueItemRepository shadowCopyQueueItemRepository;
  private final DataAcquisitionProjectManagementService projectManagementService;
  private final DataAcquisitionProjectRepository repository;
  private final JaversService javersService;

  public DataAcquisitionProjectVersionsServiceTest(
      @Autowired final DataAcquisitionProjectVersionsService versionsService,
      @Autowired final ShadowCopyQueueItemRepository shadowCopyQueueItemRepository,
      @Autowired final DataAcquisitionProjectManagementService projectManagementService,
      @Autowired final DataAcquisitionProjectRepository repository,
      @Autowired final JaversService javersService,
      @Value("${metadatamanagement.authmanagement.server.endpoint}")
      final String authServerEndpoint,
      @Autowired final UserApiService userApiService
  ) {
    super(authServerEndpoint, userApiService);

    this.shadowCopyQueueItemRepository = shadowCopyQueueItemRepository;
    this.projectManagementService = projectManagementService;
    this.repository = repository;
    this.javersService = javersService;
    this.versionsService = versionsService;

    this.mockServer.users(
        new User(
            "1234",
            PUBLISHER_LOGIN,
            PUBLISHER_EMAIL,
            "de",
            false,
            AuthoritiesConstants.toSearchValue(AuthoritiesConstants.PUBLISHER)
        )
    );
  }

  @AfterEach
  public void tearDown() throws FolderException {
    shadowCopyQueueItemRepository.deleteAll();
    repository.deleteAll();
    javersService.deleteAll();
    greenMail.purgeEmailFromAllMailboxes();
  }

  @Test
  public void testMultipleReleases() throws MessagingException {
    this.addFindAllByLoginInRequest(Set.of(PUBLISHER_LOGIN));
    this.addFindAllByLoginInRequest(2, Set.of());

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project = projectManagementService.save(project);

    // The defaultPublisher should have received an email
    assertEquals(1, greenMail.getReceivedMessages().length);
    assertEquals(PUBLISHER_EMAIL, greenMail.getReceivedMessages()[0].getHeader("To")[0]);

    project.setRelease(Release.builder().version("0.0.1").lastDate(LocalDateTime.now()).build());
    project = projectManagementService.save(project);

    Release previousRelease =
        versionsService.findPreviousRelease(project.getId(), project.getRelease());
    assertNull(previousRelease);

    // unrelease
    project.setRelease(null);
    project = projectManagementService.save(project);

    previousRelease = versionsService.findPreviousRelease(project.getId(), null);
    assertThat(previousRelease.getVersion(), equalTo("0.0.1"));

    // release again
    project.setRelease(Release.builder().version("0.1.0").lastDate(LocalDateTime.now()).build());
    project = projectManagementService.save(project);

    previousRelease = versionsService.findPreviousRelease(project.getId(), project.getRelease());
    assertThat(previousRelease.getVersion(), equalTo("0.0.1"));

    // unrelease
    project.setRelease(null);
    project = projectManagementService.save(project);

    previousRelease = versionsService.findPreviousRelease(project.getId(), null);
    assertThat(previousRelease.getVersion(), equalTo("0.1.0"));

    // release again
    project.setRelease(Release.builder().version("0.2.0").lastDate(LocalDateTime.now()).build());
    project = projectManagementService.save(project);

    previousRelease = versionsService.findPreviousRelease(project.getId(), project.getRelease());
    assertThat(previousRelease.getVersion(), equalTo("0.1.0"));

    // now we have 0.0.1 -> 0.1.0 -> 0.2.0
    previousRelease = versionsService.findPreviousRelease(project.getId(),
        Release.builder().version("0.1.0").build());
    assertThat(previousRelease.getVersion(), equalTo("0.0.1"));
  }
}
