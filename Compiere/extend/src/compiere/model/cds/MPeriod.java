package compiere.model.cds;

import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.model.MYear;
import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MPeriod extends org.compiere.model.MPeriod{

	public MPeriod(MYear year, int PeriodNo, String name, Timestamp startDate,
			Timestamp endDate) {
		super(year, PeriodNo, name, startDate, endDate);
		// TODO Auto-generated constructor stub
	}

	public MPeriod(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MPeriod(Ctx ctx, int C_Period_ID, Trx trx) {
		super(ctx, C_Period_ID, trx);
		// TODO Auto-generated constructor stub
	}

	
	  public void setXX_CloseSalePurchase (boolean XX_CloseSalePurchase)
	  {
	        set_Value ("XX_CloseSalePurchase", XX_CloseSalePurchase);
	        
	  }
	 
	  public Boolean getXX_CloseSalePurchase() 
	  {
	     return (Boolean)get_Value("XX_CloseSalePurchase");
	  }
	
}
