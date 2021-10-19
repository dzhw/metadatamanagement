package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.service.GenericDomainObjectVersionsService;

/**
 * Service responsible for retrieving an initializing the analysis package history.
 * 
 * @author René Reitmann
 */
@Service
public class AnalysisPackageVersionsService
    extends GenericDomainObjectVersionsService<AnalysisPackage, AnalysisPackageRepository> {
  /**
   * Construct the service.
   */
  public AnalysisPackageVersionsService(Javers javers,
      AnalysisPackageRepository analysisPackageRepository,
      MetadataManagementProperties metadataManagementProperties) {
    super(AnalysisPackage.class, javers, analysisPackageRepository, metadataManagementProperties);
  }

  /**
   * Init Javers with all current analysis packages if there are no analysis package commits in
   * Javers yet.
   */
  @PostConstruct
  public void initJaversForDataPackages() {
    super.initJaversWithCurrentVersions();
  }
}
