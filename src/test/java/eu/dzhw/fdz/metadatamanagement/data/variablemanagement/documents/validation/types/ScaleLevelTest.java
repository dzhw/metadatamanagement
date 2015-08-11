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

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.types.ScaleLevel;

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

}
