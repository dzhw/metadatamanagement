/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.i18n;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.config.i18n.CustomLocalDateFormatter;

/**
 * @author Daniel Katzberg
 *
 */
public class CustomLocalDateFormatterTest {

  private CustomLocalDateFormatter dateFormatter;
  
  @Before
  public void beforeTest(){
    this.dateFormatter = new CustomLocalDateFormatter();
  }
  
  
  @Test
  public void testNullDate(){
    //Arrange
    
    //Act
    String returnValue = this.dateFormatter.format(null, Locale.GERMAN);
    
    //Assert
    assertEquals("", returnValue);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInvalidLocale(){
    //Arrange
    
    //Act
    this.dateFormatter.format(LocalDate.now(), Locale.CHINESE);
    
    //Assert -> Check is on anntation
  }
  
  @Test
  public void testValidLocaleEnglish(){
    //Arrange
    
    //Act
    String returnValue = this.dateFormatter.format(LocalDate.now(), Locale.ENGLISH);
    
    //Assert -> Check is on anntation
    assertEquals(true, returnValue.contains("/"));
    assertEquals(false, returnValue.contains("."));
  }
  
  @Test
  public void testValidLocaleGerman(){
    //Arrange
    
    //Act
    String returnValue = this.dateFormatter.format(LocalDate.now(), Locale.GERMAN);
    
    //Assert -> Check is on anntation
    assertEquals(true, returnValue.contains("."));
    assertEquals(false, returnValue.contains("/"));
  }
  
}
