package compiere.model.cds.callouts;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;

import org.compiere.model.CalloutEngine;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MWarehouse;

import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;

import compiere.model.cds.MMovement;
import compiere.model.cds.X_Ref_XX_Ref_TypeDispatchGuide;
import compiere.model.cds.X_XX_VLO_Fleet;
import compiere.model.cds.X_XX_VMR_Order;

/**
 * @author Jorge E. Pires G.
 */
public class VLO_DispatchGuideCallout extends CalloutEngine {

	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());
	
	
	public String TypeDetailDispatch(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		GridField campoTipoDetalle = mTab.getField("XX_TypeDetailDispatchGuide");
		if (campoTipoDetalle.getValue() != null){
			mTab.setValue("XX_VMR_Order_ID", null);
		}		
		
		return "";
	}
	
	public String fleet(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		if (isCalloutActive() || value==null)
			return "";
		
		GridField campoFleet = 	mTab.getField("XX_VLO_Fleet_ID");
		if (campoFleet.getValue() != null){
			X_XX_VLO_Fleet fleet = new X_XX_VLO_Fleet(ctx, (Integer)campoFleet.getValue(), null);
			
			mTab.setValue("XX_Assistant1_ID", fleet.getXX_Assistant1_ID());
			mTab.setValue("XX_Assistant2_ID", fleet.getXX_Assistant2_ID());
			mTab.setValue("XX_Driver_ID", fleet.getXX_Driver_ID());	
		}		
		
		return "";
	}
	
	public String order(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		GridField campoPedido = 	mTab.getField("XX_VMR_Order_ID");
		if (campoPedido.getValue() != null){
			X_XX_VMR_Order pedido = new X_XX_VMR_Order(ctx, (Integer)campoPedido.getValue(), null);
			
			mTab.setValue("XX_PlacedOrderPackages", pedido.getXX_PackageQuantity());
			//mTab.setValue("XX_PackagesReceived", pedido.getXX_PackageQuantity());
			mTab.setValue("XX_PackagesSent", pedido.getXX_PackageQuantity());
			// inicio VLOMONACO
			// muestra la cantidad de bultos restantes por enviar
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			int bultosRestantes = 0;
			
			String sql = "SELECT count(*) " +
						 "FROM XX_VLO_ORDERDETAILPACKAGE DP " +
						 "WHERE DP.XX_VMR_ORDER_ID = " +pedido.getXX_VMR_Order_ID()+ " " +
						 "AND DP.XX_VLO_DISPATCHGUIDE_ID IS NULL ";
			
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					bultosRestantes = rs.getInt(1);
				}

			} catch (Exception e) {
				log.fine(e.getMessage());
			}finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			mTab.setValue("XX_RemainingPackages", bultosRestantes);
		}
		
		return "";
	}
	
	// vlomonaco
	// agrega la fecha de regreso del camion
	public String setDateAfter(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){

		Calendar cal = Calendar.getInstance();
	    Timestamp t = new Timestamp(cal.getTime().getTime());
		mTab.setValue("XX_DateAfterDispatch", t);
		
		return "";
	}
	
	public String Movement(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){

		//GridField campoMovimiento = mTab.getField("M_Movement_ID");
		if (mTab.getValue("M_Movement_ID") != null){
			MMovement movimiento = new MMovement(ctx, (Integer)mTab.getValue("M_Movement_ID"), null);
			
			mTab.setValue("XX_QuantitySent", movimiento.getXX_PackageQuantity());
		}
		
		return "";
	}

	public String warehouse(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
	
		if (isCalloutActive() || value==null)
			return "";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		GridField campoArrivalWare = mTab.getField("XX_ArrivalWarehouse_ID");
		GridField campoDepaturWare = mTab.getField("XX_DepartureWarehouse_ID");
		
		MWarehouse tiendaOrigen = new MWarehouse(ctx, (Integer) campoDepaturWare.getValue(), null);
		MWarehouse tiendaDestino = new MWarehouse(ctx, (Integer) campoArrivalWare.getValue(), null);
		// si el warehouse de salida y el de llegada son centros de 
		if (tiendaOrigen.getName().contains("CENTRO") && tiendaDestino.getName().contains("CENTRO"))
			
		
		
		if (isCalloutActive() || campoArrivalWare.getValue() == null || campoDepaturWare.getValue() == null)
			return "";
		setCalloutActive(true);
		
		if (campoArrivalWare.getValue() != null && campoDepaturWare.getValue() != null){
			String sql = "SELECT CT.XX_CIRCUITKILOMETERS " +
						 "FROM XX_VLO_CIRCUITTOUR CT " +
						 "WHERE " +
						 "((CT.XX_DEPARTUREWAREHOUSE_ID = " +campoDepaturWare.getValue()+ " "+
						 "AND CT.XX_ARRIVALWAREHOUSE_ID = "+campoArrivalWare.getValue()+ ") OR " +
						 "(CT.XX_DEPARTUREWAREHOUSE_ID = " +campoArrivalWare.getValue()+ " "+
						 "AND CT.XX_ARRIVALWAREHOUSE_ID = "+campoDepaturWare.getValue()+ ")) "+
						 "AND CT.ISACTIVE = 'Y'";
			
			try{
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				
				BigDecimal auxKilometer = null;
				
				if(rs.next()){
					auxKilometer = rs.getBigDecimal("XX_CIRCUITKILOMETERS");
					mTab.setValue("XX_KilometersPlanned", new BigDecimal(auxKilometer.doubleValue()));
				}
			
			}catch (Exception e) {
				log.fine(Msg.getMsg(ctx, "Error"));
				setCalloutActive(false);
			}
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}		
		
		setCalloutActive(false);
		return "";
	}
}
