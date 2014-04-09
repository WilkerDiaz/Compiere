package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVLODetailDispatchGuide extends X_XX_VLO_DetailDispatchGuide {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3361327695278389931L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVLODetailDispatchGuide.class);

	public MVLODetailDispatchGuide(Ctx ctx, int XX_VLO_DetailDispatchGuide_ID,
			Trx trx) {
		super(ctx, XX_VLO_DetailDispatchGuide_ID, trx);
	}
	
	public MVLODetailDispatchGuide(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}

	
	@Override
	protected boolean beforeDelete(){
		X_XX_VLO_DispatchGuide dg = new X_XX_VLO_DispatchGuide(getCtx(), getXX_VLO_DispatchGuide_ID(), null);
		if(dg.getXX_DispatchGuideStatus().equals(X_Ref_XX_Ref_DispatchGuideStatus.PENDIENTE.getValue())){
			if(getXX_TypeDetailDispatchGuide().equals(X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue())){
				String sql = "UPDATE XX_VLO_OrderDetailPackage " +
					  		 "SET XX_VLO_DISPATCHGUIDE_ID = null, XX_ORDERDETAILPACKAGESTATUS='PEN' " +
					  		 "WHERE XX_VMR_ORDER_ID = "+ getXX_VMR_Order_ID() + " " +
					  		 "AND XX_VLO_DISPATCHGUIDE_ID =" + getXX_VLO_DispatchGuide_ID();
				
				DB.executeUpdate(null, sql);
				return true;
			}			
			return true;
		}else{
			return false;			
		}
	}
	
	/**
	 * 	Executed after Delete operation.
	 * 	@param success true if record deleted
	 *	@return true if delete is a success
	 */
	@Override
	protected boolean afterDelete (boolean success){
		if(success){
			X_XX_VLO_DispatchGuide dispatchGuide = new X_XX_VLO_DispatchGuide(getCtx(), getXX_VLO_DispatchGuide_ID(), get_Trx());
			X_XX_VLO_Travel travel = new X_XX_VLO_Travel(getCtx(), dispatchGuide.getXX_VLO_Travel_ID(), get_Trx());
			
			if(getXX_TypeDetailDispatchGuide().equals(X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue())){
				travel.setXX_TotalPackages(travel.getXX_TotalPackages() - getXX_PlacedOrderPackages());
				travel.setXX_TotalPackagesReceive(travel.getXX_TotalPackagesReceive() - getXX_PackagesReceived());
				travel.setXX_TotalPackagesSent(travel.getXX_TotalPackagesSent() - getXX_PackagesSent());
				
				dispatchGuide.setXX_TotalPackages(dispatchGuide.getXX_TotalPackages() - getXX_PlacedOrderPackages());
				dispatchGuide.setXX_TotalPackagesReceive(dispatchGuide.getXX_TotalPackagesReceive() - getXX_PackagesReceived());
				dispatchGuide.setXX_TotalPackagesSent(dispatchGuide.getXX_TotalPackagesSent() - getXX_PackagesSent());
				
			}else if(getXX_TypeDetailDispatchGuide().equals(X_Ref_XX_Ref_TypeDispatchGuide.OTHER_GOODS.getValue())){
				travel.setXX_TotalPackages(travel.getXX_TotalPackages() - getXX_QuantitySent());
				travel.setXX_TotalPackagesReceive(travel.getXX_TotalPackagesReceive() - getXX_QuantityReceived());
				travel.setXX_TotalPackagesSent(travel.getXX_TotalPackagesSent() - getXX_QuantitySent());
				
				dispatchGuide.setXX_TotalPackages(dispatchGuide.getXX_TotalPackages() - getXX_QuantitySent());
				dispatchGuide.setXX_TotalPackagesReceive(dispatchGuide.getXX_TotalPackagesReceive() - getXX_QuantityReceived());
				dispatchGuide.setXX_TotalPackagesSent(dispatchGuide.getXX_TotalPackagesSent() - getXX_QuantitySent());
			}		
			travel.save();
			dispatchGuide.save();
		}
		return true;
	} 	//	afterDelete
	
	
	
