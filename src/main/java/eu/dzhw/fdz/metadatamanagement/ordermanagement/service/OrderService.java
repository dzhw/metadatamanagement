package eu.dzhw.fdz.metadatamanagement.ordermanagement.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderAlreadyCompletedException;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderState;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.repository.OrderRepository;


/**
 * Service for creating and managing orders.
 * @author René Reitmann
 */
@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  /**
   * Create the given order and save it with the correct state.
   * @param order the order to be saved
   */
  public Order create(Order order) {
    order.setState(OrderState.CREATED);
    return orderRepository.save(order);
  }

  /**
   * Update an existing order with the given order data.
   * @param orderId       Id of the order that should be updated
   * @param orderToUpdate order data to use in the update
   * @return Optional of the updated order, might contain nothing if order could not be found
   */
  public Optional<Order> update(String orderId, Order orderToUpdate) {
    Optional<Order> optional = orderRepository.findById(orderId);
    if (optional.isPresent()) {
      Order persistedOrder = optional.get();
      if (persistedOrder.getState() == OrderState.ORDERED) {
        throw new OrderAlreadyCompletedException();
      }
      BeanUtils.copyProperties(orderToUpdate, persistedOrder, "id", "createdDate", "createdBy");
      persistedOrder = orderRepository.save(persistedOrder);
      return Optional.of(persistedOrder);
    } else {
      return Optional.empty();
    }
  }
}
