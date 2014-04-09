package compiere.model.cds.forms;

import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class XX_BudgetRenderer implements TableCellRenderer
	 {
	 

		private static final long serialVersionUID = 1L;

		/**  Returns the component used for drawing the cell.  This method is
	      *  used to configure the renderer appropriately before drawing.
	      *
	      * @param	table		the <code>JTable</code> that is asking the
	      * 				renderer to draw; can be <code>null</code>
	      * @param	value		the value of the cell to be rendered.  It is
	      * 				up to the specific renderer to interpret
	      * 				and draw the value.  For example, if
	      * 				<code>value</code>
	      * 				is the string "true", it could be rendered as a
	      * 				string or it could be rendered as a check
	      * 				box that is checked.  <code>null</code> is a
	      * 				valid value
	      * @param	isSelected	true if the cell is to be rendered with the
	      * 				selection highlighted; otherwise false
	      * @param	hasFocus	if true, render cell appropriately.  For
	      * 				example, put a special border on the cell, if
	      * 				the cell can be edited, render in the color used
	      * 				to indicate editing
	      * @param	row	        the row index of the cell being drawn.  When
	      * 				drawing the header, the value of
	      * 				<code>row</code> is -1
	      * @param	column	        the column index of the cell being drawn
	      *
	      */
	     public Component getTableCellRendererComponent(JTable table, Object value,
	        boolean isSelected, boolean hasFocus, int row, int column) 
	     {
	    	 JFormattedTextField label = null;
	        	 
	        if(column==0){
	        	label = new JFormattedTextField();
	        	label.setText((String)value);
	        }
	        else{
	        	label = new JFormattedTextField();
	        	label.setHorizontalAlignment(JTextField.RIGHT);
	        	label.setValue(value);
	        }
	        
	        if(column==0){
	        	
		        if(row==0 || row==2 || row==12 || row==17 || row==19 || row==21 || row==23 || row==25)
		        	label.setBackground (Color.GRAY);
		        else if(row==3 || row==7 || row==8 || row==18)
		        	label.setBackground(Color.ORANGE);
		        else if(row==28)	
		        	label.setBackground (Color.YELLOW);
		        else
		        	label.setBackground(Color.lightGray);
	        }
	        else{
	        	if(row==0 || row==2 || row==12 || row==17 || row==19 || row==21 || row==23 || row==25)
		        	label.setBackground (Color.GRAY);
	        	else if(row==28)	
		        	label.setBackground (Color.YELLOW);
	        	else if(row==3 || row==7 || row==8 || row==18)
		        	label.setBackground(Color.ORANGE);
	        	else
		        	label.setBackground(Color.lightGray);
	        }
	        
	        if(isSelected){
	        	label.setBackground(Color.cyan);
	        }
	        
	        label.setOpaque(true);
	        label.setBorder(null);
	         
	         return label;
	     }

	}