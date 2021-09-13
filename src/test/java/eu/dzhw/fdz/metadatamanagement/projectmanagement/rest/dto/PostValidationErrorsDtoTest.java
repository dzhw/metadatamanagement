/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest.dto;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class PostValidationErrorsDtoTest {
  
  @Test
  public void testToString() {
    //Arrange
    PostValidationErrorsDto postValidationErrorsDto = new PostValidationErrorsDto(new ArrayList<>());
    postValidationErrorsDto.setErrors(new ArrayList<>());//check set method
    
    //Act
    String toString = postValidationErrorsDto.toString();
    
    //Assert
    assertThat(toString, is("PostValidationErrorsDto{errors=[]}"));
  }

}
