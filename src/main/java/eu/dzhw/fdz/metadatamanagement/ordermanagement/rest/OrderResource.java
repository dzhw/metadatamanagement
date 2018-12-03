package eu.dzhw.fdz.metadatamanagement.ordermanagement.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.service.OrderService;

/**
 * REST controller for ordering data products.
 */
@RestController
public class OrderResource {

  @Autowired
  private OrderService orderService;

  /**
   * Order data products.
   */
  @RequestMapping(value = "/api/order", method = RequestMethod.POST)
  public ResponseEntity<?> create(@RequestBody @Valid Order order) {
    orderService.create(order);
    return ResponseEntity.ok().build();
  }
}
