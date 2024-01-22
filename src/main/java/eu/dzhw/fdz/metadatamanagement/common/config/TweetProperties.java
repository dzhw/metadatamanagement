package eu.dzhw.fdz.metadatamanagement.common.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Tweet Properties.
 */
@ConfigurationProperties(prefix = "tweet", ignoreUnknownFields = false)
@Getter
@Setter
public class TweetProperties {

  private String consumerkey;
  private String consumersecret;
  private String oauthtokensecret;
  private String oauthtoken;
  private String endpointurl;
  private String mediaendpointurl;
  private String imagepath;

}
