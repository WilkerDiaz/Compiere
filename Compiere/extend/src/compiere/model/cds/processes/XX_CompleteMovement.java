
package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_M_Movement;
import org.compiere.process.DocumentEngine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MMovement;
import compiere.model.cds.MProduct;
import compiere.model.cds.As400DbManager;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VLO_DispatchGuide;
import compiere.model.cds.X_XX_VLO_Travel;
import compiere.model.cds.X_XX_VMR_Department;


/**
 * @author Javier Pino
 * 
 * Maneja la aprobacion de movimientos
 * 
 * */
public class XX_CompleteMovement extends SvrProcess {


	
	@Override
	protected String doIt() throws Exception {

		//Aprobar el movimiento, y colocar la fecha de solicitud		
		MMovement move = new MMovement(getCtx(), getRecord_ID(), get_TrxName());
		int rolActual = Env.getCtx().getContextAsInt("#AD_Role_ID");
		int tipoMovimiento = move.getC_DocType_ID();

		
		//Primera aprobación
		if (move.getXX_Status().equals("CR")) {
			
			//Si es un traspaso, debe realizarla un planificador sino detenerse
			if (Utilities.esTraspaso(tipoMovimiento) || Utilities.esMovimientoCD(tipoMovimiento)) { 					
				if (!Utilities.esRolDePlanificacion(rolActual)) {
					ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "XX_NotAPlanner");
					return Msg.translate(getCtx(), "XX_NotAPlanner");					
				}
			
			//Si es una devolucion, debe realizarla un usuario de tienda sino detenerse
			} else if (Utilities.esDevolucion(tipoMovimiento)) {
				if (!Utilities.esRolDeTienda(rolActual)) {
					ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "XX_NotAStoreRole");
					return Msg.translate(getCtx(), "XX_NotAPlanner");
				}
				
			}		
			//Si los usuarios están autorizados, continuar

