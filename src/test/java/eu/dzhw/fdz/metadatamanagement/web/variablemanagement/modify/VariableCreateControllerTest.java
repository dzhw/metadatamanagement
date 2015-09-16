package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.ValidationResultDto;

/**
 * 
 * @author Daniel Katzberg
 * @author Amine Limouri
 *
 */
public class VariableCreateControllerTest extends AbstractWebTest {

  @Autowired
  private VariableService variableService;

  @Before
  public void setLocale() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
  }

  @Test
  public void testGetForm() throws Exception {
    // Check the Requestpath of the VariableModifyControllerPath
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/create"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??"))))
        .andExpect(model().attributeHasFieldErrors("variableDocument")).andReturn();
    boolean validHtml = this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "VariableCreateControllerTest.testGetForm");

    assertThat(validHtml, is(true));
  }

  @Test
  public void testPostRemoveAnswerOptionMethod() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(
            post("/de/variables/create").param(VariableDocument.ID_FIELD.getAbsolutePath(), "ID007")
                .param(VariableDocument.NAME_FIELD.getAbsolutePath(), "Ein Name")
                .param(VariableDocument.QUESTION_FIELD.getAbsolutePath(), "Eine Frage?")
                .param("answerOptions[0].code", "11111")
                .param("answerOptions[0].label", "11111Label").param("removeAnswerOption", "0"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null)).andReturn();

    boolean validHtml = this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "VariableCreateControllerTest.testPostRemoveAnswerOptionMethod");
    assertThat(validHtml, is(true));

    // Delete
    this.variableService.delete("ID007");
  }

  @Test
  public void testPostAddAnswerOptionMethod() throws Exception {
    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(
            post("/de/variables/create").param(VariableDocument.ID_FIELD.getAbsolutePath(), "ID007")
                .param(VariableDocument.NAME_FIELD.getAbsolutePath(), "Ein Name")
                .param(VariableDocument.QUESTION_FIELD.getAbsolutePath(), "Eine Frage?")
                .param(VariableDocument.NESTED_ANSWER_OPTIONS_CODE_FIELD.getAbsolutePath(), "11111")
                .param(VariableDocument.NESTED_ANSWER_OPTIONS_LABEL_FIELD.getAbsolutePath(),
                    "11111Label")
                .param("addAnswerOption", ""))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null)).andReturn();

    boolean validHtml = this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "VariableCreateControllerTest.testPostAddAnswerOptionMethod");
    assertThat(validHtml, is(true));

    // Delete
    this.variableService.delete("ID007");
  }

  @Test
  public void testPostAddFirstAnswerOptionMethod() throws Exception {
    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(
            post("/de/variables/create").param(VariableDocument.ID_FIELD.getAbsolutePath(), "ID007")
                .param(VariableDocument.NAME_FIELD.getAbsolutePath(), "Ein Name")
                .param(VariableDocument.QUESTION_FIELD.getAbsolutePath(), "Eine Frage?")
                .param("addAnswerOption", ""))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null)).andReturn();

    boolean validHtml = this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "VariableCreateControllerTest.testPostAddFirstAnswerOptionMethod");
    assertThat(validHtml, is(true));

    // Delete
    this.variableService.delete("ID007");
  }

  @Test
  public void testPostInvalidVariableDocument() throws Exception { // Arrange // EmtyQuestion Field
                                                                   // generates a error
    MvcResult mvcResult = this.mockMvc
        .perform(
            post("/de/variables/create").param(VariableDocument.ID_FIELD.getAbsolutePath(), "ID007")
                .param(VariableDocument.NAME_FIELD.getAbsolutePath(), "Ein Name"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null)).andReturn();

    boolean validHtml = this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "VariableCreateControllerTest.testPostInvalidVariableDocument");
    assertThat(validHtml, is(true));

    // Delete
    this.variableService.delete("ID007");
  }

  @Test
  public void testPostValidVariableDocument() throws Exception {
    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(
            post("/de/variables/create").param(VariableDocument.ID_FIELD.getAbsolutePath(), "ID007")
                .param(VariableDocument.NAME_FIELD.getAbsolutePath(), "Ein Name")
                .param(VariableDocument.LABEL_FIELD.getAbsolutePath(), "Ein Label")
                .param(VariableDocument.QUESTION_FIELD.getAbsolutePath(), "Eine Frage?")
                .param(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD.getAbsolutePath(),
                    "VariableSurveyID001")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD.getAbsolutePath(),
            "VariableSurveyTitel001")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD.getAbsolutePath(),
            "VariableSurveyAlias001")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE.getAbsolutePath(),
            LocalDate.now().toString())
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE.getAbsolutePath(),
            LocalDate.now().plusDays(2).toString()))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    mvcResult =
        this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/de/variables/ID007")).andReturn();

    // No W3C Check because of the json content type

    // Delete
    this.variableService.delete("ID007");
  }

  @Test
  public void testPostValidateInvalidVariableDocument() throws Exception {
    // Arrange
    // EmtyQuestion Field
    // generates a error
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/de/variables/create/validate")
                    .param(VariableDocument.ID_FIELD.getAbsolutePath(),
                        "testPostValidateInvalidID008")
                    .param(VariableDocument.LABEL_FIELD.getAbsolutePath(), "Ein Label 5")
                    .param(VariableDocument.NAME_FIELD.getAbsolutePath(), "Ein Name 9")
                    .param(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD.getAbsolutePath(),
                        "VariableSurveyID082")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD.getAbsolutePath(),
            "VariableSurveyTitel012")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD.getAbsolutePath(),
            "VariableSurveyAlias054")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE.getAbsolutePath(),
            LocalDate.now().toString())
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE.getAbsolutePath(),
            LocalDate.now().plusDays(2).toString())).andExpect(status().isOk())
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ValidationResultDto.class))).andReturn();

    ValidationResultDto validationResultDto = (ValidationResultDto) mvcResult.getAsyncResult();
    int errorKeySize = validationResultDto.getErrorMessageMap().keySet().size();
    String key = validationResultDto.getErrorMessageMap().keySet().iterator().next();
    List<String> errors = validationResultDto.getErrorMessageMap().get(key);
    int errorsSize = errors.size();
    String error1 = errors.get(0);

    // Act and Assert
    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null)).andReturn();

    // No W3C Check because of the json content type

    assertEquals(1, errorKeySize);
    assertEquals(1, errorsSize);
    assertThat(error1, is("Bitte geben Sie eine Frage an."));

    // Delete
    this.variableService.delete("testPostValidateInvalidID008");
  }

  @Test
  public void testPostValidateValidVariableDocument() throws Exception {
    LocaleContextHolder.setLocale(Locale.GERMAN);

    // Arrange
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/de/variables/create/validate")
                    .param(VariableDocument.ID_FIELD.getAbsolutePath(),
                        "testPostValidateValidID007")
                    .param(VariableDocument.QUESTION_FIELD.getAbsolutePath(), "Question.")
                    .param(VariableDocument.LABEL_FIELD.getAbsolutePath(), "Ein Label")
                    .param(VariableDocument.NAME_FIELD.getAbsolutePath(), "Ein Name")
                    .param(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD.getAbsolutePath(),
                        "VariableSurveyID001")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD.getAbsolutePath(),
            "VariableSurveyTitel001")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD.getAbsolutePath(),
            "VariableSurveyAlias001")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE.getAbsolutePath(),
            LocalDate.now().toString())
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE.getAbsolutePath(),
            LocalDate.now().plusDays(2).toString())).andExpect(status().isOk())
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ValidationResultDto.class))).andReturn();

    ValidationResultDto validationResultDto = (ValidationResultDto) mvcResult.getAsyncResult();
    int errorKeySize = validationResultDto.getErrorMessageMap().keySet().size();

    // Act
    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null)).andReturn();

    // No W3C Check because of the json content type

    // Assert
    assertEquals(0, errorKeySize);

    // Delete
    this.variableService.delete("testPostValidateValidID007");
  }

  @Test
  public void testValidSurveyPeriod() throws Exception {
    // Arrange
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/de/variables/create")
                    .param(VariableDocument.ID_FIELD.getAbsolutePath(),
                        "testValidSurveyPeriodID007")
                    .param(VariableDocument.NAME_FIELD.getAbsolutePath(), "Ein Name")
                    .param(VariableDocument.QUESTION_FIELD.getAbsolutePath(), "Eine Frage?")
                    .param(VariableDocument.LABEL_FIELD.getAbsolutePath(), "Ein Label")
                    .param(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD.getAbsolutePath(),
                        "ID001")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD.getAbsolutePath(), "ID001")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD.getAbsolutePath(),
            "ID007")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE.getAbsolutePath(),
            "2013-02-01")
        .param(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE.getAbsolutePath(),
            "2013-02-02")).andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    mvcResult =
        this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/de/variables/testValidSurveyPeriodID007")).andReturn();

    // No W3C Check because of the json content type

    // Delete
    this.variableService.delete("testValidSurveyPeriodID007");
  }
}
