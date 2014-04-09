package compiere.model.cds.processes;

import java.awt.Container;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import compiere.model.cds.X_XX_VCN_TradeAgreements;

	//Proceso que cambia el estado del acuerdo comercial a "Vencido"

public class XX_TradeAgreementExpire extends SvrProcess{

	@Override
	protected String doIt() throws Exception {

		X_XX_VCN_TradeAgreements agreement = new X_XX_VCN_TradeAgreements(Env.getCtx(), getRecord_ID(), get_TrxName());
		agreement.setXX_Status("VENCIDO");
		agreement.save();
		ADialog.info(1, new Container(), "XX_ExpireTradeAgree");
		return null;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
