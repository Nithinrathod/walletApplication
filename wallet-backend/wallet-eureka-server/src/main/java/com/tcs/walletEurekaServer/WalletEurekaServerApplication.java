package com.tcs.walletEurekaServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class WalletEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletEurekaServerApplication.class, args);
	}

}
