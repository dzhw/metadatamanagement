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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * @author Daniel Katzberg
 *
 */
public class ElasticSearchMappingTest extends AbstractTest {

  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;

  private final String[] ignoreFieldsArray = {"highlightedFields", "$jacocoData"};

  @Test
  @SuppressWarnings("rawtypes")
  public void testTypeExistsInAllIndices() throws ClassNotFoundException {
    // Arrange
    List<String> types = this.getTypes();

    // Act
    for (String type : types) {
      for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
        Map mapping =
            this.elasticsearchTemplate.getMapping("metadata_" + locale.getLanguage(), type);

        // Assert
        assertThat(mapping, is(notNullValue()));
        assertThat(mapping.size(), greaterThan(0));
      }
    }
  }

  @Test
  @SuppressWarnings("rawtypes")
  public void testAllFieldsAreInMapping() throws ClassNotFoundException {
    // Arrange
    List<String> types = this.getTypes();
    Map<String, Map<String, Class>> allFields = this.getAListWithAllFields();
    Map<String, Class> allFieldsOfALayerAndTypes =
        allFields.get(VariableDocument.class.getSimpleName());

    // Act
    for (String type : types) {
      for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
        Map mapping =
            this.elasticsearchTemplate.getMapping("metadata_" + locale.getLanguage(), type);

        // Assert
        this.testAssertLayer(allFields, allFieldsOfALayerAndTypes, mapping);
      }
    }
  }
  
  /**
   * BELOW THIS LINE: ONLY HELPER PRIVATE METHODS!
   */

  /**
   * This method has to be declared in every document. It defines the fields for the nested
   * 
   * @return A map, where the key is the relative part of fields and nested fields.
   * @throws ClassNotFoundException
   */
  @SuppressWarnings("rawtypes")
  private Map<String, Map<String, Class>> getAListWithAllFields() throws ClassNotFoundException {

    // get Bean definitions
    Set<BeanDefinition> components = this.getBeanDefinitions();
    Map<String, Map<String, Class>> allFields = new HashMap<>();

    // look for different document classes
    for (BeanDefinition component : components) {
      Class documentClass = Class.forName(component.getBeanClassName());

      // AbstractDocument
      Map<String, Class> allFieldsOfVariableDocument = new HashMap<>();
      allFieldsOfVariableDocument.putAll(this.getAllFieldsFromClass(documentClass.getSuperclass()));

      // Document
      allFieldsOfVariableDocument.putAll(this.getAllFieldsFromClass(documentClass));
      allFields.put(VariableDocument.class.getSimpleName(), allFieldsOfVariableDocument);
    }

    return allFields;
  }

  /**
   * @return returns a list of all type names which should be in the indices of elasticsearch. 
   * @throws ClassNotFoundException
   */
  @SuppressWarnings("rawtypes")
  private List<String> getTypes() throws ClassNotFoundException {

    Set<BeanDefinition> components = this.getBeanDefinitions();
    List<String> types = new ArrayList<>();

    // look for different document classes
    for (BeanDefinition component : components) {
      Class documentClass = Class.forName(component.getBeanClassName());
      types.add(AnnotationUtils.getAnnotation(documentClass, Document.class).type());
    }

    return types;
  }

  /**
   * @return Returns the BeanDefinitions (get the sub classes by the AbstractDocument.class) and
   *         within the package path eu/dzhw/fdz/metadatamanagement/data
   */
  private Set<BeanDefinition> getBeanDefinitions() {
    // prepare scanning
    ClassPathScanningCandidateComponentProvider provider =
        new ClassPathScanningCandidateComponentProvider(false);
    provider.addIncludeFilter(new AssignableTypeFilter(AbstractDocument.class));
    return provider.findCandidateComponents("eu/dzhw/fdz/metadatamanagement/data");
  }

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
  private void testAssertLayer(Map<String, Map<String, Class>> allFields,
      Map<String, Class> allFieldsOfALayerAndTypes, Map mapping) throws ClassNotFoundException {

    // Assert all properties in with a any nested depth, if there are in the allFields Map
    LinkedHashMap propertiesMap = (LinkedHashMap) mapping.get("properties");
    Set<String> allFieldsOfALayer = allFieldsOfALayerAndTypes.keySet();
    for (String fieldName : allFieldsOfALayer) {

      // ignore field
      if (this.ignoreFields(fieldName)) {
        continue;
      }

      // get type from elasticsearch
      Map mappingNested = (LinkedHashMap) propertiesMap.get(fieldName);
      String type = (String) mappingNested.get("type");

      // nested types has to be checked too
      if (type.equals("nested")) {
        // Get Nested Class and the fields
        Class nestedClass = allFieldsOfALayerAndTypes.get(fieldName);
        allFields.put(fieldName, this.getAllFieldsFromClass(nestedClass));
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
  private Map<String, Class> getAllFieldsFromClass(Class clazz) {

    Map<String, Class> fieldNamesAndTypes = new HashMap<>();

    // check all private fields of the class
    for (Field field : clazz.getDeclaredFields()) {
      if (Modifier.isPrivate(field.getModifiers())) {
        fieldNamesAndTypes.put(field.getName(), field.getType());
      }
    }

    return fieldNamesAndTypes;
  }
}
