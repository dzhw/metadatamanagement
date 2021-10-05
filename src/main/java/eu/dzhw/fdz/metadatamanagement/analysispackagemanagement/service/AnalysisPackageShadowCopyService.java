package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper.AnalysisPackageShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;

/**
 * Service which generates shadow copies of all analysis packages of a project, when the project has
 * been released.
 * 
 * @author René Reitmann
 */
@Service
public class AnalysisPackageShadowCopyService extends ShadowCopyHelper<AnalysisPackage> {
  public AnalysisPackageShadowCopyService(
      AnalysisPackageShadowCopyDataSource analysisPackageShadowCopyDataSource) {
    super(analysisPackageShadowCopyDataSource);
  }
}

