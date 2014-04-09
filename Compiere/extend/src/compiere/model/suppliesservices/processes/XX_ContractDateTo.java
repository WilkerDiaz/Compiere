package compiere.model.suppliesservices.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/** Purchase of Supplies and Services 
 * Maria Vintimilla Funcion 29
 * Notification via e-mail if the DateTo has passed. 
 * Set Status to Finalizado **/
public class XX_ContractDateTo extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		Integer Contract_ID = new Integer(0);
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		String SQL1 = " SELECT XX_Contract_ID " +
					" FROM XX_Contract " +
					// Verificar si el contrato no tiene renovación automatica
					" WHERE XX_AutomaticRenovation = 'N' and " + 
					// Verificar si el contrato no está terminado anticipadamente o finalizado
					" XX_Status not in ('A','F') and" +
					// Verificar la fecha desde del contrato
					" TRUNC(XX_DateTo) < sysdate " +
					" AND AD_Client_ID = " + 
					Env.getCtx().getAD_Client_ID()+
					" ORDER BY XX_DateTo, XX_AutomaticRenovation ";
					//System.out.println("SQL1: "+SQL1);

		try {
			pstmt1 = DB.prepareStatement(SQL1, null); 
			rs1 = pstmt1.executeQuery();

			while(rs1.next()){
				Contract_ID = rs1.getInt("XX_Contract_ID");
				String SQL2 = "UPDATE XX_Contract " +
							" SET XX_Status = 'F', ISACTIVE = 'N' " +
							" WHERE XX_Contract_ID = "+Contract_ID;
				//System.out.println(SQL2);
				int no = DB.executeUpdate(get_Trx(), SQL2);
				if (no != 1)
					log.warning("(Contrato vencido o finalizado) #" + no);
			}// while
		}// try
		catch (Exception e) {
			log.log(Level.SEVERE, SQL1);
			e.printStackTrace();
		}
		finally {
			DB.closeResultSet(rs1);
			DB.closeStatement(pstmt1);

		}//finally
		return " Se actualizaron los contratos vencidos ";
	}

	@Override
	protected void prepare() {

	}

}//XX_ContractDateTo
