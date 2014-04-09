package compiere.model.cds;

import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;

public class MiniTablePreparator extends MiniTable {

	private static final long serialVersionUID = 14581172146967136L;
	
	
	/**Modifica el minitable, le coloca las columnas y genera un sql*/
	public String prepareTable(ColumnInfo[] layout, 
			String from, String where, boolean multiSelection, String tableName)
	{
			m_layout = layout;		
			setRowSelectionAllowed(true);
			setMultiSelection(multiSelection);
			
			String sql = "SELECT ";
			//  add columns & sql
			for (int i = 0; i < layout.length; i++)
			{
				//  create sql
				if (i > 0)
					sql += ", ";
				sql += layout[i].getColSQL();
				//  adding ID column
				if (layout[i].isKeyPairCol())
					sql += "," + layout[i].getKeyPairColSQL();

				//  add to model
				addColumn(layout[i].getColHeader());
				if (layout[i].isColorColumn())
					setColorColumn(i);
				if (layout[i].getColClass() == IDColumn.class)
					p_keyColumnIndex = i;
			}
			//  set editors (two steps)
			for (int i = 0; i < layout.length; i++)
				setColumnClass(i, layout[i].getColClass(), layout[i].isReadOnly(), 
						layout[i].getColHeader(), layout[i].getWidth());
			return sql;
		}   //  prepareTable
	
	
	/** Constructor */
	public MiniTablePreparator () {
		super();
	}


}
