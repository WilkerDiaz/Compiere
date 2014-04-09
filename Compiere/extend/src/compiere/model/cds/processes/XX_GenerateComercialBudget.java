package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MPeriod;
import compiere.model.cds.MVMRBudgetSalesDepart;
import compiere.model.cds.MVMRComercialBudgetTab;
import compiere.model.cds.MVMRRealComercialBudget;

public class XX_GenerateComercialBudget extends SvrProcess{
	
	@Override
	protected String doIt() throws Exception {
		
		//System.out.println("paso por aca");
		
		// En primer lugar borramos las otras tablas creadas en el ultimo presupuesto		
		borrarTablas();
						
		String sql = "select XX_VMR_BudgetSalesDepart_ID from XX_VMR_BudgetSalesDepart where isactive='Y'";
		
		String sql2 = "";
		
		int BudgetSalesDepart_ID = 0;		
			
		PreparedStatement prst = DB.prepareStatement(sql,null);
		// Por cada dep y periodo del escenario inicial
		try {
			ResultSet rs = prst.executeQuery();
			while (rs.next())
			{
				BudgetSalesDepart_ID = rs.getInt(1);
				
				MVMRBudgetSalesDepart salesDepart = new MVMRBudgetSalesDepart(getCtx(),rs.getInt(1),get_TrxName());				
				MVMRRealComercialBudget InitialScenario = new MVMRRealComercialBudget(getCtx(),salesDepart.getXX_VMR_RealComercialBudget_ID(),get_TrxName());
				// Llenamos los campos basicos del presupuesto
				MVMRComercialBudgetTab newaux = new MVMRComercialBudgetTab(getCtx(), 0, get_TrxName());
				newaux.setAD_Org_ID(0);
				newaux.setAD_Client_ID(salesDepart.getAD_Client_ID());
				newaux.setIsActive(true);
				newaux.setXX_VMR_BudgetSalesDepart_ID(BudgetSalesDepart_ID);
				newaux.setXX_VMR_Department_ID(salesDepart.getXX_VMR_Department_ID());
				newaux.setC_Period_ID(InitialScenario.getC_Period_ID());

			///////////////////////////////////////////////////////	
			////// Calculo de las ventas //////////////////////////
			///////////////////////////////////////////////////////
			// Formula: Se lo trae de la tabla previa
				
			newaux.setXX_SalesDistribution(salesDepart.getXX_SalesDistribution());
				
				
			///////////////////////////////////////////////////////	
			////// Calculo del Inventario Inicial Presupuestado////
			///////////////////////////////////////////////////////
			// Formula: Inventario Inicial = Inventario Final Presupuestado Mes Anterior	
				// Busqueda de añomespre del mes anterior
				
				MPeriod p = new MPeriod(getCtx(),InitialScenario.getC_Period_ID(),get_TrxName());
	
				//System.out.println("con periodo " + p.getName());				
				
				int mes = mesDeStringAEntero(p.getName())-1;

				String j = Integer.toString(mes);
				if ((mes)<10 & mes>1)
					j =  "0" + j; 
									
				int anio = Integer.parseInt(p.getName().substring(((p.getName().length())-4)));
				if (mes==0)
				{
					j = "12";
					anio = anio-1;
				}
				j = Integer.toString(anio) + j;
					
				PreparedStatement prst2;
					
				// Si mes es seis, quiere decir que estoy buscando el presupuesto de julio (porque le reste uno antes)
				// en este caso busco el inventario inicial de la prld01
				//System.out.println("mes " + mes);
				//System.out.println("j vale " + j);
				if (mes == 6)
				{
					
					sql2 = "select coalesce(sum(XX_FINALINVAMOUNTBUD2),0) from xx_vmr_prld01 where XX_BUDGETYEARMONTH='"+j+"' and XX_VMR_DEPARTMENT_ID=" + salesDepart.getXX_VMR_Department_ID() + " AND XX_CODCAT<>99 and XX_CODDEP<>99 and XX_CODLIN<>99 and XX_CODSEC<>99 and XX_CODTIE<>99";			

					prst2 = DB.prepareStatement(sql2,null);
					
					try {
						ResultSet rs2 = prst2.executeQuery();
						if (rs2.next())
						{
							
							newaux.setXX_InitialInventory(rs2.getBigDecimal(1));
			
						} 
						rs2.close();
						prst2.close();
					} catch (SQLException e){
						System.out.println("Error " + e);	
						System.out.println(sql2);
					}	
					
				} else // Si el mes no es julio, en vez de buscarlo de la prld01 lo busco en el propio comercial budget y es el final del mes anterior
				{
					// debo buscar en la comercialbudgettab el finalinventory del que tenga el mismo departamento en el que estoy, pero
					// el period con el mes anterior
					
					
					// Para ello debo conseguir el periodo de las ventas del mes siguiente

					Integer periodoMesAnterior = 0;
					
					sql2 = "(select C_Period_ID from C_Period where periodNo=(select periodNo from C_Period where C_Period_ID=("+InitialScenario.getC_Period_ID()+") and C_Year_ID="+InitialScenario.getC_Year_ID()+")-1 and C_Year_ID="+InitialScenario.getC_Year_ID()+")";
					
					prst2 = DB.prepareStatement(sql2,null);
					
					try {
						ResultSet rs2 = prst2.executeQuery();
						if (rs2.next())
						{
							periodoMesAnterior = rs2.getInt(1);
						} 
						rs2.close();
						prst2.close();
					} catch (SQLException e){
						System.out.println("Error " + e);			
						System.out.println(sql2);
					}	
					// Si el periodo da vacio, quiere decir que el mes actual era de numero 12, por ello al buscar el 13 no lo consiguio
					// lo que debemos hacer es buscar el periodo uno pero del año siguiente al actual
/**					
					if (periodoMesSiguiente==0)
					{
						sql2 = "select C_Period_ID from C_Period where periodNo=1 and C_Year_ID=(select min(C_Year_ID) from C_Period where C_Year_ID>"+InitialScenario.getC_Year_ID()+")";
						
						prst2 = DB.prepareStatement(sql2,null);
						
						try {
							ResultSet rs2 = prst2.executeQuery();
							if (rs2.next())
							{
								periodoMesSiguiente = rs2.getInt(1);
							} 
							rs2.close();
							prst2.close();
						} catch (SQLException e){
							System.out.println("Error " + e);			
							System.out.println(sql2);
						}	
					}
*/	
					
					sql2 = "select xx_budgetedfinalinventory from xx_vmr_comercialbudgettab where xx_vmr_department_id=" + salesDepart.getXX_VMR_Department_ID() + " and c_period_id=" + periodoMesAnterior;

					BigDecimal inventarioFinal = new BigDecimal(0);
					prst2 = DB.prepareStatement(sql2,null);
					
					try {
						ResultSet rs2 = prst2.executeQuery();
						if (rs2.next())
						{
							inventarioFinal = rs2.getBigDecimal(1);
						} 
						rs2.close();
						prst2.close();
					} catch (SQLException e){
						System.out.println("Error " + e);			
						System.out.println(sql2);
					}	
					
					newaux.setXX_InitialInventory(inventarioFinal);

				}

				///////////////////////////////////////////////////////	
				////// Calculo de rebajas mes en curso ////////////////
				///////////////////////////////////////////////////////				
				// Formula: Rebajas Mes en Curso = Ventas Presupuestadas Dpto Bs * (% de rebajas presupuestadas del departamento/100)
				BigDecimal ventasPresupestadasDptoBs = new BigDecimal(0);
				try {
					ventasPresupestadasDptoBs = salesDepart.getXX_SalesDistribution().divide(salesDepart.getXX_DepartmentHistoricWeight(), 2, RoundingMode.HALF_UP);
				} catch(ArithmeticException e){
					System.out.println("Se produjo una division por cero al calcular ventasPresupestadasDptoBs ya que el peso historico del departamento es cero");
				}
				// Falta calcular el procentaje de rebajas presupuestadas

				BigDecimal procentajeRebajasPresupuestadas = new BigDecimal(0); 
				
				sql2 = "select XX_PECTSALEPROMBUD,XX_PORTSALEFRBUD,XX_PERCENTSQALEFINALBUD from xx_vmr_prld01 where XX_VMR_Department_ID="+salesDepart.getXX_VMR_Department_ID()+" and XX_BUDGETYEARMONTH='"+(Integer.parseInt(j)-100)+"'";
				prst2 = DB.prepareStatement(sql2,null);
				
				try {
					ResultSet rs2 = prst2.executeQuery();
					if (rs2.next())
					{
						procentajeRebajasPresupuestadas = rs2.getBigDecimal(1).add(rs2.getBigDecimal(2)).add(rs2.getBigDecimal(3));
					} 
					rs2.close();
					prst2.close();
				} catch (SQLException e){
					System.out.println("Error " + e);		
					System.out.println(sql2);
				}	

				newaux.setXX_Rebate1((ventasPresupestadasDptoBs.multiply(procentajeRebajasPresupuestadas.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP))).setScale (2,BigDecimal.ROUND_HALF_UP));				

				///////////////////////////////////////////////////////	
				////// Calculo de merma mes en curso //////////////////
				///////////////////////////////////////////////////////
				// Merma mes en curso = %merma por departamento * ventas presupuestadas
				
				// Primero buscamos el % de merma por dep
				BigDecimal mermaGlobal = new BigDecimal(0);
				BigDecimal mermaDep = new BigDecimal(0);
				
				sql2 = "select XX_Decrease from xx_vmr_comercialbudget";
				
				prst2 = DB.prepareStatement(sql2,null);
				
				try {
					ResultSet rs2 = prst2.executeQuery();
					while (rs2.next())
					{
						mermaGlobal = rs2.getBigDecimal(1);
					} 
					rs2.close();
					prst2.close();
				} catch (SQLException e){
					System.out.println("Error " + e);	
					System.out.println(sql2);
				}		
				
				sql2 = "select XX_Decrease from XX_VMR_DepartmentBudget where XX_VMR_Department_ID=" + newaux.getXX_VMR_Department_ID();
				
				prst2 = DB.prepareStatement(sql2,null);
				
				try {
					ResultSet rs2 = prst2.executeQuery();
					while (rs2.next())
					{
						mermaDep = rs2.getBigDecimal(1);
					} 
					rs2.close();
					prst2.close();
				} catch (SQLException e){
					System.out.println("Error " + e);	
					System.out.println(sql2);
				}	
				if (mermaDep.equals(new BigDecimal(0)))
					newaux.setXX_Decrease((mermaGlobal.multiply(ventasPresupestadasDptoBs)).setScale (2,BigDecimal.ROUND_HALF_UP));			
				else 
					newaux.setXX_Decrease((mermaDep.multiply(ventasPresupestadasDptoBs)).setScale (2,BigDecimal.ROUND_HALF_UP));	
				
				///////////////////////////////////////////////////////	
				////// Calculo del Descuento de empleado //////////////
				///////////////////////////////////////////////////////
				// Formula: Descuento de empleado = %Descuento empleado * ventas presupuestadas
				
				
				// Debo buscar el porcentaje de decuento empleado. Esto se encuentra en la ventana initial parameters tabla xx_vmr_comercialbudget
				
				BigDecimal porcentajeDescuentoEmpleado = new BigDecimal(0);
				
				sql2 = "select xx_historicemployeediscount from xx_vmr_comercialbudget";
				
				prst2 = DB.prepareStatement(sql2,null);
				
				try {
					ResultSet rs2 = prst2.executeQuery();
					if (rs2.next())
					{
						porcentajeDescuentoEmpleado = rs2.getBigDecimal(1);
					} 
					rs2.close();
					prst2.close();
				} catch (SQLException e){
					System.out.println("Error " + e);	
					System.out.println(sql2);
				}		
				
				newaux.setXX_Discount1((porcentajeDescuentoEmpleado.multiply(ventasPresupestadasDptoBs)).setScale (2,BigDecimal.ROUND_HALF_UP));
				
				
				///////////////////////////////////////////////////////	
				////// Calculo de Compras mes en curso ////////////////
				///////////////////////////////////////////////////////
				// Formula: Compras mes en curso = Ventas mes siguiente + Rebaja mes en curso (cambiado a mes siguiente) + descuento empleado mes en curso (cambiado a mes siguiente) + merma mes en curso (cambiado a mes siguiente)

				// primero debo buscar las ventas mes siguiente

				// Para ello debo conseguir el periodo de las ventas del mes siguiente

				Integer periodoMesSiguiente = 0;
				
				sql2 = "(select C_Period_ID from C_Period where periodNo=(select periodNo from C_Period where C_Period_ID=("+InitialScenario.getC_Period_ID()+") and C_Year_ID="+InitialScenario.getC_Year_ID()+")+1 and C_Year_ID="+InitialScenario.getC_Year_ID()+")";
				
				prst2 = DB.prepareStatement(sql2,null);
				
				try {
					ResultSet rs2 = prst2.executeQuery();
					if (rs2.next())
					{
						periodoMesSiguiente = rs2.getInt(1);
					} 
					rs2.close();
					prst2.close();
				} catch (SQLException e){
					System.out.println("Error " + e);			
					System.out.println(sql2);
				}	
				// Si el periodo da vacio, quiere decir que el mes actual era de numero 12, por ello al buscar el 13 no lo consiguio
				// lo que debemos hacer es buscar el periodo uno pero del año siguiente al actual
				
				if (periodoMesSiguiente==0)
				{
					sql2 = "select C_Period_ID from C_Period where periodNo=1 and C_Year_ID=(select min(C_Year_ID) from C_Period where C_Year_ID>"+InitialScenario.getC_Year_ID()+")";
					
					prst2 = DB.prepareStatement(sql2,null);
					
					try {
						ResultSet rs2 = prst2.executeQuery();
						if (rs2.next())
						{
							periodoMesSiguiente = rs2.getInt(1);
						} 
						rs2.close();
						prst2.close();
					} catch (SQLException e){
						System.out.println("Error " + e);			
						System.out.println(sql2);
					}	
				}
				
				// ahora si, teniendo el periodo, puedo buscar sus ventas.
				
				
				sql2 = "select XX_SalesDistribution from XX_VMR_BudgetSalesDepart where XX_VMR_RealComercialBudget_ID=(select XX_VMR_RealComercialBudget_ID from XX_VMR_RealComercialBudget where C_Period_ID="+periodoMesSiguiente+" and XX_VMR_Department_ID="+salesDepart.getXX_VMR_Department_ID()+")";
				
				BigDecimal ventasMesSiguiente = new BigDecimal(0);
				prst2 = DB.prepareStatement(sql2,null);
				ResultSet rs2 = null;
				try {
					rs2 = prst2.executeQuery();
					if (rs2.next())
					{
						try {
							ventasMesSiguiente = rs2.getBigDecimal(1).divide(salesDepart.getXX_DepartmentHistoricWeight(), 2, RoundingMode.HALF_UP);
						} catch(ArithmeticException e){
							System.out.println("Se produjo una division por cero al calcular ventasMesSiguiente ya que el peso historico del departamento es cero");
						}						

					} 

				} catch (SQLException e){
					System.out.println("Error " + e);			
					System.out.println(sql2);
				}finally{
					
					try {rs2.close();} catch (SQLException e1){e1.printStackTrace();}
					try {prst2.close();} catch (SQLException e) {e.printStackTrace();}
				}	
				
				
				newaux.setXX_Purchases((ventasMesSiguiente.add(newaux.getXX_Rebate1().add(newaux.getXX_Discount1()).add(newaux.getXX_Decrease()))).setScale (2,BigDecimal.ROUND_HALF_UP));
				
				///////////////////////////////////////////////////////	
				////// Calculo de Inventario Final Presupuestado ///////
				///////////////////////////////////////////////////////
				// Formula: Inventario final presupuestado = Inventario inicial mes en curso + compras mes en curso - Ventas mes en curso - Rebaja mes en curso
				// continuacion:                             - merma mes en curso - descuento de empleado en curso
				// Nota: Si el inventario final da menor a la carga minima por departamento (esta en los parametros iniciales), se debera
				// sumar esta diferencia a las compras de mes en curso
				
				newaux.setXX_BudgetedFinalInventory((newaux.getXX_InitialInventory().add(newaux.getXX_Purchases()).subtract(newaux.getXX_Rebate1()).subtract(newaux.getXX_Decrease()).subtract(newaux.getXX_Discount1()).subtract(ventasPresupestadasDptoBs)).setScale (2,BigDecimal.ROUND_HALF_UP));

				
				///////////////////////////////////////////////////////	
				////// Calculo de la Cobertura ///////
				///////////////////////////////////////////////////////
				// Formula: Cobertura = Inventario Inicial / Ventas
				try {
					newaux.setXX_Cobert(newaux.getXX_InitialInventory().divide(salesDepart.getXX_SalesDistribution(), 2, RoundingMode.HALF_UP));
				} catch(ArithmeticException e){
					System.out.println("Se produjo una division por cero al calcular la cobertura ya que las ventas del departamento es cero");
				}						

				///////////////////////////////////////////////////////	
				////// Calculo de la Rotacion  //////////////// ///////
				///////////////////////////////////////////////////////
				// Formula: Cobertura = Inventario Inicial / Ventas
				try {
					newaux.setXX_Rotation((salesDepart.getXX_SalesDistribution().multiply(new BigDecimal(24))).divide(newaux.getXX_BudgetedFinalInventory().add(newaux.getXX_InitialInventory()), 2, RoundingMode.HALF_UP));
				} catch(ArithmeticException e){
					System.out.println("Se produjo una division por cero al calcular la rotacion ya que el inventario final es cero");
				}		

				newaux.save();	
				
				commit();

			} 	
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error: " + e);	
			System.out.println(sql);
		}	

