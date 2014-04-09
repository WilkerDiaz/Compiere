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
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.framework.Lookup;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VFile;
import org.compiere.grid.ed.VLookup;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.X_Ref_XX_OrderStatus;
import compiere.model.cds.forms.indicator.XX_Indicator;

/**
 *  @author Gabrielle Huchet
 *  @version 
 */

public class XX_WeightedInventoryDays extends CPanel implements FormPanel,
ActionListener  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**	Logger			*/
	protected static CLogger log = CLogger.getCLogger(XX_WeightedInventoryDays.class);

	/** FormFrame */
	private FormFrame m_frame;
	/** Window No */
	private int m_WindowNo = 0;
	/** Contexto general*/ 
	protected Ctx ctx = Env.getCtx();
	
	/* Panel y campos de la ventana */
	private CPanel mainPanel = new CPanel();
	protected CPanel parameterPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private CPanel commandPanel = new CPanel();
	private JScrollPane dataPane = new JScrollPane();
	private JScrollPane dataPane2 = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder("Ponderado de Días de Inventario");
	private StatusBar statusBar = new StatusBar();
	
	/* Layouts */
	private BorderLayout mainLayout = new BorderLayout();
	private BorderLayout centerLayout = new BorderLayout();
	private GridBagLayout parameterLayout = new GridBagLayout();	
	private FlowLayout commandLayout = new FlowLayout();


	private final static int PURCHASEORDER = 1, PRODUCT = 0, 
	WAREHOUSE = 1, LOCATOR = 2 ,CATEGORY = 3, DEPARTMENT = 4, COLLECTION = 5 , PACKAGE = 6, 
	TOTALPIECES = 7, WEIGHTED = 8,
	FIELDSPERGROUP = 3;

	private static int option;
	private Vector<String> selected = new Vector<String>();
	
	//******* Campos del filtro disponibles
	//Radio button
	private JRadioButton rPurchOrder = new JRadioButton(Msg.translate(Env.getCtx(), "C_Order_ID"),false);
	private JRadioButton rProduct = new JRadioButton(Msg.translate(Env.getCtx(), "XX_Product"),true);
	private CPanel panelGroup = new CPanel();
	private ButtonGroup buttonGroup = new ButtonGroup();
	//Categoría
	private CLabel labelCategory = new CLabel(Msg.translate(ctx, "XX_VMR_Category_ID"));
	private CComboBox comboCategory = new CComboBox();
	//Departamento
	private CLabel labelDepartment = new CLabel(Msg.translate(ctx, "XX_VMR_Department_ID"));
	private CComboBox comboDepartment = new CComboBox();
	//Colección
	private CLabel labelCollection = new CLabel(Msg.translate(ctx, "XX_VMR_Collection_ID"));
	private CComboBox comboCollection = new CComboBox();
	//Paquete
	private CLabel labelPackage = new CLabel(Msg.translate(ctx, "XX_VMR_Package_ID"));
	private CComboBox comboPackage = new CComboBox();
	//Producto
	protected CLabel labelProduct = new CLabel(Msg.translate(ctx, "M_Product_ID"));
	protected VLookup lookupProduct = null;
	//Almacén
	private CLabel labelWarehouse = new CLabel(Msg.translate(ctx, "XX_Store"));
	private CComboBox comboWarehouse = new CComboBox();
	//Ubicación
	private CLabel labelLocator = new CLabel(Msg.translate(ctx, "M_Locator_ID"));
	private CComboBox comboLocator = new CComboBox();
	//Orden de Compra
	private CLabel labelPurchaseOrder = new CLabel(Msg.translate(ctx, "C_Order_ID"));
	private VLookup lookupPurchaseOrder = null;	
	//Estado O/C
	private CLabel labelOPurchaseStatus= new CLabel(Msg.translate(ctx,"XX_OrderStatus"));
	private CComboBox comboOPurchaseStatus = new CComboBox();
	private Vector<String> OPurchaseStatus_name = new Vector<String>();
	//Fecha Estimada de Llegada
	private CLabel labelEstimatedDate = new CLabel(Msg.translate(ctx, "XX_EstimatedDate"));
	private VDate EstimatedDate = new VDate();

	/*Seleccionar todos en la tabla*/
	private CLabel labelSelectAll = new CLabel("Seleccionar Todos");
	private CCheckBox checkSelectAll = new CCheckBox();
	private CPanel checkPanel = new CPanel();
	private FlowLayout checkLayout = new FlowLayout();
	
	/* Botones */
	private CButton bSearch = new CButton(Msg.translate(ctx, "XX_Search"));
	private CButton bReset = new CButton(Msg.translate(ctx, "XX_ClearFilter"));
	protected CButton bCalWeighted = new CButton("Calcular Ponderado");
	protected CButton bPrint = new CButton(Msg.translate(ctx, "ExportExcel"));
	protected VFile bFile = new VFile("File", Msg.getMsg(ctx, "File"),true, false, true, true, JFileChooser.SAVE_DIALOG);
	
	/** La tabla donde se guardarán los datos de las ordenes de compra o productos que cumplen con los filtros*/
	protected MiniTablePreparator miniTable = new MiniTablePreparator();
	
	/** La tabla donde se guardarán el ponderado de días de inventario  de las ordenes de compra o productos seleccionados*/
	protected MiniTablePreparator miniTable2 = new MiniTablePreparator();

	private CLabel labelTotalWeightedInventoryDays = new CLabel(Msg.translate(Env.getCtx(), "Amount")+ " " +Msg.translate(Env.getCtx(),"XX_WeightedInventoryDays")+":");
	private CLabel totalWeightedInventoryDays = new CLabel();
	private Double totalInvDaysAccumulated = 0.0;
	private Double totalPiecesAccumulated = 0.0;
	private Integer maxBySql = 990;

	private Integer CD_CHEQUEADO = Env.getCtx().getContextAsInt("#XX_L_LOCATORCDCHEQUEADO_ID");

	
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	} // dispose

	/** Inicializar la forma */
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		
		try {		
			//Se configura el lookup de orden de compra para que esté deseleccionado el check de Transacción de Ventas
			Env.getCtx().setIsSOTrx(m_WindowNo, false);
			//Creando Lookup para orden de compra
			Lookup l = MLookupFactory.get(Env.getCtx(), m_WindowNo, Env
					.getCtx().getContextAsInt("#XX_L_C_ORDERCOLUMN_ID"),
					DisplayTypeConstants.Search);
			lookupPurchaseOrder = new VLookup("C_Order_ID", false, false, true, l);
			lookupPurchaseOrder.setVerifyInputWhenFocusTarget(false);
			//Creando Lookup para producto
			lookupProduct = VLookup.createProduct(m_WindowNo);	
			option = PRODUCT;
			jbInit();
			dynInit();
		//	frame.getContentPane().add(commandPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);	
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);	

		} catch(Exception e) {
			log.log(Level.SEVERE, "", e);
		}	
	}
	
	/** Inicializador de los datos por defecto */
	private void dynInit () {
		removeActionListeners();		
		uploadBasicInfo();		
		addActionListeners();
	}
	
	/** Inicialización de Campos */
	private final void jbInit() throws Exception {
		
		CompiereColor.setBackground(this);		
		//m_frame.getRootPane().setDefaultButton(bSearch);
		mainPanel.setLayout(mainLayout);

		//Creación del panel principal
		parameterPanel.setLayout(parameterLayout);
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		
		
		miniTable.setRowHeight(miniTable.getRowHeight() + 2);
		miniTable2.setRowHeight(miniTable2.getRowHeight() + 2);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(centerLayout);
		centerPanel.add(dataPane, BorderLayout.CENTER);
		centerPanel.add(dataPane2, BorderLayout.SOUTH);
		dataPane.getViewport().add(miniTable, null);		
		dataPane2.getViewport().add(miniTable2, null);	
		dataPane.setPreferredSize(new Dimension(900, 300));
		dataPane2.setPreferredSize(new Dimension(900, 300));
		dataPane2.setBorder(xBorder);
		//Panel Inferior
		
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		commandPanel.add(labelTotalWeightedInventoryDays,0);
		commandPanel.add(totalWeightedInventoryDays,1);
		commandPanel.add(bCalWeighted, null);
		commandPanel.add(bSearch, null);
		commandPanel.add(bReset, null);	
		commandPanel.add(bFile, null);
		commandPanel.add(bPrint, null);
		mainPanel.add(commandPanel, BorderLayout.SOUTH);

		totalWeightedInventoryDays.setVisible(false);
		labelTotalWeightedInventoryDays.setVisible(false);
	   // Group the radio buttons.
		buttonGroup.add(rPurchOrder);
		buttonGroup.add(rProduct);

	   
      // Panel of radio buttons.
		panelGroup.add(rProduct);
		panelGroup.add(rPurchOrder);



	   addParameter(panelGroup, 1 ,0, 2);   
	   addParameters();
	   setOptionParameters();
		//Agregar los action listeners
	   addActionListeners();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		//Desactivar los efectos de las acciones 
		removeActionListeners();
		//Se actualizan los departamentos una vez se ha modificada la categoría 
		if (e.getSource() == rPurchOrder){
			if(rPurchOrder.isSelected()){
				option = PURCHASEORDER;
		
			}
			uploadBasicInfo();
		}//Calcular Ponderado
		else if (e.getSource() == bCalWeighted)
		{
			getSelected();
			if(miniTable2.getRowCount()<1){
				bPrint.setEnabled(false);
				bFile.setReadWrite(false);
			}else {
				bPrint.setEnabled(true);
				bFile.setReadWrite(true);
			}
		
		}
		else if (e.getSource() == rProduct ){
			if(rProduct.isSelected()){
				option = PRODUCT;
			}
			uploadBasicInfo();
		}
		else if (e.getSource() == comboCategory ){
			KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		//	if (catg != null && catg.getKey() != 0 && catg.getKey() != 99999999){ 
			if (catg != null  && catg.getKey() != 99999999){ 
				set(comboDepartment, true);		
				uploadDepartments();
			} else if(catg.getKey() == 99999999){
				comboDepartment.setSelectedIndex(0);
				set(comboDepartment, false);				
			}	
		}
		else if (e.getSource() == comboCollection ){
			KeyNamePair collection = (KeyNamePair)comboCollection.getSelectedItem();
		//	if (collection!= null && collection.getKey() != 0 && collection.getKey() != 99999999){ 
			if (collection != null  && collection.getKey() != 99999999){ 
				set(comboPackage, true);
			} else if(collection.getKey() == 99999999){
				comboPackage.setSelectedIndex(0);
				set(comboPackage, false);				
			}
			uploadPackages();
		}
		else if (e.getSource() == comboWarehouse ){
			KeyNamePair warehouse = (KeyNamePair)comboWarehouse.getSelectedItem();
			if (warehouse != null  &&  warehouse.getKey() != 99999999 ){ //&& warehouse.getKey() != 0){ 
				set(comboLocator, true);
			} else if(warehouse.getKey() == 99999999 ){
				comboLocator.setSelectedIndex(0);
				set(comboLocator, false);	
				}	
			uploadLocator();
		}
		//Limpiar el filtro
		else if(e.getSource() == bReset){
			resetFilter();
		}
		//Execute Search
		else if(e.getSource() == bSearch) {
			try {
				checkSelectAll.setValue(false);
				totalWeightedInventoryDays.setText("");
				llenarTabla();	
				bCalWeighted.setEnabled(true);
				if(miniTable.getRowCount()>0){
					labelSelectAll.setVisible(true);
					checkPanel.setVisible(true);
				}
				else{
					labelSelectAll.setVisible(false);
					checkPanel.setVisible(false);
				}
				if(option == PURCHASEORDER){
					statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_POListedQty"));
					statusBar.setStatusDB(miniTable.getRowCount());
				}else if(option == PRODUCT){
					statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_ProdListedQty"));
					statusBar.setStatusDB(miniTable.getRowCount());
				}	
			} catch (NullPointerException n) {
			}	
		} 
		//seleccionar todos los elementos de la tabla
		else if(e.getSource() == checkSelectAll) {
			if((Boolean)checkSelectAll.getValue()){
				selectAll(true);	
			}else 	selectAll(false);	
		} 
		//Imprimir la seleccion
		else if (e.getSource() == bPrint)
		{
			try {
				XX_Indicator.imprimirArchivo(miniTable2, bFile, m_WindowNo, m_frame); 
			} catch (Exception ex) {
				log.log(Level.SEVERE, "", ex);
			}			
		}
		m_frame.setCursor(Cursor.getDefaultCursor());	
		addActionListeners();
	}	
	
	
	/** Selecciona todos los elementos de la tabla */
	private void selectAll(Boolean selected) {

		if(selected){
			for(int j=0; j<miniTable.getRowCount(); j++){
					miniTable.setValueAt(selected, j, 0);	
			}
		}else if(!selected){
			for(int j=0; j<miniTable.getRowCount(); j++){
				miniTable.setValueAt(selected, j, 0);	
			}
		}
	}

	/** Agrega los filtros al panel superior */
	private void addParameters() {

		addParameter(labelPurchaseOrder,1,4,1);
		addParameter(lookupPurchaseOrder,1,5,1);
		addParameter(labelOPurchaseStatus,2,0,1);
		addParameter(comboOPurchaseStatus,2,1,1);
		addParameter(labelEstimatedDate,2,2,1);
		addParameter(EstimatedDate,2,3,1);
		
		addParameter(labelProduct,1,4,1);
		addParameter(lookupProduct,1,5,1);
		addParameter(labelWarehouse,2,0,1);
		addParameter(comboWarehouse,2,1,1);
		addParameter(labelLocator,2,2,1);
		addParameter(comboLocator,2,3,1);
		
		addParameter(labelCategory,2,4,1);
		addParameter(comboCategory,2,5,1);
		addParameter(labelDepartment,3,0,1);
		addParameter(comboDepartment,3,1,1);
		addParameter(labelCollection,3,2,1);
		addParameter(comboCollection,3,3,1);
		addParameter(labelPackage,3,4,1);
		addParameter(comboPackage,3,5,1);
		
		checkPanel.setLayout(checkLayout);
		checkLayout.setAlignment(FlowLayout.LEFT);
		checkPanel.add(checkSelectAll);
		addParameter(labelSelectAll,4,0,1);
		addParameter(checkPanel,4,1,1);

	}

	/**Dependiendo de la opción seleccionada se muestraran los filtros correspondientes*/
	private void setOptionParameters() {

		if(option == PURCHASEORDER){
			set(lookupProduct,false,false);
			set(comboWarehouse,false,false, 0);
			set(comboLocator,false,false, 0);
			set(lookupPurchaseOrder, true,true);
			set(comboOPurchaseStatus,true);
			EstimatedDate.setVisible(true);
			set(labelProduct,false);
			set(labelWarehouse,false);
			set(labelLocator,false);
			set(labelPurchaseOrder,true);
			set(labelEstimatedDate,true);
			set(labelOPurchaseStatus,true);
		}else if (option == PRODUCT){
			set(lookupProduct,true,true);
			set(comboWarehouse,true);
			set(comboLocator,true);
			set(lookupPurchaseOrder, false, false);
			set(comboOPurchaseStatus,false,false, 0);
			EstimatedDate.setVisible(false);
			set(labelProduct,true);
			set(labelWarehouse,true);
			set(labelLocator,true);
			set(labelPurchaseOrder,false);
			set(labelEstimatedDate,false);
			set(labelOPurchaseStatus,false);
		}
		
	}

	/** Activa todos los listeners creados */
	protected final void addActionListeners () {
		
		comboCategory.addActionListener(this);	
		comboCollection.addActionListener(this);
		comboWarehouse.addActionListener(this);
		bCalWeighted.addActionListener(this);
		bReset.addActionListener(this);
		bSearch.addActionListener(this);
		bPrint.addActionListener(this);
	    rPurchOrder.addActionListener(this);
	    rProduct.addActionListener(this);
	    checkSelectAll.addActionListener(this);
	} 
	
	/** Deshabilitar Action Listeners */
	protected final void removeActionListeners () {
		
		comboCategory.removeActionListener(this);	
		comboCollection.removeActionListener(this);
		comboWarehouse.removeActionListener(this);
		bCalWeighted.removeActionListener(this);
		bReset.removeActionListener(this);
		bSearch.removeActionListener(this);
		bPrint.removeActionListener(this);
	    rPurchOrder.removeActionListener(this);
	    rProduct.removeActionListener(this);
	    checkSelectAll.removeActionListener(this);

	}	
	
	/** Método invocado al presionar el botón de limpiar filtro */
	protected void resetFilter() { 		
		uploadBasicInfo();
	}
	
	/** Se inicializan los datos en los filtros y tablas  */
	public final void uploadBasicInfo() {
		
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		miniTable2.setRowCount(0);
		miniTable2 = new MiniTablePreparator();
		
		dataPane.getViewport().add(miniTable, null);		
		dataPane2.getViewport().add(miniTable2, null);	
		
		bCalWeighted.setEnabled(false);
	
		uploadCategory();
		uploadDepartments();
		uploadCollection();
		uploadPackages();
	
		//Restore ComboBoxes and CheckBoxes		
		if(option == PURCHASEORDER){
			EstimatedDate.setValue(null);
			uploadStatusPurchaseOrder();
		}else if(option == PRODUCT){
			uploadWarehouse();
			uploadLocator();
		}
		
		setOptionParameters();
		labelSelectAll.setVisible(false);
		set(comboPackage, true);
		set(comboDepartment, true);
		labelSelectAll.setVisible(false);
		checkPanel.setVisible(false);
		set(checkSelectAll, true, false);
		bPrint.setEnabled(false);
		bFile.setValue(null);
		bFile.setReadWrite(false);
		
		totalWeightedInventoryDays.setText("");
		
		if(option == PURCHASEORDER){
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_POListedQty"));
			statusBar.setStatusDB(miniTable.getRowCount());
		}else if(option == PRODUCT){
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_ProdListedQty"));
			statusBar.setStatusDB(miniTable.getRowCount());
		}	
		//Actualizar los cambios
		repaint();

	}
	

	/** Llena la tabla con las O/C o productos que cumplen con los filtros obtenidos del query*/
	protected void llenarTabla() {
			
			//Si no se ha cargado el header previamente
			miniTable2.setRowCount(0);
			miniTable2 = new MiniTablePreparator();
			
			//Si no se ha cargado el header previamente
			miniTable.setRowCount(0);
			miniTable = new MiniTablePreparator();

			//Calcular el query
			try {
				if(option==PRODUCT){
					calculateQueryProduct();
				}else if(option==PURCHASEORDER){
					calculateQueryPurchaseOrder();
				}			
				
				miniTable.setRowSelectionAllowed(true);
				miniTable.setSelectionBackground(Color.white);
			//	miniTable.autoSize();
				miniTable.setAutoResizeMode(4);
				miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
				miniTable.getTableHeader().setReorderingAllowed(false);			
			} catch (Exception e) {
				
			}
		}
	
	/** Llena la tabla de resultados de O/C o productos seleccionados con los datos obtenidos del query*/
	protected void llenarTabla2() {
		
			//Si no se ha cargado el header previamente
			miniTable2.setRowCount(0);
			miniTable2 = new MiniTablePreparator();

			//Calcular el query
			try {
				if(option==PRODUCT){
					calculateQueryProductWeighted();
				}else if(option==PURCHASEORDER){
					calculateQueryPurchaseOWeighted();
				}			
				//miniTable2.repaint();
				//dataPane2.repaint();
				miniTable2.setRowSelectionAllowed(true);
				miniTable2.setSelectionBackground(Color.white);
			//	miniTable.autoSize();
				miniTable2.setAutoResizeMode(4);
				miniTable2.setRowHeight(miniTable2.getRowHeight() + 2 );
				miniTable2.getTableHeader().setReorderingAllowed(false);	

			} catch (Exception e) {}
		}
	
	
	/** Se crea el query que devuelve las 0/C que tengan piezas en CD de acuerdo a los parámetros ingresados en el filtro de la opcion Orden de Compra*/
	public void calculateQueryPurchaseOrder() {

		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
		Integer status = comboOPurchaseStatus.getSelectedIndex();
		Integer order= (Integer)lookupPurchaseOrder.getValue();	
		Timestamp date = (Timestamp) EstimatedDate.getValue();
		
		String with_cheq = ""; 
		String with_recib = ""; 
		String where="";
		String sql= ""; 
		String select= ""; 
		
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();

		//Agregar columnas 
		columnasAgregadas.add(colcheck);
		columnasAgregadas.add(colOrder);
		columnasAgregadas.add(colStatus);
		columnasAgregadas.add(colCatg);
		columnasAgregadas.add(colDept);
		columnasAgregadas.add(colColle);
		columnasAgregadas.add(colPack);
		columnasAgregadas.add(colDate);
		
		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);
		
		select = "\n" + miniTable.prepareTable(layout, null, null, false, null);
		
		//Categoría
		if(catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
			if (catg.getKey()!= 99999999 ) {
				if (where.isEmpty())
					where += "\nWHERE c.XX_VMR_Category_ID = " + catg.getKey();
				else 
					where += "\nAND c.XX_VMR_Category_ID = " + catg.getKey();
			}
		}	
		//Departamento			
		if(dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {
			if (dept.getKey()!= 99999999 ) {
				if (where.isEmpty())
					where += "\nWHERE d.XX_VMR_Department_ID = " + dept.getKey();
				else
					where += "\nAND d.XX_VMR_Department_ID = " + dept.getKey();
			}
		}
		//Collección
		if(coll != null && (coll.getKey()==99999999 || coll.getKey()!= 0)) {
			if (coll.getKey()!= 99999999 ) {
				if (where.isEmpty()){
					where += "\nWHERE o.XX_VMR_Collection_ID = " + coll.getKey();
				}else { 
					where += "\nAND o.XX_VMR_Collection_ID = " + coll.getKey();
				}
			}
		}	
		//Paquete 
		if(pack != null && (pack.getKey()==99999999 ||pack.getKey()!= 0 )){	
			if (pack.getKey()!= 99999999 ) {
					if (where.isEmpty()){
						where += "\nWHERE o.XX_VMR_Package_ID = " + pack.getKey();
					}else { 
						where+= "\nAND o.XX_VMR_Package_ID = " + pack.getKey();
					}
			}
		}
		
		//Orden de Compra
		if(order != null ){
			if (where.isEmpty())
				where += "\nWHERE o.C_Order_ID= " +order;
				
			else
				where += "\nAND o.C_Order_ID= " +order;
		}
		if(date!= null ){
			if (where.isEmpty()){
				where += "\nWHERE TRUNC(o.XX_EstimatedDate,'DD')  = " +DB.TO_DATE(date,true) +" ";
			}else {
				where += "\nAND TRUNC(o.XX_EstimatedDate,'DD')  = " +DB.TO_DATE(date,true) +" ";		
			}
		}
		if(status == null || status == 0){
			if (where.isEmpty())
				where += "\nWHERE o.XX_OrderStatus in ('CH','RE') AND o.isSOTRX = 'N' ";
			else where += "\nAND o.XX_OrderStatus in ('CH','RE') AND o.isSOTRX = 'N' ";
		}else if (status == 1) {
	
			if (where.isEmpty())
				where += "\nWHERE o.XX_OrderStatus in ('CH','RE') AND o.isSOTRX = 'N' ";
			else where += "\nAND o.XX_OrderStatus in ('CH','RE') AND o.isSOTRX = 'N' ";
		}else {
			if (where.isEmpty())
				where += "\nWHERE o.XX_OrderStatus = '" + OPurchaseStatus_name.elementAt(status)+"' AND o.isSOTRX = 'N' ";
			else where += "\nAND o.XX_OrderStatus = '" + OPurchaseStatus_name.elementAt(status)+"' AND o.isSOTRX = 'N' ";
		}
		if (where.isEmpty())
			where += "\nWHERE o.isSOTRX = 'N' AND o.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" ";
		else where += "\nAND o.isSOTRX = 'N' AND  o.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" ";
		
		
		//Si el estado de la O/C seleccionado en el filtro es distinto de RE (Recibidas)
		if(OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())!="RE"){
		
			with_cheq += "WITH " +
			//Total Piezas de O/C Chequeadas Pre-distribuidas que están en tienda
		   "\nEN_TIENDA_PD AS " +
			    "\n(" +
			    "\nSELECT o.C_Order_ID, SUM(de.XX_ProductQuantity) cantTienda " +
				"\nFROM  C_Order o INNER JOIN XX_VMR_Order pe on (o.C_Order_ID = pe.C_Order_ID) " +
			    	"\nINNER JOIN XX_VMR_OrderRequestDetail de on (de.XX_VMR_Order_ID = pe.XX_VMR_Order_ID)  " +
			    "\nWHERE pe.XX_OrderRequestStatus = 'TI' AND o.isSOTRX = 'N' " +
				    "\nAND o.XX_OrderStatus ='CH' " +
				    "\nAND o.XX_VLO_TypeDelivery = 'PD' ";
		   with_cheq  += "\nGROUP BY o.C_Order_ID " +
		   		"\n),";
				//Total Piezas de O/C Chequeadas Pre-distribuidas 
		   with_cheq  +=
			   "\nEN_OC_PD AS " +
			    "\n(" +
			    "\nSELECT o.C_Order_ID, SUM(iol.PickedQty)-SUM(iol.ScrappedQty) cantOC " +
			    "\nFROM  C_Order o INNER JOIN M_InOut io on (io.C_Order_ID = o.C_Order_ID) " +
			    	"\nINNER JOIN M_InOutLine iol on (iol.M_INOUT_ID = io.M_INOUT_ID) " +
			    "\nWHERE o.XX_OrderStatus ='CH' AND o.isSOTRX = 'N' AND io.isSOTRX = 'N' " +
			    	"\nAND o.XX_VLO_TypeDelivery = 'PD' ";
		   with_cheq   += "\nGROUP BY o.C_Order_ID " +
	   			"\n),";
			//Total Piezas de O/C en estado Chequeada  de tipo Centro de Distribución que están en tienda
		   with_cheq +=  
			   "EN_TIENDA_CD AS " +
		    	"\n(" +
		    	"\nSELECT  o.C_Order_ID, SUM(de.XX_ProductQuantity) cantTienda " +
		    	"\nFROM C_Order o INNER JOIN M_InOut io on (io.C_Order_ID = o.C_Order_ID) " +
		    		"\nINNER JOIN M_InOutLine iol on (iol.M_INOUT_ID = io.M_INOUT_ID) " +
		    		"\nINNER JOIN XX_VMR_OrderRequestDetail de on (iol.M_Product_ID = de.M_Product_ID) " +
		    		"\nINNER JOIN  XX_VMR_Order pe on (de.XX_VMR_Order_ID = pe.XX_VMR_Order_ID) " +
		    	"\nWHERE o.XX_OrderStatus ='CH' AND o.isSOTRX = 'N' AND io.isSOTRX = 'N' " +
		    		"\nAND o.XX_VLO_TypeDelivery = 'CD' " +
		    		"\nAND pe.XX_OrderRequestStatus = 'TI' " +
		    		"\nAND pe.XX_DATESTATUSONSTORE > o.XX_CHECKUPDATE ";
		    with_cheq   += "\nGROUP BY o.C_Order_ID " +
	   		"\n),";
		    	//Total Piezas de O/C  en estado Chequeada de tipo Centro de Distribución
		    with_cheq  +=
		    	"\nEN_OC_CD AS " +
		    	"\n(" +
		    	"\nSELECT o.C_Order_ID, SUM(iol.PickedQty)-SUM(iol.ScrappedQty) cantOC " +
		    	"\nFROM  C_Order o INNER JOIN M_INOUT io on (io.C_Order_ID = o.C_Order_ID) " +
		    		"\nINNER JOIN M_INOUTLINE iol on (iol.M_INOUT_ID = io.M_INOUT_ID) " +
		    	"\nWHERE o.XX_OrderStatus ='CH' AND o.isSOTRX = 'N' AND io.isSOTRX = 'N' " +
		    	"\nAND o.XX_VLO_TypeDelivery = 'CD' ";
		    with_cheq   += "\nGROUP BY o.C_Order_ID " +
	   		"\n),";
		   
		 //Total Piezas de O/C menos total piezas en tienda en estado Chequeada de tipo Pre-distribuida 
		    with_cheq  += 
		    	"\nCHEQUEADAS_PD AS " +
			    "\n(" +
			    "\nSELECT oc.C_Order_ID ord, "+
			    	"\n(CASE WHEN ti.cantTienda is null THEN oc.cantOC " +
			    	"\nWHEN oc.cantOC - ti.cantTienda < 0  THEN 0 " +
			    	"\nELSE (oc.cantOC - ti.cantTienda) END) TotalPieces " +
			    "\nFROM EN_OC_PD oc " +
			    "\nLEFT JOIN EN_TIENDA_PD ti ON (oc.C_Order_ID = ti.C_Order_ID) ";
		    with_cheq += "\n),";
		  //Total Piezas de O/C menos total piezas en tienda en estado Chequeada de tipo En Centro de Distribución
		    with_cheq  += 
		    	"\nCHEQUEADAS_CD AS " +
			    "\n(" +
			    "\nSELECT oc.C_Order_ID ord, " +
			    	"\n(CASE WHEN ti.cantTienda is null THEN oc.cantOC " +
			    	"\nWHEN oc.cantOC - ti.cantTienda < 0  THEN 0 " +
			    	"\nELSE (oc.cantOC - ti.cantTienda) END) TotalPieces " +
			    "\nFROM EN_OC_CD oc " +
			    "\nLEFT JOIN EN_TIENDA_CD ti ON (oc.C_Order_ID = ti.C_Order_ID) ";
		    with_cheq += "\n)";
	
		}
		
		//Si el estado de la O/C seleccionado en el filtro es diferente a CH (Chequeadas)
		if(OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())!="CH"){
			 //Total Piezas de O/C en estado Recibida
			with_recib += 
				"\nOC_RECIBIDAS AS" +
				"\n(" +
				"\nSELECT oc.C_Order_ID ord, "+
					"\nSUM(oc.XX_ProductQuantity) TotalPieces " +
				"\nFROM  C_Order oc " +
				"\nWHERE oc.XX_OrderStatus ='RE' AND oc.isSOTRX = 'N' ";
			with_recib += "\nGROUP BY oc.C_Order_ID " +
   			"\n)";
		}
		
		//Si el estado de la O/C seleccionado en el filtro es igual a CH (Chequeadas)
		if(OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())=="CH"){
			sql =  with_cheq + select +
			
		   		"\nFROM (SELECT *  from CHEQUEADAS_CD WHERE TotalPieces >  0 " +
		   		"\nUNION ALL " +
		   		"\nSELECT * from CHEQUEADAS_PD  WHERE TotalPieces > 0 ) " +
		   		"\nINNER JOIN C_Order o ON (ord = o.C_Order_ID) " + 
		    	"\nLEFT JOIN XX_VMR_Category c ON (o.XX_VMR_Category_ID = c.XX_VMR_Category_ID) " +
		    	"\nLEFT JOIN XX_VMR_Department d ON (o.XX_VMR_Department_ID = d.XX_VMR_Department_ID) " +
		    	"\nLEFT JOIN XX_VMR_Collection co ON (o.XX_VMR_Collection_ID = co.XX_VMR_Collection_ID) " +
		    	"\nLEFT JOIN XX_VMR_Package pa ON (o.XX_VMR_Package_ID = pa.XX_VMR_Package_ID)";
			sql += where +" "+
		    	"\nGROUP BY  o.DocumentNo, TO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY'),  c.Value||'-'||c.Name, d.Value||'-'||d.Name, co.Name, pa.Name, o.XX_OrderStatus, o.XX_CHECKUPDATE, o.XX_RECEPTIONDATE" +
		   		"\nORDER BY o.DocumentNo, TO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY'),  c.Value||'-'||c.Name, d.Value||'-'||d.Name, co.Name, pa.Name, o.XX_OrderStatus";

		//Si el estado de la O/C seleccionado en el filtro es igual a RE (Recibidas)
		}else if(OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())=="RE"){
			sql = "WITH" + with_recib + select + 
					"\nFROM OC_RECIBIDAS " +
					"\nINNER JOIN C_Order o ON (ord = o.C_Order_ID) " + 
			    	"\nLEFT JOIN XX_VMR_Category c ON (o.XX_VMR_Category_ID = c.XX_VMR_Category_ID) " +
			    	"\nLEFT JOIN XX_VMR_Department d ON (o.XX_VMR_Department_ID = d.XX_VMR_Department_ID) " +
			    	"\nLEFT JOIN XX_VMR_Collection co ON (o.XX_VMR_Collection_ID = co.XX_VMR_Collection_ID) " +
			    	"\nLEFT JOIN XX_VMR_Package pa ON (o.XX_VMR_Package_ID = pa.XX_VMR_Package_ID)";
			sql += where +" "+
			    	"\nGROUP BY o.DocumentNo, TO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY'),  c.Value||'-'||c.Name, d.Value||'-'||d.Name, co.Name, pa.Name, o.XX_OrderStatus, o.XX_CHECKUPDATE, o.XX_RECEPTIONDATE" +
			   		"\nORDER BY o.DocumentNo, TO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY'),  c.Value||'-'||c.Name, d.Value||'-'||d.Name, co.Name, pa.Name, o.XX_OrderStatus";
			
			
		//Si no se seleccionó ningún estado de la O/C o se seleccionó Todos los estados 
		}else {
			sql = with_cheq + ", " + with_recib + select +
	   		"\nFROM (SELECT *  " +
	   			  "\nFROM (SELECT * FROM CHEQUEADAS_CD WHERE TotalPieces > 0 " +
	   				      "\nUNION ALL " +
	   					  "\nSELECT * FROM CHEQUEADAS_PD  WHERE TotalPieces > 0 )" +
	   			  "\nUNION ALL " +
	   			  "\nSELECT * FROM OC_RECIBIDAS)" +				  
			   		"\nINNER JOIN C_Order o ON (ord = o.C_Order_ID) " + 
			    	"\nLEFT JOIN XX_VMR_Category c ON (o.XX_VMR_Category_ID = c.XX_VMR_Category_ID) " +
			    	"\nLEFT JOIN XX_VMR_Department d ON (o.XX_VMR_Department_ID = d.XX_VMR_Department_ID) " +
			    	"\nLEFT JOIN XX_VMR_Collection co ON (o.XX_VMR_Collection_ID = co.XX_VMR_Collection_ID) " +
			    	"\nLEFT JOIN XX_VMR_Package pa ON (o.XX_VMR_Package_ID = pa.XX_VMR_Package_ID) ";
			sql += where +" "+
			    	"\nGROUP BY  o.DocumentNo, TO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY'),  c.Value||'-'||c.Name, d.Value||'-'||d.Name, co.Name, pa.Name, o.XX_OrderStatus, o.XX_CHECKUPDATE, o.XX_RECEPTIONDATE" +
			   		"\nORDER BY o.DocumentNo, TO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY'),  c.Value||'-'||c.Name, d.Value||'-'||d.Name, co.Name, pa.Name, o.XX_OrderStatus";

		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTable.loadTable(rs);
			miniTable.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
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
	
	/** Se crea el query que devuelve los productos de acuerdo a los parámetros ingresados en el filtro de la opcion Producto*/
	public void calculateQueryProduct() {

		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
		KeyNamePair ware = (KeyNamePair)comboWarehouse.getSelectedItem();
		KeyNamePair loca = (KeyNamePair)comboLocator.getSelectedItem();
		Integer product  =  (Integer)lookupProduct.getValue();	
		
		String with = "\nWITH "; 
		String fromCheq =  "";
		String fromRes = "";
		String selectRes = "";
		String whereRes = ""; 
		String selectCheq = "";
		String whereCheq = "";
		String groupByCheq ="";
		String groupByRes ="";
		
		ColumnInfo colProd = new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"), 
				"\nproduct",String.class);
		
		ColumnInfo colWare = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Store"), 
				"\nwarehouse", String.class);
		
		ColumnInfo colLoca = new ColumnInfo(Msg.translate(Env.getCtx(), "M_Locator_ID"), 
				"\nlocat", String.class);
		ColumnInfo colProdCatg = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"), 
				"\ncatg", String.class);
		
		ColumnInfo colProdDept = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),
				"\ndept", String.class);
		
		ColumnInfo colProdColle = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Collection_ID"), 
				"\ncoll", String.class);
		
		ColumnInfo colProdPack = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Package_ID"),
				"\npack", String.class);
		
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();

		
		//Agregar columnas de Check y Producto
	    columnasAgregadas.add(colcheck);
		columnasAgregadas.add(colProd);
		
		//Se obtienen los productos que cumplan con los filtros seleccionados
		selectCheq = "\nCHEQUEADOS AS" +
		"\n(SELECT p.Value||'-'||p.Name product "; 
	    fromCheq += "\nFROM M_Product p " +
	    		"\nINNER JOIN M_Storage s ON (s.M_Product_ID = p.M_Product_ID) " +
	    		"\nINNER JOIN M_Locator l ON (l.M_Locator_ID = s.M_Locator_ID) ";
	    whereCheq += "\nWHERE s.QTYONHAND > 0 ";
	    groupByCheq += "\nGROUP BY p.Value||'-'||p.Name";
	
		selectRes += "\nRECIBIDOS AS" +
		"\n(SELECT " +
		"p.Value||'-'||p.Name product  "; 
		fromRes += "\nFROM  C_Order o " +
		"\nINNER JOIN XX_VMR_PO_LineRefProv lrp ON ( lrp.C_Order_ID = o.C_Order_ID) " +
		"\nINNER JOIN XX_VMR_ReferenceMatrix rm ON ( rm.XX_VMR_PO_LineRefProv_ID = lrp.XX_VMR_PO_LineRefProv_ID) " +
		"\nINNER JOIN M_Product p ON (rm.M_Product = p.M_Product_ID) ";
		whereRes += "\nWHERE o.XX_OrderStatus ='RE' AND o.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" ";
		groupByRes += "\nGROUP BY p.Value||'-'||p.Name";	

		// Se nececita M_Locator y M_Warehouse cuando se filtra por almacén o por ubicación
		if((ware != null && ware.getKey()!=99999999 && ware.getKey()!= 0 ) 
				|| (loca != null && loca.getKey()!=99999999 && loca.getKey()!= 0 )){	
			fromCheq += "\nLEFT JOIN M_Warehouse w ON (w.M_Warehouse_ID = l.M_Warehouse_ID) ";
			columnasAgregadas.add(colWare);
			selectCheq +="\n,w.Value||'-'||w.Name warehouse " ;
			groupByCheq += "\n,w.Value||'-'||w.Name ";
			selectRes +="\n,TO_NCHAR('001-CENTRO DE DISTRIBUCION BOLEITA') warehouse " ;
			groupByRes +="\n,TO_NCHAR('001-CENTRO DE DISTRIBUCION BOLEITA') ";
		}
		//Almacén
		if(ware != null && ware.getKey()!=99999999 && ware.getKey()!= 0 ){	
			whereCheq+= "\nAND l.M_Warehouse_ID = " + ware.getKey();
		}
		//Ubicación
		if(loca != null && loca.getKey()!=99999999 && loca.getKey()!= 0 ){	
			columnasAgregadas.add(colLoca);
			selectCheq +="\n,l.Value locat ";
			groupByCheq += "\n,l.Value ";
			selectRes += "\n,TO_NCHAR('CENTRO DE DISTRIBUCION BOLEITA - RECIBIDO')  locat ";
			groupByRes += "\n,TO_NCHAR('CENTRO DE DISTRIBUCION BOLEITA - RECIBIDO') ";
			if (loca.getKey()!= 99999999 ) {
				whereCheq+= "\nAND s.M_Locator_ID = " + loca.getKey();	
			}
		}
		//Categoría
		if((catg != null && catg.getKey()!=99999999 && catg.getKey()!= 0 ) 
				|| (dept != null && dept.getKey()!=99999999 && dept.getKey()!= 0)){	
			fromCheq += "\nLEFT JOIN XX_VMR_Category c ON (c.XX_VMR_Category_ID = p.XX_VMR_Category_ID) ";
			fromRes +="\nLEFT JOIN XX_VMR_Category c ON (c.XX_VMR_Category_ID = o.XX_VMR_Category_ID) ";
			columnasAgregadas.add(colProdCatg);
			selectCheq += "\n,c.Value||'-'||c.Name catg ";
			groupByCheq += "\n,c.Value||'-'||c.Name ";
			selectRes += "\n,c.Value||'-'||c.Name catg ";
			groupByRes += "\n,c.Value||'-'||c.Name ";
		}
		if(catg != null && catg.getKey()!=99999999 && catg.getKey()!= 0) {	
			whereCheq += "\nAND p.XX_VMR_Category_ID = " + catg.getKey();
			whereRes += "\nAND o.XX_VMR_Category_ID = " + catg.getKey();
		}	
		//Departamento			
		if(dept != null && dept.getKey()!=99999999 &&  dept.getKey()!= 0) {
			fromCheq += "\nLEFT JOIN XX_VMR_Department d ON (d.XX_VMR_Department_ID = p.XX_VMR_Department_ID) "; 
			fromRes += "\nLEFT JOIN XX_VMR_Department d ON (d.XX_VMR_Department_ID = o.XX_VMR_Department_ID) ";
			columnasAgregadas.add(colProdDept);
			selectCheq += "\n, d.Value||'-'||d.Name dept ";
			groupByCheq += "\n, d.Value||'-'||d.Name ";
			selectRes +="\n,d.Value||'-'||d.Name dept ";
			groupByRes += "\n,d.Value||'-'||d.Name ";
			
			if (dept.getKey()!= 99999999 ) {
				whereCheq += "\nAND p.XX_VMR_Department_ID = " + dept.getKey();
				whereRes +=  "\nAND o.XX_VMR_Department_ID = " + dept.getKey();
			}
		}		
		// Se necesita el M_ATTRIBUTESETINSTANCE, XX_VMR_Package y XX_VMR_Collection cuando se filtra por Colección o por paquete
		if((coll != null && coll.getKey()!=99999999 && coll.getKey()!= 0) 
				|| (pack != null && pack.getKey()!=99999999 && pack.getKey()!= 0 )) {
			fromCheq += "LEFT JOIN M_ATTRIBUTESETINSTANCE asi ON (s.M_ATTRIBUTESETINSTANCE_ID = asi.M_ATTRIBUTESETINSTANCE_ID)";
			fromCheq += "\nLEFT JOIN XX_VMR_Package pa ON (pa.XX_VMR_Package_ID = asi.XX_VMR_Package_ID)";
			fromCheq += "\nLEFT JOIN XX_VMR_Collection co ON (co.XX_VMR_Collection_ID = pa.XX_VMR_Collection_ID) ";
			fromRes += "\nLEFT JOIN XX_VMR_Collection co ON (co.XX_VMR_Collection_ID = o.XX_VMR_Collection_ID) " ;
			columnasAgregadas.add(colProdColle);
			selectCheq += "\n,co.Name coll";
			groupByCheq += "\n,co.Name ";
			selectRes +="\n,co.Name coll ";
			groupByRes += "\n,co.Name ";
		}		
		
		//Colección
		if(coll != null && coll.getKey()!=99999999 && coll.getKey()!= 0) {
			whereCheq += "\nAND pa.XX_VMR_Collection_ID = " + coll.getKey();
			whereRes += "\nAND o.XX_VMR_Collection_ID = " + coll.getKey();
			
		}	
		//Paquete 
		if(pack != null && pack.getKey()!=99999999 && pack.getKey()!= 0 ){	
			fromRes += "\nLEFT JOIN XX_VMR_Package pa ON (pa.XX_VMR_Package_ID = o.XX_VMR_Package_ID) " ;
			columnasAgregadas.add(colProdPack);
			selectCheq +="\n,pa.Name pack ";
			groupByCheq += "\n,pa.Name ";
			selectRes +="\n,pa.Name pack ";
			groupByRes += "\n,pa.Name ";
			
			if (pack.getKey()!= 99999999 ) {
				whereCheq+= "\nAND asi.XX_VMR_Package_ID = " + pack.getKey();
				whereRes+= "\nAND o.XX_VMR_Package_ID = " + pack.getKey();
			}
		}
		//Producto
		if(product != null ){
			whereCheq += "\nAND p.M_Product_ID= " +product;
			whereRes += "\nAND p.M_Product_ID= " +product;
		}
		// en M_Locator si es alguna tienda IsDefault = Y es q esta en estado EN TIENDA con excepción al Centro de Distribución
		// por lo que se toma IsDefault = N para obtener solo M_Locator de tiendas en estado EN TRANSITO con excepcion al CD
		if (whereCheq.isEmpty()){
			whereCheq += "\n l.IsDefault =(CASE WHEN l.M_Locator_ID = " +
			Env.getCtx().getContextAsInt("#XX_L_LOCATORCDCHEQUEADO_ID")+" THEN 'Y' ELSE 'N' END)";
		}else { 
			whereCheq += "\nAND l.IsDefault =(CASE WHEN l.M_Locator_ID = " +
			Env.getCtx().getContextAsInt("#XX_L_LOCATORCDCHEQUEADO_ID")+" THEN 'Y' ELSE 'N' END)";
		}
		
		//Cliente 
		if (whereCheq.isEmpty())
			whereCheq += "\np.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" ";
		else whereCheq += "\nAND p.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" ";
		
		
		//Arreglo de Columnas que se mostraran en la tabla
		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);
		String select = "\n" + miniTable.prepareTable(layout, null, null, false, null);
		
		String sqlCheq = selectCheq + fromCheq + whereCheq + groupByCheq +"\n)";
		String sqlRes = selectRes + fromRes + whereRes + groupByRes +")";
		
		String sql = "";
		if(loca.getKey()==-1){
			with += sqlRes;
			sql = with + select +"\nFROM RECIBIDOS";
		}
		else if(loca.getKey()!=-1 && loca.getKey()!=99999999 && loca.getKey()!=0){
			with += sqlCheq;
			sql = with + select +"\nFROM CHEQUEADOS";
		}
		else if(ware.getKey()!=Env.getCtx().getContextAsInt("XX_L_WAREHOUSECENTRODIST_ID") && ware.getKey()!= 99999999 && ware.getKey()!= 0){
			with += sqlCheq;
			sql = with + select +"\nFROM CHEQUEADOS";
		}else {
			with += sqlCheq +"," +sqlRes;
			String sqlUnion = with + "\n, UNIONALL AS (SELECT * FROM RECIBIDOS UNION SELECT * FROM CHEQUEADOS)";
			sql = sqlUnion + select + "\nFROM UNIONALL";
		}
	
		//System.out.println("SQL: "+sql);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTable.loadTable(rs);
			miniTable.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
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
	/** Se determina el ponderado de dias de inventario de las O/C seleccionadas*/
	public void calculateQueryPurchaseOWeighted() {
		
		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable2.setRowCount(0);
		miniTable2 = new MiniTablePreparator();
		dataPane2.getViewport().add(miniTable2);

		//Se reinicia el acumulado del total del ponderado de días de inventario de las o/c selecionadas. 
		totalInvDaysAccumulated = 0.0;
		totalPiecesAccumulated = 0.0;
	
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
		Timestamp date = (Timestamp) EstimatedDate.getValue();

		String select= ""; 
		String where="";
		
	 //Categoría
		if(catg != null && catg.getKey()!=99999999 && catg.getKey()!= 0 ) {	
				if (where.isEmpty()){
					where += "\nWHERE o.XX_VMR_Category_ID = " + catg.getKey();
				}else { 
					where += "\nAND o.XX_VMR_Category_ID = " + catg.getKey();					
				}
		}	
		//Departamento			
		if(dept != null && dept.getKey()!=99999999 &&  dept.getKey()!= 0) {
				if (where.isEmpty()){
					where += "\nWHERE o.XX_VMR_Department_ID = " + dept.getKey();	
				}
				else{
					where += "\nAND o.XX_VMR_Department_ID = " + dept.getKey();	
				}
			
		}
		//Collección
		if(coll != null && coll.getKey()!=99999999 && coll.getKey()!= 0) {

				if (where.isEmpty()){
					where += "\nWHERE o.XX_VMR_Collection_ID = " + coll.getKey();
				}else { 
					where +=  "\nAND o.XX_VMR_Collection_ID = " + coll.getKey();
				}
		}	
		//Paquete 
		if(pack != null && pack.getKey()!=99999999 && pack.getKey()!= 0 ){			
					if (where.isEmpty()){
						where += "\nWHERE o.XX_VMR_Package_ID = " + pack.getKey();
					}else { 
						where += "\nAND o.XX_VMR_Package_ID = " + pack.getKey();
					}
		}
		if(date!= null ){
			if (where.isEmpty()){
				where += "\nWHERE TRUNC(o.XX_EstimatedDate) = " +DB.TO_DATE(date, true) +" ";
				
			}else{
				where += "\nAND TRUNC(oc.XX_EstimatedDate) = " +DB.TO_DATE(date, true) +" ";
			}
		}
		
		//Cliente
		if (where.isEmpty())
			where += "\nWHERE o.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" ";
		else where += "\nAND o.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" ";
		
		
		select = "\n" + miniTable2.prepareTable(columnOrderPurchase, null, null, false, null);

		Integer desde = 0;
		Integer hasta = 0;
		String selecOC = null;
		while(desde < selected.size()){
			
			String with_cheq = ""; 
			String with_recib = ""; 
			String sql= ""; 
			String sqlTotal = "";
			
			hasta = desde + maxBySql;
			Integer desdeTemp = desde;
			//Se obtiene un string con las O/C seleccionadas
			selecOC = "(0";
			for (int i = desde;  i <= hasta && i < selected.size(); i++) {
				
				selecOC += ", "+selected.get(i);
				desdeTemp++;
			}
			selecOC += ")";
			
			
			//Si el estado de la O/C seleccionado en el filtro es distinto de RE (Recibidas)
			if(OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())!="RE"){
			
				with_cheq += "WITH " +
				//Total Piezas de O/C Chequeadas Pre-distribuidas que están en tienda
			   "\nEN_TIENDA_PD AS " +
				    "\n(" +
				    "\nSELECT o.C_Order_ID, SUM(de.XX_ProductQuantity) cantTienda " +
					"\nFROM  C_Order o INNER JOIN XX_VMR_Order pe on (o.C_Order_ID = pe.C_Order_ID) " +
				    	"\nINNER JOIN XX_VMR_OrderRequestDetail de on (de.XX_VMR_Order_ID = pe.XX_VMR_Order_ID)  " +
				    "\nWHERE pe.XX_OrderRequestStatus = 'TI' " +
					    "\nAND o.XX_OrderStatus ='CH' " +
					    "\nAND o.XX_VLO_TypeDelivery = 'PD' ";
			   if(selected.size()>0 ){
				   with_cheq += "\nAND  o.DocumentNo in " +selecOC;
				}
			   with_cheq  += "\nGROUP BY o.C_Order_ID " +
			   		"\n),";
					//Total Piezas de O/C Chequeadas Pre-distribuidas 
			   with_cheq  +=
				   "\nEN_OC_PD AS " +
				    "\n(" +
				    "\nSELECT o.C_Order_ID, SUM(iol.PickedQty)-SUM(iol.ScrappedQty) cantOC " +
				    "\nFROM  C_Order o INNER JOIN M_InOut io on (io.C_Order_ID = o.C_Order_ID) " +
				    	"\nINNER JOIN M_InOutLine iol on (iol.M_INOUT_ID = io.M_INOUT_ID) " +
				    "\nWHERE o.XX_OrderStatus ='CH' " +
				    	"\nAND o.XX_VLO_TypeDelivery = 'PD' ";
			   if(selected.size()>0  ){
				   with_cheq += "\nAND  o.DocumentNo in " +selecOC;
				}
			   with_cheq   += "\nGROUP BY o.C_Order_ID " +
		   			"\n),";
				//Total Piezas de O/C en estado Chequeada  de tipo Centro de Distribución que están en tienda
			   with_cheq +=  
				   "EN_TIENDA_CD AS " +
			    	"\n(" +
			    	"\nSELECT  o.C_Order_ID, SUM(de.XX_ProductQuantity) cantTienda " +
			    	"\nFROM C_Order o INNER JOIN M_InOut io on (io.C_Order_ID = o.C_Order_ID) " +
			    		"\nINNER JOIN M_InOutLine iol on (iol.M_INOUT_ID = io.M_INOUT_ID) " +
			    		"\nINNER JOIN XX_VMR_OrderRequestDetail de on (iol.M_Product_ID = de.M_Product_ID) " +
			    		"\nINNER JOIN  XX_VMR_Order pe on (de.XX_VMR_Order_ID = pe.XX_VMR_Order_ID) " +
			    	"\nWHERE o.XX_OrderStatus ='CH' " +
			    		"\nAND o.XX_VLO_TypeDelivery = 'CD' " +
			    		"\nAND pe.XX_OrderRequestStatus = 'TI' " +
			    		"\nAND pe.XX_DATESTATUSONSTORE > o.XX_CHECKUPDATE ";
			   if(selected.size()>0 ){
				   with_cheq += "\nAND  o.DocumentNo in " +selecOC;
				}
			    with_cheq   += "\nGROUP BY o.C_Order_ID " +
		   		"\n),";
			    	//Total Piezas de O/C  en estado Chequeada de tipo Centro de Distribución
			    with_cheq  +=
			    	"\nEN_OC_CD AS " +
			    	"\n(" +
			    	"\nSELECT o.C_Order_ID, SUM(iol.PickedQty)-SUM(iol.ScrappedQty) cantOC " +
			    	"\nFROM  C_Order o INNER JOIN M_INOUT io on (io.C_Order_ID = o.C_Order_ID) " +
			    		"\nINNER JOIN M_INOUTLINE iol on (iol.M_INOUT_ID = io.M_INOUT_ID) " +
			    	"\nWHERE o.XX_OrderStatus ='CH' " +
			    	"\nAND o.XX_VLO_TypeDelivery = 'CD' ";
				if(selected.size()>0 ){
					   with_cheq += "\nAND o.DocumentNo in " +selecOC;
				}
			    with_cheq   += "\nGROUP BY o.C_Order_ID " +
		   		"\n),";
			   
			 //Total Piezas de O/C menos total piezas en tienda en estado Chequeada de tipo Pre-distribuida 
			    with_cheq  += 
			    	"\nCHEQUEADAS_PD AS " +
				    "\n(" +
				    "\nSELECT oc.C_Order_ID ord, "+
				    	"\n(CASE WHEN ti.cantTienda is null THEN oc.cantOC " +
				    	"\nWHEN oc.cantOC - ti.cantTienda < 0  THEN 0 " +
				    	"\nELSE (oc.cantOC - ti.cantTienda) END) TotalPieces " +
				    "\nFROM EN_OC_PD oc " +
				    "\nLEFT JOIN EN_TIENDA_PD ti ON (oc.C_Order_ID = ti.C_Order_ID) ";
			    with_cheq += "\n),";
			  //Total Piezas de O/C menos total piezas en tienda en estado Chequeada de tipo En Centro de Distribución
			    with_cheq  += 
			    	"\nCHEQUEADAS_CD AS " +
				    "\n(" +
				    "\nSELECT oc.C_Order_ID ord, " +
				    	"\n(CASE WHEN ti.cantTienda is null THEN oc.cantOC " +
				    	"\nWHEN oc.cantOC - ti.cantTienda < 0  THEN 0 " +
				    	"\nELSE (oc.cantOC - ti.cantTienda) END) TotalPieces " +
				    "\nFROM EN_OC_CD oc " +
				    "\nLEFT JOIN EN_TIENDA_CD ti ON (oc.C_Order_ID = ti.C_Order_ID) ";
			    with_cheq += "\n)";
		
			}
			
			//Si el estado de la O/C seleccionado en el filtro es diferente a CH (Chequeadas)
			if(OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())!="CH"){
				 //Total Piezas de O/C en estado Recibida
				with_recib += 
					"\nOC_RECIBIDAS AS" +
					"\n(" +
					"\nSELECT oc.C_Order_ID ord, "+
						"\nSUM(oc.XX_ProductQuantity) TotalPieces " +
					"\nFROM  C_Order oc " +
					"\nWHERE oc.XX_OrderStatus ='RE'";
				   if(selected.size()>0 ){
					   with_recib += "\nAND  oc.DocumentNo in " +selecOC;
					}
				with_recib += "\nGROUP BY oc.C_Order_ID " +
	   			"\n)";
			}
			
			//Si el estado de la O/C seleccionado en el filtro es igual a CH (Chequeadas)
			if(OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())=="CH"){
				sql =  with_cheq + select +
				
			   		"\nFROM (SELECT *  from CHEQUEADAS_CD WHERE TotalPieces >  0 " +
			   		"\nUNION ALL " +
			   		"\nSELECT * from CHEQUEADAS_PD  WHERE TotalPieces > 0 ) " +
			   		"\nINNER JOIN C_Order o ON (ord = o.C_Order_ID) " + 
			    	"\nLEFT JOIN XX_VMR_Category c ON (o.XX_VMR_Category_ID = c.XX_VMR_Category_ID) " +
			    	"\nLEFT JOIN XX_VMR_Department d ON (o.XX_VMR_Department_ID = d.XX_VMR_Department_ID) " +
			    	"\nLEFT JOIN XX_VMR_Collection co ON (o.XX_VMR_Collection_ID = co.XX_VMR_Collection_ID) " +
			    	"\nLEFT JOIN XX_VMR_Package pa ON (o.XX_VMR_Package_ID = pa.XX_VMR_Package_ID)";
				sql += where +" "+
			    	"\nGROUP BY  o.DocumentNo, TO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY'),  c.Value||'-'||c.Name, d.Value||'-'||d.Name, co.Name, pa.Name, o.XX_OrderStatus, o.XX_CHECKUPDATE, o.XX_RECEPTIONDATE" +
			   		"\nORDER BY o.DocumentNo, TO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY'),  c.Value||'-'||c.Name, d.Value||'-'||d.Name, co.Name, pa.Name, o.XX_OrderStatus";
	
				//Query que calcula el total del ponderado de días de inventario de todas las ordenes de compra seleccionadas en estado chequeadas
				sqlTotal =  with_cheq +
					"\nSELECT sum(TotalPieces*(CASE WHEN o.XX_OrderStatus ='CH' THEN BUSINESS_DAYS(o.XX_CHECKUPDATE,sysdate) "+
					"\nWHEN o.XX_OrderStatus ='RE' THEN BUSINESS_DAYS(o.XX_RECEPTIONDATE,sysdate)" +
					"\nELSE 0 END)), sum(TotalPieces) " +
					"\nFROM (SELECT *  from CHEQUEADAS_CD WHERE TotalPieces >  0 " +
			   		"\nUNION ALL " +
			   		"\nSELECT * from CHEQUEADAS_PD  WHERE TotalPieces > 0 ) " +
			   		"\nINNER JOIN C_Order o ON (ord = o.C_Order_ID) ";
				sqlTotal += where;
	
			//Si el estado de la O/C seleccionado en el filtro es igual a RE (Recibidas)
			}else if(OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())=="RE"){
				sql = "WITH" + with_recib + select + 
						"\nFROM OC_RECIBIDAS " +
						"\nINNER JOIN C_Order o ON (ord = o.C_Order_ID) " + 
				    	"\nLEFT JOIN XX_VMR_Category c ON (o.XX_VMR_Category_ID = c.XX_VMR_Category_ID) " +
				    	"\nLEFT JOIN XX_VMR_Department d ON (o.XX_VMR_Department_ID = d.XX_VMR_Department_ID) " +
				    	"\nLEFT JOIN XX_VMR_Collection co ON (o.XX_VMR_Collection_ID = co.XX_VMR_Collection_ID) " +
				    	"\nLEFT JOIN XX_VMR_Package pa ON (o.XX_VMR_Package_ID = pa.XX_VMR_Package_ID)";
				sql += where +" "+
				    	"\nGROUP BY  o.DocumentNo, TO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY'),  c.Value||'-'||c.Name, d.Value||'-'||d.Name, co.Name, pa.Name, o.XX_OrderStatus, o.XX_CHECKUPDATE, o.XX_RECEPTIONDATE" +
				   		"\nORDER BY o.DocumentNo, TO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY'),  c.Value||'-'||c.Name, d.Value||'-'||d.Name, co.Name, pa.Name, o.XX_OrderStatus";
				
				//Query que calcula el total del ponderado de días de inventario de todas las ordenes de compra seleccionadas en estado recibidas
				sqlTotal = "WITH" + with_recib +
					"\nSELECT sum(TotalPieces*(CASE WHEN o.XX_OrderStatus ='CH' THEN BUSINESS_DAYS(o.XX_CHECKUPDATE,sysdate) "+
					"\nWHEN o.XX_OrderStatus ='RE' THEN BUSINESS_DAYS(o.XX_RECEPTIONDATE,sysdate)" +
					"\nELSE 0 END)), sum(TotalPieces) " +
					"\nFROM OC_RECIBIDAS " +
			   		"\nINNER JOIN C_Order o ON (ord = o.C_Order_ID) ";
				sqlTotal += where;
				
			//Si no se seleccionó ningún estado de la O/C o se seleccionó Todos los estados 
			}else {
				sql = with_cheq + ", " + with_recib + select +
		   		"\nFROM (SELECT *  " +
		   			  "\nFROM (SELECT * FROM CHEQUEADAS_CD WHERE TotalPieces > 0 " +
		   				      "\nUNION ALL " +
		   					  "\nSELECT * FROM CHEQUEADAS_PD  WHERE TotalPieces > 0 )" +
		   			  "\nUNION ALL " +
		   			  "\nSELECT * FROM OC_RECIBIDAS)" +				  
				   		"\nINNER JOIN C_Order o ON (ord = o.C_Order_ID) " + 
				    	"\nLEFT JOIN XX_VMR_Category c ON (o.XX_VMR_Category_ID = c.XX_VMR_Category_ID) " +
				    	"\nLEFT JOIN XX_VMR_Department d ON (o.XX_VMR_Department_ID = d.XX_VMR_Department_ID) " +
				    	"\nLEFT JOIN XX_VMR_Collection co ON (o.XX_VMR_Collection_ID = co.XX_VMR_Collection_ID) " +
				    	"\nLEFT JOIN XX_VMR_Package pa ON (o.XX_VMR_Package_ID = pa.XX_VMR_Package_ID) ";
				sql += where +" "+
				    	"\nGROUP BY  o.DocumentNo, TO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY'),  c.Value||'-'||c.Name, d.Value||'-'||d.Name, co.Name, pa.Name, o.XX_OrderStatus, o.XX_CHECKUPDATE, o.XX_RECEPTIONDATE" +
				   		"\nORDER BY o.DocumentNo, TO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY'),  c.Value||'-'||c.Name, d.Value||'-'||d.Name, co.Name, pa.Name, o.XX_OrderStatus";
	
				//Query que calcula el total del ponderado de días de inventario de todas las ordenes de compra seleccionadas en estado recibidas y chequeadas
				sqlTotal = with_cheq + ", " + with_recib +
				"\nSELECT sum(TotalPieces*(CASE WHEN o.XX_OrderStatus ='CH' THEN BUSINESS_DAYS(o.XX_CHECKUPDATE,sysdate) "+
				"\nWHEN o.XX_OrderStatus ='RE' THEN BUSINESS_DAYS(o.XX_RECEPTIONDATE,sysdate)" +
				"\nELSE 0 END)), sum(TotalPieces)" +
				"\nFROM (SELECT *  " +
	 			  "\nFROM (SELECT * FROM CHEQUEADAS_CD WHERE TotalPieces > 0 " +
	 				      "\nUNION ALL " +
	 					  "\nSELECT * FROM CHEQUEADAS_PD  WHERE TotalPieces > 0 )" +
	 			  "\nUNION ALL " +
	 			  "\nSELECT * FROM OC_RECIBIDAS)" +	
		   		"\nINNER JOIN C_Order o ON (ord = o.C_Order_ID) ";
				sqlTotal += where;
			}
			
			//System.out.println(sql);
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = DB.prepareStatement(sql, null);			
				rs = ps.executeQuery();
				int row = desde;
				while (rs.next()) {
					miniTable2.setRowCount(row + 1);
					miniTable2.setValueAt(rs.getString(1), row, 0);
					miniTable2.setValueAt(rs.getString(2), row, 1);
					miniTable2.setValueAt(rs.getString(3), row, 2);
					miniTable2.setValueAt(rs.getString(4), row, 3);
					miniTable2.setValueAt(rs.getString(5), row, 4);
					miniTable2.setValueAt(rs.getString(6), row, 5);
					miniTable2.setValueAt(rs.getString(7), row, 6);
					miniTable2.setValueAt(rs.getInt(8), row, 7);
					miniTable2.setValueAt(rs.getDouble(9), row, 8);
					row++;
				}
			} catch (SQLException e) {			
				log.log(Level.SEVERE, sql, e);
			} catch (Exception e) {
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
			desde = desdeTemp;
			//Se obtiene el total del ponderado de días de inventario de todas las ordenes de compra seleccionadas
			CalculateTotalWeightedInventoryDays(sqlTotal);
		}
		
	}
	
	/**Obtiene el total del ponderado de días de inventario de todas
	 *  las ordenes de compra seleccionadas
	 * @param sql
	 */
	private void CalculateTotalWeightedInventoryDays(String sql) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			while (rs.next())
			{
				Double resultPiecesDays = rs.getDouble(1);
				Double resultPieces = rs.getDouble(2);
				//Si el resultado es diferente a NULL se muestra los labels del resultado
					if(resultPiecesDays!=null && resultPieces !=null && (totalPiecesAccumulated + resultPieces)!=0){
						Double result =(totalInvDaysAccumulated + resultPiecesDays)/(totalPiecesAccumulated + resultPieces);
						String val = result+"";
						BigDecimal big = new BigDecimal(val);
						big = big.setScale(2, RoundingMode.HALF_UP);

						//System.out.println("RESULTADO TOTAL: " +big);
						
						totalWeightedInventoryDays.setText(big.toString());
						totalWeightedInventoryDays.setVisible(true);
						labelTotalWeightedInventoryDays.setVisible(true);
				//Si el resultado es NULL no se muestra los labels del resultado
					}else {
						totalWeightedInventoryDays.setVisible(false);
						labelTotalWeightedInventoryDays.setVisible(false);
					}
					totalInvDaysAccumulated = totalInvDaysAccumulated + resultPiecesDays;
					totalPiecesAccumulated = totalPiecesAccumulated + resultPieces;
					
			}	
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
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

	/** Se determina el ponderado de dias de inventario de los productos seleccionados*/
	public void calculateQueryProductWeighted() {
	
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable2.setRowCount(0);
		miniTable2 = new MiniTablePreparator();
		dataPane2.getViewport().add(miniTable2);
		
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
		KeyNamePair ware = (KeyNamePair)comboWarehouse.getSelectedItem();
		KeyNamePair loca = (KeyNamePair)comboLocator.getSelectedItem();
		
		/* Add the column definition for the table */
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"), 
						".", KeyNamePair.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Store"), 
						".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Locator_ID"),
						".", KeyNamePair.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"),
						".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"), 
						".", String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Collection_ID"), 
						".", String.class, "."),		
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Package_ID"), 
						".", String.class, "."),
				new ColumnInfo( Msg.translate(Env.getCtx(), "Amount")+ " " +Msg.translate(Env.getCtx(), "XX_Pieces"),
						".", Integer.class),
				new ColumnInfo( Msg.translate(Env.getCtx(), "XX_WeightedInventoryDays"),
						".", Double.class),
				};
		
		miniTable2.prepareTable(layout, "", "", true, "");
		int rows = 0;
		Integer desde = 0;
		Integer hasta = 0;
		String selecProd = null;
		while(desde < selected.size()){
					
			hasta = desde + maxBySql;
			Integer desdeTemp = desde;
			//Se obtiene un string con las O/C seleccionadas
			selecProd = "(''";
			for (int i = desde;  i <= hasta && i < selected.size(); i++) {

				selecProd += ", '"+selected.get(i)+"'";
				desdeTemp++;
			}
			selecProd += ")";
	
			//Si el estado de la O/C seleccionado en el filtro es distinto de RE (Recibidas)
			if((loca.getKey()==-1 || loca.getKey()==99999999 || loca.getKey()==0) && 
					(ware.getKey()==Env.getCtx().getContextAsInt("XX_L_WAREHOUSECENTRODIST_ID") || ware.getKey()== 99999999 && ware.getKey()== 0)){
				//Se Obtiene ponderado de dias de inventario para los productos en estado recibido
				String sql = "WITH RECIBIDOS AS" +
				"\n(SELECT " +
				"p.Value||'-'||p.Name product,  " +
				"\nTO_NCHAR('001-CENTRO DE DISTRIBUCION BOLEITA') warehouse, " +
				"\nTO_NCHAR('CENTRO DE DISTRIBUCION BOLEITA - RECIBIDO')  locat, " +
				"\nc.Value||'-'||c.Name catg, " +
				"\nd.Value||'-'||d.Name dept, " +
				"\nco.Name coll, " +
				"\npa.Name pack, " +
				"\no.XX_ReceptionDate dateLoca, " +
				"\nrm.XX_QuantityV qtyProd " +
				"\nFROM  C_Order o " +
				"\nINNER JOIN XX_VMR_PO_LineRefProv lrp ON ( lrp.C_Order_ID = o.C_Order_ID) " +
				"\nINNER JOIN XX_VMR_ReferenceMatrix rm ON ( rm.XX_VMR_PO_LineRefProv_ID = lrp.XX_VMR_PO_LineRefProv_ID) " +
				"\nLEFT JOIN XX_VMR_Category c ON (c.XX_VMR_Category_ID = o.XX_VMR_Category_ID) " +
				"\nLEFT JOIN XX_VMR_Department d ON (d.XX_VMR_Department_ID = o.XX_VMR_Department_ID) " +
				"\nLEFT JOIN XX_VMR_Package pa ON (pa.XX_VMR_Package_ID = o.XX_VMR_Package_ID) " +
				"\nLEFT JOIN XX_VMR_Collection co ON (co.XX_VMR_Collection_ID = o.XX_VMR_Collection_ID) " +
				"\nLEFT JOIN M_Product p ON (rm.M_Product = p.M_Product_ID) " +
				"\nWHERE o.XX_OrderStatus ='RE' AND o.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" ";
			
				 if(selected.size()>0 ){
					   sql += "\nAND p.Value||'-'||p.Name IN " + selecProd;
					}
					//Categoría
					if(catg != null && catg.getKey()!=99999999 && catg.getKey()!= 0) {	
						sql+= "\nAND o.XX_VMR_Category_ID = " + catg.getKey();
					}	
					//Departamento			
					if(dept != null && dept.getKey()!=99999999 && dept.getKey()!= 0) {
						sql+=  "\nAND o.XX_VMR_Department_ID = " + dept.getKey();
					}
					//Colección
					if(coll != null && coll.getKey()!=99999999 && coll.getKey()!= 0) {
						sql+=  "\nAND o.XX_VMR_Collection_ID = " + coll.getKey();
					}
					//Paquete 
					if(pack != null && pack.getKey()!= 99999999 && pack.getKey()!= 0 ){	
						sql+= "\nAND o.XX_VMR_Package_ID = " + pack.getKey();
					}
				
				sql+= "\nGROUP BY p.Value||'-'||p.Name " +
				"\n, c.Value||'-'||c.Name " +
				"\n, d.Value||'-'||d.Name " +
				"\n, co.Name " +
				"\n, pa.Name " +
				"\n, o.XX_ReceptionDate " +
				"\n, rm.XX_QuantityV) ";
			
				
				sql +="\nSELECT product, " +
				"\nwarehouse, " +
				"\nlocat, " +
				"\ncatg, " +
				"\ndept, " +
				"\ncoll, " +
				"\npack, " +
				"\nsum(qtyProd), " +
				"\nTRUNC((CASE WHEN sum(qtyProd)> 0 THEN sum(qtyProd * BUSINESS_DAYS(dateLoca,sysdate))/sum(qtyProd) " +
				"\n            ELSE 0 END),2) Pond " +
				"\nFROM RECIBIDOS " +
				"\nGROUP BY product, " +
				"\nwarehouse, " +
				"\nlocat, " +
				"\ncatg, " +
				"\ndept, " +
				"\ncoll, " +
				"\npack ";
				
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = DB.prepareStatement(sql, null);			
					rs = ps.executeQuery();
					while (rs.next()) {
						miniTable2.setRowCount(rows + 1);
						miniTable2.setValueAt(new KeyNamePair(0, rs.getString(1)), rows,PRODUCT);
						miniTable2.setValueAt(rs.getString(2), rows,WAREHOUSE);
						miniTable2.setValueAt(new KeyNamePair(0, rs.getString(3)), rows,LOCATOR);
						miniTable2.setValueAt(rs.getString(4), rows,CATEGORY);
						miniTable2.setValueAt(rs.getString(5), rows,DEPARTMENT);
						miniTable2.setValueAt(rs.getString(6), rows,COLLECTION);
						miniTable2.setValueAt(rs.getString(7), rows,PACKAGE);
						miniTable2.setValueAt(rs.getInt(8), rows,TOTALPIECES);
						miniTable2.setValueAt(rs.getDouble(9), rows,WEIGHTED);
						miniTable2.repaint();
						rows++;
					}	
				} catch (SQLException e) {			
					log.log(Level.SEVERE, sql, e);
				} catch (Exception e) {
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
			if(loca.getKey()!=-1){
				rows = calculateQueryCheq(rows,selecProd);
			}
			desde = desdeTemp;
		}
		calculateTotalProductWeightedDays();
	}

	/**Obtiene ponderado de dias de inventario para los productos 
	 * en estado chequeado
	 * @param rows
	 */
	private Integer calculateQueryCheq(Integer rows, String selecProd) {
		
		
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
		KeyNamePair ware = (KeyNamePair)comboWarehouse.getSelectedItem();
		KeyNamePair loca = (KeyNamePair)comboLocator.getSelectedItem();
		
		String sql = 
		    "\nSELECT "+
		    "\np.M_Product_ID, "+
		    "\np.Value||'-'||p.Name product, " +
		    "\nw.Name warehouse, "+
		    "\nl.M_Locator_ID, "+
		    "\nl.Value locat," +
		    "\nc.Value||'-'||c.Name catg, "+
		    "\nd.Value||'-'||d.Name dept, "+
		    "\nco.Name coll, "+
		    "\npa.Name pack, "+
		    "\ns.QTYONHAND qtyProd, "+
		    "\n s.M_ATTRIBUTESETINSTANCE_ID lote "+
		    "\nFROM M_Product p "+
		    "\nINNER JOIN M_Storage s ON (s.M_Product_ID = p.M_Product_ID) "+
		    "\nINNER JOIN M_Locator l ON (l.M_Locator_ID = s.M_Locator_ID) "+
		    "\nINNER JOIN M_Warehouse w ON (w.M_Warehouse_ID = l.M_Warehouse_ID) "+
		    "\nLEFT JOIN XX_VMR_Category c ON (c.XX_VMR_Category_ID = p.XX_VMR_Category_ID) "+
		    "\nLEFT JOIN XX_VMR_Department d ON (d.XX_VMR_Department_ID = p.XX_VMR_Department_ID) "+
		    "\nLEFT JOIN M_ATTRIBUTESETINSTANCE asi ON (s.M_ATTRIBUTESETINSTANCE_ID = asi.M_ATTRIBUTESETINSTANCE_ID) "+
		    "\nLEFT JOIN XX_VMR_Package pa ON (pa.XX_VMR_Package_ID = asi.XX_VMR_Package_ID) "+
		    "\nLEFT JOIN XX_VMR_Collection co ON (co.XX_VMR_Collection_ID = pa.XX_VMR_Collection_ID) "+
		    "\nWHERE s.QTYONHAND > 0 AND p.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" ";
			 if(selected.size()>0 ){
				   sql += "\nAND p.Value||'-'||p.Name IN " + selecProd;
				}
				//Warehouse
				if(ware != null && ware.getKey()!=99999999 && ware.getKey()!= 0) {	
					sql+= "\nAND w.M_Warehouse_ID = " + ware.getKey();
				}	
				//Locator	
				if(loca != null && loca.getKey()!=99999999 && loca.getKey()!= 0) {
					sql+=  "\nAND l.M_Locator_ID = " + loca.getKey();
				}
				//Categoría
				if(catg != null && catg.getKey()!=99999999 && catg.getKey()!= 0) {	
					sql+= "\nAND c.XX_VMR_Category_ID = " + catg.getKey();
				}	
				//Departamento			
				if(dept != null && dept.getKey()!=99999999 && dept.getKey()!= 0) {
					sql+=  "\nAND d.XX_VMR_Department_ID = " + dept.getKey();
				}
				//Colección
				if(coll != null && coll.getKey()!=99999999 && coll.getKey()!= 0) {
					sql+=  "\nAND co.XX_VMR_Collection_ID = " + coll.getKey();
				}
				//Paquete 
				if(pack != null && pack.getKey()!= 99999999 && pack.getKey()!= 0 ){	
					sql+= "\nAND pa.XX_VMR_Package_ID = " + pack.getKey();
				}
			
			sql += "\nAND l.IsDefault =(CASE WHEN l.M_Locator_ID = "+CD_CHEQUEADO+" THEN 'Y' ELSE 'N' END) ";

			sql +=  "\nGROUP BY p.M_Product_ID "+
			"\n, p.Value||'-'||p.Name "+
			"\n, w.Name "+
		    "\n, l.M_Locator_ID "+
		    "\n, l.Value "+
		    "\n, c.Value||'-'||c.Name "+
		    "\n, d.Value||'-'||d.Name "+
		    "\n, co.Name "+
		    "\n, pa.Name "+
		    "\n, s.QTYONHAND "+
		    "\n, s.M_ATTRIBUTESETINSTANCE_ID ";
	
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		//System.out.println(sql);
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			while (rs.next()) {

				miniTable2.setRowCount(rows + 1);
				miniTable2.setValueAt(new KeyNamePair(rs.getInt(1), rs.getString(2)), rows,PRODUCT);
				miniTable2.setValueAt(rs.getString(3), rows,WAREHOUSE);
				miniTable2.setValueAt(new KeyNamePair(rs.getInt(4), rs.getString(5)), rows,LOCATOR);
				miniTable2.setValueAt(rs.getString(6), rows,CATEGORY);
				miniTable2.setValueAt(rs.getString(7), rows,DEPARTMENT);
				miniTable2.setValueAt(rs.getString(8), rows,COLLECTION);
				miniTable2.setValueAt(rs.getString(9), rows,PACKAGE); 
				miniTable2.setValueAt(rs.getInt(10), rows,TOTALPIECES); //Cantidad de piezas en existencia en un M_Locator para un M_Product y M_ATTRIBUTESETINSTANCE_ID específico
				
				/* Query que obtiene la cantidad de un producto de un lote específico
				 * que entró en un estado y una tienda en específico y los días 
				 * que han pasado hasta la fecha actual*/
				String sql_dates = "\nSELECT MOVEMENTQTY, " +
					"\nBUSINESS_DAYS(MOVEMENTDATE,SYSDATE) " +
					"\nFROM M_TRANSACTION " +
					"\nWHERE MOVEMENTQTY > 0" +
					"\nAND M_PRODUCT_ID = " + rs.getInt(1) +
					"\nAND M_LOCATOR_ID = "+ rs.getInt(4) +
					"\nAND M_ATTRIBUTESETINSTANCE_ID = "+ rs.getInt(11) +
					"\nAND AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID() +
					"\nORDER BY MOVEMENTDATE DESC";
				
			Double weightedDays = calculateTransactionWeightedDays(sql_dates, rs.getInt(10));
			miniTable2.setValueAt(weightedDays, rows, WEIGHTED);
			rows++;
				}	
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
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
		return rows;
	}

	/**Se Obtiene ponderado de dias de inventario para los productos en
	 *  estado diferente a recibidos o chequeados
	 * @param sql
	 * @param qty
	 * @return
	 */
	private Double calculateTransactionWeightedDays(String sql, int qty){
		
		ArrayList<Integer> daysQty = null;
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		Integer qtyTotal = 0;
		Integer qtyTotalTemp = 0;
		Double weightedDays = 0.0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			int i=0;
			
			/* Se almacena en una lista un arreglo con la cantidad del producto 
			 * y dias que lleva en inventario hasta que se llegue a la cantidad 
			 * total en existencia */
			while (rs.next())
			{
				qtyTotalTemp = qtyTotal;
				Integer qtyTemp = rs.getInt(1);
				Integer days = rs.getInt(2);
				daysQty = new ArrayList<Integer>(2);
				qtyTotalTemp = qtyTotalTemp + qtyTemp;
		
				/* Si la cantidad acumulada es igual o mayor que la cantidad en existencia
				 * almacena el ultimo arreglo a la lista y se sale del while
				 */
				if(qtyTotalTemp >= qty){
					qtyTemp = qty - qtyTotal;
					daysQty.add(0, qtyTemp);
					daysQty.add(1, days);
					list.add(i, daysQty);
					break;
				}
				daysQty.add(0, qtyTemp);
				daysQty.add(1, days);
				list.add(i, daysQty);
				qtyTotal = qtyTotal + qtyTemp;
				i++;
			}	
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
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
		
		/* Para el producto, lote y localización (M_Locator) seleccionado 
		 * se calcula el ponderado de dias de inventario usando los datos
		 * almacenados en la lista
		 */
		for (int i = 0; i < list.size(); i++) {
			ArrayList<Integer> temp = list.get(i);
			weightedDays =  weightedDays + (temp.get(0)*temp.get(1)); 
		}
		
		weightedDays = weightedDays/qty;

	
		return truncate(weightedDays);
	}
	
	/** Obtiene el total del ponderado de días de inventario de todas
	 *  los productos  seleccionados
	 */
	private void calculateTotalProductWeightedDays() {
		Double totalWeighted = 0.0;
		Integer totalQty = 0;
		Integer qty = 0;
		Double days = 0.0;
		for (int j = 0; j < miniTable2.getRowCount(); j++) {
			qty = (Integer) miniTable2.getValueAt(j,TOTALPIECES);
			days = (Double) miniTable2.getValueAt(j,WEIGHTED);
			totalWeighted += qty *days;
			totalQty += qty;
		}
		//Si el resultado es mayor a 0 se muestra el resultado, en caso contrario se oculta
		if(totalQty>0){
			totalWeighted =	truncate(totalWeighted/totalQty);
			totalWeightedInventoryDays.setText(Double.toString((totalWeighted)));
			totalWeightedInventoryDays.setVisible(true);
			labelTotalWeightedInventoryDays.setVisible(true);
		}else {
			totalWeightedInventoryDays.setVisible(false);
			labelTotalWeightedInventoryDays.setVisible(false);
		}
	}
	/**	Devuelve las O/C o Productos seleccionados en la minitable */
	private void getSelected(){
		
	Boolean isOk = true;
	selected = new Vector<String>();
	int i=0;
		for(int j=0; j<miniTable.getRowCount() && isOk; j++){
			if(new Boolean(miniTable.getModel().getValueAt(j, 0).toString())){
				i=i+1;
					String selectedKey = (String)miniTable.getValueAt(j, 1);	
					selected.add(selectedKey);
				}
		}
		if(selected.isEmpty()){
			String msg = "Debe seleccionar al menos un item de la lista.";
			ADialog.error(m_WindowNo, m_frame, msg);
			isOk =false;
		}
		if(isOk) llenarTabla2();	
	
	}

	//**************** Métodos de utilidad para esta ventana *********************************
	
	/** Carga datos del filtro de Ubicación de los Porductos */
	private void uploadLocator() {
		KeyNamePair warehouse = (KeyNamePair)comboWarehouse.getSelectedItem();				
		KeyNamePair loadKNP;
		String sql;
		
		comboLocator.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboLocator.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllLocators"));
		comboLocator.addItem(loadKNP);	
		
		if(warehouse ==null || warehouse.getKey() == 0 || warehouse.getKey() == 99999999 || warehouse.getKey() == Env.getCtx().getContextAsInt("XX_L_WAREHOUSECENTRODIST_ID")){
			loadKNP = new KeyNamePair(-1,"CENTRO DE DISTRIBUCION BOLEITA - RECIBIDO");
			comboLocator.addItem(loadKNP);	
		}
	
		if (warehouse != null   && warehouse.getKey() != 0 && warehouse.getKey() != 99999999){			
			sql = "SELECT lo.M_Locator_ID, lo.VALUE FROM M_Locator lo " +
				"\nWHERE lo.M_Warehouse_ID = " + warehouse.getKey() + 
				"\n AND lo.AD_Client_ID = "+ ctx.getAD_Client_ID()+
				"\n AND lo.IsDefault =(CASE WHEN lo.M_Locator_ID = " +
					Env.getCtx().getContextAsInt("#XX_L_LOCATORCDCHEQUEADO_ID")+" THEN 'Y' ELSE 'N' END)"+
				"\n ORDER BY lo.VALUE ";
		}else {
			sql = "SELECT lo.M_Locator_ID, lo.VALUE FROM M_Locator lo " +
			"\nWHERE lo.AD_Client_ID = "+ ctx.getAD_Client_ID()+
			"\n AND lo.IsDefault =(CASE WHEN lo.M_Locator_ID = " +
			Env.getCtx().getContextAsInt("#XX_L_LOCATORCDCHEQUEADO_ID")+" THEN 'Y' ELSE 'N' END)"+
			"\n ORDER BY lo.VALUE ";
		}
		
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboLocator.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}		
	}

	/** Carga datos del filtro de Almacén */
	private void uploadWarehouse() {
		KeyNamePair loadKNP;
		String sql = "";						
		comboWarehouse.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboWarehouse.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllStores"));
		comboWarehouse.addItem(loadKNP);	
		sql = "SELECT wa.M_Warehouse_ID, wa.VALUE||'-'||wa.NAME " +
				"\nFROM M_Warehouse wa WHERE wa.ISACTIVE = 'Y' " +
				"\nAND wa.AD_Client_ID = "+ ctx.getAD_Client_ID()+
				"\n ORDER BY wa.VALUE"; 			
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboWarehouse.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
	}	
		
	/** Carga datos del filtro de estado de O/C */
	private void uploadStatusPurchaseOrder() {
		comboOPurchaseStatus.removeAllItems();
		OPurchaseStatus_name.removeAllElements();
		// LLenar los combo de listas
		comboOPurchaseStatus.addItem("");
		OPurchaseStatus_name.add("");
		comboOPurchaseStatus.addItem(Msg.getMsg(Env.getCtx(), "XX_AllStatus"));
		OPurchaseStatus_name.add(Msg.getMsg(Env.getCtx(), "XX_AllStatus"));
		try{
			for (X_Ref_XX_OrderStatus v : X_Ref_XX_OrderStatus.values()) {
				if(v.getValue()== "RE" || v.getValue()=="CH"){
					comboOPurchaseStatus.addItem(v.getValue() + "-" + v);
					OPurchaseStatus_name.add(v.getValue());
				}
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, null, e);
		}
	
		comboOPurchaseStatus.setEnabled(true);		
		comboOPurchaseStatus.setEditable(true);
		comboOPurchaseStatus.setSelectedIndex(0);
	}
	
	/** Carga datos del filtro paquetes */
	private void uploadPackages() {
		KeyNamePair collection = (KeyNamePair)comboCollection.getSelectedItem();				
		KeyNamePair loadKNP;
		String sql;
		
		comboPackage.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboPackage.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllPackages"));
		comboPackage.addItem(loadKNP);	
		
		if (collection != null  && collection.getKey() != 0 && collection.getKey() != 99999999){			
			sql = "SELECT pa.XX_VMR_PACKAGE_ID, pa.NAME FROM XX_VMR_PACKAGE pa " +
				"\nWHERE pa.XX_VMR_COLLECTION_ID = " + collection.getKey() + 
				"\n AND pa.AD_Client_ID = "+ ctx.getAD_Client_ID()+
				"\n ORDER BY pa.NAME ";
		} else {					
			sql = "SELECT XX_VMR_PACKAGE_ID, NAME FROM XX_VMR_PACKAGE " +
				"\n WHERE AD_Client_ID = "+ ctx.getAD_Client_ID()+
				 " ORDER BY NAME";									
		} 
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboPackage.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}		
	}

	/** Carga datos del filtro colecciones */
	private void uploadCollection() {
		KeyNamePair loadKNP;
		String sql = "";						
		comboCollection.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboCollection.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllCollections"));
		comboCollection.addItem(loadKNP);	
		sql = "SELECT co.XX_VMR_COLLECTION_ID, co.NAME " +
				"\nFROM XX_VMR_COLLECTION co WHERE co.ISACTIVE = 'Y' " +
				"\nAND co.AD_Client_ID = "+ ctx.getAD_Client_ID()+
				"\n ORDER BY co.NAME"; 			
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboCollection.addItem(loadKNP);			
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
	private void uploadDepartments() {
		
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();				
		KeyNamePair loadKNP;
		String sql;
		
		comboDepartment.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboDepartment.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllDepartments"));
		comboDepartment.addItem(loadKNP);	
		
		if (catg != null && catg.getKey() != 0 && catg.getKey() != 99999999){			
			sql = "SELECT dp.XX_VMR_DEPARTMENT_ID, dp.VALUE||'-'||dp.NAME FROM XX_VMR_DEPARTMENT dp " 
				+ "WHERE dp.XX_VMR_CATEGORY_ID = " + catg.getKey() +  " ORDER BY dp.VALUE ";
		}  else {					
			sql = "SELECT XX_VMR_DEPARTMENT_ID, VALUE||'-'||NAME FROM XX_VMR_DEPARTMENT " +
					"WHERE AD_Client_ID = "+ ctx.getAD_Client_ID();
			sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
				+ " ORDER BY VALUE||'-'||NAME";									
		} 
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(loadKNP);			
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
		comboCategory.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboCategory.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllCategories"));
		comboCategory.addItem(loadKNP);	
		sql = " SELECT ct.XX_VMR_CATEGORY_ID, ct.VALUE||'-'||ct.NAME " +
				" FROM XX_VMR_CATEGORY ct WHERE ct.ISACTIVE = 'Y' AND ct.AD_Client_ID = "+ ctx.getAD_Client_ID(); 			
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboCategory.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
	}
	
	/** Permite truncar un double */
	private static double truncate(double x){
		  if ( x > 0 )
		    return Math.floor(x * 100)/100;
		  else
		    return Math.ceil(x * 100)/100;
		}
	
	/** Permite agregar un campo a la panel de parametros en la fila disponible
	 * especificando cuantas columnas ocupará 
	 * */
	protected void addParameter (Component component, int row, int col, int horSpace) {
		
		/* Asignar la orientación dependiendo de la posicion*/
		int orientation = 0, orientation2 = 0 ;
		
		if (col % FIELDSPERGROUP == 1) {
			orientation = GridBagConstraints.WEST;
			orientation2 = GridBagConstraints.HORIZONTAL;
		} else {
			orientation = GridBagConstraints.EAST;
			orientation2 = GridBagConstraints.NONE;
		}
	
		//Agregar al panel de parametros
		parameterPanel.add(component,  
				new GridBagConstraints(
						col, row, horSpace, 1, 0.0, 0.0,
						orientation, orientation2, new Insets(5, 5, 5, 5), 0, 0));
	//	displayParameter += horSpace;
	}
	
	/** Habilita y deshabilita componentes apropiadamente y coloca valores por defecto si aplican */
	protected static final void set (Component component, boolean enable) {
		if (component instanceof CComboBox)			
			set((CComboBox)component, enable, true, 0);
		else if (component instanceof CCheckBox)
			set((CCheckBox)component, enable, enable);
		else if (component instanceof VFile) {			
			((VFile) component).setReadWrite(enable);			
			if (!enable) {
				((VFile) component).setValue(null);
			}			
		} else if (component instanceof CLabel)
			component.setVisible(enable);
		else {
			component.setEnabled(enable);			
		}
	}
	
	/** Habilita y deshabilita componentes apropiadamente */
	protected static final void set (CComboBox component, boolean enable, boolean visible, int value) {		
		component.setValue(value);
		component.setEnabled(enable);		
		component.setEditable(enable);
		component.setVisible(visible);
					
	}

	/** Habilita y deshabilita componentes apropiadamente */
	protected static final void set (VLookup component, boolean enable, boolean visible) {		
		component.setEnabled(enable);		
		component.setVisible(visible);
		component.setValue(null);					
	}
	
	
	/** Habilita y deshabilita componentes apropiadamente */
	protected static final void set (CCheckBox component, boolean enable, boolean value) {		
		component.setEnabled(enable);
		component.setValue(value);
	}

	//Cabeceras de las columnas de la minitable
	private ColumnInfo colcheck = new ColumnInfo(Msg.translate(Env.getCtx(), "Select"), "0",  Boolean.class, false, false,"");

	private ColumnInfo colCatg = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"), 
			"\nc.Value||'-'||c.Name", String.class);
	
	private ColumnInfo colDept = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),
			"\nd.Value||'-'||d.Name", String.class);
	
	private ColumnInfo colColle = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Collection_ID"), 
			"\nco.Name", String.class);
	
	private ColumnInfo colPack = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Package_ID"),
			"\npa.Name", String.class);
	
	private ColumnInfo colOrder = new ColumnInfo(Msg.translate(Env.getCtx(), "C_Order_ID"),
			"\no.DocumentNo", String.class);
	
	private ColumnInfo colStatus = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OrderStatus"),
			"\n(CASE WHEN o.XX_OrderStatus ='AN' THEN 'ANULADA' " +
			"\nWHEN o.XX_OrderStatus ='AP' THEN 'APROBADA' " +
			"\nWHEN o.XX_OrderStatus ='CH' THEN 'CHEQUEADA' " +
			"\nWHEN o.XX_OrderStatus ='EA' THEN 'EN ADUANA' " +
			"\nWHEN o.XX_OrderStatus ='EAC' THEN 'ENTREGADA AL AGENTE DE CARGA' " +
			"\nWHEN o.XX_OrderStatus ='EP' THEN 'EN PRODUCCIÓN' " +
			"\nWHEN o.XX_OrderStatus ='EPN' THEN 'EN PROCESO DE NACIONALIZACIÓN' " +
			"\nWHEN o.XX_OrderStatus ='ETI' THEN 'EN TRÁNSITO INTERNACIONAL' " +
			"\nWHEN o.XX_OrderStatus ='ETN' THEN 'EN TRÁNSITO NACIONAL' " +
			"\nWHEN o.XX_OrderStatus ='LDC' THEN 'LLEGADA A CD' " +
			"\nWHEN o.XX_OrderStatus ='LVE' THEN 'LLEGADA A VENEZUELA' " +
			"\nWHEN o.XX_OrderStatus ='PEN' THEN 'PENDIENTE' " +
			"\nWHEN o.XX_OrderStatus ='PRO' THEN 'PROFORMA' " +
			"\nWHEN o.XX_OrderStatus ='RE' THEN 'RECIBIDA' " +
			"\nWHEN o.XX_OrderStatus ='SIT' THEN 'SITME' " +
			"\nELSE '-' END)", String.class);
    
	private ColumnInfo colDate = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_EstimatedDate"), 
			"\nTO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY')", String.class);
	

	//Cabeceras de las columnas para tabla de la opcion Orden de Compra
	private ColumnInfo[] columnOrderPurchase = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "C_Order_ID"),"\no.DocumentNo", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_EstimatedDate"),"\nTO_CHAR(o.XX_EstimatedDate,'DD/MM/YYYY')", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"), "\nc.Value||'-'||c.Name", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"), "\nd.Value||'-'||d.Name", String.class),
		    new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Collection_ID"), "\nco.Name", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Package_ID"), "\npa.Name", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OrderStatus"),
				"\n(CASE WHEN o.XX_OrderStatus ='CH' THEN 'CHEQUEADA' " +
				"\nWHEN o.XX_OrderStatus ='RE' THEN 'RECIBIDA' " +
				"\nELSE '-' END)", String.class),
			new ColumnInfo( Msg.translate(Env.getCtx(), "Amount")+ " " +Msg.translate(Env.getCtx(), "XX_Pieces"),
					"sum(TotalPieces)", Integer.class),
			new ColumnInfo( Msg.translate(Env.getCtx(), "XX_WeightedInventoryDays"), 
					"\nTRUNC((CASE WHEN o.XX_OrderStatus ='CH' THEN (sysdate - o.XX_CHECKUPDATE) "+
					"\nWHEN o.XX_OrderStatus ='RE' THEN (sysdate - o.XX_RECEPTIONDATE)" +
					"\nELSE 0 END),2) ", Double.class)
	};
	
}
