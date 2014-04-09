package compiere.model.cds.forms.indicator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.compiere.apps.ADialog;
import org.compiere.grid.ed.VComboBox;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;


/** Clase que extiende al indicador genérico y representa al indicador de merchandising y mercadeo
 * Agrega campos adicionales como tienda y comprador, y oculta productos
 * */
public class XX_MarketingMerchandising extends XX_Indicator {

	private static final long serialVersionUID = -6440503234752622560L;
	
	//Campos necesarios para este indicador 
	private CLabel labelTienda = new CLabel(Msg.translate(ctx, "XX_Store"));
	private VComboBox comboTienda = new VComboBox();
	private CCheckBox checkTienda = new CCheckBox();
	
	private CLabel labelComprador = new CLabel(Msg.translate(ctx, "XX_Buyer"));
	private VComboBox comboComprador = new VComboBox();
	private CCheckBox checkComprador = new CCheckBox();
	
	private CLabel labelDetailed = new CLabel("Detallado");
	private CCheckBox checkDetailed = new CCheckBox();
	
	protected CPanel centerPanel = new CPanel();
	protected JScrollPane dataPane2 = new JScrollPane();
	protected MiniTablePreparator miniTable2 = new MiniTablePreparator();

	private TitledBorder xBorder = new TitledBorder(Msg.getMsg(Env.getCtx(), "XX_Category"));
	private TitledBorder xBorder2 = new TitledBorder(Msg.getMsg(Env.getCtx(), "Product"));
	
	//Indice de la tabla
	int indiceTabla = 0;
	
	
	@Override
	protected void ocultarParametrosDefecto() {		
		
		//Producto no es un campo que se desee para este indicador
		//ocultarParametro(PRODUCTO); // Agregado por GMARQUES
	}
	
	@Override
	protected void agregarParametros() {
		//Agregar Comprador y Tienda
		agregarParametro(labelTienda);
		agregarParametro(comboTienda);
		agregarParametro(checkTienda);		
		agregarParametro(labelComprador);
		agregarParametro(comboComprador);
		agregarParametro(checkComprador);	
		agregarParametro(labelDetailed);	
		agregarParametro(checkDetailed);	
	}
	
