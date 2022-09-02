package eu.dzhw.fdz.metadatamanagement.common.config.audit;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.ThreadLocalTargetSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * Configuration which handles setting up the AuditorStore which will be used by
 * {@link AuditorStoreAware}.
 */
@Configuration
public class AuditorConfig {

  /**
   * Create the {@link ThreadLocalTargetSource} which will store the {@link AuditorStore} Bean.
   *
   * @return the Object which handles storing a unique {@link AuditorStore} for each thread
   */
  @Bean(destroyMethod = "destroy")
  public ThreadLocalTargetSource auditTargetSource() {
    ThreadLocalTargetSource targetSource = new ThreadLocalTargetSource();
    targetSource.setTargetBeanName("auditorStore");

    return targetSource;
  }

  /**
   * A ProxyFactory which handles creating the proxy for the {@link ThreadLocalTargetSource}
   * which holds each requests specific {@link AuditorStore}.
   *
   * @param targetSource the holder of the thread specific {@link AuditorStore}
   * @return the proxy factory which makes it possible to treat the {@link ThreadLocalTargetSource}
   *         as a @Bean
   */
  @Primary
  @Bean(name = "proxiedThreadLocalTargetSource")
  public ProxyFactoryBean proxiedThreadLocalTargetSource(ThreadLocalTargetSource targetSource) {
    ProxyFactoryBean proxyFactory = new ProxyFactoryBean();
    proxyFactory.setTargetSource(targetSource);

    return proxyFactory;
  }

  /**
   * Create an {@link AuditorStore} prototype @Bean.
   *
   * @return the store which stores the auditor info for each individual session
   */
  @Bean(name = "auditorStore")
  @Scope(scopeName = "prototype")
  public AuditorStore auditorStore() {
    return new AuditorStore();
  }
}
