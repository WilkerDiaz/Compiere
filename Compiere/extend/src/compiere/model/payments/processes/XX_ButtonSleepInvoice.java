package compiere.model.payments.processes;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 * Actualiza el estado de las cuentas por pagar en factura una vez ejecutado el proceso
 * @author Jessica Mendoza
 *
 */
public class XX_ButtonSleepInvoice extends SvrProcess{

	@Override
	protected String doIt() throws Exception {

		/*MInvoice invoice = new MInvoice(Env.getCtx(), getRecord_ID(),null);
		invoice.setXX_AccountPayableStatus("S");
		invoice.save();*/
		
		String sql = "update C_Invoice " +
					 "set XX_AccountPayableStatus = 'S' " +
					 "where C_Invoice_ID = " + getRecord_ID();
		DB.executeUpdate(null, sql);
		
		return null;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	
}
