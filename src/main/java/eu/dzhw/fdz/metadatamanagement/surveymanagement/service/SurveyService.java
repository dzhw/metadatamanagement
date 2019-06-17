package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
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
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetChangesProvider;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentChangesProvider;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectReleasedEvent;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.SurveySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.IdAndNumberSurveyProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.VariableChangesProvider;

/**
 * Service which deletes surveys when the corresponding dataAcquisitionProject has been deleted.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Service
@RepositoryEventHandler
public class SurveyService {
  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private InstrumentChangesProvider instrumentChangesProvider;

  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private VariableChangesProvider variableChangesProvider;

  @Autowired
  private DataSetChangesProvider dataSetChangesProvider;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Autowired
  private SurveyResponseRateImageService imageService;

  @Autowired
  private SurveyAttachmentService surveyAttachmentService;

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private RelatedPublicationChangesProvider relatedPublicationChangesProvider;

  @Autowired
  private ShadowCopyService<Survey> shadowCopyService;

  @Autowired
  private SurveyShadowCopyDataSource surveyShadowCopyDataSource;

  /**
   * Listener, which will be activate by a deletion of a data acquisition project.
   * 
   * @param dataAcquisitionProject A reference to the data acquisition project.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllSurveysByProjectId(dataAcquisitionProject.getId());
  }

  /**
   * Update all {@link SurveySearchDocument} when the project is released.
   * 
   * @param dataAcquisitionProject The changed project
   */
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> surveyRepository.streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()),
        ElasticsearchType.surveys);
  }

  /**
   * A service method for deletion of surveys within a data acquisition project.
   * 
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteAllSurveysByProjectId(String dataAcquisitionProjectId) {
    try (Stream<Survey> surveys =
        surveyRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      surveys.forEach(survey -> {
        if (survey.isShadow()) {
          throw new ShadowCopyDeleteNotAllowedException();
        }
        eventPublisher.publishEvent(new BeforeDeleteEvent(survey));
        surveyRepository.delete(survey);
        eventPublisher.publishEvent(new AfterDeleteEvent(survey));
      });
    }
  }

  /**
   * Enqueue deletion of survey search document when the survey is deleted.
   * 
   * @param survey the deleted survey.
   */
  @HandleAfterDelete
  public void onSurveyDeleted(Survey survey) {
    this.imageService.deleteAllSurveyImagesById(survey.getId());
    this.surveyAttachmentService.deleteAllBySurveyId(survey.getId());
    elasticsearchUpdateQueueService.enqueue(survey.getId(), ElasticsearchType.surveys,
        ElasticsearchUpdateQueueAction.DELETE);
  }

  /**
   * Enqueue update of survey search document when the survey is updated.
   * 
   * @param survey the updated or created survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onSurveySaved(Survey survey) {
    elasticsearchUpdateQueueService.enqueue(survey.getId(), ElasticsearchType.surveys,
        ElasticsearchUpdateQueueAction.UPSERT);
  }

  /**
   * Enqueue update of survey search documents when the instrument is changed.
   * 
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    List<String> surveyIds = instrumentChangesProvider.getAffectedSurveyIds(instrument.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> surveyRepository.streamIdsByIdIn(surveyIds), ElasticsearchType.surveys);
  }

  /**
   * Enqueue update of survey search documents when the question is changed.
   * 
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Optional<Instrument> optional = instrumentRepository.findById(question.getInstrumentId());
      if (optional.isPresent() && optional.get().getSurveyIds() != null) {
        return surveyRepository.streamIdsByIdIn(optional.get().getSurveyIds());
      }
      return Stream.empty();
    }, ElasticsearchType.surveys);
  }

  /**
   * Enqueue update of survey search documents when the study is changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onStudyChanged(Study study) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> surveyRepository.streamIdsByStudyId(study.getId()), ElasticsearchType.surveys);
  }

  /**
   * Enqueue update of survey search document when the variable is changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    List<String> surveyIds = variableChangesProvider.getAffectedSurveyIds(variable.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> surveyRepository.streamIdsByIdIn(surveyIds), ElasticsearchType.surveys);
  }

  /**
   * Enqueue update of survey search document when the dataSet is updated.
   * 
   * @param dataSet the updated or created dataSet.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetChanged(DataSet dataSet) {
    List<String> surveyIds = dataSetChangesProvider.getAffectedSurveyIds(dataSet.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> surveyRepository.streamIdsByIdIn(surveyIds), ElasticsearchType.surveys);
  }

  /**
   * Enqueue update of survey search document when the related publication is updated.
   * 
   * @param relatedPublication the updated or created publication.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    List<String> surveyIds =
        relatedPublicationChangesProvider.getAffectedSurveyIds(relatedPublication.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> surveyRepository.streamIdsByMasterIdIn(surveyIds), ElasticsearchType.surveys);
  }

  /**
   * Enqueue update of survey search documents when the concept is changed.
   * 
   * @param concept the updated, created or deleted concept.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onConceptChanged(Concept concept) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> surveyIds = instrumentRepository.streamIdsByConceptIdsContaining(concept.getId())
          .map(instrument -> instrument.getSurveyIds()).flatMap(List::stream)
          .collect(Collectors.toSet());
      Set<String> instrumentIds =
          questionRepository.streamIdsByConceptIdsContaining(concept.getId())
              .map(question -> question.getInstrumentId()).collect(Collectors.toSet());
      surveyIds.addAll(instrumentRepository.streamIdsByIdIn(instrumentIds)
          .map(instrument -> instrument.getSurveyIds()).flatMap(List::stream)
          .collect(Collectors.toSet()));
      return surveyRepository.streamIdsByIdIn(surveyIds);
    }, ElasticsearchType.surveys);
  }

  /**
   * Create shadow copies for {@link Survey} on project release.
   * 
   * @param projectReleasedEvent Released project event
   */
  @EventListener
  public void onProjectReleaseEvent(ProjectReleasedEvent projectReleasedEvent) {
    shadowCopyService.createShadowCopies(projectReleasedEvent.getDataAcquisitionProject(),
        projectReleasedEvent.getPreviousReleaseVersion(), surveyShadowCopyDataSource);
  }

  /**
   * Get a list of available survey numbers for creating a new survey.
   * 
   * @param dataAcquisitionProjectId The project id.
   * @return A list of available survey numbers.
   */
  public List<Integer> getFreeSurveyNumbers(String dataAcquisitionProjectId) {
    List<Integer> result = new ArrayList<>();
    List<IdAndNumberSurveyProjection> existingNumbers =
        surveyRepository.findSurveyNumbersByDataAcquisitionProjectId(dataAcquisitionProjectId);
    Optional<IdAndNumberSurveyProjection> max = existingNumbers.stream()
        .max((survey1, survey2) -> Integer.compare(survey1.getNumber(), survey2.getNumber()));
    if (!max.isPresent()) {
      result.add(1);
    } else {
      for (int i = 1; i < max.get().getNumber(); i++) {
        if (!surveyNumberExists(existingNumbers, i)) {
          result.add(i);
        }
      }
      result.add(max.get().getNumber() + 1);
    }
    return result;
  }

  private boolean surveyNumberExists(List<IdAndNumberSurveyProjection> surveys, Integer number) {
    Predicate<IdAndNumberSurveyProjection> predicate = survey -> survey.getNumber().equals(number);
    return surveys.stream().filter(predicate).findFirst().isPresent();
  }
}
