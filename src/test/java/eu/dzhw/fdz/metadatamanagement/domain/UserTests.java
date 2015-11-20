/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.domain.builders.UserBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class UserTests{
  
  @Test
  public void testEquals() {
    
    //Arrange
    User user1 = new UserBuilder().withLogin("1").build();
    User user2 = new UserBuilder().withLogin("2").build();
    
    //Act
    boolean checkNull = user1.equals(null);
    boolean checkClass = user1.equals(new Object());
    boolean checkSame = user1.equals(user1);
    boolean checkDifferent = user1.equals(user2);
    
    //Assert
    assertThat(checkNull, is(false));
    assertThat(checkClass, is(false));
    assertThat(checkSame, is(true));
    assertThat(checkDifferent, is(false));
    
  }
  
  @Test
  public void testHashCode() {
    //Arrange
    User user1 = new UserBuilder().withLogin("1").build();
    User user2 = new UserBuilder().withLogin(null).build();
    
    //Act
    int hashCodeWithLogin = user1.hashCode();
    int hashCodeWithoutLogin = user2.hashCode();
    
    //Assert
    assertThat(hashCodeWithLogin, not(0));
    assertThat(hashCodeWithoutLogin, is(0));
  }

}
