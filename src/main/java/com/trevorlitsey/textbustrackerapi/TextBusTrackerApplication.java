package com.trevorlitsey.textbustrackerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableMongoAuditing
@SpringBootApplication
@EnableSwagger2
public class TextBusTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TextBusTrackerApplication.class, args);
	}

}
