package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.io.Serializable;

import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A representation of a sponsor.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class Sponsor implements Serializable {

  private static final long serialVersionUID = -2205399590286037466L;

  /**
   * The name of the sponsor which has sponsored the study or project from which this data
   * package results.
   *
   * It must be specified in German and English and it must not contain more than 512 characters.
   */
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "data-package-management.error.data-package.sponsor.i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "data-package-management.error.data-package.sponsor.i18n-string-size")
  private I18nString name;

  /**
   * The funding reference number.
   * 
   * May be a number or a combination of numbers, letters and/or special characters.
   */
  private String fundingRef;
}