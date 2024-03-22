package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Partial {@link AnalysisPackage} which is
 * part of a {@link Product}. It is a copy of the
 * {@link AnalysisPackage} attributes which
 * is made when the customer places the orders.
 */
@Data
@Schema(
    description = "Go <a href='https://dzhw.github.io/metadatamanagement/"
        + "eu/dzhw/fdz/metadatamanagement/ordermanagement/domain/OrderedAnalysisPackage.html'>"
        + "here</a> for further details.")
public class OrderedAnalysisPackage implements Serializable {
  private static final long serialVersionUID = 7779148758852222375L;

  /**
   * The id of the {@link AnalysisPackage}.
   *
   * Must not be empty.
   */
  @NotEmpty
  private String id;

  /**
   * The title of the {@link AnalysisPackage}.
   *
   * Must not be empty neither in German nor in English.
   */
  @NotNull
  @I18nStringEntireNotEmpty
  private I18nString title;

  /**
   * The annotations of the {@link AnalysisPackage}.
   */
  private I18nString annotations;

  /**
   * The DOI of the {@link AnalysisPackage}.
   */
  private String doi;
}
