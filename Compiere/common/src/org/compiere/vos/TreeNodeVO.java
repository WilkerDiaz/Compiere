package org.compiere.vos;

import java.io.Serializable;

/*******************************************************************************
 * Tree Maintenance List Item
 ******************************************************************************/
public class TreeNodeVO implements Serializable {
	/**
	 * ListItem
	 * 
	 * @param ID
	 * @param Name
	 * @param Description
	 * @param summary
	 * @param ImageIndicator
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** ID */
	public int id;
	/** Name */
	public String name;
	/** Description */
	public String description;
	/** Summary */
	public boolean isSummary;
	/** Indicator */
	public String imageIndicator; // Menu - Action
	/** Color in RRGGBB format */
	public String color;

	/**
	 * 	ListItem
	 *	@param ID
	 *	@param Name
	 *	@param Description
	 *	@param summary
	 *	@param ImageIndicator
	 */
	public TreeNodeVO (int ID, String Name, String Description, 
		boolean summary, String ImageIndicator)
	{
		id = ID;
		name = Name;
		description = Description;
		isSummary = summary;
		imageIndicator = ImageIndicator;
	}	//	ListItem

	public TreeNodeVO() {
	}
	
	/**
	 * 	To String
	 *	@return	String Representation
	 */
	@Override
	public String toString ()
	{
		String retValue = name;
		if (description != null && description.length() > 0)
			retValue += " (" + description + ")";
		return retValue;
	}	//	toString
	
} // ListItem

