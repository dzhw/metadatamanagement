/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

  private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchMappingTest.class);

  private final String[] ignoreFieldsArray = {"highlightedFields"};
  
  private Set<BeanDefinition> components;
  private List<String> types;
  
  @PostConstruct
  public void postConstruct() throws ClassNotFoundException {
    this.components = this.getBeanDefinitions();
    this.types = this.getTypes();
  }

  @Test
  @SuppressWarnings("rawtypes")
  public void testTypesExistInAllIndices() throws ClassNotFoundException {
    // Arrange

    // Act
    for (String type : this.types) {
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
    Map<String, Map<String, Class>> allFields = this.getAListWithAllFields();
    
    // Act
    for (Entry<String, Map<String, Class>> entry : allFields.entrySet()) {
      for (String type : this.types) {
        for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
          Map mapping =
              this.elasticsearchTemplate.getMapping("metadata_" + locale.getLanguage(), type);
  
          // Assert
          this.assertFieldInMapping(allFields, entry.getValue(), mapping);
        }
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
    List<String> types = new ArrayList<>();

    // look for different document classes
    for (BeanDefinition component : this.components) {
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
  private boolean ignoreField(String fieldName) {
    return Arrays.asList(this.ignoreFieldsArray).contains(fieldName);
  }

  // Assert
  @SuppressWarnings("rawtypes")
  private void assertFieldInMapping(Map<String, Map<String, Class>> allFields,
      Map<String, Class> allFieldsOfALayerAndTypes, Map mapping) throws ClassNotFoundException {

    // Assert all properties in with a any nested depth, if there are in the allFields Map
    LinkedHashMap propertiesMap = (LinkedHashMap) mapping.get("properties");
    Set<String> allFieldsOfALayer = allFieldsOfALayerAndTypes.keySet();
    for (String fieldName : allFieldsOfALayer) {

      // get type from elasticsearch
      Map mappingNested = (LinkedHashMap) propertiesMap.get(fieldName);
      if (mappingNested != null) {
        String type = (String) mappingNested.get("type");

        // nested types has to be checked too
        if (type.equals("nested")) {
          // Get Nested Class and the fields
          Class nestedClass = allFieldsOfALayerAndTypes.get(fieldName);
          allFields.put(fieldName, this.getAllFieldsFromClass(nestedClass));
          this.assertFieldInMapping(allFields, allFields.get(fieldName), mappingNested);
        }
      }

      // for easier debug, if there is any field missed in a mapping
      if (propertiesMap.get(fieldName) == null) {
        LOG.error("Missed Field in Mapping: " + fieldName);
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
  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map<String, Class> getAllFieldsFromClass(Class clazz) {

    Map<String, Class> fieldNamesAndTypes = new HashMap<>();
    PropertyDescriptor[] properties = BeanUtils.getPropertyDescriptors(clazz);
    
    if (clazz.isAssignableFrom(List.class)) {
      return fieldNamesAndTypes;
    }
    
    for(PropertyDescriptor property : properties) {
      if(!property.getPropertyType().isAssignableFrom(Class.class) && !this.ignoreField(property.getName())) {
        fieldNamesAndTypes.put(property.getName(), property.getPropertyType());
      }
    }

    return fieldNamesAndTypes;
  }
}
