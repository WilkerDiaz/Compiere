package compiere.model.cds.forms;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.border.TitledBorder;

import org.apache.ecs.xhtml.label;
import org.compiere.grid.ed.VComboBox;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.forms.indicator.XX_Indicator;

public class XX_ConsolidatedWeightedInventoryDay extends XX_Indicator{

	/**
	 *  @author Gabrielle Huchet
	 *  @version 
	 */
	private static final long serialVersionUID = 1L;

	//Campos extra necesarios para este indicador 
	
	//Colección
	private CLabel labelCollection = new CLabel(Msg.translate(ctx, "XX_VMR_Collection_ID"));
	private VComboBox comboCollection = new VComboBox();
	private CCheckBox checkCollection = new CCheckBox();

	//Paquete
	private CLabel labelPackage = new CLabel(Msg.translate(ctx, "XX_VMR_Package_ID"));
	private VComboBox comboPackage = new VComboBox();
	private CCheckBox checkPackage = new CCheckBox();
	
	//KeyNameInfos usadas 
	private Integer CD_DEVOLUCION  = Env.getCtx().getContextAsInt("#XX_L_LOCATORCDDEVOLUCION_ID");
	private Integer CD_MERMA  = Env.getCtx().getContextAsInt("#XX_L_LOCATORCDMERMA_ID");
	private Integer CD_POR_VALIDAR  = Env.getCtx().getContextAsInt("#XX_L_TOVALIDATELOCATOR_ID");
	private Integer CD_CHEQUEADO = Env.getCtx().getContextAsInt("#XX_L_LOCATORCDCHEQUEADO_ID");
	
	// Tabla temporalpara calcular el ponderado de dias de inventario de los productos 
	protected MiniTablePreparator miniTable2 = new MiniTablePreparator();
	
	
	private TitledBorder xBorder = new TitledBorder("Ponderado de Días de Inventario Consolidado");
	
	private final static int  PRODUCT = 0, 
	LOCATOR = 1, TOTALPIECES = 2, WEIGHTED = 3, STATUS = 4;
	
	@Override
	protected void agregarParametros() {
		// TODO Auto-generated method stub
		agregarParametro(labelCollection);
		agregarParametro(comboCollection);
		agregarParametro(checkCollection);	
		agregarParametro(labelPackage);
		agregarParametro(comboPackage);
		agregarParametro(checkPackage);	
	}

	@Override
	protected void ocultarParametrosDefecto() {

		//Estos campos no son necesarios para este indicador
		ocultarParametro(PRODUCTO);
		ocultarParametro(LINEA);
		ocultarParametro(SECCION);
		ocultarParametro(SOCIODENEGOCIO);
	}

	@Override
	protected void personalizar() {
		
		//Status Bar: texto a mostrar
		statusBar.setStatusLine("Ponderado de Días de Inventario Consolidado");
		
		monthPeriod.setPreferredSize(new Dimension(50,15));
		dataPane.setPreferredSize(new Dimension(720, 200));
		dataPane.setBorder(xBorder);
		
		//Cargar los datos de los campos que se agregaron en este indicador
		uploadCollection();
		uploadPackages();
	
		//Así mismo dice que el período es un campo obligatorio para procesar
		monthPeriod.setBackground(true);
		yearPeriod.setBackground(true);
		//Comenzando
		configurar(bSearch, false);
		configurar(bPrint, false);
		configurar(bFile, false);
		
		//Agregar listeners a los nuevos campos
		activarActionListenersAgregados();
	
	}

	protected void limpiarFiltro () {
		
		//Limpia los campos del filtro padre
		super.limpiarFiltro();		
		desactivarActionListenersAgregados();
		//Borrar aquellos campos que se agregaron en esta forma
		configurar(comboCollection, true);
		configurar(checkCollection, true, false);		
		configurar(comboPackage, true);
		configurar(checkPackage, true, false);
		activarActionListenersAgregados();
	}
	
	private void activarActionListenersAgregados () {
		checkCollection.addActionListener(this);
		checkPackage.addActionListener(this);
		comboCollection.addActionListener(this);
		comboPackage.addActionListener(this);
	}
	
	private void desactivarActionListenersAgregados () {
		checkCollection.removeActionListener(this);
		checkPackage.removeActionListener(this);
		comboCollection.removeActionListener(this);
		comboPackage.removeActionListener(this);
	}
	
