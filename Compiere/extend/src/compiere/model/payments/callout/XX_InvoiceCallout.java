package compiere.model.payments.callout;

import java.util.Calendar;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;

/**
 * 
 * @author Jessica Mendoza
 *
 */
public class XX_InvoiceCallout extends CalloutEngine {
	
	/**
	 * Se encarga de validar si el año introducido es igual al año actual o menor un año al actual
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @param oldValue
	 * 					compiere.model.payments.callout.XX_InvoiceCallout.validateYear
	 * @return			
	 */
	public String validateYear(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
		Calendar cal = Calendar.getInstance();
		Integer yearFac = (Integer)mField.getValue();
		Integer year = cal.get(Calendar.YEAR);
		int longYear = String.valueOf(year).length();
		Integer longValue = String.valueOf(yearFac).length();
		Integer aux = new Integer(1);

		if (yearFac == 0)
			return "";
		else if (yearFac == year)
			return "";
		else{
			if (longYear == longValue){
				if (yearFac != year){
					aux = year-aux;
					if (yearFac.compareTo(aux)!=0){
						mField.setValue(year, true);
					}
				}
			}else
				mTab.setValue("XX_Year", year);
		}

		return "";
	}
	
	/*
	 * Setea el tipo de Proveedor y selecciona VEB si es Nacional 
	 */
	public String vendorClass(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
		
		mTab.setValue("XX_VendorClass", "Nacional");

		return "";
	}

}
