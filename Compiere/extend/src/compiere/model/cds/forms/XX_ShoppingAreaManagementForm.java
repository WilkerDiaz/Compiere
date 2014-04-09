package compiere.model.cds.forms;


import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.Box;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CTextField;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.X_Ref_XX_OrderStatus;
import compiere.model.cds.X_Ref_XX_OrderType;
import compiere.model.cds.forms.indicator.XX_Indicator;


/** Clase que extiende al indicador genérico y representa al indicador del area de compras
 * Agrega campos adicionales como  comprador,estado de o/c y oculta productos y tienda
 * @author Diana Rozo
 * @author Gabriela Marques
 * */
public class XX_ShoppingAreaManagementForm extends XX_Indicator {

	private static final long serialVersionUID = -6440503234752622560L;
	
	//Campos necesarios para este indicador 	
	
	private CLabel labelComprador = new CLabel(Msg.translate(ctx, "XX_Buyer"));
	private VComboBox comboComprador = new VComboBox();
	private CCheckBox checkComprador = new CCheckBox();
	// Label período hasta
	protected VNumber monthPeriod2 = new VNumber();
	protected VNumber yearPeriod2 = new VNumber();
	Box boxPeriod2 = Box.createHorizontalBox();	
	private CLabel labelPeriod2 =  new CLabel("Hasta ("+Msg.getMsg(ctx, "XX_MonthYear")+")");
	
	//% ordenes anuladas y con entrega con retraso
	private CLabel labelEntregaRetrasoNac = new CLabel();
	private CLabel labelEntregaRetrasoImp = new CLabel();
	private CLabel labelEntregaRetraso = new CLabel();
	private CLabel labelOrdAnuladaNational = new CLabel();
	private CLabel labelOrdAnuladaImported = new CLabel();
	private CLabel labelOrdAnulada = new CLabel();
	private CLabel labelCumplimiento = new CLabel();
	private CLabel labelProdDef = new CLabel();
	private CTextField PorcRetrasoNac = new CTextField();
	private CTextField PorcRetrasoImp = new CTextField();
	private CTextField PorcRetraso = new CTextField();
	private CTextField PorcAnuladaNational = new CTextField();
	private CTextField PorcAnuladaImported = new CTextField();
	private CTextField PorcAnulada = new CTextField();
	private CTextField PorcCumplimiento = new CTextField();
	private CTextField PorcProdDef = new CTextField();
	

