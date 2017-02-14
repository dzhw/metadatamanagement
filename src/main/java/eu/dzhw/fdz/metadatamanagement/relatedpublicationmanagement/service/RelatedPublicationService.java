package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * This service for {@link RelatedPublicationService} will wait for delete events 
 * of a {@link RelatedPublication}.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
@RepositoryEventHandler
public class RelatedPublicationService {

  @Autowired
  private RelatedPublicationRepository relatedPublicationRepository;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  /**
   * A service method for deletion of relatedPublications within a data acquisition project.
   */
  public void deleteAll() {
    Pageable pageable = new PageRequest(0, 100);
    Slice<RelatedPublication> relatedPublications = relatedPublicationRepository.findBy(pageable);

    while (relatedPublications.hasContent()) {
      relatedPublications.forEach(relatedPublication -> {
        relatedPublicationRepository.delete(relatedPublication);
        elasticsearchUpdateQueueService.enqueue(
            relatedPublication.getId(), 
            ElasticsearchType.related_publications, 
            ElasticsearchUpdateQueueAction.DELETE);      
      });
      relatedPublications = relatedPublicationRepository.findBy(pageable);
    }
  }
  
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
   * Enqueue update of related publication search documents when the study changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onStudyChanged(Study study) {
    List<RelatedPublication> relatedPublications = relatedPublicationRepository
        .findByStudyIdsContaining(study.getId());
    relatedPublications.forEach(relatedPublication -> {      
      elasticsearchUpdateQueueService.enqueue(
          relatedPublication.getId(), 
          ElasticsearchType.related_publications, 
          ElasticsearchUpdateQueueAction.UPSERT);
    });
  }
  
  /**
   * Enqueue update of related publication search documents when the question changed.
   * 
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    List<RelatedPublication> relatedPublications = relatedPublicationRepository
        .findByQuestionIdsContaining(question.getId());
    relatedPublications.forEach(relatedPublication -> {      
      elasticsearchUpdateQueueService.enqueue(
          relatedPublication.getId(), 
          ElasticsearchType.related_publications, 
          ElasticsearchUpdateQueueAction.UPSERT);
    });
  }
  
  /**
   * Enqueue update of related publication search documents when the instrument changed.
   * 
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    List<RelatedPublication> relatedPublications = relatedPublicationRepository
        .findByInstrumentIdsContaining(instrument.getId());
    relatedPublications.forEach(relatedPublication -> {      
      elasticsearchUpdateQueueService.enqueue(
          relatedPublication.getId(), 
          ElasticsearchType.related_publications, 
          ElasticsearchUpdateQueueAction.UPSERT);
    });
  }
  
  /**
   * Enqueue update of related publication search documents when the survey changed.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    List<RelatedPublication> relatedPublications = relatedPublicationRepository
        .findBySurveyIdsContaining(survey.getId());
    relatedPublications.forEach(relatedPublication -> {      
      elasticsearchUpdateQueueService.enqueue(
          relatedPublication.getId(), 
          ElasticsearchType.related_publications, 
          ElasticsearchUpdateQueueAction.UPSERT);
    });
  }
  
  /**
   * Enqueue update of related publication search documents when the data set changed.
   * 
   * @param dataSet the updated, created or deleted data set.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetChanged(DataSet dataSet) {
    List<RelatedPublication> relatedPublications = relatedPublicationRepository
        .findByDataSetIdsContaining(dataSet.getId());
    relatedPublications.forEach(relatedPublication -> {      
      elasticsearchUpdateQueueService.enqueue(
          relatedPublication.getId(), 
          ElasticsearchType.related_publications, 
          ElasticsearchUpdateQueueAction.UPSERT);
    });
  }
  
  /**
   * Enqueue update of related publication search documents when the variable changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    List<RelatedPublication> relatedPublications = relatedPublicationRepository
        .findByVariableIdsContaining(variable.getId());
    relatedPublications.forEach(relatedPublication -> {      
      elasticsearchUpdateQueueService.enqueue(
          relatedPublication.getId(), 
          ElasticsearchType.related_publications, 
          ElasticsearchUpdateQueueAction.UPSERT);
    });
  }
}
