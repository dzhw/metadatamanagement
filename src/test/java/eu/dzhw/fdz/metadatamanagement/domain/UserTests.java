/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.ZonedDateTime;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.domain.builders.UserBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class UserTests {

  @Test
  public void testActivationKey() {
    // Arrange
    User user1 = new UserBuilder().withLogin("1")
      .withActivationKey("Key")
      .build();

    // Act

    // Assert
    assertThat(user1.getActivationKey(), is("Key"));
  }

  @Test
  public void testEquals() {

    // Arrange
    User user1 = new UserBuilder().withLogin("1")
      .build();
    User user1_1 = new UserBuilder().withLogin("1")
      .withActivationKey("Key")
      .build();
    User user2 = new UserBuilder().withLogin("2")
      .build();

    // Act
    boolean checkNull = user1.equals(null);
    boolean checkClass = user1.equals(new Object());
    boolean checkSame = user1.equals(user1);
    boolean checkDifferent = user1.equals(user2);
    boolean checkSameButDifferent = user1.equals(user1_1);

    // Assert
    assertThat(checkNull, is(false));
    assertThat(checkClass, is(false));
    assertThat(checkSame, is(true));
    assertThat(checkDifferent, is(false));
    assertThat(checkSameButDifferent, is(true));

  }

  @Test
  public void testHashCode() {
    // Arrange
    User user1 = new UserBuilder().withLogin("1")
      .build();
    User user2 = new UserBuilder().withLogin(null)
      .build();

    // Act
    int hashCodeWithLogin = user1.hashCode();
    int hashCodeWithoutLogin = user2.hashCode();

    // Assert
    assertThat(hashCodeWithLogin, not(0));
    assertThat(hashCodeWithoutLogin, is(0));
  }
  
  @Test
  public void testAbstractAuditingEntity() {
    // Arrange
    User userFieldsFromAbstractAuditing = new UserBuilder().build();
    
    // Act
    userFieldsFromAbstractAuditing.setCreatedBy("CreatedBy");
    ZonedDateTime zonedDateTime = ZonedDateTime.now();
    userFieldsFromAbstractAuditing.setCreatedDate(zonedDateTime);
    userFieldsFromAbstractAuditing.setLastModifiedBy("lastModified");
    ZonedDateTime lastEditDateTime = ZonedDateTime.now();
    userFieldsFromAbstractAuditing.setLastModifiedDate(lastEditDateTime);

    // Assert
    assertThat(userFieldsFromAbstractAuditing, not(nullValue()));
    assertThat(userFieldsFromAbstractAuditing.getCreatedBy(), is("CreatedBy"));
    assertThat(userFieldsFromAbstractAuditing.getCreatedDate(), is(zonedDateTime));
    assertThat(userFieldsFromAbstractAuditing.getLastModifiedBy(), is("lastModified"));
    assertThat(userFieldsFromAbstractAuditing.getLastModifiedDate(), is(lastEditDateTime));
    
  }

}
