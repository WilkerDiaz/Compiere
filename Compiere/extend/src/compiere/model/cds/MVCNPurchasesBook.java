package compiere.model.cds;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

public class MVCNPurchasesBook extends X_XX_VCN_PurchasesBook {
	
	Utilities util = new Utilities();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MVCNPurchasesBook(Ctx ctx, int XX_VCN_PurchasesBook_ID,
			Trx trx) {
		super(ctx, XX_VCN_PurchasesBook_ID, trx);
		
	}
	
	public MVCNPurchasesBook (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MOrderTax
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord){
		if (!util.clientRetentionAgent(getAD_Client_ID()))
			setXX_WithholdingTax(new BigDecimal(0));
		
		if(getXX_VLO_BoardingGuide_ID() !=0 ){
			if( newRecord && existsImportForm()){
				ADialog.error(1, new Container(), "XX_ImportFormError");
				return false;
			}else{
				setXX_DATE(getCreated());
				setXX_TotalInvCost(getXX_ExemptBase().add(getXX_NotSubjectBase()).add(getXX_TaxableBase()).add(getXX_TaxAmount()));
			}
		}
		return true;		
	}
	

	@Override
	protected boolean beforeDelete() {
		
		ADialog.error(1, new Container(), "XX_CannotDelete");
		return false;
	}

	private boolean existsImportForm() {
		String sql = "select XX_VCN_PurchasesBook_ID FROM XX_VCN_PurchasesBook where XX_VLO_BoardingGuide_ID ="+getXX_VLO_BoardingGuide_ID();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			rs = pstmt.executeQuery ();
			if (rs.next ()){
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return false;
	}

}
