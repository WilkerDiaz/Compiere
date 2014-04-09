package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;

import compiere.model.cds.MVLODispatchGuide;
import compiere.model.cds.X_Ref_XX_Ref_DispatchGuideStatus;
import compiere.model.cds.X_Ref_XX_Ref_TypeDispatchGuide;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VLO_DetailDispatchGuide;
import compiere.model.cds.X_XX_VLO_Fleet;

public class XX_DispatchGuidePlanningProcess extends SvrProcess {
	
	private Integer buscarKilometros (Integer wareDeparture, Integer wareArrival){
		String sql = "SELECT CT.XX_CIRCUITKILOMETERS " +
					 "FROM XX_VLO_CIRCUITTOUR CT " +
					 "WHERE CT.XX_DEPARTUREWAREHOUSE_ID = " +wareDeparture+ " "+
					 "AND CT.XX_ARRIVALWAREHOUSE_ID = "+wareArrival+ " "+
					 "AND CT.ISACTIVE = 'Y'";
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			Integer auxKilometer = null;
			
			if(rs.next()){
				auxKilometer = rs.getInt("XX_CIRCUITKILOMETERS");
				rs.close();
				pstmt.close();
			}
			return auxKilometer;
		}catch (Exception e) {
			log.fine(Msg.getMsg(getCtx(), "Error"));
			return -1;
		}
	}
	
	private Integer selectFleet(Integer warehouse){
		Integer fleet = new Integer(0);
		String sql =  "SELECT WA.XX_VLO_FLEET_ID " +
					  "FROM XX_VLO_WarehouseAssociated WA " +
					  "WHERE WA.M_WAREHOUSE_ID = "+ warehouse +" " +
					  "AND WA.XX_VLO_FLEET_ID NOT IN (	" +
					  	  "SELECT UNIQUE(DG.XX_VLO_FLEET_ID) " +
					  	  "FROM XX_VLO_DispatchGuide DG " +
					  	  "WHERE TO_CHAR(DG.XX_DISPATCHDATE,'DDMMYYYY') = TO_CHAR(sysdate,'DDMMYYYY') " +
					  ")";

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				fleet = rs.getInt("XX_VLO_FLEET_ID");
				rs.close();
				pstmt.close();
			}else{
				sql = "SELECT MIN(AUX.B) XX_VLO_FLEET_ID " +
					  "FROM( " +
					  	"SELECT (SELECT COUNT(*) FROM XX_VLO_TRAVEL T WHERE T.XX_VLO_FLEET_ID = WA.XX_VLO_FLEET_ID AND TO_CHAR(T.XX_TRAVELDATE,'DDMMYYYY') = TO_CHAR(sysdate,'DDMMYYYY')) A, WA.XX_VLO_FLEET_ID B " +
					  	"FROM XX_VLO_WarehouseAssociated WA " +
					  	"WHERE WA.M_WAREHOUSE_ID = "+warehouse+" " +
					  	") AUX";
				
				PreparedStatement pstmt1 = DB.prepareStatement(sql, null);
				ResultSet rs1 = pstmt1.executeQuery();
				
				if(rs1.next()){
					fleet = rs1.getInt("XX_VLO_FLEET_ID");
					rs1.close();
					pstmt1.close();
				}
			}
			
			return fleet;			
		}catch (SQLException e) {
			return -1;
		}
	}
	
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	
	@Override
	protected String doIt() throws Exception {
		Integer sumaPaquetes = null; 
		Integer warehouse = null; 
		X_XX_VLO_Fleet fleet = null; 
		Date utilDate = new Date();
		long lnMilisegundos = utilDate.getTime();
		Timestamp fechaActual = new Timestamp(lnMilisegundos);


		String sql =  "SELECT SUM(O.XX_PACKAGEQUANTITY), O.M_WAREHOUSE_ID " +
					  "FROM XX_VMR_ORDER O " +
					  "WHERE O.XX_ORDERREQUESTSTATUS = '"+X_Ref_XX_VMR_OrderStatus.EN_BAHIA.getValue()+"'" +
					  "AND O.XX_ORDERREADY = 'Y'" +
					  "GROUP BY O.M_WAREHOUSE_ID";
		
		System.out.println(sql);

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				sumaPaquetes = rs.getInt("SUM(O.XX_PACKAGEQUANTITY)");				
				warehouse = rs.getInt("M_WAREHOUSE_ID");
				fleet = new X_XX_VLO_Fleet(getCtx(), selectFleet(warehouse), get_TrxName());
				
				BigDecimal auxEquivalentPackage = fleet.getXX_AdjustmentsAmount().multiply(new BigDecimal(fleet.getXX_PackageQuantity()));
				Integer auxEquivalentPackageInt = auxEquivalentPackage.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
				
				if (sumaPaquetes <= auxEquivalentPackageInt){//si los paquetes del pedido caben en una flota
					MVLODispatchGuide dispatchGuide = new MVLODispatchGuide(getCtx(), 0, get_TrxName());
					
					dispatchGuide.setXX_ArrivalWarehouse_ID(warehouse);
					dispatchGuide.setXX_DepartureWarehouse_ID(getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"));
					dispatchGuide.setXX_DispatchGuideStatus(X_Ref_XX_Ref_DispatchGuideStatus.SUGERIDO.getValue());
					dispatchGuide.setXX_VLO_Fleet_ID(fleet.get_ID());
					dispatchGuide.setXX_TotalPackages(sumaPaquetes);
					dispatchGuide.setXX_Assistant1_ID(fleet.getXX_Assistant1_ID());
					dispatchGuide.setXX_Assistant2_ID(fleet.getXX_Assistant2_ID());
					dispatchGuide.setXX_Driver_ID(fleet.getXX_Driver_ID());
					dispatchGuide.setXX_DispatchDate(fechaActual);
					dispatchGuide.setXX_ApproveDispatchGuide("N");

					BigDecimal auxKilometros = new BigDecimal( buscarKilometros(getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"), warehouse));
					dispatchGuide.setXX_KilometersPlanned(auxKilometros);
					BigDecimal auxPercentageOccupancy= new BigDecimal( sumaPaquetes / fleet.getXX_MaximumCapacity() );
					dispatchGuide.setXX_PercentageOccupancyVehicle(auxPercentageOccupancy);
					
					dispatchGuide.save();
					commit();
					
					String sql1 = "SELECT O.XX_VMR_ORDER_ID, O.XX_PACKAGEQUANTITY " +
								  "FROM XX_VMR_ORDER O " +
								  "WHERE O.XX_ORDERREQUESTSTATUS = '"+X_Ref_XX_VMR_OrderStatus.EN_BAHIA.getValue()+"' " +
								  "AND O.M_WAREHOUSE_ID = "+warehouse+" ";
					
					PreparedStatement pstmt1 = DB.prepareStatement(sql1, null);
					ResultSet rs1 = pstmt1.executeQuery();
					while (rs1.next()){
						X_XX_VLO_DetailDispatchGuide detalleDispatchGuide = new X_XX_VLO_DetailDispatchGuide(getCtx(), 0, get_TrxName());
						
						detalleDispatchGuide.setXX_TypeDetailDispatchGuide(X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue());
						detalleDispatchGuide.setXX_VLO_DispatchGuide_ID(dispatchGuide.get_ID());
						detalleDispatchGuide.setXX_PlacedOrderPackages(rs1.getInt("XX_PACKAGEQUANTITY"));
						detalleDispatchGuide.setXX_PackagesReceived(rs1.getInt("XX_PACKAGEQUANTITY"));
						detalleDispatchGuide.setXX_PackagesSent(rs1.getInt("XX_PACKAGEQUANTITY"));
						detalleDispatchGuide.setXX_VMR_Order_ID(rs1.getInt("XX_VMR_ORDER_ID"));
						detalleDispatchGuide.setXX_PackagesReceived(0);
						detalleDispatchGuide.setXX_PackagesSent(0);
						
						detalleDispatchGuide.save();
					}
					rs1.close();
					pstmt1.close();
					commit();
				}else{// si los paquetes de los pedidos no entran en una sola flota
					boolean creoDG = true;
					String sql2 = "SELECT O.XX_PACKAGEQUANTITY , O.XX_VMR_ORDER_ID " +
								  "FROM XX_VMR_ORDER O " +
								  "WHERE O.XX_ORDERREQUESTSTATUS = '"+X_Ref_XX_VMR_OrderStatus.EN_BAHIA.getValue()+"'" +
								  "AND O.M_WAREHOUSE_ID = "+ warehouse +"";
					
					PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
					ResultSet rs2 = pstmt2.executeQuery();
					
					/***************/
					/*cree DG nueva*/
					/***************/
					MVLODispatchGuide dispatchGuide = new MVLODispatchGuide(getCtx(), 0, get_TrxName());
					dispatchGuide.setXX_ArrivalWarehouse_ID(warehouse);
					dispatchGuide.setXX_DepartureWarehouse_ID(getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"));
					dispatchGuide.setXX_DispatchGuideStatus(X_Ref_XX_Ref_DispatchGuideStatus.SUGERIDO.getValue());
					dispatchGuide.setXX_VLO_Fleet_ID(fleet.get_ID());
					dispatchGuide.setXX_TotalPackages(0);
					dispatchGuide.setXX_Assistant1_ID(fleet.getXX_Assistant1_ID());
					dispatchGuide.setXX_Assistant2_ID(fleet.getXX_Assistant2_ID());
					dispatchGuide.setXX_Driver_ID(fleet.getXX_Driver_ID());
					dispatchGuide.setXX_DispatchDate(fechaActual);
					dispatchGuide.setXX_ApproveDispatchGuide("N");
					
					BigDecimal auxKilometros = new BigDecimal( buscarKilometros(getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"), warehouse));
					dispatchGuide.setXX_KilometersPlanned(auxKilometros);
					BigDecimal auxPercentageOccupancy= new BigDecimal( 0 / fleet.getXX_MaximumCapacity() );
					dispatchGuide.setXX_PercentageOccupancyVehicle(auxPercentageOccupancy);
					
					dispatchGuide.save();
					commit();
					/*************/
					/*************/
					
					Integer sumaPaqueteActual = 0;
					
					while(rs2.next()){
						Integer cantidadPaquetes = rs2.getInt("XX_PACKAGEQUANTITY");				
						Integer pedidoID = rs2.getInt("XX_VMR_ORDER_ID");
						
						if(auxEquivalentPackageInt-cantidadPaquetes >= 0){
							sumaPaqueteActual = sumaPaqueteActual + cantidadPaquetes;
							
							dispatchGuide.setXX_TotalPackages(sumaPaqueteActual);
							auxPercentageOccupancy= new BigDecimal( sumaPaqueteActual / fleet.getXX_MaximumCapacity() );
							dispatchGuide.setXX_PercentageOccupancyVehicle(auxPercentageOccupancy);
							dispatchGuide.save();
							commit();
							
							X_XX_VLO_DetailDispatchGuide detalleDispatchGuide = new X_XX_VLO_DetailDispatchGuide(getCtx(), 0, get_TrxName());
							
							detalleDispatchGuide.setXX_TypeDetailDispatchGuide(X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue());
							detalleDispatchGuide.setXX_VLO_DispatchGuide_ID(dispatchGuide.get_ID());
							detalleDispatchGuide.setXX_PlacedOrderPackages(cantidadPaquetes);
							detalleDispatchGuide.setXX_VMR_Order_ID(pedidoID);
							detalleDispatchGuide.setXX_PackagesReceived(0);
							detalleDispatchGuide.setXX_PackagesSent(0);
							
							detalleDispatchGuide.save();
							
							auxEquivalentPackageInt = auxEquivalentPackageInt - cantidadPaquetes;
							creoDG = false;
						}else{
							sumaPaqueteActual = 0;
							sumaPaqueteActual = sumaPaqueteActual + cantidadPaquetes;
							
							
							
							if(!creoDG){
								dispatchGuide = new MVLODispatchGuide(getCtx(), 0, get_TrxName());	
								fleet = new X_XX_VLO_Fleet(getCtx(), selectFleet(warehouse), get_TrxName());
								
								auxEquivalentPackage = fleet.getXX_AdjustmentsAmount().multiply(new BigDecimal(fleet.getXX_PackageQuantity()));
								auxEquivalentPackageInt = auxEquivalentPackage.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
							}else{
								creoDG = false;
							}
							
							dispatchGuide.setXX_ArrivalWarehouse_ID(warehouse);
							dispatchGuide.setXX_DepartureWarehouse_ID(getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"));
							dispatchGuide.setXX_DispatchGuideStatus(X_Ref_XX_Ref_DispatchGuideStatus.SUGERIDO.getValue());
							dispatchGuide.setXX_VLO_Fleet_ID(fleet.get_ID());
							dispatchGuide.setXX_TotalPackages(0);
							dispatchGuide.setXX_Assistant1_ID(fleet.getXX_Assistant1_ID());
							dispatchGuide.setXX_Assistant2_ID(fleet.getXX_Assistant2_ID());
							dispatchGuide.setXX_Driver_ID(fleet.getXX_Driver_ID());
							dispatchGuide.setXX_DispatchDate(fechaActual);
							dispatchGuide.setXX_ApproveDispatchGuide("N");
							dispatchGuide.setXX_TotalPackages(sumaPaqueteActual);
							
							auxKilometros = new BigDecimal( buscarKilometros(getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"), warehouse));
							dispatchGuide.setXX_KilometersPlanned(auxKilometros);
							auxPercentageOccupancy= new BigDecimal( 0 / fleet.getXX_MaximumCapacity() );
							dispatchGuide.setXX_PercentageOccupancyVehicle(auxPercentageOccupancy);
							
							dispatchGuide.save();
							commit();
							X_XX_VLO_DetailDispatchGuide detalleDispatchGuide = new X_XX_VLO_DetailDispatchGuide(getCtx(), 0, get_TrxName());
							
							detalleDispatchGuide.setXX_TypeDetailDispatchGuide(X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue());
							detalleDispatchGuide.setXX_VLO_DispatchGuide_ID(dispatchGuide.get_ID());
							detalleDispatchGuide.setXX_PlacedOrderPackages(cantidadPaquetes);
							detalleDispatchGuide.setXX_VMR_Order_ID(pedidoID);
							detalleDispatchGuide.setXX_PackagesReceived(0);
							detalleDispatchGuide.setXX_PackagesSent(0);
							
							detalleDispatchGuide.save();
						}
					
					}
					rs2.close();
					pstmt2.close();
					commit();
				}
				

			}
			rs.close();
			pstmt.close();
			return "";			
		}catch (SQLException e) {
			return e.toString();
		}
	}
}