/*	private Vector<String> buscarPedidoporDispatchGuide (String dispatchGuide){
		Vector<String> placedOrdersAux = new Vector<String>();
		String sql = "SELECT DDG.XX_VMR_ORDER_ID " +
					 "FROM XX_VLO_DETAILDISPATCHGUIDE DDG " +
					 "WHERE DDG.XX_TYPEDETAILDISPATCHGUIDE = '" +X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+ "' " +
					 "AND DDG.XX_VLO_DISPATCHGUIDE_ID = "+ dispatchGuide +" " +
					 "AND DDG.XX_VMR_ORDER_ID IS NOT NULL";

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				placedOrdersAux.add(rs.getString("XX_VMR_ORDER_ID"));
			}
			
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.fine(e.getMessage());
		}
		
		return placedOrdersAux;
	}
	
	private Vector<String> buscarPedidoIDporDispatchGuide (String dispatchGuide){
		Vector<String> placedOrdersAux = new Vector<String>();
		String sql = "SELECT DDG.XX_VLO_DETAILDISPATCHGUIDE_ID " +
					 "FROM XX_VLO_DETAILDISPATCHGUIDE DDG " +
					 "WHERE DDG.XX_TYPEDETAILDISPATCHGUIDE = '" +X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+ "' " +
					 "AND DDG.XX_VLO_DISPATCHGUIDE_ID = "+ dispatchGuide +" " +
					 "AND DDG.XX_VMR_ORDER_ID IS NOT NULL";

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				placedOrdersAux.add(rs.getString("XX_VLO_DETAILDISPATCHGUIDE_ID"));
			}
			
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.fine(e.getMessage());
		}
		
		return placedOrdersAux;
	}
	*/

	private Vector<Integer> buscarBultosID (String pedido, Integer dispatchGuide){
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Vector<Integer> bultosAux = new Vector<Integer>();
		String sql = "SELECT DP.XX_VLO_ORDERDETAILPACKAGE_ID " +
					 "FROM XX_VLO_ORDERDETAILPACKAGE DP " +
					 "WHERE DP.XX_VMR_ORDER_ID = " +pedido+ " " +
					 "AND DP.XX_VLO_DISPATCHGUIDE_ID = "+dispatchGuide+"";
		
		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				bultosAux.add(rs.getInt("XX_VLO_ORDERDETAILPACKAGE_ID"));
			}

		} catch (Exception e) {
			log.fine(e.getMessage());
			return new Vector<Integer>();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return bultosAux;
	}
	
	private Vector<Integer> buscarBultosIDNulos (String pedido){
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Vector<Integer> bultosAux = new Vector<Integer>();
		String sql = "SELECT DP.XX_VLO_ORDERDETAILPACKAGE_ID " +
					 "FROM XX_VLO_ORDERDETAILPACKAGE DP " +
					 "WHERE DP.XX_VMR_ORDER_ID = " +pedido+ " " +
					 "AND DP.XX_VLO_DISPATCHGUIDE_ID IS NULL ";
		
		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				bultosAux.add(rs.getInt("XX_VLO_ORDERDETAILPACKAGE_ID"));
			}
			
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.fine(e.getMessage());
			return new Vector<Integer>();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return bultosAux;
	}
	
