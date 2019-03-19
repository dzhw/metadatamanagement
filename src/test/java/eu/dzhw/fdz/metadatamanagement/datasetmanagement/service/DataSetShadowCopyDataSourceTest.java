package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DataSetShadowCopyDataSourceTest extends AbstractTest {

  private static final Integer SURVEY_NUMBER = 1;

  private static final String PROJECT_ID = "issue1991";

  private static final String SURVEY_ID = String.format("sur-%s-sy%d", PROJECT_ID, SURVEY_NUMBER);

  @Autowired
  private DataSetRepository dataSetRepository;

  @Autowired
  private JaversService javersService;

  private ShadowCopyService<DataSet> shadowCopyService;

  @Autowired
  private DataSetShadowCopyDataSource dataSetShadowCopyDataSource;

  @Mock
  private ApplicationEventPublisher applicationEventPublisher;

  private DataAcquisitionProject project;

  private Release release;

  @Before
  public void setUp() {
    release = new Release("1.0.0", LocalDateTime.now());
    shadowCopyService = new ShadowCopyService<>();
    DataAcquisitionProject releasedProject = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProject();
    releasedProject.setRelease(release);
    project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setRelease(release);
    project.setId(PROJECT_ID);
  }

  @After
  public void tearDown() {
    dataSetRepository.deleteAll();
    javersService.deleteAll();
  }

  @Test
  public void createShadowCopy() {
    DataSet master = UnitTestCreateDomainObjectUtils.buildDataSet(PROJECT_ID, SURVEY_ID, SURVEY_NUMBER);
    dataSetRepository.save(master);

    shadowCopyService.createShadowCopies(project, null, dataSetShadowCopyDataSource);

    List<DataSet> result = dataSetRepository.findAll();

    assertThat(result.size(), equalTo(2));

    Optional<DataSet> shadowCopyOpt = result.stream().filter(DataSet::isShadow).findFirst();
    assertThat(shadowCopyOpt.isPresent(), equalTo(true));

    DataSet shadowCopy = shadowCopyOpt.get();

    assertThat(shadowCopy.getId(), equalTo(master.getId() + "-" + release.getVersion()));
    assertThat(shadowCopy.isShadow(), equalTo(true));
    assertThat(shadowCopy.getSuccessorId(), equalTo(null));
    assertThat(shadowCopy.getMasterId(), equalTo(master.getId()));
  }

  @Test
  public void createShadowCopyWithSameReleaseVersion() {
    DataSet master = UnitTestCreateDomainObjectUtils.buildDataSet(PROJECT_ID,SURVEY_ID,SURVEY_NUMBER);
    DataSet shadow = createShadow(master, release.getVersion());
    dataSetRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project, "1.0.0", dataSetShadowCopyDataSource);

    long count = dataSetRepository.count();
    assertThat(count, equalTo(2L));

    Optional<DataSet> persistedShadow = dataSetRepository.findById(shadow.getId());
    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), nullValue());
  }

  @Test
  public void createShadowCopyLinkPredecessorToSuccessor() {
    DataSet master = UnitTestCreateDomainObjectUtils.buildDataSet(PROJECT_ID,SURVEY_ID,SURVEY_NUMBER);
    DataSet shadow = createShadow(master, release.getVersion());
    release.setVersion("1.0.1");

    dataSetRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project, "1.0.0", dataSetShadowCopyDataSource);

    List<DataSet> result = dataSetRepository.findAll().stream().filter(DataSet::isShadow)
        .collect(Collectors.toList());

    assertThat(result.size(), equalTo(2));

    String successorId = master.getId() + "-" + release.getVersion();

    Optional<DataSet> successor = result.stream().filter(s -> s.getId().equals(successorId))
        .findFirst();
    assertThat(successor.isPresent(), equalTo(true));
    assertThat(successor.get().getSuccessorId(), equalTo(null));

    Optional<DataSet> predecessor = result.stream().filter(s -> s.getId().equals(shadow.getId()))
        .findFirst();
    assertThat(predecessor.isPresent(), equalTo(true));
    assertThat(predecessor.get().getSuccessorId(), equalTo(successorId));
  }

  @Test
  public void createShadowCopyWithDeletedMaster() {
    DataSet master = UnitTestCreateDomainObjectUtils.buildDataSet(PROJECT_ID,SURVEY_ID,SURVEY_NUMBER);
    DataSet shadow = createShadow(master, release.getVersion());
    shadow = dataSetRepository.save(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(project, "1.0.0", dataSetShadowCopyDataSource);

    Optional<DataSet> persistedShadow = dataSetRepository.findById(shadow.getId());

    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), equalTo("DELETED"));
  }

  private DataSet createShadow(DataSet master, String version) {
    DataSet shadow = new DataSet(master);
    shadow.setId(master.getId() + "-" + version);
    shadow.setStudyId(master.getStudyId() + "-" + version);
    shadow.setDataAcquisitionProjectId(master.getDataAcquisitionProjectId() + "-" + version);
    return shadow;
  }
}