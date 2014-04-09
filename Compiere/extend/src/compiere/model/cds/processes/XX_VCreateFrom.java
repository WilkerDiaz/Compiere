package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.framework.Query;
import org.compiere.model.GridTab;
import org.compiere.model.GridWindow;
import org.compiere.model.GridWindowVO;
import org.compiere.model.GridWorkbench;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import compiere.model.cds.forms.VCreateFrom;

public class XX_VCreateFrom extends SvrProcess {

	static Integer sync = new Integer(0);
	
	@Override
	protected void prepare() {

	}

	private GridWorkbench	m_mWorkbench;
	private GridTab			m_curTab;

	
	@Override
	protected String doIt() throws Exception {
		
		m_mWorkbench = new GridWorkbench(Env.getCtx(), 183);
		
		GridWindowVO wVO = AEnv.getMWindowVO(getCtx().getContextAsInt("#WindowNo"), m_mWorkbench.getWindowID(0), 0);
		GridWindow mWindow = new GridWindow (wVO);
		m_mWorkbench.setWindowNo(0, getCtx().getContextAsInt("#WindowNo"));
		m_mWorkbench.setMWindow(0, mWindow);
		GridTab gTab = m_mWorkbench.getMWindow(0).getTab(0);
		
		Env.getCtx().remove("#WindowNo");
		
		int tab = 0;
		Query query = new Query("C_Invoice");
		int wb = 0;
	//  Query first tab
		if (tab == 0)
		{
			//  initial user query for single workbench tab
			if (m_mWorkbench.getWindowCount() == 1)
			{
				query.addRestriction("C_Invoice_ID", Query.EQUAL, getRecord_ID(), "C_Invoice_ID", "Factura");
			}
			
			//	Set initial Query on first tab
			if (query != null)
				gTab.setQuery(query);
			if (wb == 0)
				m_curTab = gTab;	
		}
		
		m_curTab.query(0, 0, false);
		
	//  m_curWindowNo
		VCreateFrom vcf = VCreateFrom.create (m_curTab, getRecord_ID());
		if (vcf != null)
		{
			if (vcf.isInitOK())
			{
				vcf.setVisible(true);
				vcf.dispose();
				m_curTab.dataRefresh();
			}
			else
				vcf.dispose();
		}
		
		return "";
	}

}