package com.activity.tracker.event_producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EventProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventProducerApplication.class, args);
	}

}
