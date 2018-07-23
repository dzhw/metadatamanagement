package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The release includes all additional information for a release of a data acquisition project. It
 * is not a regular domain object with a own id, because it is additional for the Data Aquisition
 * Project.
 *
 * @author Daniel Katzberg
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Release {

  @NotEmpty(message = "data-acquisition-project-management."
      + "error.release.version.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "data-acquisition-project-management.error.release.version.size")
  @Pattern(regexp = Patterns.SEMVER,
      message = "data-acquisition-project-management.error.release.version.pattern")
  private String version;

  @NotNull(message = "data-acquisition-project-management.error.release.date.not-null")
  private LocalDateTime date;
}
