
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
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AggregationType;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.FilterType;

/**
 * @author Daniel Katzberg
 *
 */
public class SearchFormDtoTest {

  @Test
  public void testGetAllFiltersEmptyMap() {
    // Arrange
    VariableSearchFilter emptyFormDto = new VariableSearchFilter();

    // Act
    Map<Field, String> emptyMap = emptyFormDto.getAllFilterValues();

    // Assert
    assertThat(emptyMap.size(), is(0));
  }

  @Test
  public void testGetAllFiltersIncompleteDateRange() {
    // Arrange
    VariableSearchFilter formDto = new VariableSearchFilter();
    formDto.setQuery("Query");
    formDto.setScaleLevel("ScaleLevel");
    formDto.setSurveyTitle("SurveyTitle");
    formDto.setDateRange(new DateRangeBuilder().build());

    // Act
    Map<Field, String> filterMap = formDto.getAllFilterValues();

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
    VariableSearchFilter formDto = new VariableSearchFilter();
    formDto.setQuery("Query");
    formDto.setScaleLevel("ScaleLevel");
    formDto.setSurveyTitle("SurveyTitle");
    formDto.setDateRange(new DateRangeBuilder().withEndDate(LocalDate.now())
        .withStartDate(LocalDate.now().minusDays(2)).build());

    // Act
    Map<Field, String> filterMap = formDto.getAllFilterValues();

    // Assert
    assertThat(filterMap.size(), is(4));
    assertThat(filterMap.get(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE),
        is(LocalDate.now().minusDays(2).toString()));
    assertThat(filterMap.get(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE),
        is(LocalDate.now().toString()));
    assertThat(filterMap.get(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD),
        is("SurveyTitle"));
    assertThat(filterMap.get(VariableDocument.SCALE_LEVEL_FIELD), is("ScaleLevel"));
    assertThat(formDto.getQuery(), is("Query"));
  }

  @Test
  public void testGetFilterFields() {
    // Arrange
    VariableSearchFilter formDto = new VariableSearchFilter();

    // Act
    Map<Field, FilterType> filterFields = formDto.getAllFilterTypes();

    // Assert
    assertThat(filterFields.size(), is(4));
    assertThat(filterFields.get(VariableDocument.SCALE_LEVEL_FIELD), is(FilterType.TERM));
    assertThat(filterFields.get(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD),
        is(FilterType.TERM));
    assertThat(filterFields.get(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE),
        is(FilterType.RANGE_LTE));
    assertThat(filterFields.get(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE),
        is(FilterType.RANGE_GTE));
  }

  @Test
  public void testGetAggrogationFields() {
    // Arrange
    VariableSearchFilter formDto = new VariableSearchFilter();

    // Act
    Map<Field, AggregationType> aggrogationFields = formDto.getAllAggregationTypes();

    // Assert
    assertThat(aggrogationFields.size(), is(2));
    assertThat(aggrogationFields.get(VariableDocument.SCALE_LEVEL_FIELD), is(AggregationType.TERM));
    assertThat(aggrogationFields.get(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD),
        is(AggregationType.TERM));
  }

}
