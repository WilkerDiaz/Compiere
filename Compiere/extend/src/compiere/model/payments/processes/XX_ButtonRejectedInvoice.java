package compiere.model.payments.processes;

import java.sql.Timestamp;
import java.util.Date;

import org.compiere.model.MOrder;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MInvoice;
import compiere.model.cds.Utilities;

/**
 * Envia un correo al usuario para indicar el rechazo de una factura
 * @author Jessica Mendoza
 *
 */
public class XX_ButtonRejectedInvoice extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		Timestamp fechaActual = new Timestamp(new Date().getTime());
		MInvoice invoice = new MInvoice(Env.getCtx(), getRecord_ID(),null);
		MOrder order = new MOrder(Env.getCtx(), invoice.getC_Order_ID(), null);
		int userId = invoice.getAD_User_ID();
		String condicion = this.nameReasonRejectionInvoice(invoice.getXX_ReasonRejectionInvoice());
		String mensaje = Msg.getMsg(Env.getCtx(), "XX_InvoiceRejectedOne") +
			invoice.getDocumentNo() + Msg.getMsg(Env.getCtx(), "XX_InvoiceRejectedTwo") + " "+
			order.getDocumentNo() + Msg.getMsg(Env.getCtx(), "XX_InvoiceRejectedThree") + " "+
			condicion + ". " + Msg.getMsg(Env.getCtx(), "XX_InvoiceRejectedFour");
		
		Utilities util = new Utilities(Env.getCtx(), null,
				Env.getCtx().getContextAsInt("#XX_L_REJECTEDINVOICE_ID"), 
				mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,
				-1, userId, null);
		
		util.ejecutarMail(); 
		util = null;		
		invoice.setXX_RejectionDate(fechaActual);
		invoice.setXX_UserInvoiceRejection_ID(getAD_User_ID());
		invoice.save();
		return null;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
	
	public String nameReasonRejectionInvoice(String valor){
		if (valor.equals("EAI")){
			valor = "Error en el monto total de la factura";
		}else{
			if (valor.equals("EAT")){
				valor = "Error en el monto del IVA sobre la base imponible";
			}else {
				if (valor.equals("EDC")){
					valor = "Error en los datos fiscales de la empresa adquiriente";
				}else {
					if (valor.equals("FPF")){
						valor = "Incumplimiento de deberes formales pre-impresos";
					}
				}
			}
		}
		return valor;
	}

}
