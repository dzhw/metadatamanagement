package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class QuestionDetailsGuiLabelsTest {
  @Test
  public void testTranslations() {
    assertThat(QuestionDetailsGuiLabels.GUI_LABELS.getDe(), containsString("Fragen"));
    assertThat(QuestionDetailsGuiLabels.GUI_LABELS.getEn(), containsString("Questions"));
  }
}