			//Preparar el movimiento
			move.setDocAction(X_M_Movement.DOCACTION_Prepare);
			DocumentEngine.processIt(move, X_M_Movement.DOCACTION_Prepare);	
			move.save();
			move.load(get_TrxName());
			if (move.getDocStatus().equals("IN")) {
				ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "NoLines");
			} else {
				
				//Aca debo actualizar las cantidades aprobadas
				String sql = "SELECT QtyRequired, XX_ApprovedQty FROM M_MOVEMENTLINE WHERE M_MOVEMENT_ID = " + move.getM_Movement_ID();
				PreparedStatement ps_1 = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, null);
				ResultSet rs_1 = ps_1.executeQuery();
				int qty_required = 0; 
				try {
					while (rs_1.next()) {							
						qty_required = rs_1.getInt("QtyRequired");
						rs_1.updateInt("XX_ApprovedQty", qty_required);
						rs_1.updateRow();
					}
				} catch (Exception e) {
					e.printStackTrace();
				
				} finally {
					rs_1.close();
					ps_1.close();
				}

				move.setXX_Status("PE");
				move.setXX_RequestDate(move.getUpdated());	
				move.save();		
				
				//Se envian correos dependiendo del tipo de documento
				if (Utilities.esTraspaso(tipoMovimiento)) { 
					enviarCorreoATienda(move,  Env.getCtx().getContextAsInt("#XX_L_MT_PTRANSFERAPPROVAL_ID"));
				} else if (Utilities.esMovimientoCD(tipoMovimiento)) {
					enviarCorreoJefeCD(move,  Env.getCtx().getContextAsInt("#XX_L_MT_PTRANSFERAPPROVAL_ID"));
				} else {
					enviarCorreoAComprador(move, Env.getCtx().getContextAsInt("#XX_L_MT_PTRANSFERAPPROVAL_ID"));
				}
			}
		} else if (move.getXX_Status().equals("PE")) {
			
			//Verificar los roles
			
			//Si es un traspaso debe realizarla tienda sino detenerse
			if (Utilities.esTraspaso(tipoMovimiento)) { 					
				if (!Utilities.esRolDeTienda(rolActual)) {
					ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "XX_NotAStoreRole");
					return Msg.translate(getCtx(), "XX_NotAStoreRole");					
				}
			
				//Si es un movimiento de inventario entre cd debe realizarla el jefe de cd
			} else if (Utilities.esMovimientoCD(tipoMovimiento)) { 					
				if (rolActual!=1000102) {
					ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "Debe tener el rol de Jefe de CD para completar este movimiento ");
					return Msg.translate(getCtx(), "Debe tener el rol de Jefe de CD para completar este movimiento ");					
				}
			}
			//Si es una devolucion, debe realizarla un comprador sino detenerse
			else if (Utilities.esDevolucion(tipoMovimiento)) {
								
				if (Utilities.esRolDeCompras(rolActual)) {
					
					//Aca debo verificar que la suma no pase del monto de cuentas
					MBPartner business_partner = null;
					String sql_detalles =
						" SELECT LI.LINE LIN, LI.M_PRODUCT_ID PRO," +
						" PR.C_BPARTNER_ID BP, LI.PRICEACTUAL PRE, LI.QTYREQUIRED QTY," +
						" (SELECT rate FROM C_Tax WHERE ValidFrom= " +
						" (SELECT MAX(ValidFrom) FROM C_Tax WHERE C_TaxCategory_ID= PR.C_TaxCategory_ID)) RATE" +    
						" FROM M_MOVEMENTLINE LI JOIN M_PRODUCT PR ON (PR.M_PRODUCT_ID = LI.M_PRODUCT_ID) " +
						" WHERE M_MOVEMENT_ID = ?";
						
					PreparedStatement ps = null;
					ResultSet rs = null;
					try {
						 
						BigDecimal costoBruto = Env.ZERO;
						BigDecimal cantidad = null;
						BigDecimal precio = null;
						BigDecimal impuesto = null;
						
						ps = DB.prepareStatement(sql_detalles, null);
						ps.setInt(1, move.get_ID());					
						rs = ps.executeQuery();
						while (rs.next()) {
							
							//Buscar el business partner del producto
							business_partner = null;
							if (business_partner == null && rs.getInt("BP") != 0)
								business_partner = new MBPartner(Env.getCtx(), rs.getInt("BP"), null);
													
							//El producto tiene un proveedor asociado
							if (business_partner == null) {
								MProduct producto = new MProduct(Env.getCtx(), rs.getInt("PRO"), null);						
								String mss = Msg.getMsg(Env.getCtx(), "XX_ProductWithoutBPartner", 
										new String[] {
											producto.getValue() + "-" + producto.getName()});
								rs.close();
								ps.close();
								ADialog.error(1, new Container(), mss);
								return mss;
							}
							precio = rs.getBigDecimal("PRE");
							impuesto = rs.getBigDecimal("RATE");
							if (impuesto != null) {
								impuesto = impuesto.divide(Env.ONEHUNDRED,2, RoundingMode.HALF_UP);
								impuesto = impuesto.add(Env.ONE);
								precio = precio.multiply(impuesto);
							} 
													
							//Obtener el costobruto del business partner
							cantidad = rs.getBigDecimal("QTY");
							costoBruto = costoBruto.add(precio.multiply(cantidad));
						}
						rs.close();
						ps.close();					
						
						/****Jessica Mendoza****/ 
						/****Saldo total del socio de negocio***/
						BigDecimal saldo = balancePartner(business_partner.get_ID());
						/****Monto total de las devoluciones***/
						BigDecimal amount = amountReturns(move.getM_Movement_ID());
						BigDecimal valor = new BigDecimal(-1);
						amount = amount.multiply(valor);
						if ((saldo.compareTo(amount) == 1) || (saldo.equals(amount))){
							String mss = Msg.getMsg(Env.getCtx(), "XX_BalanceExcededBPartner", 
									new String[] {
										"" + saldo.setScale(2, RoundingMode.HALF_UP), 
										"" + amount.setScale(2, RoundingMode.HALF_UP),							
										business_partner.getValue() + "-" + business_partner.getName()});									
							ADialog.error(1, new Container(), mss);
							return mss;
						}else{
							updateStatusPartner(business_partner.get_ID());
							//ADialog.error(EnvConstants.WINDOW_INFO, new Container(), Msg.getMsg(Env.getCtx(), "XX_BalanceLowerError"));
						
							//ESTE ES EL CODIGO QUE HACIA ANTERIORMENTE PARA CALCULAR EL SALDO DESDE EL AS400
						/*	As400DbManager as400 = new As400DbManager();
							as400.conectar();
							
							//Acá debo verificar la conexion al as400 para sacar el saldo
							BigDecimal cuentasPorPagar = null;
							String sql_cuentasxpagar = "SELECT COALESCE(SUM(MONMOV + MIPMOV),0) FROM ICTFILE.CPPD01 " 
								+ "WHERE COEMPE = " + Integer.parseInt(business_partner.getValue()) + " AND STACTA IN ('1','3','5')";
							//Crear la sentencia
							Statement ps_s = as400.conexion.createStatement(
									ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);				
							rs = as400.realizarConsulta(sql_cuentasxpagar, ps_s);				
							if (rs.next()) {
								cuentasPorPagar = rs.getBigDecimal(1);				
							}
							rs.close();
							ps_s.close();					
							as400.desconectar();			
							//Verificar que la cantidad no supere el saldo del proovedor
							if (costoBruto.compareTo(cuentasPorPagar) == 1) {					
								String mss = Msg.getMsg(Env.getCtx(), "XX_BalanceExcededBPartner", 
										new String[] {
											"" + costoBruto.setScale(2, RoundingMode.HALF_UP), 
											"" + cuentasPorPagar.setScale(2, RoundingMode.HALF_UP),							
											business_partner.getValue() + "-" + business_partner.getName()});									
	
								ADialog.error(1, new Container(), mss);
								return mss;
							}		*/
						}/****Fin el codigo de Jessica Mendoza****/
						
					} catch (SQLException e) {
						ADialog.error(1, new Container(), "XX_DatabaseAccessError");
						return Msg.translate(Env.getCtx(), "XX_DatabaseAccessError");
					}
					
				} else {
					ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "XX_NotABuyer");
					return Msg.translate(getCtx(), "XX_NotABuyer");
				}				
			}
		
			move.setXX_DispatchDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));			
			if ( move.getDocStatus().equals(X_M_Movement.DOCSTATUS_InProgress)) {
				if (Utilities.esTraspaso(tipoMovimiento) || Utilities.esMovimientoCD(tipoMovimiento)) {
					move.setXX_Status("ED");
				} else if (Utilities.esDevolucion(tipoMovimiento)) {
					move.setXX_Status("AP");
				} 				
				move.save();				
			}	
			
			//Aca debo actualizar las cantidades a Recibir WDIAZ
			String sql = "SELECT XX_ApprovedQty, XX_QuantityReceived  FROM M_MOVEMENTLINE " +
					"WHERE M_MOVEMENT_ID = " + move.getM_Movement_ID();
			PreparedStatement ps_1 = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, get_TrxName());
			ResultSet rs_1 = ps_1.executeQuery();
			int qtyAproved = 0; 
			try {
				while (rs_1.next()) {							
					qtyAproved = rs_1.getInt("XX_ApprovedQty");
					rs_1.updateInt("XX_QuantityReceived", qtyAproved);
					rs_1.updateRow();
				}
			} catch (Exception e) {
				e.printStackTrace();
			
			} finally {
				rs_1.close();
				ps_1.close();
			}
			//Fin			
			
			//Acá se envian los correos de confirmación
			
			
		} else {
			
		}		
		return "";
	}
	
	private void enviarCorreoJefeCD(X_XX_VLO_DispatchGuide guiaDespacho){		
		MWarehouse almaSalida = new MWarehouse(getCtx(), guiaDespacho.getXX_DepartureWarehouse_ID(), null);
		MWarehouse almaLleada = new MWarehouse(getCtx(), guiaDespacho.getXX_ArrivalWarehouse_ID(), null);
		X_XX_VLO_Travel viaje = new X_XX_VLO_Travel(getCtx(), guiaDespacho.getXX_VLO_Travel_ID(), null);
		
		/*
		 * Fecha
		 * */
		Date fechaViaje = new Date(viaje.getXX_TravelDate().getTime());
		SimpleDateFormat formatoY = new SimpleDateFormat("yyyy");
		String ano = formatoY.format(fechaViaje);
		
		SimpleDateFormat formatoM = new SimpleDateFormat("MM");
		String mes = formatoM.format(fechaViaje);
		
		SimpleDateFormat formatoD = new SimpleDateFormat("dd");
		String dia = formatoD.format(fechaViaje);
		
		
		String Mensaje = Msg.getMsg( getCtx(), 
				 "XX_MsgDGStore", 
				 new String[]{guiaDespacho.getDocumentNo(),
							  almaSalida.getValue()+"-"+almaSalida.getName(),
							  almaLleada.getValue()+"-"+almaLleada.getName(),
							  dia+"/"+mes+"/"+ano
		});
		
		//Al Jefe de CD
		//Selecciono el o los Jefes de CD
		String SQL = "select ad_user_id from AD_User_Roles where AD_Role_ID=1000102	";
		
    	Vector<Integer> storeManagers = new Vector<Integer>();
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){	
				storeManagers.add(rs.getInt("AD_USER_ID"));
			}							
									
		}
		catch(Exception a){
			log.log(Level.SEVERE,SQL,a);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//Envio correos a los jefes
		Utilities f = null;
		for(int i=0; i<storeManagers.size();i++){
	
			f = new Utilities(Env.getCtx(), null, 1000055, Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, storeManagers.get(i),null);
			try {
				f.ejecutarMail(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			f = null;
		}
		

	}//Fin del metodo
	
	
	
	
	/** Envia correo al comprador al Aprobarse una devolución */
	private void enviarCorreoAComprador (MMovement movimiento, int plantillaDeCorreo) {
		
		MWarehouse almaSalida = new MWarehouse(getCtx(), movimiento.getM_WarehouseFrom_ID(), null);
		MWarehouse almaLleada = new MWarehouse(getCtx(), movimiento.getM_WarehouseTo_ID(), null);
		X_XX_VMR_Department departamento = new X_XX_VMR_Department(getCtx(), movimiento.getXX_VMR_Department_ID(), null);
		
		
		//Mensaje debe indicar departamento, origen, destino y traspaso		
		String mensaje = Msg.getMsg( getCtx(), "XX_PReturnApproval", 
				 new String[]{movimiento.getDocumentNo(),
							  departamento.getValue() + "-" + departamento.getName(),								
							  almaSalida.getValue()+"-"+almaSalida.getName(),
							  almaLleada.getValue()+"-"+almaLleada.getName(),
							  movimiento.getXX_StatusName()
		});
		
		//Se busca al comprador
		String SQL = "SELECT AD_USER_ID FROM AD_USER WHERE ISACTIVE='Y' " +
					" AND C_BPARTNER_ID = " + departamento.getXX_UserBuyer_ID() +
					 "AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";		
		
    	int comprador = 0;
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();			
			if (rs.next()){	
				comprador = rs.getInt("AD_USER_ID");
			} else {
				return ;
			}
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a){
			log.log(Level.SEVERE,SQL,a);
		}
		
		//Envio correo al comprador
		Utilities f = null;		
		f = new Utilities(Env.getCtx(), null, plantillaDeCorreo, mensaje, -1, 
				Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, comprador, null);
		try {
			f.ejecutarMail(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
			f = null;

	} 
	
	/** Envia correos al jefe de cd en caso de la aprobación de un traspaso */
	private void enviarCorreoJefeCD( MMovement movimiento , int plantillaDeCorreo){
				
		MWarehouse almaSalida = new MWarehouse(getCtx(), movimiento.getM_WarehouseFrom_ID(), null);
		MWarehouse almaLleada = new MWarehouse(getCtx(), movimiento.getM_WarehouseTo_ID(), null);
		X_XX_VMR_Department departamento = new X_XX_VMR_Department(getCtx(), movimiento.getXX_VMR_Department_ID(), null);
		
		//Mensaje debe indicar departamento, origen, destino y traspaso		
		String mensaje = Msg.getMsg( getCtx(), "XX_PTransferApproval", 
				 new String[]{movimiento.getDocumentNo(),
							  departamento.getValue() + "-" + departamento.getName(),								
							  almaSalida.getValue()+"-"+almaSalida.getName(),
							  almaLleada.getValue()+"-"+almaLleada.getName(),
							  movimiento.getXX_StatusName()
		});
		
		//Al Jefe de CD

		String SQL = "select ad_user_id from AD_User_Roles where AD_Role_ID=1000102	";		
		
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
		
		//Envio correos a los jefes
		Utilities f = null;
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
	
	/** Envia correos al personal de tienda en caso de la aprobación de un traspaso */
	private void enviarCorreoATienda( MMovement movimiento , int plantillaDeCorreo){
				
		MWarehouse almaSalida = new MWarehouse(getCtx(), movimiento.getM_WarehouseFrom_ID(), null);
		MWarehouse almaLleada = new MWarehouse(getCtx(), movimiento.getM_WarehouseTo_ID(), null);
		X_XX_VMR_Department departamento = new X_XX_VMR_Department(getCtx(), movimiento.getXX_VMR_Department_ID(), null);
		
		//Mensaje debe indicar departamento, origen, destino y traspaso		
		String mensaje = Msg.getMsg( getCtx(), "XX_PTransferApproval", 
				 new String[]{movimiento.getDocumentNo(),
							  departamento.getValue() + "-" + departamento.getName(),								
							  almaSalida.getValue()+"-"+almaSalida.getName(),
							  almaLleada.getValue()+"-"+almaLleada.getName(),
							  movimiento.getXX_StatusName()
		});
		
		//Al Gerente de Tienda
		//Selecciono el o los gerentes de Tienda
		String SQL = "SELECT AD_USER_ID FROM AD_USER WHERE ISACTIVE='Y' " +
					" AND C_BPARTNER_ID IN "+
					"("+
						"SELECT C_BPARTNER_ID " +
						"FROM C_BPARTNER WHERE isActive='Y' "+
						"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_STOREMAN_ID")+" " +
						"AND (M_WAREHOUSE_ID = "+ almaLleada.get_ID()+" " +
						"OR M_WAREHOUSE_ID = "+ almaSalida.get_ID() +") " +
						"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")"+
					") "+
					"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";		
		
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
		
		//Envio correos a los gerentes
		Utilities f = null;
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
		
		//*********************************
		//Selecciono los asesores de almacen
    	SQL = "SELECT AD_USER_ID FROM AD_USER WHERE  ISACTIVE='Y' " +
    			"AND C_BPARTNER_ID IN "+
				"("+
					"SELECT C_BPARTNER_ID FROM C_BPARTNER WHERE isActive='Y' "+
					"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_DEPASE_ID")+" " +
					"AND (M_WAREHOUSE_ID = "+ almaLleada.get_ID()+" " +
					"OR M_WAREHOUSE_ID = "+ almaSalida.get_ID() +") " +
					"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")"+
				") "+
				"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";			
		
    	Vector<Integer> warehouseAsessors = new Vector<Integer>();
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				warehouseAsessors.add(rs.getInt("AD_USER_ID"));
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		//Envio correos a los asesores
		Utilities m = null;
		for(int i=0; i<warehouseAsessors.size();i++){
			
			m = new Utilities(Env.getCtx(), null, plantillaDeCorreo, mensaje, -1,
					Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, warehouseAsessors.get(i),null);
			try {
				m.ejecutarMail();
			} catch (Exception e) {
				e.printStackTrace();
			}
			m = null;
		}	
		
		
		//*********************************
		//Selecciono los Gerentes de Area ADMIN y MERCA, Asesor de Inventario
    	SQL = "SELECT AD_USER_ID FROM AD_USER WHERE  ISACTIVE='Y' " +
    			"AND C_BPARTNER_ID IN "+
				"("+
					"SELECT C_BPARTNER_ID FROM C_BPARTNER WHERE isActive='Y' "+
					"AND C_JOB_ID IN (" +
					" " + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_ADMINMANAG_ID")+"," +
					" " + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_GAMERC_ID")+"," +
					" " + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_INVASSES_ID")+
					") " +
					"AND (M_WAREHOUSE_ID = "+ almaLleada.get_ID()+" " +
					"OR M_WAREHOUSE_ID = "+ almaSalida.get_ID() +") " +
					"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")"+
				") "+
				"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";			
		
    	Vector<Integer> managerArea = new Vector<Integer>();
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				managerArea.add(rs.getInt("AD_USER_ID"));
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		//Envio correos a gerentes de area
		Utilities u = null;
		for(int i=0; i<managerArea.size();i++){
			
			u = new Utilities(Env.getCtx(), null, plantillaDeCorreo, mensaje, -1,
					Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, managerArea.get(i),null);
			try {
				u.ejecutarMail();
			} catch (Exception e) {
				e.printStackTrace();
			}
			u = null;
		}	
	}	

	@Override
	protected void prepare() {
				
	}
	
	/**
	 * Busca el saldo de un socio de negocio especifico
	 * @author Jessica Mendoza
	 * @param idPartner identidifcador del socio de negocio
	 * @return saldo saldo del socio de negocio
	 * @throws SQLException 
	 */
	public BigDecimal balancePartner(int idPartner) throws SQLException{
		BigDecimal saldo = new BigDecimal(0);
		String sql = "select totalOpenBalance " +
					 "from C_BPartner " +
					 "where C_BPartner_ID = " + idPartner;
		PreparedStatement pstmt = DB.prepareStatement(sql, null); 
		ResultSet rs = pstmt.executeQuery();
		try{
			if(rs.next()) {							
				saldo = rs.getBigDecimal(1);
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		
		}
		return saldo;
	}
	
	/**
	 * Busca el total de la devolucion del proveedor
	 * @author Jessica Mendoza
	 * @param idMov id del movimiento
	 * @return amount monto de la suma de los productos de un movimiento
	 * @throws SQLException
	 */
	public BigDecimal amountReturns(int idMov) throws SQLException{
		BigDecimal amount = new BigDecimal(0);
		String sql = "select sum(priceActual * qtyRequired) " +
					 "from M_MovementLine " +
					 "where M_Movement_ID = " + idMov;
		PreparedStatement pstmt = DB.prepareStatement(sql, null); 
		ResultSet rs = pstmt.executeQuery();
		try{
			if(rs.next()) {							
				amount = rs.getBigDecimal(1);
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		
		}
		return amount;
	}
	
	/**
	 * Modifica el status del socio de negocio a 'Bloqueado'
	 * @author Jessica Mendoza
	 * @param idPartner
	 */
	public void updateStatusPartner(int idPartner){
		String sql = "update C_BPartner set C_BP_Status_ID = " +
					 "(select C_BP_Status_ID from  C_BP_Status where value = 'BLO') " +
					 "where C_BPartner_ID = " + idPartner;
		try {
			DB.executeUpdate(null,sql);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		
	}
}