	@Override
	protected void personalizar() {
		// Crear el panel para albergar la 2da tabla
		mainPanel.remove(dataPane);
		
		BoxLayout centerLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
		centerPanel.setLayout(centerLayout);
		
		dataPane.setBorder(xBorder);
		dataPane2.setBorder(xBorder2);
		
		
		dataPane.setPreferredSize(new Dimension(1200, 600)); //Redimensionar el primer panel
		dataPane2.setVisible(false);
//		dataPane2.setPreferredSize(new Dimension(800, 500));
		
		miniTable.setRowHeight(miniTable.getRowHeight() + 2);
		miniTable2.setRowHeight(miniTable2.getRowHeight() + 2);
		
		dataPane.getViewport().add(miniTable, null);
		dataPane2.getViewport().add(miniTable2, null);
		centerPanel.add(dataPane,  BorderLayout.NORTH);
		centerPanel.add(dataPane2,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		//repaint();
		
		//Cargar los datos de los campos que se agregaron en este indicador
		cargarTiendas();
		cargarCompradores();
		
		//Configurar dichos campos
		configurar(comboTienda, true);
		configurar(checkTienda, true, false);
		configurar(comboComprador, true);
		configurar(checkComprador, true, false);
		configurar(checkDetailed, false, false); // Por defecto no se muestra el detalle de productos 
												// y está bloqueado hasta seleccionar categoría
		
		//Este indicador exige que los departamentos dependan de la categoria
		configurar(comboDepartment, false);
		configurar(checkDepartment, false);
						
		//Así mismo dice que el período es un campo obligatorio para procesar
		monthPeriod.setBackground(true);
		yearPeriod.setBackground(true);
		
		//Comenzando
		configurar(bSearch, false);
		configurar(bPrint, false);
		configurar(bFile, false);
	
		statusBar.setStatusLine("");
		
		//Agregar listeners a los nuevos campos
		checkComprador.addActionListener(this);
		checkTienda.addActionListener(this);
		checkDetailed.addActionListener(this);
		lookupProduct.addActionListener(this);
	}
	
	protected void limpiarFiltro () {
		
		//Limpia los campos del filtro padre
		super.limpiarFiltro();		
		
		//Borrar aquellos campos que se agregaron en esta forma
		configurar(comboTienda, true);
		configurar(checkTienda, true, false);		
		configurar(comboComprador, true);
		configurar(checkComprador, true, false);
		configurar(checkDetailed, false, false);
	}
	

	@Override
	/** En este método agrego acciones a las acciones predeterminadas */
	public void actionPerformed(ActionEvent e) {
		
		//Ejecutará las acciones por defecto
		super.actionPerformed(e);
		
		//Desactivar los escuchadores mientras realizo las modifciaciones
		desactivarActionListeners();
		checkComprador.removeActionListener(this);
		checkTienda.removeActionListener(this);
		checkDetailed.removeActionListener(this);
		lookupProduct.removeActionListener(this);
		

		//Acciones adicionales a las que vienen con el padre para deshabilitar un campo
		if (e.getSource() == checkCategory) {			
			configurar(comboDepartment, false);				
			configurar(checkDepartment, false);
			configurar(checkDetailed, false, false);
		}
		else if (e.getSource() == comboCategory) {			
			KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
			if (catg != null && catg.getKey() != 0 && catg.getKey() != 99999999){ 
				configurar(comboDepartment, true);				
				configurar(checkDepartment, true, false);
				//if(lookupProduct.getValue() == null){
					configurar(checkDetailed, true, false);
				//}
			} else {
				configurar(comboDepartment, false);				
				configurar(checkDepartment, false);
				configurar(checkDetailed, false, false);
			}	
			
		//Verificar si se selecciono el check de comprador
		}
		if (e.getSource() == checkComprador) {			
			if ((Boolean)checkComprador.getValue()) {				
				configurar(comboComprador, false, 99999999 );				
			} else {
				configurar(comboComprador, true);
			}
		}
		//Verificar si se selecciono el check de comprador
		if (e.getSource() == checkTienda) {			
			if ((Boolean)checkTienda.getValue()) {				
				configurar(comboTienda, false, 99999999 );				
			} else {
				configurar(comboTienda, true);
			}
		}
		else if (e.getSource() == monthPeriod || e.getSource() == yearPeriod) {
			boolean procesar = true;
			if (monthPeriod.getValue() != null) {
				monthPeriod.setBackground(false);				
			} else {
				monthPeriod.setBackground(true);
				procesar = false;
			}				
			if (yearPeriod.getValue() != null) {
				yearPeriod.setBackground(false);				
			} else {
				yearPeriod.setBackground(true);
				procesar = false;
			}
			//Verificar que los campos periodo se hayan marcado
			configurar(bSearch, procesar);			
		} 
		else if(e.getSource() == checkProduct){
			if((Boolean) checkProduct.getValue() == true){
				configurar(checkDetailed, false, true);
			}else{
				configurar(checkDetailed, true, false);
			}
		}
		else if (e.getSource() == lookupProduct){
			if(lookupProduct.getValue() != null){
				configurar(checkDetailed, false, false);
			}else{
				configurar(checkDetailed, true);
			}
		}

		//Activar los escuchadores
		activarActionListeners();
		checkComprador.addActionListener(this);
		checkTienda.addActionListener(this);
		checkDetailed.addActionListener(this);
		lookupProduct.addActionListener(this);
	}

	
	/** Cargar las tiendas  */
	protected final void cargarTiendas() {
		
		KeyNamePair loadKNP;
		String sql = "";						
		comboTienda.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboTienda.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllStores"));
		comboTienda.addItem(loadKNP);	
		sql = " SELECT ct.M_WAREHOUSE_ID, ct.VALUE||'-'||ct.NAME " +
				" FROM M_WAREHOUSE ct WHERE ct.ISACTIVE = 'Y' " +
				"AND ct.M_WAREHOUSE_ID != " + Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID")
				+ "ORDER BY ct.VALUE"; 		
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboTienda.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
	}
	
	/** Cargar los compradores */
	protected final void cargarCompradores() {
		
		KeyNamePair loadKNP;
		String sql = "";						
		comboComprador.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboComprador.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllBuyers"));
		comboComprador.addItem(loadKNP);	
		sql = "SELECT C_BPARTNER_ID, NAME FROM AD_USER WHERE ISACTIVE='Y' AND C_BPARTNER_ID IN "+
			"(SELECT C_BPARTNER_ID " +
			"FROM C_BPARTNER WHERE isActive='Y' "+
			"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_BUYER_ID")+ ") " +
			" ORDER BY NAME";
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboComprador.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)	{
			log.log(Level.SEVERE, sql, e);
		}	
	}
	
	@Override
	protected void llenarTabla() {

		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		//Verificar que el periodo se haya 
		if (monthPeriod.getValue() == null || yearPeriod.getValue() == null) {
			ADialog.error(m_WindowNo, m_frame, "XX_MandatoryPeriod" );
			return;		
		}	

		// Limpiar y crear nuevamente
		dataPane.getViewport().remove(miniTable);
		miniTable = new MiniTablePreparator();
		miniTable.setRowCount(0);
		dataPane.getViewport().add(miniTable, null);
		miniTable.setAutoResizeMode(MiniTable.AUTO_RESIZE_OFF);
		
		// Si no se ha seleccionado el detallado o si se ha seleccionado producto
		if (!(Boolean)checkDetailed.getValue() || lookupProduct.getValue() != null){
			dataPane2.setVisible(false);
			dataPane.setPreferredSize(new Dimension(1200, 600)); //Redimensionar el primer panel
			dataPane.setMaximumSize(null);
			//dataPane2.setPreferredSize(new Dimension(1200, 0));
			//Calcular el query
			try {
				calcularQuery(miniTable, false);			
				miniTable.setRowSelectionAllowed(true);
				miniTable.setSelectionBackground(Color.white);
				miniTable.autoSize();
				miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
				miniTable.getTableHeader().setReorderingAllowed(false);			
				if (miniTable.getRowCount() > 0) {
					configurar(bFile, true);				
					configurar(bPrint, true);
				} else {
					configurar(bFile, false);
					configurar(bPrint, false);
				}
			} catch (Exception e) {}
		
		} else {
			// Si se ha seleccionado el detallado, además mostrar por producto
			dataPane2.setVisible(true);
			dataPane2.getViewport().remove(miniTable2);
			miniTable2 = new MiniTablePreparator();
			miniTable2.setRowCount(0);
			dataPane2.getViewport().add(miniTable2, null);
			miniTable2.setAutoResizeMode(MiniTable.AUTO_RESIZE_OFF);
			
			dataPane.setPreferredSize(new Dimension(1200, 80)); //Redimensionar el primer panel
			dataPane.setMinimumSize(new Dimension(1200, 80));
			dataPane.setMaximumSize(new Dimension(1200, 80));
			//dataPane.setSize(new Dimension(1200, 80));
			dataPane2.setPreferredSize(new Dimension(1200, 500));
			dataPane2.setMinimumSize(new Dimension(1200, 500));
			
			//Calcular el query
			try {
				calcularQuery(miniTable2, true);				
				miniTable2.setRowSelectionAllowed(true);
				miniTable2.setSelectionBackground(Color.white);
				miniTable2.autoSize();
				miniTable2.setRowHeight(miniTable2.getRowHeight() + 2 );
				miniTable2.getTableHeader().setReorderingAllowed(false);			
				if (miniTable2.getRowCount() > 0) {
					configurar(bFile, true);				
					configurar(bPrint, true);
				} else {
					configurar(bFile, false);
					configurar(bPrint, false);
				}
			} catch (Exception e) {}
		} 
		repaint();
		dataPane.validate();
		dataPane2.validate();
		centerPanel.validate();
		mainPanel.validate();
		
		m_frame.setCursor(Cursor.getDefaultCursor());		
	}
	
	/** Se determina el query de acuerdo a los parámetros ingresados*/
	public void calcularQuery(MiniTablePreparator miniTable, boolean detall) {
		int mes = (Integer)monthPeriod.getValue(); 
		int año = (Integer)yearPeriod.getValue();	
		
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair sect = (KeyNamePair)comboSection.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();		
		KeyNamePair bcomp= (KeyNamePair)comboComprador.getSelectedItem();
		KeyNamePair tienda= (KeyNamePair)comboTienda.getSelectedItem();
		String with = 
			" WITH " +
			"\nVENTAS AS " +
			"\n(SELECT L.M_PRODUCT_ID, C.M_WAREHOUSE_ID, " +
			"\nSUM(L.QTYORDERED) PIEZAS, " +
			"\nROUND(SUM(L.LINENETAMT * (1 + (C.XX_EMPLOYEEDISCOUNT/C.TOTALLINES))), 2) MONTO " +
			"\nFROM C_ORDER C, C_ORDERLINE L " +
			"\nWHERE C.AD_CLIENT_ID = " + ctx.getAD_Client_ID() + 
			"\nAND C.ISSOTRX= 'Y' " +
			"\nAND TO_CHAR(C.DATEORDERED, 'MM') = " + mes +
			"\nAND TO_CHAR(C.DATEORDERED, 'YYYY') = " + año +
			"\nAND L.C_ORDER_ID = C.C_ORDER_ID " +		
			"\nGROUP BY L.M_PRODUCT_ID, C.M_WAREHOUSE_ID), \n" +	
			
			"\nINVENTARIOINICIAL AS " +
			"\n(SELECT M_PRODUCT_ID, M_WAREHOUSE_ID," +
			"			SUM(IV.QTY) PIEZAS," +
			"			A.priceactual MONTO" +
			"\n	FROM M_StorageDetail IV  JOIN M_LOCATOR L ON (IV.M_LOCATOR_ID = L.M_LOCATOR_ID)" +
			"\n	       join m_attributesetinstance A on (IV.m_attributesetinstance_id = A.m_attributesetinstance_id)" +
			"\nWHERE M_WAREHOUSE_ID != " + Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID") +
			"\nAND AD_CLIENT_ID = " + ctx.getAD_Client_ID() +
			"\nAND TO_CHAR(IV.CREATED, 'MM') = " + mes +
			"\nAND TO_CHAR(IV.CREATED, 'YYYY') = " + año +
			"\nAND IV.QtyType= 'H' " +
			"\nGROUP BY IV.M_PRODUCT_ID, L.M_WAREHOUSE_ID, A.priceactual),\n" +			
			"\nINGRESOSTIENDA AS " +
			"\n(SELECT T.M_PRODUCT_ID, L.M_WAREHOUSE_ID, " +
			"\nSUM(T.MOVEMENTQTY) PIEZAS, SUM(T.MOVEMENTQTY*P.XX_SALEPRICE) MONTO " +
			"\nFROM M_TRANSACTION T JOIN M_LOCATOR L ON (T.M_LOCATOR_ID = L.M_LOCATOR_ID) " +
			"\nJOIN M_MOVEMENTLINE M ON (T.M_MOVEMENTLINE_ID = M.M_MOVEMENTLINE_ID) "+
			"\nJOIN XX_VMR_PRICECONSECUTIVE P ON (P.XX_PRICECONSECUTIVE = M.XX_PRICECONSECUTIVE AND T.M_PRODUCT_ID = P.M_PRODUCT_ID) " +
			"\nWHERE L.M_WAREHOUSE_ID != " + Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID") +
			"\nAND T.AD_CLIENT_ID = " + ctx.getAD_Client_ID() + 
			"\nAND T.MOVEMENTTYPE = 'M+' " +
			"\nAND TO_CHAR(T.MOVEMENTDATE, 'MM') = " + mes +
			"\nAND TO_CHAR(T.MOVEMENTDATE, 'YYYY') = " + año +
			"\nAND L.ISDEFAULT = 'Y' " +
			"\nGROUP BY T.M_PRODUCT_ID, L.M_WAREHOUSE_ID) \n";
		String select = "";
		String from = "\nFROM " +
				"\n(SELECT V.*, 1 AS X FROM VENTAS V" +
				"\nUNION ALL" +
				"\nSELECT I.*, 2 AS X FROM INVENTARIOINICIAL I" +
				"\nUNION ALL" +
				"\nSELECT I.*, 3 AS X FROM INGRESOSTIENDA I) UNI" +
				"\nINNER JOIN M_PRODUCT PR ON (UNI.M_PRODUCT_ID = PR.M_PRODUCT_ID )";
		String where = "";
		String groupby = "" , groupById = ""; 
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();

		//Agregamos la información del departamento si se utilizará
		if (((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 ))
			|| ((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)
			|| ((Boolean)checkComprador.getValue()==true || bcomp != null && (bcomp.getKey() == 99999999 || bcomp.getKey()!= 0)))
		){  from += "\nINNER JOIN XX_VMR_DEPARTMENT dp ON (dp.XX_VMR_DEPARTMENT_ID = PR.XX_VMR_DEPARTMENT_ID) "; }
		
		//Categoría
		indiceTabla = 0;
		if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
			//indiceTabla++; 
			indiceCat = indiceTabla++;
			from += "\nINNER JOIN XX_VMR_CATEGORY ct ON (ct.XX_VMR_CATEGORY_ID = dp.XX_VMR_CATEGORY_ID) ";
			columnasAgregadas.add(colCatg);
			groupById = "\nct.XX_VMR_Category_ID";
			groupby += "\n,ct.XX_VMR_Category_ID, ct.Value||'-'||ct.Name";
			if (catg.getKey()!= 99999999 ) 
				if (where.isEmpty())
					where += "\nct.XX_VMR_Category_ID = " + catg.getKey();
				else 
					where += "\nAND ct.XX_VMR_Category_ID = " + catg.getKey();
		}		
		//Departamento			
		if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {
			//indiceTabla++;
			indiceDept = indiceTabla++;
			columnasAgregadas.add(colDept);
			groupById = "\ndp.XX_VMR_DEPARTMENT_ID";
			groupby += "\n,dp.XX_VMR_DEPARTMENT_ID, dp.value||'-'||dp.Name";
			if (dept.getKey()!= 99999999 ) 
				if (where.isEmpty())
					where += "\ndp.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
				else
					where += "\nAND dp.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
			
		}
		//Linea
		if((Boolean)checkLine.getValue()==true || line != null && (line.getKey() == 99999999 || line.getKey()!= 0)){
			indiceTabla++;
			from += "\nINNER JOIN XX_VMR_LINE li ON (li.XX_VMR_LINE_ID = PR.XX_VMR_LINE_ID)" ;
			columnasAgregadas.add(colLine);
			groupById = "\nli.XX_VMR_LINE_ID";
			groupby += "\n,li.XX_VMR_LINE_ID, li.VALUE||'-'||li.NAME";
			if (line.getKey()!= 99999999)
				if (where.isEmpty())
					where += "\nli.XX_VMR_LINE_ID = " + line.getKey();
				else 
					where += "\nAND li.XX_VMR_LINE_ID = " + line.getKey();
		}
		//Seccion 
		if ((Boolean)checkSection.getValue()==true || sect != null && (sect.getKey() == 99999999 || sect.getKey()!= 0)){
			indiceTabla++;
			from += "\nINNER JOIN XX_VMR_SECTION se ON (se.XX_VMR_SECTION_ID = PR.XX_VMR_SECTION_ID) ";
			columnasAgregadas.add(colSect);
			groupById = "\nse.XX_VMR_SECTION_ID";
			groupby += "\n, se.XX_VMR_SECTION_ID, se.VALUE||'-'||se.NAME";	
			if (sect.getKey()!= 99999999 ) 
				if (where.isEmpty())
					where += "\nse.XX_VMR_SECTION_ID = " + sect.getKey();
				else
					where += "\nAND se.XX_VMR_SECTION_ID = " + sect.getKey();			
		}
		// Producto
		if(detall || (Boolean)checkProduct.getValue() == true || lookupProduct.getValue() != null ){
			indiceTabla++;
			columnasAgregadas.add(colProd);
			groupById = "\n pr.M_PRODUCT_ID";
			groupby += "\n, pr.M_Product_ID, pr.value||'-'||pr.Name";			
			if (lookupProduct.getValue() != null && checkProduct.getValue().equals(false)) 
				if (where.isEmpty())
					where += "\npr.M_PRODUCT_ID = " + lookupProduct.getValue();
				else 
					where += "\nAND pr.M_PRODUCT_ID = " + lookupProduct.getValue();	
			
		}
		
		//Proveedor
		if((Boolean)checkBPartner.getValue() == true || bpar != null && (bpar.getKey() == 99999999 || bpar.getKey() != 0)){
			indiceTabla++;
			from += " \nINNER JOIN C_BPARTNER bp ON (bp.C_BPARTNER_ID = PR.C_BPARTNER_ID) ";
			columnasAgregadas.add(colBPar);
			groupById = "\nbp.C_BPARTNER_ID";
			groupby += "\n, bp.NAME, bp.C_BPARTNER_ID";			
			if (bpar.getKey()!= 99999999) 
				if (where.isEmpty())
					where += "\nbp.C_BPARTNER_ID = " + bpar.getKey();
				else
					where += "\nAND bp.C_BPARTNER_ID = " + bpar.getKey();				
		}
		//Almacén 
		if ((Boolean)checkTienda.getValue()==true || tienda != null && (tienda.getKey() == 99999999 || tienda.getKey()!= 0)){	
			indiceTabla++;
			from += "\nINNER JOIN M_WAREHOUSE wa ON (wa.M_WAREHOUSE_ID = UNI.M_WAREHOUSE_ID) ";
			columnasAgregadas.add(colSto);
			groupById = "\nuni.M_WAREHOUSE_ID";
			groupby += "\n, wa.VALUE||'-'||wa.NAME, uni.M_WAREHOUSE_ID";	
			if (tienda.getKey()!= 99999999 ) 
				if (where.isEmpty())
					where += "\nuni.M_WAREHOUSE_ID = " + tienda.getKey();			
				else 
					where += "\nAND uni.M_WAREHOUSE_ID = " + tienda.getKey();
		}
		//Comprador
		if ((Boolean)checkComprador.getValue()==true || bcomp != null && (bcomp.getKey() == 99999999 || bcomp.getKey()!= 0)){	
			indiceTabla++;
			from += "\nINNER JOIN AD_USER us ON (dp.XX_USERBUYER_ID = us.C_BPartner_ID)";					
			columnasAgregadas.add(colBuy);
			groupById = "\ndp.XX_USERBUYER_ID";
			groupby += "\n, us.NAME, dp.XX_USERBUYER_ID";	
			if (bcomp.getKey()!= 99999999 )
			if (where.isEmpty())
				where += "\ndp.XX_USERBUYER_ID = " + bcomp.getKey();			
			else 
				where += "\nAND dp.XX_USERBUYER_ID = " + bcomp.getKey();
		}	
		// Las cantidades 		
		columnasAgregadas.add(colQntyA);
		columnasAgregadas.add(colQntyB);
		columnasAgregadas.add(colQntyC);
		columnasAgregadas.add(colQntyD);		
		columnasAgregadas.add(colQntyE);
		columnasAgregadas.add(colQntyF);
		columnasAgregadas.add(colQntyG);
		columnasAgregadas.add(colQntyH);
		columnasAgregadas.add(colQntyI);
		columnasAgregadas.add(colQntyJ);

		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);
		
		select = "\n" + miniTable.prepareTable(layout, null, null, false, null);
		String sql =  with + select + from ;
		
		if (!where.isEmpty()) {
			where = "\nWHERE  " + where;
			sql += where;
		}
		if (!groupById.isEmpty()) {
			groupby = "\nGROUP BY " + groupById + groupby;
			sql += groupby ;
		}	
		PreparedStatement ps = null;
		ResultSet rs = null;
		//System.out.println(sql);
		
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
		
		//Calcular el indicador
		final int SellThroughPieces  = indiceTabla;
		final int SellThroughAmount = indiceTabla + 1;
		final int PerformancePieces = indiceTabla + 2;
		final int PerformanceAmount = indiceTabla + 3;		
		
		SalesPiecesTot = 0; SalesAmountTot = 0; InventoryPiecesTot = 0; InventoryAmountTot = 0; 
		AditionPiecesTot = 0; AditionAmountTot = 0;	

		for (int fila = 0; fila < miniTable.getRowCount(); fila++) {

			Double SalesPieces = (Double)miniTable.getValueAt(fila, indiceTabla + 4);
			Double SalesAmount = (Double)miniTable.getValueAt(fila, indiceTabla + 5);		
			Double InventoryPieces = (Double)miniTable.getValueAt(fila, indiceTabla + 6);
			Double InventoryAmount = (Double)miniTable.getValueAt(fila, indiceTabla + 7);
			Double AditionPieces =  (Double)miniTable.getValueAt(fila, indiceTabla + 8);
			Double AditionAmount = (Double)miniTable.getValueAt(fila, indiceTabla + 9);		
			
			SalesPiecesTot += SalesPieces;
			SalesAmountTot += SalesAmount;
			InventoryPiecesTot += InventoryPieces;
			InventoryAmountTot += InventoryAmount;
			AditionPiecesTot += AditionPieces;
			AditionAmountTot += AditionAmount;
			
			configurarIndicadorVentas(miniTable, fila, SalesPieces, InventoryPieces, SellThroughPieces);
			configurarIndicadorVentas(miniTable, fila, SalesAmount, InventoryAmount, SellThroughAmount);
			configurarIndicadorDesempeño(miniTable, fila, SalesPieces, InventoryPieces, AditionPieces, PerformancePieces);
			configurarIndicadorDesempeño(miniTable, fila, SalesAmount, InventoryAmount, AditionAmount, PerformanceAmount);
		}		

		if (detall){
			cargarTablaCons();
		}
		miniTable.setAutoResizeMode(0);
	}
	
	
	private void cargarTablaCons() {
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();

		columnasAgregadas.add(consQntyA);
		columnasAgregadas.add(consQntyB);
		columnasAgregadas.add(consQntyC);
		columnasAgregadas.add(consQntyD);	
		columnasAgregadas.add(consQntyE);
		columnasAgregadas.add(consQntyF);
		columnasAgregadas.add(consQntyG);
		columnasAgregadas.add(consQntyH);
		columnasAgregadas.add(consQntyI);
		columnasAgregadas.add(consQntyJ);

		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);
		miniTable.prepareTable(layout, null, null, false, null);
		
		//redondeo el resultado
		DecimalFormat Result = new DecimalFormat("###,###.##");
		
