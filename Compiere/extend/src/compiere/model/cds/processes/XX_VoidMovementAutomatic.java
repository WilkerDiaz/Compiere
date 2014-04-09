package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_M_Movement;
import org.compiere.process.DocumentEngine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MMovement;

/**
 * Esta función permitirá la anulación de un traspaso en forma automática a 
 * aquellos traspasos a los que no se les ha generado una guía de despacho.
 * @author Javier Pino
 */
public class XX_VoidMovementAutomatic extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		String select = "SELECT M_MOVEMENT_ID FROM M_MOVEMENT WHERE C_DOCTYPE_ID = " + 
		+ Env.getCtx().getContextAsInt("#XX_L_DOCTYPETRANSFER_ID") +
		" AND ((XX_STATUS = 'CR' AND SYSDATE - MOVEMENTDATE > 30)" +
		" OR (XX_STATUS  = 'PE' AND SYSDATE - XX_REQUESTDATE > 30)) " +
		" AND M_MOVEMENT_ID NOT IN ( SELECT D.M_MOVEMENTT_ID FROM XX_VLO_DETAILDISPATCHGUIDE D " +
			" WHERE D.M_MOVEMENTT_ID IS NOT NULL ) ";		
		PreparedStatement ps = DB.prepareStatement(select, null);
		ResultSet rs = ps.executeQuery();
		try { 
			while(rs.next()) {
				int movement = rs.getInt(1);
				MMovement move = new MMovement(getCtx(), movement , get_TrxName());							
				//Verificar que al anularse cambie el usuario de anulacion
				
				move.setXX_UserVoid_ID(getAD_User_ID());
				move.setXX_VoidMethod("AT");
				move.setXX_Status("VO");
				move.save();
				move.load(get_TrxName());
				move.setXX_VoidDate(move.getUpdated());				
				
				if ( move.getDocStatus().equals("IP") || move.getDocStatus().equals("DR")) {
					move.setDocAction(X_M_Movement.DOCACTION_Void);
					DocumentEngine.processIt(move, X_C_Order.DOCACTION_Void);
				}		    
				move.save();									
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "XX_DatabaseAccessError");
		}
		return "";
	}

	@Override
	protected void prepare() {
				
	}

}
