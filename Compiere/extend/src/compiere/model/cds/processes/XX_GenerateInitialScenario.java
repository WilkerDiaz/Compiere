package compiere.model.cds.processes;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;


import org.compiere.process.SvrProcess;
import org.compiere.util.DB;


import compiere.model.cds.MVMRComercialBudget;
import compiere.model.cds.MVMRRealComercialBudget;
import compiere.model.cds.MVMR_BudgetSalesDepart;


public class XX_GenerateInitialScenario extends SvrProcess {
	
	@Override
	protected String doIt() throws Exception {
		
		String sql;
		
		// En primer lugar borramos las otras tablas creadas
		
		
		// Borro la tabla de distribución de tienda
		String SQL6 = ("DELETE FROM  XX_VMR_StoreDistri ");
		DB.executeUpdate(null, SQL6);
		
		// Borro la tabla de Plan de Compra
		String SQL7 = ("DELETE FROM  XX_VMR_PurchasePlan ");
		DB.executeUpdate(null, SQL7);
		
		// Borro la tabla de Plan de Surtido
		String SQL8 = ("DELETE FROM  XX_VMR_AssortmentPlan ");
		DB.executeUpdate(null, SQL8);
		
		
		sql = "DELETE FROM XX_VMR_ComercialBudgetTab";						
		DB.executeUpdate(null, sql);

		sql = "DELETE FROM XX_VMR_BudgetSalesDepart";						//////////////////////
		DB.executeUpdate(null, sql);
		
		sql = "DELETE FROM XX_VMR_RealComercialBudget";						//////////////////////
		DB.executeUpdate(null, sql);

	
		
		// aux contiene el comercial budget sobre el cual estoy parado
		MVMRComercialBudget aux = new MVMRComercialBudget(getCtx(),getRecord_ID(),get_TrxName());
		
		// Primero debemos verificar que todos los departamentos activos tengan datos
		
		String sql2 = null;

		PreparedStatement prst = null;
		PreparedStatement prst2 = null;
		// el codigo de abajo al final debe ser descomentado. Esta hecho asi por los momentos por eficiencia al probar
/**
		try {
			ResultSet rs = prst.executeQuery();
			ResultSet rs2 = prst2.executeQuery();
			if (rs.next() && rs2.next())
			{
				if ((rs.getInt(1))!=(rs2.getInt(1)))
				{
					ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_DepartmentsMissing"));
					rs.close();
					prst.close();
					rs2.close();
					prst2.close();
					return "";
				}	
			}	

			rs.close();
			prst.close();
			
			rs2.close();
			prst2.close();			
			
		} catch (SQLException e){
			System.out.println("Error al buscar los departamentos en el comercial budget " + e);
			e.printStackTrace();
		}	
*/		
		// guardo en anio nuevo el año de inicio del periodo anterior
		Calendar fecha = Calendar.getInstance();
		
		int mesActual = fecha.get(Calendar.MONTH) + 1; 
		int anioActual = fecha.get(Calendar.YEAR);

/**		
// Esta parte verifica que el periodo del presupuesto comercial exista		
		DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" );
		Timestamp tsActual = new Timestamp( df.parse(diaActual+"/"+mesActual+"/"+anioActual ).getTime() );
		
		sql1 = "select fiscalyear from c_year where c_year_id=(SELECT C_YEAR_ID"
			+ " FROM C_PERIOD"
			+ " WHERE STARTDATE<"+tsActual+" AND ENDDATE>"+tsActual+")";
		System.out.println(sql1);
		prst = DB.prepareStatement(sql1,null);


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
				rs.close();
					prst.close();
		} catch (SQLException e){
			System.out.println("Es posible que existan mas de dos periodos con la misma fecha de inicio y fin " + e);			
		}	
							
*/		
		int anioNuevo = 0;
		
		if (mesActual < 7)
			anioNuevo = anioActual-1;
		else
			anioNuevo = anioActual;
		
		String mesActualStr = "";
		if (mesActual<10)
			mesActualStr = "0" + mesActual;
		else 
			mesActualStr = Integer.toString(mesActual);
		
		BigDecimal ventasPresupuestadasBsTotales = new BigDecimal(0);

		sql = "";
		
		double tasaInflacionMensual = Math.pow(((aux.getXX_AnnualInflation().divide(new BigDecimal(100), 0, RoundingMode.HALF_UP)).add(new BigDecimal(1))).doubleValue(),0.0833);

		System.out.println("tasa de inflacion mensual formula: Math.pow(((aux.getXX_AnnualInflation().divide(new BigDecimal(100), 0, RoundingMode.HALF_UP)).add(new BigDecimal(1))).doubleValue(),0.0833)" );
		System.out.println("tasa de inflacion mensual valor: " +  tasaInflacionMensual);
		for (int i=1;i<13;i++)
		{
			//ahora tengo en i el mes sobre el que me toca trabajar y procedo a hacer los calculos para cada campo
			
			// En primer lugar debo crear la instancia real comercial budget
			MVMRRealComercialBudget newaux = new MVMRRealComercialBudget(getCtx(), 0, get_TrxName());
			
			// ahora procedo a setear los parametros del real comercial budget
			// primero los basicos que no requieren calculos
			newaux.setAD_Org_ID(0);
			newaux.setAD_Client_ID(aux.getAD_Client_ID());
			newaux.setIsActive(true);
			newaux.setC_Year_ID(aux.getC_Year_ID());
			newaux.setC_Period_ID(getPeriodID(i, aux.getC_Year_ID()));
			
			int ventasBasicosPiezas = 0;
			int ventasTendenciaPiezas = 0;		
			int ventasFallaBasicosPiezas = 0;
			BigDecimal ventasBs;
			int ventasPiezas = 0;
			int ventasBasicosPiezasRebajados;
			BigDecimal ventasBasicosBsRebajados;

			// primero de acuerdo al mes en el que estoy parado voy a ver si me corresponde buscar las
			// ventas reales o presupuestadas de los basicos
			
			String j = Integer.toString(i);
			if (i<10)
				j =  "0" + j;
			if (i<7)
				j = Integer.toString(anioNuevo+1) + j;
			else
				j = Integer.toString(anioNuevo) + j;
			
			System.out.println("periodo " + j);

			if (Integer.parseInt((Integer.toString(anioActual)+mesActualStr)) > Integer.parseInt(j))   // si mes actual es mayor o igual al mes a tratar uso las reales
			{	System.out.println("busca reales");
				if (i<7)
				{
					System.out.println("con mes menor que 7");
					System.out.print("ventasBasicosPiezas ");
					ventasBasicosPiezas = ventasRealesBasicosPiezas(i,anioNuevo+1);
					System.out.println(ventasBasicosPiezas);
					System.out.print("ventasTendenciaPiezas ");
					ventasTendenciaPiezas = ventasRealesTendenciaPiezas(i,anioNuevo+1);	
					System.out.println(ventasTendenciaPiezas);
					System.out.print("ventasBs ");
					ventasBs = ventasRealesBs(i, anioNuevo+1);
					System.out.println(ventasBs);
					System.out.print("ventasPiezas ");
					ventasPiezas = ventasRealesPiezas(i,anioNuevo+1);
					System.out.println(ventasPiezas);
					
				} else
				{
					System.out.println("con mes mayor que 7");
					System.out.print("ventasBasicosPiezas ");
					ventasBasicosPiezas = ventasRealesBasicosPiezas(i,anioNuevo);
					System.out.println(ventasBasicosPiezas);
					System.out.print("ventasTendenciaPiezas ");
					ventasTendenciaPiezas = ventasRealesTendenciaPiezas(i,anioNuevo);
					System.out.println(ventasTendenciaPiezas);
					System.out.print("ventasBs ");
					ventasBs = ventasRealesBs(i, anioNuevo);
					System.out.println(ventasBs);
					System.out.print("ventasPiezas ");
					ventasPiezas = ventasRealesPiezas(i,anioNuevo);
					System.out.println(ventasPiezas);
				}	
				System.out.print("ventasFallaBasicosPiezas ");
				ventasFallaBasicosPiezas = ventasRealesFallasBasicosPiezas(i, anioNuevo);	
				ventasBasicosPiezasRebajados = ventasRealesPiezasBasicosRebajados(i,anioNuevo);
				ventasBasicosBsRebajados =  ventasRealesBsBasicosRebajados(i,anioNuevo);
				System.out.println("ventasBasicosPiezasRebajados " + ventasBasicosPiezasRebajados);
				System.out.println("ventasBasicosPiezasRebajados " + ventasBasicosPiezasRebajados);

			} else // si debo buscar ventas presupuestadas en lugar de ventas reales
			{
				System.out.println("busca presupuestadas");
				if (i<7)
				{
					System.out.println("con mes menor que 7");
					System.out.print("ventasBasicosPiezas ");
					ventasBasicosPiezas = ventasPresupuestadasBasicoPiezas(i, anioNuevo+1);	
					System.out.println(ventasBasicosPiezas);
					System.out.print("ventasTendenciaPiezas ");
					ventasTendenciaPiezas = ventasPresupuestadasTendenciaPiezas(i, anioNuevo+1);
					System.out.println(ventasTendenciaPiezas);
					System.out.print("ventasBs ");
					ventasBs = ventasPresupuestadasBs(i, anioNuevo+1);
					System.out.println(ventasBs);
					System.out.print("ventasPiezas ");
					ventasPiezas = ventasPresupuestadasPiezas(i, anioNuevo+1);
					System.out.println(ventasPiezas);
				} else
				{
					System.out.println("con mes mayor que 7");
					System.out.print("ventasBasicosPiezas ");
					ventasBasicosPiezas = ventasPresupuestadasBasicoPiezas(i, anioNuevo);	
					System.out.println(ventasBasicosPiezas);
					System.out.print("ventasTendenciaPiezas ");
					ventasTendenciaPiezas = ventasPresupuestadasTendenciaPiezas(i, anioNuevo);
					System.out.println(ventasTendenciaPiezas);
					System.out.print("ventasBs ");
					ventasBs = ventasPresupuestadasBs(i, anioNuevo);
					System.out.println(ventasBs);
					System.out.print("ventasPiezas ");
					ventasPiezas = ventasPresupuestadasPiezas(i, anioNuevo);
					System.out.println(ventasPiezas);
				}
				System.out.print("ventasFallaBasicosPiezas ");
				ventasFallaBasicosPiezas = ventasPresupuestadasFallasBasicosPiezas(i, anioNuevo);
				System.out.println(ventasFallaBasicosPiezas);
				ventasBasicosPiezasRebajados = ventasRealesPiezasBasicosRebajados(i,anioNuevo-1);
				ventasBasicosBsRebajados =  ventasRealesBsBasicosRebajados(i,anioNuevo-1);
				System.out.println("ventasBasicosPiezasRebajados " + ventasBasicosPiezasRebajados);
				System.out.println("ventasBasicosPiezasRebajados " + ventasBasicosPiezasRebajados);
			}
			
			
			
			int ventasBasicosPiezasReexpresadas = ventasBasicosPiezas + ventasFallaBasicosPiezas;
			System.out.println("ventasBasicosPiezasReexpresadas " + ventasBasicosPiezasReexpresadas);

			
			int ventasNormalizadasPiezas = ventasBasicosPiezasReexpresadas - ventasBasicosPiezasRebajados + ventasTendenciaPiezas;
			System.out.println("ventasNormalizadasPiezas " + ventasNormalizadasPiezas);
			
			
			// Ahora unicamente falta el calculo de Ventas Presupuestadas en piezas
			// Ventas Presupuestadas en Piezas = Ventas en Piezas Normalizadas * (%Crecimiento en Ventas + 1)
			//         donde %Crecimiento es ventas es un campo en los parametros iniciales
			
			
			int ventasPresupuestadasPiezas = ventasNormalizadasPiezas*((aux.getXX_SalesGrowth().divide(new BigDecimal(100), 0, RoundingMode.HALF_UP).intValue())+1);
			System.out.println("ventasPresupuestadasPiezas " + ventasPresupuestadasPiezas);
			
			

			
			// PrecioPromedioOrigen(mes a comparar) = (ventas en Bs - ventas en basicos rebajados) / (ventas en piezas - ventas en piezas basicos rebajados)
			BigDecimal precioPromedioOrigen;
			if (ventasPiezas==0)
				precioPromedioOrigen = new BigDecimal(0);
			else 
				precioPromedioOrigen = (ventasBs.subtract(ventasBasicosBsRebajados)).divide(new BigDecimal(ventasPiezas-ventasBasicosPiezasRebajados), 2, RoundingMode.HALF_UP);
			
			
			// mesActual i
			int mesesDiferencia = 0;
			if ((mesActual>6 & i>6) | (mesActual<7 & i<7))
				mesesDiferencia = Math.abs(mesActual-i);
			else
				mesesDiferencia = 12-i+mesActual;
			
			BigDecimal PrecioPromedioCompañia = precioPromedioOrigen.multiply(new BigDecimal(Math.pow(tasaInflacionMensual, mesesDiferencia)));
			System.out.println("PrecioPromedioCompañia " + PrecioPromedioCompañia);
			
			ventasPresupuestadasBsTotales = ventasPresupuestadasBsTotales.add(precioPromedioOrigen.multiply(new BigDecimal(ventasPresupuestadasPiezas)));
			System.out.println("ventasPresupuestadasBsTotales " + ventasPresupuestadasBsTotales);
	
			newaux.setXX_BudgetSalesPieces(ventasPresupuestadasPiezas);
			newaux.setXX_BudgetSalesBs(precioPromedioOrigen.multiply(new BigDecimal(ventasPresupuestadasPiezas)));
			newaux.setXX_CompanyAveragePrice(PrecioPromedioCompañia);
					
			newaux.save();
			
		}
		
		commit();
		
		try {
			
			sql2 = "select R.XX_VMR_RealComercialBudget_ID, P.name from XX_VMR_RealComercialBudget R, C_Period P where R.C_Period_ID=P.C_Period_ID";
			prst2 = DB.prepareStatement(sql2,null);
			ResultSet rs2 = prst2.executeQuery();
			
			//System.out.println("antes de while rs2");
			while (rs2.next())
			{		
				//System.out.println("entra al while rs2");
			
				// ahora debo crear en el detalle cada uno de los departamentos
				sql = "SELECT xx_vmr_department_id"
					+ " FROM XX_VMR_DEPARTMENT"
					+ " WHERE ISACTIVE='Y' ORDER BY VALUE";
		
				prst = DB.prepareStatement(sql,null);
				BigDecimal ventasBsDepartamentoMes = new BigDecimal(0);
				BigDecimal ventasBsMes = new BigDecimal(0);
				BigDecimal pesoHistoricoDepMes = new BigDecimal(0);
		
				
				ResultSet rs = prst.executeQuery();
	
				while(rs.next())
				{
					//System.out.println("entra al while rs");

					MVMR_BudgetSalesDepart newauxdetail = new MVMR_BudgetSalesDepart(getCtx(),0,get_TrxName());
					newauxdetail.setAD_Org_ID(0);
					newauxdetail.setAD_Client_ID(aux.getAD_Client_ID());
					newauxdetail.setIsActive(true);
					newauxdetail.setXX_VMR_Department_ID(rs.getInt(1));
					newauxdetail.setXX_VMR_RealComercialBudget_ID(rs2.getInt(1));
					
					int i = mesDeStringAEntero(rs2.getString(2));
					
					String j = Integer.toString(i);
					if (i<10)
						j =  "0" + j;
					if (i<7)
						j = Integer.toString(anioNuevo+1) + j;
					else
						j = Integer.toString(anioNuevo) + j;
					
					
					
					// calculo de ventasDepartamentoMes
					if (Integer.parseInt((Integer.toString(anioActual)+mesActualStr)) > Integer.parseInt(j))   // si mes actual es mayor o igual al mes a tratar uso las reales
					{ // ventas reales
						if (i<7)
						{
							System.out.println("1");
							System.out.println(i + " " + anioNuevo+1);
							System.out.println("ventasBsMes " + ventasBsMes);
							ventasBsMes = ventasRealesBs(i, anioNuevo+1);
							System.out.println("ventasBsMes " + ventasBsMes);
							ventasBsDepartamentoMes = ventasRealesBsDepMes(i, anioNuevo+1, rs.getInt(1));
						} else 
						{
							ventasBsMes = ventasRealesBs(i, anioNuevo);
							ventasBsDepartamentoMes = ventasRealesBsDepMes(i, anioNuevo, rs.getInt(1));
						}
						
	
					} else
					{ // ventas presupuestadas
						if (i<7)
						{
							System.out.println("2");
							System.out.println(i + " " + anioNuevo+1);
							ventasBsMes = ventasPresupuestadasBs(i, anioNuevo+1);
							ventasBsDepartamentoMes = ventasPresupuestadasBsDepMes(i, anioNuevo+1, rs.getInt(1));
						} else 
						{
							ventasBsMes = ventasPresupuestadasBs(i, anioNuevo);
							ventasBsDepartamentoMes = ventasPresupuestadasBsDepMes(i, anioNuevo, rs.getInt(1));
						}
						
					}
					//System.out.println("3");   ////////////////////////////
					//System.out.println("ventasBsDepartamentoMes " + ventasBsDepartamentoMes);
					//System.out.println("ventasBsMes " + ventasBsMes);
					pesoHistoricoDepMes = ventasBsDepartamentoMes.divide(ventasBsMes, 2, RoundingMode.HALF_UP);
					//System.out.println("4");
					
					if (pesoHistoricoDepMes.compareTo(new BigDecimal(0.0))==0)
						pesoHistoricoDepMes = new BigDecimal(0.001);
					
					newauxdetail.setXX_DepartmentHistoricWeight(pesoHistoricoDepMes.setScale (2,BigDecimal.ROUND_HALF_UP));
					//System.out.println("getXX_DepartmentHistoricWeight() " + newauxdetail.getXX_DepartmentHistoricWeight());
					
					// en la proxima linea falta el calculo para el calculo del monto
					// por los momentos le voy a setear cero
					newauxdetail.setXX_SalesDistribution(ventasPresupuestadasBsTotales.multiply(pesoHistoricoDepMes).setScale (2,BigDecimal.ROUND_HALF_UP));

					newauxdetail.save();	

				}	
	
				rs.close();
				prst.close();
			}
			rs2.close();
			prst2.close();
	
			} catch (SQLException e){
				System.out.println("Error al buscar los departamentos en el comercial budget " + e);	
				e.printStackTrace();
			}	

		return "";
	
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
	
	public int getPeriodID(int mesNoFiscal, int yearID)
	{
		int period_ID = 0;
		String sql = "";
		if (mesNoFiscal==1)	
			sql = "select c_period_id from c_period where c_year_id=" + yearID + " and PeriodNo=" + 7;
		if (mesNoFiscal==2)	
			sql = "select c_period_id from c_period where c_year_id=" + yearID + " and PeriodNo=" + 8;
		if (mesNoFiscal==3)	
			sql = "select c_period_id from c_period where c_year_id=" + yearID + " and PeriodNo=" + 9;
		if (mesNoFiscal==4)	
			sql = "select c_period_id from c_period where c_year_id=" + yearID + " and PeriodNo=" + 10;
		if (mesNoFiscal==5)	
			sql = "select c_period_id from c_period where c_year_id=" + yearID + " and PeriodNo=" + 11;
		if (mesNoFiscal==6)	
			sql = "select c_period_id from c_period where c_year_id=" + yearID + " and PeriodNo=" + 12;
		if (mesNoFiscal==7)	
			sql = "select c_period_id from c_period where c_year_id=" + yearID + " and PeriodNo=" + 1;
		if (mesNoFiscal==8)	
			sql = "select c_period_id from c_period where c_year_id=" + yearID + " and PeriodNo=" + 2;
		if (mesNoFiscal==9)	
			sql = "select c_period_id from c_period where c_year_id=" + yearID + " and PeriodNo=" + 3;
		if (mesNoFiscal==10)	
			sql = "select c_period_id from c_period where c_year_id=" + yearID + " and PeriodNo=" + 4;
		if (mesNoFiscal==11)	
			sql = "select c_period_id from c_period where c_year_id=" + yearID + " and PeriodNo=" + 5;
		if (mesNoFiscal==12)	
			sql = "select c_period_id from c_period where c_year_id=" + yearID + " and PeriodNo=" + 6;		
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();

			if (rs.next())
			{
				period_ID = rs.getInt(1);
			}	

			rs.close();
			prst.close();
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventas reales basicos " + e);
			e.printStackTrace();
		}
		return period_ID;
	}

