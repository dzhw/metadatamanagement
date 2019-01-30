package eu.dzhw.fdz.metadatamanagement.ordermanagement.service;

import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderState;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

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
        mailService.sendOrderCreatedMail(order, ccEmail);
        order.setState(OrderState.NOTIFIED);
        orderRepository.save(order);
      });
    }
    log.info("Finished processing created orders...");
  }
}
