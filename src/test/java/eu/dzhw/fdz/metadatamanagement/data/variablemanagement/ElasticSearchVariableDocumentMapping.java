/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
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
  public void test() {
    //Arrange
    List<String> allFields = this.getAListWithAllFields();
    
    //Act
    for(Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      Map map = this.elasticsearchTemplate.getMapping("metadata_" + locale.getLanguage(), "variables");
      LinkedHashMap propertiesMap = (LinkedHashMap) map.get("properties");
      
      //Assert
      for (String fieldName : allFields) {
        assertThat(propertiesMap.get(fieldName), is(notNullValue()));
      }
    }    
  }
  
  private List<String> getAListWithAllFields() {
    List<String> allFields = new ArrayList<>();
    VariableDocument document = new VariableDocumentBuilder().build();
    
    //AbstractDocument
    allFields.addAll(this.getAllFieldsFromClass(document.getClass().getSuperclass()));
    
    //VariableDocument
    allFields.addAll(this.getAllFieldsFromClass(document.getClass()));    
    
    //TODO DKatzberg SurveyPeriod
    
    //TODO DKatzberg DateRange
    
    //Remove special highlighting fields
    allFields.remove("highlightedFields");
    
    return allFields;
  }
  
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
