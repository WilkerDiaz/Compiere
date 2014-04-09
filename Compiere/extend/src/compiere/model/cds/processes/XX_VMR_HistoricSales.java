package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class XX_VMR_HistoricSales  extends SvrProcess{
	@Override
	protected String doIt() throws Exception {	
		String msj = "";
		Calendar cal = Calendar.getInstance();
		int anioMes = ((cal.get(Calendar.YEAR)*100)+cal.get(Calendar.MONTH)+1)+cal.get(Calendar.DAY_OF_MONTH);
		String SQLSales = "";
		PreparedStatement psSales = null;
		ResultSet rsSales = null;
		
		//Se inserta la información de las ventas en la tabla XX_VMR_HISTORICSALES
		int idMax = findMaxID("XX_VMR_HISTORICSALES");
		
		SQLSales = " INSERT INTO XX_VMR_HISTORICSALES (" +
				" XX_VMR_HISTORICSALES_ID, Value, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, " +
				" Updated, UpdatedBy, "+
				" M_WAREHOUSE_ID, XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID," +	
				" XX_VMR_SALESDATE, XX_VMR_SALESACUMAMOUNT, XX_VMR_SALESACUMPIECES, XX_VMR_SALESBUTGETAMOUNT)  " +
				"(SELECT "+ idMax + " + rownum , TO_CHAR("+ idMax + " + rownum ), Client_ID, OrgID, 'Y' ISACTIVE, TODAYDATE1, " +
				" CreatedToday, TODAYDATE2, UpdatedToday," +	
				" WAREHOUSE_ID, CATEGORY_ID, DEPARTMENT_ID, LINE_ID, SECTION_ID, " +
				" DATEORDERED, AMOUNTSALES, PIECESSALES,  BUDGETAMOUNT "+
				" FROM (" +
				" SELECT C.AD_Client_ID Client_ID, C.AD_Org_ID OrgID, 'Y' ISACTIVE,TRUNC(C.DATEORDERED) TODAYDATE1, " +	
				" C.CreatedBy CreatedToday, TRUNC(C.DATEORDERED) TODAYDATE2, " +	
				" C.UpdatedBy UpdatedToday," +
				" C.M_WAREHOUSE_ID WAREHOUSE_ID, P.XX_VMR_CATEGORY_ID CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID DEPARTMENT_ID, " +
				" P.XX_VMR_LINE_ID LINE_ID, P.XX_VMR_SECTION_ID SECTION_ID, C.DATEORDERED DATEORDERED, " +	
				" ROUND(SUM((L.PRICEACTUAL + L.XX_EMPLOYEEDISCOUNT)*L.QTYORDERED),2) AMOUNTSALES," +
				" SUM(L.QTYORDERED) PIECESSALES, PRE.XX_MONESTIR BUDGETAMOUNT " +
				" FROM C_ORDER C INNER JOIN C_ORDERLINE L ON (C.C_ORDER_ID = L.C_ORDER_ID)" +
				" LEFT OUTER JOIN AD_ORG O ON (O.AD_ORG_ID = C.AD_ORG_ID)" +
				" LEFT OUTER JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = L.M_PRODUCT_ID)" +
				" LEFT OUTER JOIN XX_VMR_PRLD03 PRE ON (PRE.XX_VMR_DEPARTMENT_ID = P.XX_VMR_DEPARTMENT_ID" +
				" AND PRE.XX_YEARBUDGET = TO_CHAR(C.DATEORDERED,'YYYY') AND PRE.XX_MONTHBUDGET = TO_CHAR(C.DATEORDERED,'MM') " +
				" AND PRE.XX_BUDGETDAY = TO_CHAR(C.DATEORDERED,'DD') AND PRE.M_WAREHOUSE_ID = C.M_WAREHOUSE_ID)" +
				" WHERE C.ISSOTRX= 'Y' AND C.DOCSTATUS = 'CO' " +
				" AND TRUNC(C.DATEORDERED) =  TRUNC(SYSDATE-1)  " +
				//" AND TRUNC(C.DATEORDERED) between TRUNC(TO_DATE('01/05/2013','DD/MM/YYYY')) and TRUNC(TO_DATE('15/07/2013','DD/MM/YYYY')) " +
				" GROUP BY C.AD_Client_ID, C.AD_Org_ID, 'Y', TRUNC(C.DATEORDERED), C.CreatedBy, TRUNC(C.DATEORDERED), C.UpdatedBy," +
				" C.M_WAREHOUSE_ID, P.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID, " +		// 15
				" C.DATEORDERED, PRE.XX_MONESTIR))";
				System.out.println(SQLSales);

				
		try{
			int updated =  DB.executeUpdate(null, SQLSales);
			System.out.println("Ventas: "+updated);
		}//try		
		catch(Exception e){	
			e.printStackTrace();
		}
		finally{
			DB.closeResultSet(rsSales);
			DB.closeStatement(psSales);
		}
		
		return msj;
	}//doIt

	@Override
	protected void prepare() {
		
	}//prepare	
	
	/** findMaxID
	 * Funcion auxiliar que permite determinar el id maximo existente en una tabla, para así asignar
	 * el siguiente valor al insertar nuevos datos. 
	 * @return IDMax Máximo id en XX_VMR_HISTORICSALES */	
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

} //public class XX_VMR_HistoricSales  extends SvrProcess{

