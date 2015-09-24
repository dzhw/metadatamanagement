/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * @author Daniel Katzberg
 *
 */
public abstract class AbstractElasticSearchMappingTest extends AbstractTest {

  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;

  private static final Logger LOG = LoggerFactory.getLogger(AbstractElasticSearchMappingTest.class);
  
  private final String[] ignoreFieldsArray = {"highlightedFields", "$jacocoData"};

  @SuppressWarnings("rawtypes")
  protected void testAllLanguageIndices(String type) {
    // Arrange

    // Act
    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      Map mapping = this.elasticsearchTemplate.getMapping("metadata_" + locale.getLanguage(), type);

      // Assert
      assertThat(mapping, is(notNullValue()));
      assertThat(mapping.size(), greaterThan(0));
    }
  }


  @SuppressWarnings("rawtypes")
  protected void testAllFieldsAreInMapping(String type) {
    // Arrange
    Map<String, List<String>> allFields = this.getAListWithAllFields();
    List<String> allFieldsOfALayer = allFields.get(VariableDocument.class.getSimpleName());

    // Act
    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      Map mapping = this.elasticsearchTemplate.getMapping("metadata_" + locale.getLanguage(), type);

      // Assert
      this.testAssertLayer(allFields, allFieldsOfALayer, mapping);
    }
  }

  /**
   * This method has to be declared in every document. It defines the fields for the nested
   * 
   * @return A map, where the key is the relative part of fields and nested fields.
   */
  public abstract Map<String, List<String>> getAListWithAllFields();

  /**
   * 
   * @param fieldName
   * @return
   */
  private boolean ignoreFields(String fieldName) {
    return Arrays.asList(this.ignoreFieldsArray).contains(fieldName);
  }

  // Assert
  @SuppressWarnings("rawtypes")
  protected void testAssertLayer(Map<String, List<String>> allFields,
      List<String> allFieldsOfALayer, Map mapping) {

    // Assert all properties in with a any nested depth, if there are in the allFields Map
    LinkedHashMap propertiesMap = (LinkedHashMap) mapping.get("properties");
    for (String fieldName : allFieldsOfALayer) {

      LOG.info("Check fieldname: " + fieldName);
      
      //ignore field
      if(this.ignoreFields(fieldName)) {
        LOG.info("Ignore fieldname: " + fieldName);
        continue;
      }

      if (allFields.containsKey(fieldName)) {
        Map mappingNested = (LinkedHashMap) propertiesMap.get(fieldName);
        this.testAssertLayer(allFields, allFields.get(fieldName), mappingNested);
      }

      assertThat(propertiesMap.get(fieldName), is(notNullValue()));
    }
  }

  /**
   * This method works with reflection and get all private (!) fields from a class.
   * 
   * @param clazz the class of a document or a nested object from the document
   * @return a list with all field names.
   */
  @SuppressWarnings("rawtypes")
  protected List<String> getAllFieldsFromClass(Class clazz) {

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
