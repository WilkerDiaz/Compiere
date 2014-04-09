package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Vector;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;

import compiere.model.cds.X_XX_VMR_WarehouseWeight;
import compiere.model.cds.X_XX_VMR_WeightFilter;

public class XX_StoreDistributionWeight extends SvrProcess{

	Integer cyearId = 0;
	
	@Override
	protected String doIt() throws Exception {
		
		Calendar fecha = Calendar.getInstance();
		
		int mesActual = fecha.get(Calendar.MONTH) + 1; 
		int anioActual = fecha.get(Calendar.YEAR);
		
		Integer tiendaId = 0;
		Integer dptoId = 0;
		Integer lineId = 0;
		Integer sectionId = 0;
		
		String mesActualStr = "";
		if(mesActual<10)
			mesActualStr = "0" + mesActual;
		
		Vector <Integer> store = new Vector <Integer>();
		Vector<int[]> dptoLineSec = new Vector<int[]>();
			
		
		
		String SQL5 = ("SELECT M_WAREHOUSE_ID AS TIENDAID FROM M_WAREHOUSE WHERE ISACTIVE = 'Y' ");
		
		try
		{
			PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null);
			ResultSet rs5 = pstmt5.executeQuery();
			
			while(rs5.next())
			{
				//tiendaId = rs5.getInt("TIENDAID");
				//System.out.println("TIENDA "+tiendaId);
				
				//Lleno el vector con todas las tienda
				store.add(rs5.getInt("TIENDAID"));
				
			}	
			rs5.close();
			pstmt5.close();
			
			String SQL6 = ("SELECT A.XX_VMR_DEPARTMENT_ID AS DPTO, B.XX_VMR_LINE_ID AS LINE, C.XX_VMR_SECTION_ID AS SECTION " +
					"FROM XX_VMR_DEPARTMENT A, XX_VMR_LINE B, XX_VMR_SECTION C " +
					"WHERE A.XX_VMR_DEPARTMENT_ID = B.XX_VMR_DEPARTMENT_ID " +
					"AND B.XX_VMR_LINE_ID = C.XX_VMR_LINE_ID " +
					"AND A.ISACTIVE = 'Y' AND B.ISACTIVE = 'Y' AND C.ISACTIVE = 'Y' ");
			
			PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null);
			ResultSet rs6 = pstmt6.executeQuery();
			
			while(rs6.next())
			{
				int [] array = new int[3];
				//Dpto
		        array[0] = rs6.getInt("DPTO");
		        //Line
		        array[1] = rs6.getInt("LINE");
		        //Section
		        array[2] = rs6.getInt("SECTION");
		        
		        dptoLineSec.add(array);
			}
			rs6.close();
			pstmt6.close();
		}
		catch (Exception e) {
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		
		int y;
		int x;
		String mes = null;
		Integer year = null;
		String anomescomp = null;
		Integer anioFiscal = 0;
		String aniomesFiscal = null; 
		Integer periodId = 0;
		
		BigDecimal ventasTienda =  new BigDecimal(0);
		BigDecimal ventasCadena = new BigDecimal(0);
		BigDecimal pesoTienda = new BigDecimal(0);
		
		
		for (y=0; y<store.size(); y++)
		{	System.out.println("TIENDA "+ y);
			//System.out.println(store.get(y));
			tiendaId = store.get(y);
			X_XX_VMR_WeightFilter weightFilter = new X_XX_VMR_WeightFilter(getCtx(), 0, null);
			
			for(x=0; x<dptoLineSec.size(); x++)
			{
				//System.out.println(dptoLineSec.get(x)[0] +" "+ dptoLineSec.get(x)[1]+" "+dptoLineSec.get(x)[2]);
				//System.out.println("DPTO "+ x);
				dptoId = dptoLineSec.get(x)[0];
				lineId = dptoLineSec.get(x)[1];
				sectionId = dptoLineSec.get(x)[2];
								
				for (Integer i=1;i<13;i++)
				{
					X_XX_VMR_WarehouseWeight warehoseWeight = new X_XX_VMR_WarehouseWeight(getCtx(), 0, get_TrxName());
					//System.out.println("MES "+ i);
					
					if(i<10)
						mes = "0" + i;
					else
						mes = i.toString();
						
					
					if(i>=7)
						year = anioActual - 1;
					else
						year = anioActual;

					//System.out.println(mes);
					anomescomp = year+mes;
					
					//anomespre = anioActual+mes.toString();
					
					// Busco el año fiscal que voy setear
					
					if(i>=7)
						anioFiscal = anioActual;
					else
						anioFiscal = anioActual + 1;
					
					aniomesFiscal = anioFiscal + mes;
					
					//System.out.println("Añomes comparar "+anomescomp);
					//System.out.println("Añomes fical "+aniomesFiscal);
					
					String SQL1 = ("SELECT A.C_PERIOD_ID AS PERIODID, B.C_YEAR_ID AS YEARID " +
							"FROM C_PERIOD A, C_YEAR B " +
							"WHERE TO_CHAR (A.STARTDATE, 'YYYYMM') = '"+aniomesFiscal+"' " +
							"AND A.ISACTIVE = 'Y' AND B.ISACTIVE = 'Y' " +
							"AND A.C_YEAR_ID = B.C_YEAR_ID ");
					
					PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null);
					ResultSet rs1 = pstmt1.executeQuery();
					
					if(rs1.next())
					{
						periodId = rs1.getInt("PERIODID");
						cyearId = rs1.getInt("YEARID");
					}
					rs1.close();
					pstmt1.close();
					
					// agregar un error de que le periodo fiscal no existe o no se encuentra activo
			
					if(Integer.parseInt((Integer.toString(anioActual)+mesActualStr)) > Integer.parseInt(anomescomp))
					{
						//System.out.println("Busco las reales");
						
						//Ventas de tiendas
						ventasTienda = calculoVentasTienda (year.toString(), mes, tiendaId, dptoId, lineId, sectionId);
						//Ventas total cadena
						ventasCadena = calculoVentasCadena (year.toString(), mes);
						//Peso Tienda
						pesoTienda = ventasTienda.divide(ventasCadena, 2, RoundingMode.HALF_UP);
						
						
					}// end if
					else
					{
						// Nunca pasa
						//System.out.println("Busco las Presupuestadas");
						
					}
					
					if(pesoTienda.compareTo(new BigDecimal(0)) == 0)
						warehoseWeight.setXX_Check(true);
					else
						warehoseWeight.setXX_Check(false);
						
					warehoseWeight.setC_Year_ID(cyearId);
					warehoseWeight.setC_Period_ID(periodId);
					warehoseWeight.setM_Warehouse_ID(tiendaId);
					warehoseWeight.setXX_VMR_Department_ID(dptoId);
					warehoseWeight.setXX_VMR_Line_ID(lineId);
					warehoseWeight.setXX_VMR_Section_ID(sectionId);
					warehoseWeight.setXX_WarehouseWeight(pesoTienda);
					warehoseWeight.save();
					
					
				}// end for
				
				
			}// end for dptolinesec
			
			weightFilter.setM_Warehouse_ID(tiendaId);
			weightFilter.setC_Year_ID(cyearId);
			weightFilter.save();

		}// end forstore
		

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
	
	public BigDecimal calculoVentasCadena (String year, String mes)
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
