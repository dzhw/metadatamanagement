package eu.dzhw.fdz.metadatamanagement.common.config.audit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.icegreen.greenmail.store.FolderException;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AbstractUserApiTests;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.UserApiService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils.User;
import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuditorStoreAwareTests extends AbstractUserApiTests {
  private static final String INIT_USER = "user";

  private final WebApplicationContext wac;
  private final AuditorStoreAware auditorStoreAware;
  private final AuditorStore auditorStore;
  private final DataPackageRepository dataPackageRepository;
  private final JaversService javersService;

  public AuditorStoreAwareTests(
      @Autowired final WebApplicationContext wac,
      @Autowired final AuditorStoreAware auditorStoreAware,
      @Autowired final AuditorStore auditorStore,
      @Autowired final DataPackageRepository dataPackageRepository,
      @Autowired final JaversService javersService,
      @Value("${metadatamanagement.authmanagement.server.endpoint}")
      final String authServerEndpoint,
      @Autowired final UserApiService userApiService
  ) {
    super(authServerEndpoint, userApiService);

    this.wac = wac;
    this.auditorStoreAware = auditorStoreAware;
    this.auditorStore = auditorStore;
    this.dataPackageRepository = dataPackageRepository;
    this.javersService = javersService;
  }

  @AfterEach
  public void cleanUp() throws FolderException {
    dataPackageRepository.deleteAll();
    javersService.deleteAll();
    greenMail.purgeEmailFromAllMailboxes();
    auditorStore.clear();
  }

  @Test
  public void defaultAuditorTest() {
    var auditor = this.auditorStoreAware.getCurrentAuditor();

    assertThat(auditor.isPresent(), is(true));
    assertThat(auditor.get(), is(Constants.SYSTEM_ACCOUNT));
  }

  @Test
  public void singleAuditorStoreSetTest() {
    final var testAuditor = "test";
    auditorStore.setAuditor(testAuditor);

    var auditor = this.auditorStoreAware.getCurrentAuditor();

    assertThat(auditor.isPresent(), is(true));
    assertThat(auditor.get(), is(testAuditor));
  }

  @Test
  public void repositoryAuditorSetTest() {
    var auditor = "repo_auditor";

    auditorStore.setAuditor(auditor);

    var dataPackage =
        UnitTestCreateDomainObjectUtils.buildDataPackage("REPO_AUDITOR_TEST_PROJECT");

    dataPackage = dataPackageRepository.save(dataPackage);

    assertThat(dataPackage.getCreatedBy(), is(auditor));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.TASK_USER, username = INIT_USER)
  public void mvcAuditorSetTest() throws Exception {
    final var PROJECT_ID = "mvcAuditorTestProject";
    final var AUDITOR = "mvc_auditor";
    final MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

    this.mockServer.users(
        new User(
            "1234",
            AUDITOR,
            "auditor@local",
            "de",
            false,
            "task_user"
        )
    );
    this.addFindOneByLoginRequest(AUDITOR);

    var dataPackage = dataPackageRepository.save(
        UnitTestCreateDomainObjectUtils.buildDataPackage(PROJECT_ID)
    );

    assertThat(dataPackage.getCreatedBy(), is(INIT_USER));

    MockMultipartFile attachment = new MockMultipartFile(
        "file",
        "filename.txt",
        "text/plain", "some text".getBytes()
    );
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata(dataPackage.getId());
    MockMultipartFile metadata = new MockMultipartFile(
        "dataPackageAttachmentMetadata",
        "Blob",
        "application/json",
        TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata)
    );

    mockMvc.perform(
        MockMvcRequestBuilders.multipart(
            "/api/data-packages/" + dataPackage.getId() + "/overview/de"
        )
            .file(attachment)
            .file(metadata)
            .param("onBehalfOf", AUDITOR)
    )
        .andExpect(status().isOk());

    dataPackage = dataPackageRepository.findById(dataPackage.getId()).get();

    dataPackage.setTitle(I18nString.builder().de("Titel Aktualisiert De").en("Title Updated En").build());

    dataPackageRepository.save(dataPackage);

    assertEquals(INIT_USER, dataPackage.getLastModifiedBy());
  }
}
