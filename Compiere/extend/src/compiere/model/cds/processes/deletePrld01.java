package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class deletePrld01 extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		String myLog = "";
		
		//Borrado de los registros del mes
		Date actualDate= new Date();
	
		myLog += "\n" + "Antes de borrar: " + actualDate.toString() + "\n" ;
		
		actualDate = new Date();
		SimpleDateFormat formatoYM = new SimpleDateFormat("yyyyMM");
		String yearMonth = formatoYM.format(actualDate);
		//
		SimpleDateFormat formatoD = new SimpleDateFormat("dd");
		String day = formatoD.format(actualDate);
		
		String queryDelete = "";
		
		if(lastDayOfMonth(new Integer(day))){
			//Si es el ultimo dia del mes borro solo los meses siguientes
			queryDelete = "DELETE FROM XX_VMR_PRLD01 WHERE XX_BUDGETYEARMONTH >" + yearMonth;
		}
		else{
			queryDelete = "DELETE FROM XX_VMR_PRLD01 WHERE XX_BUDGETYEARMONTH >=" + yearMonth;
		}
		
		PreparedStatement pstmtQuery = DB.prepareStatement(queryDelete, null);
	    pstmtQuery.executeUpdate(queryDelete);
	    pstmtQuery.close();
	    
	    actualDate= new Date();
	    myLog += "Luego de borrar: " + actualDate.toString();
		
	    //JTRIAS
	    //sendNotificationMail(1020183, myLog);
	    //Thread.sleep(1000);
	    
		return "Ready";
	}

	@Override
	protected void prepare() {
	}
	
	private boolean lastDayOfMonth(int actualDay){
		
		Calendar calendar = Calendar.getInstance();
		
		int lastDate = calendar.getActualMaximum(Calendar.DATE);
		
		if (lastDate==actualDay)
			return true;
		else
			return false;	
	}

}
