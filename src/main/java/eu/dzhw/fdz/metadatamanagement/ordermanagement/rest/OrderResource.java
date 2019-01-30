package eu.dzhw.fdz.metadatamanagement.ordermanagement.rest;

import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderClient;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.repository.OrderRepository;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * REST controller for ordering data products.
 */
@RestController
@Api(value = "Order Resource",
    description = "Endpoints used by the MDM and the DLP to manage orders.")
public class OrderResource {
  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderService orderService;

  @Value("${metadatamanagement.server.context-root}")
  private String baseUrl;

  @Value("${metadatamanagement.dlp.endpoint}")
  private String dlpUrl;

  /**
   * Order data products.
   */
  @PostMapping("/api/orders")
  @ApiOperation("The MDM creates orders and sends them to the DLP with this endpoint.")
  @ApiResponses(value = {@ApiResponse(code = 201,
      message = "Successfully created a new order."
          + " Follow the returned Location header to proceed with the order process.",
      responseHeaders = {@ResponseHeader(name = "Location", response = URI.class,
          description = "URL to which the client should go now.")})})
  @ResponseStatus(value = HttpStatus.CREATED)
  public ResponseEntity<?> createOrder(@RequestBody @Valid Order order) {

    if (order.getClient() != OrderClient.MDM) {
      return ResponseEntity.badRequest().build();
    }

    order = orderService.create(order);

    return ResponseEntity
        .created(UriComponentsBuilder.fromUriString(getDlpUrl(order.getId())).build().toUri())
        .build();
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   * @param id a order id
   * @return the Order or not found
   */
  @GetMapping("/api/orders/{id:.+}")
  @ApiOperation("Get the current status of the order as it is stored in the MDM.")
  public ResponseEntity<Order> findOrder(@PathVariable String id) {
    Optional<Order> optional = orderRepository.findById(id);

    if (!optional.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    Order entity = optional.get();
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
        .eTag(entity.getVersion().toString())
        .lastModified(
            entity.getLastModifiedDate().atZone(ZoneId.of("GMT")).toInstant().toEpochMilli())
        .body(entity);
  }

  /**
   * Update the order to add or remove products for instance.
   */
  @PutMapping("/api/orders/{id:.+}")
  @ApiOperation("The DLP and MDM use this endpoint to update an order in the MDM.")
  @ApiResponses(value = {@ApiResponse(code = 200,
      message = "Successfully updated the order."
          + " Follow the returned Location header to proceed with the order process.",
      responseHeaders = @ResponseHeader(name = "Location", response = URI.class,
          description = "URL to which the client should go now."))})
  public ResponseEntity<?> updateOrder(@PathVariable String id, @RequestBody @Valid Order order) {
    Optional<Order> optional = orderService.update(id, order);
    if (!optional.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    String destinationUrl;
    if (order.getClient().equals(OrderClient.MDM)) {
      destinationUrl = getDlpUrl(id);
    } else {
      destinationUrl = baseUrl + "/#!/" + order.getLanguageKey() + "/shopping-cart/"
          + order.getId();
    }

    return ResponseEntity.status(HttpStatus.OK)
        .location(UriComponentsBuilder.fromUriString(destinationUrl).build().toUri()).build();
  }

  /**
   * Generate a DLP url for the given order id.
   *
   * @param orderId Order Id
   * @return URL as string
   */
  private String getDlpUrl(String orderId) {
    return UriComponentsBuilder.fromHttpUrl(dlpUrl)
        .queryParam("mdm_order_id", orderId)
        .toUriString();
  }
}
