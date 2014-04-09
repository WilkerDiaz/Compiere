package compiere.model.cds;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MRequisitionApproval extends X_XX_RequisitionApproval {

	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param XX_RequisitionApproval_ID id
	 *	@param trxName transaction
	 */
	public MRequisitionApproval(Ctx ctx, int XX_RequisitionApproval_ID,	Trx trx) 
	{ 
		super(ctx, XX_RequisitionApproval_ID, trx);
	}//M_XX_RequisitionApproval_ID

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MRequisitionApproval(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		BigDecimal limit = getXX_Limit();
		BigDecimal limitTotal  = getXX_LimitTotal();
		//Integer auxT = 0;
		BigDecimal aux, aux1, auxT;
		BigDecimal val1 = new BigDecimal(100);
		
		X_XX_RequisitionApproval req = new X_XX_RequisitionApproval(getCtx(), 0, get_Trx());
		
		
		if (!newRecord)
		{
			//if (limit > limitTotal)
			if(limit.compareTo(limitTotal) == 1)
			{
				//aux = ((limit * 100) / limitTotal) - 100 ;
			
				
				aux = limit.multiply(val1);
				aux1 = aux.divide(limitTotal, 2, RoundingMode.HALF_UP); 
				auxT = aux1.subtract(val1); 
				
				if (auxT.compareTo(new BigDecimal(10)) > -1 && auxT.compareTo(new BigDecimal(30)) < 1)
				{
					log.saveError("Info", Msg.getMsg(getCtx(), "XX_MeRequisitionApproval"));
					req.setIsApproved(false);
				}
				else if (auxT.compareTo(new BigDecimal(30)) == 0)
				{
					log.saveError("Info", Msg.getMsg(getCtx(), "XX_MeRequisitionApproval01"));
				}
				else if (auxT.compareTo(new BigDecimal(30)) == 1)
				{
					log.saveError("Info", Msg.getMsg(getCtx(), "XX_MeRequisitionApproval02"));
				}
			}
			else
			{
				log.saveError("Info", Msg.getMsg(getCtx(), "XX_MeRequisitionApproval03"));
				return true;
				
			}
		 return false;
		}
		else
			return true;
				
	}

}