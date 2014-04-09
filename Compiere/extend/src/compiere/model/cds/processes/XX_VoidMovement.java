package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_M_InOut;
import org.compiere.model.X_M_Movement;
import org.compiere.process.DocumentEngine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MMovement;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VLO_DispatchGuide;

public class XX_VoidMovement extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		MMovement move = new MMovement(getCtx(), getRecord_ID(), get_TrxName());
		
		//Evitar que el movimiento esté en una guia de despacho aunque no esté aprobada no se pueda anular		
		String select = "SELECT D.XX_VLO_DISPATCHGUIDE_ID " +
				" FROM XX_VLO_DETAILDISPATCHGUIDE D WHERE D.M_MOVEMENTT_ID = ? OR D.M_MOVEMENTR_ID = ? OR D.M_MOVEMENTM_ID = ? ";
		int guia_despacho = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(select, null);
			ps.setInt(1, getRecord_ID());
			ps.setInt(2, getRecord_ID());
			ps.setInt(3, getRecord_ID());
			rs = ps.executeQuery();			
			if (rs.next()) {
				guia_despacho = rs.getInt(1); 
			}
			
			//Si la guia existe entonces no se puede anular
			if (guia_despacho > 0) {
				X_XX_VLO_DispatchGuide guia = new X_XX_VLO_DispatchGuide(getCtx(), guia_despacho, null);
				String mss = Msg.getMsg(Env.getCtx(), "XX_MovementCantbeVoided", 
						//Order					
						new String[] {guia.getDocumentNo()});
				ADialog.info(1, new Container(), mss);
				return mss;
			}	
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			ADialog.error(1, new Container(), "XX_DatabaseAccessError");			
		} finally {
			if (ps != null) 
				try {
					ps.close();
				} catch (SQLException e2) {}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {}
			
		}
		
		//Verificacion de roles
		int rolActual = Env.getCtx().getContextAsInt("#AD_Role_ID");
		int tipoMovimiento = move.getC_DocType_ID();
		boolean puedeAnularse = false;
		
		//Si es coordinador de cuentas por pagar
		if (rolActual == Env.getCtx().getContextAsInt("#XX_L_ROLE_ACCOUNTTOPAYCOORD_ID")) {
			puedeAnularse = true;			
		} else {
			//Si es una devolucion, debe realizarla un comprador sino detenerse
			if (Utilities.esDevolucion(tipoMovimiento)) {							
				if (Utilities.esRolDeCompras(rolActual)) 
					puedeAnularse = true;
			}
			//Si es un traspaso o movimiento de inventario entre cds debe realizarlo planificacion		
			else if (Utilities.esRolDePlanificacion(rolActual)) { 					
				puedeAnularse = true;						
			}
		}
		if (!puedeAnularse) {
			ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "XX_VoidNotAllowed");
			return Msg.translate(getCtx(), "XX_VoidNotAllowed");
		}
		
		//Verificar que al anularse cambie el usuario de anulacion		
		move.setXX_UserVoid_ID(getAD_User_ID());
		move.setXX_VoidMethod("MN");
		move.save();
		move.load(get_TrxName());
		move.setXX_VoidDate(move.getUpdated());
		
		move.setDocAction(X_M_Movement.DOCACTION_Void);
		DocumentEngine.processIt(move, X_C_Order.DOCACTION_Void);
	    move.setXX_Status("VO");
		move.save();
		
		ADialog.info(1, new Container(), "XX_MovementVoided");
		return "";	
	}

	@Override
	protected void prepare() {
				
	}

}
