package com.sjsu.parser;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

@Nonnull
public class JavaUMLParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(JavaUMLParser.class);

	public static void main(String[] args) {
		checkArgument(args != null, "Expected not null arguments.");
		LOGGER.info("Hello World!");
		File[] files = readFileFolder("D:/Java/Programs/exception");
		parse(files);
		
	}

	public static File[] readFileFolder(String folderPath) {
		File folder = new File(folderPath);
		File[] javaFiles = new File[0];
		try {
			FileFilter fileFilter = new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					if(isJavaFile(pathname))
						return true;
					return false;
				}


			};
			javaFiles = folder.listFiles(fileFilter);
			if(javaFiles.length == 0)
				throw new Exception("No Java Files Found in specified folder");

		} catch (FileNotFoundException e) {
			LOGGER.error("Please enter valid source folder location");
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return javaFiles;
	}
	
	public static String parse(File[] files){
		
		JavaParser javaParser = new JavaParser();
		for(File file : files) {
			try {
				CompilationUnit compilationUnit = JavaParser.parse(file);
				if(compilationUnit.getPackageDeclaration() != null && 
						compilationUnit.getPackageDeclaration().isPresent())
					throw new Exception("All Java Files should be in Default folder");
				System.out.println(compilationUnit.toString());
			} catch (FileNotFoundException e) {
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			}
			
		}
		
		
		return null;
		
	}

	/**
	 * @param pathname
	 * @return
	 */
	private static boolean isJavaFile(File pathname) {
		return pathname.getName().toLowerCase().endsWith(".java");
	}
}
