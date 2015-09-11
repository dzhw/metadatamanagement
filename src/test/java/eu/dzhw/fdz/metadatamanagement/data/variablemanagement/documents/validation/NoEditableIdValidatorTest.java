/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import javax.validation.ConstraintValidatorContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableDocumentRepository;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class NoEditableIdValidatorTest extends AbstractWebTest {

  private NoEditableIdValidator noEditableIdValidator;
  
  @Autowired 
  private VariableDocumentRepository variableRepository;
  
  @Before
  public void beforeTest(){
    this.noEditableIdValidator = new NoEditableIdValidator(this.variableRepository);
    
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("knownId").withName("ThisNameIsOkay.")
            .withVariableSurvey(variableSurvey).withQuestion("DefaultQuestion?")
            .withLabel("LabelIsOkay").build();
    this.variableRepository.save(variableDocument);
  }
  
  @After
  public void afterTest(){
    this.variableRepository.delete("knownId");;
  }

  @Test
  public void testNullObject() {
    // Assert
    ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
    
    // Act
    boolean nullValue = this.noEditableIdValidator.isValid(null, context);

    // Assert
    assertEquals(false, nullValue);
  }
  
  
  @Test
  public void testUnknownId() {
    // Assert
    ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
    
    // Act
    boolean unknownId = this.noEditableIdValidator.isValid("UnknownId", context);

    // Assert
    assertEquals(false, unknownId);
  }
  
  
  @Test
  public void testKnownId() {
    // Assert
    ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
    
    // Act
    boolean knownId = this.noEditableIdValidator.isValid("knownId", context);

    // Assert
    assertEquals(true, knownId);
  }

}
