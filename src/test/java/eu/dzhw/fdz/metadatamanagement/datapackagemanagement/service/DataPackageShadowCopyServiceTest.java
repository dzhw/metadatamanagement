package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DataPackageShadowCopyServiceTest extends AbstractTest {

  private static final String PROJECT_ID = "issue1991";

  @Autowired
  private DataPackageRepository dataPackageRepository;

  @Autowired
  private JaversService javersService;

  @Autowired
  private DataPackageShadowCopyService shadowCopyService;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  private DataAcquisitionProject project;

  private Release release;

  @BeforeEach
  public void setUp() {
    release = new Release("1.0.0", LocalDateTime.now(), null, false);
    DataAcquisitionProject releasedProject =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    releasedProject.setRelease(release);
    project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setRelease(release);
    project.setId(PROJECT_ID);
  }

  @AfterEach
  public void tearDown() {
    dataPackageRepository.deleteAll();
    javersService.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
  }

  @Test
  public void createShadowCopy() {
    DataPackage master = UnitTestCreateDomainObjectUtils.buildDataPackage(PROJECT_ID);
    dataPackageRepository.save(master);

    shadowCopyService.createShadowCopies(project.getId(), project.getRelease(), null);

    List<DataPackage> result = dataPackageRepository.findAll();

    assertThat(result.size(), equalTo(2));

    Optional<DataPackage> shadowCopyOpt = result.stream().filter(DataPackage::isShadow).findFirst();
    assertThat(shadowCopyOpt.isPresent(), equalTo(true));

    DataPackage shadowCopy = shadowCopyOpt.get();

    assertThat(shadowCopy.getId(), equalTo(master.getId() + "-" + release.getVersion()));
    assertThat(shadowCopy.isShadow(), equalTo(true));
    assertThat(shadowCopy.getSuccessorId(), equalTo(null));
    assertThat(shadowCopy.getMasterId(), equalTo(master.getId()));
  }

  @Test
  public void createShadowCopyWithSameReleaseVersion() {
    DataPackage master = UnitTestCreateDomainObjectUtils.buildDataPackage(PROJECT_ID);
    DataPackage shadow = createShadow(master, release.getVersion());
    dataPackageRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project.getId(), project.getRelease(),
        "1.0.0");

    long count = dataPackageRepository.count();
    assertThat(count, equalTo(2L));

    Optional<DataPackage> persistedShadow = dataPackageRepository.findById(shadow.getId());
    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), nullValue());
  }

  @Test
  public void createShadowCopyLinkPredecessorToSuccessor() {
    DataPackage master = UnitTestCreateDomainObjectUtils.buildDataPackage(PROJECT_ID);
    DataPackage shadow = createShadow(master, release.getVersion());
    release.setVersion("1.0.1");

    dataPackageRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project.getId(), project.getRelease(),
        "1.0.0");

    List<DataPackage> result =
        dataPackageRepository.findAll().stream().filter(DataPackage::isShadow).collect(Collectors.toList());

    assertThat(result.size(), equalTo(2));

    String successorId = master.getId() + "-" + release.getVersion();

    Optional<DataPackage> successor =
        result.stream().filter(s -> s.getId().equals(successorId)).findFirst();
    assertThat(successor.isPresent(), equalTo(true));
    assertThat(successor.get().getSuccessorId(), equalTo(null));

    Optional<DataPackage> predecessor =
        result.stream().filter(s -> s.getId().equals(shadow.getId())).findFirst();
    assertThat(predecessor.isPresent(), equalTo(true));
    assertThat(predecessor.get().getSuccessorId(), equalTo(successorId));
  }

  @Test
  public void createShadowCopyWithDeletedMaster() {
    DataPackage master = UnitTestCreateDomainObjectUtils.buildDataPackage(PROJECT_ID);
    DataPackage shadow = createShadow(master, release.getVersion());
    shadow = dataPackageRepository.save(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(project.getId(), project.getRelease(),
        "1.0.0");

    Optional<DataPackage> persistedShadow = dataPackageRepository.findById(shadow.getId());

    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), equalTo("DELETED"));
  }

  private DataPackage createShadow(DataPackage master, String version) {
    DataPackage shadow = new DataPackage(master);
    shadow.setId(master.getId() + "-" + version);
    shadow.setDataAcquisitionProjectId(master.getDataAcquisitionProjectId() + "-" + version);
    return shadow;
  }
}
