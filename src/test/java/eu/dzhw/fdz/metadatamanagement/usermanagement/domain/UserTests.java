/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class UserTests {

  @Test
  public void testActivationKey() {
    // Arrange
    User user1 = User.builder().login("1")
      .activationKey("Key")
      .build();

    // Act

    // Assert
    assertThat(user1.getActivationKey(), is("Key"));
  }

  @Test
  public void testEquals() {

    // Arrange
    User user1 = User.builder().login("1")
      .build();
    User user1_1 = User.builder().login("1")
      .activationKey("Key")
      .build();
    User user2 = User.builder().login("2")
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
  public void testAbstractAuditingEntity() {
    // Arrange
    User userFieldsFromAbstractAuditing = User.builder().build();

    // Act
    userFieldsFromAbstractAuditing.setCreatedBy("CreatedBy");
    LocalDateTime zonedDateTime = LocalDateTime.now();
    userFieldsFromAbstractAuditing.setCreatedDate(zonedDateTime);
    userFieldsFromAbstractAuditing.setLastModifiedBy("lastModified");
    LocalDateTime lastEditDateTime = LocalDateTime.now();
    userFieldsFromAbstractAuditing.setLastModifiedDate(lastEditDateTime);

    // Assert
    assertThat(userFieldsFromAbstractAuditing, not(nullValue()));
    assertThat(userFieldsFromAbstractAuditing.getCreatedBy(), is("CreatedBy"));
    assertThat(userFieldsFromAbstractAuditing.getCreatedDate(), is(zonedDateTime));
    assertThat(userFieldsFromAbstractAuditing.getLastModifiedBy(), is("lastModified"));
    assertThat(userFieldsFromAbstractAuditing.getLastModifiedDate(), is(lastEditDateTime));

  }

}