//		System.out.println(SalesPiecesTot);
//		System.out.println(SalesAmountTot);
//		System.out.println(InventoryPiecesTot);	
//		System.out.println(InventoryAmountTot);						
//		System.out.println(AditionPiecesTot);
//		System.out.println(AditionAmountTot);
		
		miniTable.setRowCount(1);
		int i = 0;
		miniTable.setValueAt(calcIndVentas(SalesPiecesTot, InventoryPiecesTot), 0, i++);
		miniTable.setValueAt(calcIndVentas(SalesAmountTot, InventoryAmountTot), 0, i++);
		miniTable.setValueAt(calcIndDes(SalesPiecesTot, InventoryPiecesTot, AditionPiecesTot), 0, i++);
		miniTable.setValueAt(calcIndDes(SalesAmountTot, InventoryAmountTot, AditionAmountTot), 0, i++);
	
		miniTable.setValueAt(Result.format(SalesPiecesTot), 0, i++);
		miniTable.setValueAt(Result.format(SalesAmountTot), 0, i++);
		miniTable.setValueAt(Result.format(InventoryPiecesTot), 0, i++);	
		miniTable.setValueAt(Result.format(InventoryAmountTot), 0, i++);						
		miniTable.setValueAt(Result.format(AditionPiecesTot), 0, i++);
		miniTable.setValueAt(Result.format(AditionAmountTot), 0, i++);
		miniTable.repaint();
	
		miniTable.getValueAt(0, 4).getClass();

		//miniTable.setAutoResizeMode(3);
		miniTable.setAutoResizeMode(0);
	}
		
	private double calcIndVentas(Double dividendo, Double divisor) {
		if (dividendo == null)
			dividendo = 0.0;
		if (divisor == null)
			divisor = 0.0;
		if (divisor == 0.0) 
			return new Double(0.0);
		else
			return new BigDecimal(dividendo/divisor).multiply(Env.ONEHUNDRED)
					.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	/** Configurar el indicador de ventas*/
	private void configurarIndicadorVentas (MiniTablePreparator miniTable, int fila, 
			Double dividendo, Double divisor, int resultadoInd) {
		
		miniTable.setValueAt(calcIndVentas(dividendo, divisor), fila, resultadoInd);
	}
	
	private double calcIndDes(Double dividendo, Double divisorA, Double divisorB){
		if (dividendo == null)
			dividendo = 0.0;
		if (divisorA == null)
			divisorA = 0.0;
		if (divisorB == null)
			divisorB = 0.0;
		if ((divisorA + divisorB)  == 0.0) 
			return new Double(0.0);
		else
			return new BigDecimal(dividendo/(divisorA + divisorB)).multiply(Env.ONEHUNDRED).
					setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	/** Configurar el indicador de desempeño */
	private void configurarIndicadorDesempeño (MiniTablePreparator miniTable, int fila, 
			Double dividendo, Double divisorA, Double divisorB, int resultadoInd) {
		
		miniTable.setValueAt(calcIndDes(dividendo, divisorA, divisorB), fila, resultadoInd);
	}
	
	//Cabeceras de las columnas de la tabla 
	private ColumnInfo colCatg = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Category_ID"), 
			"\nct.Value||'-'||ct.Name", KeyNamePair.class, true, false, "\nct.XX_VMR_Category_ID");
	private ColumnInfo colDept = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Department_ID"),
			"\ndp.value||'-'||dp.Name", KeyNamePair.class, true, false, "\ndp.XX_VMR_DEPARTMENT_ID");
	private ColumnInfo colLine = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Line_ID"),
			"\nli.VALUE||'-'||li.NAME", KeyNamePair.class, true, false, "\nli.XX_VMR_LINE_ID");
	private ColumnInfo colSect = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Section_ID"), 
			"\nse.VALUE||'-'||se.NAME", KeyNamePair.class, true, false, "\nse.XX_VMR_SECTION_ID");
	private ColumnInfo colProd = new ColumnInfo(Msg.translate(ctx, "Product"),
			"\npr.value||'-'||pr.Name", String.class);
	private ColumnInfo colBPar = new ColumnInfo(Msg.translate(ctx, "C_BPARTNER_ID"), 
			"\nbp.NAME", KeyNamePair.class, true, false, "\nbp.C_BPARTNER_ID");
	private ColumnInfo colSto = new ColumnInfo(Msg.translate(ctx, "XX_Store"),
			"\nwa.VALUE||'-'||wa.NAME", KeyNamePair.class, true, false, "\nuni.M_WAREHOUSE_ID");
	private ColumnInfo colBuy = new ColumnInfo(Msg.translate(ctx, "XX_Buyer"), 
			"\nus.NAME", KeyNamePair.class, true, false, "dp.XX_USERBUYER_ID");
	
	private ColumnInfo colQntyA = new ColumnInfo( Msg.translate(ctx, "XX_SellThrough") 
			+ "-" + Msg.translate(ctx, "XX_Pieces"), "0", Double.class );
	private ColumnInfo colQntyB = new ColumnInfo(Msg.translate(ctx, "XX_SellThrough") +
			"-Bs", "0", Double.class );
	private ColumnInfo colQntyC = new ColumnInfo(Msg.translate(ctx, "Performance") + 
			"-" + Msg.translate(ctx, "XX_Pieces"), "0", Double.class );
	private ColumnInfo colQntyD = new ColumnInfo(Msg.translate(ctx, "Performance") +
			"-Bs", "0", Double.class );	
	private ColumnInfo colQntyE = new ColumnInfo(Msg.translate(ctx, "XX_Sales") +
			"-" + Msg.translate(ctx, "XX_Pieces"), "\nSUM(DECODE(X, 1,  UNI.PIEZAS, 0)) PZASVENTA", Double.class);
	private ColumnInfo colQntyF = new ColumnInfo(Msg.translate(ctx, "XX_Sales") +
			"-Bs",  "\nSUM(DECODE(X, 1,  UNI.MONTO, 0)) MNTVENTA", Double.class);	
	private ColumnInfo colQntyG = new ColumnInfo(Msg.getMsg(ctx, "XX_InventoryQty") +
			"-" + Msg.translate(ctx, "XX_Pieces"), "\nSUM(DECODE(X, 2,  UNI.PIEZAS, 0)) PZASINV", Double.class);
	private ColumnInfo colQntyH = new ColumnInfo(Msg.getMsg(ctx, "XX_InventoryQty") +
			"-Bs",  "\nSUM(DECODE(X, 2,  UNI.PIEZAS*UNI.MONTO, 0)) MNTINV", Double.class);
	private ColumnInfo colQntyI = new ColumnInfo(Msg.translate(ctx, "XX_Aditions") +
			"-" + Msg.translate(ctx, "XX_Pieces"), "\nSUM(DECODE(X, 3,  UNI.PIEZAS, 0)) PZASADC", Double.class);
	private ColumnInfo colQntyJ = new ColumnInfo(Msg.translate(ctx, "XX_Aditions") +
			"-Bs",  "\nSUM(DECODE(X, 3,  UNI.MONTO, 0)) MNTADC", Double.class);

	double SalesPiecesTot = 0, SalesAmountTot = 0, InventoryPiecesTot = 0, InventoryAmountTot = 0, 
			AditionPiecesTot = 0, AditionAmountTot = 0;	
	
	int indiceCat = 0, indiceDept = 0;
	String Category = "", Dept = "";
	
	private ColumnInfo consQntyA = new ColumnInfo( Msg.translate(ctx, "XX_SellThrough") 
			+ "-" + Msg.translate(ctx, "XX_Pieces"), "0", Double.class );
	private ColumnInfo consQntyB = new ColumnInfo(Msg.translate(ctx, "XX_SellThrough") +
			"-Bs", "0", Double.class );
	private ColumnInfo consQntyC = new ColumnInfo(Msg.translate(ctx, "Performance") + 
			"-" + Msg.translate(ctx, "XX_Pieces") , "0", Double.class );
	private ColumnInfo consQntyD = new ColumnInfo(Msg.translate(ctx, "Performance") +
			"-Bs", "0", Double.class );	
	private ColumnInfo consQntyE = new ColumnInfo(Msg.translate(ctx, "XX_Sales") +
			"-" + Msg.translate(ctx, "XX_Pieces") , "\nSUM(DECODE(X, 1,  UNI.PIEZAS, 0)) PZASVENTA", String.class);
	private ColumnInfo consQntyF = new ColumnInfo(Msg.translate(ctx, "XX_Sales") +
			"-Bs",  "\nSUM(DECODE(X, 1,  UNI.MONTO, 0)) MNTVENTA", Double.class);	
	private ColumnInfo consQntyG = new ColumnInfo(Msg.getMsg(ctx, "XX_InventoryQty") +
			"-" + Msg.translate(ctx, "XX_Pieces") , "\nSUM(DECODE(X, 2,  UNI.PIEZAS, 0)) PZASINV", Double.class);
	private ColumnInfo consQntyH = new ColumnInfo(Msg.getMsg(ctx, "XX_InventoryQty") +
			"-Bs",  "\nSUM(DECODE(X, 2,  UNI.PIEZAS*UNI.MONTO, 0)) MNTINV", Double.class);
	private ColumnInfo consQntyI = new ColumnInfo(Msg.translate(ctx, "XX_Aditions") +
			"-" + Msg.translate(ctx, "XX_Pieces") , "\nSUM(DECODE(X, 3,  UNI.PIEZAS, 0)) PZASADC", Double.class);
	private ColumnInfo consQntyJ = new ColumnInfo(Msg.translate(ctx, "XX_Aditions") +
			"-Bs",  "\nSUM(DECODE(X, 3,  UNI.MONTO, 0)) MNTADC", Double.class);

}


