package org.compiere.fsrv.process;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CompiereSystemException;
import org.compiere.fsrv.model.MFSRVVisit;

public class InvoiceFieldServiceVisit extends SvrProcess {
	
	/**	Parameter				*/
	private int	p_FSRV_Visit_ID = 0; 
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null && element.getParameter_To() == null)
				;
          else if (name.equals("FSRV_Visit_ID")) {
				p_FSRV_Visit_ID = element.getParameterAsInt();
            }    
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}
	
	@Override
	protected String doIt() throws Exception {
		log.info("FSRV_Visit_ID=" + p_FSRV_Visit_ID);
		MFSRVVisit visit = new MFSRVVisit (getCtx(), p_FSRV_Visit_ID, get_TrxName());
		if (visit.get_ID() == 0 || visit.get_ID() != p_FSRV_Visit_ID)
			throw new CompiereSystemException("@NotFound@ @FSRV_Visit_ID@ " + p_FSRV_Visit_ID);
		
		//	Nothing to do
		//if (visit.isProcessed())
			//throw new CompiereUserException("@FSRV_Visit_ID@ @Processed@");
		
		//	Create Invoice Header
		MInvoice invoice = new MInvoice (getCtx(), 0, get_Trx());
		invoice.setAD_Org_ID(visit.getAD_Org_ID());
		invoice.setIsSOTrx(true);
		invoice.setC_DocTypeTarget_ID();
		//	Set Business Partner
		MBPartner bp = new MBPartner (getCtx(), visit.getC_BPartner_ID(), null);
		invoice.setBPartner(bp);
		invoice.setC_BPartner_Location_ID(visit.getC_BPartner_Location_ID());
		invoice.setAD_User_ID(visit.getAD_User_ID());
		//	Other Invoice Details
		invoice.setSalesRep_ID(visit.getSalesRep_ID());
		invoice.setDescription(visit.getDescription());
		if (!invoice.save())
			throw new CompiereSystemException("Could not save Invoice");
		
		//	Create Invoice Line
		MInvoiceLine line = new MInvoiceLine(invoice);
		line.setQty(visit.getMinutes());
		line.setPrice(new BigDecimal(5));
		line.setDescription(visit.getDescription());
		if (line.getDescription() == null)
			line.setDescription(visit.getDescription());
		line.setTax();
		if (!line.save())
			throw new CompiereSystemException("Could not save invoice line");
		
		//	Update Visit
		//visit.setProcessed(true);
		visit.save();
		
		//	Process Invoice
		invoice.setDocAction(MInvoice.DOCACTION_Complete);
		invoice.processIt(MInvoice.DOCACTION_Complete);
		invoice.save();
				
		return "@C_Invoice_ID@ " + invoice.getDocumentNo();
	}
	
	

}
