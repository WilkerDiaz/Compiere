package compiere.model.cds.forms;
/**
 * 
 * 
 * 
 * */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.swing.CLabel;
import org.compiere.swing.CTextField;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.forms.indicator.XX_Indicator;
/**
 * Clase que permite tener una herramienta que permita analizar los productos 
 * que son susceptibles al proceso de surtir el área de venta
 * 
 * @author Diana Rozo
 * */
public class XX_AreaSalesAssortmentForm extends XX_Indicator {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4336072597251452500L;

	private final int store = Env.getCtx().getContextAsInt("#M_Warehouse_ID");
	private int num = 0;
	
//	private final static int PRODUCT = 0,TOTALTIENDA=1,TOTALTRANSITO=2,VENTAANTERIOR=3,
//	VENTAANTERIORBSF=4,VENTAMES=5,VENTAMESBSF=6,COBERTURA=7,SELLTHROUGH=8,
//	CATEGORY = 9, DEPARTMENT = 10,LINE=11, SECTION=12, TYPEINVENTORY=13,
//	BRAND=14, PACKAGE = 15,COLLECTION = 16,SEASON=17  ;
	
	//Ítems por consulta
	private CLabel labelItem= new CLabel(Msg.translate(ctx,"XX_itemConsult"));	
	private  VNumber cantItem = new VNumber();
	
	//Creiterio de Ordenar
	private CLabel labelOrderedBy= new CLabel(Msg.translate(ctx,"XX_OrderBy"));	
	private VComboBox comboOrderedby= new VComboBox();
	
	//Product Type 
	private CLabel labelTypeInventory= new CLabel(Msg.translate(ctx,"XX_typeInventory_2"));	
	private VComboBox comboTypeInventory = new VComboBox();
	private VCheckBox checkTypeInventory = new VCheckBox();
	
	//Colección
	private VCheckBox checkCollection = new VCheckBox();
	private CLabel labelCollection = new CLabel(Msg.translate(ctx, "XX_VMR_Collection_ID"));
	private VComboBox comboCollection = new VComboBox();
	
	//Paquete
	private VCheckBox checkPackage = new VCheckBox();
	private CLabel labelPackage = new CLabel(Msg.translate(ctx, "XX_VMR_Package_ID"));
	private VComboBox comboPackage = new VComboBox();
	
	//Temporada
	private VCheckBox checkSeason = new VCheckBox();
	private CLabel labelSeason = new CLabel(Msg.translate(Env.getCtx(), "XX_VMR_Season_ID"));
	private VComboBox comboSeason = new VComboBox();
	
	//Fecha
	private CLabel dateLabel = new CLabel(Msg.translate(Env.getCtx(), "Date"));
	private VDate vDateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	
	//Tienda
	private CLabel labelTienda = new CLabel(Msg.translate(ctx, "XX_Store_2"));	
	private CTextField textStore = new CTextField(); 
	
	//Marca
	private VCheckBox checkMark = new VCheckBox();
	private CLabel labelMark = new CLabel(Msg.translate(Env.getCtx(), "XX_Brand"));
	private VComboBox comboMark = new VComboBox();
	
	private CLabel labelBlanco = new CLabel("");
	private int valorInicial=10;
	Date fechaActual = new Date();
	
	Date FechaAyer = new Date( fechaActual.getTime()-86400000);
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat dateFormatBD = new SimpleDateFormat("yyyyMMdd");
	

	
	
