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

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.VariableDocumentCreateValidator;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.VariableDocumentValidator;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class ValidDataTypeTest extends AbstractWebTest {

  @Autowired
  private VariableDocumentCreateValidator variableDocumentCreateValidator;

  @Autowired
  private DataTypesProvider dataTypeProvider;

  @Test
  public void testValidDataField() {
    // Assert
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withLabel("LabelIsOkay").withQuestion("DefaultQuestion?")
        .withDataType(dataTypeProvider.getStringValueByLocale()).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testInvalidDataField() {
    // Assert
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withDataType("sTrinGIsNotOkay").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.DATA_TYPE_FIELD).getCode(),
        is(ValidDataType.class.getSimpleName()));
  }

  @Test
  public void testNotNullScaleLevelAtNumericDataType() {
    // Assert
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withLabel("LabelIsOkay").withQuestion("DefaultQuestion?")
        .withDataType(dataTypeProvider.getNumericValueByLocale()).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertThat(errors.getFieldError(VariableDocument.SCALE_LEVEL_FIELD).getCode(),
        is(VariableDocumentValidator.MANDATORY_SCALE_LEVEL_MESSAGE_CODE));
  }
}
