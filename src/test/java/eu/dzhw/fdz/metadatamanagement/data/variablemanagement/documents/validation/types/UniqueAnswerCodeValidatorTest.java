/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.types;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.AnswerOption;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class UniqueAnswerCodeValidatorTest extends AbstractWebTest{
  
  private static final Logger LOGGER = LoggerFactory.getLogger(UniqueAnswerCodeValidatorTest.class);
  
  @Autowired
  private Validator validator;
  
  @Test
  public void testValidAnswerCode() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    
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
    Set<ConstraintViolation<VariableDocument>> variableViolations =
        this.validator.validate(variableDocument);

    // Assert
    assertEquals(0, variableViolations.size());
  }
  
  @Test
  public void testInvalidAnswerCode() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");
    
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
    Set<ConstraintViolation<VariableDocument>> variableViolations =
        this.validator.validate(variableDocument);

    // Assert
    assertEquals(1, variableViolations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableViolations) {

      LOGGER.debug("testInvalidDataField() " + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals(
          "{eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.uniqueanswercode.message}",
          variableVialation.getMessageTemplate());
    }
  }

}
