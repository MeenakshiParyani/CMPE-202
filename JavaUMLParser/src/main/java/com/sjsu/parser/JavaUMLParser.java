package com.sjsu.parser;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

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
	public static CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());
	
	public static void main(String[] args) {
		checkArgument(args != null, "Expected not null arguments.");
		LOGGER.info("Hello World!");
		String source = ".\\src\\test\\resources\\Test-Case-0";
		String outputFile = "output.png";
		parse(source, outputFile);

	}

	/**
	 * Parses the Java source files and generates the UML class diagram
	 * 
	 * @param sourceFolder
	 * @param outputFile
	 * @return
	 */
	public static String parse(String sourceFolder, String outputFile){
		File[] files = readFileFolder(sourceFolder);
		combinedTypeSolver.add(new JavaParserTypeSolver(new File(sourceFolder)));
		String outoutFile = "";
		List<ClassOrInterfaceDeclaration> classOrInterfaces = new ArrayList<ClassOrInterfaceDeclaration>();

		try {
			for(File file : files) {
				CompilationUnit compilationUnit = JavaParser.parse(file);
				if(compilationUnit.getPackageDeclaration() != null && 
						compilationUnit.getPackageDeclaration().isPresent())
					throw new Exception("All Java Files should be in Default folder");
				ClassOrInterfaceDeclaration classOrInterfaceDeclaration = compilationUnit.getNodesByType(ClassOrInterfaceDeclaration.class).get(0);
				classOrInterfaces.add(classOrInterfaceDeclaration);
				//com.github.javaparser.ast.body.
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}



		try{
			UMLGenerator generator = new UMLGenerator();
			finalUML.append(generator.getClassOrInterfaceUML(classOrInterfaces));
			SourceStringReader sourceStringReader = new SourceStringReader(finalUML.toString());
			String outputFileName = sourceFolder+ "\\output.png";
			outoutFile = sourceStringReader.generateImage(new FileOutputStream(outputFileName), new FileFormatOption(FileFormat.PNG));

		}catch(FileNotFoundException e){
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}catch(IOException e){
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return "Image generated is " + outoutFile;
	}

	/**
	 * Returns the Java source files in the given folderpath
	 * 
	 * @param folderPath
	 * @return
	 */
	private static File[] readFileFolder(String folderPath) {
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


	/**
	 * @param pathname
	 * @return
	 */
	private static boolean isJavaFile(File pathname) {
		return pathname.getName().toLowerCase().endsWith(".java");
	}
}
