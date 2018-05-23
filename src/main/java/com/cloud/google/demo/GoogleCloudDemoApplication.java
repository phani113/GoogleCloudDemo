package com.cloud.google.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@EnableDiscoveryClient
//@EnableFeignClients
public class GoogleCloudDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoogleCloudDemoApplication.class, args);
	}
}
