/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.types;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author Daniel Katzberg
 *
 */
public class ScaleLevelTest {
  
  @Test
  public void testValidNumericFieldGerman(){
    LocaleContextHolder.setLocale(Locale.GERMAN);
    String messageDE = ScaleLevel.getScaleLevel().getMetricByLocal();
    assertEquals("metrisch", messageDE);
  }
  
  @Test
  public void testInValidNumericFieldEnglish(){
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = ScaleLevel.getScaleLevel().getMetricByLocal();
    assertThat("metrisch", not(messageEN));
  }
  
  @Test
  public void testValidNominalFieldGerman(){
    LocaleContextHolder.setLocale(Locale.GERMAN);
    String messageDE = ScaleLevel.getScaleLevel().getNominalByLocal();
    assertEquals("nominal", messageDE);
  }
  
  @Test
  public void testInValidNominalFieldEnglish(){
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = ScaleLevel.getScaleLevel().getNominalByLocal();
    assertEquals("nominal", messageEN);
  }
  
  @Test
  public void testValidOrdinalFieldGerman(){
    LocaleContextHolder.setLocale(Locale.GERMAN);
    String messageDE = ScaleLevel.getScaleLevel().getOrdinalByLocal();
    assertEquals("ordinal", messageDE);
  }
  
  @Test
  public void testInValidOrdinalFieldEnglish(){
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = ScaleLevel.getScaleLevel().getOrdinalByLocal();
    assertEquals("ordinal", messageEN);
  }

}
