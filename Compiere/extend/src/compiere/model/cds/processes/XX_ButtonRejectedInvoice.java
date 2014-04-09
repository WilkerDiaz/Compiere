package compiere.model.cds.processes;

import org.compiere.process.SvrProcess;

import compiere.model.cds.MInvoice;
import compiere.model.cds.Utilities;

public class XX_ButtonRejectedInvoice extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		
		MInvoice invoice = new MInvoice(getCtx(), getRecord_ID(),get_TrxName());
		int userId = invoice.getAD_User_ID();
		String condicion = this.nameReasonRejectionInvoice(invoice.getXX_ReasonRejectionInvoice());
		String mensaje = "La presente es para informarle que la factura N°: "+
		invoice.getC_Invoice_ID()+
		" registrada por usted, correspondiente a la Orden de Compra/Contrato N°: "+
		invoice.getC_Order_ID()+" fue rechazada por: "+
		condicion+". Favor pasar por contabilidad.";
		
		Utilities util = new Utilities(getCtx(), null,
				getCtx().getContextAsInt("#XX_L_REJECTEDINVOICE_ID"), 
				mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,
				-1, userId, null);
		
		util.ejecutarMail(); 
		util = null;
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