	// Ventas reales de productos basicos para el mes i y el anio anioNuevo	
	public int ventasRealesBasicosPiezas(int i, int anioNuevo)
	{
		String j = Integer.toString(i);
		if (i<10)
			j =  "0" + j;
		j = Integer.toString(anioNuevo) + j;
		int ventasBasicoPiezas = 0;       // xx_canvenrea
		String sql = "select coalesce(sum(XX_QUANTACTUALSALE),0) from xx_vmr_prld01 where XX_BUDGETYEARMONTH=" + j +
		"and xx_vmr_line_id in (select xx_vmr_line_id from xx_vmr_line where isactive='Y' and XX_VMR_TypeInventory_ID="+ getCtx().getContextAsInt("#XX_L_TYPEINVENTORYBASICO_ID") + " )";

		
/**		
		// esto me busca ventas reales tendencia
		String sql = "select coalesce(sum(qtyordered),0) from c_orderline where c_order_id in (select c_order_id from c_order where issotrx='Y' and dateordered= (TO_DATE( '" + i + "-" + (anioNuevo) + "', 'MM-YYYY' )))" +                      
		" and m_product_id in (select m_product_id from m_product where isactive='Y' and XX_VMR_TYPEINVENTORY_ID=" + getCtx().getContextAsInt("#XX_L_TYPEINVENTORYBASICO_ID") + " )";
*/		PreparedStatement prst = DB.prepareStatement(sql,null);

		try {
			ResultSet rs = prst.executeQuery();

			if (rs.next())
			{
				ventasBasicoPiezas = rs.getInt(1);
			}	

			rs.close();
			prst.close();
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventas reales basicos " + e);
			e.printStackTrace();
		}
				
		return ventasBasicoPiezas;
		
	}

	
	// Ventas reales de productos tendencia para el mes i y el anio anioNuevo	
	public int ventasRealesTendenciaPiezas(int i, int anioNuevo)
	{
		int ventasTendenciaPiezas = 0;
		
		String j = Integer.toString(i);
		if (i<10)
			j =  "0" + j;
		j = Integer.toString(anioNuevo) + j;

		String sql = "select coalesce(sum(XX_QUANTACTUALSALE),0) from xx_vmr_prld01 where XX_BUDGETYEARMONTH=" + j +
		"and xx_vmr_line_id in (select xx_vmr_line_id from xx_vmr_line where isactive='Y' and XX_VMR_TypeInventory_ID="+ getCtx().getContextAsInt("#XX_L_TYPEINVENTORYTENDENCIA_ID") + " )";

/**		
		// esto me busca ventas reales tendencia
		String sql = "select coalesce(sum(qtyordered),0) from c_orderline where c_order_id in (select c_order_id from c_order where issotrx='Y' and dateordered= (TO_DATE( '" + i + "-" + (anioNuevo) + "', 'MM-YYYY' )))" +                      
		" and m_product_id in (select m_product_id from m_product where isactive='Y' and XX_VMR_TYPEINVENTORY_ID=" + getCtx().getContextAsInt("#XX_L_TYPEINVENTORYTENDENCIA_ID ") + " )";
*/
		PreparedStatement prst = DB.prepareStatement(sql,null);

		try {
			ResultSet rs = prst.executeQuery();

			if (rs.next())
			{
				ventasTendenciaPiezas = rs.getInt(1);
			}	

			rs.close();
			prst.close();
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventas reales tendencia " + e);	
			e.printStackTrace();
		}
				
		return ventasTendenciaPiezas;
		
	}
	
