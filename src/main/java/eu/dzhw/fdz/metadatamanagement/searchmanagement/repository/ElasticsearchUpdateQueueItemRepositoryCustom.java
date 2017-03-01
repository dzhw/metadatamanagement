package eu.dzhw.fdz.metadatamanagement.searchmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueItem;

/**
 * Custom repository for retrieving locked and unlocked queue items.
 * 
 * @author René Reitmann
 */
public interface ElasticsearchUpdateQueueItemRepositoryCustom {

  void lockUnlockedOrExpiredItems(LocalDateTime updateStartedAt, String updateStartedBy);

  List<ElasticsearchUpdateQueueItem> findOldestLockedItems(String updateStartedBy,
      LocalDateTime updateStartedAt);
}
