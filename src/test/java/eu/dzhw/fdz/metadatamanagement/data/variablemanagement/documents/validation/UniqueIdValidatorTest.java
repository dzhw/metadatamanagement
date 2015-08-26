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
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.VariableDocumentCreateValidator;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class UniqueIdValidatorTest extends AbstractWebTest {

  @Autowired
  private VariableRepository repository;

  @Autowired
  private VariableDocumentCreateValidator variableDocumentCreateValidator;

  @Test
  public void testValidUniqueId() {

    // Arrange
    VariableDocument variableDocument1 =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withLabel("LabelIsOkay").withQuestion("DefaultQuestion?").build();
    this.repository.save(variableDocument1);

    VariableDocument variableDocument2 =
        new VariableDocumentBuilder().withId("ThisIDisStillOkay").withName("ThisNameIsOkay.")
            .withLabel("LabelIsOkay").withQuestion("DefaultQuestion?").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument2, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument2, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());

    // Delete
    this.repository.delete(variableDocument1.getId());
  }

  @Test
  public void testInvalidUniqueId() {
    // Arrange
    VariableDocument variableDocument1 =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withLabel("LabelIsOkay").withQuestion("DefaultQuestion?").build();
    this.repository.save(variableDocument1);

    VariableDocument variableDocument2 =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withLabel("LabelIsOkay").withQuestion("DefaultQuestion?").build();
    this.repository.save(variableDocument1);

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument2, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument2, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.ID_FIELD).getCode(),
        is(UniqueId.class.getSimpleName()));

    // Delete
    this.repository.delete(variableDocument1.getId());
  }
}
