package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.Size;

import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This statics class has statistical information about a variable. The information of this class
 * will be represented for a boxplot on the frontend.
 * 
 * @author Daniel Katzberg
 *
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Statistics {

  private Double meanValue;

  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.minimum.size")
  private String minimum;

  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.maximum.size")
  private String maximum;

  private Double skewness;

  private Double kurtosis;

  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.median.size")
  private String median;

  private Double standardDeviation;

  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.first-quartile.size")
  private String firstQuartile;

  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.third-quartile.size")
  private String thirdQuartile;

  private Double lowWhisker;

  private Double highWhisker;
  
  private String mode;
  
  private Double deviance;
 
  private Double meanDeviation;
}
