package compiere.model.cds.distribution;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_Order;


/**Proceso que imprime etiquetas de productos de una distribución
 *@author ghuchet */

public class XX_PrintDistributionLabels  extends SvrProcess {

	
	static Object m_readLock = new Object();
	static int glued = -1, hanging = -1, distribution = 0, auxiliary = 0;
	private static boolean processActive = false;

	/**
	 * 	Is Process Active
	 *	@return true if active
	 */
	protected static boolean isProcessActive()
	{
		int  aux = Env.getCtx().getContextAsInt("#XX_VMR_PRINTDDACTIVE");
		if(aux == 1){
			return true;
		}else return false;

	}
	
	/**
	 * 	Set Process (in)active
	 *	@param active active
	 */
	protected static void setProcessActive (int active)
	{
		Env.getCtx().setContext("#XX_VMR_PRINTDDACTIVE",active);
	}

	@Override
	protected void prepare() {
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("Printer_Hanging")) {
				for(int i=0; i<services.length; i++){
					if(services[i].getName().equals(element.getParameter())){
						hanging = i;
						break;
					}
				}
			} else if (name.equals("Printer_Glued")) {
				for(int i=0; i<services.length; i++){
					if(services[i].getName().equals(element.getParameter())){
						glued = i;
						break;
					}
				}
			}else if (name.equals("XX_VMR_DistributionHeader_ID")) {
				distribution = element.getParameterAsInt();
			}else if (name.equals("XX_CheckAuxiliary_ID")){
				auxiliary =  element.getParameterAsInt();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}
	}
	
	@Override
	protected String doIt() throws Exception {
		
		if(isProcessActive()){
			return "Este proceso ya se está corriendo en esta máquina. " +
					"\nSi en efecto está ejecutando ya el proceso, espere a que el mismo termine e intente de nuevo." +
					"\nEn caso contrario reinicie su sesión de Compiere. ";
	    }
			
		setProcessActive(1);

		try{
			MVMRDistributionHeader header= new MVMRDistributionHeader(Env.getCtx(),distribution, null);
			if(header.isXX_IsPrintedLabels()){
				String msg = "Tenga en cuenta que las etiquetas de esta distribución han sido impresas anteriormente." +
						"\n¿Desea Continuar?";
				boolean imprimir = ADialog.ask(1, new Container(), msg);
				if(imprimir) {
					printLabels();
				}
			}else {
				printLabels();
			}
			
		}
		catch (Exception e)  {
			e.printStackTrace();
			return "Ocurrió un error al imprimir las etiquetas. " +
					"\nIntente nuevamente o contacte al administrador del sistema";
		}
		setProcessActive(0);
		Env.getCtx().remove("#XX_VMR_PRINTLABEL_Distribution_ID");
		return "Proceso Completado.";
	}



	private boolean generateConsecutive() throws Exception{
		
		String sql = "\nSELECT XX_VMR_ORDER_ID " +
				"\nFROM XX_VMR_ORDER " +
				"\nWHERE XX_VMR_DISTRIBUTIONHEADER_ID = "+distribution+ 
				"\nAND XX_ORDERREQUESTSTATUS NOT IN  ('AN', 'TI') ";
		
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    try{
	    	pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
		    while(rs.next()){
		    	X_XX_VMR_Order placedOrder = new X_XX_VMR_Order(getCtx(),rs.getInt(1), null);
		    	
		    	//Generar el Consecutivo de Precios a los productos de detalles de pedido que no lo tengan creado
				try {
					new Utilities().GenerarConsecutivo(placedOrder);
				} catch (Exception e) {
					e.printStackTrace();
			    	return false;
				}
				placedOrder.setXX_CheckAuxiliary_ID(auxiliary);
				placedOrder.setXX_AssignmentDate(placedOrder.getUpdated());
				placedOrder.save();
		    }
	    }catch(Exception e){
	    	log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
	    	return false;
	    }finally{
	    	DB.closeResultSet(rs);
	    	DB.closeStatement(pstmt);
		}
		return true;
	}

	private void printLabels() throws Exception {
		
		FormFrame form = new FormFrame();
		int form_id = 0;
		
		if(generateConsecutive()){
		//Seteo variables de contexto del proceso de imprimir etiquetas
		synchronized( m_readLock ) {

			//Context Variables are created, they are to be removed in the Form
			
			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Distribution_ID", distribution);
			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Hanging",hanging);
			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Glued", glued);
			
		//	Displays the Form
			form.setName("Print Product Labels");
			form_id = Env.getCtx().getContextAsInt("#XX_L_FORMPRINTDISTLABEL_ID");			
			form.openForm(form_id);
		}
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		}
	}


}
