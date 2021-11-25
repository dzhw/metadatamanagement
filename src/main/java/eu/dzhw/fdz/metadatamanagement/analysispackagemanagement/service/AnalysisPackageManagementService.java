package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.context.event.EventListener;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper.AnalysisPackageCrudHelper;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingEndedEvent;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing the domain object/aggregate {@link AnalysisPackage}.
 *
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
public class AnalysisPackageManagementService implements CrudService<AnalysisPackage> {

  private final AnalysisPackageRepository analysisPackageRepository;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final AnalysisPackageCrudHelper crudHelper;

  private final AnalysisPackageAttachmentService analysisPackageAttachmentService;

  private final ScriptAttachmentService scriptAttachmentService;

  private final RelatedPublicationChangesProvider relatedPublicationChangesProvider;

  /**
   * Delete all analysis packages when the dataAcquisitionProject was deleted.
   *
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAnalysisPackagesByProjectId(dataAcquisitionProject.getId());
  }

  /**
   * Update the {@link AnalysisPackage}s of the project when the project is released.
   *
   * @param dataAcquisitionProject the changed project
   */
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> analysisPackageRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()),
        ElasticsearchType.analysis_packages);
  }

  /**
   * Enqueue update of analysisPackage search documents when a related publication is changed.
   *
   * @param relatedPublication the updated, created or deleted publication.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    List<String> analysisPackageIds =
        relatedPublicationChangesProvider.getAffectedAnalysisPackageIds(relatedPublication.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> analysisPackageRepository.streamIdsByMasterIdIn(analysisPackageIds),
        ElasticsearchType.analysis_packages);
  }

  /**
   * Re-indexes analysis package search documents with new data package references if a shadow copy
   * of a project is saved.
   *
   * @param shadowCopyingEndedEvent Event emitted by {@link ShadowCopyQueueItemService}.
   */
  @EventListener
  public void onShadowCopyingEnded(ShadowCopyingEndedEvent shadowCopyingEndedEvent) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      String masterId = "stu-" + shadowCopyingEndedEvent.getDataAcquisitionProjectId() + "$";
      String version = shadowCopyingEndedEvent.getRelease().getVersion();
      return analysisPackageRepository.streamIdsByDataPackageMasterIdAndVersion(masterId, version);
    }, ElasticsearchType.analysis_packages);
  }

  /**
   * A service method for deletion of analysis packages within a data acquisition project.
   *
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void deleteAnalysisPackagesByProjectId(String dataAcquisitionProjectId) {
    try (Stream<AnalysisPackage> analysisPackages =
        analysisPackageRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      analysisPackages.forEach(analysisPackage -> {
        scriptAttachmentService.deleteAllByAnalysisPackageId(analysisPackage.getId());
        analysisPackageAttachmentService.deleteAllByAnalysisPackageId(analysisPackage.getId());
        crudHelper.deleteMaster(analysisPackage);
      });
    }
  }

  @Override
  public Optional<AnalysisPackage> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void delete(AnalysisPackage analysisPackage) {
    // TODO check project access rights
    crudHelper.deleteMaster(analysisPackage);
    scriptAttachmentService.deleteAllByAnalysisPackageId(analysisPackage.getId());
    analysisPackageAttachmentService.deleteAllByAnalysisPackageId(analysisPackage.getId());
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public AnalysisPackage save(AnalysisPackage analysisPackage) {
    // TODO check project access rights
    return crudHelper.saveMaster(analysisPackage);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public AnalysisPackage create(AnalysisPackage analysisPackage) {
    // TODO check project access rights
    return crudHelper.createMaster(analysisPackage);
  }

  @Override
  public Optional<AnalysisPackage> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }
}