		/**
		String sql = "select "
		
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next())
			{
				rs.getInt(1);
			} else
			{
				rs.close();
				prst.close();
				ADialog.error(1, new Container(), Msg.getMsg(getCtx(), "XX_PeriodNotCreated"));
				return "";
			}		
	} catch (SQLException e){
		System.out.println("Es posible que existan mas de dos periodos con la misma fecha de inicio y fin " + e);			
	}	
		*/
		
		return "";
	}
	
	private void borrarTablas() {

		// Borro la tabla de distribución de tienda
		String SQL6 = ("DELETE FROM  XX_VMR_StoreDistri ");
		DB.executeUpdate(null, SQL6);
		
		// Borro la tabla de Plan de Compra
		String SQL7 = ("DELETE FROM  XX_VMR_PurchasePlan ");
		DB.executeUpdate(null, SQL7);
		
		// Borro la tabla de Plan de Surtido
		String SQL8 = ("DELETE FROM  XX_VMR_AssortmentPlan ");
		DB.executeUpdate(null, SQL8);
		
		
		String sql = "DELETE FROM XX_VMR_ComercialBudgetTab";						
		DB.executeUpdate(null, sql);
		
	}


	
	public int mesDeCalendarANormal(int mesCalendar)
	{
		int mesTradicional=0;
		
		if (mesCalendar==1)
			mesTradicional = 7;
		if (mesCalendar==2)
			mesTradicional = 8;
		if (mesCalendar==3)
			mesTradicional = 9;
		if (mesCalendar==4)
			mesTradicional = 10;
		if (mesCalendar==5)
			mesTradicional = 11;
		if (mesCalendar==6)
			mesTradicional = 12;
		if (mesCalendar==7)
			mesTradicional = 1;
		if (mesCalendar==8)
			mesTradicional = 2;
		if (mesCalendar==9)
			mesTradicional = 3;
		if (mesCalendar==10)
			mesTradicional = 4;
		if (mesCalendar==11)
			mesTradicional = 5;
		if (mesCalendar==12)
			mesTradicional = 6;
		
		return mesTradicional;
	}
	
	public int mesDeStringAEntero(String mesString)
	{
		
		int i=0;
		mesString = mesString.toUpperCase();
		
		if (mesString.indexOf("ENERO") == 0)
			i=1;
		if (mesString.indexOf("FEBRERO") == 0)
			i=2;
		if (mesString.indexOf("MARZO") == 0)
			i=3;
		if (mesString.indexOf("ABRIL") == 0)
			i=4;
		if (mesString.indexOf("MAYO") == 0)
			i=5;
		if (mesString.indexOf("JUNIO") == 0)
			i=6;
		if (mesString.indexOf("JULIO") == 0)
			i=7;
		if (mesString.indexOf("AGOSTO") == 0)
			i=8;
		if (mesString.indexOf("SEPTIEMBRE") == 0)
			i=9;
		if (mesString.indexOf("OCTUBRE") == 0)
			i=10;
		if (mesString.indexOf("NOVIEMBRE") == 0)
			i=11;
		if (mesString.indexOf("DICIEMBRE") == 0)
			i=12;

		return i;
	}
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}

}
