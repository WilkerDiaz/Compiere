package compiere.model.cds.processes;

import java.awt.Container;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.model.MOrder;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class XX_ChangePayments extends SvrProcess {

	Integer paymentTerm = 0;
	String paymentRule = "";
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_PaymentTerm_ID"))
				paymentTerm = new Integer(element.getParameter().toString());
			else if (name.equals("PaymentRule"))
				paymentRule = element.getParameter().toString();
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
	
		MOrder order = new MOrder( getCtx(), getRecord_ID(), null);
		compiere.model.cds.MOrder orderToAsk = new compiere.model.cds.MOrder( getCtx(), getRecord_ID(), null);
		
		if(!orderToAsk.getXX_InvoicingStatus().equalsIgnoreCase("AP")){
			
			order.setC_PaymentTerm_ID(paymentTerm);
			order.setPaymentRule(paymentRule);
			order.save();
			
			return  Msg.getMsg(Env.getCtx(), "Success");
		}
		else{
			
			ADialog.info(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_CantChangePayments"));
			return Msg.getMsg(Env.getCtx(), "XX_CantChangePayments");
		}
	}
}
