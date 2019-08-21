package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem;

/**
 * Spring Data MongoDB repository for {@link ShadowCopyQueueItem}.
 */
@Repository
public interface ShadowCopyQueueItemRepository
    extends BaseRepository<ShadowCopyQueueItem, String>, ShadowCopyQueueRepositoryCustom {

  Optional<ShadowCopyQueueItem> findByDataAcquisitionProjectIdAndReleaseVersion(
      String dataAcquisitionProjectId, String version);
}
