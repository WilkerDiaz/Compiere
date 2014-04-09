package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;


public class MVMRBudgetSalesDepart extends X_XX_VMR_BudgetSalesDepart{
	
	private static final long serialVersionUID = 1L;
	
	public MVMRBudgetSalesDepart (Ctx ctx, int ComercialBudget_ID, Trx trx)
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
	
	public MVMRBudgetSalesDepart (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
	
	protected boolean beforeSave(boolean newRecord)
	{
		
		
		try
		{

			String sql = "select name from XX_VMR_Department where XX_VMR_Department_ID=" + getXX_VMR_Department_ID();
			String sql2 = "select name from C_Period where C_Period_ID = (select C_Period_ID from XX_VMR_RealComercialBudget where XX_VMR_RealComercialBudget_ID=" + getXX_VMR_RealComercialBudget_ID() + ")";

			PreparedStatement Pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = Pstmt.executeQuery();
			PreparedStatement Pstmt2 = DB.prepareStatement(sql2, null);
			ResultSet rs2 = Pstmt2.executeQuery();

			if (rs.next() & rs2.next())
			{
				setName(rs.getString(1) + " -- " + rs2.getString(1));
			}
			rs.close();
			Pstmt.close();
			rs2.close();
			Pstmt2.close();
		}catch (Exception e) {
			System.out.println(e);
			return false;
		}
	return true;
	}

}