	@Override
	/** En este método agrego acciones aparte de las acciones predeterminadas de la clase padre XX_Indicator */
	public void actionPerformed(ActionEvent e) {
		
		//Ejecutará las acciones por defecto
		super.actionPerformed(e);
		
		//Desactivar los listeners mientras realizo las modifciaciones
		desactivarActionListeners();
		desactivarActionListenersAgregados();

		//Acciones adicionales a las que vienen con el padre 
		if (e.getSource() == comboCategory) {			
			KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
			if (catg != null  && catg.getKey() != 99999999){ 
				configurar(comboDepartment, true);				
				configurar(checkDepartment, true, false);
			} else {
				configurar(comboDepartment, false);				
				configurar(checkDepartment, false);
			}	
		}
		
		//Verificar si se selecciono el check de collection
		if (e.getSource() == checkCollection) {			
			if ((Boolean)checkCollection.getValue()) {				
				configurar(comboCollection, false ,99999999);				
			} else {
				configurar(comboCollection, true);
			}
		}

		//Verificar si se selecciono el check de package
		if (e.getSource() == checkPackage) {			
			if ((Boolean)checkPackage.getValue()) {				
				configurar(comboPackage, false ,99999999);				
			} else {
				configurar(comboPackage, true);
			}
		}
		
		else if (e.getSource() == comboCollection) {			
			KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
			if (coll != null && coll.getKey() != 99999999){ 
				configurar(comboPackage, true);				
				configurar(checkPackage, true, false);
				uploadPackages();
			} else {
				configurar(comboPackage, false);				
				configurar(checkPackage, false);
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
		
		//Activar listeners
		activarActionListeners();
		activarActionListenersAgregados();
	}
	
	/** Llena la tabla a mostrar con los datos obtenidos del query*/
	protected void llenarTabla() {
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		//Si no se ha cargado el header previamente
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();

		//Calcular el query
		try {
			calculateQueryProduct();
			calculateQueryPurchaseOrder();

			if (miniTable.getRowCount() > 0) {
				configurar(bFile, true);				
				configurar(bPrint, true);
			} else {
				configurar(bFile, false);
				configurar(bPrint, false);
			}
			miniTable.setRowSelectionAllowed(true);
			miniTable.setSelectionBackground(Color.white);
		//	miniTable.autoSize();
			miniTable.setAutoResizeMode(4);
			miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
			miniTable.getTableHeader().setReorderingAllowed(false);			
		} catch (Exception e) {
			
		}
		
		m_frame.setCursor(Cursor.getDefaultCursor());		

	}
	
	/** Agrega a la tabla el total ponderado días de inventario de O/C Chequeadas y Recibidas*/
	private void calculateQueryPurchaseOrder() {
	
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		/* Se agregan las columnas de la tabla */
		//OJO cambiar mensajes 
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo("0/C Recibidas", 
					".", Double.class),
				new ColumnInfo("0/C Chequeadas", 
					".", Double.class),
				new ColumnInfo("Productos Chequeados", 
					".", Double.class),
				new ColumnInfo("Productos En Transito", 
					".", Double.class),
				new ColumnInfo("Productos En Tienda", 
					".", Double.class),
				};
		
		miniTable.prepareTable(layout, "", "", true, "");
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
		int mes = (Integer)monthPeriod.getValue(); 
		int año = (Integer)yearPeriod.getValue();	
		
		String with_cheq = ""; 
		String with_recib = ""; 
		String where=""; 
		String sqlTotalCheq = "";
		String sqlTotalReci = "";
		String sqlTotal ="";
		
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

		
			with_cheq += "WITH " +
			//Total Piezas de O/C Chequeadas Pre-distribuidas que están en tienda
		   "\nEN_TIENDA_PD AS " +
			    "\n(" +
			    "\nSELECT o.C_Order_ID, SUM(de.XX_ProductQuantity) cantTienda " +
				"\nFROM  C_Order o INNER JOIN XX_VMR_Order pe on (o.C_Order_ID = pe.C_Order_ID) " +
			    	"\nINNER JOIN XX_VMR_OrderRequestDetail de on (de.XX_VMR_Order_ID = pe.XX_VMR_Order_ID)  " +
			    "\nWHERE pe.XX_OrderRequestStatus = 'TI' AND o.isSOTRX = 'N' " +
				    "\nAND o.XX_OrderStatus ='CH' " +
				    "\nAND TO_CHAR(o.XX_CHECKUPDATE, 'MM') = " + mes +
					"\nAND TO_CHAR(o.XX_CHECKUPDATE, 'YYYY') = " + año +
				    "\nAND pe.XX_ORDERREQUESTTYPE  = 'P' ";
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
			    "\nAND TO_CHAR(XX_CHECKUPDATE, 'MM') = " + mes +
				"\nAND TO_CHAR(XX_CHECKUPDATE, 'YYYY') = " + año +
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
		    	"\nWHERE io.isSOTRX = 'N' " +
		    		"\nAND pe.XX_OrderRequestStatus = 'TI' " +
		    		"\nAND pe.XX_DATESTATUSONSTORE > o.XX_CHECKUPDATE " +
		    		"\nAND o.C_ORDER_ID in ( " +
					"\nSELECT  oc.C_Order_ID " +
					"\nFROM C_Order oc WHERE oc.isSOTRX = 'N' AND oc.XX_VLO_TypeDelivery IS NOT NULL " +
					"\nAND oc.XX_VLO_TypeDelivery = 'CD' " +
					"\nAND oc.XX_OrderStatus ='CH' AND TO_CHAR(oc.XX_CHECKUPDATE, 'YYYYMM') = " + año + "" + String.format("%02d", mes) + " " +
					"\n)";
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
			    "\nAND TO_CHAR(o.XX_CHECKUPDATE, 'MM') = " + mes +
				"\nAND TO_CHAR(o.XX_CHECKUPDATE, 'YYYY') = " + año +
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
		    with_cheq += "\n),";
	
			//Total Piezas de O/C en estado Recibida
			with_recib += 
				"\nOC_RECIBIDAS AS" +
				"\n(" +
				"\nSELECT oc.C_Order_ID ord, "+
					"\n(CASE WHEN SUM(oc.XX_ProductQuantity) IS NULL THEN 0 ELSE SUM(oc.XX_ProductQuantity) END) TotalPieces " +
				"\nFROM  C_Order oc " +
				"\nWHERE oc.XX_OrderStatus ='RE' AND oc.isSOTRX = 'N' " +
			    "\nAND TO_CHAR(oc.XX_RECEPTIONDATE, 'MM') = " + mes + 
				"\nAND TO_CHAR(oc.XX_RECEPTIONDATE, 'YYYY') = " + año+" ";
			with_recib += "\nGROUP BY oc.C_Order_ID " +
   			"\n)";

		//Total ponderado días de inventario de O/C Chequeadas	
		sqlTotalCheq =  
				"\n, TOTALCHEQUEADO AS (SELECT  " +
				"TRUNC((CASE WHEN sum(TotalPieces) !=0 THEN " +
				"sum(TotalPieces*(sysdate-o.XX_CHECKUPDATE))/sum(TotalPieces) ELSE 0 END),2) totalCH" +
				"\nFROM (SELECT *  from CHEQUEADAS_CD WHERE TotalPieces >  0 " +
		   		"\nUNION ALL " +
		   		"\nSELECT * from CHEQUEADAS_PD  WHERE TotalPieces > 0 ) " +
		   		"\nINNER JOIN C_Order o ON (ord = o.C_Order_ID) ";
		sqlTotalCheq += where+" )";
		//Total ponderado días de inventario de O/C Recibidas	
		sqlTotalReci = 
			"\n, TOTALRECIBIDO AS (SELECT  " +
			"TRUNC((CASE WHEN sum(TotalPieces) !=0 THEN " +
			"sum(TotalPieces*(sysdate-o.XX_RECEPTIONDATE))/sum(TotalPieces) ELSE 0 END),2) totalRE" +
			"\nFROM OC_RECIBIDAS " +
	   		"\nINNER JOIN C_Order o ON (ord = o.C_Order_ID) ";
		sqlTotalReci += where+" AND o.isSOTRX = 'N') ";;
		sqlTotal = with_cheq +  with_recib + sqlTotalCheq + sqlTotalReci;
		
		//Total ponderado días de inventario de O/C Chequeadas y Recibidas	
		sqlTotal += "\nSELECT totalRE, totalCH FROM TOTALCHEQUEADO, TOTALRECIBIDO";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Double> prodPDI = calculateTotalProductWeightedDays();
		try {
			ps = DB.prepareStatement(sqlTotal, null);			
			rs = ps.executeQuery();
			int rows = 0;
			while (rs.next()) {
		
				miniTable.setRowCount(rows + 1);
				miniTable.setValueAt(rs.getDouble(1), rows,0);
				miniTable.setValueAt(rs.getDouble(2), rows,1);
				miniTable.setValueAt(prodPDI.get(0), rows,2);
				miniTable.setValueAt(prodPDI.get(1), rows,3);
				miniTable.setValueAt(prodPDI.get(2), rows,4);
				rows ++;
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE,sqlTotal, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, sqlTotal, e);			
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
	
	/** Agrega a la tabla el total ponderado días de inventario de Productos en Ubicación Chequeado, Transito y Tienda*/
	private void calculateQueryProduct() {

		Integer rows=0;
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
		int mes = (Integer)monthPeriod.getValue(); 
		int año = (Integer)yearPeriod.getValue();	
		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable2.setRowCount(0);
		miniTable2 = new MiniTablePreparator();
		/* Add the column definition for the table */
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"), 
						".", KeyNamePair.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Locator_ID"),
						".", KeyNamePair.class),
				new ColumnInfo( Msg.translate(Env.getCtx(), "Amount")+ " " +Msg.translate(Env.getCtx(), "XX_Pieces"),
						".", Integer.class),
				new ColumnInfo( Msg.translate(Env.getCtx(), "XX_WeightedInventoryDays"),
						".", Double.class),
				new ColumnInfo( Msg.translate(Env.getCtx(), "Status"),
						".", String.class),
				};
		
		miniTable2.prepareTable(layout, "", "", true, "");
		String sql = 
		    "\nSELECT "+
		    "\np.M_Product_ID, "+
		    "\nl.M_Locator_ID, "+
		    "\ns.QTYONHAND qtyProd, "+
		    "\ns.M_ATTRIBUTESETINSTANCE_ID lote, "+
		    "\n(CASE WHEN l.IsDefault = 'N' THEN 'ET' " +
    		"WHEN l.M_Locator_ID = "+CD_CHEQUEADO+" THEN 'CH' " +
    				"ELSE 'TI' END) status"+
		    "\nFROM M_Product p "+
		    "\nINNER JOIN M_Storage s ON (s.M_Product_ID = p.M_Product_ID) "+
		    "\nINNER JOIN M_Locator l ON (l.M_Locator_ID = s.M_Locator_ID) "+
		    "\nINNER JOIN M_Warehouse w ON (w.M_Warehouse_ID = l.M_Warehouse_ID) "+
		    "\nINNER JOIN XX_VMR_Category c ON (c.XX_VMR_Category_ID = p.XX_VMR_Category_ID) "+
		    "\nINNER JOIN XX_VMR_Department d ON (d.XX_VMR_Department_ID = p.XX_VMR_Department_ID) "+
		    "\nINNER JOIN M_ATTRIBUTESETINSTANCE asi ON (s.M_ATTRIBUTESETINSTANCE_ID = asi.M_ATTRIBUTESETINSTANCE_ID) "+
		    "\nINNER JOIN XX_VMR_Package pa ON (pa.XX_VMR_Package_ID = asi.XX_VMR_Package_ID) "+
		    "\nINNER JOIN XX_VMR_Collection co ON (co.XX_VMR_Collection_ID = pa.XX_VMR_Collection_ID) "+
		    "\nWHERE s.QTYONHAND > 0 " +
		    "\nAND l.M_Locator_ID NOT IN("+CD_DEVOLUCION+","+CD_MERMA +","+CD_POR_VALIDAR+")";
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
			sql +=  "\nGROUP BY p.M_Product_ID "+
		    "\n, l.M_Locator_ID "+
		    "\n, s.QTYONHAND "+
		    "\n, s.M_ATTRIBUTESETINSTANCE_ID " +
		    "\n, (CASE WHEN l.IsDefault = 'N' THEN 'ET' " +
    		"WHEN l.M_Locator_ID = "+CD_CHEQUEADO+" THEN 'CH' " +
    		"ELSE 'TI' END)";
	
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			while (rs.next() ) {
				miniTable2.setRowCount(rows + 1);
				miniTable2.setValueAt(rs.getInt(1), rows,PRODUCT);
				miniTable2.setValueAt(rs.getInt(2), rows,LOCATOR);
				miniTable2.setValueAt(rs.getInt(3), rows,TOTALPIECES);
				String sql_dates = "\nSELECT MOVEMENTQTY, " +
					"\n(sysdate-MOVEMENTDATE), " +
					"\n(CASE WHEN TO_CHAR(MOVEMENTDATE, 'MM') = " + mes + " AND" +
						"\n TO_CHAR(MOVEMENTDATE, 'YYYY') = " + año + " THEN " +
								"'Y' ELSE 'N' END) " +
					"\nFROM M_TRANSACTION " +
					"\nWHERE MOVEMENTQTY > 0" +
					"\n AND M_PRODUCT_ID = " + rs.getInt(1) +
					"\n AND M_LOCATOR_ID = "+ rs.getInt(2) +
					"\n AND TO_CHAR(MOVEMENTDATE, 'YYYYMM') >= (CASE WHEN "+mes+" >=10 THEN CONCAT('"+año+"','"+mes+"')" +
					"\n ELSE CONCAT(CONCAT('"+año+"','0'),'"+mes+"') END)" +
					"\n AND M_ATTRIBUTESETINSTANCE_ID = "+ rs.getInt(4) +
					"\n ORDER BY MOVEMENTDATE DESC";
				
			Double weightedDays = calculateTransactionWeightedDays(sql_dates, rs.getInt(3));
			miniTable2.setValueAt(weightedDays, rows, WEIGHTED);
			miniTable2.setValueAt(rs.getString(5), rows, STATUS);
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
	
	/** Devuelve total ponderado días de inventario que lleva la cantidad "qty" de un producto y 
	 * lote específico en una ubicación específica  */
	private Double calculateTransactionWeightedDays(String sql, int qty){
		ArrayList<Integer> daysQty = null;
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		Integer qtyTotal = 0;
		Integer qtyTotalTemp = 0;
		Integer qtyTotalY = 0;
		Double weightedDays = 0.0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			while (rs.next())
			{
				qtyTotalTemp = qtyTotal;
				Integer qtyTemp = rs.getInt(1);
				Integer days = rs.getInt(2);
				String isPeriod = rs.getString(3);
				daysQty = new ArrayList<Integer>(2);
				qtyTotalTemp = qtyTotalTemp + qtyTemp;
				
				if(qtyTotalTemp >= qty){
					qtyTemp = qty - qtyTotal;
					if(isPeriod.equals("Y")){
						qtyTotalY = qtyTotalY + qtyTemp;
						daysQty.add(0, qtyTemp);
						daysQty.add(1, days);
						list.add(daysQty);
					}
					break;
				}
				if(isPeriod.equals("Y")){
					//System.out.println("QtyYI"+qtyTotalY);
					qtyTotalY = qtyTotalY + qtyTemp;
					//System.out.println("QtyYF"+qtyTotalY);
					daysQty.add(0, qtyTemp);
					daysQty.add(1, days);
					list.add(daysQty);
				}
				qtyTotal = qtyTotal + qtyTemp;

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
		//System.out.println("NUEVO");
		if( list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				ArrayList<Integer> temp = list.get(i);
			//	System.out.println("days:"+temp.get(1) );
			//	System.out.println("qty:"+temp.get(0) );
				weightedDays = weightedDays + (temp.get(0)*temp.get(1)); 
			//	System.out.println("W:"+weightedDays );
		
			}
			weightedDays = weightedDays/qtyTotalY;
		//	System.out.println("qtyTotal:"+qtyTotalY  );
			//System.out.println("daysTotal:"+weightedDays  );
		}else weightedDays = 0.0;

		return truncate(weightedDays);
	}

	private ArrayList<Double> calculateTotalProductWeightedDays() {
		Double totalWeightedCH = 0.0;
		Double totalWeightedET = 0.0;
		Double totalWeightedTI = 0.0;
		Integer totalQtyCH = 0;
		Integer totalQtyET = 0;
		Integer totalQtyTI = 0;
		Integer qty = 0;
		String status = null;
		Double days = 0.0;
		for (int j = 0; j < miniTable2.getRowCount(); j++) {
			
			qty = (Integer) miniTable2.getValueAt(j,TOTALPIECES);
			days = (Double) miniTable2.getValueAt(j,WEIGHTED);
			status = (String) miniTable2.getValueAt(j,STATUS);
			if(days>0.0){
				if(status.equals("CH")){
					totalWeightedCH += qty *days;
					totalQtyCH += qty;
				}else if(status.equals("TI")){
					totalWeightedTI += qty *days;
					totalQtyTI += qty;
				}else if(status.equals("ET")){
					totalWeightedET += qty *days;
					totalQtyET += qty;
				}
			}
		}
		ArrayList<Double> result = new ArrayList<Double>();
		
		if(totalQtyCH>0){
			totalWeightedCH =truncate(totalWeightedCH/totalQtyCH);
		}else {
			totalWeightedCH = 0.0;
		}
		result.add(totalWeightedCH);
	
		if(totalQtyET>0){
			totalWeightedET =truncate(totalWeightedET/totalQtyET);
		}else {
			totalWeightedET = 0.0;
		}
		result.add(totalWeightedET);
		
		if(totalQtyTI>0){
			totalWeightedTI =truncate(totalWeightedTI/totalQtyTI);
		}else {
			totalWeightedTI = 0.0;
		}
		result.add(totalWeightedTI);

		return result;
	}
	
	/*Permite truncar un double */
	private static double truncate(double x){
		  if ( x > 0 )
		    return Math.floor(x * 100)/100;
		  else
		    return Math.ceil(x * 100)/100;
		}
	
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



}
