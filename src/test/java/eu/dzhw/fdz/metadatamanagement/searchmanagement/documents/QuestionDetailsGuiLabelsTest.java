package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class QuestionDetailsGuiLabelsTest {
  @Test
  public void testTranslations() {
    assertThat(QuestionDetailsGuiLabels.GUI_LABELS.getDe(), containsString("Fragen"));
    assertThat(QuestionDetailsGuiLabels.GUI_LABELS.getEn(), containsString("Questions"));
  }
}
