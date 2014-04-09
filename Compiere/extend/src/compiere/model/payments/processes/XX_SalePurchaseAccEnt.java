package compiere.model.payments.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;

import org.compiere.model.MFactAcct;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.Utilities;
import compiere.model.payments.X_Ref_XX_TypeTransferSPList;
import compiere.model.payments.X_XX_VCN_AccoutingEntry;


public class XX_SalePurchaseAccEnt {

	BigDecimal debe = new BigDecimal(0);
	BigDecimal haber = new BigDecimal(0);
	int line = 0;
	int month = 0;
	int year = 0;
	int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	int m_Warehouse_ID = 0;
	String typeTransferSp = "";
	StringBuffer m_sql = null;
	StringBuffer m_where = null;
	Utilities util = new Utilities();
	Timestamp globalDateTo = null;

	private void globalDateTo(){
		
		Calendar dateLast = Calendar.getInstance();
		dateLast.set(Calendar.YEAR, year);
		dateLast.set(Calendar.MONTH, month-1);
		dateLast.set(Calendar.DAY_OF_MONTH, util.ultimoDiaMes(month));
		
		globalDateTo = new Timestamp(dateLast.getTimeInMillis());
		
	}
	
	public void setMonth (int monthAux){
		month = monthAux;
	}
	
	public void setYear (int yearAux){
		year = yearAux;
	}
	
	public void execute(){
		
		Calendar dateInit = Calendar.getInstance();
		Calendar dateLast = Calendar.getInstance();
		int i = 0;
		int warehouses[] = getWarehouses();
		X_Ref_XX_TypeTransferSPList values[] = X_Ref_XX_TypeTransferSPList.values();
		
		dateInit.set(Calendar.YEAR, year);
		dateLast.set(Calendar.YEAR, year);
		
		dateInit.set(Calendar.MONTH, month-1);
		dateLast.set(Calendar.MONTH, month-1);
		
		dateInit.set(Calendar.DAY_OF_MONTH,1);
		dateLast.set(Calendar.DAY_OF_MONTH, util.ultimoDiaMes(month+1));	

		//Mes de cierre ejercicio
		int closureMonth = getMonth();
		globalDateTo();
		
		deletePast();
	   
		for(i = 0; i < warehouses.length; i++){				
			m_Warehouse_ID = warehouses[i];			
			
			for(int j = 0; j < values.length; j++ ){
				
				typeTransferSp = values[j].toString();
				
				if(closureMonth==month && typeTransferSp.equalsIgnoreCase("YEARLY_INVENTORY"))
					continue;
				
				executeHead(new Timestamp(dateInit.getTimeInMillis()),new Timestamp(dateLast.getTimeInMillis()));				
			}					
		}
	}
	