/*	private String buscarPedidoID(String becoOrderCorrelative){
		String XX_VMR_ORDER_ID = null;
		
		String sql = "SELECT XX_VMR_ORDER_ID " +
					 "FROM XX_VMR_ORDER " +
					 "WHERE XX_ORDERBECOCORRELATIVE = '" +becoOrderCorrelative+ "'";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();

			
			if(rs.next()){
				XX_VMR_ORDER_ID = rs.getString("XX_VMR_ORDER_ID");
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.fine(e.getMessage());
			return null;
		}
		
		return XX_VMR_ORDER_ID;		
	}
	
	private String buscarBultoID(String value){
		String XX_VMR_ORDER_ID = null;
		
		String sql = "SELECT XX_VLO_ORDERDETAILPACKAGE_ID " +
					 "FROM XX_VLO_ORDERDETAILPACKAGE " +
					 "WHERE VALUE = '" +value+ "' ";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();

			
			if(rs.next()){
				XX_VMR_ORDER_ID = rs.getString("XX_VLO_ORDERDETAILPACKAGE_ID");
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.fine(e.getMessage());
			return null;
		}
		
		return XX_VMR_ORDER_ID;		
	}
	
	private String buscarDetalleDispatchGuideConPedido(String pedido, Integer DispatchGuide){
		String XX_VLO_DETAILDISPATCHGUIDE_ID = null;
		
		String sql = "SELECT XX_VLO_DETAILDISPATCHGUIDE_ID " +
					 "FROM XX_VLO_DETAILDISPATCHGUIDE " +
					 "WHERE XX_VLO_DISPATCHGUIDE_ID = " +DispatchGuide+" "+
					 "AND XX_VMR_ORDER_ID = " +pedido+ " ";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();

			
			if(rs.next()){
				XX_VLO_DETAILDISPATCHGUIDE_ID = rs.getString("XX_VLO_DETAILDISPATCHGUIDE_ID");
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.fine(e.getMessage());
			return null;
		}
		
		return XX_VLO_DETAILDISPATCHGUIDE_ID;		
	}*/
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return success
	 */
	@Override
	protected boolean beforeSave (boolean newRecord){
		
		
		// Verifica que en la misma guia no esté montado ese pedido
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM XX_VLO_DETAILDISPATCHGUIDE WHERE XX_VMR_ORDER_ID=" + getXX_VMR_Order_ID() +
						" AND XX_VLO_DISPATCHGUIDE_ID=" + getXX_VLO_DispatchGuide_ID();
		try{
			pstmt = DB.prepareStatement(sql, get_Trx());
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				log.saveError("Error",Msg.getMsg(getCtx(),"El pedido ya se encuentra montado en la guía de despacho"));
				return false;	
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.fine(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
				
		if(getXX_TypeDetailDispatchGuide().equals(X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()) && getXX_VMR_Order_ID()==0){
			log.saveError("Error",Msg.getMsg(getCtx(),"XX_MustSelectPlaceOrder"));
			return false;
		}
		
		
		if(getM_MovementT_ID()!=0 && duplicatedTransfer(getM_MovementT_ID(), get_ID())){
			log.saveError("Error",Msg.getMsg(getCtx(),"XX_DuplicatedTransfer"));
			return false;
		}
		
		
		X_XX_VLO_DispatchGuide dispatchGuide = new X_XX_VLO_DispatchGuide(getCtx(), getXX_VLO_DispatchGuide_ID(), get_Trx()); 
		Integer XX_PLACEDORDERPACKAGES = null;
		Integer XX_PACKAGESRECEIVED = null;
		Integer XX_PACKAGESSENT = null;
		Integer XX_QUANTITYRECEIVED = null;
		Integer XX_QUANTITYSENT = null;	
		String XX_TYPEDETAILDISPATCHGUIDE = null;
		
		
		
		Integer warehouse = new Integer(getCtx().getContextAsInt("#M_Warehouse_ID"));
		//Integer warehouseCD = new Integer(getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"));
		
		/*AGREGADO GHUCHET - Se obtiene el  CD asociado a la distribución del pedido -PROYECTO CD VALENCIA*/
		MVMROrder pedidoAux = new MVMROrder(getCtx(), getXX_VMR_Order_ID(), null);
		int warehouseCD =Utilities.obtenerDistribucionCD(pedidoAux.getXX_VMR_DistributionHeader_ID());
		/*FIN AGREGADO GHUCHET*/
		
		if(!warehouse.equals(warehouseCD)){
			if(getXX_TypeDetailDispatchGuide().equals(X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue())){	
				log.saveError("Error",Msg.getMsg(getCtx(),"XX_NoChangeInfoPlaced"));
				return false;
			}
		}
		
		
		
		
		sql = "SELECT * " +
					 "FROM XX_VLO_DETAILDISPATCHGUIDE DDG " +
					 "WHERE DDG.XX_VLO_DETAILDISPATCHGUIDE_ID = "+get_ID()+"";

		try{
			pstmt = DB.prepareStatement(sql, get_Trx());
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				XX_PLACEDORDERPACKAGES = rs.getInt("XX_PLACEDORDERPACKAGES");				
				XX_PACKAGESRECEIVED = rs.getInt("XX_PACKAGESRECEIVED");				
				XX_PACKAGESSENT = rs.getInt("XX_PACKAGESSENT");				
				XX_QUANTITYRECEIVED = rs.getInt("XX_QUANTITYRECEIVED");				
				XX_QUANTITYSENT = rs.getInt("XX_QUANTITYSENT");	
				XX_TYPEDETAILDISPATCHGUIDE = rs.getString("XX_TYPEDETAILDISPATCHGUIDE");	
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
			log.fine(e.getMessage());
		}
		
		//X_XX_VLO_DetailDispatchGuide detalleDispatchGuide = new X_XX_VLO_DetailDispatchGuide(getCtx(), get_ID(), get_TrxName());
		
		if(!newRecord){
			if(XX_TYPEDETAILDISPATCHGUIDE.equals(X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue())){
				X_XX_VLO_DispatchGuide dispatchGuide1 = new X_XX_VLO_DispatchGuide(getCtx(), getXX_VLO_DispatchGuide_ID(), get_Trx());
				
				try{
					String sqlUpdateCabecera = "update XX_VLO_DispatchGuide set     XX_TotalPackages = XX_TotalPackages - " + XX_PLACEDORDERPACKAGES +
					  							" ,XX_TotalPackagesSent = XX_TotalPackagesSent - " + XX_PACKAGESSENT +
					  							" ,XX_TotalPackagesReceive = XX_TotalPackagesReceive - " + XX_PACKAGESRECEIVED +
					  							" where XX_VLO_DispatchGuide_ID = " + dispatchGuide1.getXX_VLO_DispatchGuide_ID();

					DB.executeUpdate(get_Trx(),sqlUpdateCabecera);
					
				}catch (Exception e) {
					log.log(log.getLevel(), e.getMessage());
					e.printStackTrace();
				}
				
				/*dispatchGuide1.setXX_TotalPackages(dispatchGuide1.getXX_TotalPackages() - XX_PLACEDORDERPACKAGES);
				dispatchGuide1.setXX_TotalPackagesSent(dispatchGuide1.getXX_TotalPackagesSent() - XX_PACKAGESSENT);
				dispatchGuide1.setXX_TotalPackagesReceive(dispatchGuide1.getXX_TotalPackagesReceive() - XX_PACKAGESRECEIVED);
				dispatchGuide1.save();*/
				
				X_XX_VLO_Travel travel = new X_XX_VLO_Travel(getCtx(), dispatchGuide.getXX_VLO_Travel_ID(), get_Trx());
				travel.setXX_TotalPackages(travel.getXX_TotalPackages() - XX_PLACEDORDERPACKAGES);
				travel.setXX_TotalPackagesSent(travel.getXX_TotalPackagesSent() - XX_PACKAGESSENT);
				travel.setXX_TotalPackagesReceive(travel.getXX_TotalPackagesReceive() - XX_PACKAGESRECEIVED);
				travel.save();
			}else{
				X_XX_VLO_DispatchGuide dispatchGuide1 = new X_XX_VLO_DispatchGuide(getCtx(), getXX_VLO_DispatchGuide_ID(), get_Trx());
				
				try{
					String sqlUpdateCabecera = "update XX_VLO_DispatchGuide set     XX_TotalPackages = XX_TotalPackages - " + XX_QUANTITYSENT +
						" ,XX_TotalPackagesSent = XX_TotalPackagesSent - " + XX_QUANTITYSENT +
						" ,XX_TotalPackagesReceive = XX_TotalPackagesReceive - " + XX_QUANTITYRECEIVED +
						" where XX_VLO_DispatchGuide_ID = " + dispatchGuide1.getXX_VLO_DispatchGuide_ID();

					DB.executeUpdate(get_Trx(),sqlUpdateCabecera);
					
				}catch (Exception e) {
					log.log(log.getLevel(), e.getMessage());
					e.printStackTrace();
				}
				
				/*dispatchGuide1.setXX_TotalPackages(dispatchGuide1.getXX_TotalPackages() - XX_QUANTITYSENT);
				dispatchGuide1.setXX_TotalPackagesSent(dispatchGuide1.getXX_TotalPackagesSent() - XX_QUANTITYSENT);
				dispatchGuide1.setXX_TotalPackagesReceive(dispatchGuide1.getXX_TotalPackagesReceive() - XX_QUANTITYRECEIVED);
				dispatchGuide1.save();*/
				
				X_XX_VLO_Travel travel = new X_XX_VLO_Travel(getCtx(), dispatchGuide.getXX_VLO_Travel_ID(), get_Trx());
				travel.setXX_TotalPackages(travel.getXX_TotalPackages() - XX_QUANTITYSENT);
				travel.setXX_TotalPackagesSent(travel.getXX_TotalPackagesSent() - XX_QUANTITYSENT);
				travel.setXX_TotalPackagesReceive(travel.getXX_TotalPackagesReceive() - XX_QUANTITYRECEIVED);
				travel.save();
			}		
		}
		
		X_XX_VLO_Travel travel = new X_XX_VLO_Travel(getCtx(), dispatchGuide.getXX_VLO_Travel_ID(), get_Trx());
		
		X_XX_VLO_DetailDispatchGuide detailDispatchGuide = null;
		X_XX_VMR_Order pedido = null;
		X_XX_VLO_OrderDetailPackage detallePedido = null;
		
		
		
		/*
		 * Inicio de carga de bultos de pedidos a mano
		 * 
		 * */
		//inicializo
		if(getXX_TypeDetailDispatchGuide().equals(X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue())){
			if(get_ValueOld("XX_PackagesSent") == null || !get_ValueOld("XX_PackagesSent").equals(getXX_PackagesSent())){
	
				Vector<Integer> bultosID = buscarBultosID(getXX_VMR_Order_ID()+"", dispatchGuide.get_ID());
	
				/*if (!newRecord) {
					if(dispatchGuide.getXX_DispatchGuideStatus().equals(X_Ref_XX_Ref_DispatchGuideStatus.PENDIENTE.getValue())){
						//travel = new X_XX_VLO_Travel(Env.getCtx(), dispatchGuide.getXX_VLO_Travel_ID(), get_TrxName());
						//travel.setXX_TotalPackagesSent(travel.getXX_TotalPackagesSent()-bultosID.size());
						//travel.save();
					}
				}*/
				
				for (int k = 0; k < bultosID.size() ; k++) {
					detallePedido = new X_XX_VLO_OrderDetailPackage(Env.getCtx(), bultosID.elementAt(k) , get_Trx());
					detallePedido.setXX_OrderDetailPackageStatus(X_Ref_XX_Ref_OrderDetailPackageStatus.PENDIENTE.getValue());
					detallePedido.setXX_VLO_DispatchGuide_ID(-1);
					detallePedido.save();	
				}
				detallePedido = null;
				detailDispatchGuide = null;
				//termino inicializacion
		
				
				//dispatchGuide.setXX_TotalPackagesSent(dispatchGuide.getXX_TotalPackagesSent() + getXX_PackagesSent());
				//travel = new X_XX_VLO_Travel(Env.getCtx(), dispatchGuide.getXX_VLO_Travel_ID(), get_TrxName());
				//travel.setXX_TotalPackagesSent(travel.getXX_TotalPackagesSent() + getXX_PackagesSent());
				//travel.save();
				
				Vector<Integer> bultosID1 = buscarBultosIDNulos(getXX_VMR_Order_ID()+"");
				X_XX_VLO_OrderDetailPackage auxPaquete = null;
				X_XX_VMR_Order auxPedido = null;
				//String codeNumber = new String();
				
				
				if(getXX_PackagesSent() > bultosID1.size()){
					log.saveError("Error",Msg.getMsg(getCtx(),"XX_PackageSendLess"));
					return false;
				}			
				if(getXX_PackagesSent() > getXX_PlacedOrderPackages()){
					log.saveError("Error",Msg.getMsg(getCtx(),"XX_PlacedOrderPackage"));
					return false;
				}
				for(int i=0; i<getXX_PackagesSent(); i++){
					auxPaquete = new X_XX_VLO_OrderDetailPackage(Env.getCtx(), bultosID1.elementAt(i), get_Trx());
					auxPedido = new X_XX_VMR_Order(getCtx(), auxPaquete.getXX_VMR_Order_ID(), get_Trx());
					
					//codeNumber = auxPedido.getXX_OrderBecoCorrelative();
					String placedOrder = auxPedido.getXX_OrderBecoCorrelative();   
					String orderDetailPackage = bultosID1.elementAt(i).toString();      
					
					//pedido = new X_XX_VMR_Order(Env.getCtx(), new Integer(buscarPedidoID(placedOrder)), get_TrxName());
					
					/*detailDispatchGuide = new X_XX_VLO_DetailDispatchGuide(Env.getCtx(), new Integer (buscarDetalleDispatchGuideConPedido(""+pedido.get_ID(), dispatchGuide.get_ID() )) , null);
					detailDispatchGuide.setXX_PackagesSent(detailDispatchGuide.getXX_PackagesSent()+1);
					detailDispatchGuide.save();*/
					
					detallePedido = new X_XX_VLO_OrderDetailPackage(Env.getCtx(),new Integer(orderDetailPackage) , get_Trx());
					detallePedido.setXX_VLO_DispatchGuide_ID(dispatchGuide.get_ID());
					detallePedido.setXX_OrderDetailPackageStatus(X_Ref_XX_Ref_OrderDetailPackageStatus.APROBADO.getValue());
					detallePedido.save();			
				}
			}
		}
		/*
		 * Fin de carga de bultos de pedidos a mano
		 * 
		 * */
		
		if(getXX_TypeDetailDispatchGuide().equals(X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue())){
			if(travel.getXX_TotalPackages()+getXX_PlacedOrderPackages() > travel.getXX_EquivalentPackageQuantity()){
				log.saveError("Error",Msg.getMsg(getCtx(),"XX_PlacedOrderBig"));
				return true;	
			}
		}else if(getXX_TypeDetailDispatchGuide().equals(X_Ref_XX_Ref_TypeDispatchGuide.OTHER_GOODS.getValue())){
			if(travel.getXX_TotalPackages()+getXX_QuantitySent() > travel.getXX_EquivalentPackageQuantity()){
				log.saveError("Error",Msg.getMsg(getCtx(),"XX_OtherGoodBig"));
				return true;	
			}			
		}
		
		return true;
	}
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success){
		
		
		if(success){
			if(getXX_TypeDetailDispatchGuide().equals(X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue())){
				
				X_XX_VLO_DispatchGuide dispatchGuide = new X_XX_VLO_DispatchGuide(getCtx(), getXX_VLO_DispatchGuide_ID(), get_Trx());
				
				try{
					String sqlUpdateCabecera = "update XX_VLO_DispatchGuide set     XX_TotalPackages = XX_TotalPackages + " + getXX_PlacedOrderPackages() +  
					  							" ,XX_TotalPackagesSent = XX_TotalPackagesSent + " + getXX_PackagesSent() +
					  							" ,XX_TotalPackagesReceive = XX_TotalPackagesReceive +  " + getXX_PackagesReceived() +
					  							" where XX_VLO_DispatchGuide_ID = " + dispatchGuide.getXX_VLO_DispatchGuide_ID();
					DB.executeUpdate(get_Trx(),sqlUpdateCabecera);

				}catch (Exception e) {
					log.log(log.getLevel(), e.getMessage());
					e.printStackTrace();
				}
				
				/*dispatchGuide.setXX_TotalPackages(dispatchGuide.getXX_TotalPackages()+getXX_PlacedOrderPackages());
				dispatchGuide.setXX_TotalPackagesSent(dispatchGuide.getXX_TotalPackagesSent()+getXX_PackagesSent());
				dispatchGuide.setXX_TotalPackagesReceive(dispatchGuide.getXX_TotalPackagesReceive()+getXX_PackagesReceived());
				dispatchGuide.save();*/
				
				X_XX_VLO_Travel travel = new X_XX_VLO_Travel(getCtx(), dispatchGuide.getXX_VLO_Travel_ID(), get_Trx());
				travel.setXX_TotalPackages(travel.getXX_TotalPackages()+getXX_PlacedOrderPackages());
				travel.setXX_TotalPackagesSent(travel.getXX_TotalPackagesSent()+getXX_PackagesSent());
				travel.setXX_TotalPackagesReceive(travel.getXX_TotalPackagesReceive()+getXX_PackagesReceived());
				travel.save();
				
			}else{
				X_XX_VLO_DispatchGuide dispatchGuide = new X_XX_VLO_DispatchGuide(getCtx(), getXX_VLO_DispatchGuide_ID(), get_Trx());
				
				try{
					String sqlUpdateCabecera = "update XX_VLO_DispatchGuide set     XX_TotalPackages = XX_TotalPackages + " +  getXX_QuantitySent() +
					  							" ,XX_TotalPackagesSent = XX_TotalPackagesSent + " + getXX_QuantitySent() +
					  							" ,XX_TotalPackagesReceive = XX_TotalPackagesReceive + " + getXX_QuantityReceived() +
				   								" where XX_VLO_DispatchGuide_ID = " + dispatchGuide.getXX_VLO_DispatchGuide_ID();

					DB.executeUpdate(get_Trx(),sqlUpdateCabecera);
					
				}catch (Exception e) {
					log.log(log.getLevel(), e.getMessage());
					e.printStackTrace();
				}			
				/*dispatchGuide.setXX_TotalPackages(dispatchGuide.getXX_TotalPackages()+getXX_QuantitySent());
				dispatchGuide.setXX_TotalPackagesSent(dispatchGuide.getXX_TotalPackagesSent()+getXX_QuantitySent());
				dispatchGuide.setXX_TotalPackagesReceive(dispatchGuide.getXX_TotalPackagesReceive()+getXX_QuantityReceived());
				dispatchGuide.save();*/
				
				X_XX_VLO_Travel travel = new X_XX_VLO_Travel(getCtx(), dispatchGuide.getXX_VLO_Travel_ID(), get_Trx());
				travel.setXX_TotalPackages(travel.getXX_TotalPackages()+getXX_QuantitySent());
				travel.setXX_TotalPackagesSent(travel.getXX_TotalPackagesSent()+getXX_QuantitySent());
				travel.setXX_TotalPackagesReceive(travel.getXX_TotalPackagesReceive()+getXX_QuantityReceived());
				travel.save();
			}
		}
		return true;
	}
	
	private boolean duplicatedTransfer(int transfer, int detailID){
		
		String SQL = "SELECT * " +
					 "FROM XX_VLO_DetailDispatchGuide " +
					 "WHERE XX_VLO_DetailDispatchGuide_ID<> " + detailID + " AND M_MOVEMENTT_ID = " + transfer;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				return true;
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return false;
	}

}
