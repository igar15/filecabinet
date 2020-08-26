package com.igar15.filecabinet;

import com.igar15.filecabinet.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilecabinetApplication {

	public static void main(String[] args) {
		SpringApplication.run(new Class[] { FilecabinetApplication.class, SecurityConfig.class }, args);
	}

}
