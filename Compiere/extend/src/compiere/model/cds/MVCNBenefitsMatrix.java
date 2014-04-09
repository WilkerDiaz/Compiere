package compiere.model.cds;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MVCNBenefitsMatrix extends X_XX_VCN_BenefitsMatrix {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -418112730451887902L;
	
	public MVCNBenefitsMatrix(Ctx ctx, int XX_VCN_BenefitsMatrix_ID,Trx trx) {
		super(ctx, XX_VCN_BenefitsMatrix_ID, trx);
		// TODO Auto-generated constructor stub
	}

	public MVCNBenefitsMatrix(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord){
		if(isXX_IsAllVendor()){
			setC_BPartner_ID(-1);
		}
		return true;
	}

}
