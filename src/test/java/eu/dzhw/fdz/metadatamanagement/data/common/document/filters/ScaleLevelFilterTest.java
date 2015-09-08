/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.document.filters;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.filters.ScaleLevelFilter;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * @author Daniel Katzberg
 *
 */
public class ScaleLevelFilterTest{

  @Test
  public void testConstructor() {

    // Arrange
    ScaleLevelFilter filter = new ScaleLevelFilter();

    // Act

    // Assert
    assertThat(filter.getName(), is(VariableDocument.SCALE_LEVEL_FIELD));
    assertThat(filter.isChoosen(), is(false));
    assertThat(filter.foundResults(), is(false));
    assertThat(filter.getDocCount(), is(0L));
    assertThat(filter.getValue(), is(nullValue()));
  }
  
  @Test
  public void testBasicManipulation() {

    // Arrange
    ScaleLevelFilter filter = new ScaleLevelFilter();

    // Act
    filter.setChoosen(true);
    filter.setDocCount(1L);
    filter.setName("Name");
    filter.setValue("Value");

    // Assert
    assertThat(filter.getName(), not(VariableDocument.SCALE_LEVEL_FIELD));
    assertThat(filter.isChoosen(), is(true));
    assertThat(filter.foundResults(), is(true));
    assertThat(filter.getDocCount(), is(1L));
    assertThat(filter.getValue(), is("Value"));
  }

}
