package eu.dzhw.fdz.metadatamanagement.domain.enumeration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class AtomicQuestionTypeTest {


  @Test
  public void testValueOf() {
    // Arrange
    AtomicQuestionType singleChoice = AtomicQuestionType.valueOf("singlechoice");

    // Act

    // Assert
    assertThat(singleChoice.toString(), is("singlechoice"));
  }
}
