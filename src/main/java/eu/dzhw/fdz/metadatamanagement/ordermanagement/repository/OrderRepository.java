package eu.dzhw.fdz.metadatamanagement.ordermanagement.repository;

import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderState;

/**
 * Repository for accessing {@link Order}s.
 * 
 * @author René Reitmann
 */
public interface OrderRepository
    extends MongoRepository<Order, String>, CrudRepository<Order, String> {
  Stream<Order> findByState(OrderState state);
}
