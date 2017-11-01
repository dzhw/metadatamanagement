package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The related Question includes the references to the question and instrument. This is a sub 
 * element by the variable.
 * 
 * @author Daniel Katzberg
 *
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class RelatedQuestion {

  /* Domain Object listed attributes */
  @NotEmpty(message = "variable-management.error.variable."
      + "related-question-instrument-number-not-empty")
  private String instrumentNumber;
  
  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.variable."
      + "related-question-number-size")
  @NotEmpty(message = "variable-management.error.variable."
      + "related-question-number-not-empty")
  private String questionNumber;
  
  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.variable.related-question-strings.i18n-string-size")
  private I18nString relatedQuestionStrings;
  
  /* Nested Objects */
  
  /* Foreign Keys */
  @Indexed
  private String questionId;
  
  @Indexed
  private String instrumentId;
}
