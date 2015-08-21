/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.validate;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.ValidationResultDto;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableValidateControllerTest extends AbstractWebTest {

  @Test
  public void testvalidateWithMinOneError() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/validate").param(VariableDocument.ID_FIELD, "ID007")
            .param(VariableDocument.NAME_FIELD, "Ein Name").param("addSurvey", ""))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ValidationResultDto.class))).andReturn();

    // min 1 error: no question
    ValidationResultDto validationResultDto = (ValidationResultDto) mvcResult.getAsyncResult();
    boolean isEmpty = validationResultDto.getErrorMessageMap().isEmpty();
    int size = validationResultDto.getErrorMessageMap().size();

    // Act and Assert
    assertEquals(false, isEmpty);
    assertThat(size, greaterThanOrEqualTo(1));

  }

  @Test
  public void testvalidateWithMinTwoFieldError() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/validate").param(VariableDocument.NAME_FIELD, "Ein Name")
            .param("addSurvey", ""))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ValidationResultDto.class))).andReturn();

    // min 1 error by new survey field
    ValidationResultDto validationResultDto = (ValidationResultDto) mvcResult.getAsyncResult();
    boolean isEmpty = validationResultDto.getErrorMessageMap().isEmpty();
    int size = validationResultDto.getErrorMessageMap().size();

    // Act and Assert
    assertEquals(false, isEmpty);
    assertThat(size, greaterThanOrEqualTo(2));

  }

  @Test
  public void testvalidateWithNoError() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(post("/de/variables/validate").param(VariableDocument.ID_FIELD, "ID007")
            .param(VariableDocument.NAME_FIELD, "Ein Name")
            .param(VariableDocument.QUESTION_FIELD, "Eine Frage?").param("removeSurvey", ""))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ValidationResultDto.class))).andReturn();

    // min 1 error by new survey field
    ValidationResultDto validationResultDto = (ValidationResultDto) mvcResult.getAsyncResult();
    boolean isEmpty = validationResultDto.getErrorMessageMap().isEmpty();
    int size = validationResultDto.getErrorMessageMap().size();

    // Act and Assert
    assertEquals(true, isEmpty);
    assertThat(size, is(0));

  }

}
