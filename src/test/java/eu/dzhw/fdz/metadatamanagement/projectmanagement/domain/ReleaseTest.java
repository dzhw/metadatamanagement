/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.ZonedDateTime;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.common.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.builders.ReleaseBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class ReleaseTest {

  @Test
  public void testGetter() {

    // Arrange
    Release release = new ReleaseBuilder().withDoi("A Test Doi")
      .withNotes(new I18nStringBuilder().withDe("Eine Notiz für die Version 1.0")
        .withEn("A notice for the version 1.0.")
        .build())
      .withVersion("1.0")
      .withDate(ZonedDateTime.now())
      .build();

    // Act

    // Assert
    assertThat(release.getDoi(), is("A Test Doi"));
    assertThat(release.getNotes()
      .getDe(), is("Eine Notiz für die Version 1.0"));
    assertThat(release.getNotes()
      .getEn(), is("A notice for the version 1.0."));
    assertThat(release.getVersion(), is("1.0"));
  }

  @Test
  public void testToString() {
    // Arrange
    Release release = new ReleaseBuilder().withDoi("A Test Doi")
      .withNotes(new I18nStringBuilder().withDe("Eine Notiz für die Version 1.0")
        .withEn("A notice for the version 1.0.")
        .build())
      .withVersion("1.0")
      .build();

    // Act
    String toString = release.toString();

    // Assert
    assertThat(toString, is(
        "Release{version=1.0, doi=A Test Doi, date=null, notes=I18nString{de='Eine Notiz für die Version 1.0', en='A notice for the version 1.0.'}}"));

  }

}
