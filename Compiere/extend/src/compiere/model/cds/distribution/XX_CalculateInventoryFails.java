package compiere.model.cds.distribution;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.X_XX_VMR_Factor;

/** Crea y llena las tablas necesarias para el cálculo de falla de inventario 
 * (Factor de multiplicacion, venta estimada, inventario, reposición) 
 * @author ghuchet
 * */
public class XX_CalculateInventoryFails extends SvrProcess {

	int year = 0;
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		
	//Se obtiene el último año que tiene presupuesto
	getFiscalYear();
	//Si el año es mayor a 0 se calculan los factores de multiplicacion para el último año fiscal que tenga presupuesto.
	if(year > 0){
		//Se eliminan los factores de multiplicacion anteriores
		if (deleteFactors()){
			//Se calculan los factores de multiplicacion
			if(calculateFactor()){		
			}else return "No se completó el proceso.";
		}else return "No se completó el proceso.";
	}
	
	//Se elimina la venta promedio mensual anterior
	if (deleteAverageSales()){
		//Se calcula la venta promedio mensual
		if(calculateAverageSales()){
		}else return "No se completó el proceso.";
	}else return "No se completó el proceso.";

	//Se elimina la venta real del mes en curso
	if(deleteSales()){
		//Se calcula la venta real del mes en curso
		if(calculateSales()){
		}else return "No se completó el proceso.";
	}else return "No se completó el proceso.";
	
	//Se elimina la venta estimada
	if(deleteEstimatedSales()){
		//Se calcula la venta estimada
		if(calculateEstimatedSales()){
		}else  return "No se completó el proceso.";
	}else return "No se completó el proceso.";
	
	return "Proceso Completado.";
	}
	

	/**Elimina los Factores de Multiplicación existentes
	 * 
	 */
	private boolean deleteFactors() {
		
		PreparedStatement psDelete =null;
		try {
			String sql= "DELETE FROM XX_VMR_FACTOR";
	
			psDelete = DB.prepareStatement(sql,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			 e.printStackTrace();
			 return false;
		}finally{
			DB.closeStatement(psDelete);
		}
		return true;
	}
	
	/**Se obtiene el último año que tiene presupuesto
	 * 
	 */
	private void getFiscalYear() {
		String sql =  "SELECT MAX(SUBSTR(XX_BUDGETYEARMONTH,0,4)) FROM XX_VMR_PRLD01";
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null); 
		ResultSet rs = null;
		try
		{
			rs = pstmt.executeQuery();
		    
		    if(rs.next())
		    {
		    	year = rs.getInt(1);
		    }
		    pstmt.close();
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}

	/** Se calculan los Factores de Multiplicación por Departamento/Mes.
	 * 
	 * */
	private boolean calculateFactor() {
		int deptID = 0;
		String sql =  "SELECT XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT WHERE ISACTIVE = 'Y'";
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null); 
		ResultSet rs = null;
		try
		{
			rs = pstmt.executeQuery();
		    
		    while(rs.next()){
		    	deptID = rs.getInt(1);
		    	for (int i = 1; i < 13; i++) {
					if(i<7)
						calculateFactorMonth(i,(year*100)+i,  deptID);
					else 
						calculateFactorMonth(i,((year-1)*100)+i, deptID);
				}
		    }
		    pstmt.close();
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return true;
	}

	/**Se calcula el Factor de Multiplicación de un  Departamento/Mes.
	 * @throws Exception 
	 *
	 */
	private void calculateFactorMonth(int month, int yearmonth, int deptID) throws Exception {
		
		int yearMonthIni = ((year-1)*100)+07;
		int yearMonthEnd = (year*100)+06;
		
		String sql = "\nWITH TOTALMESDPTO AS (SELECT SUM(NVL(A.XX_SALESAMOUNTBUD2,0)) TOTALMES " +
				"\nFROM XX_VMR_PRLD01 A WHERE A.XX_BUDGETYEARMONTH = " + yearmonth + " AND XX_VMR_DEPARTMENT_ID = "+deptID+")," +
				"\nPROMEDIODPTO AS (SELECT SUM(NVL(A.XX_SALESAMOUNTBUD2,0))/12 PROMEDIO" +
				"\nFROM XX_VMR_PRLD01 A WHERE A.XX_BUDGETYEARMONTH >= " +yearMonthIni+" AND A.XX_BUDGETYEARMONTH <= " +yearMonthEnd+
				"\nAND XX_VMR_DEPARTMENT_ID = "+deptID+") " +
				"\nSELECT DECODE(PROMEDIO,0,0,TOTALMES/PROMEDIO) " +
				"\nFROM TOTALMESDPTO, PROMEDIODPTO";
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null); 
		ResultSet rs = null;
			rs = pstmt.executeQuery();
		    if(rs.next()){
		    	BigDecimal totalFactor = rs.getBigDecimal(1);
		    
		    	X_XX_VMR_Factor factor = new X_XX_VMR_Factor(Env.getCtx(), 0, null);
			    factor.setXX_Month(month);
				if(totalFactor==null) 
					factor.setXX_MultiplicationFactor(new BigDecimal(0));
		    	else 
		    		factor.setXX_MultiplicationFactor(totalFactor.setScale(2, BigDecimal.ROUND_HALF_UP));
				factor.setXX_VMR_Department_ID(deptID);
			    factor.save();
		    }
		DB.closeResultSet(rs);
		DB.closeStatement(pstmt);
	}
	

	private boolean deleteAverageSales() {
		try{
			String sql = "DROP TABLE XX_AVERAGESALES";
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			return false;
		}

		return true;
	}

	private boolean calculateAverageSales() {
		
		try{
			String sql = "CREATE TABLE XX_AVERAGESALES AS SELECT * FROM XX_AVERAGESALESVIEW";
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			return false;
		}

		return true;
	}
	
	private boolean deleteSales() {
		try{
			String sql = "DROP TABLE XX_SALES";
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			return false;
		}

		return true;
	}

	private boolean calculateSales() {
		
		try{
			String sql = "CREATE TABLE XX_SALES AS SELECT * FROM XX_SALESVIEW";
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			return false;
		}

		return true;
	}


	private boolean deleteEstimatedSales() {
		try{
			String sql = "DROP TABLE XX_ESTIMATEDSALES";
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			return false;
		}

		return true;
	}

	private boolean calculateEstimatedSales() {
		
		try{
			String sql = "CREATE TABLE XX_ESTIMATEDSALES AS SELECT * FROM XX_ESTIMATEDSALESVIEW";
			DB.executeUpdate(null, sql);
			sql = "UPDATE XX_ESTIMATEDSALES SET REPOSICIONNEGATIVOS = CASE WHEN NVL(QTYSTORE+QTYTRANSIT - (NVL(ESTIMATEDSALES,0)-NVL(REALSALES,0)),0) > 0 THEN 0 ELSE NVL(QTYSTORE+QTYTRANSIT - (NVL(ESTIMATEDSALES,0)-NVL(REALSALES,0)),0) END ";
			DB.executeUpdate(null, sql);
			sql = "UPDATE XX_ESTIMATEDSALES SET REPOSICION =  NVL(QTYSTORE+QTYTRANSIT - (NVL(ESTIMATEDSALES,0)-NVL(REALSALES,0)),0) ";
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			return false;
		}

		return true;
	}
}
