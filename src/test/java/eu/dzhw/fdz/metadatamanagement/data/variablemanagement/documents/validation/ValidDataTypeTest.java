/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class ValidDataTypeTest extends AbstractWebTest {
  
  @Autowired
  private VariableDocumentValidator variableDocumentValidator;
  
  @Test
  public void testValidDataField() {
    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setDataType("string");
    variableDocument.setQuestion("DefaultQuestion?");

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testInvalidDataField() {
    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setDataType("sTrinGIsNotOkay");
    variableDocument.setQuestion("DefaultQuestion?");

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.DATA_TYPE_FIELD).getCode(),
        is(ValidDataType.class.getSimpleName()));
  }
  
  @Test
  public void testNotNullScaleLevelAtNumericDataType() {
    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setDataType("numeric");
    variableDocument.setQuestion("DefaultQuestion?");

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    variableDocumentValidator.validate(variableDocument, errors);

    // Assert
    assertThat(errors.getFieldError(VariableDocument.SCALE_LEVEL_FIELD).getCode(),
        is(VariableDocumentValidator.MANDATORY_SCALE_LEVEL_MESSAGE_CODE));

  }

}
