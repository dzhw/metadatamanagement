package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;

import java.util.List;

/**
 * Custom repository methods for {@link DataAcquisitionProjectRepository}
 */
public interface DataAcquisitionProjectRepositoryCustom {
  List<DataAcquisitionProject> findAllByIdLikeAndPublisherId(String projectId, String publisherId);
}
