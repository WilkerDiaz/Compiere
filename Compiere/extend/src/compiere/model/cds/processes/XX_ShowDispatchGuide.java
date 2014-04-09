package compiere.model.cds.processes;

import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import compiere.model.cds.X_XX_VLO_DispatchGuide;

public class XX_ShowDispatchGuide extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		new compiere.model.cds.Utilities().showReportDispatchGuide(new X_XX_VLO_DispatchGuide(Env.getCtx(), getRecord_ID(), null));
		return "";
	}

	@Override
	protected void prepare() {

	}

}
