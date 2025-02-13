package com.ganeshsiripuram.springtesting.springtesting;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
class SpringtestingApplicationTests {

//	@BeforeEach
//	void Setup1(){
//		log.info("before each");
//	}
//
//	@AfterEach
//	void setUp2(){
//		log.info("after each");
//	}
//
//	@BeforeAll
//	static void setUp3(){
//		log.info("before all");
//	}
//
//	@AfterAll
//	static  void setUp4(){
//		log.info("after all");
//	}


//	@Test
////	@Disabled
//	void test1() {
//		log.info("test one");
//	}
//
//	@Test
////	@DisplayName("Displaytest1")
//	void test2(){
//		log.info("test two");
//	}

	public int addTwoNumbers(int a,int b){
		return a+b;
	}

	@Test
	void   test(){
	int a=7;
	int b=77;
	int result=addTwoNumbers(a,b);
//	Assertions.assertEquals(84, addTwoNumbers(a,b));   junit
//
//		Assertions.assertThat(result)
//				.isEqualTo(84)
//				.isCloseTo(85, Offset.offset(1));

//		Assertions.assertThat("Apple")
//				.isEqualTo("Apple")
//				.startsWith("Ap")
//				.endsWith("l")
//				.hasSize(5);
//
		Assertions.assertThat(List.of("Apple","Banana"))
				.contains("Apple")
				.doesNotContain("Orange")
				.hasSize(2);
	}

	double divideTwoNumbers(int a,int b){
	try{
		return a/b;
	}catch(ArithmeticException e){
		log.error("Arthmetic Exception occured"+e.getLocalizedMessage());
		throw new ArithmeticException("Tried to divide by zero");
	}
	}
	@Test
	void test1(){
	int a=5;
	int b=0;
		Assertions.assertThatThrownBy(()->divideTwoNumbers(a,b))
				.isInstanceOf(ArithmeticException.class)
				.hasMessage("Tried to divide by  ");
	}


}
