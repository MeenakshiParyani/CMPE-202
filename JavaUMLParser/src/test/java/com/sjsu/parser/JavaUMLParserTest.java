package com.sjsu.parser;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Meenakshi
 *
 */
public class JavaUMLParserTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(JavaUMLParserTest.class);

	@Test
	public void testAppTestCase0() {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		JavaUMLParser javaUMLParser = new JavaUMLParser();
		javaUMLParser.parse(".\\src\\test\\resources\\Test-Case-0", "output.png");
	}
	
	@Test
	public void testAppTestCase1() {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		JavaUMLParser javaUMLParser = new JavaUMLParser();
		javaUMLParser.parse(".\\src\\test\\resources\\Test-Case-1", "output.png");
	}
	
	@Test
	public void testAppTestCase2() {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		JavaUMLParser javaUMLParser = new JavaUMLParser();
		javaUMLParser.parse(".\\src\\test\\resources\\Test-Case-2", "output.png");
	}
	
	@Test
	public void testAppTestCase3() {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		JavaUMLParser javaUMLParser = new JavaUMLParser();
		javaUMLParser.parse(".\\src\\test\\resources\\Test-Case-3", "output.png");
	}
	
	@Test
	public void testAppTestCase4() {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		JavaUMLParser javaUMLParser = new JavaUMLParser();
		javaUMLParser.parse(".\\src\\test\\resources\\Test-Case-4", "output.png");
	}
}
