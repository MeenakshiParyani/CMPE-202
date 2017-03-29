package com.sjsu.parser;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
public class JavaUMLParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(JavaUMLParser.class);
	public StringBuilder finalUML = new StringBuilder();
	public static String libs = System.getProperty("java.home") + "\\lib";
	public static CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());

	public static void main(String[] args) {
		checkArgument(args != null, "Expected not null arguments.");
		LOGGER.info("Hello World!");
		String source = ".\\src\\test\\resources\\Test-Case-0";
		String outputFile = "output.png";
		JavaUMLParser javaUMLParser = new JavaUMLParser();
		javaUMLParser.parse(source, outputFile);

	}

	/**
	 * Parses the Java source files and generates the UML class diagram
	 * 
	 * @param sourceFolder
	 * @param outputFile
	 * @return
	 */
	public String parse(String sourceFolder, String outputFile){
		File[] files = readFileFolder(sourceFolder);
		combinedTypeSolver.add(new JavaParserTypeSolver(new File(libs)));
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
			//finalUML.append(getStaticUML());
			finalUML.append(generator.getClassOrInterfaceUML(classOrInterfaces));
			SourceStringReader sourceStringReader = new SourceStringReader(finalUML.toString());
			String outputFileName = sourceFolder+ "\\output.png";
			File file = new File(outputFileName);
			if(file.exists())
				file.delete();
			FileOutputStream fileOutputStream = new FileOutputStream(outputFileName);
			outoutFile = sourceStringReader.generateImage(fileOutputStream, new FileFormatOption(FileFormat.PNG));
			fileOutputStream.close();
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
	
	public static String getStaticUML(){
		
		StringBuilder builder = new StringBuilder();
		builder.append("@startuml");
		/*builder.append("\ninterface B");
		builder.append("\nclass A extends V implements B");
		builder.append("\n}");*/
		/*builder.append("\ninterface Mary");
		builder.append("\nabstract class MaryImpl implements Mary");
		builder.append("\ninterface Bob");
		builder.append("\nclass BobtImpl extends MaryImpl implements Bob");*/
		//builder.append("\ncomponent Something {");
		//builder.append("\ncomponent Provider");
		//builder.append("\ninterface A2");
		//builder.append("\nB2 - A2");
		//builder.append("\n}");
		//builder.append("\ncomponent SomethingElse {");
		//builder.append("\ncomponent User");
		//builder.append("\n A2 )- C2");
		//builder.append("\n}");
		builder.append("\ninterface A1");
		builder.append("\ninterface A2");
		builder.append("\nclass B1 extends P implements A1 {");
		builder.append("\n}");
		builder.append("\nclass B2 extends P implements A1,A2 {");
		builder.append("\n}");
		builder.append("\nclass C1 {");
		builder.append("\n+test : void()");
		builder.append("\n}");
		builder.append("\nclass C2 {");
		builder.append("\n+test : void()");
		builder.append("\n}");
		builder.append("\nclass P {");
		builder.append("\n}");
		builder.append("\nB1 ()- A1");
		builder.append("\nB2 ()- A1");
		builder.append("\nB2 ()- A2");
		builder.append("\nA1 )-- C1");
		builder.append("\nA2 )-- C2");
		builder.append("\n@enduml");
		System.out.println(builder.toString());
		return builder.toString();
		
	}
}
