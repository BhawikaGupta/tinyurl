package com.tiny.url;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.tiny.url.configuration.ApplicationConfiguration.dropExisitingTables;

@SpringBootApplication
public class TinyUrlApplication {

	public static void main(String[] args) {
		dropExisitingTables();
		SpringApplication.run(TinyUrlApplication.class, args);
	}

}
