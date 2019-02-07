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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class SurveyShadowCopyTest extends AbstractTest {

  private static final String PROJECT_ID = "project1991";

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private JaversService javersService;

  private ShadowCopyService surveyShadowCopyService;

  @Autowired
  private SurveyShadowCopyDataProvider surveyShadowCopyDataProvider;

  private DataAcquisitionProject project;

  private Release release = new Release("1.0.0", LocalDateTime.now());

  @Before
  public void setUp() {
    surveyShadowCopyService = new ShadowCopyService();
    DataAcquisitionProject unreleasedProject = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProject();
    unreleasedProject.setRelease(null);
    DataAcquisitionProject releasedProject = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProject();
    releasedProject.setRelease(release);
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

    surveyShadowCopyService.createShadowCopies(PROJECT_ID, release.getVersion(),
        surveyShadowCopyDataProvider);

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
  public void createShadowCopy_link_predecessor_to_successor() {
    Survey master = UnitTestCreateDomainObjectUtils.buildSurvey(PROJECT_ID);

    String shadowId = createSurveyId(master.getNumber(), release.getVersion());

    Survey shadow = UnitTestCreateDomainObjectUtils.buildSurvey(PROJECT_ID);
    shadow.setShadow(true);
    shadow.setId(shadowId);

    release.setVersion("1.0.1");
    surveyRepository.saveAll(Arrays.asList(master, shadow));

    surveyShadowCopyService.createShadowCopies(PROJECT_ID, release.getVersion(),
        surveyShadowCopyDataProvider);

    List<Survey> result = surveyRepository.findAll().stream().filter(Survey::isShadow)
        .collect(Collectors.toList());

    assertThat(result.size(), equalTo(2));

    String successorId = createSurveyId(master.getNumber(), release.getVersion());

    Optional<Survey> successor = result.stream().filter(s -> s.getId().equals(successorId))
        .findFirst();
    assertThat(successor.isPresent(), equalTo(true));
    assertThat(successor.get().getSuccessorId(), equalTo(null));

    Optional<Survey> predecessor = result.stream().filter(s -> s.getId().equals(shadowId))
        .findFirst();
    assertThat(predecessor.isPresent(), equalTo(true));
    assertThat(predecessor.get().getSuccessorId(), equalTo(successorId));
  }

  @Test
  public void writeDeletedSuccessorIdToShadowCopiesWithoutSuccessorId() {
    Survey shadow = UnitTestCreateDomainObjectUtils.buildSurvey(PROJECT_ID);
    String shadowId = createSurveyId(shadow.getNumber(), release.getVersion());
    shadow.setShadow(true);
    shadow.setId(shadowId);
    surveyRepository.save(shadow);

    surveyShadowCopyService.createShadowCopies(PROJECT_ID, release.getVersion(),
        surveyShadowCopyDataProvider);

    Optional<Survey> persistedShadow = surveyRepository.findById(shadowId);
    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), equalTo("DELETED"));
  }

  private static String createSurveyId(Integer number, String version) {
    String versionSuffix = version != null && !version.isEmpty() ? "-" + version : "";
    return String.format("sur-%s-sy%d$%s", PROJECT_ID, number, versionSuffix);
  }

}