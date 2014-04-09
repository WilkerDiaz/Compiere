package compiere.model.payments.processes;

import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import compiere.model.cds.MInvoice;

/**
 * Actualiza el estado de las cuentas por pagar en la factura al ejecutar el proceso
 * @author Jessica Mendoza
 *
 */
public class XX_ButtonActiveInvoice extends SvrProcess{

	@Override
	protected String doIt() throws Exception {

		MInvoice invoice = new MInvoice(Env.getCtx(), getRecord_ID(),null);
		invoice.setXX_AccountPayableStatus("A");
		invoice.save();
		return null;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
	
}
