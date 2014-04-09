package compiere.model.cds.forms;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
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
import org.compiere.minigrid.*;
import org.compiere.model.GridTable;
import org.compiere.model.MPInstance;
import org.compiere.model.MRole;
import org.compiere.plaf.*;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.*;
import org.compiere.util.*;

import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRCriticalTaskForClose;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.X_Ref_M_Product_ProductType;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_Line;
import compiere.model.cds.X_XX_VMR_PO_LineRefProv;
import compiere.model.cds.X_XX_VMR_ReferenceMatrix;
import compiere.model.cds.X_XX_VMR_Section;

/**
 *  Convert Vendors Reference to BECO products
 *
 *  @author     José Trías
 *  @version    
 */
public class XX_AssociateReference_Form extends CPanel
	
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
	static CLogger 			log = CLogger.getCLogger(XX_AssociateReference_Form.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();

	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";

	private static X_XX_VMR_PO_LineRefProv LineRefProv = null;
	
	private CLabel LineRefProv_Label = new CLabel();
	static Ctx ctx_aux = new Ctx();
	static Integer Product_ID;
	static Integer associatedReference_ID;
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CButton bNewProduct = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CButton bDisassociate = new CButton();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xProductScrollPane = new JScrollPane();
	private TitledBorder xProductBorder = new TitledBorder(Msg.translate(Env.getCtx(), "SuggestedProducts"));
	private MiniTable xProductTable = new MiniTable();
	private JScrollPane xAssociateScrollPane = new JScrollPane();
	private TitledBorder xAssociateBorder = new TitledBorder(Msg.translate(Env.getCtx(), "AssociatedProduct"));
	private MiniTable xAssociateTable = new MiniTable();
	private CPanel xPanel = new CPanel();
	
	private static int tableInit_option;

	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);

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
		
		//LineRefProv_Label.setText(Msg.getMsg(Env.getCtx(), "RecordNo",new String[] {""+LineRefProv.getXX_VMR_PO_LineRefProv_ID()}));
		LineRefProv_Label.setText("");
		southPanel.setLayout(southLayout);
		bNewProduct.setText(Msg.translate(Env.getCtx(), "NewProduct"));
		bNewProduct.setEnabled(true);
		bDisassociate.setText(Msg.translate(Env.getCtx(), "Disassociate"));
		bDisassociate.setEnabled(false);
		centerPanel.setLayout(centerLayout);
		xProductScrollPane.setBorder(xProductBorder);
		xProductScrollPane.setPreferredSize(new Dimension(450, 150));
		xAssociateScrollPane.setBorder(xAssociateBorder);
		xAssociateScrollPane.setPreferredSize(new Dimension(450, 58));
		
		xPanel.setLayout(xLayout);
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		northPanel.add(LineRefProv_Label,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xProductScrollPane,  BorderLayout.NORTH);
		xProductScrollPane.getViewport().add(xProductTable, null);
		centerPanel.add(xAssociateScrollPane,  BorderLayout.SOUTH);
		centerPanel.add(xPanel, BorderLayout.CENTER);
		xAssociateScrollPane.getViewport().add(xAssociateTable, null);
		
		southPanel.add(bDisassociate,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
		southPanel.add(bNewProduct,   new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
		
		
		
		}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		
		ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"),   ".", KeyNamePair.class),            //  1
			new ColumnInfo(Msg.translate(Env.getCtx(), "Name"),   ".", String.class)             //  2
		};

		xProductTable.prepareTable(layout, "", "", false, "");
		xAssociateTable.prepareTable(layout, "", "", true, "");
		
		xAssociateTable.setAutoResizeMode(3);
		xProductTable.setAutoResizeMode(3);
		
		//  Visual
		CompiereColor.setBackground (this);

		//  Listener
		xProductTable.getSelectionModel().addListSelectionListener(this);
		xAssociateTable.getModel().addTableModelListener(this);
		bDisassociate.addActionListener(this);
		bNewProduct.addActionListener(this);
		
		//pregunto si hay productos asociados para ver si cargo las tablas
		boolean ready=verify();
		
		if(ready==false){
			
			tableInit_option=0;
			tableInit();
			tableLoad (xProductTable);
			
		}else{
			tableInit_option=1;
			tableInit();
			tableLoad (xAssociateTable);
		
			bNewProduct.setEnabled(false);
			bDisassociate.setEnabled(true);
		}
		
		
		xProductTable.addMouseListener(new MouseAdapter() 
		{
		    public void mouseClicked(MouseEvent e){
		        if (e.getClickCount() == 2){
		        	
			    	int rowP = xProductTable.getSelectedRow();
					
			    	if(!(rowP==-1)){
			    		cmd_associate();
			    	}
		        }
		    }
		        
		});
		
		//  Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);
		
		MOrder order = new MOrder(Env.getCtx(),LineRefProv.getC_Order_ID(),null);
		
		boolean block=false;
		String oS = order.getXX_OrderStatus();
		String compS = order.getDocStatus();
		if(oS.equals("AN") || compS.equals("CO")){
			block=true;
		}
		
		if(order.isXX_OrderReadyStatus() && order.getXX_OrderType().equalsIgnoreCase("Nacional"))
			block = true;
		
		if (order.getXX_OrderType().equalsIgnoreCase("Importada") && order.get_ValueAsInt("XX_ImportingCompany_ID") != 0){
			
			if(order.get_ValueAsInt("XX_ImportingCompany_ID") != Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID")){
				block = true;
			}
		}
		
		if(order.getXX_StoreDistribution().equals("Y") || block){
			xProductTable.setEnabled(false);
			xAssociateTable.setEnabled(false);
			bNewProduct.setEnabled(false);
			bDisassociate.setEnabled(false);
		}
		
	}   //  dynInit

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
		
		MOrder order = new MOrder(Env.getCtx(),LineRefProv.getC_Order_ID(),null);
		String oS = order.getXX_OrderStatus();
		String compS = order.getDocStatus();
		if(!oS.equals("AN") && !compS.equals("CO")){
			
			if(isInMatrix()){
				
				fullAssociated();
				
			}else{
				String SQL10 = "UPDATE XX_VMR_PO_LineRefProv "
				    +" SET XX_ReferenceIsAssociated='N'"
				    + " WHERE XX_VMR_PO_LineRefProv_ID="+LineRefProv.getXX_VMR_PO_LineRefProv_ID();
					
					DB.executeUpdate(null, SQL10);
//				LineRefProv.setXX_ReferenceIsAssociated(false);
//				LineRefProv.save();
			}
		}
		
		//pelle
		
				
		String SQL = ("SELECT A.XX_REFERENCEISASSOCIATED AS ASOCIATE " +
				"FROM XX_VMR_PO_LINEREFPROV A , C_ORDER B " +
				"WHERE A.C_ORDER_ID = B.C_ORDER_ID " +
				"AND A.C_ORDER_ID = '"+LineRefProv.getC_Order_ID()+"' ");
		
		try
		{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();
		    
		    BigDecimal cifTotal = new BigDecimal(0);
		    Boolean check = true;
		    Integer maritimo = 0;
		    Integer aereo = 0;
		    Integer terrestre = 0;
		    BigDecimal montoBs = new BigDecimal(0);
		    BigDecimal rate = new BigDecimal(0);
		    BigDecimal Seguro = new BigDecimal(0);
		    BigDecimal perfleteInt = new BigDecimal(0);
		    BigDecimal fleteInternac = new BigDecimal(0);
		    
		    
		    Integer productid = 0;
			Integer poline = 0;
			BigDecimal costoitem = new BigDecimal(0);
			BigDecimal porcentitem = new BigDecimal(0);
			BigDecimal cifuni = new BigDecimal(0);
		    BigDecimal percentarancel = new BigDecimal(0);
			BigDecimal arancelitem = new BigDecimal(0);
			BigDecimal rateseniat = new BigDecimal(0);
			BigDecimal montoseniat = new BigDecimal(0);
			BigDecimal ratetesoreria = new BigDecimal(0);
			BigDecimal montotesoreria = new BigDecimal(0);
			BigDecimal tasaaduana = new BigDecimal(0);
			BigDecimal part1 = new BigDecimal(0);
			BigDecimal part2 = new BigDecimal(0);
			BigDecimal iva = new BigDecimal(0);
			BigDecimal ivaactual = new BigDecimal(0);
			BigDecimal part = new BigDecimal(0);
			BigDecimal montesorerianac = new BigDecimal(0);
			
			
		    
		    while(rs.next())
		    {
		    	if(rs.getString("ASOCIATE").trim().equalsIgnoreCase("N"))
		    	{
		    		check = false;
		    	}
		    }
		    rs.close();
		    pstmt.close();
		    if(check == true)
		    {
		    	
		    	String SQL2 = ("SELECT (A.TOTALLINES * B.MULTIPLYRATE) + A.XX_INTNACESTMEDAMOUNT + A.XX_ESTEEMEDINSURANCEAMOUNT  AS CIFTOTAL " +
						"FROM C_ORDER A, C_CONVERSION_RATE B " +
						"WHERE B.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
						"AND A.C_ORDER_ID = '"+LineRefProv.getC_Order_ID()+"' " +
						"AND A.AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+") " +
						"AND B.AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+")");
				
				PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null); 
				ResultSet rs2 = pstmt2.executeQuery();
				 
				 if (rs2.next())
				 {
					 // Calculo del CIF Total
					 
				    cifTotal = rs2.getBigDecimal("CIFTOTAL");
				 }
				 rs2.close();
				 pstmt2.close();
			
				 
				 // Busco la via de despacho de la O/C
				 String SQL3 = ("SELECT XX_L_DISPATCHROUTE AS AEREO, XX_L_DISPATCHROUTEMAR AS MARITIMO, XX_L_DISPATCHROUTETER AS TERRESTRE " +
				 		"FROM XX_VSI_KEYNAMEINFO ");
				 
				 PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null); 
				 ResultSet rs3 = pstmt3.executeQuery();
				 
				 if(rs3.next())
				 {
					maritimo = rs3.getInt("MARITIMO");
					terrestre = rs3.getInt("TERRESTRE");
					aereo = rs3.getInt("AEREO");
				 }
				 rs3.close();
				 pstmt3.close();
				 
				 
				 String SQL4 = ("SELECT (A.TOTALLINES * B.MULTIPLYRATE) AS MONTO " +
					 		"FROM C_ORDER A, C_CONVERSION_RATE B " +
					 		"WHERE B.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
					 		"AND A.C_ORDER_ID = '"+LineRefProv.getC_Order_ID()+"' ");
					 
				 PreparedStatement pstmt4 = DB.prepareStatement(SQL4, null); 
				 ResultSet rs4 = pstmt4.executeQuery();
				 
				 if(rs4.next())
				 {
					 // Monto de la O/C en Bs
					 montoBs = rs4.getBigDecimal("MONTO");
				 }
				 rs4.close();
				 pstmt4.close();
				 
				 
				 if(order.getXX_VLO_DispatchRoute_ID()== maritimo)
				 {
					 
					 String SQL5 = ("SELECT XX_RATE AS RATE " +
					 		"FROM XX_VLO_DispatchRoute " +
					 		"WHERE XX_VLO_DispatchRoute_ID = '"+maritimo+"' ");
					 
					 PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null); 
					 ResultSet rs5 = pstmt5.executeQuery();
					 
					 if(rs5.next())
					 {
						 rate = rs5.getBigDecimal("RATE").divide(new BigDecimal(100));
						 //(new BigDecimal(100), 2, RoundingMode.HALF_UP);
					
						 // Calculo del Seguro con Via Maritimo 
						 Seguro = montoBs.multiply(rate);
					 }
					 rs5.close();
					 pstmt5.close();
					 
				 }
				 else if(order.getXX_VLO_DispatchRoute_ID()== aereo)
				 {
					 String SQL5 = ("SELECT XX_RATE AS RATE " +
						 		"FROM XX_VLO_DispatchRoute " +
						 		"WHERE XX_VLO_DispatchRoute_ID = '"+aereo+"' ");
						 
						 PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null); 
						 ResultSet rs5 = pstmt5.executeQuery();
						 
						 if(rs5.next())
						 {
							 rate = rs5.getBigDecimal("RATE").divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
							 
							 // Calculo del Seguro con Via aereo 
							 Seguro = montoBs.multiply(rate);							 
						 }
						 rs5.close();
						 pstmt5.close();
				 }
				 else if(order.getXX_VLO_DispatchRoute_ID()== terrestre)
				 {
					 String SQL5 = ("SELECT XX_RATE AS RATE " +
						 		"FROM XX_VLO_DispatchRoute " +
						 		"WHERE XX_VLO_DispatchRoute_ID = '"+terrestre+"' ");
						 
						 PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null); 
						 ResultSet rs5 = pstmt5.executeQuery();
						 
						 if(rs5.next())
						 {
							 rate = rs5.getBigDecimal("RATE").divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
							 
							 // Calculo del Seguro con Via terrestre 
							 Seguro = montoBs.multiply(rate);							 
						 }
						 rs5.close();
						 pstmt5.close();
				 }
				 
				 
				 // Busco el porcentaje del flete internacional segun el proveedor, puerto y pais de la O/C
				 
				 String SQL6 = ("SELECT DISTINCT A.XX_INTERFREESTIMATEPERT AS PERTFLEINTER " +
				 		"FROM XX_VLO_COSTSPERCENT A, C_ORDER B " +
				 		"WHERE B.XX_VLO_ARRIVALPORT_ID = '"+order.getXX_VLO_ArrivalPort_ID()+"' " +
				 		"AND B.C_BPARTNER_ID = '"+order.getC_BPartner_ID()+"' " +
				 		"AND B.C_COUNTRY_ID = '"+order.getC_Country_ID()+"' " +
				 		"AND B.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
				 		"AND B.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
				 		"AND B.C_BPARTNER_ID = A.C_BPARTNER_ID " +
				 		"AND B.C_COUNTRY_ID = A.C_COUNTRY_ID ");
				  
				 
				 PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null); 
				 ResultSet rs6 = pstmt6.executeQuery();
				 
				 if(rs6.next())
				 {
					 // Porcentaje del flete internacional
					 perfleteInt = rs6.getBigDecimal("PERTFLEINTER").divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
					 //Flete Internacional 
					 fleteInternac = montoBs.multiply(perfleteInt);
				 }
				 rs6.close();
				 pstmt6.close();
				 
				 // Busco los productos que tienen asociados las Ref de la O/C
				
				 String SQL7 = ("SELECT C.M_PRODUCT AS PRODUCT, A.XX_VMR_PO_LINEREFPROV_ID AS POLINE " +
						"FROM XX_VMR_PO_LINEREFPROV A, C_ORDER B, XX_VMR_REFERENCEMATRIX C " +
						"WHERE A.XX_VMR_PO_LINEREFPROV_ID = C.XX_VMR_PO_LINEREFPROV_ID " +
						"AND A.C_ORDER_ID = B.C_ORDER_ID " +
						"AND A.C_ORDER_ID = '"+LineRefProv.getC_Order_ID()+"' ");
				 
				PreparedStatement pstmt7 = DB.prepareStatement(SQL7, null); 
				ResultSet rs7 = pstmt7.executeQuery(); 
				
				while(rs7.next())
				{
					// Calculo del Porcentaje de cada item = costo de cada item Bs / Monto O/C Bs
					productid = rs7.getInt("PRODUCT");
					poline = rs7.getInt("POLINE");

					// Costo de cada item Bs
					
					/*String SQL8 = ("SELECT (A.XX_UNITPURCHASEPRICE / B.XX_UNITCONVERSION) AS COSTO " +
							"FROM XX_VMR_PO_LINEREFPROV A, XX_VMR_UNITCONVERSION B, XX_VMR_REFERENCEMATRIX C, M_PRODUCT D " +
							"WHERE A.XX_VMR_UNITCONVERSION_ID = B.XX_VMR_UNITCONVERSION_ID " +
							"AND A.XX_VMR_PO_LINEREFPROV_ID = C.XX_VMR_PO_LINEREFPROV_ID " +
							"AND C.M_PRODUCT = D.M_PRODUCT_ID " +
							"AND C.M_PRODUCT = '"+productid+"' " +
							"AND A.XX_VMR_PO_LINEREFPROV_ID = '"+poline+"' ");*/
					
					String SQL8 = ("SELECT (A.LINENETAMT * E.MULTIPLYRATE) AS COSTO " +
							"FROM XX_VMR_PO_LINEREFPROV A, XX_VMR_REFERENCEMATRIX C, M_PRODUCT D, C_CONVERSION_RATE E, C_ORDER F  " +
							"WHERE A.XX_VMR_PO_LINEREFPROV_ID = C.XX_VMR_PO_LINEREFPROV_ID " +
							"AND C.M_PRODUCT = D.M_PRODUCT_ID " +
							"AND E.C_CONVERSION_RATE_ID = F.XX_CONVERSIONRATE_ID " +
							"AND A.C_ORDER_ID = F.C_ORDER_ID " +
							"AND C.M_PRODUCT = '"+productid+"' " +
							"AND A.XX_VMR_PO_LINEREFPROV_ID = '"+poline+"' ");
					
					
					PreparedStatement pstmt8 = DB.prepareStatement(SQL8, null); 
					ResultSet rs8 = pstmt8.executeQuery(); 
					
					
					if(rs8.next())
					{
						costoitem = rs8.getBigDecimal("COSTO");
											
						//Porcentaje de cada item = costo de cada item Bs / Monto O/C Bs
						porcentitem = (costoitem.divide(montoBs, 8, RoundingMode.HALF_UP));
						
						//CIF Unitario = Porcentaje de cada Item * CIF total
						cifuni = porcentitem.multiply(cifTotal);
						
						// Busco Porcentaje Arancelario
						String SQL9 = ("SELECT (D.XX_PERCENTAGETARIFF/100) AS PERARANCEL " +
								"FROM XX_VMR_PO_LINEREFPROV A, C_ORDER B, XX_VMR_REFERENCEMATRIX C, M_PRODUCT D " +
								"WHERE A.XX_VMR_PO_LINEREFPROV_ID = C.XX_VMR_PO_LINEREFPROV_ID " +
								"AND A.C_ORDER_ID = B.C_ORDER_ID " +
								"AND C.M_PRODUCT = D.M_PRODUCT_ID " +
								"AND D.M_PRODUCT_ID = '"+productid+"' " +
								"AND A.C_ORDER_ID = '"+LineRefProv.getC_Order_ID()+"' ");
						
						PreparedStatement pstmt9 = DB.prepareStatement(SQL9, null); 
						ResultSet rs9 = pstmt9.executeQuery();
						
						
						if(rs9.next())
						{
							// Porcentaje Arancelario
							percentarancel = rs9.getBigDecimal("PERARANCEL");
							
							//Arancel de cada item = CIF unitario * Porcentaje Arancelario
							arancelitem = cifuni.multiply(percentarancel).add(arancelitem);
						}
						rs9.close();
						pstmt9.close();

					}
					rs8.close();
					pstmt8.close();

					// cif total(Creo que no hace falta) t arancel item se usan abajo
				}// end While rs7
				rs7.close();
				pstmt7.close();
				
				String SQL9 = ("SELECT XX_RATE/100 AS RATESENIAT FROM XX_VLO_IMPORTRATE WHERE NAME = 'Tasa de Servicio de aduanas SENIAT' " +
				 		"And AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+")");
				    
				 PreparedStatement pstmt9 = DB.prepareStatement(SQL9, null); 
				 ResultSet rs9 = pstmt9.executeQuery();
				    
				 if (rs9.next())
				 {
				  	rateseniat = rs9.getBigDecimal("RATESENIAT");
				  	montoseniat = arancelitem.multiply(rateseniat);
				  	
				  	
				  	String SQL10 = ("SELECT XX_RATE/100 AS RATETESORERIA FROM XX_VLO_IMPORTRATE WHERE NAME = 'Tasa de Servicio Aduana Tesorería' " +
					 		"And AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+")");
					    
					 PreparedStatement pstmt10 = DB.prepareStatement(SQL10, null); 
					 ResultSet rs10 = pstmt10.executeQuery();
						    
					 if (rs10.next())
					 {
					  	ratetesoreria = rs10.getBigDecimal("RATETESORERIA");
					  	montotesoreria = arancelitem.multiply(ratetesoreria);
					  	
					  	//Monto Tasa aduanera =monto tasa seniat + monto tasa tesoreria
						 tasaaduana = montoseniat.add(montotesoreria);
					 }
					 rs10.close();
					 pstmt10.close();
	
				 }
				 rs9.close();
				 pstmt9.close();
	
				 //Calculo del IVA = (CIF total + Impuesto de importación + tasa aduanera) *%iva actual
				 
				 String SQL11 = ("SELECT MAX (VALIDFROM) AS FECHA, A.RATE/100 AS IVACT " +
				 		"FROM C_TAX A, C_TAXCATEGORY B " +
				 		"WHERE A.C_TAXCATEGORY_ID = B.C_TAXCATEGORY_ID " +
				 		"AND B.DESCRIPTION = 'Impuesto al Valor Agregado' " +
				 		"AND A.C_TAXCATEGORY_ID = B.C_TAXCATEGORY_ID " +
				 		"GROUP BY A.RATE ");
				 
				 PreparedStatement pstmt11 = DB.prepareStatement(SQL11, null); 
				 ResultSet rs11 = pstmt11.executeQuery();
				 
				 if(rs11.next())
				 {
					 part1 = cifTotal.add(arancelitem); 
					 part2 = part1.add(tasaaduana);
					 iva = part2.multiply(rs11.getBigDecimal("IVACT"));
				 }
				 rs11.close();
				 pstmt11.close();
				 
				 //Monto tesorería Nacional = IVA + impuesto de importación + monto tasa tesoreria
				 
				 part = iva.add(arancelitem);
				 montesorerianac = part.add(montotesoreria);
				 
				 // redondeo los BigDecimal
				 montesorerianac = montesorerianac.setScale(2,BigDecimal.ROUND_UP);
				 montoseniat = montoseniat.setScale(2,BigDecimal.ROUND_UP);
				 
				 String sql = "UPDATE C_Order po"											
						+ " SET XX_NatTreasuryEstAmount=" + montesorerianac
						+ "    , XX_SENIATESTEEMEDAMUNT=" + montoseniat
						+ " WHERE po.C_Order_ID=" + LineRefProv.getC_Order_ID();						
					DB.executeUpdate(null, sql);
 
		    } // end if Check
		    else
		    {
		    	// alguna referencia no tiene producto asociado
				 String sql = "UPDATE C_Order po"											
						+ " SET XX_NatTreasuryEstAmount=" + 0
						+ "    , XX_SENIATESTEEMEDAMUNT=" + 0
						+ " WHERE po.C_Order_ID=" + LineRefProv.getC_Order_ID();						
					DB.executeUpdate(null, sql);
		    }
		    
		    
		    
		} // end try
		catch (Exception e) {
			
		}
		
		
		
	}	//	dispose

	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == bDisassociate)
			cmd_disassociate();
		else if (e.getSource() == bNewProduct)
			cmd_newProduct();
	}   //  actionPerformed

	/**
	 *  Associate Button Pressed
	 */
	private void cmd_associate()
	{
		int productRow = xProductTable.getSelectedRow();
		log.config("Row=" + productRow);
		
		KeyNamePair product = (KeyNamePair)xProductTable.getValueAt(productRow, 0);

		doInsert(product.getKey(),0);
		
		//  ** Load Table **
		tableInit_option=1;
		tableInit();
		tableLoad (xAssociateTable);
		
	//  ** Load Table **
		tableInit_option=2;
		tableInit();
		tableLoad (xProductTable);
		
		bDisassociate.setEnabled(true);
		bNewProduct.setEnabled(false);
		
		//Coloco la referencia como asociada
		//MVMRVendorProdRef vendorProdRef = new MVMRVendorProdRef(Env.getCtx(), LineRefProv.getXX_VMR_VendorProdRef_ID(), null);
		//vendorProdRef.setXX_IsAssociated(true);
		//vendorProdRef.save();
		
	}   //  cmd_associate
	
	/**
	 *  Disassociate Button Pressed
	 */
	private void cmd_disassociate()
	{
		if(verify()){
			//desasocio el producto
			X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix(Env.getCtx(),associatedReference_ID,null);
			matrix.delete(true);
			
			tableInit_option=0;
			tableInit();
			tableLoad (xProductTable);
			
			tableInit_option=1;
			tableInit();
			tableLoad(xAssociateTable);
			
			bDisassociate.setEnabled(false);
			bNewProduct.setEnabled(true);

		}
	}   //  cmd_disassociate
	
	/**
	 *  Hacer el insert en la tabla REFERENCEMATRIX 
	 */
	public void doInsert(int Product,int option){
		
		//Asocio producto
		X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix(Env.getCtx(),0,null);
		
		matrix.setXX_VALUE1(0);
		matrix.setXX_VALUE2(0);
		matrix.setXX_COLUMN(0);
		matrix.setXX_ROW(0);
		matrix.setXX_QUANTITYC(LineRefProv.getQty());
		matrix.setXX_QUANTITYV(LineRefProv.getSaleQty());
		matrix.setXX_QUANTITYO(LineRefProv.getXX_GiftsQty());
		matrix.setXX_VMR_PO_LineRefProv_ID((Integer)LineRefProv.get_Value("XX_VMR_PO_LineRefProv_ID"));
		matrix.setM_Product(Product);
		
		matrix.save();
		
		if(option==1){
			
			tableInit_option=1;
			tableInit();
			tableLoad(xAssociateTable);
			
			bDisassociate.setEnabled(true);
			bNewProduct.setEnabled(false);
			
		}
		
	}

	/**
	 *  Process Button Pressed - Process Matching
	 */
	private void cmd_newProduct()
	{
		//Selecciono el departamento
    	int depart=0;
    	String SQL ="Select XX_VMR_DEPARTMENT_ID " +
    			"from XX_VMR_VENDORPRODREF " +
    			"where XX_VMR_VENDORPRODREF_ID="+LineRefProv.getXX_VMR_VendorProdRef_ID();		
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				depart=rs.getInt("XX_VMR_DEPARTMENT_ID");
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		MVMRVendorProdRef vendorProdRef = new MVMRVendorProdRef(Env.getCtx(), LineRefProv.getXX_VMR_VendorProdRef_ID(), null);
		
		if(vendorProdRef.getXX_VMR_ProductClass_ID() > 0 && vendorProdRef.getXX_VMR_TypeLabel_ID() > 0) {
			MOrder order = new MOrder(Env.getCtx(), LineRefProv.getC_Order_ID(), null);
			
			//Selecciono el departamento
			X_XX_VMR_Department dept = new X_XX_VMR_Department(Env.getCtx(),vendorProdRef.getXX_VMR_Department_ID(),null);				    
			int category = dept.getXX_VMR_Category_ID();
			
			//Selecciono la línea
			X_XX_VMR_Line line = new X_XX_VMR_Line(Env.getCtx(), vendorProdRef.getXX_VMR_Line_ID(),null);
			int typeInventory = line.getXX_VMR_TypeInventory_ID();
				   		
			MProduct newProduct = new MProduct(Env.getCtx(), vendorProdRef.getXX_VMR_Department_ID(), 
				    vendorProdRef.getXX_VMR_Line_ID(), vendorProdRef.getXX_VMR_Section_ID(), vendorProdRef.get_ID(),
				    vendorProdRef.getC_TaxCategory_ID(),vendorProdRef.getXX_VME_ConceptValue_ID(),typeInventory, null);
			
			// Se buscará si por la referencia para producto ya existe para asignarle el Tipo de Exhibición
			String sql = "select * from M_Product where XX_VMR_DEPARTMENT_ID = "+vendorProdRef.getXX_VMR_Department_ID()+" and " +
				  "XX_VMR_LINE_ID = "+vendorProdRef.getXX_VMR_Line_ID()+" and XX_VMR_SECTION_ID = "+vendorProdRef.getXX_VMR_Section_ID()+" and " +
				  "XX_VMR_VendorProdRef_id = "+vendorProdRef.getXX_VMR_VendorProdRef_ID()+" order by M_Product_ID desc";
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = null;
			try {
				rs = pstmt.executeQuery();
				if (rs.next()) newProduct.setXX_VMR_TypeExhibition_ID(rs.getInt("XX_VMR_TypeExhibition_ID"));	 
			} catch (SQLException e) {
				
				e.printStackTrace();
			}finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			
		    if(vendorProdRef.getXX_VMR_Section_ID() >0){
		    	X_XX_VMR_Section section = new X_XX_VMR_Section(Env.getCtx(), vendorProdRef.getXX_VMR_Section_ID(), null);
		    	newProduct.setName(section.getName());
			 }else {
				 newProduct.setName(vendorProdRef.getName());
			 }
			newProduct.setXX_VMR_Category_ID(category);
			newProduct.setXX_VMR_LongCharacteristic_ID(vendorProdRef.getXX_VMR_LongCharacteristic_ID());
			newProduct.setXX_VMR_Brand_ID(vendorProdRef.getXX_VMR_Brand_ID());				    
			newProduct.setXX_VMR_UnitConversion_ID(vendorProdRef.getXX_VMR_UnitConversion_ID());
			newProduct.setXX_PiecesBySale_ID(vendorProdRef.getXX_PiecesBySale_ID());
			newProduct.setXX_VMR_UnitPurchase_ID(vendorProdRef.getXX_VMR_UnitPurchase_ID());
			newProduct.setXX_SaleUnit_ID(vendorProdRef.getXX_SaleUnit_ID());
			newProduct.setC_Country_ID(order.getC_Country_ID());
			newProduct.setIsActive(true);		
			newProduct.setC_BPartner_ID(vendorProdRef.getC_BPartner_ID());
			newProduct.setXX_VMR_TypeLabel_ID(vendorProdRef.getXX_VMR_TypeLabel_ID());
			newProduct.setXX_VMR_ProductClass_ID(vendorProdRef.getXX_VMR_ProductClass_ID());
		    newProduct.setM_AttributeSet_ID( Env.getCtx().getContextAsInt("#XX_L_P_ATTRIBUTESETST_ID"));
		    newProduct.setProductType(X_Ref_M_Product_ProductType.ITEM.getValue());
			newProduct.save();

		}else {
	    	//Creo variables de sesion para atraparlas en la ventana producto	
			Env.getCtx().setContext("#Depart_Aux",depart);
			Env.getCtx().setContext("#Section_Aux",LineRefProv.getXX_VMR_Section_ID());
			Env.getCtx().setContext("#Line_Aux",LineRefProv.getXX_VMR_Line_ID());
			Env.getCtx().setContext("#VendorRef_Aux",LineRefProv.getXX_VMR_VendorProdRef_ID());
			Env.getCtx().setContext("#FromProcess_Aux","Y");
	 
	    	AWindow window_product = new AWindow();
	    	Query query = Query.getNoRecordQuery("M_Product", true);
	    	window_product.initWindow(140, query);
	    	AEnv.showCenterScreen(window_product);
	    	
	    	// Obtenemos el GridController para setear la variable m_changed=true
	    	JRootPane jRootPane  = ((JRootPane)window_product.getComponent(0));
	    	JLayeredPane jLayeredPane = (JLayeredPane)jRootPane.getComponent(1);
	    	JPanel jPanel = (JPanel)jLayeredPane.getComponent(0);
	    	APanel aPanel = (APanel)jPanel.getComponent(0);
	    	VTabbedPane vTabbedPane = (VTabbedPane)aPanel.getComponent(0);
	    	GridController gridController = (GridController)vTabbedPane.getComponent(0);
	    	GridTable mTable = gridController.getMTab().getTableModel();
			mTable.setChanged(true);
	    	
	    	MProduct.loadLineRefProv(LineRefProv, Env.getCtx());
	    	
	    	//Borro las variables de sesion creadas
	    	Env.getCtx().remove("#Depart_Aux");
	    	Env.getCtx().remove("#FromProcess_Aux");
	    	Env.getCtx().remove("#Line_Aux");
	    	Env.getCtx().remove("#Section_Aux");
	    	Env.getCtx().remove("#VendorRef_Aux");
		}

		

    	
    	
	}   //  cmd_newProduct
	
	/**
	 *  cargar la linea a la cual está relacionada la referencia
	 */
	public static void loadLineRefProv(X_XX_VMR_PO_LineRefProv LineRefProv_aux, Ctx ctx)
	{
		LineRefProv=LineRefProv_aux;
		ctx_aux = ctx;
		
	}	//	LineRefProv

	
	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;
	//	log.config( "VMatch.valueChanged");
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setCursor(Cursor.getDefaultCursor());
	}   //  valueChanged

	
	/***************************************************************************
	 *  Table Model Listener - calculate matchd Qty
	 *  @param e event
	 */
	public void tableChanged (TableModelEvent e)
	{
		//  Matched From
		
		//statusBar.setStatusDB(noRows);
	}   //  tableChanged

	
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

		if(tableInit_option==0)
		{
			m_sql.append("SELECT value,tab.M_Product_ID,tab.Name "
				+ "FROM M_Product tab "
				+ "WHERE XX_VMR_VENDORPRODREF_ID="+LineRefProv.getXX_VMR_VendorProdRef_ID()+" "
				+"AND ISACTIVE='Y' " +
				"AND M_ATTRIBUTESET_ID = " + Env.getCtx().getContextAsInt("#XX_L_P_ATTRIBUTESETST_ID"));
				
				/*//Si caracteristica larga es null
				if(LineRefProv.getXX_VMR_LongCharacteristic_ID()!=0)
				{
					m_sql.append("AND XX_VMR_LONGCHARACTERISTIC_ID="+LineRefProv.getXX_VMR_LongCharacteristic_ID()+" ");
				}
				else{
					m_sql.append("AND XX_VMR_LONGCHARACTERISTIC_ID IS NULL ");
				}*/
				
				m_orderBy = " order by M_Product_ID";
		}
		else if(tableInit_option==1)
		{
			m_sql.append("SELECT value,tab.M_Product_ID,tab.Name "
					+ "FROM M_Product tab "
					+ "WHERE tab.M_Product_ID IN "
					+ "(select M_Product from XX_VMR_REFERENCEMATRIX where XX_VMR_PO_LINEREFPROV_ID="+(Integer)LineRefProv.getXX_VMR_PO_LineRefProv_ID()+")");
		}
		else if(tableInit_option==2)
		{
			m_sql.append("SELECT value,tab.M_Product_ID,tab.Name "
					+ "FROM M_Product tab "
					+ "WHERE XX_VMR_VENDORPRODREF_ID = 0 AND M_PRODUCT_ID = 0");
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
			m_sql.toString(), "tab", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
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
	
	/**
	 *  verifica si hay productos asociados
	 *  @param table table
	 */
	private boolean verify()
	{
		String sql ="SELECT XX_VMR_ReferenceMatrix_ID "
			+ "FROM XX_VMR_ReferenceMatrix "
			+ "WHERE XX_VMR_PO_LINEREFPROV_ID="+(Integer)LineRefProv.getXX_VMR_PO_LineRefProv_ID();	
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try 
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				associatedReference_ID=rs.getInt("XX_VMR_ReferenceMatrix_ID");
				rs.close();
				pstmt.close();
				return true;
			}
								
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return false;
	}   //  tableLoad

	private void fullAssociated(){
		
		boolean associated=true;
		
		String SQL ="Select * " +
		"from XX_VMR_REFERENCEMATRIX " +
		"where M_product IS NULL AND XX_VMR_PO_LINEREFPROV_ID="+LineRefProv.get_ID();		
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				associated=false;
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		
		if(associated==true){
			String SQL10 = "UPDATE XX_VMR_PO_LineRefProv "
			    +" SET XX_ReferenceIsAssociated='Y'"
			    + " WHERE XX_VMR_PO_LineRefProv_ID="+LineRefProv.getXX_VMR_PO_LineRefProv_ID();
				
				DB.executeUpdate(null, SQL10);

//			LineRefProv.setXX_ReferenceIsAssociated(true);
//			LineRefProv.save();
			
			int dialog=Env.getCtx().getContextAsInt("#Dialog_Associate_Aux");

			if(dialog==1){
				ADialog.info(m_WindowNo,m_frame,"MustRefresh");
				Env.getCtx().remove("#Dialog_Associate_Aux");
			}
				
		}else{
			String SQL10 = "UPDATE XX_VMR_PO_LineRefProv "
			    +" SET XX_ReferenceIsAssociated='N'"
			    + " WHERE XX_VMR_PO_LineRefProv_ID="+LineRefProv.getXX_VMR_PO_LineRefProv_ID();
				
				DB.executeUpdate(null, SQL10);
//			LineRefProv.setXX_ReferenceIsAssociated(false);
//			LineRefProv.save();
		}
		
	}
	
	private boolean isInMatrix(){
		
		String SQL ="Select * " +
		"from XX_VMR_REFERENCEMATRIX " +
		"where XX_VMR_PO_LINEREFPROV_ID="+LineRefProv.get_ID();		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try 
		{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				return true;
			}
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return false;
		
	}
	
}   //  ConvertReference

