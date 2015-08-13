package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.AnswerOption;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;


public class VariableModifyControllerTest extends AbstractWebTest {

  @Test
  public void testgetForm() throws Exception {
    MvcResult mvcResult =
        this.mockMvc.perform(get("/de/variables/create")).andExpect(status().isOk())
            .andExpect(request().asyncStarted())
            .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        // .andExpect(content().string(not(containsString("??"))))
        .andExpect(model().attributeHasFieldErrors("variableDocument"));

  }

  @Test
  public void testPost() throws Exception {

    /*
     * MvcResult mvcResult = this.mockMvc.perform(get("/")).andExpect(status().isOk())
     * .andExpect(request().asyncStarted())
     * .andExpect(request().asyncResult(instanceOf(String.class))).andReturn();
     * 
     * this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
     * .andExpect(model().attributeExists("person"))
     * .andExpect(content().string((not(containsString("</html>")))))
     * .andExpect(content().string((not(containsString("<body>")))))
     * .andExpect(content().string((containsString("has-error"))))
     * .andExpect(content().string(not(containsString("#{"))))
     * .andExpect(content().string(not(containsString("${"))));
     */
  }

  @Test
  public void testPostInvalidVariableDocument() throws Exception {

  }

  @Test
  public void testPostValidVariableDocument() throws Exception {

    VariableDocument document = new VariableDocument();
    VariableSurvey survey = new VariableSurvey();
    List<AnswerOption> answerOpt = new ArrayList<>();
    AnswerOption answer = new AnswerOption();
    DateRange range = new DateRange();
    LocalDate today = LocalDate.now();
    LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);

    range.setStartDate(today);
    range.setEndDate(tomorrow);

    // answer.setCode("Code");
    answer.setCode(2);
    answer.setLabel("Label");
    answerOpt.add(answer);

    survey.setSurveyId("SurveyID");
    survey.setTitle("SurveyTitle");
    survey.setVariableAlias("VariableAlias");
    survey.setSurveyPeriod(range);

    // survey.getSurveyPeriod().setStartDate(startDate);

    document.setId("123");
    document.setLabel("Label");
    document.setName("name");
    document.setAnswerOptions(answerOpt);
    document.setScaleLevel("NOMINAL");
    document.setVariableSurvey(survey);



  }
}
