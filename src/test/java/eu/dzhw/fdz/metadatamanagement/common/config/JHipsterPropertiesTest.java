/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties.Http;
import eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties.Http.Cache;
import eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties.Mail;
import eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties.Metrics.Graphite;
import eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties.Metrics.Spark;

/**
 * @author Daniel Katzberg
 *
 */
public class JHipsterPropertiesTest {

  @Test
  public void testHttp() {
    // Arrange
    JHipsterProperties jHipsterProperties = new JHipsterProperties();

    // Act
    Http http = jHipsterProperties.getHttp();
    Cache httpCache = http.getCache();
    httpCache.setTimeToLiveInDays(1);

    // Assert
    assertThat(http, not(nullValue()));
    assertThat(httpCache, not(nullValue()));
    assertThat(httpCache.getTimeToLiveInDays(), is(1));
  }

  @Test
  public void testCache() {
    // Arrange
    JHipsterProperties jHipsterProperties = new JHipsterProperties();

    // Act
    eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties.Cache cache =
        jHipsterProperties.getCache();
    cache.setTimeToLiveSeconds(100);

    // Assert
    assertThat(cache, not(nullValue()));
    assertThat(cache.getTimeToLiveSeconds(), is(100));
  }

  @Test
  public void testMail() {
    // Arrange
    JHipsterProperties jHipsterProperties = new JHipsterProperties();

    // Act
    Mail mail = jHipsterProperties.getMail();
    mail.setFrom("from@from.from");

    // Assert
    assertThat(mail, not(nullValue()));
    assertThat(mail.getFrom(), is("from@from.from"));
  }

  @Test
  public void testMetrics() {
    // Arrange
    JHipsterProperties jHipsterProperties = new JHipsterProperties();

    // Act
    Spark spark = jHipsterProperties.getMetrics()
      .getSpark();
    spark.setHost("localhostTest");
    spark.setPort(8018);
    spark.setEnabled(false);
    Graphite graphite = new JHipsterProperties().getMetrics()
      .getGraphite();
    graphite.setEnabled(false);
    graphite.setHost("localhostTest2");
    graphite.setPort(1234);
    graphite.setPrefix("preGraphite");

    // Assert
    assertThat(spark, not(nullValue()));
    assertThat(spark.getHost(), is("localhostTest"));
    assertThat(spark.getPort(), is(8018));
    assertThat(spark.isEnabled(), is(false));
    assertThat(graphite, not(nullValue()));
    assertThat(graphite.isEnabled(), is(false));
    assertThat(graphite.getPort(), is(1234));
    assertThat(graphite.getHost(), is("localhostTest2"));
    assertThat(graphite.getPrefix(), is("preGraphite"));
  }
}
