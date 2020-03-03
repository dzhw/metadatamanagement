package eu.dzhw.fdz.metadatamanagement.common.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;

public class MarkdownHelperTest extends AbstractTest {
  @Autowired
  private MarkdownHelper helper;

  @Test
  public void testPlainString() {
    String input = "Hallo ich bin ein Test!";
    String output = helper.getPlainText(input);

    assertEquals(input, output);
  }

  @Test
  public void testTextWithLink() {
    String input = "Hallo ich bin ein [Testlink](http://localhost:8080)!";
    String expected = "Hallo ich bin ein Testlink!";

    String output = helper.getPlainText(input);

    assertEquals(expected, output);
  }

  @Test
  public void testTextWithLinkAndBoldAndItalic() {
    String input = "Hallo **ich** bin _ein_ [Testlink](http://localhost:8080)!";
    String expected = "Hallo ich bin ein Testlink!";

    String output = helper.getPlainText(input);

    assertEquals(expected, output);
  }

  @Test
  public void testTextWithLineBreaks() {
    String input = "Hallo ich bin ein Test.\nUnd hier ist die nächste Zeile.";

    String output = helper.getPlainText(input);

    assertEquals(input, output);
  }

  @Test
  public void testTextWithStrikethrough() {
    String input = "Spaß mit ~~Flaggen~~\njkdafhkadjf\nadkjfhadjkf";
    String expected = "Spaß mit Flaggen\njkdafhkadjf\nadkjfhadjkf";

    String output = helper.getPlainText(input);

    assertEquals(expected, output);
  }
}
