package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import com.sun.java.swing.plaf.windows.resources.windows;

import compiere.model.cds.X_Ref_XX_ConsecutiveOrigin;
import compiere.model.cds.X_XX_VMR_PriceConsecutive;


/**
 * Proceso que permite reimprimir las etiquetas de un producto con un consecutivo de precio específico
 * @author Gabrielle Huchet
 *
 */
public class XX_ReprintProductLabels extends SvrProcess {
	
	FormFrame form = new FormFrame();
	int form_id = 0;
	int productQuantity = 0, warehouse_id = 0;
	static int glued = -1, hanging = -1;
	
	protected String doIt() throws Exception {

		Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_ProductQuantity", productQuantity);
		Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Warehouse_ID", warehouse_id);
		Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_PriceConsecutive_ID", getRecord_ID());	

		Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Hanging",hanging);
		Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Glued", glued);
		
		X_XX_VMR_PriceConsecutive priceConsecutive = new X_XX_VMR_PriceConsecutive(getCtx(), getRecord_ID() , null);
		Calendar dateCreated = Calendar.getInstance();
		dateCreated.setTime(priceConsecutive.getCreated());
		boolean dateValid = false;
		Calendar date = Calendar.getInstance();
		String orig = priceConsecutive.getXX_ConsecutiveOrigin();
		System.out.println(dateCreated.getTime());
		if(orig.compareTo(X_Ref_XX_ConsecutiveOrigin.PEDIDO.getValue())==0 && dateCreated.get(Calendar.YEAR)>2008){
			dateCreated.add(Calendar.YEAR, 4);
			if(date.getTime().before(dateCreated.getTime())){
				dateValid = true;
			}
		}
		else if(orig.compareTo(X_Ref_XX_ConsecutiveOrigin.REBAJAS.getValue())==0 && dateCreated.get(Calendar.YEAR)>2008){
			dateCreated.add(Calendar.YEAR, 2);
			if(date.getTime().before(dateCreated.getTime())){
				dateValid = true;
			}
		}
		System.out.println(dateCreated.getTime());
		if(dateValid){
			form.setName("Print Product Labels");
			form_id = Env.getCtx().getContextAsInt("#XX_L_FORMPRINTLABELPRICECON_ID");		//XX_PrintProductPriceConseLabels
			form.openForm(form_id);		
			AEnv.showCenterScreen(form);
			while (form.isVisible())
				Thread.sleep(500);
		}else {
			if(orig.compareTo(X_Ref_XX_ConsecutiveOrigin.PEDIDO.getValue())==0)
				ADialog.error(1,new Container(), "XX_PrintableInvalidDate");
			else if(orig.compareTo(X_Ref_XX_ConsecutiveOrigin.REBAJAS.getValue())==0) {
				ADialog.error(1,new Container(), "XX_PrintableInvalidDateDiscount");
			}
		}
		return null;
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
			} else if (name.equals("ProductQty")) {
				productQuantity = element.getParameterAsInt();				
			} else if (name.equals("M_Warehouse_ID")) {
				warehouse_id = element.getParameterAsInt();				
			} 
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}
		
	}
	
}
