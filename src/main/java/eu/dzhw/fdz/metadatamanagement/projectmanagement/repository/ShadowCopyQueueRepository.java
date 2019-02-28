package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for {@link ShadowCopyQueueItem}.
 */
@Repository
public interface ShadowCopyQueueRepository extends MongoRepository<ShadowCopyQueueItem, String>,
    ShadowCopyQueueRepositoryCustom {

  Optional<ShadowCopyQueueItem> findByDataAcquisitionProjectIdAndShadowCopyVersion(
      String dataAcquisitionProjectId, String shadowCopyVersion);
}
