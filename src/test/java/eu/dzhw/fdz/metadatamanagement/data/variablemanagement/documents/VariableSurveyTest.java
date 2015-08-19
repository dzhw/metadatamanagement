/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableSurveyTest {

  @Test
  public void testHashCode() {

    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder().withSurveyId("Survey01").build();

    // Act

    // Assert
    assertEquals(-2079109466, variableSurvey.hashCode());
  }

  @Test
  public void testEquals() {

    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder().build();
    VariableSurvey variableSurvey2 = new VariableSurveyBuilder().build();

    DateRange dateRange = new DateRange();
    dateRange.setStartDate(LocalDate.now().plusDays(1));
    DateRange dateRange2 = new DateRange();
    dateRange2.setStartDate(LocalDate.now().plusDays(2));

    // Act
    boolean checkNullObject = variableSurvey.equals(null);
    boolean checkDifferentClass = variableSurvey.equals(new Object());
    variableSurvey2.setSurveyId("ID2");
    boolean checkSurveyOther = variableSurvey.equals(variableSurvey2);
    variableSurvey.setSurveyId("ID1");
    boolean checkSurveyBoth = variableSurvey.equals(variableSurvey2);
    variableSurvey.setSurveyId("ID2");
    boolean checkSurveyBothSame = variableSurvey.equals(variableSurvey2);
    variableSurvey.setSurveyId(null);
    variableSurvey2.setSurveyId(null);
    variableSurvey2.setSurveyPeriod(dateRange2);
    boolean checkSurveyPeriodOther = variableSurvey.equals(variableSurvey2);
    variableSurvey.setSurveyPeriod(dateRange);
    boolean checkSurveyPeriodBoth = variableSurvey.equals(variableSurvey2);
    variableSurvey.setSurveyPeriod(dateRange2);
    boolean checkSurveyPeriodBothSame = variableSurvey.equals(variableSurvey2);
    variableSurvey.setSurveyPeriod(null);
    variableSurvey2.setSurveyPeriod(null);
    variableSurvey2.setTitle("Title 2");
    boolean checkTitleOther = variableSurvey.equals(variableSurvey2);
    variableSurvey.setTitle("Title 1");
    boolean checkTitleBoth = variableSurvey.equals(variableSurvey2);
    variableSurvey.setTitle("Title 2");
    boolean checkTitleBothSame = variableSurvey.equals(variableSurvey2);
    variableSurvey.setTitle(null);
    variableSurvey2.setTitle(null);
    variableSurvey2.setVariableAlias("Alias 2");
    boolean checkVariableAliasOther = variableSurvey.equals(variableSurvey2);
    variableSurvey.setVariableAlias("Alias 1");
    boolean checkVariableAliasBoth = variableSurvey.equals(variableSurvey2);
    variableSurvey.setVariableAlias("Alias 2");
    boolean checkVariableAliasBothSame = variableSurvey.equals(variableSurvey2);

    // Assert
    assertEquals(false, checkNullObject);
    assertEquals(false, checkDifferentClass);
    assertEquals(false, checkSurveyOther);
    assertEquals(false, checkSurveyBoth);
    assertEquals(true, checkSurveyBothSame);
    assertEquals(false, checkSurveyPeriodOther);
    assertEquals(false, checkSurveyPeriodBoth);
    assertEquals(true, checkSurveyPeriodBothSame);
    assertEquals(false, checkTitleOther);
    assertEquals(false, checkTitleBoth);
    assertEquals(true, checkTitleBothSame);
    assertEquals(false, checkVariableAliasOther);
    assertEquals(false, checkVariableAliasBoth);
    assertEquals(true, checkVariableAliasBothSame);
  }

}
