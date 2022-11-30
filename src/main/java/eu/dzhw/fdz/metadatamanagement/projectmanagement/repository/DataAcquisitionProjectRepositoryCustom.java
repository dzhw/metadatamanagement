package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Custom repository methods for {@link DataAcquisitionProjectRepository}.
 */
public interface DataAcquisitionProjectRepositoryCustom {
  Page<DataAcquisitionProject> findAllMastersByIdLikeAndPublisherIdOrderByIdAsc(String projectId,
                                                                                String publisherId, Pageable pageable);

  Optional<DataAcquisitionProject> findByProjectIdAndDataProviderId(
      String projectId,
      String dataProviderId
  );
}
