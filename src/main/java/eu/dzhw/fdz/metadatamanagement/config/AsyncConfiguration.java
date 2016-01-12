package eu.dzhw.fdz.metadatamanagement.config;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import eu.dzhw.fdz.metadatamanagement.async.ExceptionHandlingAsyncTaskExecutor;

/**
 * Configuration for {@link Async} and {@link Scheduled} tasks. *
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer {

  private final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

  @Inject
  private JHipsterProperties jhipsterProperties;

  @Override
  @Bean(name = "taskExecutor")
  public Executor getAsyncExecutor() {
    log.debug("Creating Async Task Executor");
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(jhipsterProperties.getAsync()
        .getCorePoolSize());
    executor.setMaxPoolSize(jhipsterProperties.getAsync()
        .getMaxPoolSize());
    executor.setQueueCapacity(jhipsterProperties.getAsync()
        .getQueueCapacity());
    executor.setThreadNamePrefix("metadatamanagement-Executor-");
    return new ExceptionHandlingAsyncTaskExecutor(executor);
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }
}
