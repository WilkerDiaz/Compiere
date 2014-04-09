package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.birt.BIRTReport;
import compiere.model.cds.MPeriod;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VCN_SalePurchase;
import compiere.model.payments.processes.XX_SalePurchaseAccEnt;

/**
 * 
 * @author jtrias
 *
 */
public class XX_CalculationSalePurchase extends SvrProcess {

	Integer BP = 0;
	int periodo=0;
	Vector<Integer> warehouses=null;
	Vector<Integer> departments=null;
	String yearmonth = "";
	String years = null;
	String months = null;
	String closes = null;
	Integer periodids = 0;
	Integer month = 0;
	Integer yearFiscal = 0;
	Integer year = 0;
	Utilities util = new Utilities();
	String formatedMonth = "00";
	Map<String,BigDecimal> mapAcum = new HashMap<String,BigDecimal>();
	
	public void setMonthYear(int monthAux, int yearAux){
		month = monthAux;
		year = yearAux;
	}
	
	//Valor Fijo
	String monthFiscal = "00";
	Integer monthFiscalInt = 0;
	boolean rectification = false;
	
	@Override
	protected String doIt() throws Exception {
		
		MPeriod cPeriod = new MPeriod(getCtx(),periodids,get_TrxName()); 
			
		if(closes.equalsIgnoreCase("Y") || closes.equalsIgnoreCase("S")){
			String sql = ("DELETE XX_VCN_SalePurchase WHERE XX_MONTH = '"+month+"' AND XX_YEAR = '"+year+"' ");			
			DB.executeUpdate(null, sql); 
			
			cPeriod.setXX_CloseSalePurchase(true);
			cPeriod.save();
			
			//Calculo de los descuentos de acuerdos comerciales por aporte a publicidad
			util.calculateTradeAgreePubli(yearmonth);
						
		}else{
			String sql = ("DELETE XX_VCN_SalePurchase WHERE XX_MONTH = '"+month+"' AND XX_YEAR = '"+year+"' ");		
			DB.executeUpdate(null, sql);
		}
		
		///Proceso de compra/Venta///
		if(month<7)
			yearFiscal = year-1;
		else
			yearFiscal = year;
		
		//Mes y Año Acumulado
		int monthAcum = 0;
		int yearAcum = 0;
		if(month==1){
			monthAcum = 12;
			yearAcum = year - 1;
		}else{
			monthAcum = month-1;
			yearAcum = year;
		}
		
		//Comienzo año fiscal
		setInitialMonths(cPeriod);
		
		//Ultimo mes (Cierre de Ejecicio)
		boolean last = isLastMonth(cPeriod);
		if(last && closes.equalsIgnoreCase("N")){
			
			if(ADialog.ask( 1, new Container(), Msg.getMsg( getCtx(), "RectificationSalePurchase")))
				rectification=true;
		}
		else if(last && (closes.equalsIgnoreCase("Y") || closes.equalsIgnoreCase("S")))
			rectification=true;
		
		
		//Carga los acumulados;
		if(month!=7)
			loadAcum(monthAcum, yearAcum);
		
		calculateReg_01_02_03_07_17_31();
		if(rectification)
			calculateReg_18_28();
		calculateReg_09_10_14_21_22_25_30();
		calculateReg_15_27();
		calculateReg_26();
		calculateReg_35();
		calculateReg_19_33_36_38();
		calculateReg_39_40();
		
		//Por ultimo se agregan los registros acumulados no encontrados
		if(month!=7)
			addNotFoundAcum(monthAcum, yearAcum);

		if(periodo!=0)
			printReports();
		
		if(closes.equalsIgnoreCase("Y") || closes.equalsIgnoreCase("S")){
			XX_SalePurchaseAccEnt sp = new XX_SalePurchaseAccEnt();
			sp.setMonth(month);
			sp.setYear(year);
			sp.execute();
		}
		
		return Msg.getMsg(Env.getCtx(), "ProcessOK");
	}

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String yearAux="", monthAux="";
		PreparedStatement pstmt3 = null; 
	    ResultSet rs3 = null;
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null){
				;
			}else if (name.equals("C_Year_ID")){
				yearAux = element.getInfo();
			}else if (name.equals("C_Period_ID")){
				monthAux = element.getInfo();
			}else if (name.equals("XX_CloseSalePurchase")){
				closes = element.getInfo().substring(0, 1);
			}
		}
		
		monthAux = monthAux.toUpperCase();
		
		monthAux=monthAux.replaceAll("ENERO ", "01");
		monthAux=monthAux.replaceAll("FEBRERO ", "02");
		monthAux=monthAux.replaceAll("MARZO ", "03");
		monthAux=monthAux.replaceAll("ABRIL ", "04");
		monthAux=monthAux.replaceAll("MAYO ", "05");
		monthAux=monthAux.replaceAll("JUNIO ", "06");
		monthAux=monthAux.replaceAll("JULIO ", "07");
		monthAux=monthAux.replaceAll("AGOSTO ", "08");
		monthAux=monthAux.replaceAll("SEPTIEMBRE ", "09");
		monthAux=monthAux.replaceAll("OCTUBRE ", "10");
		monthAux=monthAux.replaceAll("NOVIEMBRE ", "11");
		monthAux=monthAux.replaceAll("DICIEMBRE ", "12");
		monthAux=monthAux.replaceAll(" ", "");
		monthAux=monthAux.substring(0,2);
		month = Integer.parseInt(monthAux);
		
		if (month<7) 
			year=Integer.parseInt(yearAux.substring(5, 9));
		else 
			year=Integer.parseInt(yearAux.substring(0, 4));
	
		formatedMonth = monthAux;
		
		month = Integer.parseInt(monthAux);
		//year = Integer.parseInt(yearAux);
		yearmonth = year.toString()+monthAux;
		
		//Obteniendo periodo
		String sqlPeriod = ("select max(PeriodNo) as PERIODO, C_Period_ID as ID " +
						    "from C_Period " +
						    "where to_char(StartDate, 'YYYYMM') = '"+yearmonth+"' " +
						    "and IsActive = 'Y' " +
						    "group by C_Period_ID ");
		try{
			pstmt3 = DB.prepareStatement(sqlPeriod, null); 
		    rs3 = pstmt3.executeQuery();
		    if(rs3.next()){ 
		    	periodo = rs3.getInt("PERIODO");
		    	periodids = rs3.getInt("ID");
		    }
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			 try {
				rs3.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			 try {
				pstmt3.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Primer mes del año fiscal
	 */
	private void setInitialMonths(MPeriod period){
		
		String sql = "SELECT * FROM C_Period where C_YEAR_ID = " + period.getC_Year_ID() +" "+
					 "AND PERIODNO = (Select MIN(PERIODNO) from C_Period where C_YEAR_ID = "+ period.getC_Year_ID() +")";
	
		PreparedStatement pstmt = DB.prepareStatement(sql, null);	
		ResultSet rs = null;
		
		try {
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(rs.getTimestamp("STARTDATE"));

				monthFiscal = String.format("%02d", cal.get(Calendar.MONTH)+1);
				monthFiscalInt = cal.get(Calendar.MONTH)+1;
			}
		} 
		catch (SQLException e){
			log.log(Level.SEVERE, sql);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}
	
	/*
	 * Ultimo mes del año fiscal
	 */
	private boolean isLastMonth(MPeriod period){
		
		boolean last = false;

		String sql = "SELECT * FROM C_Period where C_YEAR_ID = " + period.getC_Year_ID() +" "+
					 "AND PERIODNO = (Select MAX(PERIODNO) from C_Period where C_YEAR_ID = "+ period.getC_Year_ID() +")";
	
		PreparedStatement pstmt = DB.prepareStatement(sql, null);	
		ResultSet rs = null;
		
		try {
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				
				if(rs.getInt("C_Period_ID")==period.get_ID())
					last = true;
			}
		} 
		catch (SQLException e){
			log.log(Level.SEVERE, sql);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return last;
	}
	
	/*
	 * 
	 */
	public void printReports(){
	
		//Genera el reporte de las devoluciones consolidados (nacionales)
		generateReportReturns(String.format("%02d", month), String.valueOf(year), 10000005, "ReturnsConsolNational");
		//Genera el reporte de las devoluciones consolidados (Importadas)
		generateReportReturns(String.format("%02d", month), String.valueOf(year), 10000006, "ReturnsConsolNational");
		//Genera el reporte de los traspasos consolidados (salidas)
		generateReport(String.format("%02d", month), String.valueOf(year),"TransfersConsolOut");		
		//Genera el reporte de los traspasos consolidados (entradas)
		generateReport(String.format("%02d", month), String.valueOf(year),"TransfersConsolEntry");     
		//Genera el reporte de los traspasos redistribuidos (salidas-CD)
		generateReport(String.format("%02d", month), String.valueOf(year),"RedistTransfersOut"); 
		//Genera el reporte de los traspasos redistribuidos (entradas-tiendas)
		generateReport(String.format("%02d", month), String.valueOf(year),"RedistTransfersEntry");
		//Reporte AC Aporte Publicidad
		generateReport(String.format("%02d", month), String.valueOf(year), Env.getCtx().getAD_Client_ID(), "TradeAgreementPublicity");
		
		//Capturo los AD_Orgs de los CD para mostrar los reportes
		Vector<Integer> CDs = getCDs();
		for(int i=0; i<CDs.size(); i++){
    		//Genera el reporte de los ajustes de las compras por envío de mercancía a tienda
    		generateReportPSS(String.format("%02d", month), String.valueOf(year),"PurchaseSendStore", CDs.get(i)); 
		}
		
		//Genera el reporte de ventas al contado
		generateReport(String.format("%02d", month), String.valueOf(year),"CashSales");
		//Genera el reporte de las rebajas definitivas
		generateReport(String.format("%02d", month), String.valueOf(year),"Discount"); 
		//Genera el reporte de las rebajas a cero
		generateReport(String.format("%02d", month), String.valueOf(year),"DiscountZero"); 
		//Genera el reporte de cambio de inventario
		generateReport(String.format("%02d", month), String.valueOf(year),"InventoryChange");
		//Genera el reporte de descuentos
		generateReport(String.format("%02d", month), String.valueOf(year),"DiscountsEmployee"); 
		//Genera el reporte de las compras consolidadas Nacional
		generateReport(String.format("%02d", month), String.valueOf(year),"PurchaseConsolidatedNac");  
		//Genera el reporte de las compras consolidadas Importadas
		generateReport(String.format("%02d", month), String.valueOf(year),"PurchaseConsolidatedImp");
		//Genera el reporte de los ajustes importados (Summary)
		generateReport(String.format("%02d", month), String.valueOf(year),"SummaryAdjustments");
	}
	
	/**
	 * Se encarga de generar los reportes después del calculo del cierre de la compra venta
	 * @author Jessica Mendoza
	 * @param month mes
	 * @param year año
	 * @param designName nombre del reporte
	 */
	public void generateReport(String month, String year, String designName){
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("month");
		myReport.parameterValue.add(month);
		myReport.parameterName.add("year");
		myReport.parameterValue.add(year);
		
		if(designName.equalsIgnoreCase("Discount") 
					|| designName.equalsIgnoreCase("CashSales") 
							|| designName.equalsIgnoreCase("DiscountsEmployee")){
			myReport.parameterName.add("yearMonth");
			myReport.parameterValue.add(year+month+"01");
		}
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
	}
	
	
	/**
	 * Se encarga de generar los reportes después del calculo del cierre de la compra venta
	 * @author JTrias
	 * @param month mes
	 * @param year año
	 * @param client compañia
	 * @param designName nombre del reporte
	 */
	public void generateReport(String month, String year, Integer client, String designName){
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("month");
		myReport.parameterValue.add(month);
		myReport.parameterName.add("year");
		myReport.parameterValue.add(year);
		myReport.parameterName.add("client");
		myReport.parameterValue.add(client);
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
	}
	
	/**
	 * Se encarga de generar los reportes de devolucion después del calculo del cierre de la compra venta
	 */
	public void generateReportReturns(String month, String year, int vendorClass, String designName){
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("month");
		myReport.parameterValue.add(month);
		myReport.parameterName.add("year");
		myReport.parameterValue.add(year);
		myReport.parameterName.add("VendorClass");
		myReport.parameterValue.add(vendorClass);
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
	}
	
	
	/**
	 * Se encarga de generar los reportes de ajuste en envio de mercancia
	 */
	public void generateReportPSS(String month, String year, String designName, int warehouse){
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("month");
		myReport.parameterValue.add(month);
		myReport.parameterName.add("year");
		myReport.parameterValue.add(year);
		myReport.parameterName.add("warehouse");
		myReport.parameterValue.add(warehouse);
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
	}
	
	/*
	 * Calculo el registro 01, 02, 03, 07
	 */
	private void calculateReg_01_02_03_07_17_31(){		
		
		String sql = "SELECT " +
					 "oc.m_warehouse_id, p.xx_vmr_department_id, " +
					 "SUM((priceactual+ol.xx_employeediscount)*QTYORDERED) VENTASCONTADO, " +
				     "SUM(ol.xx_employeediscount*QTYORDERED) DESCUENTO, " +
				     "SUM(priceactual*QTYORDERED) VENTASNETAS " +
					 "from C_Order OC " +
					 "inner join C_OrderLine ol ON (oc.c_order_id = ol.c_order_id and issotrx='Y' and ol.dateordered = oc.dateordered) " +
					 "inner join M_Product p ON (p.m_product_id = ol.m_product_id) " +
					 "and oc.docstatus = 'CO' " +
					 "and oc.dateordered between TO_DATE('"+ year + formatedMonth +"01','YYYYMMDD') " +
					 						"and last_day(TO_DATE('"+ year + formatedMonth +"01','YYYYMMDD')) " +
					 "group by oc.m_warehouse_id, p.xx_vmr_department_id "+ 
					 "order by oc.m_warehouse_id";
					
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
			    
		    X_XX_VCN_SalePurchase sp1 = null;
		    X_XX_VCN_SalePurchase sp2 = null;
		    X_XX_VCN_SalePurchase sp3 = null;
		    X_XX_VCN_SalePurchase sp7 = null;
		    X_XX_VCN_SalePurchase sp17 = null;
		    X_XX_VCN_SalePurchase sp31 = null;
		    
		    while(rs.next())
		    {
		    	//01
				sp1 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp1.setM_Warehouse_ID(rs.getInt(1));
		    	sp1.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp1.setXX_Month(month);
		    	sp1.setXX_Year(year);
		    	sp1.setXX_AmountMonth(rs.getBigDecimal(3));
		    	sp1.setXX_TypeReg("01");
		    	//Acumulado
		    	sp1.setXX_AmountAcu(rs.getBigDecimal(3).add(getAcum("01", rs.getInt(1), rs.getInt(2))));
		    	sp1.save();
		    	
		    	//02
		    	sp2 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp2.setM_Warehouse_ID(rs.getInt(1));
		    	sp2.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp2.setXX_Month(month);
		    	sp2.setXX_Year(year);
		    	sp2.setXX_AmountMonth(rs.getBigDecimal(4));
		    	sp2.setXX_TypeReg("02");
		       	//Acumulado
		    	sp2.setXX_AmountAcu(rs.getBigDecimal(4).add(getAcum("02", rs.getInt(1), rs.getInt(2))));
		    	sp2.save();
		    	
		    	//03
		    	sp3 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp3.setM_Warehouse_ID(rs.getInt(1));
		    	sp3.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp3.setXX_Month(month);
		    	sp3.setXX_Year(year);
		    	sp3.setXX_AmountMonth(rs.getBigDecimal(5));
		    	sp3.setXX_TypeReg("03");
		       	//Acumulado
		    	sp3.setXX_AmountAcu(rs.getBigDecimal(5).add(getAcum("03", rs.getInt(1), rs.getInt(2))));
		    	sp3.save();
		    	
		    	//07
		    	sp7 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp7.setM_Warehouse_ID(rs.getInt(1));
		    	sp7.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp7.setXX_Month(month);
		    	sp7.setXX_Year(year);
		    	sp7.setXX_AmountMonth(rs.getBigDecimal(5));
		    	sp7.setXX_TypeReg("07");
		       	//Acumulado
		    	sp7.setXX_AmountAcu(rs.getBigDecimal(5).add(getAcum("07", rs.getInt(1), rs.getInt(2))));
		    	sp7.save();
		    	
		    	//17
		    	sp17 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp17.setM_Warehouse_ID(rs.getInt(1));
		    	sp17.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp17.setXX_Month(month);
		    	sp17.setXX_Year(year);
		    	sp17.setXX_AmountMonth(rs.getBigDecimal(3));
		    	sp17.setXX_TypeReg("17");
		       	//Acumulado
		    	sp17.setXX_AmountAcu(rs.getBigDecimal(3).add(getAcum("17", rs.getInt(1), rs.getInt(2))));
		    	sp17.save();
		    	
		    	//31
		    	sp31 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp31.setM_Warehouse_ID(rs.getInt(1));
		    	sp31.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp31.setXX_Month(month);
		    	sp31.setXX_Year(year);
		    	sp31.setXX_AmountMonth(rs.getBigDecimal(3));
		    	sp31.setXX_TypeReg("31");
		       	//Acumulado
		    	sp31.setXX_AmountAcu(rs.getBigDecimal(3).add(getAcum("31", rs.getInt(1), rs.getInt(2))));
		    	sp31.save();
		    }
		    commit();

		}
		catch (Exception e) {
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
	}
	
	/*
	 * Calculo el registro 09, 10, 14, 21, 22, 25, 30
	 */
	private void calculateReg_09_10_14_21_22_25_30(){
		
		String sql = "WITH " +
						"DISCOUNT AS " +
							"(select war.M_WAREHOUSE_ID, dep.XX_VMR_DEPARTMENT_ID, " +
							"sum(dad.XX_LoweringQuantity * XX_UNITPURCHASEPRICE) as AMOUNT " +
							"from XX_VMR_DiscountRequest dr " +
							"inner join XX_VMR_DiscountAppliDetail dad on (dr.XX_VMR_DiscountRequest_ID = dad.XX_VMR_DiscountRequest_ID) " +
							"left outer join M_Product pro on (dad.M_Product_ID = pro.M_Product_ID) " +
							"inner join M_Warehouse war on (dr.M_Warehouse_ID = war.M_Warehouse_ID) " +
							"inner join AD_Client adc on (dr.AD_Client_ID = adc.AD_Client_ID) " +
							"inner join XX_VMR_Department dep on (pro.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
							"inner join XX_VMR_PRICECONSECUTIVE pcon on (pcon.XX_VMR_PRICECONSECUTIVE_ID = dad.XX_VMR_PRICECONSECUTIVE_ID) " +
							"where dr.XX_Status in ('AP', 'AN', 'RV')  " +
							"and (select name from XX_VMR_DISCOUNTTYPE where XX_VMR_DISCOUNTTYPE_ID=dad.XX_VMR_DISCOUNTTYPE_ID) = 'REBAJA  A  CERO' " + 
							"and dad.XX_LoweringQuantity is not null and dr.XX_MONTHUPDATE = " + month + " and dr.XX_YEARUPDATE = "+ year +" " +
							"group by war.M_WAREHOUSE_ID, dep.XX_VMR_DEPARTMENT_ID), " +
						"DEV AS (select a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID, " +
							"sum(a.XX_MOVEMENTAMOUNT) dev,  sum(b.XX_INITIALINVENTORYCOSTPRICE*a.XX_MOVEMENTQUANTITY) devCost " + 
							"from XX_VCN_INVENTORYMOVDETAIL a, XX_VCN_INVENTORY b " +
							"where " +
							"NVL(a.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(b.M_ATTRIBUTESETINSTANCE_ID, 0) " +
							"AND a.M_PRODUCT_ID = b.M_PRODUCT_ID " +
							"AND a.XX_CONSECUTIVEPRICE = b.XX_CONSECUTIVEPRICE " +
							"AND a.M_WAREHOUSE_ID = b.M_WAREHOUSE_ID " +
							"AND a.XX_INVENTORYYEAR||a.XX_INVENTORYMONTH = b.XX_INVENTORYYEAR||b.XX_INVENTORYMONTH AND " +
							"a.XX_INVENTORYYEAR||a.XX_INVENTORYMONTH = " + year + month +" "+
							"AND XX_MOVEMENTTYPE = 1 " +
							"group by a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID), " +
						"TRAS AS (select a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID, " + 
							"sum(a.XX_MOVEMENTAMOUNT) tras,  sum(b.XX_INITIALINVENTORYCOSTPRICE*a.XX_MOVEMENTQUANTITY) trasCost " + 
							"from XX_VCN_INVENTORYMOVDETAIL a, XX_VCN_INVENTORY b " +
							"where " +
							"NVL(a.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(b.M_ATTRIBUTESETINSTANCE_ID, 0) " +
							"AND a.M_PRODUCT_ID = b.M_PRODUCT_ID " +
							"AND a.XX_CONSECUTIVEPRICE = b.XX_CONSECUTIVEPRICE " +
							"AND a.M_WAREHOUSE_ID = b.M_WAREHOUSE_ID " +
							"AND a.XX_INVENTORYYEAR||a.XX_INVENTORYMONTH = b.XX_INVENTORYYEAR||b.XX_INVENTORYMONTH AND " +
							"a.XX_INVENTORYYEAR||a.XX_INVENTORYMONTH = " + year + month +" "+
							"AND a.XX_MOVEMENTTYPE = 2 " +
							"group by a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID), " +
						"PED AS (select a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID, " + 
							"sum(a.XX_MOVEMENTAMOUNT) ped,  sum(b.XX_INITIALINVENTORYCOSTPRICE*a.XX_MOVEMENTQUANTITY) pedCost " + 
							"from XX_VCN_INVENTORYMOVDETAIL a, XX_VCN_INVENTORY b " +
							"where " +
							"NVL(a.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(b.M_ATTRIBUTESETINSTANCE_ID, 0) " +
							"AND a.M_PRODUCT_ID = b.M_PRODUCT_ID " +
							"AND a.XX_CONSECUTIVEPRICE = b.XX_CONSECUTIVEPRICE " +
							"AND a.M_WAREHOUSE_ID = b.M_WAREHOUSE_ID " +
							"AND a.XX_INVENTORYYEAR||a.XX_INVENTORYMONTH = b.XX_INVENTORYYEAR||b.XX_INVENTORYMONTH AND " +
							"a.XX_INVENTORYYEAR||a.XX_INVENTORYMONTH = " + year + month +" "+
							"AND a.XX_MOVEMENTTYPE = 10 " +
							"group by a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID), " +
						"INVENTORY AS ( " +
							"SELECT a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID, " + 
							"SUM(CASE " +
							    "WHEN a.XX_INVENTORYMONTH = "+ month +" and a.XX_INVENTORYYEAR = "+ year +" THEN XX_INITIALINVENTORYAMOUNT " +
							    "ELSE 0 END) INITINV, " +
							"sum(XX_INITIALINVENTORYAMOUNT) INITINV_ACUM, " +
							"SUM(CASE " +
							    "WHEN a.XX_INVENTORYMONTH = "+ month +" and a.XX_INVENTORYYEAR = "+ year +" THEN XX_SHOPPINGAMOUNT " +
							    "ELSE 0 END) COMP, " +
							"sum(XX_SHOPPINGAMOUNT) COMP_ACUM, " +
							"SUM(CASE " +
								    "WHEN a.XX_INVENTORYMONTH = "+ month +" and a.XX_INVENTORYYEAR = " + year + " THEN (nvl(XX_INITIALINVENTORYCOSTPRICE,0)*XX_INITIALINVENTORYQUANTITY) " +
								    "ELSE 0 END) CostoInvIni, " +
							"SUM(nvl(XX_INITIALINVENTORYCOSTPRICE,0)*XX_INITIALINVENTORYQUANTITY) CostoInvIni_ACUM, " +
							"SUM(CASE " +
								    "WHEN a.XX_INVENTORYMONTH = "+ month +" and a.XX_INVENTORYYEAR = " + year + " THEN (nvl(XX_INITIALINVENTORYCOSTPRICE,0)*XX_SHOPPINGQUANTITY) " +
								    "ELSE 0 END) CostoComp, " +
							"SUM(nvl(XX_INITIALINVENTORYCOSTPRICE,0)*XX_SHOPPINGQUANTITY) CostoComp_ACUM, " +
								    
								//MARGEN GANADO
								"SUM(CASE " +
							    "WHEN a.XX_INVENTORYMONTH = "+ month +" and a.XX_INVENTORYYEAR = " + year + " THEN " +
							    		"(nvl(XX_SALESAMOUNT,0)-(NVL(XX_SALESQUANTITY,0)*NVL(XX_INITIALINVENTORYCOSTPRICE,0))) " +
							    "ELSE 0 END) MargGan, " +
								"SUM(nvl(XX_SALESAMOUNT,0)-(NVL(XX_SALESQUANTITY,0)*NVL(XX_INITIALINVENTORYCOSTPRICE,0))) MargGan_ACUM, " +
								"SUM(NVL(XX_INITIALINVENTORYCOSTPRICE,0)) costo " +
								"FROM XX_VCN_Inventory a " +
								"WHERE " +
								"a.XX_INVENTORYYEAR||a.XX_INVENTORYMONTH = " + year + month +" "+
								"group by  a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID), " +
					
						 "Reg28 AS ( " +
								 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
								 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
								 "FROM XX_VCN_SALEPURCHASE " +
								 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
								 "AND XX_TYPEREG ='28' " +
								 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID) " +
						
						"SELECT a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID, " +
						"sum(nvl(INITINV,0)) INITINV, " +
						
						"(sum(nvl(COMP,0)) + sum(NVL(b.dev,0)) + sum(NVL(c.tras,0)) + sum(NVL(d.ped,0))) COMPRAS, " +
						
						"sum(INITINV) - sum(a.CostoInvIni) MargGanar, " +
						
						"(sum(nvl(COMP,0)) + sum(NVL(b.dev,0)) + sum(NVL(c.tras,0)) + sum(NVL(d.ped,0))) " +
						"- (sum(nvl(CostoComp,0)) + sum(nvl(b.devCost,0)) + sum(nvl(c.trasCost,0)) + sum(nvl(d.pedCost,0))) MargComp, " +
						
						"sum(nvl(INITINV,0)) - sum(nvl(a.CostoInvIni,0)) + " +
						"sum(nvl(COMP,0)) + sum(nvl(b.dev,0)) + sum(nvl(tras,0)) + sum(nvl(ped,0)) - sum(nvl(CostoComp,0)) MargDisp, " +
				
						"(sum(nvl(a.MargGan,0)) - sum(nvl(e.AMOUNT,0))) - sum(nvl(f.AMOUNT,0)) " + 
						
						"from inventory a, dev b, tras c, ped d, Reg28 e, discount f " +
						"WHERE a.M_WAREHOUSE_ID = b.M_WAREHOUSE_ID (+) and a.XX_VMR_DEPARTMENT_ID = b.XX_VMR_DEPARTMENT_ID (+) and " +
						"a.M_WAREHOUSE_ID = c.M_WAREHOUSE_ID (+) and a.XX_VMR_DEPARTMENT_ID = c.XX_VMR_DEPARTMENT_ID (+) and " +
						"a.M_WAREHOUSE_ID = d.M_WAREHOUSE_ID (+) and a.XX_VMR_DEPARTMENT_ID = d.XX_VMR_DEPARTMENT_ID (+) and " +
						"a.M_WAREHOUSE_ID = e.M_WAREHOUSE_ID (+) and a.XX_VMR_DEPARTMENT_ID = e.XX_VMR_DEPARTMENT_ID (+) and " +
						"a.M_WAREHOUSE_ID = f.M_WAREHOUSE_ID (+) and a.XX_VMR_DEPARTMENT_ID = f.XX_VMR_DEPARTMENT_ID (+) " +
						"group by a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID";
					
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    
	    System.out.println(sql);
	    
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
			    
		    X_XX_VCN_SalePurchase sp9 = null;
		    X_XX_VCN_SalePurchase sp10 = null;
		    X_XX_VCN_SalePurchase sp14 = null;
		    X_XX_VCN_SalePurchase sp21 = null;
		    X_XX_VCN_SalePurchase sp22 = null;
		    X_XX_VCN_SalePurchase sp25 = null;
		    X_XX_VCN_SalePurchase sp30 = null;
		    BigDecimal acum = BigDecimal.ZERO;
		    		
		    while(rs.next())
		    {
		    	//09
				sp9 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp9.setM_Warehouse_ID(rs.getInt(1));
		    	sp9.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp9.setXX_Month(month);
		    	sp9.setXX_Year(year);
		    	sp9.setXX_AmountMonth(rs.getBigDecimal(3));
		    	sp9.setXX_TypeReg("09");
		       	//Acumulado
		    	if(month!=7)
		    		acum = getAcum("09", rs.getInt(1), rs.getInt(2));
		    	else
		    		acum = rs.getBigDecimal(3);
		    	
		    	sp9.setXX_AmountAcu(acum);
		    	sp9.save();
		    	
		    	//10
		    	sp10 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp10.setM_Warehouse_ID(rs.getInt(1));
		    	sp10.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp10.setXX_Month(month);
		    	sp10.setXX_Year(year);
		    	sp10.setXX_AmountMonth(rs.getBigDecimal(4));
		    	sp10.setXX_TypeReg("10");
		      	//Acumulado
		    	sp10.setXX_AmountAcu(rs.getBigDecimal(4).add(getAcum("10", rs.getInt(1), rs.getInt(2))));
		    	sp10.save();
		    	
		    	//14
		    	sp14 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp14.setM_Warehouse_ID(rs.getInt(1));
		    	sp14.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp14.setXX_Month(month);
		    	sp14.setXX_Year(year);
		    	sp14.setXX_AmountMonth(rs.getBigDecimal(3).add(rs.getBigDecimal(4)));
		      	//Acumulado
		    	sp14.setXX_AmountAcu(sp9.getXX_AmountAcu().add(sp10.getXX_AmountAcu()));
		    	sp14.setXX_TypeReg("14");
		    	sp14.save();
		    	
		    	//21
		    	sp21 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp21.setM_Warehouse_ID(rs.getInt(1));
		    	sp21.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp21.setXX_Month(month);
		    	sp21.setXX_Year(year);
		    	sp21.setXX_AmountMonth(rs.getBigDecimal(5));
		      	//Acumulado
		    	//Acumulado
		    	if(month!=7)
		    		acum = getAcum("21", rs.getInt(1), rs.getInt(2));
		    	else
		    		acum = rs.getBigDecimal(5);
		    	
		    	sp21.setXX_AmountAcu(acum);
		    	sp21.setXX_TypeReg("21");
		    	sp21.save();
		    	
		    	//22
		    	sp22 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp22.setM_Warehouse_ID(rs.getInt(1));
		    	sp22.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp22.setXX_Month(month);
		    	sp22.setXX_Year(year);
		    	sp22.setXX_AmountMonth(rs.getBigDecimal(6));
		      	//Acumulado
		    	sp22.setXX_AmountAcu(rs.getBigDecimal(6).add(getAcum("22", rs.getInt(1), rs.getInt(2))));
		    	sp22.setXX_TypeReg("22");
		    	sp22.save();
		    	
		    	//25
		    	sp25 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp25.setM_Warehouse_ID(rs.getInt(1));
		    	sp25.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp25.setXX_Month(month);
		    	sp25.setXX_Year(year);
		    	sp25.setXX_AmountMonth(sp21.getXX_AmountMonth().add(sp22.getXX_AmountMonth()).add(BigDecimal.ZERO)); //21+22+23
		      	//Acumulado
		    	sp25.setXX_AmountAcu(sp21.getXX_AmountAcu().add(sp22.getXX_AmountAcu()));
		    	sp25.setXX_TypeReg("25");
		    	sp25.save();
		    	
		    	//30
				sp30 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp30.setM_Warehouse_ID(rs.getInt(1));
		    	sp30.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp30.setXX_Month(month);
		    	sp30.setXX_Year(year);
		    	sp30.setXX_AmountMonth(rs.getBigDecimal(8));
		      	//Acumulado
		    	sp30.setXX_AmountAcu(rs.getBigDecimal(8).add(getAcum("30", rs.getInt(1), rs.getInt(2))));
		    	sp30.setXX_TypeReg("30");
		    	sp30.save();
		    }
		    commit();

		}
		catch (Exception e) {
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
	}
	
	/*
	 * Calculo el registro 15 y 27
	 */
	private void calculateReg_15_27(){
		
		String sql = "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
					 "SUM(CASE " +
					 "WHEN a.XX_INVENTORYMONTH = "+ month +" and a.XX_INVENTORYYEAR = "+ year +" THEN XX_MOVEMENTAMOUNT*-1 " +
					 "ELSE 0 END) reb, " +
					 "SUM(XX_MOVEMENTAMOUNT*-1) reb_ACUM " + 
					 "FROM XX_VCN_INVENTORYMOVDETAIL a " +
					 "WHERE " +
					 "a.XX_INVENTORYYEAR||a.XX_INVENTORYMONTH = "+ year + month +" "+
					 "AND (XX_MOVEMENTTYPE = 4 OR XX_MOVEMENTTYPE = 7) " +
					 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID";
					
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
			    
		    X_XX_VCN_SalePurchase sp15 = null;
		    X_XX_VCN_SalePurchase sp27 = null;
		    
		    while(rs.next())
		    {
		    	//15
				sp15 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp15.setM_Warehouse_ID(rs.getInt(1));
		    	sp15.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp15.setXX_Month(month);
		    	sp15.setXX_Year(year);
		    	sp15.setXX_AmountMonth(rs.getBigDecimal(3));
		      	//Acumulado
		    	sp15.setXX_AmountAcu(rs.getBigDecimal(3).add(getAcum("15", rs.getInt(1), rs.getInt(2))));
		    	sp15.setXX_TypeReg("15");
		    	sp15.save();
		    	
		    	//27
				sp27 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp27.setM_Warehouse_ID(rs.getInt(1));
		    	sp27.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp27.setXX_Month(month);
		    	sp27.setXX_Year(year);
		    	sp27.setXX_AmountMonth(rs.getBigDecimal(3));
		      	//Acumulado
		    	sp27.setXX_AmountAcu(rs.getBigDecimal(3).add(getAcum("27", rs.getInt(1), rs.getInt(2))));
		    	sp27.setXX_TypeReg("27");
		    	sp27.save();
		    }
		    commit();

		}
		catch (Exception e) {
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
	}
	
	/*
	 * Calculo el registro 19, 33, 36, 38
	 */
	private void calculateReg_19_33_36_38(){
		
		String sql = "WITH Reg14 AS ( " +
						 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
						 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
						 "FROM XX_VCN_SALEPURCHASE " +
						 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
						 "AND XX_TYPEREG ='14' " +
						 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Reg15 AS ( " +
						 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
						 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
						 "FROM XX_VCN_SALEPURCHASE " +
						 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
						 "AND XX_TYPEREG ='15' " +
						 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Reg17 AS ( " +
						 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
						 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM "+
						 "FROM XX_VCN_SALEPURCHASE " +
						 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
						 "AND XX_TYPEREG ='17' " +
						 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Reg18 AS ( " +
						 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
						 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
						 "FROM XX_VCN_SALEPURCHASE " +
						 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
						 "AND XX_TYPEREG ='18' " +
						 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Reg25 AS ( " +
						 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
						 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
						 "FROM XX_VCN_SALEPURCHASE " +
						 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
						 "AND XX_TYPEREG ='25' " +
						 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Reg26 AS ( " +
						 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
						 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
						 "FROM XX_VCN_SALEPURCHASE " +
						 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
						 "AND XX_TYPEREG ='26' " +
						 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Reg27 AS ( " +
						 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
						 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
						 "FROM XX_VCN_SALEPURCHASE " +
						 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
						 "AND XX_TYPEREG ='27' " +
						 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Reg28 AS ( " +
						 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
						 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
						 "FROM XX_VCN_SALEPURCHASE " +
						 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
						 "AND XX_TYPEREG ='28' " +
						 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Reg01 AS ( " +
						 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
						 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
						 "FROM XX_VCN_SALEPURCHASE " +
						 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
						 "AND XX_TYPEREG ='01' " +
						 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Reg35 AS ( " +
							 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
							 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
							 "FROM XX_VCN_SALEPURCHASE " +
							 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
							 "AND XX_TYPEREG ='35' " +
							 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Reg30 AS ( " +
							 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
							 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
							 "FROM XX_VCN_SALEPURCHASE " +
							 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
							 "AND XX_TYPEREG ='30' " +
							 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					"Reg07 AS ( " +
							 "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
							 "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
							 "FROM XX_VCN_SALEPURCHASE " +
							 "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
							 "AND XX_TYPEREG ='07' " +
							 "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "WarDep AS (SELECT wh.M_WAREHOUSE_ID, dep.XX_VMR_DEPARTMENT_ID FROM M_WAREHOUSE wh, XX_VMR_DEPARTMENT dep) " +
							
					 "SELECT  wardep.M_WAREHOUSE_ID, wardep.XX_VMR_DEPARTMENT_ID, " +
					 "SUM(nvl(reg14.AMOUNT,0)) - SUM(nvl(reg15.AMOUNT,0)) - SUM(nvl(reg17.AMOUNT,0)) - SUM(nvl(reg18.AMOUNT,0)) AMOUNT19, "+
					 "SUM(nvl(reg14.AMOUNT_ACUM,0)) - SUM(nvl(reg15.AMOUNT_ACUM,0)) - SUM(nvl(reg17.AMOUNT_ACUM,0)) - SUM(nvl(reg18.AMOUNT_ACUM,0)) AMOUNT_ACUM19, " +
					 "SUM(nvl(reg25.AMOUNT,0)) - SUM(nvl(reg26.AMOUNT,0)) - SUM(nvl(reg27.AMOUNT,0)) - SUM(nvl(reg28.AMOUNT,0)) AMOUNT30, " +
					 "SUM(nvl(reg25.AMOUNT_ACUM,0)) - SUM(nvl(reg26.AMOUNT_ACUM,0)) - SUM(nvl(reg27.AMOUNT_ACUM,0)) - SUM(nvl(reg28.AMOUNT_ACUM,0)) AMOUNT_ACUM30, " + 	 
					 
					 "SUM(nvl(reg01.AMOUNT,0)) - SUM(nvl(reg30.AMOUNT,0)) AMOUNT33, " +
					 "SUM(nvl(reg01.AMOUNT_ACUM,0)) -  SUM(nvl(reg30.AMOUNT_ACUM,0)) AMOUNT_ACUM33, " + 
					 
					 "SUM(nvl(reg01.AMOUNT,0)) - SUM(nvl(reg30.AMOUNT,0)) + sum(NVL(reg35.AMOUNT,0)) AMOUNT36," +
					 "SUM(nvl(reg01.AMOUNT_ACUM,0)) - SUM(nvl(reg30.AMOUNT_ACUM,0)) + sum(NVL(reg35.AMOUNT_ACUM,0)) AMOUNT_ACUM36, " + 			  
					 
					 "SUM(nvl(reg07.AMOUNT,0)) - (SUM(nvl(reg01.AMOUNT,0)) - SUM(nvl(reg30.AMOUNT,0)) + sum(NVL(reg35.AMOUNT,0))) AMOUNT38, " +
					 "SUM(nvl(reg07.AMOUNT_ACUM,0)) - (SUM(nvl(reg01.AMOUNT_ACUM,0)) - SUM(nvl(reg30.AMOUNT_ACUM,0)) + sum(NVL(reg35.AMOUNT_ACUM,0))) AMOUNT_ACUM38 " +
					 
					 "FROM wardep, reg14, reg15, reg17, reg18, reg25, reg26, reg27, reg28, reg01, reg35, reg07, reg30 " +
					 "WHERE " +
					 "wardep.M_WAREHOUSE_ID = reg15.M_WAREHOUSE_ID (+) AND wardep.XX_VMR_DEPARTMENT_ID = reg15.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND wardep.M_WAREHOUSE_ID = reg14.M_WAREHOUSE_ID (+) AND wardep.XX_VMR_DEPARTMENT_ID = reg14.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND wardep.M_WAREHOUSE_ID = reg17.M_WAREHOUSE_ID (+) AND wardep.XX_VMR_DEPARTMENT_ID = reg17.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND wardep.M_WAREHOUSE_ID = reg18.M_WAREHOUSE_ID (+) AND wardep.XX_VMR_DEPARTMENT_ID = reg18.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND wardep.M_WAREHOUSE_ID = reg25.M_WAREHOUSE_ID (+) AND wardep.XX_VMR_DEPARTMENT_ID = reg25.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND wardep.M_WAREHOUSE_ID = reg26.M_WAREHOUSE_ID (+) AND wardep.XX_VMR_DEPARTMENT_ID = reg26.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND wardep.M_WAREHOUSE_ID = reg27.M_WAREHOUSE_ID (+) AND wardep.XX_VMR_DEPARTMENT_ID = reg27.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND wardep.M_WAREHOUSE_ID = reg28.M_WAREHOUSE_ID (+) AND wardep.XX_VMR_DEPARTMENT_ID = reg28.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND wardep.M_WAREHOUSE_ID = reg01.M_WAREHOUSE_ID (+) AND wardep.XX_VMR_DEPARTMENT_ID = reg01.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND wardep.M_WAREHOUSE_ID = reg35.M_WAREHOUSE_ID (+) AND wardep.XX_VMR_DEPARTMENT_ID = reg35.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND wardep.M_WAREHOUSE_ID = reg07.M_WAREHOUSE_ID (+) AND wardep.XX_VMR_DEPARTMENT_ID = reg07.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND wardep.M_WAREHOUSE_ID = reg30.M_WAREHOUSE_ID (+) AND wardep.XX_VMR_DEPARTMENT_ID = reg30.XX_VMR_DEPARTMENT_ID (+) " +
				 	 "group by wardep.M_WAREHOUSE_ID, wardep.XX_VMR_DEPARTMENT_ID";
				 	 
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
			    
		    X_XX_VCN_SalePurchase sp19 = null;
		    X_XX_VCN_SalePurchase sp33 = null;
		    X_XX_VCN_SalePurchase sp36 = null;
		    X_XX_VCN_SalePurchase sp38 = null;
		    
		    while(rs.next())
		    {
		    	//19
				sp19 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp19.setM_Warehouse_ID(rs.getInt(1));
		    	sp19.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp19.setXX_Month(month);
		    	sp19.setXX_Year(year);
		    	sp19.setXX_AmountMonth(rs.getBigDecimal(3));
		      	//Acumulado
		    	sp19.setXX_AmountAcu(sp19.getXX_AmountMonth());
		    	sp19.setXX_TypeReg("19");
		    	sp19.save();
		    	
		    	//33
				sp33 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp33.setM_Warehouse_ID(rs.getInt(1));
		    	sp33.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp33.setXX_Month(month);
		    	sp33.setXX_Year(year);
		    	sp33.setXX_AmountMonth(rs.getBigDecimal(7));
		      	//Acumulado
		    	sp33.setXX_AmountAcu(rs.getBigDecimal(7).add(getAcum("33", rs.getInt(1), rs.getInt(2))));
		    	sp33.setXX_TypeReg("33");
		    	sp33.save();
		    	
		    	//36
				sp36 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp36.setM_Warehouse_ID(rs.getInt(1));
		    	sp36.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp36.setXX_Month(month);
		    	sp36.setXX_Year(year);
		    	sp36.setXX_AmountMonth(rs.getBigDecimal(9));
		      	//Acumulado
		    	sp36.setXX_AmountAcu(rs.getBigDecimal(9).add(getAcum("36", rs.getInt(1), rs.getInt(2))));
		    	sp36.setXX_TypeReg("36");
		    	sp36.save();
		    	
		    	//38
				sp38 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp38.setM_Warehouse_ID(rs.getInt(1));
		    	sp38.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp38.setXX_Month(month);
		    	sp38.setXX_Year(year);
		    	sp38.setXX_AmountMonth(rs.getBigDecimal(11));
		      	//Acumulado
		    	sp38.setXX_AmountAcu(rs.getBigDecimal(11).add(getAcum("38", rs.getInt(1), rs.getInt(2))));
		    	sp38.setXX_TypeReg("38");
		    	sp38.save();
		    }
		    commit();

		}
		catch (Exception e) {
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
	}
	
	
	/*
	 * Calculo el registro 39, 40
	 */
	private void calculateReg_39_40(){
		
		String sql = "WITH reg07 AS ( " +
					        "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
					        "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +  
					        "FROM XX_VCN_SALEPURCHASE " +
					        "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
					        "AND XX_TYPEREG ='07' " +
					        "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "reg19 AS ( " +
						 	"SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
						    "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
							"FROM XX_VCN_SALEPURCHASE " +
							"WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
					        "AND XX_TYPEREG ='19' " +
					        "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "reg26 AS ( " +
					        "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
					        "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " + 
					        "FROM XX_VCN_SALEPURCHASE " +
					        "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
					        "AND XX_TYPEREG ='26' " +
					        "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "reg38 AS ( " +
					        "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, " +
					        "SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
					        "FROM XX_VCN_SALEPURCHASE " +
					        "WHERE XX_YEAR = "+ year +" and XX_MONTH = " + month + " " +
					        "AND XX_TYPEREG ='38' " +
					        "group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID) " +
					"SELECT reg07.M_WAREHOUSE_ID, reg07.XX_VMR_DEPARTMENT_ID, " +
					"ROUND(sum(CASE WHEN reg07.AMOUNT <> 0 THEN (reg38.AMOUNT/reg07.AMOUNT)*100 ELSE 0 END),2) AMOUNT39, " +
					"ROUND(sum(CASE WHEN reg07.AMOUNT_ACUM <> 0 THEN (reg38.AMOUNT/reg07.AMOUNT_ACUM)*100 ELSE 0 END),2) AMOUNT_ACUM39, " +
					"ROUND(sum(CASE WHEN NVL(reg19.AMOUNT,0) <> 0 THEN (NVL(reg26.AMOUNT,0)/NVL(reg19.AMOUNT,0))*100 ELSE 0 END),2) AMOUNT40, " +
					"ROUND(sum(CASE WHEN reg19.AMOUNT_ACUM <> 0 THEN (NVL(reg26.AMOUNT,0)/reg19.AMOUNT_ACUM)*100 ELSE 0 END),2) AMOUNT_ACUM40 " +
					"FROM reg07, reg19, reg26, reg38 " +
					"WHERE " +
					"reg07.M_WAREHOUSE_ID = reg19.M_WAREHOUSE_ID (+) AND reg07.XX_VMR_DEPARTMENT_ID = reg19.XX_VMR_DEPARTMENT_ID (+) " +
					"AND reg07.M_WAREHOUSE_ID = reg26.M_WAREHOUSE_ID (+) AND reg07.XX_VMR_DEPARTMENT_ID = reg26.XX_VMR_DEPARTMENT_ID (+) " +
					"AND reg07.M_WAREHOUSE_ID = reg38.M_WAREHOUSE_ID (+) AND reg07.XX_VMR_DEPARTMENT_ID = reg38.XX_VMR_DEPARTMENT_ID (+) " +
					"GROUP BY reg07.M_WAREHOUSE_ID, reg07.XX_VMR_DEPARTMENT_ID";
		
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
			    
		    X_XX_VCN_SalePurchase sp39 = null;
		    X_XX_VCN_SalePurchase sp40 = null;
		    
		    while(rs.next())
		    {
		    	//39
				sp39 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp39.setM_Warehouse_ID(rs.getInt(1));
		    	sp39.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp39.setXX_Month(month);
		    	sp39.setXX_Year(year);
		    	sp39.setXX_AmountMonth(rs.getBigDecimal(3));
		      	//Acumulado
		    	sp39.setXX_AmountAcu(rs.getBigDecimal(3).add(getAcum("39", rs.getInt(1), rs.getInt(2))));
		    	sp39.setXX_TypeReg("39");
		    	sp39.save();
		    	
		    	//40
				sp40 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp40.setM_Warehouse_ID(rs.getInt(1));
		    	sp40.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp40.setXX_Month(month);
		    	sp40.setXX_Year(year);
		    	sp40.setXX_AmountMonth(rs.getBigDecimal(5));
		      	//Acumulado
		    	sp40.setXX_AmountAcu(rs.getBigDecimal(5).add(getAcum("40", rs.getInt(1), rs.getInt(2))));
		    	sp40.setXX_TypeReg("40");
		    	sp40.save();
		    }
		    commit();

		}
		catch (Exception e) {
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
	}
	
	/*
	 * Calculo el registro 35
	 */
	private void calculateReg_35(){
		
		String sql = "SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, ROUND(SUM(NVL(XX_COSANT,0)+NVL(XX_COSTFREE,0)),2) AMOUNT " +
					 "FROM XX_VLO_SUMMARY " +
					 "WHERE C_INVOICE_ID IS NOT NULL AND TO_CHAR(XX_DATEREGISTRATION, 'YYYYMM') = "+ year + formatedMonth + " " +
					 "AND XX_Visible = 'Y' AND M_WAREHOUSE_ID IS NOT NULL " +
					 "GROUP BY M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID " +
					 "ORDER BY M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID";
			
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
			    
		    X_XX_VCN_SalePurchase sp35 = null;
		    
		    while(rs.next())
		    {
		    	//35
				sp35 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp35.setM_Warehouse_ID(rs.getInt(1));
		    	sp35.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp35.setXX_Month(month);
		    	sp35.setXX_Year(year);
		    	sp35.setXX_AmountMonth(rs.getBigDecimal(3));
		      	//Acumulado
		    	sp35.setXX_AmountAcu(rs.getBigDecimal(3).add(getAcum("35", rs.getInt(1), rs.getInt(2))));
		    	sp35.setXX_TypeReg("35");
		    	sp35.save();
		    }
		    commit();

		}
		catch (Exception e) {
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
	}
	
	/*
	 * Calculo el registro 18, 28
	 */
	private void calculateReg_18_28(){
		
		String sql = "SELECT a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID, SUM(a.XX_ADJUSTMENTSAMOUNT*-1) " +
					 "FROM XX_VCN_INVENTORY a "+
					 "WHERE "+
					 "a.XX_INVENTORYYEAR||a.XX_INVENTORYMONTH =  "+ year + month +" " +
					 "GROUP BY a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID "+
					 "ORDER BY a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID";
			
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
			    
		    X_XX_VCN_SalePurchase sp18 = null;
		    X_XX_VCN_SalePurchase sp28 = null;
		    
		    while(rs.next())
		    {
		    	//18
				sp18 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp18.setM_Warehouse_ID(rs.getInt(1));
		    	sp18.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp18.setXX_Month(month);
		    	sp18.setXX_Year(year);
		    	sp18.setXX_AmountMonth(rs.getBigDecimal(3));
		      	//Acumulado
		    	sp18.setXX_AmountAcu(sp18.getXX_AmountMonth());
		    	sp18.setXX_TypeReg("18");
		    	sp18.save();
		    	
		    	//28
				sp28 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp28.setM_Warehouse_ID(rs.getInt(1));
		    	sp28.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp28.setXX_Month(month);
		    	sp28.setXX_Year(year);
		    	sp28.setXX_AmountMonth(rs.getBigDecimal(3));
		      	//Acumulado
		    	sp28.setXX_AmountAcu(sp28.getXX_AmountMonth());
		    	sp28.setXX_TypeReg("28");
		    	sp28.save();
		    }
		    commit();

		}
		catch (Exception e) {
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
	}
	
	/*
	 * Calculo el registro 26
	 */
	private void calculateReg_26(){
		
		String sql = "WITH " +
					 "Reg14 AS ( SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
					 "FROM XX_VCN_SALEPURCHASE WHERE XX_YEAR = "+ year +" and XX_MONTH = "+ formatedMonth +" AND XX_TYPEREG ='14' group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " + 
					 "Reg15 AS ( SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
					 "FROM XX_VCN_SALEPURCHASE WHERE XX_YEAR = "+ year +" and XX_MONTH = "+ formatedMonth +" AND XX_TYPEREG ='15' group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Reg17 AS ( SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
					 "FROM XX_VCN_SALEPURCHASE WHERE XX_YEAR = "+ year +" and XX_MONTH = "+ formatedMonth +" AND XX_TYPEREG ='17' group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Reg18 AS ( SELECT M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, SUM(XX_AMOUNTMONTH) AMOUNT, SUM(XX_AMOUNTACU) AMOUNT_ACUM " +
					 "FROM XX_VCN_SALEPURCHASE WHERE XX_YEAR = "+ year +" and XX_MONTH = "+ formatedMonth +" AND XX_TYPEREG ='18' group by M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID), " +
					 "Inventory AS ( " +
					 "SELECT a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID, " +
					 "SUM(CASE " +
					 "WHEN a.XX_INVENTORYMONTH = "+ formatedMonth +" and a.XX_INVENTORYYEAR = "+ year +" THEN " +
					    "(( NVL(XX_INITIALINVENTORYQUANTITY,0) + NVL(XX_SHOPPINGQUANTITY,0) - NVL(XX_SALESQUANTITY,0) + NVL(XX_MOVEMENTQUANTITY,0) ) " +
					        "* XX_INITIALINVENTORYCOSTPRICE) " +
					    "ELSE 0 END) INVFINALCOSTO, " +
					 "sum((NVL(XX_INITIALINVENTORYQUANTITY,0) + NVL(XX_SHOPPINGQUANTITY,0) - NVL(XX_SALESQUANTITY,0) + NVL(XX_MOVEMENTQUANTITY,0) ) " +
					 "* NVL(XX_INITIALINVENTORYCOSTPRICE,0)) INVFINALCOSTO_ACUM " +
					 "FROM XX_VCN_INVENTORY a " +
					 "WHERE " +
					 "a.XX_INVENTORYYEAR||a.XX_INVENTORYMONTH = "+ year + month +" " +
					 "group by a.M_WAREHOUSE_ID, a.XX_VMR_DEPARTMENT_ID) " +
					 "SELECT  reg14.M_WAREHOUSE_ID, reg14.XX_VMR_DEPARTMENT_ID, " +
					 "SUM(nvl(reg14.AMOUNT,0)) - SUM(nvl(reg15.AMOUNT,0)) - SUM(nvl(reg17.AMOUNT,0)) - SUM(nvl(reg18.AMOUNT,0)) reg19, " +
				 	 "SUM(nvl(reg14.AMOUNT_ACUM,0)) - SUM(nvl(reg15.AMOUNT_ACUM,0)) " +
				 	 "- SUM(nvl(reg17.AMOUNT_ACUM,0)) - SUM(nvl(reg18.AMOUNT_ACUM,0)) reg19_ACUM, " +
			 		 "SUM(nvl(reg14.AMOUNT,0)) - SUM(nvl(reg15.AMOUNT,0)) - SUM(nvl(reg17.AMOUNT,0)) - SUM(nvl(reg18.AMOUNT,0)) - sum(INVFINALCOSTO) reg26, " +
					 "SUM(nvl(reg14.AMOUNT_ACUM,0)) - SUM(nvl(reg15.AMOUNT_ACUM,0)) - SUM(nvl(reg17.AMOUNT_ACUM,0)) - SUM(nvl(reg18.AMOUNT_ACUM,0)) - sum(INVFINALCOSTO_ACUM) reg26_ACUM " +
					 "FROM reg14, reg15, reg17, reg18, inventory " +
					 "WHERE reg14.M_WAREHOUSE_ID = reg15.M_WAREHOUSE_ID (+) AND reg14.XX_VMR_DEPARTMENT_ID = reg15.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND reg14.M_WAREHOUSE_ID = reg17.M_WAREHOUSE_ID (+) AND reg14.XX_VMR_DEPARTMENT_ID = reg17.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND reg14.M_WAREHOUSE_ID = reg18.M_WAREHOUSE_ID (+) AND reg14.XX_VMR_DEPARTMENT_ID = reg18.XX_VMR_DEPARTMENT_ID (+) " +
					 "AND reg14.M_WAREHOUSE_ID = inventory.M_WAREHOUSE_ID (+) AND reg14.XX_VMR_DEPARTMENT_ID = inventory.XX_VMR_DEPARTMENT_ID (+) " +
					 "group by reg14.M_WAREHOUSE_ID, reg14.XX_VMR_DEPARTMENT_ID";
				 	  
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
			    
		    //X_XX_VCN_SalePurchase sp19 = null;
		    X_XX_VCN_SalePurchase sp26 = null;
		    
		    while(rs.next())
		    {
		    
		    	//26
				sp26 = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp26.setM_Warehouse_ID(rs.getInt(1));
		    	sp26.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp26.setXX_Month(month);
		    	sp26.setXX_Year(year);
		    	sp26.setXX_AmountMonth(rs.getBigDecimal(5));
		      	//Acumulado
			    sp26.setXX_AmountAcu(sp26.getXX_AmountMonth());
		    	sp26.setXX_TypeReg("26");
		    	sp26.save();
		    }
		    commit();

		}
		catch (Exception e) {
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
	}
	
	/*
	 * Carga los acumulados del mes anterior
	 */
	private void loadAcum(int pastMonth, int pastYear){
		
		String sql = "SELECT XX_TypeReg||'-'|| m_warehouse_id||'-'||xx_vmr_department_id key, SUM(XX_AMOUNTACU) amount " +
					 "FROM XX_VCN_SalePurchase sp " +
					 "WHERE XX_Month = " + pastMonth +" AND XX_Year = " + pastYear + " " +
					 "GROUP BY XX_TypeReg, m_warehouse_id, xx_vmr_department_id " +
					 "ORDER BY XX_TypeReg, M_WAREHOUSE_ID, xx_vmr_department_id";
		
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
		    
		    while(rs.next())
		    {
		    	mapAcum.put(rs.getString(1), rs.getBigDecimal(2));
		    }
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}
	
	
	/*
	 * Obtiene el monto para el tipo de registro, tienda y departamento indicado
	 */
	private BigDecimal getAcum(String reg, Integer warehouse, Integer department){
		
		if(mapAcum==null)
			return BigDecimal.ZERO;
		
		String key = reg+"-"+warehouse+"-"+department;
		BigDecimal amount = mapAcum.get(key);
		
		if(amount==null)
			amount = BigDecimal.ZERO;
		
		return amount;
	}
	
	private void addNotFoundAcum(int monthAcum, int yearAcum){
		
		String sql = "SELECT m_warehouse_id, xx_vmr_department_id department, XX_TypeReg " +
					 "FROM XX_VCN_SalePurchase sp " +
					 "WHERE XX_Month = "+ monthAcum +" AND XX_Year = "+ yearAcum +" " +
					 "GROUP BY XX_TypeReg, m_warehouse_id, xx_vmr_department_id " +
					 "MINUS " +
					 "SELECT m_warehouse_id, xx_vmr_department_id, XX_TypeReg " +
					 "FROM XX_VCN_SalePurchase sp " +
					 "WHERE XX_Month = "+ month +" AND XX_Year = "+ year +" " +
					 "GROUP BY XX_TypeReg, m_warehouse_id, xx_vmr_department_id";
		
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    X_XX_VCN_SalePurchase sp = null;
	    
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
		    
		    while(rs.next())
		    {
		    	
		    	sp = new X_XX_VCN_SalePurchase(getCtx(),0,get_TrxName());
		    	sp.setM_Warehouse_ID(rs.getInt(1));
		    	sp.setXX_VMR_Department_ID(rs.getInt(2));
		    	sp.setXX_Month(month);
		    	sp.setXX_Year(year);
		    	sp.setXX_AmountMonth(BigDecimal.ZERO);
			    sp.setXX_AmountAcu(getAcum(rs.getString(3), rs.getInt(1), rs.getInt(2)));
		    	sp.setXX_TypeReg(rs.getString(3));
		    	sp.save();
		    	
		    	commit();
		    }
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}	
	}
	
	
	private Vector<Integer> getCDs(){
		
		Vector<Integer> cds = new Vector<Integer>();
		
		String sql = "SELECT AD_ORG_ID FROM XX_VMR_Order " +
					 "where AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID() + " AND " +
					 "to_char(XX_DateStatusOnStore,'MM') = " + month + " " +
					 "and to_char(XX_DateStatusOnStore,'yyyy') = " + year + " " +
					 "group by AD_ORG_ID ";
	
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
		    
		    while(rs.next())
		    {
		    	cds.add(rs.getInt(1));
		    }
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}	
		
		return cds;
	}
	
}// End


	
