package compiere.model.cds.forms;

import java.awt.Color;
import java.awt.Component;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class XX_IndicatorCellRenderer extends JProgressBar implements TableCellRenderer {

		private static final long serialVersionUID = 1L;
		private Hashtable<Integer,Color> limitColors;
		private int[] limitValues;

		public XX_IndicatorCellRenderer(int min, int max) {
			super(JProgressBar.HORIZONTAL, min, max);
			setBorderPainted(false);
		}// Fin XX_IndicatorCellRenderer

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			int n = 0;
			if (!(value instanceof Number)) {
				String str;
				if (value instanceof String) {
					str = (String) value;
				} else {
					str = (value == null) ? "": value.toString();
				}
				try {
					n = Integer.valueOf(str).intValue();
				} catch (NumberFormatException ex) {
				}
			} else {
				n = ((Number) value).intValue();
			}
			Color color = getColor(n);
			if (color != null) {
				setForeground(color);
			}
			if (value != null)
				setString(value.toString()+" %");
			else 
				setString("");
			setValue(n);
			return this;
		}//Fin getTableCellRendererComponent
		
		public void setLimits(Hashtable<Integer, Color> limitColors) {
			this.limitColors = limitColors;
			int i = 0;
			int n = limitColors.size();
			limitValues = new int[n];
			Enumeration<Integer> e = limitColors.keys();
			while (e.hasMoreElements()) {
				limitValues[i++] = e.nextElement();
			}
			sort(limitValues);
		}//Fin setLimits

		private Color getColor(int value) {
			Color color = null;
			if (limitValues != null) {
				int i;
				for (i = 0; i < limitValues.length; i++) {
					if (limitValues[i] < value) {
						color = (Color) limitColors
						.get(new Integer(limitValues[i]));
					}
				}
			}
			return color;
		}//Fin getColor

		private void sort(int[] a) {
			int n = a.length;
			for (int i = 0; i < n - 1; i++) {
				int k = i;
				for (int j = i + 1; j < n; j++) {
					if (a[j] < a[k]) {
						k = j;
					}
				}
				int tmp = a[i];
				a[i] = a[k];
				a[k] = tmp;
			}
		}// Fin sort
	}
