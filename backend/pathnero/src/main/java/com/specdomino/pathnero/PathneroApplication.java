package com.specdomino.pathnero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.specdomino.pathnero"})
public class PathneroApplication {

	public static void main(String[] args) {
		SpringApplication.run(PathneroApplication.class, args);
	}

}
