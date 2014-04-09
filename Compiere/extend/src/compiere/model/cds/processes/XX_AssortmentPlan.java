package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MVMRComercialBudgetTab;
import compiere.model.cds.X_XX_VMR_AssortmentPlan;

public class XX_AssortmentPlan extends SvrProcess{

	Integer department = 0;
	Integer periodo = 0;
	
	BigDecimal initialInventory = new BigDecimal(0);
	BigDecimal sales = new BigDecimal(0);
	BigDecimal rebate1 = new BigDecimal(0);
	BigDecimal decrease = new BigDecimal(0);
	BigDecimal discount1 = new BigDecimal(0);
	BigDecimal purchase = new BigDecimal(0);
	BigDecimal BudgetedFinalInventory = new BigDecimal(0);
	
	protected String doIt() throws Exception {
		
		BigDecimal peso = new BigDecimal(0);
		BigDecimal pesoB = new BigDecimal(0);
		BigDecimal pesoM = new BigDecimal(0);
		BigDecimal pesoO = new BigDecimal(0);
		Integer line = 0;
		Integer comercialBugID = 0;
		Integer nroPeriod = 0;
		Integer anio = 0;
		
		//Inicio el proceso borrando las tablas que estan despues del Plan de Surtido
		// Borro la tabla de distribución de tienda
		String SQL6 = ("DELETE FROM  XX_VMR_StoreDistri ");
		
		PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null); 
	    ResultSet rs6 = pstmt6.executeQuery();
		rs6.close();
		pstmt6.close();
		commit();
		// Borro la tabla de Plan de Compra
		String SQL7 = ("DELETE FROM  XX_VMR_PurchasePlan ");
		
		PreparedStatement pstmt7 = DB.prepareStatement(SQL7, null); 
	    ResultSet rs7 = pstmt7.executeQuery();
		rs7.close();
		pstmt7.close();
		commit();
		// Borro la tabla de Plan de Surtido
		String SQL8 = ("DELETE FROM  XX_VMR_AssortmentPlan ");
		
		PreparedStatement pstmt8 = DB.prepareStatement(SQL8, null); 
	    ResultSet rs8 = pstmt8.executeQuery();
		rs8.close();
		pstmt8.close();
		commit();
		
		//String SQL = ("SELECT XX_VMR_COMERCIALBUDGETTAB_ID AS COMERBUTID FROM XX_VMR_COMERCIALBUDGETTAB ");
		
		String SQL = ("SELECT A.XX_VMR_COMERCIALBUDGETTAB_ID AS COMERBUTID, B.PERIODNO AS NROPERIOD, TO_NUMBER(TO_CHAR (B.STARTDATE, 'YYYY')) ANIO " +
				"FROM XX_VMR_COMERCIALBUDGETTAB A, C_PERIOD B " +
				"WHERE A.C_PERIOD_ID = B.C_PERIOD_ID " +
				"ORDER BY B.PERIODNO ASC ");
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				comercialBugID = rs.getInt("COMERBUTID");
				nroPeriod = rs.getInt("NROPERIOD");
				anio = rs.getInt("ANIO");
				MVMRComercialBudgetTab comerButget = new MVMRComercialBudgetTab(getCtx(), comercialBugID, get_TrxName());
				
				// Departamento
				department = comerButget.getXX_VMR_Department_ID();
				//System.out.println(department);
				// Inventario inicial
				initialInventory = comerButget.getXX_InitialInventory();
				//System.out.println(initialInventory);
				//Ventas
				sales = comerButget.getXX_SalesDistribution();
				// Rebajas del mes en curso
				rebate1 = comerButget.getXX_Rebate1();
				//System.out.println(rebate1);
				// Merma del mes en curso
				decrease = comerButget.getXX_Decrease();
				//System.out.println(decrease);
				// Descuento Empleado
				discount1 = comerButget.getXX_Discount1();
				//System.out.println(discount1);
				// Compras
				purchase = comerButget.getXX_Purchases();
				//System.out.println(purchase);
				//Inventario Final Presupuestado
				BudgetedFinalInventory = comerButget.getXX_BudgetedFinalInventory();
				//System.out.println(BudgetedFinalInventory);
				// Periodo
				periodo = comerButget.getC_Period_ID();
				//System.out.println(periodo);
				
				// Busco las lineas del departamento
				String SQL1 = ("SELECT XX_VMR_LINE_ID AS LINEID " +
						"FROM XX_VMR_LINE " +
						"WHERE XX_VMR_DEPARTMENT_ID = '"+department+"' AND ISACTIVE = 'Y' ");
				
				PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null);
				ResultSet rs1 = pstmt1.executeQuery();
				
				//System.out.println(SQL1);
				
				while(rs1.next())
				{
					//System.out.println(rs1.getInt("LINEID"));
					line = rs1.getInt("LINEID");
					//X_XX_VMR_AssortmentPlan assortPlan = new X_XX_VMR_AssortmentPlan(getCtx(), 0, get_TrxName());
					
					// Busco el peso de las lineas encontradas
					String SQL2 = ("SELECT CASE WHEN SUM(Weight) IS NULL THEN 0 ELSE SUM(Weight) END AS PESO " +
							"FROM XX_VMR_WeightAssigned " +
							"WHERE XX_VMR_DEPARTMENT_ID = '"+department+"' " +
							"AND XX_VMR_LINE_ID = '"+line+"' " +
							"AND XX_VMR_SECTION_ID IS NULL " +
							"AND XX_VME_CONCEPTVALUE_ID IS NULL ");
					
					PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null);
					ResultSet rs2 = pstmt2.executeQuery();
					
					if(rs2.next())
					{
						peso = rs2.getBigDecimal("PESO");
						if(peso.compareTo(new BigDecimal(0))==0)
							peso = new BigDecimal(1);
					}
					rs2.close();
				    pstmt2.close();
				    
				    // Busco el peso BUENO de la linea
				    String SQL3 = ("SELECT CASE WHEN SUM(Weight) IS NULL THEN 0 ELSE SUM(Weight) END AS PESOBUENO " +
				    		"FROM XX_VMR_WeightAssigned A, XX_VME_CONCEPTVALUE B " +
				    		"WHERE A.XX_VMR_DEPARTMENT_ID = '"+department+"' " +
				    		"AND A.XX_VMR_LINE_ID = '"+line+"' " +
				    		"AND A.XX_VMR_SECTION_ID IS NULL " +
				    		"AND A.XX_VME_CONCEPTVALUE_ID = B.XX_VME_CONCEPTVALUE_ID " +
				    		"AND B.VALUE= 1000000 ");
				    
				    PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null);
					ResultSet rs3 = pstmt3.executeQuery();
					
					// Realizo los calculos con el peso BUENO de la linea
					if(rs3.next())
					{
						pesoB = rs3.getBigDecimal("PESOBUENO");
						if(pesoB.compareTo(new BigDecimal(0))==0)
							pesoB = new BigDecimal(1);
					}
					rs3.close();
				    pstmt3.close();
				    
				    // Busco el peso MEJOR de la linea
				    String SQL4 = ("SELECT CASE WHEN SUM(Weight) IS NULL THEN 0 ELSE SUM(Weight) END AS PESOMEJOR " +
				    		"FROM XX_VMR_WeightAssigned A, XX_VME_CONCEPTVALUE B " +
				    		"WHERE A.XX_VMR_DEPARTMENT_ID = '"+department+"' " +
				    		"AND A.XX_VMR_LINE_ID = '"+line+"' " +
				    		"AND A.XX_VMR_SECTION_ID IS NULL " +
				    		"AND A.XX_VME_CONCEPTVALUE_ID = B.XX_VME_CONCEPTVALUE_ID " +
				    		"AND B.VALUE= 1000003 ");
				    
				    PreparedStatement pstmt4 = DB.prepareStatement(SQL4, null);
					ResultSet rs4 = pstmt4.executeQuery();
					
					// Realizo los calculos con el peso MEJOR de la linea
					if(rs4.next())
					{
						pesoM = rs4.getBigDecimal("PESOMEJOR");
						if(pesoM.compareTo(new BigDecimal(0))==0)
							pesoM = new BigDecimal(1);
					}
					rs4.close();
				    pstmt4.close();
				    
				    
				    // Busco el peso OPTIMO de la linea
				    String SQL5 = ("SELECT CASE WHEN SUM(Weight) IS NULL THEN 0 ELSE SUM(Weight) END AS PESOOPTIMO " +
				    		"FROM XX_VMR_WeightAssigned A, XX_VME_CONCEPTVALUE B " +
				    		"WHERE A.XX_VMR_DEPARTMENT_ID = '"+department+"' " +
				    		"AND A.XX_VMR_LINE_ID = '"+line+"' " +
				    		"AND A.XX_VMR_SECTION_ID IS NULL " +
				    		"AND A.XX_VME_CONCEPTVALUE_ID = B.XX_VME_CONCEPTVALUE_ID " +
				    		"AND B.VALUE= 1000002 ");
				    
				    PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null);
					ResultSet rs5 = pstmt5.executeQuery();
					
					// Realizo los calculos con el peso OPTIMO de la linea
					if(rs5.next())
					{
						pesoO = rs5.getBigDecimal("PESOOPTIMO");
						if(pesoO.compareTo(new BigDecimal(0))==0)
							pesoO = new BigDecimal(1);
					}
					rs5.close();
				    pstmt5.close();
				    
				    // Realizo los calculos con todos los pesos por cada linea
				    calculatePlan(peso, periodo, line, department, comercialBugID, pesoB, pesoM, pesoO, nroPeriod, anio);

				} // end while1
				rs1.close();
			    pstmt1.close();
				
				
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
	
	private boolean calculatePlan(BigDecimal peso, Integer periodo, Integer line, Integer department, Integer comercialBugID, 
			BigDecimal pesoB, BigDecimal pesoM, BigDecimal pesoO, Integer nroPeriod, Integer anio)
	{
		BigDecimal initialInventoryaux = new BigDecimal(0);
		BigDecimal salesaux = new BigDecimal(0);
		BigDecimal rebate1aux = new BigDecimal(0);
		BigDecimal decreaseaux = new BigDecimal(0);
		BigDecimal discount1aux = new BigDecimal(0);
		BigDecimal purchaseaux = new BigDecimal(0);
		BigDecimal BudgetedFinalInventoryaux = new BigDecimal(0);
		String aniomes = null;
		BigDecimal invFinalProy = new BigDecimal(0);
		BigDecimal ventasMesCurso = new BigDecimal(0);
		//BigDecimal salesDistribution = new BigDecimal(0);
		
		X_XX_VMR_AssortmentPlan assortPlan = new X_XX_VMR_AssortmentPlan(getCtx(), 0, get_TrxName());
		
		// Busco las distribucion de venta del departamento por el periodo, luego calculas las Ventas del Mes en Curso
		//LO TENGO CON EL NUEVO CAMPO QUE AGREGO VICTOR (BORRAR LUEGO)
		/*String SQL7 = ("SELECT A.XX_SALESDISTRIBUTION AS SALESDISTRI " +
				"FROM XX_VMR_BUDGETSALESDEPART A, XX_VMR_REALCOMERCIALBUDGET B " +
				"WHERE XX_VMR_DEPARTMENT_ID = '"+department+"' " +
				"AND A.XX_VMR_REALCOMERCIALBUDGET_ID = B.XX_VMR_REALCOMERCIALBUDGET_ID " +
				"AND B.C_PERIOD_ID = '"+periodo+"' ");
		
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
		if(nroPeriod == 1) // pregunto si el periodo es el mes de JULIO para buscar su inventario inicial
		{
			// Busco el inventario final proyectado del mes de Junio(06anio) que va ser el inicial del mes de Julio del nuevo periodo
			aniomes = anio+"06";
			String sql2 = "select coalesce(sum(XX_MOINVFIPR),0) from i_xx_vmr_prld01 where xx_añomespre ='"+aniomes+"' and XX_VMR_DEPARTMENT_ID = '"+department+"' " +
					" AND XX_VMR_LINE_ID = '"+line+"' " +
					" AND XX_CODCAT<>99 and XX_CODDEP<>99 and XX_CODLIN<>99 and XX_CODSEC<>99 and XX_CODTIE<>99 ";			
			
			PreparedStatement prst12 = DB.prepareStatement(sql2,null);
			
			try {
				ResultSet rs12 = prst12.executeQuery();
				if (rs12.next())
				{
					if(rs12.getBigDecimal(1).compareTo(new BigDecimal(0))==0)
						invFinalProy = new BigDecimal(1);
					else
						invFinalProy = rs12.getBigDecimal(1);
					
					//System.out.println("inventario inicial Final MOINVFIPR " + invFinalProy);
					
					if(peso != null)
					{
						// Invetario inicial (Es el Inv final Proyectado distribuido por el peso de la Dptolinea)
						initialInventoryaux = invFinalProy.multiply(peso);
						assortPlan.setXX_InitialInventory(initialInventoryaux);
						
						//Ventas
						salesaux = sales.multiply(peso);
						assortPlan.setXX_SalesDistribution(salesaux);
						
						// Rebajas del mes en curso
						rebate1aux = rebate1.multiply(peso);
						assortPlan.setXX_Discount(rebate1aux);
						
						// Merma del mes en curso
						decreaseaux = decrease.multiply(peso);
						assortPlan.setXX_Decrease(decreaseaux);
						
						// Descuento Empleado
						discount1aux = discount1.multiply(peso);
						assortPlan.setXX_EmployeeDiscount(discount1aux);
						
						//Compras del mes en curso
						purchaseaux =  purchase.multiply(peso);
						assortPlan.setXX_Purchases(purchaseaux);
						
						//Inventario Final Presupuestado 
						//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
						// continuacion:                             - merma mes en curso - descuento de empleado en curso
						
						/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
						 * Ventas de Mes en Curso = DistribucionVenta/pesoLineaDpto */
						
						ventasMesCurso = sales.divide(peso, 2, RoundingMode.HALF_UP);
						
						BudgetedFinalInventoryaux = initialInventoryaux.add(purchaseaux).subtract(ventasMesCurso).subtract(rebate1aux).subtract(decreaseaux).subtract(discount1aux);
						
						// Ya no aplica este forma de calcular el Inventario Final
						/*BudgetedFinalInventoryaux = BudgetedFinalInventory.multiply(peso);*/
						assortPlan.setXX_BudgetedFinalInventory(BudgetedFinalInventoryaux);
					}
					
					if(pesoB != null)
					{
						// Invetario inicial Ya no aplica este calculo
						//initialInventoryaux = initialInventory.multiply(pesoB);
						// Invetario inicial (Es el Inv final Proyectado distribuido por el peso de la Dptolinea)
						initialInventoryaux = invFinalProy.multiply(pesoB);
						assortPlan.setXX_INITIALINVENTORYOK(initialInventoryaux);
						
						//Ventas
						salesaux = sales.multiply(pesoB);
						assortPlan.setXX_SalesDistributionOk(salesaux);
											
						// Rebajas del mes en curso
						rebate1aux = rebate1.multiply(pesoB);
						assortPlan.setXX_DISCOUNTOK(rebate1aux);
						
						// Merma del mes en curso
						decreaseaux = decrease.multiply(pesoB);
						assortPlan.setXX_DECREASEOK(decreaseaux);
						
						// Descuento Empleado
						discount1aux = discount1.multiply(peso);
						assortPlan.setXX_EMPLOYEDISCOUNTOK(discount1aux);
						
						//Compras del mes en curso
						purchaseaux =  purchase.multiply(pesoB);
						assortPlan.setXX_PURCHASESOK(purchaseaux);
						
						//Ya no aplica este forma de calcular el Inventario Final
						//Inventario Final Presupuestado
						//BudgetedFinalInventoryaux = BudgetedFinalInventory.multiply(pesoB);
						
						//Inventario Final Presupuestado 
						//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
						// continuacion:                             - merma mes en curso - descuento de empleado en curso

						/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
						 * Ventas de Mes en Curso = DistribucionVenta/pesoLineaDpto */
						
						ventasMesCurso = sales.divide(pesoB, 2, RoundingMode.HALF_UP);
						
						BudgetedFinalInventoryaux = initialInventoryaux.add(purchaseaux).subtract(ventasMesCurso).subtract(rebate1aux).subtract(decreaseaux).subtract(discount1aux);
						assortPlan.setXX_BUDGETEDFINALINVENTORYOK(BudgetedFinalInventoryaux);
					}
					
					if(pesoM != null) // tipo de peso MEJOR 
					{
						// Invetario inicial ya no aplica este calculo
						//initialInventoryaux = initialInventory.multiply(pesoM);
						
						// Invetario inicial (Es el Inv final Proyectado distribuido por el peso de la Dptolinea)
						initialInventoryaux = invFinalProy.multiply(pesoM);
						assortPlan.setXX_INITIALINVENTORYBEST(initialInventoryaux);
						
						//Ventas
						salesaux = sales.multiply(pesoM);
						assortPlan.setXX_SalesDistributionBest(salesaux);
											
						// Rebajas del mes en curso
						rebate1aux = rebate1.multiply(pesoM);
						assortPlan.setXX_DISCOUNTBEST(rebate1aux);
						
						// Merma del mes en curso
						decreaseaux = decrease.multiply(pesoM);
						assortPlan.setXX_DECREASEBEST(decreaseaux);
						
						// Descuento Empleado
						discount1aux = discount1.multiply(pesoM);
						assortPlan.setXX_EMPLOYEDISCOUNTBEST(discount1aux);
						
						//Compras del mes en curso
						purchaseaux =  purchase.multiply(pesoM);
						assortPlan.setXX_PURCHASESBEST(purchaseaux);
						
						//Inventario Final Presupuestado ya no aplica este calculo
						//BudgetedFinalInventoryaux = BudgetedFinalInventory.multiply(pesoM);
						
						
						//Inventario Final Presupuestado 
						//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
						// continuacion:                             - merma mes en curso - descuento de empleado en curso
						
						/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
						 * Ventas de Mes en Curso = DistribucionVenta/pesoLineaDpto */
						
						ventasMesCurso = sales.divide(pesoM, 2, RoundingMode.HALF_UP);
						
						BudgetedFinalInventoryaux = initialInventoryaux.add(purchaseaux).subtract(ventasMesCurso).subtract(rebate1aux).subtract(decreaseaux).subtract(discount1aux);
						assortPlan.setXX_BUDGETEDFINALINVENTORYBEST(BudgetedFinalInventoryaux);
					}
					
					if(pesoO != null) // tipo de peso OPTIMO
					{
						// Invetario inicial ya no aplica este calculo
						//initialInventoryaux = initialInventory.multiply(pesoO);
						
						// Invetario inicial (Es el Inv final Proyectado distribuido por el peso de la Dptolinea)
						initialInventoryaux = invFinalProy.multiply(pesoO);
						assortPlan.setXX_INITIALINVENTORYOPT(initialInventoryaux);
						
						//Ventas
						salesaux = sales.multiply(pesoO);
						assortPlan.setXX_SalesDistributionOpt(salesaux);
									
						// Rebajas del mes en curso
						rebate1aux = rebate1.multiply(pesoO);
						assortPlan.setXX_DISCOUNTOPT(rebate1aux);
						
						// Merma del mes en curso
						decreaseaux = decrease.multiply(pesoO);
						assortPlan.setXX_DECREASEOPT(decreaseaux);
						
						// Descuento Empleado
						discount1aux = discount1.multiply(pesoO);
						assortPlan.setXX_EMPLOYEDISCOUNTOPT(discount1aux);
						
						//Compras del mes en curso
						purchaseaux =  purchase.multiply(pesoO);
						assortPlan.setXX_PURCHASESOPT(purchaseaux);
						
						//Inventario Final Presupuestado ya no aplica este calculo
						//BudgetedFinalInventoryaux = BudgetedFinalInventory.multiply(pesoO);
			
						//Inventario Final Presupuestado 
						//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
						// continuacion:                             - merma mes en curso - descuento de empleado en curso
						
						/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
						 * Ventas de Mes en Curso = DistribucionVenta/pesoLineaDpto */
						
						ventasMesCurso = sales.divide(pesoO, 2, RoundingMode.HALF_UP);
						
						BudgetedFinalInventoryaux = initialInventoryaux.add(purchaseaux).subtract(ventasMesCurso).subtract(rebate1aux).subtract(decreaseaux).subtract(discount1aux);
						assortPlan.setXX_BUDGETEDFINALINVENTORYOPT(BudgetedFinalInventoryaux);
					}
					
					//Periodo
					assortPlan.setC_Period_ID(periodo);
					//Linea
					assortPlan.setXX_VMR_Line_ID(line);
					//Department
					assortPlan.setXX_VMR_Department_ID(department);
					assortPlan.setXX_VMR_ComercialBudgetTab_ID(comercialBugID);
					assortPlan.save();
					commit();
				} 
				rs12.close();
				prst12.close();
			} catch (SQLException e){
				System.out.println("Error " + e);	
				System.out.println(sql2);
			}
			
		} // end if
		else //Si el mes no es julio, en vez de buscarlo de la prld01 lo busco en el XX_VMR_AssortmentPlan y es el final del mes anterior
		{
			System.out.println("LLEGO A LOS PERIODOS MAYOR A 1");
			nroPeriod = nroPeriod - 1; // Le resto uno para que sea el periodo anterior
			BigDecimal finalInvPeso = new BigDecimal(0);
			BigDecimal finalInvPesoOk = new BigDecimal(0);
			BigDecimal finalInvPesoBest = new BigDecimal(0);
			BigDecimal finalInvPesoOp = new BigDecimal(0);
			
			/** Busco el inventario final con los diferentes peso del mes anterior que va ser el 
			 * Inventario Inicial */
			String SQL6 = ("SELECT XX_BUDGETEDFINALINVENTORY AS PESO, XX_BUDGETEDFINALINVENTORYOK AS PESOOK, XX_BUDGETEDFINALINVENTORYBEST AS PESOBEST, XX_BUDGETEDFINALINVENTORYOPT AS PESOOPT " +
					"FROM XX_VMR_ASSORTMENTPLAN A, C_PERIOD B " +
					"WHERE A.C_PERIOD_ID = B.C_PERIOD_ID " +
					"AND PERIODNO = '"+nroPeriod+"' " +
					"AND A.XX_VMR_DEPARTMENT_ID = '"+department+"' " +
					"AND A.XX_VMR_LINE_ID = '"+line+"' ");
		
			try
			{
				PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null);
				ResultSet rs6 = pstmt6.executeQuery();
				//System.out.println(SQL6); 
				if(rs6.next())
				{
					finalInvPeso = rs6.getBigDecimal("PESO");
					finalInvPesoOk = rs6.getBigDecimal("PESOOK");
					finalInvPesoBest = rs6.getBigDecimal("PESOBEST");
					finalInvPesoOp = rs6.getBigDecimal("PESOOPT");
				}
				rs6.close();
				pstmt6.close();
			}
			catch (SQLException e) {
				System.out.println("Error " + e);	
			}
			
			if(peso != null)
			{
				//Invetario inicial
				assortPlan.setXX_InitialInventory(finalInvPeso); //Guardo el Inventario Final del mes pasado que va ser el inicial de ese mes
				
				//Ventas
				salesaux = sales.multiply(peso);
				assortPlan.setXX_SalesDistribution(salesaux);
				
				// Rebajas del mes en curso
				rebate1aux = rebate1.multiply(peso);
				assortPlan.setXX_Discount(rebate1aux);
				
				// Merma del mes en curso
				decreaseaux = decrease.multiply(peso);
				assortPlan.setXX_Decrease(decreaseaux);
				
				// Descuento Empleado
				discount1aux = discount1.multiply(peso);
				assortPlan.setXX_EmployeeDiscount(discount1aux);
				
				//Compras del mes en curso
				purchaseaux =  purchase.multiply(peso);
				assortPlan.setXX_Purchases(purchaseaux);
				
				//Inventario Final Presupuestado 
				//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
				// continuacion:                             - merma mes en curso - descuento de empleado en curso
				
				/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
				 * Ventas de Mes en Curso = DistribucionVenta/pesoLineaDpto */
				
				ventasMesCurso = sales.divide(peso, 2, RoundingMode.HALF_UP);
				
				BudgetedFinalInventoryaux = finalInvPeso.add(purchaseaux).subtract(ventasMesCurso).subtract(rebate1aux).subtract(decreaseaux).subtract(discount1aux);
				assortPlan.setXX_BudgetedFinalInventory(BudgetedFinalInventoryaux);
				
				// Ya no aplica este forma de calcular el Inventario Final
				//BudgetedFinalInventoryaux = BudgetedFinalInventory.multiply(peso);	
			}
			if(pesoB != null)
			{
				//Invetario inicial
				assortPlan.setXX_INITIALINVENTORYOK(finalInvPesoOk); //Guardo el Inventario Final del mes pasado que va ser el inicial de ese mes
				
				//Ventas
				salesaux = sales.multiply(pesoB);
				assortPlan.setXX_SalesDistributionOk(salesaux);
								
				// Rebajas del mes en curso
				rebate1aux = rebate1.multiply(pesoB);
				assortPlan.setXX_DISCOUNTOK(rebate1aux);
				
				// Merma del mes en curso
				decreaseaux = decrease.multiply(pesoB);
				assortPlan.setXX_DECREASEOK(decreaseaux);
				
				// Descuento Empleado
				discount1aux = discount1.multiply(peso);
				assortPlan.setXX_EMPLOYEDISCOUNTOK(discount1aux);
				
				//Compras del mes en curso
				purchaseaux =  purchase.multiply(pesoB);
				assortPlan.setXX_PURCHASESOK(purchaseaux);
				
				//Ya no aplica este forma de calcular el Inventario Final
				//Inventario Final Presupuestado
				//BudgetedFinalInventoryaux = BudgetedFinalInventory.multiply(pesoB);
				
				//Inventario Final Presupuestado 
				//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
				// continuacion:                             - merma mes en curso - descuento de empleado en curso

				/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
				 * Ventas de Mes en Curso = DistribucionVenta/pesoLineaDpto */
				
				ventasMesCurso = sales.divide(pesoB, 2, RoundingMode.HALF_UP);
				
				BudgetedFinalInventoryaux = finalInvPesoOk.add(purchaseaux).subtract(ventasMesCurso).subtract(rebate1aux).subtract(decreaseaux).subtract(discount1aux);
				assortPlan.setXX_BUDGETEDFINALINVENTORYOK(BudgetedFinalInventoryaux);
			}
			if(pesoM != null) // tipo de peso MEJOR 
			{
				//Invetario inicial
				assortPlan.setXX_INITIALINVENTORYBEST(finalInvPesoBest); //Guardo el Inventario Final del mes pasado que va ser el inicial de ese mes
				
				//Ventas
				salesaux = sales.multiply(pesoM);
				assortPlan.setXX_SalesDistributionBest(salesaux);
						
				// Rebajas del mes en curso
				rebate1aux = rebate1.multiply(pesoM);
				assortPlan.setXX_DISCOUNTBEST(rebate1aux);
				
				// Merma del mes en curso
				decreaseaux = decrease.multiply(pesoM);
				assortPlan.setXX_DECREASEBEST(decreaseaux);
				
				// Descuento Empleado
				discount1aux = discount1.multiply(pesoM);
				assortPlan.setXX_EMPLOYEDISCOUNTBEST(discount1aux);
				
				//Compras del mes en curso
				purchaseaux =  purchase.multiply(pesoM);
				assortPlan.setXX_PURCHASESBEST(purchaseaux);
				
				//Inventario Final Presupuestado ya no aplica este calculo
				//BudgetedFinalInventoryaux = BudgetedFinalInventory.multiply(pesoM);
				
				
				//Inventario Final Presupuestado 
				//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
				// continuacion:                             - merma mes en curso - descuento de empleado en curso
				
				/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
				 * Ventas de Mes en Curso = DistribucionVenta/pesoLineaDpto */
				
				ventasMesCurso = sales.divide(pesoM, 2, RoundingMode.HALF_UP);
				
				BudgetedFinalInventoryaux = finalInvPesoBest.add(purchaseaux).subtract(ventasMesCurso).subtract(rebate1aux).subtract(decreaseaux).subtract(discount1aux);
				assortPlan.setXX_BUDGETEDFINALINVENTORYBEST(BudgetedFinalInventoryaux);
			}
			if(pesoO != null) // tipo de peso OPTIMO
			{
				// Invetario inicial
				assortPlan.setXX_INITIALINVENTORYOPT(finalInvPesoOp); //Guardo el Inventario Final del mes pasado que va ser el inicial de ese mes
				
				//Ventas
				salesaux = sales.multiply(pesoO);
				assortPlan.setXX_SalesDistributionOpt(salesaux);
				
				// Rebajas del mes en curso
				rebate1aux = rebate1.multiply(pesoO);
				assortPlan.setXX_DISCOUNTOPT(rebate1aux);
				
				// Merma del mes en curso
				decreaseaux = decrease.multiply(pesoO);
				assortPlan.setXX_DECREASEOPT(decreaseaux);
				
				// Descuento Empleado
				discount1aux = discount1.multiply(pesoO);
				assortPlan.setXX_EMPLOYEDISCOUNTOPT(discount1aux);
				
				//Compras del mes en curso
				purchaseaux =  purchase.multiply(pesoO);
				assortPlan.setXX_PURCHASESOPT(purchaseaux);
				
				//Inventario Final Presupuestado ya no aplica este calculo
				//BudgetedFinalInventoryaux = BudgetedFinalInventory.multiply(pesoO);
	
				//Inventario Final Presupuestado 
				//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
				// continuacion:                             - merma mes en curso - descuento de empleado en curso
				
				/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
				 * Ventas de Mes en Curso = DistribucionVenta/pesoLineaDpto */
				
				ventasMesCurso = sales.divide(pesoO, 2, RoundingMode.HALF_UP);
				
				BudgetedFinalInventoryaux = finalInvPesoOp.add(purchaseaux).subtract(ventasMesCurso).subtract(rebate1aux).subtract(decreaseaux).subtract(discount1aux);
				assortPlan.setXX_BUDGETEDFINALINVENTORYOPT(BudgetedFinalInventoryaux);
			}
			
			//Periodo
			assortPlan.setC_Period_ID(periodo);
			// Linea
			assortPlan.setXX_VMR_Line_ID(line);
			// department
			assortPlan.setXX_VMR_Department_ID(department);
			assortPlan.setXX_VMR_ComercialBudgetTab_ID(comercialBugID);
			assortPlan.save();
			commit();
			
		} // end else
		
		/*if(peso != null) 
		{
			// Invetario inicial
			initialInventoryaux = initialInventory.multiply(peso);
			assortPlan.setXX_InitialInventory(initialInventoryaux);
			
			// Rebajas del mes en curso
			rebate1aux = rebate1.multiply(peso);
			assortPlan.setXX_Discount(rebate1aux);
			
			// Merma del mes en curso
			decreaseaux = decrease.multiply(peso);
			assortPlan.setXX_Decrease(decreaseaux);
			
			// Descuento Empleado
			discount1aux = discount1.multiply(peso);
			assortPlan.setXX_EmployeeDiscount(discount1aux);
			
			//Compras del mes en curso
			purchaseaux =  purchase.multiply(peso);
			assortPlan.setXX_Purchases(purchaseaux);
			
			//Inventario Final Presupuestado
			BudgetedFinalInventoryaux = BudgetedFinalInventory.multiply(peso);
			assortPlan.setXX_BudgetedFinalInventory(BudgetedFinalInventoryaux);
		}*/
		
		/*if(pesoB != null)
		{
			// Invetario inicial
			initialInventoryaux = initialInventory.multiply(pesoB);
			assortPlan.setXX_INITIALINVENTORYOK(initialInventoryaux);
			
			// Rebajas del mes en curso
			rebate1aux = rebate1.multiply(pesoB);
			assortPlan.setXX_DISCOUNTOK(rebate1aux);
			
			// Merma del mes en curso
			decreaseaux = decrease.multiply(pesoB);
			assortPlan.setXX_DECREASEOK(decreaseaux);
			
			// Descuento Empleado
			discount1aux = discount1.multiply(peso);
			assortPlan.setXX_EMPLOYEDISCOUNTOK(discount1aux);
			
			//Compras del mes en curso
			purchaseaux =  purchase.multiply(pesoB);
			assortPlan.setXX_PURCHASESOK(purchaseaux);
			
			//Inventario Final Presupuestado
			BudgetedFinalInventoryaux = BudgetedFinalInventory.multiply(pesoB);
			assortPlan.setXX_BUDGETEDFINALINVENTORYOK(BudgetedFinalInventoryaux);
		}*/
		
		/*if(pesoM != null) // tipo de peso MEJOR 
		{
			// Invetario inicial
			initialInventoryaux = initialInventory.multiply(pesoM);
			assortPlan.setXX_INITIALINVENTORYBEST(initialInventoryaux);
			
			// Rebajas del mes en curso
			rebate1aux = rebate1.multiply(pesoM);
			assortPlan.setXX_DISCOUNTBEST(rebate1aux);
			
			// Merma del mes en curso
			decreaseaux = decrease.multiply(pesoM);
			assortPlan.setXX_DECREASEBEST(decreaseaux);
			
			// Descuento Empleado
			discount1aux = discount1.multiply(pesoM);
			assortPlan.setXX_EMPLOYEDISCOUNTBEST(discount1aux);
			
			//Compras del mes en curso
			purchaseaux =  purchase.multiply(pesoM);
			assortPlan.setXX_PURCHASESBEST(purchaseaux);
			
			//Inventario Final Presupuestado
			BudgetedFinalInventoryaux = BudgetedFinalInventory.multiply(pesoM);
			assortPlan.setXX_BUDGETEDFINALINVENTORYBEST(BudgetedFinalInventoryaux);

		}*/
		/*if(pesoO != null) // tipo de peso OPTIMO
		{
			// Invetario inicial
			initialInventoryaux = initialInventory.multiply(pesoO);
			assortPlan.setXX_INITIALINVENTORYOPT(initialInventoryaux);
			
			// Rebajas del mes en curso
			rebate1aux = rebate1.multiply(pesoO);
			assortPlan.setXX_DISCOUNTOPT(rebate1aux);
			
			// Merma del mes en curso
			decreaseaux = decrease.multiply(pesoO);
			assortPlan.setXX_DECREASEOPT(decreaseaux);
			
			// Descuento Empleado
			discount1aux = discount1.multiply(pesoO);
			assortPlan.setXX_EMPLOYEDISCOUNTOPT(discount1aux);
			
			//Compras del mes en curso
			purchaseaux =  purchase.multiply(pesoO);
			assortPlan.setXX_PURCHASESOPT(purchaseaux);
			
			//Inventario Final Presupuestado
			BudgetedFinalInventoryaux = BudgetedFinalInventory.multiply(pesoO);
			assortPlan.setXX_BUDGETEDFINALINVENTORYOPT(BudgetedFinalInventoryaux);
		}*/
		/*
		//Periodo
		assortPlan.setC_Period_ID(periodo);
		
		// Linea
		assortPlan.setXX_VMR_Line_ID(line);
		
		// department
		assortPlan.setXX_VMR_Department_ID(department);
		
		assortPlan.setXX_VMR_ComercialBudgetTab_ID(comercialBugID);
				
		assortPlan.save();*/

		return true;
	}
	

}
