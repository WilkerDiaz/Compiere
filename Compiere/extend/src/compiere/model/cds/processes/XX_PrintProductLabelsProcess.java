package compiere.model.cds.processes;

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

import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRDiscountRequest;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_Order;

import javax.swing.JOptionPane;
import javax.swing.JComponent.*;


/**
 * Distribution Process for Purchase Order by Sales + Budget
 * @author Javier Pino.
 *
 */
public class XX_PrintProductLabelsProcess extends SvrProcess {
	
	static Object m_readLock = new Object();
	static int glued = -1, hanging = -1;
	static int check_assistant = 0;
	

	@Override
	protected String doIt() throws Exception {
		
		FormFrame form = new FormFrame();
		int form_id = 0;
		
		synchronized( m_readLock ) {

			//Context Variables are created, they are to be removed in the Form
			if(check_assistant!=0)
				Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_PlacedOrder_ID", getRecord_ID());
			else
				Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_DiscountRequest_ID", getRecord_ID());
				
			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Hanging",hanging);
			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Glued", glued);
			
			if(check_assistant!=0)
			{
				X_XX_VMR_Order placedOrder = new X_XX_VMR_Order(getCtx(), getRecord_ID(), get_TrxName());
				
				//Calcular el Consecutivo de Precios
				try {
					new Utilities().GenerarConsecutivo(placedOrder);
				} catch (NullPointerException e) {
					
				}
				
				placedOrder.setXX_CheckAuxiliary_ID(check_assistant);
				placedOrder.save();
				placedOrder.load(get_TrxName());
				placedOrder.setXX_AssignmentDate(placedOrder.getUpdated());
				placedOrder.save();
				commit();
	
				//Displays the Form
				form.setName("Print Product Labels");
				form_id = Env.getCtx().getContextAsInt("#XX_L_FORMPRINTPRODUCTLABEL_ID");			
				form.openForm(form_id);
			}
			else
			{	
				// primero busco a ver en el detalle de las rebajas si hay alguna que no sea a cero
				int conteo = 0;				
				
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try{	
					String SQL = ("select count(*) as CANT " +
								  "from ((select distinct(select name from xx_vmr_discounttype where xx_vmr_discounttype_id=d.XX_VMR_DISCOUNTTYPE_ID) as tipo from XX_VMR_DISCOUNTAPPLIDETAIL d where xx_vmr_discountrequest_id="+getRecord_ID()+")) " +
								  "where tipo not like '%CERO%'");
			
					pstmt = DB.prepareStatement(SQL, null);
					rs = pstmt.executeQuery();
					
					if(rs.next()){
						conteo = rs.getInt("CANT");
					}
				}
				catch (Exception e){
					log.saveError("Error al buscar si hay rebajas distintas de cero ", Msg.getMsg(getCtx(), e.getMessage()));
				} finally {
					DB.closeStatement(pstmt);
					DB.closeResultSet(rs);
				}
				
				//Si todas las rebajas son a cero, indico que
				// no hay ninguna etiqueta a imprimir
				if (conteo==0)
				{
					ADialog.info(1, new Container(), "XX_AllRebatesToZero");
					return "";
				}
								
				Object[] options = {"Reimpresión de Etiqueta","Imprimir Etiquetas de Rebajas"};
				int n = JOptionPane.showOptionDialog(null,"Qué tipo de impresión desea hacer? ","Impresión de Etiquetas Originales",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null,options,options[1]);
				
				if (n==0) // Si se selecciona reimpresion
				{
					form.setName("Print Product Labels");
					form_id = Env.getCtx().getContextAsInt("#XX_L_FORMPRINTDISCPRODLABEL_ID");		
					System.out.println("id de la forma " + form_id);
					form.openForm(form_id);
				} else if (n==1) // si se selecciona impresion de etiqueta de rebajas
				{
					form.setName("Print Product Labels");
					form_id = Env.getCtx().getContextAsInt("#XX_L_PRINTDISCPRODLABEL2_ID");			
					form.openForm(form_id);
				} else if (n!=1 && n!=2)
				{
					return null;
				}
			}
		}
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
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
			} else if (name.equals("Check_Person")) {
				check_assistant = element.getParameterAsInt();				
			} 
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}
		
	}
}
