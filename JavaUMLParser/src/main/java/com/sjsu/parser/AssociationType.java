/**
 * 
 */
package com.sjsu.parser;

import com.sjsu.uml.UMLRelationship;

/**
 * To track the type of class associations
 * 
 * @author Meenakshi
 *
 */
public enum AssociationType implements UMLRelationship{
	
	ONE_TO_ONE(" -- ", "has"), 
	ONE_TO_MANY(" -- \" *\" ", "");
	//MANY_TO_ONE(" \"*\" -- \"1\" "), 
	//MANY_TO_MANY(" \"*\" -- \"*\" ");
	
	private String symbol;
	private String label;
	
	AssociationType(String symbol, String label) {
		this.symbol = symbol;
		this.label = label;
	}
	
	public String getSymbol(){
		return symbol;
	}

	@Override
	public String getLabel() {
		return null;
	}
	
	
}