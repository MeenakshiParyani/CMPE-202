/**
 * 
 */
package com.sjsu.parser;

/**
 * Enum to track class or interface relatioships
 * 
 * @author Meenakshi
 *
 */
public enum RelationshipTypes implements Relationships{
	
	
	EXTENDS(" extends "),
	IMPLEMENTS(" implements "),
	USES(" ..> ");
	//USES(" -() "),
	//PROVIDES(" ()- ");
	
	
	private String symbol;

	private RelationshipTypes(String symbol){
		this.symbol = symbol;
	}
	
	public String getSymbol(){
		return symbol;
	}
}
