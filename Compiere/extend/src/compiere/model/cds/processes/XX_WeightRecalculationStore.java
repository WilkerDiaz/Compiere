package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_XX_VMR_WarehouseWeight;

public class XX_WeightRecalculationStore extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		X_XX_VMR_WarehouseWeight warehoseWeight;
		
		// Se hace al inicio del proceso para saber donde estoy parado en la tabla
		warehoseWeight = new X_XX_VMR_WarehouseWeight(getCtx(), getRecord_ID(),null);
		
		String aniomes =  null;
		String anio =  null;
		String mes = null;
		BigDecimal ventasTienda =  new BigDecimal(0);
		BigDecimal ventasCadena = new BigDecimal(0);
		BigDecimal pesoTienda = new BigDecimal(0);
		BigDecimal pesoTotal = new BigDecimal(0);
		Integer warehoseWeightID = 0;
			
		// Calendario
		//warehoseWeight.getC_Year_ID();
		
		// Busco la suma de los PESOS de todas las tienda de la cadena en ese calendario especifico
		
		String SQL3 = ("SELECT SUM(XX_WarehouseWeight) SUMAPESO " +
				"FROM XX_VMR_WarehouseWeight " +
				"WHERE C_Year_ID = '"+warehoseWeight.getC_Year_ID()+"' ");
		
		PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null);
		ResultSet rs3 = pstmt3.executeQuery();
		
		if(rs3.next())
		{
			pesoTotal = rs3.getBigDecimal("SUMAPESO");
			//System.out.println("Peso de todas las tienda "+pesoTotal);
		}
		rs3.close();
		pstmt3.close();
		
		
		/** Busco el id de los registro donde el calendario sea igual a la que 
		inicio el proceso y a los que no le agregaron nuevo peso*/
		
		String SQL1 = ("SELECT XX_VMR_WarehouseWeight_ID AS WWID " +
				"FROM XX_VMR_WarehouseWeight " +
				"WHERE C_Year_ID = '"+warehoseWeight.getC_Year_ID()+"' " +
				"AND XX_WeightCheck = 'N' ");
		
		PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null);
		ResultSet rs1 = pstmt1.executeQuery();
		//int i=0;
		while(rs1.next())
		{//System.out.println(i++);
			warehoseWeightID = rs1.getInt("WWID");
			warehoseWeight = new X_XX_VMR_WarehouseWeight(getCtx(),warehoseWeightID ,null);
			
			// Periodo
			//warehoseWeight.getC_Period_ID();
			// Tienda
			//warehoseWeight.getM_Warehouse_ID();
			// Peso Nuevo
			//warehoseWeight.getXX_WarehouseWeight();
			
			String SQL2 = ("SELECT TO_CHAR (STARTDATE, 'YYYYMM') AS AÑOMES " +
					"FROM  C_PERIOD WHERE C_PERIOD_ID = '"+warehoseWeight.getC_Period_ID()+"' ");

			PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null);
			ResultSet rs2 = pstmt2.executeQuery();
			
			Integer anioaux = 0;
			if(rs2.next())
			{			
				aniomes = rs2.getString("AÑOMES");
				anio = aniomes.substring(0, 4);
				mes = aniomes.substring(4, 6);
				
				//año anterior al conseguido
				anioaux = Integer.parseInt(anio);
				anioaux = anioaux - 1;
				
				ventasTienda = calculoVentasTienda (anioaux.toString(), mes, warehoseWeight.getM_Warehouse_ID(), warehoseWeight.getXX_VMR_Department_ID(), 
														warehoseWeight.getXX_VMR_Line_ID(), warehoseWeight.getXX_VMR_Section_ID());
				//Ventas total cadena
				ventasCadena = calculoVentasCadena (anioaux.toString(), mes, warehoseWeight.getM_Warehouse_ID());
				// Peso nuevo de la tienda
				pesoTienda = ventasTienda.divide(ventasCadena.multiply(pesoTotal.add(new BigDecimal(1))), 4, RoundingMode.HALF_UP);
				
				//System.out.println("Ventas tienda " + ventasTienda);
				//System.out.println("Ventas cadena " + ventasCadena);
				//System.out.println("Peso Tienda " + pesoTienda);
				
			}
			rs2.close();
			pstmt2.close();
			
			// Peso Nuevo
			warehoseWeight.setXX_WarehouseWeight(pesoTienda);
			if(pesoTienda.compareTo(new BigDecimal(0)) == 0)
				warehoseWeight.setXX_Check(true);
			else
				warehoseWeight.setXX_Check(false);

						
			warehoseWeight.save();
			
		}// end while 
		rs1.close();
		pstmt1.close();
			
		return "Proceso Realizado";
	}

	public BigDecimal calculoVentasTienda (String year, String mes, Integer tienda, Integer dpto, Integer line, Integer section)
	{
		BigDecimal ventasTienda = new BigDecimal(0);
		String anomes = null;
		
		anomes = year+mes;
		//>200907
		String SQL = ("SELECT coalesce(sum(XX_monvenrea),0) AS VENTATIE " +
				"FROM M_WAREHOUSE A, i_xx_vmr_prld01 B, XX_VMR_DEPARTMENT C, XX_VMR_LINE D, XX_VMR_SECTION E " +
				"WHERE A.ISACTIVE = 'Y' " +
				"AND A.M_WAREHOUSE_ID = B.M_WAREHOUSE_ID " +
				"AND A.M_WAREHOUSE_ID = '"+tienda+"' " +
				"AND B.XX_VMR_DEPARTMENT_ID = C.XX_VMR_DEPARTMENT_ID " +
				"AND B.XX_VMR_DEPARTMENT_ID = '"+dpto+"' " +
				"AND B.XX_VMR_LINE_ID = D.XX_VMR_LINE_ID " +
				"AND B.XX_VMR_LINE_ID = '"+line+"' " +
				"AND B.XX_VMR_SECTION_ID = E.XX_VMR_SECTION_ID " +
				"AND B.XX_VMR_SECTION_ID = '"+section+"' " +
				"AND B.xx_añomespre = '"+anomes+"' ");
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				ventasTienda = rs.getBigDecimal("VENTATIE");
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e) {
			System.out.println("Error al buscar ventasTienda " + e);			
		}
		
		if (ventasTienda.intValue()==0)
			ventasTienda = new BigDecimal(1);
	

		return ventasTienda;
	}
	
	
	public BigDecimal calculoVentasCadena (String year, String mes, Integer tienda)
	{
		BigDecimal ventasTiendaCadena = new BigDecimal(0);
		String anomes = null;
		
		anomes = year+mes;
		//>200907
		String SQL = ("SELECT coalesce(sum(XX_monvenrea),0) AS VENTACAD " +
				"FROM M_WAREHOUSE A, i_xx_vmr_prld01 B, XX_VMR_DEPARTMENT C, XX_VMR_LINE D, XX_VMR_SECTION E " +
				"WHERE A.ISACTIVE = 'Y' " +
				"AND A.M_WAREHOUSE_ID = B.M_WAREHOUSE_ID " +
				"AND B.XX_VMR_DEPARTMENT_ID = C.XX_VMR_DEPARTMENT_ID " +
				"AND B.XX_VMR_LINE_ID = D.XX_VMR_LINE_ID " +
				"AND B.XX_VMR_SECTION_ID = E.XX_VMR_SECTION_ID " +
				"AND B.xx_añomespre = '"+anomes+"' ");
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				ventasTiendaCadena = rs.getBigDecimal("VENTACAD");
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e) {
			System.out.println("Error al buscar ventasTiendaCadena " + e);			
		}
		
		if (ventasTiendaCadena.intValue()==0)
			ventasTiendaCadena = new BigDecimal(1);
	

		return ventasTiendaCadena;
	}
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
