package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem;

/**
 * Custom repository for retrieving locked and unlocked shadow copy queue items.
 */
@Component
public class ShadowCopyQueueRepositoryCustomImpl implements ShadowCopyQueueRepositoryCustom {

  private MongoOperations mongoOperations;

  /**
   * Creates a new instance.
   */
  public ShadowCopyQueueRepositoryCustomImpl(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public void lockAllUnlockedOrExpiredItems(LocalDateTime updateStartedAt, String updateStartedBy) {
    Criteria unlockedOrExpiredCriteria = new Criteria().orOperator(
        Criteria.where("updateStartedAt").lte(LocalDateTime.now()
            .minusMinutes(10)),
        Criteria.where("updateStartedAt").exists(false));

    Query query = new Query(unlockedOrExpiredCriteria);
    Update update = new Update()
        .set("updateStartedAt", updateStartedAt).set("updateStartedBy", updateStartedBy);
    mongoOperations.updateMulti(query, update, ShadowCopyQueueItem.class);
  }

  @Override
  public List<ShadowCopyQueueItem> findOldestLockedItems(LocalDateTime updateStartedAt,
      String updateStartedBy) {
    Query query = new Query(new Criteria().andOperator(Criteria.where("updateStartedBy")
            .is(updateStartedBy),
        Criteria.where("updateStartedAt")
            .is(updateStartedAt)))
        .with(Sort.by(Sort.Direction.ASC, "createdDate"));
    return mongoOperations.find(query, ShadowCopyQueueItem.class);
  }
}
