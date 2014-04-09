package compiere.model.payments.processes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import compiere.model.cds.MOrder;

/*
 * Asociar Comprobante de Retencion a Venta
 */
public class XX_AssociateWithholding extends SvrProcess {

	Integer XX_WithholdingNo = 0;
	BigDecimal XX_WithholdingAmount = BigDecimal.ZERO;
	private Timestamp XX_WithholdingDate = null;
	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_WithholdingNo"))
				XX_WithholdingNo = element.getParameterAsInt();
			else if (name.equals("XX_WithholdingAmount"))
				XX_WithholdingAmount = (BigDecimal) element.getParameter();
			else if (name.equals("XX_WithholdingDate"))
				XX_WithholdingDate = (Timestamp) element.getParameter();
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		 
		MOrder order = new MOrder(Env.getCtx(), getRecord_ID(), null);
		java.util.Date date= new java.util.Date();
		order.setXX_WithholdingCreated(new Timestamp(date.getTime()));
		order.setXX_WithholdingDate(XX_WithholdingDate);
		order.setXX_WithholdingNo(XX_WithholdingNo);
		order.setXX_WithholdingAmount(XX_WithholdingAmount);
		order.save();
		
		return "";
	}

}
