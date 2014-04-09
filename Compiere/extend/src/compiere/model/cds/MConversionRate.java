package compiere.model.cds;

import java.awt.Container;
import java.awt.Dialog;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.compiere.apps.ADialog;
import org.compiere.model.X_C_Period;
import org.compiere.util.Ctx;
import org.compiere.util.Env;
import org.compiere.util.Trx;

/**
 *	C_Conversion_Rate Model
 *	
 *  @author ghuchet 
 */

public class MConversionRate extends org.compiere.model.MConversionRate {

	public MConversionRate(Ctx ctx, int C_Conversion_Rate_ID, Trx trx) {
		super(ctx, C_Conversion_Rate_ID, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
		boolean save = super.afterSave(newRecord, success);

		if(save){
			if(!newRecord){
				Date time= Utilities.currentServerDate();
			  	Calendar date = Calendar.getInstance();
			 	date.setTime(time);
			  	
				X_C_Period period = new X_C_Period(Env.getCtx(),get_ValueAsInt("XX_Period_ID"), null);
			  //Si se esta en medio de un período se agrega a la tabla X_XX_VCN_ReplacementFactor el factor de reposición anterior.
				Calendar start = Calendar.getInstance();
				start.setTime(period.getStartDate());
				Calendar end = Calendar.getInstance();
				end.setTime(period.getEndDate());
				if(date.after(start) & date.before(end)){
					BigDecimal percentageIncrease = new BigDecimal(Env.getCtx().getContext("#XX_L_VCN_STORAGECHARGE")); 
					X_XX_VCN_ReplacementFactor replacementFactorOld = new X_XX_VCN_ReplacementFactor(Env.getCtx(), 0, null);
					replacementFactorOld.setC_Conversion_Rate_ID(get_ID());
					replacementFactorOld.setXX_ReplacementFactor((BigDecimal)get_ValueOld("XX_ReplacementFactor"));
					replacementFactorOld.setMultiplyRate((BigDecimal)get_ValueOld("MultiplyRate"));
					replacementFactorOld.setXX_PercentageIncrease(percentageIncrease);
					replacementFactorOld.save();
					replacementFactorOld.setValidTo(replacementFactorOld.getCreated());
					replacementFactorOld.save();
				}
			}
		}
		
		return save;

	}
	

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{	
		boolean save = super.beforeSave(newRecord);

		if(save){
			if(!newRecord){
				Date time= Utilities.currentServerDate();
			  	Calendar date = Calendar.getInstance();
			 	date.setTime(time);
			  	Integer periodID =(Integer)get_Value("XX_Period_ID");
			  	if(periodID != null){
			  		X_C_Period period = new X_C_Period(Env.getCtx(),periodID, null);
					  //Si se esta en medio de un período se agrega a la tabla X_XX_VCN_ReplacementFactor el factor de reposición anterior.
						Calendar end = Calendar.getInstance();
						end.setTime(period.getEndDate());
						if(date.after(end)){
							String msg = "No puede modificar tasas de periodos anteriores.";
							ADialog.error(1, new Container(), msg);
							return false;
						}
			  	}
				
			}
		}
		
		return save;

	}
	
}
