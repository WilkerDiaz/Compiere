/**
 * 
 */
package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;

/**
 * @author Jorge E. Pires G.
 *
 */
public class InactiVendorProcess extends SvrProcess {

	private Integer m_XX_Observation = null;
	
	/** ID del BPartner				*/
	private int 	m_C_BPartner_ID = 0;

	/**
	 * 	Consulta el status de O/C por C_BPartner
	 */
	private String ConsultaOrdenesCompras(int C_BPartner_ID){
		String aux = "VACIO";

		String sql = "SELECT COUNT(*) "
				   + "FROM C_ORDER "
				   + "WHERE C_BPartner_ID = " + C_BPartner_ID + " AND "
				   		  +"upper(XX_ORDERSTATUS) NOT IN ('ANULADA', 'CHEQUEADA')";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			//System.out.println(sql);
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getString("COUNT(*)");
			}
			return aux;
		}
		catch (SQLException e){
			// TODO: handle exception
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return aux;
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}//ConsultaOrdenesCompras

	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {
		String CantidadOC = ConsultaOrdenesCompras(m_C_BPartner_ID);
		
		//System.out.println(CantidadOC);
		if ( (CantidadOC.equals("VACIO")) || (new Integer (CantidadOC).intValue() <= 0 ) ){
			
			MBPartner AuxBPartner = new MBPartner(Env.getCtx(), m_C_BPartner_ID, null);
			//AuxBPartner.setXX_Observation(m_XX_Observation);
			AuxBPartner.setXX_CancellationMotive_ID(m_XX_Observation);
			Calendar cal=Calendar.getInstance();
			
			int year = cal.get(Calendar.YEAR);
          	int month = cal.get(Calendar.MONTH);
          	int day = cal.get(Calendar.DATE);
          	int hour = cal.get(Calendar.HOUR);
          	int min = cal.get(Calendar.MINUTE);
          	int sec = cal.get(Calendar.SECOND);
          	int nano = cal.get(Calendar.SECOND);
			
			Timestamp XXExitDate = new Timestamp(year-1900,month,day,hour,min,sec,nano);
			AuxBPartner.setXX_ExitDate(XXExitDate);
			
			AuxBPartner.setIsActive(false);
			
			/****Jessica Mendoza****/
			/****Desactivar la sincronizacion con el banco****/
			if (AuxBPartner.getXX_SynchronizationBank() == true){
				AuxBPartner.setXX_SynchronizationBank(false);
			}
			/****Fin código - Jessica Mendoza****/
			
			if (AuxBPartner.save()){
				return Msg.getMsg(getCtx(), "Success");
			}else{
				return Msg.getMsg(getCtx(), "Failure");
			}
		}else
			return Msg.getMsg(getCtx(), "XX_VendorAssociatePurchaOrder");
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("m_C_BPartner_ID"))
				m_C_BPartner_ID = element.getParameterAsInt();
			else if (name.equals("m_XX_Observation"))
				m_XX_Observation = ((BigDecimal)element.getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

}
