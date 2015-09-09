
package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.builders.VariableSearchFormDtoBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class SearchFormDtoTest {

  
  @Test
  public void testGetFillerForErrorMessageComplete(){
    //Arrange
    VariableSearchFormDto variableSearchFormDto =
        new VariableSearchFormDtoBuilder().withQuery("Query").withScaleLevel("ScaleLevel").build();
    
    //Act
    
    //Assert
    assertThat(variableSearchFormDto.getFillerForErrorMessage(), is("(Query,ScaleLevel)"));
  }
  
  @Test
  public void testGetFillerForErrorMessageNoQuery(){
    //Arrange
    VariableSearchFormDto variableSearchFormDto = new VariableSearchFormDtoBuilder().withScaleLevel("ScaleLevel").build();
    
    //Act
    
    //Assert
    assertThat(variableSearchFormDto.getFillerForErrorMessage(), is("(ScaleLevel)"));
  }
  
  @Test
  public void testGetFillerForErrorMessageNoScaleLevel(){
    //Arrange
    VariableSearchFormDto variableSearchFormDto = new VariableSearchFormDtoBuilder().withQuery("Query").build();
    
    //Act
    
    //Assert
    assertThat(variableSearchFormDto.getFillerForErrorMessage(), is("(Query)"));
  }
  
}
