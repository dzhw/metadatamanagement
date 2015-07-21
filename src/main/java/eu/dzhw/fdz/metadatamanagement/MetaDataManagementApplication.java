package eu.dzhw.fdz.metadatamanagement;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MetaDataManagementApplication {

    public static void main(String[] args) {
    	System.setProperty("info.app.startuptime", new Date().toString());
        SpringApplication.run(MetaDataManagementApplication.class, args);
    }
}
