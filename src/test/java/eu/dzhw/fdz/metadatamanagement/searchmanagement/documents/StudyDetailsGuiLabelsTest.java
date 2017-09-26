package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StudyDetailsGuiLabelsTest {
  @Test
  public void testTranslations() {
    assertThat(StudyDetailsGuiLabels.GUI_LABELS.getDe(), containsString("Studien"));
    assertThat(StudyDetailsGuiLabels.GUI_LABELS.getEn(), containsString("Studies"));
  }
}
