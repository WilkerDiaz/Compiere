package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import compiere.model.cds.X_XX_VMR_Prld01;

public class RealValueBudget extends SvrProcess {

	/** 
	 * Modificado por JTrias
	 */
	
	X_XX_VMR_Prld01 presupuesto = null;
	Integer anioMes = 0;
	Integer anioMesPrev = 0;
	int anio = 0;
	int Mes = 0; 
	
	protected String doIt() throws Exception {
		
		//Año y mes (Dia anterior)
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		
		anioMes = (cal.get(Calendar.YEAR)*100)+cal.get(Calendar.MONTH)+1;
		anio = (cal.get(Calendar.YEAR));
		Mes =  cal.get(Calendar.MONTH)+1;
		
		//Mes anterior
		cal.add(Calendar.MONTH, -1);
		anioMesPrev = (cal.get(Calendar.YEAR)*100)+cal.get(Calendar.MONTH)+1;
		
		Date actualDate = new Date();
		System.out.println("Comenzando a las: " + actualDate.toString());
		
		//Borrado de variables REALES
		deleteRealValues();
		System.out.println("Borrado de valores reales");
		
		// Asigna el Costo y Cantidad de Order de Compra Nacionales e Internacionales por Año, Mes, Cat, Dep, Lin
		comprasColocadas();
		System.out.println("compras");
		
		comprasColocadasMesesFuturos();
		System.out.println("compras futuras");

		//Recepciones //Nuevo rubro
		recibidas();
		System.out.println("recibidas");
		
		//Asigna el Costo y Cantidad de Order de Recepciones Nacionales e Internacionales por Año, Mes, Cat, Dep, Lin
		chequeos();
		System.out.println("chequeos");
		
		ventas();
		System.out.println("ventas");
		
		//Trapasos enviados
		pedidosEnv();
		traspasosEnviados();
		System.out.println("traspasos enviados");
		
		//Traspasos recibidos
		pedidosRec();
		traspasosRecibidos();
		System.out.println("traspados recibidos");
	
		//Rebajas
		rebajasFR();
		rebajasFRRV();
		rebajasDEF();
		rebajasDEFRV();
		System.out.println("rebajas");
		
		//Devoluciones
		devoluciones();
		System.out.println("devoluciones");
		
		//Inventario Inicial
		if(Mes==7)
			inventoryFirstMonth();
		else
			inventario();
		
		System.out.println("Inv Inicial Real, primer mes del ejecicio");
		
		InvFinalReal();
		System.out.println("Inv Final Real");
		
		inventarioProyectado();
		System.out.println("Inv Proyectado");
		
		margenRotacion();
		System.out.println("Rotacion");
		
		limite();
		actualDate = new Date();
		System.out.println("Limite a las: " + actualDate.toString());
		
		Vector<Integer> months = monthForwad();
		
		inventarioACeroMesFuturo();
		System.out.println("Inventario a Cero meses Futuros");
		
		for(int i=0; i<months.size() ; i++){
			System.out.println("Mes: " + i + " de: " + months.size());
			inventarioMesesFuturos(months.get(i));
			inventarioProyectadoMesesFurutos(months.get(i));
			limiteFuturo(months.get(i));
			margenRotacionFuturo(months.get(i));
		}
		
		actualDate= new Date();
		System.out.println("Finalizando a las: " + actualDate.toString());
		
		return "Completado el Proceso";
	}

	@Override
	protected void prepare() {

	}
	
	// Está Funcion Extrae de las Ordenes de Compra Nacionales e Internacionales El Costo, Monto y Cantidad por Año, Mes, Cat, Dep, Lin
	private void comprasColocadas ()
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql;
		int numPresupuesto;
		
