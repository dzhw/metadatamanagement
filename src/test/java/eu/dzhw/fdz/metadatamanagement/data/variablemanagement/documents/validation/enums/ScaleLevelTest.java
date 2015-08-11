/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.enums;

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
    LocaleContextHolder.setLocale(Locale.GERMANY);
    String messageDE = ScaleLevel.METRIC.getI18nValue();
    assertEquals("metrisch", messageDE);
  }
  
  @Test
  public void testInValidNumericFieldEnglish(){
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = ScaleLevel.METRIC.getI18nValue();
    assertThat("metrisch", not(messageEN));
  }

}
