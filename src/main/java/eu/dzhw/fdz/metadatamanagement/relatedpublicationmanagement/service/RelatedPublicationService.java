package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

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
}
