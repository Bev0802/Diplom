package com.bev0802.salesaccounting.productdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductDbApplication.class, args);
	}

}
