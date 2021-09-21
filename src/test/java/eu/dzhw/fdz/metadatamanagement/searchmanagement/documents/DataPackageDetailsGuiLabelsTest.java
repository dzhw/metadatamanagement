package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class DataPackageDetailsGuiLabelsTest {
  @Test
  public void testTranslations() {
    assertThat(DataPackageDetailsGuiLabels.GUI_LABELS.getDe(), containsString("Datenpakete"));
    assertThat(DataPackageDetailsGuiLabels.GUI_LABELS.getEn(), containsString("Data Packages"));
  }
}
