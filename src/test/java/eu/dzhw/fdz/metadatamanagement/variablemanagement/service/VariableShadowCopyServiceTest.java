package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class VariableShadowCopyServiceTest extends AbstractTest {

  private static final String PROJECT_ID = "issue1991";

  @Autowired
  private VariableRepository variableRepository;

  @Autowired
  private JaversService javersService;

  @Autowired
  private VariableShadowCopyService shadowCopyService;

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
    variableRepository.deleteAll();
    javersService.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
  }

  @Test
  public void createShadowCopy() {
    Variable master = buildVariable();
    variableRepository.save(master);

    shadowCopyService.createShadowCopies(project.getId(), project.getRelease(), null);

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

    shadowCopyService.createShadowCopies(project.getId(), project.getRelease(),
        "1.0.0");

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

    shadowCopyService.createShadowCopies(project.getId(), project.getRelease(),
        "1.0.0");

    List<Variable> result = variableRepository.findAll().stream().filter(Variable::isShadow)
        .collect(Collectors.toList());

    assertThat(result.size(), equalTo(2));

    String successorId = master.getId() + "-" + release.getVersion();

    Optional<Variable> successor =
        result.stream().filter(s -> s.getId().equals(successorId)).findFirst();
    assertThat(successor.isPresent(), equalTo(true));
    assertThat(successor.get().getSuccessorId(), equalTo(null));

    Optional<Variable> predecessor =
        result.stream().filter(s -> s.getId().equals(shadow.getId())).findFirst();
    assertThat(predecessor.isPresent(), equalTo(true));
    assertThat(predecessor.get().getSuccessorId(), equalTo(successorId));
  }

  @Test
  public void createShadowCopyWithDeletedMaster() {
    Variable master = buildVariable();
    Variable shadow = createShadow(master, release.getVersion());
    shadow = variableRepository.save(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(project.getId(), project.getRelease(),
        "1.0.0");

    Optional<Variable> persistedShadow = variableRepository.findById(shadow.getId());

    assertThat(persistedShadow.isPresent(), equalTo(true));
    assertThat(persistedShadow.get().getSuccessorId(), equalTo("DELETED"));
  }

  private Variable createShadow(Variable master, String version) {
    Variable shadow = new Variable(master);
    shadow.setId(master.getId() + "-" + version);
    shadow.setDataAcquisitionProjectId(master.getDataAcquisitionProjectId() + "-" + version);
    shadow.setDataPackageId(master.getDataPackageId() + "-" + version);
    shadow.setDataSetId(master.getDataSetId() + "-" + version);
    return shadow;
  }

  private Variable buildVariable() {
    return UnitTestCreateDomainObjectUtils.buildVariable(PROJECT_ID, 1, "testVariable", 1,
        Collections.singletonList(1));
  }
}
