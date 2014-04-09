package compiere.model.cds.processes;

import org.compiere.process.SvrProcess;

import compiere.model.cds.MVMRCriticalTaskForClose;

/**
 *  Cierre de Tareas Criticas
 *
 *  @author     Rosmaira Arvelo
 *  @version    
 */

public class XX_CloseAlertCriticalTask extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		
		if(getRecord_ID()!=0)
		{
			MVMRCriticalTaskForClose Task= new MVMRCriticalTaskForClose(getCtx(), getRecord_ID(), null);
			Task.setXX_StatusCriticalTask("Com");
			Task.setXX_PWFValProd("Y");
			Task.setIsActive(false);
			Task.save();
		}

		return "";
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
