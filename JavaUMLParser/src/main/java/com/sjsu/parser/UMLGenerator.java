/**
 * 
 */
package com.sjsu.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

/**
 * @author Meenakshi
 *
 */
public class UMLGenerator {
	
	ArrayList<String> umlRelations = new ArrayList<String>();
	ArrayList<String[]> classRelations = new ArrayList<String[]>();
	public String getClassOrInterfaceUML(List<ClassOrInterfaceDeclaration> classOrInterfaces){
		
		StringBuilder builder = new StringBuilder("@startuml\n");
		
		for(ClassOrInterfaceDeclaration classOrInterface : classOrInterfaces) {
			List<MethodDeclaration> methods = classOrInterface.getMethods();
			if(classOrInterface.isInterface()){
				classOrInterface.getAncestorOfType(Object.class);
				classOrInterface.getExtendedTypes();
			}else {
				builder.append("class " + classOrInterface.getNameAsString() + " { \n");
				getFieldsUML(builder, classOrInterface);
				getMethodsUML(builder, methods);
				builder.append("\n}\n");
			}
			


		}
		addClassRelationships(builder, umlRelations);
		builder.append("@enduml");
		System.out.println(builder.toString());
		return builder.toString();
	}
	
	/**
	 * To add class relationships
	 * 
	 * @param builder
	 * @param classRelations
	 */
	private void addClassRelationships(StringBuilder builder, ArrayList<String> classRelations) {
		for(String relation : classRelations){
			builder.append(relation + "\n"); 
		}
	}

	/**
	 * Returns the Plant UML notation for Class/Interface fields
	 * Only Returns Public and Private Fields
	 * 
	 * @param builder
	 * @param fields
	 * @throws ClassNotFoundException 
	 */
	private void getFieldsUML(StringBuilder builder,  ClassOrInterfaceDeclaration classOrInterface) {
		List<FieldDeclaration> fields = classOrInterface.getFields();
		fields.stream().filter(field -> field.isPrivate() || field.isPublic());
		for(FieldDeclaration field : fields) {
			if(field.getElementType().getClass().equals(ClassOrInterfaceType.class)){
				String containingClass = classOrInterface.getNameAsString();
				String className = field.getVariable(0).getType().toString();
				String containedClass;
				if(className.startsWith("Collection")){
					containedClass = StringUtils.substringBetween(className, "<", ">");
					checkForExistingRelationships(containingClass, containedClass, Association.ONE_TO_MANY);

				}else {
					containedClass = className;
					checkForExistingRelationships(containingClass, className, Association.ONE_TO_ONE);
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

	/**
	 * Check if the relationship already exists
	 * 
	 * @param containingClass
	 * @param containedClass
	 */
	private void checkForExistingRelationships(String containingClass, String containedClass, Association association) {
		SymbolReference symbolReference = JavaUMLParser.combinedTypeSolver.tryToSolveType(containedClass);
		if(symbolReference.isSolved() && !umlRelations.stream().anyMatch(p -> p.contains(containingClass) && p.contains(containedClass))){
				umlRelations.add(addClassAssociations(containingClass, containedClass, association));
		}
	}
	
	private void getMethodsUML(StringBuilder builder, List<MethodDeclaration> methods) {
		methods.stream().filter(method -> method.isPublic());
		for(MethodDeclaration method : methods){
			builder.append("\n +" + method.getNameAsString() + " : " + method.getType() + "()");
		}
	}
	
	private static String addClassAssociations(String class1, String class2, Association association){
		String uml = class1 + association.getSymbol() + class2;
		return uml;
	}
}
