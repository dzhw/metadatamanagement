/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.types;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.provider.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class ScaleLevelTest extends AbstractWebTest{
  
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
}
