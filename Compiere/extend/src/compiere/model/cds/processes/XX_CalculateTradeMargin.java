package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.X_Ref_XX_MovementType;

/*** Proceso que calcula el porcentaje de cumplimiento del margen comercial con respecto al margen presupuestado
 * @author ghuchet*/

public class XX_CalculateTradeMargin extends SvrProcess {

	int year, month, SITME;
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, -1);
		month = date.get(Calendar.MONTH) + 1;
		year = date.get(Calendar.YEAR);

//		month =1; 
//		year = 2014;
		
		//Inicializar costo - solo la primera vez
//		calculateImportedTradeFactor(true);
//		calculateImportedTradeFactor(false);
//		InitializelTradeCost();
		//FIN Inicializar costo solo la primera vez
		
		//Busca el costo origen y lo guarda en una tabla temporal por producto
		deleteSalesOriginCost();
		calculateSalesOriginCost();
		
		//Calcula el margen comercial y lo guarda en una tabla temporal por departamento
		deleteBudgetMarginDep();
		calculateBudgetMarginDep();
		
		//Calcula el margen comercial y lo guarda en una tabla temporal por categoría
		deleteBudgetMarginCat();
		calculateBudgetMarginCat();
		
		//Calcula el descuento de empleado y promocional de las ventas por departamento y categoría
		deleteEmployeeDiscount();
		calculateEmployeeDiscount();
		deleteEmployeeDiscountCat();
		calculateEmployeeDiscountCat();
		
		//Calcula las rebajas definitivas por departamento y categoría
		deleteDefinitiveDiscountCat();
		deleteDefinitiveDiscount();
		calculateDefinitiveDiscount();
		calculateDefinitiveDiscountCat();
		
		//Calcula los ajustes y lo guarda en una tabla temporal por departamento y  categoría 
		deleteTradeAdjustments();
		calculateTradeAdjustments();
		deleteTradeAdjustmentsCat();
		calculateTradeAdjustmentsCat();
		
		//Calcula Rebajas a 0 y lo guarda en una tabla temporal por departamento y categoría
		deleteZeroDiscount();
		calculateZeroDiscount();
		deleteZeroDiscountCat();
		calculateZeroDiscountCat();

		//Calcula el Margen comercial
		deleteTradeMargin();
		calculateTradeMargin();
		calculateTradeMarginCat();
		
		
		return "";
	}



	private boolean deleteSalesOriginCost() {
		try{
			String sql = "DROP TABLE XX_TEMP_SALESORIGENCOSTS";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			System.out.println("No se borro la tabla XX_TEMP_SALESORIGENCOSTS porque la misma no existe");
			//e.printStackTrace();
			return false;
		}

		return true;
	}
	
	private boolean calculateSalesOriginCost() {
		
		try{

			String sql = "CREATE TABLE XX_TEMP_SALESORIGENCOSTS AS (SELECT " +
					"\n INV.M_PRODUCT_ID M_PRODUCT_ID, NVL(INV.M_AttributeSetInstance_ID,0) M_AttributeSetInstance_ID, " +
					"\n CASE WHEN max(nvl(LRP.XX_TradeCost,0)) < 0.02 THEN max(nvl(INV.XX_INITIALINVENTORYCOSTPRICE,0)) ELSE max(nvl(LRP.XX_TradeCost,0)) END COST, " +
					"\n max(nvl(INV.XX_INITIALINVENTORYCOSTPRICE,0)) BOOKCOST, " +
					"\n INV.XX_INVENTORYMONTH  XX_MONTH, INV.XX_INVENTORYYEAR  XX_YEAR " +
					"\n FROM XX_VCN_INVENTORY INV " +
					"\n JOIN XX_VMR_DEPARTMENT D ON (D.XX_VMR_DEPARTMENT_ID = INV.XX_VMR_DEPARTMENT_ID) " +
					"\n JOIN M_PRODUCT P ON (P.M_Product_ID= INV.M_Product_ID) " +
					"\n LEFT JOIN M_InOutLine IOL ON (NVL(IOL.M_AttributeSetInstance_ID,0) = NVL(INV.M_AttributeSetInstance_ID,0) " +
					"\n AND IOL.M_Product_ID= INV.M_Product_ID) " +
					"\n LEFT JOIN M_InOut IO ON (IO.M_INOUT_ID = IOL.M_INOUT_ID  AND IO.ISSOTRX = 'N' ) " +
					"\n LEFT JOIN C_ORDER O ON (O.C_Order_ID = IO.C_ORDER_ID AND O.ISSOTRX = 'N' ) " +
					"\n LEFT JOIN XX_VMR_PO_LineRefProv LRP ON (P.XX_VMR_vendorProdRef_ID = LRP.XX_VMR_vendorProdRef_ID" +
					"\n AND LRP.C_Order_ID = O.C_Order_ID) " +
					"\n WHERE INV.XX_INVENTORYMONTH = "+month+" AND INV.XX_INVENTORYYEAR = " +year+
					"\n AND (INV.XX_SALESAMOUNT != 0 or XX_SALESQUANTITy != 0) " +
					"\n GROUP BY  INV.M_PRODUCT_ID, NVL(INV.M_AttributeSetInstance_ID,0), INV.XX_INVENTORYMONTH, INV.XX_INVENTORYYEAR " +
					"\n )";
			
//			String sql = "CREATE TABLE XX_TEMP_SALESORIGENCOSTS AS (SELECT " +
//					"\n P.M_PRODUCT_ID M_PRODUCT_ID, NVL(INV.M_AttributeSetInstance_ID,0) M_AttributeSetInstance_ID, " +
//					"\n max(nvl(ASI.XX_TradeCost,0)) COST, " +
//					"\n INV.XX_INVENTORYMONTH  XX_MONTH, INV.XX_INVENTORYYEAR  XX_YEAR " +
//					"\n FROM XX_VCN_INVENTORY INV " +
//					"\n JOIN M_AttributeSetInstance ASI ON (ASI.M_AttributeSetInstance_ID = INV.M_AttributeSetInstance_ID) " +
//					"\n WHERE INV.XX_INVENTORYMONTH = "+month+" AND INV.XX_INVENTORYYEAR = " +year+
//					"\n GROUP BY  INV.M_PRODUCT_ID, NVL(INV.M_AttributeSetInstance_ID,0), INV.XX_INVENTORYMONTH, INV.XX_INVENTORYYEAR" +
//					"\n )";

			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	private boolean deleteBudgetMarginDep() {
		try{
			String sql = " DROP TABLE XX_TEMP_MARGINBUDGET ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			System.out.println("No se borro la tabla XX_TEMP_MARGINBUDGET porque la misma no existe");
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean calculateBudgetMarginDep() {
		
		try{
			String sql = "CREATE TABLE XX_TEMP_MARGINBUDGET AS (SELECT  " +
					"\n XX_VMR_Department_ID, XX_VMR_Category_ID, sum(xx_salesamountbud2) ventasPpto, " +
					"\n round(sum(xx_salesamountbud2 * XX_LISCKGROSSMARGPERCTBUD/100),2) margenBrutoPptoBs, " +
					"\n round(sum(xx_salesamountbud2 * XX_LISCKGROSSMARGPERCTBUD)/ " +
					"\n decode(sum(xx_salesamountbud2),0,1,sum(xx_salesamountbud2)),2) margenBrutoPptoPerc, " +
					"\n XX_BUDGETYEARMONTH " +
					"\n FROM XX_VMR_PRLD01  " +
					"\n where XX_BUDGETYEARMONTH = "+year+"||(CASE WHEN "+month+"< 10 THEN '0'||"+month+
					"\n ELSE TO_CHAR("+month+") END)  " +
					"\n group by XX_VMR_Department_ID, XX_VMR_Category_ID, XX_BUDGETYEARMONTH)";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	private boolean deleteBudgetMarginCat() {
		try{
			String sql = " DROP TABLE XX_TEMP_MARGINBUDGETCAT ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			System.out.println("No se borro la tabla XX_TEMP_TRADEADJUSTMENTS porque la misma no existe");
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean calculateBudgetMarginCat() {
		
		try{
			String sql = "CREATE TABLE XX_TEMP_MARGINBUDGETCAT AS (SELECT  " +
					"\n XX_VMR_Category_ID, sum(xx_salesamountbud2) ventasPpto, " +
					"\n round(sum(xx_salesamountbud2 * XX_LISCKGROSSMARGPERCTBUD/100),2) margenBrutoPptoBs, " +
					"\n round(sum(xx_salesamountbud2 * XX_LISCKGROSSMARGPERCTBUD)/ " +
					"\n decode(sum(xx_salesamountbud2),0,1,sum(xx_salesamountbud2)),2) margenBrutoPptoPerc, " +
					"\n XX_BUDGETYEARMONTH " +
					"\n FROM XX_VMR_PRLD01  " +
					"\n where XX_BUDGETYEARMONTH = "+year+"||(CASE WHEN "+month+"< 10 THEN '0'||"+month+
					"\n ELSE TO_CHAR("+month+") END)  " +
					"\n group by XX_VMR_Category_ID, XX_BUDGETYEARMONTH)";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	private boolean deleteTradeAdjustments() {
		try{
			String sql = " DROP TABLE XX_TEMP_TRADEADJUSTMENTS ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			System.out.println("No se borro la tabla XX_TEMP_TRADEADJUSTMENTS porque la misma no existe");
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean calculateTradeAdjustmentsTemp() {
		
		try{
			String sql = "CREATE TABLE XX_TEMP_TRADEADJUSTMENTS AS (SELECT  " +
					"\n d.XX_VMR_Department_ID, d.XX_VMR_Category_ID, " +
					"\n NVL((SELECT SUM(s.XX_COSANT) FROM  XX_VLO_Summary s WHERE  s.DataType = 'R' AND d.XX_VMR_Department_ID = s.XX_VMR_Department_ID AND TO_CHAR(XX_DATEREGISTRATION, 'YYYY') = "+year+" AND TO_CHAR(XX_DATEREGISTRATION, 'MM') ="+month+"),0) ADJ_R," +
					"\n NVL((SELECT SUM(s.XX_COSANT) FROM  XX_VLO_Summary s WHERE  s.DataType = 'P' AND d.XX_VMR_Department_ID = s.XX_VMR_Department_ID AND TO_CHAR(XX_DATEREGISTRATION, 'YYYY') = "+year+" AND TO_CHAR(XX_DATEREGISTRATION, 'MM') ="+month+"),0) ADJ_P," +
					"\n NVL((SELECT SUM(s.XX_COSANT) FROM  XX_VLO_Summary s WHERE  s.DataType = 'N' AND d.XX_VMR_Department_ID = s.XX_VMR_Department_ID AND TO_CHAR(XX_DATEREGISTRATION, 'YYYY') = "+year+" AND TO_CHAR(XX_DATEREGISTRATION, 'MM') ="+month+"),0) ADJ_N," +
					"\n NVL((SELECT SUM(s.XX_COSANT) FROM  XX_VLO_Summary s WHERE  s.DataType = 'T' AND d.XX_VMR_Department_ID = s.XX_VMR_Department_ID AND TO_CHAR(XX_DATEREGISTRATION, 'YYYY') = "+year+" AND TO_CHAR(XX_DATEREGISTRATION, 'MM') ="+month+"),0) ADJ_C" +
					"\n FROM XX_VMR_Department d" +
					"\n group by d.XX_VMR_Category_ID, d.XX_VMR_Department_ID) ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	private boolean calculateTradeAdjustments() {
		
		try{
			String sql = "CREATE TABLE XX_TEMP_TRADEADJUSTMENTS AS (SELECT  " +
					"\n d.XX_VMR_Department_ID, d.XX_VMR_Category_ID, " +
					"\n NVL((SELECT SUM(NVL(XX_COSANT,0)+NVL(XX_COSTFREE,0)) FROM  XX_VLO_Summary s WHERE  s.DataType = 'R' AND d.XX_VMR_Department_ID = s.XX_VMR_Department_ID AND TO_CHAR(XX_DATEREGISTRATION, 'YYYY') = "+year+" AND TO_CHAR(XX_DATEREGISTRATION, 'MM') ="+month+"),0) ADJ_R," +
					"\n NVL((SELECT SUM(NVL(XX_COSANT,0)+NVL(XX_COSTFREE,0)) FROM  XX_VLO_Summary s WHERE  s.DataType = 'P' AND d.XX_VMR_Department_ID = s.XX_VMR_Department_ID AND TO_CHAR(XX_DATEREGISTRATION, 'YYYY') = "+year+" AND TO_CHAR(XX_DATEREGISTRATION, 'MM') ="+month+"),0) ADJ_P," +
					"\n NVL((SELECT SUM(NVL(XX_COSANT,0)+NVL(XX_COSTFREE,0)) FROM  XX_VLO_Summary s WHERE  s.DataType = 'N' AND d.XX_VMR_Department_ID = s.XX_VMR_Department_ID AND TO_CHAR(XX_DATEREGISTRATION, 'YYYY') = "+year+" AND TO_CHAR(XX_DATEREGISTRATION, 'MM') ="+month+"),0) ADJ_N," +
					"\n NVL((SELECT SUM(NVL(XX_COSANT,0)+NVL(XX_COSTFREE,0)) FROM  XX_VLO_Summary s WHERE  s.DataType = 'C' AND d.XX_VMR_Department_ID = s.XX_VMR_Department_ID AND TO_CHAR(XX_DATEREGISTRATION, 'YYYY') = "+year+" AND TO_CHAR(XX_DATEREGISTRATION, 'MM') ="+month+"),0) ADJ_C" +
					"\n FROM XX_VMR_Department d" +
					"\n group by d.XX_VMR_Category_ID, d.XX_VMR_Department_ID) ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	
	private boolean deleteTradeAdjustmentsCat() {
		try{
			String sql = " DROP TABLE XX_TEMP_TRADEADJUSTMENTSCAT ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			System.out.println("No se borro la tabla XX_TEMP_TRADEADJUSTMENTSCATS porque la misma no existe");
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean calculateTradeAdjustmentsCat() {
		
		try{
			String sql = "CREATE TABLE XX_TEMP_TRADEADJUSTMENTSCAT AS (SELECT  " +
					"\n C.XX_VMR_Category_ID, " +
					"\n NVL((SELECT SUM(s.ADJ_R) FROM  XX_TEMP_TRADEADJUSTMENTS s WHERE  C.XX_VMR_Category_ID = s.XX_VMR_Category_ID ),0) ADJ_R," +
					"\n NVL((SELECT SUM(s.ADJ_P) FROM  XX_TEMP_TRADEADJUSTMENTS s WHERE  C.XX_VMR_Category_ID = s.XX_VMR_Category_ID ),0) ADJ_P," +
					"\n NVL((SELECT SUM(s.ADJ_N) FROM  XX_TEMP_TRADEADJUSTMENTS s WHERE  C.XX_VMR_Category_ID = s.XX_VMR_Category_ID ),0) ADJ_N," +
					"\n NVL((SELECT SUM(s.ADJ_C) FROM  XX_TEMP_TRADEADJUSTMENTS s WHERE  C.XX_VMR_Category_ID = s.XX_VMR_Category_ID ),0) ADJ_C" +
					"\n FROM XX_VMR_Category C" +
					"\n group by C.XX_VMR_Category_ID) ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	
	private boolean deleteTradeMargin() {
		try{
			String sql = " DELETE FROM XX_VMR_TradeMargin WHERE XX_YEAR = "+year+" and XX_MONTH = "+month+ " and AD_Client_ID = "+Env.getCtx().getAD_Client_ID();
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			System.out.println("No se borro la tabla XX_VMR_TradeMargin porque la misma no existe");
			//e.printStackTrace();
			return false;
		}

		return true;
	}
	
	
	
	
		private boolean calculateTradeMarginTemp() {
		
		try{
			
			//Se inserta la información de las ventas en la tabla XX_VMR_TradeMargin
			int idMax = findMaxID("XX_VMR_TradeMargin");
			
			String sql = "\n INSERT INTO XX_VMR_TradeMargin (XX_VMR_TradeMargin_ID, AD_Client_ID, AD_Org_ID, Created, CreatedBy, Updated, UpdatedBy, IsActive, " +
					"\n XX_VMR_Department_ID, XX_Month, XX_Year, XX_RealSalesAmount, XX_EmployeeDiscount, XX_TradeCost," +
					"\n XX_TradeMargin,  XX_TradeMarginDef, XX_TradeMarginPerc, XX_BudgetMarginPerc, XX_BudgetMargin, XX_BudgetSalesAmount, XX_CompliancePerc," +
					"XX_Adj_R, XX_Adj_P, XX_Adj_N, XX_Adj_C) " +
					"\n (" +
					"\n SELECT  "+ idMax + " + MAX(rownum), "+Env.getCtx().getAD_Client_ID() +" AD_Client_ID,"+Env.getCtx().getAD_Org_ID()+" AD_Org_ID, " +
					"\n SYSDATE Created, 0 CreatedBy,  SYSDATE Updated, 0 UpdatedBy, 'Y' IsActive, INV.XX_VMR_DEPARTMENT_ID, INV.XX_INVENTORYMONTH , INV.XX_INVENTORYYEAR," +
					"\n SUM(INV.XX_SALESAMOUNT) ventasMonto, " +
					"\n NVL(ED.EmployeeDiscount,0) DescuentoEmpleado," +
					"\n sum(INV.XX_SALESQUANTITY * NVL(inv.XX_INITIALINVENTORYCOSTPRICE,0)) costoComercial, " +
					"\n round((SUM(INV.XX_SALESAMOUNT)-sum(INV.XX_SALESQUANTITY * NVL(inv.XX_INITIALINVENTORYCOSTPRICE,0))),2) margenComercialBs," +
					"\n round((SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)-(sum(INV.XX_SALESQUANTITY * NVL(inv.XX_INITIALINVENTORYCOSTPRICE,0)) + ADJ.ADJ_R + ADJ.ADJ_P + ADJ.ADJ_C + ADJ.ADJ_N)),2) margenBsDef," +
					"\n round(DECODE((SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)),0,0,(SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)-(sum(INV.XX_SALESQUANTITY * NVL(inv.XX_INITIALINVENTORYCOSTPRICE,0)) + ADJ.ADJ_R + ADJ.ADJ_P + ADJ.ADJ_C + ADJ.ADJ_N))/(SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0))*100),2) margenPercDef, " +
					"\n PRE.margenBrutoPptoPerc,  " +
					"\n PRE.margenBrutoPptoBs, " +
					"\n PRE.VentasPpto, " +
					"\n ROUND(decode(PRE.margenBrutoPptoBs,0,0,(SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)-(sum(INV.XX_SALESQUANTITY * NVL(inv.XX_INITIALINVENTORYCOSTPRICE,0))+ ADJ.ADJ_R + ADJ.ADJ_P + ADJ.ADJ_C + ADJ.ADJ_N))/PRE.margenBrutoPptoBs),2)*100 PercCumplimiento, " +
					"\n ADJ.ADJ_R," + 
					"\n ADJ.ADJ_P," + 
					"\n ADJ.ADJ_N," + 
					"\n ADJ.ADJ_C" + 
					"\n FROM XX_VCN_INVENTORY INV " +
					"\n JOIN XX_VMR_DEPARTMENT D ON (D.XX_VMR_DEPARTMENT_ID = INV.XX_VMR_DEPARTMENT_ID)" +
					"\n LEFT JOIN XX_TEMP_EmployeeDiscount ED  ON (ED.XX_VMR_DEPARTMENT_ID = INV.XX_VMR_DEPARTMENT_ID)"+
					"\n LEFT JOIN XX_TEMP_MARGINBUDGET PRE ON (PRE.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID" +
					"\n and PRE.XX_BUDGETYEARMONTH = INV.XX_INVENTORYYEAR||(CASE WHEN INV.XX_INVENTORYMONTH  < 10 THEN '0'||INV.XX_INVENTORYMONTH   ELSE TO_CHAR(INV.XX_INVENTORYMONTH) END)) " +
					"\n LEFT JOIN XX_TEMP_TRADEADJUSTMENTS ADJ ON (ADJ.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID)" +
					"\n WHERE INV.XX_INVENTORYMONTH =  "+month+"  AND INV.XX_INVENTORYYEAR = " +year+
					"\n AND (INV.XX_SALESAMOUNT != 0 or XX_SALESQUANTITy != 0) " +
					"\n GROUP BY  INV.XX_VMR_DEPARTMENT_ID,   INV.XX_INVENTORYMONTH , INV.XX_INVENTORYYEAR, " +
					"\n PRE.margenBrutoPptoPerc, PRE.margenBrutoPptoBs, PRE.ventasPpto, ADJ.ADJ_R, ADJ.ADJ_P, ADJ.ADJ_C, ADJ.ADJ_N, NVL(ED.EmployeeDiscount,0) " +
					"\n )";
			System.out.println(sql);
			int inserted = DB.executeUpdate(null, sql);
			System.out.println("insertados:"+ inserted);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	

	private boolean calculateTradeMarginCatTemp() {
		
		try{
			
			//Se inserta la información de las ventas en la tabla XX_VMR_TradeMargin
			int idMax = findMaxID("XX_VMR_TradeMargin");
			
			String sql = "\n INSERT INTO XX_VMR_TradeMargin (XX_VMR_TradeMargin_ID, AD_Client_ID, AD_Org_ID, Created, CreatedBy, Updated, UpdatedBy, IsActive, " +
					"\n XX_VMR_Category_ID, XX_Month, XX_Year, XX_RealSalesAmount,XX_EmployeeDiscount, XX_TradeCost," +
					"\n XX_TradeMargin,  XX_TradeMarginDef, XX_TradeMarginPerc,  XX_BudgetMarginPerc, XX_BudgetMargin, XX_BudgetSalesAmount, XX_CompliancePerc," +
					"XX_Adj_R, XX_Adj_P, XX_Adj_N, XX_Adj_C) " +
					"\n (" +
					"\n SELECT  "+ idMax + " + MAX(rownum), "+Env.getCtx().getAD_Client_ID() +" AD_Client_ID,"+Env.getCtx().getAD_Org_ID()+" AD_Org_ID, " +
					"\n SYSDATE Created, 0 CreatedBy,  SYSDATE Updated, 0 UpdatedBy, 'Y' IsActive, INV.XX_VMR_CATEGORY_ID,  INV.XX_INVENTORYMONTH , INV.XX_INVENTORYYEAR," +
					"\n SUM(INV.XX_SALESAMOUNT) ventasMonto, " +
					"\n NVL(ED.EmployeeDiscount,0) DescuentoEmpleado," +
					"\n sum(INV.XX_SALESQUANTITY * NVL(inv.XX_INITIALINVENTORYCOSTPRICE,0)) costoComercial, " +
					"\n round((SUM(INV.XX_SALESAMOUNT)-sum(INV.XX_SALESQUANTITY * NVL(NVL(inv.XX_INITIALINVENTORYCOSTPRICE,0),0))),2) margenComercialBs," +
					"\n round((SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)-(sum(INV.XX_SALESQUANTITY * NVL(NVL(inv.XX_INITIALINVENTORYCOSTPRICE,0),0)) + ADJ.ADJ_R + ADJ.ADJ_P + ADJ.ADJ_C + ADJ.ADJ_N)),2) margenBsDef," +
					"\n round(DECODE((SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)),0,0,(SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)-(sum(INV.XX_SALESQUANTITY * NVL(inv.XX_INITIALINVENTORYCOSTPRICE,0)) + ADJ.ADJ_R + ADJ.ADJ_P + ADJ.ADJ_C + ADJ.ADJ_N))/(SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0))*100),2) margenPercDef, " +
					"\n PRE.margenBrutoPptoPerc,  " +
					"\n PRE.margenBrutoPptoBs, " +
					"\n PRE.VentasPpto, " +
					"\n ROUND(decode(PRE.margenBrutoPptoBs,0,0,(SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)-(sum(INV.XX_SALESQUANTITY * NVL(NVL(inv.XX_INITIALINVENTORYCOSTPRICE,0),0))+ ADJ.ADJ_R + ADJ.ADJ_P + ADJ.ADJ_C + ADJ.ADJ_N))/PRE.margenBrutoPptoBs),2)*100 PercCumplimiento, " +
					"\n ADJ.ADJ_R," + 
					"\n ADJ.ADJ_P," + 
					"\n ADJ.ADJ_N," + 
					"\n ADJ.ADJ_C" + 
					"\n FROM XX_VCN_INVENTORY INV " +
					"\n LEFT JOIN XX_TEMP_EmployeeDiscountCat ED  ON (ED.XX_VMR_CATEGORY_ID = INV.XX_VMR_CATEGORY_ID)"+
					"\n LEFT JOIN XX_TEMP_MARGINBUDGETCAT PRE ON (PRE.XX_VMR_CATEGORY_ID = INV.XX_VMR_CATEGORY_ID" +
					"\n and PRE.XX_BUDGETYEARMONTH = INV.XX_INVENTORYYEAR||(CASE WHEN INV.XX_INVENTORYMONTH  < 10 THEN '0'||INV.XX_INVENTORYMONTH   ELSE TO_CHAR(INV.XX_INVENTORYMONTH) END)) " +
					"\n LEFT JOIN XX_TEMP_TRADEADJUSTMENTSCAT ADJ ON (ADJ.XX_VMR_CATEGORY_ID = INV.XX_VMR_CATEGORY_ID)" +
					"\n WHERE INV.XX_INVENTORYMONTH =  "+month+"  AND INV.XX_INVENTORYYEAR = " +year+
					"\n AND (INV.XX_SALESAMOUNT != 0 or XX_SALESQUANTITy != 0) " +
					"\n GROUP BY  INV.XX_VMR_CATEGORY_ID,  INV.XX_INVENTORYMONTH , INV.XX_INVENTORYYEAR, " +
					"\n PRE.margenBrutoPptoPerc, PRE.margenBrutoPptoBs, PRE.ventasPpto, ADJ.ADJ_R, ADJ.ADJ_P, ADJ.ADJ_C, ADJ.ADJ_N,  NVL(ED.EmployeeDiscount,0)"+
					"\n )";
			System.out.println(sql);
			int inserted = DB.executeUpdate(null, sql);
			System.out.println("insertados:"+ inserted);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	private boolean calculateEmployeeDiscount() {
		try{
			String sql = "CREATE TABLE XX_TEMP_EmployeeDiscount AS (SELECT " +
					 "\np.xx_vmr_department_id, p.xx_vmr_category_id, " +
					 "\nSUM(ol.xx_employeediscount*QTYORDERED) EmployeeDiscount," +
					 "\nSUM(CASE WHEN OL.PRICEACTUAL IS NOT NULL AND OL.PRICELIST IS NOT NULL AND OL.QTYORDERED IS NOT NULL  " +
					 "\nTHEN (OL.PRICEACTUAL+OL.XX_EMPLOYEEDISCOUNT-OL.PRICELIST)*OL.QTYORDERED  ELSE 0 END)  Promo " +
					 "\nfrom C_Order OC " +
					 "\ninner join C_OrderLine ol ON (oc.c_order_id = ol.c_order_id) " +
					 "\ninner join M_Product p ON (p.m_product_id = ol.m_product_id) " +
					 "\nWHERE oc.docstatus = 'CO' and issotrx='Y' and ol.dateordered = oc.dateordered" +
					 "\nand TO_CHAR(oc.dateordered, 'YYYY') = "+year+" AND TO_CHAR(oc.dateordered, 'MM') ="+month+
					 "\ngroup by  p.xx_vmr_department_id, p.xx_vmr_category_id) ";
			System.out.println(sql);
			int inserted = DB.executeUpdate(null, sql);
			System.out.println("insertados:"+ inserted);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	
	}
	
	private boolean calculateEmployeeDiscountCat() {
		try{
			String sql = "CREATE TABLE XX_TEMP_EmployeeDiscountCat AS (SELECT " +
					 "\nxx_vmr_category_id, " +
					 "\nSUM(EmployeeDiscount) EmployeeDiscount, SUM(Promo) Promo" +
					 "\nfrom XX_TEMP_EmployeeDiscount " +
					 "\ngroup by xx_vmr_category_id) ";
			System.out.println(sql);
			int inserted = DB.executeUpdate(null, sql);
			System.out.println("insertados:"+ inserted);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	
	}
	
	private boolean calculateDefinitiveDiscount() {
	
		try{
			String sql = "CREATE TABLE XX_TEMP_DefinitiveDiscount AS (SELECT " +
					 "\nINV.XX_VMR_DEPARTMENT_ID, INV.XX_VMR_CATEGORY_ID, " +
					 "\nSUM(INV. XX_MOVEMENTAMOUNT) DefinitiveDiscount" +
					 "\nFROM XX_VCN_INVENTORYMOVDETAIL INV " +
					 "\nWHERE  INV.XX_INVENTORYMONTH =  "+month+"  AND INV.XX_INVENTORYYEAR = " +year+
					 "\nAND XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue()+
					 "\ngroup by  INV.XX_VMR_DEPARTMENT_ID, INV.XX_VMR_CATEGORY_ID) ";
			System.out.println(sql);
			int inserted = DB.executeUpdate(null, sql);
			System.out.println("insertados:"+ inserted);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	
	}
	
	private boolean calculateDefinitiveDiscountCat() {
		try{
			String sql = "CREATE TABLE XX_TEMP_DefinitiveDiscountCat AS (SELECT " +
					 "\nXX_VMR_CATEGORY_ID, " +
					 "\nSUM(DefinitiveDiscount) DefinitiveDiscount" +
					 "\nFROM XX_TEMP_DefinitiveDiscount " +
					 "\ngroup by XX_VMR_CATEGORY_ID) ";
	
			System.out.println(sql);
			int inserted = DB.executeUpdate(null, sql);
			System.out.println("insertados:"+ inserted);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	
	}
	

	private boolean deleteEmployeeDiscountCat() {
		try{
			String sql = " DROP TABLE XX_TEMP_EmployeeDiscountCat ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			System.out.println("No se borro la tabla XX_TEMP_EmployeeDiscountCat porque la misma no existe");
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean deleteEmployeeDiscount() {
		try{
			String sql = " DROP TABLE XX_TEMP_EmployeeDiscount ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			System.out.println("No se borro la tabla XX_TEMP_EmployeeDiscount porque la misma no existe");
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean deleteDefinitiveDiscountCat() {
		try{
			String sql = " DROP TABLE XX_TEMP_DefinitiveDiscountCat ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			System.out.println("No se borro la tabla XX_TEMP_DefinitiveDiscountCat porque la misma no existe");
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean deleteDefinitiveDiscount() {
		try{
			String sql = " DROP TABLE XX_TEMP_DefinitiveDiscount ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			System.out.println("No se borro la tabla XX_TEMP_DefinitiveDiscount porque la misma no existe");
			//e.printStackTrace();
			return false;
		}

		return true;
	}
	
	private boolean deleteZeroDiscount() {
		try{
			String sql = " DROP TABLE XX_TEMP_ZERODISCOUNT ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			System.out.println("No se borro la tabla XX_TEMP_ZERODISCOUNT porque la misma no existe");
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	
	private boolean calculateZeroDiscount() {
		
		try{
			String sql = "CREATE TABLE XX_TEMP_ZERODISCOUNT AS (SELECT  PRO.XX_VMR_DEPARTMENT_ID, " +
					"\nSUM(DAD.XX_LOWERINGQUANTITY * PCON.XX_UNITPURCHASEPRICE) AS ZERODISCOUNT " +
					"\nFROM XX_VMR_DISCOUNTREQUEST DR " +
					"\nINNER JOIN XX_VMR_DISCOUNTAPPLIDETAIL DAD ON (DR.XX_VMR_DISCOUNTREQUEST_ID = DAD.XX_VMR_DISCOUNTREQUEST_ID) " +
					"\nINNER JOIN M_PRODUCT PRO ON (DAD.M_PRODUCT_ID = PRO.M_PRODUCT_ID) " +
					"\nINNER JOIN AD_CLIENT ADC ON (DR.AD_CLIENT_ID = ADC.AD_CLIENT_ID)  " +
					"\nINNER JOIN XX_VMR_PRICECONSECUTIVE PCON ON (PCON.XX_VMR_PRICECONSECUTIVE_ID = DAD.XX_VMR_PRICECONSECUTIVE_ID) " +
					"\nWHERE DR.XX_STATUS IN ('AP', 'AN', 'RV') " +
					"\nAND (SELECT NAME FROM XX_VMR_DISCOUNTTYPE WHERE XX_VMR_DISCOUNTTYPE_ID=DAD.XX_VMR_DISCOUNTTYPE_ID) = 'REBAJA  A  CERO' " +
					"\nAND DAD.XX_LOWERINGQUANTITY IS NOT NULL AND DR.XX_MONTHUPDATE = "+month+"AND DR.XX_YEARUPDATE = " +year+
					"\nGROUP BY  PRO.XX_VMR_DEPARTMENT_ID) ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	
	private boolean deleteZeroDiscountCat() {
		try{
			String sql = " DROP TABLE XX_TEMP_ZERODISCOUNTCAT ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			System.out.println("No se borro la tabla XX_TEMP_ZERODISCOUNTCAT porque la misma no existe");
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean calculateZeroDiscountCat() {
		
		try{
			String sql = "CREATE TABLE XX_TEMP_ZERODISCOUNTCAT AS (SELECT  PRO.XX_VMR_CATEGORY_ID, " +
					"\nSUM(DAD.XX_LOWERINGQUANTITY * PCON.XX_UNITPURCHASEPRICE) AS ZERODISCOUNT " +
					"\nFROM XX_VMR_DISCOUNTREQUEST DR " +
					"\nINNER JOIN XX_VMR_DISCOUNTAPPLIDETAIL DAD ON (DR.XX_VMR_DISCOUNTREQUEST_ID = DAD.XX_VMR_DISCOUNTREQUEST_ID) " +
					"\nINNER JOIN M_PRODUCT PRO ON (DAD.M_PRODUCT_ID = PRO.M_PRODUCT_ID) " +
					"\nINNER JOIN AD_CLIENT ADC ON (DR.AD_CLIENT_ID = ADC.AD_CLIENT_ID)  " +
					"\nINNER JOIN XX_VMR_PRICECONSECUTIVE PCON ON (PCON.XX_VMR_PRICECONSECUTIVE_ID = DAD.XX_VMR_PRICECONSECUTIVE_ID) " +
					"\nWHERE DR.XX_STATUS IN ('AP', 'AN', 'RV') " +
					"\nAND (SELECT NAME FROM XX_VMR_DISCOUNTTYPE WHERE XX_VMR_DISCOUNTTYPE_ID=DAD.XX_VMR_DISCOUNTTYPE_ID) = 'REBAJA  A  CERO' " +
					"\nAND DAD.XX_LOWERINGQUANTITY IS NOT NULL AND DR.XX_MONTHUPDATE = "+month+"AND DR.XX_YEARUPDATE = " +year+					
					"\nGROUP BY PRO.XX_VMR_CATEGORY_ID) ";
			System.out.println(sql);
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	
	
	private boolean calculateTradeMargin() {
		
		try{
			
			//Se inserta la información de las ventas en la tabla XX_VMR_TradeMargin
			int idMax = findMaxID("XX_VMR_TradeMargin");
			
			String sql = "\n INSERT INTO XX_VMR_TradeMargin (XX_VMR_TradeMargin_ID, AD_Client_ID, AD_Org_ID, Created, CreatedBy, Updated, UpdatedBy, IsActive, " +
					"\n XX_VMR_Department_ID, XX_Month, XX_Year, XX_RealSalesAmount,XX_EmployeeDiscount, XX_TradeCost," +
					"\n XX_TradeMargin,  XX_TradeMarginDef, XX_TradeMarginPerc, XX_BudgetMarginPerc, XX_BudgetMargin, XX_BudgetSalesAmount, XX_CompliancePerc," +
					"\n XX_Adj_R, XX_Adj_P, XX_Adj_N, XX_Adj_C, XX_Discount, XX_ZeroDiscount) " +
					"\n (" +
					"\n SELECT  "+ idMax + " + MAX(rownum), "+Env.getCtx().getAD_Client_ID() +" AD_Client_ID,"+Env.getCtx().getAD_Org_ID()+" AD_Org_ID, " +
					"\n SYSDATE Created, 0 CreatedBy,  SYSDATE Updated, 0 UpdatedBy, 'Y' IsActive, INV.XX_VMR_DEPARTMENT_ID, INV.XX_INVENTORYMONTH , INV.XX_INVENTORYYEAR," +
					"\n SUM(INV.XX_SALESAMOUNT) ventasMonto, " +
					"\n NVL(ED.EmployeeDiscount,0) DescuentoEmpleado," +
					"\n sum(INV.XX_SALESQUANTITY * nvl(CO.COST,0)) costoComercial, " +
					"\n round((sum(INV.XX_SALESAMOUNT)-sum(INV.XX_SALESQUANTITY * nvl(CO.COST,0)) - NVL(ZD.ZERODISCOUNT,0)),2) margenComercialBs," +
					"\n round((sum(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)-(sum(INV.XX_SALESQUANTITY * nvl(CO.COST,0)) + ADJ.ADJ_R + ADJ.ADJ_P + ADJ.ADJ_C + ADJ.ADJ_N) - NVL(ZD.ZERODISCOUNT,0)),2) margenBsDef," +
					"\n round(DECODE((SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)),0,0,(SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)-(sum(INV.XX_SALESQUANTITY * nvl(CO.COST,0)) + ADJ.ADJ_R + ADJ.ADJ_P + ADJ.ADJ_C + ADJ.ADJ_N) - NVL(ZD.ZERODISCOUNT,0))/(SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0))*100),2) margenPercDef, " +
					"\n PRE.margenBrutoPptoPerc,  " +
					"\n PRE.margenBrutoPptoBs, " +
					"\n PRE.VentasPpto, " +
					"\n ROUND(decode(PRE.margenBrutoPptoBs,0,0,(sum(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)-(sum(INV.XX_SALESQUANTITY * nvl(CO.COST,0))+ ADJ.ADJ_R + ADJ.ADJ_P + ADJ.ADJ_C + ADJ.ADJ_N) - NVL(ZD.ZERODISCOUNT,0))/PRE.margenBrutoPptoBs)*100 ,2) PercCumplimiento, " +
					"\n ADJ.ADJ_R," + 
					"\n ADJ.ADJ_P," + 
					"\n ADJ.ADJ_N," + 
					"\n ADJ.ADJ_C," + 
					"\n (NVL(ED.Promo,0) + NVL (DD.DefinitiveDiscount,0))*(-1) RebajasBs, " +
					"\n NVL(ZD.ZERODISCOUNT,0) rebajasCero "+
					"\n FROM XX_VCN_INVENTORY INV " +
					"\n JOIN XX_VMR_DEPARTMENT D ON (D.XX_VMR_DEPARTMENT_ID = INV.XX_VMR_DEPARTMENT_ID)" +
					"\n LEFT JOIN XX_TEMP_EmployeeDiscount ED  ON (ED.XX_VMR_DEPARTMENT_ID = INV.XX_VMR_DEPARTMENT_ID)"+
					"\n LEFT JOIN XX_TEMP_DefinitiveDiscount DD ON (DD.XX_VMR_DEPARTMENT_ID = INV.XX_VMR_DEPARTMENT_ID)"+
					"\n LEFT JOIN XX_TEMP_MARGINBUDGET PRE ON (PRE.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID" +
					"\n and PRE.XX_BUDGETYEARMONTH = INV.XX_INVENTORYYEAR||(CASE WHEN INV.XX_INVENTORYMONTH  < 10 THEN '0'||INV.XX_INVENTORYMONTH   ELSE TO_CHAR(INV.XX_INVENTORYMONTH) END)) " +
					"\n LEFT JOIN  XX_TEMP_SALESORIGENCOSTS CO ON (CO.M_PRODUCT_ID = INV.M_PRODUCT_ID AND NVL(CO.M_AttributeSetInstance_ID,0)  = NVL(INV.M_AttributeSetInstance_ID,0) AND" +
					"\n INV.XX_INVENTORYMONTH =  CO.XX_MONTH AND INV.XX_INVENTORYYEAR = CO.XX_YEAR)" +
					"\n LEFT JOIN XX_TEMP_TRADEADJUSTMENTS ADJ ON (ADJ.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID)" +
					"\n LEFT JOIN XX_TEMP_ZERODISCOUNT ZD ON (ZD.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID) " +
					"\n WHERE INV.XX_INVENTORYMONTH =  "+month+"  AND INV.XX_INVENTORYYEAR = " +year+
					"\n AND (INV.XX_SALESAMOUNT != 0 or XX_SALESQUANTITy != 0) " +
					"\n GROUP BY  INV.XX_VMR_DEPARTMENT_ID,   INV.XX_INVENTORYMONTH , INV.XX_INVENTORYYEAR, " +
					"\n PRE.margenBrutoPptoPerc, PRE.margenBrutoPptoBs, PRE.ventasPpto, ADJ.ADJ_R, ADJ.ADJ_P, ADJ.ADJ_C, ADJ.ADJ_N, NVL(ED.EmployeeDiscount,0), NVL (DD.DefinitiveDiscount,0),NVL(ED.Promo,0), NVL(ZD.ZERODISCOUNT,0)  " +
					"\n )";
			System.out.println(sql);
			int inserted = DB.executeUpdate(null, sql);
			System.out.println("insertados:"+ inserted);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	

private boolean calculateTradeMarginCat() {
		
		try{
			
			//Se inserta la información de las ventas en la tabla XX_VMR_TradeMargin
			int idMax = findMaxID("XX_VMR_TradeMargin");
			
			String sql = "\n INSERT INTO XX_VMR_TradeMargin (XX_VMR_TradeMargin_ID, AD_Client_ID, AD_Org_ID, Created, CreatedBy, Updated, UpdatedBy, IsActive, " +
					"\n XX_VMR_Category_ID, XX_Month, XX_Year, XX_RealSalesAmount, XX_EmployeeDiscount, XX_TradeCost," +
					"\n XX_TradeMargin,  XX_TradeMarginDef, XX_TradeMarginPerc,  XX_BudgetMarginPerc, XX_BudgetMargin, XX_BudgetSalesAmount, XX_CompliancePerc," +
					"XX_Adj_R, XX_Adj_P, XX_Adj_N, XX_Adj_C, XX_Discount, XX_ZeroDiscount) " +
					"\n (" +
					"\n SELECT  "+ idMax + " + MAX(rownum), "+Env.getCtx().getAD_Client_ID() +" AD_Client_ID,"+Env.getCtx().getAD_Org_ID()+" AD_Org_ID, " +
					"\n SYSDATE Created, 0 CreatedBy,  SYSDATE Updated, 0 UpdatedBy, 'Y' IsActive, INV.XX_VMR_CATEGORY_ID,  INV.XX_INVENTORYMONTH , INV.XX_INVENTORYYEAR," +
					"\n SUM(INV.XX_SALESAMOUNT) ventasMonto, " +
					"\n NVL(ED.EmployeeDiscount,0) DescuentoEmpleado," +
					"\n sum(INV.XX_SALESQUANTITY * nvl(CO.COST,0)) costoComercial, " +
					"\n round((sum(INV.XX_SALESAMOUNT)-sum(INV.XX_SALESQUANTITY * nvl(CO.COST,0)) - NVL(ZD.ZERODISCOUNT,0)),2) margenComercialBs," +
					"\n round((sum(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)-(sum(INV.XX_SALESQUANTITY * nvl(CO.COST,0)) + ADJ.ADJ_R + ADJ.ADJ_P + ADJ.ADJ_C + ADJ.ADJ_N) - NVL(ZD.ZERODISCOUNT,0)),2) margenBsDef," +
					"\n round(DECODE((SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)),0,0,(SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)-(sum(INV.XX_SALESQUANTITY * nvl(CO.COST,0)) + ADJ.ADJ_R + ADJ.ADJ_P + ADJ.ADJ_C + ADJ.ADJ_N) - NVL(ZD.ZERODISCOUNT,0))/(SUM(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0))*100),2) margenPercDef, " +
					"\n PRE.margenBrutoPptoPerc,  " +
					"\n PRE.margenBrutoPptoBs, " +
					"\n PRE.VentasPpto, " +
					"\n ROUND(decode(PRE.margenBrutoPptoBs,0,0,(sum(INV.XX_SALESAMOUNT)-NVL(ED.EmployeeDiscount,0)-(sum(INV.XX_SALESQUANTITY * nvl(CO.COST,0))+ ADJ.ADJ_R + ADJ.ADJ_P + ADJ.ADJ_C + ADJ.ADJ_N) - NVL(ZD.ZERODISCOUNT,0))/PRE.margenBrutoPptoBs)*100 ,2) PercCumplimiento, " +
					"\n ADJ.ADJ_R," + 
					"\n ADJ.ADJ_P," + 
					"\n ADJ.ADJ_N," + 
					"\n ADJ.ADJ_C," +
					"\n (NVL(ED.Promo,0) + NVL (DD.DefinitiveDiscount,0))*(-1) RebajasBs, " +
					"\n NVL(ZD.ZERODISCOUNT,0) rebajasCero "+
					"\n FROM XX_VCN_INVENTORY INV " +
					"\n LEFT JOIN XX_TEMP_EmployeeDiscountCat ED  ON (ED.XX_VMR_CATEGORY_ID = INV.XX_VMR_CATEGORY_ID)"+
					"\n LEFT JOIN XX_TEMP_DefinitiveDiscountCat DD ON (DD.XX_VMR_CATEGORY_ID = INV.XX_VMR_CATEGORY_ID)"+
					"\n LEFT JOIN XX_TEMP_MARGINBUDGETCAT PRE ON (PRE.XX_VMR_CATEGORY_ID = INV.XX_VMR_CATEGORY_ID" +
					"\n and PRE.XX_BUDGETYEARMONTH = INV.XX_INVENTORYYEAR||(CASE WHEN INV.XX_INVENTORYMONTH  < 10 THEN '0'||INV.XX_INVENTORYMONTH   ELSE TO_CHAR(INV.XX_INVENTORYMONTH) END)) " +
					"\n LEFT JOIN XX_TEMP_SALESORIGENCOSTS CO ON (CO.M_PRODUCT_ID = INV.M_PRODUCT_ID AND nvl(CO.M_AttributeSetInstance_ID,0)  = nvl(INV.M_AttributeSetInstance_ID,0) AND" +
					"\n INV.XX_INVENTORYMONTH =  CO.XX_MONTH AND INV.XX_INVENTORYYEAR = CO.XX_YEAR)" +
					"\n LEFT JOIN XX_TEMP_TRADEADJUSTMENTSCAT ADJ ON (ADJ.XX_VMR_CATEGORY_ID = INV.XX_VMR_CATEGORY_ID)" +
					"\n LEFT JOIN XX_TEMP_ZERODISCOUNTCAT ZD ON (ZD.XX_VMR_CATEGORY_ID = INV.XX_VMR_CATEGORY_ID) " +
					"\n WHERE INV.XX_INVENTORYMONTH =  "+month+"  AND INV.XX_INVENTORYYEAR = " +year+
					"\n AND (INV.XX_SALESAMOUNT != 0 or XX_SALESQUANTITy != 0) " +
					"\n GROUP BY  INV.XX_VMR_CATEGORY_ID,  INV.XX_INVENTORYMONTH , INV.XX_INVENTORYYEAR, " +
					"\n PRE.margenBrutoPptoPerc, PRE.margenBrutoPptoBs, PRE.ventasPpto, ADJ.ADJ_R, ADJ.ADJ_P, ADJ.ADJ_C, ADJ.ADJ_N, NVL(ED.EmployeeDiscount,0), NVL (DD.DefinitiveDiscount,0),NVL(ED.Promo,0), NVL(ZD.ZERODISCOUNT,0) "+
					"\n )";
			System.out.println(sql);
			int inserted = DB.executeUpdate(null, sql);
			System.out.println("insertados:"+ inserted);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/** findMaxID
	 * Funcion auxiliar que permite determinar el id maximo existente en una tabla, para así asignar
	 * el siguiente valor al insertar nuevos datos. 
	 * @return IDMax Máximo id en la Tabla  */	
	private static Integer findMaxID(String tabla) {
		String ultimoId = " SELECT MAX("+tabla+"_ID) FROM "+tabla;
		PreparedStatement ultDet = DB.prepareStatement(ultimoId, null);
		ResultSet maximo = null;
		Integer idResQty = 0;
		
		try {
			maximo = ultDet.executeQuery();
			while (maximo.next()) {
				idResQty = maximo.getInt(1); 
			}
		} 
		catch (SQLException e1) { e1.printStackTrace(); }
		finally{
			DB.closeResultSet(maximo);
			DB.closeStatement(ultDet);
		}
		return idResQty;
	} // findMaxID

	

	private void calculateImportedTradeFactor(boolean sitme) {
		
		try {
			String SQL_Update = "\nUPDATE C_ORDER O SET O.XX_DefinitiveTradeFactor = " +
					"\n( " +
					"\n(( " +
					"\n(O.XX_DefinitiveFactor * O.XX_VendorInvoiceAmount) - (O.XX_VendorInvoiceAmount * O.XX_RealExchangeRate) " +
					"\n- " +
					"\nnvl(( " +
					"\n	(SELECT  (nvl(B.XX_RealMerchManCost,0) + nvl(B.XX_FeeRealAmountFac,0)) *  " +
					"\n		( " +
					"\n			( " +
					"\n				(  " +
					"\n					O.XX_VendorInvoiceAmount * O.XX_RealExchangeRate " +
					"\n				)/ " +
					"\n				(	 " +
					"\n					(SELECT SUM(O3.XX_VendorInvoiceAmount) FROM C_ORDER O3  " +
					"\n					WHERE O.XX_VLO_BOARDINGGUIDE_ID = O3.XX_VLO_BOARDINGGUIDE_ID AND O3.ISSOTRX = 'N') * O.XX_RealExchangeRate " +
					"\n				) " +
					"\n			) *100 " +
					"\n		) " +
					"\n	FROM XX_VLO_BOARDINGGUIDE B WHERE O.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID) " +
					"\n	/100),0) "+
					"\n)/O.XX_VendorInvoiceAmount " +
					"\n) + ";
			if(sitme){
					SQL_Update +="\n(select XX_RATE from XX_VMR_TradeExchangeRate where  TRUNC(O.XX_InsertedStatusDate) >= trunc(XX_INITDATE) and   (TRUNC(O.XX_InsertedStatusDate) <  trunc(XX_ENDDATE) OR xx_enddate is null)) ";
			}else {
					SQL_Update += "\n(select XX_RATE from XX_VMR_TradeExchangeRate where  TRUNC(O.XX_APPROVALDATE) >= trunc(XX_INITDATE) and   (TRUNC(O.XX_APPROVALDATE) <  trunc(XX_ENDDATE) OR xx_enddate is null)) ";
			}
			SQL_Update += "\n) " +
					"\nwhere  O.XX_ORDERTYPE = 'Importada'  " +
					"\nAND O.ISSOTRX = 'N' AND O.XX_VendorInvoiceAmount <> 0 AND  O.XX_RealExchangeRate <> 0 ";
			if(sitme){
				SQL_Update +="\n AND   TO_CHAR(o.XX_InsertedStatusDate,'YYYYMM') >= 201312 ";
		}else {
				SQL_Update += "\nAND TO_CHAR(o.XX_APPROVALDATE,'YYYYMM') >= 201312";
		}
			if(sitme){
				SQL_Update += "\nAND O.XX_ORDERSTATUS = 'SIT' ";		
			}else {
				SQL_Update += "\nAND O.XX_ORDERSTATUS IN ( 'AP', 'RE', 'CH') ";
			}
			int updated = DB.executeUpdate(get_Trx(),SQL_Update);
			System.out.println(SQL_Update);
			System.out.println("Updated Factor: "+ updated);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void InitializelTradeCost() {
	
		String SQL_Update;
		int updated;
		try {
			//INICIAL TODOS
			SQL_Update = 
					"\nUPDATE  XX_VMR_PO_LineRefProv LRP SET LRP.XX_TradeCost = " +
					"  (LRP.PriceActual / (SELECT PURCHASEUNIT.XX_UnitConversion FROM XX_VMR_UNITCONVERSION PURCHASEUNIT WHERE PURCHASEUNIT.XX_VMR_UNITCONVERSION_ID =  LRP.XX_VMR_UnitConversion_ID))" +
					" * (SELECT SALEUNIT.XX_UnitConversion FROM  XX_VMR_UNITCONVERSION SALEUNIT WHERE SALEUNIT.XX_VMR_UNITCONVERSION_ID =  LRP.XX_PiecesBySale_ID) " +
					"\nWHERE LRP.c_order_id in " +
					"\n		(SELECT o.c_order_id FROM c_order o WHERE O.XX_ORDERTYPE = 'Nacional'  " +
					"\n		AND O.ISSOTRX = 'N' AND  O.XX_POTYPE = 'POM' " +
					"\n		AND TO_CHAR(o.XX_CHECKUPDATE,'YYYYMM') >= 201001" +
					"\n		)";
			updated = DB.executeUpdate(get_Trx(),SQL_Update);
			commit();
			System.out.println(SQL_Update);
			System.out.println("Updated 1: "+ updated);
		}catch (Exception e){
			System.out.println("Error Inicializar todos");
			e.printStackTrace();
		}
		
		try{

			//update OC IMPORTADAS
			SQL_Update = 
						"\nUPDATE XX_VMR_PO_LineRefProv LRP SET LRP.XX_TradeCost = " +
						"\n		(SELECT ((LRP.XX_CostWithDiscounts / PURCHASEUNIT.XX_UnitConversion)*SALEUNIT.XX_UnitConversion) * ORDEN.XX_DefinitiveTradeFactor " +
						"\n		FROM C_ORDER ORDEN, XX_VMR_UNITCONVERSION PURCHASEUNIT, XX_VMR_UNITCONVERSION SALEUNIT  " +
						"\n		WHERE LRP.C_Order_ID = ORDEN.C_Order_ID  " +
						"\n		AND PURCHASEUNIT.XX_VMR_UNITCONVERSION_ID =  LRP.XX_VMR_UnitConversion_ID " +
						"\n		AND SALEUNIT.XX_VMR_UNITCONVERSION_ID =  LRP.XX_PiecesBySale_ID " +
						"\n		AND ORDEN.ISSOTRX = 'N' AND ORDEN.XX_POTYPE = 'POM' " +
						"\n		) " +
						"\nWHERE LRP.c_order_id in " +
						"\n		(SELECT o.c_order_id FROM c_order o WHERE O.XX_ORDERTYPE = 'Importada'  " +
						"\n		AND O.ISSOTRX = 'N' AND  O.XX_POTYPE = 'POM' " +
						"\n		AND TO_CHAR(o.XX_CHECKUPDATE,'YYYYMM') >= 201312" +
						"\n		)";
			updated = DB.executeUpdate(get_Trx(),SQL_Update);			
			commit();
			System.out.println(SQL_Update);
			System.out.println("Updated 2: "+ updated);
		}catch (Exception e){
			System.out.println("Error Inicializar Importadas");
			e.printStackTrace();
		}
		try{
			//update OC NACIONALES DE ASOCIADOS
			SQL_Update = 
					"\nUPDATE  XX_VMR_PO_LineRefProv LRP SET LRP.XX_TradeCost = " +
					"\n		(SELECT  ((LRP2.XX_CostWithDiscounts / PURCHASEUNIT.XX_UnitConversion)*SALEUNIT.XX_UnitConversion) * ORIGEN.XX_DefinitiveTradeFactor * 1.1 " +
					"\n	FROM C_ORDER ORDEN, C_ORDER ORIGEN,  XX_VMR_PO_LineRefProv LRP2, " +
					"\n	XX_VMR_UNITCONVERSION PURCHASEUNIT, XX_VMR_UNITCONVERSION SALEUNIT " +
					"\n	WHERE ORDEN.ISSOTRX = 'N' AND  ORIGEN.ISSOTRX = 'N' " +
					"\n	AND ORIGEN.C_Order_ID =  ORDEN.Ref_Order_ID  " +
					"\n	AND LRP2.C_Order_ID = ORIGEN.C_Order_ID AND LRP2.isActive = 'Y' " +
					"\n	AND LRP.C_Order_ID = ORDEN.C_Order_ID AND LRP.XX_VMR_VENDORPRODREF_ID = LRP2.XX_VMR_VENDORPRODREF_ID " +
					"\n	AND  PURCHASEUNIT.XX_VMR_UNITCONVERSION_ID =  LRP.XX_VMR_UnitConversion_ID " +
					"\n	AND SALEUNIT.XX_VMR_UNITCONVERSION_ID =  LRP.XX_PiecesBySale_ID " +
					"\n AND origen.c_order_id not in (1543058,2215760) " +
					"\n		)" +
					"\nWHERE LRP.c_order_id in " +
					"\n		(SELECT o.c_order_id FROM c_order o " +
					"\n		WHERE O.XX_ORDERTYPE = 'Nacional' and  O.XX_ComesFromSITME = 'Y' AND O.ISSOTRX = 'N' " +
					"\n		AND TO_CHAR(o.XX_CHECKUPDATE,'YYYYMM') >= 201312 AND  O.XX_POTYPE = 'POM'" +
					"\n		)" +
					"\nAND LRP.isActive = 'Y' ";
			updated = DB.executeUpdate(get_Trx(),SQL_Update);
			commit();
			System.out.println(SQL_Update);
			System.out.println("Updated 3: "+ updated);
		}catch (Exception e){
			System.out.println("Error Inicializar Nacional de Asociados");
			e.printStackTrace();
		}
		
	}

}
