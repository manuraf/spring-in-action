package it.ifm.springinaction.confsrv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfsrvApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfsrvApplication.class, args);
	}

}
