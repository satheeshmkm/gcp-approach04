package com.sck.gcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.sck" })
public class GcpApproach04Application {

	public static void main(String[] args) {
		SpringApplication.run(GcpApproach04Application.class, args);
	}
}
