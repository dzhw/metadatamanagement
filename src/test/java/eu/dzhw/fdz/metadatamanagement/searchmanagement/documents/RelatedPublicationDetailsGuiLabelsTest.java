package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RelatedPublicationDetailsGuiLabelsTest {
  @Test
  public void testTranslations() {
    assertThat(RelatedPublicationDetailsGuiLabels.GUI_LABELS.getDe(), containsString("Publikationen"));
    assertThat(RelatedPublicationDetailsGuiLabels.GUI_LABELS.getEn(), containsString("Publications"));
  }
}
