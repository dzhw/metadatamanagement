package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DaraUpdateQueueItem;

/**
 * Custom Repository for lock and unlock dara queue items.
 * 
 * @author Daniel Katzberg
 *
 */
public interface DaraUpdateQueueItemRepositoryCustom {
  
  void lockAllUnlockedOrExpiredItems(LocalDateTime updateStartedAt, String updateStartedBy);
    
  List<DaraUpdateQueueItem> findOldestLockedItems(String updateStartedBy,
      LocalDateTime updateStartedAt);
  
  void unlockItem(DaraUpdateQueueItem item);
}
