package compiere.model.cds;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.tag.X_XX_VCN_TradeAgrCategory;
import compiere.model.tag.X_XX_VCN_TradeAgrDepartment;

public class MVCNTradeAgrCategory extends X_XX_VCN_TradeAgrCategory {
	
	public MVCNTradeAgrCategory(Ctx ctx, ResultSet rs, Trx trxName) {
		super(ctx, rs, trxName);
	}
	public MVCNTradeAgrCategory(Ctx ctx, int XX_VCN_TradeAgrCategory_ID,
			Trx trxName) {
		super(ctx, XX_VCN_TradeAgrCategory_ID, trxName);
	}


	private static final long serialVersionUID = 1L;

	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPartner.class);
	
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if(newRecord){
			String SQL= "SELECT * " +
					"FROM XX_VCN_TRADEAGRCATEGORY " +
					"WHERE XX_VCN_TradeAgreements_ID="+getXX_VCN_TradeAgreements_ID()+
					" AND XX_VMR_CATEGORY_ID="+getXX_VMR_Category_ID()+
					" AND AD_CLIENT_ID="+  getCtx().getAD_Client_ID();
			PreparedStatement pstmt = DB.prepareStatement(SQL,null);
			ResultSet rs = null;
			try {
				rs = pstmt.executeQuery();
				if(rs.next()){
					ADialog.error(1, new Container(), "XX_TradeCategoryError");
					return false;
				}
			} catch (SQLException e){
				log.log(Level.SEVERE, e.getMessage());
				return false;
			}finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);	
			}
		}
		return true;
		
	}
	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if(newRecord){
			String sql ="SELECT XX_VMR_DEPARTMENT_ID " +
						"FROM XX_VMR_DEPARTMENT " +
						"WHERE isactive = 'Y' AND XX_VMR_CATEGORY_ID="+getXX_VMR_Category_ID()+
						"MINUS SELECT XX_DEPARTAMENTO_ID " +
						"FROM XX_VCN_TRADEAGRDEPARTMENT " +
						"WHERE XX_VCN_TradeAgreements_ID="+getXX_VCN_TradeAgreements_ID() + 
						" AND AD_CLIENT_ID="+Env.getCtx().getAD_Client_ID();
												
			PreparedStatement pstmt = DB.prepareStatement(sql,null);
			ResultSet rs = null;
			
			try {
				
				rs = pstmt.executeQuery();
				//Se guardan todos los departamentas asociados a esa categoría
				while (rs.next())
				{
					X_XX_VMR_Department department = new X_XX_VMR_Department(Env.getCtx(), rs.getInt(1), null);
					X_XX_VCN_TradeAgrDepartment tradeAgrDep = new X_XX_VCN_TradeAgrDepartment(Env.getCtx(), 0, null);
					tradeAgrDep.setXX_DEPARTAMENTO_ID(department.getXX_VMR_Department_ID());
					tradeAgrDep.setXX_VCN_TradeAgreements_ID(getXX_VCN_TradeAgreements_ID());
					tradeAgrDep.setXX_VMR_Category_ID(getXX_VMR_Category_ID());
					tradeAgrDep.save();
				}
				
				} catch (SQLException e){
					log.log(Level.SEVERE, e.getMessage());
					return false;
				}
				finally{
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);	
				}
		}
		else{
			
			int tradeAgID = getXX_VCN_TradeAgreements_ID();
			int tradeCatID = getXX_VMR_Category_ID();
			String SQL= "select XX_VCN_TRADEAGRDEPARTMENT_ID " +
					"from XX_VCN_TRADEAGRDEPARTMENT " +
					"where XX_VCN_TradeAgreements_ID="+tradeAgID + 
					" and XX_VMR_CATEGORY_ID="+tradeCatID+
					" and AD_CLIENT_ID="+Env.getCtx().getAD_Client_ID();
			PreparedStatement pstmt = DB.prepareStatement(SQL,null);
			ResultSet rs = null;
			try {
				rs = pstmt.executeQuery();
				while(rs.next()){
					X_XX_VCN_TradeAgrDepartment tradeDep = new X_XX_VCN_TradeAgrDepartment(getCtx(), rs.getInt(1), null);
					if(get_ValueAsString("IsActive").equals("N"))
						tradeDep.setIsActive(false);
					else
						tradeDep.setIsActive(true);
					tradeDep.save();
					}
					
				}
			 catch (SQLException e){
				 System.out.print(e.getMessage());
			 }
			 finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);	
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
	
	@Override
	protected boolean afterDelete (boolean success){
		if(success){
			String sql ="DELETE " +
			"FROM XX_VCN_TRADEAGRDEPARTMENT " +
			"WHERE XX_VMR_CATEGORY_ID="+getXX_VMR_Category_ID()+
			" AND XX_VCN_TradeAgreements_ID="+getXX_VCN_TradeAgreements_ID();
			
			PreparedStatement prst = DB.prepareStatement(sql,get_Trx());
			try {
				prst.executeQuery();			
				} catch (SQLException e){
					log.log(Level.SEVERE, e.getMessage());
					return false;
			}
			return true;
		}
		else return false;
	}

}

