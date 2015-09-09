/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.datatypes;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFormDto;

/**
 * @author Daniel Katzberg
 *
 */
public class PageWithBucketsTest extends AbstractWebTest {

  @Autowired
  private DataTypesProvider dataTypesProvider;

  @Autowired
  private ScaleLevelProvider scaleLevelProvider;

  @Autowired
  private VariableService variableService;

  @Before
  public void before() {
    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      LocaleContextHolder.setLocale(locale);
      for (int i = 1; i <= 9; i++) {
        VariableSurvey variableSurvey = new VariableSurveyBuilder()
            .withSurveyId("SearchUnitTest_Survey_ID").withTitle("SearchUnitTestTitle 0" + i)
            .withVariableAlias("SearchUnitTestVariableAlias 0" + i).build();

        VariableDocument variableDocument = new VariableDocumentBuilder()
            .withId("SearchUnitTest_ID0" + i).withName("SearchUnitTestName 0" + i)
            .withLabel("SearchUnitTestLabel 0" + i).withQuestion("SearchUnitTestQuestion 0" + i)
            .withDataType(this.dataTypesProvider.getNumericValueByLocale())
            .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
            .withVariableSurvey(variableSurvey).build();
        this.variableService.save(variableDocument);
      }
    }
  }

  @After
  public void after() {
    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      LocaleContextHolder.setLocale(locale);
      // Delete
      for (int i = 1; i <= 9; i++) {
        this.variableService.delete("SearchUnitTest_ID0" + i);
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testHashCode() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(get("/de/variables/search?query=SearchUnitTestName&scaleLevel=metrisch"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act
    PageWithBuckets<VariableDocument> pageableAggregrationType =
        (PageWithBuckets<VariableDocument>) ((ModelAndView) mvcResult.getAsyncResult())
            .getModelMap().get("pageableWithBuckets");

    VariableSearchFormDto variableSearchFormDto = (VariableSearchFormDto) ((ModelAndView) mvcResult.getAsyncResult())
        .getModelMap().get("searchFormDto");


    // Assert
    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult.getAsyncResult(),
        "variables/search");
    assertThat(variableSearchFormDto.getQuery(), is("SearchUnitTestName"));
    assertThat(variableSearchFormDto.getScaleLevel(), is("metrisch"));
    assertThat(pageableAggregrationType.hashCode(), not(0));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testEquals() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(get("/de/variables/search?query=SearchUnitTestName&scaleLevel=metrisch"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();
    PageWithBuckets<VariableDocument> pageWithBuckets =
        (PageWithBuckets<VariableDocument>) ((ModelAndView) mvcResult.getAsyncResult())
            .getModelMap().get("pageableWithBuckets");
    MvcResult mvcResult2 = this.mockMvc
        .perform(get("/de/variables/search?query=SearchUnitTestName&scaleLevel=nominal"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();
    PageWithBuckets<VariableDocument> pageWithBuckets2 =
        (PageWithBuckets<VariableDocument>) ((ModelAndView) mvcResult2.getAsyncResult())
            .getModelMap().get("pageableAggregrationType");

    // Act
    VariableSearchFormDto variableSearchFormDto = (VariableSearchFormDto) ((ModelAndView) mvcResult.getAsyncResult())
        .getModelMap().get("searchFormDto");
    VariableSearchFormDto searchFormDto2 = (VariableSearchFormDto) ((ModelAndView) mvcResult2.getAsyncResult())
        .getModelMap().get("searchFormDto");

    boolean checkNullObject = pageWithBuckets.equals(null);
    boolean checkSame = pageWithBuckets.equals(pageWithBuckets);
    boolean checkDifferentClass = pageWithBuckets.equals(new Object());
    boolean checkSurveyOther = pageWithBuckets.equals(pageWithBuckets2);

    // Assert
    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult.getAsyncResult(),
        "variables/search");
    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult2.getAsyncResult(),
        "variables/search");
    assertThat(variableSearchFormDto.getQuery(), is("SearchUnitTestName"));
    assertThat(variableSearchFormDto.getScaleLevel(), is("metrisch"));
    assertThat(searchFormDto2.getQuery(), is("SearchUnitTestName"));
    assertThat(searchFormDto2.getScaleLevel(), is("nominal"));
    assertEquals(false, checkNullObject);
    assertEquals(true, checkSame);
    assertEquals(false, checkDifferentClass);
    assertEquals(false, checkSurveyOther);
  }
}
