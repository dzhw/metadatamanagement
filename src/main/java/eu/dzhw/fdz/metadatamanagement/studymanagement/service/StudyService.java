package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectReleasedEvent;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.data.rest.core.event.BeforeDeleteEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

/**
 * Service for creating and updating variable. Used for updating variables in mongo and
 * elasticsearch.
 */
@Service
@RepositoryEventHandler
public class StudyService {

  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private StudyAttachmentService studyAttachmentService;
  
  @Autowired
  private StudyChangesProvider studyChangesProvider;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private RelatedPublicationChangesProvider relatedPublicationChangesProvider;

  @Autowired
  private ShadowCopyService<Study> shadowCopyService;

  @Autowired
  private StudyShadowCopyDataSource studyShadowCopyDataSource;
  
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
        eventPublisher.publishEvent(new BeforeDeleteEvent(study));
        studyRepository.delete(study);
        eventPublisher.publishEvent(new AfterDeleteEvent(study));
      });
    }
  }

  /**
   * Enqueue deletion of study search document when the study is deleted.
   * 
   * @param study the deleted variable.
   */
  @HandleAfterDelete
  public void onStudyDeleted(Study study) {
    studyAttachmentService.deleteAllByStudyId(study.getId());
    elasticsearchUpdateQueueService.enqueue(study.getId(), ElasticsearchType.studies,
        ElasticsearchUpdateQueueAction.DELETE);
  }

  /**
   * Enqueue update of study search document when the study is updated.
   * 
   * @param study the updated or created study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onStudySaved(Study study) {
    elasticsearchUpdateQueueService.enqueue(study.getId(), ElasticsearchType.studies,
        ElasticsearchUpdateQueueAction.UPSERT);
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
        () -> studyRepository.streamIdsByIdIn(studyIds),
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
   * Create shadow copies for studies on project release.
   * @param projectReleasedEvent Released project event
   */
  @EventListener
  public void onProjectReleaseEvent(ProjectReleasedEvent projectReleasedEvent) {
    shadowCopyService.createShadowCopies(projectReleasedEvent.getDataAcquisitionProject(),
        studyShadowCopyDataSource);
  }
}
