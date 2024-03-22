package eu.dzhw.fdz.metadatamanagement.ordermanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Set;

import org.javers.common.collections.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderedAnalysisPackage;
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


  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @AfterEach
  public void cleanUp() {
    this.orderRepository.deleteAll();
  }

  @Test
  public void updateOrder() throws Exception {
    Order order = createOrder();
    mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(order))).andExpect(status().isCreated());

    order = orderRepository.findAll().get(0);
    order.getProducts().add(createProduct(order.getId()));

    mockMvc
        .perform(put(UPDATE_ORDER_URL + order.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
        .andExpect(status().isOk()).andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.id", equalTo(order.getId())))
        .andExpect(jsonPath("$.version", equalTo((int) (order.getVersion() + 1))));

    mockMvc.perform(get(UPDATE_ORDER_URL + order.getId())).andExpect(status().isOk())
        .andExpect(jsonPath("$.products.length()", is(1)))
        .andExpect(jsonPath("$.products[0].dataPackage.title.de",
            is(order.getProducts().get(0).getDataPackage().getTitle().getDe())));

    // now update as DLP
    order = orderRepository.findAll().get(0);
    order.getProducts().remove(0);
    order.setClient(OrderClient.DLP);

    mockMvc
        .perform(put(UPDATE_ORDER_URL + order.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
        .andExpect(status().isOk()).andExpect(header().exists("Location"))
        .andExpect(header().string("Location", containsString("/shopping-cart/" + order.getId())))
        .andExpect(jsonPath("$.id", equalTo(order.getId())));
  }

  @Test
  public void testNotFound() throws Exception {
    mockMvc.perform(get(UPDATE_ORDER_URL + "spaß")).andExpect(status().isNotFound());

    Order order = createOrder();
    mockMvc.perform(put("/api/orders/spaß").contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(order))).andExpect(status().isNotFound());
  }

  @Test
  public void testCreateAsDlp() throws Exception {
    Order order = createOrder();
    order.setClient(OrderClient.DLP);
    mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(order))).andExpect(status().isBadRequest());
  }

  @Test
  public void updateOrder_optimistic_locking_is_ignored() throws Exception {
    Order order = createOrder();
    order = orderRepository.save(order);
    order.getProducts().add(createProduct(order.getId()));
    order.setVersion(-1L);

    mockMvc.perform(put(UPDATE_ORDER_URL + order.getId()).contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(order))).andExpect(status().isOk());
  }

  @Test
  public void updateOrder_already_ordered() throws Exception {
    Order order = createOrder();
    order.setState(OrderState.ORDERED);
    order = orderRepository.save(order);
    order.getProducts().add(createProduct(order.getId()));

    mockMvc.perform(put(UPDATE_ORDER_URL + order.getId()).contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(order))).andExpect(status().isBadRequest());
  }

  @Test
  public void shouldFailToCreateOrderWithoutDataPackageAndWithoutAnalysisPackage()
      throws Exception {
    Order order = createOrder();
    order.getProducts().add(createProduct("gra2005"));
    order.getProducts().get(0).setDataPackage(null);

    mockMvc
        .perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message", is(
            "eu.dzhw.fdz.metadatamanagement.domain.validationorder-management.error."
            + "either-analysis-package-or-data-package")));
  }

  @Test
  public void shouldFailToCreateOrderWithDataPackageAndWithAnalysisPackage()
      throws Exception {
    Order order = createOrder();
    order.getProducts().add(createProduct("gra2005"));
    OrderedAnalysisPackage analysisPackage = new OrderedAnalysisPackage();
    analysisPackage.setId("ana-gra2005$-1.0.0");
    analysisPackage.setTitle(new I18nString("Titel", "Title"));;
    order.getProducts().get(0).setAnalysisPackage(analysisPackage);

    mockMvc
        .perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message", is(
            "eu.dzhw.fdz.metadatamanagement.domain.validationorder-management.error."
            + "either-analysis-package-or-data-package")));
  }

  @Test
  public void shouldCreateOrderWithWithAnalysisPackage()
      throws Exception {
    Order order = createOrder();
    order.getProducts().add(createProduct("gra2005"));
    OrderedAnalysisPackage analysisPackage = new OrderedAnalysisPackage();
    analysisPackage.setId("ana-gra2005$-1.0.0");
    analysisPackage.setTitle(new I18nString("Titel", "Title"));;
    order.getProducts().get(0).setAnalysisPackage(analysisPackage);
    order.getProducts().get(0).setDataPackage(null);

    mockMvc
        .perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
        .andExpect(status().isCreated());
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
    return new Product(dataAcquisitionProjectId, dataPackage, null,
        "remote-desktop-suf", "1.0.0", Set.of(DataFormat.R));
  }
}
