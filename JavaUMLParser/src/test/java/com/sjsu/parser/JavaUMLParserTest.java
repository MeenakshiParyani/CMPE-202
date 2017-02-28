package com.sjsu.parser;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Meenakshi
 *
 */
@Nonnull
public class JavaUMLParserTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(JavaUMLParserTest.class);

	@Test
	public void testAppTestCase0() {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		JavaUMLParser.parse(".\\src\\test\\resources\\Test-Case-0", "output.png");
	}
	
	@Test
	public void testAppTestCase1() {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		JavaUMLParser.parse(".\\src\\test\\resources\\Test-Case-1", "output.png");
	}
}
