package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_XX_VMR_PurchasePlan;

public class XX_CalculationPiece extends SvrProcess{


	protected String doIt() throws Exception {
		
		// guardo en anio nuevo el año de inicio del periodo anterior
		Calendar fecha = Calendar.getInstance();
		
		int mesActual = fecha.get(Calendar.MONTH) + 1; 
		int anioActual = fecha.get(Calendar.YEAR);
		
		Integer purchasePlanid = 0;
		Integer period =0;
		Integer department = 0;
		Integer line = 0;
		Integer section = 0;
		String anomes = null;
		String ano = null;
		String mes = null;
		String anomesaux = null;
		BigDecimal inflacion = new BigDecimal(0);
		Integer nroPeriod = 0;
		Integer anio = 0;
		
		BigDecimal initialInventory = new BigDecimal(0);
		BigDecimal sales = new BigDecimal(0);
		BigDecimal decrease = new BigDecimal(0);
		BigDecimal purchase = new BigDecimal(0);
		BigDecimal BudgetedFinalInventory = new BigDecimal(0);
		
		BigDecimal ventasBs;
		BigDecimal ventasBasicosBsRebajados;
		int ventasPiezas = 0;
		int ventasBasicosPiezasRebajados;
		
		/////////////////////////////////////////////////////////////
		BigDecimal peso = new BigDecimal(0);
		//BigDecimal salesDistribution = new BigDecimal(0);
		String aniomes = null;
		BigDecimal invFinalProyPieces = new BigDecimal(0);

		
		String mesActualStr = "";
		if(mesActual<10)
			mesActualStr = "0" + mesActual;
		
		//System.out.println("mes actual "+mesActualStr+" año actual "+anioActual);
		
		String SQL = ("SELECT XX_VMR_PURCHASEPLAN_ID AS PURID, B.PERIODNO AS NROPERIOD, TO_NUMBER(TO_CHAR (B.STARTDATE, 'YYYY')) ANIO " +
				"FROM XX_VMR_PURCHASEPLAN A, C_PERIOD B " +
				"WHERE A.C_PERIOD_ID = B.C_PERIOD_ID " +
				"ORDER BY B.PERIODNO ASC ");
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				purchasePlanid = rs.getInt("PURID");
				nroPeriod = rs.getInt("NROPERIOD");
				anio = rs.getInt("ANIO");
				BigDecimal ventasMesCurso = new BigDecimal(0);
				
				X_XX_VMR_PurchasePlan purchasePlan = new X_XX_VMR_PurchasePlan(getCtx(), purchasePlanid, get_TrxName());
	
				period = purchasePlan.getC_Period_ID();
				department = purchasePlan.getXX_VMR_Department_ID();
				line = purchasePlan.getXX_VMR_Line_ID();
				section = purchasePlan.getXX_VMR_Section_ID();
				initialInventory = purchasePlan.getXX_InitialInventory();
				sales = purchasePlan.getXX_SalesDistribution();
				decrease = purchasePlan.getXX_Decrease();
				purchase = purchasePlan.getXX_Purchases();
				BudgetedFinalInventory = purchasePlan.getXX_BudgetedFinalInventory();
		
				
				String SQL2 = ("SELECT TO_CHAR (STARTDATE, 'YYYYMM') AS AÑOMES " +
						"FROM  C_PERIOD WHERE C_PERIOD_ID = '"+period+"' ");
				
				PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null);
				ResultSet rs2 = pstmt2.executeQuery();
		
