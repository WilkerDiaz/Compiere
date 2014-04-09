package compiere.model.cds.callouts;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MTab;
import org.compiere.model.X_C_Conversion_Rate;
import org.compiere.model.X_C_Period;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VCN_ReplacementFactor;

public class VCN_ReplacementFactorCallout extends CalloutEngine {

	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());
	
//	private static boolean s_calloutActive = false;
//
//	/**
//	 * 	Is Callout Active
//	 *	@return true if active
//	 */
//	protected static boolean isCalloutActive()
//	{
//		return s_calloutActive;
//	}	//	isCalloutActive
//
//	/**
//	 * 	Set Callout (in)active
//	 *	@param active active
//	 */
//	protected static void setCalloutActive (boolean active)
//	{
//		s_calloutActive = active;
//	}	//	setCalloutActive
	
	/**
	 *	Rate - set Multiply Rate from Divide Rate and vice versa
	 *	org.compiere.model.CalloutEngine.rate
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String rate (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)		//	assuming it is Conversion_Rate
			return "";
		setCalloutActive(true);

		BigDecimal rate1 = (BigDecimal)value;
		BigDecimal rate2 = Env.ZERO;
		BigDecimal one = new BigDecimal(1.0);
		BigDecimal replacementFactor = new BigDecimal(0);
		BigDecimal percentageIncrease = new BigDecimal(Env.getCtx().getContext("#XX_L_VCN_STORAGECHARGE")); 
		if (rate1.doubleValue() != 0.0)	//	no divide by zero
			rate2 = one.divide(rate1, 12, BigDecimal.ROUND_HALF_UP);
		//
		if (mField.getColumnName().equals("MultiplyRate"))
			mTab.setValue("DivideRate", rate2);
		else
			mTab.setValue("MultiplyRate", rate2);
		
		BigDecimal multiplyRate = (BigDecimal)mTab.getValue("MultiplyRate");
		
		//AGREGADO POR GHUCHET -  Cambio en fórmula de Factor de Reposición
		replacementFactor =  multiplyRate.add((multiplyRate.multiply(percentageIncrease)));

		mTab.setValue("XX_ReplacementFactor", replacementFactor);
		
		
		log.info(mField.getColumnName() + "=" + rate1 + " => " + rate2);
		setCalloutActive(false);
		return "";
	}	//	rate
}
