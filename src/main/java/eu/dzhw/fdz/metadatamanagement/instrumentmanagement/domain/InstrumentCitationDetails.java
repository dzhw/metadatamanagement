package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;


import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import eu.dzhw.fdz.metadatamanagement.common.domain.InstrumentPerson;
import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Optional additional details for {@link InstrumentAttachmentMetadata} for types instrument types
 * 'Variable Questionnaire' and 'Questionnaire'.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class InstrumentCitationDetails implements Serializable {

  private static final long serialVersionUID = 2681357446514512384L;

  /**
   * The year in which the questionnaire/variable questionnaire was published.
   *
   * Must not be before 1990, and not after 2050.
   */
  @Min(value = 1990,
      message = "data-package-management.error.data-package-attachment-metadata."
      + "publication-year.min")
  @Max(value = 2050,
      message = "data-package-management.error.data-package-attachment-metadata."
      + "publication-year.max")
  private Integer publicationYear;

  /**
   * The location of the institution which created the questionnaire/variable questionnaire.
   *
   * Must not be more than 512 characters.
   */
  @Size(max = StringLengths.MEDIUM,
      message = "data-package-management.error.data-package-attachment-metadata."
      + "location.string-size")
  private String location;

  /**
   * The institution which created the questionnaire/variable questionnaire.
   *
   * Must not be more than 512 characters.
   */
  @Size(max = StringLengths.MEDIUM,
      message = "data-package-management.error.data-package-attachment-metadata."
      + "institution.string-size")
  private String institution;

  /**
   * List of {@link InstrumentPerson}s which have authored this questionnaire/variable questionnaire.
   */
  @Valid
  private List<InstrumentPerson> authors;

}
