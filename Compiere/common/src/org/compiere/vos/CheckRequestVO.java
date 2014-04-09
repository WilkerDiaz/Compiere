package org.compiere.vos;

import java.io.Serializable;
import java.util.ArrayList;

import org.compiere.util.ValueNamePair;

public class CheckRequestVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String menu;
	public String where;
	public ArrayList<ValueNamePair> fieldValuePairs = null;
	public CheckRequestVO(String menu, String where) {
		this.menu = menu;
		this.where = where;
	}
	public CheckRequestVO() {}
	public void setValue(String colName, String val) {
		if(fieldValuePairs == null)
			fieldValuePairs = new ArrayList<ValueNamePair>();
		fieldValuePairs.add(new ValueNamePair(colName, val));
				
	}
}
