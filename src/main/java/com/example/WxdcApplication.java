package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class WxdcApplication {
	/**
	 * springboot启动类
	 * @param args
	 */
	public static void main(String[] args) {

		SpringApplication.run(WxdcApplication.class, args);
	}

}
