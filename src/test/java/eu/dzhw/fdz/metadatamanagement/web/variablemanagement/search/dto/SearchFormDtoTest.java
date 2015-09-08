
package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.builders.SearchFormDtoBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class SearchFormDtoTest {

  
  @Test
  public void testGetFillerForErrorMessageComplete(){
    //Arrange
    SearchFormDto searchFormDto =
        new SearchFormDtoBuilder().withQuery("Query").withScaleLevel("ScaleLevel").build();
    
    //Act
    
    //Assert
    assertThat(searchFormDto.getFillerForErrorMessage(), is("(Query,ScaleLevel)"));
  }
  
  @Test
  public void testGetFillerForErrorMessageNoQuery(){
    //Arrange
    SearchFormDto searchFormDto = new SearchFormDtoBuilder().withScaleLevel("ScaleLevel").build();
    
    //Act
    
    //Assert
    assertThat(searchFormDto.getFillerForErrorMessage(), is("(ScaleLevel)"));
  }
  
  @Test
  public void testGetFillerForErrorMessageNoScaleLevel(){
    //Arrange
    SearchFormDto searchFormDto = new SearchFormDtoBuilder().withQuery("Query").build();
    
    //Act
    
    //Assert
    assertThat(searchFormDto.getFillerForErrorMessage(), is("(Query)"));
  }
  
}
