package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.types;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.provider.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class DataTypeTest extends AbstractWebTest{
  
  @Autowired
  private DataTypesProvider dataTypesProvider;
  
  @Test
  public void testValidNumericFieldGerman(){
    LocaleContextHolder.setLocale(Locale.GERMAN);
    String messageDE = this.dataTypesProvider.getNumericValueByLocale();
    assertEquals("numerisch", messageDE);
  }
  
  @Test
  public void testInValidNumericFieldEnglish(){
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = this.dataTypesProvider.getNumericValueByLocale();
    assertThat("numerisch", not(messageEN));
  }
  
  @Test
  public void testValidStringFieldGerman(){
    LocaleContextHolder.setLocale(Locale.GERMAN);
    String messageDE = this.dataTypesProvider.getStringValueByLocale();
    assertEquals("string", messageDE);
  }
  
  @Test
  public void testInValidStringFieldEnglish(){
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = this.dataTypesProvider.getStringValueByLocale();
    assertEquals("string", messageEN);
  }

}
