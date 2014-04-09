package compiere.model.cds.processes;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class XX_MyTableModel extends DefaultTableModel {
	

	private static final long serialVersionUID = 3504416702188087810L;
	int trow, tcolumn;
    
    public XX_MyTableModel(Object[][] data, Object[] columnNames) {  
        super(data,columnNames);  
    }  
                 
    @Override
    public boolean isCellEditable(int row, int col) { 
        return false;
    }

}

