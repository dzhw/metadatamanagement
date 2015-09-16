/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.ValidationResultDto;
import eu.dzhw.fdz.metadatamanagement.web.common.exceptions.DocumentNotFoundException;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableEditControllerTest extends AbstractWebTest {

  @Autowired
  private VariableService variableService;

  @Before
  public void setLocale() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
  }

  @Test
  public void testGetForm() throws Exception {
    // Arrange
    String id = "testGetEditForm";
    VariableDocument variableDocument = new VariableDocumentBuilder().withId(id).build();
    this.variableService.save(variableDocument);

    // Check the Requestpath of the VariableModifyControllerPath
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/" + id + "/edit"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??"))))
        .andExpect(model().attributeHasFieldErrors("variableDocument"));

    // Delete
    this.variableService.delete(id);
  }

  @Test
  public void testGetInvalidForm() throws Exception {
    // Arrange
    String validId = "testGetEditForm";
    VariableDocument variableDocument = new VariableDocumentBuilder().withId(validId).build();
    this.variableService.save(variableDocument);

    String invalidId = "nowitsinvalid";

    // Check the Requestpath of the VariableModifyControllerPath
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/" + invalidId + "/edit"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(DocumentNotFoundException.class))).andReturn();

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isNotFound())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??"))));

    // Delete
    this.variableService.delete(validId);
  }

  @Test
  public void testPostValidateInvalidVariableDocument() throws Exception {
    // Arrange
    // EmtyQuestion Field
    // generates a error
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument = new VariableDocumentBuilder()
        .withId("testPostInvalidateValidID007").withQuestion("Question").withName("Ein Name")
        .withVariableSurvey(variableSurvey).build();

    this.variableService.save(variableDocument);

    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/testPostInvalidateValidID007/edit/validate")
            .param(VariableDocument.ID_FIELD.getPath(), "testPostInvalidateValidID007")
            .param(VariableDocument.LABEL_FIELD.getPath(), "Ein Label")
            .param(VariableDocument.NAME_FIELD.getPath(), "Ein Name")
            .param(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD.getLeafSubFieldPath(),
                "VariableSurveyID001")
            .param(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD.getLeafSubFieldPath(),
                "VariableSurveyTitel001")
            .param(VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD.getLeafSubFieldPath(),
                "VariableSurveyAlias001")
            .param(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE.getLeafSubFieldPath(),
                LocalDate.now().toString())
            .param(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE.getLeafSubFieldPath(),
                LocalDate.now().plusDays(2).toString()))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ValidationResultDto.class))).andReturn();

    ValidationResultDto validationResultDto = (ValidationResultDto) mvcResult.getAsyncResult();
    int errorKeySize = validationResultDto.getErrorMessageMap().keySet().size();
    String key = validationResultDto.getErrorMessageMap().keySet().iterator().next();
    List<String> errors = validationResultDto.getErrorMessageMap().get(key);
    int errorsSize = errors.size();
    String error1 = errors.get(0);

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null));
    assertEquals(1, errorKeySize);
    assertEquals(1, errorsSize);
    assertThat(error1, is("Bitte geben Sie eine Frage an."));

    // Delete
    this.variableService.delete(variableDocument.getId());
  }

  @Test
  public void testPostValidateValidVariableDocument() throws Exception {
    // Arrange
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("testPostValidateValidID007").withQuestion("Question")
            .withName("Ein Name").withVariableSurvey(variableSurvey).build();

    this.variableService.save(variableDocument);

    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/testPostValidateValidID007/edit/validate")
            .param(VariableDocument.ID_FIELD.getPath(), "testPostValidateValidID007")
            .param(VariableDocument.QUESTION_FIELD.getPath(), "Question.")
            .param(VariableDocument.LABEL_FIELD.getPath(), "Ein Label")
            .param(VariableDocument.NAME_FIELD.getPath(), "Ein Name")
            .param(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD.getLeafSubFieldPath(),
                "VariableSurveyID001")
            .param(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD.getLeafSubFieldPath(),
                "VariableSurveyTitel001")
            .param(VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD.getLeafSubFieldPath(),
                "VariableSurveyAlias001")
            .param(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE.getLeafSubFieldPath(),
                LocalDate.now().toString())
            .param(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE.getLeafSubFieldPath(),
                LocalDate.now().plusDays(2).toString()))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ValidationResultDto.class))).andReturn();

    ValidationResultDto validationResultDto = (ValidationResultDto) mvcResult.getAsyncResult();
    int errorKeySize = validationResultDto.getErrorMessageMap().keySet().size();

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null));
    assertEquals(0, errorKeySize);

    // Delete
    this.variableService.delete(variableDocument.getId());
  }

  @Test
  public void testGetWithInvalidId() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/hurz/edit"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(DocumentNotFoundException.class))).andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isNotFound())
        .andExpect(view().name("exceptionView"))
        .andExpect(model().attribute("navMessageLanguage", "nav.language.german"));
  }
}
