package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class DataTypeProviderTest extends AbstractWebTest{
  
  @Autowired
  private DataTypesProvider dataTypesProvider;
  
  @Test
  public void testValidNumericFieldGerman(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.GERMAN);
    String messageDE = this.dataTypesProvider.getNumericValueByLocale();
    
    //Act
    
    //Assert
    assertEquals("numerisch", messageDE);
  }
  
  @Test
  public void testInValidNumericFieldEnglish(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = this.dataTypesProvider.getNumericValueByLocale();
    
    //Act
    
    //Assert
    assertThat("numerisch", not(messageEN));
  }
  
  @Test
  public void testValidStringFieldGerman(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.GERMAN);
    String messageDE = this.dataTypesProvider.getStringValueByLocale();
    
    //Act
    
    //Assert
    assertEquals("string", messageDE);
  }
  
  @Test
  public void testInValidStringFieldEnglish(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = this.dataTypesProvider.getStringValueByLocale();
    
    //Act
    
    //Assert
    assertEquals("string", messageEN);
  }
  
  @Test
  public void testGetAllDataTypeEnglish(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.ENGLISH);
        
    //Act
    HashSet<String> allDataTypes = this.dataTypesProvider.getAllDataTypes();
    
    //Assert
    assertEquals(true, allDataTypes.contains("numeric"));
    assertEquals(2, allDataTypes.size());
  }

  @Test
  public void testGetAllDataTypeGerman(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.GERMAN);
        
    //Act
    HashSet<String> allDataTypes = this.dataTypesProvider.getAllDataTypes();
    
    //Assert
    assertEquals(true, allDataTypes.contains("numerisch"));
    assertEquals(2, allDataTypes.size());
  }
  
}
