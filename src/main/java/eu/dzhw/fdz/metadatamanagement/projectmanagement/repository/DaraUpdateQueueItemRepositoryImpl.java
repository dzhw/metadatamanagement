package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DaraUpdateQueueItem;

/**
 * Implementation of the Dara Update Queue Item Repository.
 * 
 * @author Daniel Katzberg
 *
 */
@Component
public class DaraUpdateQueueItemRepositoryImpl implements DaraUpdateQueueItemRepositoryCustom {

  //update lock is valid for 10 minutes
  private static final int UPDATE_LOCK_EXPIRED = 10;

  //number of queue items to be processed in one batch
  private static final int BULK_SIZE = 5;

  @Autowired
  private MongoOperations mongoOperations;
  
  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.projectmanagement.repository
   *    .DaraUpdateQueueItemRepositoryCustom
   *    #lockAllUnlockedOrExpiredItems(java.time.LocalDateTime, java.lang.String)
   */
  @Override
  public void lockAllUnlockedOrExpiredItems(LocalDateTime updateStartedAt, String updateStartedBy) {
    Query query = new Query(getUnlockedOrExpiredCriteria()).limit(BULK_SIZE);
    Update update = new Update()
        .set("updateStartedAt", updateStartedAt).set("updateStartedBy", updateStartedBy);
    mongoOperations.updateMulti(query, update, DaraUpdateQueueItem.class);
  }

  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.projectmanagement.repository
   *    .DaraUpdateQueueItemRepositoryCustom
   *    #findOldestLockedItems(java.lang.String, java.time.LocalDateTime)
   */
  @Override
  public List<DaraUpdateQueueItem> findOldestLockedItems(String updateStartedBy,
      LocalDateTime updateStartedAt) {
    Query query = new Query(new Criteria().andOperator(Criteria.where("updateStartedBy")
        .is(updateStartedBy),
        Criteria.where("updateStartedAt")
          .is(updateStartedAt))).limit(BULK_SIZE)
            .with(new Sort(Direction.ASC, "createdDate"));
    return mongoOperations.find(query, DaraUpdateQueueItem.class);
  }  
  
  private Criteria getUnlockedOrExpiredCriteria() {
    return new Criteria().orOperator(
        Criteria.where("updateStartedAt").lte(LocalDateTime.now()
            .minusMinutes(UPDATE_LOCK_EXPIRED)),
        Criteria.where("updateStartedAt").exists(false));
  }

  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.projectmanagement
   *    .repository.DaraUpdateQueueItemRepositoryCustom#unlockItem
   *    (eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DaraUpdateQueueItem)
   */
  @Override
  public void unlockItem(DaraUpdateQueueItem item) {
    Query query = new Query(Criteria.where("id").is(item.getId()));
    Update update = new Update()
        .unset("updateStartedAt")
        .unset("updateStartedBy");
    mongoOperations.updateFirst(query, update, DaraUpdateQueueItem.class);    
  }  
}
