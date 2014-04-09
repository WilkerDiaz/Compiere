package compiere.model.cds.forms;

import java.awt.BorderLayout;
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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.framework.Lookup;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VFile;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.MLookupFactory;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.X_Ref_XX_Ref_PriorityMotive;
import compiere.model.cds.forms.indicator.XX_Indicator;
import org.compiere.swing.CComboBox;


public class XX_CheckupPriority_Form extends CPanel 
implements FormPanel, ActionListener,  ListSelectionListener{

	/**Da una lista ordenada de las O/C que tienen prioridad de chequeo
	 *  @author Gabrielle Huchet
	 *  @version 
	 */
	
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
	static CLogger log = CLogger.getCLogger(XX_CheckupPriority_Form.class);
	
	/* Panel de la ventana */
	private CPanel mainPanel = new CPanel();
	private CPanel northPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private JScrollPane xScrollPane = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder("Ordenes de Compra a Chequear");
	private CPanel xPanel = new CPanel();
	
	/* Layouts */
	private BorderLayout mainLayout = new BorderLayout();
	private GridBagLayout northLayout = new GridBagLayout();
	private FlowLayout southLayout = new FlowLayout();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	

	/* Botones */
	private CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "Update") + " Lista");
	private CButton bClear = new CButton(Msg.translate(Env.getCtx(), "Clear"));
	private CButton bPrint = new CButton(Msg.translate(Env.getCtx(), "ExportExcel"));
	private CButton bManagePriority = new CButton(Msg.translate(Env.getCtx(), "XX_ManagePriorityOC"));
	private CButton searchSelected = new CButton(Msg.translate(Env.getCtx(), "Search")+" O/C");

	/* Archivo a exportar*/
	private VFile bFile = new VFile("File", Msg.getMsg(Env.getCtx(), "File"),true, false, true, true, JFileChooser.SAVE_DIALOG);
	

	private CLabel labelFile = new CLabel();
	
	/* Campos del filtro disponibles */
	//Fecha de busqueda
	private VDate dateSearch = new VDate(Msg.translate(Env.getCtx(), "Date"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "Date"));
	private CLabel dateLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_Date"));
	//Orden de Compra
	private VLookup purchaseSearch = null;
	private CLabel purchaseLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_PurchaseOrder"));

	//Ubicación  - PROYECTO CD VALENCIA 
	private CLabel locatorLabel = new CLabel(Msg.translate(Env.getCtx(), "M_Locator_ID"));
	private CComboBox locatorCombo = new CComboBox();
	private int warehouseID = 0;
	
	/** La tabla donde se guardarán los datos*/
	private MiniTablePreparator table = new MiniTablePreparator();
	private static int MAX_PRIORIDAD = 1111; 
	
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;

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
		xScrollPane.setPreferredSize(new Dimension(800, 400));
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);

		xScrollPane.getViewport().add(table, null);
		
		northPanel.add(dateLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(dateSearch, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(locatorLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(locatorCombo, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(purchaseLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(purchaseSearch, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(searchSelected, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
	

		southLayout.setAlignment(FlowLayout.RIGHT);
		southLayout.setHgap(15);
		southPanel.add(bManagePriority, null);
		southPanel.add(bSearch, null);
	//	southPanel.add(bClear, null);
		southPanel.add(labelFile, null);
		southPanel.add(bFile, null);
		southPanel.add(bPrint, null);
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
			new ColumnInfo("",   ".", Integer.class),        			// Posición
			new ColumnInfo(Msg.translate(Env.getCtx(), "C_Order_ID"),   ".", KeyNamePair.class),     //Orden de Compra
			new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"),   ".", String.class),   	//Proveedor
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),   ".", String.class), //Departamento
			new ColumnInfo("Marcas",   ".", String.class),        //  Marcas
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Collection_ID"),   ".", String.class), //Collección
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMA_Brochure_ID"),   ".", String.class), //Folleto
			new ColumnInfo(Msg.translate(Env.getCtx(), "ProductQty"),   ".", Integer.class), 		//Cantidad de Productos
			new ColumnInfo("Motivo",   ".", String.class),        //  Motivo
			new ColumnInfo("Prioridades Máx:1,Min:4",   ".", String.class),  //   Prioridades
			new ColumnInfo("Días en Espera",   ".", Integer.class), 		//Días en Espera
			new ColumnInfo("Asistente Asignado",   ".", Boolean.class), 		//Tiene Asistente Asignado

		};
		table.prepareTable(layout, "", "", false, "");
		table.setAutoResizeMode(2);
		table.setSortEnabled(false);

		Calendar actualDate = Calendar.getInstance();
		dateSearch.setValue(actualDate.getTime());
		dateSearch.setEnabled(false);
	
		//  Visual
		CompiereColor.setBackground (this);
		
		//  Listener
		table.getSelectionModel().addListSelectionListener(this);	
		addActionListeners();
		dynLocators();
		
		if(locatorCombo.getSelectedItem()!=null){
			KeyNamePair locator = (KeyNamePair) locatorCombo.getSelectedItem();
			warehouseID = locator.getKey();
		}
		
		cmd_Search();
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_PurchaseLQty"));
		statusBar.setStatusDB(table.getRowCount());

	}   //  dynInit

	/** Activa todos los listeners creados */
	protected final void addActionListeners () {
		
		bSearch.addActionListener(this);
		bClear.addActionListener(this);
		bPrint.addActionListener(this);
		bManagePriority.addActionListener(this);
		searchSelected.addActionListener(this);
		locatorCombo.addActionListener(this);
	} 
	
	/** Deshabilitar Action Listeners */
	protected final void removeActionListeners () {
		
		bSearch.removeActionListener(this);
		bClear.removeActionListener(this);
	    bPrint.removeActionListener(this);
	    bManagePriority.removeActionListener(this);
	    searchSelected.removeActionListener(this);
	    locatorCombo.removeActionListener(this);
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

	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{		
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		removeActionListeners();
		if (e.getSource() == bSearch){
				KeyNamePair locator = (KeyNamePair) locatorCombo.getSelectedItem();
					warehouseID = locator.getKey();
			cmd_Search();
		}
		else if (e.getSource() == bClear) {			
			clearFilter();
		}
		else if (e.getSource() == bPrint)
		{
			try {			
				XX_Indicator.imprimirArchivo( table, bFile, m_WindowNo, m_frame); 
			} catch (Exception ex) {
				log.log(Level.SEVERE, "", ex);
			}			
			
		}else if (e.getSource() == bManagePriority){
			AWindow window_oc = new AWindow();
	    	window_oc.initWindow(Env.getCtx().getContextAsInt("#XX_L_W_OCPRIORITY_ID"), null); //query);
	    	AEnv.showCenterScreen(window_oc);
		}else if (e.getSource() == searchSelected){
			table.getSelectionModel().removeSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
			for (int i = 0; i < table.getRowCount(); i++) {
				if(purchaseSearch.getDisplay() != null && ((KeyNamePair)table.getValueAt(i, 1)).getName().compareTo((String)purchaseSearch.getDisplay())==0){
					table.getSelectionModel().setSelectionInterval(i,i);
					break;
				}
			}
		}else if(e.getSource() == locatorCombo){
			KeyNamePair locator = (KeyNamePair) locatorCombo.getSelectedItem();
			warehouseID = locator.getKey();
			cmd_Search();
		}
	
		addActionListeners();
	
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
		table.getSelectionModel().removeSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
		for (int i = 0; i < table.getRowCount(); i++) {
			if(purchaseSearch.getDisplay() != null && ((KeyNamePair)table.getValueAt(i, 1)).getName().compareTo((String)purchaseSearch.getDisplay())==0){
				table.getSelectionModel().setSelectionInterval(i,i);
				break;
			}
		}
	
			

	}   //  cmd_Search
	
	
	/** Limpia el filtro*/
	void clearFilter() {
	
		//dateSearch.setValue(null);
	}
	
	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		removeActionListeners();
		int tableRow = table.getSelectedRow();
			
		if(tableRow==-1) //Si no tengo nada seleccionado en la tabla 
		{
			statusBar.setStatusLine(Msg.translate(Env.getCtx(),"XX_PurchaseLQty")); 
			statusBar.setStatusDB(table.getRowCount());
		}
		else
		{
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "SelectedOrder")); 
			KeyNamePair orderKey = (KeyNamePair) table.getValueAt(tableRow, 1);
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
		deleteOCPrioritiesCH();
		table.setRowCount(0);
		Timestamp date= (Timestamp)dateSearch.getValue();	
		
		String sql =  "\nWITH " +
		   		//O/C EN ESTADO "RECIBIDA" QUE SE LES HA AÑADIDO UNA PRIORIDAD MANUALMENTE 
			   "\nPRIORIDAD_FORZADA AS(" +
			   "\nSELECT O.C_ORDER_ID, -P.XX_POSITION||'0000' NIVEL_PRIORIDAD, 0 PRIORIDAD_SECUNDARIA " +
			   "\nFROM XX_VMR_PRIORITY P INNER JOIN C_ORDER O ON (P.C_ORDER_ID = O.C_ORDER_ID) " +
			   "\nWHERE  O.AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+" AND O.XX_ORDERSTATUS = 'RE'  AND  P.ISACTIVE = 'Y' AND O.ISSOTRX = 'N' " +
			   "\n)," +
			   //O/C EN ESTADO "RECIBIDA" QUE ESTAN EN EL PROXIMO FOLLETO, Y FALTAN MENOS DE 1 MES PARA QUE SALGA DICHO FOLLETO
			   "\nPRIORIDAD_1 AS("+
			   "\nSELECT O.C_ORDER_ID, '1000' NIVEL_PRIORIDAD, 0 PRIORIDAD_SECUNDARIA"+
			   "\nFROM C_ORDER O "+
			   "\nINNER JOIN  XX_VMA_MARKETINGACTIVITY M ON (O.XX_VMA_BROCHURE_ID = M.XX_VMA_BROCHURE_ID) "+
			   "\nWHERE O.AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+" "+
			   "\nAND O.XX_VLO_TYPEDELIVERY != 'DD' "+ //NO DESPACHO DIRECTO
			   "\nAND O.XX_ORDERSTATUS = 'RE' AND O.ISSOTRX = 'N' "+ //RECIBIDA
			   "\nAND M.STARTDATE IN (SELECT MIN(M2.STARTDATE) "+//PERTENECE AL SIGUIENTE FOLLETO QUE INICIA DENTRO DE UN MES O MENOS
			   "\nFROM XX_VMA_MARKETINGACTIVITY M2 WHERE "+DB.TO_DATE(date,true)+" < M2.STARTDATE "+
			   "\nAND "+DB.TO_DATE(date,true)+" >= ADD_MONTHS(M2.STARTDATE,-1))"+
			   "\n),"+
			   //O/C EN ESTADO "RECIBIDA" QUE PERTENECEN A LA COLECCIÓN ACTUAL
			   "\nPRIORIDAD_2 AS ("+
			   "\nSELECT O.C_ORDER_ID, '100' NIVEL_PRIORIDAD, 0 PRIORIDAD_SECUNDARIA "+
			   "\nFROM C_ORDER O, XX_VMR_PO_LINEREFPROV RP, XX_VMR_LINE LI, XX_VMR_COLLECTION CO "+
			   "\nWHERE  O.AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+" AND O.XX_ORDERSTATUS = 'RE'  "+
			   "\nAND O.C_ORDER_ID = RP.C_ORDER_ID AND O.ISSOTRX = 'N' "+
			   "\nAND RP.XX_VMR_LINE_ID = LI.XX_VMR_LINE_ID "+
			   "\nAND LI.XX_VMR_TYPEINVENTORY_ID = 1000027  "+ //TENDENCIA
			   "\nAND CO.XX_VMR_COLLECTION_ID = O.XX_VMR_COLLECTION_ID "+
			   "\nAND TO_CHAR("+DB.TO_DATE(date,true)+",'MM') IN (CO.XX_STARTINGMONTH, CO.XX_ENDINGMONTH) "+
			   "\nGROUP BY O.C_ORDER_ID "+
			   "\n)," +
			   "\nINVENTARIO AS ( "+
			   "\nSELECT SUM(XX_FINALINVAMOUNTBUD2) INVPRE, SUM(XX_INVAMOUNTFINALREAL) INVREAL, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID " +
			   "\nFROM XX_VMR_PRLD01 " +
			   "\nWHERE AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+
			   "\nAND XX_BUDGETYEARMONTH =  TO_CHAR("+DB.TO_DATE(date,true)+",'YYYYMM') "+
			   "\nAND M_WAREHOUSE_ID != "+ Env.getCtx().getContextAsInt("XX_L_WAREHOUSECENTRODIST_ID")+
			   "\nGROUP BY M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID" +
			   "\n), " +
			   //O/C EN ESTADO "RECIBIDA" QUE TIENEN MENOR INVENTARIO QUE EL PRESUPUESTADO
			   "\nPRIORIDAD_3 AS (" +
			   "\nSELECT O.C_ORDER_ID, '10' NIVEL_PRIORIDAD, SUM(I.INVPRE-I.INVREAL) PRIORIDAD_SECUNDARIA " +
			   "\nFROM C_ORDER O INNER JOIN XX_VMR_PO_LINEREFPROV RP ON (O.C_ORDER_ID = RP.C_ORDER_ID) " +
			   "\nINNER JOIN INVENTARIO I ON (O.XX_VMR_DEPARTMENT_ID = I.XX_VMR_DEPARTMENT_ID AND RP.XX_VMR_LINE_ID = I.XX_VMR_LINE_ID) " +
			   "\nWHERE O.AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+
			   "\nAND I.INVPRE > I.INVREAL AND XX_ORDERSTATUS = 'RE' AND O.ISSOTRX = 'N' " +
			   "\nGROUP BY O.C_ORDER_ID " +
			   "\n)," +
			   "\nVENTAS AS (" +
			   "\nSELECT SUM(XX_SALESAMOUNTBUD2) VENTASPRE, SUM(XX_AMOUNTACTUALSALE) VENTASREAL, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID " +
			   "\nFROM XX_VMR_PRLD01 " +
			   "\nWHERE AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+
			   "\nAND XX_BUDGETYEARMONTH = TO_CHAR("+DB.TO_DATE(date,true)+",'YYYYMM')"+
			   "\nAND M_WAREHOUSE_ID != "+ Env.getCtx().getContextAsInt("XX_L_WAREHOUSECENTRODIST_ID")+
			   "\nGROUP BY M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID " +
			   "\n), " +
			   //O/C EN ESTADO "RECIBIDA"  QUE TIENEN MAYOR VENTA QUE LA PRESUPUESTADA
			   "\nPRIORIDAD_4 AS( " +
			   "\nSELECT O.C_ORDER_ID,  '1' NIVEL_PRIORIDAD, SUM(V.VENTASREAL-V.VENTASPRE) PRIORIDAD_SECUNDARIA " +
			   "\nFROM C_ORDER O INNER JOIN XX_VMR_PO_LINEREFPROV RP ON (O.C_ORDER_ID = RP.C_ORDER_ID) " +
			   "\nINNER JOIN VENTAS V ON (O.XX_VMR_DEPARTMENT_ID = V.XX_VMR_DEPARTMENT_ID AND RP.XX_VMR_LINE_ID = V.XX_VMR_LINE_ID) " +
			   "\nWHERE O.AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+
			   "\nAND V.VENTASPRE < V.VENTASREAL AND XX_ORDERSTATUS = 'RE' AND O.ISSOTRX = 'N' " +
			   "\nGROUP BY O.C_ORDER_ID " +
			   "\n)," +
			   //O/C EN ESTADO "RECIBIDA" 
			   "\nRECIBIDAS AS( " +
			   "\nSELECT O.C_ORDER_ID,  '0' NIVEL_PRIORIDAD, " +
			   "\nCASE WHEN SUM(V.VENTASREAL-V.VENTASPRE) IS NULL THEN -999999999 ELSE SUM(V.VENTASREAL-V.VENTASPRE) END PRIORIDAD_SECUNDARIA " +
			   "\nFROM C_ORDER O INNER JOIN XX_VMR_PO_LINEREFPROV RP ON (O.C_ORDER_ID = RP.C_ORDER_ID) " +
			   "\nLEFT JOIN VENTAS V ON (O.XX_VMR_DEPARTMENT_ID = V.XX_VMR_DEPARTMENT_ID AND RP.XX_VMR_LINE_ID = V.XX_VMR_LINE_ID) " +
			   "\nWHERE O.AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+
			   "\nAND XX_ORDERSTATUS = 'RE' AND O.ISSOTRX = 'N' " +
			   "\nGROUP BY O.C_ORDER_ID " +
			   "\n)," +
			   "\nTODAS_PRIORIDADES AS (" +
			   "\nSELECT * FROM  PRIORIDAD_1  UNION " +
			   "\nSELECT * FROM  PRIORIDAD_2 UNION " +
			   "\nSELECT * FROM  PRIORIDAD_3 UNION " +
			   "\nSELECT * FROM  PRIORIDAD_4 UNION" +
			   "\nSELECT * FROM  RECIBIDAS UNION" +
			   "\nSELECT * FROM  PRIORIDAD_FORZADA " +
			   "\n)"+
			   "\nSELECT O.C_ORDER_ID, O.DOCUMENTNO, BP.NAME, D.VALUE||'-'||D.NAME, C.NAME, B.NAME, " +
			   "\nCASE WHEN MIN(NIVEL_PRIORIDAD)< 1 THEN TO_NUMBER(MIN(NIVEL_PRIORIDAD)) ELSE SUM(DISTINCT NIVEL_PRIORIDAD) END," +
			   "\nCASE WHEN SUM(DISTINCT NIVEL_PRIORIDAD)=1111 THEN 'Prioridades 1,2,3,4' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1110 THEN 'Prioridades 1,2,3' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1101 THEN 'Prioridades 1,2,4' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1100 THEN 'Prioridades 1,2' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1011 THEN 'Prioridades 1,3,4' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1010 THEN 'Prioridades 1,3' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1001 THEN 'Prioridades 1,4' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1000 THEN 'Prioridad 1' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=111 THEN 'Prioridad 2,3,4' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=110 THEN 'Prioridad 2,3' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=101 THEN 'Prioridad 2,4' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=100 THEN 'Prioridad 2' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=11 THEN 'Prioridad 3,4' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=10 THEN 'Prioridad 3' " +
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1 THEN 'Prioridad 4' "+
			   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=0 THEN 'Sin Prioridad' ELSE 'Prioridad Forzada' END PRIORIDADES, " +
			   "\nTRUNC(AVG(SYSDATE - O.XX_ReceptionDate)) DIAS_ESPERA," +
			   "\nDECODE(IO.XX_CHECKASSISTANT_ID,NULL,0,1) ASISTENTE_ASIGNADO" +
			   "\nFROM  TODAS_PRIORIDADES T INNER JOIN C_ORDER O ON (O.C_ORDER_ID = T.C_ORDER_ID)" +
			   "\nJOIN M_INOUT IO ON (O.C_ORDER_ID = IO.C_ORDER_ID) " +
			   "\nLEFT JOIN C_BPARTNER BP ON (BP.C_BPARTNER_ID = O.C_BPARTNER_ID) " +
			   "\nLEFT JOIN XX_VMR_DEPARTMENT D ON (D.XX_VMR_DEPARTMENT_ID = O.XX_VMR_DEPARTMENT_ID) " +
			   "\nLEFT JOIN XX_VMR_COLLECTION C ON (C.XX_VMR_COLLECTION_ID = O.XX_VMR_COLLECTION_ID)" +
			   "\nLEFT JOIN XX_VMA_BROCHURE B ON (B.XX_VMA_BROCHURE_ID = O.XX_VMA_BROCHURE_ID) " +
			   "\nWHERE IO.ISSOTRX = 'N' AND O.ISSOTRX = 'N' AND IO.XX_INOUTSTATUS = 'RE' AND O.M_WAREHOUSE_ID = "+warehouseID+
			   "\nGROUP BY O.C_ORDER_ID, O.DOCUMENTNO, BP.NAME, D.VALUE||'-'||D.NAME, C.NAME, B.NAME, IO.XX_CHECKASSISTANT_ID " +
			   "\nORDER BY SUM(DISTINCT NIVEL_PRIORIDAD) DESC, SUM(PRIORIDAD_SECUNDARIA) DESC, TRUNC(AVG(SYSDATE - O.XX_ReceptionDate)) DESC, O.DOCUMENTNO ASC ";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmt_2 =null;
        ResultSet rs_2 = null;
        String sql_temp = null;
		try
		{
			stmt = DB.prepareStatement(sql, null);
			rs = stmt.executeQuery();
			
			int row = 0;
			int rowPrio = 0;
			while (rs.next()) {
			
				String prioridad = rs.getString(7);
				String posicion = null;
				String brands = null;
				Integer productQty = null;
				String motive = null;		
				table.setRowCount(row + 1);
				try
				{
					sql_temp = "\nSELECT DISTINCT B.NAME " +
							"\nFROM XX_VMR_PO_LINEREFPROV PO  " +
							"\nINNER JOIN XX_VMR_BRAND B ON (B.XX_VMR_BRAND_ID =PO.XX_VMR_BRAND_ID) " +
							"\nWHERE PO.C_ORDER_ID = "+rs.getInt(1);
					stmt_2 = DB.prepareStatement(sql_temp, null);
					rs_2 = stmt_2.executeQuery();
					while (rs_2.next()) { //Marcas
						if(brands ==null){
							brands = rs_2.getString(1); 
						}else
							brands = brands+", "+rs_2.getString(1); 
					}
				
				}catch (SQLException e){
					log.log(Level.SEVERE, sql_temp, e);
				}
				finally {
					DB.closeResultSet(rs_2);
					DB.closeStatement(stmt_2);
				}
				try
				{
					sql_temp = "\nSELECT SUM(IOL.PICKEDQTY) " +
							"FROM  M_INOUTLINE IOL, M_INOUT M " +
							"WHERE IOL.M_INOUT_ID = M.M_INOUT_ID " +
							"AND M.ISSOTRX='N' AND M.C_ORDER_ID ="+rs.getInt(1);
					stmt_2 = DB.prepareStatement(sql_temp, null);
					rs_2 = stmt_2.executeQuery();
					while (rs_2.next()) {  //cantidad de productos
							productQty = rs_2.getInt(1);
					}
				
				}catch (SQLException e){
					log.log(Level.SEVERE, sql_temp, e);
				}
				finally {
					DB.closeResultSet(rs_2);
					DB.closeStatement(stmt_2);
				}
				if(Integer.parseInt(prioridad)*(-1) > MAX_PRIORIDAD ){
						posicion = prioridad.substring(1, prioridad.length()-4);
						
						try
						{
							sql_temp = "\nSELECT XX_MOTIVE " +
									"\nFROM XX_VMR_PRIORITY P "+
									"\nWHERE C_ORDER_ID = "+rs.getInt(1);
							stmt_2 = DB.prepareStatement(sql_temp, null);
							rs_2 = stmt_2.executeQuery();
							while (rs_2.next()) {  //cantidad de productos
								for (X_Ref_XX_Ref_PriorityMotive v : X_Ref_XX_Ref_PriorityMotive.values()) {
									if (v.getValue().compareTo(rs_2.getString(1))==0)
										motive = v.name();
								}
							}
						
						}catch (SQLException e){
							log.log(Level.SEVERE, sql_temp, e);
						}
						finally {
							DB.closeResultSet(rs_2);
							DB.closeStatement(stmt_2);
						}
						if (Integer.parseInt(posicion)< row){
					//		System.out.println("posicion menor q total:"+rs.getString(2)+", "+rs.getString(8)+", "+posicion);
							rowPrio = Integer.parseInt(posicion)-1;
							moveRow(rowPrio, row-1, row);
							table.setValueAt(rowPrio+1, rowPrio, 0); 
							table.setValueAt(new KeyNamePair(rs.getInt(1),rs.getString(2)), rowPrio, 1); //orden de compra
							table.setValueAt(rs.getString(3), rowPrio, 2); // proveedor
							table.setValueAt(rs.getString(4), rowPrio, 3); // departamento
							table.setValueAt(brands, rowPrio, 4); // marcas
							table.setValueAt(rs.getString(5), rowPrio, 5); // coleccion
							table.setValueAt(rs.getString(6), rowPrio, 6); // folleto
							table.setValueAt(productQty, rowPrio, 7); // Cantidad de Productos
							table.setValueAt(motive, rowPrio, 8); //Motivo
							table.setValueAt(rs.getString(8), rowPrio, 9); // prioridades			
							table.setValueAt(rs.getInt(9), rowPrio, 10); // días en espera	
							table.setValueAt(rs.getBoolean(10), rowPrio, 11); // asistente asignado	
						}else {
					//		System.out.println("posicion mayor q total:"+rs.getString(2)+","+rs.getString(8)+", "+posicion);
							table.setValueAt(row+1, row, 0); 
							table.setValueAt(new KeyNamePair(rs.getInt(1),rs.getString(2)), row, 1); //orden de compra
							table.setValueAt(rs.getString(3), row, 2); // proveedor
							table.setValueAt(rs.getString(4), row, 3); // departamento
							table.setValueAt(brands, row, 4); // marcas
							table.setValueAt(rs.getString(5), row, 5); // coleccion
							table.setValueAt(rs.getString(6), row, 6); // folleto
							table.setValueAt(productQty, row, 7); // Cantidad de Productos
							table.setValueAt(motive, row, 8); //Motivo
							table.setValueAt(rs.getString(8), row, 9); // prioridades
							table.setValueAt(rs.getInt(9), row, 10); // días en espera	
							table.setValueAt(rs.getBoolean(10), row, 11); // asistente asignado	
						}
				}else {
				//	System.out.println("menor prioridad:"+rs.getString(2)+","+rs.getString(8));
					table.setValueAt(row+1, row, 0); 
					table.setValueAt(new KeyNamePair(rs.getInt(1),rs.getString(2)), row, 1); //orden de compra
					table.setValueAt(rs.getString(3), row, 2); // proveedor
					table.setValueAt(rs.getString(4), row, 3); // departamento
					table.setValueAt(brands, row, 4); // marcas
					table.setValueAt(rs.getString(5), row, 5); // coleccion
					table.setValueAt(rs.getString(6), row, 6); // folleto
					table.setValueAt(productQty, row, 7); // Cantidad de Productos
					table.setValueAt(motive, row, 8); //Motivo
					table.setValueAt(rs.getString(8), row, 9); // prioridades
					table.setValueAt(rs.getInt(9), row, 10); // días en espera	
					table.setValueAt(rs.getBoolean(10), row, 11); // asistente asignado	
				}
				row++;
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(stmt);
		}
	}   //  tableLoad
	
	/**Mueve una o más filas comenzando desde ini hasta end 
	 * y las coloca una fila mas abajo hasta llegar a newEnd */
	private void moveRow(int ini, int end, int newEnd) {
		int row = newEnd;
		for (int j = end; j >= ini; j--) {
			table.setValueAt(row+1, row, 0); //posición
			table.setValueAt(table.getValueAt(j, 1), row, 1); //orden de compra
			table.setValueAt(table.getValueAt(j, 2), row, 2); // proveedor
			table.setValueAt(table.getValueAt(j, 3), row, 3); // departamento
			table.setValueAt(table.getValueAt(j, 4), row, 4); // marcas
			table.setValueAt(table.getValueAt(j, 5), row, 5); // coleccion
			table.setValueAt(table.getValueAt(j, 6), row, 6); // folleto
			table.setValueAt(table.getValueAt(j, 7), row, 7); // Cantidad de Productos
			table.setValueAt(table.getValueAt(j, 8), row, 8); // Motivo
			table.setValueAt(table.getValueAt(j, 9), row, 9); // prioridades
			table.setValueAt(table.getValueAt(j, 10), row, 10); // días en espera	
			table.setValueAt(table.getValueAt(j, 11), row, 11); // asistente asignado	
			row =row-1;
		}
	}

	/** Elimina de la tabla XX_VMR_PRIORITY las ordenes de compra que ya fueron chequeadas */
	private void deleteOCPrioritiesCH() {
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		
		String sql = "\nSELECT P.C_ORDER_ID " +
				"\nFROM XX_VMR_PRIORITY P INNER JOIN C_ORDER O ON (P.C_ORDER_ID = O.C_ORDER_ID)" +
				"\nWHERE XX_ORDERSTATUS != 'RE'";
		try {
			stmt = DB.prepareStatement(sql, null);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				
				String sqlDelete = "DELETE FROM XX_VMR_PRIORITY WHERE C_ORDER_ID = " +rs.getInt(1);			
				DB.executeUpdate(null, sqlDelete);
				
				//System.out.println("\nSe eliminó de tabla de prioridades la O/C nro: "+rs.getInt(1));
			}
		}catch (Exception e) {
			System.out.println("\nError eliminando O/C de la tabla de prioridades " +e.getMessage());
		}finally{
			
			DB.closeResultSet(rs);
			DB.closeStatement(stmt);
		}
	}
	
	private boolean dynLocators() {
		locatorCombo.removeActionListener(this);
		String sql = "\nSELECT  W.M_WAREHOUSE_ID, W.VALUE||'-'||W.NAME " +
				"\nFROM M_WAREHOUSE W WHERE   W.XX_ISSTORE = 'N' " +
				"ORDER BY W.VALUE";
		PreparedStatement ps = null;
		ResultSet rs = null;
		locatorCombo.removeAllItems();
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while(rs.next()) {
				locatorCombo.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {ps.close();}
				catch (Exception e) {}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {}				
			} 
		}		
		locatorCombo.setSelectedIndex(0);
		locatorCombo.addActionListener(this);
		return (locatorCombo.getItemCount() > 1);

	}
	

}

