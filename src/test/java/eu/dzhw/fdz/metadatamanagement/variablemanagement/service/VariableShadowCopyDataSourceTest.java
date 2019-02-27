package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class VariableShadowCopyDataSourceTest extends AbstractTest {

  private static final String PROJECT_ID = "issue1991";

  @Autowired
  private VariableRepository variableRepository;

  @Autowired
  private JaversService javersService;

  private ShadowCopyService<Variable> shadowCopyService;

  @Autowired
  private VariableShadowCopyDataSource shadowCopyDataSource;

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
    variableRepository.deleteAll();
    javersService.deleteAll();
  }

  @Test
  public void createShadowCopy() {
    Variable master = buildVariable();
    variableRepository.save(master);

    shadowCopyService.createShadowCopies(project, null, shadowCopyDataSource);

    List<Variable> result = variableRepository.findAll();

    assertThat(result.size(), equalTo(2));

    Optional<Variable> shadowCopyOpt = result.stream().filter(Variable::isShadow).findFirst();
    assertThat(shadowCopyOpt.isPresent(), equalTo(true));

    Variable shadowCopy = shadowCopyOpt.get();

    assertThat(shadowCopy.getId(), equalTo(master.getId() + "-" + release.getVersion()));
    assertThat(shadowCopy.isShadow(), equalTo(true));
    assertThat(shadowCopy.getSuccessorId(), equalTo(null));
    assertThat(shadowCopy.getMasterId(), equalTo(master.getId()));
  }

  @Test
  public void createShadowCopyWithSameReleaseVersion() {
    Variable master = buildVariable();
    Variable shadow = createShadow(master, release.getVersion());
    variableRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project, "1.0.0", shadowCopyDataSource);

    long count = variableRepository.count();
    assertThat(count, equalTo(2L));

    Optional<Variable> persistedShadow = variableRepository.findById(shadow.getId());
    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), nullValue());
  }

  @Test
  public void createShadowCopyLinkPredecessorToSuccessor() {
    Variable master = buildVariable();
    Variable shadow = createShadow(master, release.getVersion());
    release.setVersion("1.0.1");

    variableRepository.saveAll(Arrays.asList(master, shadow));

    shadowCopyService.createShadowCopies(project, "1.0.0", shadowCopyDataSource);

    List<Variable> result = variableRepository.findAll().stream().filter(Variable::isShadow)
        .collect(Collectors.toList());

    assertThat(result.size(), equalTo(2));

    String successorId = master.getId() + "-" + release.getVersion();

    Optional<Variable> successor = result.stream().filter(s -> s.getId().equals(successorId))
        .findFirst();
    assertThat(successor.isPresent(), equalTo(true));
    assertThat(successor.get().getSuccessorId(), equalTo(null));

    Optional<Variable> predecessor = result.stream().filter(s -> s.getId().equals(shadow.getId()))
        .findFirst();
    assertThat(predecessor.isPresent(), equalTo(true));
    assertThat(predecessor.get().getSuccessorId(), equalTo(successorId));
  }

  @Test
  public void createShadowCopyWithDeletedMaster() {
    Variable master = buildVariable();
    Variable shadow = createShadow(master, release.getVersion());
    shadow = variableRepository.save(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(project, "1.0.0", shadowCopyDataSource);

    Optional<Variable> persistedShadow = variableRepository.findById(shadow.getId());

    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), equalTo("DELETED"));
  }

  private Variable createShadow(Variable master, String version) {
    Variable shadow = new Variable(master);
    shadow.setId(master.getId() + "-" + version);
    shadow.setDataAcquisitionProjectId(master.getDataAcquisitionProjectId() + "-" + version);
    shadow.setStudyId(master.getStudyId() + "-" + version);
    shadow.setDataSetId(master.getDataSetId() + "-" + version);
    return shadow;
  }

  private Variable buildVariable() {
    return UnitTestCreateDomainObjectUtils.buildVariable(PROJECT_ID, 1, "testVariable",1, Collections.singletonList(1));
  }
}