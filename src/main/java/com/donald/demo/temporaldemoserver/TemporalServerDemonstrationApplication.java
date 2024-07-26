package com.donald.demo.temporaldemoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.donald.demo.temporaldemoserver.namespace.model.CloudOperationsServerConfig;




@SpringBootApplication
public class TemporalServerDemonstrationApplication {
	


	public static void main(String[] args) {
		SpringApplication.run(TemporalServerDemonstrationApplication.class, args);
	}

}
