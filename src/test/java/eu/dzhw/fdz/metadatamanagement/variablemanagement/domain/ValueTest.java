/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.common.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders.ValueBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class ValueTest {

  @Test
  public void testGetter() {
    // Arrange
    Value value = new ValueBuilder().withCode(1234)
      .withAbsoluteFrequency(123)
      .withIsAMissing(false)
      .withLabel(new I18nStringBuilder().withDe("german")
        .withEn("english")
        .build())
      .withRelativeFrequency(new Double(25.15))
      .build();

    // Act

    // Assert
    assertThat(value.getCode(), is(1234));
    assertThat(value.getAbsoluteFrequency(), is(123));
    assertThat(value.getIsAMissing(), is(false));
    assertThat(value.getLabel()
      .getDe(), is("german"));
    assertThat(value.getLabel()
      .getEn(), is("english"));
    assertThat(value.getRelativeFrequency(), is(25.15));
  }

  @Test
  public void testToString() {
    // Arrange
    Value value = new ValueBuilder().withCode(1234)
      .withAbsoluteFrequency(123)
      .withIsAMissing(false)
      .withLabel(new I18nStringBuilder().withDe("german")
        .withEn("english")
        .build())
      .withRelativeFrequency(new Double(25.15))
      .withValueClass("Any Class")
      .build();

    // Act
    String toString = value.toString();

    // Assert
    assertThat(toString, is(
        "Value{code=1234, label=I18nString{de='german', en='english'}, absoluteFrequency=123, relativeFrequency=25.15, isAMissing=false, valueClass=Any Class}"));

  }

}
