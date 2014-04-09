package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.APanel;
import org.compiere.apps.AWindow;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.framework.Query;
import org.compiere.grid.GridController;
import org.compiere.grid.VTabbedPane;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.GridTable;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MRole;
import org.compiere.model.MTransaction;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_M_InOut;
import org.compiere.model.X_Ref_Quantity_Type;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.DocumentEngine;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MInOut;
import compiere.model.cds.MInOutLine;
import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVLOUnsolicitedProduct;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.X_XX_VMR_Line;
import compiere.model.cds.X_XX_VMR_Section;

/**
 *  Productos no Solicitados Incorrectos
 *
 *  @author     Rosmaira Arvelo
 *  @version    
 */

public class XX_UnsolicitedProductIncorrect_Form extends CPanel

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
		m_frame.setName("UnsolicitedProductIncorrect");
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		
		fromProduct = Env.getCtx().getContextAsInt("#XX_FROMPRODUCT");
		
		
		Env.getCtx().remove("#XX_FROMPRODUCT");
		
	
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
	static CLogger 			log = CLogger.getCLogger(XX_UnsolicitedProductIncorrect_Form.class);
	
	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	
	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	static Ctx             ctx_aux = Env.getCtx();
	static MProduct 	   productReference = new MProduct(Env.getCtx(),0,null);
	static MVLOUnsolicitedProduct unsolProd = new MVLOUnsolicitedProduct(Env.getCtx(),0,null);	
	
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
	private TitledBorder xBorder = new TitledBorder(Msg.translate(Env.getCtx(), "Product"));
	private MiniTable xTable = new MiniTable();
	private CPanel xPanel = new CPanel();
	
	private static MOrder     order;	
	private static CComboBox  productRefCombo = new CComboBox();
	private static VLookup    product = null;
	
	private CLabel productRef = new CLabel(Msg.translate(Env.getCtx(), "VendorProductRef"));		
	private CLabel productLabel = new CLabel(Msg.translate(Env.getCtx(), "M_Product_ID"));	
	
	private CButton bSearch = new CButton();
	private CButton bNewProduct = new CButton();
	private JButton bSave = ConfirmPanel.createSaveButton(true);
	private JButton bCancel = ConfirmPanel.createCancelButton(true);	
	
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);

	private Integer    fromProduct = 0;
	
	KeyNamePair loadKNP = new KeyNamePair(0, "");
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
		product = VLookup.createProduct(m_WindowNo);
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		centerPanel.setLayout(centerLayout);
		southPanel.setLayout(southLayout);
		
		xPanel.setLayout(xLayout);
	
		bSearch.setText(Msg.translate(Env.getCtx(), "Search"));
		bSearch.setPreferredSize(new Dimension(80,22));	
		bSearch.setEnabled(true);
		
		bNewProduct.setText(Msg.translate(Env.getCtx(), "NewProduct"));
		bNewProduct.setEnabled(true);
		
		bSearch.addActionListener(this);
		bNewProduct.addActionListener(this);
		bSave.addActionListener(this);
		bCancel.addActionListener(this);
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(650, 250));		
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
	
		xScrollPane.getViewport().add(xTable, null);
		
		northPanel.add(productRef,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		northPanel.add(productRefCombo,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));	
		northPanel.add(productLabel,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		northPanel.add(product,     new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));		
		
		northPanel.add(bSearch,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
		northPanel.add(bNewProduct,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
	    
		southPanel.add(bSave,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
		southPanel.add(bCancel,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
	    southPanel.validate();
	
	}   //  jbInit
	
	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{		
		MInOutLine inOutLine = new MInOutLine(Env.getCtx(),unsolProd.getM_InOutLine_ID(),null);
		MInOut inOut = new MInOut(Env.getCtx(),inOutLine.getM_InOut_ID(),null);
		order = new MOrder(Env.getCtx(), inOut.getC_Order_ID(),null);
		
		loadInfo();		
		
		if(productReference.get_ID()!=0){		
			tableInit();
			tableLoad (xTable);
			
			if(xTable.getRowCount()!=0){
				bSave.setEnabled(false);
				statusBar.setStatusLine("");
				statusBar.setStatusDB(xTable.getRowCount());				
			}
			else{
				bSave.setEnabled(false);
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(),"FindZeroRecords"));
				statusBar.setStatusLine(Msg.getMsg(Env.getCtx(),"FindZeroRecords"));
			}
		}		
		
	}   //  dynInit
	
	private void loadInfo()
	{
		productRefCombo.removeActionListener(this);
		product.removeActionListener(this);
		
		productRefCombo.removeAllItems();	
		product.setValue(null);
		
		int lin = unsolProd.getXX_VMR_Line_ID();
		X_XX_VMR_Line linea = new X_XX_VMR_Line(Env.getCtx(),lin,null);
		
		int sec = unsolProd.getXX_VMR_Section_ID();
		X_XX_VMR_Section section = new X_XX_VMR_Section(Env.getCtx(),sec,null);
			
		// Si la linea o seccion del producto incorrecto son genericas
		// lo inactivo en el maestro de producto y en el maestro de referencias
		if(linea.getValue().equals("99") || section.getValue().equals("99"))
			bNewProduct.setEnabled(false);
		else
			bNewProduct.setEnabled(true);
		
		bSave.setEnabled(false);
		
		//Loading Products References
		String sql = "SELECT DISTINCT VALUE FROM XX_VMR_VENDORPRODREF WHERE C_BPARTNER_ID="+order.getC_BPartner_ID()+" AND XX_VMR_DEPARTMENT_ID="+order.getXX_VMR_DEPARTMENT_ID()+" ORDER BY VALUE ASC";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			loadKNP=new KeyNamePair(0, new String());
			productRefCombo.addItem(loadKNP);
			int i=0;
			while (rs.next())
			{
				loadKNP = new KeyNamePair(i++, rs.getString(1));
				productRefCombo.addItem(loadKNP);//agrego las referencias de los productos
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "VendorProductRef"),   ".", KeyNamePair.class),     //  1
			new ColumnInfo(Msg.getMsg(Env.getCtx(), "ProductKey"),   ".", String.class),     //  2
			new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"),   ".", KeyNamePair.class),      //  3
			new ColumnInfo(Msg.getMsg(Env.getCtx(), "Vendor"),   ".", KeyNamePair.class),			 //  4
			new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Department"),   ".", KeyNamePair.class),			 //  5
			new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Line"),   ".", KeyNamePair.class),			 //  6
			new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Section"),   ".", KeyNamePair.class),			 //  7
			new ColumnInfo(Msg.translate(Env.getCtx(), "AttributeSetInstance"),   ".", KeyNamePair.class),  //  8
			new ColumnInfo(Msg.getMsg(Env.getCtx(), "MainCharacteristic"),   ".", KeyNamePair.class),     //  9
		};
	
		xTable.prepareTable(layout, "", "", false, "");
		xTable.setAutoResizeMode(3);
		
		//  Visual
		CompiereColor.setBackground (this);
	
		//  Listener
		xTable.getSelectionModel().addListSelectionListener(this);
		xTable.getModel().addTableModelListener(this);
		xTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				int tableRow = xTable.getSelectedRow();
				
				if(tableRow!=-1){ // Si se selecciona alguna linea en la tabla de productos
					xTable.setSelectionBackground(Color.white);
					bSave.setEnabled(true);
				}				
			}
		});		
		
		product.addActionListener(this);		
		
		//  Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);
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
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == bSearch)
			cmd_Search();
		else if (e.getSource() == bNewProduct)
			cmd_newProduct();
		else if (e.getSource() == bSave)
			cmd_Save();
		else if (e.getSource() == bCancel){
			dispose();
			unsolProd = new MVLOUnsolicitedProduct(Env.getCtx(),0,null);
			order = new MOrder(Env.getCtx(),0,null);
		}
		
	}   //  actionPerformed
	
	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{		
		if((productRefCombo.getSelectedIndex()!=0) || (product.getValue() != null)){
			tableInit();
			tableLoad (xTable);
			
			if(xTable.getRowCount()!=0){
				bSave.setEnabled(false);
				statusBar.setStatusLine("");
				statusBar.setStatusDB(xTable.getRowCount());				
			}
			else{
				bSave.setEnabled(false);
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(),"FindZeroRecords"));
				statusBar.setStatusLine(Msg.getMsg(Env.getCtx(),"FindZeroRecords"));
			}
		}
		else{
			xScrollPane.getViewport().remove(xTable);
			xTable = new MiniTable();
			xScrollPane.getViewport().add(xTable, null);
			ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(),"SelectData"));
			loadInfo();
			
			statusBar.setStatusLine(Msg.getMsg(Env.getCtx(),"FindZeroRecords"));
		}
		
	}   //  cmd_Search
	
	
	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{	
	}   //  valueChanged
	
	/*
	 * 
	 */
	private void cmd_newProduct()
	{	
		Env.getCtx().setContext("#Depart_Aux", order.getXX_VMR_DEPARTMENT_ID());
		Env.getCtx().setContext("#FromProcess_Aux", "U");
		
		AWindow windowProduct = new AWindow();
		Query query = Query.getNoRecordQuery("M_Product", true);
		windowProduct.initWindow(140, query);
		AEnv.showCenterScreen(windowProduct);
		
		// Obtenemos el GridController para setear la variable m_changed=true
    	JRootPane jRootPane  = ((JRootPane)windowProduct.getComponent(0));
    	JLayeredPane jLayeredPane = (JLayeredPane)jRootPane.getComponent(1);
    	JPanel jPanel = (JPanel)jLayeredPane.getComponent(0);
    	APanel aPanel = (APanel)jPanel.getComponent(0);
    	VTabbedPane vTabbedPane = (VTabbedPane)aPanel.getComponent(0);
    	GridController gridController = (GridController)vTabbedPane.getComponent(0);
    	GridTable mTable = gridController.getMTab().getTableModel();
		mTable.setChanged(true);    
		
		MProduct.loadUnsolicitedProduct(unsolProd, ctx_aux);
		
		Env.getCtx().remove("#Depart_Aux");
		Env.getCtx().remove("#FromProcess_Aux");		
    	
	}// fin cmd_newProduct
	
	private void cmd_Save()
	{		
		if((xTable.getRowCount()!=0))
		{
			int row = xTable.getSelectedRow();
			
			if(row!=-1) // Si tengo seleccionada alguna lines en la tabla producto
			{	
				if(ADialog.ask(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "SaveProduct?")))
				{					
					MVLOUnsolicitedProduct unsolicitedProduct = new MVLOUnsolicitedProduct (Env.getCtx(), 0, null);
					
					KeyNamePair prodKey = (KeyNamePair)xTable.getValueAt(row, 2);
					Integer count = getUnsolicitedProductCount(prodKey.getKey(),order.get_ID());
					
					if(count==0){
						//Obtengo la cantidad de productos no solicitados hasta ahora
						int countPrevious = getUnsolicitedProductCount();
						
						// Agrego el nuevo producto no solicitado con estatus validado
						unsolicitedProduct.setM_Product_ID(prodKey.getKey());
						unsolicitedProduct.setXX_ValidateProduct(true);
						unsolicitedProduct.setXX_Record_ID(prodKey.getKey());
						unsolicitedProduct.save();							
						
						//Obtengo la cantidad de productos no solicitados finalmente
						int countFinal = getUnsolicitedProductCount();
						
						if(countPrevious<countFinal){ // Si se agregó el producto
							
							ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "RecordSaved"));
							
							// Creo la recepcion del producto correcto
							MInOutLine inOutLine = new MInOutLine(Env.getCtx(),unsolProd.getM_InOutLine_ID(),null);
							BigDecimal quantity = inOutLine.getMovementQty();
							
							Integer cbpartner = getC_BPartner(prodKey.getKey());							
														
							MInOut inOut = new MInOut(Env.getCtx(),0,null);
							inOut.setC_BPartner_ID(cbpartner);
							inOut.setC_BPartner_Location_ID(getC_BPartnerLocation(cbpartner));
							inOut.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEMMRECEIPT_ID"));
							inOut.setMovementType(Env.getCtx().getContext("#XX_L_MOVETYPEVENDORRECE_ID"));
							inOut.setM_Warehouse_ID(inOutLine.getM_Warehouse_ID());
							inOut.setAD_User_ID(Env.getCtx().getAD_User_ID());
							inOut.save();
							
							MInOutLine l = new MInOutLine(Env.getCtx(),0,null);
							l.setM_Product_ID(prodKey.getKey());
							l.setM_Locator_ID(getLocatorCheck(inOutLine.getM_Warehouse_ID()));
							l.setM_InOut_ID(inOut.get_ID());
							l.setPickedQty(quantity);
							l.setC_UOM_ID(Env.getCtx().getContextAsInt("#XX_L_C_UOMEACH_ID"));
							l.setLine(10);
							l.setM_AttributeSetInstance_ID(0);
							l.save();
							
							inOut.setDocAction(X_M_InOut.DOCACTION_Complete);
							DocumentEngine.processIt(inOut, X_M_InOut.DOCACTION_Complete);
						    inOut.save();
							
							// Hago el ajuste por Inventario Fisico del producto incorrecto que
						    // esta en el Locator "Por Validar" 						    
						    MWarehouse warehouse = new MWarehouse(Env.getCtx(), inOutLine.getM_Warehouse_ID(), null);
						    
						    MInventory inventory = new MInventory(Env.getCtx(),0,null);
						    inventory.setAD_Org_ID(warehouse.getAD_Org_ID());
						    inventory.setM_Warehouse_ID(warehouse.get_ID());
						    inventory.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEPHYINVEN_ID"));
						    inventory.save();
						    
						    MInventoryLine inventoryLine = new MInventoryLine(Env.getCtx(),0,null);
						    inventoryLine.setM_AttributeSetInstance_ID(inOutLine.getM_AttributeSetInstance_ID());
						    inventoryLine.setM_Inventory_ID(inventory.get_ID());
						    inventoryLine.setM_Locator_ID(inOutLine.getM_Locator_ID());
						    inventoryLine.setM_Product_ID(inOutLine.getM_Product_ID());
						    inventoryLine.setQtyCount(new BigDecimal(0));
						    inventoryLine.setQtyBook(getSumQtyBook(inOutLine.getM_Locator_ID(), unsolProd.getM_Product_ID()));
						    inventoryLine.save();
						    
						    inventory.setDocAction(MInventory.DOCACTION_Complete);
						    DocumentEngine.processIt(inventory, MInventory.DOCACTION_Complete);
						    inventory.save();							
							
							//Guardo la linea de chequeo creada para el producto correcto
							unsolicitedProduct.setM_InOutLine_ID(l.get_ID());
							unsolicitedProduct.save();
							
							// Guardo el producto correcto en el incorrecto para saber cual valide 
							// e inactivo el incorrecro
							unsolProd.setXX_ValidateProduct(true);
							unsolProd.setXX_Record_ID(unsolicitedProduct.getM_Product_ID());
							unsolProd.setIsActive(false);
							unsolProd.save();
								
							int lin = unsolProd.getXX_VMR_Line_ID();
							X_XX_VMR_Line linea = new X_XX_VMR_Line(Env.getCtx(),lin,null);
							
							int sec = unsolProd.getXX_VMR_Section_ID();
							X_XX_VMR_Section section = new X_XX_VMR_Section(Env.getCtx(),sec,null);
								
							// Si la linea o seccion del producto incorrecto son genericas
							// lo inactivo en el maestro de producto y en el maestro de referencias
							if(linea.getValue().equals("99") || section.getValue().equals("99"))
							{	
								Env.getCtx().setContext("#FromProcess_Aux", "R");								
								MProduct prod = new MProduct(Env.getCtx(),unsolProd.getM_Product_ID(),null);
								prod.setIsActive(false);
								prod.save();
								Env.getCtx().remove("#FromProcess_Aux");
										
								MVMRVendorProdRef venProRef = new MVMRVendorProdRef(Env.getCtx(),unsolProd.getXX_VMR_VendorProdRef_ID(),null);
								venProRef.setIsActive(false);
								venProRef.save();
							}
								
							xScrollPane.getViewport().remove(xTable);
							xTable = new MiniTable();
							xScrollPane.getViewport().add(xTable, null);
							
							productRefCombo.setEnabled(true);
							product.setEnabled(true);
							bSearch.setEnabled(true);
							bNewProduct.setEnabled(true);
								
							loadInfo();
							
							if(fromProduct != 0){
								ADialog.info(m_WindowNo,m_frame,Msg.getMsg(Env.getCtx(),"MustRefresh"));
							}
							
							dispose();
						}
						else{
							ADialog.error(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "NoRecordCreated"));
						}
										
					}
					else
					{
						ADialog.error(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "ProdCodError", new String[] {"'"+order.getDocumentNo()+"'"}));
					}
				}	
			}
		}
	}// fin cmd_Save
	
	/**************************************************************************
	 *  Initialize Table access - create SQL, dateColumn.
	 *  <br>
	 *  The driving table is "hdr", e.g. for hdr.C_BPartner_ID=..
	 *  The line table is "lin", e.g. for lin.M_Product_ID=..
	 *  You use the dateColumn/qtyColumn variable directly as it is table specific.
	 *  <br>
	 *  The sql is dependent on MatchMode:
	 *  - If Matched - all (fully or partially) matched records are listed
	 *  - If Not Matched - all not fully matched records are listed
	 *  @param display (Invoice, Shipment, Order) see MATCH_*
	 *  @param matchToType (Invoice, Shipment, Order) see MATCH_*
	 */
	private static void tableInit ()
	{
		m_sql = new StringBuffer ();
	
		m_sql.append("SELECT vpr.Value, vpr.XX_VMR_VendorProdRef_ID, p.Value, p.Name, p.M_Product_ID, c.Name, "
					  + "c.C_BPartner_ID, d.Name, d.XX_VMR_Department_ID, l.Name, l.XX_VMR_Line_ID, s.Name, " 
					  + "s.XX_VMR_Section_ID, m.Description, m.M_AttributeSetInstance_ID, ca.Name, " 
					  + "ca.XX_VMR_LongCharacteristic_ID "
					  + "FROM M_Product p, XX_VMR_VendorProdRef vpr, C_BPartner c, XX_VMR_Department d, "
					  + "XX_VMR_Line l, XX_VMR_Section s, M_AttributeSetInstance m, XX_VMR_LongCharacteristic ca "
					  + "WHERE p.ISACTIVE='Y' AND vpr.ISACTIVE='Y' AND c.ISACTIVE='Y' AND c.ISVENDOR='Y' "
					  + "AND p.XX_VMR_VendorProdRef_ID=vpr.XX_VMR_VendorProdRef_ID "
					  + "AND c.C_BPartner_ID="+order.getC_BPartner_ID()
					  + "AND vpr.C_BPartner_ID=c.C_BPartner_ID "
					  + "AND d.XX_VMR_Department_ID="+order.getXX_VMR_DEPARTMENT_ID()
					  + "AND p.XX_VMR_Department_ID = d.XX_VMR_Department_ID "
					  + "AND p.XX_VMR_Line_ID = l.XX_VMR_Line_ID "
					  + "AND p.XX_VMR_Section_ID = s.XX_VMR_Section_ID "
					  + "AND p.XX_VMR_LongCharacteristic_ID = ca.XX_VMR_LongCharacteristic_ID (+) "
					  + "AND p.M_AttributeSetInstance_ID = m.M_AttributeSetInstance_ID (+)");
			
		if(productRefCombo.getSelectedIndex()!=0){
			m_sql.append(" AND ").append("vpr.Value=").append("'").append(productRefCombo.getSelectedItem().toString()).append("'");
		}
		if(product.getValue() != null){
			m_sql.append(" AND ").append("p.M_Product_ID=").append(product.getValue());
		}
		if(productReference.get_ID()!=0)
		{
			m_sql.append(" AND ").append("p.M_Product_ID=").append(productReference.get_ID());
			productReference = new MProduct(Env.getCtx(), 0,null);
		}
			
		m_orderBy = " order by vpr.Value ";			
		
	}   //  tableInit
	
	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private static void tableLoad (MiniTable table)
	{
		String sql = MRole.getDefault().addAccessSQL(
			m_sql.toString(), "vpr", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
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
	
	/*
	 *	Obtengo la cantidad de productos no solicitados
	 */
	private int getUnsolicitedProductCount(){
		
		int count=0;
		String SQL = "SELECT COUNT(*) FROM XX_VLO_UnsolicitedProduct "
				   + "ORDER BY XX_VLO_UnsolicitedProduct_ID ASC";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				count = rs.getInt("COUNT(*)");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return count;
	}
	
	/*
	 * Obtengo la cantidad de productos no solicitados según la orden y el producto seleccionados
	*/
	private Integer getUnsolicitedProductCount(Integer M_Product_ID, Integer C_Order_ID)
	{
		Integer count=0;
		String sql = "SELECT COUNT(*)" 
			       + "FROM XX_VLO_UnsolicitedProduct WHERE C_Order_ID="+C_Order_ID+" " 
			       + "AND M_Product_ID="+M_Product_ID+" ";
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				count = rs.getInt("COUNT(*)");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, sql);
			}
		
		return count;
	}	
	
	/*
	 *	Obtengo el ID del C_BPartner segun el producto 
	 */
	private Integer getC_BPartner(Integer M_Product_ID){
		
		Integer partner=0;
		String SQL = "SELECT v.C_BPartner_ID FROM M_Product m, XX_VMR_VendorProdRef v "
				   + "WHERE m.M_Product_ID="+M_Product_ID
				   + " AND v.XX_VMR_VendorProdRef_ID=m.XX_VMR_VendorProdRef_ID";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				partner = rs.getInt("C_BPartner_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return partner;
	}
	
	/*
	 *	Obtengo el ID del C_BPartnerLocation segun el C_BPartner 
	 */
	private Integer getC_BPartnerLocation(Integer C_BPartner_ID){
		
		Integer location=0;
		String SQL = "SELECT C_BPartner_Location_ID FROM C_BPartner_Location "
				   + "WHERE C_BPartner_ID="+C_BPartner_ID;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				location = rs.getInt("C_BPartner_Location_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return location;
	}
	
	/*
	 *	Obtengo el ID del Locator segun el Warehouse del producto 
	 */
	private Integer getLocatorCheck(Integer M_Warehouse_ID){
		
		Integer locator=0;
		String SQL = "SELECT l.M_Locator_ID FROM M_Locator l, M_Warehouse w "
				   + "WHERE l.M_Warehouse_ID=w.M_Warehouse_ID AND IsDefault='Y' "
				   + "AND w.M_Warehouse_ID="+M_Warehouse_ID+" AND l.IsActive='Y'";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				locator = rs.getInt("M_Locator_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return locator;
	}
	
	/*
	 * Obtengo la suma de las piezas del locator "por validar" del producto seleccionado
	*/
	private BigDecimal getSumQtyBook(Integer M_Locator_ID, Integer M_Product_ID)
	{
		BigDecimal sum= new BigDecimal(0);
		String sql = "SELECT COALESCE(SUM(Qty),0) as suma " 
			       + "FROM M_StorageDetail WHERE M_Locator_ID="+M_Locator_ID 
			       + " AND QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"'"
			       + " AND M_AttributeSetInstance_ID>=0" 
				   + " AND M_lOCATOR_ID >= 0" 
			       + " AND M_Product_ID="+M_Product_ID+" ";
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				sum = rs.getBigDecimal("suma");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, sql);
			}
		
		return sum;
	}
	
	/**
	 *  cargar el producto al cual está relacionado la referencia
	 */
	public static void loadMProduct(MProduct product_aux, Ctx ctx)
	{
		productReference=product_aux;
		ctx_aux = ctx;
		
	}	//	product
	
	/**
	 *  cargar el producto no solicitado al cual está relacionado la referencia
	 */
	public static void loadUnsolicitedProduct(MVLOUnsolicitedProduct unsolProd_aux, Ctx ctx)
	{
		unsolProd=unsolProd_aux;
		ctx_aux = ctx;
		
	}	//	Unsolicited Product 	
	
	@Override
	public void tableChanged(TableModelEvent e) 
	{
		
	}
	
	
}//End form
