package com.free.map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class DeliveryZonesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryZonesApplication.class, args);
	}

}
