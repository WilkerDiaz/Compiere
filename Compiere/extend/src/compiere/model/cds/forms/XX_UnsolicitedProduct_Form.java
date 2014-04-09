package compiere.model.cds.forms;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.framework.Query;
import org.compiere.grid.GridController;
import org.compiere.grid.VTabbedPane;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.*;
import org.compiere.model.GridTable;
import org.compiere.model.MPInstance;
import org.compiere.model.MRole;
import org.compiere.plaf.*;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.*;
import org.compiere.util.*;
import compiere.model.cds.MInOut;
import compiere.model.cds.MInOutLine;
import compiere.model.cds.MInvoice;
import compiere.model.cds.MInvoiceLine;
import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVLOUnsolicitedProduct;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.Utilities;

 
/**
 *  Productos no Solicitados
 *
 *  @author     Rosmaira Arvelo
 *  @version    
 */

public class XX_UnsolicitedProduct_Form extends CPanel

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
		m_frame.setName("UnsolicitedProduct");
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		
		inOutLineID = Env.getCtx().getContextAsInt("#XX_CheckupInOutLine");
		invoiceLineID = Env.getCtx().getContextAsInt("#XX_UnsolicitePrd_InvoiceLine");
		fromVendorProdRef = Env.getCtx().getContextAsInt("#XX_FROMVENDORPRODREF");
		
		Env.getCtx().remove("#XX_CheckupInOutLine");
		Env.getCtx().remove("#XX_FROMVENDORPRODREF");
		Env.getCtx().remove("#XX_UnsolicitePrd_InvoiceLine");
	
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
	static CLogger 			log = CLogger.getCLogger(XX_UnsolicitedProduct_Form.class);
	
	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	
	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	static Ctx             ctx_aux = Env.getCtx();
	static MProduct        productReference = new MProduct(Env.getCtx(),0,null);		
	
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
	private CButton bNewVendorProdRef = new CButton();
	private JButton bSave = ConfirmPanel.createSaveButton(true);
	private JButton bCancel = ConfirmPanel.createCancelButton(true);	
		
	private HashMap<Integer,Integer> attachments = null;
	private String window = Env.getCtx().getContext("#XX_L_T_UNSOLICITEDPRODUCT");

	private String invoiceType = "";
	private Integer inOutLineID = 0;
	private Integer invoiceLineID = 0;
	private Integer fromVendorProdRef = 0;
	
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	
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
		if(invoiceLineID != 0)
		{
			MInvoiceLine invoiceLine = new MInvoiceLine(Env.getCtx(),invoiceLineID,null);
			MInvoice invoice = new MInvoice(Env.getCtx(),invoiceLine.getC_Invoice_ID(),null);
			invoiceType = invoice.getXX_InvoiceType();
		}
		
		product = VLookup.createProduct(m_WindowNo);
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		centerPanel.setLayout(centerLayout);
		southPanel.setLayout(southLayout);
		
		xPanel.setLayout(xLayout);
	
		bSearch.setText(Msg.translate(Env.getCtx(), "Search"));
		bSearch.setPreferredSize(new Dimension(80,22));	
		bSearch.setEnabled(true);
		
		bNewVendorProdRef.setText(Msg.translate(Env.getCtx(), "NewVendorProductRef"));
		bNewVendorProdRef.setEnabled(true);
		
		bSearch.addActionListener(this);
		bNewVendorProdRef.addActionListener(this);
		bSave.addActionListener(this);
		bCancel.addActionListener(this);
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(650, 250));		
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
	
		xScrollPane.getViewport().add(xTable, null);
		
		if(invoiceType.equalsIgnoreCase("I")){
			northPanel.add(productRef,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
			northPanel.add(productRefCombo,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		}	
		
		northPanel.add(productLabel,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 12, 5, 5), 0, 0));
		northPanel.add(product,     new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 0), 0, 0));	
		
		northPanel.add(bSearch,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
		
		if(invoiceType.equals("I"))
			northPanel.add(bNewVendorProdRef,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
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
		if(inOutLineID != 0)
		{
			MInOutLine inOutLine = new MInOutLine(Env.getCtx(),inOutLineID,null);
			MInOut inOut = new MInOut(Env.getCtx(),inOutLine.getM_InOut_ID(),null);
			order = new MOrder(Env.getCtx(), inOut.getC_Order_ID(),null);
		}
		
		if(invoiceLineID != 0)
		{
			MInvoiceLine invoiceLine = new MInvoiceLine(Env.getCtx(),invoiceLineID,null);
			MInvoice invoice = new MInvoice(Env.getCtx(),invoiceLine.getC_Invoice_ID(),null);
			order = new MOrder(Env.getCtx(), invoice.getC_Order_ID(),null);
		}
		
		loadInfo();
		
		if(productReference.get_ID()!=0){
			
			tableInit(invoiceType);
			tableLoad (xTable);
			
			if(xTable.getRowCount()!=0){
				bSave.setEnabled(false);
				statusBar.setStatusLine(Msg.getMsg(Env.getCtx(),"Product"));
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
		bNewVendorProdRef.setEnabled(true);
		bSave.setEnabled(false);
		
		//Loading Products References
		String sql = "SELECT DISTINCT VALUE FROM XX_VMR_VENDORPRODREF WHERE C_BPARTNER_ID="+order.getC_BPartner_ID()+" AND XX_VMR_DEPARTMENT_ID="+order.getXX_VMR_DEPARTMENT_ID()+" and IsActive = 'Y' ORDER BY VALUE ASC";
		
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
		
		ColumnInfo[] layout = null;
		
		if(invoiceType.equalsIgnoreCase("I")){
			layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "VendorProductRef"),   ".", KeyNamePair.class),     //  1
				new ColumnInfo(Msg.translate(Env.getCtx(), "ProductKey"),   ".", String.class),     //  2
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"),   ".", KeyNamePair.class),      //  3
				new ColumnInfo(Msg.translate(Env.getCtx(), "Vendor"),   ".", KeyNamePair.class),			 //  4
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Department"),   ".", KeyNamePair.class),			 //  5
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Line"),   ".", KeyNamePair.class),			 //  6
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Section"),   ".", KeyNamePair.class),			 //  7
				new ColumnInfo(Msg.translate(Env.getCtx(), "AttributeSetInstance"),   ".", KeyNamePair.class),  //  8
				new ColumnInfo(Msg.translate(Env.getCtx(), "MainCharacteristic"),   ".", KeyNamePair.class),     //  9
			};
		}else
		{
			layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "ProductKey"),   ".", String.class),     //  1
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"),   ".", KeyNamePair.class),      //  2
			};
		}
	
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
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {	
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
		else if (e.getSource() == bNewVendorProdRef)
			cmd_newVendorProdRef();
		else if (e.getSource() == bSave)
			cmd_Save();
		else if (e.getSource() == bCancel){
			dispose();
			order = new MOrder(Env.getCtx(),0,null);
		}
		
	}   //  actionPerformed
	
	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{		
		if((productRefCombo.getSelectedIndex()!=0) || (product.getValue() != null)){
			tableInit(invoiceType);
			tableLoad (xTable);
			
			if(xTable.getRowCount()!=0){
				bSave.setEnabled(false);
				statusBar.setStatusLine(Msg.getMsg(Env.getCtx(),"Product"));
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
	private void cmd_newVendorProdRef()
	{			
		Integer line = getLine(order.getXX_VMR_DEPARTMENT_ID());
		Integer section = getSection(line);
		Integer conversion = getUnitConversion();
		Integer purchase = getUnitPurchase();
		
		//Creo variables de sesion para atraparlas en la ventana Vendor Product Reference
		ctx_aux.setContext("#XX_Vendor_Default", order.getC_BPartner_ID());
		ctx_aux.setContext("#XX_Depart_Default",order.getXX_VMR_DEPARTMENT_ID());
		ctx_aux.setContext("#XX_Line_Default",line);
    	ctx_aux.setContext("#XX_Section_Default",section);
    	ctx_aux.setContext("#XX_PiecesPurchase_Default",conversion);
    	ctx_aux.setContext("#XX_PiecesSale_Default",conversion);
    	ctx_aux.setContext("#XX_PurchaseUnit_Default", purchase);
    	ctx_aux.setContext("#XX_SaleUnit_Default", purchase);
    	ctx_aux.setContext("#XX_FromProcess_Default", "Y"); 	
    	    	   	
		AWindow window_vendorProdRef = new AWindow();
		Query query = Query.getNoRecordQuery("XX_VMR_VendorProdRef", true);
		String wind = Env.getCtx().getContext("#XX_L_W_VENDORPRODUCT_ID");
		Integer win = Integer.parseInt(wind);
		window_vendorProdRef.initWindow(win, query);
		AEnv.showCenterScreen(window_vendorProdRef);
		
		// Obtenemos el GridController para setear la variable m_changed=true
    	JRootPane jRootPane  = ((JRootPane)window_vendorProdRef.getComponent(0));
    	JLayeredPane jLayeredPane = (JLayeredPane)jRootPane.getComponent(1);
    	JPanel jPanel = (JPanel)jLayeredPane.getComponent(0);
    	APanel aPanel = (APanel)jPanel.getComponent(0);
    	VTabbedPane vTabbedPane = (VTabbedPane)aPanel.getComponent(0);
    	GridController gridController = (GridController)vTabbedPane.getComponent(0);
    	GridTable mTable = gridController.getMTab().getTableModel();
		mTable.setChanged(true);    
		
		MVMRVendorProdRef.loadMInOutLine(inOutLineID, ctx_aux);
		MVMRVendorProdRef.loadMInvoiceLine(invoiceLineID, ctx_aux);
		
		//Borro las variables de sesion creadas
		ctx_aux.remove("#XX_Vendor_Default");
		ctx_aux.remove("#XX_Depart_Default");
		ctx_aux.remove("#XX_Line_Default");
    	ctx_aux.remove("#XX_Section_Default");
    	ctx_aux.remove("#XX_PiecesPurchase_Default");
    	ctx_aux.remove("#XX_PiecesSale_Default");
    	ctx_aux.remove("#XX_PurchaseUnit_Default");
    	ctx_aux.remove("#XX_SaleUnit_Default");
    	ctx_aux.remove("#XX_FromProcess_Default");	
    	
	}// fin cmd_newVendorProdRef
	
	private void cmd_Save()
	{		
		if((xTable.getRowCount()!=0))
		{
			int row = xTable.getSelectedRow();
			
			if(row!=-1) // Si tengo seleccionada alguna lines en la tabla producto
			{	
				if(ADialog.ask(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "SaveProduct?")))
				{	
					KeyNamePair prodKey;
					if(invoiceType.equalsIgnoreCase("I"))
						prodKey = (KeyNamePair)xTable.getValueAt(row, 2);
					else
						prodKey = (KeyNamePair)xTable.getValueAt(row, 1);
					
					Integer count = 0;
					
					MInOutLine inOutLine = null;
					MInOut inOut = null;
					
					if(inOutLineID != 0)
					{
						inOutLine = new MInOutLine(Env.getCtx(),inOutLineID,null);
						inOut = new MInOut(Env.getCtx(),inOutLine.getM_InOut_ID(),null);
						count = getMInOutLineCount(prodKey.getKey(),inOut.get_ID());
					}
					
					if(invoiceLineID != 0)
					{
						MInvoiceLine invoiceLine = new MInvoiceLine(Env.getCtx(),invoiceLineID,null);
						MInvoice invoice = new MInvoice(Env.getCtx(),invoiceLine.getC_Invoice_ID(),null);
						count = getMInvoiceLineCount(prodKey.getKey(),invoice.get_ID());
					}
					
					if(count==0){
						
						//Obtengo la cantidad de productos no solicitados hasta ahora
						int countPrevious = getUnsolicitedProductCount();
						
						/* 
						 * Si el producto solo tiene una caracteristica diferente, no hace falta validar (JTrias)
						 */
						if((inOutLineID != 0) && onlyDiffers1Characteristic(prodKey.getKey(), inOut)){
					
							if(inOutLineID != 0)
							{							
								inOutLine.setM_Locator_ID(findLocator(inOut));
								inOutLine.setM_Product_ID(prodKey.getKey());
								inOutLine.save();
								dispose();
								//Fin JTrias
							}
							
						}else{
						
							// Agrego el nuevo producto no solicitado
							MVLOUnsolicitedProduct unsolicitedProduct = new MVLOUnsolicitedProduct (Env.getCtx(), 0, null);
							unsolicitedProduct.setC_Order_ID(order.get_ID());
							unsolicitedProduct.setM_InOutLine_ID(inOutLineID);
							unsolicitedProduct.setC_InvoiceLine_ID(invoiceLineID);
							unsolicitedProduct.setM_Product_ID(prodKey.getKey());				
							unsolicitedProduct.save();
							
							//Obtengo la cantidad de productos no solicitados finalmente
							int countFinal = getUnsolicitedProductCount();
							
							if(countPrevious<countFinal){ // Si se agregó el producto, adjunto su imagen
								
								if(attachImage(unsolicitedProduct.get_ID())){
								
									ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "RecordSaved"));
									
									//Envio correo al Jefe de Categoria y al Comprador
									if(invoiceType.equals("I")){
										sendMailToCategoryManager();
										sendMailToBuyer();
									}
									
									try
									{
										//Env.getCtx().setContext("#XX_TypeAlert",1);
										Env.getCtx().setContext("#XX_TypeAlertVP","VP");
										Env.getCtx().setContext("#XX_UnsolProdCT",unsolicitedProduct.get_ID());
										
										MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID"), unsolicitedProduct.get_ID()); 
										mpi.save();
										
										ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
										pi.setRecord_ID(mpi.getRecord_ID());
										pi.setAD_PInstance_ID(mpi.get_ID());
										pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
										pi.setClassName(""); 
										pi.setTitle("Validate Product"); 
										
										ProcessCtl pc = new ProcessCtl(null ,pi,null); 
										pc.start();
									}
									catch(Exception e)
									{
										log.log(Level.SEVERE,e.getMessage());
									}
									
									if(inOutLineID != 0)
									{
										/* Producto no solicitado (desde el Chequeo)
										 * JTrias
										 */
										inOutLine.setXX_CodeToValidate(true);
										inOutLine.setM_Locator_ID(Env.getCtx().getContextAsInt("#XX_L_TOVALIDATELOCATOR_ID"));
										inOutLine.setM_Product_ID(prodKey.getKey());
										inOutLine.save();
										//Fin JTrias
									}
									
									if(invoiceLineID != 0)
									{
										/* Producto no solicitado (desde Facturacion)
										 * Patricia Ayuso
										 */
										MInvoiceLine invoiceLine = new MInvoiceLine(Env.getCtx(), invoiceLineID, null);
										invoiceLine.setM_Product_ID(prodKey.getKey());
										
										if(!invoiceType.equals("I"))
											invoiceLine.setXX_Product_ID(prodKey.getKey());
										
										invoiceLine.save();
										//Fin Patricia Ayuso 
									}
									
									xScrollPane.getViewport().remove(xTable);
									xTable = new MiniTable();
									xScrollPane.getViewport().add(xTable, null);
									
									productRefCombo.setEnabled(true);
									product.setEnabled(true);
									bSearch.setEnabled(true);
									bNewVendorProdRef.setEnabled(true);
									
									loadInfo();
									
									dispose();
									if(fromVendorProdRef != 0){
										ADialog.info(m_WindowNo,m_frame,Msg.getMsg(Env.getCtx(),"MustRefresh"));
									}
								}
								else
									unsolicitedProduct.delete(true);
							}
							else{
								ADialog.error(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "NoRecordCreated"));
							}
							
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
	private static void tableInit (String invoiceType)
	{
		m_sql = new StringBuffer ();
	
		if(invoiceType.equalsIgnoreCase("I")){
			
			m_sql.append("SELECT vpr.Value, vpr.XX_VMR_VendorProdRef_ID, p.Value, p.Name, p.M_Product_ID, c.Name, "
					   + "c.C_BPartner_ID, d.Name, d.XX_VMR_Department_ID, l.Name, l.XX_VMR_Line_ID, s.Name, " 
					   + "s.XX_VMR_Section_ID, m.Description, m.M_AttributeSetInstance_ID, ca.Name, " 
					   + "ca.XX_VMR_LongCharacteristic_ID "
					   + "FROM XX_VMR_VendorProdRef vpr, M_Product p, C_BPartner c, XX_VMR_Department d, "
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
		}		
		else{
			
			m_sql.append("SELECT vpr.Value, vpr.Name, vpr.M_Product_ID "
                      + "FROM M_Product vpr "
					  + "WHERE vpr.ISACTIVE='Y' ");
			
			if(product.getValue() != null){
				m_sql.append(" AND ").append("vpr.M_Product_ID=").append(product.getValue());
			}
		}
		
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
	 * Obtengo la cantidad de lineas en el chequeo segun el producto seleccionado
	*/
	private Integer getMInOutLineCount(Integer M_Product_ID, Integer M_InOut_ID)
	{
		Integer count=0;
		String sql = "SELECT COUNT(*)" 
			       + "FROM M_InOutLine WHERE M_InOut_ID="+M_InOut_ID+" " 
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
	 * Obtengo la cantidad de lineas en la factura segun el producto seleccionado 
	*/
	private Integer getMInvoiceLineCount(Integer M_Product_ID, Integer C_Invoice_ID)
	{
		Integer count=0;
		String sql = "SELECT COUNT(*)" 
			       + "FROM C_InvoiceLine WHERE C_Invoice_ID="+C_Invoice_ID+" " 
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
	 *	Obtengo el ID de la linea 99 segun el departamento de la O/C 
	 */
	private Integer getLine(Integer depart){
		
		Integer line=0;
		String SQL = "SELECT XX_VMR_Line_ID FROM XX_VMR_Line "
				   + "WHERE XX_VMR_Department_ID="+depart
				   + " AND Value=99";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				line = rs.getInt("XX_VMR_Line_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return line;
	}
	
	/*
	 *	Obtengo el ID de la seccion 99 segun la linea de la O/C 
	 */
	private Integer getSection(Integer line){
		
		Integer section=0;
		String SQL = "SELECT XX_VMR_Section_ID FROM XX_VMR_Section "
				   + "WHERE XX_VMR_Line_ID="+line
				   + " AND Value=99";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				section = rs.getInt("XX_VMR_Section_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return section;
	}
	
	/*
	 *	Obtengo el ID donde UnitConversion es igual a 1
	 */
	private Integer getUnitConversion(){
		
		Integer conversion=0;
		String SQL = "SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion "
				   + "WHERE XX_UnitConversion=1";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				conversion = rs.getInt("XX_VMR_UnitConversion_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return conversion;
	}
	
	/*
	 *	Obtengo el ID donde UnitPurchase es igual a PZ
	 */
	private Integer getUnitPurchase(){
		
		Integer purchase=0;
		String SQL = "SELECT XX_VMR_UnitPurchase_ID FROM XX_VMR_UnitPurchase "
				   + "WHERE Value='PZ'";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				purchase = rs.getInt("XX_VMR_UnitPurchase_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return purchase;
	}
	
	/*
	 * Adjunto imagen en el producto no solicitado que estoy agregando
	 */
	private boolean attachImage(Integer unsolicitedProdID)
	{ 
		if(!invoiceType.equals("I"))
			return true;
		
		attachments = null;
		
		Integer wind = new Integer(window);
		
		MVLOUnsolicitedProduct unsolicitedProd = new MVLOUnsolicitedProduct(Env.getCtx(), unsolicitedProdID,null);
				
		//Pregunto cuantos attachments tengo
		long countPrevious = getAttachmentsSize(unsolicitedProd.get_ID(), wind);
			
		//Agrego el Attachment
		new Attachment(m_frame, m_WindowNo, getAD_AttachmentID(unsolicitedProd.get_ID()), wind, unsolicitedProd.get_ID(), null);
			
		//Pregunto cuantos attachments tengo ahora
		long countFinal = getAttachmentsSize(unsolicitedProd.get_ID(), wind);
			
		if(countPrevious<countFinal){ // Si adjuntó la imagen del producto no solicitado
				
			ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "ImageSave"));			
				
		}
		else{
			ADialog.warn(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(),"AttachImage"));
			//attachImage(unsolicitedProdID);
			if (invoiceLineID == 0)
				return false;
		}
		return true;
	}
	
	/**
	 *	Get Attachment_ID for current record.
	 *	@return ID or 0, if not found
	 */
	public int getAD_AttachmentID(Integer unsolicitedProdID)
	{
		if (attachments == null)
			loadAttachments();
		if (attachments.isEmpty())
			return 0;
		//
		Integer key = unsolicitedProdID;
		Integer value = attachments.get(key);
		if (value == null)
			return 0;
		else
			return value.intValue();
	}	//	getAttachmentID
	
	/**************************************************************************
	 *	Load Attachments for this table
	 */
	public void loadAttachments()
	{
		Integer wind = new Integer(window);
		String SQL = "SELECT AD_Attachment_ID, Record_ID FROM AD_Attachment "
			+ "WHERE AD_Table_ID=?";
		try
		{
			if (attachments == null)
				attachments = new HashMap<Integer,Integer>();
			else
				attachments.clear();
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			pstmt.setInt(1, wind);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				Integer key = Integer.valueOf(rs.getInt(2));
				Integer value = Integer.valueOf(rs.getInt(1));
				attachments.put(key, value);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "loadAttachments", e);
		}
		log.config("#" + attachments.size());
	}	//	loadAttachment
	
	/*
	 *	Count the Attachments for this table and record
	 */
	private long getAttachmentsSize(Integer unsolicitedProdID, Integer wind){
		
		String SQL = "SELECT BINARYDATA FROM AD_Attachment "
			+ "WHERE AD_Table_ID="+wind+" "
			+ "AND Record_ID="+unsolicitedProdID;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				Blob file = rs.getBlob("BINARYDATA");
			
				return(file.length());
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return 0;
	}
	
	/*	
	 * Envia correo al Jefe de Categoria
	 */
	private void sendMailToCategoryManager()
	{		
		int prodRow = xTable.getSelectedRow();
		KeyNamePair prodKey = (KeyNamePair)xTable.getValueAt(prodRow, 2); 
		
		String sql = "SELECT XX_CATEGORYMANAGER_ID " +
		"FROM XX_VMR_CATEGORY " +
		"WHERE XX_VMR_CATEGORY_ID=" + order.getXX_Category_ID();		

		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				Integer catManager = rs.getInt("XX_CATEGORYMANAGER_ID");
				
				Integer UserAuxID = getAD_User_ID(catManager);
				String Mensaje = Msg.getMsg(Env.getCtx(), "XX_SendMailCMUnsolProd", new String[] {"'"+prodKey.getName()+"'", "'"+order.getDocumentNo()+"'"});			
				Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_UnsolicitedProduct_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, null);
				//Utilities f = new Utilities(Env.getCtx(), null,Env.getCtx().getContextAsInt("#XX_L_MT_UNSOLICITEDPRODUCT_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, 1019246, null);
				f.ejecutarMail();  
				f = null;

			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
	}
	
	/*
	 * Envia correo al Comprador
	*/
	private void sendMailToBuyer()
	{
		int prodRow = xTable.getSelectedRow();
		KeyNamePair prodKey = (KeyNamePair)xTable.getValueAt(prodRow, 2);
		
		String sql = "SELECT XX_UserBuyer_ID " +
		"FROM XX_VMR_Department " +
		"WHERE XX_VMR_DEPARTMENT_ID=" + order.getXX_VMR_DEPARTMENT_ID();

		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				Integer buyer = rs.getInt("XX_UserBuyer_ID");
				
				Integer UserAuxID = getAD_User_ID(buyer);
							
				String Mensaje = Msg.getMsg(Env.getCtx(), "XX_SendMailCMUnsolProd", new String[] {"'"+prodKey.getName()+"'", "'"+order.getDocumentNo()+"'"});
				Utilities f = new Utilities(Env.getCtx(), null,Env.getCtx().getContextAsInt("#XX_L_MT_UnsolicitedProduct_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, null);
				//Utilities f = new Utilities(Env.getCtx(), null,Env.getCtx().getContextAsInt("#XX_L_MT_UNSOLICITEDPRODUCT_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, 1019246, null);
				f.ejecutarMail();
				f = null;

			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
	}
	
	/*
	 * Obtiene el AD_USER_ID del CBParter Indicado
	 */
	private Integer getAD_User_ID(Integer CBPartner)
	{
		Integer AD_User_ID=0;
		
		String SQL = "Select AD_USER_ID FROM AD_USER " +
					 "WHERE C_BPartner_ID IN "+CBPartner;
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				AD_User_ID = rs.getInt("AD_USER_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return AD_User_ID;
	}
	
	/**
	 *  cargar el producto al cual está relacionado la referencia
	 */
	public static void loadMProduct(MProduct product_aux, Ctx ctx)
	{
		productReference=product_aux;
		ctx_aux = ctx;
		
	}	//	product
	
	@Override
	public void tableChanged(TableModelEvent e) 
	{
		
	}

	/*
	 * JTrias
	 */
	private int findLocator(MInOut inOut) 
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//Busco el locator para asignarlo a las lineas de recepcion
		String SQL = "Select M_Locator_ID FROM M_LOCATOR " +
					 "WHERE M_WAREHOUSE_ID="+inOut.getM_Warehouse_ID()+" AND ISDEFAULT='Y' AND ISACTIVE='Y' " +
				     "AND AD_CLIENT_ID IN (0,"+inOut.getAD_Client_ID()+")";
		
		try
		{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
	
			while(rs.next()){
				return rs.getInt("M_Locator_ID");
			}
		
		}
		catch (Exception e) {
			log.log(Level.SEVERE, SQL, e.getMessage());
		}finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return 0;
	}
	
	/*
	 * JTrias
	 */
	private boolean onlyDiffers1Characteristic(int productID, MInOut inOut){
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		MProduct product = new MProduct( Env.getCtx(), productID, null);
		Vector<Integer> attributeSI = productsWithSameAttSet(inOut, product.getM_AttributeSet_ID());
		
		if(attributeSI.size()==0){
			return false;
		}
		
		//Cuento cuantos atributos tiene la intancia de atributos
		String SQL = "(SELECT COUNT(*) AS QTY " + 
	 	 			 "FROM M_ATTRIBUTEINSTANCE " +
	 	 			 "WHERE M_ATTRIBUTESETINSTANCE_ID= " + product.getM_AttributeSetInstance_ID() + 
	 	 			 " AND M_ATTRIBUTEVALUE_ID IS NOT NULL)";
		
		int count = 0;
		try
		{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				count = rs.getInt("QTY");
			}
			
			rs.close();
			pstmt.close();
		}
		catch (Exception e) {
			log.log(Level.SEVERE, SQL, e.getMessage());
		}
		
		//Luego verifico cuantas tienen en comun con las de los otros productos
		for(int i=0; i<attributeSI.size(); i++){
			
			SQL = "SELECT COUNT(*) AS QTY " +
						 "FROM M_ATTRIBUTEINSTANCE " +
						 "WHERE M_ATTRIBUTESETINSTANCE_ID=" + attributeSI.get(i) +
						 " AND M_ATTRIBUTEVALUE_ID IN " +
			    		 	"(SELECT M_ATTRIBUTEVALUE_ID " + 
			     		 	 "FROM M_ATTRIBUTEINSTANCE " +
			     			 "WHERE M_ATTRIBUTESETINSTANCE_ID= " + product.getM_AttributeSetInstance_ID() + 
			     			 " AND M_ATTRIBUTEVALUE_ID IS NOT NULL)";
			
			try
			{
				pstmt = DB.prepareStatement(SQL, null); 
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					
					/*if(rs.getInt("QTY")==(count-1)){
						return true;
					}*/

					if(count==2 && rs.getInt("QTY")==1){
						return true;
					}
				}

			}
			catch (Exception e) {
				log.log(Level.SEVERE, SQL, e.getMessage());
			}finally{
				
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}
		
		return false;
	}
	
	/*
	 * JTrias
	 */
	private Vector<Integer> productsWithSameAttSet(MInOut inOut, int attributeSetID){
	
		//agarro los attribute set instances de los productos con el mismo attribute set para comparar luego
		Vector<Integer> products = new Vector<Integer>();
		
		String SQL = "SELECT P.M_ATTRIBUTESETINSTANCE_ID FROM M_INOUTLINE IOL, M_PRODUCT P " +
		 "WHERE P.M_ATTRIBUTESET_ID = " + attributeSetID +
		 " AND IOL.M_INOUT_ID = "+ inOut.get_ID() +
	     " AND P.M_ATTRIBUTESETINSTANCE_ID IS NOT NULL" +
	     " AND IOL.M_PRODUCT_ID = P.M_PRODUCT_ID" +
	     " AND IOL.AD_CLIENT_ID IN (0,"+ Env.getCtx().getAD_Client_ID() +")";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				products.add(rs.getInt("M_ATTRIBUTESETINSTANCE_ID"));
			}
			
			rs.close();
			pstmt.close();
		}
		catch (Exception e) {
			log.log(Level.SEVERE, SQL, e.getMessage());
		}
		
		return products;
	}
	
	
}//End form
