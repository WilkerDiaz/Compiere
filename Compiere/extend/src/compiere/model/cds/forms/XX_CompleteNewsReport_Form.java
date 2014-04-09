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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MClient;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MRole;
import org.compiere.model.MUOMConversion;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_M_InOut;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.DocumentEngine;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MInOut;
import compiere.model.cds.MOrder;
import compiere.model.cds.MOrderLine;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.MVMRPOLineRefProv;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_OrderType;
import compiere.model.cds.X_Ref_XX_Ref_TypeDelivery;
import compiere.model.cds.X_XX_VLO_NewsReport;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_PO_LineRefProv;
import compiere.model.cds.X_XX_VMR_ReferenceMatrix;
import compiere.model.cds.X_XX_VMR_UnitConversion;

/**
 *  News Report (Complete It)
 *
 *  @author     José Trías
 *  @version    
 */
public class XX_CompleteNewsReport_Form extends CPanel

	implements FormPanel, ActionListener, TableModelListener, ListSelectionListener
	{
	/**
	 * 
	 */
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
		
		reportID = new Integer(Env.getCtx().get_ValueAsString("#XX_COMPLNEWSREPORT_ID"));
		Env.getCtx().remove("#XX_COMPLNEWSREPORT_ID");
	
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
	static CLogger 			log = CLogger.getCLogger(XX_CompleteNewsReport_Form.class);
	
	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	
	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	
	static int PU=0;
	static int PS=0;
	private Integer reportID = 0;
	private X_XX_VLO_NewsReport newsReport = null;
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xScrollPane = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder("");
	private MiniTable xTable = new MiniTable();
	private CPanel xPanel = new CPanel();
	private static int tableInit_option;
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private CButton bSave = new CButton();
	private CButton next = new CButton("Siguiente");
	private CButton back = new CButton("Regresar");
	private int oldOrderID = 0;
	private int lines = 0;
	
	// North panel
	private CLabel depart_Label = new CLabel("Departamento");
	private CLabel season_Label = new CLabel("Temporada");
	private CLabel collection_Label = new CLabel("Colección");
	private CLabel brochure_Label = new CLabel("Folleto");
	private CLabel vendor_Label = new CLabel("Proveedor");
	private CLabel pack_Label = new CLabel("Paquete");
	private CComboBox season = new CComboBox();
	private CComboBox collection = new CComboBox();
	private CComboBox brochure = new CComboBox();
	private CComboBox pack = new CComboBox();
	private CTextField vendor = new CTextField();
	private CTextField depart = new CTextField();
	int globalVendorID = 0;
	MVMRDepartment department = null;
	Vector<String> emailRoles = null;
	
	
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
		northPanel.setVisible(false);
		
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		centerPanel.setLayout(centerLayout);
		southPanel.setLayout(southLayout);
		
		xPanel.setLayout(xLayout);
		
		bSave.setText(Msg.translate(Env.getCtx(), "CreateOrder"));
		//bSave.setPreferredSize(new Dimension(100,22));	
		bSave.setEnabled(false);
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(950, 300));
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);

		Dimension dim = new Dimension( 170, 20);
		vendor.setPreferredSize( dim);
		vendor.setEnabled(false);
		
		depart.setPreferredSize(dim);
		depart.setEnabled(false);
		
		collection.setBackground(true);;
		season.setBackground(true);
		pack.setBackground(true);
		
		northPanel.add(vendor_Label,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(50, 10, 0, 0), 0, 0));
		
		northPanel.add(vendor,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(50, 10, 0, 0), 0, 0));
		
		northPanel.add(depart_Label,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		northPanel.add(depart,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		northPanel.add(season_Label,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		northPanel.add(season,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		northPanel.add(collection_Label,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		northPanel.add(collection,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		northPanel.add(brochure_Label,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		northPanel.add(brochure,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		northPanel.add(pack_Label,   new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		northPanel.add(pack,   new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
	
		xScrollPane.getViewport().add(xTable, null);
		
		
		southPanel.add(back,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 10, 10), 0, 0));
		
		southPanel.add(bSave,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 10, 10), 0, 0));
		
		southPanel.add(next,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 0), 0, 0));
		 
	    southPanel.validate();
	
	}   //  jbInit
	
	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		//Carga de combos
		loadSeasons();
		
		globalVendorID = getVendorID();
		
		collection.setEditable(false);
		collection.setEnabled(false);
		brochure.setEditable(false);
		brochure.setEnabled(false);
		pack.setEditable(false);
		pack.setEnabled(false);
		
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "Select"),   ".", Boolean.class, false, false, ""), 
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"),   ".", KeyNamePair.class), 
				new ColumnInfo(Msg.translate(Env.getCtx(), "Name"),   ".", KeyNamePair.class), 
				new ColumnInfo(Msg.translate(Env.getCtx(), "Prec. Venta"),   ".",  BigDecimal.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "% Impuesto"),   ".",  BigDecimal.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Prec. Venta con Imp."),   ".",  BigDecimal.class, false, false, ""),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Quantity"),   ".",  Integer.class), 
				new ColumnInfo(Msg.translate(Env.getCtx(), "NewsReport"),  ".",  KeyNamePair.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "OC"),   ".",  Integer.class)
		};
	
		xTable.prepareTable(layout, "", "", false, "");
		xTable.getColumnModel().getColumn(0).setMaxWidth(70);
		xTable.getColumnModel().getColumn(1).setMaxWidth(150);
		xTable.getColumnModel().getColumn(3).setMaxWidth(100);
		xTable.getColumnModel().getColumn(4).setMaxWidth(100);
		xTable.getColumnModel().getColumn(5).setMaxWidth(180);
		xTable.getColumnModel().getColumn(6).setMaxWidth(100);
		xTable.getColumnModel().getColumn(7).setMaxWidth(100);
		xTable.getColumnModel().getColumn(8).setMaxWidth(100);
	
		xTable.setAutoResizeMode(3);
		
		xTable.setSortEnabled(false);

		//  Visual
		CompiereColor.setBackground (this);
			
		newsReport = new X_XX_VLO_NewsReport( Env.getCtx(), reportID, null);
		oldOrderID = newsReport.getC_Order_ID();
		MProduct product = new MProduct( Env.getCtx(), newsReport.getM_Product_ID(), null);
		department = new MVMRDepartment( Env.getCtx(), product.getXX_VMR_Department_ID(), null);
		depart.setText(department.getValue()+" - "+department.getName());
		back.setVisible(false);
		bSave.setVisible(false);
		
		//Cargo la tabla 1
		tableInit_option=0;
		tableInit(newsReport);
		tableLoad (xTable);
		
		bSave.addActionListener(this);
		next.addActionListener(this);
		back.addActionListener(this);
		
		xTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				if(!xTable.isEditing()){
					becoPrice();
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
				statusBar.setStatusDB(0);
				int count = 0;
				for( int i=0; i < xTable.getRowCount(); i++){
					
					if((Boolean)xTable.getValueAt( i, 0)){
						count = count + 1;
					}
				}
				
				statusBar.setStatusDB(count);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				statusBar.setStatusDB(0);
				int count = 0;
				for( int i=0; i < xTable.getRowCount(); i++){
					
					if((Boolean)xTable.getValueAt( i, 0)){
						count = count + 1;
					}
				}
				
				statusBar.setStatusDB(count);
			}
		});
		
		
		xTable.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				if(!xTable.isEditing() && xTable.getRowCount()>0){
					becoPrice();	
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {		
			}
			
		});
		
		
		//Acciones para el Combo temporada
		season.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {
			
			@Override
			public void intervalRemoved(ListDataEvent e) {

			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
	
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {

				if(season.getSelectedIndex()>0){
					season.setBackground(false);
					loadCollections();
					collection.setEditable(true);
					collection.setEnabled(true);
					brochure.removeAllItems();
					brochure.setEditable(false);
					brochure.setEnabled(false);
					pack.removeAllItems();
					pack.setEditable(false);
					pack.setEnabled(false);
					collection.setBackground(true);
				}
				else{
					season.setBackground(true);
					collection.removeAllItems();
					collection.setEditable(false);
					collection.setEnabled(false);
					brochure.removeAllItems();
					brochure.setEditable(false);
					brochure.setEnabled(false);
					pack.removeAllItems();
					pack.setEditable(false);
					pack.setEnabled(false);
				}	
			}
		});
		
		//Acciones para el Combo colecion
		collection.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {
			
			@Override
			public void intervalRemoved(ListDataEvent e) {

			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
	
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {

				if(collection.getSelectedIndex()>0){
					collection.setBackground(false);
					loadBrochure();
					loadPacks();
					brochure.setEditable(true);
					brochure.setEnabled(true);
					pack.setEditable(true);
					pack.setEnabled(true);
					pack.setBackground(true);
				}
				else{
					collection.setBackground(true);
					brochure.removeAllItems();
					brochure.setEditable(false);
					brochure.setEnabled(false);
					pack.removeAllItems();
					pack.setEditable(false);
					pack.setEnabled(false);
				}	
			}
		});
		
		//Acciones para el Combo pack
		pack.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {
			
			@Override
			public void intervalRemoved(ListDataEvent e) {

			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
	
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {

				if(pack.getSelectedIndex()>0){
					
					pack.setBackground(false);
				}
				else{
					pack.setBackground(true);
				}	
			}
		});
		
		//Acciones para el combo paquete
		pack.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {
			
			@Override
			public void intervalRemoved(ListDataEvent e) {

			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
	
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {

				if(pack.getSelectedIndex()>0){
					bSave.setEnabled(true);
				}
				else{
					bSave.setEnabled(false);
				}	
			}
		});
		
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "ProductQty"));
		statusBar.setStatusDB(1);
		
	}   //  dynInit
	
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
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == bSave)
			cmd_Save();
		else if (e.getSource() == next)
			cmd_Next();
		else if (e.getSource() == back)
			cmd_Back();
	
	}   //  actionPerformed
	
	/**
	 *  Save Button Pressed
	 */
	private void cmd_Save()
	{
		if(xTable.isEditing()){
			xTable.getCellEditor().stopCellEditing();
		}
		
		MOrder newOrder = null;
		boolean completeIt = false;
		
		Vector<Vector <Integer>> megaVector = new Vector<Vector <Integer>>();
		KeyNamePair prod = null;
		Vector<Integer> product = null;
		Vector<String> productValue = new Vector<String>();
		Vector<String> refDescByProduct = new Vector<String>();
		KeyNamePair referenceCheck = null;
		Integer referenceID = 0;
		BigDecimal salePrice = BigDecimal.ZERO;
		BigDecimal pricePlusTax = BigDecimal.ZERO;
		int row=0;
		
		for( int i=0; i < xTable.getRowCount(); i++){
			
			referenceCheck = (KeyNamePair) xTable.getValueAt(i, 2);
			
			//Si las referencias son iguales agrego los productos al vector
			if(completeIt && (Boolean)xTable.getValueAt( i, 0) && referenceID==referenceCheck.getKey()){
				prod = (KeyNamePair) xTable.getValueAt(i, 1);
				product = new Vector<Integer>();
				product.add(prod.getKey());
				product.add(new Integer(xTable.getValueAt(i, 6).toString()));
				megaVector.add(product);
				productValue.add(prod.getName());
			}
			else if(completeIt && (Boolean)xTable.getValueAt( i, 0)){
				
				salePrice = (BigDecimal) xTable.getValueAt(i-1, 3);
				pricePlusTax = (BigDecimal) xTable.getValueAt(i-1, 5);
				
				row ++;
				createLine( newOrder, megaVector, salePrice, pricePlusTax, row);
				megaVector.removeAllElements();
				prod = (KeyNamePair) xTable.getValueAt(i, 1);
				product = new Vector<Integer>();
				product.add(prod.getKey());
				product.add(new Integer(xTable.getValueAt(i, 6).toString()));
				megaVector.add(product);
				productValue.add(prod.getName());
				referenceID = referenceCheck.getKey();
			}
			
			if((Boolean)xTable.getValueAt( i, 0) && !completeIt){
				completeIt = true;
				newOrder = createOrder(newOrder);
				prod = (KeyNamePair) xTable.getValueAt(i, 1);
				referenceID = referenceCheck.getKey();
				product = new Vector<Integer>();
				product.add(prod.getKey());
				product.add(new Integer(xTable.getValueAt(i, 6).toString()));
				megaVector.add(product);
				productValue.add(prod.getName());
			}
			
			if(i==xTable.getRowCount()-1){
				
				salePrice = (BigDecimal) xTable.getValueAt(i, 3);
				pricePlusTax = (BigDecimal) xTable.getValueAt(i, 5);
				row ++;
				createLine( newOrder, megaVector, salePrice, pricePlusTax, row);
			}

		}
		
		if(completeIt){ //Si se puede completar
		
			createOrderLines(newOrder);
			
			if(lines==0){
				ADialog.info(m_WindowNo, this.mainPanel, "No se han podido crear las lineas de la O/C (revisar los productos)");
				return;
			}
						
			newOrder.setDocAction(X_C_Order.DOCACTION_Complete);
		    DocumentEngine.processIt(newOrder, X_C_Order.DOCACTION_Complete);
			
			newOrder.setXX_OrderStatus("CH");
			newOrder.save();
			
			Trx trans = Trx.get("Transaccion");
			
			createMInOut(newOrder, trans);
			
			X_XX_VLO_NewsReport report = null;
			KeyNamePair pair = null;
			String refDescReport = "";
			for( int i=0; i < xTable.getRowCount(); i++){
				
				if((Boolean)xTable.getValueAt( i, 0)){
				
					pair = (KeyNamePair)xTable.getValueAt( i, 7);
					report = new X_XX_VLO_NewsReport( Env.getCtx(), pair.getKey(), null);
					report.setXX_SalePrice( new BigDecimal(xTable.getValueAt( i, 3).toString()));
					report.setXX_NewOrder_ID(newOrder.get_ID());
					report.save();
					
					refDescReport = "";
					
					if(report.getXX_VendorReference()!=null)
						refDescReport = report.getXX_VendorReference();
					if(report.getDescription()!=null)
						refDescReport += " " + report.getDescription();
					
					refDescByProduct.add(refDescReport);
				}
			}
			
			//Enviar correo Indicando O/C y productos
			
			MOrder oldOrder = new MOrder( Env.getCtx(), oldOrderID, null);
			
			sendMail( oldOrder.getDocumentNo(), oldOrder.getXX_VMR_DEPARTMENT_ID(), newOrder.getDocumentNo(), newOrder.getXX_VMR_DEPARTMENT_ID(), productValue, refDescByProduct);
			trans.commit();
			
			dispose();
		}
		
	}   //  cmd_Save
	
	/**
	 *  next Button Pressed
	 */
	private void cmd_Next()
	{
		if(xTable.isEditing()){
			xTable.getCellEditor().stopCellEditing();
		}
		
		becoPrice();
		
		Vector<Object> products = new Vector<Object>();
		
		for( int i=0; i < xTable.getRowCount(); i++){
			
			if(products.contains(xTable.getValueAt(i, 1))){
				
				ADialog.info(m_WindowNo, this.mainPanel, "Existen productos duplicados en la lista (deben tener códigos diferentes) ("+xTable.getValueAt(i, 1)+")");
				return;
			}
			else
				products.add(xTable.getValueAt( i, 1));
			
			if(!(Boolean)xTable.getValueAt( i, 0)){
				return;
			}
		}
	
		centerPanel.setVisible(false);
		northPanel.setVisible(true);
		back.setVisible(true);
		bSave.setVisible(true);
		next.setVisible(false);
		
		statusBar.setStatusDB(0);
		int count = 0;
		for( int i=0; i < xTable.getRowCount(); i++){
			
			if((Boolean)xTable.getValueAt( i, 0)){
				count = count + 1;
			}
		}
		
		statusBar.setStatusDB(count);
	}
	
	/**
	 *  Back Button Pressed
	 */
	private void cmd_Back()
	{
		centerPanel.setVisible(true);
		northPanel.setVisible(false);
		back.setVisible(false);
		bSave.setVisible(false);
		next.setVisible(true);
	}
	
	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{		
		if (e.getValueIsAdjusting())
			return;
	
	}   //  valueChanged
	
	/**************************************************************************
	 *  Initialize Table access - create SQL, dateColumn.
	 */
	private static void tableInit (X_XX_VLO_NewsReport report)
	{	
		m_sql = new StringBuffer ();
	
		if(tableInit_option==0)
		{
			m_sql.append(
					"SELECT 'Y', " +
					"pro.Value, pro.M_Product_ID, vpr.value||' - '||pro.name, vpr.XX_VMR_VendorProdRef_ID, 0," +
					"(SELECT rate FROM C_Tax WHERE ValidFrom = " +		
					"(SELECT MAX(ValidFrom) FROM C_Tax WHERE C_TaxCategory_ID=pro.C_TaxCategory_ID))IVA," +
					"0, nr.XX_Quantity, nr.documentNo, nr.XX_VLO_Newsreport_ID, ord.documentNo " +
					"FROM xx_vlo_newsreport nr, m_product pro_Act, m_product pro, c_order ord, XX_VMR_VendorProdRef vpr " +
					"WHERE XX_STATUS = 'AO' and XX_NOSENCAMER = 'N' and nr.M_Product_id is not null " +
					"and pro.m_product_id = nr. m_product_id " +
					"and ord.c_order_id = nr.c_order_id " +
					"and nr.XX_NewOrder_ID is null " +
					"and pro.xx_vmr_department_id = pro_Act.xx_vmr_department_id " +
					"and vpr.XX_VMR_VendorProdRef_ID = pro.XX_VMR_VendorProdRef_ID " +
					"and pro_Act.m_product_id = " + report.getM_Product_ID() + " " +
					"and nr.c_order_id = " + report.getC_Order_ID()
			);
			
			m_orderBy = " order by XX_VMR_VENDORPRODREF_ID";
		}

	}   //  tableInit
	
	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private static void tableLoad (MiniTable table)
	{
	//	log.finest(m_sql + " - " +  m_groupBy);
		String sql = MRole.getDefault().addAccessSQL(
			m_sql.toString(), "nr", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
			+ m_groupBy + m_orderBy;
		
		log.finest(sql);
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
	
			table.loadTable(rs);
	
			stmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}   //  tableLoad
	
	
	@Override
	public void tableChanged(TableModelEvent e) {
			
	}
	
	/**
	 * Carga del combobox de las temporadas
	 */
	private void loadSeasons(){
		
		season.removeAllItems();
		
		String sql = "SELECT XX_VMA_SEASON_ID, NAME FROM XX_VMA_SEASON WHERE ISACTIVE = 'Y' ";
		
		sql += " order by value";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			KeyNamePair loadKNP=new KeyNamePair(0, new String());
			season.addItem(loadKNP);
			
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1),rs.getString(2));
				season.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	/**
	 * Carga del combobox de las colecciones
	 */
	private void loadCollections(){
		
		collection.removeAllItems();
		
		String sql = "SELECT XX_VMR_COLLECTION_ID, NAME FROM XX_VMR_COLLECTION WHERE ISACTIVE = 'Y' ";
		
		if(season.getSelectedIndex()>0){
			int clave =((KeyNamePair)season.getSelectedItem()).getKey();
			sql += " AND XX_VMA_SEASON_ID=" + clave;
		}
		
		sql += " order by value";
			
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			KeyNamePair loadKNP=new KeyNamePair(0, new String());
			collection.addItem(loadKNP);
			
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1),rs.getString(2));
				collection.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	/**
	 * Carga del combobox de los folletos
	 */
	private void loadBrochure(){
		
		brochure.removeAllItems();
		
		int clave =((KeyNamePair)collection.getSelectedItem()).getKey();
	
		String sql = "SELECT XX_VMA_BROCHURE_ID, NAME FROM XX_VMA_BROCHURE WHERE XX_VMA_BROCHURE_ID IN ( " +
				"Select XX_VMA_BROCHURE_ID " +
				"From XX_VMA_MARKETINGACTIVITY " +
				"Where ISACTIVE = 'Y' AND C_CAMPAIGN_ID IN " +
				"( " +
				"Select C_CAMPAIGN_ID " +
				"From XX_VMA_COLLECTION_V " +
				"Where XX_VMR_COLLECTION_ID = " + clave +
				")" +
		")";
		
		sql += " order by Name";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			KeyNamePair loadKNP=new KeyNamePair(0, new String());
			brochure.addItem(loadKNP);
			
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1),rs.getString(2));
				brochure.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	/**
	 * Carga del combobox de los paquetes
	 */
	private void loadPacks(){
		
		pack.removeAllItems();
		
		int clave =((KeyNamePair)collection.getSelectedItem()).getKey();
	
		String sql = "SELECT XX_VMR_PACKAGE_ID, NAME FROM XX_VMR_PACKAGE WHERE ISACTIVE='Y' AND XX_VMR_COLLECTION_ID = " + clave;
		
		sql += " ORDER BY NAME";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			KeyNamePair loadKNP=new KeyNamePair(0, new String());
			pack.addItem(loadKNP);
			
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1),rs.getString(2));
				pack.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	/**
	 * get de Global Vendor ID
	 */
	private int getVendorID(){
		
		int vendorID = 0;
		String name = "";
	
		String sql = "SELECT C_BPARTNER_ID,NAME FROM C_BPARTNER WHERE ISACTIVE='Y' AND NAME LIKE '%SAN PANTALEON%'";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				vendorID = rs.getInt(1);
				name = rs.getString(2);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		vendor.setText(name);
		
		return vendorID;
	}
		
	private MOrder createOrder(MOrder n_Order){
				
		n_Order = new MOrder( Env.getCtx(), 0, null);
		MOrder oldOrder = new MOrder( Env.getCtx(), oldOrderID, null);
		MBPartner n_Vendor = new MBPartner( Env.getCtx(), globalVendorID, null);
		
		Calendar today = Calendar.getInstance();

		n_Order.setXX_CheckupDate(new Timestamp(today.getTimeInMillis()));
		n_Order.setXX_ReceptionDate(new Timestamp(today.getTimeInMillis()));
		n_Order.setXX_EntranceDate(new Timestamp(today.getTimeInMillis()));
		n_Order.setXX_ArrivalDate(new Timestamp(today.getTimeInMillis())); 
		n_Order.setXX_EstimatedDate(new Timestamp(today.getTimeInMillis()));
		n_Order.setXX_ComesFromSITME(false);
		n_Order.setDocStatus("DR");
		n_Order.setDocAction("CO");
		n_Order.setXX_POType("POM");
		n_Order.setIsSOTrx(false);
		n_Order.setProcessed(false); 
		n_Order.setProcessing(false);
		n_Order.setXX_Void(false); 
		n_Order.setXX_ComesFromCopy(false);
		n_Order.setRef_Order_ID(oldOrder.get_ID());
		n_Order.setXX_Alert10("Reporte de Novedad (O/C origen: " + oldOrder.getDocumentNo() + ")");
		n_Order.setXX_PurchaseOrderComments("Reporte de Novedad");
		n_Order.setC_Country_ID(339); //Venezuela
		n_Order.setC_Currency_ID(205); //Bs.F
		n_Order.setXX_OrderType(X_Ref_XX_OrderType.NACIONAL.getValue());
		n_Order.setC_BPartner_ID(globalVendorID);
		n_Order.setAD_Org_ID(Env.getCtx().getContextAsInt("#XX_L_ORGANIZATIONCD_ID"));
		n_Order.setM_Warehouse_ID(oldOrder.getM_Warehouse_ID());
		n_Order.setC_DocTypeTarget_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEORDER_ID"));
		n_Order.setCopyFrom("N"); 
		n_Order.setVolume(BigDecimal.ZERO); 
		n_Order.setWeight(BigDecimal.ZERO);
		n_Order.setXX_Department(department.get_ID());
		n_Order.setXX_Discount1(BigDecimal.ZERO);
		n_Order.setXX_Discount2(BigDecimal.ZERO); 
		n_Order.setXX_Discount3(BigDecimal.ZERO); 
		n_Order.setXX_Discount4(BigDecimal.ZERO); 
		n_Order.setXX_MontLimit(BigDecimal.ZERO); 
		n_Order.setXX_ProductQuantity(new BigDecimal(0));
		n_Order.setTotalPVP(BigDecimal.ZERO); 
		n_Order.setXX_TotalPVPPlusTax(BigDecimal.ZERO); 
		n_Order.setXX_EstimatedFactor(BigDecimal.ZERO);
		n_Order.setXX_DefinitiveFactor(BigDecimal.ZERO);
		n_Order.setXX_ReplacementFactor(BigDecimal.ZERO);
		n_Order.setXX_Annul('N');
		n_Order.setXX_Copy("N");
		n_Order.setXX_VLO_TypeDelivery(X_Ref_XX_Ref_TypeDelivery.EN_CENTRO_DE_DISTRIBUCIÓN.getValue());
		n_Order.setXX_CustomAgentAmountPro(BigDecimal.ZERO);
		n_Order.setXX_NacInvoiceAmountPro(BigDecimal.ZERO);
		n_Order.setXX_RUSAD(0);
		n_Order.setXX_AAD(0);
		n_Order.setXX_INTNACESTMEDAMOUNT(BigDecimal.ZERO);
		n_Order.setXX_ShowMotiveChange("N");
		n_Order.setXX_TotalVendInv(BigDecimal.ZERO);
		n_Order.setXX_OrderReadyDate(new Timestamp(today.getTimeInMillis()));
		n_Order.setXX_OrderNotReady("N");
		n_Order.setXX_Season_ID(((KeyNamePair)season.getValue()).getKey());
		n_Order.setXX_Collection_ID(((KeyNamePair)collection.getValue()).getKey());
		n_Order.setXX_Brochure_ID(((KeyNamePair)brochure.getValue()).getKey());
		n_Order.setXX_VMR_Package_ID(((KeyNamePair)pack.getValue()).getKey());
		n_Order.setXX_VMR_Subject_ID(Env.getCtx().getContextAsInt("#XX_L_PO_SUBJ_SURTREG_ID"));
		n_Order.set_CustomColumn("XX_TOTALCOSTBS", 0);
		n_Order.setXX_Category_ID(department.getXX_VMR_Category_ID());
		n_Order.setXX_UserBuyer_ID(department.getXX_UserBuyer_ID());
		n_Order.setXX_OrderReady("Y");
		n_Order.setXX_OrderReadyStatus(true);
		n_Order.setC_PaymentTerm_ID(n_Vendor.getC_PaymentTerm_ID());
		n_Order.setPaymentRule(n_Vendor.getPaymentRulePO());
			
		//Obtenemos el contacto de ventas del proveedor
		String SQL = "SELECT AD_USER_ID "+
			         "FROM AD_USER ADU "+
			         "WHERE XX_CONTACTTYPE = "+ Env.getCtx().getContext("#XX_L_CONTACTTYPESALES")+" "+
			         "AND C_BPARTNER_ID = "+ globalVendorID +" AND ROWNUM=1 " ;

		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();

			if(rs.next()) {
				n_Order.setAD_User_ID(rs.getInt("AD_USER_ID"));
			}

			rs.close();
			pstmt.close();
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		
		n_Order.save();
		
		//System.out.println(n_Order.getDocumentNo());
		
		return n_Order;
	}
	
	private void createLine(MOrder order, Vector<Vector<Integer>> products, BigDecimal salePrice, BigDecimal pricePlusTax, int row){
			
		BigDecimal noCost = new BigDecimal(0.01);
		
		Vector<Integer> firstProduct = products.get(0);
		MProduct product = new MProduct( Env.getCtx(), firstProduct.get(0), null);
		MVMRVendorProdRef reference = new MVMRVendorProdRef( Env.getCtx(), product.getXX_VMR_VendorProdRef_ID(), null);
		
		MVMRPOLineRefProv lineRef = new MVMRPOLineRefProv( Env.getCtx(), 0, null);
		lineRef.setC_Order_ID( order.get_ID());
		lineRef.setXX_UnitPurchasePrice(noCost); //Costo 0.01
		lineRef.setPriceActual(noCost);
		lineRef.setXX_CostWithDiscounts(noCost);
		lineRef.setXX_VMR_Brand_ID(product.getXX_VMR_Brand_ID());
		lineRef.setXX_VMR_Line_ID(product.getXX_VMR_Line_ID());
		lineRef.setXX_VMR_Section_ID(product.getXX_VMR_Section_ID());
		lineRef.setXX_VMR_VendorProdRef_ID(product.getXX_VMR_VendorProdRef_ID());
		lineRef.setLine(new BigDecimal(row+10));
		lineRef.setXX_VMR_LongCharacteristic_ID(product.getXX_VMR_LongCharacteristic_ID());
		lineRef.setXX_VMR_UnitConversion_ID(product.getXX_VMR_UnitConversion_ID());
		lineRef.setXX_VMR_UnitPurchase_ID(product.getXX_VMR_UnitPurchase_ID());
		lineRef.setXX_SaleUnit_ID(product.getXX_SaleUnit_ID());
		lineRef.setXX_PiecesBySale_ID(product.getXX_PiecesBySale_ID());
		lineRef.setXX_Rebate1(BigDecimal.ZERO);
		lineRef.setXX_ReferenceIsAssociated(true);
		lineRef.setXX_VME_ConceptValue_ID(product.getXX_VME_ConceptValue_ID());
		lineRef.setC_TaxCategory_ID(product.getC_TaxCategory_ID());
		lineRef.setXX_Margin(new BigDecimal(99.99));
		lineRef.setXX_ShowMatrix("Y");
		
		//Ojo pedir
		lineRef.setXX_PackageMultiple(1);
		lineRef.setXX_SalePrice(salePrice);
		lineRef.setXX_TaxAmount(pricePlusTax.subtract(salePrice));
		lineRef.setXX_SalePricePlusTax(pricePlusTax);
		
		//Calcula la cantidad vendida
		X_XX_VMR_UnitConversion PU = new X_XX_VMR_UnitConversion( Env.getCtx(), lineRef.getXX_VMR_UnitConversion_ID(), null);
		X_XX_VMR_UnitConversion PS = new X_XX_VMR_UnitConversion( Env.getCtx(), lineRef.getXX_PiecesBySale_ID(), null);
		
		int myQty = 0;
		//Calcula la cantidad total
		for(int w=0; w < products.size(); w++){
			 myQty = myQty + products.get(w).get(1);
		}
		
		lineRef.setQty(myQty);
		int CV=(myQty*PU.getXX_UnitConversion())/PS.getXX_UnitConversion();
		
		lineRef.setQty(myQty);
		lineRef.setXX_LineQty(myQty);
		lineRef.setSaleQty(CV);
		
		//Con caracteristicas
		if(reference.getM_AttributeSet_ID()!=0){
			
			lineRef.setXX_WithCharacteristic(true);
			
			String sql = "SELECT DISTINCT ai.M_ATTRIBUTE_ID, M_ATTRIBUTEVALUE_ID from M_PRODUCT pro, M_ATTRIBUTEINSTANCE ai " +
						 "WHERE " +
						 "ai.M_ATTRIBUTESETINSTANCE_ID = pro.M_ATTRIBUTESETINSTANCE_ID AND " +
						 "pro.M_PRODUCT_ID IN (";
						 
						 for(int j=0; j < products.size(); j++){
							 
							 if(j==0)
								 sql += products.get(j).get(0);
						     else
								 sql += "," + products.get(j).get(0);
						 }
						 
						 sql += ") " +
						 "ORDER BY ai.M_ATTRIBUTE_ID, M_ATTRIBUTEVALUE_ID";
			
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
			
				int firstAttribute = 0;
				int j=0;
				int c1 = 0;
				int c2 = 0;
				boolean ready = false;
				
				while (rs.next())
				{
					j++;
					if(j==1)
					{
						firstAttribute = rs.getInt("M_ATTRIBUTE_ID");
						lineRef.setXX_Characteristic1_ID(firstAttribute);
						lineRef.setXX_Characteristic1Value1_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						c1++;
					}
					
					if(j>1 && firstAttribute!=rs.getInt("M_ATTRIBUTE_ID") && !ready)
					{				
						c2++;
						
						if(c2==1){
							lineRef.setXX_Characteristic2_ID(rs.getInt("M_ATTRIBUTE_ID"));
							lineRef.setXX_Characteristic2Value1_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						}
						else if(c2==2)
							lineRef.setXX_Characteristic2Value2_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c2==3)
							lineRef.setXX_Characteristic2Value3_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c2==4)
							lineRef.setXX_Characteristic2Value4_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c2==5)
							lineRef.setXX_Characteristic2Value5_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c2==6)
							lineRef.setXX_Characteristic2Value6_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c2==7)
							lineRef.setXX_Characteristic2Value7_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c2==8)
							lineRef.setXX_Characteristic2Value8_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c2==9)
							lineRef.setXX_Characteristic2Value9_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c2==10){
							lineRef.setXX_Characteristic2Value10_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
							ready = true;
						}
					}
					else if(j!=1)
					{				
						c1++;
						
						if(c1==2)
							lineRef.setXX_Characteristic1Value2_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c1==3)
							lineRef.setXX_Characteristic1Value3_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c1==4)
							lineRef.setXX_Characteristic1Value4_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c1==5)
							lineRef.setXX_Characteristic1Value5_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c1==6)
							lineRef.setXX_Characteristic1Value6_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c1==7)
							lineRef.setXX_Characteristic1Value7_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c1==8)
							lineRef.setXX_Characteristic1Value8_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c1==9)
							lineRef.setXX_Characteristic1Value9_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						else if(c1==10)
							lineRef.setXX_Characteristic1Value10_ID(rs.getInt("M_ATTRIBUTEVALUE_ID"));
					}
				}
				
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			
			lineRef.save();
			
			Vector<Integer> columnsIDs = columnsVector(lineRef.get_ID());
			Vector<Integer> rowsIDs = rowsVector(lineRef.get_ID());
			createData( columnsIDs, rowsIDs, columnsIDs.size(), rowsIDs.size(), lineRef.get_ID());
			
			int CantV = 0;
			//Cantidad de cada combinacion
			for(int i=0; i<products.size(); i++){
				
				myQty = products.get(i).get(1);
				CantV=(myQty*PU.getXX_UnitConversion())/PS.getXX_UnitConversion();
				setQty( products.get(i).get(0), myQty, CantV, lineRef.get_ID());
			}
			
		}else{
		
			lineRef.setXX_WithCharacteristic(false);
			lineRef.save();
			
			X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix( Env.getCtx(), 0, null);
			matrix.setXX_VALUE1(0);
			matrix.setXX_VALUE2(0);
			matrix.setXX_COLUMN(0);
			matrix.setXX_ROW(0);
			matrix.setXX_QUANTITYC(myQty);
			matrix.setXX_QUANTITYV(CV);
			matrix.setXX_QUANTITYO(0);
			matrix.setM_Product(product.get_ID());
			matrix.setXX_VMR_PO_LineRefProv_ID(lineRef.get_ID());
			
			matrix.save();
		}	
	}
	
	private void createOrderLines(MOrder order){
		
	    String SQL = ("SELECT DISTINCT A.XX_VMR_PO_LINEREFPROV_ID AS IDLINE, B.C_BPARTNER_ID AS PROV, TO_NUMBER (TO_CHAR (B.DATEPROMISED, 'YYYYMMDD')) AS DATEPRO, TO_NUMBER (TO_CHAR (B.DATEORDERED, 'YYYYMMDD')) AS DATEOR, " +
	    		"C.M_PRODUCT AS PRODUCT, A.QTY AS CANTIDAD, A.XX_VMR_UNITPURCHASE_ID AS UOM, " +
	    		"A.XX_COSTWITHDISCOUNTS AS PRECIO, " +
	    		"( SELECT C_TAX_ID FROM C_Tax WHERE ValidFrom = (SELECT MAX(ValidFrom) FROM C_Tax WHERE C_TaxCategory_ID = A.C_TAXcategory_ID)) as TAX, C.XX_QUANTITYC AS CC " +
	    		"FROM XX_VMR_PO_LINEREFPROV A, C_ORDER B, XX_VMR_REFERENCEMATRIX C " +
	    		"WHERE A.C_ORDER_ID = B.C_ORDER_ID " +
	    		"AND C.XX_QUANTITYV <> 0 " +
	    		"AND C.XX_VMR_PO_LINEREFPROV_ID = A.XX_VMR_PO_LINEREFPROV_ID " +
	    		"AND C.M_PRODUCT IS NOT NULL " +
	    		"AND B.C_ORDER_ID = '"+order.get_ID()+"'");
	    
	    try{
		    
	    	PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();
	   
		    while(rs.next())
		    {
		    	lines++;
		    	MOrderLine orderLine = new MOrderLine( Env.getCtx(), 0, null);
		    	BigDecimal PriceEntered, PriceActual, LineAmount;
		    	BigDecimal priceListPO = new BigDecimal(1);
		    	
				PriceEntered = MUOMConversion.convertProductFrom( Env.getCtx(), rs.getInt("PRODUCT"), 100, rs.getBigDecimal("PRECIO"));
		    	orderLine.setPriceEntered(PriceEntered);
			    PriceActual = PriceEntered;
			       	log.fine("PriceEntered=" + PriceEntered 
							+ " -> PriceActual=" + PriceActual);
			    orderLine.setPriceActual(PriceActual);
			    LineAmount = rs.getBigDecimal("CANTIDAD").multiply(PriceActual); 
			    orderLine.setLineNetAmt(LineAmount);
		    	orderLine.setAD_Org_ID(order.getAD_Org_ID());
		    	orderLine.setC_Order_ID(order.get_ID());
		       	orderLine.setXX_VMR_PO_LineRefProv_ID(rs.getInt("IDLINE"));
		       	orderLine.setC_BPartner_ID(rs.getInt("PROV"));
		       	orderLine.setM_Product_ID(rs.getInt("PRODUCT"));
		       	orderLine.setQtyEntered(rs.getBigDecimal("CC"));
		       	orderLine.setC_UOM_ID(100);
		       	orderLine.setC_Tax_ID(rs.getInt("TAX"));
		       	orderLine.setPriceList(priceListPO);
		       	orderLine.save();
		    }
		    
		    rs.close();
		    pstmt.close();
	    
	    }catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		
	}

	private void becoPrice(){
	
		BigDecimal price = null;	
		BigDecimal beco = null;
		PreparedStatement priceRulePstmt = null;
		ResultSet priceRuleRs = null;
		Integer precioInt = null;
		BigDecimal precioBig = null;
		BigDecimal oneBig = new BigDecimal(1);
		BigDecimal hundredBig = new BigDecimal(100);
		Integer auxPrice = 0;
		BigDecimal terminacion = null;
		BigDecimal var = null;
		Boolean selected = false;
		BigDecimal taxAmount = null;
		
		for(int f=0; f<xTable.getRowCount(); f++){
		
			price = (BigDecimal)xTable.getValueAt( f, 5);
			selected = (Boolean)xTable.getValueAt( f, 0);
			taxAmount = BigDecimal.ZERO;
			beco = BigDecimal.ZERO;
			
			if(price.compareTo(BigDecimal.ZERO)>0 && selected){

				precioInt = 0;
				precioBig = BigDecimal.ZERO;
				terminacion = BigDecimal.ZERO;
				var = BigDecimal.ZERO;
				auxPrice = 0;
				
				String priceRuleSQL = "SELECT XX_Lowrank,XX_Highrank,XX_Termination,xx_increase,XX_infinitevalue " +
									  "FROM xx_vme_pricerule ORDER BY (xx_lowrank)";
				
				try{
					
					priceRulePstmt = DB.prepareStatement(priceRuleSQL, null);
					priceRuleRs = priceRulePstmt.executeQuery();
			
					precioInt = price.intValue();
					precioBig = new BigDecimal(precioInt);
					
					while(priceRuleRs.next())
					{	
						if(precioBig.compareTo(priceRuleRs.getBigDecimal("xx_lowrank"))>=0 && precioBig.compareTo(priceRuleRs.getBigDecimal("xx_highrank"))<=0) 
						{
							Integer incremento = priceRuleRs.getInt("xx_increase");
						    	  
						    for(auxPrice=priceRuleRs.getInt("xx_lowrank")-1;auxPrice<=priceRuleRs.getInt("xx_highrank");auxPrice=auxPrice+incremento)
						    {
						    	var = new BigDecimal(auxPrice);
						    		 
						    	if(precioBig.compareTo(var) <= 0)
						    	{
						    		beco = var;
						    			  
						    		terminacion = priceRuleRs.getBigDecimal("xx_termination");
						    		if(terminacion.intValue()==0)
						    		{
						    			beco = var.add(terminacion);
						    		}
						    		else
						    		{
						    			var = var.divide(new BigDecimal(10));
						    			Integer aux = var.intValue()*10;
						    			beco = new BigDecimal(aux).add(terminacion);
						    		}
						    			 
						    		priceRuleRs.close();
						 			priceRulePstmt.close();
						    	 }
							}
						}
					}
					priceRuleRs.close();
					priceRulePstmt.close();
				}
				catch (Exception e) {
					
				}
				
				taxAmount = (BigDecimal)xTable.getValueAt( f, 4);
				taxAmount = taxAmount.divide( hundredBig, 2, BigDecimal.ROUND_HALF_UP);
				taxAmount = taxAmount.add(oneBig);
				taxAmount = beco.divide(taxAmount, 2, BigDecimal.ROUND_HALF_UP);
				
				xTable.setValueAt( taxAmount, f, 3);
				xTable.setValueAt( beco, f, 5);
			}
		}
		
	}
	
	private String createData(Vector <Integer> vector, Vector <Integer> vector2, int tamanoX, int tamanoY, int lineRef){
		
		Trx trans = Trx.get("trans");
		
		if(tamanoY==0){
			tamanoY=1;
		}
		
		for (int i=0;i<tamanoX;i++){
			for (int j=0;j<tamanoY*3;j=j+3){
		
				X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix(Env.getCtx(),0,trans);
				
				matrix.setXX_VALUE1(vector.get(i));
				
				if(vector2.size()==0){
					matrix.setXX_VALUE2(0);
				}else{
					matrix.setXX_VALUE2(vector2.get(j/3));	
				}
				
				matrix.setXX_COLUMN(i);
				matrix.setXX_ROW(j);
				matrix.setXX_QUANTITYC(0);
				matrix.setXX_QUANTITYV(0);
				matrix.setXX_QUANTITYO(0);
				matrix.setXX_VMR_PO_LineRefProv_ID(lineRef);
				
				matrix.save();
			}
		}
		
		trans.commit();
		
		return "";
	}
	
	/*
	 * Vector que contiene los id de las columnas
	 */
	private Vector <Integer> columnsVector(Integer LineRefProv_ID){
		
		X_XX_VMR_PO_LineRefProv Line = new X_XX_VMR_PO_LineRefProv(Env.getCtx(),LineRefProv_ID, null);
		
		Vector <Integer> vector = new Vector <Integer>();
		
		if(Line.getXX_Characteristic1Value1_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value1_ID());
		}
		if(Line.getXX_Characteristic1Value2_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value2_ID());
		}
		if(Line.getXX_Characteristic1Value3_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value3_ID());
		}
		if(Line.getXX_Characteristic1Value4_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value4_ID());
		}
		if(Line.getXX_Characteristic1Value5_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value5_ID());
		}
		if(Line.getXX_Characteristic1Value6_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value6_ID());
		}
		if(Line.getXX_Characteristic1Value7_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value7_ID());
		}
		if(Line.getXX_Characteristic1Value8_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value8_ID());
		}
		if(Line.getXX_Characteristic1Value9_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value9_ID());
		}
		if(Line.getXX_Characteristic1Value10_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value10_ID());
		}
		
		return vector;
	}
	
	/*
	 * Vector que contiene los id de las filas
	 */
	private Vector <Integer> rowsVector(Integer LineRefProv_ID){
		
		X_XX_VMR_PO_LineRefProv Line = new X_XX_VMR_PO_LineRefProv(Env.getCtx(),LineRefProv_ID, null);
		
		Vector <Integer> vector = new Vector <Integer>();
		
		if(Line.getXX_Characteristic2Value1_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value1_ID());
		}
		if(Line.getXX_Characteristic2Value2_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value2_ID());
		}
		if(Line.getXX_Characteristic2Value3_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value3_ID());
		}
		if(Line.getXX_Characteristic2Value4_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value4_ID());
		}
		if(Line.getXX_Characteristic2Value5_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value5_ID());
		}
		if(Line.getXX_Characteristic2Value6_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value6_ID());
		}
		if(Line.getXX_Characteristic2Value7_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value7_ID());
		}
		if(Line.getXX_Characteristic2Value8_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value8_ID());
		}
		if(Line.getXX_Characteristic2Value9_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value9_ID());
		}
		if(Line.getXX_Characteristic2Value10_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value10_ID());
		}
		
		return vector;
	}
	
	private void setQty(int product, int qtyc, int qtyv, int line){
		
		String sql = "update XX_VMR_REFERENCEMATRIX set XX_QUANTITYC = "+ qtyc +", XX_QUANTITYV = "+ qtyv +", M_PRODUCT = "+product+" WHERE XX_VMR_REFERENCEMATRIX_ID = " +
					 "(select XX_VMR_REFERENCEMATRIX_ID " +
					 "FROM XX_VMR_ReferenceMatrix rm " +
					 "WHERE " +
					 "XX_VALUE1 IN ( " +
					 "SELECT M_ATTRIBUTEVALUE_ID from M_PRODUCT pro, M_ATTRIBUTEINSTANCE ai " +
					 "WHERE ai.M_ATTRIBUTESETINSTANCE_ID = pro.M_ATTRIBUTESETINSTANCE_ID AND " +
					 "pro.M_PRODUCT_ID = "+ product +") " +
					 "AND XX_VALUE2 IN ( " +
					 "SELECT M_ATTRIBUTEVALUE_ID from M_PRODUCT pro, M_ATTRIBUTEINSTANCE ai " +
					 "WHERE ai.M_ATTRIBUTESETINSTANCE_ID = pro.M_ATTRIBUTESETINSTANCE_ID AND " +
					 "pro.M_PRODUCT_ID = "+ product +") " +
					 "and rm.XX_VMR_PO_LineRefProv_ID = "+ line +")";
			
		DB.executeUpdate(null, sql);
	}
	
	public static boolean saveData(Ctx ctx, MOrder p_order, MInvoice m_invoice, int M_Locator_ID, int M_InOut_ID )
	{
		MInOut inout = new MInOut (ctx, M_InOut_ID, null);
	
		String SQL = "SELECT l.QtyOrdered-SUM(COALESCE(m.Qty,0)) as Qty, l.C_UOM_ID as UOM, COALESCE(l.M_Product_ID,0) as Product, l.C_OrderLine_ID as Line " +
				     "FROM C_OrderLine l LEFT OUTER JOIN M_MatchPO m ON (l.C_OrderLine_ID=m.C_OrderLine_ID AND m.M_InOutLine_ID IS NOT NULL) LEFT OUTER JOIN M_Product p ON (l.M_Product_ID=p.M_Product_ID) LEFT OUTER JOIN C_Charge c ON (l.C_Charge_ID=c.C_Charge_ID) LEFT OUTER JOIN C_UOM uom ON (l.C_UOM_ID=uom.C_UOM_ID) " +
					 "WHERE l.C_Order_ID="+p_order.getC_Order_ID()+ " " +
					 "GROUP BY l.QtyOrdered,CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END, l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name), l.M_Product_ID,COALESCE(p.Name,c.Name), l.Line,l.C_OrderLine_ID ORDER BY l.Line";
		
		try
		{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();
		    
		    while(rs.next()){
		    	
				Number d = rs.getInt("Qty");      //  1-Qty
				BigDecimal QtyEntered = new BigDecimal(d .doubleValue());
		
				int C_UOM_ID = rs.getInt("UOM");                    //  2-UOM
				int M_Product_ID = rs.getInt("Product");                          //  3-Product
				int C_OrderLine_ID = rs.getInt("Line");      //  4-OrderLine
				int C_InvoiceLine_ID = 0;                                  //  6-InvoiceLine
				
				MInvoiceLine il = null;
				if (C_InvoiceLine_ID != 0)
					il = new MInvoiceLine (ctx, C_InvoiceLine_ID, null);
				//	Precision of Qty UOM
				int precision = 2;
				if (M_Product_ID != 0)
				{
					org.compiere.model.MProduct product = org.compiere.model.MProduct.get(ctx, M_Product_ID);
					precision = product.getUOMPrecision();
				}
				QtyEntered = QtyEntered.setScale(precision, BigDecimal.ROUND_HALF_DOWN);
						
				//	Create new InOut Line
				MInOutLine iol = new MInOutLine (inout);
				iol.setM_Product_ID(M_Product_ID, C_UOM_ID);	//	Line UOM
		

				//Le seteo a la cantidad que va a inventario la cantidad Recibida * unidad de compra/unidad de venta
				getPUPS(C_OrderLine_ID);
				Integer auxQty = ((QtyEntered.intValue()*PU)/PS);
				iol.setTargetQty(new BigDecimal(auxQty));
				iol.setPickedQty(new BigDecimal(auxQty));
				iol.setQty(new BigDecimal(auxQty));
				iol.setMovementQty(new BigDecimal(auxQty));
				QtyEntered = new BigDecimal(auxQty);
				
				//
				MOrderLine ol = null;
				if (C_OrderLine_ID != 0)
				{
					iol.setC_OrderLine_ID(C_OrderLine_ID);
					ol = new MOrderLine (ctx, C_OrderLine_ID, null);
					//	iol.setOrderLine(ol, M_Locator_ID, QtyEntered);
					if (ol.getQtyEntered().compareTo(ol.getQtyOrdered()) != 0)
					{
						iol.setMovementQty(QtyEntered
							.multiply(ol.getQtyOrdered())
							.divide(ol.getQtyEntered(), 12, BigDecimal.ROUND_HALF_UP));
						iol.setC_UOM_ID(ol.getC_UOM_ID());
					}
					iol.setM_AttributeSetInstance_ID(ol.getM_AttributeSetInstance_ID());
					iol.setDescription(ol.getDescription());
					//
					iol.setC_Project_ID(ol.getC_Project_ID());
					iol.setC_ProjectPhase_ID(ol.getC_ProjectPhase_ID());
					iol.setC_ProjectTask_ID(ol.getC_ProjectTask_ID());
					iol.setC_Activity_ID(ol.getC_Activity_ID());
					iol.setC_Campaign_ID(ol.getC_Campaign_ID());
					iol.setAD_OrgTrx_ID(ol.getAD_OrgTrx_ID());
					iol.setUser1_ID(ol.getUser1_ID());
					iol.setUser2_ID(ol.getUser2_ID());
				}
				else if (il != null)
				{
				//	iol.setInvoiceLine(il, M_Locator_ID, QtyEntered);
					if (il.getQtyEntered().compareTo(il.getQtyInvoiced()) != 0)
					{
						iol.setQtyEntered(QtyEntered
							.multiply(il.getQtyInvoiced())
							.divide(il.getQtyEntered(), 12, BigDecimal.ROUND_HALF_UP));
						iol.setC_UOM_ID(il.getC_UOM_ID());
					}
					iol.setDescription(il.getDescription());
					iol.setC_Project_ID(il.getC_Project_ID());
					iol.setC_ProjectPhase_ID(il.getC_ProjectPhase_ID());
					iol.setC_ProjectTask_ID(il.getC_ProjectTask_ID());
					iol.setC_Activity_ID(il.getC_Activity_ID());
					iol.setC_Campaign_ID(il.getC_Campaign_ID());
					iol.setAD_OrgTrx_ID(il.getAD_OrgTrx_ID());
					iol.setUser1_ID(il.getUser1_ID());
					iol.setUser2_ID(il.getUser2_ID());
				}
				//	Charge
				if (M_Product_ID == 0)
				{
					if (ol != null && ol.getC_Charge_ID() != 0)			//	from order
						iol.setC_Charge_ID(ol.getC_Charge_ID());
					else if (il != null && il.getC_Charge_ID() != 0)	//	from invoice
						iol.setC_Charge_ID(il.getC_Charge_ID());
				}
				//
				iol.setM_Locator_ID(M_Locator_ID);
				if (!iol.save()){
		
				}
				//	Create Invoice Line Link
				else if (il != null)
				{
					il.setM_InOutLine_ID(iol.getM_InOutLine_ID());
					il.save();
				}
		    } //while
		    
			 rs.close();
			 pstmt.close();
		
		} //try
		catch (Exception e) {
			log.log(Level.SEVERE, SQL, e.getMessage());
		}
		
		
		/**
		 *  Update Header
		 *  - if linked to another order/invoice - remove link
		 *  - if no link set it
		 */
		if (p_order != null && p_order.getC_Order_ID() != 0)
		{
			inout.setC_Order_ID (p_order.getC_Order_ID());
			inout.setAD_OrgTrx_ID(p_order.getAD_OrgTrx_ID());
			inout.setC_Project_ID(p_order.getC_Project_ID());
			inout.setC_Campaign_ID(p_order.getC_Campaign_ID());
			inout.setC_Activity_ID(p_order.getC_Activity_ID());
			inout.setUser1_ID(p_order.getUser1_ID());
			inout.setUser2_ID(p_order.getUser2_ID());
			inout.setDateOrdered(p_order.getDateOrdered());
		}
		if (m_invoice != null && m_invoice.getC_Invoice_ID() != 0)
		{
			if (inout.getC_Order_ID() == 0)
				inout.setC_Order_ID (m_invoice.getC_Order_ID());
			inout.setC_Invoice_ID (m_invoice.getC_Invoice_ID());
			inout.setAD_OrgTrx_ID(m_invoice.getAD_OrgTrx_ID());
			inout.setC_Project_ID(m_invoice.getC_Project_ID());
			inout.setC_Campaign_ID(m_invoice.getC_Campaign_ID());
			inout.setC_Activity_ID(m_invoice.getC_Activity_ID());
			inout.setUser1_ID(m_invoice.getUser1_ID());
			inout.setUser2_ID(m_invoice.getUser2_ID());			
		}
		
		inout.setXX_CompleteReception("Y");
		inout.setXX_InOutStatus("CH");
		inout.save();
		
		return true;
	}   //  save
	
	private static void getPUPS(int C_OrderLine_ID){
		
		//se obtienen la unidad de compra y la unidad de venta para el CV
		String SQL =
			"Select uc.XX_UNITCONVERSION, us.XX_UNITCONVERSION "+
			"from XX_VMR_PO_LINEREFPROV lp, XX_VMR_UnitConversion uc, XX_VMR_UnitConversion us "+
			"where "+
			"lp.XX_PiecesBySale_ID = us.XX_VMR_UnitConversion_ID "+
			"and lp.XX_VMR_UnitConversion_ID = uc.XX_VMR_UnitConversion_ID "+
			"and lp.XX_VMR_PO_LINEREFPROV_ID=" +
			"(SELECT XX_VMR_PO_LINEREFPROV_ID FROM C_ORDERLINE WHERE C_ORDERLINE_ID="+C_OrderLine_ID+")";
		
		PU=1;
		PS=1;
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();	
			
			while(rs.next()) //compruebo que ya exista la matriz
			{	
				PU=rs.getInt(1);
				PS=rs.getInt(2);
			}
						
			rs.close();
			pstmt.close();
						
		}
		catch(Exception e)
		{	
			e.getMessage();
		}
	}

	private void createMInOut(MOrder order, Trx trans){
	
		MInOut inOut = new MInOut( Env.getCtx(), 0, trans);
		MWarehouse w = new MWarehouse(Env.getCtx(), order.getM_Warehouse_ID(), null);
		inOut.setAD_Org_ID(w.getAD_Org_ID());
		inOut.setM_Warehouse_ID(order.getM_Warehouse_ID());
		inOut.setC_BPartner_ID(order.getC_BPartner_ID());
		inOut.setC_Order_ID(order.get_ID());
		inOut.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());
		inOut.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPERECEIPT_ID"));
		inOut.setMovementType("V+");
		inOut.set_Value("XX_POType", order.getXX_POType());
		inOut.save();
		trans.commit();
		saveData( Env.getCtx(), order, null, Utilities.obtenerLocatorChequeado(order.getM_Warehouse_ID()).getM_Locator_ID(), inOut.get_ID());
		
		inOut.setXX_CompleteCheckup("Y");
	    
	    inOut.setDocAction(X_M_InOut.DOCACTION_Complete);
	    DocumentEngine.processIt(inOut, X_M_InOut.DOCACTION_Complete);
	    inOut.save();
	}
	
	private boolean sendMail(String oldOrderDoc, Integer oldDepartID, String newOrderDoc, Integer newDepartID, Vector<String> products, Vector<String> refDescByProduct){
		
		//Revisoria
		String emailTo = "revisoria@beco.com.ve";
			
		MClient m_client = MClient.get(Env.getCtx());
		X_XX_VMR_Department oldDepart = new X_XX_VMR_Department( Env.getCtx(), oldDepartID, null);
		X_XX_VMR_Department newDepart = new X_XX_VMR_Department( Env.getCtx(), newDepartID, null);
			
		String subject = "Productos Ingresados por Reporte de Novedad, O/C antigua: " + oldOrderDoc + " - O/C nueva: " + newOrderDoc;
		String msg = "Se han ingresado al inventario productos provenientes del Reporte de Novedad de la " +
					 "O/C: " + oldOrderDoc + " (Dpto: " + oldDepart.getValue() + " " + oldDepart.getName() +")";
		
		msg += "\nNueva O/C: " + newOrderDoc + " (Dpto: " + newDepart.getValue() + " " + newDepart.getName() +")";
		
		msg += "\n\nProductos:";
		
		for( int i=0; i < products.size(); i++){
			
			msg += "\n- " + products.get(i);
			msg += " (" + refDescByProduct.get(i) + ")";
		}
			
		EMail email = m_client.createEMail(null, emailTo, "Revisoria", subject, msg);
		
		if (email != null)
		{			
				
			Vector<String> emails = getEmails(oldDepartID);
			
			for(int i=0; i<emails.size(); i++){
		
				if(emails.get(i).contains("@")){
					email.addTo(emails.get(i), emailRoles.get(i));
				}				
			}

			String status = email.send();
			
			log.info("Email Send status: " + status);
				
			if (email.isSentOK())
			{
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	private Vector<String> getEmails(int oldDepartID){
		
		Vector<String> emails = new Vector<String>();
		emailRoles =  new Vector<String>();
		
		String sqlEmails = "select email,c.name from ad_user a, AD_User_Roles b, AD_Role c " +
						   "where a.ad_user_id = b.ad_user_id and b.ISACTIVE = 'Y' " +
                           "and b.ad_role_id=c.ad_role_id and " +
                           "(b.AD_ROLE_ID = " + Env.getCtx().getContextAsInt("#XX_L_ROLECHECKUPCOORDINATOR_ID") +
                           " or b.AD_ROLE_ID = " + Env.getCtx().getContextAsInt("#XX_L_ROLEWAREHOUSECOORD_ID") +
                           " or b.AD_ROLE_ID = " + Env.getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID") +
                           ") order by b.AD_ROLE_ID";

		ResultSet rs_Email = null;
		PreparedStatement prst_Email = null;
		
		try {
		
			prst_Email = DB.prepareStatement(sqlEmails, null);
			rs_Email = prst_Email.executeQuery();
			
			while(rs_Email.next()){
				
				emails.add(rs_Email.getString(1));
				emailRoles.add(rs_Email.getString(2));
			}
			
		}catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		finally
		{
			DB.closeResultSet(rs_Email);
			DB.closeStatement(prst_Email);
		}
		
		//Envia correo al Planificador
		X_XX_VMR_Department oldDepart = new X_XX_VMR_Department( Env.getCtx(), oldDepartID, null);
		
		sqlEmails = "select email from AD_USER WHERE C_BPARTNER_ID = " + oldDepart.getXX_InventorySchedule_ID();

		rs_Email = null;
		prst_Email = null;
		
		try {
		
			prst_Email = DB.prepareStatement(sqlEmails, null);
			rs_Email = prst_Email.executeQuery();
			
			while(rs_Email.next()){
				
				emails.add(rs_Email.getString(1));
				emailRoles.add("Planificador");
			}
			
		}catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		finally
		{
			DB.closeResultSet(rs_Email);
			DB.closeStatement(prst_Email);
		}
		
		return emails;
	}
	
}//End form

