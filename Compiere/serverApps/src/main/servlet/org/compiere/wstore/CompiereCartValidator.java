package org.compiere.wstore;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


public class CompiereCartValidator {
	
	private static int PROD_STD   = 1000135;
	private static int PROD_PROF  = 1000219;
	private static int PROD_CLOUD = 1000265;
	
	private static int PROD_MANUF = 1000252;
	private static int PROD_WAREH = 1000253;
	
	private static Set<Integer> ELIGIBLE_PRODS = null;
	private static Set<Integer> OPTION_PRODS = null;
	
	private static BigDecimal DEC_MIN_QTY = new BigDecimal(10);
	
	//messages
	private static String MSG_OPTIONS_DELETED = 
		"Manufacturing and Warehouse Options are only for Professional and Cloud Editions.\n";
	private static String MSG_MIN_QTY = 
		"Minimum Quantity is 10 users.\n";
	
	
	private static Set<Integer> getEligibleProducts() {
		if (ELIGIBLE_PRODS == null) {
			ELIGIBLE_PRODS = new HashSet<Integer>();
			ELIGIBLE_PRODS.add(PROD_PROF);
			ELIGIBLE_PRODS.add(PROD_CLOUD);
		}
		return ELIGIBLE_PRODS;
	}
	
	private static Set<Integer> getOptionProducts() {
		if (OPTION_PRODS == null) {
			OPTION_PRODS = new HashSet<Integer>();
			OPTION_PRODS.add(PROD_MANUF);
			OPTION_PRODS.add(PROD_WAREH);
		}
		return OPTION_PRODS;
	}
	
	public static String validate(WebBasket cart) 
	{
		WebBasketLine optEligibleLine = null;
		Set<WebBasketLine> optionLines = new HashSet<WebBasketLine>();
		String message = "";

		for (WebBasketLine line : cart.getLines()) {
			if (getEligibleProducts().contains(line.getM_Product_ID())) {
				optEligibleLine = line;
				if (line.getQuantity().compareTo(DEC_MIN_QTY) < 0) {
					message = MSG_MIN_QTY;
					line.setQuantity(DEC_MIN_QTY);
				}
			}
			else if (getOptionProducts().contains(line.getM_Product_ID())) {
				optionLines.add(line);
			}
			else if (line.getM_Product_ID() == PROD_STD) {
				if (line.getQuantity().compareTo(DEC_MIN_QTY) < 0) {
					message = MSG_MIN_QTY;
					line.setQuantity(DEC_MIN_QTY);
				}
			}
			
		}
		
		
		if (optEligibleLine == null) {
//			if (optionLines.size() > 0) {
//				for (WebBasketLine line : optionLines) {
//					cart.delete(line.getLine());
//				}
//				message += MSG_OPTIONS_DELETED; 
//			}
		}
		
		// options validation
		for (WebBasketLine line : optionLines) {
			if (line.getM_Product_ID() == PROD_MANUF)
			{
				if ((optEligibleLine != null)) {
					line.setReadOnly(true);
					if (line.getQuantity().compareTo(optEligibleLine.getQuantity()) != 0) {
						line.setQuantity(optEligibleLine.getQuantity());
						//message += "Manufacturing Option quantity was adjusted."; 
					}
				}
				else {
					line.setReadOnly(false);
				}
					
			}
		}
		
		return message;
	}

}
