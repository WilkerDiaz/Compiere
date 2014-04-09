package org.compiere.model;

import java.math.BigDecimal;

import org.compiere.util.Ctx;

public class CalloutConfirm extends CalloutEngine {

	/**
	 *	M_InOutLineConfirm, M_MovementLineConfirm - qty.
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab model
	 *	@param mField field model
	 *	@param value new value
	 *	@return error message or ""
	 */
	public String qty (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		
		setCalloutActive(true);
		
		 if (mField.getColumnName().equals("ScrappedQty"))
		 {
			 BigDecimal ScrappedQty = (BigDecimal)value;
			 BigDecimal TargetQty = (BigDecimal)mTab.getValue("TargetQty");
			 BigDecimal ConfirmedQty = (BigDecimal)mTab.getValue("ConfirmedQty");
			 
			 mTab.setValue("DifferenceQty", TargetQty.subtract(ConfirmedQty).subtract(ScrappedQty));
		 }
		 else if (mField.getColumnName().equals("ConfirmedQty"))
		 {
			 BigDecimal ConfirmedQty = (BigDecimal)value;
			 BigDecimal TargetQty = (BigDecimal)mTab.getValue("TargetQty");
			 BigDecimal ScrappedQty = (BigDecimal)mTab.getValue("ScrappedQty");
			 
			 mTab.setValue("DifferenceQty", TargetQty.subtract(ConfirmedQty).subtract(ScrappedQty));
		 }
		 
		setCalloutActive(false);
		return "";
	}	//	asi
}
