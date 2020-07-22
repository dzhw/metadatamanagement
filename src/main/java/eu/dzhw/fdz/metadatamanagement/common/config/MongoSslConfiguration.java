package eu.dzhw.fdz.metadatamanagement.common.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mongodb.MongoClientOptions;

/**
 * Configure MongoDB Client to use SSL in the cloud.
 * 
 * Get the required CA certificate from the environment.
 * 
 * @author René Reitmann
 */
@Configuration
@Profile({Constants.SPRING_PROFILE_DEVELOPMENT, Constants.SPRING_PROFILE_PROD,
    Constants.SPRING_PROFILE_TEST})
public class MongoSslConfiguration {
  @Value("${metadatamanagement.mongodb.ssl-ca-certificate}")
  private String sslCaCertificate;

  /**
   * Create a custom SSL Context for the MongoClient.
   * 
   * @return the {@link MongoClientOptions}
   * @throws Exception When configuring the SSL context fails
   */
  @Bean
  public MongoClientOptions mongoClientOptions() throws Exception {
    InputStream inputStream =
        new ByteArrayInputStream(sslCaCertificate.getBytes(StandardCharsets.UTF_8));
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    X509Certificate caCert = (X509Certificate) certificateFactory.generateCertificate(inputStream);

    TrustManagerFactory trustManagerFactory =
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null); // You don't need the KeyStore instance to come from a file.
    keyStore.setCertificateEntry("caCert", caCert);

    trustManagerFactory.init(keyStore);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

    MongoClientOptions.Builder mongoClientOptions =
        MongoClientOptions.builder().sslInvalidHostNameAllowed(true).sslEnabled(true);
    mongoClientOptions.sslContext(sslContext);
    mongoClientOptions.sslInvalidHostNameAllowed(true);

    return mongoClientOptions.build();
  }
}
