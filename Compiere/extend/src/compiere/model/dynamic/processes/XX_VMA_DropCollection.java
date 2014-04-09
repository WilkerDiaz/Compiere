package compiere.model.dynamic.processes;
import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.MCampaign;

/**
 * 
 * Esta clase contiene el proceso que se va a ejecutar al momento de querer
 * eliminar una colección asociada a una campaña
 * 
 * @author María Vintimilla
 * @version 1.0
 */

public class XX_VMA_DropCollection extends SvrProcess{

	private int Collection_V_ID = 0;
	private int Collection_ID = 0;
	private int C_Campaign_ID = 0;
		
	/**
	 * Se toman los parámetros del proceso.
	 */
	protected void prepare() {	
		ProcessInfoParameter[] para = getParameter();
		for(int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("XX_VMR_Collection_ID"))
				Collection_V_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		C_Campaign_ID = getRecord_ID();
		
	}//prepare
		
	/**
	 * procedimiento que ejecuta la eliminación de la colección asociada
	 * a una campaña
	 */
	protected String doIt() throws Exception {
		MCampaign campaign = 
			new MCampaign(Env.getCtx(),C_Campaign_ID,null);
		String sql1 = " Select XX_VMR_Collection_ID ID " +
				" From XX_VMA_Collection_V " +
				" Where XX_VMA_Collection_V_ID = " + Collection_V_ID; 
		
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		
		try {
			pstmt1 = DB.prepareStatement(sql1, null);
			rs1 = pstmt1.executeQuery();
			if (rs1.next()) {
				Collection_ID = rs1.getInt("ID");
			}
		} // try
		catch (SQLException e) {
			log.log(Level.SEVERE, sql1, e);
		}
		finally{
			DB.closeResultSet(rs1);
			DB.closeStatement(pstmt1);
		}
		
		String sql = " Delete " +
					" From XX_VMA_RelCampaign " +
					" Where C_Campaign_ID = " + C_Campaign_ID +
					" And XX_VMR_Collection_ID = " + Collection_ID;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				ADialog.error(Env.WINDOW_INFO, null, "Error", 
						Msg.translate(Env.getCtx(), "XX_ErrorDrop"));
				throw new Exception("No se borró la colección");
			}
		} // try
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		campaign.setXX_VMA_RelCampaign_ID(0);

		if(campaign.save()){
			ADialog.info(1, new Container(),
					Msg.translate(Env.getCtx(), "XX_DeleteCollection"));
			return "";
		}
		else{
			ADialog.error(Env.WINDOW_INFO, null, "Error", 
					Msg.translate(Env.getCtx(), "XX_ErrorDrop"));
			throw new Exception("No se borró la colección");
		}
		
	} // doIt
	
} // XX_VMA_DropCollection
