package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper.DataAcquisitionProjectShadowCopyDataSource;

/**
 * Service which generates shadow copies of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class DataAcquisitionProjectShadowCopyService
    extends ShadowCopyHelper<DataAcquisitionProject> {
  public DataAcquisitionProjectShadowCopyService(
      DataAcquisitionProjectShadowCopyDataSource projectShadowCopyDataSource) {
    super(projectShadowCopyDataSource);
  }
}
