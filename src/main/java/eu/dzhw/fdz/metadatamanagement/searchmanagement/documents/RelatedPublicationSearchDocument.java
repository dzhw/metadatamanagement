package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of a related publication which is stored in elasticsearch.
 *
 * @author Daniel Katzberg
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("CPD-START")
public class RelatedPublicationSearchDocument extends RelatedPublication
    implements SearchDocumentInterface {

  private static final long serialVersionUID = -2413839238037781610L;

  static final String[] FIELDS_TO_EXCLUDE_ON_DESERIALIZATION =
      new String[] {"nested*", "guiLabels"};

  private List<DataPackageSubDocument> dataPackages = new ArrayList<>();
  private List<DataPackageNestedDocument> nestedDataPackages = new ArrayList<>();

  private List<AnalysisPackageSubDocument> analysisPackages = new ArrayList<>();
  private List<AnalysisPackageNestedDocument> nestedAnalysisPackages = new ArrayList<>();

  private List<I18nString> nestedStudySerieses = new ArrayList<>();

  // dummy string which ensures that related publications are always released
  private String release = "released";

  /*
   * dummy field which ensures that related publications can be found.
   */
  private boolean shadow = false;

  private I18nString guiLabels = RelatedPublicationDetailsGuiLabels.GUI_LABELS;

  private I18nString completeTitle;

  /**
   * Construct the search document with all related subdocuments.
   *
   * @param relatedPublication the related publication to be searched for
   * @param dataPackages the dataPackages for which the publication was published
   * @param nestedDataPackages the dataPackages for which the publication was published
   */
  public RelatedPublicationSearchDocument(RelatedPublication relatedPublication,
      List<DataPackageSubDocument> dataPackages,
      List<DataPackageNestedDocument> nestedDataPackages,
      List<AnalysisPackageSubDocument> analysisPackages,
      List<AnalysisPackageNestedDocument> nestedAnalysisPackages) {
    super(relatedPublication);
    if (dataPackages != null && !dataPackages.isEmpty()) {
      this.dataPackages = dataPackages;
      nestedStudySerieses = dataPackages.stream().map(DataPackageSubDocument::getStudySeries)
          .dropWhile(studySeries -> studySeries == null).distinct().collect(Collectors.toList());
    }
    if (nestedDataPackages != null && !nestedDataPackages.isEmpty()) {
      this.nestedDataPackages = nestedDataPackages;
    }
    if (analysisPackages != null && !analysisPackages.isEmpty()) {
      this.analysisPackages = analysisPackages;
    }
    if (nestedAnalysisPackages != null && !nestedAnalysisPackages.isEmpty()) {
      this.nestedAnalysisPackages = nestedAnalysisPackages;
    }
    this.completeTitle = I18nString.builder()
        .de(relatedPublication.getTitle() + " (" + relatedPublication.getId() + ")")
        .en(relatedPublication.getTitle() + " (" + relatedPublication.getId() + ")").build();
  }
}
