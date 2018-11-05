package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.mongodb.core.index.Indexed;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A related question is a {@link Question} which has been asked to generate the values of a
 * {@link Variable}. It contains the ids of the {@link Instrument} and the {@link Question} as well
 * as all Strings of the {@link Question} which are related to this {@link Variable}.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class RelatedQuestion {

  /**
   * The number of the {@link Instrument} of this {@link Question}.
   * 
   * Must not be empty.
   */
  @NotEmpty(message = "variable-management.error.variable."
      + "related-question-instrument-number-not-empty")
  private String instrumentNumber;

  /**
   * The number of the corresponding {@link Question}.
   * 
   * Must not be empty.
   */
  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.variable."
      + "related-question-number-size")
  @NotEmpty(message = "variable-management.error.variable."
      + "related-question-number-not-empty")
  private String questionNumber;

  /**
   * All Strings (concatenated) of this {@link Question} which "belong" to this {@link Variable}.
   * These Strings typically overlap with String from other {@link Variable}s of the same
   * {@link Question}.
   */
  @I18nStringSize(max = StringLengths.X_LARGE,
      message = "variable-management.error.variable.related-question-strings.i18n-string-size")
  private I18nString relatedQuestionStrings;

  /**
   * The id of the corresponding {@link Question}.
   * 
   * Must not be empty.
   */
  @Indexed
  private String questionId;

  /**
   * The id of the {@link Instrument} of this {@link Question}.
   * 
   * Must not be empty.
   */
  @Indexed
  private String instrumentId;
}
