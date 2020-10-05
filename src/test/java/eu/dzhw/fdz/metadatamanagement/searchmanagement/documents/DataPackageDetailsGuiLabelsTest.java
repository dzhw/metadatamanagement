package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DataPackageDetailsGuiLabelsTest {
  @Test
  public void testTranslations() {
    assertThat(DataPackageDetailsGuiLabels.GUI_LABELS.getDe(), containsString("Datenpakete"));
    assertThat(DataPackageDetailsGuiLabels.GUI_LABELS.getEn(), containsString("Data Packages"));
  }
}