	//Cabeceras de las columnas de la tabla 
	private ColumnInfo colOrd = new ColumnInfo(Msg.translate(ctx, "C_ORDER_ID"), 
			"\nord.documentno",String.class);
	private ColumnInfo colStatus = new ColumnInfo(Msg.translate(ctx, "XX_OrderStatus"), 
			"\n(CASE WHEN ord.xx_orderstatus ='CH' THEN 'CHEQUEADA' " +			
			"\nWHEN ord.xx_orderstatus ='RE' THEN 'RECIBIDA' " +		
			"\nWHEN ord.xx_orderstatus ='AN' THEN 'ANULADA' " +		
			"\nELSE '-' END) ", String.class);			
	private ColumnInfo aprDate = new ColumnInfo(Msg.translate(ctx, "XX_APPROVALDATE"), 
					"\nto_char(ord.XX_APPROVALDATE,'DD/MM/YYYY')",String.class);
	private ColumnInfo arrivDate = new ColumnInfo(Msg.translate(ctx, "XX_ARRIVALDATE"), 
			"\nto_char(ord.XX_ARRIVALDATE,'DD/MM/YYYY')",String.class);
	private ColumnInfo colEstimatedDate = new ColumnInfo(Msg.translate(ctx, "XX_EstimateDate"), 
			"\nto_char(ord.XX_ESTIMATEDDATE,'DD/MM/YYYY')",String.class);
	private ColumnInfo colEntranceDate = new ColumnInfo(Msg.translate(ctx, "XX_EntraceDate"), 
			"\nto_char(ord.XX_ENTRANCEDATE,'DD/MM/YYYY')",String.class);
	private ColumnInfo colCatg = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Category_ID"), 
			"\ncat.Name",String.class);	
	private ColumnInfo colOrdType = new ColumnInfo(Msg.translate(ctx, "XX_OrderType_I"), 
			"\nord.XX_ORDERTYPE",String.class);	
	private ColumnInfo colDept = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Department_ID"),
			"\ndep.NAME",String.class);	
	private ColumnInfo colBPar = new ColumnInfo(Msg.translate(ctx, "C_BPARTNER_ID"), 
			"\npart.NAME",String.class);	
	private ColumnInfo colBuy = new ColumnInfo(Msg.translate(ctx, "XX_Buyer"), 
			"\ncomp.NAME",String.class);	
	private ColumnInfo colDelyCompl = new ColumnInfo(Msg.translate(ctx, "XX_DeliveryCompliance"),
			/*"\nround(sum(case when m.c_order_id IS NULL THEN NULL " +			
			"\nwhen ol.QTYORDERED = 0 then 100 " +
			"\nwhen (ol.QTYORDERED >= iol.PICKEDQTY ) " +
			"\nthen (iol.PICKEDQTY/ol.QTYORDERED)*100 else 100 end)/count(*),2)*/
			"\n round( case sum(iol.TargetQty) when 0 then 0 else sum(iol.PICKEDQTY)*100 / sum(iol.TargetQty) end, 2) cumplimiento", Double.class);
	
		
	private ColumnInfo colRejProd = new ColumnInfo(Msg.translate(ctx, "XX_DefectiveProducts"),
			/*"\nround(sum(case when m.c_order_id IS NULL THEN NULL " +			
			"\nwhen iol.PICKEDQTY = 0 then 0 " +
			"\nwhen ord.xx_ordertype='"+ X_Ref_XX_OrderType.IMPORTADA.getValue() + "' then (iol.scrappedqty/iol.PICKEDQTY)*100 " +
			"\nwhen ord.xx_ordertype='"+ X_Ref_XX_OrderType.NACIONAL.getValue() + "' " +
			"\nthen (iol.scrappedqty/(iol.PICKEDQTY-iol.XX_EXTRAPIECES))*100end)/count(*),2)"*/
			"\n round( case sum(iol.PICKEDQTY) when 0 then 0 " +
					"	else sum(iol.scrappedqty)*100/(sum(iol.PICKEDQTY)-sum(iol.XX_EXTRAPIECES)) end, 2) defectuosos", Double.class);	
	
	//Indice de la tabla
	int indiceTabla = 0;
	// Cláusula WHERE global (filtros)
	//private String fromG = "";
	private String whereG = "", fechasG= "", fechasAn = "";
	//Período
	private int mes=1, mes2=1, anio=0, anio2 = 0;
	
	//Selecciones actuales
	KeyNamePair catg = null;
	KeyNamePair dept = null;		
	KeyNamePair bpar = null;	
	KeyNamePair bcomp = null;
	
	//Total de OC buscadas
	int totOCNac = 0, totOCImpt = 0, totOCCons = 0;
	
	//Formato
	DecimalFormat df = new DecimalFormat("##0.00");
	
	
	@Override
	protected void ocultarParametrosDefecto() {		
		//Producto no es un campo que se desee para este indicador
		ocultarParametro(PRODUCTO);
		ocultarParametro(SECCION);
		ocultarParametro(LINEA);
	}
	
	@Override
	protected void agregarParametros() {
		//label de % de entrega con retraso Nacional
		labelEntregaRetrasoNac.setText(Msg.translate(Env.getCtx(), "XX_Delayed_Deliveries_National"));
		PorcRetrasoNac.setPreferredSize(new Dimension(45,20));
		PorcRetrasoNac.setEditable(false);
		
		//label de % de entrega con retraso Importado
		labelEntregaRetrasoImp.setText(Msg.translate(Env.getCtx(), "XX_Delayed_Deliveries_Imported"));
		PorcRetrasoImp.setPreferredSize(new Dimension(45,20));
		PorcRetrasoImp.setEditable(false);
		
		//label de % de entrega con retraso Consolidado
		labelEntregaRetraso.setText(Msg.translate(Env.getCtx(), "XX_Delayed_Deliveries"));
		PorcRetraso.setPreferredSize(new Dimension(45,20));
		PorcRetraso.setEditable(false);
		
		//label de % de ordenes anuladas nacionales
		labelOrdAnuladaNational.setText(Msg.translate(Env.getCtx(), "XX_Cancelled_PO_National"));
		PorcAnuladaNational.setPreferredSize(new Dimension(45,20));
		PorcAnuladaNational.setEditable(false);
		
		//label de % de ordenes anuladas Importadas
		labelOrdAnuladaImported.setText(Msg.translate(Env.getCtx(), "XX_Cancelled_PO_Imported"));
		PorcAnuladaImported.setPreferredSize(new Dimension(45,20));
		PorcAnuladaImported.setEditable(false);
		
		//label de % de ordenes anuladas consolidado
		labelOrdAnulada.setText(Msg.translate(Env.getCtx(), "XX_Cancelled_PO"));
		PorcAnulada.setPreferredSize(new Dimension(45,20));
		PorcAnulada.setEditable(false);
		
		//label de % promedio de cumplimiento
		labelCumplimiento.setText("Promedio de cumplimiento");//Msg.translate(Env.getCtx(), ""));
		PorcCumplimiento.setPreferredSize(new Dimension(45,20));
		PorcCumplimiento.setEditable(false);
		
		//label de % promedio de productos defectuosos
		labelProdDef.setText("Promedio de productos defectuosos");//Msg.translate(Env.getCtx(), ""));
		PorcProdDef.setPreferredSize(new Dimension(45,20));
		PorcProdDef.setEditable(false);
		
		
		//Agregar los labels en el panel de resultados
		//Fila1
		resultPanel.add(labelEntregaRetrasoNac, new GridBagConstraints(
				0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(PorcRetrasoNac,new GridBagConstraints(
				2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));		
		resultPanel.add(labelOrdAnuladaNational,new GridBagConstraints(
				4, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(PorcAnuladaNational,new GridBagConstraints(
				5, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelCumplimiento,new GridBagConstraints(
				7, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(PorcCumplimiento,new GridBagConstraints(
				8, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		//Fila 2
		resultPanel.add(labelEntregaRetrasoImp, new GridBagConstraints(
				0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(PorcRetrasoImp,new GridBagConstraints(
				2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));	
		resultPanel.add(labelOrdAnuladaImported,new GridBagConstraints(
				4, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(PorcAnuladaImported,new GridBagConstraints(
				5, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelProdDef,new GridBagConstraints(
				7, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(PorcProdDef,new GridBagConstraints(
				8, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		//Fila 3
		resultPanel.add(labelEntregaRetraso, new GridBagConstraints(
				0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(PorcRetraso,new GridBagConstraints(
				2, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));	
		resultPanel.add(labelOrdAnulada,new GridBagConstraints(
				4, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(PorcAnulada,new GridBagConstraints(
				5, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	
		
		//Agregar Hasta período
		boxPeriod2.add(labelPeriod2);
		boxPeriod2.add(monthPeriod2);
		boxPeriod2.add(slashPeriod);
		boxPeriod2.add(yearPeriod2);
		monthPeriod2.setPreferredSize(new Dimension(monthPeriod2.getPreferredSize().width/4, monthPeriod2.getPreferredSize().height));
		yearPeriod2.setPreferredSize(new Dimension(yearPeriod2.getPreferredSize().width/2, monthPeriod2.getPreferredSize().height));
		monthPeriod2.setRange(1.0, 12.0);
		monthPeriod2.setDisplayType(DisplayTypeConstants.Integer);
		yearPeriod2.setRange(1.0, Double.MAX_VALUE);
		yearPeriod2.setDisplayType(DisplayTypeConstants.Integer);
		agregarParametro(labelPeriod2);			
		agregarParametro(boxPeriod2, 2);	

		//Agregar Comprador			
		agregarParametro(labelComprador);
		agregarParametro(comboComprador);
		agregarParametro(checkComprador);
		
		
		//Para que me coloque el campo de color blanco
		monthPeriod.setBackground(false);
		yearPeriod.setBackground(false);
		
		dataPane.setPreferredSize(new Dimension(800, 600));
	}
	
	@Override
	protected void personalizar() {
		
		//Cambiar Label período
		labelPeriod.setText("Desde ("+Msg.getMsg(ctx, "XX_MonthYear")+")");
		
		//Cargar los datos de los campos que se agregaron en este indicador
		cargarCompradores();
		
		//Configurar campos de comprador	
		configurar(comboComprador, true);
		configurar(checkComprador, true, false);
		
		//Este indicador exige que los departamentos dependan de la categoria
		configurar(comboDepartment, false);
		configurar(checkDepartment, false);
						
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_TotalPurchaseOrder"));
		
		//Comenzando
		configurar(bSearch, true);
		configurar(bPrint, false);
		configurar(bFile, false);
	
		//Agregar listeners a los nuevos campos
		checkComprador.addActionListener(this);
		monthPeriod2.addActionListener(this);
		yearPeriod2.addActionListener(this);
		
	} //personalizar()
	
	protected void limpiarFiltro () {
		//Limpia los campos del filtro padre
		super.limpiarFiltro();		
		//Borrar aquellos campos que se agregaron en esta forma
		configurar(comboComprador, true);
		configurar(checkComprador, true, false);
		monthPeriod2.setValue(null);
		yearPeriod2.setValue(null);
	} //limpiarFiltro
	
	
	/** En este método agrega acciones a las acciones predeterminadas */
	public void actionPerformed(ActionEvent e) {
		//Ejecutará las acciones por defecto
		super.actionPerformed(e);

		//Desactivar los escuchadores mientras realizo las modifciaciones
		desactivarActionListeners();
		//Desactivar listeners adicionales
		checkComprador.removeActionListener(this);
		monthPeriod2.removeActionListener(this);
		yearPeriod2.removeActionListener(this);
		
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

		//Verificar si se selecciono el check de comprador
		if (e.getSource() == checkComprador) {			
			if ((Boolean)checkComprador.getValue()) {				
				configurar(comboComprador, false, 99999999 );				
			} else {
				configurar(comboComprador, true);
			}
		}
		
		//Activar los escuchadores
		activarActionListeners();
		//Agregar listeners a los nuevos campos
		checkComprador.addActionListener(this);
		monthPeriod2.addActionListener(this);
		yearPeriod2.addActionListener(this);
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
	
		PreparedStatement ps = null;		
		ResultSet rs = null;
		try{			
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboComprador.addItem(loadKNP);			
			}			
			rs.close();
			ps.close();		
		}
		catch (SQLException e)	{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
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
	
	
	/** llenarTabla() Se va a ejecutar cuando el usuario presione buscar*/
	@Override
	protected void llenarTabla() {

		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
	
		//Si no se ha cargado el header previamente
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		
		// Setear variables globales
		catg = (KeyNamePair)comboCategory.getSelectedItem();
		dept = (KeyNamePair)comboDepartment.getSelectedItem();		
		bpar = (KeyNamePair)comboBPartner.getSelectedItem();		
		bcomp= (KeyNamePair)comboComprador.getSelectedItem();	
		
		if (monthPeriod.getValue() != null ){
			mes = (Integer)monthPeriod.getValue(); 
		}
		if (yearPeriod.getValue() != null ){
			anio = (Integer)yearPeriod.getValue();	
		}	
		if (monthPeriod2.getValue() != null ){
			mes2 = (Integer)monthPeriod2.getValue() + 1;  // Para la comparación de fechas
		}
		if (yearPeriod2.getValue() != null ){
			anio2 = (Integer)yearPeriod2.getValue();	
		}	
		if (mes2 == 13) {
			mes2 = 1; anio2++;
		}
		
		//Calcular el query
		try {
			calcularQuery();	 // Tabla Resultado	
			totalOrdenesBuscadas(); // Setear variables globales de total de órdenes buscadas
			
			//Promedios al pie de la ventana
			int total = miniTable.getRowCount(); //int total = totalOrdenesBuscadas();
			
			if (total > 0) { // Hay resultados
				porcEntregaConRetraso(); //Porcentaje de entrega con retraso consolidado, nacional e importado
				porOrdAnuladasNac(); //Porcentaje de entrega anuladas consolidado, nacional e importado
				promedios(total);
						
				miniTable.setRowSelectionAllowed(true);
				miniTable.setSelectionBackground(Color.white);
				//miniTable.autoSize();
				miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
				miniTable.getTableHeader().setReorderingAllowed(false);	
				
				configurar(bFile, true);				
				configurar(bPrint, true);
			} else {	
				configurar(bFile, false);
				configurar(bPrint, false);
				PorcRetrasoNac.setText("0");
				PorcRetrasoImp.setText("0");
				PorcRetraso.setText("0");
				PorcAnuladaNational.setText("0");
				PorcAnuladaImported.setText("0");
				PorcAnulada.setText("0");
				PorcCumplimiento.setText("0");
				PorcProdDef.setText("0");
				statusBar.setStatusLine(Msg.getMsg(Env.getCtx(),"FindZeroRecords"));
				ADialog.info(m_WindowNo,  new Container() , Msg.getMsg(Env.getCtx(),"FindZeroRecords"));	
			}
		} catch (Exception e) {}
		
		m_frame.setCursor(Cursor.getDefaultCursor());
		
	}
	
	/** Se determina el query de acuerdo a los parámetros ingresados*/
	public void calcularQuery () {
		//Limpiar búsquedas anteriores
		whereG = "";
		fechasG = "";
		fechasAn = "";
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();
		indiceTabla++;
		//Agrego las columnas 
		columnasAgregadas.add(colOrd);
		columnasAgregadas.add(colStatus);
		columnasAgregadas.add(colOrdType);
		columnasAgregadas.add(aprDate);		
		columnasAgregadas.add(arrivDate);
		columnasAgregadas.add(colEstimatedDate);
		columnasAgregadas.add(colEntranceDate);
		columnasAgregadas.add(colCatg);
		columnasAgregadas.add(colDept);
		columnasAgregadas.add(colBPar);
		columnasAgregadas.add(colBuy);
		columnasAgregadas.add(colDelyCompl);
		columnasAgregadas.add(colRejProd);
		
		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);
		
		// Creación del query
		String select = miniTable.prepareTable(layout, null, null, false, null);
		/* Version Pasante
		 * String query =
			"\nFROM C_BPARTNER part, C_BPARTNER comp, C_ORDER ord,XX_VMR_Department dep," +
			"\nXX_VMR_CATEGORY cat,c_orderline ol,m_inoutline iol ,m_inout m" +
			"\nWHERE ord.C_BPARTNER_ID=part.C_BPARTNER_ID " +
			"\nAND ord.XX_USERBUYER_ID=comp.C_BPARTNER_ID " +
			"\nAND ord.XX_VMR_Department_ID = dep.XX_VMR_Department_ID" +
			"\nAND cat.XX_VMR_CATEGORY_ID = ord.XX_VMR_CATEGORY_ID " +
			"\nand ol.c_orderline_id=iol.c_orderline_id (+)and ol.c_order_id=ord.c_order_id " +
			"and m.c_order_id (+)=ord.c_order_id and ord.issotrx='N' "+
			"\nand ord.xx_orderstatus in ('CH','RE')";
		*/
		
		String query =
			"\n FROM c_order ord JOIN c_orderline ol on (ord.C_ORDER_ID = ol.C_ORDER_ID)" +
			"\n    RIGHT JOIN m_inoutline iol ON (iol.c_orderline_id = ol.c_orderline_id)" +
			"\n    RIGHT JOIN m_inout m ON (m.c_order_id = ord.c_order_id)" +
			"\n    JOIN C_BPARTNER part ON (ord.C_BPARTNER_ID = part.C_BPARTNER_ID )" +
			"\n    JOIN C_BPARTNER comp ON (ord.XX_USERBUYER_ID=comp.C_BPARTNER_ID )" +
			"\n    JOIN XX_VMR_Department dep ON (ord.XX_VMR_Department_ID = dep.XX_VMR_Department_ID)" +
			"\n    JOIN XX_VMR_CATEGORY cat ON (cat.XX_VMR_CATEGORY_ID = ord.XX_VMR_CATEGORY_ID)" +
			"\n WHERE ord.issotrx='N' and ord.xx_orderstatus in ('CH','RE', 'AN')" ;

		//Categoría
			indiceTabla = 0;
			if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
				indiceTabla++;
				if (catg.getKey()!= 99999999 )	{		
					whereG += "\nAND ord.XX_VMR_Category_ID = " + catg.getKey();
				}
			}		
			//Departamento			
			if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {
				indiceTabla++;
				if (dept.getKey()!= 99999999 ) {
					whereG += "\nAND ord.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
				}
			}
			
			//Proveedor
			if((Boolean)checkBPartner.getValue() == true || bpar != null && (bpar.getKey() == 99999999 || bpar.getKey() != 0)){
				indiceTabla++;
				if (bpar.getKey()!= 99999999) {
					whereG += "\nAND ord.C_BPARTNER_ID = " + bpar.getKey();
				}
			}
			
			//Comprador
			if ((Boolean)checkComprador.getValue()==true || bcomp != null && (bcomp.getKey() == 99999999 || bcomp.getKey()!= 0)){	
				indiceTabla++;					
				if (bcomp.getKey()!= 99999999 ) {
					whereG += "\nAND ord.XX_USERBUYER_ID = " + bcomp.getKey();
				}
			}	
			//mes y año
			if (monthPeriod.getValue() != null && monthPeriod2.getValue() != null && 
						yearPeriod.getValue() != null && yearPeriod2.getValue() != null ) {
				fechasG += //"\nand  TO_CHAR(ord.XX_ENTRANCEDATE, 'MM')="+ mes ;	
						" \nAND ord.XX_ENTRANCEDATE >= to_date('"+anio+"-"+mes+"-01', 'YYYY-MM-DD') " +
						" \nAND ord.XX_ENTRANCEDATE < to_date('"+anio2+"-"+mes2+"-01', 'YYYY-MM-DD')";
				fechasAn += //"\nand  TO_CHAR(ord.XX_ENTRANCEDATE, 'MM')="+ mes ;	
					" \nAND ord.UPDATED >= to_date('"+anio+"-"+mes+"-01', 'YYYY-MM-DD') " +
					" \nAND ord.UPDATED < to_date('"+anio2+"-"+mes2+"-01', 'YYYY-MM-DD')";
			}				
		
		String groupby = "\n group by ord.documentno, cat.NAME, dep.NAME, part.NAME," +
						"\n comp.name, ord.xx_orderstatus,ord.XX_ORDERTYPE, " +
						"\n to_char(ord.XX_ESTIMATEDDATE,'DD/MM/YYYY'), to_char(ord.XX_ENTRANCEDATE,'DD/MM/YYYY'), " +
						"\n to_char(ord.XX_APPROVALDATE,'DD/MM/YYYY'), to_char(ord.XX_ARRIVALDATE,'DD/MM/YYYY') ";
		String orderby ="\n order by(ord.documentno)";
		
		String sql = select + query + whereG + fechasG + groupby + orderby;
		//System.out.println("SQL TABLA: "+ sql);
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
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			}
			if (ps != null) {
				try {
					ps.close();
					ps = null;
				} catch (SQLException e) {}	
			}
		}			
	}
	

	/**Metodo que calcula la cantidad de ordenes encontradas dependiendo de los filtros
	 * realizados
	 * @return total de ordenes*/
	private void totalOrdenesBuscadas(){
		
		/*Calculo la cantidad total de las ordenes de compra nacionales, importadas y consolidado */	
		String sql ="\nSELECT count(distinct case when ord.XX_ORDERTYPE='"+ X_Ref_XX_OrderType.NACIONAL.getValue() + "' then (ord.c_order_id) end)  NAC," +
				"\n		count(distinct case when ord.XX_ORDERTYPE='"+ X_Ref_XX_OrderType.IMPORTADA.getValue() + "' then (ord.c_order_id) end) IMPT " +
				"\n FROM c_order ord ";
		String where = "\n WHERE ord.issotrx='N' and ord.xx_orderstatus in ('CH','RE') " ;
		
		sql+= where + whereG + fechasG;
		
		//System.out.println("\nTotal OC buscadas: "+sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();	
			if (rs.next()) {
				totOCNac = (rs.getInt("NAC"));
				totOCImpt = (rs.getInt("IMPT"));
				totOCCons = totOCNac + totOCImpt;
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);			
		} finally {			
			//Cerrar los statements
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			}
			if (ps != null) {
				try {
					ps.close();
					ps = null;
				} catch (SQLException e) {}		
			}
		}
			
	}
	

	
	/** Calculo el Porcentaje de retraso de las ordenes de compras
	 * @param total es el nro de ordenes buscadas*/
	private void porcEntregaConRetraso() {
		double delayedDeliveryNac = 0, delayedDeliveryImpt = 0, delayedDeliveryCons = 0;
			
		//Version de la pasante (semi)
		/*sql ="select count(count(ord.c_order_id))" +
			"\nfrom c_order ord,C_BPARTNER part, C_BPARTNER comp,XX_VMR_Department dep," +
			"\nXX_VMR_CATEGORY cat,c_orderline ol,m_inoutline iol ,m_inout m" +
			"\nwhere  ord.C_BPARTNER_ID=part.C_BPARTNER_ID AND ord.XX_USERBUYER_ID=comp.C_BPARTNER_ID " +
			"\nand ord.XX_VMR_Department_ID = dep.XX_VMR_Department_ID AND cat.XX_VMR_CATEGORY_ID =ord.XX_VMR_CATEGORY_ID" +
			"\nand ol.c_orderline_id=iol.c_orderline_id(+) and ol.c_order_id=ord.c_order_id and m.c_order_id(+)=ord.c_order_id" +
			"\nand to_char(ord.XX_ESTIMATEDDATE, 'YYYYMMDD') < to_char(ord.XX_ENTRANCEDATE, 'YYYYMMDD') " +
			"\nand ord.XX_ORDERTYPE='"+ X_Ref_XX_OrderType.NACIONAL.getValue() + "'";
		String groupby= "\ngroup by(ord.c_order_id)"; 
	
		sql+= whereG + groupby;
		System.out.println("\nEntrega con retraso Nacional: " +sql);*/
		
		String sql = 
				" SELECT count(distinct case when ord.XX_ORDERTYPE='"+ X_Ref_XX_OrderType.NACIONAL.getValue() + "' then (ord.c_order_id) end) NAC," +
				"\n    count(distinct case when ord.XX_ORDERTYPE='"+ X_Ref_XX_OrderType.IMPORTADA.getValue() + "' then (ord.c_order_id) end) IMPT" +
				"\n from c_order ord " ;
		String where = "\n WHERE  ord.issotrx='N'  and ord.xx_orderstatus in ('CH','RE')" +
				"\n AND to_char(ord.XX_ESTIMATEDDATE, 'YYYYMMDD') < to_char(ord.XX_ENTRANCEDATE, 'YYYYMMDD') " ;
			//	"\n AND XX_MOTIVCHANESTIMDATE_ID='1000104' "; Ya no se toma en cuenta el motivo
	
		sql += where + whereG + fechasG;
		//System.out.println("\nEntrega con retraso Nacional: " +sql);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();	
			if (rs.next()) {
				 delayedDeliveryNac = (rs.getInt("NAC"));
				 delayedDeliveryImpt = (rs.getInt("IMPT"));
				 delayedDeliveryCons = delayedDeliveryNac + delayedDeliveryImpt;
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);			
		} finally {			
			//Cerrar los statements
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			}
			if (ps != null) {
				try {
					ps.close();
					ps = null;
				} catch (SQLException e) {}		
			}
		}
		
		//redondeo el resultado
		DecimalFormat Result = new DecimalFormat("##0.00");

		// Evitar división por cero
		if (totOCNac == 0) { 
			PorcRetrasoNac.setText("0");
		} else {
			PorcRetrasoNac.setText(Result.format((delayedDeliveryNac/totOCNac)*100));
		}
		if (totOCImpt == 0) {
			PorcRetrasoImp.setText("0");
		} else {
			PorcRetrasoImp.setText(Result.format((delayedDeliveryImpt/totOCImpt)*100));
		}
		PorcRetraso.setText(Result.format((delayedDeliveryCons/totOCCons)*100)); //Ya se filtró en la otra rutina
			
	}
	
	
	
	/**Calculo el Porcentaje de las ordenes nacionales que fueron anuladas
	 * @param total es el nro de ordenes buscadas*/
	private void porOrdAnuladasNac(){
		double anuladasNac=0, anuladasImpt=0, anuladasCons=0;
		String sql =" ";
		
	    /*
		sql = "SELECT count(count(ord.c_order_id)) " +
			"\nFROM c_order ord,C_BPARTNER part, C_BPARTNER comp,XX_VMR_Department dep," +
			"\nXX_VMR_CATEGORY cat,c_orderline ol,m_inoutline iol ,m_inout m" +
			"\nWHERE  ord.C_BPARTNER_ID=part.C_BPARTNER_ID AND " +
			"\nord.XX_USERBUYER_ID=comp.C_BPARTNER_ID" +
			"\nand ord.XX_VMR_Department_ID = dep.XX_VMR_Department_ID " +
			"\nAND cat.XX_VMR_CATEGORY_ID =ord.XX_VMR_CATEGORY_ID" +
			"\nand ol.c_orderline_id=iol.c_orderline_id(+) and ol.c_order_id=ord.c_order_id " +
			"\nand m.c_order_id (+)=ord.c_order_id "+ //and ord.issotrx='N' " +			
			"\nand ord.XX_ORDERTYPE='"+ X_Ref_XX_OrderType.NACIONAL.getValue() + "'"+
			"\nand ord.xx_orderstatus='"+ X_Ref_XX_OrderStatus.ANULADA.getValue() + "'";
				
		String groupby= "Group by(ord.c_order_id)";
		String query= sql + whereG + groupby;
		*/
		

		sql = "SELECT count(distinct case when ord.XX_ORDERTYPE='"+ X_Ref_XX_OrderType.NACIONAL.getValue() 
					+ "'\n then (ord.c_order_id) end) NAC," +
				"\n    count(distinct case when ord.XX_ORDERTYPE='"+ X_Ref_XX_OrderType.IMPORTADA.getValue() 
					+ "'\n then (ord.c_order_id) end) IMPT " +
				"\n FROM c_order ord " ;
		String where = "\n WHERE ord.issotrx='N' " +
				"\n AND ord.xx_orderstatus='"+ X_Ref_XX_OrderStatus.ANULADA.getValue() + "' ";
				
		String query= sql + where + whereG + fechasAn;
		
		//System.out.println("\n Porc anuladas "+ query);
		//System.out.println(totOCNac +", "+ totOCImpt);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(query, null);			
			rs = ps.executeQuery();	
			if (rs.next()) {
				 anuladasNac = (rs.getInt("NAC"));
				 anuladasImpt = (rs.getInt("IMPT"));
				 anuladasCons = anuladasNac + anuladasImpt;
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
		
		//redondeo el resultado
		// Evitar división por cero
		if (totOCNac == 0) { 
			PorcAnuladaNational.setText("0");
		} else {
			PorcAnuladaNational.setText(df.format((anuladasNac/(totOCNac+anuladasNac))*100));
		}
		if (totOCImpt == 0) {
			PorcAnuladaImported.setText("0");
		} else {
			PorcAnuladaImported.setText(df.format((anuladasImpt/(totOCImpt+anuladasImpt))*100));
		}
		PorcAnulada.setText(df.format((anuladasCons/(totOCCons+anuladasCons))*100)); //Ya se filtró en la otra rutina
			
		
	}
	
	/**Calculo el Promedio de Cumplimiento
	 * @param total es el nro de ordenes buscadas */
	private void promedios(int total) {
		double cump = 0, defect = 0;
		
		String sql = "SELECT sum( case sum(iol.TargetQty) when 0 then 0" +
				"\n            else sum(iol.PICKEDQTY)*100 / sum(iol.TargetQty) end) cumplimiento, " +
				"\n			 sum(case when sum(iol.PICKEDQTY) = 0 then 0" +
				"\n            else sum(iol.scrappedqty)*100/sum(iol.PICKEDQTY-iol.XX_EXTRAPIECES) end) defectuosos" +
				"\n  FROM C_ORDER ord," +
				"\n    c_orderline ol," +
				"\n    m_inoutline iol" ;
		
		String where = "\n WHERE ord.issotrx='N' and ord.xx_orderstatus in ('CH','RE','AN')" +
				" and ol.c_orderline_id=iol.c_orderline_id (+)and ol.c_order_id=ord.c_order_id ";
		String groupby = "\n GROUP BY ord.documentno ";
		String query= sql + where + whereG + fechasG + groupby;

		
		//System.out.println("\nsql Promedios: "+ query);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(query, null);			
			rs = ps.executeQuery();	
			if (rs.next())  {
				cump = rs.getDouble("cumplimiento");
				defect = rs.getDouble("defectuosos");
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
			
		PorcCumplimiento.setText(df.format(cump/total));
		PorcProdDef.setText(df.format(defect/total));
	}
	
	
}