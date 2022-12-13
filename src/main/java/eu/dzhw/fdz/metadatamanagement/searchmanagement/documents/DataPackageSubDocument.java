package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.Sponsor;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.projection.DataPackageSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Attributes of a dataPackage which are stored in other search documents.
 *
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class DataPackageSubDocument extends AbstractRdcDomainObject
    implements DataPackageSubDocumentProjection {

  private static final long serialVersionUID = -8641253155035394262L;

  private String id;

  private String dataAcquisitionProjectId;

  private List<I18nString> institutions;

  private List<Sponsor> sponsors;

  private I18nString studySeries;

  private I18nString title;

  private List<Person> projectContributors;

  private String doi;

  private I18nString surveyDesign;

  private String masterId;

  private String successorId;

  private boolean shadow;

  public DataPackageSubDocument() {
    super();
  }

  /**
   * Create a DataPackageSubdocument from a projection and a doi.
   * @param projection a dataPackage projection
   * @param doi a doi or null
   */
  public DataPackageSubDocument(DataPackageSubDocumentProjection projection, String doi) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.doi = doi;
  }
}
