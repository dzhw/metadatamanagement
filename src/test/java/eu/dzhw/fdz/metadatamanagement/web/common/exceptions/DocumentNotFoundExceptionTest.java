package eu.dzhw.fdz.metadatamanagement.web.common.exceptions;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

public class DocumentNotFoundExceptionTest {


  @Test
  public void testThrowDocumentNotFoundException() {
    //Arrange
    DocumentNotFoundException exception =
        new DocumentNotFoundException("Id01", Locale.GERMAN, VariableDocument.class.getSimpleName());
    
    //Act 
    String message = exception.getMessage();
    
    //Assert
    assertEquals("VariableDocument with IDId01 (de) not found!", message);
  }

}
