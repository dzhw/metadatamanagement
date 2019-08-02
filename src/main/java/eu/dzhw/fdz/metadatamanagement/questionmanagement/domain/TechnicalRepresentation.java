package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The technical representation of a {@link Question} which was used to generate the question for
 * instance in an online {@link Instrument}.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class TechnicalRepresentation implements Serializable {

  private static final long serialVersionUID = 5361313820939313016L;

  /**
   * The type of the technical representation. E.g. "zofar".
   * 
   * Must not be empty and must not contain more than 32 characters.
   */
  @NotEmpty(message = "question-management.error.technical-representation.type.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "question-management.error.technical-representation.type.size")
  private String type;

  /**
   * The technical language of the source of this representation. E.g. "qml".
   * 
   * Must not be empty and must not contain more than 32 characters.
   */
  @NotEmpty(message = "question-management.error.technical-representation.language.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "question-management.error.technical-representation.language.size")
  private String language;

  /**
   * The source code of the question.
   * 
   * Must not be empty and must not contain more than 1 MB characters.
   */
  @NotEmpty(message = "question-management.error.technical-representation.source.not-empty")
  @Size(max = StringLengths.X_LARGE,
      message = "question-management.error.technical-representation.source.size")
  private String source;
}
