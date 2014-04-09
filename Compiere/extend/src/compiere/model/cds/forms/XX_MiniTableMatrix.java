package compiere.model.cds.forms;


import org.compiere.minigrid.MiniTable;

public class XX_MiniTableMatrix extends MiniTable {

	/** */
	private static final long serialVersionUID = 1L;
	
	/**
	 *  Default Constructor
	 */
	public XX_MiniTableMatrix()
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
		//  if the first column is a boolean and it is false, it is not editable
		if (column != 0
				&& getValueAt(row, 0) instanceof Boolean
				&& !((Boolean)getValueAt(row, 0)).booleanValue())
			return false;
		
		if(column==(getColumnCount()-1)){
			return false;
		}	
		
		if (((row+1)%(3))==0){
    		return false;
        }

		return true;
	}   //  isCellEditable
	
}
