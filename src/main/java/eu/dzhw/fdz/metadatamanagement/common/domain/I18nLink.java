package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;
import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * URLs with display text in English and German.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class I18nLink implements Serializable {
  private static final long serialVersionUID = 2944876105515200207L;

  /**
   * The URL of the link.
   * 
   * Must not be longer than 2000 characters, must not be empty and must be a valid url.
   */
  @URL(message = "global.error.i18nlink.url.invalid")
  @Size(max = 2000, message = "global.error.i18nlink.url.length")
  @NotEmpty(message = "global.error.i18nlink.url.not-empty")
  private String url;

  /**
   * An optional {@link I18nString} for displaying the link.
   * 
   * Must not be longer than 512 characters.
   */
  @I18nStringSize(max = StringLengths.MEDIUM)
  private I18nString displayText;
}
