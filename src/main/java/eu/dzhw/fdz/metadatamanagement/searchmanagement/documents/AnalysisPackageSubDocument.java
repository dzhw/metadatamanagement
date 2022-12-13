package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.projection.AnalysisPackageSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.Sponsor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Attributes of an analyis package which are stored in other search documents.
 *
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class AnalysisPackageSubDocument extends AbstractRdcDomainObject
    implements AnalysisPackageSubDocumentProjection {

  private static final long serialVersionUID = -8641253155035394262L;

  private String id;

  private String dataAcquisitionProjectId;

  private List<I18nString> institutions;

  private List<Sponsor> sponsors;

  private I18nString title;

  private List<Person> authors;

  private String doi;

  private String masterId;

  private String successorId;

  private boolean shadow;

  public AnalysisPackageSubDocument() {
    super();
  }

  /**
   * Create a AnalysisPackageSubdocument from a projection and a doi.
   * @param projection an anlysis package projection
   * @param doi a doi or null
   */
  public AnalysisPackageSubDocument(AnalysisPackageSubDocumentProjection projection, String doi) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.doi = doi;
  }
}
