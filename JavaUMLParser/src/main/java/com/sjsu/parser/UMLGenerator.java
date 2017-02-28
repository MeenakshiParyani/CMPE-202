/**
 * 
 */
package com.sjsu.parser;

import java.util.List;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * @author Meenakshi
 *
 */
public class UMLGenerator {
	
	public static String getClassOrInterfaceUML(List<ClassOrInterfaceDeclaration> classOrInterfaces){

		StringBuilder builder = new StringBuilder("@startuml\n");

		for(ClassOrInterfaceDeclaration classOrInterface : classOrInterfaces) {
			List<FieldDeclaration> fields = classOrInterface.getFields();
			List<MethodDeclaration> methods = classOrInterface.getMethods();
			if(classOrInterface.isInterface()){

			}else {
				builder.append("class " + classOrInterface.getNameAsString() + " { \n");
				getFieldsUML(builder, fields);
				getMethodsUML(builder, methods);
				builder.append("\n}\n");
			}
			


		}
		builder.append("@enduml");
		return builder.toString();
	}
	
	/**
	 * Returns the Plant UML notation for Class/Interface fields
	 * Only Returns Public and Private Fields
	 * 
	 * @param builder
	 * @param fields
	 */
	private static void getFieldsUML(StringBuilder builder, List<FieldDeclaration> fields) {
		fields.stream().filter(field -> field.isPrivate() || field.isPublic());
		for(FieldDeclaration field : fields) {
			if(field.isPrivate()){
				builder.append("\n -" + field.getVariables().get(0) + " : " + field.getElementType());
			}else{
				builder.append("\n +" + field.getVariables().get(0) + " : " + field.getElementType());
			}
			
		}
	}
	
	private static void getMethodsUML(StringBuilder builder, List<MethodDeclaration> methods) {
		methods.stream().filter(method -> method.isPublic());
		for(MethodDeclaration method : methods){
			builder.append("\n +" + method.getNameAsString() + " : " + method.getType() + "()");
		}
	}
}
