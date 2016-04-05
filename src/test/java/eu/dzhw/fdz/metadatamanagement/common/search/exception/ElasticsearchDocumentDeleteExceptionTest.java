/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.search.exception;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.common.search.exception.ElasticsearchDocumentDeleteException;

/**
 * @author Daniel Katzberg
 *
 */
public class ElasticsearchDocumentDeleteExceptionTest {

  @Test
  public void testElasticsearchDocumentDeleteExceptionWithId() {
    // Arrange
    ElasticsearchDocumentDeleteException deleteException =
        new ElasticsearchDocumentDeleteException("index", "type", "id", "reason");

    // Act

    // Assert
    assertThat(deleteException.getMessage(),
        is("Unable to delete Document (ID=id) of type type from index index: reason"));
  }

  @Test
  public void testElasticsearchDocumentDeleteExceptionDeleteAll() {
    // Arrange
    ElasticsearchDocumentDeleteException deleteException =
        new ElasticsearchDocumentDeleteException("index", "type", "reason");

    // Act

    // Assert
    assertThat(deleteException.getMessage(),
        is("Unable to delete all documents of type type from index index: reason"));
  }

  @Test
  public void testElasticsearchDocumentDeleteExceptionWithFieldName() {
    // Arrange
    ElasticsearchDocumentDeleteException deleteException =
        new ElasticsearchDocumentDeleteException("index", "type", "fieldName", "value", "reason");

    // Act

    // Assert
    assertThat(deleteException.getMessage(), is(
        "Unable to delete Document (field=fieldName, value=value) of type type from index index: reason"));
  }
}
