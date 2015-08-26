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

import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.ValidationResultDto;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableEditControllerTest extends AbstractWebTest {

  @Autowired
  private VariableService variableService;

  @Test
  public void testGetForm() throws Exception {

    // Arrange
    LocaleContextHolder.setLocale(Locale.GERMAN);
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
    this.variableService.delete("");
  }

  @Test
  public void testPostValidateInvalidVariableDocument() throws Exception {
    LocaleContextHolder.setLocale(Locale.GERMAN);

    // Arrange
    // EmtyQuestion Field
    // generates a error
    VariableDocument variableDocument = new VariableDocumentBuilder()
        .withId("testPostInvalidateValidID007").withQuestion("Question").withName("Ein Name").build();

    this.variableService.save(variableDocument);
    
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/testPostInvalidateValidID007/edit/validate")
            .param(VariableDocument.ID_FIELD, "testPostInvalidateValidID007")
            .param(VariableDocument.NAME_FIELD, "Ein Name"))
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
    this.variableService.delete("testPostValidateValidID007");
  }

  @Test
  public void testPostValidateValidVariableDocument() throws Exception {
    LocaleContextHolder.setLocale(Locale.GERMAN);

    VariableDocument variableDocument = new VariableDocumentBuilder()
        .withId("testPostValidateValidID007").withQuestion("Question").withName("Ein Name").build();

    this.variableService.save(variableDocument);

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/testPostValidateValidID007/edit/validate")
            .param(VariableDocument.ID_FIELD, "testPostValidateValidID007")
            .param(VariableDocument.QUESTION_FIELD, "Question.")
            .param(VariableDocument.NAME_FIELD, "Ein Name"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ValidationResultDto.class))).andReturn();

    ValidationResultDto validationResultDto = (ValidationResultDto) mvcResult.getAsyncResult();
    int errorKeySize = validationResultDto.getErrorMessageMap().keySet().size();

    // Act and Assert
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().is2xxSuccessful())
        .andExpect(redirectedUrl(null));
    assertEquals(0, errorKeySize);

    // Delete
    this.variableService.delete("testPostValidateValidID007");
  }

}
