package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectReleasedEvent;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Service for managing the domain object/aggregate {@link Study}.
 * 
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
public class StudyManagementService implements CrudService<Study> {

  private final StudyRepository studyRepository;
  
  private final InstrumentRepository instrumentRepository;
  
  private final QuestionRepository questionRepository;

  private final StudyAttachmentService studyAttachmentService;
  
  private final StudyChangesProvider studyChangesProvider;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final RelatedPublicationChangesProvider relatedPublicationChangesProvider;

  private final ShadowCopyService<Study> shadowCopyService;

  private final StudyShadowCopyDataSource studyShadowCopyDataSource;
  
  private final GenericShadowableDomainObjectCrudHelper<Study, StudyRepository> crudHelper;
  
  public StudyManagementService(StudyRepository studyRepository,
      InstrumentRepository instrumentRepository, QuestionRepository questionRepository,
      StudyAttachmentService studyAttachmentService, StudyChangesProvider studyChangesProvider,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      ApplicationEventPublisher eventPublisher,
      RelatedPublicationChangesProvider relatedPublicationChangesProvider,
      ShadowCopyService<Study> shadowCopyService,
      StudyShadowCopyDataSource studyShadowCopyDataSource) {
    super();
    this.studyRepository = studyRepository;
    this.instrumentRepository = instrumentRepository;
    this.questionRepository = questionRepository;
    this.studyAttachmentService = studyAttachmentService;
    this.studyChangesProvider = studyChangesProvider;
    this.elasticsearchUpdateQueueService = elasticsearchUpdateQueueService;
    this.relatedPublicationChangesProvider = relatedPublicationChangesProvider;
    this.shadowCopyService = shadowCopyService;
    this.studyShadowCopyDataSource = studyShadowCopyDataSource;
    this.crudHelper = new GenericShadowableDomainObjectCrudHelper<>(
        studyRepository, eventPublisher, elasticsearchUpdateQueueService,
        ElasticsearchType.studies);
  }

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
   * Remember the old and new study.
   * 
   * @param study the new study
   */
  @HandleBeforeSave
  public void onBeforeStudySaved(Study study) {
    Study oldStudy = studyRepository.findById(study.getId()).orElse(null);
    studyChangesProvider.put(study, oldStudy);
  }
  
  @HandleBeforeCreate
  public void onBeforeStudyCreated(Study study) {
    studyChangesProvider.put(study, null);
  }
  
  @HandleBeforeDelete
  public void onBeforeStudyDeleted(Study study) {
    studyChangesProvider.put(null, study);
  }

  /**
   * A service method for deletion of studies within a data acquisition project.
   * 
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteAllStudiesByProjectId(String dataAcquisitionProjectId) {
    try (Stream<Study> studies =
        studyRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      studies.forEach(study -> {
        crudHelper.deleteMaster(study, false);
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

  /**
   * Create shadow copies for studies on project release.
   * @param projectReleasedEvent Released project event
   */
  @EventListener
  public void onProjectReleaseEvent(ProjectReleasedEvent projectReleasedEvent) {
    shadowCopyService.createShadowCopies(projectReleasedEvent.getDataAcquisitionProject(),
        projectReleasedEvent.getPreviousReleaseVersion(), studyShadowCopyDataSource);
  }

  @Override
  public Optional<Study> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  public void delete(Study study) {
    // TODO check project access rights
    crudHelper.deleteMaster(study, true);
    studyAttachmentService.deleteAllByStudyId(study.getId());
  }

  @Override
  public Study save(Study study) {
    // TODO check project access rights
    return crudHelper.saveMaster(study, true);
  }

  @Override
  public Study create(Study study) {
    // TODO check project access rights
    return crudHelper.createMaster(study, true);
  }
}
