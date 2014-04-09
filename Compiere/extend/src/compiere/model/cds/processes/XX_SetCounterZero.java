package compiere.model.cds.processes;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
/**
 * Reinicia a cero los contadores de fallas por productos de una referencia básica
 * @author Trinimar Acevedo.
 *
 */
public class XX_SetCounterZero extends SvrProcess{
	@Override
	protected String doIt() throws Exception {
		
		String sum = "UPDATE XX_VMR_faultCounter SET XX_Counter = 0 ";
		DB.executeUpdate(null, sum );

		return null;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
	
}