		sql =   "SELECT o.M_WareHouse_ID, o.XX_VMR_Category_ID, o.XX_VMR_Department_ID,  rf.XX_VMR_LINE_ID, rf.XX_VMR_SECTION_ID, " +
					"sum(case when o.XX_ORDERTYPE = 'Nacional' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') = "+ anioMes + "  then (rf.XX_LINEQTY)" +
					"*(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
					"/(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
					" else 0 end) as CantidadNacional, "+
					"ROUND(sum(case when o.XX_ORDERTYPE = 'Nacional' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') = "+ anioMes + "  then " +
					"(rf.XX_SalePrice*rf.XX_LINEQTY)" +
					"*(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
					"/(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
					" else 0 end), 2) as MontoNacionalPVP, "+
					"ROUND(sum(case when o.XX_ORDERTYPE = 'Nacional' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') = "+ anioMes + "  then (rf.PriceActual*rf.XX_LINEQTY)" +
					" else 0 end), 2) as MontoNacionalCosto, "+
					"sum(case when o.XX_ORDERTYPE = 'Importada' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') = "+ anioMes + " then (rf.XX_LINEQTY)" +
					"*(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
					"/(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
					" else 0 end) as CantidadImportada, "+
					"ROUND(sum(case when o.XX_ORDERTYPE = 'Importada' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') = "+ anioMes + " then " +
					"(rf.XX_SalePrice*rf.XX_LINEQTY)" +
					"*(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
					"/(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
					" else 0 end), 2) as MontoImportadaPVP,"+
					"ROUND(sum(case when o.XX_ORDERTYPE = 'Importada' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') = "+ anioMes + " then rf.PriceActual*rf.XX_LINEQTY else 0 end), 2) as MontoImportadaCosto, "+
					"sum(case when TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') < "+ anioMes + " then rf.XX_LineQty else 0 end) as CantidadMesesAnt, "+
					"ROUND(sum(case when TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') < "+ anioMes + " then (rf.XX_SalePrice*rf.XX_LineQty)" +
					"*(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
					"/(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
					" else 0 end), 2) as MontoMesesAntPVP, "+
					"ROUND(sum(case when TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') < "+ anioMes + " then rf.PriceActual*rf.XX_LineQty else 0 end), 2) as MontoMesesAntCosto, "+
					"decode(prl.XX_VMR_PRLD01_ID,NULL,0,prl.XX_VMR_PRLD01_ID) XX_VMR_PRLD01_ID " +
				"from C_Order o join XX_VMR_PO_LINEREFPROV rf on (o.C_Order_ID = rf.C_Order_ID) "+
				"left outer join XX_VMR_PRLD01 prl on "+
				"(o.XX_VMR_DEPARTMENT_ID = prl.XX_VMR_DEPARTMENT_ID and rf.XX_VMR_LINE_ID = prl.XX_VMR_LINE_ID "+
				"and rf.XX_VMR_SECTION_ID = prl.XX_VMR_SECTION_ID " +
				"and prl.XX_BUDGETYEARMONTH = "+ anioMes + " "+
				"and o.M_WareHouse_ID = prl.M_WareHouse_ID and o.XX_VMR_Category_ID = prl.XX_VMR_Category_ID) "+
				"where " +
				"(o.XX_ORDERSTATUS NOT IN ('PRO','SIT','AN','RE','CH') OR (o.XX_ORDERSTATUS = 'PRO' AND o.XX_COMESFROMSITME = 'Y')) " +
				"AND o.ISSOTRX = 'N' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') <= "+ anioMes + " "+
				"group by o.M_WareHouse_ID, o.XX_VMR_Category_ID, o.XX_VMR_Department_ID, rf.XX_VMR_LINE_ID, rf.XX_VMR_SECTION_ID, " +
				"prl.XX_VMR_PRLD01_ID ";
		try {
			 ps = DB.prepareStatement(sql, null);
			 rs = ps.executeQuery();
		    while (rs.next())
		    {
		    	//se Verifica en la tabla de Presupuesto si existe el registro
		    	 numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		//Actualizamos los Campos en el registro ya existente
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 mantenerCompras(presupuesto, rs);
		    	 }
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), anioMes, presupuesto);
		    		 mantenerCompras(presupuesto, rs);
		    		 
		    	 }
		    }	
		    commit();
		    
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
			rs.close();
			ps.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	// Está Funcion Extrae de las Ordenes de Compra Nacionales e Internacionales El Costo, Monto y Cantidad por Año, Mes, Cat, Dep, Lin, Sec, Marca. para meses futuros
	private void comprasColocadasMesesFuturos()
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql;
		int numPresupuesto;
		
		sql =   "SELECT o.M_WareHouse_ID, o.XX_VMR_Category_ID, o.XX_VMR_Department_ID,  rf.XX_VMR_LINE_ID, rf.XX_VMR_SECTION_ID, " +
					"sum(case when o.XX_ORDERTYPE = 'Nacional' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') > "+ anioMes +"  then rf.XX_LINEQTY else 0 end) as CantidadNacional, "+
					"ROUND(sum(case when o.XX_ORDERTYPE = 'Nacional' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') > "+ anioMes +"  then rf.XX_SalePrice*rf.XX_LINEQTY else 0 end), 2) as MontoNacionalPVP, "+
					"ROUND(sum(case when o.XX_ORDERTYPE = 'Nacional' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') > "+ anioMes +"  then rf.PriceActual*rf.XX_LINEQTY else 0 end), 2) as MontoNacionalCosto, "+
					"sum(case when o.XX_ORDERTYPE = 'Importada' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') > "+ anioMes +" then rf.XX_LINEQTY else 0 end) as CantidadImportada, "+
					"ROUND(sum(case when o.XX_ORDERTYPE = 'Importada' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') > "+ anioMes +" then rf.XX_SalePrice*rf.XX_LINEQTY else 0 end), 2) as MontoImportadaPVP,"+
					"ROUND(sum(case when o.XX_ORDERTYPE = 'Importada' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') > "+ anioMes +" then rf.PriceActual*rf.XX_LINEQTY else 0 end), 2) as MontoImportadaCosto, "+
					"0 as CantidadMesesAnt, " +
					"0 as MontoMesesAntPVP, " +
					"0 as MontoMesesAntCosto, " +
					"decode(prl.XX_VMR_PRLD01_ID,NULL,0,prl.XX_VMR_PRLD01_ID) XX_VMR_PRLD01_ID, " +
					"TO_CHAR(XX_ESTIMATEDDATE,'YYYYMM') XX_ESTIMATEDDATE " +
				"from C_Order o join XX_VMR_PO_LINEREFPROV rf on (o.C_Order_ID = rf.C_Order_ID) "+
				"left outer join XX_VMR_PRLD01 prl on "+
				"(o.XX_VMR_DEPARTMENT_ID = prl.XX_VMR_DEPARTMENT_ID and rf.XX_VMR_LINE_ID = prl.XX_VMR_LINE_ID "+
				"and rf.XX_VMR_SECTION_ID = prl.XX_VMR_SECTION_ID " +
				"and prl.XX_BUDGETYEARMONTH = TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') "+
				"and o.M_WareHouse_ID = prl.M_WareHouse_ID and o.XX_VMR_Category_ID = prl.XX_VMR_Category_ID) "+
				"where " +
				"(o.XX_ORDERSTATUS NOT IN ('PRO','SIT','AN','RE','CH') OR (o.XX_ORDERSTATUS = 'PRO' AND o.XX_COMESFROMSITME = 'Y')) " +
				"AND o.ISSOTRX = 'N' and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') > " + anioMes + " " + 
				"group by o.M_WareHouse_ID, o.XX_VMR_Category_ID, o.XX_VMR_Department_ID, rf.XX_VMR_LINE_ID, rf.XX_VMR_SECTION_ID, " +
				"prl.XX_VMR_PRLD01_ID, TO_CHAR(XX_ESTIMATEDDATE,'YYYYMM') ORDER BY TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM')";
		try {
			 ps = DB.prepareStatement(sql, null);
			 rs = ps.executeQuery();
		    while (rs.next())
		    {
		    	//se Verifica en la tabla de Presupuesto si existe el registro
		    	 numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		//Actualizamos los Campos en el registro ya existente
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 mantenerCompras(presupuesto, rs);
		    	 }
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  rs.getInt("XX_ESTIMATEDDATE"), presupuesto);
		    		 mantenerCompras(presupuesto, rs);
		    		 
		    	 }
		    }	
		    commit();
		    
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
			rs.close();
			ps.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	// Está Funcion Extrae de las Recepciones El Costo, PVP y Cantidad de O/C  Nacionales e Internacionales  por Año, Mes, Cat, Dep, Lin, Sec, Marca
	// No se estan seteando los campos al Costo
	private void chequeos()
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql;
		int numPresupuesto;
		
		sql = "SELECT o.M_WareHouse_ID, o.XX_VMR_Category_ID, o.XX_VMR_Department_ID, rf.XX_VMR_LINE_ID, rf.XX_VMR_SECTION_ID, " +
			  "sum(case " +
			  "when o.XX_ORDERTYPE = 'Nacional' and TO_CHAR(o.XX_RECEPTIONDATE,'YYYYMM') = "+ anioMes +" and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') >= "+ anioMes +" " +
			  "and TO_CHAR(o.XX_CHECKUPDATE,'YYYYMM') = "+ anioMes +" then lr.MovementQty " +
			  "else 0 end) as CantidadNacional, " +
			  
			  "ROUND(sum( " +
			  "case " +
			  "when o.XX_ORDERTYPE = 'Nacional' and TO_CHAR(o.XX_RECEPTIONDATE,'YYYYMM') = "+ anioMes +" and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') >= "+ anioMes +" " +
			  "and TO_CHAR(o.XX_CHECKUPDATE,'YYYYMM') = "+ anioMes +" then ROUND(asi.XX_SalePrice,2)*lr.MovementQty " +
			  "else 0 end), 2) as MontoNacionalPVP, " +
			  
			  "ROUND(sum((case " +
			  "when o.XX_ORDERTYPE = 'Nacional' and TO_CHAR(o.XX_RECEPTIONDATE,'YYYYMM') = "+ anioMes +" and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') >= "+ anioMes +" " +
			  "and TO_CHAR(o.XX_CHECKUPDate,'YYYYMM') = "+ anioMes +" then ROUND(rf.PriceActual,2)*lr.MovementQty " +
			   "else 0 end) " +
			  "/(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
			  "*(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID) ),2) as MontoNacionalCosto, " +
			  
			  "ROUND(sum(case " +
			  "when o.XX_ORDERTYPE = 'Importada' and TO_CHAR(o.XX_RECEPTIONDATE,'YYYYMM') = "+ anioMes +" and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') >= "+ anioMes +" " +
			  "and TO_CHAR(o.XX_CHECKUPDATE,'YYYYMM') = "+ anioMes +" then ROUND(asi.XX_SalePrice,2)*lr.MovementQty " +
			  "else 0 end), 2) as MontoImportadaPVP, " +
			  
			  "ROUND(sum(case " +
			  "when o.XX_ORDERTYPE = 'Importada' and TO_CHAR(o.XX_RECEPTIONDATE,'YYYYMM') = "+ anioMes +" and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') >= "+ anioMes +" " +
			  "and TO_CHAR(o.XX_CHECKUPDATE,'YYYYMM') = "+ anioMes +" then lr.MovementQty " +
			  "else 0 end), 2) as CantidadImportada, " +
 
			  "ROUND(sum((case " +
			  "when o.XX_ORDERTYPE = 'Importada' and TO_CHAR(o.XX_RECEPTIONDATE,'YYYYMM') = "+ anioMes +" and TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') >= "+ anioMes +" " +
			  "and TO_CHAR(o.XX_CHECKUPDate,'YYYYMM') = "+ anioMes +" then ROUND(rf.PriceActual,2)*(lr.PickedQty - XX_ExtraPieces + XX_MissingPieces) " +
			  "else 0 end) " +
			  "/(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
			  "*(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID)),2) as MontoImportadaCosto, " +
			 
			  "ROUND(sum(case " +
			  "when TO_CHAR(o.XX_CHECKUPDATE,'YYYYMM') = "+ anioMes +" AND " +
			  		"(TO_CHAR(o.XX_RECEPTIONDATE,'YYYYMM') < "+ anioMes +" OR TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') < "+ anioMes +") " +
			  "then lr.MovementQty else 0 end), 2) as CantidadMesesAnt, " +
			  
			  "ROUND(sum(case " +
			  "when TO_CHAR(o.XX_CHECKUPDATE,'YYYYMM') = "+ anioMes +" AND " +
			  		"(TO_CHAR(o.XX_RECEPTIONDATE,'YYYYMM') < "+ anioMes +" OR TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') < "+ anioMes +") " +
			  "then asi.XX_SalePrice*lr.MovementQty else 0 end), 2) as MontoMesesAntPVP, " +
			  
			  "ROUND(sum((case " + 
			  "when TO_CHAR(o.XX_CHECKUPDate,'YYYYMM') = "+ anioMes +" AND " +
			  		"(TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') < "+ anioMes +" OR TO_CHAR(o.XX_RECEPTIONDATE,'YYYYMM') < "+ anioMes +") " +
			  "then asi.PriceActual*lr.MovementQty else 0 end) " +
			  "/(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID) " +
			  "*(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE rf.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID)),2) as MontoMesesAntCosto, " +
			  
			  "decode(prl.XX_VMR_PRLD01_ID,NULL,0,prl.XX_VMR_PRLD01_ID) XX_VMR_PRLD01_ID " +
			  "from  C_Order o join XX_VMR_PO_LineRefProv rf on (o.C_Order_ID = rf.C_Order_ID) " +
			  "left outer join XX_VMR_PRLD01 prl on (o.XX_VMR_DEPARTMENT_ID = prl.XX_VMR_DEPARTMENT_ID and rf.XX_VMR_LINE_ID = prl.XX_VMR_LINE_ID " +
			  "and rf.XX_VMR_SECTION_ID = prl.XX_VMR_SECTION_ID and prl.XX_BUDGETYEARMONTH = "+ anioMes +" " +
			  "and o.M_WareHouse_ID = prl.M_WareHouse_ID and o.XX_VMR_Category_ID = prl.XX_VMR_Category_ID) join C_OrderLine ol on " +
			  "(rf.XX_VMR_PO_LineRefProv_ID =ol.XX_VMR_PO_LineRefProv_ID and o.C_Order_ID=ol.C_Order_ID) join M_InOutLine lr on (ol.C_OrderLine_ID = lr.C_OrderLine_ID) " +
			  "inner join M_AttributeSetInstance asi on (lr.M_AttributeSetInstance_ID = asi.M_AttributeSetInstance_ID) " + 
			  "where o.ISSOTRX = 'N' and (o.XX_ORDERSTATUS = 'CH') and TO_CHAR(o.XX_CHECKUPDATE,'YYYYMM') = "+ anioMes +" " +
			  "group by  o.M_WareHouse_ID, o.XX_VMR_Category_ID, o.XX_VMR_Department_ID, rf.XX_VMR_LINE_ID, rf.XX_VMR_SECTION_ID, prl.XX_VMR_PRLD01_ID";
		
		try{
			
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
		    {
		    	//se Verifica en la tabla de Presupuesto si existe el registro
		    	 numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		//Actualizamos los Campos en el registro ya existente
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 mantenerChequeo(presupuesto, rs);
		    	 }
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 mantenerChequeo(presupuesto, rs);
		    	 }
		    }
			
			commit();
			
		}catch (Exception e) {
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
			rs.close();
			ps.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void ventas()
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql;
		int numPresupuesto;
		
		sql = 	"with A as (select M_Product_ID, M_AttributeSetInstance_ID, XX_PRICECONSECUTIVE, TO_CHAR(created,'YYYYMM'), " +
				"XX_SALEPRICE, XX_UNITPURCHASEPRICE " +
				"from XX_VMR_PRICECONSECUTIVE " +
				"group by M_Product_ID, M_AttributeSetInstance_ID, XX_PRICECONSECUTIVE, TO_CHAR(created,'YYYYMM'), XX_SALEPRICE, XX_UNITPURCHASEPRICE)" +
				
				"select l.M_WareHouse_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_Line_ID, p.XX_VMR_Section_ID, " +
					"sum(l.QtyOrdered) as CantidadVenta, " +
					"ROUND(sum((l.PriceActual+l.XX_EmployeeDiscount)*l.QtyOrdered), 2) as MontoVentaPVP, " +
					"ROUND(sum(case when A.XX_UNITPURCHASEPRICE*l.QtyOrdered is not null then A.XX_UNITPURCHASEPRICE*l.QtyOrdered else 0 end), 2) as MontoVentaCosto, " +
					"sum(case when l.PRICELIST - (l.PRICEACTUAL+l.XX_EmployeeDiscount) > 0 then l.QTYORDERED else 0 end) as CantPromocion, " +
					"ROUND(sum(case when l.PRICELIST - (l.PRICEACTUAL+l.XX_EmployeeDiscount) > 0 then (l.PRICELIST - (l.PRICEACTUAL+l.XX_EmployeeDiscount))*l.QTYORDERED else 0 end), 2) as MontoPromocion, " +
					"ROUND(sum(case when (PRICELIST*QTYORDERED) > 0  then (((PRICELIST - (PRICEACTUAL+l.XX_EmployeeDiscount))*QTYORDERED)*100) / (PRICELIST*QTYORDERED) else 0 end), 2) as PorcentajeDescuento, " +
					"decode(prl.XX_VMR_PRLD01_ID,NULL,0,prl.XX_VMR_PRLD01_ID) XX_VMR_PRLD01_ID "+
			   "from C_Order o join C_OrderLine l on (o.C_Order_ID = l.C_Order_ID AND o.dateordered=l.dateordered) " +
			   "join M_Product p on (l.M_Product_ID = p.M_Product_ID) " +
			   "left outer join A on (p.M_Product_ID = A.M_Product_ID " +
				 "and l.XX_PriceConsecutive = A.XX_PriceConsecutive and l.M_AttributeSetInstance_ID = A.M_AttributeSetInstance_ID) " +
				 "left outer join XX_VMR_PRLD01 prl on "+
				 "(p.XX_VMR_DEPARTMENT_ID = prl.XX_VMR_DEPARTMENT_ID and p.XX_VMR_LINE_ID = prl.XX_VMR_LINE_ID "+
				 "and p.XX_VMR_SECTION_ID = prl.XX_VMR_SECTION_ID " +
				 "and prl.XX_BUDGETYEARMONTH = " + anioMes + " " +
				 "and o.M_WareHouse_ID = prl.M_WareHouse_ID and p.XX_VMR_Category_ID = prl.XX_VMR_Category_ID) "+
			   "where  o.ISSOTRX = 'Y' and o.DOCSTATUS = 'CO' and " +
			   "o.dateordered between TO_DATE('"+ anioMes +"01','YYYYMMDD') " +
					"and last_day(TO_DATE('"+ anioMes +"01','YYYYMMDD')) " +
			   "group by l.M_WareHouse_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_Line_ID, p.XX_VMR_Section_ID, " +
			   "prl.XX_VMR_PRLD01_ID";
			   
		
		try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while(rs.next())
			{
				//se Verifica en la tabla de Presupuesto si existe el registro
		    	 numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 mantenerVentas(presupuesto, rs);
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 mantenerVentas(presupuesto, rs);
		    	 } 
		    	 
			}
			
			commit();
			
		}catch (Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		} finally{
			try{
				ps.close();
				rs.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//Se suman a los pedidos enviados
	private void traspasosEnviados()
	{
		int numPresupuesto;
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		sql = "SELECT * FROM" +
			    "(WITH A as ( " +
				"SELECT TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM') fecha,XX_DEPARTUREWAREHOUSE_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID, " +
				"sum(ml.XX_APPROVEDQTY) cantidad,sum(ml.XX_SALEPRICE*XX_APPROVEDQTY) monto " +
				"FROM M_Movement M, M_MovementLine ML, XX_VLO_DISPATCHGUIDE D, XX_VLO_DETAILDISPATCHGUIDE DD, M_PRODUCT P " +
				"WHERE M.M_Movement_ID = ML.M_Movement_ID AND ML.M_PRODUCT_ID = P.M_PRODUCT_ID " +
				"AND DD.M_MOVEMENTT_ID = M.M_Movement_ID AND DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID " +
				"AND M.C_DOCTYPE_ID IN (1000356) AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM') = "+ anioMes + " " +
				"AND M.XX_Status = 'AT' " +
				"group by TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM'),XX_DEPARTUREWAREHOUSE_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID " +
				")" +
				"select " +
				"A.XX_DEPARTUREWAREHOUSE_ID, A.XX_VMR_Category_ID, A.XX_VMR_Department_ID, A.XX_VMR_LINE_ID, A.XX_VMR_SECTION_ID," + 
				"cantidad, monto, NVL(PRLD.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID " +
				"from A left outer join XX_VMR_PRLD01 PRLD " +
				"on (PRLD.M_WAREHOUSE_ID = A.XX_DEPARTUREWAREHOUSE_ID AND A.XX_VMR_Category_ID = PRLD.XX_VMR_Category_ID " +
				"AND A.XX_VMR_DEPARTMENT_ID = PRLD.XX_VMR_DEPARTMENT_ID AND A.XX_VMR_LINE_ID = PRLD.XX_VMR_LINE_ID " +
				"AND A.XX_VMR_SECTION_ID = PRLD.XX_VMR_SECTION_ID AND XX_BUDGETYEARMONTH = fecha)" +
			")" +
			"UNION " +
			"SELECT * FROM " +
				"(WITH A as ( " +
				"SELECT TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM') fecha,XX_DEPARTUREWAREHOUSE_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID, " +
				"sum(ml.XX_APPROVEDQTY) cantidad,sum(asi.XX_SALEPRICE*XX_APPROVEDQTY) monto " +
				"FROM M_Movement M, M_MovementLine ML, XX_VLO_DISPATCHGUIDE D, XX_VLO_DETAILDISPATCHGUIDE DD, M_PRODUCT P, M_ATTRIBUTESETINSTANCE ASI " +
				"WHERE M.M_Movement_ID = ML.M_Movement_ID AND ML.M_PRODUCT_ID = P.M_PRODUCT_ID " +
				"AND DD.M_MOVEMENTM_ID = M.M_Movement_ID AND DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID " +
				"AND M.C_DOCTYPE_ID IN (1000335) AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM') = "+ anioMes + " " +
				"AND M.XX_Status = 'AC' AND ASI.M_ATTRIBUTESETINSTANCE_ID = ML.M_ATTRIBUTESETINSTANCE_ID " +
				"group by TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM'),XX_DEPARTUREWAREHOUSE_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID " +
				")" +
				"select " +
				"A.XX_DEPARTUREWAREHOUSE_ID, A.XX_VMR_Category_ID, A.XX_VMR_Department_ID, A.XX_VMR_LINE_ID, A.XX_VMR_SECTION_ID," + 
				"cantidad, monto, NVL(PRLD.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID " +
				"from A left outer join XX_VMR_PRLD01 PRLD " +
				"on (PRLD.M_WAREHOUSE_ID = A.XX_DEPARTUREWAREHOUSE_ID AND A.XX_VMR_Category_ID = PRLD.XX_VMR_Category_ID " +
				"AND A.XX_VMR_DEPARTMENT_ID = PRLD.XX_VMR_DEPARTMENT_ID AND A.XX_VMR_LINE_ID = PRLD.XX_VMR_LINE_ID " +
				"AND A.XX_VMR_SECTION_ID = PRLD.XX_VMR_SECTION_ID " +
				"AND XX_BUDGETYEARMONTH = fecha)" +
			   ")";
		
		try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 presupuesto.setXX_NUMTRANSFSENT(rs.getBigDecimal("cantidad").add(presupuesto.getXX_NUMTRANSFSENT()));
		    		 presupuesto.setXX_TRANSFAMOUNTSENT(rs.getBigDecimal("monto").add(presupuesto.getXX_TRANSFAMOUNTSENT())); 
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 presupuesto.setXX_NUMTRANSFSENT(rs.getBigDecimal("cantidad"));
		    		 presupuesto.setXX_TRANSFAMOUNTSENT(rs.getBigDecimal("monto"));
		    	 }
		    	 
		    	 presupuesto.save(); 
			}
			
			commit();
			
		}catch (Exception e){
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//Se suman a los pedidos recibidos
	private void traspasosRecibidos()
	{
		int numPresupuesto;
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		sql = "SELECT * FROM " +
				"(WITH A as ( " +
				"SELECT TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM') fecha,XX_ARRIVALWAREHOUSE_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID, " +
				"sum(ml.XX_APPROVEDQTY) cantidad,sum(ml.XX_SALEPRICE*XX_APPROVEDQTY) monto " +
				"FROM M_Movement M, M_MovementLine ML, XX_VLO_DISPATCHGUIDE D, XX_VLO_DETAILDISPATCHGUIDE DD, M_PRODUCT P " +
				"WHERE M.M_Movement_ID = ML.M_Movement_ID AND ML.M_PRODUCT_ID = P.M_PRODUCT_ID " +
				"AND DD.M_MOVEMENTT_ID = M.M_Movement_ID AND DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID " +
				"AND M.C_DOCTYPE_ID IN (1000356) AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM') = "+ anioMes + " " +
				"AND M.XX_Status = 'AT' " +
				"group by TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM'),XX_ARRIVALWAREHOUSE_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID " +
				")" +
				"select " +
				"A.XX_ARRIVALWAREHOUSE_ID, A.XX_VMR_Category_ID, A.XX_VMR_Department_ID, A.XX_VMR_LINE_ID, A.XX_VMR_SECTION_ID," + 
				"cantidad, monto, NVL(PRLD.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID " +
				"from A left outer join XX_VMR_PRLD01 PRLD " +
				"on (PRLD.M_WAREHOUSE_ID = A.XX_ARRIVALWAREHOUSE_ID AND A.XX_VMR_Category_ID = PRLD.XX_VMR_Category_ID " +
				"AND A.XX_VMR_DEPARTMENT_ID = PRLD.XX_VMR_DEPARTMENT_ID AND A.XX_VMR_LINE_ID = PRLD.XX_VMR_LINE_ID " +
				"AND A.XX_VMR_SECTION_ID = PRLD.XX_VMR_SECTION_ID " +
				"AND XX_BUDGETYEARMONTH = fecha)" +
				")" +
			   "UNION " +
			   "SELECT * FROM " +
				"(WITH A as ( " +
				"SELECT TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM') fecha,XX_ARRIVALWAREHOUSE_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID, " +
				"sum(ml.XX_APPROVEDQTY) cantidad,sum(asi.XX_SALEPRICE*XX_APPROVEDQTY) monto " +
				"FROM M_Movement M, M_MovementLine ML, XX_VLO_DISPATCHGUIDE D, XX_VLO_DETAILDISPATCHGUIDE DD, M_PRODUCT P, M_ATTRIBUTESETINSTANCE ASI " +
				"WHERE M.M_Movement_ID = ML.M_Movement_ID AND ML.M_PRODUCT_ID = P.M_PRODUCT_ID " +
				"AND DD.M_MOVEMENTM_ID = M.M_Movement_ID AND DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID " +
				"AND M.C_DOCTYPE_ID IN (1000335) AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM') = "+ anioMes + " " +
				"AND M.XX_Status = 'AC' AND ASI.M_ATTRIBUTESETINSTANCE_ID = ML.M_ATTRIBUTESETINSTANCE_ID " +
				"group by TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM'),XX_ARRIVALWAREHOUSE_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID " +
				")" +
				"select " +
				"A.XX_ARRIVALWAREHOUSE_ID, A.XX_VMR_Category_ID, A.XX_VMR_Department_ID, A.XX_VMR_LINE_ID, A.XX_VMR_SECTION_ID," + 
				"cantidad, monto, NVL(PRLD.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID " +
				"from A left outer join XX_VMR_PRLD01 PRLD " +
				"on (PRLD.M_WAREHOUSE_ID = A.XX_ARRIVALWAREHOUSE_ID AND A.XX_VMR_Category_ID = PRLD.XX_VMR_Category_ID " +
				"AND A.XX_VMR_DEPARTMENT_ID = PRLD.XX_VMR_DEPARTMENT_ID AND A.XX_VMR_LINE_ID = PRLD.XX_VMR_LINE_ID " +
				"AND A.XX_VMR_SECTION_ID = PRLD.XX_VMR_SECTION_ID " +
				"AND XX_BUDGETYEARMONTH = fecha)" +
			   ")";
			
		
		try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 presupuesto.setXX_NUMBTRANSFREV(rs.getBigDecimal("cantidad").add(presupuesto.getXX_NUMBTRANSFREV()));
		    		 presupuesto.setXX_TRANSFAMOUNTRECEIVED(rs.getBigDecimal("monto").add(presupuesto.getXX_TRANSFAMOUNTRECEIVED())); 
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 presupuesto.setXX_NUMBTRANSFREV(rs.getBigDecimal("cantidad"));
		    		 presupuesto.setXX_TRANSFAMOUNTRECEIVED(rs.getBigDecimal("monto"));;
		    	 }
		    	 
		    	 presupuesto.save(); 
			}
			
			commit();
			
		}catch (Exception e){
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void inventarioProyectado()
	{
		int numPresupuesto;
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		sql = "with VentasPresupuestas as " +
			  "(select " +
			  "sum(case when XX_YEARBUDGET||XX_MONTHBUDGET || (case when length(to_char(XX_BUDGETDAY)) = 2 then to_char(XX_BUDGETDAY) else 0 || XX_BUDGETDAY end) " +
			  "between to_char(sysdate, 'YYYYMMDD') and to_char(last_day(sysdate), 'YYYYMMDD') then XX_MONESTIR else 0 end) ventaspresu, " +
			  "p.M_WareHouse_ID, d.XX_VMR_category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID " +
			  "from  XX_VMR_PRLD03 p, XX_VMR_Department d " +
		      "where p.XX_VMR_Department_ID = d.XX_VMR_Department_ID " +
		      "and p.XX_YEARBUDGET||p.XX_MONTHBUDGET = " + anioMes + " " +
		      "group by p.M_WareHouse_ID, d.XX_VMR_category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID " +
		      "), totaldep as " +
		      "(select p.XX_BUDGETYEARMONTH, p.M_WareHouse_ID, p.XX_VMR_category_ID, p.XX_VMR_Department_ID, sum(p.XX_SALESAMOUNTBUD2) total " +
		      "from XX_VMR_PRLD01 p " +
		      "group by p.XX_BUDGETYEARMONTH, p.M_WareHouse_ID, p.XX_VMR_category_ID, p.XX_VMR_Department_ID) "+
		      ", totalsec as " +
		      "(select p.XX_BUDGETYEARMONTH, p.M_WareHouse_ID, p.XX_VMR_category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID, sum(p.XX_SALESAMOUNTBUD2) total " +
		      "from XX_VMR_PRLD01 p " +
		      "group by p.XX_BUDGETYEARMONTH, p.M_WareHouse_ID, p.XX_VMR_category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID) " +       
		      "select p.M_WareHouse_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, " +
		      
		      "(p.XX_AmountIniInveReal + (p.XX_AMOUNTPLACEDNACPURCHCOST + p.XX_PURCHAMOUNTPLACEDCOSTIMP + p.XX_PURCHAMOUNTPLADPASTMONTHS) + XX_ReceiptPVP + " +
		      "(p.XX_NACPURCHAMOUNTRECEIVED + p.XX_PURCHAMOUNTREVIMPD + p.XX_PURCHAMOUNTREDPASTMONTHS) + XX_PlacedOrderPVPAdjustment " +
		      "- (p.XX_PROMSALEAMOUNTBUD + p.XX_AMOUNTSALEFRBUD + p.XX_FINALSALEAMOUNTBUD) - p.XX_ReturnsPVP + " +
		      "p.XX_TRANSFAMOUNTRECEIVED - p.XX_TRANSFAMOUNTSENT " +
		      "-(XX_AmountActualSale + (COALESCE(pp.ventaspresu, 0) * ROUND((case when tdep.total = 0 then 0 else (tsec.total * 100 / tdep.total) end)/100,2)))) " +			
		      "InvFinalProyectado, " +
		      
		      "(p.XX_NUMINIINVEREAL + (p.XX_NUMNACSHOPPINGPLACED + p.XX_PURCHQUANTIMPDPLACED + p.XX_PURCHQUANTPLACEDMONTHS ) + XX_ReceiptQTY + " +
		      "(p.XX_QUANTPURCHNAC + p.XX_QUANTPURCHAMOUNTSREV + p.XX_NUMMONTHSREDSHOP) - p.XX_ReturnsQTY + (p.XX_PROMSALENUMBUD + p.XX_BUDAMOUNTFRSALE + p.XX_FINALBUDAMOUNTSALE) + " +
		      "p.XX_NUMBTRANSFREV - p.XX_NUMTRANSFSENT - p.XX_QUANTACTUALSALE) " +
		      "CantInvFinalProyectado, " +
		      
		      "ROUND( " +
		      "(case when XX_AmountActualSale + (COALESCE(pp.ventaspresu, 0) * ROUND((case when tdep.total = 0 then 0 else (tsec.total * 100 / tdep.total) end)/100,2)) = 0 then  0 " +
		      "else p.XX_AmountIniInveReal / (XX_AmountActualSale + (COALESCE(pp.ventaspresu, 0) * ROUND((case when tdep.total = 0 then 0 else (tsec.total * 100 / tdep.total) end)/100,2))) end), 2) " +
			  "CoberturaReal, " +
			  
			  "decode(p.XX_VMR_PRLD01_ID,NULL,0,p.XX_VMR_PRLD01_ID) XX_VMR_PRLD01_ID " +
			  "from XX_VMR_PRLD01 p " +
			  "left outer join VentasPresupuestas pp on (pp.M_WAREHOUSE_ID = p.M_WAREHOUSE_ID and pp.XX_VMR_category_ID = p.XX_VMR_category_ID " +
			  "and pp.XX_VMR_Department_ID = p.XX_VMR_Department_ID) " +
			  "left outer join totaldep tdep         on (tdep.M_WAREHOUSE_ID = p.M_WAREHOUSE_ID and tdep.XX_VMR_category_ID = p.XX_VMR_category_ID " +
			  "and tdep.XX_VMR_Department_ID = p.XX_VMR_Department_ID AND tdep.XX_BUDGETYEARMONTH = p.XX_BUDGETYEARMONTH) " +
			  "left outer join totalsec tsec         on (tsec.M_WAREHOUSE_ID = p.M_WAREHOUSE_ID and tsec.XX_VMR_category_ID = p.XX_VMR_category_ID " +
			  "and tsec.XX_VMR_Department_ID = p.XX_VMR_Department_ID and tsec.XX_VMR_LINE_ID = p.XX_VMR_LINE_ID and tsec.XX_VMR_SECTION_ID = p.XX_VMR_SECTION_ID AND tsec.XX_BUDGETYEARMONTH = p.XX_BUDGETYEARMONTH) " +
			  "where p.XX_BUDGETYEARMONTH = " + anioMes + " " +
			  "order by p.M_WAREHOUSE_ID, p.XX_VMR_CATEGORY_ID, p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_LINE_ID";
		   
		   try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 mantenerInvProy(presupuesto, rs);
		    		 
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 mantenerInvProy(presupuesto, rs);
		    	 }
		    	 
			}
			
			commit();
			
		} catch (Exception e) {
		
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void inventarioProyectadoMesesFurutos(Integer yearMonth)
	{
		int numPresupuesto;
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		   sql ="select p.M_WareHouse_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID, " +
				"" +
				"(" +
				"  (p.XX_AmountIniInveReal + (p.XX_AMOUNTPLACEDNACPURCHCOST + p.XX_PURCHAMOUNTPLACEDCOSTIMP + p.XX_PURCHAMOUNTPLADPASTMONTHS)) " +
				"- (p.XX_PROMSALEAMOUNTBUD + p.XX_AMOUNTSALEFRBUD + p.XX_FINALSALEAMOUNTBUD) " +
				"- (XX_SALESAMOUNTBUD2) " +
				") InvFinalProyectado," +
				""+
				"  (p.XX_NUMINIINVEREAL + (p.XX_NUMNACSHOPPINGPLACED + p.XX_PURCHQUANTIMPDPLACED + p.XX_PURCHQUANTPLACEDMONTHS ) + " + 
				"  (p.XX_QUANTPURCHNAC + p.XX_QUANTPURCHAMOUNTSREV + p.XX_NUMMONTHSREDSHOP) + (p.XX_PROMSALENUMBUD + p.XX_BUDAMOUNTFRSALE + p.XX_FINALBUDAMOUNTSALE) + " + 
				"   - p.XX_SALESAMOUNTBUD) " +
				" CantInvFinalProyectado, " +
				"  0 CoberturaReal, " +
				" XX_VMR_PRLD01_ID "+
				" from XX_VMR_PRLD01 p " +
				"where p.XX_BUDGETYEARMONTH = " + yearMonth;
		   
		   try {
				ps = DB.prepareStatement(sql, null);
				rs = ps.executeQuery();
				
				while (rs.next())
				{
					numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
			    	 if(numPresupuesto > 0)
			    	 {
			    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
			    		 mantenerInvProy(presupuesto, rs);
			    		 
			    	 } 
			    	 else
			    	 {
			    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
			    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  yearMonth, presupuesto);
			    		 mantenerInvProy(presupuesto, rs);
			    	 }
				}
			
				commit();
			
		   } catch (Exception e) {
		
				log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
				rollback();
		   }finally{
			   try{
				   rs.close();
				   ps.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
		   }
	}
	
	private void margenRotacion()
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		sql = "with PASTMONTH as (select M_WareHouse_ID, XX_VMR_Category_ID, XX_VMR_Department_ID, XX_VMR_LINE_ID, XX_VMR_Section_ID, XX_INVAMOUNTFINALREAL " +
			  "from XX_VMR_PRLD01 p where XX_BUDGETYEARMONTH = " +  anioMesPrev + " " +
			  ")" +
			  "select p.M_WareHouse_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_Section_ID, " +		
					
			  "ROUND(case when (past.XX_INVAMOUNTFINALREAL + p.XX_INVAMOUNTFINALREAL) <> 0 then (p.XX_AmountActualSale * 12) / ((past.XX_INVAMOUNTFINALREAL + p.XX_INVAMOUNTFINALREAL)/2) else 0 end, 2) as RotacionReal, " +
					
			  "ROUND(case when (p.XX_AMOUNTPLACEDNACCAMPRA + p.XX_PURCHAMOUNTPLACEDIMPD + p.XX_PURCHAMOUNTPLADPASTMONTHS) <> 0 then" +
			  "(((p.XX_AMOUNTPLACEDNACCAMPRA + p.XX_PURCHAMOUNTPLACEDIMPD + p.XX_PURCHAMOUNTPLADPASTMONTHS) - " +
			  "(p.XX_AMOUNTPLACEDNACPURCHCOST + p.XX_PURCHAMOUNTPLACEDCOSTIMP + p.XX_PURCHAMOUNTPLADPASTMONTHS)) / " +
			  "(p.XX_AMOUNTPLACEDNACCAMPRA + p.XX_PURCHAMOUNTPLACEDIMPD + p.XX_PURCHAMOUNTPLADPASTMONTHS))*100 else 0 end, 2) as MargenCompraReal, " +
					
			  "ROUND(case when  XX_AMOUNTACTUALSALE <> 0 then((XX_AMOUNTACTUALSALE - XX_AMOUNTSALECOST) / XX_AMOUNTACTUALSALE) * 100 else 0 end, 2) MargenBrutoGanado, " +
					
			  "ROUND(case when XX_AMOUNTINIINVEREAL <> 0 then ((XX_AMOUNTINIINVEREAL - XX_INIAMOUNTINVECOST) / XX_AMOUNTINIINVEREAL) * 100 else 0 end, 2) MargenPorGanar, " +
					
			  "XX_VMR_PRLD01_ID "+
			  "from XX_VMR_PRLD01 p, PASTMONTH past " +
			  "where XX_BUDGETYEARMONTH = " + anioMes + " " +
			  "and p.M_WareHouse_ID = past.M_WareHouse_ID and p.XX_VMR_Category_ID = past.XX_VMR_Category_ID " +
			  "and p.XX_VMR_Department_ID = past.XX_VMR_Department_ID and p.XX_VMR_LINE_ID = past.XX_VMR_LINE_ID " +
			  "and p.XX_VMR_Section_ID = past.XX_VMR_Section_ID";
		
		try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{ 
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 matenerMargenRotacion(presupuesto, rs);
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
	    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 matenerMargenRotacion(presupuesto, rs);
		    	 }	 
			}
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void limite()
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		sql = "select p.M_WareHouse_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_Section_ID, " +
					
			  "NVL(XX_FINALINVAMOUNTBUD2,0) - NVL(XX_FINALINVAMOUNTPROJD,0) as MontoLimiteCompra," +
			  "NVL(XX_FINALINVAMOUNTBUD,0) - NVL(XX_NUMPROJDFINALINV,0) as CantidadLimiteCompra," +
			  
			  "XX_VMR_PRLD01_ID "+
			  "from XX_VMR_PRLD01 p " +
			  "where XX_BUDGETYEARMONTH = " + anioMes;
		
		
		try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{ 
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 matenerLimite(presupuesto, rs);
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
	    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 matenerLimite(presupuesto, rs);
		    	 }	 
			}
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Margen Rotacion Meses Futuros
	 */
	private void margenRotacionFuturo(Integer yearMonth)
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		sql = "with PASTMONTH as (select M_WareHouse_ID, XX_VMR_Category_ID, XX_VMR_Department_ID, XX_VMR_LINE_ID, XX_VMR_Section_ID, XX_INVAMOUNTFINALREAL " +
			  "from XX_VMR_PRLD01 p where XX_BUDGETYEARMONTH = TO_CHAR(add_months(sysdate,-1),'YYYYMM')" +
			  ")" +
			  "select p.M_WareHouse_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_Section_ID, " +
					
			  "ROUND(case when (past.XX_INVAMOUNTFINALREAL + p.XX_INVAMOUNTFINALREAL) <> 0 then (p.XX_AmountActualSale * 12) / ((past.XX_INVAMOUNTFINALREAL + p.XX_INVAMOUNTFINALREAL)/2) else 0 end, 2) as RotacionReal, " +
					
			  "ROUND(case when (p.XX_AMOUNTPLACEDNACCAMPRA + p.XX_PURCHAMOUNTPLACEDIMPD + p.XX_PURCHAMOUNTPLADPASTMONTHS) <> 0 then" +
			  "(((p.XX_AMOUNTPLACEDNACCAMPRA + p.XX_PURCHAMOUNTPLACEDIMPD + p.XX_PURCHAMOUNTPLADPASTMONTHS) - " +
			  "(p.XX_AMOUNTPLACEDNACPURCHCOST + p.XX_PURCHAMOUNTPLACEDCOSTIMP + p.XX_PURCHAMOUNTPLADPASTMONTHS)) / " +
			  "(p.XX_AMOUNTPLACEDNACCAMPRA + p.XX_PURCHAMOUNTPLACEDIMPD + p.XX_PURCHAMOUNTPLADPASTMONTHS))*100 else 0 end, 2) as MargenCompraReal, " +
					
			  "ROUND(case when  XX_AMOUNTACTUALSALE <> 0 then((XX_AMOUNTACTUALSALE - XX_AMOUNTSALECOST) / XX_AMOUNTACTUALSALE) * 100 else 0 end, 2) MargenBrutoGanado, " +
					
			  "ROUND(case when XX_AMOUNTINIINVEREAL <> 0 then ((XX_AMOUNTINIINVEREAL - XX_INIAMOUNTINVECOST) / XX_AMOUNTINIINVEREAL) * 100 else 0 end, 2) MargenPorGanar, " +
					
			  "XX_VMR_PRLD01_ID "+
			  "from XX_VMR_PRLD01 p, PASTMONTH past " +
			  "where XX_BUDGETYEARMONTH = " + yearMonth + " " +
			  "and p.M_WareHouse_ID = past.M_WareHouse_ID and p.XX_VMR_Category_ID = past.XX_VMR_Category_ID " +
			  "and p.XX_VMR_Department_ID = past.XX_VMR_Department_ID and p.XX_VMR_LINE_ID = past.XX_VMR_LINE_ID " +
			  "and p.XX_VMR_Section_ID = past.XX_VMR_Section_ID";
		
		try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{ 
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 matenerMargenRotacion(presupuesto, rs);
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
	    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 matenerMargenRotacion(presupuesto, rs);
		    	 }	 
			}
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void limiteFuturo(Integer yearMonth)
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Calendar cal = Calendar.getInstance();
		int anioMes = (cal.get(Calendar.YEAR)*100)+cal.get(Calendar.MONTH)+1;
		
		sql = "select p.M_WareHouse_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_Section_ID, " +
				
			  "NVL(XX_FINALINVAMOUNTBUD2,0) - NVL(XX_FINALINVAMOUNTPROJD,0) as MontoLimiteCompra," +
			  "NVL(XX_FINALINVAMOUNTBUD,0) - NVL(XX_NUMPROJDFINALINV,0) as CantidadLimiteCompra," +
			  
			  "XX_VMR_PRLD01_ID "+  
			  "from XX_VMR_PRLD01 p " +
			  "where XX_BUDGETYEARMONTH = " + yearMonth;
			 
		try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{ 
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 matenerLimite(presupuesto, rs);
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
	    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 matenerLimite(presupuesto, rs);
		    	 }	 
			}
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Inventario Inicial Real
	 */
	private void inventario()
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String aux = anioMes.toString();
		
		sql = " WITH S AS " +
		  "(SELECT XX_VMR_PRLD01_ID, M_WareHouse_ID, XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, " +
		  "XX_VMR_LINE_ID, XX_VMR_Section_ID FROM XX_VMR_PRLD01 b WHERE b.XX_BUDGETYEARMONTH = "+anioMes+") , " +
		  "A AS " +
		  "(SELECT M_WareHouse_ID, XX_VMR_PRLD01_ID, XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, " +
		  "XX_VMR_LINE_ID, XX_VMR_Section_ID, XX_INVAMOUNTFINALREAL, XX_NUMREALFINALINV " +
		  "FROM XX_VMR_PRLD01  WHERE ";
		  
		  if(aux.substring(4, 6).equals("01")){
				 
			  Integer year = new Integer(aux.substring(0, 4));
			  year = year - 1;
			  aux = year.toString() + "12";
			  
			  sql += "XX_BUDGETYEARMONTH = "+ (aux);
		  }
		  else
			  sql += "XX_BUDGETYEARMONTH = "+ (anioMes - 1);			  
		  
		  sql += ") " +
		  
		  "SELECT a.M_WareHouse_ID, a.XX_VMR_Category_ID, a.XX_VMR_Department_ID, a.XX_VMR_LINE_ID, " +
		  "a.XX_VMR_Section_ID, nvl(S.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID, " +
		  "NVL(Sum(A.XX_INVAMOUNTFINALREAL), 0) InvInicialReal, NVL(sum(A.XX_NUMREALFINALINV), 0) CantInvInicialReal " +
		  "FROM " +
		  "A LEFT JOIN S ON ( " +
		  "S.M_WAREHOUSE_ID = A.M_WAREHOUSE_ID " +
		  "AND S.XX_VMR_Category_ID = A.XX_VMR_Category_ID " + 
		  "AND S.XX_VMR_DEPARTMENT_ID = A.XX_VMR_DEPARTMENT_ID " +
		  "AND S.XX_VMR_LINE_ID = A.XX_VMR_LINE_ID) " +
		  "AND S.XX_VMR_Section_ID = A.XX_VMR_Section_ID " +
	 	  "WHERE (A.XX_INVAMOUNTFINALREAL != 0 OR A.XX_NUMREALFINALINV != 0) " +
		  "GROUP BY a.M_WareHouse_ID, a.XX_VMR_Category_ID, a.XX_VMR_Department_ID, " +
		  "a.XX_VMR_LINE_ID, a.XX_VMR_Section_ID,S.XX_VMR_PRLD01_ID";
		
		try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 matenerInventario(presupuesto, rs);
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 matenerInventario(presupuesto, rs);
		    	 }	 
			}
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * Inventario Inicial Real
	 */
	private void inventoryFirstMonth()
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		sql = " WITH S AS " +
				  "(SELECT XX_VMR_PRLD01_ID, M_WareHouse_ID, XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, " +
				  "XX_VMR_LINE_ID, XX_VMR_Section_ID FROM XX_VMR_PRLD01 b WHERE b.XX_BUDGETYEARMONTH = "+anioMes+") , " +
			  "A AS " +
				  "(SELECT M_WAREHOUSE_ID,XX_VMR_CATEGORY_ID,XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, " +
				  "XX_VMR_SECTION_ID, XX_INITIALINVENTORYQUANTITY, XX_INITIALINVENTORYAMOUNT " +
				  "FROM XX_VCN_INVENTORY " +
				  "WHERE XX_INVENTORYMONTH = "+ Mes +" AND XX_INVENTORYYEAR = "+ anio +") " +
				  
			  "SELECT a.M_WareHouse_ID, a.XX_VMR_Category_ID, a.XX_VMR_Department_ID, a.XX_VMR_LINE_ID, " +
			  "a.XX_VMR_Section_ID, nvl(S.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID, " +
			  "NVL(Sum(A.XX_INITIALINVENTORYAMOUNT), 0) InvInicialReal, NVL(sum(A.XX_INITIALINVENTORYQUANTITY), 0) CantInvInicialReal " +
			  "FROM " +
			  "A LEFT JOIN S ON ( " +
			  "S.M_WAREHOUSE_ID = A.M_WAREHOUSE_ID " +
			  "AND S.XX_VMR_Category_ID = A.XX_VMR_Category_ID " + 
			  "AND S.XX_VMR_DEPARTMENT_ID = A.XX_VMR_DEPARTMENT_ID " +
			  "AND S.XX_VMR_LINE_ID = A.XX_VMR_LINE_ID) " +
			  "AND S.XX_VMR_Section_ID = A.XX_VMR_Section_ID " +
		 	  "WHERE (A.XX_INITIALINVENTORYAMOUNT <> 0 OR A.XX_INITIALINVENTORYQUANTITY <> 0) " +
			  "GROUP BY a.M_WareHouse_ID, a.XX_VMR_Category_ID, a.XX_VMR_Department_ID, " +
			  "a.XX_VMR_LINE_ID, a.XX_VMR_Section_ID,S.XX_VMR_PRLD01_ID";
		
		try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 matenerInventario(presupuesto, rs);
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 matenerInventario(presupuesto, rs);
		    	 }	 
			}
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * Inventario Meses Futuros
	 */
	private void inventarioMesesFuturos(Integer yearMonth)
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String aux = yearMonth.toString();
		
		sql = " WITH S AS " +
			  "(SELECT XX_VMR_PRLD01_ID, M_WareHouse_ID, XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, " +
			  "XX_VMR_LINE_ID, XX_VMR_Section_ID FROM XX_VMR_PRLD01 b WHERE b.XX_BUDGETYEARMONTH = "+yearMonth+") , " +
			  "A AS " +
			  "(SELECT M_WareHouse_ID, XX_VMR_PRLD01_ID, XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, " +
			  "XX_VMR_LINE_ID, XX_VMR_Section_ID, XX_FINALINVAMOUNTPROJD, XX_NUMPROJDFINALINV " +
			  "FROM XX_VMR_PRLD01  WHERE ";
			  
			  if(aux.substring(4, 6).equals("01")){
					 
				  Integer year = new Integer(aux.substring(0, 4));
				  year = year - 1;
				  aux = year.toString() + "12";
				  
				  sql += "XX_BUDGETYEARMONTH = "+ (aux);
			  }
			  else
				  sql += "XX_BUDGETYEARMONTH = "+ (yearMonth - 1);			  
			  
			  sql += ") " +
			  
			  "SELECT a.M_WareHouse_ID, a.XX_VMR_Category_ID, a.XX_VMR_Department_ID, a.XX_VMR_LINE_ID, " +
			  "a.XX_VMR_Section_ID, nvl(S.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID, " +
			  "Sum(a.XX_FINALINVAMOUNTPROJD) InvFinalProyectado, sum(a.XX_NUMPROJDFINALINV) CantInvFinalProyectado " +
			  "FROM " +
			  "A LEFT JOIN S ON ( " +
  			  "S.M_WAREHOUSE_ID = A.M_WAREHOUSE_ID " +
			  "AND S.XX_VMR_Category_ID = A.XX_VMR_Category_ID " + 
			  "AND S.XX_VMR_DEPARTMENT_ID = A.XX_VMR_DEPARTMENT_ID " +
			  "AND S.XX_VMR_LINE_ID = A.XX_VMR_LINE_ID) " +
			  "AND S.XX_VMR_Section_ID = A.XX_VMR_Section_ID " +
		 	  "WHERE (A.XX_FINALINVAMOUNTPROJD != 0 OR A.XX_NUMPROJDFINALINV != 0) " +
			  "GROUP BY a.M_WareHouse_ID, a.XX_VMR_Category_ID, a.XX_VMR_Department_ID, " +
			  "a.XX_VMR_LINE_ID, a.XX_VMR_Section_ID,S.XX_VMR_PRLD01_ID";
		
		try{
			
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 matenerInventarioMesesFuturos(presupuesto, rs);
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  yearMonth, presupuesto);
		    		 matenerInventarioMesesFuturos(presupuesto, rs);
		    	 }	 
			}
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void rebajasFR()
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
	   sql = "with A as( " +
			 "select " +
			 "dr.M_WareHouse_ID,p.XX_VMR_Category_ID, p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_SECTION_ID, p.XX_VMR_LINE_ID, " +
			 "sum(XX_LOWERINGQUANTITY) cantidad, " +
			 "sum(round(XX_PRICEBEFOREDISCOUNT-XX_discountprice,2)*XX_LOWERINGQUANTITY) monto " +
			 "from XX_VMR_DiscountRequest dr, XX_VMR_DiscountAppliDetail drl, M_Product p " +
			 "where " +
			 "dr.XX_YEARUPDATE = "+ anio + " AND  dr.XX_MONTHUPDATE = " + Mes + " " +
			 "AND dr.XX_VMR_DiscountRequest_ID = drl.XX_VMR_DiscountRequest_ID AND p.M_Product_ID = drl.M_Product_ID AND dr.XX_Status<>'RV' AND dr.XX_Status<>'PE' AND " +
			 "(select name from XX_VMR_DISCOUNTTYPE where XX_VMR_DISCOUNTTYPE_ID=drl.XX_VMR_DISCOUNTTYPE_ID) <> 'REBAJA  A  CERO' " +
			 "group by dr.M_WAREHOUSE_ID,p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_Category_ID, p.XX_VMR_SECTION_ID, p.XX_VMR_LINE_ID" +
			 ")" +
			 "select " +
			 "A.M_WareHouse_ID, A.XX_VMR_Category_ID, A.XX_VMR_DEPARTMENT_ID, A.XX_VMR_LINE_ID, A.XX_VMR_SECTION_ID," +
			 "A.cantidad, A.monto, NVL(prl.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID " +
			 "from A left outer join XX_VMR_PRLD01 prl on ( " +
			 "A.XX_VMR_Category_ID  = prl.XX_VMR_Category_ID  AND A.XX_VMR_DEPARTMENT_ID = prl.XX_VMR_DEPARTMENT_ID " +
			 "AND A.XX_VMR_LINE_ID  = prl.XX_VMR_LINE_ID AND A.XX_VMR_SECTION_ID = prl.XX_VMR_SECTION_ID " +
			 "AND prl.XX_BUDGETYEARMONTH = "+ anioMes +" AND A.M_WAREHOUSE_ID = prl.M_WAREHOUSE_ID) " +
			 "order by A.M_WareHouse_ID";
	
	   try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 presupuesto.setXX_ACTAMOUNTSALEFR(rs.getBigDecimal("monto"));
		    		 presupuesto.setXX_AMOUNTSALEFRINTERESTS(rs.getBigDecimal("cantidad"));
		    		 //presupuesto.setXX_FINALACTAMOUNTSALE(rs.getBigDecimal("rebajasDefinitivasMonto"));
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 presupuesto.setXX_ACTAMOUNTSALEFR(rs.getBigDecimal("monto"));
		    		 presupuesto.setXX_AMOUNTSALEFRINTERESTS(rs.getBigDecimal("cantidad"));
		    		 //presupuesto.setXX_FINALACTAMOUNTSALE(rs.getBigDecimal("rebajasDefinitivasMonto"));
		    	 }	
		    	 presupuesto.save(); 
		    }
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void rebajasFRRV()
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
	   sql = "with A as( " +
			 "select " +
			 "dr.M_WareHouse_ID,p.XX_VMR_Category_ID, p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_SECTION_ID, p.XX_VMR_LINE_ID, " +
			 "sum(-XX_LOWERINGQUANTITY) cantidad, " +
			 "sum(round(XX_PRICEBEFOREDISCOUNT-XX_discountprice,2)*XX_LOWERINGQUANTITY) monto " +
			 "from XX_VMR_DiscountRequest dr, XX_VMR_DiscountAppliDetail drl, M_Product p " +
			 "where " +
			 "TO_CHAR(dr.CREATED, 'YYYYMM') = "+ anioMes + " " +
			 "AND dr.XX_VMR_DiscountRequest_ID = drl.XX_VMR_DiscountRequest_ID AND p.M_Product_ID = drl.M_Product_ID AND dr.XX_Status='RV' AND " +
			 "(select name from XX_VMR_DISCOUNTTYPE where XX_VMR_DISCOUNTTYPE_ID=drl.XX_VMR_DISCOUNTTYPE_ID) <> 'REBAJA  A  CERO' " +
			 "group by dr.M_WAREHOUSE_ID,p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_Category_ID, p.XX_VMR_SECTION_ID, p.XX_VMR_LINE_ID" +
			 ")" +
			 "select " +
			 "A.M_WareHouse_ID, A.XX_VMR_Category_ID, A.XX_VMR_DEPARTMENT_ID, A.XX_VMR_LINE_ID, A.XX_VMR_SECTION_ID," +
			 "A.cantidad, A.monto, NVL(prl.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID " +
			 "from A left outer join XX_VMR_PRLD01 prl on ( " +
			 "A.XX_VMR_Category_ID  = prl.XX_VMR_Category_ID  AND A.XX_VMR_DEPARTMENT_ID = prl.XX_VMR_DEPARTMENT_ID " +
			 "AND A.XX_VMR_LINE_ID  = prl.XX_VMR_LINE_ID AND A.XX_VMR_SECTION_ID = prl.XX_VMR_SECTION_ID " +
			 "AND prl.XX_BUDGETYEARMONTH = "+ anioMes +" AND A.M_WAREHOUSE_ID = prl.M_WAREHOUSE_ID) " +
			 "order by A.M_WareHouse_ID";
	
	   try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 presupuesto.setXX_ACTAMOUNTSALEFR(rs.getBigDecimal("monto").add(presupuesto.getXX_ACTAMOUNTSALEFR()));
		    		 presupuesto.setXX_AMOUNTSALEFRINTERESTS(rs.getBigDecimal("cantidad").add(presupuesto.getXX_AMOUNTSALEFRINTERESTS()));
		    		 //presupuesto.setXX_FINALACTAMOUNTSALE(rs.getBigDecimal("rebajasDefinitivasMonto"));
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 presupuesto.setXX_ACTAMOUNTSALEFR(rs.getBigDecimal("monto"));
		    		 presupuesto.setXX_AMOUNTSALEFRINTERESTS(rs.getBigDecimal("cantidad"));
		    		 //presupuesto.setXX_FINALACTAMOUNTSALE(rs.getBigDecimal("rebajasDefinitivasMonto"));
		    	 }	
		    	 presupuesto.save(); 
		    }
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void rebajasDEF()
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
	   sql = "with A as( " +
			 "select " +
			 "dr.M_WareHouse_ID,p.XX_VMR_Category_ID, p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_SECTION_ID, p.XX_VMR_LINE_ID," +
			 "sum(XX_LOWERINGQUANTITY) cantidad, " +
			 "sum(round(XX_PRICEBEFOREDISCOUNT-XX_discountprice,2)*XX_LOWERINGQUANTITY) monto " +
			 "from XX_VMR_DiscountRequest dr, XX_VMR_DiscountAppliDetail drl, M_Product p " +
			 "where " +
			 "dr.XX_YEARUPDATE = "+ anio + " AND  dr.XX_MONTHUPDATE = " + Mes + " " +
			 "AND dr.XX_VMR_DiscountRequest_ID = drl.XX_VMR_DiscountRequest_ID AND p.M_Product_ID = drl.M_Product_ID AND dr.XX_Status<>'RV' AND dr.XX_Status<>'PE' AND " +
			 "(select name from XX_VMR_DISCOUNTTYPE where XX_VMR_DISCOUNTTYPE_ID=drl.XX_VMR_DISCOUNTTYPE_ID) = 'REBAJA  A  CERO' " +
			 "group by dr.M_WAREHOUSE_ID,p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_Category_ID, p.XX_VMR_SECTION_ID, p.XX_VMR_LINE_ID " +
			 ")" +
			 "select " +
			 "A.M_WareHouse_ID, A.XX_VMR_Category_ID, A.XX_VMR_DEPARTMENT_ID, A.XX_VMR_LINE_ID, A.XX_VMR_SECTION_ID," +
			 "A.cantidad, A.monto, NVL(prl.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID " +
			 "from A left outer join XX_VMR_PRLD01 prl on ( " +
			 "A.XX_VMR_Category_ID  = prl.XX_VMR_Category_ID  AND A.XX_VMR_DEPARTMENT_ID = prl.XX_VMR_DEPARTMENT_ID " +
			 "AND A.XX_VMR_LINE_ID  = prl.XX_VMR_LINE_ID AND A.XX_VMR_SECTION_ID = prl.XX_VMR_SECTION_ID " +
			 "AND prl.XX_BUDGETYEARMONTH = "+ anioMes +" AND A.M_WAREHOUSE_ID = prl.M_WAREHOUSE_ID) " +
			 "order by A.M_WareHouse_ID";
	
	   try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 //presupuesto.setXX_ACTAMOUNTSALEFR(rs.getBigDecimal("rebajasFRMonto"));
		    		 presupuesto.setXX_FINALACTAMOUNTSALE(rs.getBigDecimal("monto"));
		    		 presupuesto.setXX_FINALSALEAMOUNTINTERESTS(rs.getBigDecimal("cantidad"));
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 //presupuesto.setXX_ACTAMOUNTSALEFR(rs.getBigDecimal("rebajasFRMonto"));
		    		 presupuesto.setXX_FINALACTAMOUNTSALE(rs.getBigDecimal("monto"));
		    		 presupuesto.setXX_FINALSALEAMOUNTINTERESTS(rs.getBigDecimal("cantidad"));
		    	 }	
		    	 presupuesto.save(); 
		    }
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void rebajasDEFRV()
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
	   sql = "with A as( " +
			 "select " +
			 "dr.M_WareHouse_ID,p.XX_VMR_Category_ID, p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_SECTION_ID, p.XX_VMR_LINE_ID," +
			 "sum(-XX_LOWERINGQUANTITY) cantidad, " +
			 "sum(round(XX_PRICEBEFOREDISCOUNT-XX_discountprice,2)*XX_LOWERINGQUANTITY) monto " +
			 "from XX_VMR_DiscountRequest dr, XX_VMR_DiscountAppliDetail drl, M_Product p " +
			 "where " +
			 "TO_CHAR(dr.CREATED, 'YYYYMM') = "+ anioMes + " " +
			 "AND dr.XX_VMR_DiscountRequest_ID = drl.XX_VMR_DiscountRequest_ID AND p.M_Product_ID = drl.M_Product_ID AND dr.XX_Status='RV' AND " +
			 "(select name from XX_VMR_DISCOUNTTYPE where XX_VMR_DISCOUNTTYPE_ID=drl.XX_VMR_DISCOUNTTYPE_ID) = 'REBAJA  A  CERO' " +
			 "group by dr.M_WAREHOUSE_ID,p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_Category_ID, p.XX_VMR_SECTION_ID, p.XX_VMR_LINE_ID " +
			 ")" +
			 "select " +
			 "A.M_WareHouse_ID, A.XX_VMR_Category_ID, A.XX_VMR_DEPARTMENT_ID, A.XX_VMR_LINE_ID, A.XX_VMR_SECTION_ID," +
			 "A.cantidad, A.monto, NVL(prl.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID " +
			 "from A left outer join XX_VMR_PRLD01 prl on ( " +
			 "A.XX_VMR_Category_ID  = prl.XX_VMR_Category_ID  AND A.XX_VMR_DEPARTMENT_ID = prl.XX_VMR_DEPARTMENT_ID " +
			 "AND A.XX_VMR_LINE_ID  = prl.XX_VMR_LINE_ID AND A.XX_VMR_SECTION_ID = prl.XX_VMR_SECTION_ID " +
			 "AND prl.XX_BUDGETYEARMONTH = "+ anioMes +" AND A.M_WAREHOUSE_ID = prl.M_WAREHOUSE_ID) " +
			 "order by A.M_WareHouse_ID";
	
	   try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 //presupuesto.setXX_ACTAMOUNTSALEFR(rs.getBigDecimal("rebajasFRMonto"));
		    		 presupuesto.setXX_FINALACTAMOUNTSALE(rs.getBigDecimal("monto").add(presupuesto.getXX_FINALACTAMOUNTSALE()));
		    		 presupuesto.setXX_FINALSALEAMOUNTINTERESTS(rs.getBigDecimal("cantidad").add(presupuesto.getXX_FINALSALEAMOUNTINTERESTS()));
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 //presupuesto.setXX_ACTAMOUNTSALEFR(rs.getBigDecimal("rebajasFRMonto"));
		    		 presupuesto.setXX_FINALACTAMOUNTSALE(rs.getBigDecimal("monto"));
		    		 presupuesto.setXX_FINALSALEAMOUNTINTERESTS(rs.getBigDecimal("cantidad"));
		    	 }	
		    	 presupuesto.save(); 
		    }
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	   
	}
	
	private void llenarBasicos(int warehouse, int category, int deparment, int line, int section, int anioMes, X_XX_VMR_Prld01 presupuesto)
	{
		 presupuesto.setM_Warehouse_ID(warehouse);
		 presupuesto.setXX_VMR_Category_ID(category);
		 presupuesto.setXX_VMR_Department_ID(deparment);
		 presupuesto.setXX_VMR_Line_ID(line);
		 presupuesto.setXX_VMR_Section_ID(section);
		 presupuesto.setXX_BUDGETYEARMONTH(anioMes);
	}
	
	private void mantenerCompras(X_XX_VMR_Prld01 presupuesto, ResultSet rs) throws Exception
	{
		 presupuesto.setXX_NUMNACSHOPPINGPLACED(rs.getBigDecimal("CantidadNacional"));
		 presupuesto.setXX_AMOUNTPLACEDNACCAMPRA(rs.getBigDecimal("MontoNacionalCosto"));
		 presupuesto.setXX_AMOUNTPLACEDNACPURCHCOST(rs.getBigDecimal("MontoNacionalPVP"));
		 
		 presupuesto.setXX_PURCHQUANTIMPDPLACED(rs.getBigDecimal("CantidadImportada"));
		 presupuesto.setXX_PURCHAMOUNTPLACEDCOSTIMP(rs.getBigDecimal("MontoImportadaPVP"));
		 presupuesto.setXX_PURCHAMOUNTPLACEDIMPD(rs.getBigDecimal("MontoImportadaCosto"));
		 
		 presupuesto.setXX_PURCHQUANTPLACEDMONTHS(rs.getBigDecimal("CantidadMesesAnt"));
		 presupuesto.setXX_PURCHAMOUNTPLADPASTMONTHS(rs.getBigDecimal("MontoMesesAntPVP"));
		 //presupuesto.setXX_PURCHAMOUNTPLADPASTMONTHS(rs.getBigDecimal("MontoMesesAntCosto"));
		 
		 // Falta set de Costo Meses Anteriores
		// presupuesto.setXX_PURCHAMOUNTPASTMONTHSPVP(rs.getBigDecimal("MontoMesesAntPVP"));
		 presupuesto.save();
		 commit();
	}
	
	private void mantenerChequeo(X_XX_VMR_Prld01 presupuesto, ResultSet rs) throws Exception
	{
		 
			 presupuesto.setXX_QUANTPURCHNAC(rs.getBigDecimal("CantidadNacional"));
    		 presupuesto.setXX_NACPURCHAMOUNTRECEIVED(rs.getBigDecimal("MontoNacionalPVP"));
    		 
    		 presupuesto.setXX_QUANTPURCHAMOUNTSREV(rs.getBigDecimal("CantidadImportada"));
    		 presupuesto.setXX_PURCHAMOUNTREVIMPD(rs.getBigDecimal("MontoImportadaPVP"));
    		 
    		 presupuesto.setXX_NUMMONTHSREDSHOP(rs.getBigDecimal("CantidadMesesAnt"));
    		 presupuesto.setXX_PURCHAMOUNTREDPASTMONTHS(rs.getBigDecimal("MontoMesesAntPVP"));
    		
    		 //falta los Set de los Montos al Costo
    		 //presupuesto.setXX_NACPURCHAMOUNTRECEIVEDCOST(rs.getBigDecimal("MontoNacionalCosto"));
    		 //presupuesto.setXX_PURCHAMOUNTREVIMPDCOST(rs.getBigDecimal("MontoImportadaCosto"));
    		 //presupuesto.setXX_PURCHAMOUNTPASTMONTHSCOST(rs.getBigDecimal("MontoMesesAntCosto"));
    		 presupuesto.save();

	}
	
	private void mantenerRecepcion(X_XX_VMR_Prld01 presupuesto, ResultSet rs) throws Exception
	{
		 
			 presupuesto.setXX_ReceiptPVP(rs.getBigDecimal("RecepcionPVP"));
			 presupuesto.setXX_ReceiptQty(rs.getInt("RecepcionQty"));
    		
    		 presupuesto.save();
	}
	
	private void mantenerVentas(X_XX_VMR_Prld01 presupuesto, ResultSet rs) throws Exception
	{
		 
			 presupuesto.setXX_QUANTACTUALSALE(rs.getBigDecimal("CantidadVenta"));
    		 presupuesto.setXX_AMOUNTACTUALSALE(rs.getBigDecimal("MontoVentaPVP")); 
    		 presupuesto.setXX_AMOUNTSALECOST(rs.getBigDecimal("MontoVentaCosto"));
    		 
    		 //presupuesto.setXX_NUMREALFINALINV(rs.getBigDecimal("MontoPromocion")); //Monto de Rebajas Promocional Reales
    		 presupuesto.setXX_ACTAMOUNTSALEPROM(rs.getBigDecimal("MontoPromocion")); //Monto de Rebajas Promocional Reales
    		 presupuesto.setXX_AMOUNTSALEPROMINTERESTS(rs.getBigDecimal("CantPromocion"));//Cantidad de Rebajas Promocional Reales
    		 presupuesto.setXX_BYWINMARGPERTBUD(rs.getBigDecimal("PorcentajeDescuento"));//Porcentaje de Rebajas Promocional Reales
    		
    		 presupuesto.save();
	}
	
	// Insercion o Actualizacion Monto y Cantidad de Inventario Final Proyectado, y % de Cobertura Real
	private void mantenerInvProy(X_XX_VMR_Prld01 presupuesto, ResultSet rs) throws Exception
	{
		 presupuesto.setXX_FINALINVAMOUNTPROJD(rs.getBigDecimal("InvFinalProyectado"));
    	 presupuesto.setXX_NUMPROJDFINALINV(rs.getBigDecimal("CantInvFinalProyectado"));
    	 presupuesto.setXX_REALPERCCOVERAGE(rs.getBigDecimal("CoberturaReal"));
    		
    	 presupuesto.save();
	}
	
	private void matenerMargenRotacion(X_XX_VMR_Prld01 presupuesto, ResultSet rs) throws Exception
	{
			presupuesto.setXX_ROTATIONREAL(rs.getBigDecimal("RotacionReal"));
			presupuesto.setXX_MARGACCORDINGBUYREAL(rs.getBigDecimal("MargenCompraReal"));
			presupuesto.setXX_LISCKGROSSMARGPERTREAL(rs.getBigDecimal("MargenBrutoGanado"));
			presupuesto.setXX_PERTWINGMARGREAL(rs.getBigDecimal("MargenPorGanar"));
		
			presupuesto.save();
	}
	
	private void matenerLimite(X_XX_VMR_Prld01 presupuesto, ResultSet rs) throws Exception
	{
		    presupuesto.setXX_AMOUNTPURCHASELIMIT(rs.getBigDecimal("MontoLimiteCompra"));
			presupuesto.setXX_QUANTITYPURCHLIMIT(rs.getBigDecimal("CantidadLimiteCompra"));
		
			presupuesto.save();
	}
	
	private void matenerInventario(X_XX_VMR_Prld01 presupuesto, ResultSet rs) throws Exception
	{
		
		presupuesto.setXX_AMOUNTINIINVEREAL(rs.getBigDecimal("InvInicialReal"));
		presupuesto.setXX_NUMINIINVEREAL(rs.getBigDecimal("CantInvInicialReal"));
		
		presupuesto.save();
	}
	
	private void matenerInventarioMesesFuturos(X_XX_VMR_Prld01 presupuesto, ResultSet rs) throws Exception
	{
		
		presupuesto.setXX_AMOUNTINIINVEREAL(rs.getBigDecimal("InvFinalProyectado"));
		
		presupuesto.setXX_NUMINIINVEREAL(rs.getBigDecimal("CantInvFinalProyectado"));
		
		presupuesto.setXX_INVAMOUNTFINALREAL(rs.getBigDecimal("InvFinalProyectado"));
		
		presupuesto.save();
		
	}
	
	private Vector<Integer> monthForwad(){
		
		Vector<Integer> count = new Vector<Integer>();
		
		String sql = "select xx_budgetyearmonth from xx_vmr_prld01 " +
					 "where xx_budgetyearmonth > " + anioMes + " " +
					 "group by xx_budgetyearmonth " +
					 "order by xx_budgetyearmonth";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			
			while(rs.next()){
				count.add(rs.getInt(1));
			}
				
		}catch(Exception e){
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return count;
	}
	
	private void inventarioACeroMesFuturo(){
		
		String sql = "update xx_vmr_prld01 set XX_AMOUNTINIINVEREAL = 0, XX_NUMINIINVEREAL = 0, XX_INVAMOUNTFINALREAL = 0 " +
		             "where xx_budgetyearmonth > " + anioMes;
		
		try{
			DB.executeUpdate(null, sql);

		}catch(Exception e){
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
		}finally{
			try{
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void pedidosEnv()
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
	   sql ="WITH A as " +
		    "(SELECT Z.M_WAREHOUSE_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID, " +
				"SUM(od.XX_SALEPRICE*OD.XX_PRODUCTQUANTITY) monto, sum(OD.XX_PRODUCTQUANTITY) cantidad, " +
				"(sum(od.XX_SalePrice*od.XX_ProductQuantity) - sum(asi.XX_SalePrice*od.XX_ProductQuantity)) as diferencia " +
				"FROM XX_VMR_ORDER O, XX_VMR_ORDERREQUESTDETAIL OD, M_PRODUCT P, " +
				"(select O.XX_VMR_ORDER_ID, D.XX_DEPARTUREWAREHOUSE_ID M_WAREHOUSE_ID from XX_VMR_ORDER O, XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D " +
	                        "where DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND " +
	                        "DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID " +
	                        "AND nvl(TO_CHAR(O.XX_DATESTATUSONSTORE,'YYYYMM'),TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM')) = "+ anioMes 
	                        +" AND O.XX_ORDERREQUESTSTATUS = 'TI' " +
	                        "AND DD.XX_TYPEDETAILDISPATCHGUIDE = 'MCD' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' " +
	                        "group by O.XX_VMR_ORDER_ID, XX_DEPARTUREWAREHOUSE_ID) Z, M_ATTRIBUTESETINSTANCE ASI " +
				"WHERE O.XX_VMR_ORDER_ID=OD.XX_VMR_ORDER_ID AND Z.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID " +
				"AND OD.M_PRODUCT_ID = P.M_PRODUCT_ID AND NVL(OD.XX_PRODUCTBATCH_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) " +
				"group by Z.M_WAREHOUSE_ID, p.XX_VMR_CATEGORY_ID, p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID) " +
			"Select " +
			"A.M_WAREHOUSE_ID, A.XX_VMR_Category_ID, A.XX_VMR_DEPARTMENT_ID, A.XX_VMR_LINE_ID, A.XX_VMR_SECTION_ID, " +
			"cantidad, monto, NVL(PRLD.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID, diferencia " +
			"FROM A left outer join XX_VMR_PRLD01 PRLD " +
			"on (A.XX_VMR_Category_ID = PRLD.XX_VMR_Category_ID AND A.XX_VMR_LINE_ID = PRLD.XX_VMR_LINE_ID AND A.XX_VMR_DEPARTMENT_ID = PRLD.XX_VMR_DEPARTMENT_ID " +
			"AND A.XX_VMR_SECTION_ID = PRLD.XX_VMR_SECTION_ID AND XX_BUDGETYEARMONTH = " +anioMes+ " and PRLD.M_WAREHOUSE_ID = A.M_WAREHOUSE_ID)";

	   try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 presupuesto.setXX_NUMTRANSFSENT(rs.getBigDecimal("cantidad"));
		    		 presupuesto.setXX_TRANSFAMOUNTSENT(rs.getBigDecimal("monto")); 
		    		 presupuesto.setXX_PlacedOrderPVPAdjustment(rs.getBigDecimal("diferencia"));
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 presupuesto.setXX_NUMTRANSFSENT(rs.getBigDecimal("cantidad"));
		    		 presupuesto.setXX_TRANSFAMOUNTSENT(rs.getBigDecimal("monto"));
		    		 presupuesto.setXX_PlacedOrderPVPAdjustment(rs.getBigDecimal("diferencia"));
		    	 }
		    	 presupuesto.save(); 
		    }
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	   
	}
	
	private void pedidosRec()
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
	   sql = "WITH A as " +
			   "(SELECT Z.M_WAREHOUSE_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID, " +
				"SUM(od.XX_SALEPRICE*OD.XX_PRODUCTQUANTITY) monto, sum(OD.XX_PRODUCTQUANTITY) cantidad, " +
				"(sum(od.XX_SalePrice*od.XX_ProductQuantity) - sum(asi.XX_SalePrice*od.XX_ProductQuantity)) as diferencia " +
				"FROM XX_VMR_ORDER O, XX_VMR_ORDERREQUESTDETAIL OD, M_PRODUCT P, " +
				   "(select O.XX_VMR_ORDER_ID, D.XX_ARRIVALWAREHOUSE_ID M_WAREHOUSE_ID " +
				   "from XX_VMR_ORDER O, XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D " +
				   "where DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND " +
				   "DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID " +
				   "AND nvl(TO_CHAR(O.XX_DATESTATUSONSTORE,'YYYYMM'),TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM')) = " + anioMes + " " +
				   "AND O.XX_ORDERREQUESTSTATUS = 'TI' " +
				   "AND DD.XX_TYPEDETAILDISPATCHGUIDE = 'MCD' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' " +
				   "group by O.XX_VMR_ORDER_ID, XX_ARRIVALWAREHOUSE_ID) Z, M_ATTRIBUTESETINSTANCE ASI " +
				   "WHERE O.XX_VMR_ORDER_ID=OD.XX_VMR_ORDER_ID AND Z.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID " +
				"AND OD.M_PRODUCT_ID = P.M_PRODUCT_ID AND NVL(OD.XX_PRODUCTBATCH_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) " +
				"group by Z.M_WAREHOUSE_ID, p.XX_VMR_CATEGORY_ID, p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID) " +
			 
			 "Select " +
			 "A.M_WAREHOUSE_ID, A.XX_VMR_Category_ID, A.XX_VMR_Department_ID, A.XX_VMR_LINE_ID, A.XX_VMR_SECTION_ID, "+
			 "NVL(PRLD.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID, cantidad, monto " +
			 "FROM A left outer join XX_VMR_PRLD01 PRLD on (A.XX_VMR_Category_ID = PRLD.XX_VMR_Category_ID AND A.XX_VMR_LINE_ID = PRLD.XX_VMR_LINE_ID " +
			 "AND A.XX_VMR_DEPARTMENT_ID = PRLD.XX_VMR_DEPARTMENT_ID " +
			 "AND A.XX_VMR_SECTION_ID = PRLD.XX_VMR_SECTION_ID "+
			 "AND XX_BUDGETYEARMONTH = " + anioMes + " and PRLD.M_WAREHOUSE_ID = A.M_WAREHOUSE_ID)";
		   
	   try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 presupuesto.setXX_NUMBTRANSFREV(rs.getBigDecimal("cantidad"));
		    		 presupuesto.setXX_TRANSFAMOUNTRECEIVED(rs.getBigDecimal("monto"));  
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 presupuesto.setXX_NUMBTRANSFREV(rs.getBigDecimal("cantidad"));
		    		 presupuesto.setXX_TRANSFAMOUNTRECEIVED(rs.getBigDecimal("monto")); 
		    	 }
		    	 presupuesto.save(); 
		    }
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	   
	}
	
	private void deleteRealValues(){
		
		String sql = "update xx_vmr_prld01 set " +
					 //Inventario Inicial
					 "XX_AMOUNTINIINVEREAL = 0," +
					 "XX_NUMINIINVEREAL = 0," +
					 //Inventario Final
					 "XX_INVAMOUNTFINALREAL = 0," +
					 "XX_NUMREALFINALINV = 0," +
					 //Compras
					 "XX_AMOUNTPLACEDNACPURCHCOST = 0," +
					 "XX_NUMNACSHOPPINGPLACED = 0," +
					 "XX_AMOUNTPLACEDNACCAMPRA = 0," +
					 "XX_PURCHQUANTIMPDPLACED = 0," +
					 "XX_PURCHAMOUNTPLACEDCOSTIMP = 0," +
					 "XX_PURCHAMOUNTPLACEDIMPD = 0," +
					 "XX_PURCHQUANTPLACEDMONTHS = 0," +
					 "XX_PURCHAMOUNTPLADPASTMONTHS = 0," +
					 //Recepciones
					 "XX_ReceiptPVP = 0," +
					 "XX_ReceiptQty = 0," +
					 //Chequeos
					 "XX_QUANTPURCHNAC = 0," +
					 "XX_NACPURCHAMOUNTRECEIVED = 0," +
					 "XX_PURCHAMOUNTREVIMPD = 0," +
					 "XX_NUMMONTHSREDSHOP = 0," +
					 "XX_PURCHAMOUNTREDPASTMONTHS = 0," +
					 //Diferencia PVP
					 "XX_PlacedOrderPVPAdjustment = 0," +
					 //Ventas
					 "XX_QUANTACTUALSALE = 0," +
					 "XX_AMOUNTACTUALSALE = 0," +
					 "XX_AMOUNTSALECOST = 0," +
					 "XX_QUANTPURCHAMOUNTSREV = 0," +
					 "XX_BYWINMARGPERTBUD = 0," +
					 //Traspasos
					 "XX_NUMTRANSFSENT = 0," +
					 "XX_NUMBTRANSFREV = 0," +
					 "XX_TRANSFAMOUNTRECEIVED = 0," +
					 "XX_TRANSFAMOUNTSENT = 0," +
					 //REBAJAS
					 "XX_ACTAMOUNTSALEFR = 0," +
					 "XX_FINALACTAMOUNTSALE = 0," +
					 "XX_ACTAMOUNTSALEPROM = 0," +
					 "XX_AMOUNTSALEPROMINTERESTS = 0,"+
					 //Devoluciones
					 "XX_RETURNSPVP = 0," +
					 "XX_RETURNSQTY = 0," +
					 //
					 "XX_FINALINVAMOUNTPROJD = 0," +
					 "XX_NUMPROJDFINALINV = 0," +
					 "XX_REALPERCCOVERAGE = 0," +
					 //Rotacion
					 "XX_AMOUNTPURCHASELIMIT = 0," +
					 "XX_QUANTITYPURCHLIMIT = 0," +
					 "XX_ROTATIONREAL = 0," +
					 "XX_MARGACCORDINGBUYREAL = 0," +
					 "XX_LISCKGROSSMARGPERTREAL = 0," +
					 "XX_PERTWINGMARGREAL = 0 " +

		             "WHERE xx_budgetyearmonth >= " + anioMes ;
		
		try{
			DB.executeUpdate(null, sql);
			commit();

		}catch(Exception e){
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
		}finally{
			try{
	
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void InvFinalReal(){
		
		String sql = "update xx_vmr_prld01 set " +
		 
		//Montos
		 "XX_INVAMOUNTFINALREAL = (" +
		 		"XX_AMOUNTINIINVEREAL + " +
		 		"(XX_NACPURCHAMOUNTRECEIVED + XX_PURCHAMOUNTREVIMPD + XX_PURCHAMOUNTREDPASTMONTHS) + XX_PlacedOrderPVPAdjustment " +
		 		"- XX_RETURNSPVP " +
		 		"- XX_AMOUNTACTUALSALE " +
		 		"- XX_TRANSFAMOUNTSENT + XX_TRANSFAMOUNTRECEIVED" +
		 		"- (XX_ACTAMOUNTSALEPROM + XX_ACTAMOUNTSALEFR + XX_FINALACTAMOUNTSALE)" +
		 		"), " +
		 //Cantidades
		 "XX_NUMREALFINALINV = (" +
		 		"XX_NUMINIINVEREAL + " +
		 		"(XX_QUANTPURCHNAC+XX_QUANTPURCHAMOUNTSREV+XX_NUMMONTHSREDSHOP) " +
		 		"- XX_RETURNSQTY " +
		 		"- XX_QUANTACTUALSALE " +
		 		"- XX_NUMTRANSFSENT + XX_NUMBTRANSFREV " +
		 		"- ( XX_FINALSALEAMOUNTINTERESTS)" +
		 		") " +
        
		 "WHERE xx_budgetyearmonth = " + anioMes;
		
		try{
			DB.executeUpdate(null, sql);
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
		}finally{
			try{
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	// Está Funcion Extrae de las Recepciones El Costo, PVP y Cantidad de O/C  Nacionales e Internacionales  por Año, Mes, Cat, Dep, Lin, Sec, Marca
	// No se estan seteando los campos al Costo
	private void recibidas()
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql;
		int numPresupuesto;
		
		sql = "SELECT o.M_WareHouse_ID, o.XX_VMR_Category_ID, o.XX_VMR_Department_ID, rf.XX_VMR_LINE_ID, rf.XX_VMR_SECTION_ID, " +
			 "ROUND(sum(case when TO_CHAR(o.XX_RECEPTIONDATE,'YYYYMM') = "+anioMes+" and " +
			 "TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') <= "+anioMes+" then rf.XX_SalePrice*lr.MovementQty else 0 end), 2) as RecepcionPVP, " +
			 "ROUND(sum(case when TO_CHAR(o.XX_RECEPTIONDATE,'YYYYMM') = "+anioMes+" and " +
			 "TO_CHAR(o.XX_ESTIMATEDDATE,'YYYYMM') <= "+anioMes+" then lr.MovementQty else 0 end), 2) as RecepcionQty, " +
			 "decode(prl.XX_VMR_PRLD01_ID,NULL,0,prl.XX_VMR_PRLD01_ID) XX_VMR_PRLD01_ID "+
			  "from  C_Order o join XX_VMR_PO_LineRefProv rf on (o.C_Order_ID = rf.C_Order_ID) " +
			    "left outer join XX_VMR_PRLD01 prl on "+
			    "(o.XX_VMR_DEPARTMENT_ID = prl.XX_VMR_DEPARTMENT_ID and rf.XX_VMR_LINE_ID = prl.XX_VMR_LINE_ID "+
				"and rf.XX_VMR_SECTION_ID = prl.XX_VMR_SECTION_ID " +
				"and prl.XX_BUDGETYEARMONTH = " + anioMes + " " +
				"and o.M_WareHouse_ID = prl.M_WareHouse_ID and o.XX_VMR_Category_ID = prl.XX_VMR_Category_ID) "+
				"join C_OrderLine ol on (rf.XX_VMR_PO_LineRefProv_ID =ol.XX_VMR_PO_LineRefProv_ID and o.C_Order_ID=ol.C_Order_ID) " +
				"join M_InOutLine lr on (ol.C_OrderLine_ID = lr.C_OrderLine_ID) " +
			 "where o.ISSOTRX = 'N' and (o.XX_ORDERSTATUS = 'RE') " +
			 	   "and TO_CHAR(o.XX_RECEPTIONDATE,'YYYYMM') = " + anioMes + " " +
			 "group by  o.M_WareHouse_ID, o.XX_VMR_Category_ID, o.XX_VMR_Department_ID, rf.XX_VMR_LINE_ID, rf.XX_VMR_SECTION_ID, " +
			 "prl.XX_VMR_PRLD01_ID";
		try{
			
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
		    {
		    	//se Verifica en la tabla de Presupuesto si existe el registro
		    	 numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		//Actualizamos los Campos en el registro ya existente
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 mantenerRecepcion(presupuesto, rs);
		    	 }
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 mantenerRecepcion(presupuesto, rs);
		    	 }
		    }
			
			commit();
			
		}catch (Exception e) {
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
			rs.close();
			ps.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	//Devoluciones de Proveedor
	private void devoluciones()
	{
		int numPresupuesto;
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		sql = "WITH A as ( " +
			"SELECT TO_CHAR(A.CREATED,'YYYYMM') fecha, M_WAREHOUSE_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID, " +
			"sum(ml.XX_SALEPRICE*ml.XX_QuantityReceived) monto, sum(ml.XX_QuantityReceived) cantidad " +
			"FROM XX_VLO_ReturnOfProduct A, M_Movement M, M_MovementLine ML, M_PRODUCT P " +
			"WHERE " +
			"A.M_Movement_ID = M.M_Movement_ID AND M.M_Movement_ID = ML.M_Movement_ID AND ML.M_PRODUCT_ID = P.M_PRODUCT_ID " +
			"AND TO_CHAR(A.CREATED,'YYYYMM') = " + anioMes + " " +
			"group by TO_CHAR(A.CREATED,'YYYYMM'), M_WAREHOUSE_ID, p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID" +
			")" +
			"select " +
			"A.M_WAREHOUSE_ID, A.XX_VMR_Category_ID, A.XX_VMR_Department_ID, A.XX_VMR_LINE_ID, A.XX_VMR_SECTION_ID," + 
			"monto, cantidad, NVL(PRLD.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID " +
			"from A left outer join XX_VMR_PRLD01 PRLD " +
			"on (PRLD.M_WAREHOUSE_ID = A.M_WAREHOUSE_ID AND A.XX_VMR_Category_ID = PRLD.XX_VMR_Category_ID " +
			"AND A.XX_VMR_DEPARTMENT_ID = PRLD.XX_VMR_DEPARTMENT_ID AND A.XX_VMR_LINE_ID = PRLD.XX_VMR_LINE_ID " +
			"AND A.XX_VMR_SECTION_ID = PRLD.XX_VMR_SECTION_ID AND XX_BUDGETYEARMONTH = fecha)" ;
		
		try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 presupuesto.setXX_ReturnsPVP(rs.getBigDecimal("monto"));
		    		 presupuesto.setXX_ReturnsQty(rs.getInt("cantidad"));
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 presupuesto.setXX_ReturnsPVP(rs.getBigDecimal("monto"));
		    		 presupuesto.setXX_ReturnsQty(rs.getInt("cantidad"));
		    	 }
		    	 
		    	 presupuesto.save(); 
			}
			
			commit();
			
		}catch (Exception e){
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	private void difPrecioChequeadasIMP()
	{
		String sql = null;
		int numPresupuesto;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Calendar cal = Calendar.getInstance();
		int anioMes = (cal.get(Calendar.YEAR)*100)+cal.get(Calendar.MONTH)+1;
		
	   sql = "SELECT " +
	         "1000053, NVL(PRLD.XX_VMR_PRLD01_ID,0) XX_VMR_PRLD01_ID, " +
	         "p.XX_VMR_Category_ID, p.XX_VMR_Department_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID, " +
		     "SUM(ROUND(ASI.XX_SALEPRICE,2)*XX_PRODUCTQUANTITY) - SUM(OD.XX_SALEPRICE*XX_PRODUCTQUANTITY) diferencia " +
		     "FROM XX_VMR_ORDER O, XX_VMR_ORDERREQUESTDETAIL OD, XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D, M_PRODUCT P " +
		     ", M_ATTRIBUTESETINSTANCE ASI, XX_VMR_PRICECONSECUTIVE PC, XX_VMR_PRLD01 PRLD " +
		     "WHERE " + 
		     "p.XX_VMR_Category_ID = PRLD.XX_VMR_Category_ID AND p.XX_VMR_LINE_ID = PRLD.XX_VMR_LINE_ID AND p.XX_VMR_DEPARTMENT_ID = PRLD.XX_VMR_DEPARTMENT_ID " +
	         "AND p.XX_VMR_SECTION_ID = PRLD.XX_VMR_SECTION_ID AND XX_BUDGETYEARMONTH = " + anioMes + " and PRLD.M_WAREHOUSE_ID = 1000053 " +
		     "AND P.C_COUNTRY_ID <> 339 AND " +
		     "OD.M_PRODUCT_ID = PC.M_PRODUCT_ID AND OD.XX_PRICECONSECUTIVE = PC.XX_PRICECONSECUTIVE " +
		     "AND ASI.M_ATTRIBUTESETINSTANCE_ID = PC.M_ATTRIBUTESETINSTANCE_ID AND PC.XX_CONSECUTIVEORIGIN = 'P' " +
	         "AND ASI.M_ATTRIBUTESETINSTANCE_ID = " +
			 "(SELECT MAX(ASI.M_ATTRIBUTESETINSTANCE_ID) FROM M_ATTRIBUTESETINSTANCE ASI, XX_VMR_PRICECONSECUTIVE PC " +
			 "WHERE OD.M_PRODUCT_ID = PC.M_PRODUCT_ID AND OD.XX_PRICECONSECUTIVE = PC.XX_PRICECONSECUTIVE  " +
			 "AND ASI.M_ATTRIBUTESETINSTANCE_ID = PC.M_ATTRIBUTESETINSTANCE_ID AND PC.XX_CONSECUTIVEORIGIN = 'P') " +
	         "AND O.XX_VMR_ORDER_ID=OD.XX_VMR_ORDER_ID AND DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID " +
	         "AND OD.M_PRODUCT_ID = P.M_PRODUCT_ID AND DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID " +
	         "AND TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM') = XX_BUDGETYEARMONTH " +
	         "AND XX_DISPATCHGUIDESTATUS <> 'SUG' " +
	         "AND (XX_ORDERREQUESTSTATUS = 'TI') AND XX_TYPEDETAILDISPATCHGUIDE = 'MCD' " +
	         "group by TO_CHAR(D.XX_DISPATCHDATE,'YYYYMM'), p.XX_VMR_CATEGORY_ID, p.XX_VMR_DEPARTMENT_ID, p.XX_VMR_LINE_ID, p.XX_VMR_SECTION_ID, PRLD.XX_VMR_PRLD01_ID";
		   
	   try{
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				numPresupuesto = rs.getInt("XX_VMR_PRLD01_ID");//existe(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes);
		    	 if(numPresupuesto > 0)
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), numPresupuesto, get_TrxName());
		    		 presupuesto.setXX_PURCHAMOUNTREVIMPD(rs.getBigDecimal("diferencia").add(presupuesto.getXX_PURCHAMOUNTREVIMPD()));
		    	 } 
		    	 else
		    	 {
		    		 presupuesto = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
		    		 llenarBasicos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),  anioMes, presupuesto);
		    		 presupuesto.setXX_PURCHAMOUNTREVIMPD(rs.getBigDecimal("diferencia"));
	
		    	 }
		    	 presupuesto.save(); 
		    }
			
			commit();
			
		}catch(Exception e)
		{
			log.log(Level.SEVERE, "Unknown Parameter: " + e.getMessage()); 
			rollback();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
