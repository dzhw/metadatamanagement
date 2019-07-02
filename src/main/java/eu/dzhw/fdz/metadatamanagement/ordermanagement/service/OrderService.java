package eu.dzhw.fdz.metadatamanagement.ordermanagement.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderAlreadyCompletedException;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderState;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for creating and managing orders.
 * @author René Reitmann
 */
@Service
@Slf4j
public class OrderService {
  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private MailService mailService;

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

  /**
   * Send notification emails for every created order every minute.
   */
  @Scheduled(fixedRate = 1000 * 60, initialDelay = 1000 * 60)
  public void sendEmailsForCreatedOrders() {
    if (instanceId != 0) {
      return;
    }
    log.info("Starting processing created orders...");
    try (Stream<Order> orders = orderRepository.findByState(OrderState.CREATED)) {
      orders.forEach(order -> {
        mailService.sendOrderCreatedMail(order, ccEmail, new String[] {ccEmail});
        order.setState(OrderState.NOTIFIED);
        orderRepository.save(order);
      });
    }
    log.info("Finished processing created orders...");
  }
}
