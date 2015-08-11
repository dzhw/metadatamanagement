package eu.dzhw.fdz.metadatamanagement.service.variablemanagement.enums;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.enums.DataType;

/**
 * @author Daniel Katzberg
 *
 */
public class DataTypeTest{
  
  @Test
  public void testValidNumericFieldGerman(){
    LocaleContextHolder.setLocale(Locale.GERMANY);
    String messageDE = DataType.NUMERIC.getI18nValue();
    assertEquals("Numerisch", messageDE);
  }
  
  @Test
  public void testInValidNumericFieldEnglish(){
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = DataType.NUMERIC.getI18nValue();
    assertThat("Numerisch", not(messageEN));
  }

}
