/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionSurvey;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.service.questionmanagement.QuestionService;

/**
 * @author Daniel Katzberg
 * @author Amine limouri
 *
 */
public class QuestionDetailsControllerTest extends AbstractTest {

  @Autowired
  private QuestionService questionService;

  @Before
  public void before() {

    createQuestion(Locale.GERMAN, "GetByValidTest_ID01", "GetByValidTestName 01",
        "GetByValidTestQuestion 01", "GetByValidTestTitle 01", "GetByValidTest_Survey_ID");

    createQuestion(Locale.ENGLISH, "GetByValidTest_ID01", "GetByValidTestName 01",
        "GetByValidTestQuestion 01", "GetByValidTestTitle 01", "GetByValidTest_Survey_ID");
  }

  @After
  public void after() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    this.questionService.delete("GetByValidTest_ID01");

    LocaleContextHolder.setLocale(Locale.ENGLISH);
    this.questionService.delete("GetByValidTest_ID01");
  }

  @Test
  public void testGermanTemplate() throws Exception {
   
    MvcResult mvcResult = this.mockMvc.perform(get("/de/questions/GetByValidTest_ID01"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    QuestionDetailsResource resource =
        (QuestionDetailsResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??")))).andReturn();

    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "QuestionDetailsControllerTest.testGermanTemplate");

   assertThat(resource.getQuestionResource().getQuestionDocument().getName(), is("GetByValidTestName 01"));

  }

  @Test
  public void testEnglishTemplate() throws Exception {
    
    MvcResult mvcResult = this.mockMvc.perform(get("/en/questions/GetByValidTest_ID01"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    QuestionDetailsResource resource =
        (QuestionDetailsResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Language"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??")))).andReturn();

    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "QuestionDetailsControllerTest.testEnglishTemplate");

   assertThat(resource.getQuestionResource().getQuestionDocument().getName(), is("GetByValidTestName 01"));
    
  }

  private void createQuestion(Locale locale, String id, String name, String question, String title,
      String surveyId) {

    LocaleContextHolder.setLocale(locale);
    QuestionSurvey questionSurvey =
        new QuestionSurveyBuilder().withSurveyId(surveyId).withTitle(title).build();

    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId(id).withName(name)
        .withQuestion(question).withQuestionSurvey(questionSurvey).build();

    this.questionService.save(questionDocument);

  }

}
