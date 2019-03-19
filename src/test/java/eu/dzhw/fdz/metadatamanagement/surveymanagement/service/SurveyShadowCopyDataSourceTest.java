package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
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
public class SurveyShadowCopyDataSourceTest extends AbstractTest {

  private static final String PROJECT_ID = "issue1991";

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private JaversService javersService;

  private ShadowCopyService<Survey> shadowCopyService;

  @Autowired
  private SurveyShadowCopyDataSource surveyShadowCopyDataProvider;

  @Mock
  private ApplicationEventPublisher applicationEventPublisher;

  private DataAcquisitionProject project;

  private Release release;

  @Before
  public void setUp() {
    release = new Release("1.0.0", LocalDateTime.now());
    shadowCopyService = new ShadowCopyService<>();
    DataAcquisitionProject unreleasedProject = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProject();
    unreleasedProject.setRelease(null);
    DataAcquisitionProject releasedProject = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProject();
    releasedProject.setRelease(release);
    project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.setRelease(release);
    project.setId(PROJECT_ID);
  }

  @After
  public void tearDown() {
    surveyRepository.deleteAll();
    javersService.deleteAll();
  }

  @Test
  public void createShadowCopy() {
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(PROJECT_ID);
    surveyRepository.save(survey);

    shadowCopyService.createShadowCopies(project, null, surveyShadowCopyDataProvider);

    List<Survey> result = surveyRepository.findAll();

    assertThat(result.size(), equalTo(2));

    Optional<Survey> shadowCopyOpt = result.stream().filter(Survey::isShadow).findFirst();
    assertThat(shadowCopyOpt.isPresent(), equalTo(true));

    Survey shadowCopy = shadowCopyOpt.get();

    assertThat(shadowCopy.getId(), equalTo(createSurveyId(survey.getNumber(), release
        .getVersion())));
    assertThat(shadowCopy.isShadow(), equalTo(true));
    assertThat(shadowCopy.getSuccessorId(), equalTo(null));
    assertThat(shadowCopy.getMasterId(), equalTo(survey.getId()));
  }

  @Test
  public void createShadowCopyWithSameReleaseVersion() {
    Survey master = UnitTestCreateDomainObjectUtils.buildSurvey(PROJECT_ID);
    Survey shadow = createShadow(master, release.getVersion());
    surveyRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project, "1.0.0", surveyShadowCopyDataProvider);

    long count = surveyRepository.count();
    assertThat(count, equalTo(2L));

    Optional<Survey> persistedShadow = surveyRepository.findById(shadow.getId());
    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), nullValue());
  }

  @Test
  public void createShadowCopyLinkPredecessorToSuccessor() {
    Survey master = UnitTestCreateDomainObjectUtils.buildSurvey(PROJECT_ID);
    Survey shadow = createShadow(master, release.getVersion());
    release.setVersion("1.0.1");

    surveyRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project, "1.0.0", surveyShadowCopyDataProvider);

    List<Survey> result = surveyRepository.findAll().stream().filter(Survey::isShadow)
        .collect(Collectors.toList());

    assertThat(result.size(), equalTo(2));

    String successorId = createSurveyId(master.getNumber(), release.getVersion());

    Optional<Survey> successor = result.stream().filter(s -> s.getId().equals(successorId))
        .findFirst();
    assertThat(successor.isPresent(), equalTo(true));
    assertThat(successor.get().getSuccessorId(), equalTo(null));

    Optional<Survey> predecessor = result.stream().filter(s -> s.getId().equals(shadow.getId()))
        .findFirst();
    assertThat(predecessor.isPresent(), equalTo(true));
    assertThat(predecessor.get().getSuccessorId(), equalTo(successorId));
  }

  @Test
  public void createShadowCopyWithDeletedMaster() {
    Survey master = UnitTestCreateDomainObjectUtils.buildSurvey(PROJECT_ID);
    Survey shadow = createShadow(master, release.getVersion());
    shadow = surveyRepository.save(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(project, "1.0.0", surveyShadowCopyDataProvider);

    Optional<Survey> persistedShadow = surveyRepository.findById(shadow.getId());

    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), equalTo("DELETED"));
  }

  private static String createSurveyId(Integer number, String version) {
    String versionSuffix = version != null && !version.isEmpty() ? "-" + version : "";
    return String.format("sur-%s-sy%d$%s", PROJECT_ID, number, versionSuffix);
  }

  private Survey createShadow(Survey master, String version) {
    Survey shadow = new Survey(master);
    shadow.setId(master.getId() + "-" + version);
    shadow.setStudyId(master.getStudyId() + "-" + version);
    shadow.setDataAcquisitionProjectId(master.getDataAcquisitionProjectId() + "-" + version);
    return shadow;
  }

}