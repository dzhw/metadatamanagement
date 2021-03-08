package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.io.Serializable;

import javax.validation.constraints.Size;

import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Descriptive metrics of this {@link Variable}.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Statistics implements Serializable {

  private static final long serialVersionUID = 3546740788424870277L;

  /**
   * The arithmetic mean of the values ({@link ValidResponse}s) of this {@link Variable}.
   */
  private Double meanValue;

  /**
   * The minimum of the values ({@link ValidResponse}s) of this {@link Variable}.
   * 
   * Must not contain more than 32 characters.
   */
  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.minimum.size")
  private String minimum;

  /**
   * The maximum of the values ({@link ValidResponse}s) of this {@link Variable}.
   * 
   * Must not contain more than 32 characters.
   */
  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.maximum.size")
  private String maximum;

  /**
   * See <a href="https://en.wikipedia.org/wiki/Skewness">Skewness (Wikipedia)</a>.
   */
  private Double skewness;

  /**
   * See <a href="https://en.wikipedia.org/wiki/Kurtosis">Kurtosis (Wikipedia)</a>.
   */
  private Double kurtosis;

  /**
   * The median is the value separating the higher half from the lower half of the values
   * ({@link ValidResponse}s) of this {@link Variable}.
   * 
   * Must not contain more than 32 characters.
   */
  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.median.size")
  private String median;

  /**
   * Measure that is used to quantify the amount of variation of the values ({@link ValidResponse}s)
   * of this {@link Variable}.
   */
  private Double standardDeviation;

  /**
   * Splits off the lowest 25% of the values ({@link ValidResponse}s) of this {@link Variable} from
   * the highest 75%.
   * 
   * Must not contain more than 32 characters.
   */
  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.first-quartile.size")
  private String firstQuartile;

  /**
   * Splits off the highest 25% of the values ({@link ValidResponse}s) of this {@link Variable} from
   * the lowest 75%.
   * 
   * Must not contain more than 32 characters.
   */
  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.third-quartile.size")
  private String thirdQuartile;

  /**
   * The mode is the value ({@link ValidResponse}) that appears most often.
   */
  private String mode;

  /**
   * See <a href="https://en.wikipedia.org/wiki/Deviance_(statistics)">Deviance (Wikipedia)</a>.
   */
  private Double deviance;

  /**
   * See <a href="https://en.wikipedia.org/wiki/Average_absolute_deviation">Mean Absolute Deviation
   * (Wikipedia)</a>.
   */
  private Double meanDeviation;
}
