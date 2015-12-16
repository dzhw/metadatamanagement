/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class I18nStringTest {

  @Test
  public void testHashCode() {
    // Arrange
    I18nString i18nString = new I18nStringBuilder().build();

    // Act
    int hashCodeWithoutDeEn = i18nString.hashCode();
    i18nString.setDe("de");
    int hashCodeWithoutEn = i18nString.hashCode();
    i18nString.setEn("en");
    int hashCode = i18nString.hashCode();

    // Assert
    assertThat(hashCodeWithoutDeEn, not(0));
    assertThat(hashCodeWithoutEn, not(0));
    assertThat(hashCode, not(0));
    assertThat(hashCode, not(hashCodeWithoutEn));
    assertThat(hashCode, not(hashCodeWithoutDeEn));
    assertThat(hashCodeWithoutEn, not(hashCodeWithoutDeEn));
  }

  @Test
  public void testEquals() {
    // Arrange
    String de1 = "de1";
    String de2 = "de2";
    String en1 = "en1";
    String en2 = "en2";
    I18nString i18nString1 = new I18nStringBuilder().build();
    I18nString i18nString2 = new I18nStringBuilder().build();

    // Act
    boolean checkNull = i18nString1.equals(null);
    boolean checkClass = i18nString1.equals(new Object());
    boolean checkSame = i18nString1.equals(i18nString1);
    boolean checkSameButDifferentNoEnNoDe = i18nString1.equals(i18nString2);
    i18nString2.setDe(de1);
    boolean checkSameButDifferentNoEnNoDePart = i18nString1.equals(i18nString2);
    i18nString1.setDe(de1);
    boolean checkSameButDifferentNoEn = i18nString1.equals(i18nString2);
    i18nString2.setEn(en1);
    boolean checkSameButDifferentNoEnPart = i18nString1.equals(i18nString2);
    i18nString1.setEn(en1);
    boolean checkSameButDifferent = i18nString1.equals(i18nString2);
    i18nString2.setEn(en2);
    boolean checkSameButDifferentStart = i18nString1.equals(i18nString2);
    i18nString2.setEn(en1);
    i18nString2.setDe(de2);
    boolean checkSameButDifferentEnd = i18nString1.equals(i18nString2);

    // Assert
    assertThat(checkNull, is(false));
    assertThat(checkClass, is(false));
    assertThat(checkSame, is(true));
    assertThat(checkSameButDifferentNoEnNoDe, is(true));
    assertThat(checkSameButDifferentNoEnNoDePart, is(false));
    assertThat(checkSameButDifferentNoEn, is(true));
    assertThat(checkSameButDifferentNoEnPart, is(false));
    assertThat(checkSameButDifferent, is(true));
    assertThat(checkSameButDifferentStart, is(false));
    assertThat(checkSameButDifferentEnd, is(false));
  }

}
