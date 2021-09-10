package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class DataSetDetailsGuiLabelsTest {
  @Test
  public void testTranslations() {
    assertThat(DataSetDetailsGuiLabels.GUI_LABELS.getDe(), containsString("Datensätze"));
    assertThat(DataSetDetailsGuiLabels.GUI_LABELS.getEn(), containsString("Data Sets"));
  }
}
