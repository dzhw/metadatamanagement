package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Locale;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;
import eu.dzhw.fdz.metadatamanagement.web.common.exceptions.DocumentNotFoundException;

/**
 * Test which checks if the {@link VariableDetailsController} answers as expected
 * 
 * @author Amine Limouri
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

    createVariable(Locale.GERMAN, "GetByValidTest_ID01", "GetByValidTestName 01",
        "GetByValidTestLabel 01", "GetByValidTestQuestion 01", "GetByValidTestTitle 01",
        "GetByValidTestVariableAlias 01", "GetByValidTest_Survey_ID");

    createVariable(Locale.ENGLISH, "GetByValidTest_ID01", "GetByValidTestName 01",
        "GetByValidTestLabel 01", "GetByValidTestQuestion 01", "GetByValidTestTitle 01",
        "GetByValidTestVariableAlias 01", "GetByValidTest_Survey_ID");
  }

  @After
  public void after() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    this.variableService.delete("GetByValidTest_ID01");

    LocaleContextHolder.setLocale(Locale.ENGLISH);
    this.variableService.delete("GetByValidTest_ID01");
  }

  @Test
  public void testGermanTemplate() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/GetByValidTest_ID01"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??"))));
  }

  @Test
  public void testEnglishTemplate() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/en/variables/GetByValidTest_ID01"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Language"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??"))));
  }

  @Test
  public void testGetByValidId() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/GetByValidTest_ID01"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??"))));

    VariableDocument variableDocument =
        (VariableDocument) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("variableDocument");
    VariableDetailsResource resource =
        (VariableDetailsResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    assertThat(variableDocument.getId(), is("GetByValidTest_ID01"));
    assertThat(resource.getEnglishLink().getHref().contains("/en/variables/GetByValidTest_ID01"),
        is(true));
  }

  @Test
  public void testGetByInvalidId() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/GetByInvalidTest_ID012"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(DocumentNotFoundException.class))).andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isNotFound())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??"))))
        .andExpect(view().name("exceptionView"))
        .andExpect(model().attribute("navMessageLanguage", "nav.language.german"))
        .andExpect(model().attribute("documentType", "documenttype.variable"))
        .andExpect(model().attribute("resource",
            Matchers.hasProperty("exception",
                Matchers.hasProperty("unknownId", Matchers.equalTo("GetByInvalidTest_ID012")))))
        .andExpect(model().attribute("documentType", "documenttype.variable"));

  }

  public void createVariable(Locale locale, String variableId, String variableName,
      String variableLabel, String question, String title, String alias, String surveyId) {

    LocaleContextHolder.setLocale(locale);
    VariableSurvey variableSurvey = new VariableSurveyBuilder().withSurveyId(surveyId)
        .withTitle(title).withVariableAlias(alias).build();

    VariableDocument variableDocument = new VariableDocumentBuilder().withId(variableId)
        .withName(variableName).withLabel(variableLabel).withQuestion(question)
        .withDataType(this.dataTypesProvider.getNumericValueByLocale())
        .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
        .withVariableSurvey(variableSurvey).build();

    this.variableService.save(variableDocument);

  }
}
