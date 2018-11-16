package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation.SetHasBeenReleasedBeforeOnlyOnce;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation.ValidSemanticVersion;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The data acquisition project collects the metadata for the data products which are published by
 * our RDC. One project can contain one {@link Study}, many {@link Survey}s, many
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
public class DataAcquisitionProject extends AbstractRdcDomainObject {

  /**
   * The id of this project.
   * 
   * Must not be empty and must only contain lower cased (english) letters and numbers. Must not
   * contain more than 32 characters.
   */
  @Id
  @NotEmpty(message = "data-acquisition-project-management.error."
      + "data-acquisition-project.id.not-empty")
  @Pattern(regexp = "^[a-z0-9]*$",
      message = "data-acquisition-project-management.error.data-acquisition-project.id.pattern")
  @Size(max = StringLengths.SMALL,
      message = "data-acquisition-project-management.error.data-acquisition-project.id.size")
  private String id;

  /**
   * Flag indicating whether this project has ever been released in its life. It is used to ensure
   * that project cannot be deleted once they have been released.
   */
  @NotNull(message = "data-acquisition-project-management.error.data-acquisition-project."
      + "has-been-released-before.not-null")
  private Boolean hasBeenReleasedBefore;

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
  @NotNull(message ="data-acquisition-project-management.error.data-acquisition-project.not-null")
  private Configuration configuration;
}
