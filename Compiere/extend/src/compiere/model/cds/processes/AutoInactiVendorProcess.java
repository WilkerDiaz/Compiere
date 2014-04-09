package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;

public class AutoInactiVendorProcess extends SvrProcess {

	private Integer 	m_XX_DaysInactivity = 0;
	
	private Integer 	m_XX_Observation = null;
	
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("m_XX_DaysInactivity"))
				m_XX_DaysInactivity = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("m_XX_Observation"))
				m_XX_Observation = ((BigDecimal)element.getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		String SQL = "select C_BPartner_ID " +
					 "from C_Order " +
					 "where C_BPartner_ID not in ( " +
			  		 "    select o.C_BPartner_id " +
			  		 "    from C_Order o " +
			  		 "    where upper(XX_ORDERSTATUS) NOT IN ('ANULADA', 'CHEQUEADA') " +
					 ") and round(sysdate-XX_APPROVALDATE, 2) >= "+m_XX_DaysInactivity;
		
		//System.out.println(SQL);
		Integer AuxC_Bpartner_ID = new Integer(0);
		MBPartner Aux_MBPartner;
		PreparedStatement pstmt = DB.prepareStatement(SQL, null);
		ResultSet rs = pstmt.executeQuery();
		Integer Cantidad = 0;
		while (rs.next()){
			AuxC_Bpartner_ID = rs.getInt("C_BPARTNER_ID");
			Aux_MBPartner = new MBPartner(getCtx(), AuxC_Bpartner_ID, get_TrxName());
			
			if(Aux_MBPartner.getisActive().equals("Y")){
				//Aux_MBPartner.setXX_Observation(Msg.getMsg(getCtx(), "XX_InactiveAutomatic"));
				Aux_MBPartner.setXX_CancellationMotive_ID(m_XX_Observation);
				Aux_MBPartner.setIsActive(false);
				
				Calendar cal=Calendar.getInstance();
				
				int year = cal.get(Calendar.YEAR);
	          	int month = cal.get(Calendar.MONTH);
	          	int day = cal.get(Calendar.DATE);
	          	int hour = cal.get(Calendar.HOUR);
	          	int min = cal.get(Calendar.MINUTE);
	          	int sec = cal.get(Calendar.SECOND);
	          	int nano = cal.get(Calendar.SECOND);
				
				Timestamp XXExitDate = new Timestamp(year-1900,month,day,hour,min,sec,nano);
				Aux_MBPartner.setXX_ExitDate(XXExitDate);
				
				if (Aux_MBPartner.save())
					Cantidad++;	
			}
		}
		
		rs.close();
		pstmt.close();
		
		/*return Msg.getMsg(getCtx(), "XX_NumberBPartnerInactive", );*/
		return Msg.getMsg(getCtx(), "XX_NumberBPartnerInactive", new String[] {Cantidad.toString()});
		
	}

}
