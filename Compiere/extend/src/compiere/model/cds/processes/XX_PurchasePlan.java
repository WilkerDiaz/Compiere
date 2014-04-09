package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_XX_VMR_AssortmentPlan;
import compiere.model.cds.X_XX_VMR_PurchasePlan;

public class XX_PurchasePlan extends SvrProcess{

	Integer department = 0;
	Integer line = 0;
	Integer periodo = 0;
	
	BigDecimal initialInventory = new BigDecimal(0);
	BigDecimal sales = new BigDecimal(0);
	BigDecimal rebate1 = new BigDecimal(0);
	BigDecimal decrease = new BigDecimal(0);
	BigDecimal discount1 = new BigDecimal(0);
	BigDecimal purchase = new BigDecimal(0);
	BigDecimal BudgetedFinalInventory = new BigDecimal(0);
	
	protected String doIt() throws Exception {
		
		Integer assormentPlanID = 0;
		Integer section = 0;
		BigDecimal peso = new BigDecimal(0);
		BigDecimal pesoB = new BigDecimal(0);
		BigDecimal pesoM = new BigDecimal(0);
		BigDecimal pesoO = new BigDecimal(0);
		Integer nroPeriod = 0;
		Integer anio = 0;
		String aniomes = null;
		BigDecimal invFinalProy = new BigDecimal(0);
		//BigDecimal salesDistribution = new BigDecimal(0);
		
		//Inicio el proceso borrando las tablas que estan despues del Plan de Compra
		// Borro la tabla de distribución de tienda
		String SQL8 = ("DELETE FROM  XX_VMR_StoreDistri ");
		
		PreparedStatement pstmt8 = DB.prepareStatement(SQL8, null); 
	    ResultSet rs8 = pstmt8.executeQuery();
		rs8.close();
		pstmt8.close();
		commit();
		// Borro la tabla de Plan de Compra
		String SQL7 = ("DELETE FROM  XX_VMR_PurchasePlan ");
		
		PreparedStatement pstmt7 = DB.prepareStatement(SQL7, null); 
	    ResultSet rs7 = pstmt7.executeQuery();
		rs7.close();
		pstmt7.close();
		commit();
		
		//String SQL = ("SELECT XX_VMR_ASSORTMENTPLAN_ID AS ASSORPLAN FROM XX_VMR_ASSORTMENTPLAN ");
		String SQL = ("SELECT XX_VMR_ASSORTMENTPLAN_ID AS ASSORPLAN, B.PERIODNO AS NROPERIOD, TO_NUMBER(TO_CHAR (B.STARTDATE, 'YYYY')) ANIO " +
				"FROM XX_VMR_ASSORTMENTPLAN A, C_PERIOD B " +
				"WHERE A.C_PERIOD_ID = B.C_PERIOD_ID " +
				"ORDER BY B.PERIODNO ASC ");
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				assormentPlanID = rs.getInt("ASSORPLAN");
				nroPeriod = rs.getInt("NROPERIOD");
				anio = rs.getInt("ANIO");
				BigDecimal ventasMesCurso = new BigDecimal(0);
								
				X_XX_VMR_AssortmentPlan assormentPlan =  new X_XX_VMR_AssortmentPlan(getCtx(), assormentPlanID, get_TrxName());
				
				//Departamento
				department = assormentPlan.getXX_VMR_Department_ID();
				//Linea
				line = assormentPlan.getXX_VMR_Line_ID();
				//Periodo
				periodo = assormentPlan.getC_Period_ID();
				
				//Busco las distribucion de venta del departamento por el periodo, luego calculas las Ventas del Mes en Curso con ese valor
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
				
				 /**Busco los los pesos con consepto de valor por dpto y linea para calcular las variables del presupuesto
				  * comercial con el consepto de valor el el Plan de Compra */ 
			
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

				if(rs5.next())
				{
					pesoO = rs5.getBigDecimal("PESOOPTIMO");
					if(pesoO.compareTo(new BigDecimal(0))==0)
						pesoO = new BigDecimal(1);
				}
				rs5.close();
			    pstmt5.close();
	
			    
				//Busco las secciones de las lineas
				String SQL1 = ("SELECT XX_VMR_SECTION_ID AS SECTION " +
						"FROM XX_VMR_SECTION " +
						"WHERE XX_VMR_LINE_ID = '"+line+"' AND ISACTIVE = 'Y' ");

				PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null);
				ResultSet rs1 = pstmt1.executeQuery();
				
