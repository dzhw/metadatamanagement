/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class ElasticSearchVariableDocumentMapping extends AbstractWebTest {

  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;

  @SuppressWarnings("rawtypes")
  @Test
  public void testAllLanguageIndices() {
    // Arrange

    // Act
    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      Map mapping =
          this.elasticsearchTemplate.getMapping("metadata_" + locale.getLanguage(), "variables");

      // Assert
      assertThat(mapping, is(notNullValue()));
      assertThat(mapping.size(), greaterThan(0));
    }
  }


  @SuppressWarnings("rawtypes")
  @Test
  public void testAllFieldsAreInMapping() {
    // Arrange
    Map<String, List<String>> allFields = this.getAListWithAllFields();
    List<String> allFieldsOfALayer = allFields.get(VariableDocument.class.getSimpleName());

    // Act
    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      Map mapping =
          this.elasticsearchTemplate.getMapping("metadata_" + locale.getLanguage(), "variables");      
      
      //Assert
      this.testAssertLayer(allFields, allFieldsOfALayer, mapping);      
    }
  }
  
  //Assert
  @SuppressWarnings("rawtypes")
  private void testAssertLayer(Map<String, List<String>> allFields, List<String> allFieldsOfALayer, Map mapping) {
    
    //Assert all properties in with a any nested depth, if there are in the allFields Map
    LinkedHashMap propertiesMap = (LinkedHashMap) mapping.get("properties");    
    for (String fieldName : allFieldsOfALayer) {
      if( allFields.containsKey(fieldName) ) {        
        Map mappingNested = (LinkedHashMap) propertiesMap.get(fieldName);
        this.testAssertLayer(allFields, allFields.get(fieldName), mappingNested);
      }
      
      assertThat(propertiesMap.get(fieldName), is(notNullValue()));
    }
  }

  /**
   * This method has to be declared in every document. It defines the fields for the nested 
   * @return A map, where the key is the relative part of fields and nested fields.
   */
  private Map<String, List<String>> getAListWithAllFields() {
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
    allFields.put(VariableDocument.NESTED_VARIABLE_SURVEY_PERIOD_FIELD.getRelativePath(), allFieldsOfALayer);

    return allFields;
  }

  /**
   * This method works with reflection and get all private (!) fields from a class.
   * @param clazz the class of ja document or a nested object from the document
   * @return a list with all field names.
   */
  @SuppressWarnings("rawtypes")
  private List<String> getAllFieldsFromClass(Class clazz) {

    List<String> allFields = new ArrayList<>();

    // check all private fields of the class
    for (Field field : clazz.getDeclaredFields()) {
      if (Modifier.isPrivate(field.getModifiers())) {
        allFields.add(field.getName());
      }
    }

    return allFields;
  }
}
