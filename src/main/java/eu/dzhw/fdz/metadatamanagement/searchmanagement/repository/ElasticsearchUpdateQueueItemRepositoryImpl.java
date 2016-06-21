package eu.dzhw.fdz.metadatamanagement.searchmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueItem;

/**
 * Custom repository for retrieving locked and unlocked queue items.
 * 
 * @author René Reitmann
 */
@Component
public class ElasticsearchUpdateQueueItemRepositoryImpl
    implements ElasticsearchUpdateQueueItemRepositoryCustom {

  // update lock is valid for 10 minutes
  private static final int UPDATE_LOCK_EXPIRED = 10;

  // number of queue items to be processed in one batch
  private static final int BULK_SIZE = 100;

  @Inject
  private MongoOperations mongoOperations;

  @Override
  public List<ElasticsearchUpdateQueueItem> findUnlockedOrExpiredItems() {
    Query query = new Query(new Criteria().orOperator(Criteria.where("updateStartedAt")
        .lte(LocalDateTime.now()
        .minusMinutes(UPDATE_LOCK_EXPIRED)),
        Criteria.where("updateStartedAt")
          .exists(false))).limit(BULK_SIZE);
    return mongoOperations.find(query, ElasticsearchUpdateQueueItem.class);
  }

  @Override
  public List<ElasticsearchUpdateQueueItem> findOldestLockedItems(String updateStartedBy,
      LocalDateTime updateStartedAt) {
    Query query = new Query(new Criteria().andOperator(Criteria.where("updateStartedBy")
        .is(updateStartedBy),
        Criteria.where("updateStartedAt")
          .is(updateStartedAt))).limit(BULK_SIZE)
            .with(new Sort(Direction.ASC, "createdDate"));
    return mongoOperations.find(query, ElasticsearchUpdateQueueItem.class);
  }

}
