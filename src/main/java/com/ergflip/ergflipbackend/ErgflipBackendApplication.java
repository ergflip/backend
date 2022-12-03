package com.ergflip.ergflipbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.FileNotFoundException;

@SpringBootApplication
public class ErgflipBackendApplication {

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(ErgflipBackendApplication.class, args);
	}

}
