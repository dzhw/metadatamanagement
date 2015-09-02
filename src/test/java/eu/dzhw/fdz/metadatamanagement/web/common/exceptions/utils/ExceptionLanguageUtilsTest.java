/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.common.exceptions.utils;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * @author Daniel Katzberg
 *
 */
public class ExceptionLanguageUtilsTest {
  
  private ExceptionLanguageUtils exceptionLanguageUtils;
  
  @Before
  public void beforeTest(){
    this.exceptionLanguageUtils = new ExceptionLanguageUtils();
  }
  
  @Test
  public void testGetCorrectReadableLanguage(){
    //Arrange
    
    //Act
    String correctReadableGerman = this.exceptionLanguageUtils.getCorrectReadableLanguage(Locale.GERMAN);
    String correctReadableEnglish = this.exceptionLanguageUtils.getCorrectReadableLanguage(Locale.ENGLISH);
    
    //Assert
    assertThat(correctReadableGerman, is(ExceptionLanguageUtils.NAV_LANGUAGE_GERMAN));
    assertThat(correctReadableEnglish, is(ExceptionLanguageUtils.NAV_LANGUAGE_ENGLISH));
  }
  
  
  @Test
  public void testGetCorrectDocumentType(){
    //Arrange
    
    //Act
    String variableDocumentClass = this.exceptionLanguageUtils.getCorrectDocumentType(VariableDocument.class);
    String emptyClass = this.exceptionLanguageUtils.getCorrectDocumentType(String.class);
    
    //Assert
    assertThat(variableDocumentClass, is(ExceptionLanguageUtils.DOCUMENTTYPE_VARIABLE));
    assertThat(emptyClass, is(""));
  }

}
