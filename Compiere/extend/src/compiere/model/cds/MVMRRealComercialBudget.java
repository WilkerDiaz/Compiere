package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;


public class MVMRRealComercialBudget extends X_XX_VMR_RealComercialBudget{
	
	private static final long serialVersionUID = 1L;
	
	public MVMRRealComercialBudget (Ctx ctx, int ComercialBudget_ID, Trx trx)
	{
		super (ctx, ComercialBudget_ID, trx);
		if (ComercialBudget_ID == 0)
		{
		//	setM_Requisition_ID (0);
//			setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM M_RequisitionLine WHERE M_Requisition_ID=@M_Requisition_ID@
//			setLineNetAmt (Env.ZERO);
//			setPriceActual (Env.ZERO);
//			setQty (Env.ONE);	// 1
//			setMargin (Env.ONE);			/////////////////////////////////////////////////////////////////
//			setSalePrice (Env.ONE);			/////////////////////////////////////////////////////////////////
//			setLinePVPAmount (Env.ZERO);	/////////////////////////////////////////////////////////////////
//			setLinePlusTaxAmount (Env.ZERO);	////////////////////////////////////////////////////////////
//			setPackageMultiple (Env.ONE);				///////////////////////////////////////////

		}
		
	}	
	
	public MVMRRealComercialBudget (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
	
	protected boolean beforeSave(boolean newRecord)
	{
		
		
		try
		{

			String sql = "select name from c_period where c_period_id=" + getC_Period_ID();

			PreparedStatement Pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = Pstmt.executeQuery();

			if (rs.next())
			{
				setName(rs.getString(1));
			}
			rs.close();
			Pstmt.close();
		}catch (Exception e) {
			return false;
		}
	return true;
	}

}
