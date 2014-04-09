package compiere.model.cds;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

import compiere.model.tag.X_XX_VCN_TradeAgrDepartment;

public class MVCNTradeAgrDepartment extends X_XX_VCN_TradeAgrDepartment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPartner.class);
	
	public MVCNTradeAgrDepartment(Ctx ctx, ResultSet rs, Trx trxName) {
		super(ctx, rs, trxName);
	}
	public MVCNTradeAgrDepartment(Ctx ctx, int XX_VCN_TradeAgrDepartment_ID,
			Trx trxName) {
		super(ctx, XX_VCN_TradeAgrDepartment_ID, trxName);
	}

	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if(newRecord){
			String SQL= "SELECT * " +
					"FROM XX_VCN_TRADEAGRDEPARTMENT " +
					"WHERE XX_VCN_TradeAgreements_ID="+getXX_VCN_TradeAgreements_ID()+  
					" AND XX_DEPARTAMENTO_ID="+getXX_DEPARTAMENTO_ID()+
					" AND AD_CLIENT_ID="+  getCtx().getAD_Client_ID();
			PreparedStatement prst = DB.prepareStatement(SQL,null);
			ResultSet rs = null;
			try {
				rs = prst.executeQuery();
				if(rs.next()){
					ADialog.error(1, new Container(), "XX_TradeDepartment");
					return false;
				}
				rs.close();
				prst.close();
			} catch (SQLException e){
				log.log(Level.SEVERE, e.getMessage());
				return false;
			}
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(prst);	
			}
			
			
			String SQL2= "SELECT XX_VMR_CATEGORY_ID " +
			"FROM XX_VMR_DEPARTMENT " +
			"WHERE XX_VMR_DEPARTMENT_ID="+getXX_DEPARTAMENTO_ID()+
			" AND AD_CLIENT_ID="+  getCtx().getAD_Client_ID();
			PreparedStatement prst2 = DB.prepareStatement(SQL2,null);
			
			try {
				rs = prst2.executeQuery();
				if(rs.next()){
					setXX_VMR_Category_ID(rs.getInt(1));
				}
			} catch (SQLException e){
				log.log(Level.SEVERE, e.getMessage());
				return false;
			}
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(prst2);	
			}
		}
			return true;

	}
	
	@Override
	protected boolean beforeDelete()
	{
		//Solo se puede borrar cuando sea status BORRADOR
		X_XX_VCN_TradeAgreements agreement = new X_XX_VCN_TradeAgreements(getCtx(), getXX_VCN_TradeAgreements_ID(), get_Trx());
		if(agreement.getXX_Status().equals("BORRADOR"))
			return true;
		else{
			ADialog.error(1, new Container(), "XX_TradeAgreementEraseError");
			return false;
		}
		
	}
	
	

}
