package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

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
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.IdAndNumberInstrumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.helper.InstrumentCrudHelper;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.VariableChangesProvider;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing the domain object/aggregate {@link Instrument}.
 *
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
public class InstrumentManagementService implements CrudService<Instrument> {

  private final InstrumentRepository instrumentRepository;

  private final QuestionRepository questionRepository;

  private final VariableChangesProvider variableChangesProvider;

  private final InstrumentAttachmentService instrumentAttachmentService;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final InstrumentCrudHelper crudHelper;

  /**
   * Delete all instruments when the dataAcquisitionProject was deleted.
   *
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllInstrumentsByProjectId(dataAcquisitionProject.getId());
  }

  /**
   * Update all Instruments of the project, when the project is released.
   *
   * @param dataAcquisitionProject the changed project
   */
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> instrumentRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()),
        ElasticsearchType.instruments);
  }

  /**
   * A service method for deletion of instruments within a data acquisition project.
   *
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void deleteAllInstrumentsByProjectId(String dataAcquisitionProjectId) {
    // TODO check project access rights
    try (Stream<Instrument> instruments =
        instrumentRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      instruments.forEach(instrument -> {
        instrumentAttachmentService.deleteAllByInstrumentId(instrument.getId());
        crudHelper.deleteMaster(instrument);
      });
    }
  }

  /**
   * Enqueue update of instrument search documents when the dataPackage changed.
   *
   * @param dataPackage the updated, created or deleted dataPackage.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataPackageChanged(DataPackage dataPackage) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> instrumentRepository.streamIdsByDataPackageId(dataPackage.getId()),
        ElasticsearchType.instruments);
  }

  /**
   * Enqueue update of instrument search documents when the survey changed.
   *
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> instrumentRepository.streamIdsBySurveyIdsContaining(survey.getId()),
        ElasticsearchType.instruments);
  }

  /**
   * Enqueue update of instrument search documents when the question changed.
   *
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> instrumentRepository.findOneIdAndVersionById(question.getInstrumentId()),
        ElasticsearchType.instruments);
  }

  /**
   * Enqueue update of instrument search documents when the variable changed.
   *
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    List<String> instrumentIds = variableChangesProvider.getAffectedInstrumentIds(variable.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> instrumentRepository.streamIdsByIdIn(instrumentIds), ElasticsearchType.instruments);
  }

  /**
   * Enqueue update of instrument search documents when the concept is changed.
   *
   * @param concept the updated, created or deleted concept.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onConceptChanged(Concept concept) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> instrumentRepository.streamIdsByConceptIdsContaining(concept.getId()),
        ElasticsearchType.instruments);

    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> instrumentIds =
          questionRepository.streamIdsByConceptIdsContaining(concept.getId())
              .map(question -> question.getInstrumentId()).collect(Collectors.toSet());
      return instrumentRepository.streamIdsByIdIn(instrumentIds);
    }, ElasticsearchType.instruments);
  }

  /**
   * Get a list of available instrument numbers for creating a new instrument.
   *
   * @param dataAcquisitionProjectId The project id.
   * @return A list of available instrument numbers.
   */
  public List<Integer> getFreeInstrumentNumbers(String dataAcquisitionProjectId) {
    List<Integer> result = new ArrayList<>();
    List<IdAndNumberInstrumentProjection> existingNumbers = instrumentRepository
        .findInstrumentNumbersByDataAcquisitionProjectId(dataAcquisitionProjectId);
    Optional<IdAndNumberInstrumentProjection> max = existingNumbers.stream().max((instrument1,
        instrument2) -> Integer.compare(instrument1.getNumber(), instrument2.getNumber()));
    if (!max.isPresent()) {
      result.add(1);
    } else {
      for (int i = 1; i < max.get().getNumber(); i++) {
        if (!instrumentNumberExists(existingNumbers, i)) {
          result.add(i);
        }
      }
      result.add(max.get().getNumber() + 1);
    }
    return result;
  }

  private boolean instrumentNumberExists(List<IdAndNumberInstrumentProjection> instruments,
      Integer number) {
    Predicate<IdAndNumberInstrumentProjection> predicate =
        instrument -> instrument.getNumber().equals(number);
    return instruments.stream().filter(predicate).findFirst().isPresent();
  }

  @Override
  public Optional<Instrument> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void delete(Instrument instrument) {
    // TODO check project access rights
    crudHelper.deleteMaster(instrument);
    instrumentAttachmentService.deleteAllByInstrumentId(instrument.getId());
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public Instrument save(Instrument instrument) {
    // TODO check project access rights
    return crudHelper.saveMaster(instrument);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public Instrument create(Instrument instrument) {
    // TODO check project access rights
    return crudHelper.createMaster(instrument);
  }

  @Override
  public Optional<Instrument> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }
}
