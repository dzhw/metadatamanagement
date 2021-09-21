package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class InstrumentDetailsGuiLabelsTest {
  @Test
  public void testTranslations() {
    assertThat(InstrumentDetailsGuiLabels.GUI_LABELS.getDe(), containsString("Instrumente"));
    assertThat(InstrumentDetailsGuiLabels.GUI_LABELS.getEn(), containsString("Instruments"));
  }
}
