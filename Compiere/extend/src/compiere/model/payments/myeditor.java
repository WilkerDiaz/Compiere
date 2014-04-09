package compiere.model.payments;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import org.compiere.minigrid.MiniTable;
import org.compiere.swing.CButton;

import compiere.model.payments.forms.XX_PlanPayForm;

public class myeditor extends AbstractCellEditor implements TableCellEditor, ActionListener{

	private static final long serialVersionUID = 1L;
	Boolean currentValue;
    private CButton avisos;
    protected static final String EDIT = "edit";
    private MiniTable mgTable;

    public myeditor(MiniTable mTable) {
    	avisos = new CButton();
    	avisos.setActionCommand(EDIT);
    	avisos.addActionListener(this);
    	avisos.setBorderPainted(false);
    	avisos.setSize(10, 10);
        this.mgTable = mTable;
    }

    public void actionPerformed(ActionEvent e) {
    	System.out.println("HOLA");
    	/*XX_PlanWeekPayForm f = new XX_PlanWeekPayForm();
    	f.cmdAvisos();*/
    	fireEditingStopped();
    }

	/**
	 * Implement the one CellEditor method that AbstractCellEditor doesn't.
	 */
    public Object getCellEditorValue() {
        return currentValue;
    }

    /**
     * Implement the one method defined by TableCellEditor.
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    	return this.avisos;
    }

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelCellEditing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopCellEditing() {
		// TODO Auto-generated method stub
		return false;
	}
}
