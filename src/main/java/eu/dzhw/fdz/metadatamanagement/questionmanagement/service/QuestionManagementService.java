package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.helper.QuestionCrudHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.QuestionSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.VariableChangesProvider;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing the domain object/aggregate {@link Question}.
 *
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
public class QuestionManagementService implements CrudService<Question> {

  private final QuestionRepository questionRepository;

  private final InstrumentRepository instrumentRepository;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final QuestionImageService imageService;

  private final VariableChangesProvider variableChangesProvider;

  private final QuestionCrudHelper crudHelper;

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
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void deleteQuestionsByProjectId(String dataAcquisitionProjectId) {
    // TODO check project access rights
    try (Stream<Question> questions =
        questionRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      questions.forEach(question -> {
        crudHelper.deleteMaster(question);
        imageService.deleteQuestionImages(question.getId());
      });
    }
  }

  /**
   * Enqueue update of question search documents when the dataPackage is changed.
   *
   * @param dataPackage the updated, created or deleted dataPackage.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataPackageChanged(DataPackage dataPackage) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> questionRepository.streamIdsByDataPackageId(dataPackage.getId()),
        ElasticsearchType.questions);
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
    List<String> questionIds = variableChangesProvider.getAffectedQuestionIds(variable.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> questionRepository.streamIdsByIdIn(questionIds), ElasticsearchType.questions);
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
        () -> questionRepository.streamIdsByConceptIdsContaining(concept.getId()),
        ElasticsearchType.questions);
  }

  @Override
  public Optional<Question> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void delete(Question question) {
    // TODO check project access rights
    crudHelper.deleteMaster(question);
    imageService.deleteQuestionImages(question.getId());
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public Question save(Question question) {
    // TODO check project access rights
    return crudHelper.saveMaster(question);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public Question create(Question question) {
    // TODO check project access rights
    return crudHelper.createMaster(question);
  }

  @Override
  public Optional<Question> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }
}
