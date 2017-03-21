/**
 * 
 */
package com.sjsu.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.symbolsolver.model.declarations.InterfaceDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

/**
 * @author Meenakshi
 *
 */
public class UMLGenerator {

	public ArrayList<String> umlRelations = new ArrayList<String>();
	public final List<ClassOrInterfaceDeclaration> classesOrInterfaces = new ArrayList<>();
	public String getClassOrInterfaceUML(List<ClassOrInterfaceDeclaration> classOrInterfaces){
		classesOrInterfaces.addAll(classOrInterfaces);
		StringBuilder builder = new StringBuilder("@startuml");

		for(ClassOrInterfaceDeclaration classOrInterface : classOrInterfaces) {
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
		}
		builder.append(" {");
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
		List<FieldDeclaration> filteredFields = new ArrayList<FieldDeclaration>();
		filteredFields = fields.stream().filter(f -> f.isPrivate() || f.isPublic() || f.isProtected()).collect(Collectors.toList());
		for(FieldDeclaration field : filteredFields) {
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
					if(OneToManyRelationExists(containedClass, containingClass))
						checkAndAddRelationships(containingClass, className, AssociationTypes.ONE_TO_ONE, "");
				}
			}else {
				String fieldName = field.getVariable(0).toString();
				if(field.isPrivate() && !isGetterOrSetterMethod(classOrInterface, "", fieldName)){
					builder.append("\n -" + fieldName + " : " + field.getCommonType());
				}else if (field.isPublic()){
					builder.append("\n +" + fieldName + " : " + field.getCommonType());
				}
			}
		}
	}

	/**
	 * Check if the relationship already exists to avoid duplicates
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
		List<MethodDeclaration> filteredMethods = new ArrayList<MethodDeclaration>();
		filteredMethods = methods.stream().filter(method -> (method.isPublic() || method.isProtected()) &&
				!isGetterOrSetterMethod(classOrInterface, method.getNameAsString(), "")).collect(Collectors.toList());
		for(MethodDeclaration method : filteredMethods){
			if(method.isPublic() && isNotIncludedInParent(method.getNameAsString(), classOrInterface))
				builder.append("\n +" + method.getNameAsString() + " : " + method.getType() + "()");
			NodeList<Parameter> parameters = method.getParameters();
			parameters.stream().forEach(parameter -> 
			{	
				SymbolReference symbolReference = JavaUMLParser.combinedTypeSolver.tryToSolveType(parameter.getType().toString());
				if(symbolReference.isSolved()){
					//if(symbolReference.getCorrespondingDeclaration() instanceof InterfaceDeclaration)
					//checkAndAddRelationships(classOrInterface.getNameAsString(), parameter.getType().toString(), RelationshipTypes.USES, RelationshipTypes.USES.getLabel());
					//else
					checkAndAddRelationships(classOrInterface.getNameAsString(), parameter.getType().toString(), RelationshipTypes.USES, RelationshipTypes.USES.getLabel());
				}
			});
		}
	}

	/**
	 * To ignore the method if present in parent class/interface
	 * @param methodName 
	 * 
	 * @param classOrInterface
	 * @return
	 */
	private boolean isNotIncludedInParent(String methodName, ClassOrInterfaceDeclaration classOrInterface) {
		NodeList<ClassOrInterfaceType> implementedTypes =  classOrInterface.getImplementedTypes();
		for(ClassOrInterfaceType parent : implementedTypes){
			ClassOrInterfaceDeclaration parentInterface = getClassOrInterfaceByName(parent.getNameAsString());
			List<MethodDeclaration> parentMethods = parentInterface.getMethods();
			for(MethodDeclaration parentMethod : parentMethods){
				if(parentMethod.getNameAsString().equalsIgnoreCase(methodName))
					return false;
			}
		}
		return true;
	}

	/**
	 * For a given field name, checks if the corresponding getter and setter methods exist
	 * Given a method, like getter and setter, checks if corresponding field exists
	 * 
	 * @param classOrInterface
	 * @param methodName
	 * @param fieldName
	 * @return
	 */
	private boolean isGetterOrSetterMethod(ClassOrInterfaceDeclaration classOrInterface, String methodName, String fieldName) {

		if(fieldName == null || fieldName == ""){
			String field = "";
			if(methodName.startsWith("get"))
				field = StringUtils.substringAfter(methodName, "get").toLowerCase();
			if(methodName.startsWith("set"))
				field = StringUtils.substringAfter(methodName, "set").toLowerCase();
			if(classOrInterface.getFieldByName(field).isPresent())
				return true;
		}else {
			String gettermethod = "get" + WordUtils.capitalizeFully(fieldName);
			String setterMethod = "set" + WordUtils.capitalizeFully(fieldName);
			if(!classOrInterface.getMethodsByName(gettermethod).isEmpty() && !classOrInterface.getMethodsByName(setterMethod).isEmpty())
				return true;
		}
		return false;
	}

	/**
	 * Get the class or interface declaration by name
	 * 
	 * @param classOrInterfaceName
	 * @return
	 */
	public ClassOrInterfaceDeclaration getClassOrInterfaceByName(String classOrInterfaceName){
		for(ClassOrInterfaceDeclaration classOrInterfaceDeclaration: classesOrInterfaces){
			if(classOrInterfaceDeclaration.getNameAsString().equalsIgnoreCase(classOrInterfaceName))
				return classOrInterfaceDeclaration;
		}
		return null;

	}
	
	public boolean OneToManyRelationExists(String containingClass, String containedClass){
		for(ClassOrInterfaceDeclaration classOrInterface : classesOrInterfaces){
			if(classOrInterface.getNameAsString().equalsIgnoreCase(containingClass)){
				List<FieldDeclaration> fields = classOrInterface.getFields();
				for(FieldDeclaration field : fields) {
					String fieldClass = field.getVariable(0).getNameAsString();
					if(fieldClass.startsWith("Collection")){
						fieldClass = StringUtils.substringBetween(containedClass, "<", ">");
						if(fieldClass.equalsIgnoreCase(containedClass))
							return true;
					}
				}
			}
		}
		return false;
	}	
	

}
