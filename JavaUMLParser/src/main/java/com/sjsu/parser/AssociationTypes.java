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
public enum AssociationTypes implements Relationship{
	
	ONE_TO_ONE(" \" \" -- \"1\" ", "has"), 
	ONE_TO_MANY(" \" \" -- \"0..*\" ", "");
	//MANY_TO_ONE(" \"*\" -- \"1\" "), 
	//MANY_TO_MANY(" \"*\" -- \"*\" ");
	
	private String symbol;
	private String label;
	
	AssociationTypes(String symbol, String label) {
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