	/**
	 * Obtiene la lista de todos los alamacenes
	 * @author Gustavo Briceño
	 * @return Un arreglo con todos los M_Warehouse_ID del sistema
	 * 
	 */
	private int[] getWarehouses(){
		String sql = "select count(M_Warehouse_id) from M_Warehouse " +
				"where AD_CLient_ID = " + Env.getCtx().getAD_Client_ID();		
		PreparedStatement pstmt = DB.prepareStatement(sql, null);	
		ResultSet rs;
		int size = 0;
		
		try {
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				size = rs.getInt(1);
			}			
			
			rs.close();		
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sql = "select M_Warehouse_id from M_Warehouse where AD_CLient_ID = " + Env.getCtx().getAD_Client_ID();			
		pstmt = DB.prepareStatement(sql, null);			
		
		int warehouses[] = null;
		
		try {
			rs = pstmt.executeQuery();
			int i = 0;
			warehouses = new int[size]; 
			
			while(rs.next()){
				warehouses[i] = rs.getInt(1);
				i++;
			}			
			
			rs.close();		
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return warehouses;
	}
	
	/**
	 * Elimina los datos de un posible asiento anterior del mismo mes.
	 */
	public void deletePast(){

		String deleteDetail = "DELETE FROM FACT_ACCT " +
							  "WHERE XX_VCN_ACCOUTINGENTRY_ID IN " +
							  "(SELECT XX_VCN_ACCOUTINGENTRY_ID FROM XX_VCN_ACCOUTINGENTRY " +
									"WHERE TO_CHAR(dateFrom, 'MM') = "+month+" AND TO_CHAR(dateFrom, 'YYYY') = "+year+" AND XX_LISTCX017 = 'CV') ";
		DB.executeUpdate( null, deleteDetail);
		
		String deleteHeader = "DELETE from XX_VCN_ACCOUTINGENTRY " +
							  "WHERE TO_CHAR(dateFrom, 'MM') = "+month+" AND TO_CHAR(dateFrom, 'YYYY') = "+year+" AND XX_LISTCX017 = 'CV'";
		DB.executeUpdate( null, deleteHeader);
	}
	
	
	/**
	 * Se encarga de generar la cabecera de los asientos contables, y almacenarlos en su tabla correspondiente 
	 * @param sql
	 * @param dateFrom fecha desde
	 * @param dateTo fecha hasta
	 * @param process tipo de procesamiento
	 * @param numTransfer número de la transferencia
	 */
	private void executeHead(Timestamp dateFrom, Timestamp dateTo){

		boolean result = false;
		X_XX_VCN_AccoutingEntry accountingEntry;
		accountingEntry = new X_XX_VCN_AccoutingEntry(Env.getCtx(), 0, null);
		accountingEntry.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
		accountingEntry.setAD_User_ID(Env.getCtx().getAD_User_ID());
		accountingEntry.setDateFrom(dateFrom);
		accountingEntry.setDateTo(dateTo);
		accountingEntry.setDateTrx(new Timestamp(Calendar.getInstance().getTimeInMillis()));					
		accountingEntry.setDescription("COMPROBANTE DIARIO");	
					
		//Buscar número de comprobante
		String numberControl = String.valueOf(3) + "0" + 0 + String.valueOf(numberControl());
		accountingEntry.setXX_ControlNumber(numberControl);
		accountingEntry.setXX_ListCX017("CV");
					
		//accountingEntry.setXX_ProcessType(processType);
		accountingEntry.setXX_TypeTransferSP(typeTransferSp.substring(0,1)); 
		accountingEntry.setM_Warehouse_ID(m_Warehouse_ID);
		accountingEntry.setXX_Office(getWarehouse(m_Warehouse_ID));
		accountingEntry.setXX_TotalHave(BigDecimal.ZERO);
		accountingEntry.setXX_TotalShould(BigDecimal.ZERO);
		accountingEntry.save();
		
		
		if(typeTransferSp.equals("PURCHASES")){
			result = buildDetailCVPurchases(dateFrom, dateTo,accountingEntry.getXX_VCN_AccoutingEntry_ID());
		}else if(typeTransferSp.equals("DISCOUNT")){
			result = buildDetailCVDiscount(dateFrom, dateTo,accountingEntry.getXX_VCN_AccoutingEntry_ID());
		}else if(typeTransferSp.equals("INVENTORY_CHANGE")){
			result = buildDetailCVInventoryChange(dateFrom, dateTo,accountingEntry.getXX_VCN_AccoutingEntry_ID());
		}else if(typeTransferSp.equals("SALES")){
			result = buildDetailCVSales(dateFrom, dateTo,accountingEntry.getXX_VCN_AccoutingEntry_ID());
		}else if(typeTransferSp.equals("YEARLY_INVENTORY")){
			line = 0;
			//result = buildDetailAI(dateFrom, dateTo,accountingEntry.getXX_VCN_AccoutingEntry_ID());
		}					
		
		if(accountingEntry.getXX_TotalHave().compareTo(BigDecimal.ZERO)==0 && accountingEntry.getXX_TotalShould().compareTo(BigDecimal.ZERO)==0){
			//accountingEntry.delete(false);
		}
		
		if (!result){
			//rollback();
			//flag = false;
		}
				
	}
	
	/**
	 * Busca el número de comprobante siguiente, correspondiente a la interfaz CX017
	 * @return número de comprobante
	 */
	public int numberControl(){
		int number = 0;
		String sql_comprobante = "select XX_NUMCOMPROBANTECX017.NEXTVAL from dual";
		PreparedStatement ps_comprobante = null;
		ResultSet rs_comprobante = null;
		try{
			ps_comprobante = DB.prepareStatement(sql_comprobante, null);
			rs_comprobante = ps_comprobante.executeQuery();
			rs_comprobante.next();
			number = rs_comprobante.getInt(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs_comprobante.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ps_comprobante.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return number;
	}
	
	/**
	 * Obtiene el nombre de un almacen
	 * @author Gustavo Briceño
	 * @param Warehouse_id El id del alamacen.
	 * @return El nombre del almacen.
	 */
	public String getWarehouse(int Warehouse_id){
		String result = "";
		String sql = "Select value from M_Warehouse where M_Warehouse_ID = " + Warehouse_id + " and AD_Client_id = " + Env.getCtx().getAD_Client_ID();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			if(rs.next()) {
				result = rs.getString(1);
			}
					
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return result;
	}
	
	/**
	 * Procesamiento para los asientos de compra/venta
	 * @param dateFrom Fecha desde
	 * @param dateTo Fecha hasta
	 * @param process Numero de procesamiento
	 * @param headID Identificador de la cabezera
	 * @return Success boolean
	 */
	public Boolean buildDetailCVPurchases(Timestamp dateFrom, Timestamp dateTo, int headID){
				
		String sql = "";	
		Boolean result = true;
		int c_ElementValue_ID = 0;
		line = 0;
		
		// Compras al Precio de Venta, Margen sobre Compras y Ajustes a las Compras.  		
		
		int xx_typereg[] = new int[3];
		
		xx_typereg[0] = 10;
		xx_typereg[1] = 22;
		xx_typereg[2] = 35;
		
		m_sql = new StringBuffer("select round(nvl(sum(xx_amountmonth),0),2) " +
			     "from xx_vcn_salepurchase sp ");
		
		m_where = new StringBuffer("where xx_year= " + year + " and xx_month= " + month 
				+ " and m_warehouse_id = " + m_Warehouse_ID 
				+ " and ad_client_id = " + m_AD_Client_ID 
				+ " and xx_typereg= ");
				
		for (int i = 0; i < xx_typereg.length; i++) {
			sql = m_sql.toString() + m_where.toString() + xx_typereg[i];
			c_ElementValue_ID = getC_Elementvalue_id(xx_typereg[i]);
			result = result && executeDetailCV(sql, i, headID, c_ElementValue_ID, dateTo, xx_typereg[i]);
		}	
		
		//COMPRAS (IMPORTADAS).		
		m_sql = new StringBuffer(
				"SELECT NVL(SUM(NVL(XX_COSANT,0)), 0) monto, cou.NAME PAIS, C_ELEMENTVALUE_ID " +
				"FROM XX_VLO_SUMMARY summ inner join C_Order ord ON (ord.C_ORDER_ID = summ.C_ORDER_ID) " + 
				"inner join C_Country cou ON (ord.C_Country_ID = cou.C_Country_ID) " +
				"left outer join C_ElementValue ev ON (cou.C_Country_ID = ev.C_Country_ID) " +
				"WHERE to_char(XX_DATEREGISTRATION, 'MM') = "+ month +" AND to_char(XX_DATEREGISTRATION, 'YYYY')  = " + year +" "+
				"AND C_INVOICE_ID IS NOT NULL AND XX_Visible = 'Y' AND summ.M_Warehouse_id = "+ m_Warehouse_ID +" " +
				"AND cou.C_country_id <> 339 " +
				"group by cou.NAME, summ.M_Warehouse_id, C_ELEMENTVALUE_ID " +
				"UNION " +
				"select sum(case when asi.PriceActual is null then (0.01) else asi.PriceActual*iol.MovementQty end) as COMPRAS, " +
				"cou.NAME, C_ELEMENTVALUE_ID " +
				"from M_InOut mio left join AD_Client adc on (mio.AD_Client_ID = adc.AD_Client_ID) " +
				"inner join M_InOutLine iol on (mio.M_InOut_ID = iol.M_InOut_ID) " +
				"left outer join M_Warehouse war on (mio.M_Warehouse_ID = war.M_Warehouse_ID) " +
				"inner join M_Product pro on (iol.M_Product_ID = pro.M_Product_ID) " +
				"inner join XX_VMR_Department dep on (pro.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
				"inner join M_AttributeSetInstance asi on (iol.M_AttributeSetInstance_ID = asi.M_AttributeSetInstance_ID) " +
				"inner join C_Order ord on (mio.C_Order_ID = ord.C_Order_ID) inner join C_Country cou ON (ord.C_Country_ID = cou.C_Country_ID) " +
				"left outer join C_ElementValue ev ON (cou.C_Country_ID = ev.C_Country_ID) " +
				"where to_char(ord.XX_CheckUpdate, 'MM') = "+ month +" and to_char(ord.XX_CheckUpdate, 'yyyy') = " + year +" "+
				"AND mio.ISSOTRX = 'N' AND ord.M_Warehouse_id = "+ m_Warehouse_ID +" " +
				"AND ord.C_Currency_ID <> 205 AND cou.C_country_id <> 339 " +
				"GROUP BY cou.NAME, cou.C_COUNTRY_ID, C_ELEMENTVALUE_ID ");

		sql = m_sql.toString();
		result = result && executeDetailCV(sql, 4, headID, 0, dateTo, 0);
		
		//COMPRAS NACIONALES
		m_sql = new StringBuffer(
				"SELECT SUM(COMPRAS), SUM(SUMMARY), SUM(TRASP_ENTR), SUM(TRASP_SAL), SUM(DEV), SUM(ajuste), SUM(NVL(ENTRANTE,0)), SUM(NVL(SALIENTE,0)) FROM " +
					"(select " +
					"sum(case when asi.PriceActual is null then (0.01) " +
					"else asi.PriceActual*iol.MovementQty end) as COMPRAS, 0 SUMMARY, 0 TRASP_ENTR, 0 TRASP_SAL, 0 DEV,  0 ajuste, 0 ENTRANTE, 0 SALIENTE " +
					"from M_InOut mio " +
					"inner join AD_Client adc on (mio.AD_Client_ID = adc.AD_Client_ID) " +
					"inner join M_InOutLine iol on (mio.M_InOut_ID = iol.M_InOut_ID) " +
					"inner join M_Warehouse war on (mio.M_Warehouse_ID = war.M_Warehouse_ID) " +
					"inner join M_Product pro on (iol.M_Product_ID = pro.M_Product_ID) " +
					"inner join XX_VMR_Department dep on (pro.XX_VMR_Department_ID = dep.XX_VMR_Department_ID)  " +
					"inner join M_AttributeSetInstance asi on (iol.M_AttributeSetInstance_ID = asi.M_AttributeSetInstance_ID) " +
					"inner join C_Order ord on (mio.C_Order_ID = ord.C_Order_ID) " +
					"where to_char(ord.XX_CheckUpdate, 'MM') = "+ month +" AND mio.ISSOTRX = 'N' " +
					"and to_char(ord.XX_CheckUpdate, 'yyyy') = "+ year +" AND M_Warehouse_id = "+ m_Warehouse_ID +" " + 
					"AND ord.C_Currency_ID = 205 " +
					
					"UNION  " +
					
					"(SELECT " +
					"0, 0, sum( " +
					"(CASE WHEN asi.PriceActual IS NULL THEN " +
						"(select MAX(XX_UNITPURCHASEPRICE) from XX_VMR_PriceConsecutive a " +
						"where a.XX_PRICECONSECUTIVE = ml.XX_PRICECONSECUTIVE AND ml.m_product_id = a.m_product_id) " +
						"ELSE asi.PriceActual END)*XX_APPROVEDQTY) costoSTax, 0, 0, 0, 0 ENTRANTE, 0 SALIENTE " +
					"FROM M_Movement M " +
					"inner join M_MovementLine ML ON ( M.M_Movement_ID = ML.M_Movement_ID) " +
					"inner join M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = ML.M_ATTRIBUTESETINSTANCE_ID) " +
					"inner join XX_VLO_DETAILDISPATCHGUIDE DD ON (DD.M_MOVEMENTT_ID = M.M_Movement_ID) " +
					"inner join XX_VLO_DISPATCHGUIDE D ON (DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID) " +
					"inner join M_PRODUCT P ON (ML.M_PRODUCT_ID = P.M_PRODUCT_ID) " +
					"inner join XX_VMR_Department dep on (p.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
					"inner join XX_VMR_Category cat on (p.XX_VMR_Category_ID = cat.XX_VMR_Category_ID) " +
					"inner join M_Warehouse war on (d.XX_ARRIVALWAREHOUSE_ID = war.M_Warehouse_ID) " +
					"inner join AD_Client adc on (adc.AD_Client_ID = m.AD_Client_ID) " +
					"WHERE  " +
					"M.C_DOCTYPE_ID IN (1000356) AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' " +
					"and to_char(D.XX_DispatchDate,'yyyy') = "+ year +" " +
					"and to_char(D.XX_DispatchDate,'MM') = "+ month +" " +
					"AND M.XX_Status = 'AT' AND war.M_Warehouse_ID = "+ m_Warehouse_ID +" " +
					
					"UNION " +
					
					"SELECT  " +
					"0, 0, sum( " +
					"(CASE WHEN asi.PriceActual IS NULL THEN  " +
						"(select MAX(XX_UNITPURCHASEPRICE) from XX_VMR_PriceConsecutive a  " +
						"where a.XX_PRICECONSECUTIVE = ml.XX_PRICECONSECUTIVE AND ml.m_product_id = a.m_product_id) " +
						"ELSE asi.PriceActual END)*MOVEMENTQTY) costoSTax, 0, 0, 0, 0, 0 " +
					"FROM M_Movement M " +
					"inner join M_MovementLine ML ON ( M.M_Movement_ID = ML.M_Movement_ID) " +
					"inner join M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = ML.M_ATTRIBUTESETINSTANCE_ID) " +
					"inner join XX_VLO_DETAILDISPATCHGUIDE DD ON (DD.M_MOVEMENTM_ID = M.M_Movement_ID) " +
					"inner join XX_VLO_DISPATCHGUIDE D ON (DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID) " +
					"inner join M_PRODUCT P ON (ML.M_PRODUCT_ID = P.M_PRODUCT_ID) " +
					"inner join XX_VMR_Department dep on (p.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
					"inner join XX_VMR_Category cat on (p.XX_VMR_Category_ID = cat.XX_VMR_Category_ID) " +
					"inner join M_Warehouse war on (d.XX_ARRIVALWAREHOUSE_ID = war.M_Warehouse_ID) " +
					"inner join AD_Client adc on (adc.AD_Client_ID = m.AD_Client_ID) " +
					"WHERE " +
					"M.C_DOCTYPE_ID IN (1000335) AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' " +
					"and to_char(D.XX_DispatchDate,'yyyy') = "+ year +" " +
					"and to_char(D.XX_DispatchDate,'MM') = "+ month +" " +
					"AND M.XX_Status = 'AC' AND war.M_Warehouse_ID = "+ m_Warehouse_ID +") " +
					
					"UNION " +
					
					"(SELECT " +
					"0, 0, 0, sum( " +
					"(CASE WHEN asi.PriceActual IS NULL THEN " +
						"(select MAX(XX_UNITPURCHASEPRICE) from XX_VMR_PriceConsecutive a " +
						"where a.XX_PRICECONSECUTIVE = ml.XX_PRICECONSECUTIVE AND ml.m_product_id = a.m_product_id) " +
						"ELSE asi.PriceActual END)*XX_APPROVEDQTY) costoSTax, 0, 0, 0, 0 " +
					"FROM M_Movement M " +
					"inner join M_MovementLine ML ON ( M.M_Movement_ID = ML.M_Movement_ID) " +
					"inner join M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = ML.M_ATTRIBUTESETINSTANCE_ID) " +
					"inner join XX_VLO_DETAILDISPATCHGUIDE DD ON (DD.M_MOVEMENTT_ID = M.M_Movement_ID) " +
					"inner join XX_VLO_DISPATCHGUIDE D ON (DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID) " +
					"inner join M_PRODUCT P ON (ML.M_PRODUCT_ID = P.M_PRODUCT_ID) " +
					"inner join XX_VMR_Department dep on (p.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
					"inner join XX_VMR_Category cat on (p.XX_VMR_Category_ID = cat.XX_VMR_Category_ID) " +
					"inner join M_Warehouse war on (d.XX_DEPARTUREWAREHOUSE_ID = war.M_Warehouse_ID) " +
					"inner join AD_Client adc on (adc.AD_Client_ID = m.AD_Client_ID) " +
					"WHERE " +
					"M.C_DOCTYPE_ID IN (1000356) AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' " +
					"and to_char(D.XX_DispatchDate,'yyyy') = "+ year +" " +
					"and to_char(D.XX_DispatchDate,'MM') = "+ month +" " +
					"AND M.XX_Status = 'AT' AND war.M_Warehouse_ID = "+ m_Warehouse_ID +" ");
					
		m_where = new StringBuffer(
					"UNION " +
					"SELECT " +
					"0, 0, 0, sum( " +
					"(CASE WHEN asi.PriceActual IS NULL THEN " +
						"(select MAX(XX_UNITPURCHASEPRICE) from XX_VMR_PriceConsecutive a " +
						"where a.XX_PRICECONSECUTIVE = ml.XX_PRICECONSECUTIVE AND ml.m_product_id = a.m_product_id) " +
						"ELSE asi.PriceActual END)*MOVEMENTQTY) costoSTax, 0, 0, 0, 0 " +
					"FROM M_Movement M " +
					"inner join M_MovementLine ML ON ( M.M_Movement_ID = ML.M_Movement_ID) " +
					"inner join M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = ML.M_ATTRIBUTESETINSTANCE_ID) " +
					"inner join XX_VLO_DETAILDISPATCHGUIDE DD ON (DD.M_MOVEMENTM_ID = M.M_Movement_ID) " +
					"inner join XX_VLO_DISPATCHGUIDE D ON (DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID) " +
					"inner join M_PRODUCT P ON (ML.M_PRODUCT_ID = P.M_PRODUCT_ID) " +
					"inner join XX_VMR_Department dep on (p.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
					"inner join XX_VMR_Category cat on (p.XX_VMR_Category_ID = cat.XX_VMR_Category_ID) " +
					"inner join M_Warehouse war on (d.XX_DEPARTUREWAREHOUSE_ID = war.M_Warehouse_ID) " +
					"inner join AD_Client adc on (adc.AD_Client_ID = m.AD_Client_ID) " +
					"WHERE " +
					"M.C_DOCTYPE_ID IN (1000335) AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' " + 
					"and to_char(D.XX_DispatchDate,'yyyy') = "+ year +" " +
					"and to_char(D.XX_DispatchDate,'MM') = "+ month +" " +
					"AND M.XX_Status = 'AC' AND war.M_Warehouse_ID = "+ m_Warehouse_ID +") " +
					
					"UNION " +
					
					"select 0, 0, 0, 0, " +
					"SUM(CASE WHEN asi.PriceActual IS NULL THEN " +
					"(select MAX(XX_UNITPURCHASEPRICE) from XX_VMR_PriceConsecutive a " + 
					"where a.XX_PRICECONSECUTIVE = mol.XX_PRICECONSECUTIVE AND mol.m_product_id = a.m_product_id)*XX_QuantityReceived " +
					"ELSE asi.PriceActual*XX_QuantityReceived END) as costoSTax, 0, 0, 0 " +
					"from M_Movement mov " +
					"inner join XX_VLO_ReturnOfProduct rop on (rop.M_Movement_ID = mov.M_Movement_ID) " +
					"inner join M_MovementLine mol on (mov.M_Movement_ID = mol.M_Movement_ID) " +
					"inner join AD_Client adc on (adc.AD_Client_ID = mov.AD_Client_ID) " +
					"inner join M_Warehouse war on (mov.M_WarehouseFrom_ID = war.M_Warehouse_ID) " +
					"inner join M_Product P on (mol.M_Product_ID = p.M_Product_ID) " +
					"inner join C_BPartner par on (rop.C_BPartner_ID = par.C_BPartner_ID) " +
					"inner join M_AttributeSetInstance asi on (mol.M_AttributeSetInstance_ID = asi.M_AttributeSetInstance_ID) " +
					"inner join XX_VMR_Department dep on (p.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
					"where  " +
					"mov.XX_Status = 'AC' and  " +
					"mov.C_DocType_ID = 1000355 " +
					"and to_char(rop.created,'yyyy') = "+ year +" " +
					"and to_char(rop.created,'MM') = "+ month +" " +
					"and par.XX_VendorClass = 10000005 AND war.M_Warehouse_ID = "+ m_Warehouse_ID +" " +
					
					"UNION " +
					
					"SELECT 0,0,0,0,0, NVL(SUM(NVL(XX_COSANT,0)), 0) ajuste, 0 , 0 " +
					"FROM XX_VLO_SUMMARY summ inner join C_Order ord ON (ord.C_ORDER_ID = summ.C_ORDER_ID) " +
					"inner join C_Country cou ON (ord.C_Country_ID = cou.C_Country_ID) " +
					"WHERE to_char(XX_DATEREGISTRATION, 'MM') = "+ month +" AND to_char(XX_DATEREGISTRATION, 'YYYY') = "+ year +" " +
					"AND C_INVOICE_ID IS NOT NULL AND XX_Visible = 'Y' AND summ.M_Warehouse_id = " + m_Warehouse_ID + " " +
					"AND cou.C_country_id = 339" +
					
					"UNION " +
					
					"SELECT  0,0,0,0,0, 0, " +
					"sum( " +
						"(CASE WHEN asi.PriceActual IS NULL THEN " +
						"(select MAX(XX_UNITPURCHASEPRICE) from XX_VMR_PriceConsecutive a " +
						"where a.XX_PRICECONSECUTIVE = od.XX_PRICECONSECUTIVE AND od.m_product_id = a.m_product_id) " +
						"ELSE asi.PriceActual END)*OD.XX_ProductQuantity) as ENTRANTE, 0 SALIENTE " +
					"FROM XX_VMR_ORDER O, XX_VMR_ORDERREQUESTDETAIL OD, M_PRODUCT P, AD_Client adc,  XX_VMR_Department dep, M_Warehouse war, " +
					"(select O.XX_VMR_ORDER_ID from XX_VMR_ORDER O, XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D " +
					"where DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND " +
					"DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID " + 
					"AND nvl(TO_CHAR(O.XX_DATESTATUSONSTORE,'YYYY'),TO_CHAR(D.XX_DISPATCHDATE,'YYYY')) = "+ year +" " +
					"AND nvl(TO_CHAR(O.XX_DATESTATUSONSTORE,'MM'),TO_CHAR(D.XX_DISPATCHDATE,'MM'))  = "+ month +" "+
					"AND XX_ARRIVALWAREHOUSE_ID = "+ m_Warehouse_ID +" " +
					"AND O.XX_ORDERREQUESTSTATUS = 'TI' " +
					"AND DD.XX_TYPEDETAILDISPATCHGUIDE = 'MCD' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' " +
					"group by O.XX_VMR_ORDER_ID) Z, M_ATTRIBUTESETINSTANCE ASI " +
					"WHERE O.XX_VMR_ORDER_ID=OD.XX_VMR_ORDER_ID AND Z.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID " +
					"AND  dep.XX_VMR_Department_ID = p.XX_VMR_Department_ID AND war.M_Warehouse_ID = o.M_Warehouse_ID " +
					"AND OD.M_PRODUCT_ID = P.M_PRODUCT_ID AND adc.AD_Client_ID = o.AD_Client_ID " +
					"AND NVL(OD.XX_PRODUCTBATCH_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) " +
					
					"UNION " +
					
					"SELECT 0,0,0,0,0,0,0, " +
					"sum( " +
						"(CASE WHEN asi.PriceActual IS NULL THEN " +
						"(select MAX(XX_UNITPURCHASEPRICE) from XX_VMR_PriceConsecutive a " +
						"where a.XX_PRICECONSECUTIVE = od.XX_PRICECONSECUTIVE AND od.m_product_id = a.m_product_id) " +
						"ELSE asi.PriceActual END)*OD.XX_ProductQuantity) as SALIENTE " +
					
					"FROM XX_VMR_ORDER O, XX_VMR_ORDERREQUESTDETAIL OD, M_PRODUCT P, " +
					"AD_Client adc,  XX_VMR_Department dep, M_Warehouse war, " +
					"(select O.XX_VMR_ORDER_ID from XX_VMR_ORDER O, XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D " +
					"where DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND " +
					"DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID " +
					"AND nvl(TO_CHAR(O.XX_DATESTATUSONSTORE,'YYYY'),TO_CHAR(D.XX_DISPATCHDATE,'YYYY')) = "+ year +" " +
					"AND nvl(TO_CHAR(O.XX_DATESTATUSONSTORE,'MM'),TO_CHAR(D.XX_DISPATCHDATE,'MM'))  = "+ month +" "+
					"AND XX_DEPARTUREWAREHOUSE_ID = "+ m_Warehouse_ID +" " +
					"AND O.XX_ORDERREQUESTSTATUS = 'TI' " +
					"AND DD.XX_TYPEDETAILDISPATCHGUIDE = 'MCD' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' " +
					"group by O.XX_VMR_ORDER_ID) Z, AD_ORG ORG, M_ATTRIBUTESETINSTANCE ASI " +
					"WHERE O.XX_VMR_ORDER_ID=OD.XX_VMR_ORDER_ID AND Z.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID " +
					"AND  dep.XX_VMR_Department_ID = p.XX_VMR_Department_ID AND war.M_Warehouse_ID = o.M_Warehouse_ID " +
					"AND OD.M_PRODUCT_ID = P.M_PRODUCT_ID AND adc.AD_Client_ID = o.AD_Client_ID  " +
					"AND o.AD_ORG_ID =  ORG.AD_ORG_ID  " +
					"AND NVL(OD.XX_PRODUCTBATCH_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) " +
				
				")" 
			);
		
		/*
		 * Cuenta contable
		 */
		sql = "select C_ElementValue_id " +
			  "from C_ElementValue " +
			  "where XX_TRANSITIONAL = 'Y' " +
			  "and Accounttype = 'L' " +
			  "and XX_ELEMENTTYPE = 'Nacional' ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			int i = 0;
			while(rs.next()) {
				if(i == 0){
					c_ElementValue_ID = rs.getInt(1);
					i++;
				}else{
					System.out.println("Advertencia, existe mas de un elemento contable para la linea 6.");
				}
			}		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		
		sql = m_sql.toString() + m_where.toString();
		result = result && executeDetailCV(sql,5, headID, c_ElementValue_ID,dateTo, 0);
		
		//Diferencia entre debe y haber
		X_XX_VCN_AccoutingEntry accountingEntry = new X_XX_VCN_AccoutingEntry(Env.getCtx(), headID, null);
		BigDecimal dif = accountingEntry.getXX_TotalShould().subtract(accountingEntry.getXX_TotalHave());
		
		if(dif.compareTo(BigDecimal.ZERO)==0)
			return result;
		
		m_sql = new StringBuffer("select "+dif+" from dual");
		
		sql = m_sql.toString();
		//c_ElementValue_ID = getC_Elementvalue_id("APC","L");
		c_ElementValue_ID = 1002551;
		result = result && executeDetailCV(sql, 6, headID, c_ElementValue_ID,dateTo, 0);
		
		return result; 
	}
	
	/**
	 * Se encarga de generar el detalle, a apartir de la cabecera del asiento contable
	 * @param sqlDetail resulSet del query del detalle
	 * @param sql sql de la cuenta contable
	 * @param lineaAsiento número de línea
	 * @param headID identificador de la cabecera del asiento contable
	 * @return Success Boolean
	 */
	public Boolean executeDetailCV(String sql, int lineaAsiento, int headID, int c_ElementValue_id, Timestamp dateTo, Integer typeReg){

		DecimalFormat format = new DecimalFormat("00");
		String office = getWarehouse(m_Warehouse_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		int c_element = c_ElementValue_id;
		
		String sql2 = "";		
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;	
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			MFactAcct detailFactA;
						
			while(rs.next()){

				X_XX_VCN_AccoutingEntry accountingEntry = new X_XX_VCN_AccoutingEntry(Env.getCtx(), headID, null);
				
				
				detailFactA = new MFactAcct(Env.getCtx(), 0, null);
				detailFactA.set_Value("XX_VCN_AccoutingEntry_ID", headID);
				detailFactA.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
				detailFactA.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
				line++;
				detailFactA.set_Value("XX_VCN_Line",line);	
				
				if(typeTransferSp.equals("PURCHASES") && (lineaAsiento == 3) ){						
					c_element = c_ElementValue_id;	
					detailFactA.set_Value("DocumentNo","");
					detailFactA.set_Value("setXX_DocumentType","");						
				}else if(typeTransferSp.equals("PURCHASES") && lineaAsiento == 4){
					c_element = rs.getInt(3);	
					detailFactA.set_Value("DocumentNo","");
					detailFactA.set_Value("setXX_DocumentType","");						
					detailFactA.set_Value("setXX_DocumentType","AST");
					detailFactA.set_Value("XX_DocumentDate",globalDateTo);	
					detailFactA.set_Value("DocumentNo","1");
				}
				else
					detailFactA.set_Value("DocumentNo"," ");
				
				if(typeTransferSp.equals("SALES") && lineaAsiento == 3){
					detailFactA.set_Value("XX_Office","001");
					detailFactA.set_Value("DocumentNo","1");
					detailFactA.set_Value("XX_DocumentType","AST");
					detailFactA.set_Value("XX_DocumentDate",dateTo);
				}else{
					detailFactA.set_Value("XX_Office",office);
				}
				
				detailFactA.set_Value("Account_ID",c_element);
				
				sql2 = "select XX_DIV, XX_SEC, XX_AUX, XX_DEP " +
					   "from C_ElementValue " +
					   "where C_ElementValue_id = " + c_element;
				pstmt2 = DB.prepareStatement(sql2, null);
				rs2 = pstmt2.executeQuery();
				
				if(rs2.next()){
					for(int i = 1; i < 5; i++){
						if(rs2.getString(i).equals("Y")){
							switch(i){
								case 1:
									detailFactA.set_Value("XX_Division",getDiv());
									break;
								case 2:
									detailFactA.set_Value("XX_SectionCode",getSec());
									break;
								case 4:
									detailFactA.set_Value("XX_Departament","080");
									break;
								default:
									break;
							}
						}else{
							switch(i){
							case 1:
								detailFactA.set_Value("XX_Division"," ");
								break;
							case 2:
								detailFactA.set_Value("XX_SectionCode"," ");
								break;
							case 4:
								detailFactA.set_Value("XX_Departament"," ");
								break;
							default:
								break;
						}
						}							
					}
				}
				
				pstmt2.close();
				rs2.close();
				
				if(typeTransferSp.equals("SALES")){
					switch(lineaAsiento){
						case 0:
							detailFactA.setDescription("LIQ. VENTAS NETAS MES'"+ format.format(month) + " AÑO'" + year);
							break;
						case 3:
							detailFactA.setDescription("RECAUDADO S/VENTAS MES'"+ format.format(month) + " AÑO'" + year);
							break;
						default:
							detailFactA.setDescription(util.meses(month) +"'"+ year);			
							break;	
					}						
				}else if(typeTransferSp.equals("PURCHASES")){
					switch(lineaAsiento){												
						case 5:
							detailFactA.setDescription("COMP.NAC.A PRECIO D/CTOS."
									+ format.format(month) +"'"+year+" TDA."+ office);
							break;
						case 4: case 3:		
							detailFactA.setDescription(rs.getString(2) + " COMP.IMP."
									+ util.meses(month) +"'"+year);
							break;
						default:
							detailFactA.setDescription(util.meses(month) +"'"+ year);			
							break;		
					}
				}else if(typeTransferSp.equals("YEARLY_INVENTORY")){
					detailFactA.setDescription("EJERCICIO " + (year-1) + "/" + year);		
				}else{
					detailFactA.setDescription(util.meses(month) +"'"+ year);		
				}								
				
				if(typeTransferSp.equals("DISCOUNT")){						
					if(lineaAsiento == 0){
						detailFactA.setAmtAcctCr(new BigDecimal(0));
						detailFactA.setAmtSourceCr(new BigDecimal(0));
						detailFactA.setAmtAcctDr(rs.getBigDecimal(1));
						detailFactA.setAmtSourceDr(rs.getBigDecimal(1));
					}else{
						detailFactA.setAmtAcctDr(new BigDecimal(0));
						detailFactA.setAmtSourceDr(new BigDecimal(0));
						detailFactA.setAmtAcctCr(rs.getBigDecimal(1).abs());
						detailFactA.setAmtSourceCr(rs.getBigDecimal(1).abs());
					}
				}else if(typeTransferSp.equals("SALES")){
					switch(lineaAsiento){
						case 0: case 1:	
							detailFactA.setAmtAcctCr(new BigDecimal(0));
							detailFactA.setAmtSourceCr(new BigDecimal(0));
							detailFactA.setAmtAcctDr(rs.getBigDecimal(1));
							detailFactA.setAmtSourceDr(rs.getBigDecimal(1));
							break;
						case 2: case 3:
							detailFactA.setAmtAcctDr(new BigDecimal(0));
							detailFactA.setAmtSourceDr(new BigDecimal(0));
							detailFactA.setAmtAcctCr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtSourceCr(rs.getBigDecimal(1).abs());
							break;
						case 4: 
							if(rs.getBigDecimal(1).compareTo(BigDecimal.ZERO)>0){															
								detailFactA.setAmtAcctDr(new BigDecimal(0));
								detailFactA.setAmtSourceDr(new BigDecimal(0));
								detailFactA.setAmtAcctCr(rs.getBigDecimal(1).abs());
								detailFactA.setAmtSourceCr(rs.getBigDecimal(1).abs());
							}else{
								detailFactA.setAmtAcctCr(new BigDecimal(0));
								detailFactA.setAmtSourceCr(new BigDecimal(0));
								detailFactA.setAmtAcctDr(rs.getBigDecimal(1).abs());
								detailFactA.setAmtSourceDr(rs.getBigDecimal(1).abs());
							}	
							break;
						default:
							break;
					}	
				}else if(typeTransferSp.equals("YEARLY_INVENTORY")){
					switch(lineaAsiento){
						case 0:
						case 1:							
							detailFactA.setAmtAcctDr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtSourceDr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtAcctCr(new BigDecimal(0));
							detailFactA.setAmtSourceCr(new BigDecimal(0));			
							break;
						case 2:
						case 3:
							detailFactA.setAmtAcctDr(new BigDecimal(0));
							detailFactA.setAmtSourceDr(new BigDecimal(0));
							detailFactA.setAmtAcctCr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtSourceCr(rs.getBigDecimal(1).abs());	
							break;
						default:
							break;
					}						
				}else if(typeTransferSp.equals("PURCHASES")){
					
					if(typeReg==10){
						if(rs.getBigDecimal(1).compareTo(BigDecimal.ZERO)<0){
							detailFactA.setAmtAcctCr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtAcctDr(new BigDecimal(0));
						}
						else{
							detailFactA.setAmtAcctCr(new BigDecimal(0));
							detailFactA.setAmtSourceCr(new BigDecimal(0));
							detailFactA.setAmtAcctDr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtSourceDr(rs.getBigDecimal(1).abs());
						}
					}
					
					if(typeReg==22){
						if(rs.getBigDecimal(1).compareTo(BigDecimal.ZERO)>0){
							detailFactA.setAmtAcctCr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtSourceCr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtAcctDr(new BigDecimal(0));
							detailFactA.setAmtSourceDr(new BigDecimal(0));
						}
						else{
							detailFactA.setAmtAcctCr(new BigDecimal(0));
							detailFactA.setAmtSourceCr(new BigDecimal(0));
							detailFactA.setAmtAcctDr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtSourceDr(rs.getBigDecimal(1).abs());
						}
					}
					
					if(typeReg==35){	
						if(rs.getBigDecimal(1).compareTo(BigDecimal.ZERO)<0){
							detailFactA.setAmtAcctCr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtSourceCr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtAcctDr(new BigDecimal(0));
							detailFactA.setAmtSourceDr(new BigDecimal(0));
						}
						else{
							detailFactA.setAmtAcctCr(new BigDecimal(0));
							detailFactA.setAmtSourceCr(new BigDecimal(0));
							detailFactA.setAmtAcctDr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtSourceDr(rs.getBigDecimal(1).abs());
						}
					}
					
					if(lineaAsiento == 3 || lineaAsiento == 4){
						
						if(rs.getBigDecimal(1).compareTo(BigDecimal.ZERO)>0){
							detailFactA.setAmtAcctCr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtSourceCr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtAcctDr(new BigDecimal(0));
							detailFactA.setAmtSourceDr(new BigDecimal(0));
						}
						else{
							detailFactA.setAmtAcctCr(new BigDecimal(0));
							detailFactA.setAmtSourceCr(new BigDecimal(0));
							detailFactA.setAmtAcctDr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtSourceDr(rs.getBigDecimal(1).abs());
						}
					}
					
					if(lineaAsiento == 5){
						
						BigDecimal amount = rs.getBigDecimal(1).add(rs.getBigDecimal(2))
								.add(rs.getBigDecimal(3)).subtract(rs.getBigDecimal(4)).subtract(rs.getBigDecimal(5))
								.add(rs.getBigDecimal(6).add(rs.getBigDecimal(7))).subtract(rs.getBigDecimal(8));
						
						if(amount.compareTo(BigDecimal.ZERO)>0){
							detailFactA.setAmtAcctCr(amount.abs());
							detailFactA.setAmtSourceCr(amount.abs());
							detailFactA.setAmtAcctDr(new BigDecimal(0));
							detailFactA.setAmtSourceDr(new BigDecimal(0));
						}
						else{
							detailFactA.setAmtAcctCr(new BigDecimal(0));
							detailFactA.setAmtSourceCr(new BigDecimal(0));
							detailFactA.setAmtAcctDr(amount.abs());
							detailFactA.setAmtSourceDr(amount.abs());
						}
					}
					
					if(lineaAsiento == 6){
						if(rs.getBigDecimal(1).compareTo(BigDecimal.ZERO)>0){															
							detailFactA.setAmtAcctDr(new BigDecimal(0));
							detailFactA.setAmtSourceDr(new BigDecimal(0));
							detailFactA.setAmtAcctCr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtSourceCr(rs.getBigDecimal(1).abs());
						}else{
							detailFactA.setAmtAcctCr(new BigDecimal(0));
							detailFactA.setAmtSourceCr(new BigDecimal(0));
							detailFactA.setAmtAcctDr(rs.getBigDecimal(1).abs());
							detailFactA.setAmtSourceDr(rs.getBigDecimal(1).abs());
						}	
					}
				}
				else if(typeTransferSp.equals("INVENTORY_CHANGE")){
					
					if(rs.getBigDecimal(1).compareTo(BigDecimal.ZERO)>0){
						detailFactA.setAmtAcctDr(new BigDecimal(0));
						detailFactA.setAmtSourceDr(new BigDecimal(0));
						detailFactA.setAmtAcctCr(rs.getBigDecimal(1).abs());
						detailFactA.setAmtSourceCr(rs.getBigDecimal(1).abs());
					}
					else{
						detailFactA.setAmtAcctCr(new BigDecimal(0));
						detailFactA.setAmtSourceCr(new BigDecimal(0));
						detailFactA.setAmtAcctDr(rs.getBigDecimal(1).abs());
						detailFactA.setAmtSourceDr(rs.getBigDecimal(1).abs());
					}
				}
	
				//Redondeo
				detailFactA.setAmtAcctDr(detailFactA.getAmtAcctDr().setScale(2, RoundingMode.HALF_UP));
				detailFactA.setAmtSourceDr(detailFactA.getAmtSourceDr().setScale(2, RoundingMode.HALF_UP));
				
				detailFactA.setAmtAcctCr(detailFactA.getAmtAcctCr().setScale(2, RoundingMode.HALF_UP));
				detailFactA.setAmtSourceCr(detailFactA.getAmtSourceCr().setScale(2, RoundingMode.HALF_UP));
				
				if(detailFactA.getAmtAcctDr().compareTo(BigDecimal.ZERO)!=0 || detailFactA.getAmtAcctCr().compareTo(BigDecimal.ZERO)!=0)
					detailFactA.save();
				
				//Acumulativo
				accountingEntry.setXX_TotalHave(accountingEntry.getXX_TotalHave().add(detailFactA.getAmtAcctCr()));
				accountingEntry.setXX_TotalShould(accountingEntry.getXX_TotalShould().add(detailFactA.getAmtAcctDr()));
				accountingEntry.save();
				
			}		
			
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
			return false;
		}		
		
		return true;
	}
	
	/**
	 * Obtiene el Id de un C_Elementvalue a partir del campo XX_TypeReg
	 * @author Gustavo Briceño
	 * @param el valor del C_Elementvalue
	 * @return el id del C_Elementvalue
	 */
	public int getC_Elementvalue_id(int typereg){
		int c_ElementValue_ID = 0;	
		DecimalFormat format = new DecimalFormat("00");
		String sql  =  "select C_Elementvalue_ID " +		
				"from C_Elementvalue " +
				"where XX_TypeReg =  '" + format.format(typereg) + "' ";				
				
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
		
			if(rs.next()){
				c_ElementValue_ID = rs.getInt(1);
			}
			
			rs.close();
			pstmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return c_ElementValue_ID;
	}
	
	/**
	 * Obtiene el Id de un C_Elementvalue a partir de la id del país asociado
	 * @author Gustavo Briceño
	 * @param el valor del C_Elementvalue
	 * @return el id del C_Elementvalue
	 */
	public int getC_Elementvalue_id(String country_id){
		int c_ElementValue_ID = 0;		
		String sql  =  "select C_Elementvalue_ID " +		
				"from C_Elementvalue " +
				"where C_Country_ID =  '" + country_id + "' ";				
				
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
		
			if(rs.next()){
				c_ElementValue_ID = rs.getInt(1);
			}
			
			rs.close();
			pstmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return c_ElementValue_ID;
	}
	
	/**
	 * Obtiene el Id de un C_Elementvalue a partir de 
	 * su codigo de transferencia centralizada y  su tipo
	 * @author Gustavo Briceño
	 * @param el valor del C_Elementvalue
	 * @return el id del C_Elementvalue
	 */
	public int getC_Elementvalue_id(String code, String type){
		int c_ElementValue_ID = 0;		
		String sql  =  "select C_Elementvalue_ID " +		
				"from C_Elementvalue " +
				"where XX_TransCen =  '" + code + "' " +
				"and Accounttype = '" + type + "'";
				
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
		
			if(rs.next()){
				c_ElementValue_ID = rs.getInt(1);
			}
			
			rs.close();
			pstmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return c_ElementValue_ID;
	}
	
	/**
	 * Obtiene la sección
	 * @author Gustavo Briceño
	 */
	public String getSec(){
		String result = "";
		String sql = "SELECT XX_SECTIONCODE " +
				"FROM AD_ClientInfo " +
				"where AD_CLIENT_ID = " + m_AD_Client_ID;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			if(rs.next()) {
				result = rs.getString(1);
			}
					
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		return result;
	}
	
	/**
	 * Obtiene la División
	 * @author Gustavo Briceño
	 */
	public String getDiv(){
		String result = "";
		String sql = "SELECT XX_DIVISION " +
				"FROM AD_ClientInfo " +
				"where AD_CLIENT_ID = " + m_AD_Client_ID;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			if(rs.next()) {
				result = rs.getString(1);
			}
					
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		return result;
	}
	
	/**
	 * Obtiene el ultimo mes del periodo actual
	 * @return El mes en formato de numero entero
	 */
	public int getMonth(){
		int result = 0;		
		
		String sql = "select to_char(max(startdate),'mm') " +
				"from c_period prd, c_year yrd, c_calendar cld " +
				"where prd.c_year_id = yrd.c_year_id " +
				"and yrd.c_calendar_id = cld.c_calendar_id " +
				"and cld.name like '%Calendar' " +
				"and yrd.fiscalyear like '%"+year+"%'";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			if(rs.next()) {
				result = rs.getInt(1);
			}
					
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	/**
	 * Procesamiento para los asientos de compra/venta
	 * @param dateFrom Fecha desde
	 * @param dateTo Fecha hasta
	 * @param process Numero de procesamiento
	 * @param headID Identificador de la cabezera
	 * @return Success boolean
	 */
	public Boolean buildDetailCVDiscount(Timestamp dateFrom, Timestamp dateTo, int headID){
		
		boolean result = true;
		String sql = "";
		int c_ElementValue_ID = 0;
		line = 0;
		
		int xx_typereg[] = new int[2];
		
		xx_typereg[0] = 15;
		xx_typereg[1] = 27;
		
		m_sql = new StringBuffer("select round(nvl(sum(xx_amountmonth),0),2) " +
			     "from xx_vcn_salepurchase sp ");
		
		m_where = new StringBuffer("where xx_year= " + year + " and xx_month= " + month 
				+ " and m_warehouse_id = " + m_Warehouse_ID 
				+ " and ad_client_id = " + m_AD_Client_ID 
				+ " and xx_typereg= ");
				
		for (int i = 0; i < xx_typereg.length; i++) {
			sql = m_sql.toString() + m_where.toString() + xx_typereg[i];
			c_ElementValue_ID = getC_Elementvalue_id(xx_typereg[i]);
			result = result && executeDetailCV(sql, i, headID, c_ElementValue_ID, dateTo, 0);
		}	
		
		return result;
	}
	
	/**
	 * Procesamiento para los asientos de compra/venta
	 * @param dateFrom Fecha desde
	 * @param dateTo Fecha hasta
	 * @param process Numero de procesamiento
	 * @param headID Identificador de la cabezera
	 * @return Success boolean
	 */
	public Boolean buildDetailCVSales(Timestamp dateFrom, Timestamp dateTo, int headID){
		
		boolean result = true;
		String sql = "";
		int c_ElementValue_ID = 0;		
		double should = 0;
		line = 0;
		
		//Ventas por Distribuir
		As400DbManager As = new As400DbManager();
		Statement sentencia = null;
		ResultSet rs=null;
		
		try {
			
			As.conectar();
			
			m_sql = new StringBuffer("Select SUM (Mtoreg) " +
					"From ICTFILE.CAPD11 " +
					"Where Tienda = " + getWarehouse(m_Warehouse_ID) + " " +
					"and Tipreg = 'IFA' " +
					"and Correg = 1 " +
					"and SUBSTRING(Fecreg, 5, 2) = " + month + " " +
					"and SUBSTRING(Fecreg, 1, 4) = " + year + " " +
					//"and AD_Client_ID = " + m_AD_Client_ID);
					" ");
			
			sql = m_sql.toString();			
			sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = As.realizarConsulta(sql, sentencia);
			
			if(rs.next()){
				should = rs.getDouble(1);
			}
			
			sql = "select round(" + should + ",2) from dual";
			c_ElementValue_ID = getC_Elementvalue_id("VPD","A");  
			result = result && executeDetailCV(sql, 0, headID, c_ElementValue_ID, dateTo, 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}       
				
		//Descuentos sobre Ventas y Terceras ventas al contado
		String value[] = new String[2];	
		
		value[0] = "Descuentos Sobre Ventas Contado (Detal)";
		value[1] = "Ventas al Contado (Detal)"; 		
		
		int xx_typereg[] = new int[2];
		
		xx_typereg[0] = 02;
		xx_typereg[1] = 01;
		
		m_sql = new StringBuffer("select round(nvl(sum(xx_amountmonth),0),2) " +
			     "from xx_vcn_salepurchase sp ");
		
		m_where = new StringBuffer("where xx_year= " + year + " and xx_month= " + month 
				+ " and m_warehouse_id = " + m_Warehouse_ID 
				+ " and ad_client_id = " + m_AD_Client_ID 
				+ " and xx_typereg= ");
				
		for (int i = 0; i < xx_typereg.length; i++) {
			sql = m_sql.toString() + m_where.toString() + xx_typereg[i];
			c_ElementValue_ID = getC_Elementvalue_id(xx_typereg[i]);			
			result = result && executeDetailCV(sql, i+1, headID, c_ElementValue_ID,dateTo, 0);
		}	
		
		//Impuesto al Valor Agregado		
		 m_sql = new StringBuffer("select " +
				"NVL(sum(((orl.PriceActual) * orl.QtyEntered) * (tax.rate/100)),0) as impuesto " +
				"from C_OrderLine orl " +
				"inner join C_Order ord on (orl.C_Order_ID = ord.C_Order_ID and orl.dateordered = ord.dateordered) " +
				"inner join AD_Client adc on (orl.AD_Client_ID = adc.AD_Client_ID) " +
				"inner join M_Warehouse war on (orl.M_Warehouse_ID = war.M_Warehouse_ID) " +
				"inner join M_Product pro on (orl.M_Product_ID = pro.M_Product_ID) " +
				"inner join XX_VMR_Department dep on (pro.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
				"inner join C_Tax tax on (orl.C_Tax_ID = tax.C_Tax_ID) " +
				"where ord.IsSotrx = 'Y' " +				
				"and ord.dateordered between TO_DATE('"+ year + String.format("%02d", month) +"01','YYYYMMDD') " +
					"and last_day(TO_DATE('"+ year + String.format("%02d", month) +"01','YYYYMMDD')) " +
				"and M_Warehouse_ID = " + m_Warehouse_ID + " " +
				"and ord.M_WAREHOUSE_ID = " + m_Warehouse_ID + " " +
				"and ord.AD_CLIENT_ID = " + m_AD_Client_ID );	
		
		sql = m_sql.toString();
		c_ElementValue_ID = getC_Elementvalue_id("IVA","L");  
		result = result && executeDetailCV(sql, 3, headID, c_ElementValue_ID,dateTo, 0);
		
		//Diferencia entre debe y haber
		X_XX_VCN_AccoutingEntry accountingEntry = new X_XX_VCN_AccoutingEntry(Env.getCtx(), headID, null);
		BigDecimal dif = accountingEntry.getXX_TotalShould().subtract(accountingEntry.getXX_TotalHave());
		
		if(dif.compareTo(BigDecimal.ZERO)==0)
			return result;
		
		m_sql = new StringBuffer("select "+dif+" from dual");
		
		sql = m_sql.toString();
		c_ElementValue_ID = getC_Elementvalue_id("APC","L");
		result = result && executeDetailCV(sql, 4, headID, c_ElementValue_ID,dateTo, 0);
		
		return result;
	}
	
	/**
	 * Procesamiento para los asientos de compra/venta
	 * @param dateFrom Fecha desde
	 * @param dateTo Fecha hasta
	 * @param process Numero de procesamiento
	 * @param headID Identificador de la cabezera
	 * @return Success boolean
	 */
	public Boolean buildDetailCVInventoryChange(Timestamp dateFrom, Timestamp dateTo, int headID){
		
		boolean result = true;
		String sql = "";
		int c_ElementValue_ID = 0;	
		line = 0;
		
		m_sql = new StringBuffer("select round(nvl(sum(case when sp.xx_typereg = 09 or sp.xx_typereg = 26 then sp.xx_amountmonth else 0 end),0) " +
				" - nvl(sum(case when sp.xx_typereg = 19 or sp.xx_typereg = 21 then sp.xx_amountmonth else 0 end),0),2) " +
				"from xx_vcn_salepurchase sp " +
				"where sp.xx_year= " + year + " " +
				"and sp.xx_month= " + month + " " +
				"and sp.m_warehouse_id = "  + m_Warehouse_ID + " " +
				"and sp.ad_client_id = " + m_AD_Client_ID + " " +
				"and (sp.xx_typereg= 09 or sp.xx_typereg = 21 or sp.xx_typereg = 19 or sp.xx_typereg = 26)"); 
				
		
		sql = m_sql.toString();
		c_ElementValue_ID = getC_Elementvalue_id("CDI","A");  
		result = result && executeDetailCV(sql, 1, headID, c_ElementValue_ID,dateTo, 0);
		
		m_sql = new StringBuffer("select round(nvl(sum(case when sp.xx_typereg = 19 or sp.xx_typereg = 21 then sp.xx_amountmonth else 0 end),0) " +
				"- nvl(sum(case when sp.xx_typereg = 09 or sp.xx_typereg = 26 then sp.xx_amountmonth else 0 end),0),2) " +
				"from xx_vcn_salepurchase sp " +
				"where sp.xx_year= " + year + " " +
				"and sp.xx_month= " + month + " " +
				"and sp.m_warehouse_id = "  + m_Warehouse_ID + " " +
				"and sp.ad_client_id = " + m_AD_Client_ID + " " +
				"and (sp.xx_typereg= 09 or sp.xx_typereg = 21 or sp.xx_typereg = 19 or sp.xx_typereg = 26)"); 
				
		
		sql = m_sql.toString();
		c_ElementValue_ID = getC_Elementvalue_id("CDI","E");  
		result = result && executeDetailCV(sql, 2, headID, c_ElementValue_ID, dateTo, 0);
		
		return result;
	}
}


