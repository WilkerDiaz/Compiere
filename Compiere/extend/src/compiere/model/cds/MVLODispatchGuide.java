package compiere.model.cds;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;


public class MVLODispatchGuide extends X_XX_VLO_DispatchGuide {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3361327695278389931L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);

	public MVLODispatchGuide(Ctx ctx, int XX_VLO_DetailDispatchGuide_ID,
			Trx trx) {
		super(ctx, XX_VLO_DetailDispatchGuide_ID, trx);
	}
	
	public MVLODispatchGuide(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return success
	 */
	@Override
	protected boolean beforeSave (boolean newRecord){
		X_XX_VLO_Travel travel = null;
		if(!newRecord){
			Integer viaje =null;
			String sql = "SELECT DG.XX_VLO_TRAVEL_ID " +
						 "FROM XX_VLO_DISPATCHGUIDE DG " +
						 "WHERE DG.XX_VLO_DISPATCHGUIDE_ID = "+get_ID()+"";

			try{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()){
					viaje = rs.getInt("XX_VLO_TRAVEL_ID");				
				}
				rs.close();
				pstmt.close();
			}catch (Exception e) {
				e.printStackTrace();
				log.fine(e.getMessage());
			}
			if(!getXX_DispatchGuideStatus().equals(X_Ref_XX_Ref_DispatchGuideStatus.SUGERIDO.getValue())){
				travel = new  X_XX_VLO_Travel(getCtx(), viaje, get_Trx());
				travel.setXX_TotalPackages(travel.getXX_TotalPackages() - getXX_TotalPackages());
				travel.setXX_TotalPackagesReceive(travel.getXX_TotalPackagesReceive() - getXX_TotalPackagesReceive());
				travel.setXX_TotalPackagesSent(travel.getXX_TotalPackagesSent() - getXX_TotalPackagesSent());
				travel.save();
				
				travel = new  X_XX_VLO_Travel(getCtx(), getXX_VLO_Travel_ID(), get_Trx());
				travel.setXX_TotalPackages(travel.getXX_TotalPackages() + getXX_TotalPackages());
				travel.setXX_TotalPackagesReceive(travel.getXX_TotalPackagesReceive() + getXX_TotalPackagesReceive());
				travel.setXX_TotalPackagesSent(travel.getXX_TotalPackagesSent() + getXX_TotalPackagesSent());
				travel.save();	
				
			}
		}
		
		travel = new  X_XX_VLO_Travel(getCtx(), getXX_VLO_Travel_ID(), get_Trx());
		if(travel.getXX_TotalPackages() > travel.getXX_EquivalentPackageQuantity()){
			ADialog.info(EnvConstants.WINDOW_INFO, new Container(), Msg.getMsg(getCtx(), "XX_TravelFull"));				
			return true;	
		}
		return true;
	}
	
	
	@Override
	protected boolean beforeDelete(){
		if(getXX_DispatchGuideStatus().equals(X_Ref_XX_Ref_DispatchGuideStatus.PENDIENTE.getValue())){
			X_XX_VLO_Travel travel = new  X_XX_VLO_Travel(getCtx(), getXX_VLO_Travel_ID(), get_Trx());
			
			travel.setXX_TotalPackages(travel.getXX_TotalPackages() - getXX_TotalPackages());
			travel.setXX_TotalPackagesReceive(travel.getXX_TotalPackagesReceive() - getXX_TotalPackagesReceive());
			travel.setXX_TotalPackagesSent(travel.getXX_TotalPackagesSent() - getXX_TotalPackagesSent());
			travel.save();			
			return true;
		}else{
			return false;		
		}
	}

}
