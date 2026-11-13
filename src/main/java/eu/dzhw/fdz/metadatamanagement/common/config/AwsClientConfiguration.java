package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ecs.EcsClient;

/**
 * Configure the AWS API Clients.
 */
@Configuration
@Profile({Constants.SPRING_PROFILE_PROD, Constants.SPRING_PROFILE_TEST,
    Constants.SPRING_PROFILE_DEVELOPMENT})

public class AwsClientConfiguration {
  @Bean
  EcsClient ecsClient() {
    return EcsClient.builder().region(Region.EU_CENTRAL_1).build();
  }
}
