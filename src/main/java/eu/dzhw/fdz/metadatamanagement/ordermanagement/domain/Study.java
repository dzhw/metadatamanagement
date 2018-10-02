package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import lombok.Data;

/**
 * Study which is part of a {@link Product}.
 * 
 * @author René Reitmann
 */
@Data
public class Study {
  @NotEmpty
  private String id;

  @NotNull
  @I18nStringEntireNotEmpty
  private I18nString title;

  private I18nString annotations;
}
