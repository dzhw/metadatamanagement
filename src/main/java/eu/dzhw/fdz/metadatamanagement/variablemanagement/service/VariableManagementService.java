package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.helper.VariableCrudHelper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

/**
 * Service for managing the domain object/aggregate {@link Variable}.
 *
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
public class VariableManagementService implements CrudService<Variable> {

  private final QuestionRepository questionRepository;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final VariableRepository variableRepository;

  private final VariableCrudHelper crudHelper;

  /**
   * Delete all variables when the dataAcquisitionProject was deleted.
   *
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllVariablesByProjectId(dataAcquisitionProject.getId());
  }

  /**
   * Update all variables of the project, when the project is released.
   *
   * @param dataAcquisitionProject the changed project
   */
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()),
        ElasticsearchType.variables);
  }

  /**
   * A service method for deletion of variables within a data acquisition project.
   *
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void deleteAllVariablesByProjectId(String dataAcquisitionProjectId) {
    // TODO check access rights by project
    try (Stream<Variable> variables =
        variableRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      variables.forEach(variable -> {
        crudHelper.deleteMaster(variable);
      });
    }
  }

  /**
   * Enqueue update of variable search documents when the data set is changed.
   *
   * @param dataSet the updated, created or deleted data set.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetChanged(DataSet dataSet) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByDataSetId(dataSet.getId()),
        ElasticsearchType.variables);
  }

  /**
   * Enqueue update of variable search documents when the dataPackage is changed.
   *
   * @param dataPackage the updated, created or deleted dataPackage.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataPackageChanged(DataPackage dataPackage) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByDataPackageId(dataPackage.getId()),
        ElasticsearchType.variables);
  }

  /**
   * Enqueue update of variable search documents when the instrument is changed.
   *
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByRelatedQuestionsInstrumentId(instrument.getId()),
        ElasticsearchType.variables);
  }

  /**
   * Enqueue update of variable search documents when the question is changed.
   *
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByRelatedQuestionsQuestionId(question.getId()),
        ElasticsearchType.variables);
  }

  /**
   * Enqueue update of variable search documents when the survey is updated.
   *
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsBySurveyIdsContaining(survey.getId()),
        ElasticsearchType.variables);
  }

  /**
   * Enqueue update of variable search documents when the concept is changed.
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
      return variableRepository.streamIdsByRelatedQuestionsQuestionIdIn(questionIds);
    }, ElasticsearchType.variables);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public Variable create(Variable variable) {
    // TODO check access rights by project
    return crudHelper.createMaster(variable);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public Variable save(Variable variable) {
    // TODO check access rights by project
    return crudHelper.saveMaster(variable);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void delete(Variable variable) {
    // TODO check access rights by project
    crudHelper.deleteMaster(variable);
  }

  @Override
  public Optional<Variable> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  public Optional<Variable> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }
}
