package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.javers.core.metamodel.annotation.Entity;
import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation.AtMostOneAttachmentPerScript;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Metadata which will be stored with each attachment of a {@link Script}. There must be no more
 * than one attachment per script!
 */
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@AtMostOneAttachmentPerScript(
    message = "analysis-package-management.error.script-attachment-metadata"
        + ".script-uuid.not-unique")
public class ScriptAttachmentMetadata extends AbstractShadowableRdcDomainObject {

  private static final long serialVersionUID = 5380995617337286180L;

  /**
   * The id of the attachment. Holds the complete path which can be used to download the file.
   */
  @Id
  @Setter(AccessLevel.NONE)
  private String id;

  /**
   * The master id of the analysis package attachment.
   */
  @Setter(AccessLevel.NONE)
  private String masterId;

  /**
   * The id of the {@link AnalysisPackage} to which this attachment belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message = "analysis-package-management.error.script-attachment-metadata"
      + ".analysis-package-id.not-empty")
  private String analysisPackageId;

  /**
   * The uuid of the {@link Script} to which this attachment belongs.
   *
   * Must not be empty. Must be unique within an analysis package (there must be at most one
   * attachment per script). Duplicates are only possible for shadow copies.
   */
  @NotEmpty(message = "analysis-package-management.error.script-attachment-metadata"
      + ".script-uuid.not-empty")
  private String scriptUuid;

  /**
   * The id of the {@link DataAcquisitionProject} to which the {@link AnalysisPackage} of this
   * attachment belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message = "analysis-package-management.error.script-attachment-metadata"
      + ".project-id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * The filename of the attachment.
   *
   * Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and
   * ".".
   */
  @NotEmpty(message = "analysis-package-management.error.script-attachment-metadata.filename"
      + ".not-empty")
  @Pattern(message = "analysis-package-management.error.script-attachment-metadata.filename"
      + ".not-valid", regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT)
  private String fileName;

  /**
   * Generate the id of this attachment from the analysisPackageId, the scriptId and the fileName.
   */
  public void generateId() {
    // hack to satisfy javers
    this.setId("/public/files/analysis-packages/" + analysisPackageId + "/scripts/" + scriptUuid
        + "/attachments/" + fileName);
  }

  @Override
  protected void setMasterIdInternal(String masterId) {
    this.masterId = masterId;
  }

  /**
   * Returns the master id of the script attachment.
   *
   * @return Master Id
   */
  @Override
  public String getMasterId() {
    return masterId;
  }

  @Override
  protected void setIdInternal(String id) {
    this.id = id;
  }
}
