package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SurveyDetailsGuiLabelsTest {
  @Test
  public void testTranslations() {
    assertThat(SurveyDetailsGuiLabels.GUI_LABELS.getDe(), containsString("Erhebungen"));
    assertThat(SurveyDetailsGuiLabels.GUI_LABELS.getEn(), containsString("Surveys"));
  }
}
