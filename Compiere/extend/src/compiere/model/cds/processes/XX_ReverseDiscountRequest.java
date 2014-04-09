package compiere.model.cds.processes;

import org.compiere.process.SvrProcess;
import org.compiere.swing.CPanel;
import org.compiere.util.Env;

import compiere.model.cds.MVMRDiscountRequest;

public class XX_ReverseDiscountRequest extends SvrProcess{

	private int m_WindowNo = Env.getCtx().getContextAsInt("#XX_L_W_DISCOUNTREQUEST_ID");	
	private CPanel mainPanel = new CPanel();
	
	@Override
	protected String doIt() throws Exception 
	{
		MVMRDiscountRequest discReq = new MVMRDiscountRequest(Env.getCtx(),getRecord_ID(),null);
		discReq.setXX_Status("PE");
		discReq.setProcessing(false);
		discReq.save();
		
		return "";
	}
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
