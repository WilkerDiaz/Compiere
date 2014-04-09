package compiere.model.suppliesservices.forms;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VComboBox;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.MOrder;
import compiere.model.cds.MOrderLine;
import compiere.model.cds.forms.XX_IndicatorCellRenderer;
import compiere.model.suppliesservices.processes.XX_SalesPercentage;
 
public class XX_DistribSalesForm extends CPanel implements FormPanel, ActionListener, ListSelectionListener{
	// Tomado de Distribución (Javier Pino) y modificado para Bienes y Servicios
	/** Purchase of Supplies and Services
	 * Maria Vintimilla Funcion 21 
	 * Distribution By Sales (Stores)**/
	
	/**
	 * Vectors that will hold the information during the execution
	 * */
	Vector <Integer> vectorIDWarehouse = new Vector <Integer>();
	Vector <Integer> vectorIDOrg = new Vector <Integer>();
	Vector <String> vectorNameWarehouse= new Vector <String>();
	Vector <Double> percentages = new Vector <Double>();
	Vector <Integer> vectorLines = new Vector <Integer>();
	private MOrderLine detail = null;	
	private MOrder order = null;
	private Trx trans = null;
	private static final long serialVersionUID = 1L;
	private int tipo;
	private int detail_id;
	private boolean distribAll;
	private boolean it_work;

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame){
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo + " - AD_Client_ID=" + m_AD_Client_ID + 
				", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		Ctx aux = Env.getCtx();		
		detail_id = aux.getContextAsInt("#C_OrderLineForm_ID");	
		trans = Trx.get("XX_CHANGE_DISTRIB_SALES"); 
		tipo = aux.getContextAsInt("#Tipo");
		
		if(tipo == 1){
			distribAll = true;
			order = new MOrder(Env.getCtx(),detail_id,null);
		}
		else{
			distribAll = false;
			detail = new MOrderLine(aux, detail_id , trans);	
			detail.setXX_IsPiecesPercentage(false);
			detail.setXX_IsAmountDistrib(false);
			detail.setXX_ClearedDistrib(true);
			detail.save();
		}
		
		aux.remove("#C_OrderLineForm_ID");		

		try	{
			//	UI
			jbInit();
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		}
		catch(Exception e){
			log.log(Level.SEVERE, "", e);
		}
	}//	init

	/**	Window No */
	private int m_WindowNo = 0;
	/**	FormFrame */
	private FormFrame  m_frame;
	/**	Logger */
	static CLogger log = CLogger.getCLogger(XX_DistribSalesForm.class);
	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int m_by = Env.getCtx().getAD_User_ID();	
	private CPanel mainPanel = new CPanel();
	private CPanel xPanel = new CPanel();
	private CPanel northPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private CLabel monthLabel = new CLabel();
	private CLabel yearLabel = new CLabel();
	private VComboBox month = new VComboBox();
	private VComboBox year = new VComboBox();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private GridBagLayout northLayout = new GridBagLayout();
	private GridBagLayout southLayout = new GridBagLayout();
	private CButton calculate = new CButton();
	private CButton generate = new CButton();
	private JScrollPane xProductScrollPane = new JScrollPane();
	private JScrollPane xAssociateScrollPane = new JScrollPane();
	private TitledBorder xProductBorder = 
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_StoreProductDistribution"));
	private MiniTable xProductTable = new MiniTable();
	private MiniTable xAssociateTable = new MiniTable();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private static String trxname = null;
	private static Ctx context = null;
	private static int record = 0;
		
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
	private void jbInit() throws Exception {
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		calculate.setText(Msg.translate(Env.getCtx(), "XX_CalculateDistribution"));
		calculate.setEnabled(true);
		southPanel.setLayout(southLayout);
		generate.setText(Msg.translate(Env.getCtx(), "XX_GeneratePieces"));
		generate.setEnabled(true);
		centerPanel.setLayout(centerLayout);
		xProductScrollPane.setBorder(xProductBorder);
		xProductScrollPane.setPreferredSize(new Dimension(500, 230));
		xAssociateScrollPane.setPreferredSize(new Dimension(450, 58));
		xPanel.setLayout(xLayout);
		monthLabel.setText(Msg.translate(Env.getCtx(), "Month"));
		yearLabel.setText(Msg.translate(Env.getCtx(), "Year"));
		northPanel.add(monthLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		northPanel.add(month, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(yearLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(year, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
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
		//ComboBox with Data representing the Months	
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
					
		/*  Sets the default value for the combobox, the previous month
		 *  Checks if the month is January, and has to change the default year */		
		if (current_month == 0) {
			month.setSelectedIndex(11);			
			year.setSelectedItem(year.getItemCount() - 2);
		} else {
			month.setSelectedIndex(current_month - 1);
			year.setSelectedIndex(year.getItemCount() - 1);			
		}
		//Definition of the columns to be displayed
		ColumnInfo[] layout = new ColumnInfo[] {			
				new ColumnInfo(Msg.translate(Env.getCtx(), 
						"XX_Options"), "", Boolean.class, false, false, ""),//  1
				new ColumnInfo(Msg.translate(Env.getCtx(), 
						"XX_Store"),   ".", String.class), //  2
				new ColumnInfo(Msg.translate(Env.getCtx(), 
						"XX_PercentageAssigned"),   ".", Number.class), //  2
				new ColumnInfo(Msg.translate(Env.getCtx(), 
						"XX_Chart"),   ".", Double.class) //  2
			};
		xProductTable.prepareTable(layout, "", "", false, "");	
		//  Visual
		CompiereColor.setBackground (this);
		//  Listener
		xProductTable.getSelectionModel().addListSelectionListener(this);		
		xProductTable.getColumnModel().getColumn(1).setPreferredWidth(130);
		calculate.addActionListener(this);
		generate.addActionListener(this);
		//  Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);

		XX_IndicatorCellRenderer renderer = new XX_IndicatorCellRenderer(0, 100);
        renderer.setStringPainted(false);

        // Set limit value and fill color for the Related Charts
        Hashtable<Integer, Color> limitColors = new Hashtable<Integer, Color>();
        	limitColors.put(new Integer(0), Color.green);
        	limitColors.put(new Integer(60), Color.yellow);
        	limitColors.put(new Integer(80), Color.red);
        renderer.setLimits(limitColors);
	        
        xProductTable.getColumnModel().getColumn(3).setCellRenderer(renderer);		
		TableModel modelProduct = xProductTable.getModel();		
		Object[][] data = dataWarehouse();  		
		for(int i = 0; i < vectorNameWarehouse.size() ; i++){
			xProductTable.setRowCount (i + 1);			
			modelProduct.setValueAt(data[i][0], i,0);
			modelProduct.setValueAt(data[i][1], i,1);
			modelProduct.setValueAt(data[i][2], i,2);
			modelProduct.setValueAt(data[i][3], i,3);
		}
		xProductTable.autoSize();
			
	}   //  dynInit
			
	public static Ctx setContext(Ctx ctx){
		return context = ctx;
	}
		
	public static int setRecord(int rec){
		return record = rec;
	}
		
	public static String setTrxName(String trx){
		return trxname = trx;
	}

    private Object[][] dataWarehouse(){    	   	
    	//Get the Cost Centers (Stores)
    	String SQL = " SELECT M_WAREHOUSE_ID, NAME, AD_ORG_ID " +
    			" FROM M_WAREHOUSE " +
    			" WHERE ISACTIVE = 'Y' " +
    			" AND AD_Client_ID = " + 
				Env.getCtx().getAD_Client_ID() +
    			" AND AD_Org_ID NOT IN (0,1000060,1000067)";
		SQL = MRole.getDefault().addAccessSQL(SQL.toString(), "", 
				MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
				+ " ORDER BY VALUE";		
		try	{	
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();			
			while(rs.next()) {	
				vectorIDWarehouse.add(rs.getInt(1));			
				vectorNameWarehouse.add(rs.getString(2)); 
				vectorIDOrg.add(rs.getInt(3));	
			}
			rs.close();
			pstmt.close();			
		}//Try
		catch(SQLException e)	{	
			e.getMessage();
		}				
			
		//Fill the data matrix
		Object[][] data = new Object[vectorNameWarehouse.size()][4];		
		try	{
			//Get selected year and month
			int c_year = 0, c_month = month.getSelectedIndex();
			c_month++; //SQL_MANAGES MONTH LIKE 1-12 NOT 0-11 LIKE JAVA
			c_year = (Integer)year.getSelectedItem();
			String get_sum = "";
			get_sum += 	" SELECT C.M_WAREHOUSE_ID STORE,  SUM(O.QTYENTERED) QTY " +
					" FROM C_ORDER C JOIN C_ORDERLINE O ON " +
					" (C.C_ORDER_ID = O.C_ORDER_ID) " ;
			get_sum = MRole.getDefault().addAccessSQL(
					get_sum, "C", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);			
			get_sum += 	" AND C.ISSOTRX = 'Y' " +
				" AND C.M_WAREHOUSE_ID IS NOT NULL " +
				" AND TO_CHAR(C.CREATED,'MM') = ? " + 
				" AND TO_CHAR(C.CREATED,'YYYY') = ? " +  
				" AND O.QTYENTERED IS NOT NULL ";
			get_sum += " GROUP BY C.M_WAREHOUSE_ID";				
			//System.out.println(get_sum);
			PreparedStatement pstmt = DB.prepareStatement(get_sum, null);
				
			//Setting the query parameters
			pstmt.setInt(1, c_month ) ;
			pstmt.setInt(2, c_year);
			ResultSet rs = pstmt.executeQuery();
			
			//Calculate the Sales Percentages for the selected month and year
			Double sumaDivisor = 0.0;
			Double suma = 0.0;	
			int Warehouse = 0;
			DecimalFormat format = new DecimalFormat("#,##0.00");
			Vector<Double> Warehouse_sum = new Vector <Double>();
			Warehouse_sum.setSize(vectorIDWarehouse.size());
			while(rs.next()) {					
				sumaDivisor += rs.getDouble(2);// Total Sales
				suma = rs.getDouble(2);	// Quantity	
				Warehouse = rs.getInt(1);				
				int i = vectorIDWarehouse.indexOf(Warehouse);
				if (i >= 0) Warehouse_sum.setElementAt(new Double(suma), i);
			}//Fin While
			for (int i = 0 ; i < vectorIDWarehouse.size(); i++) {
				data[i][0]= new Boolean(true);
				data[i][1] = vectorNameWarehouse.get(i);				
				if (sumaDivisor.equals(0.0)) {
					data[i][2]= format.format(0.0);
					data[i][3]= 0.0;
				}//Fin if 
				else {
					Double formula = 0.0;// % Sales
					if (Warehouse_sum.elementAt(i) != null) 					
						formula = (Warehouse_sum.elementAt(i)/sumaDivisor)*100;
					if(formula.isNaN()) {						
						data[i][2]= format.format(0.0);
						data[i][3]= 0.0;
					} 
					else {
						data[i][2]= format.format(formula);
						data[i][3]= formula;
					}	
				}//Fin else
			}// Fin for		
			rs.close();
			pstmt.close();				
		}// fin Try
		catch(Exception E)	{
			E.printStackTrace();
		}
		return data;	
	}// Fin dataWarehouse

    public void generatePercentages() {
    	//Read which Warehouses are going to be used
    	Vector<Integer> acepted_codes = new Vector<Integer>();
    	for (int warehouse = 0 ; warehouse < vectorIDWarehouse.size(); warehouse++ ) {
    		if((Boolean)xProductTable.getValueAt(warehouse, 0)) {    			
    			acepted_codes.add(vectorIDWarehouse.elementAt(warehouse));	    			
    		}    		
    	}// Fin for    	
    	if (acepted_codes.isEmpty()) {
    		ADialog.error(1, new Container(), "XX_UnselectedWarehouses");
			return;    		
    	}// Fin if    	
    	String acepted_ware = acepted_codes.toString();
    	acepted_ware = acepted_ware.replace('[', '(');
    	acepted_ware = acepted_ware.replace(']', ')');    	
    	try	{    		
			//Get selected year and month
			int c_year = 0, c_month = month.getSelectedIndex();
			c_month++; //SQL_MANAGES MONTH LIKE 1-12 NOT 0-11 LIKE JAVA
			c_year = (Integer)year.getSelectedItem();
			String get_sum = "";
			get_sum +=
					" SELECT C.M_WAREHOUSE_ID STORE,  SUM(L.QTYENTERED) QTY " +
					" FROM C_ORDER C JOIN C_ORDERLINE L ON " +
					" (C.C_ORDER_ID = L.C_ORDER_ID) " ;
			get_sum = MRole.getDefault().addAccessSQL(
					get_sum, "C", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);			
			get_sum += 	" AND C.ISSOTRX = 'Y' " +
					" AND C.M_WAREHOUSE_ID IS NOT NULL " +
					" AND TO_CHAR(C.CREATED,'MM') = ? " + 
					" AND TO_CHAR(C.CREATED,'YYYY') = ? " +  
					" AND L.QTYENTERED IS NOT NULL " +
					" AND C.M_WAREHOUSE_ID IN " + acepted_ware ;
			get_sum += " GROUP BY C.M_WAREHOUSE_ID";				
			//System.out.println(get_sum);

			PreparedStatement pstmt = DB.prepareStatement(get_sum, null);
				
			//Setting the query parameters
			pstmt.setInt(1, c_month ) ;
			pstmt.setInt(2, c_year);
			ResultSet rs = pstmt.executeQuery();			
				
			Double sumaDivisor = 0.0;
			Double suma = 0.0;	
			int warehouse = 0;
			DecimalFormat format = new DecimalFormat("#,##0.00");
			Vector<Double> warehouse_sum = new Vector <Double>();
			warehouse_sum.setSize(vectorIDWarehouse.size());
			while(rs.next()) {					
				sumaDivisor += rs.getDouble(2);
				suma = rs.getDouble(2);				
				warehouse = rs.getInt(1);		
				int i = vectorIDWarehouse.indexOf(warehouse);
				if (i >= 0) warehouse_sum.setElementAt(new Double(suma), i);
			}
			for (int i = 0 ; i < vectorIDWarehouse.size(); i++) {				
				if ((Boolean)xProductTable.getValueAt(i, 0)) {	
					if (sumaDivisor.equals(0.0)) {
		    			xProductTable.setValueAt(format.format(0.0), i, 2);
		    			xProductTable.setValueAt(0.0, i, 3);							
					} // Fin sumaDivisor
					else {
						Double formula = 0.0;
						if (warehouse_sum.elementAt(i) != null) {
							formula = (warehouse_sum.elementAt(i)/sumaDivisor)*100;
							if(formula.isNaN()) {						
								xProductTable.setValueAt(format.format(0), i, 2);
				    			xProductTable.setValueAt(0, i, 3);
							} 
							else {							
								xProductTable.setValueAt(format.format(formula), i, 2);
			    				xProductTable.setValueAt(formula, i, 3);
							}						
						} // Fin warehouse
						else {
							xProductTable.setValueAt(format.format(0), i, 2);
			    			xProductTable.setValueAt(0, i, 3);							
						}					
					}
				} // Fin if
				else {
	    			xProductTable.setValueAt(format.format(0.0), i, 2);
	    			xProductTable.setValueAt(0.0, i, 3);					
				}
			}// Fin for		
			rs.close();
			pstmt.close();				
		}// Fin Try
		catch(SQLException E){
			E.printStackTrace();
		}
    	xProductTable.updateUI();
    }// Fin generatePercentages
		
    // Button B Generate Distribution
    public void generateDistribution() {    
		for (int i = 0 ; i < vectorIDWarehouse.size() ; i++) {
	   		percentages.add(new Double(xProductTable.getValueAt(i, 3).toString()));    			
	   	}	
		if(distribAll){
			String sqlLines = " SELECT C_OrderLine_ID ID " +
								" FROM C_ORDERLINE" +
								" WHERE C_ORDER_ID = " + order.getC_Order_ID();
			try {
				PreparedStatement ps = 
					DB.prepareStatement(sqlLines, null);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){ 
					vectorLines.add(rs.getInt("ID"));
				}//While
				rs.close();
				ps.close();
			}//try
			catch (SQLException e){
				e.printStackTrace();			
			}
		}
		else {
			vectorLines.add(detail.get_ID());
		}
		
		int qtyLines = vectorLines.size();
		
		for(int i = 0; i < vectorLines.size(); i++){
			MOrderLine Line = new MOrderLine(Env.getCtx(), vectorLines.get(i), null);
			it_work = XX_SalesPercentage.applyDistribution(Line, percentages, 
					vectorIDWarehouse, vectorIDOrg, m_WindowNo, m_frame, tipo, order);
//			Line.setXX_DistributionType("SA");
//			Line.save();
		}

		if (it_work ) dispose();
    }// Fin generateDistribution

	/**
	 * 	Dispose
	 */
	public void dispose(){
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose
	
	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)	{					
		if (e.getSource() == calculate)
			generatePercentages();		
		else if (e.getSource() == generate)
			generateDistribution();		
	}   //  actionPerformed

	 /**  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;			
	}   //  valueChanged	
}// Fin XX_DistribBySalesForm

