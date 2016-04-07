package eu.dzhw.fdz.metadatamanagement.common.config;

import java.lang.management.ManagementFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

/**
 * Configuration for service metrics.
 */
@Configuration
@EnableMetrics(proxyTargetClass = true)
public class MetricsConfiguration extends MetricsConfigurerAdapter {

  private static final String PROP_METRIC_REG_JVM_MEMORY = "jvm.memory";
  private static final String PROP_METRIC_REG_JVM_GARBAGE = "jvm.garbage";
  private static final String PROP_METRIC_REG_JVM_THREADS = "jvm.threads";
  private static final String PROP_METRIC_REG_JVM_FILES = "jvm.files";
  private static final String PROP_METRIC_REG_JVM_BUFFERS = "jvm.buffers";

  private final Logger log = LoggerFactory.getLogger(MetricsConfiguration.class);

  private MetricRegistry metricRegistry = new MetricRegistry();

  private HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

  @Inject
  private JHipsterProperties jhipsterProperties;

  @Override
  @Bean
  public MetricRegistry getMetricRegistry() {
    return metricRegistry;
  }

  @Override
  @Bean
  public HealthCheckRegistry getHealthCheckRegistry() {
    return healthCheckRegistry;
  }

  /**
   * Register a couple of {@link Gauge}s.
   */
  @PostConstruct
  public void init() {
    log.debug("Registering JVM gauges");
    metricRegistry.register(PROP_METRIC_REG_JVM_MEMORY, new MemoryUsageGaugeSet());
    metricRegistry.register(PROP_METRIC_REG_JVM_GARBAGE, new GarbageCollectorMetricSet());
    metricRegistry.register(PROP_METRIC_REG_JVM_THREADS, new ThreadStatesGaugeSet());
    metricRegistry.register(PROP_METRIC_REG_JVM_FILES, new FileDescriptorRatioGauge());
    metricRegistry.register(PROP_METRIC_REG_JVM_BUFFERS,
        new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
    if (jhipsterProperties.getMetrics()
        .getJmx()
        .isEnabled()) {
      log.debug("Initializing Metrics JMX reporting");
      JmxReporter jmxReporter = JmxReporter.forRegistry(metricRegistry)
          .build();
      jmxReporter.start();
    }
  }
}
