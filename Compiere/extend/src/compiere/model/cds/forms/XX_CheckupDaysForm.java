package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.framework.Lookup;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import compiere.model.cds.X_Ref_XX_DistributionStatus;

/**
 * Forma para consultar el Ponderado de Días de Chequeo.
 * @author Gabriela Marques
 *
 */

public class XX_CheckupDaysForm  extends CPanel implements FormPanel, ActionListener {
		

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);

		try {
			//	UI
			//productSearch = VLookup.createProduct(m_WindowNo);
//			productSearch.setVerifyInputWhenFocusTarget(false);
			// Se requiere un keynamepair para traer el id de la ventana

			Lookup l = MLookupFactory.get(Env.getCtx(), m_WindowNo, Env
					.getCtx().getContextAsInt("#XX_L_C_ORDERCOLUMN_ID"),
					DisplayTypeConstants.Search);
			purchaseSearch = new VLookup("C_Order_ID", false, false, true, l);
			purchaseSearch.setVerifyInputWhenFocusTarget(false);
			
			jbInit(); // Layouts
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			//frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
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
	static CLogger 			log = CLogger.getCLogger(XX_CheckupDaysForm.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();

	static StringBuffer    m_sql = null;
	static StringBuffer    m_sqlDetail = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	String		   m_selectT1 = ""; //Tabla1
	String		   m_selectT2 = ""; //Tabla detalle
	static StringBuilder sql;
	
	
	// LAYOUT >>>>>>>>
		//panel - tablas
		private CPanel mainPanel = new CPanel();
		private BorderLayout mainLayout = new BorderLayout();
		private CPanel northPanel = new CPanel(); 
		private BorderLayout northLayout = new BorderLayout();
		private CPanel parameterPanel = new CPanel();
		private CPanel parameterPanel2 = new CPanel();
		private GridBagLayout parameterLayout = new GridBagLayout();
		private CPanel commandPanel = new CPanel();
		private FlowLayout commandLayout = new FlowLayout();
//		private CPanel southPanel = new CPanel();
//		private BorderLayout southLayout = new BorderLayout();
//		private StatusBar statusBar = new StatusBar();
		private JScrollPane dataPane = new JScrollPane();
		private CPanel resultPanel = new CPanel();
		private GridBagLayout resultLayout = new GridBagLayout();
	// FIN LAYOUT <<<<<<<<<<<
	
	// FILTROS >>>>>>>>>>>
		//Checkboxes
		private CLabel labelOC = new CLabel("Orden de Compra");
		private CCheckBox checkOC = new CCheckBox();
		private CLabel labelPedido = new CLabel("Pedido");
		private CCheckBox checkPedido = new CCheckBox();
		//Orden de compra
		private CLabel labelNumOC = new CLabel("Orden de Compra"+":");
		private VLookup purchaseSearch = null;	
		//Fechas
		private CLabel labelDateOC = new CLabel(Msg.getMsg(Env.getCtx(), "DateFrom")+":");
		private CLabel labelToDateOC = new CLabel(Msg.getMsg(Env.getCtx(), "DateTo")+":");
		private VDate dateFromOC = new VDate(Msg.translate(Env.getCtx(), "DateFrom"), false, false, true, 
				DisplayTypeConstants.Date, Msg.translate(Env.getCtx(), "DateFrom"));
		private VDate dateToOC = new VDate(Msg.translate(Env.getCtx(), "DateTo"),	false, false, true,
				DisplayTypeConstants.Date, Msg.translate(Env.getCtx(), "DateTo"));
		//Colección
		private CLabel labelCollec = new CLabel("Colección"+":");
		private static CComboBox comboCollec = new CComboBox();
		//Paquete
		private CLabel labelPaquete = new CLabel("Paquete"+":");
		private static CComboBox comboPaquete = new CComboBox();
		//Pedido
		private CLabel labelNumPedido = new CLabel("Correlativo de Pedido"+":");
		private CTextField numPedido = new CTextField();
		//Fechas pedido
		private CLabel labelDate = new CLabel(Msg.getMsg(Env.getCtx(), "DateFrom")+":");
		private CLabel labelToDate = new CLabel(Msg.getMsg(Env.getCtx(), "DateTo")+":");
		private VDate dateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"), false, false, true, 
				DisplayTypeConstants.Date, Msg.translate(Env.getCtx(), "DateFrom"));
		private VDate dateTo = new VDate(Msg.translate(Env.getCtx(), "DateTo"),	false, false, true,
				DisplayTypeConstants.Date, Msg.translate(Env.getCtx(), "DateTo"));
		//Distribución
		private CLabel labelDist = new CLabel("Distribución"+":");
		private static CComboBox comboDist = new CComboBox();
	// FIN FILTROS <<<<<<<
	
	//buttons
	private CButton bSearch = new CButton();
	private JButton bReset = ConfirmPanel.createResetButton(Msg.getMsg(Env.getCtx(),"Clear"));

	private final static int FIELDSPERGROUP = 2, OCRESULTS = 3, PEDRESULTS = 4;

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

		removeActionListeners();

		//  Visual
		CompiereColor.setBackground (this);
		m_frame.getRootPane().setDefaultButton(bSearch);
		mainPanel.setLayout(mainLayout);
		
		northPanel.setLayout(northLayout);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		
		//Creación del panel de filtro1 
		panelPedido(); //Por defecto
		
		//Panel de botones
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		northPanel.add(commandPanel, BorderLayout.SOUTH);
		bSearch.setText("Calcular");
		bSearch.setPreferredSize(new Dimension(100,28));	
		bSearch.setEnabled(true);
		commandPanel.add(bSearch, null);
		commandPanel.add(bReset, null);	
		
		//Panel central
		resultPanel.setLayout(resultLayout);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(resultPanel);	
		dataPane.setPreferredSize(new Dimension(700, 200));
		
		//Panel inferior
		
		addActionListeners();
		
			
	}   //  jbInit

	
	private void panelOC() {
		//Creación del panel de filtro1 
		parameterPanel.setLayout(parameterLayout);
		northPanel.add(parameterPanel, BorderLayout.NORTH);
		
		//********PARÁMETROS********//
		int i=0;
		// CHECK pedido
		parameterPanel.add(checkPedido, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 60, 5, 5), 0, 0));
		parameterPanel.add(labelPedido, new GridBagConstraints(2, i, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(8, 0, 5, 5), 0, 0));		
		// CHECK Orden de compra 
		parameterPanel.add(checkOC, new GridBagConstraints(4, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 5, 5, 5), 0, 0));
		parameterPanel.add(labelOC, new GridBagConstraints(5, i++, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(8, 0, 5, 130), 0, 0));
		
		
		
		// Orden de Compra	
		parameterPanel.add(labelNumOC, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 60, 5, 5), 0, 0));
		parameterPanel.add(purchaseSearch, new GridBagConstraints(3, i++, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 0, 5, 130), 0, 0));
		
		// Fechas
		parameterPanel.add(labelDateOC, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 60, 10, 5), 0, 0));
		parameterPanel.add(dateFromOC, new GridBagConstraints(3, i, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(8, 0, 10, 130), 0, 0));
		parameterPanel.add(labelToDateOC, new GridBagConstraints(4, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 5, 10, 5), 0, 0));
		parameterPanel.add(dateToOC, new GridBagConstraints(6, i++, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(8, 0, 10, 60), 0, 0));
		
		
		// Colección	
		parameterPanel.add(labelCollec, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 60, 10, 5), 0, 0));
		parameterPanel.add(comboCollec, new GridBagConstraints(3, i, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(8, 0, 10, 130), 0, 0));
		// Paquete
		parameterPanel.add(labelPaquete, new GridBagConstraints(4, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 5, 10, 5), 0, 0));
		parameterPanel.add(comboPaquete, new GridBagConstraints(6, i++, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(8, 0, 10, 60), 0, 0));
		
			
	}
	
	private void panelPedido() {
		//Creación del panel de filtro2
		parameterPanel2.setLayout(parameterLayout);
		northPanel.add(parameterPanel2, BorderLayout.NORTH);
		
		int i=0;
		
		// CHECK pedido
		parameterPanel2.add(checkPedido, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 60, 5, 5), 0, 0));
		parameterPanel2.add(labelPedido, new GridBagConstraints(2, i, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(8, 0, 5, 5), 0, 0));
		// CHECK Orden de compra 
		parameterPanel2.add(checkOC, new GridBagConstraints(4, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 5, 5, 5), 0, 0));
		parameterPanel2.add(labelOC, new GridBagConstraints(5, i++, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(8, 0, 5, 130), 0, 0));
		
		// Pedido	
		parameterPanel2.add(labelNumPedido, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 60, 5, 5), 0, 0));
		parameterPanel2.add(numPedido, new GridBagConstraints(3, i, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 0, 5, 130), 0, 0));
		// Distribución	
		parameterPanel2.add(labelDist, new GridBagConstraints(4, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel2.add(comboDist, new GridBagConstraints(6, i++, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 0, 5, 5), 0, 0));
		
		// Fechas
		parameterPanel2.add(labelDate, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 60, 5, 5), 0, 0));
		parameterPanel2.add(dateFrom, new GridBagConstraints(3, i, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(8, 0, 5, 130), 0, 0));
		parameterPanel2.add(labelToDate, new GridBagConstraints(4, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 5, 5, 5), 0, 0));
		parameterPanel2.add(dateTo, new GridBagConstraints(6, i++, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(8, 0, 5, 60), 0, 0));
		
	}
	

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit() 	{	

		checkOC.setValue(false);
		checkPedido.setValue(true);
		
		//Llenar los filtros de busquedas
		loadBasicInfo();
	}   //  dynInit

	
	

	/**
	 *  Table initial state 
	 */
	private void loadBasicInfo() {
		removeActionListeners();
				
		/*//OC
		purchaseSearch.setEnabled(true);
		dateFromOC.setEnabled(true);
		dateToOC.setEnabled(true);
		
		//Pedido
		numPedido.setEnabled(true);
		comboStatusPed.setEnabled(true);
		dateFrom.setEnabled(true);
		dateTo.setEnabled(true);
		comboCollec.setEnabled(true);
		comboDist.setEnabled(true);*/
		

		comboPaquete.setEnabled(false); // Depende de la colección
		
		//Limpiar combos
		comboCollec.removeAllItems();
		comboPaquete.removeAllItems();
		comboDist.removeAllItems();
		dateFromOC.setValue(null);
		dateToOC.setValue(null);
		dateFrom.setValue(null);
		dateTo.setValue(null);
		numPedido.setValue(null);
		//Llenar los filtros de busquedas
		llenarcombos();
		
		addActionListeners();
		
	}

	/**
	 * 	Dispose
	 */
	public void dispose() 	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
		
	}	//	dispose

	

	/**
	 * 
	 */
	private  void llenarcombos(){
		KeyNamePair blanco = new KeyNamePair(0, new String()); //En blanco
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		// COMBO DISTRIBUCION
		String sql = " SELECT XX_VMR_DISTRIBUTIONHEADER_ID FROM XX_VMR_DISTRIBUTIONHEADER" +
				" WHERE XX_DISTRIBUTIONSTATUS = '" + X_Ref_XX_DistributionStatus.APROBADA.getValue() + "'";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		sql += " ORDER BY XX_VMR_DISTRIBUTIONHEADER_ID ";

		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			comboDist.addItem(blanco);
			while (rs.next()) {
				comboDist.addItem(new KeyNamePair(rs.getInt(1), rs.getString(1)));
			}
		} catch (SQLException e) { 
			log.log(Level.SEVERE, sql, e);
			e.printStackTrace();
		} finally{
			try { rs.close(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			}
			try { pstmt.close(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			}
		}
		
		// COMBO COLECCIÓN
		
		sql = "SELECT C.XX_VMR_COLLECTION_ID, C.NAME FROM XX_VMR_COLLECTION C WHERE C.ISACTIVE = 'Y' ORDER BY C.NAME";
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			comboCollec.addItem(blanco);
			//comboCollec.addItem(new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllCollections")));
			while (rs.next()) {
				comboCollec.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) { 
			log.log(Level.SEVERE, sql, e);
			e.printStackTrace();
		} finally{
			try { rs.close(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			}
			try { pstmt.close(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			}
		}

	}
	
	/**
	 * 
	 */
	private  void cargarPaquetes(KeyNamePair coll){
		KeyNamePair blanco = new KeyNamePair(0, new String()); //En blanco
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT C.XX_VMR_PACKAGE_ID, C.NAME FROM XX_VMR_PACKAGE C WHERE C.ISACTIVE = 'Y' " +
				" AND XX_VMR_COLLECTION_ID = "+coll.getKey() +"ORDER BY C.NAME";
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			comboPaquete.addItem(blanco);
			while (rs.next()) {
				comboPaquete.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) { 
			log.log(Level.SEVERE, sql, e);
			e.printStackTrace();
		} finally{
			try { rs.close(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			}
			try { pstmt.close(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			}
		}

	}
	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e) {		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		// Cargar lista de referencias si se ha seleccionado BPartner
		if(e.getSource() == checkOC && ((Boolean) checkOC.getValue())
				|| (e.getSource() == checkPedido && !((Boolean) checkPedido.getValue()))) {
			
			removeActionListeners();
			
			northPanel.remove(parameterPanel2); // Eliminamos el segundo panel
			panelOC(); // Creamos el primer panel
			northPanel.validate();
			mainPanel.validate();
			
			//Activar OC
			checkOC.setValue(true);
			checkPedido.setValue(false);
			
			addActionListeners();
		} else if ( (e.getSource() == checkOC && !((Boolean) checkOC.getValue()))
				|| (e.getSource() == checkPedido && ((Boolean) checkPedido.getValue())) ) {
			
			removeActionListeners();
			
			northPanel.remove(parameterPanel); // Eliminamos el primer panel
			panelPedido(); // Creamos el segundo panel
			northPanel.validate();
			mainPanel.validate();
			checkOC.setValue(false);
			checkPedido.setValue(true);
			
			addActionListeners();
		}
		else if (e.getSource() == comboCollec) {			
			removeActionListeners();
			// Limpiar combo
			comboPaquete.removeAllItems();
			KeyNamePair coll = (KeyNamePair)comboCollec.getSelectedItem();
			if (coll != null && coll.getKey() != 0 && coll.getKey() != 99999999){ 
				comboPaquete.setEnabled(true);
				cargarPaquetes(coll);
			}	else {
				comboPaquete.setEnabled(false);
			}
			addActionListeners();
		}

		else if (e.getSource() == bSearch) {
			removeActionListeners();
			cmd_Search();
			addActionListeners();
		} 
		else if (e.getSource() == bReset) {
			loadBasicInfo();
		} else {}
		
	}   //  actionPerformed

	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()  {

		//System.out.println("busqueda");
		resultPanel.removeAll();
		resultPanel.validate();
		if ((Boolean) checkOC.getValue()) { // ORDEN DE COMPRA
			addLabel(new CLabel("Ponderado de días Autorizado:"),1,0,1);
			addLabel(new CLabel("Ponderado de días Recibido:"),2,0,1);
			addLabel(new CLabel("Ponderado total:"),3,0,1);
			calculoOC();
			
		} else { // 
			addLabel(new CLabel("Ponderado de días Pedido:"),1,0,1);
			addLabel(new CLabel("Ponderado de días En Bahía:"),2,0,1);
			addLabel(new CLabel("Ponderado de días En Tránsito:"),3,0,1);
			addLabel(new CLabel("Ponderado total:"),4,0,1);
			calculoPedido();
		}
		
		dataPane.repaint();
		repaint();
		resultPanel.validate();
		//mainPanel.validate();
	}   //  cmd_Search
	  
	
	private void calculoOC() {
		
		// QUERY
		String query = "", filtro = "";
		
		//ADialog.info(1, new Container(), "XX_DaysPerPuchaseOrder");
		
		// Filtro: Número de orden de compra o fechas
		filtro = "";
		if (purchaseSearch.getValue()!=null) {
			filtro += " AND c_order_id=" + purchaseSearch.getValue();
		}
		if (dateFromOC.getValue() !=null & dateToOC.getValue()!=null) {
			String dateF = DB.TO_DATE((Timestamp) dateFromOC.getValue(),true); 
			String dateT = DB.TO_DATE((Timestamp) dateToOC.getValue(),true);
			filtro += " AND XX_EntranceDate between " + dateF + " and " + dateT +"+1"; 
		}
		if (comboCollec.getSelectedIndex()!=0) { // (?)
//			filtro += " (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID )" +
//					" in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id=" 
//					+ ((KeyNamePair)comboCollec.getSelectedItem()).getKey() +")";
			filtro += " AND XX_VMR_COLLECTION_ID = " + ((KeyNamePair)comboCollec.getSelectedItem()).getKey();
			if (comboPaquete.getSelectedIndex()!=0) { // (?)
				filtro += " AND XX_VMR_PACKAGE_ID = " + ((KeyNamePair)comboPaquete.getSelectedItem()).getKey();
			}
		}
		
		
		query = "\n WITH ocRECHE as (select c_order_id, sum(xx_lineqty) line, xx_receptiondate, xx_entrancedate, xx_checkupdate " +
				"\n		FROM c_order o join xx_vmr_po_linerefprov using (c_order_id)" +
				"\n		WHERE xx_orderstatus in ('RE','CH') and o.isSOTRX='N' " + filtro;
		query += "\n		GROUP BY c_order_id, xx_receptiondate, xx_entrancedate, xx_checkupdate)," +
				"\n	ocCH as (select c_order_id, sum(xx_lineqty) line, xx_receptiondate, xx_checkupdate" +
				"\n		FROM c_order o join xx_vmr_po_linerefprov using (c_order_id)" +
				"\n		WHERE xx_orderstatus = 'CH' and o.isSOTRX='N' " + filtro;
		query += "\n		GROUP BY c_order_id,  xx_receptiondate, xx_checkupdate)" +
				"\n SELECT 'aER' tipo, coalesce(round(sum(line*(xx_receptiondate-xx_entrancedate))/sum(line),3),0) dias from ocRECHE " +
				"\n UNION ALL " +
				"\n SELECT 'bRC' tipo, coalesce(round(sum(line*(xx_checkupdate-xx_receptiondate))/sum(line),3),0) dias from ocCH " +
				"\n UNION ALL" +
				"\n SELECT 'cEC' tipo, coalesce(round(sum(line*(xx_checkupdate-xx_entrancedate))/sum(line),3),0) dias from ocRECHE ";
		
		//System.out.println("query OC: " + query);
		
		PreparedStatement prst = DB.prepareStatement(query,null);
		ResultSet rs = null;
		
		ArrayList<BigDecimal> resultados = new ArrayList<BigDecimal>();
		try {
			rs = prst.executeQuery();
			while (rs.next()){
				resultados.add(rs.getBigDecimal("dias"));
			}
		} catch (SQLException e){
			System.out.println("Error al calcular el numero de dias de chequeo por número orden de compra");
			e.printStackTrace();
		} finally{
			try { rs.close(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			}
			try { prst.close(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			}
		}
		//System.out.println("res: "+resultados);
		impresionHoras(resultados, OCRESULTS);
		
	}
	
	private void calculoPedido() {
		
		// QUERY
		String query = "", filtro = "";
		
		//ADialog.info(1, new Container(), "XX_DaysPerPuchaseOrder");
		
		// Filtro: Número de pedido, fechas, colección, paquete o distribución
		if (!numPedido.getText().equals("")) {
			filtro += " AND xx_orderbecocorrelative=" + numPedido.getText().trim() ;
		}
		if (dateFrom.getValue() !=null & dateTo.getValue()!=null) {
			String dateF = DB.TO_DATE((Timestamp) dateFrom.getValue(),true); 
			String dateT = DB.TO_DATE((Timestamp) dateTo.getValue(),true);
			filtro += " AND o.created between " + dateF + " and " + dateT +"+1"; 
		}
		if (comboDist.getSelectedIndex()!=0) { // (?)
			filtro += " AND XX_VMR_DISTRIBUTIONHEADER_ID = " + ((KeyNamePair)comboDist.getSelectedItem()).getKey() +" ";
		}
		
		
		query = "\n WITH pTI as (select xx_vmr_order_id, sum(XX_ProductQuantity) qty, XX_DATESTATUSPENDING, XX_DATESTATUSTRANSIT, XX_DATESTATUSONSTORE " +
				"\n		FROM xx_vmr_order o join XX_VMR_OrderRequestDetail using (xx_vmr_order_id)" +
				"\n		WHERE XX_ORDERREQUESTSTATUS = 'TI' " + filtro ;
		query += "\n		GROUP BY xx_vmr_order_id, XX_DATESTATUSPENDING, XX_DATESTATUSTRANSIT, XX_DATESTATUSONSTORE)," +
				"\n	pTIET as (SELECT xx_vmr_order_id, sum(XX_ProductQuantity) qty, XX_DATESTATUSATBAY, XX_DATESTATUSTRANSIT" +
				"\n		FROM xx_vmr_order o join XX_VMR_OrderRequestDetail using (xx_vmr_order_id) " +
				"\n		WHERE XX_ORDERREQUESTSTATUS in ('ET', 'TI') " + filtro ;
		query += "\n		GROUP BY xx_vmr_order_id, XX_DATESTATUSATBAY, XX_DATESTATUSTRANSIT)," +
				"\n	pTIETEB as (SELECT xx_vmr_order_id, sum(XX_ProductQuantity) qty, XX_DATESTATUSPENDING, XX_DATESTATUSATBAY " +
				"\n		FROM xx_vmr_order o join XX_VMR_OrderRequestDetail using (xx_vmr_order_id) " +
				"\n		WHERE XX_ORDERREQUESTSTATUS in ('PE', 'EB', 'ET', 'TI') " + filtro ;
		query += "\n		GROUP BY xx_vmr_order_id, XX_DATESTATUSPENDING, XX_DATESTATUSATBAY) " +
				"\n SELECT 'aPE' tipo, coalesce(round(sum(qty*(XX_DATESTATUSATBAY-XX_DATESTATUSPENDING))/sum(qty),3),0) dias from pTIETEB " +
				"\n UNION ALL" +
				"\n SELECT 'bEE' tipo, coalesce(round(sum(qty*(XX_DATESTATUSTRANSIT-XX_DATESTATUSATBAY))/sum(qty),3),0) dias from PTIET " +
				"\n UNION ALL" +
				"\n SELECT 'cET' tipo, coalesce(round(sum(qty*(XX_DATESTATUSONSTORE-XX_DATESTATUSTRANSIT))/sum(qty),3),0) dias from pTI " +
				"\n UNION ALL" +
				"\n SELECT 'dPT' tipo, coalesce(round(sum(qty*(XX_DATESTATUSONSTORE-XX_DATESTATUSPENDING))/sum(qty),3),0) dias from pTI ";

		//System.out.println("query Pedido: " + query);
		
		PreparedStatement prst = DB.prepareStatement(query,null);
		ResultSet rs = null;
		
		ArrayList<BigDecimal> resultados = new ArrayList<BigDecimal>();
		try {
			rs = prst.executeQuery();
			while (rs.next()){
				resultados.add(rs.getBigDecimal("dias"));
			}
		} catch (SQLException e){
			System.out.println("Error al calcular el numero de dias de chequeo por pedido");
			e.printStackTrace();
		} finally{
			try { rs.close(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			}
			try { prst.close(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			}
		}
		impresionHoras(resultados, PEDRESULTS);
		
	}
	
	
	
	private void impresionHoras(ArrayList<BigDecimal> resultados, int n) {
		
		// Conversión a horas
		int dias = 0;
		int horas = 0;
		int minutos = 0;
		BigDecimal res = new BigDecimal(0);

		for (int i = 0; i < n; i++) {
					
			res = resultados.get(i);
			
			dias = res.intValue();
			horas = ((res.subtract(new BigDecimal(dias))).multiply(new BigDecimal(24))).intValue();
			minutos = ((((res.subtract(new BigDecimal(dias))).multiply(new BigDecimal(24))).subtract(new BigDecimal(horas))).multiply(new BigDecimal(60))).intValue();
			
			String horasString = "";
			String minutosString = "";
			
			if (horas<10)
				horasString = "0" + horas;
			else 
				horasString = "" + horas;
			if (minutos<10)
				minutosString = "0" + minutos;
			else 
				minutosString = "" + minutos;
			
			//System.out.println(dias+":"+horasString+":"+minutosString);

			JFormattedTextField temp = new JFormattedTextField();
			temp.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			temp.setBackground(Color.LIGHT_GRAY);
			temp.setEditable(false);
			temp.setPreferredSize(new Dimension(70,20));
			temp.setHorizontalAlignment(JTextField.CENTER);
			temp.setValue(dias+":"+horasString+":"+minutosString);
			//resultFields.add(temp);
			addLabel(temp, i+1, 1, 1);
		}	
		
		
		
		
	}
	
	/* Permite agregar un campo a la panel de resultados en la fila disponible
	 * especificando cuantas columnas ocupará 
	 * */
	
	private void addLabel(Component component, int row, int col, int horSpace) {
		
		/* Asignar la orientación dependiendo de la posicion*/
		int orientation = 0, orientation2 = 0 ;
		
		if (col % FIELDSPERGROUP == 1) {
			orientation = GridBagConstraints.WEST;
			orientation2 = GridBagConstraints.HORIZONTAL;
		} else {
			orientation = GridBagConstraints.EAST;
			orientation2 = GridBagConstraints.BOTH;
		}
	
		//Agregar al panel de parametros
		resultPanel.add(component,  
				new GridBagConstraints(
						col, row, horSpace, 1, 0.0, 0.0,
						orientation, orientation2, new Insets(5, 5, 5, 5), 0, 0));
	//	displayParameter += horSpace;
	}
	
		

	/** Activa todos los listeners creados */
	protected final void addActionListeners () {
		checkOC.addActionListener(this);
		checkPedido.addActionListener(this);
		purchaseSearch.addActionListener(this);
		dateFromOC.addActionListener(this);
		dateToOC.addActionListener(this);

		numPedido.addActionListener(this);
		dateFrom.addActionListener(this);
		dateTo.addActionListener(this);
		comboCollec.addActionListener(this);
		comboPaquete.addActionListener(this);
		comboDist.addActionListener(this);

		bReset.addActionListener(this);
		bSearch.addActionListener(this);
	} //addActionListeners
	
	/** Deshabilitar Action Listeners */
	protected final void removeActionListeners () {
		checkOC.removeActionListener(this);
		checkPedido.removeActionListener(this);
		purchaseSearch.removeActionListener(this);
		dateFromOC.removeActionListener(this);
		dateToOC.removeActionListener(this);

		numPedido.removeActionListener(this);
		dateFrom.removeActionListener(this);
		dateTo.removeActionListener(this);
		comboCollec.removeActionListener(this);
		comboPaquete.removeActionListener(this);
		comboDist.removeActionListener(this);

		bReset.removeActionListener(this);
		bSearch.removeActionListener(this);
	} // removeActionListeners



}
