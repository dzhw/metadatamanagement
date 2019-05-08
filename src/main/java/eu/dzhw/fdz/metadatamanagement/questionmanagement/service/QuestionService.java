package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.data.rest.core.event.BeforeDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectReleasedEvent;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.QuestionSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.VariableChangesProvider;

/**
 * Service for creating and updating questions. Used for updating questions in mongo and
 * elasticsearch.
 */
@Service
@RepositoryEventHandler
public class QuestionService {

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Autowired
  private QuestionImageService imageService;

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private VariableChangesProvider variableChangesProvider;

  @Autowired
  private RelatedPublicationChangesProvider relatedPublicationChangesProvider;

  @Autowired
  private ShadowCopyService<Question> shadowCopyService;

  @Autowired
  private QuestionShadowCopyDataSource questionShadowCopyDataSource;

  /**
   * Delete all questions when the dataAcquisitionProject was deleted.
   *
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteQuestionsByProjectId(dataAcquisitionProject.getId());
  }

  /**
   * Update all {@link QuestionSearchDocument}s when the project is released.
   * 
   * @param dataAcquisitionProject the changed project
   */
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> questionRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()),
        ElasticsearchType.questions);
  }

  /**
   * A service method for deletion of questions within a data acquisition project.
   * 
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteQuestionsByProjectId(String dataAcquisitionProjectId) {
    try (Stream<Question> questions =
        questionRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      questions.forEach(question -> {
        if (question.isShadow()) {
          throw new ShadowCopyDeleteNotAllowedException();
        }
        eventPublisher.publishEvent(new BeforeDeleteEvent(question));
        questionRepository.delete(question);
        eventPublisher.publishEvent(new AfterDeleteEvent(question));
      });
    }
  }

  /**
   * Enqueue deletion of question search document when the question is deleted.
   *
   * @param question the deleted question.
   */
  @HandleAfterDelete
  public void onQuestionDeleted(Question question) {
    imageService.deleteQuestionImages(question.getId());
    elasticsearchUpdateQueueService.enqueue(question.getId(), ElasticsearchType.questions,
        ElasticsearchUpdateQueueAction.DELETE);
  }

  /**
   * Enqueue update of question search document when the question is updated.
   *
   * @param question the updated or created question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onQuestionSaved(Question question) {
    elasticsearchUpdateQueueService.enqueue(question.getId(), ElasticsearchType.questions,
        ElasticsearchUpdateQueueAction.UPSERT);
  }

  /**
   * Enqueue update of question search documents when the study is changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onStudyChanged(Study study) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> questionRepository.streamIdsByStudyId(study.getId()), ElasticsearchType.questions);
  }

  /**
   * Enqueue update of question search documents when the instrument is changed.
   * 
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> questionRepository.streamIdsByInstrumentId(instrument.getId()),
        ElasticsearchType.questions);
  }

  /**
   * Enqueue update of question search documents when the survey is changed.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      try (Stream<IdAndVersionProjection> instruments =
          instrumentRepository.streamIdsBySurveyIdsContaining(survey.getId())) {
        return instruments.map(instrument -> {
          return questionRepository.streamIdsByInstrumentId(instrument.getId());
        }).collect(Collectors.toList());
      }
    }, ElasticsearchType.questions);
  }

  /**
   * Enqueue update of question search document when the variable is changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    List<String> questionIds = variableChangesProvider.getAffectedQuestionIds(
        variable.getId()); 
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> questionRepository.streamIdsByIdIn(questionIds),
        ElasticsearchType.questions);
  }

  /**
   * Enqueue update of question search document when the related publication is changed.
   * 
   * @param relatedPublication the updated, created or deleted publication.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    List<String> questionIds = relatedPublicationChangesProvider.getAffectedQuestionIds(
        relatedPublication.getId()); 
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> questionRepository.streamIdsByMasterIdIn(questionIds),
        ElasticsearchType.questions);
  }
  
  /**
   * Enqueue update of question search documents when the concept is changed.
   * 
   * @param concept the updated, created or deleted concept.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onConceptChanged(Concept concept) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> questionRepository.streamIdsByConceptIdsContaining(concept.getId()), ElasticsearchType.questions);
  }

  /**
   * Create shadow copies for questions on project release.
   * @param projectReleasedEvent Released project event
   */
  @EventListener
  public void onProjectReleaseEvent(ProjectReleasedEvent projectReleasedEvent) {
    shadowCopyService.createShadowCopies(projectReleasedEvent.getDataAcquisitionProject(),
        projectReleasedEvent.getPreviousReleaseVersion(), questionShadowCopyDataSource);
  }
}
