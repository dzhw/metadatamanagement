package eu.dzhw.fdz.metadatamanagement.common.config;

import java.lang.management.ManagementFactory;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.ryantenney.metrics.spring.config.annotation.DelegatingMetricsConfiguration;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurer;

/**
 * Attach spring-metrics and custom jvm metrics to spring boot actuator metrics endpoint.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Configuration
public class MetricsConfiguration extends DelegatingMetricsConfiguration 
    implements ServletContextInitializer {
  private static final String PROP_METRIC_REG_JVM_MEMORY = "jvm.memory";
  private static final String PROP_METRIC_REG_JVM_GARBAGE = "jvm.garbage";
  private static final String PROP_METRIC_REG_JVM_THREADS = "jvm.threads";
  private static final String PROP_METRIC_REG_JVM_FILES = "jvm.files";
  private static final String PROP_METRIC_REG_JVM_BUFFERS = "jvm.buffers";

  @Autowired
  private MetricRegistry metricRegistry;
  
  @Override
  @Autowired(required = false)
    public void setMetricsConfigurers(final List<MetricsConfigurer> configurers) {
  }
  
  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    metricRegistry.register(PROP_METRIC_REG_JVM_MEMORY, new MemoryUsageGaugeSet());
    metricRegistry.register(PROP_METRIC_REG_JVM_GARBAGE, new GarbageCollectorMetricSet());
    metricRegistry.register(PROP_METRIC_REG_JVM_THREADS, new ThreadStatesGaugeSet());
    metricRegistry.register(PROP_METRIC_REG_JVM_FILES, new FileDescriptorRatioGauge());
    metricRegistry.register(PROP_METRIC_REG_JVM_BUFFERS,
        new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
    
    servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, metricRegistry);

    FilterRegistration.Dynamic metricsFilter =
        servletContext.addFilter("webappMetricsFilter", new InstrumentedFilter());

    metricsFilter.addMappingForUrlPatterns(
        EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC), true,
        "/*");
    metricsFilter.setAsyncSupported(true);
  }
}
