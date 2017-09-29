package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DataSetDetailsGuiLabelsTest {
  @Test
  public void testTranslations() {
    assertThat(DataSetDetailsGuiLabels.GUI_LABELS.getDe(), containsString("Datensätze"));
    assertThat(DataSetDetailsGuiLabels.GUI_LABELS.getEn(), containsString("Data Sets"));
  }
}
