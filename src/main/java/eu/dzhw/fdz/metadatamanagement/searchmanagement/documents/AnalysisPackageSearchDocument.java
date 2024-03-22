package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Sponsor;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.projection.DataPackageSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
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

  private List<RelatedPublicationSubDocument> relatedPublications = new ArrayList<>();
  private List<RelatedPublicationNestedDocument> nestedRelatedPublications = new ArrayList<>();

  private List<DataPackageSubDocument> dataPackages = new ArrayList<>();
  private List<DataPackageNestedDocument> nestedDataPackages = new ArrayList<>();

  private List<I18nString> nestedInstitutions = new ArrayList<>();
  private List<Sponsor> nestedSponsors = new ArrayList<>();

  private Release release = null;
  private Configuration configuration = null;

  private I18nString guiLabels = AnalysisPackageDetailsGuiLabels.GUI_LABELS;

  private I18nString completeTitle;

  private String doi;

  /**
   * Construct the search document with all related subdocuments.
   *
   */
  @SuppressWarnings("CPD-START")
  public AnalysisPackageSearchDocument(AnalysisPackage analysisPackage, Release release,
      Configuration configuration, String doi, List<DataPackageSubDocumentProjection> dataPackages,
      List<RelatedPublicationSubDocumentProjection> relatedPublications) {
    super(analysisPackage);
    this.release = release;
    this.configuration = configuration;
    this.completeTitle = analysisPackage.getTitle();
    this.doi = doi;
    if (relatedPublications != null && !relatedPublications.isEmpty()) {
      this.relatedPublications = relatedPublications.stream()
          .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());
      this.nestedRelatedPublications = relatedPublications.stream()
          .map(RelatedPublicationNestedDocument::new).collect(Collectors.toList());
    }
    if (dataPackages != null && !dataPackages.isEmpty()) {
      this.dataPackages =
          dataPackages.stream().map(dataPackage -> new DataPackageSubDocument(dataPackage, doi))
              .collect(Collectors.toList());
      this.nestedDataPackages =
          dataPackages.stream().map(dataPackage -> new DataPackageNestedDocument(dataPackage))
              .collect(Collectors.toList());
    }
    this.nestedInstitutions = analysisPackage.getInstitutions();
    this.nestedSponsors = analysisPackage.getSponsors();
  }
}
