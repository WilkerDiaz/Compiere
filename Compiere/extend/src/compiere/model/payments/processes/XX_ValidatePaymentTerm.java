package compiere.model.payments.processes;

import java.math.BigDecimal;
import java.util.logging.Level;
import org.compiere.model.MPaySchedule;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import compiere.model.cds.MPaymentTerm;


/**
 * Se encarga de validar la condición de pago para todas las 
 * opciones de condiciones (simples, dobles y triples)
 * 
 * IMPORTANTE: Se quitó la llamada al proceso de compiere 
 * (org.compiere.process.PaymentTermValidate), debido que solamente
 * validaba para las condiciones de pago simples, es decir, en 
 * donde su programa de pago era 100%.
 * @author Jessica Mendoza
 * 
 * 			compiere.model.payments.processes.XX_ValidatePaymentTerm
 *
 */
public class XX_ValidatePaymentTerm extends SvrProcess{
	
	private MPaySchedule[] m_schedule;
	private compiere.model.cds.MPaySchedule m_schedule2;
	private final static BigDecimal	HUNDRED = new BigDecimal(100);

	@Override
	protected String doIt() throws Exception {
		
		log.info ("C_PaymentTerm_ID=" + getRecord_ID());
		MPaymentTerm pt = new MPaymentTerm (getCtx(), getRecord_ID(), get_TrxName());
		String msg = "";

		m_schedule = (MPaySchedule[]) pt.getSchedule(true);  		
		if (m_schedule.length == 0){
			pt.setIsValid(true);
			msg = "OK";
		}
		
		BigDecimal total = Env.ZERO;
		for (MPaySchedule element : m_schedule) {
			m_schedule2 = new compiere.model.cds.MPaySchedule(Env.getCtx(), element.get_ID(), get_TrxName());
			BigDecimal percent = m_schedule2.getPercentage();
			BigDecimal percent2 = m_schedule2.getXX_PercentageRemainingTwo();
			BigDecimal percent3 = m_schedule2.getXX_PercentageRemainingThree();
			if (percent != null)
				total = total.add(percent);
			if (percent2 != null)
				total = total.add(percent2);
			if (percent3 != null)
				total = total.add(percent3);
		}	
		
		boolean valid = total.compareTo(HUNDRED) == 0;
		pt.setIsValid(valid);
		pt.setDescription("Hola");
		if (!valid)
			msg = "Total = " + total + " - Difference = " + HUNDRED.subtract(total);
		else
			msg = "OK";		
			
		if (msg.equals("OK")){
			pt.save();
			get_TrxName().commit();
		}
		
		return msg;		
	}

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

}
