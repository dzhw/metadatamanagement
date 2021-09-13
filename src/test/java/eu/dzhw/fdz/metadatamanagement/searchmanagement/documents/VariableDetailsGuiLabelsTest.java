package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class VariableDetailsGuiLabelsTest {
  @Test
  public void testTranslations() {
    assertThat(VariableDetailsGuiLabels.GUI_LABELS.getDe(), containsString("Variablen"));
    assertThat(VariableDetailsGuiLabels.GUI_LABELS.getEn(), containsString("Variables"));
  }
}
