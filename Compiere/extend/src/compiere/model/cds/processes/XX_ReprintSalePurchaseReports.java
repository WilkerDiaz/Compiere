package compiere.model.cds.processes;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

public class XX_ReprintSalePurchaseReports extends SvrProcess {

	int year = 0;
	int month = 0;
	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
	    for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null){
				;
			}else if (name.equals("year")){
				year = new Integer(element.getParameter().toString());
			}else if (name.equals("month")){
				month = new Integer(element.getParameter().toString());
			}else {;}
		}
	}

	@Override
	protected String doIt() throws Exception {

		XX_CalculationSalePurchase csp = new XX_CalculationSalePurchase();
		
		csp.setMonthYear(month, year);
		csp.printReports();
		
		return "Revise su explorador";
	}
}
