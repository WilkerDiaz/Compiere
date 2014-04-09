package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MOrder;
import compiere.model.cds.X_XX_VLO_BoardingGuide;

public class XX_UndoDefinitivesBoardingGuide extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
	
		X_XX_VLO_BoardingGuide imports = new X_XX_VLO_BoardingGuide(getCtx(),getRecord_ID(),null);
		imports.setXX_DefinitiveShippingDate(false);
		imports.setXX_DefinitiveVzlaDate(false);
//		imports.setXX_DefinitiveInterFret(false);
///		imports.setXX_DefinitiveInsurance(false);
		imports.setXX_DefinitiveDA(false);
		imports.setXX_DefinitiveValidation(false);
		imports.setXX_DefinitiveDispatch(false);
		imports.setXX_DefinitiveCD(false);
//		imports.setXX_DefinitiveNacTreas(false);
//		imports.setXX_DefinitiveSeniat(false);
//		imports.setXX_DefinitiveAgent(false);
		
		imports.save();
		
		commit();
		
		return "Proceso Realizado";
		
	}

	@Override
	protected void prepare() {
		
		
	}

}
