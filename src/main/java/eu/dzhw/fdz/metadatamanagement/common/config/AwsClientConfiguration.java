package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsync;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClientBuilder;
import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.AmazonECSClientBuilder;

/**
 * Configure the AWS API Clients.
 */
@Configuration
@Profile({Constants.SPRING_PROFILE_PROD, Constants.SPRING_PROFILE_TEST,
    Constants.SPRING_PROFILE_DEVELOPMENT})
public class AwsClientConfiguration {
  @Bean
  AmazonECS amazonEcs() {
    return AmazonECSClientBuilder.defaultClient();
  }

  @Bean
  AmazonCloudWatchAsync cloudWatchCLient() {
    return AmazonCloudWatchAsyncClientBuilder.defaultClient();
  }
}
