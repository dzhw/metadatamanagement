package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.helper.StudyCrudHelper;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing the domain object/aggregate {@link Study}.
 * 
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
public class StudyManagementService implements CrudService<Study> {

  private final StudyRepository studyRepository;
  
  private final InstrumentRepository instrumentRepository;
  
  private final QuestionRepository questionRepository;

  private final StudyAttachmentService studyAttachmentService;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final RelatedPublicationChangesProvider relatedPublicationChangesProvider;
  
  private final StudyCrudHelper crudHelper;

  /**
   * Delete all studies when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllStudiesByProjectId(dataAcquisitionProject.getId());
  }

  /**
   * Update all {@link StudySearchDocument} of the project.
   * 
   * @param dataAcquisitionProject the changed project.
   */
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> studyRepository.streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()),
        ElasticsearchType.studies);
  }

  /**
   * A service method for deletion of studies within a data acquisition project.
   * 
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void deleteAllStudiesByProjectId(String dataAcquisitionProjectId) {
    // TODO check projects access rights
    try (Stream<Study> studies =
        studyRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      studies.forEach(study -> {
        crudHelper.deleteMaster(study);
        studyAttachmentService.deleteAllByStudyId(study.getId());
      });
    }
  }

  /**
   * Enqueue update of study search document when the data set is changed.
   * 
   * @param dataSet the updated, created or deleted dataSet.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetChanged(DataSet dataSet) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> studyRepository.findOneIdAndVersionById(dataSet.getStudyId()),
        ElasticsearchType.studies);
  }

  /**
   * Enqueue update of study search document when the variable is changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> studyRepository.findOneIdAndVersionById(variable.getStudyId()),
        ElasticsearchType.studies);
  }

  /**
   * Enqueue update of study search document when a related publication is changed.
   * 
   * @param relatedPublication the updated, created or deleted publication.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    List<String> studyIds = relatedPublicationChangesProvider.getAffectedStudyIds(
        relatedPublication.getId()); 
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> studyRepository.streamIdsByMasterIdIn(studyIds),
        ElasticsearchType.studies);
  }

  /**
   * Enqueue update of study search document when the survey is changed.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> studyRepository.findOneIdAndVersionById(survey.getStudyId()),
        ElasticsearchType.studies);
  }

  /**
   * Enqueue update of study search document when the question is changed.
   * 
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> studyRepository.findOneIdAndVersionById(question.getStudyId()),
        ElasticsearchType.studies);
  }

  /**
   * Enqueue update of study search document when the instrument is changed.
   * 
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> studyRepository.findOneIdAndVersionById(instrument.getStudyId()),
        ElasticsearchType.studies);
  }
  
  /**
   * Enqueue update of study search documents when the concept is changed.
   * 
   * @param concept the updated, created or deleted concept.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onConceptChanged(Concept concept) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> studyIds = instrumentRepository.streamIdsByConceptIdsContaining(concept.getId())
          .map(instrument -> instrument.getStudyId()).collect(Collectors.toSet());
      studyIds.addAll(questionRepository.streamIdsByConceptIdsContaining(concept.getId())
              .map(question -> question.getStudyId()).collect(Collectors.toSet()));
      return studyRepository.streamIdsByIdIn(studyIds);
    }, ElasticsearchType.studies);
  }

  @Override
  public Optional<Study> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void delete(Study study) {
    // TODO check project access rights
    crudHelper.deleteMaster(study);
    studyAttachmentService.deleteAllByStudyId(study.getId());
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public Study save(Study study) {
    // TODO check project access rights
    return crudHelper.saveMaster(study);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public Study create(Study study) {
    // TODO check project access rights
    return crudHelper.createMaster(study);
  }
}
