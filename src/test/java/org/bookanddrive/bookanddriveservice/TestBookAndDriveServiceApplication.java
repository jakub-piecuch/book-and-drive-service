package org.bookanddrive.bookanddriveservice;

import org.springframework.boot.SpringApplication;

public class TestBookAndDriveServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(BookAndDriveServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
