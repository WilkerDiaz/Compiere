package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
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
import java.util.logging.Level;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.apps.ProcessCtl;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.framework.Lookup;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPInstance;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;

/**
 * Forma que muestra una lista de las ordenes de compra de despacho directo aprobadas 
 * o chequeadas pendientes de procesar recepcion en tienda y permite imprimir las
 *  etiquetas correspondientes a los productos de la o/c seleccionada.
 *  @author Gabrielle Huchet
 *  @version 
 */


public class XX_PrintLabelsDD_Form extends CPanel 
implements FormPanel, ActionListener,  ListSelectionListener{


	private static final long serialVersionUID = 1L;

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */


	/**	Window No	*/
	private int m_WindowNo = 0;
	/**	FormFrame	*/
	private FormFrame m_frame;
	/**	Logger		*/
	static CLogger log = CLogger.getCLogger(XX_PrintLabelsDD_Form.class);
	
	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	
	/* Panel de la ventana */
	private CPanel mainPanel = new CPanel();
	private CPanel northPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private JScrollPane xScrollPane = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder("Ordenes de Compra de Despacho Directo");
	private CPanel xPanel = new CPanel();
	
	private CPanel windowsPanel = new CPanel();
	
	/* Layouts */
	private BorderLayout mainLayout = new BorderLayout();
	private GridBagLayout northLayout = new GridBagLayout();
	private FlowLayout southLayout = new FlowLayout();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	
	private GridBagLayout windowsLayout = new GridBagLayout();
	

	/* Botones */
	private CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "Search"));
	private CButton bClear = new CButton(Msg.translate(Env.getCtx(), "Clear"));
	private CButton bPrintLabels = new CButton(Msg.translate(Env.getCtx(), "XX_PrintLabelsOC"));
	private CButton bPrint = new CButton(Msg.translate(Env.getCtx(), "Print"));


	
	/* Campos del filtro disponibles */
	//Orden de Compra
	private VLookup purchaseSearch = null;
	private CLabel purchaseLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_PurchaseOrder"));
	//Proveedor
	private CComboBox vendorCombo = new CComboBox();
	private CLabel vendorLabel = new CLabel(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
	//Impresora Etiquetas Colgante
	private CComboBox cPrinterHanging = new CComboBox();
	private CLabel pHangingLabel = new CLabel(Msg.translate(Env.getCtx(), "Printer_Hanging"));
	//Impresora Etiquetas Engomadas
	private CComboBox cPrinterGlued = new CComboBox();
	private CLabel pGluedLabel = new CLabel(Msg.translate(Env.getCtx(), "Printer_Glued"));
	//Orden de compra seleccionada
	CLabel orderSelectedL = new CLabel("O/C Seleccionada:");
	CLabel orderNoL = new CLabel();
	/** La tabla donde se guardarán los datos*/
	private MiniTablePreparator table = new MiniTablePreparator();
	
	/** ID del Proceso */
	private Integer IDPROCESO = Env.getCtx().getContextAsInt("#XX_L_PROCESSPRINTLABELDD_ID");
	
	private AWindow window_print = new AWindow();
	private Integer orderToPrint = null;
	
	/**
	 * 	Is Process Active
	 *	@return true if active
	 */
	protected static boolean isProcessActive()
	{
		int  aux = Env.getCtx().getContextAsInt("#XX_VMR_PRINTDDACTIVE");
		if(aux == 1){
			return true;
		}else return false;

	}
	
	/**
	 * 	Set Process (in)active
	 *	@param active active
	 */
	protected static void setProcessActive (int active)
	{
		Env.getCtx().setContext("#XX_VMR_PRINTDDACTIVE",active);
	}
	
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;
		Env.getCtx().setContext( "#XX_VMR_PRINTDDACTIVE",0);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		
		try
		{
			Env.getCtx().setIsSOTrx(m_WindowNo, false);	
			Lookup l = MLookupFactory.get(Env.getCtx(), m_WindowNo, Env
					.getCtx().getContextAsInt("#XX_L_C_ORDERCOLUMN_ID"),
					DisplayTypeConstants.Search);
			purchaseSearch = new VLookup("C_Order_ID", false, false, true, l);
			purchaseSearch.setVerifyInputWhenFocusTarget(false);
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
	/**
	 */
	
	
	private void jbInit() throws Exception
	{
			
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		centerPanel.setLayout(centerLayout);
		southPanel.setLayout(southLayout);
		
		xPanel.setLayout(xLayout);

		bSearch.setEnabled(true);
		bClear.setEnabled(true);
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(900, 445));
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);

		xScrollPane.getViewport().add(table, null);
		
		northPanel.add(purchaseLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(purchaseSearch, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(vendorLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(vendorCombo, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));

		southLayout.setAlignment(FlowLayout.RIGHT);
		southLayout.setHgap(15);
		southPanel.add(bSearch, null);
		southPanel.add(bClear, null);
		southPanel.add(bPrintLabels, null);
	    southPanel.validate();
	    
	  
	}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		m_frame.getRootPane().setDefaultButton(bSearch);
		ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "C_Order_ID"),   ".", KeyNamePair.class),        //  1 Orden de Compra
			new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"),   ".", String.class),     //  2 Proveedor
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_EstimatedDate"),   ".", String.class),		//3	Fecha Estimada de LLegada
			new ColumnInfo(Msg.translate(Env.getCtx(),"M_Warehouse_ID"),".", String.class),	         //  4 Tienda
			new ColumnInfo("Proceso Completado",".", String.class),	         //  4 Estado
		};
		
		table.prepareTable(layout, "", "", false, "");
		table.setAutoResizeMode(3);
		
	
		//  Visual
		CompiereColor.setBackground (this);
		
		//  Listener
		table.getSelectionModel().addListSelectionListener(this);	
		addActionListeners();
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_PurchaseLQty"));
		statusBar.setStatusDB(table.getRowCount());
		
		// Agregar las acciones
		loadVendors();
		bPrintLabels.setEnabled(false);
		tableLoad ();
		
	}   //  dynInit

	/** Activa todos los listeners creados */
	protected final void addActionListeners () {
		
		bSearch.addActionListener(this);
		bClear.addActionListener(this);
		bPrint.addActionListener(this);
		bPrintLabels.addActionListener(this);
	} 

	/** Deshabilitar Action Listeners */
	protected final void removeActionListeners () {
		
		bSearch.removeActionListener(this);
		bClear.removeActionListener(this);
	    bPrint.removeActionListener(this);
	    bPrintLabels.removeActionListener(this);

	}	
	
	private void callProcess()
	{
		MPInstance mpi = new MPInstance( Env.getCtx(), IDPROCESO, orderToPrint); 
		mpi.save();
		ProcessInfo pi = new ProcessInfo("", IDPROCESO); 
		pi.setRecord_ID(mpi.getRecord_ID());
		pi.setAD_PInstance_ID(mpi.get_ID());
		pi.setAD_Process_ID(IDPROCESO); 
		pi.setClassName(""); 
		pi.setTitle(""); 
		ProcessCtl pc = new ProcessCtl(null ,pi,null); 
		pc.start();
	
	}//
	
	void loadVendors() {
		
		KeyNamePair loadKNP;
		String sql = "";
			
		vendorCombo.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		vendorCombo.addItem(loadKNP);
		
		sql = "SELECT C_BPARTNER_ID, NAME FROM C_BPARTNER WHERE " +
		" ISVENDOR = 'Y' AND XX_ISVALID = 'Y' AND ISACTIVE = 'Y'";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) + 
		" ORDER BY NAME ";					
	
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				vendorCombo.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	void loadPrinters() {
		cPrinterHanging.removeAllItems();
		cPrinterGlued.removeAllItems();
		
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null,null);
		KeyNamePair loadKNP;
		for (int i = 0; i < services.length; i++) {
			loadKNP = new KeyNamePair(i,services[i].getName());
			cPrinterHanging.addItem(loadKNP);
			cPrinterGlued.addItem(loadKNP);
		}
	}


	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null){
			m_frame.dispose();
			window_print.dispose();
		}
		m_frame = null;
		window_print = null;
		
	}	//	dispose

	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{		
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == bSearch){
				cmd_Search();
				window_print.dispose();
		}
		else if (e.getSource() == bClear) {		
				clearFilter();
				window_print.dispose();
		}
		else if (e.getSource() == bPrintLabels)
		{
			try {	
				if(isProcessActive()){
					ADialog.warn(1, new Container(), "El proceso ya se está ejecutando!");
			    }
				else{
				setProcessActive(1);
					loadPrinters();
			    	int tableRow = table.getSelectedRow();
					KeyNamePair orderKey = (KeyNamePair) table.getValueAt(tableRow, 0);
					orderToPrint = orderKey.getKey();
					window_print.removeAll();
					window_print = new AWindow();
					windowsPanel.setLayout(windowsLayout);
					orderNoL.setText(orderKey.getName());
					window_print.setTitle(orderSelectedL.getText()+" "+orderNoL.getText());
			
					windowsPanel.add(pHangingLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
							,GridBagConstraints.EAST, GridBagConstraints.NONE,
							new Insets(12, 12, 10, 5), 0, 0));
					windowsPanel.add(cPrinterHanging, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
							new Insets(12, 12, 10, 5), 0, 0));
					windowsPanel.add(pGluedLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
							new Insets(12, 12, 10, 5), 0, 0));
					windowsPanel.add(cPrinterGlued, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
							,GridBagConstraints.EAST, GridBagConstraints.NONE,
							new Insets(12, 12, 10, 5), 0, 0));
					windowsPanel.add(bPrint, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
							,GridBagConstraints.EAST, GridBagConstraints.NONE,
							new Insets(12, 12, 10, 5), 0, 0));
					window_print.add(windowsPanel);
			    	AEnv.showCenterScreen(window_print);
				}
		    	
			} catch (Exception ex) {
				log.log(Level.SEVERE, "", ex);
			}			
		}
		else if (e.getSource() == bPrint)
		{
			try {		
				
					PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
					int hanging = 0;
					int glued = 0;
					for(int i=0; i<services.length; i++){
						if(services[i].getName().equals(((KeyNamePair)cPrinterHanging.getValue()).getName())){
							hanging = i;
							break;
						}	
					}
					for(int i=0; i<services.length; i++){
						if(services[i].getName().equals(((KeyNamePair)cPrinterGlued.getValue()).getName())){
							glued= i;
							break;
						}	
					}
					Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Hanging",hanging);
					Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Glued", glued);
					window_print.dispose();
					callProcess();
					tableLoad();
				
			} catch (Exception ex) {
				log.log(Level.SEVERE, "", ex);
			}		
			
		}
		m_frame.setCursor(Cursor.getDefaultCursor());


	}   //  actionPerformed

	 /**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{	
		
		tableLoad();	
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_PurchaseLQty"));
		statusBar.setStatusDB(table.getRowCount());
			

	}   //  cmd_Search
	
	
	/** Limpia el filtro*/
	void clearFilter() {
		
		vendorCombo.setSelectedIndex(0);
		loadVendors();
		purchaseSearch.setValue(null);
		tableLoad();	
	}
	
	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		removeActionListeners();
		int tableRow = table.getSelectedRow();
		
		if(window_print.isVisible()){
			window_print.dispose();
		}
		if(tableRow==-1) //Si no tengo nada seleccionado en la tabla 
		{
			bPrintLabels.setEnabled(false);
			statusBar.setStatusLine(Msg.translate(Env.getCtx(),"XX_PurchaseLQty")); 
			statusBar.setStatusDB(table.getRowCount());
		}
		else 
		{
			bPrintLabels.setEnabled(true);
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "SelectedOrder")); 
			KeyNamePair orderKey = (KeyNamePair) table.getValueAt(tableRow, 0);
			statusBar.setStatusDB(orderKey.getName()); // Colocar nro de documento de la orden de compra
		
		}

		addActionListeners();
		
		if (e.getValueIsAdjusting())
			return;
		
	}   //  valueChanged


	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private void tableLoad ()
	{
		table.setRowCount(0);
		Integer order= (Integer)purchaseSearch.getValue();	
		KeyNamePair bPar= (KeyNamePair)vendorCombo.getSelectedItem();
		
		String select = "\nSELECT O.C_ORDER_ID, O.DOCUMENTNO, P.NAME, TO_CHAR(O.XX_ESTIMATEDDATE,'dd/MM/YYYY'), W.VALUE||'-'||W.NAME " +
				"\n, (CASE when O.XX_ORDERSTATUS ='CH'  AND PE.XX_ORDERREQUESTSTATUS = 'TI' THEN 'Si' " +
				"\n WHEN O.XX_ORDERSTATUS ='CH'  AND PE.XX_ORDERREQUESTSTATUS IS NOT NULL AND PE.XX_ORDERREQUESTSTATUS = 'ET' THEN 'Si' " +
				"\nWHEN O.XX_ORDERSTATUS !='CH' OR  PE.XX_ORDERREQUESTSTATUS IS NULL OR  PE.XX_ORDERREQUESTSTATUS !='ET'  THEN 'No' END) ";
		String from = "\nFROM C_ORDER O INNER JOIN C_BPARTNER P ON (O.C_BPARTNER_ID = P.C_BPARTNER_ID) "+
					  "\nINNER JOIN  M_WAREHOUSE W ON (O.M_WAREHOUSE_ID = W.M_WAREHOUSE_ID) " +
					  "\nLEFT JOIN XX_VMR_ORDER PE ON (PE.C_ORDER_ID = O.C_ORDER_ID)";
		String where = "\nWHERE O.AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+" "+
					   "\nAND O.XX_VLO_TYPEDELIVERY = 'DD' "+ //DESPACHO DIRECTO
					   "\nAND O.XX_ORDERSTATUS IN ('AP','CH','RE') "+ //APROBADA O CHEQUEADA O RECIBIDA
					   "\nAND (O.XX_PROCESSRECEPTIONSTORE = 'N' OR  O.DOCUMENTNO = '91583' OR  O.DOCUMENTNO = '91584')"; //NO PROCESADA LA RECEPCION  EN TIENDA
	
		//Orden de Compra
		if(order != null ){
			where += "\nAND o.C_ORDER_ID= " +order;
		}
		//Proveedor
		if(bPar != null && bPar.getKey() != 0){
				where += "\nAND O.C_BPARTNER_ID = " + bPar.getKey();	

		}
		String orderBy =  "\nORDER BY O.XX_ESTIMATEDDATE"; 
		String sql = select + from + where + orderBy;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
	
		try
		{
			stmt = DB.prepareStatement(sql, null);
			rs = stmt.executeQuery();
			
			int row = 0;
			while (rs.next()) {
				table.setRowCount(row + 1);
				table.setValueAt(new KeyNamePair(rs.getInt(1),rs.getString(2)), row, 0);
				table.setValueAt(rs.getString(3), row, 1);
				table.setValueAt(rs.getString(4), row, 2);
				table.setValueAt(rs.getString(5), row, 3);
				table.setValueAt(rs.getString(6), row, 4);
				row++;
			}
			
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}finally{
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}   //  tableLoad
	
}

