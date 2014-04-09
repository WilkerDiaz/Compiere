package compiere.model.suppliesservices.callouts;

import java.util.Date;
import java.util.Calendar;
import java.sql.Timestamp;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;

public class RenewalCallout extends CalloutEngine {
	/** Purchase of Supplies and Services
	 * Maria Vintimilla Funcion 29**/
	
	/**
	 * Renewal. Set XX_RenewalNotificationDate according the XX_RenewalNotificationDays
	 * @param ctx context
	 * @param WindowNo window no
	 * @param mTab tab
	 * @param mField field
	 * @param value value
	 */
	public String Renewal (Ctx ctx, int WindowNo, GridTab mTab,GridField mField, Object value) {
		if (isCalloutActive() || value==null)
			return "";
		// Days for the Renewal Notification
		Integer days = (Integer) mTab.getValue("XX_RenewalNotificationDays"); 
		// Contract's Date To
		Timestamp dateTo = (Timestamp)mTab.getValue("XX_DateTo"); 
		Date aux ;
		Timestamp newValue;
		if (dateTo == null  || days == null)
			return "";

		Calendar newDate = Calendar.getInstance();  // Calendar aux
		newDate.setTime(dateTo);

		if(days >= 0){			
			newDate.add(Calendar.DAY_OF_MONTH,-days);	
			aux = newDate.getTime();
			newValue = new Timestamp(aux.getTime());
			mTab.setValue("XX_RenewalNotificationDate", newValue);
		}
		setCalloutActive(false);
		return "";
	}// Fin RenewalNotificationDate
	
	/** EndDate
	 * Funcion 108
	 * Si se modifica la fecha final del contrato, debe recalcularse la fecha de
	 * renovación del mismo	
	 * @param ctx context
	 * @param WindowNo window no
	 * @param mTab tab
	 * @param mField field
	 * @param value value
	 */
	 public String EndDate (Ctx ctx, int WindowNo, GridTab mTab,GridField mField, 
			 Object value, Object oldValue) {
		if (isCalloutActive() || value==null)
			return "";	
		// Dias para la renovacion
		Integer days = new Integer(0); 
		 
			
		// Si fecha hasta cambio, se recalcula la fecha de renovacion
		if (value != null && oldValue != null && !value.equals(oldValue)) {
			days = (Integer) mTab.getValue("XX_RenewalNotificationDays"); 
			//dateTo = (Timestamp)mTab.getValue("XX_DateTo"); 
			Date aux;
			Timestamp newValue;
			Calendar newDate = Calendar.getInstance();  // Calendar aux
			newDate.setTime((Timestamp)value);
			if(days >= 0){			
				newDate.add(Calendar.DAY_OF_MONTH,-days);	
				aux = newDate.getTime();
				newValue = new Timestamp(aux.getTime());
				mTab.setValue("XX_RenewalNotificationDate", newValue);
			}
		}// Recalculo de fecha de renovacion
		setCalloutActive(false);
		return "";
	 } // Fin EndDate

}// Fin Renewal
