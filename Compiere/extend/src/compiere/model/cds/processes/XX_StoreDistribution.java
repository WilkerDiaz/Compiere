package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MVMRStoreDistri;
import compiere.model.cds.X_XX_VMR_AssortmentPlan;
import compiere.model.cds.X_XX_VMR_ComercialBudgetTab;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_PurchasePlan;

public class XX_StoreDistribution extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
			
		Integer purchasePlanid = 0;
		Integer period =0;
		Integer department = 0;
		Integer line = 0;
		Integer section = 0;
		Integer store = 0;
		Integer nroPeriod = 0;
		Integer anio = 0;
		BigDecimal salesDistribution = new BigDecimal(0);
		BigDecimal invFinalProy = new BigDecimal(0);
		String aniomes = null;
		
		// Variables Bolivares
		BigDecimal initialInventory = new BigDecimal(0);
		BigDecimal sales = new BigDecimal(0);
		BigDecimal discount = new BigDecimal(0);
		BigDecimal decrease = new BigDecimal(0);
		BigDecimal employeeDiscount = new BigDecimal(0);
		BigDecimal purchase = new BigDecimal(0);
		BigDecimal BudgetedFinalInventory = new BigDecimal(0);
		
		// Variables Piezas
		Integer initialInventoryPieces = 0;
		Integer salesPieces = 0;
		Integer decreasePieces = 0;
		Integer purchasePieces = 0;
		Integer BudgetedFinalInventoryPieces = 0;
		
		BigDecimal peso = new BigDecimal(0);
		Integer invFinalProyPieces = 0;
		
		// Borro la tabla de distribución de tienda
		String SQL8 = ("DELETE FROM  XX_VMR_StoreDistri ");
		
		PreparedStatement pstmt8 = DB.prepareStatement(SQL8, null); 
	    ResultSet rs8 = pstmt8.executeQuery();
		rs8.close();
		pstmt8.close();
		commit();

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
				
				X_XX_VMR_PurchasePlan purchasePlan = new X_XX_VMR_PurchasePlan(getCtx(), purchasePlanid, null);
				
				period = purchasePlan.getC_Period_ID();
				department = purchasePlan.getXX_VMR_Department_ID();
				line = purchasePlan.getXX_VMR_Line_ID();
				section = purchasePlan.getXX_VMR_Section_ID();

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
				
				//Comentar luego
				/*initialInventory = purchasePlan.getXX_InitialInventory();
				discount = purchasePlan.getXX_Discount();
				decrease = purchasePlan.getXX_Decrease();
				employeeDiscount = purchasePlan.getXX_EMPLOYEDISCOUNT();
				purchase = purchasePlan.getXX_Purchases();
				BudgetedFinalInventory = purchasePlan.getXX_BudgetedFinalInventory();*/
				//////////////////////////////////////////////////////////////////////////////
				String SQL2 = ("SELECT M_WAREHOUSE_ID AS TIENDAID FROM M_WAREHOUSE WHERE ISACTIVE = 'Y' ");
				
				PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null);
				ResultSet rs2 = pstmt2.executeQuery();
				
				while(rs2.next())
				{
					store = rs2.getInt("TIENDAID");
					
					String SQL3 = ("SELECT XX_WAREHOUSEWEIGHT AS PESO " +
							"FROM XX_VMR_WarehouseWeight " +
							"WHERE C_Period_ID = '"+period+"' " +
							"AND M_Warehouse_ID = '"+store+"' ");
					
					PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null);
					ResultSet rs3 = pstmt3.executeQuery();
					
					if(rs3.next())
					{
						peso = rs3.getBigDecimal("PESO");
						
						//if (peso.compareTo(new BigDecimal(0))==0)
						if(peso.intValue() == 0)
							peso = new BigDecimal(1);
						// comentar luego 
						/*System.out.println("Periodo "+period);
						System.out.println("Tienda "+store);
						System.out.println("Peso "+peso);
						////////////////////////////////////////////////////
					
						// Variables Bolivares
						initialInventory = purchasePlan.getXX_InitialInventory().divide(peso, 2, RoundingMode.HALF_UP);
						discount = purchasePlan.getXX_Discount().divide(peso, 2, RoundingMode.HALF_UP);
						decrease = purchasePlan.getXX_Decrease().divide(peso, 2, RoundingMode.HALF_UP);
						employeeDiscount = purchasePlan.getXX_EMPLOYEDISCOUNT().divide(peso, 2, RoundingMode.HALF_UP);
						purchase = purchasePlan.getXX_Purchases().divide(peso, 2, RoundingMode.HALF_UP);
						BudgetedFinalInventory = purchasePlan.getXX_BudgetedFinalInventory().divide(peso, 2, RoundingMode.HALF_UP);
						
						//Variables Piezas
						initialInventoryPieces = purchasePlan.getXX_InitialInventoryPieces().divide(peso, 2, RoundingMode.HALF_UP);
						decreasePieces = purchasePlan.getXX_DecreasePieces().divide(peso, 2, RoundingMode.HALF_UP);
						purchasePieces = purchasePlan.getXX_PurchasesPieces().divide(peso, 2, RoundingMode.HALF_UP);
						BudgetedFinalInventoryPieces = purchasePlan.getXX_BudFinalInventoryPiece().divide(peso, 2, RoundingMode.HALF_UP);
						
						MVMRStoreDistri storeDistri = new MVMRStoreDistri(getCtx(), 0, null, purchasePlanid);
						
						X_XX_VMR_Department dpto = new X_XX_VMR_Department(getCtx(), department, null); 
												
						storeDistri.setC_Period_ID(period);
						storeDistri.setXX_VMR_Category_ID(dpto.getXX_VMR_Category_ID());
						storeDistri.setXX_VMR_Department_ID(department);
						storeDistri.setXX_VMR_Line_ID(line);
						storeDistri.setXX_VMR_Section_ID(section);
						storeDistri.setM_Warehouse_ID(store);
						storeDistri.setXX_InitialInventory(initialInventory);
						storeDistri.setXX_Discount(discount);
						storeDistri.setXX_Decrease(decrease);
						storeDistri.setXX_EMPLOYEDISCOUNT(employeeDiscount);
						storeDistri.setXX_Purchases(purchase);
						storeDistri.setXX_BudgetedFinalInventory(BudgetedFinalInventory);
						//storeDistri.setXX_VMR_PURCHASEPLAN_ID(purchasePlanid);
						storeDistri.setXX_InitialInventoryPieces(initialInventoryPieces);
						storeDistri.setXX_DecreasePieces(decreasePieces);
						storeDistri.setXX_PurchasesPieces(purchasePieces);
						storeDistri.setXX_BudFinalInventoryPiece(BudgetedFinalInventoryPieces);

						storeDistri.save();*/
					}
					rs3.next();
					pstmt3.close();
					//System.out.println("NUMERO DEL PERIODO "+ nroPeriod);
					if(nroPeriod == 1) //pregunto si el periodo es el mes de JULIO para buscar su inventario inicial
					{
						aniomes = anio+"06";
				    	// Busco el inventario final proyectado del mes de Junio(06anio) que va ser el inicial del mes de Julio del nuevo periodo
				    	String SQL6 = ("select coalesce(sum(XX_MOINVFIPR),0) AS MONTO, coalesce(sum(XX_CAINVFIPR),0) AS PIEZAS " +
				    			"from i_xx_vmr_prld01 " +
				    			"where xx_añomespre = '"+aniomes+"' " +
				    			"and XX_VMR_DEPARTMENT_ID = '"+department+"' " +
				    			"AND XX_VMR_LINE_ID =  '"+line+"' " +
				    			"AND XX_VMR_SECTION_ID = '"+section+"' " +
				    			"AND M_WAREHOUSE_ID = '"+store+"' " +
				    			"AND XX_CODCAT<>99 and XX_CODDEP<>99 and XX_CODLIN<>99 and XX_CODSEC<>99 and XX_CODTIE<>99 ");
				    	
				    	PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null);
						ResultSet rs6 = pstmt6.executeQuery();
						
						if(rs6.next())
						{
							if(rs6.getBigDecimal("MONTO").compareTo(new BigDecimal(0)) == 0)
								invFinalProy = new BigDecimal(1);
							else
								invFinalProy = rs6.getBigDecimal("MONTO");
							
							if(rs6.getInt("PIEZAS")== 0)
								invFinalProyPieces = 1;
							else
								invFinalProyPieces = rs6.getInt("PIEZAS");

							System.out.println("inventario inicial Final CA_MOINVFIPR " + invFinalProy + " " + invFinalProyPieces);
							
							////////////////////////////////////
							// Variables Bolivares
							////////////////////////////////////
							initialInventory = invFinalProy.divide(peso, 2, RoundingMode.HALF_UP);
							sales = purchasePlan.getXX_SalesDistribution().divide(peso, 2, RoundingMode.HALF_UP);
							discount = purchasePlan.getXX_Discount().divide(peso, 2, RoundingMode.HALF_UP);
							decrease = purchasePlan.getXX_Decrease().divide(peso, 2, RoundingMode.HALF_UP);
							employeeDiscount = purchasePlan.getXX_EMPLOYEDISCOUNT().divide(peso, 2, RoundingMode.HALF_UP);
							purchase = purchasePlan.getXX_Purchases().divide(peso, 2, RoundingMode.HALF_UP);
							
							//Inventario Final Presupuestado
							//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
							// continuacion:                             - merma mes en curso - descuento de empleado en curso
							
							/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
							 * Ventas de Mes en Curso = DistribucionVenta/pesoLineaDpto */
							
							ventasMesCurso = sales.divide(peso, 2, RoundingMode.HALF_UP);
							BudgetedFinalInventory = initialInventory.add(purchase).subtract(ventasMesCurso).subtract(discount).subtract(decrease).subtract(employeeDiscount);
														
							////////////////////////////////
							//Variables Piezas
							////////////////////////////////
							initialInventoryPieces = invFinalProyPieces/peso.intValue();
							salesPieces = purchasePlan.getXX_SalesDistributionPiece()/peso.intValue();
							decreasePieces = purchasePlan.getXX_DecreasePieces()/peso.intValue();
							purchasePieces = purchasePlan.getXX_PurchasesPieces()/peso.intValue();
							
							//Inventario Final Presupuestado
							//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso
							// continuacion:                             - merma mes en curso
							
							/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
							 * Ventas de Mes en Curso = DistribucionVenta/peso */
							
							ventasMesCurso = salesDistribution.divide(peso, 2, RoundingMode.HALF_UP);
							BudgetedFinalInventoryPieces = (initialInventoryPieces) + (purchasePieces) - (ventasMesCurso.intValue()) - (decreasePieces);
							
							MVMRStoreDistri storeDistri = new MVMRStoreDistri(getCtx(), 0, null, purchasePlanid);
							
							X_XX_VMR_Department dpto = new X_XX_VMR_Department(getCtx(), department, null);
							
							storeDistri.setC_Period_ID(period);
							storeDistri.setXX_VMR_Category_ID(dpto.getXX_VMR_Category_ID());
							storeDistri.setXX_VMR_Department_ID(department);
							storeDistri.setXX_VMR_Line_ID(line);
							storeDistri.setXX_VMR_Section_ID(section);
							storeDistri.setM_Warehouse_ID(store);
							storeDistri.setXX_InitialInventory(initialInventory);
							storeDistri.setXX_SalesDistribution(sales);
							storeDistri.setXX_Discount(discount);
							storeDistri.setXX_Decrease(decrease);
							storeDistri.setXX_EMPLOYEDISCOUNT(employeeDiscount);
							storeDistri.setXX_Purchases(purchase);
							storeDistri.setXX_BudgetedFinalInventory(BudgetedFinalInventory);
							//storeDistri.setXX_VMR_PURCHASEPLAN_ID(purchasePlanid);
							storeDistri.setXX_InitialInventoryPieces(initialInventoryPieces);
							storeDistri.setXX_SalesDistributionPiece(salesPieces);
							storeDistri.setXX_DecreasePieces(decreasePieces);
							storeDistri.setXX_PurchasesPieces(purchasePieces);
							storeDistri.setXX_BudFinalInventoryPiece(BudgetedFinalInventoryPieces);
							storeDistri.save();
							commit();
							
						}
						rs6.close();
						pstmt6.close();
						
					}else
					{
						Integer nroPeriodAux = 0; 
						nroPeriodAux = nroPeriod - 1; // Le resto uno para que sea el periodo anterior
				    	BigDecimal finalInv = new BigDecimal(0);
				    	Integer finalInvPieces = 0;
				    	System.out.println("LLEGO A LOS PERIODOS MAYOR A 1");
				    	/** Busco el inventario final con los diferentes peso del mes anterior que va ser el 
						 * Inventario Inicial */
						String SQL6 = ("SELECT XX_BUDGETEDFINALINVENTORY AS PESO, XX_BUDFINALINVENTORYPIECE AS PIECE " +
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
							
							if(rs6.next())
							{
								finalInv = rs6.getBigDecimal("PESO");
								finalInvPieces = rs6.getInt("PIECE");
								
								/////////////////////////////////
								// Variables Bolivares
								/////////////////////////////////
								initialInventory = finalInv;
								sales = purchasePlan.getXX_SalesDistribution().divide(peso, 2, RoundingMode.HALF_UP);
								discount = purchasePlan.getXX_Discount().divide(peso, 2, RoundingMode.HALF_UP);
								decrease = purchasePlan.getXX_Decrease().divide(peso, 2, RoundingMode.HALF_UP);
								employeeDiscount = purchasePlan.getXX_EMPLOYEDISCOUNT().divide(peso, 2, RoundingMode.HALF_UP);
								purchase = purchasePlan.getXX_Purchases().divide(peso, 2, RoundingMode.HALF_UP);
								
								//Inventario Final Presupuestado
								//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
								// continuacion:                             - merma mes en curso - descuento de empleado en curso
								
								/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
								 * Ventas de Mes en Curso = DistribucionVenta/pesoLineaDpto */
								
								ventasMesCurso = sales.divide(peso, 2, RoundingMode.HALF_UP);
								BudgetedFinalInventory = initialInventory.add(purchase).subtract(ventasMesCurso).subtract(discount).subtract(decrease).subtract(employeeDiscount);
								
								//////////////////////////////////
								//Variables Piezas
								/////////////////////////////////
								initialInventoryPieces = finalInvPieces;
								salesPieces = purchasePlan.getXX_SalesDistributionPiece()/peso.intValue();
								decreasePieces = purchasePlan.getXX_DecreasePieces()/peso.intValue();
								purchasePieces = purchasePlan.getXX_PurchasesPieces()/peso.intValue();
								//Inventario Final Presupuestado
								//Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso
								// continuacion:                             - merma mes en curso
								
								/**Busco las ventas del mes en curso por las demas variables son las 4 anteriores 
								 * Ventas de Mes en Curso = DistribucionVenta/peso */
								
								ventasMesCurso = salesDistribution.divide(peso, 2, RoundingMode.HALF_UP);
								BudgetedFinalInventoryPieces = (initialInventoryPieces) + (purchasePieces) - (ventasMesCurso.intValue()) - (decreasePieces);
								
								MVMRStoreDistri storeDistri = new MVMRStoreDistri(getCtx(), 0, null, purchasePlanid);
								
								X_XX_VMR_Department dpto = new X_XX_VMR_Department(getCtx(), department, null);
								
								storeDistri.setC_Period_ID(period);
								storeDistri.setXX_VMR_Category_ID(dpto.getXX_VMR_Category_ID());
								storeDistri.setXX_VMR_Department_ID(department);
								storeDistri.setXX_VMR_Line_ID(line);
								storeDistri.setXX_VMR_Section_ID(section);
								storeDistri.setM_Warehouse_ID(store);
								storeDistri.setXX_InitialInventory(initialInventory);
								storeDistri.setXX_SalesDistribution(sales);
								storeDistri.setXX_Discount(discount);
								storeDistri.setXX_Decrease(decrease);
								storeDistri.setXX_EMPLOYEDISCOUNT(employeeDiscount);
								storeDistri.setXX_Purchases(purchase);
								storeDistri.setXX_BudgetedFinalInventory(BudgetedFinalInventory);
								//storeDistri.setXX_VMR_PURCHASEPLAN_ID(purchasePlanid);
								storeDistri.setXX_InitialInventoryPieces(initialInventoryPieces);
								storeDistri.setXX_DecreasePieces(decreasePieces);
								storeDistri.setXX_SalesDistributionPiece(salesPieces);
								storeDistri.setXX_PurchasesPieces(purchasePieces);
								storeDistri.setXX_BudFinalInventoryPiece(BudgetedFinalInventoryPieces);
								storeDistri.save();
								commit();
								
							}
							rs6.close();
							pstmt6.close();
						}
						catch (Exception e) {
							System.out.println("Error " + e);
						}
					}

				}// end while
				rs2.next();
				pstmt2.close();
				
			}// end while
			rs.next();
			pstmt.close();
			
			// llamo la funcion que me va sumar las piezas por linea y por departamento
			sumaPiezasLinea();
			commit();
			//sumaPiezaDpto();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Proceso Realizado";
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void sumaPiezasLinea()
	{
		String SQL = ("SELECT XX_VMR_AssortmentPlan_ID AS PLANID, C_PERIOD_ID AS PERIOD ,XX_VMR_LINE_ID AS LINE FROM XX_VMR_AssortmentPlan ");
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				X_XX_VMR_AssortmentPlan assortmentPlan = new X_XX_VMR_AssortmentPlan(getCtx(), rs.getInt("PLANID"), get_TrxName());
				
				String SQL1 = ("SELECT SUM(XX_INITIALINVENTORYPIECES) AS INVPIECES, SUM(XX_DECREASEPIECES) AS DEPIECES, " +
						"SUM(XX_PURCHASESPIECES) AS PURPIECES, SUM(XX_BUDFINALINVENTORYPIECE) AS BUDPIECES, SUM(XX_SALESDISTRIBUTIONPIECE) AS SALESPIECES " +
						"FROM XX_VMR_PURCHASEPLAN " +
						"WHERE XX_VMR_LINE_ID = '"+rs.getInt("LINE")+"' " +
						"AND C_PERIOD_ID = '"+rs.getInt("PERIOD")+"' ");
				
				System.out.println(SQL1);
								
				PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null);
				ResultSet rs1 = pstmt1.executeQuery();
				
				if(rs1.next())
				{
					//System.out.println(rs1.getInt("INVPIECES") + " " + rs1.getInt("DEPIECES")+ " " + rs1.getInt("PURPIECES") + " " +rs1.getInt("BUDPIECES"));
					assortmentPlan.setXX_InitialInventoryPieces(rs1.getInt("INVPIECES"));
					assortmentPlan.setXX_SalesDistributionPiece(rs1.getInt("SALESPIECES"));
					assortmentPlan.setXX_DecreasePieces(rs1.getInt("DEPIECES"));
					assortmentPlan.setXX_PurchasesPieces(rs1.getInt("PURPIECES"));
					assortmentPlan.setXX_BudFinalInventoryPiece(rs1.getInt("BUDPIECES"));
					assortmentPlan.save();
				}
				rs1.close();
				pstmt1.close();
				
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void sumaPiezaDpto()
	{
		Integer invPieces = 0;
		Integer salePieces = 0;
		Integer depPieces = 0;
		Integer purPieces = 0;
		Integer budPieces = 0;
		
		
		String SQL = ("SELECT XX_VMR_ComercialBudgetTab_ID AS COMER, C_PERIOD_ID AS PERIOD, XX_VMR_DEPARTMENT_ID AS DPTO FROM XX_VMR_ComercialBudgetTab ");
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				X_XX_VMR_ComercialBudgetTab comercialBudget = new X_XX_VMR_ComercialBudgetTab(getCtx(), rs.getInt("COMER"), get_TrxName());
				
				String SQL1 = ("SELECT SUM(XX_INITIALINVENTORYPIECES) AS INVPIECES, SUM(XX_DECREASEPIECES) AS DEPIECES, " +
						"SUM(XX_PURCHASESPIECES) AS PURPIECES, SUM(XX_BUDFINALINVENTORYPIECE) AS BUDPIECES, SUM(XX_SALESDISTRIBUTIONPIECE) AS SALESPIECES " +
						"FROM XX_VMR_AssortmentPlan " +
						"WHERE XX_VMR_DEPARTMENT_ID = '"+rs.getInt("DPTO")+"' " +
						"AND C_PERIOD_ID = '"+rs.getInt("PERIOD")+"' ");
				
				PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null);
				ResultSet rs1 = pstmt1.executeQuery();
				//System.out.println(SQL1);
				if(rs1.next())
				{
					invPieces = rs1.getInt("INVPIECES");
					salePieces = rs1.getInt("SALESPIECES");
					depPieces = rs1.getInt("DEPIECES");
					purPieces = rs1.getInt("PURPIECES");
					budPieces = rs1.getInt("BUDPIECES");
					
					if(invPieces == null)
						invPieces = 0;
					if(salePieces == null)
						salePieces = 0;
					if(depPieces == null)
						depPieces = 0;
					if(purPieces == null)
						purPieces = 0;
					if(budPieces == null)
						budPieces = 0;
					
					//System.out.println(invPieces+ " " +depPieces+ " " +purPieces+ " " +budPieces);
					comercialBudget.setXX_InitialInventoryPieces(invPieces);
					comercialBudget.setXX_SalesDistributionPiece(salePieces);
					comercialBudget.setXX_DecreasePieces(depPieces);
					comercialBudget.setXX_PurchasesPieces(purPieces);
					comercialBudget.setXX_BudFinalInventoryPiece(budPieces);
					comercialBudget.save();
				}
				rs1.close();
				pstmt1.close();
				
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
