package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.AnalysisPackageChangesProvider;
import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.AnalysisPackageSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * Component which implements CRUD functions for all {@link AnalysisPackage}s.
 *
 * @author René Reitmann
 */
@Component
public class AnalysisPackageCrudHelper
    extends GenericShadowableDomainObjectCrudHelper<AnalysisPackage, AnalysisPackageRepository> {
  /**
   * Construct the helper.
   */
  public AnalysisPackageCrudHelper(
      AnalysisPackageRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      AnalysisPackageChangesProvider analysisPackageChangesProvider,
      RestHighLevelClient elasticsearchClient,
      Gson gson
  ) {
    super(
        repository,
        applicationEventPublisher,
        elasticsearchUpdateQueueService,
        analysisPackageChangesProvider,
        elasticsearchClient,
        AnalysisPackageSearchDocument.class,
        gson
    );
  }
}
