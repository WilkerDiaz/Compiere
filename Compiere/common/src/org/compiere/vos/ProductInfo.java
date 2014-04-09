package org.compiere.vos;

import org.compiere.util.*;

public class ProductInfo extends NamePair
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ProductInfo(){}
	/**
	 * 	ProductInfo
	 *	
	 */
	public ProductInfo (int newM_Product_ID, 
		String newName,String newTitle,String newGroupName,String newBomType,String newQuantity,
		String newSupplyType, int newM_BOMProduct_ID, int newOperationSeqNo, String newFieldIdentifier)
	{
		super(newName);
		M_Product_ID = newM_Product_ID;		
		Name = newName;
		title = newTitle;
		groupName = newGroupName;
		bomType = newBomType;
		quantity = newQuantity;
		supplyType = newSupplyType;
		M_BOMProduct_ID = newM_BOMProduct_ID;
		OperationSeqNo = newOperationSeqNo;
		fieldIdentifier = newFieldIdentifier;
	}
	
	public ProductInfo (int newM_Product_ID, 
			String newName,String newTitle,String newGroupName,String newBomType,String newQuantity,
			String newSupplyType, int newOperationSeqNo, String newFieldIdentifier){
		this(  newM_Product_ID, 
		 newName, newTitle, newGroupName, newBomType, newQuantity,
		 newSupplyType, 0, newOperationSeqNo, newFieldIdentifier);
	}
	
	public int M_Product_ID;
	public String Name;
	public String title; 
	public String groupName;
	public String bomType;
	public String quantity;
	public String supplyType;	
	public String fieldIdentifier; // identifer composed of bom component ID + bom level
	public int M_BOMProduct_ID;
	public int OperationSeqNo;

	public int getOperationSeqNo() {
		return OperationSeqNo;
	}
	
	public int getM_BOMProduct_ID() {
		return M_BOMProduct_ID;
	}
	
	/**
	 * 	to String
	 *	@return infoint
	 */
	@Override
	public String toString()
	{
		return Name;
	}

	
	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return Integer.toString(M_Product_ID);
	}
	
	public int getM_Product_ID() {
		return M_Product_ID;
	}
	
	public String getGroupName(){
		return groupName;
	}
	
	public String getTitle(){
		return title;
	}
	
	/*public String getName(){
		return Name;
	}*/
	
	public String getBomType(){
		return bomType;
	}
	
	public String getQuantity(){
		return quantity;
	}
	
	public String getSupplyType(){
		return supplyType;
	}
	
	public String getFieldIdentifier(){
		return fieldIdentifier;
	}
}   //  ProductInfo


