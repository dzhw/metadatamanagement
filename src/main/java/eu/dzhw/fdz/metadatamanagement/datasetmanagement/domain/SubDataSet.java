package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.ValidAccessWay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain object of the SubDataSet.
 *
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class SubDataSet {
  
  @NotEmpty(message = "data-set-management.error.sub-data-set.name.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "data-set-management.error.sub-data-set.name.size")
  private String name;
  
  private int numberOfObservations;  
  
  @NotNull(message = "data-set-management.error.sub-data-set.access-way.not-null")
  @ValidAccessWay(
      message = "data-set-management.error.sub-data-set.access-way.valid-access-way")
  private String accessWay;
  
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "data-set-management.error.sub-data-set.description.i18n-string-size")
  @I18nStringNotEmpty(
      message = "data-set-management.error.sub-data-set.description.i18n-string-not-empty")
  private I18nString description;
  
  @I18nStringSize(max = StringLengths.LARGE,
      message = "data-set-management.error.sub-data-set.citation-hint.i18n-string-size")
  private I18nString citationHint;
}
