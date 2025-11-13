package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
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
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DataAcquisitionProjectVersionsResourceTest extends AbstractTest {
  private static final String API_DATA_ACQUISITION_PROJECTS_URI = "/api/data-acquisition-projects";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private JaversService javersService;

  @Autowired
  private DataAcquisitionProjectVersionsService versionsService;

  @Autowired
  private ShadowCopyQueueItemRepository shadowCopyQueueItemRepository;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    shadowCopyQueueItemRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateProjectAndReadVersions() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // create the dataPackage with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());

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
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    // create the dataPackage with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(project))).andExpect(status().isCreated());
    project.setVersion(0L);

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
        .andExpect(status().isNoContent());;

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
