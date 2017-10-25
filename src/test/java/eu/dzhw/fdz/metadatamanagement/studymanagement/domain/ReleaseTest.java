/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
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
      .notes(I18nString.builder().de("Eine Notiz für die Version 1.0")
        .en("A notice for the version 1.0.")
        .build())
      .version("1.0")
      .date(LocalDateTime.now())
      .build();

    // Act

    // Assert
    assertThat(release.getNotes()
      .getDe(), is("Eine Notiz für die Version 1.0"));
    assertThat(release.getNotes()
      .getEn(), is("A notice for the version 1.0."));
    assertThat(release.getVersion(), is("1.0"));
  }

  @Test
  public void testToString() {
    // Arrange
    Release release = Release.builder()
      .notes(I18nString.builder().de("Eine Notiz für die Version 1.0")
        .en("A notice for the version 1.0.")
        .build())
      .version("1.0")
      .build();

    // Act
    String toString = release.toString();

    // Assert
    assertThat(toString, is(
        "Release(version=1.0, date=null, notes=I18nString(de=Eine Notiz für die Version 1.0, en=A notice for the version 1.0.))"));

  }

}
