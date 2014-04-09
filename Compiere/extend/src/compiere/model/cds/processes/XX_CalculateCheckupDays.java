package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;

import compiere.model.cds.MOrder;
import compiere.model.cds.X_XX_VLO_CheckUpDays;


public class XX_CalculateCheckupDays extends SvrProcess {
	
	@Override
	protected String doIt() throws Exception {
		
		X_XX_VLO_CheckUpDays aux = new X_XX_VLO_CheckUpDays(getCtx(), getRecord_ID(), null);
		
		String sql1 = "";
		String sql2 = "";
		BigDecimal resultadoFinal = new BigDecimal(0);
		BigDecimal resultado1 = new BigDecimal(0);
		BigDecimal resultado2 = new BigDecimal(0);
		
		// Diferencia de fechas del estado del pedido
		String dateStat2 = "xx_datestatusonstore";
		String dateStat1= "created";		
		String status = aux.getXX_OrderRequestStatus();	
		if (status != null ) {
			if (status.equals("ET")) {
				dateStat2 = "xx_datestatustransit";
				dateStat1 = "xx_datestatusatbay";
			} else if (status.equals("EB")) {
				dateStat2 = "xx_datestatusatbay";
				dateStat1 = "xx_datestatuspending";
			} else if (status.equals("PE")) {
				dateStat2 = "xx_datestatuspending";
				dateStat1 = "created";
			} else if (status.equals("TI")) {
				dateStat2 = "xx_datestatusonstore";
				dateStat1 = "xx_datestatustransit";
			}
		}
		
		//Diferencias de fechas del estado de la Orden de Compra
		String dateOCStat2 = "xx_checkupdate";
		String dateOCStat1= "xx_receptiondate";		
		String OCstatus = aux.getXX_OrderStatus();
		if (OCstatus != null ) {
			if (OCstatus.equals("RE")) {
				dateOCStat2 = "xx_receptiondate";
				dateOCStat1 = "XX_ENTRANCEDATE";
			} 
		}
		
		if (aux.getC_Order_ID()>0)
		{		
			System.out.println("Por orden de compra de la orden");
			ADialog.info(1, new Container(), "XX_DaysPerPuchaseOrder");
			
			sql1 = "select " +
				 "round((select sum(xx_lineqty) from xx_vmr_po_linerefprov where c_order_id=o.c_order_id)*(coalesce(xx_checkupdate,sysdate)-xx_receptiondate)/(select sum(xx_lineqty) from xx_vmr_po_linerefprov where c_order_id=o.c_order_id),3) " +
				 "from c_order o " +
				 "where c_order_id=" + aux.getC_Order_ID();

			PreparedStatement prst1 = DB.prepareStatement(sql1,null);

			try {
				ResultSet rs = prst1.executeQuery();
				if (rs.next()){
					resultadoFinal = rs.getBigDecimal(1);									
				}
				rs.close();
				prst1.close();
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por orden de compra de la orden" + e);			
			}
			
		} else if (aux.getXX_OrderStatus()==null & aux.getXX_VMR_Collection_ID()>0 & aux.getXX_VMR_Package_ID()==0 & aux.getXX_ReceptionDate()==null)
		{		
			System.out.println("Por coleccion de la orden");
			ADialog.info(1, new Container(), "XX_DaysPerCollectionPurchaseOrder");
			sql1 = "select " +
				" sum((select sum(xx_lineqty) from xx_vmr_po_linerefprov where c_order_id=o.c_order_id)*(coalesce(xx_checkupdate,sysdate)-xx_receptiondate)) " +
				" from c_order o " +
				" where xx_orderstatus in ('RE','CH') and xx_vmr_collection_id=" + aux.getXX_VMR_Collection_ID();	
			
			sql2 = "select sum(xx_lineqty) from xx_vmr_po_linerefprov " +
					"where c_order_id in (select c_order_id from c_order where xx_orderstatus in ('RE','CH') and xx_vmr_collection_id=" + aux.getXX_VMR_Collection_ID() + ")";

			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por coleccion de la orden" + e);			
			}
			
		} else if (aux.getXX_OrderStatus()!=null & aux.getXX_VMR_Collection_ID()>0 & aux.getXX_VMR_Package_ID()==0 & aux.getXX_ReceptionDate()==null)
		{		
			System.out.println("Por coleccion y estado de la orden ");
			ADialog.info(1, new Container(), "XX_DaysPerCollectionPurchaseOrder");
			sql1 = "select " +
				" sum((select sum(xx_lineqty) from xx_vmr_po_linerefprov where c_order_id=o.c_order_id)*"+
                //"(coalesce(xx_checkupdate,sysdate)-xx_receptiondate)) " +
				"(case "+dateOCStat2+" when null then 0 else "+dateOCStat2+"-"+dateOCStat1+" end))" +
                //"(coalesce("+dateOCStat2+",sysdate)-"+dateOCStat1+")) " +
				" from c_order o " +
				" where " +
				//" xx_orderstatus ='" + aux.getXX_OrderStatus() + "' and " +
				" xx_vmr_collection_id=" + aux.getXX_VMR_Collection_ID();	
			
			sql2 = "select sum(xx_lineqty) from xx_vmr_po_linerefprov " +
					"where c_order_id in (select c_order_id from c_order where " + dateOCStat2 + " is not null "+ //"xx_orderstatus ='" + aux.getXX_OrderStatus() + "' 
					" and xx_vmr_collection_id=" + aux.getXX_VMR_Collection_ID() + ")";

			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por coleccion de la orden" + e);			
			}
			
		} else if (aux.getXX_OrderStatus()==null & aux.getXX_VMR_Package_ID()>0 & aux.getXX_ReceptionDate()==null)
		{		
			System.out.println("Por paquete de la orden");
			ADialog.info(1, new Container(), "XX_DaysPerPackagePuchaseOrder");
			sql1 = "select " +
			" sum((select sum(xx_lineqty) from xx_vmr_po_linerefprov where c_order_id=o.c_order_id)*(coalesce(xx_checkupdate,sysdate)-xx_receptiondate)) " +
			" from c_order o " +
			" where xx_orderstatus in ('RE','CH') and xx_vmr_package_id=" + aux.getXX_VMR_Package_ID();	
		
			sql2 = "select sum(xx_lineqty) from xx_vmr_po_linerefprov " +
				"where c_order_id in (select c_order_id from c_order where xx_orderstatus in ('RE','CH') and xx_vmr_package_id=" + aux.getXX_VMR_Package_ID() + ")";	
			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por paquete de la orden" + e);			
			}
			
		} else if (aux.getXX_OrderStatus()!=null & aux.getXX_VMR_Package_ID()>0 & aux.getXX_ReceptionDate()==null)
		{		
			System.out.println("Por paquete y estado de la orden");
			ADialog.info(1, new Container(), "XX_DaysPerPackagePuchaseOrder");
			sql1 = "select " +
			" sum((select sum(xx_lineqty) from xx_vmr_po_linerefprov where c_order_id=o.c_order_id)*"+
                "(case "+dateOCStat2+" when null then 0 else "+dateOCStat2+"-"+dateOCStat1+" end))" +
            " from c_order o " +
			" where xx_vmr_package_id=" + aux.getXX_VMR_Package_ID();	
		
			sql2 = "select sum(xx_lineqty) from xx_vmr_po_linerefprov " +
				"where c_order_id in (select c_order_id from c_order where  " + dateOCStat2 + " is not null "+
				" and xx_vmr_package_id=" + aux.getXX_VMR_Package_ID() + ")";	
			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por paquete de la orden" + e);			
			}
			
		} else if (aux.getXX_OrderStatus()==null & aux.getXX_ReceptionDate()!=null & aux.getXX_VMR_Collection_ID()==0)
		{		
			System.out.println("Por fecha recepcion de la orden");
			ADialog.info(1, new Container(), "XX_DaysPerDatePuchaseOrder");
			sql1 = "select " +
			" sum((select sum(xx_lineqty) from xx_vmr_po_linerefprov where c_order_id=o.c_order_id)*(coalesce(xx_checkupdate,sysdate)-xx_receptiondate)) " +
			" from c_order o " +
			" where xx_orderstatus in ('RE','CH') and XX_ReceptionDate>((select max(xx_receptiondate) from XX_VLO_CheckupDays)-1) and XX_ReceptionDate<((select max(xx_receptiondateuntil) from XX_VLO_CheckupDays)+1)";	
		
			sql2 = "select sum(xx_lineqty) from xx_vmr_po_linerefprov " +
				" where c_order_id in (select c_order_id from c_order where xx_orderstatus in ('RE','CH') and XX_ReceptionDate>((select max(xx_receptiondate) from XX_VLO_CheckupDays)-1) and XX_ReceptionDate<((select max(xx_receptiondateuntil) from XX_VLO_CheckupDays)+1))";	
			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por fecha de recepcion de la orden" + e);			
			}
			
		} else if (aux.getXX_OrderStatus()!=null & aux.getXX_ReceptionDate()!=null & aux.getXX_VMR_Collection_ID()==0)
		{		
			System.out.println("Por fecha recepcion y estado de la orden");
			ADialog.info(1, new Container(), "XX_DaysPerDatePuchaseOrder");
			sql1 = "select " +
			" sum((select sum(xx_lineqty) from xx_vmr_po_linerefprov where c_order_id=o.c_order_id)*"+
				"(case "+dateOCStat2+" when null then 0 else "+dateOCStat2+"-"+dateOCStat1+" end))" +
                " from c_order o " +
			" where XX_ReceptionDate>((select max(xx_receptiondate) from XX_VLO_CheckupDays)-1)" +
				" and XX_ReceptionDate<((select max(xx_receptiondateuntil) from XX_VLO_CheckupDays)+1)";	
		
			sql2 = "select sum(xx_lineqty) from xx_vmr_po_linerefprov " +
				" where c_order_id in (select c_order_id from c_order where " + dateOCStat2 + " is not null "+
					" and XX_ReceptionDate>((select max(xx_receptiondate) from XX_VLO_CheckupDays)-1)" +
					" and XX_ReceptionDate<((select max(xx_receptiondateuntil) from XX_VLO_CheckupDays)+1))";	
			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por fecha de recepcion de la orden" + e);			
			}
			
		} else if (aux.getXX_OrderStatus()==null & aux.getXX_ReceptionDate()!=null & aux.getXX_VMR_Collection_ID()>0 & aux.getXX_VMR_Package_ID()==0)
		{		
			System.out.println("Por Coleccion y fecha de la orden");
			ADialog.info(1, new Container(), "XX_DaysPerCollectionDatePO");
			sql1 = "select " +
			" sum((select sum(xx_lineqty) from xx_vmr_po_linerefprov where c_order_id=o.c_order_id)*(coalesce(xx_checkupdate,sysdate)-xx_receptiondate)) " +
			" from c_order o " +
			" where xx_orderstatus in ('RE','CH') and xx_vmr_collection_id=" + aux.getXX_VMR_Collection_ID() +" and XX_ReceptionDate>((select max(xx_receptiondate) from XX_VLO_CheckupDays)-1) and XX_ReceptionDate<((select max(xx_receptiondateuntil) from XX_VLO_CheckupDays)+1)";	
		
			sql2 = "select sum(xx_lineqty) from xx_vmr_po_linerefprov " +
				" where c_order_id in (select c_order_id from c_order where xx_orderstatus in ('RE','CH') and xx_vmr_collection_id=" + aux.getXX_VMR_Collection_ID() +" and XX_ReceptionDate>((select max(xx_receptiondate) from XX_VLO_CheckupDays)-1) and XX_ReceptionDate<((select max(xx_receptiondate) from XX_VLO_CheckupDays)+1))";	
	
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por coleccion y fecha de la orden" + e);			
			}
			
		} else if (aux.getXX_OrderStatus()!=null & aux.getXX_ReceptionDate()!=null & aux.getXX_VMR_Collection_ID()>0 & aux.getXX_VMR_Package_ID()==0)
		{		
			System.out.println("Por Coleccion, fecha y estado de la orden");
			ADialog.info(1, new Container(), "XX_DaysPerCollectionDatePO");
			sql1 = "select " +
			" sum((select sum(xx_lineqty) from xx_vmr_po_linerefprov where c_order_id=o.c_order_id)*"+
				"(case "+dateOCStat2+" when null then 0 else "+dateOCStat2+"-"+dateOCStat1+" end))" +
			" from c_order o " +
			" where xx_vmr_collection_id=" + aux.getXX_VMR_Collection_ID() +
				" and XX_ReceptionDate>((select max(xx_receptiondate) from XX_VLO_CheckupDays)-1) " +
				" and XX_ReceptionDate<((select max(xx_receptiondateuntil) from XX_VLO_CheckupDays)+1)";	
		
			sql2 = "select sum(xx_lineqty) from xx_vmr_po_linerefprov " +
				" where c_order_id in (select c_order_id from c_order where " + dateOCStat2 + " is not null " + 
					" and xx_vmr_collection_id=" + aux.getXX_VMR_Collection_ID() +" " +
					" and XX_ReceptionDate>((select max(xx_receptiondate) from XX_VLO_CheckupDays)-1) " +
					" and XX_ReceptionDate<((select max(xx_receptiondateuntil) from XX_VLO_CheckupDays)+1))";	
	
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por coleccion y fecha de la orden" + e);			
			}
			
		} else if (aux.getXX_OrderStatus()==null & aux.getXX_ReceptionDate()!=null & aux.getXX_VMR_Collection_ID()>0 & aux.getXX_VMR_Package_ID()>0)
		{		
			System.out.println("Por Paquete y fecha de la orden");
			ADialog.info(1, new Container(), "XX_DaysPerPackageDatePO");
			sql1 = "select " +
			" sum((select sum(xx_lineqty) from xx_vmr_po_linerefprov where c_order_id=o.c_order_id)*(coalesce(xx_checkupdate,sysdate)-xx_receptiondate)) " +
			" from c_order o " +
			" where xx_orderstatus in ('RE','CH') and xx_vmr_package_id=" + aux.getXX_VMR_Package_ID() +" and XX_ReceptionDate>((select max(xx_receptiondate) from XX_VLO_CheckupDays)-1) and XX_ReceptionDate<((select max(xx_receptiondateuntil) from XX_VLO_CheckupDays)+1)";	
		
			sql2 = "select sum(xx_lineqty) from xx_vmr_po_linerefprov " +
				" where c_order_id in (select c_order_id from c_order where xx_orderstatus in ('RE','CH') and xx_vmr_package_id=" + aux.getXX_VMR_Package_ID() +" and XX_ReceptionDate>((select max(xx_receptiondate) from XX_VLO_CheckupDays)-1) and XX_ReceptionDate<((select max(xx_receptiondateuntil) from XX_VLO_CheckupDays)+1))";	
		
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por paquete y fecha de la orden" + e);			
			}
			
		} else if (aux.getXX_OrderStatus()!=null & aux.getXX_ReceptionDate()!=null & aux.getXX_VMR_Collection_ID()>0 & aux.getXX_VMR_Package_ID()>0)
		{		
			System.out.println("Por Paquete, fecha y estado de la orden");
			ADialog.info(1, new Container(), "XX_DaysPerPackageDatePO");
			sql1 = "select " +
			" sum((select sum(xx_lineqty) from xx_vmr_po_linerefprov where c_order_id=o.c_order_id)*"+
				"(case "+dateOCStat2+" when null then 0 else "+dateOCStat2+"-"+dateOCStat1+" end))" +
			" from c_order o " +
			" where xx_vmr_package_id=" + aux.getXX_VMR_Package_ID() +
				" and XX_ReceptionDate>((select max(xx_receptiondate) from XX_VLO_CheckupDays)-1)" +
				" and XX_ReceptionDate<((select max(xx_receptiondateuntil) from XX_VLO_CheckupDays)+1)";	
		
			sql2 = "select sum(xx_lineqty) from xx_vmr_po_linerefprov " +
			" where c_order_id in (select c_order_id from c_order where " + dateOCStat2 + " is not null "+
				" and xx_vmr_package_id=" + aux.getXX_VMR_Package_ID() +" " +
				" and XX_ReceptionDate>((select max(xx_receptiondate) from XX_VLO_CheckupDays)-1)" +
				" and XX_ReceptionDate<((select max(xx_receptiondateuntil) from XX_VLO_CheckupDays)+1))";	
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por paquete y fecha de la orden "+e);
			}
			
		} else if (aux.getXX_VMR_Order_ID()>0)
		{		
			System.out.println("Por numero pedido");
			ADialog.info(1, new Container(), "XX_DaysPerOrderRequest");
			sql1="select " +
					" round((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)/(select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id),3) " +
					" from xx_vmr_order o " +
				" where xx_vmr_order_id=" + aux.getXX_VMR_Order_ID();		
			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);

			try {
				ResultSet rs = prst1.executeQuery();
				if (rs.next()){
					resultadoFinal = rs.getBigDecimal(1);									
				}
				rs.close();
				prst1.close();
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por numero de pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() != null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() == null & aux.getXX_CollectionOrderRequest_ID()==0 & aux.getXX_PackageOrderRequest_ID()==0)
		{		
			System.out.println("Por estado del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerStatusOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*"+
				"(case "+dateStat2+" when null then 0 else "+dateStat2+"-"+dateStat1+" end))" +
				"from xx_vmr_order o " ;			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where " + dateStat2 + " is not null "+ ")";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por estado del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() == null & aux.getXX_VMR_DistributionHeader_ID()>0 & aux.getXX_DateOrderRequest() == null & aux.getXX_CollectionOrderRequest_ID()==0 & aux.getXX_PackageOrderRequest_ID()==0)
		{		
			System.out.println("Por numero de distribucion del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDistributionOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)) " +
				"from xx_vmr_order o " +
				"where xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID();			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where XX_VMR_DistributionHeader_ID=" + aux.getXX_VMR_DistributionHeader_ID() + ")";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por numero de distribucion del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() == null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() == null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()>0)
		{		
			System.out.println("Por paquete del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerPackageOR");
			sql1="select " +
					" sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)) " +
					" from xx_vmr_order o " +
					" where xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL where xx_vmr_order_id=o.xx_vmr_order_id and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ))";			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
					" where "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID )";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por paquete del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() == null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() == null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()==0)
		{		
			System.out.println("Por coleccion del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerCollectionOR");
			sql1="select " +
					" sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)) " +
					" from xx_vmr_order o " +
					" where xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL where xx_vmr_order_id=o.xx_vmr_order_id and (select distinct(xx_vmr_package_id) from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+"))";			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
					" where (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+")";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por coleccion del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() == null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() != null & aux.getXX_CollectionOrderRequest_ID()==0 & aux.getXX_PackageOrderRequest_ID()==0)
		{	
			System.out.println("Por fecha del pedido del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDateOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)) " +
				"from xx_vmr_order o " +
				"where created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1)";			
		
			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1)";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por fecha del pedido " + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() == null & aux.getXX_VMR_DistributionHeader_ID()>0 & aux.getXX_DateOrderRequest() != null & aux.getXX_CollectionOrderRequest_ID()==0 & aux.getXX_PackageOrderRequest_ID()==0)
		{		
			System.out.println("Por numero de distribucion y fecha del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDistributionDateOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)) " +
				"from xx_vmr_order o " +
				"where created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) and xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID();			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) and XX_VMR_DistributionHeader_ID=" + aux.getXX_VMR_DistributionHeader_ID() + ")";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por numero de distribucion y fecha del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() != null & aux.getXX_VMR_DistributionHeader_ID()>0 & aux.getXX_DateOrderRequest() == null & aux.getXX_CollectionOrderRequest_ID()==0 & aux.getXX_PackageOrderRequest_ID()==0)
		{		
			System.out.println("Por numero de distribucion y estado del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDistributionStatusOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*"+
					"(case "+dateStat2+" when null then 0 else "+dateStat2+"-"+dateStat1+" end))" +
				"from xx_vmr_order o " +
				"where xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID() ;			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where " + dateStat2 + " is not null )"+
					" and VMR_DistributionHeader_ID=" + aux.getXX_VMR_DistributionHeader_ID() ;	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por numero de distribucion y estado del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() == null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() == null & aux.getXX_CollectionOrderRequest_ID()==0 & aux.getXX_PackageOrderRequest_ID()==0)
		{		
			System.out.println("Por numero de distribucion y coleccion del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDistributionCollectionOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)) " +
				"from xx_vmr_order o " +
				"where xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID() + " and xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL where xx_vmr_order_id=o.xx_vmr_order_id and (select distinct(xx_vmr_package_id) from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+"))";			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where XX_VMR_DistributionHeader_ID=" + aux.getXX_VMR_DistributionHeader_ID() + ") and (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+")";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por numero de distribucion y coleccion del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() == null & aux.getXX_VMR_DistributionHeader_ID()>0 & aux.getXX_DateOrderRequest() == null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()>0)
		{		
			System.out.println("Por numero de distribucion y paquete del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDistributionPackageOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)) " +
				"from xx_vmr_order o " +
				"where xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID() + " and xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL where xx_vmr_order_id=o.xx_vmr_order_id and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ))";			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where XX_VMR_DistributionHeader_ID=" + aux.getXX_VMR_DistributionHeader_ID() + ") and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID )";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por numero de distribucion y paquete del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() != null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() != null & aux.getXX_CollectionOrderRequest_ID()==0 & aux.getXX_PackageOrderRequest_ID()==0)
		{	
			System.out.println("Por fecha y estado del pedido del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDateStatusOR");
			sql1="select " +
				" sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*"+
					"(case "+dateStat2+" when null then 0 else "+dateStat2+"-"+dateStat1+" end))" +
				" from xx_vmr_order o " +
				" where created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) " +
					" and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1)";			
		
			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) " +
					" and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) " +
					" and xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where " + dateStat2 + " is not null )";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por fecha y estado del pedido " + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() == null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() != null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()==0)
		{	
			System.out.println("Por fecha y coleccion del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDateCollectionOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)) " +
				"from xx_vmr_order o " +
				"where created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) and xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL where xx_vmr_order_id=o.xx_vmr_order_id and (select distinct(xx_vmr_package_id) from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+"))";			
		
			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) and (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+")";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por fecha y coleccion del pedido " + e);			
			}
		} else if (aux.getXX_OrderRequestStatus() == null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() != null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()>0)
		{	
			System.out.println("Por fecha y paquete del pedido del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDatePackageOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)) " +
				"from xx_vmr_order o " +
				"where created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) and xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL where xx_vmr_order_id=o.xx_vmr_order_id and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ))";			
		
			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID )";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por fecha y paquete del pedido " + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() != null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() == null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()==0)
		{		
			System.out.println("Por estado y coleccion del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerStatusCollectionOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*"+
					"(case "+dateStat2+" when null then 0 else "+dateStat2+"-"+dateStat1+" end))" +
				" from xx_vmr_order o " +
				" where xx_vmr_order_id=(select distinct(xx_vmr_order_id) " +
							" from XX_VMR_ORDERREQUESTDETAIL " +
							" where xx_vmr_order_id=o.xx_vmr_order_id and " +
								" (select distinct(xx_vmr_package_id) " +
								" from m_attributesetinstance " +
								" where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) " +
								" in (select xx_vmr_package_id from xx_vmr_package " +
								" where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+"))";			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where " + dateStat2 + " is not null) " +
					" and (select xx_vmr_package_id from m_attributesetinstance " +
						" where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) " +
						" in (select xx_vmr_package_id from xx_vmr_package " +
						" where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+")";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por estado y coleccion del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() != null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() == null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()>0)
		{		
			System.out.println("Por estado y paquete del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerStatusPackageOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*"+
					"(case "+dateStat2+" when null then 0 else "+dateStat2+"-"+dateStat1+" end))" +
				" from xx_vmr_order o " +
				" where xx_vmr_order_id=(select distinct(xx_vmr_order_id) " +
					" from XX_VMR_ORDERREQUESTDETAIL " +
					" where xx_vmr_order_id=o.xx_vmr_order_id " +
						" and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id " +
								" from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ))";			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where " + dateStat2 + " is not null "+ ")" +
						" and "+aux.getXX_PackageOrderRequest_ID()+ " = " +
								" (select xx_vmr_package_id from m_attributesetinstance " +
								" where m_attributesetinstance_id = XX_PRODUCTBATCH_ID )";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por estado y paquete del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() == null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() == null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()==0)
		{		
			System.out.println("Por coleccion  y paquete del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerCollectionPackageOR");
			sql1="select " +
					" sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)) " +
					" from xx_vmr_order o " +
					" where xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL where xx_vmr_order_id=o.xx_vmr_order_id and (select distinct(xx_vmr_package_id) from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+"))";			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
					" where (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+")";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por coleccion del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() != null & aux.getXX_VMR_DistributionHeader_ID()>0 & aux.getXX_DateOrderRequest() != null & aux.getXX_CollectionOrderRequest_ID()==0 & aux.getXX_PackageOrderRequest_ID()==0)
		{		
			System.out.println("Por numero de distribucion, fecha y estado del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDisDateStaOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*"+
					"(case "+dateStat2+" when null then 0 else "+dateStat2+"-"+dateStat1+" end))" +
				" from xx_vmr_order o " +
				" where xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID() + 
					" and created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1)" +
					" and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1)";			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where " +
						" XX_VMR_DistributionHeader_ID=" + aux.getXX_VMR_DistributionHeader_ID() + 
						" and " + dateStat2 + " is not null) " +
					" and created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) " +
					" and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1)";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por numero de distribucion, fecha y estado del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() != null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() != null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()==0)
		{	
			System.out.println("Por fecha, estado y coleccion del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDateStaCollecOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*"+
					"(case "+dateStat2+" when null then 0 else "+dateStat2+"-"+dateStat1+" end))" +
				" from xx_vmr_order o " +
				" where created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) " +
					" and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1)" +
					" and xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL" +
						" where xx_vmr_order_id=o.xx_vmr_order_id " +
							" and (select distinct(xx_vmr_package_id) from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID )" +
							" in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+"))";			
		
			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where " + dateStat2 + " is not null) " +
						" and created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1)" +
						" and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1)" +
						" and (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) " +
							" in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+")";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por fecha, estado y coleccion del pedido " + e);			
			}
		}	else if (aux.getXX_OrderRequestStatus() != null & aux.getXX_VMR_DistributionHeader_ID()==0 & aux.getXX_DateOrderRequest() != null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()>0)
		{	
			System.out.println("Por fecha, estado y paquete del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDateStaPackaOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*"+
					"(case "+dateStat2+" when null then 0 else "+dateStat2+"-"+dateStat1+" end))" +
				" from xx_vmr_order o " +
				" where created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) " +
					" and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) " +
					" and xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL" +
						" where xx_vmr_order_id=o.xx_vmr_order_id" +
							" and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id from m_attributesetinstance " +
									" where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ))";			
		
			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where " + dateStat2 + " is not null) " +
						" and created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) " +
						" and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) " +
						" and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id " +
								" from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID )";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por fecha, estado y paquete del pedido " + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() == null & aux.getXX_VMR_DistributionHeader_ID()>0 & aux.getXX_DateOrderRequest() != null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()==0)
		{	
			System.out.println("Por distribucion, fecha y coleccion del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDisDateCollecOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)) " +
				"from xx_vmr_order o " +
				"where xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID() + " and created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) and xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL where xx_vmr_order_id=o.xx_vmr_order_id and (select distinct(xx_vmr_package_id) from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+"))";			
		
			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where XX_VMR_DistributionHeader_ID=" + aux.getXX_VMR_DistributionHeader_ID() + " and created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) and (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+")";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por distribucion, fecha y coleccion del pedido " + e);			
			}
		} else if (aux.getXX_OrderRequestStatus() == null & aux.getXX_VMR_DistributionHeader_ID()>0 & aux.getXX_DateOrderRequest() != null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()>0)
		{	
			System.out.println("Por distribucion, fecha y paquete del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDisDatePaOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*(coalesce(xx_datestatusonstore,sysdate)-created)) " +
				"from xx_vmr_order o " +
				"where xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID() + " and created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) and xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL where xx_vmr_order_id=o.xx_vmr_order_id and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ))";			
		
			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where XX_VMR_DistributionHeader_ID=" + aux.getXX_VMR_DistributionHeader_ID() + ") and created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1) and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID )";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por distribucion, fecha y paquete del pedido " + e);			
			}
		} else if (aux.getXX_OrderRequestStatus() != null & aux.getXX_VMR_DistributionHeader_ID()>0 & aux.getXX_DateOrderRequest() == null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()==0)
		{		
			System.out.println("Por distribucion, estado y coleccion del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDisStaCollecOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*"+
					" (case "+dateStat2+" when null then 0 else "+dateStat2+"-"+dateStat1+" end))" +
				" from xx_vmr_order o " +
				" where xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID() +
					" and xx_vmr_order_id=(select distinct(xx_vmr_order_id) " +
						" from XX_VMR_ORDERREQUESTDETAIL where xx_vmr_order_id=o.xx_vmr_order_id " +
						" and (select distinct(xx_vmr_package_id) from m_attributesetinstance " +
						" where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) " +
						" in (select xx_vmr_package_id from xx_vmr_package " +
						" where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+"))";			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where " +
						" xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID() + 
						" and " + dateStat2 + " is not null) " +
					" and (select xx_vmr_package_id from m_attributesetinstance " +
						" where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) " +
						" in (select xx_vmr_package_id from xx_vmr_package " +
						" where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+")";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por distribucion, estado y coleccion del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() != null & aux.getXX_VMR_DistributionHeader_ID()>0 & aux.getXX_DateOrderRequest() == null
				& aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()>0)
		{	
			System.out.println("Por distribucion, estado y paquete del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDisStaPaOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*"+
					"(case "+dateStat2+" when null then 0 else "+dateStat2+"-"+dateStat1+" end))" +
				"from xx_vmr_order o " +
				"where xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID() +
					" and xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL " +
						" where xx_vmr_order_id=o.xx_vmr_order_id and "+aux.getXX_PackageOrderRequest_ID()+
							" = (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ))";			
		
			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order" +
						" where XX_VMR_DistributionHeader_ID=" + aux.getXX_VMR_DistributionHeader_ID() +" and " + dateStat2 + " is not null)" +
					" and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id from m_attributesetinstance" +
							" where m_attributesetinstance_id = XX_PRODUCTBATCH_ID )";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por distribucion, estado y paquete del pedido " + e);			
			}
		} else if (aux.getXX_OrderRequestStatus() != null & aux.getXX_VMR_DistributionHeader_ID()>0 & aux.getXX_DateOrderRequest() != null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()==0)
		{		
			System.out.println("Por numero de distribucion, fecha, estado y coleccion del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDisDateStaCollecOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*"+
					"(case "+dateStat2+" when null then 0 else "+dateStat2+"-"+dateStat1+" end))" +
				"from xx_vmr_order o " +
				"where xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID() + 
					" and created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1) " +
					" and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1)" +
					" and xx_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL where xx_vmr_order_id=o.xx_vmr_order_id " +
						" and (select distinct(xx_vmr_package_id) from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ) " +
						" in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+"))";			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where XX_VMR_DistributionHeader_ID=" + aux.getXX_VMR_DistributionHeader_ID() +
						" and " + dateStat2 + " is not null) " +
					" and created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1)" +
					" and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1)" +
					" and (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID )" +
					" in (select xx_vmr_package_id from xx_vmr_package where xx_vmr_collection_id="+aux.getXX_CollectionOrderRequest_ID()+")";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por numero de distribucion, fecha, estado y coleccion del pedido" + e);			
			}
			
		} else if (aux.getXX_OrderRequestStatus() != null & aux.getXX_VMR_DistributionHeader_ID()>0 & aux.getXX_DateOrderRequest() != null & aux.getXX_CollectionOrderRequest_ID()>0 & aux.getXX_PackageOrderRequest_ID()>0)
		{		
			System.out.println("Por numero de distribucion, fecha, estado y paquete del pedido");
			ADialog.info(1, new Container(), "XX_DaysPerDisDateStaPaOR");
			sql1="select " +
				"sum((select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail where xx_vmr_order_id=o.xx_vmr_order_id)*"+
					"(case "+dateStat2+" when null then 0 else "+dateStat2+"-"+dateStat1+" end))" +
				"from xx_vmr_order o " +
				"where xx_vmr_distributionheader_id=" + aux.getXX_VMR_DistributionHeader_ID() +
					" and created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1)" +
					" and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1)" +
					" and x_vmr_order_id=(select distinct(xx_vmr_order_id) from XX_VMR_ORDERREQUESTDETAIL where xx_vmr_order_id=o.xx_vmr_order_id" +
						" and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id from m_attributesetinstance where m_attributesetinstance_id = XX_PRODUCTBATCH_ID ))";			

			sql2 = "select sum(XX_ProductQuantity) from XX_VMR_OrderRequestDetail " +
				" where xx_vmr_order_id in (select xx_vmr_order_id from xx_vmr_order where XX_VMR_DistributionHeader_ID=" + aux.getXX_VMR_DistributionHeader_ID() + 
						" and " + dateStat2 + " is not null) " +
					" and created>((select max(XX_DateOrderRequest) from XX_VLO_CheckupDays)-1)" +
					" and created<((select max(XX_DateOrderRequestUntil) from XX_VLO_CheckupDays)+1)" +
					" and "+aux.getXX_PackageOrderRequest_ID()+ " = (select xx_vmr_package_id from m_attributesetinstance" +
							" where m_attributesetinstance_id = XX_PRODUCTBATCH_ID )";	

			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			PreparedStatement prst2 = DB.prepareStatement(sql2,null);
			try {
				ResultSet rs1 = prst1.executeQuery();
				ResultSet rs2 = prst2.executeQuery();
				if (rs1.next()){
					resultado1 = rs1.getBigDecimal(1);									
				}
				rs1.close();
				prst1.close();
				if (rs2.next()){
					resultado2 = rs2.getBigDecimal(1);									
				}
				rs2.close();
				prst2.close();
				if (resultado2!=null)
					resultadoFinal = resultado1.divide(resultado2, 2, RoundingMode.HALF_UP);
			} catch (Exception e){
				System.out.println("Error al calcular el numero de dias de chequeo por numero de distribucion, fecha, estado y paquete del pedido" + e);			
			}
			
		}

		System.out.println(sql1);
		System.out.println(sql2);

		int dias = 0;
		int horas = 0;
		int minutos = 0;
		
		dias = resultadoFinal.intValue();
		horas = ((resultadoFinal.subtract(new BigDecimal(resultadoFinal.intValue()))).multiply(new BigDecimal(24))).intValue();
		minutos = ((((resultadoFinal.subtract(new BigDecimal(resultadoFinal.intValue()))).multiply(new BigDecimal(24))).subtract(new BigDecimal(((resultadoFinal.subtract(new BigDecimal(resultadoFinal.intValue()))).multiply(new BigDecimal(24))).intValue()))).multiply(new BigDecimal(60))).intValue();
		
		String horasString = "";
		String minutosString = "";
		
		if (horas<10)
			horasString = "0" + horas;
		else 
			horasString = "" + horas;
		if (minutos<10)
			minutosString = "0" + minutos;
		else 
			minutosString = "" + minutos;
		
		System.out.println(dias+":"+horasString+":"+minutosString);
		aux.setXX_DaysHoursMinutes(dias+":"+horasString+":"+minutosString);
		
		aux.save();
								

		return "";

	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}

}
