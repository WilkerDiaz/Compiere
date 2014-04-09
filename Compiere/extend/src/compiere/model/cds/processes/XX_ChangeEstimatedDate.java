package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import compiere.model.cds.MOrder;
import compiere.model.cds.Utilities;

public class XX_ChangeEstimatedDate extends SvrProcess {

	private Object date = "";
	private String motive = "";
	Timestamp fechaEstimadaOld = null;
	Timestamp fechaEstimadaSOld = null;
	Timestamp fechaEstimadaTOld = null;
	Timestamp fechaEstimadaNew = null;
	Timestamp fechaEstimadaSNew = null;
	Timestamp fechaEstimadaTNew = null;
	
	@Override
	protected String doIt() throws Exception {
		
		Timestamp dateAux = new Timestamp(0);
		dateAux = java.sql.Timestamp.valueOf(date.toString());
		
		//Verifico que la fecha de llegada no sea un dia no laborable
		Utilities check = new Utilities();
		if(check.isNonBusinessDay(dateAux)){
			
			ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "EstimatedNonBusinessDay"));
			return Msg.getMsg(getCtx(), "EstimatedNonBusinessDay");
			
		}else{
			
			MOrder order = new MOrder( getCtx(), getRecord_ID(), null);
			calcOldDates(order);
			order.setXX_EstimatedDate(dateAux);
			order.setXX_MotivChanEstimDate_ID(new Integer(motive));
			order.save();
			
			calcNewDates(order);
			
			if(fechaEstimadaNew!=null)
				modifyDate(order, fechaEstimadaOld, fechaEstimadaNew);
			if(fechaEstimadaSNew!=null)
				modifyDate(order, fechaEstimadaSOld, fechaEstimadaSNew);
			if(fechaEstimadaTNew!=null)
				modifyDate(order, fechaEstimadaTOld, fechaEstimadaTNew);
		}
		
		return "Finalizado";
	}

	@Override
	protected void prepare() {

		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_EstimatedDate"))
				date = element.getParameter();
			else if (name.equals("XX_MotivChanEstimDate_ID"))
				motive = element.getParameter().toString();
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}
	}
	
	/**
	 * Calcular Fechas Estimadas Viejas
	 */
    private void calcOldDates(MOrder order){
    	
		Utilities util = new Utilities();
    	
		int diasBeneficio = 0;
		int diasTotalesI = 0;
    	int diasTotalesII = 0;
    	int diasTotalesIII = 0;
		Vector<Integer> condicionPago = new Vector<Integer>(6);
		Vector<Timestamp> vector = new Vector<Timestamp>(3);
		Timestamp fechaEstimada = new Timestamp(0);
		Timestamp fechaEstimadaS = new Timestamp(0);
		Timestamp fechaEstimadaT = new Timestamp(0);
    	Calendar calendarEstimadaI = Calendar.getInstance();
    	Calendar calendarEstimadaII = Calendar.getInstance();
    	Calendar calendarEstimadaIII = Calendar.getInstance();
		
		diasBeneficio = util.benefitVendorDayAdvance(order.getC_BPartner_ID());
		condicionPago = util.infoCondicionPago(order.getC_PaymentTerm_ID());					

		if (order.getXX_OrderType().equals("Nacional")){
			diasTotalesI = condicionPago.get(1) - diasBeneficio;
			diasTotalesII = condicionPago.get(4) - diasBeneficio;
			diasTotalesIII = condicionPago.get(7) - diasBeneficio;
		}else{
			diasTotalesI = condicionPago.get(1);
			diasTotalesII = condicionPago.get(4);
			diasTotalesIII = condicionPago.get(7);
		}
		
		vector = util.calcularFecha(order.getC_PaymentTerm_ID(),order.getC_Order_ID(),"estimada");						
		if (vector.get(0) != null){
			fechaEstimada = vector.get(0);
			calendarEstimadaI.setTimeInMillis(fechaEstimada.getTime());
		    calendarEstimadaI.add(Calendar.DATE, diasTotalesI);
		    fechaEstimadaOld = new Timestamp(calendarEstimadaI.getTimeInMillis());	
		}
		if (vector.get(1) != null){
			fechaEstimadaS = vector.get(1);
			calendarEstimadaII.setTimeInMillis(fechaEstimadaS.getTime());
		    calendarEstimadaII.add(Calendar.DATE, diasTotalesII);
		    fechaEstimadaSOld = new Timestamp(calendarEstimadaII.getTimeInMillis());
		}
		if (vector.get(2) != null){
			fechaEstimadaT = vector.get(3);
			calendarEstimadaIII.setTimeInMillis(fechaEstimadaT.getTime());
		    calendarEstimadaIII.add(Calendar.DATE, diasTotalesIII);
		    fechaEstimadaTOld = new Timestamp(calendarEstimadaIII.getTimeInMillis());
		}
    }
    
	/**
	 * Calcular Fechas Estimadas Nuevas
	 */
    private void calcNewDates(MOrder order){
    	
		Utilities util = new Utilities();
    	
		int diasBeneficio = 0;
		int diasTotalesI = 0;
    	int diasTotalesII = 0;
    	int diasTotalesIII = 0;
		Vector<Integer> condicionPago = new Vector<Integer>(6);
		Vector<Timestamp> vector = new Vector<Timestamp>(3);
		Timestamp fechaEstimada = new Timestamp(0);
		Timestamp fechaEstimadaS = new Timestamp(0);
		Timestamp fechaEstimadaT = new Timestamp(0);
    	Calendar calendarEstimadaI = Calendar.getInstance();
    	Calendar calendarEstimadaII = Calendar.getInstance();
    	Calendar calendarEstimadaIII = Calendar.getInstance();
		
		diasBeneficio = util.benefitVendorDayAdvance(order.getC_BPartner_ID());
		condicionPago = util.infoCondicionPago(order.getC_PaymentTerm_ID());					

		if (order.getXX_OrderType().equals("Nacional")){
			diasTotalesI = condicionPago.get(1) - diasBeneficio;
			diasTotalesII = condicionPago.get(4) - diasBeneficio;
			diasTotalesIII = condicionPago.get(7) - diasBeneficio;
		}else{
			diasTotalesI = condicionPago.get(1);
			diasTotalesII = condicionPago.get(4);
			diasTotalesIII = condicionPago.get(7);
		}
		
		vector = util.calcularFecha(order.getC_PaymentTerm_ID(),order.getC_Order_ID(),"estimada");						
		if (vector.get(0) != null){
			fechaEstimada = vector.get(0);
			calendarEstimadaI.setTimeInMillis(fechaEstimada.getTime());
		    calendarEstimadaI.add(Calendar.DATE, diasTotalesI);
		    fechaEstimadaNew = new Timestamp(calendarEstimadaI.getTimeInMillis());	
		}
		if (vector.get(1) != null){
			fechaEstimadaS = vector.get(1);
			calendarEstimadaII.setTimeInMillis(fechaEstimadaS.getTime());
		    calendarEstimadaII.add(Calendar.DATE, diasTotalesII);
		    fechaEstimadaSNew = new Timestamp(calendarEstimadaII.getTimeInMillis());
		}
		if (vector.get(2) != null){
			fechaEstimadaT = vector.get(3);
			calendarEstimadaIII.setTimeInMillis(fechaEstimadaT.getTime());
		    calendarEstimadaIII.add(Calendar.DATE, diasTotalesIII);
		    fechaEstimadaTNew = new Timestamp(calendarEstimadaIII.getTimeInMillis());
		}
    }
    
    private void modifyDate(MOrder order, Timestamp oldDate, Timestamp newDate){
    	
    	GregorianCalendar fecha = new GregorianCalendar();
		fecha.setTime(newDate);
		
		String week = fecha.get(GregorianCalendar.WEEK_OF_YEAR)+"/"+fecha.get(Calendar.YEAR);
    	
    	String update = "update XX_VCN_EstimatedAPayable " +
					 	"SET XX_DATEESTIMATED = " + DB.TO_DATE(newDate) + ", XX_WEEKESTIMATED = '" + week + "' " +  
					 	"WHERE C_ORDER_ID = " + order.get_ID() + " " +
					 	"AND XX_DATEESTIMATED = " + DB.TO_DATE(oldDate);	
    	
		DB.executeUpdate(null, update);
    }
}
