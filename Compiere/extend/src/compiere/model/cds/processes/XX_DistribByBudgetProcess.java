package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;

import compiere.model.cds.forms.XX_DistribByBudgetForm;


public class XX_DistribByBudgetProcess extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		FormFrame form = new FormFrame();
		
		XX_DistribByBudgetForm.setContext(getCtx());
		XX_DistribByBudgetForm.setRecord(getRecord_ID());
		XX_DistribByBudgetForm.setTrxName(get_TrxName());
		
		form.openForm(getCtx().getContextAsInt("#XX_L_FORMDIST_BUDGET_ID"));
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return null;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
}



/*import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.model.MProduct;
import org.compiere.process.SvrProcess;
import org.compiere.swing.CButton;
import org.compiere.util.DB;

import compiere.model.cds.X_XX_VMR_DistribProductDetail;
import compiere.model.cds.X_XX_VMR_DistributionDetail;
import compiere.model.cds.X_XX_VMR_StorePercentDistrib;

public class XX_DistribByBudgetProcess extends SvrProcess implements TableModelListener{

	//private boolean DEBUG = true;
	Vector <Integer> codes = new Vector <Integer>();
	Vector <String> stores = new Vector <String>();
	Vector <Float> percentages = new Vector <Float>();
	Vector <Boolean> selection = new Vector <Boolean>();
	Vector<Float> budget = new Vector<Float>();
	
	MyTableModel myModel;
    JTable table;
    JFrame frame;
    
	CButton buttonA;
	CButton buttonB;
	//Dimension d = new Dimension(600,95);
	
	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub

			
		 	myModel = new MyTableModel();
	        table = new JTable(myModel);
	        table.setPreferredScrollableViewportSize(new Dimension(500, 130));
	       

	        IndicatorCellRenderer renderer = new IndicatorCellRenderer(0, 100);
	        renderer.setStringPainted(true);
	     
	        renderer.setBackground(table.getBackground());

	        // set limit value and fill color
	        Hashtable<Integer, Color> limitColors = new Hashtable<Integer, Color>();
	        limitColors.put(new Integer(0), Color.green);
	        limitColors.put(new Integer(60), Color.yellow);
	        limitColors.put(new Integer(80), Color.red);
	        renderer.setLimits(limitColors);
	        table.getColumnModel().getColumn(3).setCellRenderer(renderer);
	        
	        JScrollPane scrollPane = new JScrollPane(table);
	        JPanel panel = new JPanel();
            panel.add(scrollPane);
            panel.setBorder(
 					new TitledBorder(null,
 					"Almacenes BECO", TitledBorder.LEFT, TitledBorder.BELOW_TOP));
 			
            buttonA = new CButton();
        	panel.add(buttonA);
			//Dimension dimA = new Dimension(130,30);
			//buttonA.setPreferredSize(dimA);
			buttonA.setText("Recalcular Distribución");
			
            buttonB = new CButton();
        	panel.add(buttonB);
			//Dimension dimB = new Dimension(130,30);
			//buttonB.setPreferredSize(dimB);
			buttonB.setText("Aplicar Distribución");
		
			
  			frame = new JFrame("Distribución por Ventas Presupuestadas");
  			frame.add(panel);
 			frame.pack();
 	        frame.setVisible(true);
  			frame.setSize(650, 400);
  			frame.setLocation(290, 150);
  	        frame.setIconImage(new ImageIcon("../client/Src/org/compiere/images/C16.png").getImage());
	        frame.setResizable(false);
	       
	        buttonA.addActionListener(
				    new ActionListener() {
				        public void actionPerformed(ActionEvent e) {
				        					        
			        		recalculateDistribution();
			        		//table.updateUI();
				        }
				    }
				);
	        buttonB.addActionListener(
				    new ActionListener() {
				        public void actionPerformed(ActionEvent e) {
				        	
				        	applyDistribution();		        		
			        		//table.updateUI();
				        }
				    }
				);
		return null;
	}
	
	
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
	}
	
	
	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		if(e.getColumn()==0){
			buttonB.setEnabled(false);
			System.out.println("ENTRO AL LISTENER");
		}
	}
	
	
	private void applyDistribution(){
		
		X_XX_VMR_DistributionDetail detail = new X_XX_VMR_DistributionDetail(getCtx(), getRecord_ID(), get_TrxName());
		int result = 0;
		
		String SQL_d = "DELETE FROM XX_VMR_STOREPERCENTDISTRIB WHERE" +
				" XX_VMR_DISTRIBUTIONHEADER_ID = " + detail.getXX_VMR_DistributionHeader_ID() +
				" AND XX_VMR_DISTRIBUTIONDETAIL_ID = " + detail.getXX_VMR_DistributionDetail_ID(); 
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL_d, null);
			result = pstmt.executeUpdate();
			pstmt.close();
			
		}catch(SQLException e){
			log.log(Level.SEVERE, SQL_d, e);
		}
		
		
		String SQL_e = "DELETE FROM XX_VMR_STOREQUANTITYDISTRIB WHERE" +
		" XX_VMR_DISTRIBUTIONHEADER_ID = " + detail.getXX_VMR_DistributionHeader_ID() +
		" AND XX_VMR_DISTRIBUTIONDETAIL_ID = " + detail.getXX_VMR_DistributionDetail_ID(); 

		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL_e, null);
			result = pstmt.executeUpdate();
			pstmt.close();
			
		}catch(SQLException e){
			log.log(Level.SEVERE, SQL_d, e);
		}
		
		for(int i = 0; i<selection.size(); i++){
			
			if(selection.get(i) == true){
				
				X_XX_VMR_StorePercentDistrib store_percent = new X_XX_VMR_StorePercentDistrib(getCtx(), 0, get_TrxName());
				store_percent.setXX_VMR_DistributionHeader_ID(detail.getXX_VMR_DistributionHeader_ID());
				store_percent.setXX_VMR_DistributionDetail_ID(detail.getXX_VMR_DistributionDetail_ID());
				store_percent.setXX_Percentage(new BigDecimal(percentages.get(i)));
				store_percent.setM_Warehouse_ID(codes.get(i));
				store_percent.setXX_VMR_DistributionType_ID(detail.getXX_VMR_DistributionType_ID());
				store_percent.save();
			}
		}
		
		SQL_d = "DELETE FROM XX_VMR_DISTRIBPRODUCTDETAIL WHERE" +
				" XX_VMR_DISTRIBUTIONDETAIL_ID = " + detail.getXX_VMR_DistributionDetail_ID();
		
		
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL_d, null);
			result = pstmt.executeUpdate();
			pstmt.close();
			
		}catch(SQLException e){
			log.log(Level.SEVERE, SQL_d, e);
		}
		
		String SQL_p = "SELECT M_PRODUCT_ID, XX_DESIREDQUANTITY FROM XX_VMR_DISTRIBDETAILTEMP" +
		" WHERE XX_VMR_DISTRIBUTIONDETAIL_ID = " + detail.getXX_VMR_DistributionDetail_ID();

		System.out.println("LOS PORCENTAJES SON: " + percentages.toString());
		
		try{
			PreparedStatement pstmt3 = DB.prepareStatement(SQL_p, null);
			ResultSet rs = pstmt3.executeQuery();
			
			while(rs.next()){
				for (int i = 0; i < selection.size(); i++){
					if(percentages.get(i) != 0){
						System.out.println("TIENDA " + stores.get(i));
						X_XX_VMR_DistribProductDetail productDet = new X_XX_VMR_DistribProductDetail(getCtx(),0,get_TrxName());
						productDet.setXX_VMR_DistributionHeader_ID(detail.getXX_VMR_DistributionHeader_ID());
						productDet.setXX_VMR_DistributionDetail_ID(detail.getXX_VMR_DistributionDetail_ID());
						productDet.setM_Product_ID(rs.getInt("M_PRODUCT_ID"));
						productDet.setM_Warehouse_ID(codes.get(i));
						Float factor = new Float( percentages.get(i)/100 );
						System.out.println("CANTIDAD DESEADA:" + rs.getInt(2));
						System.out.println("EL FACTOR ES:" + factor);
						int qty = new Float(rs.getInt("XX_DESIREDQUANTITY")*factor).intValue();
						productDet.setXX_QUANTITY(qty);
						System.out.println("LA CANTIDAD ES: " + qty);
						productDet.save();
					}
				}
			}
		}catch(SQLException e){
			log.log(Level.SEVERE, SQL_p, e);
		}
		frame.dispose();
	}
	
	private void recalculateDistribution(){
		
		for(int i = 0; i < selection.size(); i++){
			selection.set(i, (Boolean)table.getValueAt(i, 0));
		}
		myModel.repaint();
		buttonB.setEnabled(true);
		table.updateUI();
		table.getModel().addTableModelListener(this);
	}
	

	 class MyTableModel extends AbstractTableModel {
		 
	
	    /**
		 * 
		 */
		/*private static final long serialVersionUID = 1L;
		
		
		String[] columnNames = {"Opción", "Almacen", "% Distribución", "Grafica"};
	    
		Object[][] distribution = gettingBudgetDistrib();
	    
	    private Object[][] gettingBudgetDistrib(){
	    	
			Calendar actualDate = Calendar.getInstance();
			int year = actualDate.get(Calendar.YEAR);
			int month = actualDate.get(Calendar.MONTH) + 1;
			String yearmonth;
			
			
			if(month < 10)
				yearmonth = year + "0" + month;
			else
				yearmonth = "" + year + month;
			
			    	
			X_XX_VMR_DistributionDetail detail = new X_XX_VMR_DistributionDetail(getCtx(), getRecord_ID(), get_TrxName());
			
			int distribType = 0;
			
			//getting the warehouses existing on budget and the respective budget 
			int dept = detail.getXX_VMR_Department_ID();
			int line = detail.getXX_VMR_Line_ID();
			int sect = detail.getXX_VMR_Section_ID();
			
			String sql_p = "SELECT wh.M_WAREHOUSE_ID, wh.NAME, ip.XX_CANVENPRE FROM I_XX_VMR_PRLD01 ip" 
						 + " INNER JOIN M_WAREHOUSE wh ON (ip.XX_CODTIE = wh.VALUE)"
						 + " INNER JOIN XX_VMR_DEPARTMENT de ON (ip.XX_CODDEP = de.VALUE)";
			
			String temp = "";
			
			if(line != 0){
				sql_p = sql_p + " INNER JOIN XX_VMR_LINE li ON (ip.XX_CODLIN = li.VALUE)";
				temp = temp + " AND li.XX_VMR_LINE_ID = " + line;
			}
			else
				temp = temp + " AND ip.XX_CODLIN = 99";
			
			if(sect != 0){
				sql_p = sql_p + " INNER JOIN XX_VMR_SECTION se ON (ip.XX_CODSEC = se.VALUE)";
				temp = temp + " AND se.XX_VMR_SECTION_ID = " + sect;
			}
			else
				temp = temp + " AND ip.XX_CODSEC = 99";
			 
			sql_p = sql_p + " WHERE ip.XX_AÑOMESPRE = " + yearmonth + " AND de.XX_VMR_DEPARTMENT_ID = " +
					dept + temp + " ORDER BY wh.M_WAREHOUSE_ID";
			
			System.out.println("SQL_P: " + sql_p);
			
			try{
				PreparedStatement pstmt = DB.prepareStatement(sql_p, null);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next())
				{	
					System.out.println("HELLO!!!");
					selection.add(false);
					codes.add(rs.getInt("M_WAREHOUSE_ID"));
					stores.add(rs.getString("NAME"));
					budget.add(rs.getFloat("XX_CANVENPRE"));
					percentages.add((float)0);
				}
				
				rs.close();
				pstmt.close();						
			}
			catch(SQLException e)
			{	
				e.getMessage();
			}
			
			//getting total budget 
			
			String sql_t = "SELECT XX_CANVENPRE FROM I_XX_VMR_PRLD01 ip" 
				 + " INNER JOIN XX_VMR_DEPARTMENT de ON (ip.XX_CODDEP = de.VALUE)";
				
			if(line != 0){
				sql_t = sql_t + " INNER JOIN XX_VMR_LINE li ON (ip.XX_CODLIN = li.VALUE)";
				temp = temp + " AND li.XX_VMR_LINE_ID = " + line;
			}
			else
				temp = temp + " AND ip.XX_CODLIN = 99";
			
			if(sect != 0){
				sql_t = sql_t + " INNER JOIN XX_VMR_SECTION se ON (ip.XX_CODSEC = se.VALUE)";
				temp = temp + " AND se.XX_VMR_SECTION_ID = " + sect;
			}
			else
				temp = temp + " AND ip.XX_CODSEC = 99";
			 
			sql_t = sql_t + " WHERE XX_CODTIE = 99 AND ip.XX_AÑOMESPRE = " + yearmonth + " AND de.XX_VMR_DEPARTMENT_ID = " +
					dept + temp;
			
			float totalBudget = 0;
			
			System.out.println("sql_t: " + sql_t);
			
			try 
			{
				PreparedStatement pstmt = DB.prepareStatement(sql_t, null);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next())
				{	
					totalBudget = rs.getFloat("XX_CANVENPRE");
				}
										
				rs.close();
				pstmt.close();
				
			}
			catch(SQLException e)
			{	
				e.getMessage();
			}
			
			//getting distribution by budget applied before (if any)
			String sql_w = "SELECT * FROM XX_VMR_STOREPERCENTDISTRIB WHERE" +
					" XX_VMR_DISTRIBUTIONHEADER_ID = " + detail.getXX_VMR_DistributionHeader_ID() + 
					" AND XX_VMR_DISTRIBUTIONDETAIL_ID = " + detail.getXX_VMR_DistributionDetail_ID();
			
			boolean pass = false; //Variable indicates if there was any actualization on vector Percentages
			
			System.out.println("SQL_w: " + sql_w);
			
			try 
			{
				PreparedStatement pstmt = DB.prepareStatement(sql_w, null);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next())
				{	
					for(int i = 0; i<codes.size(); i++){
						System.out.println("-----ENTRADA #"+i);
						System.out.println(rs.getInt("M_WAREHOUSE_ID") + "=" + codes.get(i));
						
						if(codes.get(i).equals(rs.getInt("M_WAREHOUSE_ID"))){
							percentages.set(i, rs.getFloat("XX_PERCENTAGE"));
							selection.set(i, true);
							distribType = rs.getInt("XX_VMR_DISTRIBUTIONTYPE_ID");
							pass = true;
						}
					}
				}
										
				rs.close();
				pstmt.close();
				
			}
			catch(SQLException e)
			{	
				e.getMessage();
			}
			
			//means this is the first time that is applying this distribution, or exists another distribution before
			if(!pass || (pass && distribType != detail.getXX_VMR_DistributionType_ID())){
				System.out.println("ENTRANDO");
				for (int i = 0; i < codes.size(); i++){
					percentages.set(i, (budget.get(i)/totalBudget)*100);
					selection.set(i, true);
				}
			}
			
			System.out.println("PORCENTAJES : " + percentages.toString());
			System.out.println("CODIGOS: " + codes.toString());
			System.out.println("Seleccion" + selection.toString());
			
			
			Object[][] data = new Object[stores.size()][4];
			
			for(int i=0;i<stores.size();i++)
			{	
			    data[i][0] = selection.get(i); //CHECKMARK
				data[i][1] = stores.get(i); // NAME
				data[i][2] = percentages.get(i); //%
				data[i][3] = percentages.get(i); //% GRÁFICO
			}
			
	    	return data;
	    }
	    
	    public void repaint(){
	    	
	    	
	    	Object[][] data = new Object[stores.size()][4];
	    	float totalBudget = 0;
	    	for(int i = 0; i < stores.size(); i++){
	    		if(selection.get(i) == true)
	    			totalBudget = totalBudget + budget.get(i);
	    	}
	    	
	    	for(int i = 0; i < stores.size(); i++){
	    		if(selection.get(i)==true){
	    			float per = (budget.get(i)/totalBudget)*100;
	    			percentages.set(i, per);
	    			
	    		}else{
	    			percentages.set(i, (float)0);
	    		}
	    	}
	    	
			for(int i=0;i<stores.size();i++)
			{	
			    data[i][0] = selection.get(i); //CHECKMARK
				data[i][1] = stores.get(i); // NAME
				data[i][2] = percentages.get(i); //%
				data[i][3] = percentages.get(i); //% GRÁFICO
			}
	    	
			System.out.println("RECALCULATED SELECTION: " + selection.toString());
			System.out.println("RECALCULATED STORES: " + stores.toString());
			System.out.println("RECALCULATED PERCENTAGES: " + percentages.toString());
			distribution = data;
	    	//return data;
	    }
	    
	    public int getColumnCount() {
	        return columnNames.length;
	    }
	    
	    public int getRowCount() {
	        return distribution.length;
	    }
	
	    public String getColumnName(int col) {
	        return columnNames[col];
	    }
	
	    public Object getValueAt(int row, int col) {
	        return distribution[row][col];
	    }
	
	    /*
	     * JTable uses this method to determine the default renderer/
	     * editor for each cell.  If we didn't implement this method,
	     * then the last column would contain text ("true"/"false"),
	     * rather than a check box.
	     */
	   /* public Class getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
	    }
	
	    /*
	     * Don't need to implement this method unless your table's
	     * editable.
	     */
	    /*public boolean isCellEditable(int row, int col) {
	        //Note that the data/cell address is constant,
	        //no matter where the cell appears onscreen.
	        if (col > 0) { 
	            return false;
	        } else {
	            return true;
	        }
	    }
	
	    /*
	     * Don't need to implement this method unless your table's
	     * data can change.
	     */
	    /*public void setValueAt(Object value, int row, int col) {
	    	 
	    	distribution[row][col]=value;
	    /* if (DEBUG) {
	            System.out.println("Setting value at " + row + "," + col
	                               + " to " + value
	                               + " (an instance of " 
	                               + value.getClass() + ")");
	        }
	
	        if (data[0][col] instanceof Integer) {
	            //If we don't do something like this, the column
	            //switches to contain Strings.
	            //XXX: See TableEditDemo.java for a better solution!!!
	            try 
	            {
	                data[row][col] = new Integer((String)value);
	            }
	            catch (NumberFormatException e)
	            {
	                if (SwingUtilities.isEventDispatchThread())
	                {
	                   // JOptionPane.showMessageDialog(TableDemo.this,
	                     //   "The \"" + getColumnName(col)
	                      // + "\" column accepts only integer values.");
	                } else 
	                {
	                    System.err.println("User attempted to enter non-integer"
	                                   + " value (" + value 
	                                 + ") into an integer-only column.");
	                }
	            }
	        }
	        else
	        {
	            data[row][col] = value;
	        }
	
	        if (DEBUG) {
	            System.out.println("New value of data:");
	            printDebugData();
	        }*/
	    /*}
	
	    private void printDebugData() {
	        int numRows = getRowCount();
	        int numCols = getColumnCount();
	
	        for (int i=0; i < numRows; i++) {
	            System.out.print("    row " + i + ":");
	            for (int j=0; j < numCols; j++) {
	                System.out.print("  " + distribution[i][j]);
	            }
	            System.out.println();
	        }
	        System.out.println("--------------------------");
	    }
	}
 
	 class IndicatorCellRenderer extends JProgressBar implements TableCellRenderer {
		  private Hashtable limitColors;
	
		  private int[] limitValues;
	
		  public IndicatorCellRenderer() {
		    super(JProgressBar.HORIZONTAL);
		    setBorderPainted(false);
		  }
	
		  public IndicatorCellRenderer(int min, int max) {
		    super(JProgressBar.HORIZONTAL, min, max);
		    setBorderPainted(false);
		  }
	
		  public Component getTableCellRendererComponent(JTable table, Object value,
		      boolean isSelected, boolean hasFocus, int row, int column) {
		    int n = 0;
		    if (!(value instanceof Number)) {
		      String str;
		      if (value instanceof String) {
		        str = (String) value;
		      } else {
		        str = value.toString();
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
		    setString(value.toString()+" %");
		    setValue(n);
		    return this;
		  }
	
		  public void setLimits(Hashtable limitColors) {
		    this.limitColors = limitColors;
		    int i = 0;
		    int n = limitColors.size();
		    limitValues = new int[n];
		    Enumeration e = limitColors.keys();
		    while (e.hasMoreElements()) {
		      limitValues[i++] = ((Integer) e.nextElement()).intValue();
		    }
		    sort(limitValues);
		  }
	
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
		  }
	
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
		  }
		}	
}*/
