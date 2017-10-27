package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representation of data for a histogram.
 * 
 * @author Daniel Katzberg
 *
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Histogram {

  private Integer numberOfBins;

  private Double start;

  private Double end;
}
