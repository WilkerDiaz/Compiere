
package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.model.MWarehouse;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MMovement;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_Department;


/**
 * @author Javier Pino
 * 
 * Maneja la aprobacion de movimientos
 * 
 * */
public class XX_SendPlannerlMailProcess extends SvrProcess {

	@Override
	protected String doIt() throws Exception {

		//Aprobar el movimiento, y colocar la fecha de solicitud		
		MMovement move = new MMovement(getCtx(), getRecord_ID(), get_TrxName());
		int rolActual = Env.getCtx().getContextAsInt("#AD_Role_ID");
		
		//Si es un traspaso debe realizarla tienda sino detenerse		
		if (!Utilities.esRolDeTienda(rolActual)) {
			ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "XX_NotAStoreRole");
			return Msg.translate(getCtx(), "XX_NotAStoreRole");					
		}
		enviarCorreoAPlanificacion(move, Env.getCtx().getContextAsInt("#XX_L_MT_PTRANSFERCREATION_ID"));
		return "";
	}
	
	/** Envia correo al comprador al Aprobarse una devolución */
	private void enviarCorreoAPlanificacion (MMovement movimiento, int plantillaDeCorreo) {
		
		MWarehouse almaSalida = new MWarehouse(getCtx(), movimiento.getM_WarehouseFrom_ID(), null);
		MWarehouse almaLleada = new MWarehouse(getCtx(), movimiento.getM_WarehouseTo_ID(), null);
		X_XX_VMR_Department departamento = new X_XX_VMR_Department(getCtx(), movimiento.getXX_VMR_Department_ID(), null);
		
		
		//Mensaje debe indicar departamento, origen, destino y traspaso		
		String mensaje = Msg.getMsg( getCtx(), "XX_PTransferCreation", 
				 new String[]{movimiento.getDocumentNo(),
							  departamento.getValue() + "-" + departamento.getName(),								
							  almaSalida.getValue()+"-"+almaSalida.getName(),
							  almaLleada.getValue()+"-"+almaLleada.getName(),
							  movimiento.getXX_StatusName()
		});
		
		//Se busca al planificador
		String SQL = "SELECT AD_USER_ID FROM AD_USER WHERE ISACTIVE='Y' " +
					" AND C_BPARTNER_ID = " + departamento.getXX_InventorySchedule_ID() +
					 "AND AD_Client_ID IN (0," + getAD_Client_ID() + ")";		
		
    	int planificador = 0;
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();			
			if (rs.next()){	
				planificador = rs.getInt("AD_USER_ID");
			} else {
				return ;
			}
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a){
			log.log(Level.SEVERE,SQL,a);
		}
		
		//Envio correo al Planificador		
		Utilities f = null;		
		f = new Utilities(Env.getCtx(), null, plantillaDeCorreo, mensaje, -1, 
				Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, planificador, null);
		try {
			f.ejecutarMail(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		f = null;
		
		//Buscar Jefe de Planificación
		SQL = "SELECT AD_USER_ID FROM AD_USER WHERE ISACTIVE='Y' " +
					" AND C_BPARTNER_ID IN "+
					"("+
						"SELECT C_BPARTNER_ID " +
						"FROM C_BPARTNER WHERE isActive='Y' "+
						"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_PLANMAN_ID")+" " +						
						"AND AD_Client_ID IN (0,"+getAD_Client_ID()+")"+
					") "+
					"AND AD_Client_ID IN (0,"+getAD_Client_ID()+")";		
    	Vector<Integer> storeManagers = new Vector<Integer>();
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();			
			while(rs.next()){	
				storeManagers.add(rs.getInt("AD_USER_ID"));
			}									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a){
			log.log(Level.SEVERE,SQL,a);
		}
		
		//Envio correos a los jefes de planificación
		f = null;
		for(int i=0; i<storeManagers.size();i++){
	
			f = new Utilities(Env.getCtx(), null, plantillaDeCorreo, mensaje, -1,
					Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, storeManagers.get(i),null);
			try {
				f.ejecutarMail(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			f = null;
		}
	}	

	@Override
	protected void prepare() {
				
	}
}
