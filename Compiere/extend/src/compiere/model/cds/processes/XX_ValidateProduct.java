package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.util.logging.Level;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.APanel;
import org.compiere.apps.AWindow;
import org.compiere.framework.Query;
import org.compiere.grid.GridController;
import org.compiere.grid.VTabbedPane;
import org.compiere.model.GridTable;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.swing.CPanel;
import org.compiere.util.Env;

import compiere.model.cds.MProduct;
import compiere.model.cds.MVLOUnsolicitedProduct;

/**
*
* @author Rosmaira Arvelo
*/
public class XX_ValidateProduct extends SvrProcess {

	private int m_WindowNo = Env.getCtx().getContextAsInt("#XX_L_W_UNSOLICITEDPRODUCT_ID");	
	private CPanel mainPanel = new CPanel();
	Integer XX_VMR_Line_ID = 0;
	Integer XX_VMR_Section_ID = 0;
	Integer XX_VMR_ProductClass_ID =0;
	
	@Override
	protected String doIt() throws Exception 
	{		
		MVLOUnsolicitedProduct product = new MVLOUnsolicitedProduct(getCtx(),getRecord_ID(),null);
				
		Env.getCtx().setContext("#FromProcess_Aux", "R");
		
		MProduct prod = new MProduct(getCtx(),product.getM_Product_ID(),null);			
		
		prod.set_ValueNoCheck("XX_VMR_Line_ID", XX_VMR_Line_ID);
		prod.set_ValueNoCheck("XX_VMR_Section_ID", XX_VMR_Section_ID);
		prod.set_ValueNoCheck("XX_VMR_ProductClass_ID", XX_VMR_ProductClass_ID);
		prod.save();
		
		Env.getCtx().remove("#FromProcess_Aux");
			
		Env.getCtx().setContext("#FromProcess_Aux", "V");
			
		AWindow window_Product = new AWindow();
		Query query = Query.getEqualQuery("M_Product_ID", prod.get_ID());		
		window_Product.initWindow(140, query);
		AEnv.showCenterScreen(window_Product);
			
		// Obtenemos el GridController para setear la variable m_changed=true
	    JRootPane jRootPane  = ((JRootPane)window_Product.getComponent(0));
	    JLayeredPane jLayeredPane = (JLayeredPane)jRootPane.getComponent(1);
	    JPanel jPanel = (JPanel)jLayeredPane.getComponent(0);
	    APanel aPanel = (APanel)jPanel.getComponent(0);
	    VTabbedPane vTabbedPane = (VTabbedPane)aPanel.getComponent(0);
	    GridController gridController = (GridController)vTabbedPane.getComponent(0);
	    GridTable mTable = gridController.getMTab().getTableModel();
		mTable.setChanged(true);		
			
		MProduct.loadUnsolicitedProduct(product, getCtx());
								
		while(window_Product.isVisible())
			Thread.sleep(1000);
		
		Env.getCtx().remove("#FromProcess_Aux");		
		
		ADialog.info(m_WindowNo, this.mainPanel, "MustRefresh");
		
		return "";
	}
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_VMR_Line_ID"))
				XX_VMR_Line_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("XX_VMR_Section_ID"))
				XX_VMR_Section_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("XX_VMR_ProductClass_ID"))
				XX_VMR_ProductClass_ID = ((BigDecimal)element.getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}
}
