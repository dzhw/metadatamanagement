/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class UniqueIdValidatorTest extends AbstractWebTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(UniqueIdValidatorTest.class);

  @Autowired
  private VariableRepository repository;

  @Autowired
  private Validator validator;
  
  @Test
  public void testValidUniqueId() {
    // Arrange
    VariableDocument variableDocument1 = new VariableDocument();
    variableDocument1.setId("ThisIDisOkay");
    variableDocument1.setName("ThisNameIsOkay.");
    variableDocument1.setQuestion("DefaultQuestion?");
    this.repository.save(variableDocument1);

    VariableDocument variableDocument2 = new VariableDocument();
    variableDocument2.setId("ThisIDisStillOkay");
    variableDocument2.setName("ThisNameIsOkay.");
    variableDocument2.setQuestion("DefaultQuestion?");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableViolations =
        this.validator.validate(variableDocument2);

    // Assert
    assertEquals(0, variableViolations.size());
    
    //Delete
    this.repository.delete(variableDocument1.getId());
    
  }

  @Test
  public void testInvalidUniqueId() {
    // Arrange
    VariableDocument variableDocument1 = new VariableDocument();
    variableDocument1.setId("ThisIDisOkay");
    variableDocument1.setName("ThisNameIsOkay.");
    variableDocument1.setQuestion("DefaultQuestion?");
    this.repository.save(variableDocument1);

    VariableDocument variableDocument2 = new VariableDocument();
    variableDocument2.setId("ThisIDisOkay");
    variableDocument2.setName("ThisNameIsOkay.");
    variableDocument2.setQuestion("DefaultQuestion?");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableViolations =
        this.validator.validate(variableDocument2);

    // Assert
    assertEquals(1, variableViolations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableViolations) {

      LOGGER.debug("testInvalidDataField() " + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals(
          "{eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.uniqueid.message}",
          variableVialation.getMessageTemplate());
    }
    
    //Delete
    this.repository.delete(variableDocument1.getId());
  }
}