	// Ventas presupuestadas de productos tendencia para el mes i y el anio anioNuevo	por piezas
	public int ventasPresupuestadasTendenciaPiezas(int i, int anioNuevo)
	{
		int ventasPresupuestadasTendencia = 0;
		
		String j = Integer.toString(i);
		if (i<10)
			j =  "0" + j;
		j = Integer.toString(anioNuevo) + j;
		String sql = "select coalesce(sum(XX_SALESAMOUNTBUD),0) from xx_vmr_prld01 where XX_BUDGETYEARMONTH=" + j +
					 "and xx_vmr_line_id in (select xx_vmr_line_id from xx_vmr_line where isactive='Y' and XX_VMR_TypeInventory_ID="+ getCtx().getContextAsInt("#XX_L_TYPEINVENTORYTENDENCIA_ID") + " )";

		PreparedStatement prst = DB.prepareStatement(sql, null);

		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next())
			{
				ventasPresupuestadasTendencia = rs.getInt(1);
			}	
			
			rs.close();
			prst.close();
			
		} catch (SQLException e){
			System.out.println("Error al buscar ventasPresupuestadasTendencia por piezas " + e);	
			e.printStackTrace();
		}	
		
		return ventasPresupuestadasTendencia;
		
	}
	
	// Ventas presupuestadas de productos basicos para el mes i y el anio anioNuevo por piezas	
	public int ventasPresupuestadasBasicoPiezas(int i, int anioNuevo)
	{
		int ventasPresupuestadasBasico = 0;
		
		String j = Integer.toString(i);
		if (i<10)
			j =  "0" + j;
		j = Integer.toString(anioNuevo) + j;
		
		String sql = "select coalesce(sum(XX_SALESAMOUNTBUD),0) from xx_vmr_prld01 where XX_BUDGETYEARMONTH=" + j +
		"and xx_vmr_line_id in (select xx_vmr_line_id from xx_vmr_line where isactive='Y' and XX_VMR_TypeInventory_ID="+ getCtx().getContextAsInt("#XX_L_TYPEINVENTORYBASICO_ID") + " )";

		PreparedStatement prst = DB.prepareStatement(sql, null);

		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next())
			{
				ventasPresupuestadasBasico =  rs.getInt(1);
			}	
			
			rs.close();
			prst.close();
			
		} catch (SQLException e){
			System.out.println("Error al buscar ventasPresupuestadasBasico por piezas " + e);	
			e.printStackTrace();
		}	

		return ventasPresupuestadasBasico;
		
	}
	
	

	// ventas en piezas de basicos falla para el caso en que si hay ventas reales
	public int ventasRealesFallasBasicosPiezas (int i, int anioNuevo)
	{
		int ventasFallaBasicosPiezas = 0;
		
		// primero busco todos los productos basicos en inventario
		String sql = "select m_product_id from m_product where isactive='Y' and xx_vmr_typeinventory_id=" + getCtx().getContextAsInt("#XX_L_TYPEINVENTORYBASICO_ID");
		String sql2 = "";
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		PreparedStatement prst2 = null;

		try {
			ResultSet rs = prst.executeQuery();
			while (rs.next())
			{			
				int dia = 1;	
				boolean fallo = false;
				while (dia<32 & !fallo)
				{
					ResultSet rs2 = null;
					try {
						
						sql2 = "select coalesce(sum(qtyOrdered),0) from c_orderline where m_product_id = " + rs.getInt(1) + " and" + 
						   " c_order_id in (select c_order_id from c_order where ISSOTRX='Y' and dateordered= (TO_DATE( '"+dia+"-" + i + "-" + anioNuevo + "', 'DD-MM-YYYY' )))"; 
	
						prst2 = DB.prepareStatement(sql2,null);
						rs2 = prst2.executeQuery();
						if (rs2.next())
						{
									if (rs2.getInt(1)>0)
										ventasFallaBasicosPiezas = ventasFallaBasicosPiezas + ((30 * rs2.getInt(1))/dia);
									else 
										fallo = true;
						}	
						rs2.close();
						prst2.close();
					} catch (Exception e)
					{
						
					} finally {
						DB.closeResultSet(rs2);
						DB.closeStatement(prst2);
					}
					
					dia++;
				}						
				
			}	

			rs.close();
			prst.close();
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventasFallasBasicosPiezas " + e);	
			e.printStackTrace();
		}
		
		return ventasFallaBasicosPiezas;
	}
	
	// ventas en piezas de basicos falla para el caso en que si hay ventas reales
	public int ventasPresupuestadasFallasBasicosPiezas (int i, int anioNuevo)
	{
		int ventasFallaBasicosPiezas = 0;
		
		// primero busco todos los productos basicos en inventario
		String sql = "select m_product_id from m_product where isactive='Y' and xx_vmr_typeinventory_id=" + getCtx().getContextAsInt("#XX_L_TYPEINVENTORYBASICO_ID");
		String sql2 = "";
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		PreparedStatement prst2 = null;
		ResultSet rs2 = null;
		ResultSet rs = null;

		try {
			rs = prst.executeQuery();
			while (rs.next())
			{			
				int dia = 1;	
				boolean fallo = false;
				while (dia<32 & !fallo)
				{
					try {
						sql2 = "select coalesce(sum(qtyOrdered),0) from c_orderline where m_product_id = " + rs.getInt(1) + " and" + 
						   " c_order_id in (select c_order_id from c_order where ISSOTRX='Y' and dateordered= (TO_DATE( '"+dia+"-" + i + "-" + (anioNuevo-1) + "', 'DD-MM-YYYY' )))"; 
						prst2 = DB.prepareStatement(sql2,null);
						rs2 = prst2.executeQuery();

						if (rs2.next())
						{
									if (rs2.getInt(1)>0)
										ventasFallaBasicosPiezas = ventasFallaBasicosPiezas + ((30 * rs2.getInt(1))/dia);
									else 
										fallo = true;
						}	
						
					} catch (Exception e)
					{

					} finally {
						DB.closeResultSet(rs2);
						DB.closeStatement(prst2);
					}
					
					dia++;
				}						
				
			}	

			DB.closeResultSet(rs);
			DB.closeStatement(prst);
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventasFallasBasicosPiezas " + e);
			System.out.println(sql2);
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs2);
			DB.closeStatement(prst2);
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
		
		return ventasFallaBasicosPiezas;
	}
	
	public BigDecimal ventasPresupuestadasBs(int i, int anioNuevo)
	{
		BigDecimal ventasPresupuestadasBs = new BigDecimal(0);
		
		// primero busco a ver si el mes en curso es uno de los especiales
		String sql = "select value, XX_VMR_Department_ID from xx_vmr_SpecialMonth where XX_VMR_Department_ID is null";
		PreparedStatement prst;
		prst = DB.prepareStatement(sql,null);	
		boolean especial = false; 
		try
		{
			String mes = "";
			ResultSet rs = prst.executeQuery();
			while (rs.next())
			{			
				 mes = rs.getString(1);
				 if (Integer.parseInt(mes)==i)
				 {
					 especial = true;
					 ventasPresupuestadasBs = ventasRealesBs(i, anioNuevo-1);					 
				 }
			}	
			rs.close();
			prst.close();
			
		} catch (SQLException e){
			System.out.println("Error al buscar los meses especiales " + e);	
			e.printStackTrace();
		}
		if (especial)
			return ventasPresupuestadasBs;
		
		
		
		String j = Integer.toString(i);
		if (i<10)
			j =  "0" + j;
		j = Integer.toString(anioNuevo) + j;
		
		sql = "SELECT COALESCE(SUM(XX_SALESAMOUNTBUD2),0) FROM XX_VMR_PRLD01 WHERE XX_BUDGETYEARMONTH = ('" + j + "')";
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
			e.printStackTrace();
		}
		
		
		return ventasPresupuestadasBs;
	}
	
	public BigDecimal ventasRealesBs(int i, int anioNuevo)
	{
		BigDecimal ventasRealesBs = new BigDecimal(0);
		
		String j = Integer.toString(i);
		if (i<10)
			j =  "0" + j;
		j = Integer.toString(anioNuevo) + j;
										// monvenrea
		String sql = "select coalesce(sum(XX_AMOUNTACTUALSALE),0) from xx_vmr_prld01 where XX_BUDGETYEARMONTH=" + j;

		
/**		
		String sql = "SELECT COALESCE(SUM(TOTALLINES),0) FROM C_ORDER WHERE ISSOTRX='Y' AND DATEORDERED = (TO_DATE( '" + i + "-" + (anioNuevo) + "', 'MM-YYYY' ))";
*/		PreparedStatement prst;
		prst = DB.prepareStatement(sql,null);

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
			e.printStackTrace();
		}
		if (ventasRealesBs.intValue()==0)
			ventasRealesBs = new BigDecimal(1);
		
		
		return ventasRealesBs;
	}

	
	public BigDecimal ventasPresupuestadasBsDepMes(int i, int anioNuevo, int departamento_id)
	{
		BigDecimal ventasPresupuestadasBsDepMes = new BigDecimal(0);
		
		// primero busco a ver si el mes en curso es uno de los especiales
		String sql = "select value from xx_vmr_SpecialMonth where XX_VMR_Department_ID is null";
		PreparedStatement prst;
		prst = DB.prepareStatement(sql,null);	
		boolean especial = false; 
		try
		{
			String mes = "";
			ResultSet rs = prst.executeQuery();
			while (rs.next())
			{			
				 mes = rs.getString(1);
				 if (Integer.parseInt(mes)==i)
				 {
					 especial = true;
					 ventasPresupuestadasBsDepMes = ventasRealesBsDepMes(i, anioNuevo-1, departamento_id);					 
				 }
			}
			rs.close();
			prst.close();
			
		} catch (SQLException e){
			System.out.println("Error al buscar los meses especiales " + e);
			e.printStackTrace();
		}
		if (especial)
			return ventasPresupuestadasBsDepMes;
		
		
		
		String j = Integer.toString(i);
		if (i<10)
			j =  "0" + j;
		j = Integer.toString(anioNuevo) + j;
								// monvenpre
		sql = "SELECT COALESCE(SUM(XX_SALESAMOUNTBUD2),0) FROM XX_VMR_PRLD01 WHERE XX_BUDGETYEARMONTH = ('" + j + "') AND XX_VMR_DEPARTMENT_ID=" + departamento_id;
		prst = DB.prepareStatement(sql,null);
	
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next())
			{			
				ventasPresupuestadasBsDepMes = rs.getBigDecimal(1);				
			}	
	
			rs.close();
			prst.close();
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventasPresupuestadasBs " + e);	
			e.printStackTrace();
		}
		
		
		return ventasPresupuestadasBsDepMes;
	}
	
	public BigDecimal ventasRealesBsDepMes(int i, int anioNuevo, int departamento_id)
	{
		BigDecimal ventasRealesBsDepMes = new BigDecimal(0);
		
		String j = Integer.toString(i);
		if (i<10)
			j =  "0" + j;
		j = Integer.toString(anioNuevo) + j;
										// monvenrea
		String sql = "select coalesce(sum(XX_AMOUNTACTUALSALE),0) from xx_vmr_prld01 where XX_BUDGETYEARMONTH=" + j + " and xx_vmr_department_id=" + departamento_id;
		
/**		
		String sql = "select coalesce(sum(priceactual),0) from c_orderline where c_order_id in (SELECT c_order_id FROM C_ORDER WHERE ISSOTRX='Y' AND DATEORDERED = (TO_DATE( '" + i + "-" + (anioNuevo) + "', 'MM-YYYY' ))) and m_product_id in (select m_product_id from m_product where isactive='Y' and xx_vmr_department_id=" + departamento_id + ")";
*/		PreparedStatement prst;
		prst = DB.prepareStatement(sql,null);

		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next())
			{			
				ventasRealesBsDepMes = rs.getBigDecimal(1);				
			}	

			rs.close();
			prst.close();
					
		} catch (SQLException e){
			System.out.println("Error al buscar ventasPresupuestadasBs " + e);		
			e.printStackTrace();
		}
		return ventasRealesBsDepMes;
	}
	
	public int ventasPresupuestadasPiezas(int i, int anioNuevo)
	{
		int ventasPresupuestadasPiezas = 0;
		
		// primero busco a ver si el mes en curso es uno de los especiales
		String sql = "select value from xx_vmr_SpecialMonth where XX_VMR_Department_ID is null";
		PreparedStatement prst;
		prst = DB.prepareStatement(sql,null);	
		boolean especial = false; 
		try
		{
			String mes = "";
			ResultSet rs = prst.executeQuery();
			while (rs.next())
			{			
				 mes = rs.getString(1);
				 if (Integer.parseInt(mes)==i)
				 {
					 especial = true;
					 ventasPresupuestadasPiezas = ventasRealesPiezas(i, anioNuevo-1);					 
				 }
			}
			rs.close();
			prst.close();
			
		} catch (SQLException e){
			System.out.println("Error al buscar los meses especiales " + e);	
			e.printStackTrace();
		}
		if (especial)
			return ventasPresupuestadasPiezas;
				
		String j = Integer.toString(i);
		if (i<10)
			j =  "0" + j;
		j = Integer.toString(anioNuevo) + j;
									// XX_canvenpre
		sql = "SELECT COALESCE(SUM(XX_SALESAMOUNTBUD),0) FROM XX_VMR_PRLD01 WHERE XX_BUDGETYEARMONTH = ('" + j + "')";
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
			System.out.println("Error al buscar ventasRealesPiezas " + e);
			e.printStackTrace();
		}
		
		
		return ventasPresupuestadasPiezas;
	}	
	
	public int ventasRealesPiezas(int i, int anioNuevo)
	{
		int ventasRealesPiezas = 0;

		String j = Integer.toString(i);
		if (i<10)
			j =  "0" + j;
		j = Integer.toString(anioNuevo) + j;
										// canvenrea
		String sql = "select coalesce(sum(XX_QUANTACTUALSALE),0) from xx_vmr_prld01 where XX_BUDGETYEARMONTH=" + j;
/**		
		String sql = "SELECT COALESCE(SUM(QtyOrdered),0) FROM C_ORDERLINE WHERE C_ORDER_ID IN (SELECT C_ORDER_ID FROM C_ORDER WHERE ISSOTRX='Y' AND DATEORDERED = (TO_DATE( '" + i + "-" + (anioNuevo) + "', 'MM-YYYY' )))";
*/		PreparedStatement prst;
		prst = DB.prepareStatement(sql,null);

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
			e.printStackTrace();
		}
		
		
		return ventasRealesPiezas;
	}	
	
	public int ventasRealesPiezasBasicosRebajados (int i, int anioNuevo)
	{
		int ventasRealesPiezasBasicosRebajados = 0;
		
		String sql = "select coalesce(0,sum(qtyordered)) from c_orderline where c_order_id in (select c_order_id from c_order where issotrx='Y' and dateordered= (TO_DATE( '" + i + "-" + (anioNuevo) + "', 'MM-YYYY' ))) and m_product_id in (select m_product_id from xx_vmr_priceconsecutive where xx_consecutiveorigin='R' and m_product_id in (select m_product_id from m_product where   isactive='Y' and xx_vmr_typeinventory_id=" + getCtx().getContextAsInt("#XX_L_TYPEINVENTORYBASICO_ID") + "))";
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
			System.out.println("Error al buscar ventasRealesPiezas " + e);	
			e.printStackTrace();
		}		
			
		return ventasRealesPiezasBasicosRebajados;
	}
	
	public BigDecimal ventasRealesBsBasicosRebajados (int i, int anioNuevo)
	{
		BigDecimal ventasRealesBsBasicosRebajados = new BigDecimal(0);
		
		String sql = "select coalesce(0,sum(priceactual)) from c_orderline where c_order_id in (select c_order_id from c_order where issotrx='Y' and dateordered= (TO_DATE( '" + i + "-" + (anioNuevo) + "', 'MM-YYYY' ))) and m_product_id in (select m_product_id from xx_vmr_priceconsecutive where xx_consecutiveorigin='R' and m_product_id in (select m_product_id from m_product where   isactive='Y' and xx_vmr_typeinventory_id=" + getCtx().getContextAsInt("#XX_L_TYPEINVENTORYBASICO_ID") + "))";
		PreparedStatement prst;
		prst = DB.prepareStatement(sql,null);

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
			e.printStackTrace();
		}		
			
		return ventasRealesBsBasicosRebajados;
	}
	
	public int ventasPresupuestadasPiezasRebajados(int i, int anioNuevo)
	{
		return ventasRealesPiezasBasicosRebajados(i, i-1);
	}
	
	public BigDecimal ventasPresupuestadasBsRebajados(int i, int anioNuevo)
	{
		return ventasRealesBsBasicosRebajados(i, i-1);
	}
	
	
	
	

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}

}
