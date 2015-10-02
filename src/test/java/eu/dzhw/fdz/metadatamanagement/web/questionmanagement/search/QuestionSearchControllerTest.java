/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionSurvey;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.service.questionmanagement.QuestionService;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.search.dto.QuestionSearchFilter;

/**
 * @author Daniel Katzberg
 *
 */
public class QuestionSearchControllerTest extends AbstractTest {

  @Autowired
  private QuestionService questionService;

  @Before
  public void before() {
    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      LocaleContextHolder.setLocale(locale);
      for (int i = 1; i <= 9; i++) {
        QuestionSurvey questionSurvey =
            new QuestionSurveyBuilder().withSurveyId("SurveyID").withTitle("SurveyTitel_0" + i)
                .withSurveyPeriod(new DateRangeBuilder().withEndDate(LocalDate.now().plusDays(2))
                    .withStartDate(LocalDate.now().minusDays(2)).build())
                .build();
        QuestionDocument questionDocument =
            new QuestionDocumentBuilder().withId("SearchUnitTest_ID0" + i).withName("Name_0" + i)
                .withQuestion("Question_0" + i).withQuestionSurvey(questionSurvey).build();

        this.questionService.save(questionDocument);
      }
    }
  }

  @After
  public void after() {
    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      LocaleContextHolder.setLocale(locale);
      // Delete
      for (int i = 1; i <= 9; i++) {
        this.questionService.delete("SearchUnitTest_ID0" + i);
      }
    }
  }
  
  @Test
  public void testGermanTemplate() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/de/questions/search"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    QuestionSearchResource resource =
        (QuestionSearchResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??")))).andReturn();

    // W3C Validation Check
    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "QuestionSearchControllerTest.testGermanTemplate");

    assertThat(resource.getPage().getContent().size(), is(greaterThan(0)));
  }

  @Test
  public void testEnglishTemplate() throws Exception {
    // Arrange
    MvcResult mvcResult = this.mockMvc.perform(get("/en/questions/search"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();
    QuestionSearchResource resource =
        (QuestionSearchResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Language"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??")))).andReturn();

    // Act
    // W3C Validation Check
    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "QuestionSearchControllerTest.testEnglishTemplate");

    // Assert
    assertThat(resource.getPage().getContent().size(), is(greaterThan(0)));
  }

  @Test
  public void testSearch() throws Exception {

    // Arrange
    MvcResult mvcResult =
        this.mockMvc.perform(get("/de/questions/search?query=SearchUnitTest_Survey_ID"))
            .andExpect(status().isOk()).andExpect(request().asyncStarted())
            .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act
    QuestionSearchResource resource =
        (QuestionSearchResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    QuestionSearchFilter questionSearchFilter =
        (QuestionSearchFilter) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("searchFilter");

    // Assert
    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult.getAsyncResult(),
        "questions/search");
    ModelAndViewAssert.assertModelAttributeAvailable((ModelAndView) mvcResult.getAsyncResult(),
        "resource");
    ModelAndViewAssert.assertAndReturnModelAttributeOfType(
        (ModelAndView) mvcResult.getAsyncResult(), "resource", QuestionSearchResource.class);

    mvcResult =
        this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk()).andReturn();

    // W3C Validation Check
    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "QuestionSearchControllerTest.testSearch");

    assertThat(questionSearchFilter.getQuery(), is("SearchUnitTest_Survey_ID"));
    assertThat(resource.getPage().getContent().size(), is(9));
  }

  @Test
  public void testSearchWithPage() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(get("/de/questions/search?query=SearchUnitTest_Survey_ID&page=1&size=3"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act
    QuestionSearchFilter questionSearchFilter =
        (QuestionSearchFilter) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("searchFilter");

    QuestionSearchResource resource =
        (QuestionSearchResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    // Assert
    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult.getAsyncResult(),
        "questions/search");
    ModelAndViewAssert.assertModelAttributeAvailable((ModelAndView) mvcResult.getAsyncResult(),
        "resource");
    ModelAndViewAssert.assertAndReturnModelAttributeOfType(
        (ModelAndView) mvcResult.getAsyncResult(), "resource", QuestionSearchResource.class);

    mvcResult =
        this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk()).andReturn();

    // W3C Validation Check
    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "QuestionSearchControllerTest.testSearchWithPage");

    assertThat(questionSearchFilter.getQuery(), is("SearchUnitTest_Survey_ID"));
    assertThat(resource.getPage().getContent().size(), is(3));
    assertThat(resource.getPage().getId().getHref()
        .contains("/de/questions/search?query=SearchUnitTest_Survey_ID&page=1&size=3"), is(true));
    assertThat(resource.getPage().getPreviousLink().getHref()
        .contains("/de/questions/search?query=SearchUnitTest_Survey_ID&page=0&size=3"), is(true));
    assertThat(resource.getPage().getNextLink().getHref()
        .contains("/de/questions/search?query=SearchUnitTest_Survey_ID&page=2&size=3"), is(true));
  }

}
