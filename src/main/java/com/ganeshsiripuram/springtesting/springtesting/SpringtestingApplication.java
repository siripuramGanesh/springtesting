package com.ganeshsiripuram.springtesting.springtesting;

import com.ganeshsiripuram.springtesting.springtesting.services.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class SpringtestingApplication implements CommandLineRunner {

//	private final DataService dataService;

	@Value("${my.variable}")
	private String myVariable;

	public static void main(String[] args) {
		SpringApplication.run(SpringtestingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
//		System.out.println("The data is:"+dataService.getData());
	}

}