				Integer year = 0;
				if(rs2.next())
				{
					anomes = rs2.getString("AÑOMES");
					ano = anomes.substring(0, 4);
					mes = anomes.substring(4, 6);
					year = Integer.parseInt(ano)-1;
					//System.out.println("año del periodo "+ano+ "menos 1 "+year);
					anomesaux = year+mes;
			
					if (Integer.parseInt((Integer.toString(anioActual)+mesActualStr)) > Integer.parseInt(anomesaux)) // si mes actual es mayor o igual al mes a tratar uso las reales
					{
						//System.out.println("busca reales"); 
						// Venta en Bs (Mes a comparar)
						ventasBs = ventasRealesBs(year, mes, department, line, section);
						// Ventas Basicos Rebajados
						ventasBasicosBsRebajados = ventasRealesBsBasicosRebajados (year, mes, department, line, section);
						// Ventas en Piezas
						ventasPiezas = ventasRealesPiezas(year, mes, department, line, section);
						// Ventas de basicos rebajados
						ventasBasicosPiezasRebajados = ventasRealesPiezasBasicosRebajados (year, mes, department, line, section);
				
					}// end if
					else
					{
						//System.out.println("busca presupuestadas");
						// Venta en Bs (Mes a comparar)
						ventasBs = ventasPresupuestadasBs(year, mes,department, line, section);
						// Ventas Basicos Rebajados
						ventasBasicosBsRebajados = ventasRealesBsBasicosRebajados (year-1, mes, department, line, section);
						// Ventas en Piezas
						ventasPiezas = ventasPresupuestadasPiezas(year, mes, department, line, section);
						// Ventas de basicos rebajados
						ventasBasicosPiezasRebajados = ventasRealesPiezasBasicosRebajados (year, mes, department, line, section);
						
					} // end else
		
					BigDecimal precioPromedioOrigen;
					
					if (ventasPiezas==0)
						precioPromedioOrigen = new BigDecimal(0);
					else 
						precioPromedioOrigen = (ventasBs.subtract(ventasBasicosBsRebajados)).divide(new BigDecimal(ventasPiezas-ventasBasicosPiezasRebajados), 2, RoundingMode.HALF_UP);
					
					/*System.out.println("ventasBs "+ventasBs); 
					// Ventas Basicos Rebajados
					System.out.println("ventasBasicosBsRebajados "+ventasBasicosBsRebajados); 
					// Ventas en Piezas
					System.out.println("ventasPiezas "+ventasPiezas); 
					// Ventas de basicos rebajados
					System.out.println("ventasBasicosPiezasRebajados "+ventasBasicosPiezasRebajados);*/
					
					
					//System.out.println("precioPromedioOrigen "+precioPromedioOrigen);
					
					String SQL3 = ("SELECT XX_AnnualInflation AS TASAINFLA " +
							"FROM XX_VMR_ComercialBudget " +
							"WHERE C_YEAR_ID IN (SELECT C_YEAR_ID FROM C_PERIOD WHERE C_PERIOD_ID = '"+period+"') ");
					
					PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null);
					ResultSet rs3 = pstmt3.executeQuery();
					
					double tasaInflacionMensual = 0;
					
					if(rs3.next())
					{
						inflacion = rs3.getBigDecimal("TASAINFLA");
						tasaInflacionMensual = Math.pow(((inflacion.divide(new BigDecimal(100), 0, RoundingMode.HALF_UP)).add(new BigDecimal(1))).doubleValue(),0.0833);
					}
					rs3.close();
					pstmt3.close();
					
					Integer month = 0;
					month = Integer.parseInt(mes)-1;
					
					int mesesDiferencia = 0;
					
					if ((mesActual>6 & month>6) | (mesActual<7 & month<7))
						mesesDiferencia = Math.abs(mesActual-month);
					else
						mesesDiferencia = 12-month+mesActual;
					
					
					BigDecimal PrecioPromedioCompañia = precioPromedioOrigen.multiply(new BigDecimal(Math.pow(tasaInflacionMensual, mesesDiferencia)));
					//System.out.println("Id "+purchasePlanid );
					//System.out.println("PrecioPromedioCompañia " + PrecioPromedioCompañia);
					
					if(PrecioPromedioCompañia.compareTo(new BigDecimal(0)) == 0)
						PrecioPromedioCompañia = new BigDecimal(1);
					
					//X_XX_VMR_CalculationPiece calculationPiece = new X_XX_VMR_CalculationPiece(getCtx(), 0, get_TrxName());
					
