package compiere.model.cds.processes;

import javax.swing.table.DefaultTableModel;



public class XX_ChgePercentTabModel extends DefaultTableModel {

	private static final long serialVersionUID = 3504416702188087810L;           
	
	public XX_ChgePercentTabModel(Object data [][], String table_header[]) {
		super(data,table_header);
	}
	
    public boolean isCellEditable(int row, int col) {     	  	
    	if (col == 1) return true;
    	else return false;
    }
    
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }      
}