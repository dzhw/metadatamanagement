package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class VariableDetailsGuiLabelsTest {
  @Test
  public void testTranslations() {
    assertThat(VariableDetailsGuiLabels.GUI_LABELS.getDe(), containsString("Variablen"));
    assertThat(VariableDetailsGuiLabels.GUI_LABELS.getEn(), containsString("Variables"));
  }
}
