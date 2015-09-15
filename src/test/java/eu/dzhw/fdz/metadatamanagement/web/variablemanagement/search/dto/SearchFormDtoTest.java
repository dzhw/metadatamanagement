
package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.Map;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.Field;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * @author Daniel Katzberg
 *
 */
public class SearchFormDtoTest {

  @Test
  public void testGetAllFiltersEmptyMap() {
    // Arrange
    VariableSearchFormDto emptyFormDto = new VariableSearchFormDto();

    // Act
    Map<Field, String> emptyMap = emptyFormDto.getAllFilters();

    // Assert
    assertThat(emptyMap.size(), is(0));
  }
  
  @Test
  public void testGetAllFiltersIncompleteDateRange() {
    // Arrange
    VariableSearchFormDto formDto = new VariableSearchFormDto();
    formDto.setQuery("Query");
    formDto.setScaleLevel("ScaleLevel");
    formDto.setSurveyTitle("SurveyTitle");
    formDto.setDateRange(new DateRangeBuilder().build());

    // Act
    Map<Field, String> filterMap = formDto.getAllFilters();

    // Assert
    assertThat(filterMap.size(), is(2));
    assertThat(filterMap.get(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE),
        is(nullValue()));
    assertThat(filterMap.get(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE),
        is(nullValue()));
    assertThat(filterMap.get(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD),
        is("SurveyTitle"));
    assertThat(filterMap.get(VariableDocument.SCALE_LEVEL_FIELD), is("ScaleLevel"));
    assertThat(formDto.getQuery(), is("Query"));
  }
  
  @Test
  public void testGetAllFilters() {
    // Arrange
    VariableSearchFormDto formDto = new VariableSearchFormDto();
    formDto.setQuery("Query");
    formDto.setScaleLevel("ScaleLevel");
    formDto.setSurveyTitle("SurveyTitle");
    formDto.setDateRange(new DateRangeBuilder().withEndDate(LocalDate.now())
        .withStartDate(LocalDate.now().minusDays(2)).build());

    // Act
    Map<Field, String> filterMap = formDto.getAllFilters();

    // Assert
    assertThat(filterMap.size(), is(4));
    assertThat(filterMap.get(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE),
        is(LocalDate.now().minusDays(2).toString()));
    assertThat(filterMap.get(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE),
        is(LocalDate.now().toString()));
    assertThat(filterMap.get(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD),
        is("SurveyTitle"));
    assertThat(filterMap.get(VariableDocument.SCALE_LEVEL_FIELD), is("ScaleLevel"));
    assertThat(formDto.getQuery(), is("Query"));
  }

  @Test
  public void testGetFilterFields() {
    // Arrange
    VariableSearchFormDto formDto = new VariableSearchFormDto();

    // Act
    Map<Field, Integer> filterFields = formDto.getFilterFields();

    // Assert
    assertThat(filterFields.size(), is(4));
    assertThat(filterFields.get(VariableDocument.SCALE_LEVEL_FIELD),
        is(VariableSearchFormDto.FILTER_TERM));
    assertThat(filterFields.get(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD),
        is(VariableSearchFormDto.FILTER_TERM));
    assertThat(filterFields.get(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE),
        is(VariableSearchFormDto.FILTER_RANGE_GTE));
    assertThat(filterFields.get(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE),
        is(VariableSearchFormDto.FILTER_RANGE_LTE));
  }
  
  @Test
  public void testGetAggrogationFields() {
    // Arrange
    VariableSearchFormDto formDto = new VariableSearchFormDto();

    // Act
    Map<Field, Integer> aggrogationFields = formDto.getAggregationFields();

    // Assert
    assertThat(aggrogationFields.size(), is(2));
    assertThat(aggrogationFields.get(VariableDocument.SCALE_LEVEL_FIELD),
        is(VariableSearchFormDto.AGGREGATION_TERM));
    assertThat(aggrogationFields.get(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD),
        is(VariableSearchFormDto.AGGREGATION_TERM));
  }
  
}
