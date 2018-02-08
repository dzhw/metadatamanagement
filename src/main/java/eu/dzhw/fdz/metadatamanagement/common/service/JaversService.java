package eu.dzhw.fdz.metadatamanagement.common.service;

import org.javers.core.Javers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * Service for low level access to the {@link Javers} mongodb collections.
 * 
 * @author René Reitmann
 */
@Service
public class JaversService {
  private static final String SNAPSHOTS_COLLECTION = "jv_snapshots";
  private static final String HEAD_ID_COLLECTION = "jv_head_id";
  
  @Autowired
  private MongoTemplate mongoTemplate;
  
  /**
   * Remove all documents from the history collections.
   */
  public void deleteAll() {
    mongoTemplate.remove(new Query(), SNAPSHOTS_COLLECTION);
    mongoTemplate.remove(new Query(), HEAD_ID_COLLECTION);
  }
}
