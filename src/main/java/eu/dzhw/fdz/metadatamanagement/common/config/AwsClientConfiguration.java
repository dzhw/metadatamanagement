package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.AmazonECSClientBuilder;

/**
 * Configure the AWS API Clients.
 */
@Configuration
public class AwsClientConfiguration {
  @Bean
  @Profile({Constants.SPRING_PROFILE_PROD, Constants.SPRING_PROFILE_TEST,
      Constants.SPRING_PROFILE_DEVELOPMENT})
  AmazonECS amazonEcs() {
    return AmazonECSClientBuilder.defaultClient();
  }
}
