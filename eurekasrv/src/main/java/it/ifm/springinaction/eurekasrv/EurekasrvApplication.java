package it.ifm.springinaction.eurekasrv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekasrvApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekasrvApplication.class, args);
	}

}
