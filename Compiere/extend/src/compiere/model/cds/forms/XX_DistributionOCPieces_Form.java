package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.model.X_M_AttributeSetInstance;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_XX_VMR_DistributionHeader;
import compiere.model.cds.X_XX_VMR_PO_DistribDetail;

/**
 *  Manual Product Distribution
 *  @author     Javier Pino (COMPUTACION 6)
 */
public class XX_DistributionOCPieces_Form extends CPanel
	implements FormPanel, ActionListener, TableModelListener, ListSelectionListener, KeyListener, MouseListener
{

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_DistributionOCPieces_Form.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();

	private Integer m_xMatched = 0;

	//
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();	

	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CLabel xMatchedLabel = new CLabel();
	private CLabel differenceLabel = new CLabel();
	private VNumber xMatched = new VNumber("xMatched", false, true, false, DisplayTypeConstants.Quantity, "xMatched");
	private VNumber difference = new VNumber("Difference", false, true, false, DisplayTypeConstants.Quantity, "Difference");
	private CButton bProcess = new CButton();
	private CPanel centerPanel = new CPanel();
	private BoxLayout centerLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
	private JScrollPane xMatchedScrollPane = new JScrollPane();
	private JScrollPane totalsScrollPane = new JScrollPane();
	private TitledBorder xMatchedBorder = new TitledBorder("xMatched");
	private MiniTable productTable = new MiniTable();
	private MiniTable totalsTable = new MiniTable();
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
		int header_id = aux.getContextAsInt("#XX_VMR_QTY_DistributionHeader_ID");		
		mostrarPiezas = (aux.getContextAsInt("#XX_VMR_QTY_ShowPercentages")) == 0;		
		trans = Trx.get("XX_CHANGE_PO_PRODUCTQTY");
			//Remove the no longer necessary items on the context		
		aux.remove("#XX_VMR_QTY_DistributionHeader_ID");
		if (!mostrarPiezas) 
			aux.remove("#XX_VMR_QTY_ShowPercentages");
		
			//Creates the Header with the necessary information			
		header = 
			new X_XX_VMR_DistributionHeader(aux, header_id , trans);
		try
		{			
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

	/**
	 *  Static Init.
	 *  <pre>
	 *  mainPanel	 *      
	 *      centerPanel
	 *          xMatched
	 *          xMathedTo
	 *      southPanel
	 *  </pre>
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		mainPanel.setLayout(mainLayout);
		
		southPanel.setLayout(southLayout);
		xMatchedLabel.setText(Msg.translate(Env.getCtx(), "ToBeMatched"));
		differenceLabel.setText(Msg.translate(Env.getCtx(), "Difference"));
		bProcess.setText(Msg.translate(Env.getCtx(), "Process"));
		bProcess.setEnabled(true);
		
		centerPanel.setLayout(centerLayout);
		xMatchedScrollPane.setBorder(xMatchedBorder);
		xMatchedScrollPane.setPreferredSize(new Dimension(1380, 600));
		totalsScrollPane.setPreferredSize(new Dimension(780, 35));
		
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		southPanel.add(totalsScrollPane, new GridBagConstraints(0, 0, 7, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5), 0, 0) );
		southPanel.add(Box.createRigidArea(new Dimension(10, 45)), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0,0, 0), 0, 0) );
		
		if (mostrarPiezas) {			
			southPanel.add(xMatchedLabel,        new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));
			southPanel.add(xMatched,         new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
			southPanel.add(bProcess,        new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
			southPanel.add(differenceLabel,      new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
			southPanel.add(difference,   new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));			
		}		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xMatchedScrollPane,  BorderLayout.NORTH);
		
		xMatchedScrollPane.setViewportView(productTable);		
		xMatchedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		xMatchedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//Agregando totales
		totalsScrollPane.setViewportView(totalsTable);
		totalsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		totalsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				

	}   //  jbInit

	
	/**	Indexes in Table			*/	
		
		private static final int 		M_PODISTRIB = 0;
		private static final int 		M_REF = 1;
		private static final int		M_PRODUCT = 2;
		private static final int 		M_INCONSISTENCY = 3;
		private static final int 		M_HASSIZECURVE = 4;
		private static final int		M_STORE = 5;
		private static final int		M_SC = 6;			
		
		private boolean mostrarPiezas = false;
		private X_XX_VMR_DistributionHeader header = null;
		private Trx trans = null;
		private Vector<Integer> warehouses_codes = null;
		private Vector<String> warehouses_names = null;
		private Vector<Integer> modified_rows  = null;
		private int warehouses_number = 0;
		
		private Hashtable<Integer, Hashtable<Integer, Integer>> product_qtys = null;
	
	
	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()	
		{
		//  Visual
		CompiereColor.setBackground (this);
		
			//Column names of the table 
		Vector<String> match_columnNames = new Vector<String>(),
			totalsColumnsName = new Vector<String>();
		
		
		//Adding Data
		product_qtys = new Hashtable<Integer, Hashtable<Integer, Integer>>();
	
		warehouses_codes = new Vector<Integer>();
		warehouses_names = new Vector<String>();
		modified_rows = new Vector<Integer>();
		
		//Adding Column names					
		match_columnNames.add(Msg.translate(Env.getCtx(), "XX_ProductDistribution"));
		match_columnNames.add(Msg.translate(Env.getCtx(), "VendorProductRef"));
		match_columnNames.add(Msg.getMsg(Env.getCtx(), "XX_Product"));		
		match_columnNames.add(Msg.translate(Env.getCtx(), ""));
		match_columnNames.add(Msg.translate(Env.getCtx(), "XX_SizeCurve"));
				
		//Adding columns names of totals
		totalsColumnsName.add("");
		
		//Loading Table Columns -- Warehouses
		/*Se modificó el sql para que solo muestre el CD asociado a la distribución - PROYECTO CD VALENCIA*/
		/*Se modificó para que solo tome tiendas que reciban pedidos*/
		String sql_warehouses = " SELECT M_WAREHOUSE_ID, VALUE||'-'||NAME FROM M_WAREHOUSE WHERE ISACTIVE = 'Y' AND XX_NotReceiveOrder = 'N' "+
				" AND (XX_ISSTORE = 'Y' OR M_WAREHOUSE_ID = "+header.getM_Warehouse_ID()+")";				

		//Jorge Pires - Ordenar tiendas!
		sql_warehouses = MRole.getDefault().addAccessSQL(sql_warehouses, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO)
				+ " ORDER BY VALUE";		
		PreparedStatement pstmt_ware = null;				
		ResultSet   rs_ware = null;
		try {
			System.out.println(sql_warehouses);
			pstmt_ware = DB.prepareStatement(sql_warehouses, null);				
			rs_ware = pstmt_ware.executeQuery();
			while (rs_ware.next()) {				
				warehouses_codes.add(rs_ware.getInt(1));
				warehouses_names.add(rs_ware.getString(2));
				warehouses_number++;
				
				match_columnNames.add(rs_ware.getString(2));
				match_columnNames.add(Msg.translate(Env.getCtx(),"XX_SizeCurve"));
				
				//Agregando nombre para los totales
				totalsColumnsName.add(rs_ware.getString(2));
			}			
			match_columnNames.add(Msg.translate(Env.getCtx(), "XX_DistributedQTY"));	
			totalsColumnsName.add(Msg.translate(Env.getCtx(), "XX_DistributedQTY"));
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql_warehouses, e);
		} finally {
			DB.closeStatement(pstmt_ware);
			DB.closeResultSet(rs_ware);
		}

		//Headers were created
		DefaultTableModel model_match = new DefaultTableModel(null, match_columnNames),
		 	model_total = new DefaultTableModel(null, totalsColumnsName );
		productTable.setModel(model_match);
		totalsTable.setModel(model_total);
		
		//The renderers for the columns
		productTable.setColumnClass(M_PODISTRIB, IDColumn.class, true); //  1-PO Product D
		productTable.setColumnClass(M_REF, String.class, true); //  1-PO Product D
		productTable.setColumnClass(M_PRODUCT, KeyNamePair.class, true); //  2-Product		
		productTable.setColumnClass(M_HASSIZECURVE, Boolean.class, true); //  3-Size Curve use
		productTable.setColumnClass(M_INCONSISTENCY, Integer.class, true);
		
		productTable.getColumnModel().getColumn(M_REF).setMinWidth(150);
		productTable.getColumnModel().getColumn(M_PODISTRIB).setMaxWidth(40);
		productTable.getColumnModel().getColumn(M_PRODUCT).setMinWidth(200);		
		productTable.getColumnModel().getColumn(M_HASSIZECURVE).setMaxWidth(20);
		productTable.getColumnModel().getColumn(M_INCONSISTENCY).setMaxWidth(20);
				
		//The renderers for the totals table columns
		totalsTable.setColumnClass(0, String.class, true);
		
		//Adding the columns for the warehouses
		for (int i = 0 ; i < warehouses_number ; i++) {
			
			//Si se decide mostrar los porcentajes entonces todo debe ser de solo lectura
			if (mostrarPiezas)
				productTable.setColumnClass(M_STORE + i*2, Integer.class, false);
			else 
				productTable.setColumnClass(M_STORE + i*2, BigDecimal.class, true);			
			productTable.getColumnModel().getColumn(M_STORE + i*2).setMinWidth(65);	
			productTable.setColumnClass(M_STORE + 1 + i*2, Boolean.class, true);  //4+i-Compliance
			productTable.getColumnModel().getColumn(M_STORE + 1 + i*2).setMaxWidth(20);
			
			totalsTable.setColumnClass(i + 1, BigDecimal.class, true);
			totalsTable.getColumnModel().getColumn(i +1).setMaxWidth(70);
		}		
		productTable.setColumnClass(M_STORE + 2*warehouses_number, Integer.class, true); //  5-Quantity
		productTable.getColumnModel().getColumn(3 + 2*warehouses_number).setMinWidth(70);
				
		totalsTable.setColumnClass(warehouses_number+1, BigDecimal.class, true);
		totalsTable.getColumnModel().getColumn(warehouses_number+1).setMaxWidth(70);
		totalsTable.setRowCount(1);
			
		//Fill the table with data
		fillMatchTable();		
		XX_IndicatorCellRenderer renderer = new XX_IndicatorCellRenderer(0, 1);
        renderer.setStringPainted(false);
        renderer.setBackground(Color.lightGray);
         
        // set limit value and fill color
        Hashtable<Integer, Color> limitColors = new Hashtable<Integer, Color>();        
        limitColors.put(new Integer(0), Color.yellow);        
        renderer.setLimits(limitColors);        
        
        productTable.getColumnModel().getColumn(M_INCONSISTENCY).setCellRenderer(renderer);			
		productTable.setRowSelectionAllowed(true);
		totalsTable.setRowSelectionAllowed(true);
		if (mostrarPiezas)
			productTable.setSelectionBackground(Color.lightGray);
		else 
			productTable.setSelectionBackground(Color.white);
		totalsTable.setSelectionBackground(Color.white);
		productTable.getTableHeader().setReorderingAllowed(false);		
		totalsTable.getTableHeader().setReorderingAllowed(false);
		
		productTable.addKeyListener(this);
		bProcess.addActionListener(this);
		bProcess.setEnabled(false);
		
		//Title		
		xMatchedBorder.setTitle(Msg.translate(Env.getCtx(),"XX_DistributedProducts"));
		xMatchedScrollPane.validate();
		xMatchedScrollPane.repaint();		
		productTable.getSelectionModel().addListSelectionListener(this);
		productTable.getModel().addTableModelListener(this);
		productTable.getTableHeader().addMouseListener(this);
		statusBar.setStatusLine("");
				
		
	}   //  dynInit

	/**
	 * Fills the table 
	 * */
	public void fillMatchTable( ) {
		
		//Llenar la tabla de totales con ceros		
		totalsTable.setValueAt(Msg.translate(Env.getCtx(), "XX_Total"), 0, 0);
		for (int i = 0; i < warehouses_number +1 ; i++) {
			totalsTable.setValueAt(Env.ZERO, 0, i+1);
		}		
		String sql = "SELECT P.XX_VMR_PO_PRODUCTDISTRIB_ID, P.M_AttributeSetInstance_ID, P.XX_DistributedQTY, " +
				" XX_USEDSIZECURVE AS CURVE," +
				" PR.M_PRODUCT_ID, PR.VALUE||'-'||PR.NAME as PRODUCT, RE.DESCRIPTION AS REF" +
				" FROM XX_VMR_PO_PRODUCTDISTRIB P JOIN M_PRODUCT PR " +
				" ON (P.M_PRODUCT_ID = PR.M_PRODUCT_ID) " +
				" JOIN XX_VMR_VENDORPRODREF RE ON (PR.XX_VMR_VENDORPRODREF_ID = RE.XX_VMR_VENDORPRODREF_ID)  " +
				" WHERE P.XX_VMR_DISTRIBUTIONHEADER_ID = ?  " +
				" ORDER BY RE.DESCRIPTION, PR.VALUE ";			
		try
		{
			int row = 0;
			PreparedStatement pstmt = DB.prepareStatement(sql, null);			
			pstmt.setInt(1, header.get_ID());			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				productTable.setRowCount(productTable.getRowCount() + 1);				
					//For each result add a row to the table							
				
				IDColumn idcolumn = new IDColumn(rs.getInt("XX_VMR_PO_PRODUCTDISTRIB_ID"));
					//1- PO product distrib
				productTable.setValueAt(idcolumn, row, M_PODISTRIB);
					//2- PRODUCT
				String product =  rs.getString("PRODUCT");									
					//2 - ATTRIBUTE 1	
				
				if (rs.getInt("M_AttributeSetInstance_ID")!= 0) {
					X_M_AttributeSetInstance att = 
						new X_M_AttributeSetInstance(Env.getCtx(), rs.getInt("M_AttributeSetInstance_ID") , null);
					product += "-" + att.getDescription();
				}	
				productTable.setValueAt(rs.getString("REF"), row, M_REF);
				productTable.setValueAt(new KeyNamePair(rs.getInt("M_PRODUCT_ID"), product ), row, M_PRODUCT); 	
				
				//No se muestra el numero de piezas en la version porcentual
				if (mostrarPiezas)
					productTable.setValueAt(rs.getInt("XX_DistributedQTY"), row, M_STORE + 2*warehouses_number);
				else {
					productTable.setValueAt(Env.ONEHUNDRED, row, M_STORE + 2*warehouses_number);
				}
				productTable.setValueAt(rs.getString("CURVE").equals("Y"), row, M_HASSIZECURVE);				
				product_qtys.put(rs.getInt("XX_VMR_PO_PRODUCTDISTRIB_ID"), new Hashtable<Integer, Integer>());				

				//Ver si hay inconsistencias y avisar de ellas				
				if ( loadMatchedToTable(rs.getInt("XX_VMR_PO_PRODUCTDISTRIB_ID"), row, 
						rs.getString("CURVE").equals("Y"), rs.getDouble("XX_DistributedQTY"))) {					
					productTable.setValueAt(new Integer(0), row, M_INCONSISTENCY);
				} else {
					productTable.setValueAt(new Integer(1), row, M_INCONSISTENCY);
				}
				row++;
			}
			rs.close();
			pstmt.close();			
			
			if (!mostrarPiezas) {
				//Hay que convertir los totales en porcentajes
				
				BigDecimal total = (BigDecimal)totalsTable.getValueAt(0, warehouses_number +1);
				for (int i = 0; i < warehouses_number; i++) {	
					BigDecimal valor = (BigDecimal)totalsTable.getValueAt(0, i + 1);
					valor = valor.multiply(Env.ONEHUNDRED).divide(total, 3, RoundingMode.HALF_EVEN);					
					totalsTable.setValueAt( valor , 0, i + 1);						
				}
				totalsTable.setValueAt(Env.ONEHUNDRED, 0 , warehouses_number + 1);								
			}
		}
		catch (SQLException e)	{
			log.log(Level.SEVERE, sql, e);
		}		
	}
		
	/**
	 * Reloads the inner table and its model
	 * */
	public boolean loadMatchedToTable(int po_product_id, int row , boolean size, double cantidad) {
			
		double total_piezas = 0;

		//The SQL QUERY from the child table
		String sql = "SELECT P.M_WAREHOUSE_ID ID,P.XX_PRODUCTQUANTITY QTY, P.XX_SIZECURVECOMPLIANCE SIZEC " +
			" FROM XX_VMR_PO_DISTRIBDETAIL P " +			
			" WHERE P.XX_VMR_PO_PRODUCTDISTRIB_ID = " + po_product_id;			
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);			
			ResultSet rs = pstmt.executeQuery();			

			//FOR EACH OBJECT ADD IT TO THE FINAL VECTOR
			int position = 0;
			while (rs.next()) {
				position = warehouses_codes.indexOf(rs.getInt(1));
				if (position != -1) {					
					if (mostrarPiezas)					
						productTable.setValueAt(rs.getDouble(2), row, 2*position + M_STORE);
					else {
						//Si hay que mostrar porcentajes hay que calcularlos
						if (cantidad > 0.0) {
							productTable.setValueAt(
									new BigDecimal(rs.getDouble(2)/cantidad).multiply(Env.ONEHUNDRED).setScale(3, RoundingMode.HALF_EVEN),
									row, 2*position + M_STORE);
						}
					}
					//Colocando las cantidades totales
					totalsTable.setValueAt(
							((BigDecimal)totalsTable.getValueAt(0, position + 1)).add(
									new BigDecimal(rs.getDouble(2))	), 0, position + 1);

					total_piezas += rs.getDouble(2);				
					product_qtys.get(po_product_id).put(2*position + M_STORE, rs.getInt(2));  
					if (size) {
						if ( rs.getString(3) == null || rs.getString(3).equals("N")) {					
							productTable.setValueAt(new Boolean(false), row, 2*position + M_SC);
						} else {
							productTable.setValueAt(new Boolean(true), row, 2*position + M_SC);
						}						
					} else {
						productTable.setValueAt(new Boolean(false), row, 2*position + M_SC);
					} 					
				} 
			}
			rs.close();
			pstmt.close();
			totalsTable.setValueAt(
					((BigDecimal)totalsTable.getValueAt(0, warehouses_number +1)).add(
						new BigDecimal(total_piezas)), 0, warehouses_number +1);
			 
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		return (total_piezas == cantidad); 
	}
	
	/**************************************************************************
	 *  List Selection Listener - get Info and fill xMatchedTo
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		int row = productTable.getSelectedRow();		
		if (row >= 0) {
			m_xMatched = ((Number)productTable.getValueAt(row, M_STORE + 2*warehouses_number)).intValue();			
			xMatched.setValue(m_xMatched);
			
			//Calculate the Difference
			Integer Difference = 0;
			for (int i = 0; i < warehouses_number; i++) {
				if (productTable.getValueAt(row, 2*i + M_STORE) != null) 
					Difference += ((Number)productTable.getValueAt(row, 2*i + M_STORE)).intValue();
			}			
			difference.setValue(m_xMatched - Difference);			
			if ((m_xMatched - Difference) == 0) {
				difference.setBackground(Color.lightGray);					
			} else {
				difference.setBackground(Color.yellow);				
			}
		}
		setCursor(Cursor.getDefaultCursor());
	}   //  valueChanged


	/***************************************************************************
	 *  Table Model Listener - calculate matched Qty
	 *  @param e event
	 */
	public void tableChanged (TableModelEvent e)
	{		

 		int row = e.getFirstRow();
        int column = e.getColumn();
        IDColumn po = (IDColumn)productTable.getValueAt(row, M_PODISTRIB);     
        TableModel model = (TableModel)e.getSource();  
                
        if ((row == -1) || (column == -1)) return;  
        if (column == M_INCONSISTENCY) return;
        Object obj = model.getValueAt(row, column);
    	try {   
    		int data = 0;
    		if(obj!=null)
    			data = ((Number)obj).intValue();        	
    			
    		if (data < 0) {        		
        		model.setValueAt(0, row, column);
        	} else if (data != (Math.floor(data))) {        		
        		model.setValueAt(Math.floor(data), row, column);
        	} else {
        		if (product_qtys.get(po.getRecord_ID()).containsKey( column )) { 
            		if (!product_qtys.get(po.getRecord_ID()).get(column).equals(data)) {
            			if (!modified_rows.contains(po.getRecord_ID())) {            				
                    		modified_rows.add(po.getRecord_ID());
                		}
                	}
            	}  else {
            		if (data != 0 ) {
            			if (!modified_rows.contains(po.getRecord_ID())) {            				
                    		modified_rows.add(po.getRecord_ID());                    		
                		}
            		}
            	}    		
        	}        		
    	} catch (NumberFormatException exc) {
    		//model.setValueAt(0, row, column);
    	}  catch (NullPointerException nul) {
    		//model.setValueAt(0, row, column);
    	}  	
			
		//Calculate the Difference
		Integer Difference = 0;
		
		//Actualizar los totales
		int columna_actual = (column - M_STORE)/2 + 1;
		totalsTable.setValueAt(Env.ZERO, 0, columna_actual);
					
		Double numero = null;
		for (int j = 0; j <productTable.getRowCount() ; j++) {	
			
			if (productTable.getValueAt(j, column) != null) {
				numero = ((Number)productTable.getValueAt(j, column)).doubleValue();
				totalsTable.setValueAt(
					((BigDecimal)totalsTable.getValueAt(0, columna_actual)
								).add( new BigDecimal(numero)), 0, columna_actual);
			}
		}
		BigDecimal total = Env.ZERO;
		for (int j = 0 ; j < warehouses_number ; j++) {
			total = total.add((BigDecimal)totalsTable.getValueAt(0, j + 1));
		}
		totalsTable.setValueAt(total, 0, warehouses_number + 1);
		
		
		//Verificar las inconsistencias
		for (int i = 0; i < warehouses_number; i++) {
			if (productTable.getValueAt(row, 2*i + M_STORE) != null) 
				Difference += ((Number)productTable.getValueAt(row, 2*i + M_STORE)).intValue();
		}			
		difference.setValue(m_xMatched - Difference);			
		if ((m_xMatched - Difference) == 0) {
			difference.setBackground(Color.lightGray);	
			productTable.setValueAt(new Integer(0), row, M_INCONSISTENCY);
		} else {
			difference.setBackground(Color.yellow);
			productTable.setValueAt(new Integer(1), row, M_INCONSISTENCY);
		}
		//Verificar si el boton es activable
		boolean activable = true;
		for (int i = 0; i < productTable.getRowCount() ; i++) {
			Integer inconsistency = ((Number)productTable.getValueAt(i, M_INCONSISTENCY)).intValue();
			if (inconsistency == 1) {
				activable = false;
				break;
			}
		}		
		setCursor(Cursor.getDefaultCursor());		
		if (!modified_rows.isEmpty() && activable)
			bProcess.setEnabled(true);
		else {
			bProcess.setEnabled(false);
		}
	}   //  tableChanged
	
	/**
	 * 	Dispose the frame
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == bProcess)
			cmd_process();	
		setCursor(Cursor.getDefaultCursor());
	}   //  actionPerformed

	
	/**
	 *  Process Button Pressed - Process Matching
	 */
	private void cmd_process() {
		boolean ok_to_distribute = true;
		
		//Stops any editing
		if (productTable.isEditing())
			productTable.getCellEditor().stopCellEditing();		
		
			//FOR EACH ONE MODIFIED
		int row = 0;
		if (modified_rows.size() == 0 ) {
			dispose();
			return;
		}		
		
		//If it´s ok to distribute then safe delete and store the values		
		if (ok_to_distribute) {
			//	Ask the user whether it wants or not to delete the previous data
			boolean replace =
				ADialog.ask(m_WindowNo, m_frame, "XX_SafeDeleteDistribution");
			if (!replace) return;
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
			m_frame.setCursor(hourglassCursor);
			
			//Verificar si la redistribucion implica una correccion
			X_XX_VMR_DistributionHeader header_modify = 
				new X_XX_VMR_DistributionHeader(Env.getCtx(), header.get_ID(), trans);
			if (header_modify.getXX_DistributionStatus().equals(
					X_Ref_XX_DistributionStatus.PENDIENTE_POR_REDISTRIBUCION_Y_APROBACION.getValue())) {
				header_modify.setXX_DistributionStatus(
						X_Ref_XX_DistributionStatus.LISTA_PARA_APROBAR.getValue());
				header_modify.save();
			}
			
			//Delete previous records
			IDColumn idcolumn = null;
			for (int i = 0 ; i < productTable.getRowCount(); i++) {
				
				idcolumn = (IDColumn)productTable.getValueAt(i, M_PODISTRIB);				
				if (!modified_rows.contains(idcolumn.getRecord_ID())) {
					//Se ignora esta columna
					continue;
				}				
				row = i;				
				try {
					
					String sql = "DELETE FROM XX_VMR_PO_DistribDetail WHERE XX_VMR_PO_ProductDistrib_id = " + idcolumn.getRecord_ID();
					DB.executeUpdate(trans, sql);
					
					for (int j = 0; j < warehouses_number; j++) {
						int qty = 0;
						if (productTable.getValueAt(row, 2*j + M_STORE) != null) 
							qty = ((Number)productTable.getValueAt(row, 2*j + M_STORE)).intValue();
						X_XX_VMR_PO_DistribDetail distribution = 
							new X_XX_VMR_PO_DistribDetail(Env.getCtx(), 0, trans);
											
						//Store the object on the data base
						distribution.setXX_VMR_PO_ProductDistrib_ID(idcolumn.getRecord_ID());
						distribution.setM_Warehouse_ID(warehouses_codes.elementAt(j));
						distribution.setXX_SizeCurveCompliance(false);
						distribution.setXX_ProductQuantity(qty) ;
						if (qty != 0.0) {
							distribution.save();						
						}
					}					
				} catch (Exception e) {					
					e.printStackTrace();
					ok_to_distribute = false;
				}
			}
			if (ok_to_distribute) {
				//AGREGADO GHUCHET
				int distType = Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEPIECES_ID");
				header_modify.setXX_DistributionTypeApplied(distType);
				header_modify.save(trans);
				//FIN GHUCHET
				trans.commit();			
			} else {
				trans.rollback();
				ADialog.info(m_WindowNo, m_frame, "XX_DatabaseError");
			}							
			dispose();
		} else return;
	}   //  cmd_process

	@Override
	public void keyPressed(KeyEvent e) {
	}		 	

	@Override
	/* TAB related events, this checks render the form more usuable**/
	public void keyReleased(KeyEvent e) {		
		int row = productTable.getSelectedRow(), 
	 	column = productTable.getSelectedColumn();
		
		boolean used_shift = false;
	    int modifiers = e.getModifiersEx();	    
	    int shift_mask = InputEvent.SHIFT_DOWN_MASK;
	    
	    if ((modifiers & shift_mask) == shift_mask) {
	    	used_shift = true;
	    }
	    
	    //NO USO MODIFICADORES
		if ( (e.getKeyChar() == KeyEvent.VK_TAB) && !used_shift ) { 
			//Out of the table
			 if (row == -1 && column == -1) {
				 if (productTable.getRowCount() > 0) {
					 productTable.changeSelection(0, M_STORE, false, false);
					 productTable.editCellAt(0, M_STORE);
				 }
				 //Last column of the table
			 } else if (column == (M_STORE + 2*warehouses_number)) {
				 
				 //Not last row
				 if (row < productTable.getRowCount() - 1) {
					 if (productTable.getRowCount() > 0) {				
						 productTable.changeSelection(row + 1, M_STORE , false, false);
						 productTable.editCellAt(row + 1, M_STORE);
					 }
					 //Last row
				 } else { 
					 productTable.changeSelection(0, M_STORE, false, false);
					 productTable.editCellAt(0, M_STORE);
				 } 
			 } else {				 
				 if (column < M_STORE) {
					 productTable.changeSelection(row, M_STORE , false, false);
					 productTable.editCellAt(row, M_STORE);
				 }
				 else if (column%2 == 0) {
					 productTable.changeSelection(row, column + 1 , false, false);
					 productTable.editCellAt(row, column + 1);					 
				 }
				 else {					 
					 productTable.changeSelection(row, column, false, false);
					 productTable.editCellAt(row, column);
				 }
			 }
			 
		//USO MODIFICADORES	 
		} else if ((e.getKeyChar() == KeyEvent.VK_TAB) && used_shift) {
			//Out of the table
			 if (row == -1 && column == -1) {				 
				if (productTable.getRowCount() > 0) {					
					productTable.changeSelection(productTable.getRowCount()-1, M_STORE + 2*warehouses_number, false, false);
					productTable.editCellAt(productTable.getRowCount()-1, M_STORE + 2*warehouses_number);
				}				 
			 }  else if (column <= M_STORE) {			 
				 //Not First row
				 if (row > 0) {
					 productTable.changeSelection(row - 1, M_STORE + 2*warehouses_number , false, false);
					 productTable.editCellAt(row - 1, M_STORE + 2*warehouses_number);
					 //Last row
				 } else { 
					 if (productTable.getRowCount() > 0) {
						 productTable.changeSelection(productTable.getRowCount()-1, M_STORE + 2*warehouses_number, false, false);
					 	 productTable.editCellAt(productTable.getRowCount()-1, M_STORE + 2*warehouses_number);
					 }
				 } 			 
			 } else {
				 if (column%2 == 0) {
					 productTable.changeSelection(row, column - 1 , false, false);
					 productTable.editCellAt(row, column - 1);					 
				 }
				 else {					 
					 productTable.changeSelection(row, column, false, false);
					 productTable.editCellAt(row, column);
				 }
			 }
					
		} else {
			if (row != -1 && column != -1) 
				if (productTable.isCellEditable(row, column))
					productTable.editCellAt(row, column);			
		}			
	}

	@Override
	public void keyTyped(KeyEvent e) {
		 
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (productTable.isEditing())
			productTable.stopEditor(false);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	
}   