					/*calculationPiece.setC_Period_ID(period);
					calculationPiece.setXX_VMR_Department_ID(department);
					calculationPiece.setXX_VMR_Line_ID(line);
					calculationPiece.setXX_VMR_Section_ID(section);*/
					/** Se cambio la logica para el calculo del Inventario Inicial y el 
					 * Inventario Final presupuestado  (Logica Vieja)*/
					/*
					//Variables en piezas
					initialInventory = initialInventory.divide(PrecioPromedioCompañia, 2, RoundingMode.HALF_UP);
					decrease = decrease.divide(PrecioPromedioCompañia, 2, RoundingMode.HALF_UP);
					purchase = purchase.divide(PrecioPromedioCompañia, 2, RoundingMode.HALF_UP);
					BudgetedFinalInventory = BudgetedFinalInventory.divide(PrecioPromedioCompañia, 2, RoundingMode.HALF_UP); 
					
					
					purchasePlan.setXX_InitialInventoryPieces(initialInventory);
					purchasePlan.setXX_DecreasePieces(decrease);
					purchasePlan.setXX_PurchasesPieces(purchase);
					purchasePlan.setXX_BudFinalInventoryPiece(BudgetedFinalInventory);
					purchasePlan.save();*/
					
					
					/*calculationPiece.setXX_InitialInventory(initialInventory);
					calculationPiece.setXX_Decrease(decrease);
					calculationPiece.setXX_Purchases(purchase);
					calculationPiece.setXX_BudgetedFinalInventory(BudgetedFinalInventory);
					calculationPiece.setXX_VMR_PURCHASEPLAN_ID(purchasePlanid);
					calculationPiece.save();*/		
					/**---------------------------------------------------------------------------------------- */
					
					/**De aqui en adelante se va aplicar los nuevos calculos para el Inventario Inicial y el 
					 * Inventario Final presupuestado, solo voy a distribuir la Merma del mes en curso y las Compras */
					

					//Busco el peso de por departamento linea y seccion
					String SQL4 = ("SELECT CASE WHEN SUM(Weight) IS NULL THEN 0 ELSE SUM(Weight) END AS PESO " +
							"FROM XX_VMR_WeightAssigned " +
							"WHERE XX_VMR_DEPARTMENT_ID = '"+department+"' " +
							"AND XX_VMR_LINE_ID = '"+line+"' " +
							"AND XX_VMR_SECTION_ID = '"+section+"' " +
							"AND XX_VME_CONCEPTVALUE_ID IS NULL ");
					
					PreparedStatement pstmt4 = DB.prepareStatement(SQL4, null);
					ResultSet rs4 = pstmt4.executeQuery();
					
					if(rs4.next())
					{
						peso = rs4.getBigDecimal("PESO");
						if(peso.compareTo(new BigDecimal(0))==0)
							peso = new BigDecimal(1);
					}
					rs4.close();
				    pstmt4.close();
					
				  //Busco las distribucion de venta del departamento por el periodo, luego calculas las Ventas del Mes en Curso con ese valor
				  //LO TENGO CON EL NUEVO CAMPO QUE AGREGO VICTOR (BORRAR LUEGO)
					/*String SQL7 = ("SELECT A.XX_SALESDISTRIBUTION AS SALESDISTRI " +
							"FROM XX_VMR_BUDGETSALESDEPART A, XX_VMR_REALCOMERCIALBUDGET B " +
							"WHERE XX_VMR_DEPARTMENT_ID = '"+department+"' " +
							"AND A.XX_VMR_REALCOMERCIALBUDGET_ID = B.XX_VMR_REALCOMERCIALBUDGET_ID " +
							"AND B.C_PERIOD_ID = '"+period+"' ");
					
					 PreparedStatement pstmt7 = DB.prepareStatement(SQL7, null);
					try
					{
						ResultSet rs7 = pstmt7.executeQuery();
						 
						 if(rs7.next())
						 {
							 salesDistribution = rs7.getBigDecimal("SALESDISTRI");
						 }
						 rs7.close();
						 pstmt7.close();
					}
					catch (SQLException e) {
						System.out.println("Error " + e);	
					}*/
					System.out.println("NUMERO DEL PERIODO "+ nroPeriod);
					if(nroPeriod == 1) //pregunto si el periodo es el mes de JULIO para buscar su inventario inicial
					{
						aniomes = anio+"06";
						
						// Busco el inventario final proyectado del mes de Junio(06anio) que va ser el inicial del mes de Julio del nuevo periodo
				    	String SQL6 = ("select coalesce(sum(XX_CAINVFIPR),0) from i_xx_vmr_prld01 " +
				    			"where xx_añomespre = '"+aniomes+"' " +
				    			"and XX_VMR_DEPARTMENT_ID = '"+department+"' " +
				    			"AND XX_VMR_LINE_ID =  '"+line+"' " +
				    			"AND XX_VMR_SECTION_ID = '"+section+"' " +
				    			"AND XX_CODCAT<>99 and XX_CODDEP<>99 and XX_CODLIN<>99 and XX_CODSEC<>99 and XX_CODTIE<>99 ");
				    	
				    	PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null);
						ResultSet rs6 = pstmt6.executeQuery();
						
