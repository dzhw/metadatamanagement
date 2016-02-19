/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nSvgBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class I18nSvgTest {

  @Test
  public void testHashCode() {
    // Arrange
    I18nSvg i18nSvg = new I18nSvgBuilder().build();

    // Act
    int hashCodeWithoutDeEn = i18nSvg.hashCode();
    i18nSvg.setDe("de");
    int hashCodeWithoutEn = i18nSvg.hashCode();
    i18nSvg.setEn("en");
    int hashCode = i18nSvg.hashCode();

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
    I18nSvg i18nSvg1 = new I18nSvgBuilder().build();
    I18nSvg i18nSvg2 = new I18nSvgBuilder().build();

    // Act
    boolean checkNull = i18nSvg1.equals(null);
    boolean checkClass = i18nSvg1.equals(new Object());
    boolean checkSame = i18nSvg1.equals(i18nSvg1);
    boolean checkSameButDifferentNoEnNoDe = i18nSvg1.equals(i18nSvg2);
    i18nSvg2.setDe(de1);
    boolean checkSameButDifferentNoEnNoDePart = i18nSvg1.equals(i18nSvg2);
    i18nSvg1.setDe(de1);
    boolean checkSameButDifferentNoEn = i18nSvg1.equals(i18nSvg2);
    i18nSvg2.setEn(en1);
    boolean checkSameButDifferentNoEnPart = i18nSvg1.equals(i18nSvg2);
    i18nSvg1.setEn(en1);
    boolean checkSameButDifferent = i18nSvg1.equals(i18nSvg2);
    i18nSvg2.setEn(en2);
    boolean checkSameButDifferentStart = i18nSvg1.equals(i18nSvg2);
    i18nSvg2.setEn(en1);
    i18nSvg2.setDe(de2);
    boolean checkSameButDifferentEnd = i18nSvg1.equals(i18nSvg2);

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
