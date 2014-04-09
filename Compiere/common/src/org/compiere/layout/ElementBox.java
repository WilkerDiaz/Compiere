package org.compiere.layout;


public class ElementBox implements Box {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int elementNO;
	
	public ElementBox()
	{		
	}
	
	public ElementBox( int elementNO )
	{
		this.elementNO = elementNO;
	}

	public int getElementNO() {
		return elementNO;
	}

}
