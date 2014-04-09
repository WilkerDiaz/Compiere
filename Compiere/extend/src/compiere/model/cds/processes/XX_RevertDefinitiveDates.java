package compiere.model.cds.processes;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MOrder;
import compiere.model.cds.MVLOBoardingGuide;

public class XX_RevertDefinitiveDates extends SvrProcess {

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		
		// Primero desmarcamos los checks de fechas en la guia de embarque
		MVLOBoardingGuide aux = new MVLOBoardingGuide(getCtx(),getRecord_ID(),get_TrxName());
		aux.setXX_DefinitiveCD(false);
		aux.setXX_DefinitiveDispatch(false);
		aux.setXX_DefinitiveValidation(false);
		aux.setXX_DefinitiveDA(false);
		aux.setXX_DefinitiveInsurance("N");
		aux.setXX_DefinitiveInterFret("N");
		aux.setXX_DefinitiveVzlaDate(false);
		aux.setXX_DefinitiveShippingDate(false);
		
		// luego updateamos todas las ordenes de esa guia y les cambiamos el estado a En Produccion
		String update = "UPDATE C_ORDER SET XX_ORDERSTATUS='EP' WHERE XX_VLO_BOARDINGGUIDE_ID=" + aux.getXX_VLO_BoardingGuide_ID();
		DB.executeUpdate(get_TrxName(),update);
		aux.save();
		return "Definitivos Reversados";
	}

}
