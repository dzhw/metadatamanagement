package eu.dzhw.fdz.metadatamanagement.common.websocket.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.dzhw.fdz.metadatamanagement.common.websocket.domain.ActiveWebsocketSession;

/**
 * Repository for keeping track of currently active websocket sessions.
 * 
 * @author René Reitmann
 */
@Repository
public interface ActiveWebSocketSessionRepository
    extends MongoRepository<ActiveWebsocketSession, String> {

}
