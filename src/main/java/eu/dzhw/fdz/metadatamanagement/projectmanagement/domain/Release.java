package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
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
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Release {
  
  @NotEmpty(message = "data-acquisition-project." 
      + "error.release.version.not-empty")
  @Size(max = StringLengths.SMALL, 
      message = "data-acquisition-project.error.release.version.size")
  @Pattern(regexp = Patterns.NUMERIC_WITH_DOT, 
      message = "data-acquisition-project.error.release.version.pattern")
  private String version;

  @NotNull(message = "data-acquisition-project.error.release.date.not-null")
  private LocalDateTime date;

  @I18nStringSize(max = StringLengths.LARGE, 
      message = "data-acquisition-project.error.release.notes.i18n-string-size")
  private I18nString notes;
}
