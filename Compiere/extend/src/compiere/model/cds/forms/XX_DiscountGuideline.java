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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VFile;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import com.sun.org.apache.xerces.internal.impl.dv.xs.DayDV;

import compiere.model.cds.MVMR_BudgetSalesDepart;
import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_TypeInventory;
import compiere.model.cds.forms.indicator.XX_Indicator;

/** Pauta de Rebaja (Párametros y Seguimiento de Promociones)
 * El indicador definido para medir el desempeño de la colección es el Sell Trough.
 * Para fijar una promoción se realiza un seguimiento periódico al desempeño de las colecciones.
 * @author Gabrielle Huchet
 */
public class XX_DiscountGuideline extends CPanel
implements FormPanel, ActionListener, ListSelectionListener {


	private static final long serialVersionUID = 1L;

	/**	Window No	*/
	private int m_WindowNo = 0;
	/**	FormFrame	*/
	private FormFrame m_frame;
	/**	Logger		*/
	static CLogger log = CLogger.getCLogger(XX_DiscountGuideline.class);
	
	/* Panel de la ventana */
	private CPanel mainPanel = new CPanel();
	private CPanel northPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private JScrollPane xScrollPane = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder(" ");
	private CPanel xPanel = new CPanel();
	
	/* Layouts */
	private BorderLayout mainLayout = new BorderLayout();
	private GridBagLayout northLayout = new GridBagLayout();
	private FlowLayout southLayout = new FlowLayout();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);	

	/* Botones */
	private CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "Search"));
	private CButton bClear = new CButton(Msg.translate(Env.getCtx(), "Clear"));
	private CButton bPrint = new CButton(Msg.translate(Env.getCtx(), "ExportExcel"));

	/* Archivo a exportar*/
	private VFile bFile = new VFile("File", Msg.getMsg(Env.getCtx(), "File"),true, false, true, true, JFileChooser.SAVE_DIALOG);
	private CLabel labelFile = new CLabel();
	
	/* Campos del filtro disponibles */
	//Collección
	private CComboBox collectionCombo = new CComboBox();
	private CLabel collectionLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_VMR_Collection_ID"));
	//Categoría
	private CComboBox categoryCombo = new CComboBox();
	private CLabel categoryLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"));
	//Departamento
	private CComboBox departmentCombo = new CComboBox();
	private CLabel departmentLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"));
	
	private Integer periodDay = null;
	private CLabel periodDayLabel  = null;
	/** La tabla donde se guardarán los datos*/
	private MiniTablePreparator table = new MiniTablePreparator();
	
	//Centro de Distribución
	private Integer Warehouse_CD = Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"); 
	
	//Tipos de Descuento
	private String  des1 = "20";  //20 %
	private String  des2 = "30";  //30 %
	private String  des3 = "50";  //50 %
	private String  sinDes = "Sin Descuento";  
	
	//Limites de los Descuento
	private Integer des1Lim = 40;  //30 < ST <= 40
	private Integer des2Lim = 30;  //25 <= ST <= 30
	private Integer des3Lim = 25;  //ST < 25
	


	@Override
	public void init(int WindowNo, FormFrame frame) {
		// TODO Auto-generated method stub
		m_WindowNo = WindowNo;
		m_frame = frame;
		try {
			jbInit();
			dynInit();
		}catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
	}
	
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
		
		reviewPeriod() ;
		periodDayLabel = new CLabel("Días Pasados Desde el Inicio de la Colección: "+periodDay);
		northPanel.add(collectionLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(collectionCombo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(categoryLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(categoryCombo, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(departmentLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(departmentCombo, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(periodDayLabel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));


		southLayout.setAlignment(FlowLayout.RIGHT);
		southLayout.setHgap(15);
		southPanel.add(bSearch, null);
		southPanel.add(bClear, null);
		southPanel.add(labelFile, null);
		southPanel.add(bFile, null);
		southPanel.add(bPrint, null);
	    southPanel.validate();
	    
	}
	
	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		m_frame.getRootPane().setDefaultButton(bSearch);
		ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_VendorProdRef_ID"),   ".", KeyNamePair.class),        //  1 Referencia de Prov.
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Collection_ID"),   ".", String.class),     //  2 Colección
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"),   ".", String.class),		//3	Categoria
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),".", String.class),	         //  4 Departamento
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Brand_ID"),".", String.class),	         //  5 Marca
			new ColumnInfo("Inv. Incial + Compras (pzas)",".", String.class),	         //  6 Inv. Incial + Compras
			new ColumnInfo("Ventas Piezas (pzas)",".", String.class),	         //  7 Ventas
			new ColumnInfo("Inv. Final (pzas)",".", String.class),	         //  8 Inv. Final (pzas)
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_SellThrough"),".", String.class),	         //  9 Sell Trough
			new ColumnInfo("Descuento %",".", String.class),	         //  10 Descuento %
			new ColumnInfo("Inv. Final Bs.",".", String.class),	         //  11 Inv. Final Bs.
			new ColumnInfo("Promoción Bs.",".", String.class),	         //  12 Promoción Bs.
		};
		
		table.prepareTable(layout, "", "", false, "");
		table.setAutoResizeMode(4);
		
	
		//  Visual
		CompiereColor.setBackground (this);
		
		//  Listener
		table.getSelectionModel().addListSelectionListener(this);	
		addActionListeners();
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_RefLQty"));
		statusBar.setStatusDB(table.getRowCount());
		
		// Agregar las acciones
		uploadCollection();
		uploadCategory();
		uploadDepartment();
		
	}   //  dynInit
	
	private void reviewPeriod() {

		String sql = "SELECT TRUNC(SYSDATE) - TO_DATE(CO.XX_STARTINGDAY||'-'||CO.XX_STARTINGMONTH||'-'||TO_CHAR(SYSDATE,'YYYY'),'DD-MM-YYYY')+1 DIAS" +
		"\nFROM XX_VMR_COLLECTION CO WHERE CO.ISACTIVE = 'Y' " +
		"\nAND CO.AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+
		"\nAND TO_CHAR(SYSDATE,'MM') IN (CO.XX_STARTINGMONTH, CO.XX_ENDINGMONTH)"; 			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				periodDay = rs.getInt(1);
			}			
				
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}finally {
			try {
				rs.close();
				pstmt.close();	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (periodDay < 30){
			des3Lim = 25;
			des2Lim = 30;
			des1Lim = 40;
		}else if (periodDay < 60){
			des3Lim = 45;
			des2Lim = 60;
			des1Lim = 80;
		}
	}

	@Override
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		removeActionListeners();
		if (e.getSource() == bSearch){
			cmd_Search();
		}
		else if (e.getSource() == bClear) {			
			resetFilter();
		}
		else if (e.getSource() == bPrint)
		{
			try {			
				XX_Indicator.imprimirArchivo( table, bFile, m_WindowNo, m_frame); 
			} catch (Exception ex) {
				log.log(Level.SEVERE, "", ex);
			}			
		}else if (e.getSource() == categoryCombo){
			uploadDepartment();
		}
		addActionListeners();
	
		m_frame.setCursor(Cursor.getDefaultCursor());

	}

	/** Método invocado al presionar el botón de limpiar filtro */
	protected void resetFilter() { 
		removeActionListeners();		
		uploadBasicInfo();
		addActionListeners();
	}
	
	/** Se inicializan los datos en los filtros y tablas  */	
	private void uploadBasicInfo() {

		table.setRowCount(0);
		uploadCategory();
		uploadCollection();
		uploadDepartment();
		//Actualizar los cambios
		repaint();
		
	}

	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{	
		
		tableLoad();	
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_RefLQty"));
		statusBar.setStatusDB(table.getRowCount());
			

	}   //  cmd_Search
	
	private void tableLoad() {
		
		table.setRowCount(0);
		KeyNamePair catg = (KeyNamePair)categoryCombo.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)departmentCombo.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)collectionCombo.getSelectedItem();
		String sql = "";
		String where ="";
		String with ="";
		
		with = "\nWITH " +
			//INVENTARIO INICIAL DE LA COLECCIÓN
				"\nINV_INICIAL AS ( " +
				"\nSELECT P.XX_VMR_VENDORPRODREF_ID REF, VRP.VALUE||' - '||VRP.NAME RNAME, " +
				"\nCO.NAME CNAME, I.XX_VMR_DEPARTMENT_ID DPT, P.XX_VMR_BRAND_ID BRAND,  " +
				"\nSUM(I.XX_INITIALINVENTORYQUANTITY) CANT, SUM(I.XX_INITIALINVENTORYAMOUNT) MONTO " +
				"\nFROM XX_VCN_INVENTORY I " +
				"\nJOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = I.M_ATTRIBUTESETINSTANCE_ID) " +
				"\nJOIN M_PRODUCT P ON (P.M_PRODUCT_ID = I.M_PRODUCT_ID) " +
				"\nJOIN XX_VMR_VENDORPRODREF VRP ON (VRP.XX_VMR_VENDORPRODREF_ID = P.XX_VMR_VENDORPRODREF_ID) " +
				"\nJOIN XX_VMR_TYPEINVENTORY TI ON (TI.XX_VMR_TYPEINVENTORY_ID = P.XX_VMR_TYPEINVENTORY_ID) " +
				"\nLEFT JOIN XX_VMR_PACKAGE PA ON (PA.XX_VMR_PACKAGE_ID = ASI.XX_VMR_PACKAGE_ID) " +
				"\nLEFT JOIN XX_VMR_COLLECTION CO ON (CO.XX_VMR_COLLECTION_ID = PA.XX_VMR_COLLECTION_ID) " +
				"\nWHERE I.XX_INVENTORYYEAR = TO_CHAR(SYSDATE,'YYYY') " + //AÑO DE LA COLECCION ACTUAL 
				"\nAND I.XX_INVENTORYMONTH = CO.XX_STARTINGMONTH" + //MES INICIAL DE LA COLECCION ACTUAL
				"\nAND TI.NAME = 'TENDENCIA'  " +  // SOLO TENDENCIA
				"\nAND ASI.XX_VMR_PACKAGE_ID IS NOT NULL " +
				"\nAND CO.XX_VMR_COLLECTION_ID =  " +coll.getKey()+ //COLLECCION ACTUAL
				"\nAND I.XX_INITIALINVENTORYQUANTITY > 0 " +
				"\nGROUP BY P.XX_VMR_VENDORPRODREF_ID, VRP.VALUE||' - '||VRP.NAME, " +
				"\nCO.NAME,I.XX_VMR_DEPARTMENT_ID, P.XX_VMR_BRAND_ID " +
				"\n)," +
				//COMPRAS EN ESTADO "APROBADO" DE PRODUCTOS DE LA COLECCIÓN QUE LLEGAN ANTES DE QUE ACABE LA COLECCIÓN ACTUAL
				"\nOC_APROBADAS AS ( " +
				"\nSELECT P.XX_VMR_VENDORPRODREF_ID REF, VRP.VALUE||' - '||VRP.NAME RNAME, CO.NAME CNAME, " +
				"\nO.XX_VMR_DEPARTMENT_ID DPT, P.XX_VMR_BRAND_ID BRAND,  " +
				"\nSUM(OL.QTYORDERED) CANT, SUM(OL.QTYORDERED*PRICEACTUAL) MONTO " +
				"\nFROM C_ORDER O " +
				"\nJOIN C_ORDERLINE OL ON (O.C_ORDER_ID = OL.C_ORDER_ID) " +
				"\nJOIN M_PRODUCT P ON (P.M_PRODUCT_ID = OL.M_PRODUCT_ID) " +
				"\nJOIN XX_VMR_VENDORPRODREF VRP ON (VRP.XX_VMR_VENDORPRODREF_ID = P.XX_VMR_VENDORPRODREF_ID) " +
				"\nJOIN XX_VMR_TYPEINVENTORY TI ON (TI.XX_VMR_TYPEINVENTORY_ID = P.XX_VMR_TYPEINVENTORY_ID) " +
				"\nJOIN XX_VMR_COLLECTION CO ON (CO.XX_VMR_COLLECTION_ID = O.XX_VMR_COLLECTION_ID) " +
				"\nWHERE O.XX_ORDERSTATUS IN ('AP') " +
				"\nAND TO_CHAR(O.XX_ESTIMATEDDATE,'YYYY') <= TO_CHAR(SYSDATE,'YYYY')  " + //AÑO DE LA COLECCION ACTUAL
				"\nAND TO_CHAR(O.XX_ESTIMATEDDATE,'MM') <= CO.XX_ENDINGMONTH " +
				"\nAND TO_CHAR(O.XX_ESTIMATEDDATE,'DD') <= CO.XX_ENDINGDAY  " +
				"\nAND TI.NAME = 'TENDENCIA'  " +  //SOLO TENDENCIA
				"\nAND CO.XX_VMR_COLLECTION_ID =  " +coll.getKey()+ //COLLECCION ACTUAL
				"\nAND OL.QTYORDERED > 0 " +
				"\nGROUP BY P.XX_VMR_VENDORPRODREF_ID, VRP.VALUE||' - '||VRP.NAME, " +
				"\nCO.NAME, O.XX_VMR_DEPARTMENT_ID, P.XX_VMR_BRAND_ID " +
				"\n), " +
				//COMPRAS EN ESTADO "RECIBIDA" DE PRODUCTOS DE LA COLECCIÓN 
				"\nOC_RECIBIDAS AS ( " +
				"\nSELECT P.XX_VMR_VENDORPRODREF_ID REF, VRP.VALUE||' - '||VRP.NAME RNAME," +
				"\nCO.NAME CNAME, O.XX_VMR_DEPARTMENT_ID DPT, P.XX_VMR_BRAND_ID BRAND, " +
				"\nSUM(OL.QTYORDERED) CANT, SUM(OL.QTYORDERED*PRICEACTUAL) MONTO " +
				"\nFROM C_ORDER O " +
				"\nJOIN C_ORDERLINE OL ON (O.C_ORDER_ID = OL.C_ORDER_ID) " +
				"\nJOIN M_PRODUCT P ON (P.M_PRODUCT_ID = OL.M_PRODUCT_ID) " +
				"\nJOIN XX_VMR_VENDORPRODREF VRP ON (VRP.XX_VMR_VENDORPRODREF_ID = P.XX_VMR_VENDORPRODREF_ID) " +
				"\nJOIN XX_VMR_TYPEINVENTORY TI ON (TI.XX_VMR_TYPEINVENTORY_ID = P.XX_VMR_TYPEINVENTORY_ID) " +
				"\nJOIN XX_VMR_COLLECTION CO ON (CO.XX_VMR_COLLECTION_ID = O.XX_VMR_COLLECTION_ID) " +
				"\nWHERE O.XX_ORDERSTATUS IN ('RE') " +
				"\nAND TI.NAME = 'TENDENCIA'  " +  //SOLO TENDENCIA
				"\nAND CO.XX_VMR_COLLECTION_ID =  " +coll.getKey()+ //COLLECCION ACTUAL
				"\nAND OL.QTYORDERED > 0 " +
				"\nGROUP BY P.XX_VMR_VENDORPRODREF_ID, VRP.VALUE||' - '||VRP.NAME, " +
				"\nCO.NAME, O.XX_VMR_DEPARTMENT_ID, P.XX_VMR_BRAND_ID  " +
				"\n)," +
				//COMPRAS EN ESTADO "CHEQUEADA"  DE PRODUCTOS DE LA COLECCIÓN QUE SE REGISTRARON EN EL INVENTARIO 
				"\nCOMPRAS_INV AS ( " +
				"\nSELECT P.XX_VMR_VENDORPRODREF_ID REF, VRP.VALUE||' - '||VRP.NAME RNAME, " +
				"\nCO.NAME CNAME, I.XX_VMR_DEPARTMENT_ID DPT, P.XX_VMR_BRAND_ID BRAND,  " +
				"\nSUM(I.XX_SHOPPINGQUANTITY) CANT, SUM(I.XX_SHOPPINGAMOUNT) MONTO " +
				"\nFROM XX_VCN_INVENTORY I" +
				"\nJOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = I.M_ATTRIBUTESETINSTANCE_ID) " +
				"\nJOIN M_PRODUCT P ON (P.M_PRODUCT_ID = I.M_PRODUCT_ID)" +
				"\nJOIN XX_VMR_VENDORPRODREF VRP ON (VRP.XX_VMR_VENDORPRODREF_ID = P.XX_VMR_VENDORPRODREF_ID)" +
				"\nJOIN XX_VMR_TYPEINVENTORY TI ON (TI.XX_VMR_TYPEINVENTORY_ID = P.XX_VMR_TYPEINVENTORY_ID) " +
				"\nJOIN XX_VMR_PACKAGE PA ON (PA.XX_VMR_PACKAGE_ID = ASI.XX_VMR_PACKAGE_ID)" +
				"\nJOIN XX_VMR_COLLECTION CO ON (CO.XX_VMR_COLLECTION_ID = PA.XX_VMR_COLLECTION_ID)" +
				"\nWHERE I.XX_INVENTORYYEAR = TO_CHAR(SYSDATE,'YYYY') " + //AÑO DE LA COLECCION ACTUAL
				"\nAND I.XX_INVENTORYMONTH IN (CO.XX_STARTINGMONTH, CO.XX_ENDINGMONTH) " +//MES INICIAL Y FIN DE LA COLECCION ACTUAL
				"\nAND TI.NAME = 'TENDENCIA'  " +  //SOLO TENDENCIA
				"\nAND CO.XX_VMR_COLLECTION_ID =  " +coll.getKey()+ //COLLECCION ACTUAL
				"\nAND I.M_WAREHOUSE_ID IN ("+Warehouse_CD+") " + //CD
				"\nAND I.XX_SHOPPINGQUANTITY > 0" +
				"\nGROUP BY P.XX_VMR_VENDORPRODREF_ID, VRP.VALUE||' - '||VRP.NAME, " +
				"\nCO.NAME, I.XX_VMR_DEPARTMENT_ID, P.XX_VMR_BRAND_ID" +
				"\n)," +
				//VENTAS DE PRODUCTOS DE LA COLECCIÓN QUE SE REGISTRARON EN EL INVENTARIO 
				"\nVENTAS AS (" +
				"\nSELECT P.XX_VMR_VENDORPRODREF_ID REF, VRP.VALUE||' - '||VRP.NAME RNAME, CO.NAME CNAME, I.XX_VMR_DEPARTMENT_ID DPT, P.XX_VMR_BRAND_ID BRAND,  " +
				"\nSUM(I.XX_SALESQUANTITY) CANT, SUM(I.XX_SALESAMOUNT) MONTO " +
				"\nFROM XX_VCN_INVENTORY I " +
				"\nJOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = I.M_ATTRIBUTESETINSTANCE_ID)  " +
				"\nJOIN M_PRODUCT P ON (P.M_PRODUCT_ID = I.M_PRODUCT_ID) " +
				"\nJOIN XX_VMR_VENDORPRODREF VRP ON (VRP.XX_VMR_VENDORPRODREF_ID = P.XX_VMR_VENDORPRODREF_ID) " +
				"\nJOIN XX_VMR_TYPEINVENTORY TI ON (TI.XX_VMR_TYPEINVENTORY_ID = P.XX_VMR_TYPEINVENTORY_ID) " +
				"\nLEFT JOIN XX_VMR_PACKAGE PA ON (PA.XX_VMR_PACKAGE_ID = ASI.XX_VMR_PACKAGE_ID) " +
				"\nLEFT JOIN XX_VMR_COLLECTION CO ON (CO.XX_VMR_COLLECTION_ID = PA.XX_VMR_COLLECTION_ID) " +
				"\nWHERE I.XX_INVENTORYYEAR = TO_CHAR(SYSDATE,'YYYY')  " + //AÑO DE LA COLECCION ACTUAL
				"\nAND I.XX_INVENTORYMONTH IN (CO.XX_STARTINGMONTH, CO.XX_ENDINGMONTH)   " + //MES INICIAL Y FINAL DE LA COLECCION ACTUAL
				"\nAND TI.NAME = 'TENDENCIA'  " +  //SOLO TENDENCIA
				"\nAND ASI.XX_VMR_PACKAGE_ID IS NOT NULL " +
				"\nAND CO.XX_VMR_COLLECTION_ID =  " +coll.getKey()+ //COLLECCION ACTUAL
				"\nAND I.M_WAREHOUSE_ID  NOT IN ("+Warehouse_CD+")" +
				"\nGROUP BY P.XX_VMR_VENDORPRODREF_ID, VRP.VALUE||' - '||VRP.NAME, " +
				"\nCO.NAME, I.XX_VMR_DEPARTMENT_ID, P.XX_VMR_BRAND_ID " +
				"\n), " +
				"\nUNION_COMPRAS_INV AS ( " +
				"\nSELECT * FROM INV_INICIAL UNION ALL " +
				"\nSELECT * FROM OC_APROBADAS UNION ALL " +
				"\nSELECT * FROM OC_RECIBIDAS UNION ALL " +
				"\nSELECT * FROM COMPRAS_INV  " +
				"\n), " +
				"\nINV_SUM_COMPRAS AS ( " +
				"\nSELECT REF, RNAME, CNAME, DPT, BRAND, SUM(CANT) CANT, SUM(MONTO) MONTO " +
				"\nFROM UNION_COMPRAS_INV " +
				"\nGROUP BY REF, RNAME, CNAME, DPT, BRAND " +
				"\n)";
		
		if(periodDay < 60){
			sql = "\nSELECT I.REF, I.RNAME, I.CNAME,  CA.VALUE||'-'||CA.NAME, " +
				"\nD.VALUE||'-'||D.NAME, B.NAME, I.CANT INICIAL_Y_COMPRAS, " +
				"\nCASE WHEN V.CANT IS NULL THEN 0 ELSE V.CANT END VENTAS, " +
				"\n(CASE WHEN V.CANT IS NULL THEN I.CANT ELSE ROUND(I.CANT-V.CANT,2) END) INV_FINAL, " +
				"\n(CASE WHEN V.CANT IS NULL THEN 0 ELSE ROUND((V.CANT/I.CANT*100),2) END) ST, " +
				"\nCASE WHEN (CASE WHEN V.CANT IS NULL THEN 0 " +
				"\nELSE ROUND((V.CANT/I.CANT*100),2) END) < "+des3Lim+" THEN '"+des3+"%'" +     //50%
				"\nWHEN (CASE WHEN V.CANT IS NULL THEN 0 " +
				"\nELSE ROUND((V.CANT/I.CANT*100),2) END) <= "+des2Lim+" THEN '"+des2+"%'" +    //30%
				"\nWHEN (CASE WHEN V.CANT IS NULL THEN 0 " +
				"\nELSE ROUND((V.CANT/I.CANT*100),2) END) <= "+des1Lim+" THEN '"+des1+"%'" +    //20%
				"\nELSE '"+sinDes+"' END DESCUENTO," +
				"\nI.MONTO BS_INV, " +
				"\nCASE WHEN (CASE WHEN V.CANT IS NULL THEN 0 " +
				"\nELSE ROUND((V.CANT/I.CANT*100),2) END) <= "+des3Lim+" THEN I.MONTO-(I.MONTO*("+des3+"/100))" +
				"\nWHEN (CASE WHEN V.CANT IS NULL THEN 0 " +
				"\nELSE ROUND((V.CANT/I.CANT*100),2) END) <= "+des2Lim+" THEN I.MONTO-(I.MONTO*("+des2+"/100))" +
				"\nWHEN (CASE WHEN V.CANT IS NULL THEN 0 " +
				"\nELSE ROUND((V.CANT/I.CANT*100),2) END) <= "+des1Lim+" THEN I.MONTO-(I.MONTO*("+des1+"/100))" +
				"\nELSE I.MONTO END BS_DESCUENTO" +
				"\nFROM INV_SUM_COMPRAS I LEFT JOIN VENTAS V ON (I.REF=V.REF)" +
				"\nJOIN XX_VMR_DEPARTMENT D ON (I.DPT = D.XX_VMR_DEPARTMENT_ID)" +
				"\nJOIN XX_VMR_CATEGORY CA ON (CA.XX_VMR_CATEGORY_ID = D.XX_VMR_CATEGORY_ID)" +
				"\nJOIN XX_VMR_BRAND B ON (I.BRAND = B.XX_VMR_BRAND_ID)";
		}
		else {
			sql = "\nSELECT I.REF, I.RNAME, I.CNAME,  CA.VALUE||'-'||CA.NAME, " +
				"\nD.VALUE||'-'||D.NAME, B.NAME, I.CANT INICIAL_Y_COMPRAS, " +
				"\nCASE WHEN V.CANT IS NULL THEN 0 ELSE V.CANT END VENTAS, " +
				"\n(CASE WHEN V.CANT IS NULL THEN I.CANT ELSE ROUND(I.CANT-V.CANT,2) END) INV_FINAL, " +
				"\n(CASE WHEN V.CANT IS NULL THEN 0 ELSE ROUND((V.CANT/I.CANT*100),2) END) ST, "+des3+" DESCUENTO," +
				"\nI.MONTO BS_INV, " +
				"\nI.MONTO-(I.MONTO*("+des3+"/100)) BS_DESCUENTO" +
				"\nFROM INV_SUM_COMPRAS I LEFT JOIN VENTAS V ON (I.REF=V.REF)" +
				"\nJOIN XX_VMR_DEPARTMENT D ON (I.DPT = D.XX_VMR_DEPARTMENT_ID)" +
				"\nJOIN XX_VMR_CATEGORY CA ON (CA.XX_VMR_CATEGORY_ID = D.XX_VMR_CATEGORY_ID)" +
				"\nJOIN XX_VMR_BRAND B ON (I.BRAND = B.XX_VMR_BRAND_ID)";
		}
		//Categoría
		if(catg != null && catg.getKey()!= 0) {	
			if (where.isEmpty()){
				where = "\nWHERE CA.XX_VMR_CATEGORY_ID ="+catg.getKey();
			}else {
				where = "\nAND CA.XX_VMR_CATEGORY_ID ="+catg.getKey();
			}	
		}	
		//Departamento			
		if(dept != null  && dept.getKey()!= 0) {
			if (where.isEmpty()){
				where = "\nWHERE D.XX_VMR_DEPARTMENT_ID ="+dept.getKey();
			}else {
				where = "\nAND D.XX_VMR_DEPARTMENT_ID ="+dept.getKey();
			}	
		}
			
		sql = with + sql + where;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int row = 0;
		try {
			
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			while (rs.next()) {
				table.setRowCount(row + 1);
				table.setValueAt(new KeyNamePair(rs.getInt(1), rs.getString(2)), row, 0); //Referencia de Proveedor
				table.setValueAt(rs.getString(3), row, 1); 		// Colección
				table.setValueAt(rs.getString(4), row, 2); 		// Categoría
				table.setValueAt(rs.getString(5), row, 3); 		// Departamento
				table.setValueAt(rs.getString(6), row, 4);		// Marca
				table.setValueAt(rs.getString(7), row, 5); 		// Piezas Totales de Inventario Inicial y Compras
				table.setValueAt(rs.getString(8), row, 6); 		// Piezas Totales Vendidas
				table.setValueAt(rs.getString(9), row, 7); 		// Inventario Final Piezas
				table.setValueAt(rs.getString(10), row, 8); 	// Sell Trough
				table.setValueAt(rs.getString(11), row, 9); 	// % Descuento
				table.setValueAt(rs.getString(12), row, 10);	// Inventario Final Bs.
				table.setValueAt(rs.getString(13), row, 11);	// Promoción Bs.
				row++;
			}

		}  catch (Exception e) {
			log.log(Level.SEVERE, sql, e);			
		} finally {
			//Cerrar los statements
			if (rs != null)
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			
			if (ps != null)
				try {
					ps.close();
					ps = null;
				} catch (SQLException e) {}		
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
			removeActionListeners();
			int tableRow = table.getSelectedRow();
				
			if(tableRow==-1) //Si no tengo nada seleccionado en la tabla 
			{
				statusBar.setStatusLine(Msg.translate(Env.getCtx(),"XX_RefProvLQty")); 
				statusBar.setStatusDB(table.getRowCount());
			}
			else
			{
				statusBar.setStatusLine(Msg.translate(Env.getCtx(), "SelectedRefProv")); 
				KeyNamePair refKey = (KeyNamePair) table.getValueAt(tableRow, 0);
				statusBar.setStatusDB(refKey.getName()); // Colocar value de la referencia de proveedor seleccionada
			
			}
			addActionListeners();
			if (e.getValueIsAdjusting())
				return;

	}	
	
	/** Activa todos los listeners creados */
	protected final void addActionListeners () {
		
		bSearch.addActionListener(this);
		bClear.addActionListener(this);
		bPrint.addActionListener(this);
		categoryCombo.addActionListener(this);	
	} 

	/** Deshabilitar Action Listeners */
	protected final void removeActionListeners () {
		
		bSearch.removeActionListener(this);
		bClear.removeActionListener(this);
	    bPrint.removeActionListener(this);
		categoryCombo.removeActionListener(this);	

	}
	
	/** Carga datos del filtro colecciones */
	private void uploadCollection() {
		KeyNamePair loadKNP;
		String sql = "";						
		collectionCombo.removeAllItems();
		sql = "SELECT XX_VMR_COLLECTION_ID, NAME " +
				"\nFROM XX_VMR_COLLECTION  WHERE ISACTIVE = 'Y' AND TO_CHAR(SYSDATE,'MM') IN (XX_StartingMonth, XX_EndingMonth) " +
				"\nAND AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\n ORDER BY NAME"; 			
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				collectionCombo.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
	}
	
	/** Carga datos del filtro departamentos */ 
	private void uploadDepartment() {
		
		KeyNamePair catg = (KeyNamePair)categoryCombo.getSelectedItem();				
		KeyNamePair loadKNP;
		String sql;
		
		departmentCombo.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		departmentCombo.addItem(loadKNP);
	
		if (catg != null && catg.getKey() != 0){			
			sql = "SELECT XX_VMR_DEPARTMENT_ID, VALUE||'-'||NAME FROM XX_VMR_DEPARTMENT " 
				+ "WHERE XX_VMR_CATEGORY_ID = " + catg.getKey() +  " ORDER BY VALUE ";
		}  else {					
			sql = "SELECT XX_VMR_DEPARTMENT_ID, VALUE||'-'||NAME FROM XX_VMR_DEPARTMENT " +
					"WHERE AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID();
			sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
				+ " ORDER BY VALUE||'-'||NAME";									
		} 
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				departmentCombo.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}		
		
	}

	/** Carga datos del filtro categorías */
	private void uploadCategory() {
		
		KeyNamePair loadKNP;
		String sql = "";						
		categoryCombo.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		categoryCombo.addItem(loadKNP);

		sql = " SELECT XX_VMR_CATEGORY_ID, VALUE||'-'||NAME " +
				" FROM XX_VMR_CATEGORY  WHERE ISACTIVE = 'Y' AND AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID(); 			
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				categoryCombo.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
	}
	


}
