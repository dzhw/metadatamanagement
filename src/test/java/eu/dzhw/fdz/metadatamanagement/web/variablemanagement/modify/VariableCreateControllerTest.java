package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * 
 * @author Daniel Katzberg
 * @author Amine Limouri
 *
 */
public class VariableCreateControllerTest extends AbstractWebTest {

  @Autowired
  private VariableService variableService;
  
  @Test
  public void testGetForm() throws Exception {
    // Check the Requestpath of the VariableModifyControllerPath
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/create"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??"))))
        .andExpect(model().attributeHasFieldErrors("variableDocument"));

  }

  @Test
  public void testAddSurvey() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/create").param(VariableDocument.ID_FIELD, "ID007")
            .param(VariableDocument.NAME_FIELD, "Ein Name")
            .param(VariableDocument.QUESTION_FIELD, "Eine Frage?").param("addSurvey", ""))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null));

  }

  @Test
  public void testAddSurveyWithNotNullSurvey() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/create").param(VariableDocument.ID_FIELD, "ID007")
            .param(VariableDocument.NAME_FIELD, "Ein Name")
            .param(VariableDocument.QUESTION_FIELD, "Eine Frage?")
            .param(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD, "SurveyID")
            .param("addSurvey", ""))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request()
            .asyncResult(instanceOf(ModelAndView.class)))
        .andReturn();

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null));

  }

  @Test
  public void testRemoveSurvey() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/create").param(VariableDocument.ID_FIELD, "ID007")
            .param(VariableDocument.NAME_FIELD, "Ein Name")
            .param(VariableDocument.QUESTION_FIELD, "Eine Frage?").param("removeSurvey", ""))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null));

  }

  @Test
  public void testPostRemoveAnswerOptionMethod() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/create").param(VariableDocument.ID_FIELD, "ID007")
            .param(VariableDocument.NAME_FIELD, "Ein Name")
            .param(VariableDocument.QUESTION_FIELD, "Eine Frage?")
            .param("answerOptions[0].code", "11111").param("answerOptions[0].label", "11111Label")
            .param("removeAnswerOption", "0"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null));
  }

  @Test
  public void testPostAddAnswerOptionMethod() throws Exception {
    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/create").param(VariableDocument.ID_FIELD, "ID007")
            .param(VariableDocument.NAME_FIELD, "Ein Name")
            .param(VariableDocument.QUESTION_FIELD, "Eine Frage?")
            .param(VariableDocument.NESTED_ANSWER_OPTIONS_CODE_FIELD, "11111")
            .param(VariableDocument.NESTED_ANSWER_OPTIONS_LABEL_FIELD, "11111Label")
            .param("addAnswerOption", ""))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null));
  }

  @Test
  public void testPostAddFirstAnswerOptionMethod() throws Exception {
    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/create").param(VariableDocument.ID_FIELD, "ID007")
            .param(VariableDocument.NAME_FIELD, "Ein Name")
            .param(VariableDocument.QUESTION_FIELD, "Eine Frage?").param("addAnswerOption", ""))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null));
  }

  @Test
  public void testPostInvalidVariableDocument() throws Exception { // Arrange // EmtyQuestion Field
                                                                   // generates a error
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/create").param(VariableDocument.ID_FIELD, "ID007")
            .param(VariableDocument.NAME_FIELD, "Ein Name"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null));
  }

  @Test
  public void testPostValidVariableDocument() throws Exception {
    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/create").param(VariableDocument.ID_FIELD, "ID007")
            .param(VariableDocument.NAME_FIELD, "Ein Name")
            .param(VariableDocument.QUESTION_FIELD, "Eine Frage?"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/de/variables/ID007"));

    // Delete
    this.variableService.delete("ID007");
  }
}
