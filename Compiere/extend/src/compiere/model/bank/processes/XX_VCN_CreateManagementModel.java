package compiere.model.bank.processes;

import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

import compiere.model.birt.BIRTReport;
import compiere.model.bank.MBankAccountDoc;
import compiere.model.bank.X_XX_VCN_ManagementCheck;

public class XX_VCN_CreateManagementModel extends SvrProcess{

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		X_XX_VCN_ManagementCheck aux = new X_XX_VCN_ManagementCheck (getCtx(),getRecord_ID(),get_TrxName());
		int id= aux.get_ValueAsInt("XX_VCN_ManagementCheck_ID");
		int empresa = Env.getCtx().getAD_Client_ID();
		
		String designName = "CheckManagement";

		//Intanciar reporte
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("idManagementCheck");
		myReport.parameterValue.add(id);
		
		myReport.parameterName.add("nombreEmpresa");
		myReport.parameterValue.add(empresa);
				
		//Correr Reporte
		myReport.runReport(designName,"pdf");
		
		return null;
	}

}
