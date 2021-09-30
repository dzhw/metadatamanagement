package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of an analysis package which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class AnalysisPackageSearchDocument extends AnalysisPackage
    implements SearchDocumentInterface {
  private static final long serialVersionUID = -4932203007968722541L;

  static final String[] FIELDS_TO_EXCLUDE_ON_DESERIALIZATION =
      new String[] {"nested*", "configuration", "guiLabels", "*Publications"};

  private Release release = null;
  private Configuration configuration = null;

  // TODO change this
  private I18nString guiLabels = DataSetDetailsGuiLabels.GUI_LABELS;

  private I18nString completeTitle;
  
  private String doi;

  /**
   * Construct the search document with all related subdocuments.
   * 
   */
  @SuppressWarnings("CPD-START")
  public AnalysisPackageSearchDocument(AnalysisPackage analysisPackage, Release release,
      Configuration configuration, String doi) {
    super(analysisPackage);
    this.release = release;
    this.configuration = configuration;
    this.completeTitle = analysisPackage.getTitle();
    this.doi = doi;
  }
}
