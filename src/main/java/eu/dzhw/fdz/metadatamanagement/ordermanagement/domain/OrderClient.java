package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum holding possible clients with write access to the orders.
 */
@Schema(
    description = "Go <a href='https://dzhw.github.io/metadatamanagement/"
    + "eu/dzhw/fdz/metadatamanagement/ordermanagement/domain/OrderClient.html'>here</a>"
    + " for further details.")
public enum OrderClient {
  DLP, MDM
}
