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
  
  //TODO
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
    
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId("ThisIDisOkay2");
    variableDocument.setName("ThisNameIsOkay2.");

    VariableSurvey variableSurvey = new VariableSurvey();
    variableSurvey.setSurveyId("SurveyIdIsOkay.");
    variableSurvey.setTitle("TitleIsOkay.");
    variableSurvey.setVariableAlias(variableDocument.getName());
    
    variableDocument.setVariableSurvey(variableSurvey);
    
    
    // Act
    Set<ConstraintViolation<VariableDocument>> variableViolations =
        this.validator.validate(variableDocument);

    // Assert
    assertEquals(0, variableViolations.size());
    
    //Delete
    this.repository.delete(variableDocument.getId());
  }
}
