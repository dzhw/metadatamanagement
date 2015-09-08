/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class ScaleLevelProviderTest extends AbstractWebTest{
  
  @Autowired
  private ScaleLevelProvider scaleLevelProvider;
  
  @Test
  public void testValidNumericFieldGerman(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.GERMAN);
    String messageDE = this.scaleLevelProvider.getMetricByLocal();
    
    //Act
    
    //Assert
    assertEquals("metrisch", messageDE);
  }
  
  @Test
  public void testInValidNumericFieldEnglish(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = this.scaleLevelProvider.getMetricByLocal();
    
    //Act
    
    //Assert
    assertThat("metrisch", not(messageEN));
  }
  
  @Test
  public void testValidNominalFieldGerman(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.GERMAN);
    String messageDE = this.scaleLevelProvider.getNominalByLocal();
    
    //Act
    
    //Assert
    assertEquals("nominal", messageDE);
  }
  
  @Test
  public void testInValidNominalFieldEnglish(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = this.scaleLevelProvider.getNominalByLocal();
    
    //Act
    
    //Assert
    assertEquals("nominal", messageEN);
  }
  
  @Test
  public void testValidOrdinalFieldGerman(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.GERMAN);
    String messageDE = this.scaleLevelProvider.getOrdinalByLocal();
    
    //Act
    
    //Assert
    assertEquals("ordinal", messageDE);
  }
  
  @Test
  public void testInValidOrdinalFieldEnglish(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = this.scaleLevelProvider.getOrdinalByLocal();
    
    //Act
    
    //Assert
    assertEquals("ordinal", messageEN);
  }
  
  @Test
  public void testGetAllScaleLevelEnglish(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.ENGLISH);
        
    //Act
    HashSet<String> allScaleLevel = this.scaleLevelProvider.getAllScaleLevel();
    
    //Assert
    assertEquals(true, allScaleLevel.contains("metric"));
    assertEquals(3, allScaleLevel.size());
  }
  
  @Test
  public void testGetAllScaleLevelGerman(){
    //Arrange
    LocaleContextHolder.setLocale(Locale.GERMAN);
        
    //Act
    HashSet<String> allScaleLevel = this.scaleLevelProvider.getAllScaleLevel();
    
    //Assert
    assertEquals(true, allScaleLevel.contains("metrisch"));
    assertEquals(3, allScaleLevel.size());
  }
  
  @Test
  public void testGetAllScaleLevel(){
    //Arrange
    
        
    //Act
    HashSet<String> allScaleLevel = this.scaleLevelProvider.getAllScaleLevel(Locale.GERMAN);
    
    //Assert
    assertEquals(true, allScaleLevel.contains("metrisch"));
    assertEquals(3, allScaleLevel.size());
  }
}
