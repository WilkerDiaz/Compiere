package compiere.model.cds.forms;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.grid.ed.VComboBox;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CComboBox;
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

/** Clase que extiende al indicador genérico y representa al indicador del area de importaciones
 * Agrega campos adicionales como  comprador,estado de o/c y oculta productos y tienda
 * @author Diana Rozo
 * */
public class XX_ImportManagementAreaForm extends XX_Indicator {
	
	private static final long serialVersionUID = -6440503234752622560L;
	
	//Campos necesarios para este indicador 	
	
	private CLabel labelComprador = new CLabel(Msg.translate(ctx, "XX_Buyer"));
	private VComboBox comboComprador = new VComboBox();
	private CCheckBox checkComprador = new CCheckBox();
	
	
	//Estado O/C
	private CLabel labelOPurchaseStatus= new CLabel(Msg.translate(ctx,"XX_OrderStatus"));	
	private CComboBox comboOPurchaseStatus = new CComboBox();
	private Vector<String> OPurchaseStatus_name = new Vector<String>();
	
	//% Indicadores de importacion
	//private CLabel labelTentradaProv= new CLabel();
	private CLabel labelTentradaInter = new CLabel();
	private CLabel labelTiempoTransito = new CLabel();
	private CLabel labelTiempoNac = new CLabel();
	private CLabel labelTentregaNac = new CLabel();
	private CLabel labelTiempoLogImport = new CLabel();
	private CTextField TimeProv = new CTextField();
	private CTextField TimeInter = new CTextField();
	private CTextField TimeTransito = new CTextField();
	private CTextField TimeNacionalizacion = new CTextField();
	private CTextField TimeNacional = new CTextField();
	private CTextField TimeLogImport = new CTextField();
	//private int TEP = 0;
	private int TEI = 0;
	private int TEN = 0;
	private int TET = 0;
	private int TNAC = 0;
	
	private String fromG = "", whereG = "";
	
	@Override
	protected void ocultarParametrosDefecto() {
		
		//Producto no es un campo que se desee para este indicador
		ocultarParametro(PRODUCTO);
		ocultarParametro(SECCION);
		ocultarParametro(LINEA);
		
	}
	
