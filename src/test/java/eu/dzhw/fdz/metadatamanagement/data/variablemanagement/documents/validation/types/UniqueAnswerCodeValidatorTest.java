/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.AnswerOption;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.VariableDocumentValidator;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class UniqueAnswerCodeValidatorTest extends AbstractWebTest {

  @Autowired
  private VariableDocumentValidator variableDocumentValidator;

  @Test
  public void testValidAnswerCode() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setQuestion("DefaultQuestion?");

    AnswerOption answerOption1 = new AnswerOption();
    answerOption1.setCode(1);
    answerOption1.setLabel("Label 1");
    AnswerOption answerOption2 = new AnswerOption();
    answerOption2.setCode(2);
    answerOption2.setLabel("Label 2");
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(answerOption1);
    answerOptions.add(answerOption2);

    variableDocument.setAnswerOptions(answerOptions);

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testInvalidAnswerCode() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setQuestion("DefaultQuestion?");

    AnswerOption answerOption1 = new AnswerOption();
    answerOption1.setCode(1);
    answerOption1.setLabel("Label 1");
    AnswerOption answerOption2 = new AnswerOption();
    answerOption2.setCode(1);
    answerOption2.setLabel("Label 2");
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(answerOption1);
    answerOptions.add(answerOption2);

    variableDocument.setAnswerOptions(answerOptions);

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.ANSWER_OPTIONS_FIELD).getCode(),
        is("UniqueAnswerCode"));
  }
}
