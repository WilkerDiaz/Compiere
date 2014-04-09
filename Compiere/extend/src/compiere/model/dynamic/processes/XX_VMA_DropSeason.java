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

import compiere.model.cds.X_XX_VMR_Collection;

/**
 * 
 * Esta clase contiene el proceso que se va a ejecutar al momento de querer
 * eliminar una temporada asociada a una colección
 * 
 * @author María Vintimilla
 * @version 1.0
 */

public class XX_VMA_DropSeason extends SvrProcess{
	private int Season_V_ID = 0;
	private int Collection_ID = 0;
	private int Season_ID = 0;
		
	/**
	 * prepare
	 * Se toman los parámetros del proceso.
	 */
	protected void prepare() {	
		ProcessInfoParameter[] para = getParameter();
		for(int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("XX_VMA_Season_ID"))
				Season_V_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		Collection_ID = getRecord_ID();
		
	} // Fin prepare
		
	/**
	 * doIt
	 * Procedimiento que ejecuta la eliminación de la temporada asociada
	 * a una coleccion
	 */
	protected String doIt() throws Exception {
		X_XX_VMR_Collection col = 
			new X_XX_VMR_Collection(Env.getCtx(),Collection_ID,null);
		String sql1 = " Select XX_VMA_Season_ID ID " +
				" From XX_VMA_Season_V " +
				" Where XX_VMA_Season_V_ID = " + Season_V_ID; 
		
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		
		try {
			pstmt1 = DB.prepareStatement(sql1, null);
			rs1 = pstmt1.executeQuery();
			if (rs1.next()) {
				Season_ID = rs1.getInt("ID");
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
					" From XX_VMA_RelSeason " +
					" Where XX_VMR_Collection_ID = " + Collection_ID +
					" And XX_VMA_Season_ID = " + Season_ID;
		System.out.println("SQL: "+sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				ADialog.error(Env.WINDOW_INFO, null, "Error", 
						Msg.translate(Env.getCtx(), "XX_ErrorDropSeason"));
				throw new Exception("No se borró la temporada");
			}
		} // try
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//col.setXX_VMA_RelSeason_ID(0);

		if(col.save()){
			ADialog.info(1, new Container(),
					Msg.translate(Env.getCtx(), "XX_DeleteCollection"));
			return "";
		}
		else{
			ADialog.error(Env.WINDOW_INFO, null, "Error", 
					Msg.translate(Env.getCtx(), "XX_ErrorDropSeason"));
			throw new Exception("No se borró la colección");
		}
		
	} // doIt
	
} // XX_VMA_DropSeason
