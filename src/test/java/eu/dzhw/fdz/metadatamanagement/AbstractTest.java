package eu.dzhw.fdz.metadatamanagement;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import eu.dzhw.fdz.metadatamanagement.common.config.audit.AuditorStore;
import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.repository.TaskRepository;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DaraUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.ShadowCopyQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * This class is a basic class for the most unit tests.
 *
 * @author Daniel Katzberg
 *
 */
@SpringBootTest(classes = Application.class)
@TestPropertySource("/config/application.yml")
@ActiveProfiles(Constants.SPRING_PROFILE_UNITTEST)
@WebAppConfiguration
public abstract class AbstractTest {

  @Autowired
  private DataPackageRepository dataPackageRepository;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private DataSetRepository dataSetRepository;

  @Autowired
  private VariableRepository variableRepository;

  @Autowired
  private ConceptRepository conceptRepository;

  @Autowired
  private RelatedPublicationRepository relatedPublicationRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private DaraUpdateQueueItemRepository daraUpdateQueueItemRepository;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private ShadowCopyQueueItemRepository shadowCopyQueueItemRepository;

  @Autowired
  private Javers javers;

  @Autowired
  private TaskRepository taskRepository;

  protected static GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP.withPort(4025));

  static {
    greenMail.start();
  }

  @AfterEach
  public void ensureAllDataStoresHaveBeenCleanedUp() {
    assertEquals(0, this.dataPackageRepository.count());
    assertEquals(0, this.surveyRepository.count());
    assertEquals(0, this.instrumentRepository.count());
    assertEquals(0, this.questionRepository.count());
    assertEquals(0, this.dataSetRepository.count());
    assertEquals(0, this.variableRepository.count());
    assertEquals(0, this.relatedPublicationRepository.count());
    assertEquals(0, this.conceptRepository.count());
    assertEquals(0, this.taskRepository.count());
    assertEquals(0, this.elasticsearchUpdateQueueItemRepository.count());
    assertEquals(0, this.daraUpdateQueueItemRepository.count());
    assertEquals(0, this.javers.findSnapshots(QueryBuilder.anyDomainObject().build()).size());
    assertThat(this.elasticsearchAdminService.countAllDocuments(), equalTo(0L));
    assertEquals(0, this.shadowCopyQueueItemRepository.count());
    assertEquals(0, this.dataAcquisitionProjectRepository.count());
    assertEquals(0, greenMail.getReceivedMessages().length);
  }
}
