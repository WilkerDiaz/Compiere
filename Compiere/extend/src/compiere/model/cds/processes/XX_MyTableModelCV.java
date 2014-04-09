package compiere.model.cds.processes;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author José G. Trías
 */
public class XX_MyTableModelCV extends DefaultTableModel {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 3504416702188087810L;
	int trow, tcolumn;
    
    public XX_MyTableModelCV(Object[][] data, String[] columnNames) {  
        super(data,columnNames);  
    }  
                 
    @Override
    public boolean isCellEditable(int row, int col) { 
    	
    	if (((row+1)%(3))==0){
    		return false;
        }
    	
    	return true;
    }

}