				while(rs1.next())
				{
					section = rs1.getInt("SECTION");
					
					X_XX_VMR_PurchasePlan purchasePlan = new X_XX_VMR_PurchasePlan(getCtx(), 0, get_TrxName()); 
					
					//Busco el peso de por departamento linea y seccion
					String SQL2 = ("SELECT CASE WHEN SUM(Weight) IS NULL THEN 0 ELSE SUM(Weight) END AS PESO " +
							"FROM XX_VMR_WeightAssigned " +
							"WHERE XX_VMR_DEPARTMENT_ID = '"+department+"' " +
							"AND XX_VMR_LINE_ID = '"+line+"' " +
							"AND XX_VMR_SECTION_ID = '"+section+"' " +
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
					
				    //System.out.println("NUMERO DEL PERIODO "+ nroPeriod);
				    if(nroPeriod == 1) //pregunto si el periodo es el mes de JULIO para buscar su inventario inicial
				    {
				    	aniomes = anio+"06";
				    	// Busco el inventario final proyectado del mes de Junio(06anio) que va ser el inicial del mes de Julio del nuevo periodo
				    	String SQL6 = ("select coalesce(sum(XX_MOINVFIPR),0) from i_xx_vmr_prld01 " +
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
								invFinalProy = new BigDecimal(1);
							else
								invFinalProy = rs6.getBigDecimal(1);
							
							//System.out.println("inventario inicial Final MOINVFIPR " + invFinalProy);
							
							//Inventario Inicial
							initialInventory = invFinalProy.multiply(peso);
							//Ventas
							sales = assormentPlan.getXX_SalesDistribution().multiply(peso);
							// Rebajas del mes en curso
							rebate1 = assormentPlan.getXX_Discount().multiply(peso);
							// Merma del mes en curso
							decrease = assormentPlan.getXX_Decrease().multiply(peso);
							// Descuento Empleado
							discount1 = assormentPlan.getXX_EmployeeDiscount().multiply(peso);
							// Compras
							purchase = assormentPlan.getXX_Purchases().multiply(peso);
							//Inventario Final Presupuestado
							//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
							// continuacion:                             - merma mes en curso - descuento de empleado en curso
							
							/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
							 * Ventas de Mes en Curso = DistribucionVenta/peso */
							
							ventasMesCurso = sales.divide(peso, 2, RoundingMode.HALF_UP);
							BudgetedFinalInventory = initialInventory.add(purchase).subtract(ventasMesCurso).subtract(rebate1).subtract(decrease).subtract(discount1);
							
							purchasePlan.setXX_InitialInventory(initialInventory);
							purchasePlan.setXX_SalesDistribution(sales);
						    purchasePlan.setXX_Discount(rebate1);
						    purchasePlan.setXX_Decrease(decrease);
						    purchasePlan.setXX_EMPLOYEDISCOUNT(discount1);
						    purchasePlan.setXX_Purchases(purchase);
						    purchasePlan.setXX_BudgetedFinalInventory(BudgetedFinalInventory);
						    
						    ///////////////////////////////////
						    //Con Consept Value Good 
						    //////////////////////////////////
						    //Inventario Inicial
						    initialInventory = invFinalProy.multiply(pesoB);
						    purchasePlan.setXX_INITIALINVENTORYOK(initialInventory); // Lo multiplico por el peso bueno del Dpto Linea
						    //Ventas
						    sales = assormentPlan.getXX_SalesDistributionOk().multiply(pesoB);
						    purchasePlan.setXX_SalesDistributionOk(sales);
						    //Rebajas del mes en curso
						    rebate1 = assormentPlan.getXX_DISCOUNTOK().multiply(pesoB);
						    purchasePlan.setXX_DISCOUNTOK(rebate1);
						    // Merma del mes en curso
						    decrease = assormentPlan.getXX_DECREASEOK().multiply(pesoB);
						    purchasePlan.setXX_DECREASEOK(decrease);
						    // Descuento Empleado
						    discount1 = assormentPlan.getXX_EMPLOYEDISCOUNTOK().multiply(pesoB);
						    purchasePlan.setXX_EMPLOYEDISCOUNTOK(discount1);
						    // Compras
						    purchase = assormentPlan.getXX_PURCHASESOK().multiply(pesoB);
						    purchasePlan.setXX_PURCHASESOK(purchase);
						    
						    //Inventario Final Presupuestado
							//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
							// continuacion:                             - merma mes en curso - descuento de empleado en curso
							
							/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
							 * Ventas de Mes en Curso = DistribucionVenta/peso */
						    
						    ventasMesCurso = sales.divide(pesoB, 2, RoundingMode.HALF_UP);
							BudgetedFinalInventory = initialInventory.add(purchase).subtract(ventasMesCurso).subtract(rebate1).subtract(decrease).subtract(discount1);
						    purchasePlan.setXX_BUDGETEDFINALINVENTORYOK(BudgetedFinalInventory);
						    
						    //////////////////////////////////
						    // Con Consept Value Best
						    //////////////////////////////////
						    //Inventario Inicial
						    initialInventory = invFinalProy.multiply(pesoM);
						    purchasePlan.setXX_INITIALINVENTORYBEST(initialInventory);
						    //Ventas
						    sales = assormentPlan.getXX_SalesDistributionBest().multiply(pesoM);
						    purchasePlan.setXX_SalesDistributionBest(sales);
						    //Rebajas del mes en curso
						    rebate1 = assormentPlan.getXX_DISCOUNTBEST().multiply(pesoM);
						    purchasePlan.setXX_DISCOUNTBEST(rebate1);
						    // Merma del mes en curso
						    decrease = assormentPlan.getXX_DECREASEBEST().multiply(pesoM);
						    purchasePlan.setXX_DECREASEBEST(decrease);
						    // Descuento Empleado
						    discount1 = assormentPlan.getXX_EMPLOYEDISCOUNTBEST().multiply(pesoM);
						    purchasePlan.setXX_EMPLOYEDISCOUNTBEST(discount1);
						    // Compras
						    purchase = assormentPlan.getXX_PURCHASESBEST().multiply(pesoM);
						    purchasePlan.setXX_PURCHASESBEST(purchase);
						    
						    //Inventario Final Presupuestado
						    ventasMesCurso = sales.divide(pesoM, 2, RoundingMode.HALF_UP);
							BudgetedFinalInventory = initialInventory.add(purchase).subtract(ventasMesCurso).subtract(rebate1).subtract(decrease).subtract(discount1);
							purchasePlan.setXX_BUDGETEDFINALINVENTORYBEST(BudgetedFinalInventory);
						    
							////////////////////////////////////////
							// Con Consept Value Optimum
							////////////////////////////////////////
							//Inventario Inicial
							initialInventory = invFinalProy.multiply(pesoO);
						    purchasePlan.setXX_INITIALINVENTORYOPT(initialInventory);
						    //Ventas
						    sales = assormentPlan.getXX_SalesDistributionOpt().multiply(pesoO);
						    purchasePlan.setXX_SalesDistributionOpt(sales);
						    //Rebajas del mes en curso
						    rebate1 = assormentPlan.getXX_DISCOUNTOPT().multiply(pesoO);
						    purchasePlan.setXX_DISCOUNTOPT(rebate1);
						    // Merma del mes en curso
						    decrease = assormentPlan.getXX_DECREASEOPT().multiply(pesoO);
						    purchasePlan.setXX_DECREASEOPT(decrease);
						    // Descuento Empleado
						    discount1 = assormentPlan.getXX_EMPLOYEDISCOUNTOPT().multiply(pesoO);
						    purchasePlan.setXX_EMPLOYEDISCOUNTOPT(discount1);
							// Compras
						    purchase = assormentPlan.getXX_PURCHASESOPT().multiply(pesoO);
						    purchasePlan.setXX_PURCHASESOPT(purchase);
						    //Inventario Final Presupuestado
						    ventasMesCurso = sales.divide(pesoO, 2, RoundingMode.HALF_UP);
							BudgetedFinalInventory = initialInventory.add(purchase).subtract(ventasMesCurso).subtract(rebate1).subtract(decrease).subtract(discount1);
						    purchasePlan.setXX_BUDGETEDFINALINVENTORYOPT(BudgetedFinalInventory);
							
						    purchasePlan.setC_Period_ID(periodo);
						    purchasePlan.setXX_VMR_Department_ID(department);
						    purchasePlan.setXX_VMR_Line_ID(line);
						    purchasePlan.setXX_VMR_Section_ID(section);
						    purchasePlan.setXX_VMR_ASSORTMENTPLAN_ID(assormentPlanID);
						    purchasePlan.save();
						    commit();
						}
				    	rs6.close();
				    	pstmt6.close();
				    	
				    }else //Si el mes no es julio, en vez de buscarlo de la prld01 lo busco en el XX_VMR_PurchasePlan y es el final del mes anterior
				    {
				    	//System.out.println("LLEGO A LOS PERIODOS MAYOR A 1");
				    	Integer nroPeriodAux = 0; 
				    	nroPeriodAux = nroPeriod - 1; // Le resto uno para que sea el periodo anterior
				    	BigDecimal finalInvPeso = new BigDecimal(0);
						BigDecimal finalInvPesoOk = new BigDecimal(0);
						BigDecimal finalInvPesoBest = new BigDecimal(0);
						BigDecimal finalInvPesoOp = new BigDecimal(0);
						
						/** Busco el inventario final con los diferentes peso del mes anterior que va ser el 
						 * Inventario Inicial */
						String SQL6 = ("SELECT XX_BUDGETEDFINALINVENTORY AS PESO, XX_BUDGETEDFINALINVENTORYOK AS PESOOK, XX_BUDGETEDFINALINVENTORYBEST AS PESOBEST, XX_BUDGETEDFINALINVENTORYOPT AS PESOOPT " +
								"FROM XX_VMR_PURCHASEPLAN A, C_PERIOD B " +
								"WHERE A.C_PERIOD_ID = B.C_PERIOD_ID " +
								"AND PERIODNO = '"+nroPeriodAux+"' " +
								"AND A.XX_VMR_DEPARTMENT_ID = '"+department+"' " +
								"AND A.XX_VMR_LINE_ID = '"+line+"' " +
								"AND A.XX_VMR_SECTION_ID = '"+section+"' ");
						
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
								
								
								//Inventario Inicial
								initialInventory = finalInvPeso;
								purchasePlan.setXX_InitialInventory(initialInventory);
								//Ventas
							    sales = assormentPlan.getXX_SalesDistribution().multiply(peso);
							    purchasePlan.setXX_SalesDistribution(sales);
								// Rebajas del mes en curso
								rebate1 = assormentPlan.getXX_Discount().multiply(peso);
								purchasePlan.setXX_Discount(rebate1);
								// Merma del mes en curso
								purchasePlan.setXX_Decrease(decrease);
								decrease = assormentPlan.getXX_Decrease().multiply(peso);
								// Descuento Empleado
								discount1 = assormentPlan.getXX_EmployeeDiscount().multiply(peso);
								purchasePlan.setXX_EMPLOYEDISCOUNT(discount1);
								// Compras
								purchasePlan.setXX_Purchases(purchase);
								purchase = assormentPlan.getXX_Purchases().multiply(peso);
								//Inventario Final Presupuestado
								//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
								// continuacion:                             - merma mes en curso - descuento de empleado en curso
								
								/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
								 * Ventas de Mes en Curso = DistribucionVenta/peso */
								
								ventasMesCurso = sales.divide(peso, 2, RoundingMode.HALF_UP);
								BudgetedFinalInventory = initialInventory.add(purchase).subtract(ventasMesCurso).subtract(rebate1).subtract(decrease).subtract(discount1);
								purchasePlan.setXX_BudgetedFinalInventory(BudgetedFinalInventory);
								
								///////////////////////////////////
							    //Con Consept Value Good 
							    //////////////////////////////////
							    //Inventario Inicial
							    initialInventory = finalInvPesoOk;
							    purchasePlan.setXX_INITIALINVENTORYOK(initialInventory); // Lo multiplico por el peso bueno del Dpto Linea
							    //Ventas
							    sales = assormentPlan.getXX_SalesDistributionOk().multiply(pesoB);
							    purchasePlan.setXX_SalesDistributionOk(sales);
							    //Rebajas del mes en curso
							    rebate1 = assormentPlan.getXX_DISCOUNTOK().multiply(pesoB);
							    purchasePlan.setXX_DISCOUNTOK(rebate1);
							    // Merma del mes en curso
							    decrease = assormentPlan.getXX_DECREASEOK().multiply(pesoB);
							    purchasePlan.setXX_DECREASEOK(decrease);
							    // Descuento Empleado
							    discount1 = assormentPlan.getXX_EMPLOYEDISCOUNTOK().multiply(pesoB);
							    purchasePlan.setXX_EMPLOYEDISCOUNTOK(discount1);
							    // Compras
							    purchase = assormentPlan.getXX_PURCHASESOK().multiply(pesoB);
							    purchasePlan.setXX_PURCHASESOK(purchase);
							    
							    //Inventario Final Presupuestado
								//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
								// continuacion:                             - merma mes en curso - descuento de empleado en curso
								
								/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
								 * Ventas de Mes en Curso = DistribucionVenta/peso */
							    
							    ventasMesCurso = sales.divide(pesoB, 2, RoundingMode.HALF_UP);
								BudgetedFinalInventory = initialInventory.add(purchase).subtract(ventasMesCurso).subtract(rebate1).subtract(decrease).subtract(discount1);
							    purchasePlan.setXX_BUDGETEDFINALINVENTORYOK(BudgetedFinalInventory);
								
							    //////////////////////////////////
							    // Con Consept Value Best
							    //////////////////////////////////
							    //Inventario Inicial
							    initialInventory = finalInvPesoBest;
							    purchasePlan.setXX_INITIALINVENTORYBEST(initialInventory);
							    //Ventas
							    sales = assormentPlan.getXX_SalesDistributionBest().multiply(pesoM);
							    purchasePlan.setXX_SalesDistributionBest(sales);
							    //Rebajas del mes en curso
							    rebate1 = assormentPlan.getXX_DISCOUNTBEST().multiply(pesoM);
							    purchasePlan.setXX_DISCOUNTBEST(rebate1);
							    // Merma del mes en curso
							    decrease = assormentPlan.getXX_DECREASEBEST().multiply(pesoM);
							    purchasePlan.setXX_DECREASEBEST(decrease);
							    // Descuento Empleado
							    discount1 = assormentPlan.getXX_EMPLOYEDISCOUNTBEST().multiply(pesoM);
							    purchasePlan.setXX_EMPLOYEDISCOUNTBEST(discount1);
							    // Compras
							    purchase = assormentPlan.getXX_PURCHASESBEST().multiply(pesoM);
							    purchasePlan.setXX_PURCHASESBEST(purchase);
							    
							    //Inventario Final Presupuestado
							    ventasMesCurso = sales.divide(pesoM, 2, RoundingMode.HALF_UP);
								BudgetedFinalInventory = initialInventory.add(purchase).subtract(ventasMesCurso).subtract(rebate1).subtract(decrease).subtract(discount1);
								purchasePlan.setXX_BUDGETEDFINALINVENTORYBEST(BudgetedFinalInventory);
								
								////////////////////////////////////////
								// Con Consept Value Optimum
								////////////////////////////////////////
								//Inventario Inicial
								initialInventory = finalInvPesoOp;
							    purchasePlan.setXX_INITIALINVENTORYOPT(initialInventory);
							    //Ventas
							    sales = assormentPlan.getXX_SalesDistributionOpt().multiply(pesoO);
							    purchasePlan.setXX_SalesDistributionOpt(sales);
							    //Rebajas del mes en curso
							    rebate1 = assormentPlan.getXX_DISCOUNTOPT().multiply(pesoO);
							    purchasePlan.setXX_DISCOUNTOPT(rebate1);
							    // Merma del mes en curso
							    decrease = assormentPlan.getXX_DECREASEOPT().multiply(pesoO);
							    purchasePlan.setXX_DECREASEOPT(decrease);
							    // Descuento Empleado
							    discount1 = assormentPlan.getXX_EMPLOYEDISCOUNTOPT().multiply(pesoO);
							    purchasePlan.setXX_EMPLOYEDISCOUNTOPT(discount1);
								// Compras
							    purchase = assormentPlan.getXX_PURCHASESOPT().multiply(pesoO);
							    purchasePlan.setXX_PURCHASESOPT(purchase);
							    //Inventario Final Presupuestado
							    ventasMesCurso = sales.divide(pesoO, 2, RoundingMode.HALF_UP);
								BudgetedFinalInventory = initialInventory.add(purchase).subtract(ventasMesCurso).subtract(rebate1).subtract(decrease).subtract(discount1);
							    purchasePlan.setXX_BUDGETEDFINALINVENTORYOPT(BudgetedFinalInventory);
								
							    purchasePlan.setC_Period_ID(periodo);
							    purchasePlan.setXX_VMR_Department_ID(department);
							    purchasePlan.setXX_VMR_Line_ID(line);
							    purchasePlan.setXX_VMR_Section_ID(section);
							    purchasePlan.setXX_VMR_ASSORTMENTPLAN_ID(assormentPlanID);
							    purchasePlan.save();
							    commit();
								
							}
							rs6.close();
							pstmt6.close();
						}
						catch (SQLException e) {
							System.out.println("Error " + e);	
						}
				
				    } // end else
				    
