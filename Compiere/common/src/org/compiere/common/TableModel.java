package org.compiere.common;

import java.io.*;
import java.util.*;

import org.compiere.util.*;
import org.compiere.vos.*;

public class TableModel implements Serializable{
	
	static class RowData implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7078945003484809131L;
		
		String[] values = null;
		transient State states;
		transient boolean dirty;
		boolean rowReadOnly;
		boolean isGroupRow;
		/**
		 * Number of attachments for the record
		 */
		int attachments = 0;
		String comments = null;

		
		/**
		 * Table-specific transaction info.  See GridTab.getTrxInfo().
		 */
		String trxInfo;
		
		RowData getCopy(){
			RowData copy = new RowData();
			if( values != null ){
				copy.values = new String[values.length];
				int i = 0;
				for( String v : values ){
					copy.values[i] = v;
					++i;
				}
			}
			return copy;
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final CompiereLogger	log = new CLoggerSimple(TableModel.class);

	public static interface DirtyListener {
		public void onDirty(boolean dirty);
	}
	private transient DirtyListener dirtyListener = null;

	public static enum State { READONLY, UPDATE, INSERT };

	/**
	 * If this is populated, table model also carries exception IDs. so no need to call later
	 */
	public HashMap<Integer, ArrayList<NamePair>> exceptionValues = null;
	/**
	 * If this is populated, that means that the table needs to do a in-place replacement of its column definitions (e.g. add or remove columns)
	 */
	protected ArrayList<FieldVO> m_fieldVOs = null;
	
	protected ArrayList<RowData> rows = new ArrayList<RowData>();
	


	public TableModel() {}
	
	/**
	 * Only copy the data items for now
	 * @return
	 */
	public TableModel getCopy() {
		TableModel copy = new TableModel();
		for( RowData row : rows ) {
			copy.rows.add(row.getCopy());
		}
		return copy;
	}

	public String get(int row, int col) {
		if(rows.size() > row && row >= 0){
			return rows.get(row).values[col];
		}else{
			return null;
		}
	}
	public void set(int row, int col, String v) {
		fillRows(row);
		rows.get(row).values[col] = v;
	}
	public boolean isDirty(int row) {
		if(row < 0)
			return false;
		return rows.get(row).dirty; 
	}

	public void setDirty(int row, boolean flag) {
		fillRows(row);
		log.finest("set row dirty: "+row+" dirty:"+flag);
		//only set dirty flag when flag passed in is not the same
		if(isDirty(row) != flag) {
			rows.get(row).dirty = flag; 
			if(dirtyListener != null)
				dirtyListener.onDirty(flag);
		}
	}
	public State getRowState(int row) {
		if(rows.size() > row  && row >= 0){
			return rows.get(row).states;
		}else{
			return null;
		}
	}
	public void setRowState(int row, State state) {
		fillRows(row);
		log.finest("set row state: "+row+"-- "+state);
		rows.get(row).states = state;
	}

	public String[] getRow(int row) {
		return rows.get(row).values;
	}

	public void setRow(int row, String[] r) {
		fillRows(row);
		rows.get(row).values = r;
	}

	public int getNumAttachments(int row) {
		return rows.get(row).attachments;
	}

	public void setNumAttachments(int row, int intValue) {
		fillRows(row);
		rows.get(row).attachments = intValue;
	}

	public String getComments( int row ) {
		return rows.get(row).comments;
	}

	public void setComments( int row, String comments )
	{
		fillRows(row);
		rows.get(row).comments = comments;
	}

	public void addRow(String[] r, int numAttachments, String comments) {
		RowData row = new RowData();
		row.values = r;
		//default state is in insert
		row.states = State.READONLY;
		row.dirty = true;
		row.rowReadOnly = false;
		row.attachments = numAttachments;
		row.comments = comments;
		rows.add(row);
	}

	public void copyRow(int toRow, int fromRow) {
		RowData from = rows.get(fromRow);
		RowData to = new RowData();
		to.values = new String[from.values.length];
		for(int i = 0; i < from.values.length; ++i){
			to.values[i] = from.values[i];
		}
		//default state is in insert
		to.states = from.states;
		to.dirty = from.dirty;
		to.rowReadOnly = from.rowReadOnly;
		to.attachments = from.attachments;
		to.comments = from.comments;
		rows.set(toRow, to);
	}

	public void addRow(int toRow, String[] r) {
		RowData row = new RowData();
		row.values = r;
		//default state is in insert
		row.states = State.READONLY;
		row.dirty = false;
		row.rowReadOnly = false;
		row.attachments = 0;
		row.comments = null;
		rows.add(toRow, row);
	}
	public void removeRow(int row) {
		rows.remove(row);
	}

	public boolean isEmpty() {
		return rows.size() == 0;
	}
	public int rows() {
		return rows.size();
	}
	public void resetStates() {
		for( RowData row : rows ) {
			row.states = State.READONLY;
			row.dirty = false;
		}
	}

	public boolean isRowReadOnly(int row) {
		return rows.get(row).rowReadOnly;
	}
	public void setRowReadOnly(int row, boolean readonly) {
		fillRows(row);
		rows.get(row).rowReadOnly = readonly;
	}

	public void setDirtyListener(DirtyListener dirtyListener) {
		this.dirtyListener = dirtyListener;
	}

	public ArrayList<FieldVO> getFieldVOs()
	{
		return m_fieldVOs;
	}

	public void setFieldVOs( ArrayList<FieldVO> fieldVOs )
	{
		this.m_fieldVOs = fieldVOs;
	}


	public void setGroupRow( int row, boolean b )
	{
		fillRows(row);
		rows.get(row).isGroupRow = b;
	}

	public boolean isGroupRow( int row )
	{
		if( row < rows.size() )
			return rows.get( row ).isGroupRow;
		else
			return false;
	}

	/**
	 * If the particular row does not exist, create it
	 * @param row
	 */
	private void fillRows(int row) {
		while (rows.size() <= row) {
			rows.add(new RowData());
		}
	}

	public void setTrxInfo(int row, String trxInfo) {
		fillRows(row);
		rows.get(row).trxInfo = trxInfo;
	}
	
	public String getTrxInfo(int row){
		return rows.get(row).trxInfo;
	}

}