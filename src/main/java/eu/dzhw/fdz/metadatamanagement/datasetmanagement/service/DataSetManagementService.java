package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

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
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.IdAndNumberDataSetProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper.DataSetCrudHelper;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing the domain object/aggregate {@link DataSet}.
 *
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
public class DataSetManagementService implements CrudService<DataSet> {

  private final DataSetRepository dataSetRepository;

  private final QuestionRepository questionRepository;

  private final VariableRepository variableRepository;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final DataSetAttachmentService dataSetAttachmentService;

  private final DataSetCrudHelper crudHelper;

  /**
   * Delete all data sets when the dataAcquisitionProject was deleted.
   *
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteDataSetsByProjectId(dataAcquisitionProject.getId());
  }

  /**
   * Update the {@link DataSet}s of the project when the project is released.
   *
   * @param dataAcquisitionProject the changed project
   */
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> dataSetRepository.streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()),
        ElasticsearchType.data_sets);
  }

  /**
   * A service method for deletion of dataSets within a data acquisition project.
   *
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void deleteDataSetsByProjectId(String dataAcquisitionProjectId) {
    try (Stream<DataSet> dataSets =
        dataSetRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      dataSets.forEach(dataSet -> {
        dataSetAttachmentService.deleteAllByDataSetId(dataSet.getId());
        crudHelper.deleteMaster(dataSet);
      });
    }
  }

  /**
   * Enqueue update of dataSet search documents when the dataPackage is changed.
   *
   * @param dataPackage the updated, created or deleted dataPackage.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataPackageChanged(DataPackage dataPackage) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> dataSetRepository.streamIdsByDataPackageId(dataPackage.getId()),
        ElasticsearchType.data_sets);
  }

  /**
   * Enqueue update of dataSet search documents when the survey is updated.
   *
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> dataSetRepository.streamIdsBySurveyIdsContaining(survey.getId()),
        ElasticsearchType.data_sets);
  }

  /**
   * Enqueue update of data set search documents when the question is updated.
   *
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> dataSetIds =
          variableRepository.streamIdsByRelatedQuestionsQuestionId(question.getId())
              .map(variable -> variable.getDataSetId()).collect(Collectors.toSet());
      return dataSetRepository.streamIdsByIdIn(dataSetIds);
    }, ElasticsearchType.data_sets);
  }

  /**
   * Enqueue update of data set search documents when the instrument is updated.
   *
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> dataSetIds =
          variableRepository.streamIdsByRelatedQuestionsInstrumentId(instrument.getId())
              .map(variable -> variable.getDataSetId()).collect(Collectors.toSet());
      return dataSetRepository.streamIdsByIdIn(dataSetIds);
    }, ElasticsearchType.data_sets);
  }

  /**
   * Enqueue update of dataSet search documents when the variable is changed.
   *
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> dataSetRepository.findOneIdById(variable.getDataSetId()),
        ElasticsearchType.data_sets);

  }

  /**
   * Enqueue update of dataSet search documents when the concept is changed.
   *
   * @param concept the updated, created or deleted concept.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onConceptChanged(Concept concept) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> questionIds = questionRepository.streamIdsByConceptIdsContaining(concept.getId())
          .map(question -> question.getId()).collect(Collectors.toSet());
      Set<String> dataSetIds =
          variableRepository.streamIdsByRelatedQuestionsQuestionIdIn(questionIds)
              .map(variable -> variable.getDataSetId()).collect(Collectors.toSet());
      return dataSetRepository.streamIdsByIdIn(dataSetIds);
    }, ElasticsearchType.data_sets);
  }

  /**
   * Get a list of available dataSet numbers for creating a new dataSet.
   *
   * @param dataAcquisitionProjectId The project id.
   * @return A list of available dataSet numbers.
   */
  public List<Integer> getFreeDataSetNumbers(String dataAcquisitionProjectId) {
    List<Integer> result = new ArrayList<>();
    List<IdAndNumberDataSetProjection> existingNumbers =
        dataSetRepository.findDataSetNumbersByDataAcquisitionProjectId(dataAcquisitionProjectId);
    Optional<IdAndNumberDataSetProjection> max = existingNumbers.stream()
        .max((dataSet1, dataSet2) -> Integer.compare(dataSet1.getNumber(), dataSet2.getNumber()));
    if (!max.isPresent()) {
      result.add(1);
    } else {
      for (int i = 1; i < max.get().getNumber(); i++) {
        if (!dataSetNumberExists(existingNumbers, i)) {
          result.add(i);
        }
      }
      result.add(max.get().getNumber() + 1);
    }
    return result;
  }

  private boolean dataSetNumberExists(List<IdAndNumberDataSetProjection> dataSets, Integer number) {
    Predicate<IdAndNumberDataSetProjection> predicate =
        dataSet -> dataSet.getNumber().equals(number);
    return dataSets.stream().filter(predicate).findFirst().isPresent();
  }

  @Override
  public Optional<DataSet> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void delete(DataSet dataSet) {
    // TODO check project access rights
    crudHelper.deleteMaster(dataSet);
    dataSetAttachmentService.deleteAllByDataSetId(dataSet.getId());
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public DataSet save(DataSet dataSet) {
    // TODO check project access rights
    return crudHelper.saveMaster(dataSet);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public DataSet create(DataSet dataSet) {
    // TODO check project access rights
    return crudHelper.createMaster(dataSet);
  }

  @Override
  public Optional<DataSet> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }
}
