/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.document.filters;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.filters.ScaleLevelFilter;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class ScaleLevelFilterTest extends AbstractWebTest{

  @Autowired
  private ScaleLevelProvider scaleLevelProvider;

  @Test
  public void testBasicConstructor() {

    // Arrange
    ScaleLevelFilter filter = new ScaleLevelFilter(this.scaleLevelProvider);

    // Act

    // Assert
    assertThat(filter.getName(), is(VariableDocument.SCALE_LEVEL_FIELD));
    assertThat(filter.isChoosen(), is(false));
    assertThat(filter.foundResults(), is(false));
    assertThat(filter.getDocCount(), is(0L));
    assertThat(filter.getValue(), is(nullValue()));
  }

  @Test
  public void testValidIsValid() {
    //Arrange
    ScaleLevelFilter scaleLevelFilter = new ScaleLevelFilter(false,
        this.scaleLevelProvider.getNominalByLocal(), 0L, this.scaleLevelProvider);
    
    //Act
    
    //Assert
    assertThat(scaleLevelFilter.isValid(), is(true));
  }
  
  @Test
  public void testInvalidIsValid() {
    //Arrange
    ScaleLevelFilter scaleLevelFilter = new ScaleLevelFilter(false,
        this.scaleLevelProvider.getNominalByLocal(), 0L, this.scaleLevelProvider);
    
    //Act
    scaleLevelFilter.setName("NotValidName");
    scaleLevelFilter.setValue("NotValidValue");
    
    //Assert
    assertThat(scaleLevelFilter.isValid(), is(false));
  }  

}
