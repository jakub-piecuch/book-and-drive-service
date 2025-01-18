package org.redcode.bookanddriveservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaRepositories
public class BookAndDriveServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookAndDriveServiceApplication.class, args);
    }

}
