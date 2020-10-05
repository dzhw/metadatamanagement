package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.service.GenericDomainObjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;

/**
 * Service responsible for retrieving an initializing the dataPackage history.
 * 
 * @author René Reitmann
 */
@Service
public class DataPackageVersionsService 
    extends GenericDomainObjectVersionsService<DataPackage, DataPackageRepository> {
  /**
   * Construct the service.
   */
  public DataPackageVersionsService(Javers javers, DataPackageRepository dataPackageRepository,
      MetadataManagementProperties metadataManagementProperties) {
    super(DataPackage.class, javers, dataPackageRepository, metadataManagementProperties);
  }

  /**
   * Init Javers with all current dataPackages if there are no dataPackage commits in Javers yet.
   */
  @PostConstruct
  public void initJaversForDataPackages() {
    super.initJaversWithCurrentVersions();
  }
}
