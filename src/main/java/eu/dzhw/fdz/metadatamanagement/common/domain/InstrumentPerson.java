package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javers.core.metamodel.annotation.ValueObject;

/**
 * A representation of a person (optional author) of instrument attachment types 'questionnaire' and
 * 'variable questionnaire'.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class InstrumentPerson implements Serializable {

  private static final long serialVersionUID = 1594224585287945999L;

  /**
   * The first name of the person.
   *
   * Must not be empty.
   */
  private String firstName;

  /**
   * The middle name of the person.
   */
  private String middleName;

  /**
   * The last name of the person.
   */
  private String lastName;

  /**
   * The ORCID of the person.
   *
   * If present must comply to the pattern: xxxx-xxxx-xxxx-xxxx
   */
  @Pattern(regexp = "^\\d{4}-\\d{4}-\\d{4}-(\\d{3}X|\\d{4})$",
      message = "global.error.person.orcid.pattern")
  private String orcid;
}
