package compiere.model.cds.forms.indicator;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.border.TitledBorder;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.framework.Lookup;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.MLookupFactory;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.X_Ref_XX_Ref_TypeDelivery;

/**
 *  @author Gabrielle Huchet
 *  @version 
 */

/** Clase que extiende al indicador genérico y 
 * representa al indicador de desempeño del proveedor
 * Agrega campos adicionales como Rango de Fechas, 
 * Número de Orden de Compra, Tipo de Proveedor y Tipo de Entrega
 * */

public class XX_VendorPerformance extends XX_Indicator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Campos necesarios para este indicador 
	private CLabel labelPurchaseOrder = new CLabel(Msg.translate(Env.getCtx(), "C_Order_ID"));
	private VLookup lookupPurchaseOrder = null;	
	
	private CLabel labelVendorType = new CLabel(Msg.translate(Env.getCtx(), "XX_VendorType_ID"));
	private VComboBox comboVendorType = new VComboBox();
	private CCheckBox checkVendorType = new CCheckBox();

	private CLabel labelDeliveryType = new CLabel(Msg.translate(Env.getCtx(), "XX_VLO_TypeDelivery"));
	private VComboBox comboDeliveryType = new VComboBox();
	private CCheckBox checkDeliveryType = new CCheckBox();

	
	private CLabel dateFromLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_EntranceDate")+
			" "+Msg.getMsg(Env.getCtx(), "From"));
	private VDate vDateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	
	private CLabel dateToLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_To"));

	private VDate vDateTo = new VDate(Msg.translate(Env.getCtx(), "DateTo"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateTo"));
	
	private CLabel labelDetailed = new CLabel(Msg.translate(Env.getCtx(), "XX_DisplayEvaluationCriteria"));
	private CCheckBox checkDetailed = new CCheckBox();
	private Vector<String> deliveryType_name = new Vector<String>();
	private Vector<String> abc = new Vector<String>();
	private List<String> evaluactionCriteria_name = new ArrayList<String>();
	private List<Integer> evaluactionCriteria_ID = new ArrayList<Integer>();
	
	private Integer TOTAL_VENDOR_RATING_ID = Env.getCtx().getContextAsInt("#XX_L_EC_TOTALSCOREOC_ID");
	private Integer PREVIOUS_TOTAL_VENDOR_RATING_ID = Env.getCtx().getContextAsInt("#XX_L_EC_SCOREACCUMUPREVI_ID");
	
	private TitledBorder xBorder = new TitledBorder("Desempeño del Proveedor");

	
	@Override
	protected void agregarParametros() {
		
		//Inicializar lookup de orden de compra
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		try {
		Lookup l = MLookupFactory.get(Env.getCtx(), m_WindowNo, Env
				.getCtx().getContextAsInt("#XX_L_C_ORDERCOLUMN_ID"),
				DisplayTypeConstants.Search);
		lookupPurchaseOrder = new VLookup("C_Order_ID", false, false, true, l);
		lookupPurchaseOrder.setVerifyInputWhenFocusTarget(false);
		}catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
		
		//Agregar Orden de Compra, Tipo de Proveedor y Tipo de Entrega, 
		//Periodos y Opción de Consulta Detallada}
		
		agregarParametro(labelPurchaseOrder);
		agregarParametro(lookupPurchaseOrder,2);
		agregarParametro(labelVendorType);
		agregarParametro(comboVendorType);
		agregarParametro(checkVendorType);	
		agregarParametro(labelDeliveryType);
		agregarParametro(comboDeliveryType);
		agregarParametro(checkDeliveryType);
		agregarParametro(dateFromLabel);
		agregarParametro(vDateFrom);
		agregarParametro(dateToLabel, 2, 3, 1);
		agregarParametro(vDateTo, 2, 4, 1);
		agregarParametro(labelDetailed, 2, 7, 1);
		agregarParametro(checkDetailed, 2, 8, 1);
	
	}

	@Override
	protected void ocultarParametrosDefecto() {

		//Estos campos no son necesarios para este indicador
		ocultarParametro(PRODUCTO);
		ocultarParametro(LINEA);
		ocultarParametro(SECCION);
		ocultarParametro(PERIODO);
	}
	
	@Override
	protected void personalizar() {

		//Status Bar: texto a mostrar
		statusBar.setStatusLine("Resultados Mostrados");
		dataPane.setPreferredSize(new Dimension(720, 600));
		dataPane.setBorder(xBorder);
		
		//Cargar los datos de los campos que se agregaron en este indicador
		loadVendorType();
		loadDeliveryType();
		
		//Cargar los criterios de evaluacion existentes
		loadEvaluationCriteria();
		
		//Configurar los campos agregados
		configurar(comboVendorType, true);
		configurar(checkVendorType, true, false);
		configurar(checkDeliveryType, true, false);
		configurar(checkDetailed, true, true);
		comboDeliveryType.setEnabled(true);		
		comboDeliveryType.setEditable(true);
		comboDeliveryType.setSelectedIndex(0);		
		lookupPurchaseOrder.setValue(null);
		lookupPurchaseOrder.setEnabled(true);
		
		//Este indicador exige que los departamentos dependan de la categoria
		configurar(comboDepartment, false);
		configurar(checkDepartment, false);
		
		//Comenzando
		configurar(bPrint, false);
		configurar(bFile, false);
		
		//Agregar listeners a los nuevos campos
		checkVendorType.addActionListener(this);
		checkVendorType.addActionListener(this);
		checkDeliveryType.addActionListener(this);
		checkDetailed.addActionListener(this);
		comboVendorType.addActionListener(this);
		comboDeliveryType.addActionListener(this);

	}

	//Cargar Tipos de Proveedor
	private void loadVendorType() {

		KeyNamePair loadKNP;
		String sql = "";						
		comboVendorType.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboVendorType.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.getMsg(Env.getCtx(), "XX_AllVendorType"));
		comboVendorType.addItem(loadKNP);	
		sql = " SELECT vt.XX_VCN_VendorType_ID, vt.NAME " +
				" FROM  XX_VCN_VendorType vt WHERE vt.ISACTIVE = 'Y' AND vt.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID(); 		
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboVendorType.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
		
	}
	
	//Cargar Tipos de Entrega
	private void loadDeliveryType() {
		
		comboDeliveryType.removeAllItems();
		deliveryType_name.removeAllElements();
		// LLenar los combo de listas
		comboDeliveryType.addItem("");
		deliveryType_name.add("");
		comboDeliveryType.addItem(Msg.getMsg(Env.getCtx(), "XX_AllDeliveryType"));
		deliveryType_name.add(Msg.getMsg(Env.getCtx(), "XX_AllDeliveryType"));
		for (X_Ref_XX_Ref_TypeDelivery v : X_Ref_XX_Ref_TypeDelivery.values()) {
			comboDeliveryType.addItem(v.getValue() + "-" + v);
			deliveryType_name.add(v.getValue());
		}
		
		comboDeliveryType.setEnabled(true);		
		comboDeliveryType.setEditable(true);
		comboDeliveryType.setSelectedIndex(0);
	}
	
	//Cargar Criterios
	private void loadEvaluationCriteria() {
		
		abc.add("a");
		abc.add("b");
		abc.add("c");
		abc.add("d");
		abc.add("e");
		abc.add("f");
		abc.add("g");
		abc.add("h");
		abc.add("i");
		abc.add("j");
		abc.add("k");
		
		String sql = "";						
		sql = " SELECT ev.Name, ev.XX_VCN_EvaluationCriteria_ID " +
				" FROM  XX_VCN_EvaluationCriteria ev WHERE ev.IsActive = 'Y' AND ev.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID() +
				" ORDER BY XX_ITEMSEQUENCE "; 		
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				evaluactionCriteria_name.add(rs.getString(1));
				evaluactionCriteria_ID.add(rs.getInt(2));
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
		//ordernarCriterios();
	}


	private void ordernarCriterios() {
		for (int i = 0; i < evaluactionCriteria_ID.size(); i++) {
			if(evaluactionCriteria_ID.get(i).equals(TOTAL_VENDOR_RATING_ID)){
				String temp = evaluactionCriteria_name.get(0);
				Integer temp2 = evaluactionCriteria_ID.get(0);
				evaluactionCriteria_name.set(0,evaluactionCriteria_name.get(i));
				evaluactionCriteria_ID.set(0,evaluactionCriteria_ID.get(i));
				evaluactionCriteria_name.set(i,temp);
				evaluactionCriteria_ID.set(i,temp2);
			}
			else if(evaluactionCriteria_ID.get(i).equals(PREVIOUS_TOTAL_VENDOR_RATING_ID)){
				String temp = evaluactionCriteria_name.get(1);
				Integer temp2 = evaluactionCriteria_ID.get(1);
				evaluactionCriteria_name.set(1,evaluactionCriteria_name.get(i));
				evaluactionCriteria_ID.set(1,evaluactionCriteria_ID.get(i));
				evaluactionCriteria_name.set(i,temp);
				evaluactionCriteria_ID.set(i,temp2);
			}
		}
		
	}

	protected void limpiarFiltro () {
		
		//Limpia los campos del filtro padre
		super.limpiarFiltro();		
		
		//Borrar aquellos campos que se agregaron en esta forma
		configurar(comboVendorType, true);
		configurar(checkVendorType, true, false);
		comboDeliveryType.setEnabled(true);		
		comboDeliveryType.setEditable(true);
		comboDeliveryType.setSelectedIndex(0);		
		configurar(checkDeliveryType, true, false);
		configurar(checkDetailed, true, true);
		vDateFrom.setValue(null);
		vDateTo.setValue(null);
		lookupPurchaseOrder.setValue(null);
		lookupPurchaseOrder.setEnabled(true);
		
		
	}
	
	/** En este método agrego acciones a las acciones predeterminadas */
	public void actionPerformed(ActionEvent e) {
		
		//Ejecuta las acciones por defecto
		super.actionPerformed(e);
		

		//Desactivar los listeners mientras realizo las modificaciones
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
		//Se marcó el check de todos los tipos de proveedor
		else if(e.getSource() == checkVendorType){
			
			if((Boolean) checkVendorType.getValue()){
				comboVendorType.setValue(99999999);		
				comboVendorType.setEnabled(false);
			}else{					
				configurar(comboVendorType, true);								
			}
		}
		//Se marcó el check de todos los tipos de entrega
		else if(e.getSource() == checkDeliveryType){
			
			if((Boolean)checkDeliveryType.getValue()){
				comboDeliveryType.setSelectedIndex(1);			
				comboDeliveryType.setEnabled(false);
			}else{					
				comboDeliveryType.setEnabled(true);		
				comboDeliveryType.setEditable(true);
				comboDeliveryType.setSelectedIndex(0);							
			}
		}
			
		//Activar listeners
		activarActionListeners();
	}
	
	/** Llena la tabla a mostrar con los datos obtenidos del query*/
	protected void llenarTabla() {
		if (vDateFrom.getValue() == null || vDateTo.getValue() == null) {				
			String msg = Msg.getMsg(Env.getCtx(), "XX_DateEmpty");
			ADialog.error(m_WindowNo, m_frame, msg);
		}else {
			m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			//Si no se ha cargado el header previamente
			miniTable.setRowCount(0);
			miniTable = new MiniTablePreparator();

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
			} catch (Exception e) {
				e.getMessage();
			}
			
			m_frame.setCursor(Cursor.getDefaultCursor());		
		}
	}

	/** Se determina el query de acuerdo a los parámetros ingresados*/
	public void calcularQuery () {

		Timestamp dateFrom = (Timestamp)vDateFrom.getValue(); 
		Timestamp dateTo = (Timestamp)vDateTo.getValue();	
		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair bPar = (KeyNamePair)comboBPartner.getSelectedItem();		
		KeyNamePair vendorType= (KeyNamePair)comboVendorType.getSelectedItem();
		String deliveryType= (String)comboDeliveryType.getSelectedItem();
		Integer order= (Integer)lookupPurchaseOrder.getValue();	
		
		String whereOrd = "";
		String whereFac = "";
		String whereChe = "";
		String fromOrd = "";
		String fromFac = "";
		String fromChe = "";

		String with = 
			" WITH " +
			"\nCompras_Ordenadas AS " +
			"\n(SELECT lrp.C_Order_ID orden, SUM(lrp.PriceActual*lrp.Qty) Ordenadas_Bs, " +
			"\nSUM(lrp.Qty) Ordenadas_Piezas " +
			"\nFROM XX_VMR_PO_LineRefProv lrp " +
			"\nJOIN C_Order o ON (o.C_Order_ID = lrp.C_Order_ID) ";
			whereOrd = "\nWHERE o.ISSOTRX = 'N' AND lrp.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID() +
					"\nAND TRUNC(o.XX_EntranceDate) >= "+DB.TO_DATE(dateFrom, true)+
					"\nAND TRUNC(o.XX_EntranceDate) <= "+DB.TO_DATE(dateTo, true)+" ";
			whereFac = "\nWHERE i.ISSOTRX = 'N' AND i.DocStatus = 'CO' AND i.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
					"\nAND TRUNC(o.XX_EntranceDate) >= "+DB.TO_DATE(dateFrom, true)+
					"\nAND TRUNC(o.XX_EntranceDate) <= "+DB.TO_DATE(dateTo, true)+" ";
			whereChe = "\nWHERE io.ISSOTRX = 'N' AND io.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
					"\nAND TRUNC(o.XX_EntranceDate) >= "+DB.TO_DATE(dateFrom, true)+
					"\nAND TRUNC(o.XX_EntranceDate) <= "+DB.TO_DATE(dateTo, true)+" " +
					"\nAND XX_ORDERSTATUS = 'CH' ";
			//Categoría
			if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
	
				fromOrd += "\nJOIN XX_VMR_Category c ON (c.XX_VMR_Category_ID = o.XX_VMR_Category_ID) ";
				fromFac += "\nJOIN XX_VMR_Category c ON (c.XX_VMR_Category_ID = o.XX_VMR_Category_ID) ";
				fromChe += "\nJOIN XX_VMR_Category c ON (c.XX_VMR_Category_ID = o.XX_VMR_Category_ID) ";
				if (catg.getKey()!= 99999999 ) {
					whereOrd += "\nAND c.XX_VMR_Category_ID = " + catg.getKey();
					whereFac += "\nAND c.XX_VMR_Category_ID = " + catg.getKey();
					whereChe += "\nAND c.XX_VMR_Category_ID = " + catg.getKey();
				}
			}	
			//Departamento			
			if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {
	
				fromOrd += "\nINNER JOIN XX_VMR_Department d ON (d.XX_VMR_Department_ID = o.XX_VMR_Department_ID) "; 
				fromFac += "\nINNER JOIN XX_VMR_Department d ON (d.XX_VMR_Department_ID = o.XX_VMR_Department_ID) "; 
				fromChe += "\nINNER JOIN XX_VMR_Department d ON (d.XX_VMR_Department_ID = o.XX_VMR_Department_ID) "; 
				if (dept.getKey()!= 99999999 ) {
					whereOrd += "\nAND d.XX_VMR_Department_ID = " + dept.getKey();
					whereFac += "\nAND d.XX_VMR_Department_ID = " + dept.getKey();
					whereChe += "\nAND d.XX_VMR_Department_ID = " + dept.getKey();
				}
					
				
			}
			//Proveedor
			if(((Boolean)checkBPartner.getValue() == true || bPar != null && (bPar.getKey() == 99999999 || bPar.getKey() != 0))
				|| ((Boolean)checkVendorType.getValue() == true || vendorType != null && (vendorType.getKey() == 99999999 || vendorType.getKey() != 0))){
				fromOrd += "\nINNER JOIN C_BPartner bp ON (bp.C_BPartner_ID = o.C_BPartner_ID) ";
				fromFac += "\nINNER JOIN C_BPartner bp ON (bp.C_BPartner_ID = o.C_BPartner_ID) ";
				fromChe += "\nINNER JOIN C_BPartner bp ON (bp.C_BPartner_ID = o.C_BPartner_ID) ";
	
			}
			if((Boolean)checkBPartner.getValue() == true || bPar != null && (bPar.getKey() == 99999999 || bPar.getKey() != 0)){		
				if (bPar.getKey()!= 99999999) {
					whereOrd += "\nAND bp.C_BPartner_ID = " + bPar.getKey();	
					whereFac += "\nAND bp.C_BPartner_ID = " + bPar.getKey();	
					whereChe += "\nAND bp.C_BPartner_ID = " + bPar.getKey();	
				}
			}
			//Tipo de Entrega
			if((Boolean)checkDeliveryType.getValue() == true || deliveryType != null && (deliveryType == Msg.getMsg(Env.getCtx(), "XX_AllDeliveryType") || deliveryType != "")){
				if ( deliveryType_name.elementAt(comboDeliveryType.getSelectedIndex())!= Msg.getMsg(Env.getCtx(), "XX_AllDeliveryType")) {
	
					whereOrd += "\nAND o.XX_VLO_TypeDelivery  = " +
						"'" + deliveryType_name.elementAt(comboDeliveryType.getSelectedIndex())+ "'";
					whereFac += "\nAND o.XX_VLO_TypeDelivery  = " +
					"'" + deliveryType_name.elementAt(comboDeliveryType.getSelectedIndex())+ "'";
					whereChe += "\nAND o.XX_VLO_TypeDelivery  = " +
					"'" + deliveryType_name.elementAt(comboDeliveryType.getSelectedIndex())+ "'";
				}
			}
			//Orden de Compra
			if(order != null ){
				whereOrd += "\nAND o.C_Order_ID= " +order;
				whereFac += "\nAND o.C_Order_ID= " +order;
				whereChe += "\nAND o.C_Order_ID= " +order;
			}
			with +=fromOrd + whereOrd +
			"\nGROUP BY lrp.C_Order_ID), \n" +
			"\nCompras_Facturadas AS " +
			"\n(SELECT i.C_Order_ID orden, SUM(il.PriceActual*il.QtyInvoiced) Facturadas_Bs, " +
			"\nSUM(il.QtyInvoiced) Facturadas_Piezas " +
			"\nFROM C_InvoiceLine il " +
			"\nJOIN  C_Invoice i ON (il.C_Invoice_ID = i.C_Invoice_ID) " +
			"\nJOIN C_Order o ON (o.C_Order_ID = i.C_Order_ID) ";
			with += fromFac + whereFac +
			"\nGROUP BY i.C_Order_ID), \n " +
			"\nCompras_Chequeadas AS " +
			"\n(SELECT io.C_Order_ID orden, SUM(iol.PickedQty) Chequeadas_Piezas " +
			"\nFROM M_InOutLine iol " +
			"\nJOIN M_InOut io ON (iol.M_InOut_ID = io.M_InOut_ID) "+
			"\nJOIN C_Order o ON (o.C_Order_ID = io.C_Order_ID) ";
			with += fromChe + whereChe +
			"\nGROUP BY io.C_Order_ID, XX_ORDERSTATUS), \n" +
			"" +
			"a AS( " +
	        "SELECT " +
	        "XX_C_Order_ID,";
			
		
			String casos = "";
	        for (int w = 0; w<evaluactionCriteria_ID.size(); w++)
	        {
	        	if(evaluactionCriteria_ID.size()==(w+1))
	        		casos += "case when vr.XX_VCN_EvaluationCriteria_ID = "+evaluactionCriteria_ID.get(w)+" then ROUND(vr.XX_Points, 2) else 0 end as "+abc.get(w)+" ";
	        	else
	        		casos += "case when vr.XX_VCN_EvaluationCriteria_ID = "+evaluactionCriteria_ID.get(w)+" then ROUND(vr.XX_Points, 2) else 0 end as "+abc.get(w)+",";
	        }
	        
	        with += casos;

	        with +="FROM XX_VCN_VendorRating vr ";
	        
	        String grupo = "group by XX_C_Order_ID, ";
	        
	        for (int w = 0; w<evaluactionCriteria_ID.size(); w++)
	        {
	        	if(evaluactionCriteria_ID.size()==(w+1))
	        		grupo += "case when vr.XX_VCN_EvaluationCriteria_ID = "+evaluactionCriteria_ID.get(w)+" then ROUND(vr.XX_Points, 2) else 0 end) ";
	        	else
	        		grupo += "case when vr.XX_VCN_EvaluationCriteria_ID = "+evaluactionCriteria_ID.get(w)+" then ROUND(vr.XX_Points, 2) else 0 end, ";
	        }
	        
	        with += grupo;
	  
		String select = "";
		String from = 
			"\nFROM C_Order o " +
			"\nLEFT OUTER JOIN  Compras_Ordenadas co ON (o.C_Order_ID = co.orden) " +
			"\nLEFT OUTER JOIN  Compras_Facturadas cf ON (o.C_Order_ID = cf.orden) " +
			"\nLEFT OUTER JOIN  Compras_Chequeadas cc ON (o.C_Order_ID = cc.orden) " +
			"\nLEFT OUTER JOIN  a a ON (a.XX_C_Order_ID = o.C_Order_ID) ";
		String where =
			"\n o.isSotrx = 'N' " +
			"\nAND o.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID() +
			"\nAND TRUNC(o.XX_EntranceDate) >= "+DB.TO_DATE(dateFrom, true)+
			"\nAND TRUNC(o.XX_EntranceDate) <= "+DB.TO_DATE(dateTo, true)+" ";
		String orderBy = "" , orderById = ""; 
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();
		
		//Categoría
		from += "\nINNER JOIN XX_VMR_Category c ON (c.XX_VMR_Category_ID = o.XX_VMR_Category_ID) ";
		columnasAgregadas.add(colCatg);
		orderById = "\nc.value||'-'||c.name";
		orderBy += "\n,c.value||'-'||c.name";
		if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {		
			if (catg.getKey()!= 99999999 ) 
				if (where.isEmpty())
					where += "\nc.XX_VMR_Category_ID = " + catg.getKey();
				else 
					where += "\nAND c.XX_VMR_Category_ID = " + catg.getKey();
		}	
		//Departamento
		from += "\nINNER JOIN XX_VMR_Department d ON (d.XX_VMR_Department_ID = o.XX_VMR_Department_ID) "; 
		columnasAgregadas.add(colDept);
		orderById = "\nd.value||'-'||d.name";
		orderBy += "\n,d.value||'-'||d.name";
		if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {
			if (dept.getKey()!= 99999999 ) 
				if (where.isEmpty())
					where += "\nd.XX_VMR_Department_ID = " + dept.getKey();
				else
					where += "\nAND d.XX_VMR_Department_ID = " + dept.getKey();
			
		}
		//Proveedor
		from += "\nINNER JOIN C_BPartner bp ON (bp.C_BPartner_ID = o.C_BPartner_ID) ";
		columnasAgregadas.add(colBPar);
		orderById = "\nbp.name";
		orderBy += "\n, bp.name";
		if(((Boolean)checkBPartner.getValue() == true || bPar != null && (bPar.getKey() == 99999999 || bPar.getKey() != 0))
			|| ((Boolean)checkVendorType.getValue() == true || vendorType != null && (vendorType.getKey() == 99999999 || vendorType.getKey() != 0))){

		}
		if((Boolean)checkBPartner.getValue() == true || bPar != null && (bPar.getKey() == 99999999 || bPar.getKey() != 0)){	
			if (bPar.getKey()!= 99999999) 
				if (where.isEmpty())
					where += "\nbp.C_BPartner_ID = " + bPar.getKey();
				else
					where += "\nAND bp.C_BPartner_ID = " + bPar.getKey();				
		}
		//Tipo de Proveedor
		from += "\nINNER JOIN  XX_VCN_VendorType vt  ON (bp.XX_VendorType_ID = vt.XX_VCN_VendorType_ID) ";
		columnasAgregadas.add(colVendorType);
		orderById = "\nvt.value||'-'||vt.name";
		orderBy += "\n,vt.value||'-'||vt.name";	
		if((Boolean)checkVendorType.getValue() == true || vendorType != null && (vendorType.getKey() == 99999999 || vendorType.getKey() != 0)){
			
			if (vendorType.getKey()!= 99999999) 
				if (where.isEmpty())
					where += "\nbp.XX_VendorType_ID = " + vendorType.getKey();
				else
					where += "\nAND bp.XX_VendorType_ID = " + vendorType.getKey();				
		}
		//Tipo de Entrega
		columnasAgregadas.add(colDeliveryType);
		orderById = "\no.XX_VLO_TypeDelivery";
		orderBy += "\n, o.XX_VLO_TypeDelivery";	
		if((Boolean)checkDeliveryType.getValue() == true || deliveryType != null && (deliveryType == Msg.getMsg(Env.getCtx(), "XX_AllDeliveryType") || deliveryType != "")){
					
			if ( deliveryType_name.elementAt(comboDeliveryType.getSelectedIndex())!= Msg.getMsg(Env.getCtx(), "XX_AllDeliveryType")) {
				if (where.isEmpty())
					where += "\no.XX_VLO_TypeDelivery = " +
					"'" + deliveryType_name.elementAt(comboDeliveryType.getSelectedIndex())+ "'";
				else
					where += "\nAND o.XX_VLO_TypeDelivery  = " +
					"'" + deliveryType_name.elementAt(comboDeliveryType.getSelectedIndex())+ "'";
			}
		}

		//Orden de Compra
		if(order != null ){
			if (where.isEmpty())
				where += "\no.C_Order_ID= " +order;
				
			else
				where += "\nAND o.C_Order_ID= " +order;
		}
		//Agregar columnas de Orden de Compra y Fecha de Entrada
		columnasAgregadas.add(colOrder);
		columnasAgregadas.add(colStatus);
		columnasAgregadas.add(colDate);

	
		// Agregar columnas de las cantidades	
		columnasAgregadas.add(colQntyA);
		columnasAgregadas.add(colQntyB);
		columnasAgregadas.add(colQntyC);
		columnasAgregadas.add(colQntyD);		
		columnasAgregadas.add(colQntyE);

		String selectCriteria = "";
		String groupBy = "";
		groupBy = "\nGroup By c.Value||'-'||c.Name, d.value||'-'||d.Name, bp.Name, vt.Name, XX_VLO_TypeDelivery, " +
				  "o.DocumentNo, XX_ORDERSTATUS, XX_EntranceDate, vt.value||'-'||vt.name, o.C_Order_ID, " +
				  "co.Ordenadas_Piezas, co.Ordenadas_Bs, cf.Facturadas_Piezas, cf.Facturadas_Bs, cc.Chequeadas_Piezas";
		
		if((Boolean)checkDetailed.getValue()){
						
			ColumnInfo colTemp = null;
		
	        for (int w = 0; w<evaluactionCriteria_ID.size(); w++)
	        {
	        	colTemp = new ColumnInfo(evaluactionCriteria_name.get(w),"sum(a."+abc.get(w)+")", Double.class);
				columnasAgregadas.add(colTemp);
	        }	
		}
		
		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);
		
		select = "\n" + miniTable.prepareTable(layout, null, null, false, null) + selectCriteria;
		
		String sql =  with + select + from ;
		if (!where.isEmpty()) {
			where = "\nWHERE  " + where;
			sql += where;
		}
		if (!orderById.isEmpty()) {
			orderBy = "\nOrder BY " + orderById + orderBy + "\n,o.XX_EntranceDate, o.C_Order_ID ";;
		}else {
			orderBy = "\nOrder BY " + "\no.XX_EntranceDate, o.C_Order_ID ";
		}
		sql += groupBy+ orderBy;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTable.loadTable(rs);
			miniTable.repaint();
		} catch (SQLException e) {			
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
	private ColumnInfo colCatg = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"), 
			"\nc.Value||'-'||c.Name", String.class);
	
	private ColumnInfo colDept = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),
			"\nd.value||'-'||d.Name", String.class);
	
	private ColumnInfo colOrder = new ColumnInfo(Msg.translate(Env.getCtx(), "C_Order_ID"),
			"\no.DocumentNo", String.class);
	
	private ColumnInfo colStatus = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ORDERSTATUS"),
			"\n(CASE WHEN XX_ORDERSTATUS = 'CH' THEN 'Chequeada' " +
			"    when XX_ORDERSTATUS = 'RE' THEN 'Recibida' END)", String.class);
	
	private ColumnInfo colDate = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_EntranceDate"), 
			"\nTO_CHAR(o.XX_EntranceDate,'DD/MM/YYYY')", String.class);

	private ColumnInfo colBPar = new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), 
			"\nbp.Name",String.class);
	
	private ColumnInfo colVendorType = new ColumnInfo("Tipo de Proveedor", 
			"\nvt.Name", String.class);

	private ColumnInfo colDeliveryType = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VLO_TypeDelivery"), 
			"\n(CASE WHEN o.XX_VLO_TypeDelivery='CD' THEN 'Centro de Distribución' " +
			"\nWHEN o.XX_VLO_TypeDelivery='PD' THEN 'Pre-Distribuida' " +
			"\nWHEN o.XX_VLO_TypeDelivery='DD' THEN 'Despacho Directo' " +
			"\nELSE '-' END)",String.class);
	
	private ColumnInfo colQntyA = new ColumnInfo( Msg.getMsg(Env.getCtx(), "XX_PurchaseOrdered") +
			 "-" + Msg.translate(Env.getCtx(), "Amount")+ " " +Msg.translate(Env.getCtx(), "XX_Pieces"), "\nco.Ordenadas_Piezas", Integer.class );
	
	private ColumnInfo colQntyB = new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_PurchaseOrdered") +
			"-" + Msg.getMsg(Env.getCtx(), "XX_TotalBs"), "\nco.Ordenadas_Bs", Double.class );
	
	private ColumnInfo colQntyC = new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_PurchaseInvoiced") + 
			 "-" + Msg.translate(Env.getCtx(), "Amount")+ " " +Msg.translate(Env.getCtx(), "XX_Pieces"), "\ncf.Facturadas_Piezas", Integer.class );
	
	private ColumnInfo colQntyD = new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_PurchaseInvoiced") +
			"-" + Msg.getMsg(Env.getCtx(), "XX_TotalBs"), "\ncf.Facturadas_Bs", Double.class );
	
	private ColumnInfo colQntyE = new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_PurchaseChekup") + 
			 "-" + Msg.translate(Env.getCtx(), "Amount")+ " " +Msg.translate(Env.getCtx(), "XX_Pieces"), "\ncc.Chequeadas_Piezas", Integer.class );

}
