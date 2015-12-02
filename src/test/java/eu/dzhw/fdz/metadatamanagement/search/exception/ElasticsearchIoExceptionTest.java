/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.search.exception;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

/**
 * No need for application context. No integration test.
 * 
 * @author Daniel Katzberg
 *
 */
public class ElasticsearchIoExceptionTest {

  @Test
  public void testElasticsearchIoException() {
    // Arrange
    ElasticsearchIoException ioException = new ElasticsearchIoException(new IOException("Message"));
    
    //Act
    
    //Assert
    assertThat(ioException.getMessage(), is("Unable to connect to elasticsearch."));
  }
}
