package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Technical Representation of a Question. It contains the type, 
 * the language (e.g. XML) and the Source itself.
 * @author Daniel Katzberg
 *
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class TechnicalRepresentation {
  
  @NotEmpty(message = "question-management.error.technical-representation.type.not-empty")
  @Size(max = StringLengths.SMALL, 
      message = "question-management.error.technical-representation.type.size")
  private String type;
  
  @NotEmpty(message = "question-management.error.technical-representation.language.not-empty")
  @Size(max = StringLengths.SMALL, 
      message = "question-management.error.technical-representation.language.size")
  private String language;
  
  @NotEmpty(message = "question-management.error.technical-representation.source.not-empty")
  @Size(max = StringLengths.X_LARGE, 
      message = "question-management.error.technical-representation.source.size")
  private String source;
}
