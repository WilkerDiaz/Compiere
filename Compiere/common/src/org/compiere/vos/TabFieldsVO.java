package org.compiere.vos;

import java.util.*;

import org.compiere.common.*;

public class TabFieldsVO extends ResponseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TabFieldsVO() {
	}

	public int resultCount;
	public ArrayList<FieldVO> fieldVOs = new ArrayList<FieldVO>(0);
	public TableModel tableModel = null;

	/**
	 * Cached column (field) index positions of displayed fields. Only valid
	 * after refreshCachedData() has been called.
	 */
	public ArrayList<Integer> displayedColPos = new ArrayList<Integer>();
	/**
	 * Cached column (field) index positions of key columns. Only valid after
	 * refreshCachedData() has been called.
	 */
	public transient final ArrayList<Integer> keyColPos = new ArrayList<Integer>();

	/**
	 * Get a temporary cache of the indexes of the FieldVOs that are tagged as
	 * primary key
	 * 
	 * @return
	 */
	private void refreshKeyColPos() {
		keyColPos.clear();

		if (fieldVOs != null) {
			for (int i = 0; i < fieldVOs.size(); ++i) {
				if (fieldVOs.get(i).IsKey) {
					keyColPos.add(i);
				}
			}
		}
		// If there are no key columns, then use all columns as the key (except
		// virtual columns)
		if (keyColPos.size() == 0) {
			for (int i = 0; i < fieldVOs.size(); ++i) {
				if (!fieldVOs.get(i).IsVirtualColumn)
					keyColPos.add(i);
			}
		}
	}

	private boolean isCustomized = false;
	public boolean isCustomized() {
		return isCustomized;
	}
	public void setCustomizedDisplayedColPos(ArrayList<Integer> colPos) {
		displayedColPos.clear();
		displayedColPos.addAll(colPos);
		isCustomized = true;
	}
	private void refreshDisplayedColPos() {
		//if col is from user customization, don't refresh
		if(isCustomized)
			return;
		displayedColPos.clear();
		
		// get the display sequence of table; for now just display all
		// displayed fields
		if (fieldVOs != null) {
			for (int j = 0; j < fieldVOs.size(); j++) {
				if (fieldVOs.get(j).IsDisplayed) {
					displayedColPos.add(j);
				}
			}
		}
		// sort according to sequences in table
		Collections.sort(displayedColPos, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return fieldVOs.get(o1).mrSeqNo - fieldVOs.get(o2).mrSeqNo;
			}
		});
	}

	/**
	 * Updates the fields tableSeqs and keyColsPos
	 */
	public void refreshCachedData() {
		refreshDisplayedColPos();

		refreshKeyColPos();
	}


	
	public FieldVO getFieldVO(String columnName) {
		for(int j=0; j<fieldVOs.size(); j++) {
			FieldVO fVO = fieldVOs.get(j);
			if(columnName.equals(fVO.ColumnName))
				return fVO;
		}
		return null;
	}

	
	/**
	 * Get the indexes of the FieldVOs that are who columns
	 * @return int array with the following schema:
	 * index 0: CREATED
	 * index 1: CREATEDBY
	 * index 2: UPDATED
	 * index 3: UPDATEDBY
	 */
	public int[] getWhoColsPos()
	{
		int[] whoColsPos = new int[4];
		if( fieldVOs != null )
		{
			for( int i = 0; i < fieldVOs.size(); ++i )
			{
				FieldVO vo = fieldVOs.get( i );
				if( vo.ColumnName.equals("Created") )
					whoColsPos[0] = i;
				
				if( vo.ColumnName.equals("CreatedBy") )
					whoColsPos[1] = i;
				
				if( vo.ColumnName.equals("Updated") )
					whoColsPos[2] = i;
				
				if( vo.ColumnName.equals("UpdatedBy") )
					whoColsPos[3] = i;
			}
		}
		return whoColsPos;		
	}

	public String getRowKey( String[] row )
	{
		if(row == null) // possible there is nothing
			throw new IllegalArgumentException("Row should not be null!");
			
		StringBuffer key = new StringBuffer( "" );

		// if no keyColsPos specified, use entire row as key
		if( keyColPos == null )
		{
			for( int i = 0; i < row.length; i++ )
			{
				if(i>0)
					key.append( "-" );

				key.append( row[i] );
			}
		}
		else
		{
			for( int i = 0; i < keyColPos.size(); i++ )
			{
				if(i>0)
					key.append( "-" );

				key.append( row[ keyColPos.get( i ).intValue() ] );
			}
		}

		return key.toString();
	}

	// Returns a string identifying the rowkey with column names like
	// columnName1:value1-columnName2:value2-columnName3:value3
	public String getRowKeyWithNames( String[] row )
	{
		if(row == null) // possible there is nothing
			throw new IllegalArgumentException("Row should not be null!");
			
		StringBuilder key = new StringBuilder();
		
		ArrayList<Integer> keyCols = this.keyColPos;
		// if no keyColsPos specified, try parent columns.
		if (keyCols.isEmpty()) {
			for (int i = 0; i < fieldVOs.size(); ++i) {
				if (fieldVOs.get(i).IsParent)
					keyCols.add(i);
			}
		}
		
		for( int i = 0; i < keyCols.size(); i++ )
		{
			if( i>0 )
				key.append( " - " );

			key.append( (fieldVOs.get((keyCols.get( i )).intValue())).ColumnName + ":");
			key.append( row[(keyCols.get( i )).intValue()] );
		}

		return key.toString();
	}
	
	
	
	public String keyColumnName()
	{
		if( fieldVOs != null )
		{
			for( int i = 0; i < fieldVOs.size(); ++i )
			{
				FieldVO vo = fieldVOs.get( i );
				if( vo.IsKey )
					return vo.ColumnName;
			}
		}
		return "";
	}
}
