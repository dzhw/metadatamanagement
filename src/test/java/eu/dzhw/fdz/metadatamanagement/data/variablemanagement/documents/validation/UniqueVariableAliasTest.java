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
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class UniqueVariableAliasTest extends AbstractWebTest{
  
  private static final Logger LOGGER = LoggerFactory.getLogger(UniqueVariableAliasTest.class);
  
  @Autowired
  private VariableRepository repository;

  @Autowired
  private Validator validator;
  
  @Test
  public void testInvalidVariableSecondDocumentSurvey() {

    //Arrange
    VariableDocument variableDocument1 = new VariableDocument();
    variableDocument1.setId("ThisIDisOkay");
    variableDocument1.setName("ThisNameIsOkay.");

    VariableSurvey variableSurvey1 = new VariableSurvey();
    variableSurvey1.setSurveyId("SurveyIdIsOkay.");
    variableSurvey1.setTitle("TitleIsOkay.");
    variableSurvey1.setVariableAlias(variableDocument1.getName());
    
    variableDocument1.setVariableSurvey(variableSurvey1);
    this.repository.save(variableDocument1);
    
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay2");
    variableDocument.setName("ThisNameIsOkay2.");

    VariableSurvey variableSurvey = new VariableSurvey();
    variableSurvey.setSurveyId("SurveyIdIsOkay.");
    variableSurvey.setTitle("TitleIsOkay.");
    //here is the error, double used alias
    variableSurvey.setVariableAlias(variableDocument1.getName());
    
    variableDocument.setVariableSurvey(variableSurvey);
    
    
    // Act
    Set<ConstraintViolation<VariableDocument>> variableViolations =
        this.validator.validate(variableDocument);

    // Assert
    assertEquals(1, variableViolations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableViolations) {

      LOGGER.debug("testInalidVariableSecondDocumentSurvey() " + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals(
          "{eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.uniquevariablealias.message}",
          variableVialation.getMessageTemplate());
    }
    
    //Delete
    this.repository.delete(variableDocument.getId());
  }
  
  
  @Test
  public void testValidVariableSecondDocumentSurvey() {

    //Arrange
    VariableDocument variableDocument1 = new VariableDocument();
    variableDocument1.setId("ThisIDisOkay");
    variableDocument1.setName("ThisNameIsOkay.");

    VariableSurvey variableSurvey1 = new VariableSurvey();
    variableSurvey1.setSurveyId("SurveyIdIsOkay.");
    variableSurvey1.setTitle("TitleIsOkay.");
    variableSurvey1.setVariableAlias(variableDocument1.getName());
    
    variableDocument1.setVariableSurvey(variableSurvey1);
    this.repository.save(variableDocument1);
    
    VariableDocument variableDocument2 = new VariableDocument();
    variableDocument2.setId("ThisIDisOkay2");
    variableDocument2.setName("ThisNameIsOkay2.");

    VariableSurvey variableSurvey2 = new VariableSurvey();
    variableSurvey2.setSurveyId("SurveyIdIsOkay.");
    variableSurvey2.setTitle("TitleIsOkay.");
    variableSurvey2.setVariableAlias(variableDocument2.getName());
    
    variableDocument2.setVariableSurvey(variableSurvey2);
    
    
    // Act
    Set<ConstraintViolation<VariableDocument>> variableViolations =
        this.validator.validate(variableDocument2);

    // Assert
    assertEquals(0, variableViolations.size());
    
    //Delete
    this.repository.delete(variableDocument1.getId());
  }
    
  @Test
  public void testUniqueAliasWithNullSurvey() {

    //Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay");
    variableDocument.setName("ThisNameIsOkay.");    
    variableDocument.setVariableSurvey(null);
    
    
    // Act
    Set<ConstraintViolation<VariableDocument>> variableViolations =
        this.validator.validate(variableDocument);

    // Assert
    assertEquals(0, variableViolations.size());
  }
}
