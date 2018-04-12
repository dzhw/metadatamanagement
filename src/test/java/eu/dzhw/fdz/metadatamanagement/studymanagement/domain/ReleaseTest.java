/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;

/**
 * @author Daniel Katzberg
 *
 */
public class ReleaseTest {

  @Test
  public void testGetter() {

    // Arrange
    Release release = Release.builder()
      .version("1.0")
      .date(LocalDateTime.now())
      .build();

    // Act

    // Assert
    assertThat(release.getVersion(), is("1.0"));
  }

  @Test
  public void testToString() {
    // Arrange
    Release release = Release.builder()
      .version("1.0")
      .build();

    // Act
    String toString = release.toString();

    // Assert
    assertThat(toString, is(
        "Release(version=1.0, date=null)"));

  }

}
