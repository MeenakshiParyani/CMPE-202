package com.sjsu.parser;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Nonnull
public class JavaUMLParserTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(JavaUMLParserTest.class);

	@Test
	public void testApp() {
		LOGGER.error("This test will pass.");
		Assert.assertTrue(true);
	}
}
