package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;

import java.util.List;
import java.util.Optional;

/**
 * Custom repository methods for {@link DataAcquisitionProjectRepository}.
 */
public interface DataAcquisitionProjectRepositoryCustom {
  List<DataAcquisitionProject> findAllByIdLikeAndPublisherIdOrderByIdAsc(String projectId,
                                                                         String publisherId);

  Optional<DataAcquisitionProject> findByProjectIdAndDataProviderId(
      String projectId,
      String dataProviderId
  );
}
