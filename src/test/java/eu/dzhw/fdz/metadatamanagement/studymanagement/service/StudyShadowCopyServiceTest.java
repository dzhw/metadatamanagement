package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class StudyShadowCopyServiceTest extends AbstractTest {

  private static final String PROJECT_ID = "issue1991";

  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private JaversService javersService;

  @Autowired
  private StudyShadowCopyService shadowCopyService;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;
  
  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  private DataAcquisitionProject project;

  private Release release;

  @Before
  public void setUp() {
    release = new Release("1.0.0", LocalDateTime.now(), null);
    DataAcquisitionProject releasedProject =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    releasedProject.setRelease(release);
    project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setRelease(release);
    project.setId(PROJECT_ID);
  }

  @After
  public void tearDown() {
    studyRepository.deleteAll();
    javersService.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
  }

  @Test
  public void createShadowCopy() {
    Study master = UnitTestCreateDomainObjectUtils.buildStudy(PROJECT_ID);
    studyRepository.save(master);

    shadowCopyService.createShadowCopies(project.getId(), project.getRelease().getVersion(), null);

    List<Study> result = studyRepository.findAll();

    assertThat(result.size(), equalTo(2));

    Optional<Study> shadowCopyOpt = result.stream().filter(Study::isShadow).findFirst();
    assertThat(shadowCopyOpt.isPresent(), equalTo(true));

    Study shadowCopy = shadowCopyOpt.get();

    assertThat(shadowCopy.getId(), equalTo(master.getId() + "-" + release.getVersion()));
    assertThat(shadowCopy.isShadow(), equalTo(true));
    assertThat(shadowCopy.getSuccessorId(), equalTo(null));
    assertThat(shadowCopy.getMasterId(), equalTo(master.getId()));
  }

  @Test
  public void createShadowCopyWithSameReleaseVersion() {
    Study master = UnitTestCreateDomainObjectUtils.buildStudy(PROJECT_ID);
    Study shadow = createShadow(master, release.getVersion());
    studyRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project.getId(), project.getRelease().getVersion(),
        "1.0.0");

    long count = studyRepository.count();
    assertThat(count, equalTo(2L));

    Optional<Study> persistedShadow = studyRepository.findById(shadow.getId());
    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), nullValue());
  }

  @Test
  public void createShadowCopyLinkPredecessorToSuccessor() {
    Study master = UnitTestCreateDomainObjectUtils.buildStudy(PROJECT_ID);
    Study shadow = createShadow(master, release.getVersion());
    release.setVersion("1.0.1");

    studyRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project.getId(), project.getRelease().getVersion(),
        "1.0.0");

    List<Study> result =
        studyRepository.findAll().stream().filter(Study::isShadow).collect(Collectors.toList());

    assertThat(result.size(), equalTo(2));

    String successorId = master.getId() + "-" + release.getVersion();

    Optional<Study> successor =
        result.stream().filter(s -> s.getId().equals(successorId)).findFirst();
    assertThat(successor.isPresent(), equalTo(true));
    assertThat(successor.get().getSuccessorId(), equalTo(null));

    Optional<Study> predecessor =
        result.stream().filter(s -> s.getId().equals(shadow.getId())).findFirst();
    assertThat(predecessor.isPresent(), equalTo(true));
    assertThat(predecessor.get().getSuccessorId(), equalTo(successorId));
  }

  @Test
  public void createShadowCopyWithDeletedMaster() {
    Study master = UnitTestCreateDomainObjectUtils.buildStudy(PROJECT_ID);
    Study shadow = createShadow(master, release.getVersion());
    shadow = studyRepository.save(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(project.getId(), project.getRelease().getVersion(),
        "1.0.0");

    Optional<Study> persistedShadow = studyRepository.findById(shadow.getId());

    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), equalTo("DELETED"));
  }

  private Study createShadow(Study master, String version) {
    Study shadow = new Study(master);
    shadow.setId(master.getId() + "-" + version);
    shadow.setDataAcquisitionProjectId(master.getDataAcquisitionProjectId() + "-" + version);
    return shadow;
  }
}
