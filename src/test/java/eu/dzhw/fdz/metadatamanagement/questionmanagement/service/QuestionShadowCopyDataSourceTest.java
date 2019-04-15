package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
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
public class QuestionShadowCopyDataSourceTest extends AbstractTest {

  private static final String PROJECT_ID = "issue1991";

  private static final Integer INSTRUMENT_NUMBER = 1;

  private static final String INSTRUMENT_ID = String.format("ins-%s-ins%d", PROJECT_ID, INSTRUMENT_NUMBER);

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private JaversService javersService;

  private ShadowCopyService<Question> shadowCopyService;

  @Autowired
  private QuestionShadowCopyDataSource shadowCopyDataSource;

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
    questionRepository.deleteAll();
    javersService.deleteAll();
  }

  @Test
  public void createShadowCopy() {
    Question master = UnitTestCreateDomainObjectUtils.buildQuestion(PROJECT_ID, INSTRUMENT_NUMBER, INSTRUMENT_ID);
    questionRepository.save(master);

    shadowCopyService.createShadowCopies(project, null, shadowCopyDataSource);

    List<Question> result = questionRepository.findAll();

    assertThat(result.size(), equalTo(2));

    Optional<Question> shadowCopyOpt = result.stream().filter(Question::isShadow).findFirst();
    assertThat(shadowCopyOpt.isPresent(), equalTo(true));

    Question shadowCopy = shadowCopyOpt.get();

    assertThat(shadowCopy.getId(), equalTo(master.getId() + "-" + release.getVersion()));
    assertThat(shadowCopy.isShadow(), equalTo(true));
    assertThat(shadowCopy.getSuccessorId(), equalTo(null));
    assertThat(shadowCopy.getMasterId(), equalTo(master.getId()));
  }

  @Test
  public void createShadowCopyWithSameReleaseVersion() {
    Question master = UnitTestCreateDomainObjectUtils.buildQuestion(PROJECT_ID, INSTRUMENT_NUMBER, INSTRUMENT_ID);
    Question shadow = createShadow(master, release.getVersion());
    questionRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project, "1.0.0", shadowCopyDataSource);

    long count = questionRepository.count();
    assertThat(count, equalTo(2L));

    Optional<Question> persistedShadow = questionRepository.findById(shadow.getId());
    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), nullValue());
  }

  @Test
  public void createShadowCopyLinkPredecessorToSuccessor() {
    Question master = UnitTestCreateDomainObjectUtils.buildQuestion(PROJECT_ID, INSTRUMENT_NUMBER, INSTRUMENT_ID);
    Question shadow = createShadow(master, release.getVersion());
    release.setVersion("1.0.1");

    questionRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project, "1.0.0", shadowCopyDataSource);

    List<Question> result = questionRepository.findAll().stream().filter(Question::isShadow)
        .collect(Collectors.toList());

    assertThat(result.size(), equalTo(2));

    String successorId = master.getId() + "-" + release.getVersion();

    Optional<Question> successor = result.stream().filter(s -> s.getId().equals(successorId))
        .findFirst();
    assertThat(successor.isPresent(), equalTo(true));
    assertThat(successor.get().getSuccessorId(), equalTo(null));

    Optional<Question> predecessor = result.stream().filter(s -> s.getId().equals(shadow.getId()))
        .findFirst();
    assertThat(predecessor.isPresent(), equalTo(true));
    assertThat(predecessor.get().getSuccessorId(), equalTo(successorId));
  }

  @Test
  public void createShadowCopyWithDeletedMaster() {
    Question master = UnitTestCreateDomainObjectUtils.buildQuestion(PROJECT_ID, INSTRUMENT_NUMBER, INSTRUMENT_ID);
    Question shadow = createShadow(master, release.getVersion());
    shadow = questionRepository.save(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(project, "1.0.0", shadowCopyDataSource);

    Optional<Question> persistedShadow = questionRepository.findById(shadow.getId());

    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), equalTo("DELETED"));
  }

  private Question createShadow(Question master, String version) {
    Question shadow = new Question(master);
    shadow.setId(master.getId() + "-" + version);
    shadow.setStudyId(master.getStudyId() + "-" + version);
    shadow.setDataAcquisitionProjectId(master.getDataAcquisitionProjectId() + "-" + version);
    shadow.setInstrumentId(master.getInstrumentId() + "-" + version);
    return shadow;
  }
}