	@Override
	protected void agregarParametros() {
		
		dataPane.setPreferredSize(new Dimension(1100, 450));
		
		//Agregar Tipo de producto			
		agregarParametro(labelTypeInventory);
		agregarParametro(comboTypeInventory);
		agregarParametro(checkTypeInventory);
		
		//Agregar Coleccion					
		agregarParametro(labelCollection);
		agregarParametro(comboCollection);
		agregarParametro(checkCollection);
		
		//Agregar Paquete			
		agregarParametro(labelPackage);
		agregarParametro(comboPackage);
		agregarParametro(checkPackage);
		
		//Agregar marca		
		agregarParametro(labelMark);
		agregarParametro(comboMark);
		agregarParametro(checkMark);
		
		//Agregar Temporada			
		agregarParametro(labelSeason);
		agregarParametro(comboSeason);
		agregarParametro(checkSeason);
		
		//agregar tienda
		agregarParametro(labelTienda);
		cargarTienda();
		textStore.setEditable(false);
		agregarParametro(textStore);
		agregarParametro(labelBlanco);
				
		//Item por consulta
		cantItem.setPreferredSize(new Dimension(20,20));		
		cantItem.setDisplayType(DisplayTypeConstants.Integer);
		cantItem.setValue(valorInicial);
		agregarParametro(labelItem);
		agregarParametro(cantItem);
		agregarParametro(labelBlanco);
		
		//Agregar fecha
		agregarParametro(dateLabel);
		agregarParametro(vDateFrom);
		vDateFrom.setValue(FechaAyer);
		
		agregarParametro(labelBlanco);
		
		//Agregar criterio de ordenamiento
		agregarParametro(labelOrderedBy);
		agregarParametro(comboOrderedby);
		
	}
	@Override
	protected void personalizar() {
		//carga de comboBox
		uploadSeasons();
		uploadCollection();
		uploadPackages();
		cargarMarca();
		cargaTipoOrden();
		cargarCategorías();

		cargaTipoInventario();
		
		//Configurar campos de tipo de producto	
		configurar(comboDepartment, false);
		configurar(checkDepartment, false, false);
		
		//Configurar campos de tipo de producto	
		configurar(comboTypeInventory, true);
		configurar(checkTypeInventory, true, false);
		
		//Configurar campos de coleccion	
		configurar(comboCollection, true);
		configurar(checkCollection, true, false);		
	
		//Configurar campos de Paquete	
		configurar(comboPackage, true);
		configurar(checkPackage, true, false);
		
		//Configurar campos de Temporada	
		configurar(comboSeason, true);
		configurar(checkSeason, true, false);
		
		//Configurar campos de marca	
		configurar(comboMark, true);
		configurar(checkMark, true, false);
		
		//Criterio de ordenamiento obligatorio
		configurar(comboOrderedby, true);
		comboOrderedby.setBackground(true);
		
		//Comenzando
		configurar(bSearch, false);
		configurar(bPrint, false);
		configurar(bFile, false);	
		
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "ProductQty"));
		
		//Agregar listeners a los nuevos campos
		checkTypeInventory.addActionListener(this);
		checkPackage.addActionListener(this);
		checkCollection.addActionListener(this);
		//checkTienda.addActionListener(this);
		checkSeason.addActionListener(this);
		checkMark.addActionListener(this);
		comboOrderedby.addActionListener(this);
		
	}
	@Override
	protected void ocultarParametrosDefecto() {
		ocultarParametro(PERIODO);
		ocultarParametro(SOCIODENEGOCIO);
		ocultarParametro(PRODUCTO);
	}
	
	protected void limpiarFiltro () {
		
		//Limpia los campos del filtro padre
		super.limpiarFiltro();		
		
		//Borrar aquellos campos que se agregaron en esta forma
		
		
		configurar(comboOrderedby, true);
		configurar(comboTypeInventory, true);
		configurar(checkTypeInventory, true, false);
		configurar(comboDepartment, false);
		configurar(checkDepartment, false, false);		
		configurar(comboCollection, true);
		configurar(checkCollection, true, false);	
		configurar(comboPackage, true);
		configurar(checkPackage, true, false);		
		configurar(comboSeason, true);
		configurar(checkSeason, true, false);
		configurar(comboMark, true);
		configurar(checkMark, true, false);
		
	}
	
	/** En este método agrego acciones a las acciones predeterminadas */
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == bSearch) {	
			if (cantItem.getValue()==null){				
				num=0;			
			} else {
				num =(Integer)cantItem.getValue();
				if (num <= 100){				
					try {
						llenarTabla();	
						statusBar.setStatusDB(miniTable.getRowCount());
					} catch (NullPointerException n) {
					}	
				} else  {
					ADialog.error(m_WindowNo, m_frame, "Puede indicar un máximo de 100 items por consulta");
				}
				
			}
		} else {		
			//Ejecutará las acciones por defecto
			super.actionPerformed(e);
		}
		
		//Desactivar los escuchadores mientras realizo las modifciaciones
		desactivarActionListeners();

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
		
		}	
		//Verificar si se selecciono el check de tipo de producto
		if (e.getSource() == checkTypeInventory) {			
			if ((Boolean)checkTypeInventory.getValue()) {				
				configurar(comboTypeInventory, false, 99999999 );				
			} else {
				configurar(comboTypeInventory, true);
			}
		}
		//Verificar si se selecciono el check de tipo de producto
		if (e.getSource() == checkCollection) {			
			if ((Boolean)checkCollection.getValue()) {				
				configurar(comboCollection, false, 99999999 );				
			} else {
				configurar(comboCollection, true);
			}
		}
		//Verificar si se selecciono el check de tipo de producto
		if (e.getSource() == checkPackage) {			
			if ((Boolean)checkPackage.getValue()) {				
				configurar(comboPackage, false, 99999999 );				
			} else {
				configurar(comboPackage, true);
			}
		}
		//Verificar si se selecciono el check de tipo de producto
		if (e.getSource() == checkSeason) {			
			if ((Boolean)checkSeason.getValue()) {				
				configurar(comboSeason, false, 99999999 );				
			} else {
				configurar(comboSeason, true);
			}
		}
		
		//Verificar si se selecciono el check de Marca
		if (e.getSource() == checkMark) {			
			if ((Boolean)checkMark.getValue()) {				
				configurar(comboMark, false, 99999999 );				
			} else {
				configurar(comboMark, true);
			}
		}
		
		
		//verificar si escogio el tipo de criterio para ordenar
		if (e.getSource()==comboOrderedby){
			
			KeyNamePair ord = (KeyNamePair)comboOrderedby.getSelectedItem();			
			boolean procesar = true;
			
			if ((ord!=null) && (ord.getKey()!=0))  {				
				
				comboOrderedby.setBackground(false);				
			
			}else
				{				
				comboOrderedby.setBackground(true);
				procesar = false;
				}				
			//Verificar que los campos se ordenar se hayan marcado
			configurar(bSearch, procesar);			
			}
		
		
		//Verificar si se modificó la fecha
		if (e.getSource() == vDateFrom) {			
			if (vDateFrom.getValue() == null) {				
				vDateFrom.setValue(FechaAyer);				
			} 
//				else if (((Date)vDateFrom.getValue()).before(FechaAyer)) {
//				ADialog.error(m_WindowNo, m_frame, "Sólo puede consultar las ventas del día actual y del día anterior");
//			}
		}
		
		
		 
		//Activar los escuchadores
		activarActionListeners();
 	}
	@Override
	protected void llenarTabla() {
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		//Si no se ha cargado el header previamente, se liberan las filas de la tabla, 
		//se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);
		
		//Calcular el query
		try {
			calcularQuery();		
			
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
		
		m_frame.setCursor(Cursor.getDefaultCursor());

	}
	
	/** Se determina el query de acuerdo a los parámetros ingresados*/
	public void calcularQuery () {
		
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair sect = (KeyNamePair)comboSection.getSelectedItem();		
		KeyNamePair mark = (KeyNamePair)comboMark.getSelectedItem();
		KeyNamePair typeInv = (KeyNamePair)comboTypeInventory.getSelectedItem();
		KeyNamePair season = (KeyNamePair)comboSeason.getSelectedItem();
		KeyNamePair colec = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
		
		/**Busco el orden que desean filtrar*/
		KeyNamePair order = (KeyNamePair)comboOrderedby.getSelectedItem();
		String orderby="";
		switch (order.getKey()){
			case 1:orderby = " order by NVL(ci.Sell_Through,0)";break;
			case 2:orderby = " order by NVL(venta_anterior,0) desc";break;
			case 3:orderby = " order by NVL(DiaAnterior_BFS,0) desc";break;
		}
		
		// Manejo de fechas
		String fechaBD= dateFormatBD.format(vDateFrom.getValue());
		String FechaAnterior = dateFormatBD.format(((Date)vDateFrom.getValue()).getTime()-86400000);
//		String annio = fechaBD.charAt(0)+""+fechaBD.charAt(1)+""+fechaBD.charAt(2)+""+fechaBD.charAt(3);
//		String mes = fechaBD.charAt(4)+""+fechaBD.charAt(5);
		
		String query ="";

		// Especificamos de qué tablas se hará el join. Si es FechaAyer se usa XX_VMR_SALESASSORTMENT
		String tempWhere = "";
		String tablaJoin = "i", tablaSeason = "i", tablaCol = "i", tablaPack = "i";
		if (!fechaBD.equals(dateFormatBD.format(FechaAyer)) ) {
			//tablaJoin ahora es p
			tablaJoin = "p"; tablaSeason = "season"; tablaCol = "col"; tablaPack = "pack";
		}
		
		/* CONDICIONES DEL FILTRO */
		//Categoría
		if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {
			if (catg.getKey()!= 99999999 )			
				tempWhere += "\n    			and "+tablaJoin+".XX_VMR_Category_ID = " + catg.getKey();
		}	
		
		//Departamento			
		if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {			
			if (dept.getKey()!= 99999999 ) 
				tempWhere += "\n             and "+tablaJoin+".XX_VMR_DEPARTMENT_ID = " + dept.getKey();
		}
		
		//Linea			
		if((Boolean)checkLine.getValue()==true || line != null && (line.getKey()==99999999 ||  line.getKey()!= 0)) {			
			if (line.getKey()!= 99999999 ) 
				tempWhere += "\n       		AND "+tablaJoin+".XX_VMR_LINE_ID = "+ line.getKey();
		}

		//seccion			
		if((Boolean)checkSection.getValue()==true || sect != null && (sect.getKey()==99999999 ||  sect.getKey()!= 0)) {			
			if (sect.getKey()!= 99999999 ) 
				tempWhere += "\n       		AND "+tablaJoin+".XX_VMR_SECTION_ID = " + sect.getKey();
		}
		
		//Marca			
		if((Boolean)checkMark.getValue()==true || mark != null && (mark.getKey()==99999999 ||  mark.getKey()!= 0)) {			
			if (mark.getKey()!= 99999999 ) 
				tempWhere += "\n       		AND "+tablaJoin+".XX_VMR_BRAND_ID = " + mark.getKey();
		}
		
		//Tipo de Inventario			
		if((Boolean)checkTypeInventory.getValue()==true || typeInv != null && (typeInv.getKey()==99999999 ||  typeInv.getKey()!= 0)) {			
			if (typeInv.getKey()!= 99999999 ) 
				tempWhere += "\n       		AND "+tablaJoin+".XX_VMR_TYPEINVENTORY_ID = " + typeInv.getKey();
		}
		
		//Temporada			
		if((Boolean)checkSeason.getValue()==true || season != null && (season.getKey()==99999999 ||  season.getKey()!= 0)) {			
			if (season.getKey()!= 99999999 ) 
				tempWhere += "\n       		AND "+tablaSeason+".XX_VMA_SEASON_ID = " + season.getKey();
		}
		
		//coleccion		
		if((Boolean)checkCollection.getValue()==true || colec != null && (colec.getKey()==99999999 ||  colec.getKey()!= 0)) {			
			if (colec.getKey()!= 99999999 ) 
				tempWhere += "\n       		AND "+tablaCol+".XX_VMR_COLLECTION_ID = " + colec.getKey();
		}
		
		//Paquete
		if((Boolean)checkPackage.getValue()==true || pack != null && (pack.getKey()==99999999 ||  pack.getKey()!= 0)) {			
			if (pack.getKey()!= 99999999 ) 
				tempWhere += "\n       		AND "+tablaPack+".XX_VMR_PACKAGE_ID = " + pack.getKey();
		}	
		/* FIN CONDICIONES DEL FILTRO */
		
		String sqlTemp = "";
		// Si se consulta el día anterior
		System.out.println(fechaBD +" "+ dateFormatBD.format(FechaAyer));
			if (fechaBD.equals(dateFormatBD.format(FechaAyer)) ) {
			// tablaJoin queda con i
			sqlTemp = " with " +
			"\n temp as ( "+
			"\n       select * from ( " +
			"\n       	 select i.m_product_id, (i.value||'-'||i.NAME) PRODUCT, " +
			"\n       		sum(XX_VMR_DaySalesPieces) as venta_anterior," +
			"\n       		sum(XX_VMR_DaySalesAmount) as DiaAnterior_BFS," +
			"\n       		sum(XX_VMR_MonthSalesPieces) as venta_mensual," +
			"\n       		sum(XX_VMR_MonthSalesAmount) as monto_mensual," +
			"\n       		i.XX_VMR_Category_ID, i.XX_VMR_DEPARTMENT_ID, i.XX_VMR_LINE_ID, i.XX_VMR_SECTION_ID, " +
			"\n       		i.XX_VMR_BRAND_ID, i.XX_VMR_TYPEINVENTORY_ID," +
			"\n       		XX_VMR_Collection_ID, XX_VMR_Package_ID, XX_VMA_Season_ID " +
			"\n          from XX_VMR_SalesAssortment i" +
			"\n          where " +
			"\n       		TO_CHAR(XX_VMR_SalesDate,'YYYYMMDD') = "+ fechaBD +
			"\n       		and i.M_WAREHOUSE_ID="+ store + 
			// FILTRO
			        	 tempWhere +
			"\n          group by (i.m_product_id, (i.value||'-'||i.NAME), i.XX_VMR_Category_ID, " +
								" i.XX_VMR_DEPARTMENT_ID, i.XX_VMR_LINE_ID, i.XX_VMR_SECTION_ID, i.XX_VMR_BRAND_ID," +
								" i.XX_VMR_TYPEINVENTORY_ID," +
								" XX_VMR_Collection_ID, XX_VMR_Package_ID, XX_VMA_Season_ID) " +
			"\n          " + orderby + //order by NVL(venta_anterior,0) desc 
			"\n          ) where rownum <=" + num +
			"\n       ) " ;
			
			} else { //Día actual
			sqlTemp = " with " +
			"\n temp as ( "+
			"\n       select * from ( " +
			"\n       	 select ol.m_product_id, (p.value||'-'||p.NAME) PRODUCT, " +
			"\n       		sum(ol.QTYORDERED) as venta_anterior," +
			"\n       		sum((ol.PRICEACTUAL + ol.XX_EMPLOYEEDISCOUNT)*ol.QTYORDERED)as DiaAnterior_BFS," +
			"\n       		sum(XX_VMR_MonthSalesPieces) as venta_mensual," +
			"\n       		sum(XX_VMR_MonthSalesAmount) as monto_mensual," +
			"\n       		p.XX_VMR_Category_ID, p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID, " +
			"\n       		p.XX_VMR_BRAND_ID, ol.M_ATTRIBUTESETINSTANCE_ID, p.XX_VMR_TYPEINVENTORY_ID," +
			"\n       		pack.XX_VMR_PACKAGE_ID, col.XX_VMR_COLLECTION_ID, season.XX_VMA_SEASON_ID" +
			"\n       	from c_order ord  join  c_orderline ol ON (ord.C_ORDER_ID = ol.C_ORDER_ID) " +
			"\n       		join m_product p  on (p.M_PRODUCT_ID = ol.M_PRODUCT_ID)" +
			"\n       		left join M_ATTRIBUTESETINSTANCE atri on (atri.M_ATTRIBUTESETINSTANCE_ID = ol.M_ATTRIBUTESETINSTANCE_ID)" +
			"\n       		left outer join XX_VMR_PACKAGE pack on (pack.XX_VMR_PACKAGE_ID=atri.XX_VMR_PACKAGE_ID)" +
			"\n       		left outer join XX_VMR_COLLECTION col on (col.XX_VMR_COLLECTION_ID=pack.XX_VMR_COLLECTION_ID)" +
			"\n       		left outer join XX_VMA_SEASON season on (season.XX_VMA_SEASON_ID=col.XX_VMA_SEASON_ID)" +
			"\n       		left outer join XX_VMR_SalesAssortment i on (ol.m_product_id = i.m_product_id " +
			"\n       			 and i.M_WAREHOUSE_ID = ord.M_WAREHOUSE_ID" +
			"\n       		     and ol.M_ATTRIBUTESETINSTANCE_ID = i.M_ATTRIBUTESETINSTANCE_ID" +
			"\n       		     and p.XX_VMR_Category_ID = I.XX_VMR_Category_ID" +
			"\n       		     and p.XX_VMR_DEPARTMENT_ID = I.XX_VMR_DEPARTMENT_ID" +
			"\n       		     and p.XX_VMR_LINE_ID = I.XX_VMR_LINE_ID" +
			"\n       		     and p.XX_VMR_SECTION_ID = I.XX_VMR_SECTION_ID" +
			"\n       		     and TO_CHAR(XX_VMR_SalesDate,'YYYYMMDD') = "+ FechaAnterior + " )-- dia anterior " +
			"\n       	where ord.issotrx='Y' and TO_CHAR(ord.dateordered,'YYYYMMDD') = "+ fechaBD +" --dia actual" +
			"\n       		and p.ISACTIVE='Y' " +
			"\n       		and ol.M_WAREHOUSE_ID="+ store+ 
			// FILTRO
			tempWhere +
			"\n     	group by (ol.m_product_id, (p.value||'-'||p.NAME), p.XX_VMR_Category_ID,  p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_LINE_ID," +
			"\n             p.XX_VMR_SECTION_ID, p.XX_VMR_BRAND_ID, ol.M_ATTRIBUTESETINSTANCE_ID, p.XX_VMR_TYPEINVENTORY_ID, " +
			"\n             pack.XX_VMR_PACKAGE_ID, col.XX_VMR_COLLECTION_ID, season.XX_VMA_SEASON_ID) " +
			"\n         order by NVL(venta_anterior,0) desc" +
			"\n          ) where rownum <=" + num +
			"\n       ) " ;
			
			}	
			String sqlSelect =	"\n select" +
				"\n       t.PRODUCT, " +
				"\n       coalesce(sum( CASE WHEN (loc.ISDEFAULT='N' and loc.M_WAREHOUSE_ID="+ store+") then m.QTY else 0 end), 0) as TOTALTRANSITO,"+
				"\n       coalesce(sum( CASE WHEN (loc.ISDEFAULT='Y' and loc.M_WAREHOUSE_ID="+ store+") then m.QTY else 0 end), 0) as TOTALTIENDA, " +
				"\n       coalesce(sum(CASE WHEN (m.M_LOCATOR_ID = " + Env.getCtx().getContextAsInt("#XX_L_LOCATORCDCHEQUEADO_ID")+") THEN QTY ELSE 0 END), 0) TOTALCD , " +
				"\n       coalesce(t.venta_anterior,0) VENTAANTERIOR," +
				"\n       coalesce(t.DiaAnterior_BFS,0) VENTAANTERIORBSF, " + 
				"\n       coalesce(t.venta_mensual,0) VENTAMES, " + //Si es dia actual sumarle los totales del dia
				"\n       coalesce(t.monto_mensual,0) VENTAMESBSF," +
				"\n       cat.value||'-'||cat.name CATEGORY," +
				"\n       dep.value||'-'||dep.name DEPARTMENT, " +
				"\n       l.value||'-'||l.name LINE, " +
				"\n       sec.value||'-'||sec.name SECTION," +
				"\n       br.name BRAND," +
				"\n       typeInv.name TYPEINV," +
				"\n       pack.Name PACKAGE," +
				"\n       col.Name COLLECTION," +
				"\n       season.name SEASON" +
				"\n from " +
				"\n       temp t " +
				"\n       left outer join M_STORAGEDETAIL m on (m.m_product_id = t.m_product_id and m.qtytype = 'H')" +
				"\n       left join M_LOCATOR loc on (m.M_LOCATOR_ID = loc.M_LOCATOR_ID),      " +
				"\n       XX_VMR_CATEGORY cat," +
				"\n       XX_VMR_DEPARTMENT dep," +
				"\n       XX_VMR_LINE l," +
				"\n       XX_VMR_SECTION sec," +
				"\n       XX_VMR_TYPEINVENTORY typeInv," +
				"\n       XX_VMR_BRAND br, XX_VMR_PACKAGE pack," +
				"\n       XX_VMR_COLLECTION col," +
				"\n       XX_VMA_SEASON season  " +
				"\n where  " +
				"\n       cat.XX_VMR_CATEGORY_ID = t.XX_VMR_CATEGORY_ID" +
				"\n       and dep.XX_VMR_DEPARTMENT_ID = t.XX_VMR_DEPARTMENT_ID" +
				"\n       and l.XX_VMR_LINE_ID=t.XX_VMR_LINE_ID" +
				"\n       and sec.XX_VMR_SECTION_ID=t.XX_VMR_SECTION_ID" +
				"\n       and br.XX_VMR_BRAND_ID=t.XX_VMR_BRAND_ID" +
				"\n       and typeInv.XX_VMR_TYPEINVENTORY_ID=t.XX_VMR_TYPEINVENTORY_ID" +
				"\n       and pack.XX_VMR_PACKAGE_ID=t.XX_VMR_Package_ID" +
				"\n       and col.XX_VMR_COLLECTION_ID=t.XX_VMR_COLLECTION_ID" +
				"\n       and season.XX_VMA_SEASON_ID=t.XX_VMA_SEASON_ID" +
				"\n group by t.PRODUCT," +
				"\n       cat.value||'-'||cat.name," +
				"\n       dep.value||'-'||dep.name," +
				"\n       l.value||'-'||l.name," +
				"\n       sec.value||'-'||sec.name," +
				"\n       typeInv.name," +
				"\n       br.name," +
				"\n       t.venta_anterior, " +
				"\n       t.DiaAnterior_BFS," +
				"\n       t.venta_mensual," +
				"\n       t.monto_mensual," +
				"\n       pack.Name," +
				"\n       col.Name," +
				"\n       season.name" +
				orderby; //order by NVL(t.venta_anterior,0) desc
		
		
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();

		//Agrego las columnas 
		columnasAgregadas.add(colProd);
		columnasAgregadas.add(colTotalTienda);
		columnasAgregadas.add(colTotalTransito);
		columnasAgregadas.add(colTotalCD);
		columnasAgregadas.add(colVentaAnterior);
		columnasAgregadas.add(colVentaAnteriorBSF);
		columnasAgregadas.add(colVentaMes);
		columnasAgregadas.add(colVentaMesBSF);
		//columnasAgregadas.add(colCobertura);
		//columnasAgregadas.add(colSellThrough);
		
		columnasAgregadas.add(colCategory);
		columnasAgregadas.add(colDept);
		columnasAgregadas.add(colLine);
		columnasAgregadas.add(colSection);
		columnasAgregadas.add(colTypeInv);		
		columnasAgregadas.add(colBrand);
		columnasAgregadas.add(colPack);
		columnasAgregadas.add(colColle);
		columnasAgregadas.add(colSeason);

		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);
		
		//miniTable.prepareTable(layout, null, null, false, null);
		miniTable.prepareTable(layout, "", "", true, "");
		//query = sql + groupby +orderby;
		query = sqlTemp + sqlSelect;
		System.out.println("QUERY "+ query);

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(query, null);			
			rs = ps.executeQuery();

			System.out.println("FIN EXEC QUERY ");

			int rows = 0;
			while (rs.next()) {
				miniTable.setRowCount(rows + 1);
				int i = 0;
				miniTable.setValueAt(rs.getString("PRODUCT"), rows, i++);
				miniTable.setValueAt(rs.getInt("TOTALTRANSITO"), rows, i++);
				miniTable.setValueAt(rs.getInt("TOTALTIENDA"), rows, i++);	
				miniTable.setValueAt(rs.getInt("TOTALCD"), rows, i++);						
				miniTable.setValueAt(rs.getInt("VENTAANTERIOR"), rows, i++);
				miniTable.setValueAt(rs.getDouble("VENTAANTERIORBSF"), rows, i++);
				miniTable.setValueAt(rs.getInt("VENTAMES"), rows, i++);
				miniTable.setValueAt(rs.getDouble("VENTAMESBSF"), rows, i++);
				//miniTable.setValueAt(rs.getDouble("COBERTURA"), rows, i++);
				//miniTable.setValueAt(rs.getDouble("SELLTHROUGH"), rows, i++);
				miniTable.setValueAt(rs.getString("CATEGORY"), rows, i++);
				miniTable.setValueAt(rs.getString("DEPARTMENT"), rows, i++);
				miniTable.setValueAt(rs.getString("LINE"), rows, i++);
				miniTable.setValueAt(rs.getString("SECTION"), rows, i++);
				miniTable.setValueAt(rs.getString("TYPEINV"), rows, i++);
				miniTable.setValueAt(rs.getString("BRAND"), rows, i++);
				miniTable.setValueAt(rs.getString("PACKAGE"), rows, i++);
				miniTable.setValueAt(rs.getString("COLLECTION"), rows, i++);
				miniTable.setValueAt(rs.getString("SEASON"), rows, i++);
				miniTable.repaint();
				rows++;
				miniTable.getValueAt(rows-1, 4).getClass();
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.SEVERE, query, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, query, e);			
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
	
	
	
	//Cabeceras de las columnas de la tabla 
	private ColumnInfo colProd = new ColumnInfo(Msg.translate(ctx, "Product"), 
			".",String.class);
	private ColumnInfo colTotalTienda = new ColumnInfo("Inventario en Tránsito", 
			".",Integer.class);
	private ColumnInfo colTotalTransito = new ColumnInfo("Inventario en Tienda", 
			".",Integer.class);
	private ColumnInfo colTotalCD = new ColumnInfo("Inventario en CD", 
					".",Integer.class);
	private ColumnInfo colVentaAnterior = new ColumnInfo("Venta Piezas", 
			".",Double.class);
	private ColumnInfo colVentaAnteriorBSF = new ColumnInfo("Venta Bs.", 
			".",Double.class);
	private ColumnInfo colVentaMes = new ColumnInfo("Venta Acum. Piezas", 
			".",Double.class);
	private ColumnInfo colVentaMesBSF = new ColumnInfo("Venta Acum. Bs", 
			".",Double.class);
	
	private ColumnInfo colCobertura = new ColumnInfo(Msg.translate(ctx, "XX_Coverage"), 
			".",Double.class);

	private ColumnInfo colSellThrough = new ColumnInfo(Msg.translate(ctx, "XX_SellThrough"), 
			".",Double.class);
	private ColumnInfo colCategory = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Category_ID"), 
			".",String.class);
	private ColumnInfo colDept = new ColumnInfo(Msg.translate(ctx, "XX_VMR_DEPARTMENT_ID"), 
			".",String.class);	
	private ColumnInfo colLine= new ColumnInfo(Msg.translate(ctx, "XX_VMR_LINE_ID"), 
			".",String.class);
	private ColumnInfo colSection = new ColumnInfo(Msg.translate(ctx, "XX_VMR_SECTION_ID"), 
			".",String.class);
	private ColumnInfo colTypeInv = new ColumnInfo(Msg.translate(ctx, "XX_typeInventory_2"), 
			".",String.class);
	private ColumnInfo colBrand = new ColumnInfo(Msg.translate(ctx, "XX_Brand"), 
			".",String.class);
	private ColumnInfo colPack = new ColumnInfo(Msg.translate(ctx, "Package"), 
			".",String.class);
	private ColumnInfo colColle = new ColumnInfo(Msg.translate(ctx, "XX_Collection"), 
			".",String.class);
	private ColumnInfo colSeason = new ColumnInfo(Msg.translate(ctx, "XX_Season"), 
			".",String.class);
	
	
	/**Carga todas las temporadas*/
	private void uploadSeasons() {
		KeyNamePair loadKNP;
		String sql = "";						
		comboSeason.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboSeason.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllSeasons"));
		comboSeason.addItem(loadKNP);
		
		sql = "SELECT se.XX_VMA_SEASON_ID, se.NAME " +
				"\nFROM XX_VMA_SEASON se WHERE se.ISACTIVE = 'Y' " +
				"\nAND se.AD_Client_ID = "+ ctx.getAD_Client_ID()+
				"\n ORDER BY se.NAME"; 	
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		try{			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboSeason.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			//Cerrar los statements
			if (rs != null)
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			
			if (pstmt != null)
				try {
					pstmt.close();
					pstmt = null;
				} catch (SQLException e) {}		
		}
		
	}

	/** Cargar las tiendas  */
	protected final void cargarTienda() {
		
		String sql = "";						
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String storeName = "";
		
		sql = " SELECT ct.VALUE||'-'||ct.NAME " +
				" FROM M_WAREHOUSE ct WHERE ct.ISACTIVE = 'Y' " +
				"AND ct.M_WAREHOUSE_ID = " + store ; 		
		try{			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				storeName = rs.getString(1);
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			//Cerrar los statements
			if (rs != null)
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			
			if (pstmt != null)
				try {
					pstmt.close();
					pstmt = null;
				} catch (SQLException e) {}		
		}
		textStore.setValue(storeName);
		
	}
	/** Cargar por paquetes  */
	private void uploadPackages() {
		KeyNamePair collection = (KeyNamePair)comboCollection.getSelectedItem();				
		KeyNamePair loadKNP;
		String sql;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
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
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
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
		finally {
			//Cerrar los statements
			if (rs != null)
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			
			if (pstmt != null)
				try {
					pstmt.close();
					pstmt = null;
				} catch (SQLException e) {}		
		}
	}

	/**Cargar por coleccion*/
	private void uploadCollection() {
		KeyNamePair loadKNP;
		String sql = "";						
		comboCollection.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboCollection.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllCollections"));
		comboCollection.addItem(loadKNP);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		sql = "SELECT co.XX_VMR_COLLECTION_ID, co.NAME " +
				"\nFROM XX_VMR_COLLECTION co WHERE co.ISACTIVE = 'Y' or co.ISACTIVE = 'N'" +
				"\nAND co.AD_Client_ID = "+ ctx.getAD_Client_ID()+
				"\n ORDER BY co.NAME"; 			
		try{			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
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
		finally {
			//Cerrar los statements
			if (rs != null)
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			
			if (pstmt != null)
				try {
					pstmt.close();
					pstmt = null;
				} catch (SQLException e) {}		
		}
	}
	
	/**Carga las marcas*/
	private void cargarMarca(){
		
		KeyNamePair loadKNP;
		String sql = "";						
		comboMark.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboMark.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllBrands"));
		comboMark.addItem(loadKNP);	
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		sql = " SELECT ct.XX_VMR_BRAND_ID, ct.VALUE||'-'||ct.NAME " +
				" FROM XX_VMR_BRAND ct WHERE ct.ISACTIVE = 'Y' " +	
				"\nAND ct.AD_Client_ID = "+ ctx.getAD_Client_ID()+
				"\nORDER BY ct.VALUE"; 		
		try{			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboMark.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			//Cerrar los statements
			if (rs != null)
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			
			if (pstmt != null)
				try {
					pstmt.close();
					pstmt = null;
				} catch (SQLException e) {}		
		}
		
		
	}
	private void cargaTipoOrden(){
		
		KeyNamePair loadKNP;
		
		comboOrderedby.removeAllItems();
		
		loadKNP = new KeyNamePair(0,"");
		comboOrderedby.addItem(loadKNP);
		
//		loadKNP = new KeyNamePair(1,Msg.translate(Env.getCtx(), "XX_SellThrough"));
//		comboOrderedby.addItem(loadKNP);
		loadKNP = new KeyNamePair(2,Msg.translate(Env.getCtx(), "XX_PartsSales"));
		comboOrderedby.addItem(loadKNP);
		loadKNP = new KeyNamePair(3,Msg.translate(Env.getCtx(), "XX_SalesInBolivar"));
		comboOrderedby.addItem(loadKNP);
	}
	
	private void cargaTipoInventario(){
		KeyNamePair loadKNP;
		String sql = "";						
		comboTypeInventory.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboTypeInventory.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllTypeIntentory"));
		comboTypeInventory.addItem(loadKNP);	
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		sql = " SELECT ct.XX_VMR_TYPEINVENTORY_ID, ct.VALUE||'-'||ct.NAME " +
				" FROM XX_VMR_TYPEINVENTORY ct WHERE ct.ISACTIVE = 'Y' " +
				"\nAND ct.AD_Client_ID = "+ ctx.getAD_Client_ID()+
				"\n ORDER BY ct.VALUE"; 		
		try{			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboTypeInventory.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			//Cerrar los statements
			if (rs != null)
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			
			if (pstmt != null)
				try {
					pstmt.close();
					pstmt = null;
				} catch (SQLException e) {}		
		}
		
		
		
	}
}
