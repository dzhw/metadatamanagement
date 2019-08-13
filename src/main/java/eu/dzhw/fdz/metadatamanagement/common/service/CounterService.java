package eu.dzhw.fdz.metadatamanagement.common.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.Counter;
import lombok.RequiredArgsConstructor;

/**
 * Service which generates sequence numbers as in (good) old JPA times.
 * 
 * @author René Reitmann
 */
@Service
@RequiredArgsConstructor
public class CounterService {
  
  private final MongoOperations mongo;

  /**
   * Return the next sequence number of the given sequence.
   * 
   * @param sequenceName The name of the sequence.
   * @return the next sequence number of the given sequence.
   */
  public long getNextSequence(String sequenceName) {
    Counter counter = mongo.findAndModify(query(Criteria.where("_id").is(sequenceName)),
        new Update().inc("seq", 1), options().returnNew(true), Counter.class);

    if (counter == null) {
      throw new NotImplementedException("Unknown sequence " + sequenceName);
    }

    return counter.getSeq();
  }
}
