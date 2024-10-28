package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidShadowId;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation.SetHasBeenReleasedBeforeOnlyOnce;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation.ValidSemanticVersion;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.javers.core.metamodel.annotation.Entity;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * The data acquisition project collects the metadata for the data products which are published by
 * our RDC. One project can contain one {@link DataPackage}, many {@link Survey}s, many
 * {@link Instrument}s and {@link Question}s, and many {@link DataSet}s and {@link Variable}s. A
 * project can be currently released (visible to public users) or not. When a publisher releases a
 * project and its version is greater than or equal to 1.0.0 then the metadata is published to
 * <a href="https://www.da-ra.de/home/">da|ra</a>.
 */
@Entity
@Document(collection = "data_acquisition_projects")
@SetHasBeenReleasedBeforeOnlyOnce(message = "data-acquisition-project-management."
    + "error.data-acquisition-project."
    + "has-been-released-before.set-has-been-released-before-only-once")
@ValidSemanticVersion(
    message = "data-acquisition-project-management.error.release."
        + "version.not-parsable-or-not-incremented")
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValidShadowId(message = "data-acquisition-project-management.error.data-acquisition-project."
    + "id.pattern")
public class DataAcquisitionProject extends AbstractShadowableRdcDomainObject
    implements Serializable {

  private static final long serialVersionUID = 1549622375585915772L;

  /**
   * The id of this project. Must not be empty
   */
  @Id
  @NotEmpty(message = "data-acquisition-project-management.error."
      + "data-acquisition-project.id.not-empty")
  @Setter(AccessLevel.NONE)
  private String id;

  /**
   * The master id of this project.
   * Must not be empty, must only contain lower cased (english) letters and numbers and must not
   * contain more than 32 characters.
   */
  @NotEmpty(message = "data-acquisition-project-management.error."
      + "data-acquisition-project.master-id.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "data-acquisition-project-management.error.data-acquisition-project.master-id.size")
  @Pattern(regexp = Patterns.ALPHANUMERIC, message = "data-acquisition-project-management."
      + "error.data-acquisition-project.master-id.pattern")
  @Setter(AccessLevel.NONE)
  private String masterId;

  /**
   * Flag indicating whether this project has ever been released in its life. It is used to ensure
   * that project cannot be deleted once they have been released.
   */
  @NotNull(message = "data-acquisition-project-management.error.data-acquisition-project."
      + "has-been-released-before.not-null")
  private Boolean hasBeenReleasedBefore;

  /**
   * Flag indicating whether this project has remarks for the user service. Can only be true
   * for data packages.
   */
  @NotNull(message = "data-acquisition-project-management.error.data-acquisition-project"
      + ".has-user-service-remarks.not-null")
  private Boolean hasUserServiceRemarks = false;

  /**
   * Flag indicating whether this project includes an external data package. Can only be true
   * for data packages.
   */
  @NotNull(message = "data-acquisition-project-management.error.data-acquisition-project"
      + ".is-external.not-null")
  private Boolean isExternalDataPackage = false;

  /**
   * Flag indicating whether this project is transmitted via VerbundFDB. Can only be true
   * for data packages.
   */
  @NotNull(message = "data-acquisition-project-management.error.data-acquisition-project"
      + ".is-tansmitted-via-verbundfdb.not-null")
  private Boolean isTransmittedViaVerbundFdb = false;

  /**
   * Attribute which holds information on the planned release date.
   */
  private LocalDate embargoDate;

  /**
   * A valid {@link Release} object.
   *
   * Null if the project is currently not released. The version of a {@link Release} must be a
   * syntactically correct according to semver (major.minor.patch) and must not be decreased.
   */
  @Valid
  private Release release;

  /**
   * Contains the project configuration.
   */
  @Valid
  @NotNull(message = "data-acquisition-project-management.error.data-acquisition-project"
      + ".configuration.not-null")
  @Builder.Default
  private Configuration configuration = new Configuration();

  /**
   * Determines which assignee group is able to edit data on the project.
   */
  @NotNull(message = "data-acquisition-project-management.error.data-acquisition-project"
      + ".assignee-group.not-null")
  private AssigneeGroup assigneeGroup;


  /**
   * The last message provided by an assignee group user before
   * {@link DataAcquisitionProject#assigneeGroup} value changed.
   */
  @Size(max = StringLengths.LARGE, message = "data-acquisition-project-management.error."
      + "data-acquisition-project.last-assignee-group-message.size")
  private String lastAssigneeGroupMessage;

  public DataAcquisitionProject(DataAcquisitionProject dataAcquisitionProject) {
    BeanUtils.copyProperties(dataAcquisitionProject, this);
  }

  @Override
  protected void setMasterIdInternal(String masterId) {
    this.masterId = masterId;
  }

  @Override
  protected void setIdInternal(String id) {
    this.id = id;
  }
}
