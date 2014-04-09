package compiere.model.cds.forms.indicator;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.grid.ed.VComboBox;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;

/** Clase que extiende al indicador genérico y representa al indicador de planificación
 *  Agrega campos adicionales como tienda, comprador y planificador
 *  @author Gabrielle Huchet
 * */
public class XX_Planning extends XX_Indicator {

	private static final long serialVersionUID = 5315412951677799404L;
	
	//Campos necesarios para este indicador 
	private CLabel labelTienda = new CLabel(Msg.translate(ctx, "XX_Store"));
	private VComboBox comboTienda = new VComboBox();
	private CCheckBox checkTienda = new CCheckBox();
	
	private CLabel labelComprador = new CLabel(Msg.translate(ctx, "XX_Buyer"));
	private VComboBox comboComprador = new VComboBox();
	private CCheckBox checkComprador = new CCheckBox();
	
	private CLabel labelPlanificador= new CLabel(Msg.translate(ctx, "XX_InventorySchedule_ID"));
	private VComboBox comboPlanificador = new VComboBox();
	private CCheckBox checkPlanificador = new CCheckBox();

	private ColumnInfo colWare = new ColumnInfo(Msg.translate(ctx, "M_Warehouse_ID"), "W.VALUE||'-'||W.NAME",String.class);
	private ColumnInfo colCatg = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Category_ID"), "C.VALUE||'-'||C.NAME" ,String.class);
	private ColumnInfo colDept = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Department_ID"), "D.VALUE||'-'||D.NAME",String.class);
	private ColumnInfo colLine = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Line_ID"), "L.VALUE||'-'||L.NAME",String.class);
	private ColumnInfo colSec = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Section_ID"), "S.VALUE||'-'||S.NAME",String.class);
	private ColumnInfo colProd = new ColumnInfo(Msg.translate(ctx, "M_Product_ID"),  "P.VALUE||'-'||P.NAME",String.class);
	private ColumnInfo colBPar = new ColumnInfo(Msg.translate(ctx, "C_BPARTNER_ID"), "BP.NAME",String.class);
	private ColumnInfo colBuy = new ColumnInfo(Msg.translate(ctx, "XX_Buyer"),  "U1.DESCRIPTION",String.class);
	private ColumnInfo colPlan = new ColumnInfo(Msg.translate(ctx, "XX_InventorySchedule_ID"),  "U2.DESCRIPTION",String.class);
	private ColumnInfo colTA =	new ColumnInfo("Tiempo de Aprobación","SUM(UA.T1)", String.class);
	private ColumnInfo colRMP = new ColumnInfo("Piezas Rotación Mensual ","SUM(UA.T2A)", Double.class);
	private ColumnInfo colRMB = new ColumnInfo("Bs Rotación Mensual ","SUM(UA.T2B)", Double.class);
	private ColumnInfo colRAMP = new ColumnInfo("Piezas Rotación Acumulada del Mes","SUM(UA.T3A)", Double.class);
	private ColumnInfo colRAMB = new ColumnInfo("Bs Rotación Acumulada del Mes","SUM(UA.T3B)", Double.class);
	private ColumnInfo colCMP = new ColumnInfo("Piezas Cobertura Mensual","SUM(UA.T4A)", Double.class);
	private ColumnInfo colCMB = new ColumnInfo("Bs Cobertura Mensual","SUM(UA.T4B)", Double.class);
	private ColumnInfo colPBF = new ColumnInfo("Producto Básico en Falla","SUM(UA.T5)", Double.class);
		
	private Integer CENTRO_DISTRIBUCION = Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"); 
	private Integer BASICO = Env.getCtx().getContextAsInt("#XX_L_TYPEINVENTORYBASICO_ID");
	protected void ocultarParametrosDefecto() {
		
	}

	@Override
	protected void agregarParametros() {

		//Agregar Comprador, Planificador y  Tienda
		agregarParametro(labelTienda);
		agregarParametro(comboTienda);
		agregarParametro(checkTienda);		
		agregarParametro(labelComprador);
		agregarParametro(comboComprador);
		agregarParametro(checkComprador);	
		agregarParametro(labelPlanificador);
		agregarParametro(comboPlanificador);
		agregarParametro(checkPlanificador);	
	}
	

	@Override
	protected void personalizar() {
		
		//Cargar los datos de los campos que se agregaron en este indicador
		cargarTiendas();
		cargarCompradores();
		cargarPlanificadores();
		
		//Configurar dichos campos
		configurar(comboTienda, true);
		configurar(checkTienda, true, false);
		configurar(comboComprador, true);
		configurar(checkComprador, true, false);
		configurar(comboPlanificador, true);
		configurar(checkPlanificador, true, false);
		
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
	
		//Agregar listeners a los nuevos campos
		addActionListeners();
		
	}

	private void addActionListeners(){
		checkComprador.addActionListener(this);
		checkTienda.addActionListener(this);
		checkPlanificador.addActionListener(this);
	}
	
