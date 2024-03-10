package com.bev0802.salesaccounting.wholesale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class WholeSaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(WholeSaleApplication.class, args);
	}

}
