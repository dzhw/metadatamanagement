package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class InstrumentShadowCopyDataSourceTest extends AbstractTest {

  private static final String PROJECT_ID = "issue1991";

  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
  private JaversService javersService;

  private ShadowCopyService<Instrument> shadowCopyService;

  @Autowired
  private InstrumentShadowCopyDataSource shadowCopyDataSource;
  
  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Mock
  private DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

  private DataAcquisitionProject project;

  private Release release;

  @Before
  public void setUp() {
    release = new Release("1.0.0", LocalDateTime.now());
    shadowCopyService = new ShadowCopyService<>(dataAcquisitionProjectVersionsService);
    DataAcquisitionProject releasedProject = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProject();
    releasedProject.setRelease(release);
    project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setRelease(release);
    project.setId(PROJECT_ID);
  }

  @After
  public void tearDown() {
    instrumentRepository.deleteAll();
    javersService.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
  }

  @Test
  public void createShadowCopy() {
    Instrument master = UnitTestCreateDomainObjectUtils.buildInstrument(PROJECT_ID);
    instrumentRepository.save(master);

    shadowCopyService.createShadowCopies(project, null, shadowCopyDataSource);

    List<Instrument> result = instrumentRepository.findAll();

    assertThat(result.size(), equalTo(2));

    Optional<Instrument> shadowCopyOpt = result.stream().filter(Instrument::isShadow).findFirst();
    assertThat(shadowCopyOpt.isPresent(), equalTo(true));

    Instrument shadowCopy = shadowCopyOpt.get();

    assertThat(shadowCopy.getId(), equalTo(master.getId() + "-" + release.getVersion()));
    assertThat(shadowCopy.isShadow(), equalTo(true));
    assertThat(shadowCopy.getSuccessorId(), equalTo(null));
    assertThat(shadowCopy.getMasterId(), equalTo(master.getId()));
  }

  @Test
  public void createShadowCopyWithSameReleaseVersion() {
    Instrument master = UnitTestCreateDomainObjectUtils.buildInstrument(PROJECT_ID);
    Instrument shadow = createShadow(master, release.getVersion());
    instrumentRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project, "1.0.0", shadowCopyDataSource);

    long count = instrumentRepository.count();
    assertThat(count, equalTo(2L));

    Optional<Instrument> persistedShadow = instrumentRepository.findById(shadow.getId());
    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), nullValue());
  }

  @Test
  public void createShadowCopyLinkPredecessorToSuccessor() {
    Instrument master = UnitTestCreateDomainObjectUtils.buildInstrument(PROJECT_ID);
    Instrument shadow = createShadow(master, release.getVersion());
    release.setVersion("1.0.1");

    instrumentRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project, "1.0.0", shadowCopyDataSource);

    List<Instrument> result = instrumentRepository.findAll().stream().filter(Instrument::isShadow)
        .collect(Collectors.toList());

    assertThat(result.size(), equalTo(2));

    String successorId = master.getId() + "-" + release.getVersion();

    Optional<Instrument> successor = result.stream().filter(s -> s.getId().equals(successorId))
        .findFirst();
    assertThat(successor.isPresent(), equalTo(true));
    assertThat(successor.get().getSuccessorId(), equalTo(null));

    Optional<Instrument> predecessor = result.stream().filter(s -> s.getId().equals(shadow.getId()))
        .findFirst();
    assertThat(predecessor.isPresent(), equalTo(true));
    assertThat(predecessor.get().getSuccessorId(), equalTo(successorId));
  }

  @Test
  public void createShadowCopyWithDeletedMaster() {
    Instrument master = UnitTestCreateDomainObjectUtils.buildInstrument(PROJECT_ID);
    Instrument shadow = createShadow(master, release.getVersion());
    shadow = instrumentRepository.save(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(project, "1.0.0", shadowCopyDataSource);

    Optional<Instrument> persistedShadow = instrumentRepository.findById(shadow.getId());

    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), equalTo("DELETED"));
  }

  private Instrument createShadow(Instrument master, String version) {
    Instrument shadow = new Instrument(master);
    shadow.setId(master.getId() + "-" + version);
    shadow.setStudyId(master.getStudyId() + "-" + version);
    shadow.setDataAcquisitionProjectId(master.getDataAcquisitionProjectId() + "-" + version);
    return shadow;
  }
}