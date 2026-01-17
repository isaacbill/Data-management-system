package com.student.data.management.system;

import com.student.data.management.system.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class DataManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataManagementSystemApplication.class, args);
	}
}
