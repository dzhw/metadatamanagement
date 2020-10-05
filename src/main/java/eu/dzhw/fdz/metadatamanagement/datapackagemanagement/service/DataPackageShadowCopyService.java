package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.helper.DataPackageShadowCopyDataSource;

/**
 * Service which generates shadow copies of all dataPackages of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class DataPackageShadowCopyService extends ShadowCopyHelper<DataPackage> {
  public DataPackageShadowCopyService(
      DataPackageShadowCopyDataSource dataPackageShadowCopyDataSource) {
    super(dataPackageShadowCopyDataSource);
  }
}

