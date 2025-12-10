package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Additional details required by {@link DataPackageAttachmentMetadata}s of type "Method Report".
 *
 * @author René Reitmann
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class MethodReportCitationDetails implements Serializable {

  private static final long serialVersionUID = 2681357446514512384L;

  /**
   * The year in which the method report was published.
   *
   * Must not be empty and not before 1990.
   */
  @NotNull(
      message = "data-package-management.error.data-package-attachment-metadata."
          + "publication-year.not-null")
  @Min(value = 1990,
      message = "data-package-management.error.data-package-attachment-metadata."
          + "publication-year.min")
  @Max(value = 2050,
      message = "data-package-management.error.data-package-attachment-metadata."
          + "publication-year.max")
  private Integer publicationYear;

  /**
   * The location of the institution which created the method report.
   *
   * Must not be empty and not more than 512 characters.
   */
  @NotEmpty(
      message = "data-package-management.error.data-package-attachment-metadata."
          + "location.not-empty")
  @Size(max = StringLengths.MEDIUM,
      message = "data-package-management.error.data-package-attachment-metadata."
          + "location.string-size")
  private String location;

  /**
   * The institution which created the method report.
   *
   * Must not be empty and not more than 512 characters.
   */
  @NotEmpty(
      message = "data-package-management.error.data-package-attachment-metadata."
          + "institution.not-empty")
  @Size(max = StringLengths.MEDIUM,
      message = "data-package-management.error.data-package-attachment-metadata."
          + "institution.string-size")
  private String institution;

  /**
   * List of {@link Person}s which have authored this report.
   *
   * Must not be empty.
   */
  @Valid
  @NotEmpty(
      message = "data-package-management.error.data-package-attachment-metadata.authors.not-empty")
  private List<Person> authors;
}