						if(rs6.next())
						{
							if(rs6.getBigDecimal(1).compareTo(new BigDecimal(0)) == 0)
								invFinalProyPieces = new BigDecimal(1);
							else
								invFinalProyPieces = rs6.getBigDecimal(1);
							
							System.out.println("inventario inicial Final MOINVFIPR " + invFinalProyPieces);
							
							////////////////////////////////////////
							//Variables en piezas
							////////////////////////////////////////
							//Inventario Inicial
							initialInventory = invFinalProyPieces.divide(PrecioPromedioCompañia, 2, RoundingMode.HALF_UP);
							// Sales
							sales = sales.divide(PrecioPromedioCompañia, 2, RoundingMode.HALF_UP);
							// Merma del mes en curso
							decrease = decrease.divide(PrecioPromedioCompañia, 2, RoundingMode.HALF_UP);
							// Compras
							purchase = purchase.divide(PrecioPromedioCompañia, 2, RoundingMode.HALF_UP);
							
							//Inventario Final Presupuestado
							//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso
							// continuacion:                             - merma mes en curso
							
							/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
							 * Ventas de Mes en Curso = DistribucionVenta/peso */
							
							ventasMesCurso = sales.divide(peso, 2, RoundingMode.HALF_UP);
							BudgetedFinalInventory = initialInventory.add(purchase).subtract(ventasMesCurso).subtract(decrease);
													
							purchasePlan.setXX_InitialInventoryPieces(initialInventory.intValue());
							purchasePlan.setXX_SalesDistributionPiece(sales.intValue());
							purchasePlan.setXX_DecreasePieces(decrease.intValue());
							purchasePlan.setXX_PurchasesPieces(purchase.intValue());
							purchasePlan.setXX_BudFinalInventoryPiece(BudgetedFinalInventory.intValue());
							purchasePlan.save();
							commit();

						}// end if
						rs6.close();
				    	pstmt6.close();
						
					}
					else //Si el mes no es julio, en vez de buscarlo de la prld01 lo busco en el XX_VMR_PurchasePlan y es el final del mes anterior
					{
						System.out.println("LLEGO A LOS PERIODOS MAYOR A 1");
						nroPeriod = nroPeriod - 1; // Le resto uno para que sea el periodo anterior
				    	Integer finalInvPieces = 0;
				    	Integer initialInventoryAux = 0;
				    	
				    	/** Busco el inventario final EN PIEZAS del mes anterior que va ser el 
						 * Inventario Inicial */
						String SQL6 = ("SELECT XX_BUDFINALINVENTORYPIECE AS FINALPIECES " +
								"FROM XX_VMR_PURCHASEPLAN A, C_PERIOD B " +
								"WHERE A.C_PERIOD_ID = B.C_PERIOD_ID " +
								"AND PERIODNO = '"+nroPeriod+"' " +
								"AND A.XX_VMR_DEPARTMENT_ID = '"+department+"' " +
								"AND A.XX_VMR_LINE_ID = '"+line+"' " +
								"AND A.XX_VMR_SECTION_ID = '"+section+"' ");
						
						PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null);
						ResultSet rs6 = pstmt6.executeQuery();
						
						if(rs6.next())
						{
							finalInvPieces = rs6.getInt("FINALPIECES");
							
							////////////////////////////////////////
							//Variables en piezas
							////////////////////////////////////////
							//Inventario Inicial
							initialInventoryAux = finalInvPieces;
							// Ventas
							sales = sales.divide(PrecioPromedioCompañia, 2, RoundingMode.HALF_UP);
							// Merma del mes en curso
							decrease = decrease.divide(PrecioPromedioCompañia, 2, RoundingMode.HALF_UP);
							// Compras
							purchase = purchase.divide(PrecioPromedioCompañia, 2, RoundingMode.HALF_UP);
							
							//Inventario Final Presupuestado
							//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso
							// continuacion:                             - merma mes en curso
							
							/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
							 * Ventas de Mes en Curso = DistribucionVenta/peso */
							
							ventasMesCurso = sales.divide(peso, 2, RoundingMode.HALF_UP);
							BudgetedFinalInventory = initialInventory.add(purchase).subtract(ventasMesCurso).subtract(decrease);
													
							purchasePlan.setXX_InitialInventoryPieces(initialInventoryAux);
							purchasePlan.setXX_SalesDistributionPiece(sales.intValue());
							purchasePlan.setXX_DecreasePieces(decrease.intValue());
							purchasePlan.setXX_PurchasesPieces(purchase.intValue());
							purchasePlan.setXX_BudFinalInventoryPiece(BudgetedFinalInventory.intValue());
							purchasePlan.save();
							commit();
	
						}
				    	rs6.close();
				    	pstmt6.close();
						
					}
					
			/**--------------------------------------------------------------------------------------------------------*/
					
				}// end if
				rs2.close();
				pstmt2.close();
				
			}// end while
			rs.close();
			pstmt.close();
			
			
		}// end try
		catch (Exception e) {
			e.printStackTrace();
		}

		return "Proceso Realizado";
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
	
	public BigDecimal ventasRealesBs(Integer year, String mes, Integer department, Integer line, Integer section)
	{
		BigDecimal ventasRealesBs = new BigDecimal(0);
		String anomesaux = null;
		
		anomesaux = year+mes;
		//System.out.println("añomes "+anomesaux);
		
		String SQL = ("select coalesce(sum(XX_monvenrea),0) as ventabs " +
				"from i_xx_vmr_prld01 " +
				"where XX_añomespre= '"+anomesaux+"' " +
				"and XX_VMR_DEPARTMENT_ID = '"+department+"' " +
				"and XX_VMR_LINE_ID = '"+line+"' " +
				"and XX_VMR_SECTION_ID = '"+section+"' ");
		
		//System.out.println(SQL);

/**		
		String sql = "SELECT COALESCE(SUM(TOTALLINES),0) FROM C_ORDER WHERE ISSOTRX='Y' AND DATEORDERED = (TO_DATE( '" + i + "-" + (anioNuevo) + "', 'MM-YYYY' ))";
*/		PreparedStatement prst;
		prst = DB.prepareStatement(SQL,null);

		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next())
			{			
				ventasRealesBs = rs.getBigDecimal(1);				
			}	

			rs.close();
			prst.close();
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventasPresupuestadasBs " + e);			
		}
		if (ventasRealesBs.intValue()==0)
			ventasRealesBs = new BigDecimal(1);
	
		return ventasRealesBs;
	}
	
	
	public BigDecimal ventasPresupuestadasBs(Integer year, String mes, Integer department, Integer line, Integer section)
	{
		BigDecimal ventasPresupuestadasBs = new BigDecimal(0);
		
		String anomesaux = null;
				
		// primero busco a ver si el mes en curso es uno de los especiales
		String sql = "select value, XX_VMR_Department_ID from xx_vmr_SpecialMonth";
		
		PreparedStatement prst;
		prst = DB.prepareStatement(sql,null);
		
		boolean especial = false; 
		try
		{
			String mespecial = "";
			String depEspecial = "";
			ResultSet rs = prst.executeQuery();
			
			while (rs.next())
			{			
				mespecial = rs.getString(1);
				depEspecial = rs.getString(2);
				//System.out.println("Mes especial "+mespecial);
				//System.out.println("mes del periodo "+mes);
				
				 if ((mespecial.equalsIgnoreCase(mes) && depEspecial=="") || (mespecial.equalsIgnoreCase(mes) && department.equals(depEspecial)))
				 {
					 especial = true;
					 ventasPresupuestadasBs = ventasRealesBs(year-1, mes, department, line, section);					 
				 }
			} // end while	
			rs.close();
			prst.close();
			
		} // try
		catch (SQLException e){
			System.out.println("Error al buscar los meses especiales " + e);	
		}
		
		if (especial)
			return ventasPresupuestadasBs;
		
		
		anomesaux = year+mes;
		//System.out.println("añomes "+anomesaux);
		
		//sql = "SELECT COALESCE(SUM(XX_monvenpre),0) FROM i_XX_VMR_PRLD01 WHERE XX_añomespre = ('" + anomesaux + "')";
		
		sql = ("select coalesce(sum(XX_monvenpre),0) as ventabs " +
				"from i_xx_vmr_prld01 " +
				"where XX_añomespre= '"+anomesaux+"' " +
				"and XX_VMR_DEPARTMENT_ID = '"+department+"' " +
				"and XX_VMR_LINE_ID = '"+line+"' " +
				"and XX_VMR_SECTION_ID = '"+section+"' ");
		
		prst = DB.prepareStatement(sql,null);

		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next())
			{			
				ventasPresupuestadasBs = rs.getBigDecimal(1);				
			}	

			rs.close();
			prst.close();
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventasPresupuestadasBs " + e);			
		}
		
		
		return ventasPresupuestadasBs;
	}

	
	public BigDecimal ventasRealesBsBasicosRebajados (Integer year, String mes, Integer department, Integer line, Integer section)
	{
		BigDecimal ventasRealesBsBasicosRebajados = new BigDecimal(0);
		
		String sql = "select coalesce(0,sum(priceactual)) " +
				"from c_orderline where c_order_id in (select c_order_id from c_order where issotrx='Y' and dateordered= (TO_DATE( '" + mes + "-" + (year) + "', 'MM-YYYY' ))) " +
						"and m_product_id in " +
										"(select m_product_id from xx_vmr_priceconsecutive " +
										"where xx_consecutiveorigin='R' " +
										"and m_product_id in " +
														"(select m_product_id from m_product " +
														"where isactive='Y' " +
														"and XX_VMR_DEPARTMENT_ID = '"+department+"' and XX_VMR_LINE_ID = '"+line+"' and XX_VMR_SECTION_ID = '"+section+"' " +
														"and xx_vmr_typeinventory_id=" + getCtx().getContextAsInt("#XX_L_TYPEINVENTORYBASICO_ID") + "))";
		PreparedStatement prst;
		prst = DB.prepareStatement(sql,null);
		//System.out.println(sql);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next())
			{			
				ventasRealesBsBasicosRebajados = rs.getBigDecimal(1);				
			}	

			rs.close();
			prst.close();
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventasRealesPiezas " + e);	
		}		
			
		return ventasRealesBsBasicosRebajados;
	}
	
	
	public int ventasRealesPiezas(Integer year, String mes, Integer department, Integer line, Integer section)
	{
		int ventasRealesPiezas = 0;

		String anomesaux = null;
		
		anomesaux = year+mes;
		//System.out.println("añomes "+anomesaux);

		//String sql = "select coalesce(sum(XX_canvenrea),0) from i_xx_vmr_prld01 where XX_añomespre=" + j;
		
		String SQL = ("select coalesce(sum(XX_canvenrea),0) as ventabs " +
				"from i_xx_vmr_prld01 " +
				"where XX_añomespre= '"+anomesaux+"' " +
				"and XX_VMR_DEPARTMENT_ID = '"+department+"' " +
				"and XX_VMR_LINE_ID = '"+line+"' " +
				"and XX_VMR_SECTION_ID = '"+section+"' ");
		
/**		
		String sql = "SELECT COALESCE(SUM(QtyOrdered),0) FROM C_ORDERLINE WHERE C_ORDER_ID IN (SELECT C_ORDER_ID FROM C_ORDER WHERE ISSOTRX='Y' AND DATEORDERED = (TO_DATE( '" + i + "-" + (anioNuevo) + "', 'MM-YYYY' )))";
*/		PreparedStatement prst;
		prst = DB.prepareStatement(SQL,null);

		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next())
			{			
				ventasRealesPiezas = rs.getInt(1);				
			}	

			rs.close();
			prst.close();
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventasRealesPiezas " + e);
		}

		return ventasRealesPiezas;
	}	
	
	
	public int ventasPresupuestadasPiezas(Integer year, String mes, Integer department, Integer line, Integer section)
	{
		int ventasPresupuestadasPiezas = 0;
		
		String anomesaux = null;
		
		// primero busco a ver si el mes en curso es uno de los especiales
		String sql = "select value, XX_VMR_Department_ID from xx_vmr_SpecialMonth";
		
		PreparedStatement prst;
		prst = DB.prepareStatement(sql,null);
		
		boolean especial = false; 
		try
		{
			String mespecial = "";
			String depEspecial = "";
			ResultSet rs = prst.executeQuery();
			while (rs.next())
			{			
				mespecial = rs.getString(1);
				depEspecial = rs.getString(2);
				if ((mespecial.equalsIgnoreCase(mes) && depEspecial=="") || (mespecial.equalsIgnoreCase(mes) && department.equals(depEspecial)))
				 {
					 especial = true;
					 ventasPresupuestadasPiezas = ventasRealesPiezas(year-1, mes, department, line, section);					 
				 }
			}
			rs.close();
			prst.close();
			
		} catch (SQLException e){
			System.out.println("Error al buscar los meses especiales " + e);	
		}
		if (especial)
			return ventasPresupuestadasPiezas;
				
		anomesaux = year+mes;
		//System.out.println("añomes "+anomesaux);
		
		//sql = "SELECT COALESCE(SUM(XX_canvenpre),0) FROM i_XX_VMR_PRLD01 WHERE XX_añomespre = ('" + j + "')";
		
		sql = ("select coalesce(sum(XX_canvenpre),0) as ventabs " +
				"from i_xx_vmr_prld01 " +
				"where XX_añomespre= '"+anomesaux+"' " +
				"and XX_VMR_DEPARTMENT_ID = '"+department+"' " +
				"and XX_VMR_LINE_ID = '"+line+"' " +
				"and XX_VMR_SECTION_ID = '"+section+"' ");
		
		prst = DB.prepareStatement(sql,null);

		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next())
			{			
				ventasPresupuestadasPiezas = rs.getInt(1);				
			}	

			rs.close();
			prst.close();
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventasPresupuestadasPiezas " + e);
		}
		
		
		return ventasPresupuestadasPiezas;
	}	
	
	
	public int ventasRealesPiezasBasicosRebajados (Integer year, String mes, Integer department, Integer line, Integer section)
	{
		int ventasRealesPiezasBasicosRebajados = 0;
		
		String sql = "select coalesce(0,sum(qtyordered)) " +
				"from c_orderline " +
				"where c_order_id in (select c_order_id from c_order where issotrx='Y' and dateordered= (TO_DATE( '" + mes + "-" + (year) + "', 'MM-YYYY' ))) " +
						"and m_product_id in " +
										"(select m_product_id from xx_vmr_priceconsecutive " +
										"where xx_consecutiveorigin='R' " +
										"and m_product_id in " +
														"(select m_product_id from m_product " +
														"where isactive='Y' " +
														"and XX_VMR_DEPARTMENT_ID = '"+department+"' and XX_VMR_LINE_ID = '"+line+"' and XX_VMR_SECTION_ID = '"+section+"' " +
														"and xx_vmr_typeinventory_id=" + getCtx().getContextAsInt("#XX_L_TYPEINVENTORYBASICO_ID") + "))";
		PreparedStatement prst;
		prst = DB.prepareStatement(sql,null);

		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next())
			{			
				ventasRealesPiezasBasicosRebajados = rs.getInt(1);				
			}	

			rs.close();
			prst.close();
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventasRealesPiezasBasicosRebajados " + e);			
		}		
			
		return ventasRealesPiezasBasicosRebajados;
	}
	
	
}
