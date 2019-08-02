package eu.dzhw.fdz.metadatamanagement.common.service;

import org.javers.core.Javers;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Service for low level access to the {@link Javers} mongodb collections.
 * 
 * @author René Reitmann
 */
@Service
@RequiredArgsConstructor
public class JaversService {
  private static final String SNAPSHOTS_COLLECTION = "jv_snapshots";
  private static final String HEAD_ID_COLLECTION = "jv_head_id";
  
  private final MongoTemplate mongoTemplate;
  
  /**
   * Remove all documents from the history collections.
   */
  public void deleteAll() {
    mongoTemplate.remove(new Query(), SNAPSHOTS_COLLECTION);
    mongoTemplate.remove(new Query(), HEAD_ID_COLLECTION);
  }
}
