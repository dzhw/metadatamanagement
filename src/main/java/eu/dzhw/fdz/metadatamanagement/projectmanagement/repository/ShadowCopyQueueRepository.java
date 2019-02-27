package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for {@link ShadowCopyQueueItem}.
 */
@Repository
public interface ShadowCopyQueueRepository extends MongoRepository<ShadowCopyQueueItem, String> {


  Optional<ShadowCopyQueueItem> findByDataAcquisitionProjectIdAndShadowCopyVersion(
      String dataAcquisitionProjectId, String shadowCopyVersion);
}
