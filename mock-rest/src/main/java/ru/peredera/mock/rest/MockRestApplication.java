package ru.peredera.mock.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(
		basePackages = {"ru.peredera.mock"}
)
@SpringBootApplication
public class MockRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockRestApplication.class, args);
	}

}
