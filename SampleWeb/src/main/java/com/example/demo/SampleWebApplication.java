package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@MapperScan("com.example.demo.repository.mybatis") // MyBatisリポジトリ用のスキャン
@EnableJpaRepositories("com.example.demo.repository.jpa") // JPAリポジトリ用のスキャン
public class SampleWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleWebApplication.class, args);
	}

	
}
