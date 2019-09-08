package com.gjq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gjq.mapper")//@MapperScan用于扫描Mybatis的mapper接口
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
