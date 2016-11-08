/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;

/**
 * @author Daniel Katzberg
 *
 */
public class StatisticsTest {

  // @Test
  public void testGetterSetter() {
    // Arrange
    Statistics statistics = UnitTestCreateDomainObjectUtils.buildStatistics();

    // Act

    // Assert
    assertThat(statistics.getFirstQuartile(), is(70.0));
    assertThat(statistics.getHighWhisker(), is(130.0));
    assertThat(statistics.getKurtosis(), is(234.0));
    assertThat(statistics.getLowWhisker(), is(30.0));
    assertThat(statistics.getMaximum(), is(140.0));
    assertThat(statistics.getMeanValue(), is(87.5));
    assertThat(statistics.getMinimum(), is(0.0));
    assertThat(statistics.getSkewness(), is(123.0));
    assertThat(statistics.getStandardDeviation(), is(40.0));
    assertThat(statistics.getThirdQuartile(), is(110.0));

  }


  @Test
  public void testToString() {
    // Arrange
    Statistics statistics = UnitTestCreateDomainObjectUtils.buildStatistics();

    // Act
    String toString = statistics.toString();

    // Assert
    assertThat(toString, is(
        "Statistics{meanValue=87.5, minimum=0.0, maximum=140.0, skewness=123.0, kurtosis=234.0, median=90.0, standardDeviation=40.0, firstQuartile=70.0, thirdQuartile=110.0, lowWhisker=30.0, highWhisker=130.0, mode=Mode, deviance=12.4, meanDeviation=58.7}"));

  }

}
