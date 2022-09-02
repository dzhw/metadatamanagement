package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

import java.util.Optional;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.helper.RelatedPublicationCrudHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing the domain object/aggregate {@link RelatedPublication}.
 *
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
public class RelatedPublicationManagementService implements CrudService<RelatedPublication> {

  private final RelatedPublicationRepository relatedPublicationRepository;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final RelatedPublicationCrudHelper crudHelper;

  /**
   * Enqueue update of related publication search documents when the dataPackage changed.
   *
   * @param dataPackage the updated, created or deleted dataPackage.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataPackageChanged(DataPackage dataPackage) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> relatedPublicationRepository.streamIdsByDataPackageIdsContaining(dataPackage.getId()),
        ElasticsearchType.related_publications);
  }

  /**
   * Enqueue update of related publication search documents when the analysis package changed.
   *
   * @param analysisPackage the updated, created or deleted analysisPackage.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onAnalysisPackageChanged(AnalysisPackage analysisPackage) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> relatedPublicationRepository
            .streamIdsByAnalysisPackageIdsContaining(analysisPackage.getId()),
        ElasticsearchType.related_publications);
  }

  @Override
  public Optional<RelatedPublication> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public void delete(RelatedPublication publication) {
    crudHelper.delete(publication);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public RelatedPublication save(RelatedPublication publication) {
    return crudHelper.save(publication);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public RelatedPublication create(RelatedPublication publication) {
    return crudHelper.create(publication);
  }


  @Override
  public Optional<RelatedPublication> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }
}
