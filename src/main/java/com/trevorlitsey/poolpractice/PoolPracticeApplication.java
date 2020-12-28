package com.trevorlitsey.poolpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableMongoAuditing
@SpringBootApplication
@EnableSwagger2
public class PoolPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoolPracticeApplication.class, args);
	}

}
