package eu.dzhw.fdz.metadatamanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.config.java.ServiceConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;


@Configuration
@Profile(Constants.SPRING_PROFILE_CLOUD)
public class CloudDatabaseConfiguration extends AbstractCloudConfig {

    private final Logger log = LoggerFactory.getLogger(CloudDatabaseConfiguration.class);

    @Bean
    public MongoDbFactory mongoDbFactory() {
        log.info("Configuring MongoDB datasource from a cloud provider");
        
        ServiceConnectionFactory connectionFactory = connectionFactory();
        this.log.info("CloudDatabaseConfiguration.mongoDbFactory FINDME: connectionFactory" + connectionFactory);
        
        MongoDbFactory dbFactory = connectionFactory().mongoDbFactory();        
        this.log.info("CloudDatabaseConfiguration.mongoDbFactory FINDME: Factory" + dbFactory);
        
        return dbFactory;
    }
}
