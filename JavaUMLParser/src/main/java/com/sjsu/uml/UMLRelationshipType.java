/**
 * 
 */
package com.sjsu.uml;

/**
 * Enum to track class or interface relatioships
 * 
 * @author Meenakshi
 *
 */
public enum UMLRelationshipType implements UMLRelationship{
	
	
	EXTENDS(" extends ", ""),
	IMPLEMENTS(" implements ", ""),
	USES(" ..> ", "uses");
	//USES(" -() "),
	//PROVIDES(" ()- ");
	
	
	private String symbol;
	private String label;

	private UMLRelationshipType(String symbol, String label){
		this.symbol = symbol;
		this.label = label;
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	public String getLabel() {
		return label;
	}
}
