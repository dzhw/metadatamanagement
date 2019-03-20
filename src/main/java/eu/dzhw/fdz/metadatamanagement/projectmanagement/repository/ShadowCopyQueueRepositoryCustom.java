package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Custom repository for retrieving locked and unlocked shadow copy queue items.
 */
public interface ShadowCopyQueueRepositoryCustom {

  void lockAllUnlockedOrExpiredItems(LocalDateTime updateStartedAt, String updateStartedBy);

  List<ShadowCopyQueueItem> findOldestLockedItems(LocalDateTime updateStartedAt,
                                                  String updateStartedBy);
}
