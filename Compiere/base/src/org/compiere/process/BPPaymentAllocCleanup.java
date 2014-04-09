/**
 * 
 */
package org.compiere.process;

import java.util.logging.Level;

import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.util.CompiereUserException;

/**
 * @author awang
 *
 */
public class BPPaymentAllocCleanup extends SvrProcess {

	/**	BPartner ID			*/
	int p_C_BPartner_ID = 0;

	/**
	 *	Prepare
	 */
	@Override
	protected void prepare ()
	{
		p_C_BPartner_ID = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("C_BPartner_ID=" + p_C_BPartner_ID);
		if (p_C_BPartner_ID == 0)
			throw new CompiereUserException ("No Business Partner selected");
		MPayment.setIsAllocated (getCtx(), p_C_BPartner_ID, null);
		MInvoice.setIsPaid (getCtx(), p_C_BPartner_ID, null);
		
		return "OK";
	}	//	doIt

}	//	BPPaymentAllocCleanup