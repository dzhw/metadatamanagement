package eu.dzhw.fdz.metadatamanagement.ordermanagement.service;

import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderState;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for creating and managing orders.
 * @author René Reitmann
 */
@Service
@Slf4j
public class OrderService {
  @Autowired
  private OrderRepository orderRepository;

  @Value("${metadatamanagement.ordermanagement.email}")
  private String ccEmail;

  @Value("${metadatamanagement.server.instance-index}")
  private Integer instanceId;

  /**
   * Create the given order and save it with the correct state.
   * @param order the order to be saved
   */
  public Order create(Order order) {
    order.setState(OrderState.CREATED);
    return orderRepository.save(order);
  }
}
