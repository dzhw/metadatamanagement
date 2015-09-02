package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * Test which checks if the {@link VariableDetailsController} answers as expected
 * 
 * @author René Reitmann
 */
public class VariableDetailsControllerTest extends AbstractWebTest {

  @Autowired
  private VariableService variableService;

  @Autowired
  private DataTypesProvider dataTypesProvider;

  @Autowired
  private ScaleLevelProvider scaleLevelProvider;

  @Before
  public void before() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    for (int i = 1; i <= 9; i++) {
      VariableSurvey variableSurvey = new VariableSurveyBuilder()
          .withSurveyId("GetByValidTest_Survey_ID").withTitle("GetByValidTestTitle 0" + i)
          .withVariableAlias("GetByValidTestVariableAlias 0" + i).build();

      VariableDocument variableDocument = new VariableDocumentBuilder()
          .withId("GetByValidTest_ID0" + i).withName("GetByValidTestName 0" + i)
          .withLabel("GetByValidTestLabel 0" + i).withQuestion("GetByValidTestQuestion 0" + i)
          .withDataType(this.dataTypesProvider.getNumericValueByLocale())
          .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
          .withVariableSurvey(variableSurvey).build();
      this.variableService.save(variableDocument);
    }
  }

  @After
  public void after() {
    // Delete
    for (int i = 1; i <= 9; i++) {
      this.variableService.delete("GetByValidTest_ID0" + i);
    }
  }

  @Test
  public void testGetByValidId() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/GetByValidTest_ID01"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ResponseEntity.class))).andReturn();

    this.mockMvc
        // wait for the async result
        .perform(asyncDispatch(mvcResult)).andExpect(status().isOk());
  }

  @Test
  public void testGetByInValidId() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/fjsgjfd"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted()).andReturn();

    this.mockMvc
        // wait for the async result
        .perform(asyncDispatch(mvcResult)).andExpect(status().isInternalServerError());
  }
}
