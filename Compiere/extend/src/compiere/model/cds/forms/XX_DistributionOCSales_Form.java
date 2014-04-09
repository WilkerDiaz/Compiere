package compiere.model.cds.forms;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;

import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VComboBox;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;


import compiere.model.cds.X_XX_VMR_DistributionHeader;
import compiere.model.cds.MOrder;

import compiere.model.cds.processes.XX_PercentualDistribution;

import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.border.TitledBorder;

/**
 *  
 *  @author     Javier Pino
 *  @version    
 */
public class XX_DistributionOCSales_Form extends CPanel
	implements FormPanel, ActionListener {
	
	/**
	 * Vectors that will hold the information during the execution
	 * */
	Vector <Integer> vectorIDWarehouse = new Vector <Integer>();
	Vector <String> vectorNameWarehouse = new Vector <String>();
	Vector<Double> percentages = new Vector <Double>();
	int position_cd = 0;
	
	private X_XX_VMR_DistributionHeader header = null;
	private MOrder order = null;
	private Trx trans = null;
		
	private static final long serialVersionUID = 1L;

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);

		Ctx aux = Env.getCtx();		
		int header_id = aux.getContextAsInt("#XX_VMR_Sales_DistributionHeader_ID");				
		trans = Trx.get("XX_CHANGE_PO_SALES");
			//Remove the no longer necessary items on the context		
		aux.remove("#XX_VMR_Sales_DistributionHeader_ID");		

			//Creates the Header with the necessary information					
		header = 
			new X_XX_VMR_DistributionHeader(aux, header_id , trans);
		order = new MOrder(aux, header.getC_Order_ID(), trans); 
		try
		{
			//	UI
			jbInit();
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_DistributionOCSales_Form.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
			
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CButton calculate = new CButton();
	private CButton generate = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();

	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xProductScrollPane = new JScrollPane();
	private TitledBorder xProductBorder =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_StoreProductDistribution"));
	private MiniTable xProductTable = new MiniTable();
	private JScrollPane xAssociateScrollPane = new JScrollPane();
	
	private MiniTable xAssociateTable = new MiniTable();
	private CPanel xPanel = new CPanel();

	private CLabel monthLabel = new CLabel();
	private CLabel yearLabel = new CLabel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private VComboBox month = new VComboBox();
	private VComboBox year = new VComboBox();
	
	/**
	 *  Static Init.
	 *  <pre>
	 *  mainPanel
	 *      northPanel
	 *      centerPanel
	 *          xMatched
	 *          xPanel
	 *          xMathedTo
	 *      southPanel
	 *  </pre>
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
			
		calculate.setText(Msg.translate(Env.getCtx(), "XX_CalculateDistribution"));
		calculate.setEnabled(true);
		
		southPanel.setLayout(southLayout);
		generate.setText(Msg.translate(Env.getCtx(), "XX_GeneratePercentage"));
		generate.setEnabled(true);
		
		centerPanel.setLayout(centerLayout);
		xProductScrollPane.setBorder(xProductBorder);
		xProductScrollPane.setPreferredSize(new Dimension(470, 200));
		xAssociateScrollPane.setPreferredSize(new Dimension(450, 58));
		
		xPanel.setLayout(xLayout);
		monthLabel.setText(Msg.translate(Env.getCtx(), "Month"));
		yearLabel.setText(Msg.translate(Env.getCtx(), "Year"));
		
		northPanel.add(monthLabel,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		northPanel.add(month,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(yearLabel,      new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(year,        new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xProductScrollPane,  BorderLayout.CENTER);
		xProductScrollPane.getViewport().add(xProductTable, null);

		xAssociateScrollPane.getViewport().add(xAssociateTable, null);
			
		southPanel.add(calculate,   new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
		southPanel.add(generate,   new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
		}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit() {	
		//Data representing the Months	
		month.addItem(Msg.translate(Env.getCtx(),"XX_January"));
		month.addItem(Msg.translate(Env.getCtx(),"XX_February"));
		month.addItem(Msg.translate(Env.getCtx(),"XX_March"));
		month.addItem(Msg.translate(Env.getCtx(),"XX_April"));
		month.addItem(Msg.translate(Env.getCtx(),"XX_May"));
		month.addItem(Msg.translate(Env.getCtx(),"XX_June"));
		month.addItem(Msg.translate(Env.getCtx(),"XX_July"));
		month.addItem(Msg.translate(Env.getCtx(),"XX_August"));
		month.addItem(Msg.translate(Env.getCtx(),"XX_September"));
		month.addItem(Msg.translate(Env.getCtx(),"XX_October"));
		month.addItem(Msg.translate(Env.getCtx(),"XX_November"));
		month.addItem(Msg.translate(Env.getCtx(),"XX_December"));
		month.setEditable(false);

		Calendar now = Calendar.getInstance();
		int current_month = now.get(Calendar.MONTH);
		int current_year = now.get(Calendar.YEAR);
		
		//Fills the Combo box with the last ten years  
		for (int i = current_year - 10 ; i <= current_year; i++) {
			year.addItem(new Integer(i));			
		}
		year.setEditable(false);
						
		//Add the column definition for the table
		ColumnInfo[] layout = new ColumnInfo[] {			
				new ColumnInfo(Msg.translate(Env.getCtx(), ""), "", Boolean.class, false, false, ""),//  1
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Store"),   ".", String.class),             //  2
				new ColumnInfo(Msg.translate(Env.getCtx(), "Percentage"),   ".", String.class),             //  2
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Chart"),   ".", Double.class)             //  2
			};
		xProductTable.prepareTable(layout, "", "", false, "");		


		//  Visual
		CompiereColor.setBackground (this);

		//  Listener
		xProductTable.getColumnModel().getColumn(1).setPreferredWidth(130);
		calculate.addActionListener(this);
		generate.addActionListener(this);

		//  Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);

		XX_IndicatorCellRenderer renderer = new XX_IndicatorCellRenderer(0, 100);
        renderer.setStringPainted(false);

        // set limit value and fill color
        Hashtable<Integer, Color> limitColors = new Hashtable<Integer, Color>();
        	limitColors.put(new Integer(0), Color.green);
        	limitColors.put(new Integer(60), Color.yellow);
        	limitColors.put(new Integer(80), Color.red);
        renderer.setLimits(limitColors);
        
        xProductTable.setSortEnabled(false);
        xProductTable.getTableHeader().setReorderingAllowed(false);
        xProductTable.getTableHeader().setFocusable(false);
        xProductTable.getColumnModel().getColumn(3).setCellRenderer(renderer);		
        
      //Jorge Pires - Ordenar tiendas!
      //Get the Store warehouses to work with
    	String SQL = "SELECT M_WAREHOUSE_ID, VALUE||'-'||NAME FROM M_WAREHOUSE WHERE ISACTIVE = 'Y' ";    			
		SQL = MRole.getDefault().addAccessSQL(
				SQL.toString(), "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO)
				+ " ORDER BY VALUE||'-'||NAME";	
		try 
		{	
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			int iterations = 0;
			while(rs.next()) {	
				vectorIDWarehouse.add(rs.getInt(1));				
				vectorNameWarehouse.add(rs.getString(2));
				
				if (rs.getInt(1) == Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID")) {
					position_cd = iterations; 
				} 
				iterations++;
			}
			rs.close();
			pstmt.close();
		}		
		catch(SQLException e)
		{	
			e.getMessage();
		}
		
		year.addActionListener(this);
		month.addActionListener(this);
		
        
		/*  Sets the default value for the vcombobox,
		 *  the previous month}
		 *  Checks if the month is January, and has to change de default year
		 * */			
		if (current_month == 0) {
			month.setSelectedIndex(11);			
			year.setSelectedItem(current_year - 1);
		} else {
			month.setSelectedIndex(current_month - 1);
			year.setSelectedItem(current_year);			
		}	
	}   //  dynInit
	
	
    private Object[][] dataWarehouse(){    	   	
    					
		
		//Fill the data matrix
		Object[][] data = new Object[vectorNameWarehouse.size()][4];		
		try		
		{			
			//Get selected year and month
			int c_year = 0, c_month = month.getSelectedIndex();
			c_month++; //SQL_MANAGES MONTH LIKE 1-12 NOT 0-11 LIKE JAVA
			c_year = (Integer)year.getSelectedItem();
					
			String get_sum = 
				" SELECT C.M_WAREHOUSE_ID STORE,  SUM(XX_PRODUCTQUANTITY) QTY FROM C_ORDER C" ;					
			get_sum = MRole.getDefault().addAccessSQL(
					get_sum, "C", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);			
			get_sum += 	" AND C.ISSOTRX = 'Y' " + 
				" AND C.M_WAREHOUSE_ID IS NOT NULL AND C.M_WAREHOUSE_ID <> " + 
					Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID") +
				" AND C.XX_VMR_DEPARTMENT_ID =  " + order.getXX_VMR_DEPARTMENT_ID() + 				
				" AND TO_CHAR(C.CREATED,'MM') = " + c_month +  
				" AND TO_CHAR(C.CREATED,'YYYY') = " + c_year +   
				" GROUP BY C.M_WAREHOUSE_ID";							
			PreparedStatement pstmt = DB.prepareStatement(get_sum, null);			
				//Setting the query parameters
			ResultSet rs = pstmt.executeQuery();			
			
			Long sumaDivisor = new Long(0);
			Long suma = new Long(0);	
			int warehouse = 0;
			DecimalFormat format = new DecimalFormat("#,##0.00");
			Vector<Long> warehouse_sum = new Vector <Long>();
			warehouse_sum.setSize(vectorIDWarehouse.size());
			percentages.clear();
			
			percentages.setSize(vectorIDWarehouse.size());
			while(rs.next()) {
				warehouse = rs.getInt(1);
				int i = vectorIDWarehouse.indexOf(warehouse);
				if (i != -1) {										
					suma = rs.getLong(2);
					sumaDivisor += suma;
					warehouse_sum.setElementAt(suma, i);
				}					
			}
			rs.close();
			pstmt.close();
			for (int i = 0 ; i < vectorIDWarehouse.size(); i++) {
				if (i == position_cd) data[i][0] = false;
				else data[i][0] = true;
				
				data[i][1] = vectorNameWarehouse.get(i);				
				if (sumaDivisor.equals(0.0)) {
					data[i][2]= format.format(0.0);
					data[i][3]= 0.0;
					percentages.setElementAt(0.0, i);
				} else {
					Double formula = 0.0;
					if (warehouse_sum.elementAt(i) != null) 					
						formula = (warehouse_sum.elementAt(i).doubleValue()/sumaDivisor)*100;
					if(formula.isNaN()) {						
						data[i][2]= format.format(0.0);
						data[i][3]= 0.0;
						percentages.setElementAt(0.0, i);
					} else {
						data[i][2]= format.format(formula);
						data[i][3]= formula;
						percentages.setElementAt(formula, i);
							
					}	
				}
			}			
			data[position_cd][0]= false;			
		}
		catch(Exception E) {
			E.printStackTrace();
		}
		return data;	
	}

    public void generatePercentages() {
    	
    	boolean re_generate = true;
    	DecimalFormat format = new DecimalFormat("#,##0.00");
    	
    	double sum = 0;
    	for (int i = 0 ; i < xProductTable.getRowCount(); i++) {	
			if (i == position_cd) continue;    		
    		boolean selected = (Boolean)xProductTable.getValueAt(i, 0);
			if (!selected ) { 
				re_generate = false;				
			} else {
				sum += percentages.get(i);
			} 			
		}       	
    	if (!re_generate) {    		
    		xProductTable.setValueAt( true, position_cd, 0);
    		for (int i = 0 ; i < xProductTable.getRowCount(); i++) {	
        		boolean selected = (Boolean)xProductTable.getValueAt(i, 0);
    			if (!selected ) { 
    				xProductTable.setValueAt(format.format(0.0), i, 2) ;
    				xProductTable.setValueAt(0, i, 3) ;
    			} else {
    				xProductTable.setValueAt(format.format(percentages.get(i)), i, 2) ;
    				xProductTable.setValueAt(percentages.get(i), i, 3) ;
    			}
    		}
    		if ( (100 - sum) != 0) {
    			xProductTable.setValueAt( 100 - sum, position_cd, 3);
    			xProductTable.setValueAt( format.format(100 - sum), position_cd, 2);
    		}
    		return;
    	}     	
    }
	
    
    // Boton B Generar Distribucion
	
    public void generateDistribution() {    	
    	
    	Vector<Double> perc = new Vector<Double>();
    	double cd_perc = 0.0, total = 0.0; 
    	    	
      	for (int i = 0 ; i < xProductTable.getRowCount(); i++) {	
			double per = ((Number)xProductTable.getValueAt(i, 3)).doubleValue(); 
      		total += per;
			if (i == position_cd) 
      			cd_perc = per;      		
      		perc.add(per);				
		}  		
		if (cd_perc == 100 || total == 0.0) {
    		ADialog.error(m_WindowNo, m_frame, "XX_SumPercentagesIsZero");
    		return;
		}   
    	
		//Modificado GHUCHET. Se agregó atributo isAutoAdjustement = 0
 		if (XX_PercentualDistribution.applyPOPercentualDistribution(
 				header,	perc, vectorIDWarehouse, m_WindowNo, m_frame, 0)) {						
 			dispose();
 		}
    }

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	
	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));		
		if (e.getSource() == calculate)
			generatePercentages();		
		else if (e.getSource() == generate)
			generateDistribution();
		else if ( (e.getSource() == month) || (e.getSource() == year) ) {
			Object [][] data = dataWarehouse();
	    	xProductTable.setRowCount(0);
	    	for(int i = 0; i < vectorNameWarehouse.size() ; i++) {
				xProductTable.setRowCount (i + 1);			
				xProductTable.setValueAt(data[i][0], i,0);
				xProductTable.setValueAt(data[i][1], i,1);
				xProductTable.setValueAt(data[i][2], i,2);
				xProductTable.setValueAt(data[i][3], i,3);
			}
	    	xProductTable.setValueAt( false, position_cd, 0);
	    	xProductTable.autoSize();	
		}
			
	}   //  actionPerformed
}
