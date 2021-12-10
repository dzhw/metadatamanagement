package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.icegreen.greenmail.store.FolderException;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AbstractUserApiTests;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.UserApiService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils.User;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.ShadowCopyQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;

import java.util.Set;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DataAcquisitionProjectVersionsResourceTest extends AbstractUserApiTests {
  private static final String API_DATA_ACQUISITION_PROJECTS_URI = "/api/data-acquisition-projects";

  private static final String PUBLISHER_EMAIL = "publisher@local";
  private static final String PUBLISHER_LOGIN = "defaultPublisher";

  private final DataAcquisitionProjectRepository dataAcquisitionProjectRepository;
  private final ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;
  private final ElasticsearchAdminService elasticsearchAdminService;
  private final ShadowCopyQueueItemRepository shadowCopyQueueItemRepository;
  private final JaversService javersService;
  private final DataAcquisitionProjectVersionsService versionsService;

  private final MockMvc mockMvc;

  public DataAcquisitionProjectVersionsResourceTest(
      @Autowired final DataAcquisitionProjectRepository dataAcquisitionProjectRepository,
      @Autowired final ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository,
      @Autowired final ElasticsearchAdminService elasticsearchAdminService,
      @Autowired final ShadowCopyQueueItemService shadowCopyQueueItemService,
      @Autowired final ShadowCopyQueueItemRepository shadowCopyQueueItemRepository,
      @Autowired final JaversService javersService,
      @Autowired final DataAcquisitionProjectVersionsService versionsService,
      @Value("${metadatamanagement.authmanagement.server.endpoint}")
      final String authServerEndpoint,
      @Autowired final UserApiService userApiService,
      @Autowired final WebApplicationContext wac
  ) {
      super(authServerEndpoint, userApiService);

      this.dataAcquisitionProjectRepository = dataAcquisitionProjectRepository;
      this.elasticsearchUpdateQueueItemRepository = elasticsearchUpdateQueueItemRepository;
      this.elasticsearchAdminService = elasticsearchAdminService;
      this.shadowCopyQueueItemRepository = shadowCopyQueueItemRepository;
      this.javersService = javersService;
      this.versionsService = versionsService;

      this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

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
  public void cleanUp() throws FolderException {
    dataAcquisitionProjectRepository.deleteAll();
    shadowCopyQueueItemRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
    greenMail.purgeEmailFromAllMailboxes();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateProjectAndReadVersions() throws Exception {
    this.addFindAllByLoginInRequest(Set.of(PUBLISHER_LOGIN));

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // create the dataPackage with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());

    // The defaultPublisher should have received an email
    assertEquals(1, greenMail.getReceivedMessages().length);
    assertEquals(PUBLISHER_EMAIL, greenMail.getReceivedMessages()[0].getHeader("To")[0]);

    // read the dataPackage versions
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "/versions"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(equalTo(1))))
        .andExpect(jsonPath("$[0].id", is(project.getId())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testVersionsNotFound() throws Exception {
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/spaß/versions"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testEditProjectAndReadVersions() throws Exception {
    this.addFindAllByLoginInRequest(Set.of(PUBLISHER_LOGIN));
    this.addFindAllByLoginInRequest(Set.of());

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // create the dataPackage with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());
    project.setVersion(0L);

    // The defaultPublisher should have received an email
    assertEquals(1, greenMail.getReceivedMessages().length);
    assertEquals(PUBLISHER_EMAIL, greenMail.getReceivedMessages()[0].getHeader("To")[0]);

    // update the dataPackage with the given id
    project.setHasBeenReleasedBefore(true);
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isNoContent());
    project.setVersion(1L);

    // update the dataPackage again with the given id
    project.setRelease(UnitTestCreateDomainObjectUtils.buildRelease());
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isNoContent());

    // read the dataPackage versions
    mockMvc
        .perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "/versions")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(equalTo(3))))
        .andExpect(jsonPath("$[0].id", is(project.getId())))
        .andExpect(jsonPath("$[0].hasBeenReleasedBefore", is(true)))
        .andExpect(jsonPath("$[0].version", is(equalTo(2))))
        .andExpect(jsonPath("$[0].release.version", is("1.0.0")))
        .andExpect(jsonPath("$[1].id", is(project.getId())))
        .andExpect(jsonPath("$[1].hasBeenReleasedBefore", is(true)))
        .andExpect(jsonPath("$[1].version", is(equalTo(1))))
        .andExpect(jsonPath("$[2].id", is(project.getId())))
        .andExpect(jsonPath("$[2].version", is(equalTo(0))))
        .andExpect(jsonPath("$[2].hasBeenReleasedBefore", is(false)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testReleaseCompare() throws Exception {
    this.addFindAllByLoginInRequest(Set.of(PUBLISHER_LOGIN));
    this.addFindAllByLoginInRequest(Set.of());

    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // Act
    // Save first time without release
    project.setHasBeenReleasedBefore(false);
    // project.setConfiguration(confPubAndProv);
    this.mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());

    project.setVersion(0L);
    // Assert that the last version is null
    Release lastRelease = this.versionsService.findLastRelease(project.getId());
    assertNull(lastRelease);

    // Save second time with release (simulates first release)
    project.setHasBeenReleasedBefore(true);
    project.setRelease(UnitTestCreateDomainObjectUtils.buildRelease());
    this.mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isNoContent());

    // Assert that the last version is 1.0.0
    lastRelease = this.versionsService.findLastRelease(project.getId());
    assertThat(lastRelease.getVersion(), is("1.0.0"));

    // The defaultPublisher should have received an email
    assertEquals(1, greenMail.getReceivedMessages().length);
    assertEquals(PUBLISHER_EMAIL, greenMail.getReceivedMessages()[0].getHeader("To")[0]);

    // Save third time without release (simulates unrelease)
    project.setRelease(null);
    this.mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isNoContent());

    // Assert that the last version is still 1.0.0
    lastRelease = this.versionsService.findLastRelease(project.getId());
    assertThat(lastRelease.getVersion(), is("1.0.0"));

    // Save fourth time with new release and a higher version
    project.setRelease(UnitTestCreateDomainObjectUtils.buildRelease());
    project.getRelease().setVersion("1.0.1");
    this.mockMvc
        .perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().isNoContent());

    // Assert that the last version is 1.0.1
    lastRelease = this.versionsService.findLastRelease(project.getId());
    assertThat(lastRelease.getVersion(), is("1.0.1"));

    // Assert that the previous version is 1.0.0
    lastRelease = this.versionsService.findPreviousRelease(project.getId(), project.getRelease());
    assertThat(lastRelease.getVersion(), is("1.0.0"));

    // Assert that the previous version of an unsaved release is correct
    lastRelease = this.versionsService.findPreviousRelease(project.getId(),
        Release.builder().version("2.0.0").build());
    assertThat(lastRelease.getVersion(), is("1.0.1"));
  }
}