				  /*  
				    //Inventario Inicial
					initialInventory = assormentPlan.getXX_InitialInventory().multiply(peso);
					// Rebajas del mes en curso
					rebate1 = assormentPlan.getXX_Discount().multiply(peso);
					// Merma del mes en curso
					decrease = assormentPlan.getXX_Decrease().multiply(peso);
					// Descuento Empleado
					discount1 = assormentPlan.getXX_EmployeeDiscount().multiply(peso);
					// Compras
					purchase = assormentPlan.getXX_Purchases().multiply(peso);
					//Inventario Final Presupuestado
					BudgetedFinalInventory = assormentPlan.getXX_BudgetedFinalInventory().multiply(peso);
					
					purchasePlan.setXX_InitialInventory(initialInventory);
				    purchasePlan.setXX_Discount(rebate1);
				    purchasePlan.setXX_Decrease(decrease);
				    purchasePlan.setXX_EMPLOYEDISCOUNT(discount1);
				    purchasePlan.setXX_Purchases(purchase);
				    purchasePlan.setXX_BudgetedFinalInventory(BudgetedFinalInventory);
				    */
				    // Con Consept Value Good
				    /*purchasePlan.setXX_INITIALINVENTORYOK(assormentPlan.getXX_INITIALINVENTORYOK().multiply(peso));
				    purchasePlan.setXX_DISCOUNTOK(assormentPlan.getXX_DISCOUNTOK().multiply(peso));
				    purchasePlan.setXX_DECREASEOK(assormentPlan.getXX_DECREASEOK().multiply(peso));
				    purchasePlan.setXX_EMPLOYEDISCOUNTOK(assormentPlan.getXX_EMPLOYEDISCOUNTOK().multiply(peso));
				    purchasePlan.setXX_PURCHASESOK(assormentPlan.getXX_PURCHASESOK().multiply(peso));
				    purchasePlan.setXX_BUDGETEDFINALINVENTORYOK(assormentPlan.getXX_BUDGETEDFINALINVENTORYOK().multiply(peso));
				    // Con Consept Value Best
				    purchasePlan.setXX_INITIALINVENTORYBEST(assormentPlan.getXX_INITIALINVENTORYBEST().multiply(peso));
				    purchasePlan.setXX_DISCOUNTBEST(assormentPlan.getXX_DISCOUNTBEST().multiply(peso));
				    purchasePlan.setXX_DECREASEBEST(assormentPlan.getXX_DECREASEBEST().multiply(peso));
				    purchasePlan.setXX_EMPLOYEDISCOUNTBEST(assormentPlan.getXX_EMPLOYEDISCOUNTBEST().multiply(peso));
				    purchasePlan.setXX_PURCHASESBEST(assormentPlan.getXX_PURCHASESBEST().multiply(peso));
				    purchasePlan.setXX_BUDGETEDFINALINVENTORYBEST(assormentPlan.getXX_BUDGETEDFINALINVENTORYBEST().multiply(peso));
				    // Con Consept Value Optimum
				    purchasePlan.setXX_INITIALINVENTORYOPT(assormentPlan.getXX_INITIALINVENTORYOPT().multiply(peso));
				    purchasePlan.setXX_DISCOUNTOPT(assormentPlan.getXX_DISCOUNTOPT().multiply(peso));
				    purchasePlan.setXX_DECREASEOPT(assormentPlan.getXX_DECREASEOPT().multiply(peso));
				    purchasePlan.setXX_EMPLOYEDISCOUNTOPT(assormentPlan.getXX_EMPLOYEDISCOUNTOPT().multiply(peso));
				    purchasePlan.setXX_PURCHASESOPT(assormentPlan.getXX_PURCHASESOPT().multiply(peso));
				    purchasePlan.setXX_BUDGETEDFINALINVENTORYOPT(assormentPlan.getXX_BUDGETEDFINALINVENTORYOPT().multiply(peso));
				    
				    purchasePlan.setC_Period_ID(periodo);
				    purchasePlan.setXX_VMR_Department_ID(department);
				    purchasePlan.setXX_VMR_Line_ID(line);
				    purchasePlan.setXX_VMR_Section_ID(section);
				    purchasePlan.setXX_VMR_ASSORTMENTPLAN_ID(assormentPlanID);
				    purchasePlan.save();*/
					
				}// end while1
				rs1.close();
				pstmt1.close();
				
			}// end while
			rs.close();
			pstmt.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Proceso Realizado";
	}

	@Override
	protected void prepare() {
				
	}

}
