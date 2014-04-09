package compiere.model.payments.callout;

import java.math.BigDecimal;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;

public class XX_PayScheduleCallout extends CalloutEngine{
	
	/**
	 * Verifica si la suma entre el porcentaje y el porcentaje restante 1 es menor al 100%
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @param oldValue
	 * @return			compiere.model.payments.callout.XX_PayScheduleCallout.optionThree
	 */
	public String optionThree(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue) {
		BigDecimal percentageTwo = (BigDecimal) mField.getValue();
		if (!(percentageTwo.compareTo(new BigDecimal(0)) == 0)){
			BigDecimal percentageOne = (BigDecimal) mTab.getValue("Percentage");			
			if ((percentageOne.add(percentageTwo)).compareTo(new BigDecimal(100)) == -1){
				mTab.setValue("XX_IsThreeOption", "Y");
			}                                                                                                                            
		}
		return "";
	}
	
	/**
	 * Valida si la sumatoria de los tres porcentajes es igual al 100%
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @param oldValue
	 * @return				compiere.model.payments.callout.XX_PayScheduleCallout.validateSumPercentage
	 */
	public String validateSumPercentage(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue) {		
		BigDecimal percentageThree = (BigDecimal) mField.getValue();
		if (!(percentageThree.compareTo(new BigDecimal(0)) == 0)){
			BigDecimal percentageOne = (BigDecimal) mTab.getValue("Percentage");
			BigDecimal percentageTwo = (BigDecimal) mTab.getValue("XX_PercentageRemainingTwo");
			BigDecimal sum = new BigDecimal(0);
			sum = percentageOne.add(percentageTwo).add(percentageThree);
			if (sum.compareTo(new BigDecimal(100)) != 0){
				percentageThree = (percentageOne.add(percentageTwo)).subtract(new BigDecimal(100));
				if (percentageThree.compareTo(new BigDecimal(0)) == -1)
					mTab.setValue("XX_PercentageRemainingThree", percentageThree.multiply(new BigDecimal(-1)));
				else
					mTab.setValue("XX_PercentageRemainingThree", percentageThree);
			}
		}
		return "";
	}

}
