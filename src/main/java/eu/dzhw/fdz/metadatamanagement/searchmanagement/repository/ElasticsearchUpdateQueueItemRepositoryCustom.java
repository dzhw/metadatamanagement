package eu.dzhw.fdz.metadatamanagement.searchmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;

/**
 * Custom repository for retrieving locked and unlocked queue items.
 * 
 * @author René Reitmann
 */
public interface ElasticsearchUpdateQueueItemRepositoryCustom {

  void lockAllUnlockedOrExpiredItems(LocalDateTime updateStartedAt, String updateStartedBy);
  
  void lockAllUnlockedOrExpiredItemsByType(
      LocalDateTime updateStartedAt, String updateStartedBy, ElasticsearchType type);

  List<ElasticsearchUpdateQueueItem> findOldestLockedItems(String updateStartedBy,
      LocalDateTime updateStartedAt);
  
  List<ElasticsearchUpdateQueueItem> findOldestLockedItemsByType(String updateStartedBy,
      LocalDateTime updateStartedAt, ElasticsearchType type);
}
