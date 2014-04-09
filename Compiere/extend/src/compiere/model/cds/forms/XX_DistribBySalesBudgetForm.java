package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import compiere.model.cds.X_XX_VMR_DistributionDetail;
import compiere.model.cds.processes.XX_PercentualDistributionRedistribution;

/**
 * Distribution Form for Purchase Order by Sales + Budget
 * @author Javier Pino.
 *
 */
public class XX_DistribBySalesBudgetForm extends CPanel 
	implements FormPanel, ActionListener, TableModelListener, ListSelectionListener{

	/**
	 * Generated Serial
	 */
	private static final long serialVersionUID = 1L;
	
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_DistributionOCSalesBudget_Form.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	
	static StringBuffer    	m_sql = null;
	static String          	m_groupBy = "";
	static String          	m_orderBy = "";

	private CLabel 			LineRefProv_Label = new CLabel();
	private CPanel 			mainPanel = new CPanel();
	private StatusBar 		statusBar = new StatusBar();
	private BorderLayout 	mainLayout = new BorderLayout();
	private CPanel 			northPanel = new CPanel();
	private CButton 		bAssociate = new CButton();
	private CButton 		bNewProduct = new CButton();
	private CPanel 			southPanel = new CPanel();
	private GridBagLayout 	southLayout = new GridBagLayout();
	private CPanel		 	centerPanel = new CPanel();
	private BorderLayout 	centerLayout = new BorderLayout(5,5);
	private JScrollPane 	xProductScrollPane = new JScrollPane();
	private TitledBorder 	xProductBorder = new TitledBorder(Msg.translate(Env.getCtx(), "XX_SuggestedDistribution"));
	private MiniTable 		xProductTable = new MiniTable();
	private CPanel 			xPanel = new CPanel();
	
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	
	private X_XX_VMR_DistributionDetail distributionDetail = null;
	private Calendar 					actualDate = Calendar.getInstance();
	private Vector<Double>				percentages = new Vector<Double>();
	private Vector<Integer>				codes = new Vector<Integer>();
	private DecimalFormat 				decimalFormat = new DecimalFormat("#,##0.00");
	
	/**
	 * 	Dispose
	 */
	@Override
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
		
	}   //  dispose

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	@Override
	public void init(int WindowNo, FormFrame frame) {
		
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);		
		Integer headerID  = Env.getCtx().getContextAsInt("#XX_VMR_SalesBudget_DistributionDetail_ID");
		
		//Creates the Header with the necessary information
		distributionDetail = new X_XX_VMR_DistributionDetail(Env.getCtx(), headerID, null);
		
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
		
	}   //  init
	
	/**
	 *  Static Init.
	 *  <pre>
	 *  mainPanel
	 *      northPanel
	 *      	xPanel          
	 *      centerPanel
	 *          xProductScrollPane
	 *      southPanel
	 *      	bAssociate
	 *      	bNewProduct
	 *  </pre>
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		mainPanel.setLayout(mainLayout);
		
		// Buttons - South
		bAssociate.setText(Msg.translate(Env.getCtx(), "XX_CalculateDistribution"));
		bAssociate.setEnabled(true);
		bNewProduct.setText(Msg.translate(Env.getCtx(), "XX_GeneratePieces"));
		bNewProduct.setEnabled(true);
		southPanel.setLayout(southLayout);
		southPanel.add(bAssociate,   new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
		southPanel.add(bNewProduct,   new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
		
		// Scroll Pane - Center
		centerPanel.setLayout(centerLayout);
		xProductScrollPane.setBorder(xProductBorder);
		xProductScrollPane.setPreferredSize(new Dimension(570, 200));
		xProductScrollPane.getViewport().add(xProductTable, null);
		centerPanel.add(xProductScrollPane,  BorderLayout.NORTH);
				
		// Space - North
		xPanel.setLayout(xLayout);
		northPanel.add(LineRefProv_Label,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		
		// Adding Panels to Main Panel
		mainPanel.add(southPanel,  BorderLayout.SOUTH);		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		
	}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		//  Columns		
		ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Options"), "", Boolean.class, false, false, ""),		//  0
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Store"),   ".", KeyNamePair.class),             		//  1
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PercentageAssigned"),   ".", Number.class),             			//  2
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Chart"),   ".", Double.class)             				//  3
		};

		xProductTable.prepareTable(layout, "", "", false, "");
		
		//  Visual
		CompiereColor.setBackground (this);

		//  Listeners
		xProductTable.getSelectionModel().addListSelectionListener(this);		
		xProductTable.getTableHeader().setReorderingAllowed(false);
		xProductTable.setSortEnabled(false);
		bAssociate.addActionListener(this);
		bNewProduct.addActionListener(this);

		//  Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);
		
		IndicatorCellRenderer renderer = new IndicatorCellRenderer(0, 100);
        renderer.setStringPainted(false);

        // 	Set limit value and fill color
        Hashtable<Integer, Color> limitColors = new Hashtable<Integer, Color>();
        	limitColors.put(new Integer(0), Color.green);
        	limitColors.put(new Integer(60), Color.yellow);
        	limitColors.put(new Integer(80), Color.red);
        renderer.setLimits(limitColors);
        xProductTable.getColumnModel().getColumn(3).setCellRenderer(renderer);		
		
		// Filling Table
		ArrayList<KeyNamePair> warehouses = dataWarehouse();
		//BigDecimal invDistribuir = calcularInvDistribuir();
		BigDecimal totalSale = calcularTotalSale(warehouses);
		BigDecimal totalBudget = calcularTotalBudget(warehouses);
		TableModel modelProduct = xProductTable.getModel();
		
		for (int i = 0; i < warehouses.size(); i++) {
			xProductTable.setRowCount(i+1);
			// Filling CheckMark Field
			modelProduct.setValueAt(true, i, 0);
			// Filling Warehouse Field
			modelProduct.setValueAt(warehouses.get(i), i, 1);
			// Filling Percentage Field
			BigDecimal whSaleBudget = calcularDistribucion(warehouses.get(i), totalSale, totalBudget);
			whSaleBudget = whSaleBudget.multiply(new BigDecimal(100));
			modelProduct.setValueAt(decimalFormat.format(whSaleBudget), i, 2);
			// Filling Chart Field
			modelProduct.setValueAt(whSaleBudget.doubleValue(), i, 3);
			// Adding new percentage and warehouse into globals Vectors
			percentages.add(whSaleBudget.doubleValue());
			codes.add(warehouses.get(i).getKey());			
		}		
		xProductTable.updateUI();
		xProductTable.autoSize(true);
		
	}   //  dynInit

	/**
	 *  Action Performed
	 *  @param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == bAssociate)
			calcularDistribucion();
		
		else if (e.getSource() == bNewProduct)
			generarDistribucion();
		
	}   //  actionPerformed

	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	@Override
	public void tableChanged(TableModelEvent e) {
		
	}   //  tableChanged

	/**
	 *  List Selection Listener
	 *  @param e event
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		
	}   //  valueChanged

	/**
	 * Genera y guarda la distribucion previamente calculada
	 * Método llamado desde el botón 
	 */
	public void generarDistribucion(){
		Vector<Double> percentagesDePino = new Vector<Double>();
		Vector<Integer> codesDePino = new Vector<Integer>();
		for (int i = 0; i < percentages.size(); i++) {
			if (!percentages.get(i).equals(new Double(0.0))){
				percentagesDePino.add(percentages.get(i));
				codesDePino.add(codes.get(i));
			}
		}
		//Changes
		
		if (XX_PercentualDistributionRedistribution.applyDistribution(
				distributionDetail,	percentagesDePino, codesDePino, m_WindowNo, m_frame)) {						
			dispose();
		}
	}	//	generarDistribucion
	
	/**
	 *  Cálculo de Distribución de O/C por Ventas/Presupuesto
	 *  Método llamado desde el botón
	 */
	public void calcularDistribucion(){
		
		// Filling Table
		ArrayList<KeyNamePair> warehouses = dataWarehouse();
		ArrayList<KeyNamePair> warehousesAux = new ArrayList<KeyNamePair>();
		for (int i = 0; i < warehouses.size(); i++) {			
			if(xProductTable.getValueAt(i, 0).equals(true)){				
				warehousesAux.add(warehouses.get(i));
			}			
		}
		//BigDecimal invDistribuir = calcularInvDistribuir();
		BigDecimal totalBudget = calcularTotalBudget(warehousesAux);
		BigDecimal totalSale = calcularTotalSale(warehousesAux);		
		percentages.clear();
		codes.clear();
		
		for (int i = 0; i < warehouses.size(); i++) {				
			// Filling Percentage Field
			BigDecimal whSaleBudget = new BigDecimal(0);
			if(xProductTable.getValueAt(i, 0).equals(true)){		
				whSaleBudget = calcularDistribucion(warehouses.get(i), totalSale, totalBudget);
				whSaleBudget = whSaleBudget.multiply(new BigDecimal(100));
				
				// Adding new percentage and warehouse into globals Vectors
				percentages.add(whSaleBudget.doubleValue());
				codes.add(warehouses.get(i).getKey());
			}
			xProductTable.setValueAt(decimalFormat.format(whSaleBudget), i, 2);
			
			// Filling Chart Field
			xProductTable.setValueAt(whSaleBudget.doubleValue(), i, 3);
			
		}		
	}	//	calcularDistribucion
	
	/**
	 *  Cálculo de Distribución de O/C por Ventas/Presupuesto
	 *  @return BigDecimal con el cálculo del porcentaje de distribucion del almacen dado
	 *  Método llamado desde el botón
	 */
	public BigDecimal calcularDistribucion(KeyNamePair warehouse, BigDecimal totalSale, BigDecimal totalBudget) {

		//OJO No aplica si no existe historial del producto		
		int year = actualDate.get(Calendar.YEAR);
		int monthPlusOne = actualDate.get(Calendar.MONTH) + 1; // mes en curso ( para presupuesto )
		String yearmonth = "";
		if(monthPlusOne < 10)
			yearmonth = year + "0" + monthPlusOne;
		else
			yearmonth = "" + year + monthPlusOne;
		
		BigDecimal budgetStore = new BigDecimal(0);
		BigDecimal salesStore = new BigDecimal(0);

		String sql = "SELECT SUM(ip.XX_SALESAMOUNTBUD2) FROM XX_VMR_PRLD01 ip"; 
		sql +=	" WHERE ip.XX_BUDGETYEARMONTH = " + yearmonth ;
		sql +=  " AND ip.M_WAREHOUSE_ID = " + warehouse.getKey();		
		
		//Agregar la info del detalle
		if (distributionDetail.getXX_VMR_Department_ID() != 0) {			
			sql += " AND ip.XX_VMR_DEPARTMENT_ID = " + distributionDetail.getXX_VMR_Department_ID();			
			if (distributionDetail.getXX_VMR_Line_ID() != 0) {
				sql += " AND ip.XX_VMR_LINE_ID = " + distributionDetail.getXX_VMR_Line_ID();				
				if (distributionDetail.getXX_VMR_Section_ID() != 0) {
					sql += "AND ip.XX_VMR_SECTION_ID = " + distributionDetail.getXX_VMR_Section_ID();										
				}
			}		
		}		
		sql = MRole.getDefault().addAccessSQL(sql, "ip", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);			
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
			{
				BigDecimal aux = rs.getBigDecimal(1);
				if (aux != null) 
					budgetStore = aux;
			}
	
			rs.close();
			pstmt.close();
		
		} // try
		
		catch(SQLException e)
		{
			e.getMessage();
		}		
		sql = "SELECT SUM(L.saleqty) as salesStore " +
			" FROM xx_vmr_po_linerefprov L, c_order C " +
	  		" WHERE L.c_order_id = C.c_order_id " +
	  		" AND C.issotrx= 'Y' " +
	  		" AND C.m_warehouse_id = " + warehouse.getKey() +
	  		" AND C.CREATED between (sysdate - 30) and sysdate" +
	  		" AND L.saleqty is not null";
		
		//Agregar la info del detalle
		if (distributionDetail.getXX_VMR_Department_ID() != 0) {			
			sql += " AND c.XX_VMR_DEPARTMENT_ID = " + distributionDetail.getXX_VMR_Department_ID();			
			if (distributionDetail.getXX_VMR_Line_ID() != 0) {
				sql += " AND L.XX_VMR_LINE_ID = " + distributionDetail.getXX_VMR_Line_ID();				
				if (distributionDetail.getXX_VMR_Section_ID() != 0) {
					sql += "AND L.XX_VMR_SECTION_ID = " + distributionDetail.getXX_VMR_Section_ID();										
				}
			}
		}
		if(distributionDetail.getM_Product_ID() != 0) {
			//QueryDivisor = QueryDivisor+"C.c_order_id= P.c_order_id and "+ 
			//  							 "M.XX_VMR_PO_LINEREFPROV_ID=P.XX_VMR_PO_LINEREFPROV_ID and "+
			//  							 "PRO.m_product_id= M.m_product_id and "+
			//  							 "PRO.m_product_id is not null";
		}
		if(distributionDetail.getXX_VMR_Brand_ID() != 0) {
			sql += " AND l.XX_VMR_BRAND_ID = " + distributionDetail.getXX_VMR_Brand_ID() ;
		}
		if(distributionDetail.getC_BPartner_ID() != 0) {
			sql += " AND c.C_BPARTNER_ID = " + distributionDetail.getC_BPartner_ID() ;
		}	
		//Fin info detalle
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
	
			if(rs.next())
			{
				BigDecimal aux = rs.getBigDecimal("salesStore");
				if (aux != null)
					salesStore = aux;				
			}
	
			rs.close();
			pstmt.close();
		
		} // try
		
		catch(SQLException e)
		{
			e.getMessage();
		}
		// Calculating Distribution
		BigDecimal percentage = Env.ZERO;		
		//Cambio en el calculo - Javier Pino		
		if (totalSale.compareTo(Env.ZERO) > 0)
			if (totalBudget.compareTo(Env.ZERO) > 0){
				BigDecimal budgetPercentage = budgetStore.divide(totalBudget, 2, RoundingMode.HALF_UP);
				BigDecimal salePercentage = salesStore.divide(totalSale, 2, RoundingMode.HALF_UP);
				percentage = budgetPercentage.add(salePercentage);
				percentage = percentage.divide(new BigDecimal(2));
			}		
		return percentage;
	} // calcularDistribucion
		
	/**
	 * calcularTotalSale
	 * @param warehouses
	 * @return BigDecimal totalOC
	 */
	public BigDecimal calcularTotalSale(ArrayList<KeyNamePair> warehouses){
		
		BigDecimal totalOC = new BigDecimal(0);	
		
		//	Grouping all warehouses in a structure
		ArrayList<Integer> accepted_warehouses = new ArrayList<Integer>();		
		for (int i = 0; i < warehouses.size(); i++) {
			accepted_warehouses.add(warehouses.get(i).getKey());
		}
		String accepted = accepted_warehouses.toString();
		accepted = accepted.replace('[', '(');
		accepted = accepted.replace(']', ')');
		
		String sql = " SELECT SUM(L.SALEQTY) totalOC " +
					" FROM C_ORDER C JOIN XX_VMR_PO_LINEREFPROV L ON " +
					" (C.C_ORDER_ID = L.C_ORDER_ID) " ;
		sql = MRole.getDefault().addAccessSQL(
				sql, "C", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);			
		sql += 	" AND C.ISSOTRX = 'Y' " + 
				" AND C.CREATED between (sysdate - 30) and sysdate " + 
				" AND C.M_WAREHOUSE_ID IN " + accepted +
				" AND L.SALEQTY IS NOT NULL ";
		
		//Agregar la info del detalle
		if (distributionDetail.getXX_VMR_Department_ID() != 0) {			
			sql += " AND c.XX_VMR_DEPARTMENT_ID = " + distributionDetail.getXX_VMR_Department_ID();			
			if (distributionDetail.getXX_VMR_Line_ID() != 0) {
				sql += " AND L.XX_VMR_LINE_ID = " + distributionDetail.getXX_VMR_Line_ID();				
				if (distributionDetail.getXX_VMR_Section_ID() != 0) {
					sql += " AND L.XX_VMR_SECTION_ID = " + distributionDetail.getXX_VMR_Section_ID();										
				}
			}
		}
		if(distributionDetail.getM_Product_ID() != 0) {
			//QueryDivisor = QueryDivisor+"C.c_order_id= P.c_order_id and "+ 
			//  							 "M.XX_VMR_PO_LINEREFPROV_ID=P.XX_VMR_PO_LINEREFPROV_ID and "+
			//  							 "PRO.m_product_id= M.m_product_id and "+
			//  							 "PRO.m_product_id is not null";
		}
		if(distributionDetail.getXX_VMR_Brand_ID() != 0) {
			sql += " AND l.XX_VMR_BRAND_ID = " + distributionDetail.getXX_VMR_Brand_ID() ;
		}
		if(distributionDetail.getC_BPartner_ID() != 0) {
			sql += " AND c.C_BPARTNER_ID = " + distributionDetail.getC_BPartner_ID() ;
		}		
		//Fin info detalle
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
	
			if(rs.next())
			{
				BigDecimal aux = rs.getBigDecimal("totalOC");
				if (aux != null) 
					totalOC = aux;
			}
	
			rs.close();
			pstmt.close();
		
		} // try
		
		catch(SQLException e)
		{
			e.getMessage();
		}
		return totalOC;
	}
	
	/**
	 * calcularTotalSale
	 * @return BigDecimal totalOC
	 */
	public BigDecimal calcularTotalSale(){
		
		BigDecimal totalOC = new BigDecimal(0);	
		
		String sql = " SELECT SUM(L.SALEQTY) totalOC " +
					" FROM C_ORDER C JOIN XX_VMR_PO_LINEREFPROV L ON " +
					" (C.C_ORDER_ID = L.C_ORDER_ID) " ;
		sql = MRole.getDefault().addAccessSQL(
				sql, "C", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);			
		sql += 	" AND C.ISSOTRX = 'Y' " + 
				" AND C.M_WAREHOUSE_ID IS NOT NULL " +
				" AND C.CREATED between (sysdate - 30) and sysdate " +  
				" AND L.SALEQTY IS NOT NULL ";
		
		//Agregar la info del detalle
		if (distributionDetail.getXX_VMR_Department_ID() != 0) {			
			sql += " AND c.XX_VMR_DEPARTMENT_ID = " + distributionDetail.getXX_VMR_Department_ID();			
			if (distributionDetail.getXX_VMR_Line_ID() != 0) {
				sql += " AND L.XX_VMR_LINE_ID = " + distributionDetail.getXX_VMR_Line_ID();				
				if (distributionDetail.getXX_VMR_Section_ID() != 0) {
					sql += "AND L.XX_VMR_SECTION_ID = " + distributionDetail.getXX_VMR_Section_ID();										
				}
			}
		}
		if(distributionDetail.getM_Product_ID() != 0) {
			//QueryDivisor = QueryDivisor+"C.c_order_id= P.c_order_id and "+ 
			//  							 "M.XX_VMR_PO_LINEREFPROV_ID=P.XX_VMR_PO_LINEREFPROV_ID and "+
			//  							 "PRO.m_product_id= M.m_product_id and "+
			//  							 "PRO.m_product_id is not null";
		}
		if(distributionDetail.getXX_VMR_Brand_ID() != 0) {
			sql += " AND l.XX_VMR_BRAND_ID = " + distributionDetail.getXX_VMR_Brand_ID() ;
		}
		if(distributionDetail.getC_BPartner_ID() != 0) {
			sql += " AND c.C_BPARTNER_ID = " + distributionDetail.getC_BPartner_ID() ;
		}		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
	
			if(rs.next())
			{
				int aux = rs.getInt("totalOC");
				totalOC = new BigDecimal(aux);
			}
	
			rs.close();
			pstmt.close();
		
		} // try
		
		catch(SQLException e)
		{
			e.getMessage();
		}	
		return totalOC;
	}
	
	/**
	 * calcularTotalBudget
	 * @param ArrayList<KeyNamePair> warehouses
	 * @return totalBudget
	 */
	private BigDecimal calcularTotalBudget(ArrayList<KeyNamePair> warehouses){
		
		BigDecimal totalBudget = new BigDecimal(0);
		int year = actualDate.get(Calendar.YEAR);
		int monthPlusOne = actualDate.get(Calendar.MONTH) + 1;
		String yearmonth = "";

		if(monthPlusOne < 10)
			yearmonth = year + "0" + monthPlusOne;
		else
			yearmonth = "" + year + monthPlusOne;
		
		//	Agrupping all warehouses in a structure
		ArrayList<Integer> accepted_warehouses = new ArrayList<Integer>();		
		for (int i = 0; i < warehouses.size(); i++) {
			accepted_warehouses.add(warehouses.get(i).getKey());
		}
		String accepted = accepted_warehouses.toString();
		accepted = accepted.replace('[', '(');
		accepted = accepted.replace(']', ')');
			
		String sql = "SELECT SUM(ip.XX_SALESAMOUNTBUD2) AS BUDGET FROM XX_VMR_PRLD01 ip"; 
		sql +=	" WHERE ip.XX_BUDGETYEARMONTH = " + yearmonth ;
		sql +=  " AND ip.M_WAREHOUSE_ID IN " + accepted;		
		if (distributionDetail.getXX_VMR_Department_ID() != 0) {			
			sql += " AND ip.XX_VMR_DEPARTMENT_ID = " + distributionDetail.getXX_VMR_Department_ID();			
			if (distributionDetail.getXX_VMR_Line_ID() != 0) {
				sql += " AND ip.XX_VMR_LINE_ID = " + distributionDetail.getXX_VMR_Line_ID();				
				if (distributionDetail.getXX_VMR_Section_ID() != 0) {
					sql += " AND ip.XX_VMR_SECTION_ID = " + distributionDetail.getXX_VMR_Section_ID();										
				}
			}		
		}
		sql = MRole.getDefault().addAccessSQL(sql, "ip", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
	
			if(rs.next()) {
				BigDecimal aux = rs.getBigDecimal("budget");
				if (aux != null) 
					totalBudget = aux;
			}
	
			rs.close();
			pstmt.close();			
			
		} // try
		
		catch(SQLException e) {
			e.getMessage();
		}		
		return totalBudget;	
	}
		
	/**
	 * Data of Warehouse
	 * @return ArrayList<KeyNamePair>
	 * with Keys and Names of Warehouses
	 */
	private ArrayList<KeyNamePair> dataWarehouse(){
		
		ArrayList<KeyNamePair> arrayKeyNames = new ArrayList<KeyNamePair>();
		KeyNamePair aux = null;
		//Jorge Pires - Ordenar Tiendas
		String SQL ="SELECT m_warehouse_id, value||'-'||name FROM m_warehouse WHERE isactive = 'Y'";
		SQL = MRole.getDefault().addAccessSQL(SQL, "", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO) + " ORDER BY VALUE ";		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {	
				aux = new KeyNamePair(rs.getInt(1), rs.getString(2));
				arrayKeyNames.add(aux);
			}
			rs.close();
			pstmt.close();
			return arrayKeyNames;
		}
		catch(Exception e) {	
			e.getMessage();
		}		
		return null;
	
	}   //  dataWarehouse
	
	/**
	 * Code that makes the chart display colors
	 * */
	private class IndicatorCellRenderer extends JProgressBar implements TableCellRenderer {

		private static final long serialVersionUID = 1L;
		private Hashtable<Integer,Color> limitColors;
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
}

