package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.DataTypes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Partial {@link eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage} which is
 * part of a {@link Product}. It is a copy of the
 * {@link eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage} attributes which
 * is made when the customer places the orders.
 */
@Data
@Schema(
    description = "Go <a href='https://dzhw.github.io/metadatamanagement/"
        + "eu/dzhw/fdz/metadatamanagement/ordermanagement/domain/OrderedDataPackage.html'>"
        + "here</a> for further details.")
public class OrderedDataPackage implements Serializable {

  private static final long serialVersionUID = 5959657930630887807L;

  /**
   * The id of the {@link eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage}.
   *
   * Must not be empty.
   */
  @NotEmpty
  private String id;

  /**
   * The title of the
   * {@link eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage}.
   *
   * Must not be empty neither in German nor in English.
   */
  @NotNull
  @I18nStringEntireNotEmpty
  private I18nString title;

  /**
   * The annotations of the
   * {@link eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage}.
   */
  private I18nString annotations;

  /**
   * List of {@link DataTypes}.
   *
   * Must not be empty.
   */
  @NotEmpty
  private List<I18nString> surveyDataTypes;

  /**
   * The name of the series of dataPackages to which this dataPackage belongs. May be null.
   */
  private I18nString studySeries;

  /**
   * Remarks for User Service.
   */
  private String remarksUserService;
}
