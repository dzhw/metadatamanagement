package eu.dzhw.fdz.metadatamanagement.ordermanagement.rest;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorDto;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorListDto;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderAlreadyCompletedException;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderClient;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Product;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.projection.IdAndVersionOrderProjection;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.repository.OrderRepository;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.service.OrderService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for ordering data products.
 */
@RestController
@Tag(name = "Order Resource",
    description = "Endpoints used by the MDM and the DLP to manage orders.")
@RequiredArgsConstructor
@Slf4j
public class OrderResource {

  private final OrderRepository orderRepository;

  private final OrderService orderService;

  private final ProjectionFactory projectionFactory;

  @Value("${metadatamanagement.server.context-root}")
  private String baseUrl;

  @Value("${metadatamanagement.dlp.endpoint}")
  private String dlpUrl;

  /**
   * Order data products.
   */
  @PostMapping("/api/orders")
  @Operation(summary = "The MDM creates orders and sends them to the DLP with this endpoint.")
  @ApiResponses(value = {@ApiResponse(responseCode = "201",
      description = "Successfully created a new order."
          + " Follow the returned Location header to proceed with the order process.",
      headers = {
          @Header(name = "Location", description = "URL to which the client should go now.")})})
  @ResponseStatus(value = HttpStatus.CREATED)
  public ResponseEntity<IdAndVersionOrderProjection> createOrder(@RequestBody @Valid Order order) {
    log.debug("Create new order: {}", order);
    if (order.getClient() != OrderClient.MDM) {
      return ResponseEntity.badRequest().build();
    }

    order = orderService.create(order);

    return ResponseEntity
        .created(UriComponentsBuilder
            .fromUriString(getDlpUrl(order.getId(), order.getLanguageKey())).build().toUri())
        .body(projectionFactory.createProjection(IdAndVersionOrderProjection.class, order));
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   *
   * @param id a order id
   * @return the Order or not found
   */
  @GetMapping("/api/orders/{id:.+}")
  @Operation(summary = "Get the current status of the order as it is stored in the MDM.")
  public ResponseEntity<Order> findOrder(@PathVariable String id) {
    Optional<Order> optional = orderRepository.findById(id);

    if (optional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Order entity = optional.get();

    // do not provide field remarksUserService to users without role PUBLISHER
    if (SecurityContextHolder.getContext() == null
        || SecurityContextHolder.getContext().getAuthentication() == null
        || SecurityContextHolder.getContext().getAuthentication().getAuthorities() == null
        || !(SecurityContextHolder.getContext().getAuthentication().getAuthorities()
        .contains(new SimpleGrantedAuthority(AuthoritiesConstants.PUBLISHER)))) {
      for (Product product : entity.getProducts()) {
        if (product != null && product.getDataPackage() != null) {
          product.getDataPackage().setRemarksUserService(null);
        }
      }
    }

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
  @Operation(summary = "The DLP and MDM use this endpoint to update an order in the MDM.")
  @ApiResponses(value = {@ApiResponse(responseCode = "200",
      description = "Successfully updated the order."
          + " Follow the returned Location header to proceed with the order process.",
      headers = @Header(name = "Location",
          description = "URL to which the client should go now."))})
  public ResponseEntity<IdAndVersionOrderProjection> updateOrder(@PathVariable String id,
      @RequestBody @Valid Order order) {
    log.debug("Update order: {}", order);
    Optional<Order> optional = orderService.update(id, order);
    if (optional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    String destinationUrl;
    if (order.getClient().equals(OrderClient.MDM)) {
      destinationUrl = getDlpUrl(id, order.getLanguageKey());
    } else {
      destinationUrl = baseUrl + "/" + order.getLanguageKey() + "/shopping-cart/" + order.getId();
    }

    Order persistedOrder = optional.get();

    return ResponseEntity.status(HttpStatus.OK)
        .location(UriComponentsBuilder.fromUriString(destinationUrl).build().toUri()).body(
            projectionFactory.createProjection(IdAndVersionOrderProjection.class, persistedOrder));
  }

  /**
   * Generate a DLP url for the given order id.
   *
   * @param orderId Order Id
   * @param language the language of the order
   * @return URL as string
   */
  private String getDlpUrl(String orderId, String language) {
    Map<String, String> pathVariables = new HashMap<>();
    pathVariables.put("language", language);
    return UriComponentsBuilder.fromHttpUrl(dlpUrl).queryParam("mdm_order_id", orderId)
        .buildAndExpand(pathVariables).toUriString();
  }

  @ExceptionHandler(OrderAlreadyCompletedException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ErrorListDto handleOrderAlreadyCompletedException(
      OrderAlreadyCompletedException exception) {
    log.error("Illegal attempt to modify an already completed order.", exception);
    ErrorDto errorDto =
        new ErrorDto(null, "order-management.error." + "order-already-completed", null, null);
    ErrorListDto errorListDto = new ErrorListDto();
    errorListDto.add(errorDto);
    return errorListDto;
  }
}
