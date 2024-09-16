package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.Catgry;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.Citation;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.DataDscr;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.FileDscr;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.FileTxt;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.LanguageEnum;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.StdyDscr;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.TextElement;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.TitlStmt;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.Var;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.helper.DataPackageCrudHelper;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper.DataAcquisitionProjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.CodeBook;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Service for managing the domain object/aggregate {@link DataPackage}.
 *
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
@Slf4j
public class DataPackageManagementService implements CrudService<DataPackage> {

  private final DataPackageRepository dataPackageRepository;

  private final InstrumentRepository instrumentRepository;

  private final QuestionRepository questionRepository;

  private final DataPackageAttachmentService dataPackageAttachmentService;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final RelatedPublicationChangesProvider relatedPublicationChangesProvider;

  private final DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private final DataPackageCrudHelper crudHelper;

  private final DataAcquisitionProjectCrudHelper projectCrudHelper;

  /**
   * Delete all dataPackages when the dataAcquisitionProject was deleted.
   *
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllDataPackagesByProjectId(dataAcquisitionProject.getId());
  }

  /**
   * Update all {@link DataPackageSearchDocument} of the project.
   *
   * @param dataAcquisitionProject the changed project.
   */
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> dataPackageRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()),
        ElasticsearchType.data_packages);
  }

  /**
   * A service method for deletion of dataPackages within a data acquisition project.
   *
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void deleteAllDataPackagesByProjectId(String dataAcquisitionProjectId) {
    // TODO check projects access rights
    try (Stream<DataPackage> dataPackages =
        dataPackageRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      dataPackages.forEach(dataPackage -> {
        dataPackageAttachmentService.deleteAllByDataPackageId(dataPackage.getId());
        crudHelper.deleteMaster(dataPackage);
      });
    }
  }

  /**
   * Enqueue update of dataPackage search document when the data set is changed.
   *
   * @param dataSet the updated, created or deleted dataSet.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetChanged(DataSet dataSet) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> dataPackageRepository.findOneIdAndVersionById(dataSet.getDataPackageId()),
        ElasticsearchType.data_packages);
  }

  /**
   * Enqueue update of dataPackage search document when the variable is changed.
   *
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> dataPackageRepository.findOneIdAndVersionById(variable.getDataPackageId()),
        ElasticsearchType.data_packages);
  }

  /**
   * Enqueue update of dataPackage search document when a related publication is changed.
   *
   * @param relatedPublication the updated, created or deleted publication.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    List<String> dataPackageIds =
        relatedPublicationChangesProvider.getAffectedDataPackageIds(relatedPublication.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> dataPackageRepository.streamIdsByMasterIdIn(dataPackageIds),
        ElasticsearchType.data_packages);
  }

  /**
   * Enqueue update of dataPackage search document when the survey is changed.
   *
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> dataPackageRepository.findOneIdAndVersionById(survey.getDataPackageId()),
        ElasticsearchType.data_packages);
  }

  /**
   * Enqueue update of dataPackage search document when the question is changed.
   *
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> dataPackageRepository.findOneIdAndVersionById(question.getDataPackageId()),
        ElasticsearchType.data_packages);
  }

  /**
   * Enqueue update of dataPackage search document when the instrument is changed.
   *
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> dataPackageRepository.findOneIdAndVersionById(instrument.getDataPackageId()),
        ElasticsearchType.data_packages);
  }

  /**
   * Enqueue update of dataPackage search documents when the concept is changed.
   *
   * @param concept the updated, created or deleted concept.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onConceptChanged(Concept concept) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> dataPackageIds =
          instrumentRepository.streamIdsByConceptIdsContaining(concept.getId())
              .map(instrument -> instrument.getDataPackageId()).collect(Collectors.toSet());
      dataPackageIds.addAll(questionRepository.streamIdsByConceptIdsContaining(concept.getId())
          .map(question -> question.getDataPackageId()).collect(Collectors.toSet()));
      return dataPackageRepository.streamIdsByIdIn(dataPackageIds);
    }, ElasticsearchType.data_packages);
  }

  /**
   * Enqueue update of dataPackage search documents when the {@link AnalysisPackage} is changed.
   *
   * @param analysisPackage the updated, created or deleted {@link AnalysisPackage}.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onAnalysisPackageChanged(AnalysisPackage analysisPackage) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Class<eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.DataPackage> clazz =
          eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.DataPackage.class;
      if (analysisPackage.getAnalysisDataPackages() != null) {
        Set<String> dataPackageIds = analysisPackage.getAnalysisDataPackages().stream()
            .filter(clazz::isInstance).map(clazz::cast)
            .map(dataPackage -> dataPackage.getDataPackageMasterId() + "-"
              + dataPackage.getVersion())
            .collect(Collectors.toSet());
        return dataPackageRepository.streamIdsByIdIn(dataPackageIds);
      } else {
        return Stream.empty();
      }
    }, ElasticsearchType.data_packages);
  }

  @Override
  public Optional<DataPackage> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void delete(DataPackage dataPackage) {
    // TODO check project access rights
    crudHelper.deleteMaster(dataPackage);
    dataPackageAttachmentService.deleteAllByDataPackageId(dataPackage.getId());
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public DataPackage save(DataPackage dataPackage) {
    // TODO check project access rights
    DataAcquisitionProject project =
        dataAcquisitionProjectRepository.findById(dataPackage.getDataAcquisitionProjectId()).orElse(null);
    if (project != null) {
      if (dataPackage.getRemarksUserService() != null && !dataPackage.getRemarksUserService().isBlank()) {
        project.setHasUserServiceRemarks(true);
      } else {
        project.setHasUserServiceRemarks(false);
      }
      projectCrudHelper.saveMaster(project);
    }

    return crudHelper.saveMaster(dataPackage);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public DataPackage create(DataPackage dataPackage) {
    // TODO check project access rights
    return crudHelper.createMaster(dataPackage);
  }

  @Override
  public Optional<DataPackage> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }
}
