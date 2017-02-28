/**
 * 
 */
package com.sjsu.parser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

/**
 * @author Meenakshi
 *
 */
public class UMLGenerator {
	
	public static String getClassOrInterfaceUML(ClassOrInterfaceDeclaration classOrInterface){
		StringBuilder builder = new StringBuilder("@startuml\n");
		if(classOrInterface.isInterface()){
			
		}else {
			builder.append("class " + classOrInterface.getNameAsString() + " { \n String data \n void methods() \n}\n");
		}
		builder.append("@enduml");
		return builder.toString();
	}
}
