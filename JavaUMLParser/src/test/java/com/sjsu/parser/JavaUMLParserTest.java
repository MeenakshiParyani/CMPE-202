package com.sjsu.parser;

import java.io.File;

import javax.annotation.Nonnull;

import org.junit.Assert;
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
	public void testApp() {
		LOGGER.error("This test will pass.");
		Assert.assertTrue(true);
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		File[] files = JavaUMLParser.readFileFolder(".\\src\\test\\resources\\Test-Case-0");
		JavaUMLParser.parse(".\\src\\test\\resources\\Test-Case-0", files);
	}
}
