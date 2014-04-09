package compiere.model.cds;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MVMRCriticalTaskForClose extends X_XX_VMR_CriticalTaskForClose{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MVMRCriticalTaskForClose(Ctx ctx,
			int XX_VMR_CriticalTaskForClose_ID, Trx trx) {
		super(ctx, XX_VMR_CriticalTaskForClose_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 	Load constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MVMRCriticalTaskForClose (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
}
