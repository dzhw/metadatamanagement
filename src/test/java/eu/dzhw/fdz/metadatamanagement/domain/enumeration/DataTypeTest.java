/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain.enumeration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class DataTypeTest {
  
  @Test
  public void testValueOf() {
    //Arrange
    DataType string = DataType.valueOf("string");
    
    //Act
        
    //Assert
    assertThat(string.toString(), is("string"));
  }
}
