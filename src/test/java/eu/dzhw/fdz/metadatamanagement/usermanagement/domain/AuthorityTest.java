/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;

/**
 * No need for Application Conetext. No integration test.
 * 
 * @author Daniel Katzberg
 *
 */
public class AuthorityTest {

  @Test
  public void testHashCode() {

    // Arrange
    Authority authorityWithoutName = new Authority();
    Authority authorityWithName = new Authority();
    authorityWithName.setName("Name");

    // Act
    int withoutName = authorityWithoutName.hashCode();
    int withName = authorityWithName.hashCode();

    // Assert
    assertThat(withoutName, is(0));
    assertThat(withName, not(0));
  }

  @Test
  public void testToString() {

    // Arrange
    Authority authorityWithName = new Authority();
    authorityWithName.setName("Name");

    // Act

    // Assert
    assertThat(authorityWithName.toString(), is("Authority{name='Name'}"));
  }

  @Test
  public void testEquals() {
    // Arrange
    Authority authority1 = new Authority();
    authority1.setName("1");
    Authority authority2 = new Authority();
    authority1.setName("2");

    // Act
    boolean checkNull = authority1.equals(null);
    boolean checkClass = authority1.equals(new Object());
    boolean checkSame = authority1.equals(authority1);
    boolean checkDifferent = authority1.equals(authority2);


    // Assert
    assertThat(checkNull, is(false));
    assertThat(checkClass, is(false));
    assertThat(checkSame, is(true));
    assertThat(checkDifferent, is(false));
  }

}
