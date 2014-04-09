package compiere.model.payments;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.compiere.swing.CButton;

public class myrenderer extends JLabel implements TableCellRenderer{

	private static final long serialVersionUID = 1L;
	boolean isBordered = true;
	CButton avisos = new CButton("Avisos");

    public myrenderer(boolean isBordered) {
        this.isBordered = isBordered;
        setOpaque(true);
    }

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		avisos.setBackground(Color.PINK);
		return avisos;
	}
}
