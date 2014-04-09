package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Msg;

import compiere.model.cds.Utilities;

public class ViewVendorRatingProcess extends SvrProcess {

	@Override
	protected void prepare() {

	}

	@Override
	protected String doIt() throws Exception {
		Utilities calcularPuntuacion = new Utilities();
		Double avg = new Double(0);
		avg = calcularPuntuacion.calculateVendorRating(getRecord_ID());
		
		BigDecimal bigAvg = new BigDecimal(avg);
		bigAvg = bigAvg.setScale(2, RoundingMode.HALF_UP);
		
		calcularPuntuacion = null;
		
		log.saveInfo("Vendor Rating", Msg.getMsg(getCtx(), "XX_VendorRating", new String[]{bigAvg.toString()}) );
		ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_VendorRating", new String[]{bigAvg.toString()}) );
		
		return Msg.getMsg(getCtx(), "XX_VendorRating", new String[]{bigAvg.toString()}) ;
	}

}
