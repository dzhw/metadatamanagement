package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetChangesProvider;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentChangesProvider;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.SurveySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.IdAndNumberSurveyProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper.SurveyCrudHelper;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.VariableChangesProvider;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing the domain object/aggregate {@link Survey}.
 *
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
public class SurveyManagementService implements CrudService<Survey> {
  private final SurveyRepository surveyRepository;

  private final InstrumentChangesProvider instrumentChangesProvider;

  private final InstrumentRepository instrumentRepository;

  private final QuestionRepository questionRepository;

  private final VariableChangesProvider variableChangesProvider;

  private final DataSetChangesProvider dataSetChangesProvider;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final SurveyResponseRateImageService imageService;

  private final SurveyAttachmentService surveyAttachmentService;

  private final SurveyCrudHelper crudHelper;

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
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void deleteAllSurveysByProjectId(String dataAcquisitionProjectId) {
    // TODO check project access rights
    try (Stream<Survey> surveys =
        surveyRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      surveys.forEach(survey -> {
        imageService.deleteAllSurveyImagesById(survey.getId());
        surveyAttachmentService.deleteAllBySurveyId(survey.getId());
        crudHelper.deleteMaster(survey);
      });
    }
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
   * Enqueue update of survey search documents when the dataPackage is changed.
   *
   * @param dataPackage the updated, created or deleted dataPackage.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataPackageChanged(DataPackage dataPackage) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> surveyRepository.streamIdsByDataPackageId(dataPackage.getId()),
        ElasticsearchType.surveys);
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

  @Override
  public Optional<Survey> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void delete(Survey survey) {
    // TODO check project access rights
    crudHelper.deleteMaster(survey);
    surveyAttachmentService.deleteAllBySurveyId(survey.getId());
    imageService.deleteAllSurveyImagesById(survey.getId());
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public Survey save(Survey survey) {
    // TODO check project access rights
    return crudHelper.saveMaster(survey);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public Survey create(Survey survey) {
    // TODO check project access rights
    return crudHelper.createMaster(survey);
  }

  @Override
  public Optional<Survey> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }
}
