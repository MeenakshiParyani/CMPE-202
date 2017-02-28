package com.sjsu.parser;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * @author Meenakshi
 *
 */
@Nonnull
public class JavaUMLParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(JavaUMLParser.class);
	public static StringBuilder finalUML = new StringBuilder();

	public static void main(String[] args) {
		checkArgument(args != null, "Expected not null arguments.");
		LOGGER.info("Hello World!");
		String source = ".\\src\\test\\resources\\Test-Case-0";
		File[] files = readFileFolder(source);
		parse(source, files);

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

	public static String parse(String sourceFolder, File[] files){

		JavaParser javaParser = new JavaParser();
		for(File file : files) {
			try {
				CompilationUnit compilationUnit = JavaParser.parse(file);
				if(compilationUnit.getPackageDeclaration() != null && 
						compilationUnit.getPackageDeclaration().isPresent())
					throw new Exception("All Java Files should be in Default folder");
				ClassOrInterfaceDeclaration classOrInterfaceDeclaration = compilationUnit.getNodesByType(ClassOrInterfaceDeclaration.class).get(0);
				//com.github.javaparser.ast.body.
				finalUML.append(UMLGenerator.getClassOrInterfaceUML(classOrInterfaceDeclaration));
				SourceStringReader sourceStringReader = new SourceStringReader(finalUML.toString());
				String outputFileName = sourceFolder+ "\\output.png";
				String outoutFile = sourceStringReader.generateImage(new FileOutputStream(outputFileName), new FileFormatOption(FileFormat.PNG));
				System.out.println("Image generated is " + outoutFile);

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
