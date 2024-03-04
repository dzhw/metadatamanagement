package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.ShadowCopyQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DataAcquisitionProjectVersionsServiceTest extends AbstractTest {
  @Autowired
  private DataAcquisitionProjectVersionsService versionsService;

  @Autowired
  private ShadowCopyQueueItemRepository shadowCopyQueueItemRepository;

  @Autowired
  private DataAcquisitionProjectManagementService projectManagementService;

  @Autowired
  private DataAcquisitionProjectRepository repository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private JaversService javersService;

  @AfterEach
  public void tearDown() {
    shadowCopyQueueItemRepository.deleteAll();
    repository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  public void testMultipleReleases() {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project = projectManagementService.save(project);

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
