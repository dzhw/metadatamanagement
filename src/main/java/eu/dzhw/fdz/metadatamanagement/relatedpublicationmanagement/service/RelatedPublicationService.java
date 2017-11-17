package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import lombok.extern.slf4j.Slf4j;

/**
 * This service for {@link RelatedPublicationService} will wait for delete events 
 * of a {@link RelatedPublication}.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
@RepositoryEventHandler
@Slf4j
public class RelatedPublicationService {

  @Autowired
  private RelatedPublicationRepository relatedPublicationRepository;
  
  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private DaraUpdateQueueService daraUpdateQueueService;
  
  /**
   * Enqueue deletion of related publication search document 
   * when the related publication is deleted.
   * 
   * @param relatedPublication the deleted related publication.
   */
  @HandleAfterDelete
  public void onRelatedPublicationDeleted(RelatedPublication relatedPublication) {
    elasticsearchUpdateQueueService.enqueue(
        relatedPublication.getId(), 
        ElasticsearchType.related_publications, 
        ElasticsearchUpdateQueueAction.DELETE);
  }
  
  /**
   * Enqueue update of related publication search document when the related publication is updated.
   * 
   * @param relatedPublication the updated or related publication dataSet.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onRelatedPublicationSaved(RelatedPublication relatedPublication) {
    elasticsearchUpdateQueueService.enqueue(
        relatedPublication.getId(), 
        ElasticsearchType.related_publications, 
        ElasticsearchUpdateQueueAction.UPSERT);
  }
  
  /**
   * Do checks with the related publication before delete or save operations.
   * 
   * @param relatedPublication the updated or deleted related 
   *     publication before the delete or save process 
   */
  @HandleBeforeCreate
  @HandleBeforeSave
  @HandleBeforeDelete
  public void onRelatedPublicationBeforeSavedOrDelete(RelatedPublication relatedPublication) {
    log.debug("Before Related Publication Save/Create/Delete:");
    
    for (String studyId : relatedPublication.getStudyIds()) {
      RelatedPublication oldPublication = this.relatedPublicationRepository
          .findByIdAndStudyIdsContaining(relatedPublication.getId(), studyId);      
      if (oldPublication == null) {        
        Study study = studyRepository.findOne(studyId);
        if (study != null) {
          log.error("Did not find Related Publication:" + relatedPublication.getId()
              + " with Study Id: " + studyId);
          daraUpdateQueueService.enqueue(study.getDataAcquisitionProjectId());
        }  
        //no old publication, no known study id? -> Do nothing!        
      } else {
        log.debug("Found Related Publication:" + relatedPublication.getId()
            + " with Study Id: " + studyId);
        log.debug("Old Source Reference: " + oldPublication.getSourceReference());
        log.debug("New Source Reference: " + relatedPublication.getSourceReference());
        
        //Source Reference is not equals
        if (!(oldPublication.getSourceReference()
            .equals(relatedPublication.getSourceReference()))) {
          daraUpdateQueueService.enqueue(studyId);
        }
        //Source Reference is equal. -> Do nothing.
      }
    }
  }
  
  /**
   * Enqueue update of related publication search documents when the study changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onStudyChanged(Study study) {
    enqueueUpserts(relatedPublicationRepository
        .streamIdsByStudyIdsContaining(study.getId()));
  }
  
  /**
   * Enqueue update of related publication search documents when the question changed.
   * 
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onQuestionChanged(Question question) {
    enqueueUpserts(relatedPublicationRepository
        .streamIdsByQuestionIdsContaining(question.getId()));
  }
  
  /**
   * Enqueue update of related publication search documents when the instrument changed.
   * 
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onInstrumentChanged(Instrument instrument) {
    enqueueUpserts(relatedPublicationRepository
        .streamIdsByInstrumentIdsContaining(instrument.getId()));
  }
  
  /**
   * Enqueue update of related publication search documents when the survey changed.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onSurveyChanged(Survey survey) {
    enqueueUpserts(relatedPublicationRepository
        .streamIdsBySurveyIdsContaining(survey.getId()));
  }
  
  /**
   * Enqueue update of related publication search documents when the data set changed.
   * 
   * @param dataSet the updated, created or deleted data set.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onDataSetChanged(DataSet dataSet) {
    enqueueUpserts(relatedPublicationRepository
        .streamIdsByDataSetIdsContaining(dataSet.getId()));
  }
  
  /**
   * Enqueue update of related publication search documents when the variable changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onVariableChanged(Variable variable) {
    enqueueUpserts(relatedPublicationRepository
        .streamIdsByVariableIdsContaining(variable.getId()));
  }
  
  private void enqueueUpserts(Stream<IdAndVersionProjection> relatedPublications) {
    try (Stream<IdAndVersionProjection> relatedPublicationStream = relatedPublications) {
      relatedPublicationStream.forEach(relatedPublication -> {
        elasticsearchUpdateQueueService.enqueue(relatedPublication.getId(),
            ElasticsearchType.related_publications, ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
}