	private void removeActionListeners(){
		checkComprador.removeActionListener(this);
		checkTienda.removeActionListener(this);
		checkPlanificador.removeActionListener(this);
	}
	
	protected void limpiarFiltro () {
		
		//Limpia los campos del filtro padre
		super.limpiarFiltro();		
		
		//Borrar aquellos campos que se agregaron en esta forma
		configurar(comboTienda, true);
		configurar(checkTienda, true, false);		
		configurar(comboComprador, true);
		configurar(checkComprador, true, false);
		configurar(comboPlanificador, true);
		configurar(checkPlanificador, true, false);
	}
	
	@Override
	/** En este método agrego acciones a las acciones predeterminadas */
	public void actionPerformed(ActionEvent e) {
		
		//Ejecutará las acciones por defecto
		super.actionPerformed(e);
		
		//Desactivar listeners mientras realizo las modificaciones
		desactivarActionListeners();
		removeActionListeners();

		//Acciones adicionales a las que vienen con el padre para deshabilitar un campo
		if (e.getSource() == checkCategory) {			
			configurar(comboDepartment, false);				
			configurar(checkDepartment, false);
		}
		else if (e.getSource() == comboCategory) {			
			KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
			if (catg != null && catg.getKey() != 0 && catg.getKey() != 99999999){ 
				configurar(comboDepartment, true);				
				configurar(checkDepartment, true, false);
			} else {
				configurar(comboDepartment, false);				
				configurar(checkDepartment, false);
			}	
			
		//Verificar si se selecciono el check de comprador
		}
		else if (e.getSource() == checkComprador) {			
			if ((Boolean)checkComprador.getValue()) {				
				configurar(comboComprador, false, 99999999 );				
			} else {
				configurar(comboComprador, true);
			}
		}
		//Verificar si se selecciono el check de tienda
		else if (e.getSource() == checkTienda) {			
			if ((Boolean)checkTienda.getValue()) {				
				configurar(comboTienda, false, 99999999 );				
			} else {
				configurar(comboTienda, true);
			}
		}
		//Verificar si se selecciono el check de planificador
		else if (e.getSource() == checkPlanificador) {			
			if ((Boolean)checkPlanificador.getValue()) {				
				configurar(comboPlanificador, false, 99999999 );				
			} else {
				configurar(comboPlanificador, true);
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
		
		//Activar  listeners
		activarActionListeners();
		addActionListeners();
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
	
	/** Cargar los Planificadores */
	protected final void cargarPlanificadores() {
		
		KeyNamePair loadKNP;
		String sql = "";						
		comboPlanificador.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboPlanificador.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllPlanners"));
		comboPlanificador.addItem(loadKNP);	
		sql = "SELECT C_BPARTNER_ID, NAME FROM AD_USER WHERE ISACTIVE='Y' AND C_BPARTNER_ID IN "+
			"(SELECT C_BPARTNER_ID " +
			"FROM C_BPARTNER WHERE isActive='Y' "+
			"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_INVENSCHED_ID")+ ") " +
			" ORDER BY NAME";
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboPlanificador.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)	{
			log.log(Level.SEVERE, sql, e);
		}	
	}
	protected void llenarTabla() {
	
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		//Si no se ha cargado el header previamente
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);
	
		//Calcular el query
		try {
			calcularQuery();
	
			miniTable.setRowSelectionAllowed(true);
			miniTable.setSelectionBackground(Color.white);
			miniTable.setAutoResizeMode(4);
			miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
			miniTable.getTableHeader().setReorderingAllowed(false);			
			if (miniTable.getRowCount() > 0) {
				configurar(bFile, true);				
				configurar(bPrint, true);
			} else {
				configurar(bFile, false);
				configurar(bPrint, false);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
		
		m_frame.setCursor(Cursor.getDefaultCursor());
		
		
	}

	private void calcularQuery() {
		
		int month = (Integer)monthPeriod.getValue(); 
		int year = (Integer)yearPeriod.getValue();	
		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair sect = (KeyNamePair)comboSection.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();		
		KeyNamePair bcomp= (KeyNamePair)comboComprador.getSelectedItem();
		KeyNamePair bplan= (KeyNamePair)comboPlanificador.getSelectedItem();
		KeyNamePair ware= (KeyNamePair)comboTienda.getSelectedItem();
		Integer prod = null;
		if((Boolean)checkProduct.getValue() == false){
			prod = (Integer)lookupProduct.getValue();
		}
		
		
		String with = "\nWITH ";
		String selectT1 ="";
		String fromT1 ="";
		String whereT1 ="";
		String groupT1 ="";
		String selectT2 ="";
		String fromT2 ="";
		String whereT2 ="";
		String groupT2 ="";
		String selectT3a ="";
		String selectT3 ="";
		String fromT3 ="";
		String whereT3 ="";
		String groupT3 ="";
		String selectT4 ="";
		String selectT5 ="";
		String selectT6 ="";
		String fromT6 ="";
		String whereT6 ="";
		String groupT6 ="";
		String selectT7 ="";
		String fromT7 ="";
		String whereT7 ="";
		String groupT7 ="";
		String selectT8 ="";
		String fromT8 ="";
		String whereT8 ="";
		String union ="";

		selectT1 = "\nORDERS AS ( SELECT O.C_ORDER_ID ";
		fromT1 = "\nFROM C_ORDER O JOIN C_ORDERLINE OL ON (O.C_ORDER_ID = OL.C_ORDER_ID) ";
		whereT1 = "\nWHERE O.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" "+
				"\nAND TO_CHAR(O.XX_ORDERREADYDATE,'MM')= " +month+
				"\nAND TO_CHAR(O.XX_ORDERREADYDATE,'YYYY')= " +year+
				"\nAND O.ISSOTRX = 'N' ";
		groupT1 =	"\nGROUP BY O.C_ORDER_ID ";

		selectT2 = "\n,TIEMPODEAPROBACION AS (SELECT  " + 									//TIEMPO DE APROBACION
				"\nROUND(SUM(O.XX_APPROVALDATE - trunc(O.XX_ORDERREADYDATE))/COUNT(O.C_ORDER_ID),2) T1, " +
				"\n0 T2A, 0 T2B, 0 T3A, 0 T3B, 0 T4A, 0 T4B, 0 T5 ";
		fromT2 = "\nFROM C_ORDER O INNER JOIN ORDERS OS ON (O.C_ORDER_ID = OS.C_ORDER_ID) ";
	
		whereT2 = "\nWHERE O.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" " +
				"\nAND TO_CHAR(O.XX_ORDERREADYDATE,'MM')= " +month+
				"\nAND TO_CHAR(O.XX_ORDERREADYDATE,'YYYY')= " +year+
				"\nAND O.ISSOTRX = 'N' ";
		groupT2 ="\nGROUP BY 1 ";

		selectT3a = "\n, ROTACIONMENSUAL AS (SELECT " +		 					//ROTACION MENSUAL
				"\n0 T1, " +
				"\n(CASE WHEN (SUM(INV.XX_INITIALINVENTORYQUANTITY) +  " +
				"\nSUM(INV.XX_INITIALINVENTORYQUANTITY + INV.XX_PREVIOUSADJUSTMENTSQUANTITY + " +
				"\nINV.XX_SHOPPINGQUANTITY - INV.XX_SALESQUANTITY + INV.XX_MOVEMENTQUANTITY + INV.XX_ADJUSTMENTSQUANTITY))<> 0 " +
				"\nTHEN ROUND(SUM(INV.XX_SALESQUANTITY)*12/(SUM(INV.XX_INITIALINVENTORYQUANTITY) + " +
				"\nSUM(INV.XX_INITIALINVENTORYQUANTITY + INV.XX_PREVIOUSADJUSTMENTSQUANTITY + " +
				"\nINV.XX_SHOPPINGQUANTITY - INV.XX_SALESQUANTITY + INV.XX_MOVEMENTQUANTITY + INV.XX_ADJUSTMENTSQUANTITY))/2,2) ELSE 0 END) T2A, " +
				"\n(CASE WHEN (SUM(INV.XX_INITIALINVENTORYAMOUNT) + SUM(INV.XX_INITIALINVENTORYAMOUNT + " +
				"\nINV.XX_PREVIOUSADJUSTMENTSAMOUNT + INV.XX_SHOPPINGAMOUNT - INV.XX_SALESAMOUNT + " +
				"\nINV.XX_MOVEMENTAMOUNT + INV.XX_ADJUSTMENTSAMOUNT))>0 THEN ROUND(SUM(INV.XX_SALESAMOUNT)*12/" +
				"\n(SUM(INV.XX_INITIALINVENTORYAMOUNT) + SUM(INV.XX_INITIALINVENTORYAMOUNT + INV.XX_PREVIOUSADJUSTMENTSAMOUNT + " +
				"\nINV.XX_SHOPPINGAMOUNT - INV.XX_SALESAMOUNT + INV.XX_MOVEMENTAMOUNT + INV.XX_ADJUSTMENTSAMOUNT))/2,2) ELSE 0 END) T2B, " +
				"\n0 T3A, 0 T3B, 0 T4A, 0 T4B, 0 T5 ";
		fromT3 = "\nFROM XX_VCN_INVENTORY INV ";
		whereT3 = "\nWHERE INV.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" "+
				"\nAND INV.XX_INVENTORYMONTH =  " +month+
				"\nAND INV.XX_INVENTORYYEAR = " +year+
				"\nAND INV.M_WAREHOUSE_ID <> "+CENTRO_DISTRIBUCION;
		groupT3 = "\nGROUP BY 1 ";

		selectT4 = "\n, ROTACIONACUMULADAMES AS (SELECT  " +
				"\n0 T1, 0 T2A, 0 T2B,  " +
				"\n(CASE WHEN (SUM(INV.XX_INITIALINVENTORYQUANTITY) +  " +
				"\nSUM(INV.XX_INITIALINVENTORYQUANTITY + INV.XX_PREVIOUSADJUSTMENTSQUANTITY +  " +
				"\nINV.XX_SHOPPINGQUANTITY - INV.XX_SALESQUANTITY + INV.XX_MOVEMENTQUANTITY + INV.XX_ADJUSTMENTSQUANTITY))<> 0 " +
				"\nTHEN ROUND(SUM(INV.XX_SALESQUANTITY)*360/TO_CHAR(SYSDATE-1,'DD')/  " +
				"\n(SUM(INV.XX_INITIALINVENTORYQUANTITY) +  " +
				"\nSUM(INV.XX_INITIALINVENTORYQUANTITY + INV.XX_PREVIOUSADJUSTMENTSQUANTITY +  " +
				"\nINV.XX_SHOPPINGQUANTITY - INV.XX_SALESQUANTITY + INV.XX_MOVEMENTQUANTITY + INV.XX_ADJUSTMENTSQUANTITY))/2,2)  " +
				"\nELSE 0 END) T3A, " +
				"\n(CASE WHEN (SUM(INV.XX_INITIALINVENTORYAMOUNT) +  " +
				"\nSUM(INV.XX_INITIALINVENTORYAMOUNT + INV.XX_PREVIOUSADJUSTMENTSAMOUNT +  " +
				"\nINV.XX_SHOPPINGAMOUNT - INV.XX_SALESAMOUNT + INV.XX_MOVEMENTAMOUNT + INV.XX_ADJUSTMENTSAMOUNT))>0 " +
				"\nTHEN ROUND(SUM(INV.XX_SALESAMOUNT)*360/TO_CHAR(SYSDATE-1,'DD')/  " +
				"\n(SUM(INV.XX_INITIALINVENTORYAMOUNT) +  " +
				"\nSUM(INV.XX_INITIALINVENTORYAMOUNT + INV.XX_PREVIOUSADJUSTMENTSAMOUNT +  " +
				"\nINV.XX_SHOPPINGAMOUNT - INV.XX_SALESAMOUNT + INV.XX_MOVEMENTAMOUNT + INV.XX_ADJUSTMENTSAMOUNT))/2,2)  " +
				"\nELSE 0 END) T3B, 0 T4A, 0 T4B, 0 T5 ";
			
		selectT5 = "\n, COBERTURAMENSUAL AS (SELECT " +
				"\n0 T1, 0 T2A, 0 T2B, 0 T3A, 0 T3B,  " +
				"\n(CASE WHEN SUM(INV.XX_SALESQUANTITY) <>0 THEN " +
				"\nROUND(SUM(INV.XX_INITIALINVENTORYQUANTITY)/SUM(INV.XX_SALESQUANTITY),2) ELSE 0 END) T4A,  " +
				"\n(CASE WHEN SUM(INV.XX_SALESAMOUNT) <>0 THEN " +
				"\nROUND(SUM(INV.XX_INITIALINVENTORYAMOUNT)/SUM(INV.XX_SALESAMOUNT),2) ELSE 0 END)  T4B, 0 T5  ";
			
		
		selectT6 = "\n, PIEZASMINIMAS AS (SELECT SUM(M.XX_VMR_MINIMUMPIECESQTY) PIEZASMIN ";
		fromT6 = "\nFROM XX_VMR_MINIMUMPIECES M " +
				"\nJOIN XX_VMR_DEPARTMENT DEP ON (DEP.XX_VMR_DEPARTMENT_ID = M.XX_VMR_DEPARTMENT_ID)";
		whereT6 ="\nWHERE M.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" ";
		groupT6 = "\nGROUP BY 1 ";
		
		selectT7 = "\n, INVENTARIO AS ( SELECT SUM(S.QTYONHAND) CANTINV ";
		fromT7 = "\n FROM M_STORAGE S " +
				"\nJOIN M_LOCATOR L ON (L.M_LOCATOR_ID = S.M_LOCATOR_ID ) " +
				"\nJOIN M_PRODUCT P ON (P.M_PRODUCT_ID = S.M_PRODUCT_ID) " +
				"\nJOIN XX_VMR_DEPARTMENT DEP ON (DEP.XX_VMR_DEPARTMENT_ID = P.XX_VMR_DEPARTMENT_ID) ";
		whereT7 = "\nWHERE S.AD_Client_ID = "+Env.getCtx().getAD_Client_ID()+" "+
				"\nAND P.XX_VMR_TYPEINVENTORY_ID = "+BASICO;
		groupT7 = "\nGROUP BY 1 ";
	
		selectT8 = "\n, PRODUCTOBASICOFALLA AS (SELECT 0 T1, 0 T2A, 0 T2B, 0 T3A, 0 T3B, 0 T4A, 0 T4B, " +
				"\n(CASE WHEN PM.PIEZASMIN > 0 THEN ROUND(I.CANTINV/PM.PIEZASMIN,2) ELSE 0 END) T5 ";
		fromT8 = "\nFROM INVENTARIO I, PIEZASMINIMAS PM ";
		whereT8 = "\nWHERE 1=1 ";
				
		union = "\n, UNION_ALL  AS (" +
				"\nSELECT * FROM TIEMPODEAPROBACION UNION " +
				"\nSELECT * FROM ROTACIONMENSUAL  UNION" +
				"\nSELECT * FROM ROTACIONACUMULADAMES UNION " +
				"\nSELECT * FROM COBERTURAMENSUAL UNION" +
				"\nSELECT * FROM PRODUCTOBASICOFALLA";

		String from = "\nFROM UNION_ALL UA ";
		String where = "\nWHERE 1=1 ";
		String groupBy = "\nGROUP BY 1 ";
		
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();

		//Si se selecciona linea, seccion o producto
		if(((Boolean)checkLine.getValue()==true || line != null && (line.getKey() == 99999999 || line.getKey()!= 0)) ||
				((Boolean)checkSection.getValue()==true || sect != null && (sect.getKey() == 99999999 || sect.getKey()!= 0)) ||
				((Boolean)checkProduct.getValue() == true || prod != null)){		
			fromT1 +="\nJOIN M_PRODUCT P ON (P.M_PRODUCT_ID = OL.M_PRODUCT_ID) ";
		}
		//Si se selecciona comprador o planificador
		if(((Boolean)checkComprador.getValue()==true || bcomp != null && (bcomp.getKey() == 99999999 || bcomp.getKey()!= 0)) ||
				((Boolean)checkPlanificador.getValue()==true || bplan != null && (bplan.getKey() == 99999999 || bplan.getKey()!= 0))){
			fromT2 +=   "\nINNER JOIN XX_VMR_DEPARTMENT DEP ON (O.XX_VMR_DEPARTMENT_ID = DEP.XX_VMR_DEPARTMENT_ID) ";
			fromT3 += "\nINNER JOIN XX_VMR_DEPARTMENT DEP ON (INV.XX_VMR_DEPARTMENT_ID = DEP.XX_VMR_DEPARTMENT_ID) ";		
		}
		//Almacén 
		if ((Boolean)checkTienda.getValue()==true || ware != null && (ware.getKey() == 99999999 || ware.getKey()!= 0)){	
			columnasAgregadas.add(colWare);
			selectT2 += "\n,O.M_WAREHOUSE_ID ";
			selectT3 +=	"\n,INV.M_WAREHOUSE_ID ";
			selectT6 +=	"\n, M.M_WAREHOUSE_ID ";
			selectT7 +=	"\n, L.M_WAREHOUSE_ID ";
			selectT8 +=	"\n, I.M_WAREHOUSE_ID ";
			from += "\n,M_WAREHOUSE W ";
			whereT8 += "\nAND I.M_WAREHOUSE_ID = PM.M_WAREHOUSE_ID  ";
			where += "\nAND W.M_WAREHOUSE_ID = UA.M_WAREHOUSE_ID  ";
			groupT2 +=  "\n,O.M_WAREHOUSE_ID ";
			groupT3 += "\n,INV.M_WAREHOUSE_ID ";
			groupT6 += "\n,M.M_WAREHOUSE_ID ";
			groupT7 +=	"\n, L.M_WAREHOUSE_ID ";
			groupBy += "\n,W.VALUE||'-'||W.NAME ";
			if (ware.getKey()!= 99999999 ) {
				whereT2 +=  "\nAND O.M_WAREHOUSE_ID = " +ware.getKey();
				whereT3 +=	"\nAND INV.M_WAREHOUSE_ID = " +ware.getKey();
				whereT6 +=	"\nAND M.M_WAREHOUSE_ID = " +ware.getKey();
				whereT7 +=	"\nAND L.M_WAREHOUSE_ID = " +ware.getKey();
				where += "\nAND UA.M_WAREHOUSE_ID = " +ware.getKey();
			}		
		}
		//Categoría
		if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
			columnasAgregadas.add(colCatg);
			selectT2 += "\n,O.XX_VMR_CATEGORY_ID ";
			selectT3 +=	"\n,INV.XX_VMR_CATEGORY_ID ";
			selectT6 += "\n,DEP.XX_VMR_CATEGORY_ID ";
			selectT7 += "\n,DEP.XX_VMR_CATEGORY_ID ";
			selectT8 += "\n,I.XX_VMR_CATEGORY_ID ";
			from += "\n,XX_VMR_CATEGORY C ";
			whereT8 += "\nAND I.XX_VMR_CATEGORY_ID = PM.XX_VMR_CATEGORY_ID  ";
			where += "\nAND C.XX_VMR_CATEGORY_ID = UA.XX_VMR_CATEGORY_ID  ";
			groupT2 +=	"\n,O.XX_VMR_CATEGORY_ID ";
			groupT3 += "\n,INV.XX_VMR_CATEGORY_ID ";
			groupT6 += "\n,DEP.XX_VMR_CATEGORY_ID ";
			groupT7 += "\n,DEP.XX_VMR_CATEGORY_ID ";
			groupBy += "\n,C.VALUE||'-'||C.NAME ";
			if (catg.getKey()!= 99999999 ) {
				whereT2 +=  "\nAND O.XX_VMR_CATEGORY_ID = " +catg.getKey();
				whereT3 +=	"\nAND INV.XX_VMR_CATEGORY_ID = " +catg.getKey();
				whereT6 +=  "\nAND DEP.XX_VMR_CATEGORY_ID = " +catg.getKey();
				whereT7 +=	"\nAND DEP.XX_VMR_CATEGORY_ID = " +catg.getKey();
				where += "\nAND UA.XX_VMR_CATEGORY_ID = "+ catg.getKey();
			}
		}
		//Departamento			
		if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {
			columnasAgregadas.add(colDept);
			selectT2 += "\n,O.XX_VMR_DEPARTMENT_ID ";
			selectT3 +=	"\n,INV.XX_VMR_DEPARTMENT_ID ";
			selectT6 +=	"\n,M.XX_VMR_DEPARTMENT_ID ";
			selectT7 +=	"\n,P.XX_VMR_DEPARTMENT_ID ";
			selectT8 +=	"\n,I.XX_VMR_DEPARTMENT_ID ";
			from += "\n,XX_VMR_DEPARTMENT D ";
			whereT8 += "\nAND I.XX_VMR_DEPARTMENT_ID = PM.XX_VMR_DEPARTMENT_ID  ";
			where += "\nAND D.XX_VMR_DEPARTMENT_ID = UA.XX_VMR_DEPARTMENT_ID  ";
			groupT2 +=	"\n,O.XX_VMR_DEPARTMENT_ID ";
			groupT3 += "\n,INV.XX_VMR_DEPARTMENT_ID ";
			groupT6 +=	"\n,M.XX_VMR_DEPARTMENT_ID ";
			groupT7 += "\n,P.XX_VMR_DEPARTMENT_ID ";
			groupBy += "\n,D.VALUE||'-'||D.NAME ";
			if (dept.getKey()!= 99999999 ) {
				whereT2 +=  "\nAND O.XX_VMR_DEPARTMENT_ID = " +dept.getKey();
				whereT3 +=	"\nAND INV.XX_VMR_DEPARTMENT_ID = " +dept.getKey();
				whereT6 +=  "\nAND M.XX_VMR_DEPARTMENT_ID = " +dept.getKey();
				whereT7 +=	"\nAND P.XX_VMR_DEPARTMENT_ID = " +dept.getKey();
				where += "\nAND UA.XX_VMR_DEPARTMENT_ID = " +dept.getKey();
			}
		}
		//Linea
		if((Boolean)checkLine.getValue()==true || line != null && (line.getKey() == 99999999 || line.getKey()!= 0)){
			columnasAgregadas.add(colLine);
			selectT1 +="\n,P.XX_VMR_LINE_ID ";
			selectT2 += "\n,OS.XX_VMR_LINE_ID ";
			selectT3 +=	"\n,INV.XX_VMR_LINE_ID ";
			selectT6 +=	"\n,M.XX_VMR_LINE_ID ";
			selectT7 +=	"\n,P.XX_VMR_LINE_ID ";
			selectT8 +=	"\n,I.XX_VMR_LINE_ID ";
			from += "\n,XX_VMR_LINE L ";
			whereT8 += "\nAND I.XX_VMR_LINE_ID = PM.XX_VMR_LINE_ID  ";
			where += "\nAND L.XX_VMR_LINE_ID = UA.XX_VMR_LINE_ID  ";
			groupT1 += "\n,P.XX_VMR_LINE_ID ";
			groupT2 += "\n,OS.XX_VMR_LINE_ID ";
			groupT3 += "\n,INV.XX_VMR_LINE_ID ";
			groupT6 += "\n,M.XX_VMR_LINE_ID ";
			groupT7 += "\n,P.XX_VMR_LINE_ID ";
			groupBy += "\n,L.VALUE||'-'||L.NAME ";
			if (line.getKey()!= 99999999){
				whereT1 +="\nAND P.XX_VMR_LINE_ID = "+line.getKey();
				whereT3 +=	"\nAND INV.XX_VMR_LINE_ID = "+line.getKey();
				whereT6 +="\nAND M.XX_VMR_LINE_ID = "+line.getKey();
				whereT7 +=	"\nAND P.XX_VMR_LINE_ID = "+line.getKey();
				where += "\nAND UA.XX_VMR_LINE_ID =" +line.getKey();
			}
		}
		//Seccion 
		if ((Boolean)checkSection.getValue()==true || sect != null && (sect.getKey() == 99999999 || sect.getKey()!= 0)){
			columnasAgregadas.add(colSec);
			selectT1 +="\n,P.XX_VMR_SECTION_ID ";
			selectT2 += "\n,OS.XX_VMR_SECTION_ID ";
			selectT3 +=	"\n,INV.XX_VMR_SECTION_ID ";
			selectT6 +=	"\n,M.XX_VMR_SECTION_ID ";
			selectT7 +=	"\n,P.XX_VMR_SECTION_ID ";
			selectT8 +=	"\n,I.XX_VMR_SECTION_ID ";
			from += "\n,XX_VMR_SECTION S ";
			whereT8 += "\nAND I.XX_VMR_SECTION_ID = PM.XX_VMR_SECTION_ID ";
			where += "\nAND S.XX_VMR_SECTION_ID = UA.XX_VMR_SECTION_ID ";
			groupT1 += "\n,P.XX_VMR_SECTION_ID ";
			groupT2 +=	"\n,OS.XX_VMR_SECTION_ID ";
			groupT3 += "\n,INV.XX_VMR_SECTION_ID ";
			groupT6 +=	"\n,M.XX_VMR_SECTION_ID ";
			groupT7 +=	"\n,P.XX_VMR_SECTION_ID ";
			groupBy += "\n,S.VALUE||'-'||S.NAME ";
			if (sect.getKey()!= 99999999 ) {
				whereT1 += "\nAND P.XX_VMR_SECTION_ID = "+sect.getKey();
				whereT3 +=	"\nAND INV.XX_VMR_SECTION_ID = "+sect.getKey();
				whereT6 += "\nAND M.XX_VMR_SECTION_ID = "+sect.getKey();
				whereT7 +=	"\nAND P.XX_VMR_SECTION_ID = "+sect.getKey();
				where += "\nAND UA.XX_VMR_SECTION_ID = " +sect.getKey();
			}
		}
		if((Boolean)checkProduct.getValue() == true || prod !=null){
			columnasAgregadas.add(colProd);
			selectT1 +="\n,P.M_PRODUCT_ID ";
			selectT2 += "\n,OS.M_PRODUCT_ID ";
			selectT3 +=	"\n,INV.M_PRODUCT_ID ";
			selectT7 +=	"\n,P.M_PRODUCT_ID ";
			selectT8 +=	"\n,I.M_PRODUCT_ID ";
			from += "\n,M_PRODUCT P ";
			where += "\nAND P.M_PRODUCT_ID = UA.M_PRODUCT_ID ";
			groupT1 += "\n,P.M_PRODUCT_ID ";
			groupT2 +=	"\n,OS.M_PRODUCT_ID ";
			groupT3 += "\n,INV.M_PRODUCT_ID ";
			groupT7 +=	"\n,P.M_PRODUCT_ID ";
			groupBy += "\n,P.VALUE||'-'||P.NAME ";
			if((Boolean)checkProduct.getValue() == false){
				whereT1 += "\nAND OL.M_PRODUCT_ID = "+prod;
				whereT3 +=	"\nAND INV.M_PRODUCT_ID = "+prod;
				whereT7 +=	"\nAND P.M_PRODUCT_ID = "+prod;
				where += "\nAND UA.M_PRODUCT_ID = " +prod;
			}	
		}
		//Proveedor
		if((Boolean)checkBPartner.getValue() == true || bpar != null && (bpar.getKey() == 99999999 || bpar.getKey() != 0)){
			columnasAgregadas.add(colBPar);
			selectT2 += "\n,O.C_BPARTNER_ID ";
			selectT3 +=	"\n,P.C_BPARTNER_ID ";
			selectT7 +=	"\n,P.C_BPARTNER_ID ";
			selectT8 +=	"\n,I.C_BPARTNER_ID ";
			fromT3 += "\nINNER JOIN M_PRODUCT P ON (INV.M_PRODUCT_ID = P.M_PRODUCT_ID) ";
			from += "\n,C_BPARTNER BP  ";
			where += "\nAND BP.C_BPARTNER_ID = UA.C_BPARTNER_ID  ";	
			groupT2 +=	"\n,O.C_BPARTNER_ID ";
			groupT3 += "\n,P.C_BPARTNER_ID ";
			groupT7 += "\n,P.C_BPARTNER_ID ";
			groupBy += "\n,BP.NAME ";	
			if (bpar.getKey()!= 99999999) {
				whereT2 +=  "\nAND O.C_BPARTNER_ID = " +bpar.getKey();
				whereT3 +=	"\nAND P.C_BPARTNER_ID = " +bpar.getKey();
				whereT7 +=	"\nAND P.C_BPARTNER_ID = " +bpar.getKey();
				where += "\nAND UA.C_BPARTNER_ID = " +bpar.getKey();
			}
		}
		//Comprador
		if ((Boolean)checkComprador.getValue()==true || bcomp != null && (bcomp.getKey() == 99999999 || bcomp.getKey()!= 0)){					
			columnasAgregadas.add(colBuy);
			selectT2 += "\n,DEP.XX_USERBUYER_ID ";
			selectT3 +=	"\n,DEP.XX_USERBUYER_ID ";
			selectT6 +=	"\n,DEP.XX_USERBUYER_ID ";
			selectT7 +=	"\n,DEP.XX_USERBUYER_ID ";
			selectT8 +=	"\n,I.XX_USERBUYER_ID ";
			from += "\n,AD_USER U1 ";
			whereT8 += "\nAND I.XX_USERBUYER_ID = PM.XX_USERBUYER_ID ";
			where += "\nAND U1.C_BPARTNER_ID = UA.XX_USERBUYER_ID ";
			groupT2 +=	"\n,DEP.XX_USERBUYER_ID ";
			groupT3 += "\n,DEP.XX_USERBUYER_ID ";
			groupT6 +=	"\n,DEP.XX_USERBUYER_ID ";
			groupT7 += "\n,DEP.XX_USERBUYER_ID ";
			groupBy += "\n,U1.DESCRIPTION ";
			if (bcomp.getKey()!= 99999999 ){
				whereT2 +=  "\nAND DEP.XX_USERBUYER_ID = " +bcomp.getKey();
				whereT3 +=	"\nAND DEP.XX_USERBUYER_ID = " +bcomp.getKey();
				whereT6 +=  "\nAND DEP.XX_USERBUYER_ID = " +bcomp.getKey();
				whereT7 +=	"\nAND DEP.XX_USERBUYER_ID = " +bcomp.getKey();
				where += "\nAND UA.XX_USERBUYER_ID =" +bcomp.getKey();
			}
		}	
		//Planificador
		if ((Boolean)checkPlanificador.getValue()==true || bplan != null && (bplan.getKey() == 99999999 || bplan.getKey()!= 0)){					
			columnasAgregadas.add(colPlan);
			selectT2 += "\n,DEP.XX_INVENTORYSCHEDULE_ID ";
			selectT3 +=	"\n,DEP.XX_INVENTORYSCHEDULE_ID ";
			selectT6 +=	"\n,DEP.XX_INVENTORYSCHEDULE_ID ";
			selectT7 +=	"\n,DEP.XX_INVENTORYSCHEDULE_ID ";
			selectT8 +=	"\n,I.XX_INVENTORYSCHEDULE_ID ";
			from += "\n,AD_USER U2 ";
			whereT8 += "\nAND I.XX_INVENTORYSCHEDULE_ID = PM.XX_INVENTORYSCHEDULE_ID  ";
			where += "\nAND U2.C_BPARTNER_ID = UA.XX_INVENTORYSCHEDULE_ID  ";
			groupT2 += "\n,DEP.XX_INVENTORYSCHEDULE_ID ";
			groupT3 += "\n,DEP.XX_INVENTORYSCHEDULE_ID ";
			groupT6 += "\n,DEP.XX_INVENTORYSCHEDULE_ID ";
			groupT7 += "\n,DEP.XX_INVENTORYSCHEDULE_ID ";
			groupBy += "\n,U2.DESCRIPTION ";
			if (bplan.getKey()!= 99999999 ){
				whereT2 +=  "\nAND DEP.XX_INVENTORYSCHEDULE_ID = " +bplan.getKey();
				whereT3 +=	"\nAND DEP.XX_INVENTORYSCHEDULE_ID = " +bplan.getKey();
				whereT6 +=  "\nAND DEP.XX_INVENTORYSCHEDULE_ID = " +bplan.getKey();
				whereT7 +=	"\nAND DEP.XX_INVENTORYSCHEDULE_ID = " +bplan.getKey();
				where += "\nAND UA.XX_INVENTORYSCHEDULE_ID= " +bplan.getKey();
			}
		}	

		with += selectT1 + fromT1 + whereT1 + groupT1 +	"\n)";
		with += selectT2 + fromT2 + whereT2 + groupT2 +	"\n)";
		with += selectT3a + selectT3 + fromT3 + whereT3 + groupT3 +	"\n)";
		with += selectT4 + selectT3 + fromT3 + whereT3 + groupT3 +	"\n)";
		with += selectT5 + selectT3 + fromT3 + whereT3 + groupT3 +	"\n)";
		with += selectT6 + fromT6 + whereT6 + groupT6 +	"\n)";
		with += selectT7 + fromT7 + whereT7 + groupT7 +	"\n)";
		with += selectT8 + fromT8 + whereT8 + "\n)";
		with += union + "\n)";
		
		// Las cantidades 		
		columnasAgregadas.add(colTA);
		columnasAgregadas.add(colRMP);
		columnasAgregadas.add(colRMB);
		columnasAgregadas.add(colRAMP);		
		columnasAgregadas.add(colRAMB);
		columnasAgregadas.add(colCMP);
		columnasAgregadas.add(colCMB);
		columnasAgregadas.add(colPBF);
	
		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);
		
		String select = "\n" + miniTable.prepareTable(layout, null, null, false, null);
	
		String sql =  with + select + from + where + groupBy;
		//System.out.println(sql);
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



}
