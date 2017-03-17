/**
 * 
 */
package com.sjsu.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.symbolsolver.model.declarations.InterfaceDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

/**
 * @author Meenakshi
 *
 */
public class UMLGenerator {
	
	ArrayList<String> umlRelations = new ArrayList<String>();
	public String getClassOrInterfaceUML(List<ClassOrInterfaceDeclaration> classOrInterfaces){
		
		StringBuilder builder = new StringBuilder("@startuml");
		
		for(ClassOrInterfaceDeclaration classOrInterface : classOrInterfaces) {
			List<MethodDeclaration> methods = classOrInterface.getMethods();
				getClassOrInterfaceUML(builder, classOrInterface);
				getFieldsUML(builder, classOrInterface);
				getMethodsUML(builder, classOrInterface);
				addClosingUML(builder, classOrInterface);
		}
		addClassOrInterfaceRelations(builder, umlRelations);
		builder.append("\n@enduml");
		System.out.println(builder.toString());
		return builder.toString();
	}

	/**
	 * @param builder
	 */
	private void addClosingUML(StringBuilder builder, ClassOrInterfaceDeclaration classOrInterface) {
		if(!classOrInterface.isInterface())
			builder.append("\n}\n");
	}

	/**
	 * @param builder
	 * @param classOrInterface
	 */
	private void getClassOrInterfaceUML(StringBuilder builder, ClassOrInterfaceDeclaration classOrInterface) {
		String classOrInterfaceName = classOrInterface.getNameAsString();
		if(classOrInterface.isInterface())
			builder.append("\ninterface " + classOrInterfaceName);
		else {
			NodeList<ClassOrInterfaceType> exntendedTypes = classOrInterface.getExtendedTypes();
			NodeList<ClassOrInterfaceType> implementedTypes = classOrInterface.getImplementedTypes();
			builder.append("\nclass " + classOrInterfaceName);
			exntendedTypes.stream().forEach(node -> builder.append(RelationshipTypes.EXTENDS.getSymbol() + node.getNameAsString()));
			if(!implementedTypes.isEmpty()){
				builder.append(RelationshipTypes.IMPLEMENTS.getSymbol());
				builder.append(StringUtils.join(implementedTypes.iterator(), ','));
				implementedTypes.stream().forEach(node -> 
				{
					//checkAndAddRelationships(node.getNameAsString(), classOrInterfaceName ,RelationshipTypes.PROVIDES, "");
				});
			}
			builder.append(" {");
		}
	}
	

	/**
	 * To add class relationships
	 * 
	 * @param builder
	 * @param classRelations
	 */
	private void addClassOrInterfaceRelations(StringBuilder builder, ArrayList<String> classRelations) {
		for(String relation : classRelations){
			builder.append("\n" + relation); 
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
			String className = field.getVariable(0).getType().toString();
			field.getCommonType().getClass().equals(ReferenceType.class);
			SymbolReference symbolReference = JavaUMLParser.combinedTypeSolver.tryToSolveType(field.getCommonType().toString());
			if(symbolReference.isSolved() || className.startsWith("Collection")){
				String containingClass = classOrInterface.getNameAsString();
				String containedClass;
				if(className.startsWith("Collection")){
					containedClass = StringUtils.substringBetween(className, "<", ">");
					checkAndAddRelationships(containingClass, containedClass, AssociationTypes.ONE_TO_MANY, "");

				}else {
					containedClass = className;
					checkAndAddRelationships(containingClass, className, AssociationTypes.ONE_TO_ONE, "");
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
	private void checkAndAddRelationships(String containingClass, String containedClass, Relationships relationships, String label) {
		SymbolReference symbolReference = JavaUMLParser.combinedTypeSolver.tryToSolveType(containedClass);
		if(symbolReference.isSolved() && !umlRelations.stream().anyMatch(p -> p.contains(containingClass) && p.contains(containedClass))){
				String relation = containingClass + relationships.getSymbol() + containedClass;
				if(label != null && label !="")
					relation = relation + " : " + label;
				umlRelations.add(relation);
		}
	}
	
	private void getMethodsUML(StringBuilder builder, ClassOrInterfaceDeclaration classOrInterface) {
		List<MethodDeclaration> methods = classOrInterface.getMethods();
		methods.stream().filter(method -> method.isPublic());
		for(MethodDeclaration method : methods){
			builder.append("\n +" + method.getNameAsString() + " : " + method.getType() + "()");
			NodeList<Parameter> parameters = method.getParameters();
			parameters.stream().forEach(parameter -> 
			{	
				SymbolReference symbolReference = JavaUMLParser.combinedTypeSolver.tryToSolveType(parameter.getType().toString());
				if(symbolReference.isSolved()){
					if(symbolReference.getCorrespondingDeclaration() instanceof InterfaceDeclaration)
						checkAndAddRelationships(classOrInterface.getNameAsString(), parameter.getType().toString(), RelationshipTypes.USES, "uses");
					else
						checkAndAddRelationships(classOrInterface.getNameAsString(), parameter.getType().toString(), RelationshipTypes.USES, "uses");
				}
							
			
			});
		}
	}
	
	
}
