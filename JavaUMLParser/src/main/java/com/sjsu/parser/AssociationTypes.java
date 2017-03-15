/**
 * 
 */
package com.sjsu.parser;

/**
 * To track the type of class associations
 * 
 * @author Meenakshi
 *
 */
public enum AssociationTypes implements Relationships{
	
	ONE_TO_ONE(" \" \" *-- \"1\" "), 
	ONE_TO_MANY(" \" \" *-- \"0..*\" ");
	//MANY_TO_ONE(" \"*\" -- \"1\" "), 
	//MANY_TO_MANY(" \"*\" -- \"*\" ");
	
	private String symbol;
	
	AssociationTypes(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	
}
