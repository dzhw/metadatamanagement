package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of an data acquisition project which is stored in elasticsearch.
 *
 * @author Theresa Möller
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class DataAcquisitionProjectSearchDocument extends DataAcquisitionProject implements SearchDocumentInterface {

  private static final long serialVersionUID = -3963249178909573557L;

  static final String[] FIELDS_TO_EXCLUDE_ON_DESERIALIZATION =
    new String[] {"nested*", "guiLabels"};

  private I18nString guiLabels = AnalysisPackageDetailsGuiLabels.GUI_LABELS;
  private I18nString completeTitle;

  /**
   * Construct the search document with all related subdocuments.
   *
   */
  @SuppressWarnings("CPD-START")
  public DataAcquisitionProjectSearchDocument(DataAcquisitionProject project) {
    super(project);

    // titleString is required but as projects do not have titles we use the masterID here
    I18nString titleString = new I18nString();
    titleString.setDe(project.getMasterId());
    titleString.setEn(project.getMasterId());
    this.completeTitle = titleString;
  }
}
