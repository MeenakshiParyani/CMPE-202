/**
 * 
 */
package com.sjsu.parser;

import java.util.Collection;
import java.util.List;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

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
				classOrInterface.getAncestorOfType(Object.class);
				classOrInterface.getExtendedTypes();
			}else {
				builder.append("class " + classOrInterface.getNameAsString() + " { \n");
				getFieldsUML(builder, fields, classOrInterface);
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
	private static void getFieldsUML(StringBuilder builder, List<FieldDeclaration> fields, ClassOrInterfaceDeclaration classOrInterface) {
		fields.stream().filter(field -> field.isPrivate() || field.isPublic());
		for(FieldDeclaration field : fields) {
			if(field.getElementType().equals(ClassOrInterfaceType.class)){
				String containingClass = classOrInterface.getNameAsString();
				String containedClass = "";
				if(field.getVariable(0).getType() instanceof Collection<?>){
					containedClass = field.getVariable(0).getNameAsString();
					builder.append(containingClass + " |> " + containedClass);
				}else {
					
				}
			}else {
				if(field.isPrivate()){
					builder.append("\n -" + field.getVariable(0) + " : " + field.getElementType());
				}else{
					builder.append("\n +" + field.getVariable(0) + " : " + field.getElementType());
				}
			}
			
			
		}
	}
	
	private static void getMethodsUML(StringBuilder builder, List<MethodDeclaration> methods) {
		methods.stream().filter(method -> method.isPublic());
		for(MethodDeclaration method : methods){
			builder.append("\n +" + method.getNameAsString() + " : " + method.getType() + "()");
		}
	}
	
	private static String addClassAssociations(String class1, String class2, Association association){
		String uml = "";
		switch(association){
			case ONE_TO_ONE : uml = class1 + " 1 - 1 " + class2;
			case ONE_TO_MANY : uml = class1 + " 1 - * " + class2;
			case MANY_TO_ONE : uml = class1 + " * - 1 " + class2;
			case MANY_TO_MANY : uml = class1 + " * - * " + class2;
		}
		return uml;
	}
}
