/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.AbstractElasticSearchMappingTest;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class ElasticSearchVariableDocumentMappingTest extends AbstractElasticSearchMappingTest {

  @Test
  public void testIndices() {
    this.testAllLanguageIndices("variables");
  }

  @Test
  public void testAllFieldsAreInMapping2() {
    this.testAllFieldsAreInMapping("variables");
  }

  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.data.AbstractElasticSearchMappingTest#getAListWithAllFields()
   */
  @Override
  public Map<String, List<String>> getAListWithAllFields() {
    Map<String, List<String>> allFields = new HashMap<>();
    List<String> allFieldsOfALayer = new ArrayList<>();
    VariableDocument document = new VariableDocumentBuilder()
        .withVariableSurvey(
            new VariableSurveyBuilder().withSurveyPeriod(new DateRangeBuilder().build()).build())
        .build();

    // AbstractDocument
    allFieldsOfALayer.addAll(this.getAllFieldsFromClass(document.getClass().getSuperclass()));
    allFieldsOfALayer.remove("highlightedFields"); // Remove special highlighting fields

    // VariableDocument
    allFieldsOfALayer.addAll(this.getAllFieldsFromClass(document.getClass()));
    allFields.put(VariableDocument.class.getSimpleName(), allFieldsOfALayer);
    allFieldsOfALayer = new ArrayList<>();

    // VariableSurvey
    allFieldsOfALayer.addAll(this.getAllFieldsFromClass(document.getVariableSurvey().getClass()));
    allFields.put(VariableDocument.VARIABLE_SURVEY_FIELD.getRelativePath(), allFieldsOfALayer);
    allFieldsOfALayer = new ArrayList<>();

    // SurveyPeriod
    allFieldsOfALayer.addAll(
        this.getAllFieldsFromClass(document.getVariableSurvey().getSurveyPeriod().getClass()));
    allFields.put(VariableDocument.NESTED_VARIABLE_SURVEY_PERIOD_FIELD.getRelativePath(),
        allFieldsOfALayer);

    return allFields;
  }
}
