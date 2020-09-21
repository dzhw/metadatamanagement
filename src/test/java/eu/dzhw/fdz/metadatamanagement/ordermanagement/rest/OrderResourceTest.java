package eu.dzhw.fdz.metadatamanagement.ordermanagement.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Set;

import org.javers.common.collections.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataFormat;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderClient;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderState;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderedDataPackage;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Product;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.repository.OrderRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.DataTypes;

public class OrderResourceTest extends AbstractTest {

  private static final String UPDATE_ORDER_URL = "/api/orders/";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private OrderRepository orderRepository;

  private MockMvc mockMvc;


  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
        .build();
  }

  @After
  public void cleanUp() {
    this.orderRepository.deleteAll();
  }

  @Test
  public void updateOrder() throws Exception {
    Order order = createOrder();
    order = orderRepository.save(order);
    order.getProducts().add(createProduct(order.getId()));

    mockMvc.perform(put(UPDATE_ORDER_URL + order.getId()).contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(order)))
        .andExpect(status().isOk())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.id", equalTo(order.getId())))
        .andExpect(jsonPath("$.version", equalTo((int) (order.getVersion() + 1))));
  }

  @Test
  public void updateOrder_optimistic_locking() throws Exception {
    Order order = createOrder();
    order = orderRepository.save(order);
    order.getProducts().add(createProduct(order.getId()));
    order.setVersion(-1L);

    mockMvc.perform(put(UPDATE_ORDER_URL + order.getId()).contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(order)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void updateOrder_already_ordered() throws Exception {
    Order order = createOrder();
    order.setState(OrderState.ORDERED);
    order = orderRepository.save(order);
    order.getProducts().add(createProduct(order.getId()));

    mockMvc.perform(put(UPDATE_ORDER_URL + order.getId()).contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(order)))
        .andExpect(status().isBadRequest());
  }

  private Order createOrder() {
    Order order = new Order();
    order.setState(OrderState.CREATED);
    order.setLanguageKey("de");
    order.setClient(OrderClient.MDM);
    order.setProducts(new ArrayList<>());

    return order;
  }

  private Product createProduct(String dataAcquisitionProjectId) {
    OrderedDataPackage dataPackage = new OrderedDataPackage();
    dataPackage.setSurveyDataTypes(Lists.immutableListOf(DataTypes.QUALITATIVE_DATA));
    dataPackage.setId("stu-" + dataAcquisitionProjectId + "$");
    I18nString title = new I18nString("test", "test");
    dataPackage.setTitle(title);
    return new Product(dataAcquisitionProjectId, dataPackage, "remote-desktop-suf", "1.0.0",
        Set.of(DataFormat.R));
  }
}