	@Override
	protected void agregarParametros() {
		//Agregar Comprador			
		agregarParametro(labelComprador);
		agregarParametro(comboComprador);
		agregarParametro(checkComprador);
		
		//Agregar Status de O/C			
		agregarParametro(labelOPurchaseStatus);
		agregarParametro(comboOPurchaseStatus);
		
		//Para que me coloque el campo de color blanco
		monthPeriod.setBackground(false);
		yearPeriod.setBackground(false);
		
//		//label Tiempo de entrega Proveedor 
//		labelTentradaProv.setText(Msg.translate(Env.getCtx(), "XX_VendorDeliveryTime"));
//		TimeProv.setPreferredSize(new Dimension(45,20));
//		TimeProv.setEditable(false);
		
		//label tiempo de entrega internacional
		labelTentradaInter.setText(Msg.translate(Env.getCtx(), "XX_InternationalDeliveryTime"));
		TimeInter.setPreferredSize(new Dimension(45,20));
		TimeInter.setEditable(false);
		
		//label tiempo de transito
		labelTiempoTransito.setText(Msg.translate(Env.getCtx(), "XX_TransitTime "));
		TimeTransito.setPreferredSize(new Dimension(45,20));
		TimeTransito.setEditable(false);
		
		//label tiempo de transito
		labelTiempoNac.setText(Msg.translate(Env.getCtx(), "XX_NationalizationTime_1"));
		TimeNacionalizacion.setPreferredSize(new Dimension(45,20));
		TimeNacionalizacion.setEditable(false);
		
		//label tiempo de transito
		labelTentregaNac.setText(Msg.translate(Env.getCtx(), "XX_NationalDeliveryTime"));
		TimeNacional.setPreferredSize(new Dimension(45,20));
		TimeNacional.setEditable(false);
		
		//label Tiempo de Logística de Importaciones
		labelTiempoLogImport.setText(Msg.translate(Env.getCtx(), "XX_ImportLogisticsTime"));
		TimeLogImport.setPreferredSize(new Dimension(45,20));
		TimeLogImport.setEditable(false);
		
		//Agregar los labels en el panel de resultados		
//		resultPanel.add(labelTentradaProv,  
//				new GridBagConstraints(
//						0, 0, 1, 1, 0.0, 0.0,
//						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
//		resultPanel.add(TimeProv,new GridBagConstraints(
//				2, 0, 1, 1, 0.0, 0.0,
//				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));		
		resultPanel.add(labelTentradaInter,new GridBagConstraints(
				4, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(TimeInter,new GridBagConstraints(
				5, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelTiempoTransito,new GridBagConstraints(
				7, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(TimeTransito,new GridBagConstraints(
				8, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelTiempoNac,new GridBagConstraints(
				0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(TimeNacionalizacion,new GridBagConstraints(
				2, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelTentregaNac,new GridBagConstraints(
				4, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(TimeNacional,new GridBagConstraints(
				5, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelTiempoLogImport,new GridBagConstraints(
				7, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(TimeLogImport,new GridBagConstraints(
				8, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		dataPane.setPreferredSize(new Dimension(1100, 450));
	}

	@Override
	protected void personalizar() {
		//Cargar los datos de los campos que se agregaron en este indicador
		cargarCompradores();
		uploadStatusPurchaseOrder();
		
		//Configurar campos de estados de las ordenes	
		comboOPurchaseStatus.setEnabled(true);		
		comboOPurchaseStatus.setEditable(true);
		comboOPurchaseStatus.setSelectedIndex(0);
		
		
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
		
	}
	
	protected void limpiarFiltro () {
		
		//Limpia los campos del filtro padre
		super.limpiarFiltro();		
		
		//Borrar aquellos campos que se agregaron en esta forma
		configurar(comboComprador, true);
		configurar(checkComprador, true, false);
		
		comboOPurchaseStatus.setEnabled(true);		
		comboOPurchaseStatus.setEditable(true);
		comboOPurchaseStatus.setSelectedIndex(0);
		
	}

	/** En este método agrego acciones a las acciones predeterminadas */
	public void actionPerformed(ActionEvent e) {
		
		//Ejecutará las acciones por defecto
		super.actionPerformed(e);
		
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
			
		//Verificar si se selecciono el check de comprador
		}
		
		if (e.getSource() == checkComprador) {			
			if ((Boolean)checkComprador.getValue()) {				
				configurar(comboComprador, false, 99999999 );				
			} else {
				configurar(comboComprador, true);
			}
		}
		
		
		//Activar los escuchadores
		activarActionListeners();
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
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		sql = "SELECT C_BPARTNER_ID, NAME FROM AD_USER WHERE ISACTIVE='Y' AND C_BPARTNER_ID IN "+
			"(SELECT C_BPARTNER_ID " +
			"FROM C_BPARTNER WHERE isActive='Y' "+
			"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_BUYER_ID")+ ") " +
			" ORDER BY NAME";
		
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
	/** Cargar estados de ordenes de compra*/
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
				
					comboOPurchaseStatus.addItem(v.getValue() + "-" + v);
					OPurchaseStatus_name.add(v.getValue());
				
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, "Error",e);
		}
	
		comboOPurchaseStatus.setEnabled(true);		
		comboOPurchaseStatus.setEditable(true);
		comboOPurchaseStatus.setSelectedIndex(0);
	}
	 
	
	@Override
	protected void llenarTabla() {
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		//Si no se ha cargado el header previamente
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		
		//Calcular el query
		try {
			calcularQuery();
			
			
			//LLamada al calculo de porc de los indicadores
			//TimeProv.setText(String.valueOf(TiempoEntregaProveedor()));
			TimeInter.setText(String.valueOf(TiempoEntregaInternacional())); //Aquí se setean todos los parámetros del WHERE y FROM
			TimeTransito.setText(String.valueOf(TiempoEnTránsito()));
			TimeNacionalizacion.setText(String.valueOf(TiempoNacionalizacion()));
			TimeNacional.setText(String.valueOf(TiempoEntregaNacional()));
			TimeLogImport.setText(String.valueOf(TiempoLogísticaImportaciones()));
			
					
			miniTable.setRowSelectionAllowed(true);
			miniTable.setSelectionBackground(Color.white);
			//miniTable.autoSize();
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
				
		int mes=0;
		int año=0;
		
		if (monthPeriod.getValue() != null ){
			mes = (Integer)monthPeriod.getValue(); 
		}
		if (yearPeriod.getValue() != null ){
			
			año = (Integer)yearPeriod.getValue();	;
			
		}	
		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();		
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();		
		KeyNamePair bcomp= (KeyNamePair)comboComprador.getSelectedItem();	
		String status = (String)comboOPurchaseStatus.getSelectedItem();
		
		String queryFrom =
			"\nfrom XX_VLO_BOARDINGGUIDE bo,C_ORDER ord,C_BPARTNER part," +
			"\nC_BPARTNER comp,XX_VMR_Department dep,XX_VMR_CATEGORY cat";
			
		String query =
			"\nwhere bo.XX_VLO_BOARDINGGUIDE_ID(+)=ord.XX_VLO_BOARDINGGUIDE_ID" +
			"\nand ord.C_BPARTNER_ID=part.C_BPARTNER_ID AND ord.XX_USERBUYER_ID=comp.C_BPARTNER_ID" +
			"\nAND ord.XX_VMR_Department_ID = dep.XX_VMR_Department_ID" +
			"\nAND cat.XX_VMR_CATEGORY_ID = ord.XX_VMR_CATEGORY_ID" +
			"\nAND ord.XX_ORDERTYPE='Importada'";
			
			
		String select=" ";
		
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();
		
		
		//Categoría
		
		if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
	
			if (catg.getKey()!= 99999999 )			
					query += "\nAND cat.XX_VMR_Category_ID = " + catg.getKey();
		}		
		//Departamento			
		if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {
			
			if (dept.getKey()!= 99999999 ) 
					query += "\nAND dep.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
			
		}
		
		//Proveedor
		if((Boolean)checkBPartner.getValue() == true || bpar != null && (bpar.getKey() == 99999999 || bpar.getKey() != 0)){
			
			if (bpar.getKey()!= 99999999) 
				query += "\nAND part.C_BPARTNER_ID = " + bpar.getKey();				
		}
		
		//Comprador
		if ((Boolean)checkComprador.getValue()==true || bcomp != null && (bcomp.getKey() == 99999999 || bcomp.getKey()!= 0)){	
							
			if (bcomp.getKey()!= 99999999 )
				query += "\nAND dep.XX_USERBUYER_ID = " + bcomp.getKey();
		}	
		//status
		if(status != null && (status != "" || status == Msg.getMsg(Env.getCtx(), "XX_AllStatus"))){
	
			if(status != Msg.getMsg(Env.getCtx(), "XX_AllStatus")){
				query += "\nand ord.xx_orderstatus='"+ OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())+ "'" ;
			}
		}
		
		//Agrego las columnas 
		columnasAgregadas.add(colOrd);
		//columnasAgregadas.add(colStatus);
		//columnasAgregadas.add(colArrivalDate);
		columnasAgregadas.add(colDispatchDate);
		columnasAgregadas.add(colCatg);
		columnasAgregadas.add(colDept);
		columnasAgregadas.add(colBPar);
		columnasAgregadas.add(colBuy);
		columnasAgregadas.add(colEntranceDate);
		columnasAgregadas.add(colCarAgentDelivRealDate);
		columnasAgregadas.add(colShippingRealDate);
		columnasAgregadas.add(colVzlaArrivalRealDate);
		columnasAgregadas.add(colCustomRigthCancelDate);
		columnasAgregadas.add(colRealDispatchDate);
		columnasAgregadas.add(colCDArrivalRealDate);
		//columnasAgregadas.add(colTEP);
		columnasAgregadas.add(colTEI);
		columnasAgregadas.add(colTT);
		columnasAgregadas.add(colTN);
		columnasAgregadas.add(colTEN);
		columnasAgregadas.add(colTL);
		
		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);

		select = "\n" + miniTable.prepareTable(layout, null, null, false, null);
		
		//mes y año
		if(monthPeriod.getValue() != null && yearPeriod.getValue() != null){
			
			String queryAux = queryFrom + ", C_ORDER ordb " + query;
			query += 
				"\nand  TO_CHAR(ord.XX_ENTRANCEDATE, 'MM')="+ mes +
				"\nand  TO_CHAR(ord.XX_ENTRANCEDATE, 'YYYY')="+ año +
				
				//SITME
				"\nUNION" + select + "\n" + queryAux +
				"\nAND ord.C_ORDER_ID = ordb.REF_ORDER_ID " +
				"\nAND  TO_CHAR(ordb.XX_ENTRANCEDATE, 'MM')=" + mes +
				"\nAND  TO_CHAR(ordb.XX_ENTRANCEDATE, 'YYYY')=" +año;
		}
		else if (monthPeriod.getValue() != null ){
		
			String queryAux = queryFrom + ", C_ORDER ordb " + query;
			query += 
				"\nand  TO_CHAR(ord.XX_ENTRANCEDATE, 'MM')="+ mes +
				
				//SITME
				"\nUNION" + select + "\n" + queryAux +
				"\nAND ord.C_ORDER_ID = ordb.REF_ORDER_ID " +
				"\nAND  TO_CHAR(ordb.XX_ENTRANCEDATE, 'MM')=" + mes;
		}				
		else if (yearPeriod.getValue() != null ){			
			//query += "\nand  TO_CHAR(ord.XX_ENTRANCEDATE, 'YYYY')="+ año ;
			String queryAux = queryFrom + ", C_ORDER ordb " + query;
			query += 
				"\nand  TO_CHAR(ord.XX_ENTRANCEDATE, 'YYYY')="+ año +
				
				//SITME
				"\nUNION" + select + "\n" + queryAux +
				"\nAND ord.C_ORDER_ID = ordb.REF_ORDER_ID " +
				"\nAND  TO_CHAR(ordb.XX_ENTRANCEDATE, 'YYYY')=" +año;
		}
	
		String sql = select + queryFrom + query;
		
		//System.out.println("TABLA: "+sql);
		
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
	

	//Cabeceras de las columnas de la tabla 
	private ColumnInfo colOrd = new ColumnInfo(Msg.translate(ctx, "C_ORDER_ID"), 
			"\nord.documentno",String.class);
	private ColumnInfo colStatus = new ColumnInfo(Msg.translate(ctx, "XX_OrderStatus"), 
			"\n(CASE WHEN ord.xx_orderstatus ='AN' THEN 'ANULADA' " +
			"\nWHEN ord.xx_orderstatus ='AP' THEN 'APROBADA' " +
			"\nWHEN ord.xx_orderstatus ='CH' THEN 'CHEQUEADA' " +
			"\nWHEN ord.xx_orderstatus ='EA' THEN 'EN ADUANA' " +
			"\nWHEN ord.xx_orderstatus ='EAC' THEN 'ENTREGADA AL AGENTE DE CARGA' " +
			"\nWHEN ord.xx_orderstatus ='EP' THEN 'EN PRODUCCIÓN' " +
			"\nWHEN ord.xx_orderstatus ='EPN' THEN 'EN PROCESO DE NACIONALIZACIÓN' " +
			"\nWHEN ord.xx_orderstatus ='ETI' THEN 'EN TRÁNSITO INTERNACIONAL' " +
			"\nWHEN ord.xx_orderstatus ='ETN' THEN 'EN TRÁNSITO NACIONAL' " +
			"\nWHEN ord.xx_orderstatus ='LCD' THEN 'LLEGADA A CD' " +
			"\nWHEN ord.xx_orderstatus ='LVE' THEN 'LLEGADA A VENEZUELA' " +
			"\nWHEN ord.xx_orderstatus ='PEN' THEN 'PENDIENTE' " +
			"\nWHEN ord.xx_orderstatus ='PRO' THEN 'PROFORMA' " +
			"\nWHEN ord.xx_orderstatus ='RE' THEN 'RECIBIDA' " +
			"\nWHEN ord.xx_orderstatus ='SIT' THEN 'SITME' " +
			"\nELSE '-' END) ", String.class);
	
			//"\nord.xx_orderstatus",String.class);	
	private ColumnInfo colArrivalDate = new ColumnInfo(Msg.translate(ctx, "XX_ARRIVALDATE"), 
			"\nto_char(ord.XX_ARRIVALDATE,'DD/MM/YYYY')",String.class);
	private ColumnInfo colDispatchDate = new ColumnInfo(Msg.translate(ctx, "XX_DISPATCHDATE"), 
			"\nto_char(ord.XX_DISPATCHDATE,'DD/MM/YYYY')",String.class);	
	private ColumnInfo colCatg = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Category_ID"), 
			"\ncat.Name",String.class);
	private ColumnInfo colDept = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Department_ID"),
			"\ndep.NAME",String.class);	
	private ColumnInfo colBPar = new ColumnInfo(Msg.translate(ctx, "C_BPARTNER_ID"), 
			"\npart.NAME",String.class);
	private ColumnInfo colBuy = new ColumnInfo(Msg.translate(ctx, "XX_Buyer"), 
			"\ncomp.NAME",String.class);
	private ColumnInfo colEntranceDate = new ColumnInfo(Msg.translate(ctx, "XX_ENTRANCEDATE"), 
			"\nto_char(ord.XX_ENTRANCEDATE,'DD/MM/YYYY')",String.class);
	private ColumnInfo colCarAgentDelivRealDate = new ColumnInfo(Msg.translate(ctx, "XX_CARAGENTDELIVREALDATE"), 
			"\nto_char(ord.XX_CARAGENTDELIVREALDATE,'DD/MM/YYYY')",String.class);
	private ColumnInfo colShippingRealDate = new ColumnInfo(Msg.translate(ctx, "XX_ShippingRealDate"), 
			"\nto_char(bo.XX_SHIPPINGREALDATE,'DD/MM/YYYY')",String.class);
	private ColumnInfo colVzlaArrivalRealDate = new ColumnInfo(Msg.translate(ctx, "XX_VzlaArrivalrealDate"), 
			"\nto_char(bo.XX_VZLAARRIVALREALDATE,'DD/MM/YYYY')",String.class);
	private ColumnInfo colCustomRigthCancelDate = new ColumnInfo(Msg.translate(ctx, "XX_CustomRigthrealDate"), 
			"\nto_char(bo.XX_CUSTOMRIGHTSCANCELDATE,'DD/MM/YYYY')",String.class);	
	private ColumnInfo colRealDispatchDate = new ColumnInfo(Msg.translate(ctx, "XX_RealDispatchDate"), 
			"\nto_char(bo.XX_REALDISPATCHDATE,'DD/MM/YYYY')",String.class);	
	private ColumnInfo colCDArrivalRealDate = new ColumnInfo(Msg.translate(ctx, "XX_CDArrivalRealDate"), 
			"\nto_char(bo.XX_CDARRIVALREALDATE,'DD/MM/YYYY')",String.class);

	/**Indicadores* de tiempo*/	
	private ColumnInfo colTEP = new ColumnInfo(Msg.translate(ctx, "XX_VendorDeliveryTime"), 
			"\n(case when ord.XX_DISPATCHDATE is null then 0 " +
			"\nwhen ord.XX_EPDATE is null then 0 else (ord.XX_DISPATCHDATE-ord.XX_EPDATE)end)",String.class);
	
	private ColumnInfo colTEI = new ColumnInfo(Msg.translate(ctx, "XX_InternationalDeliveryTime"), 
			"\n(case when bo.XX_SHIPPINGREALDATE is null then 0 " +
			"\nwhen ord.XX_CARAGENTDELIVREALDATE is null then 0 else " +
			"\n(bo.XX_SHIPPINGREALDATE - ord.XX_CARAGENTDELIVREALDATE)end)",String.class);
	
	private ColumnInfo colTT = new ColumnInfo(Msg.translate(ctx, "XX_TransitTime "), 
			"\n(case when bo.XX_VZLAARRIVALREALDATE is null then 0 " +
			"\nwhen bo.XX_SHIPPINGREALDATE is null then 0 " +
			"\nelse (bo.XX_VZLAARRIVALREALDATE -bo.XX_SHIPPINGREALDATE)end)",String.class);
	
	private ColumnInfo colTN = new ColumnInfo(Msg.translate(ctx, "XX_NationalizationTime_1"), 
			"\n(case when bo.XX_REALDISPATCHDATE is null then 0  " +
			"\nelse bo.XX_REALDISPATCHDATE -bo.XX_VZLAARRIVALREALDATE  end ) ",String.class);
	
	private ColumnInfo colTEN = new ColumnInfo(Msg.translate(ctx, "XX_NationalDeliveryTime"), 
			"\n(case when bo.XX_REALDISPATCHDATE is null then 0 " +
			"\nwhen bo.XX_CDARRIVALREALDATE is null then 0 " +
			"\nelse bo.XX_CDARRIVALREALDATE - bo.XX_REALDISPATCHDATE  end )",String.class);	
	
	private ColumnInfo colTL = new ColumnInfo(Msg.translate(ctx, "XX_ImportLogisticsTime"), 
			"\n((case when bo.XX_SHIPPINGREALDATE is null then 0 " +
			"\nwhen ord.XX_CARAGENTDELIVREALDATE is null then 0 " +
			"\nelse (bo.XX_SHIPPINGREALDATE - ord.XX_CARAGENTDELIVREALDATE)end)+ " +
			"\n(case when bo.XX_VZLAARRIVALREALDATE is null then 0 " +
			"\nwhen bo.XX_SHIPPINGREALDATE is null then 0 " +
			"\nelse (bo.XX_VZLAARRIVALREALDATE -bo.XX_SHIPPINGREALDATE)end)+ " +
			"\n(case when bo.XX_REALDISPATCHDATE is null then 0 " +
			"\nelse bo.XX_REALDISPATCHDATE -bo.XX_VZLAARRIVALREALDATE  end )+ " +
			"\n(case when bo.XX_REALDISPATCHDATE is null then 0 " +
			"\nwhen bo.XX_CDARRIVALREALDATE is null then 0 " +
			"\nelse bo.XX_CDARRIVALREALDATE - bo.XX_REALDISPATCHDATE  end))",String.class);
	
/*	
private int TiempoEntregaProveedor(){
	
	String sql = "";
	String sqlFrom = "";
	String sqlSelect = "";
	
	int result=0;
	int mes=0;
	int año=0;
	
	if (monthPeriod.getValue() != null ){
		mes = (Integer)monthPeriod.getValue(); 
	}
	if (yearPeriod.getValue() != null ){
		
		año = (Integer)yearPeriod.getValue();
	}		
	
	KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
	KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();		
	KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();		
	KeyNamePair bcomp= (KeyNamePair)comboComprador.getSelectedItem();	
	String status = (String)comboOPurchaseStatus.getSelectedItem();

	sqlSelect = "\nselect trunc(sum(case when ord.XX_DISPATCHDATE is null then 0 " +
		"\nwhen ord.XX_EPDATE is null then 0 else (ord.XX_DISPATCHDATE-ord.XX_EPDATE)end)/count(ord.c_order_id)) ";
		
	sqlFrom = "\nfrom XX_VLO_BOARDINGGUIDE bo,C_ORDER ord,C_BPARTNER part," +
		"\nC_BPARTNER comp,XX_VMR_Department dep,XX_VMR_CATEGORY cat";
	
	sql =
		"\nwhere bo.XX_VLO_BOARDINGGUIDE_ID(+)=ord.XX_VLO_BOARDINGGUIDE_ID" +
		"\nand ord.C_BPARTNER_ID=part.C_BPARTNER_ID AND ord.XX_USERBUYER_ID=comp.C_BPARTNER_ID" +
		"\nAND ord.XX_VMR_Department_ID = dep.XX_VMR_Department_ID" +
		"\nAND cat.XX_VMR_CATEGORY_ID = ord.XX_VMR_CATEGORY_ID" +
		"\nAND ord.XX_ORDERTYPE='"+ X_Ref_XX_OrderType.IMPORTADA.getValue() + "'";
	
	//Categoría	
	if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	

		if (catg.getKey()!= 99999999 )			
			sql += "\nAND cat.XX_VMR_Category_ID = " + catg.getKey();
	}		
	//Departamento			
	if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {
		
		if (dept.getKey()!= 99999999 ) 
			sql += "\nAND dep.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
		
	}
	
	//Proveedor
	if((Boolean)checkBPartner.getValue() == true || bpar != null && (bpar.getKey() == 99999999 || bpar.getKey() != 0)){
		
		if (bpar.getKey()!= 99999999) 
			sql += "\nAND part.C_BPARTNER_ID = " + bpar.getKey();				
	}
	
	//Comprador
	if ((Boolean)checkComprador.getValue()==true || bcomp != null && (bcomp.getKey() == 99999999 || bcomp.getKey()!= 0)){	
						
		if (bcomp.getKey()!= 99999999 )
			sql += "\nAND dep.XX_USERBUYER_ID = " + bcomp.getKey();
	}			
	//status
	if(status != null && (status != "" || status == Msg.getMsg(Env.getCtx(), "XX_AllStatus"))){

		if(status != Msg.getMsg(Env.getCtx(), "XX_AllStatus")){
			sql += "\nand ord.xx_orderstatus='"+ OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())+ "'" ;
		}
	}
		
	//mes y año
	if(monthPeriod.getValue() != null && yearPeriod.getValue() != null){
		
		String queryAux = sqlFrom + ", C_ORDER ordb " + sql;
		sql += 
			"\nand  TO_CHAR(ord.XX_ENTRANCEDATE, 'MM')="+ mes +
			"\nand  TO_CHAR(ord.XX_ENTRANCEDATE, 'YYYY')="+ año +
			
			//SITME
			"\nUNION" + sqlSelect + "\n" + queryAux +
			"\nAND ord.C_ORDER_ID = ordb.REF_ORDER_ID " +
			"\nAND  TO_CHAR(ordb.XX_ENTRANCEDATE, 'MM')=" + mes +
			"\nAND  TO_CHAR(ordb.XX_ENTRANCEDATE, 'YYYY')=" +año;
	}
	else if (monthPeriod.getValue() != null ){
	
		String queryAux = sqlFrom + ", C_ORDER ordb " + sql;
		sql += 
			"\nand  TO_CHAR(ord.XX_ENTRANCEDATE, 'MM')="+ mes +
			
			//SITME
			"\nUNION" + sqlSelect + "\n" + queryAux +
			"\nAND ord.C_ORDER_ID = ordb.REF_ORDER_ID " +
			"\nAND  TO_CHAR(ordb.XX_ENTRANCEDATE, 'MM')=" + mes;
	}				
	else if (yearPeriod.getValue() != null ){			
		
		String queryAux = sqlFrom + ", C_ORDER ordb " + sql;
		sql += 
			"\nand  TO_CHAR(ord.XX_ENTRANCEDATE, 'YYYY')="+ año +
			
			//SITME
			"\nUNION" + sqlSelect + "\n" + queryAux +
			"\nAND ord.C_ORDER_ID = ordb.REF_ORDER_ID " +
			"\nAND  TO_CHAR(ordb.XX_ENTRANCEDATE, 'YYYY')=" +año;
	}

	sql = sqlSelect + sqlFrom + sql;
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
		ps = DB.prepareStatement(sql, null);			
		rs = ps.executeQuery();	
		
		 while (rs.next())
		 {
			 result = result + (rs.getInt(1));
		 }
		 	
		 TEP = result;
		 
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
	
	return(result);
	
}*/
private int TiempoEntregaInternacional(){
	
	String sqlSelect = "";
	String sql = "";
	int result=0;
	int mes=0;
	int año=0;
	
	if (monthPeriod.getValue() != null ){
		mes = (Integer)monthPeriod.getValue(); 
	}
	if (yearPeriod.getValue() != null ){
		
		año = (Integer)yearPeriod.getValue();	;
		
	}		
	
	KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
	KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();		
	KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();		
	KeyNamePair bcomp= (KeyNamePair)comboComprador.getSelectedItem();	
	String status = (String)comboOPurchaseStatus.getSelectedItem();

	sqlSelect = "\nselect (case when ((sum(case when ord.XX_CARAGENTDELIVREALDATE is null then 0  " +
		"\nelse (bo.XX_SHIPPINGREALDATE - ord.XX_CARAGENTDELIVREALDATE) end )/count(ord.c_order_id))>0)" +		
		"\nand ((sum(case when ord.XX_CARAGENTDELIVREALDATE is null then 0 "+
		"\nelse (bo.XX_SHIPPINGREALDATE - ord.XX_CARAGENTDELIVREALDATE) end )/count(ord.c_order_id))<1)"+
		"\nthen 1 else trunc(sum(case when ord.XX_CARAGENTDELIVREALDATE is null then 0 "+
		"\nelse (bo.XX_SHIPPINGREALDATE - ord.XX_CARAGENTDELIVREALDATE) end )/count(ord.c_order_id))end )";
		
	fromG	= "\n FROM XX_VLO_BOARDINGGUIDE bo LEFT JOIN C_ORDER ord using (XX_VLO_BOARDINGGUIDE_ID) ";
	  
	whereG	= "\n WHERE ord.XX_ORDERTYPE='"+ X_Ref_XX_OrderType.IMPORTADA.getValue() + "' ";
	
	//Categoría	
	if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	

		if (catg.getKey()!= 99999999 )			
			whereG += "\nAND ord.XX_VMR_Category_ID = " + catg.getKey();
	}		
	//Departamento			
	if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {
		
		if (dept.getKey()!= 99999999 ) 
			whereG += "\nAND ord.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
		
	}
	
	//Proveedor
	if((Boolean)checkBPartner.getValue() == true || bpar != null && (bpar.getKey() == 99999999 || bpar.getKey() != 0)){
		
		if (bpar.getKey()!= 99999999) 
			whereG += "\nAND ord.C_BPARTNER_ID = " + bpar.getKey();				
	}
	
	//Comprador
	if ((Boolean)checkComprador.getValue()==true || bcomp != null && (bcomp.getKey() == 99999999 || bcomp.getKey()!= 0)){	
						
		if (bcomp.getKey()!= 99999999 )
			whereG += "\nAND ord.XX_USERBUYER_ID = " + bcomp.getKey();
	}	
	
	//status
	if(status != null && (status != "" || status == Msg.getMsg(Env.getCtx(), "XX_AllStatus"))){

		if(status != Msg.getMsg(Env.getCtx(), "XX_AllStatus")){
			whereG += "\nand ord.xx_orderstatus='"+ OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())+ "'" ;
		}
	}	
	
	//mes y año
	if(monthPeriod.getValue() != null && yearPeriod.getValue() != null){
		fromG += "\n left join C_ORDER ordb on ( ord.C_ORDER_ID = ordb.REF_ORDER_ID )";
		whereG += "\n  and (( TO_CHAR(ord.XX_ENTRANCEDATE, 'MM')="+ mes + " and  TO_CHAR(ord.XX_ENTRANCEDATE, 'YYYY')="+ año + " ) " +
				"    OR  ( TO_CHAR(ordb.XX_ENTRANCEDATE, 'MM')="+ mes + " AND  TO_CHAR(ordb.XX_ENTRANCEDATE, 'YYYY')="+ año + ") )";
	}
	else if (monthPeriod.getValue() != null ){
		fromG += "\n left join C_ORDER ordb on ( ord.C_ORDER_ID = ordb.REF_ORDER_ID )";
		whereG += "\n  and (( TO_CHAR(ord.XX_ENTRANCEDATE, 'MM')="+ mes + " ) " +
				"    OR  ( TO_CHAR(ordb.XX_ENTRANCEDATE, 'MM')="+ mes + " ) )";
	
	}				
	else if (yearPeriod.getValue() != null ){			
		fromG += "\n left join C_ORDER ordb on ( ord.C_ORDER_ID = ordb.REF_ORDER_ID )";
		whereG += "\n  and (( TO_CHAR(ord.XX_ENTRANCEDATE, 'YYYY')="+ año + " ) " +
				"    OR  ( TO_CHAR(ordb.XX_ENTRANCEDATE, 'YYYY')="+ año + ") )";
	}

	sql = sqlSelect + fromG + whereG;
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
		ps = DB.prepareStatement(sql, null);			
		rs = ps.executeQuery();	
		
		 while (rs.next())  {
			 result = rs.getInt(1);
		 }
		 TEI = result;
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
	
	return(result);
	
}
private int TiempoEnTránsito(){
	
	String sql = "";
	String sqlSelect = "";
	
	int result=0;

	sqlSelect = "\nselect (case when ((sum(bo.XX_VZLAARRIVALREALDATE -bo.XX_SHIPPINGREALDATE)/count(ord.c_order_id))>0 " +
		        "\nand (sum(bo.XX_VZLAARRIVALREALDATE -bo.XX_SHIPPINGREALDATE)/count(ord.c_order_id)) < 1 ) " +
		        "\nthen 1 else trunc(sum(bo.XX_VZLAARRIVALREALDATE -bo.XX_SHIPPINGREALDATE)/count(ord.c_order_id)) end) ";
		
	sql = sqlSelect + fromG + whereG;
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
		ps = DB.prepareStatement(sql, null);			
		rs = ps.executeQuery();	
		
		 while (rs.next()) {
			 result = rs.getInt(1);
		 }
		 TET = result;
		 
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
	
	return(result);
	
}
private int TiempoNacionalizacion(){
	
	String sqlSelect = "";
	String sql = "";
	
	int result=0;
	sqlSelect = "\nselect (case when ((sum(case when bo.XX_REALDISPATCHDATE is null then 0 " +		
				"\nelse bo.XX_REALDISPATCHDATE -bo.XX_VZLAARRIVALREALDATE  end )/count(ord.c_order_id))>0) "+
				"\nand ((sum(case when bo.XX_REALDISPATCHDATE is null then 0 "+
				"\nelse bo.XX_REALDISPATCHDATE -bo.XX_VZLAARRIVALREALDATE  end )/count(ord.c_order_id))<1) "+
				"\nthen 1 else trunc(sum(case when bo.XX_REALDISPATCHDATE is null then 0 "+
				"\nelse bo.XX_REALDISPATCHDATE -bo.XX_VZLAARRIVALREALDATE  end )/count(ord.c_order_id))end ) ";
		
	
	sql = sqlSelect + fromG + whereG;
	//System.out.println("TNAC: " +sql);
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
		ps = DB.prepareStatement(sql, null);			
		rs = ps.executeQuery();	
		
		 while (rs.next())   {
			 result = rs.getInt(1);
		 }
		 
		 TNAC = result;
		 		
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
	
	return(result);
	
}
private int TiempoEntregaNacional(){
	
	String sql = "";
	String sqlSelect = "";
	
	int result=0;
	
	sqlSelect = "\nselect (case when ((sum(case when bo.XX_REALDISPATCHDATE is null then 0 " +
		"\nwhen bo.XX_CDARRIVALREALDATE is null then 0 "+
		"\nelse bo.XX_CDARRIVALREALDATE - bo.XX_REALDISPATCHDATE  end )/count(ord.c_order_id))>0) "+
		"\nand ((sum(case when bo.XX_CUSTOMRIGHTSCANCELDATE is null then 0 "+
		"\nwhen bo.XX_CDARRIVALREALDATE is null then 0 "+
		"\nelse bo.XX_CDARRIVALREALDATE - bo.XX_REALDISPATCHDATE  end )/count(ord.c_order_id))<1) "+
		"\nthen 1 else trunc(sum(case when bo.XX_REALDISPATCHDATE is null then 0 "+
		"\nwhen bo.XX_CDARRIVALREALDATE is null then 0 "+	
		"\nelse bo.XX_CDARRIVALREALDATE - bo.XX_REALDISPATCHDATE  end )/count(ord.c_order_id)) end) ";		
		
	sql = sqlSelect + fromG + whereG;
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
		ps = DB.prepareStatement(sql, null);			
		rs = ps.executeQuery();	
		
		 while (rs.next())  {
			 result = rs.getInt(1);
		 }
		 TEN = result;
		 		
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
	
	return(result);
	
}
private int TiempoLogísticaImportaciones(){
	
	int result=0;
	
	//result = TEP + TEI + TEN + TET+ TNAC;
	result = TEI + TEN + TET+ TNAC;
	
	return(result);
}
}
