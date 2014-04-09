package compiere.model.cds.forms;

import org.compiere.minigrid.MiniTable;

public class XX_MiniTableCreditNote extends MiniTable {

	/** */
	private static final long serialVersionUID = 1L;
	
	/**
	 *  Default Constructor
	 */
	public XX_MiniTableCreditNote()
	{
		super();
		super.setSortEnabled(false);
	}   //  MiniTable
	
	
	/**
	 *  Is Cell Editable
	 *  @param row row
	 *  @param column column
	 *  @return true if editable
	 */
	@Override
	public boolean isCellEditable(int row, int column)
	{
		
		if (getValueAt(row, 13).toString().equals("ANU") || getValueAt(row, 13).toString().equals("CER")){
    		return false;
        }

		return true;
	}   //  isCellEditable
}