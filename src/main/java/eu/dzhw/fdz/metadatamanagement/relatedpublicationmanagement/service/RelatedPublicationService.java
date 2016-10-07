package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * This service for {@link DataSet} will wait for delete events of a survey or a data acquisition
 * project.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
@RepositoryEventHandler
public class RelatedPublicationService {

  @Inject
  private RelatedPublicationRepository relatedPublicationRepository;

  @Inject
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  /**
   * A service method for deletion of relatedPublications within a data acquisition project.
   * 
   * @return List of deleted relatedPublications
   */
  public List<RelatedPublication> deleteAll() {
    List<RelatedPublication> deletedRelatedPublications =
        this.relatedPublicationRepository.findAll();
    this.relatedPublicationRepository.deleteAll();
    deletedRelatedPublications.forEach(relatedPublication -> {
      elasticsearchUpdateQueueService.enqueue(
          relatedPublication.getId(), 
          ElasticsearchType.related_publications, 
          ElasticsearchUpdateQueueAction.DELETE);      
    });
    return deletedRelatedPublications;
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
  public void onDataSetSaved(RelatedPublication relatedPublication) {
    elasticsearchUpdateQueueService.enqueue(
        relatedPublication.getId(), 
        ElasticsearchType.related_publications, 
        ElasticsearchUpdateQueueAction.UPSERT);
  }
}
