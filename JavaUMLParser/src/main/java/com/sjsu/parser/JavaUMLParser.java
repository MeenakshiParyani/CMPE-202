package com.sjsu.parser;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Nonnull
public class JavaUMLParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(JavaUMLParser.class);

	public static void main(String[] args) {
		checkArgument(args != null, "Expected not null arguments.");
		LOGGER.info("Hello World!");
		System.out.println("Hello World!!");
	}
}
