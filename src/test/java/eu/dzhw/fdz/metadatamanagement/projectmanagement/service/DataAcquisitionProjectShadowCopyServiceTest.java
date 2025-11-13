package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

@Disabled
@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DataAcquisitionProjectShadowCopyServiceTest extends AbstractTest {

  @Autowired
  private DataAcquisitionProjectRepository projectRepository;

  @Autowired
  private ShadowCopyQueueItemRepository shadowCopyQueueItemRepository;

  @Autowired
  private JaversService javersService;

  @Autowired
  private DataAcquisitionProjectShadowCopyService shadowCopyService;

  private Release release;

  @BeforeEach
  public void setUp() {
    release = new Release("1.0.0", LocalDateTime.now(), null, false, null, false);
    DataAcquisitionProject releasedProject =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    releasedProject.setRelease(release);
  }

  @AfterEach
  public void tearDown() {
    shadowCopyQueueItemRepository.deleteAll();
    projectRepository.deleteAll();
    javersService.deleteAll();
  }

  @Test
  public void createShadowCopy() {
    DataAcquisitionProject master = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    projectRepository.save(master);

    master.setRelease(release);

    shadowCopyService.createShadowCopies(master.getId(), master.getRelease(), null);

    List<DataAcquisitionProject> result = projectRepository.findAll();

    assertThat(result.size(), equalTo(2));

    Optional<DataAcquisitionProject> shadowCopyOpt =
        result.stream().filter(DataAcquisitionProject::isShadow).findFirst();
    assertThat(shadowCopyOpt.isPresent(), equalTo(true));

    DataAcquisitionProject shadowCopy = shadowCopyOpt.get();

    assertThat(shadowCopy.getId(), equalTo(master.getId() + "-" + release.getVersion()));
    assertThat(shadowCopy.isShadow(), equalTo(true));
    assertThat(shadowCopy.getSuccessorId(), equalTo(null));
    assertThat(shadowCopy.getMasterId(), equalTo(master.getId()));
  }

  @Test
  public void createShadowCopyWithSameReleaseVersion() {
    DataAcquisitionProject master = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    DataAcquisitionProject shadow = createShadow(master, release.getVersion());
    projectRepository.saveAll(Arrays.asList(master, shadow));

    master.setRelease(release);

    shadowCopyService.createShadowCopies(master.getId(), master.getRelease(), "1.0.0");

    long count = projectRepository.count();
    assertThat(count, equalTo(2L));

    Optional<DataAcquisitionProject> persistedShadow = projectRepository.findById(shadow.getId());
    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), nullValue());
  }

  @Test
  public void createShadowCopyLinkPredecessorToSuccessor() {
    DataAcquisitionProject master = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    DataAcquisitionProject shadow = createShadow(master, release.getVersion());
    release.setVersion("1.0.1");
    master.setRelease(release);

    projectRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(master.getId(), master.getRelease(), "1.0.0");

    List<DataAcquisitionProject> result = projectRepository.findAll().stream()
        .filter(DataAcquisitionProject::isShadow).collect(Collectors.toList());

    assertThat(result.size(), equalTo(2));

    String successorId = master.getId() + "-" + release.getVersion();

    Optional<DataAcquisitionProject> successor =
        result.stream().filter(s -> s.getId().equals(successorId)).findFirst();
    assertThat(successor.isPresent(), equalTo(true));
    assertThat(successor.get().getSuccessorId(), equalTo(null));

    Optional<DataAcquisitionProject> predecessor =
        result.stream().filter(s -> s.getId().equals(shadow.getId())).findFirst();
    assertThat(predecessor.isPresent(), equalTo(true));
    assertThat(predecessor.get().getSuccessorId(), equalTo(successorId));
  }

  @Test
  public void createShadowCopyWithDeletedMaster() {
    DataAcquisitionProject master = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    DataAcquisitionProject shadow = createShadow(master, release.getVersion());
    shadow = projectRepository.save(shadow);
    release.setVersion("1.0.1");
    master.setRelease(release);

    shadowCopyService.createShadowCopies(master.getId(), master.getRelease(), "1.0.0");

    Optional<DataAcquisitionProject> persistedShadow = projectRepository.findById(shadow.getId());

    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), equalTo("DELETED"));
  }

  private DataAcquisitionProject createShadow(DataAcquisitionProject master, String version) {
    DataAcquisitionProject shadow = new DataAcquisitionProject(master);
    shadow.setId(master.getId() + "-" + version);
    return shadow;
  }
}
