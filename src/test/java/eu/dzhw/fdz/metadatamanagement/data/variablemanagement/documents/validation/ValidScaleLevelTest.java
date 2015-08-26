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

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.VariableDocumentCreateValidator;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class ValidScaleLevelTest extends AbstractWebTest {

  @Autowired
  private VariableDocumentCreateValidator variableDocumentCreateValidator;

  @Autowired
  private ScaleLevelProvider scaleLevelProvider;

  @Test
  public void testValidScaleLevel() {

    // Assert
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withScaleLevel(this.scaleLevelProvider.getOrdinalByLocal()).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testInvalidScaleLevel() {
    // Assert
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withScaleLevel("oRdiNalIsNotOkay").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.SCALE_LEVEL_FIELD).getCode(),
        is(ValidScaleLevel.class.getSimpleName()));
  }

}
