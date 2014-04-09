package compiere.model.payments.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MBPartner;
import compiere.model.payments.pmgBx011;

/**
 * Se encarga de generar el proceso para realizar la 
 * Interfaz de Actualización de Proveedores (Bx011)
 * Nombre del proceso en compiere: Interface Updated Partner
 * @author Jessica Mendoza
 *
 */
public class XX_InterfaceUpPartner extends SvrProcess{

	private String[][] ESTREG = {{"OK","Operación exitosa sobre el registro."},
			 {"01","Operación Cancelada, no posee autorización para utilizar esta interfaz."},
			 {"02","Operación Cancelada, el código del proveedor está en cero."},
			 {"03","Operación Cancelada, el nombre del proveedor está en blanco."},
			 {"04","Operación Cancelada, la dirección del proveedor está en blanco."},
			 {"05","Operación Cancelada, el tipo de proveedor está en blanco o no es válido."},
			 {"06","Operación Cancelada, el teléfono del proveedor está en blanco."},
			 {"07","Operación Cancelada, la persona de contacto está en blanco."},
			 {"08","Operación Cancelada, el tipo de persona está en blanco o no es válido."},
			 {"09","Operación Cancelada, el status del proveedor está en blanco o no es válido."},
			 {"10","Operación Cancelada, no se puede agregar el registro."},
			 {"11","Operación Cancelada, no se puede sustituir el registro."},
			 {"12","Operación Cancelada, no se puede eliminar el registro."},
			 {"13","Operación cancelada, el número de RIF está en blanco."},
			 {"14","Operación Cancelada, el código de la compañía está en cero o no es válido."},
			 {"15","Operación Cancelada, el número de rif no puede contener guión (-)"}};
	String idPartner = "";
	String operacion = "";
	
	
	@Override
	protected String doIt() throws Exception {
		String respuesta = "";
		String sisOri = "COMPIERE";
		for (int i = sisOri.length()+1; i <= 10; i++){
			sisOri = sisOri + " ";
		}
		pmgBx011 objRunBx011 = new pmgBx011();
		String valorRetorna = "";
		NumberFormat formatoTres = new DecimalFormat("#000");
		NumberFormat formatoSiete = new DecimalFormat("#0000000");
		
		String sql_Partner = queryPartner(Integer.parseInt(idPartner));
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql_Partner, null); 
			rs = pstmt.executeQuery(); 
			while(rs.next()){
				
				String nomPro = rs.getString("NOMPRO");
				if (nomPro.length() > 70)
					nomPro = nomPro.substring(0, 70);
				else{
					for (int i = nomPro.length()+1; i <= 70; i++){
						nomPro = nomPro + " ";
					}
				}
				
				String nroRif = rs.getString("NRORIF");
				for (int i = nroRif.length()+1; i <= 12; i++){
					nroRif = nroRif + " ";
				}
				
				String dirPro = rs.getString("DIRPRO");
				if (dirPro.length() > 100)
					dirPro = dirPro.substring(0, 100);
				else{
					for (int i = dirPro.length()+1; i <= 100; i++){
						dirPro = dirPro + " ";
					}
				}
				
				String tlfPro = rs.getString("TELPRO");
				if (tlfPro == null){
					String tlfPro2 = rs.getString("TELPRO2");
					if (tlfPro2 == null){
						tlfPro = "0";
						for (int i = tlfPro.length()+1; i <= 30; i++){
							tlfPro = tlfPro + " ";
						}
					}else
						for (int i = tlfPro2.length()+1; i <= 30; i++){
							tlfPro = tlfPro2 + " ";
						}
				}else if (tlfPro.length() > 30){
					tlfPro = tlfPro.substring(0, 30);				
				}else{
					for (int i = tlfPro.length()+1; i <= 30; i++){
						tlfPro = tlfPro + " ";
					}
				}

				String faxPro = rs.getString("FAXPRO");
				if (faxPro == null){
					faxPro = " ";
					for (int i = faxPro.length()+1; i <= 12; i++){
						faxPro = faxPro + " ";
					}
				}else{
					for (int i = faxPro.length()+1; i <= 12; i++){
						faxPro = faxPro + " ";
					}
				}
				
				Vector<String> usuarios = new Vector<String>(4);
				usuarios = queryPersonContact(rs.getInt("C_BPartner_ID"));
				String person1 = usuarios.get(0);
				if (person1.length() > 30)
					person1 = person1.substring(0, 30);
				else{
					for (int i = person1.length()+1; i <= 30; i++){
						person1 = person1 + " ";
					}
				}
				String person2 = usuarios.get(1);
				if (person2.length() > 30)
					person2 = person2.substring(0, 30);
				else{
					for (int i = person2.length()+1; i <= 30; i++){
						person2 = person2 + " ";
					}
				}
				
				String tipPer = rs.getString("TIPPER");
				for (int i = tipPer.length()+1; i <= 1; i++){
					tipPer = tipPer + " ";
				}
				
				String estPro = rs.getString("ESTPRO");
				if (estPro.equals("BLO"))
					estPro = "I";
				else
					estPro = "A";
				
				String opeReg = rs.getString("IsActive");
				if (opeReg.equals("Y"))
					opeReg = operacion;
				else
					opeReg = "E";
				
				String ctaCon = "";
				for (int i = ctaCon.length()+1; i <= 14; i++){
					ctaCon = ctaCon + " ";
				}
				String estReg = "";
				for (int i = estReg.length()+1; i <= 2; i++){
					estReg = estReg + " ";
				}
				
				String email = rs.getString("EMAIL");
				if (email.length() > 50)
					email = email.substring(0, 50);
				else{
					for (int i = email.length()+1; i <= 50; i++){
						email = email + " ";
					}
				}
				
				String[] parametros = new String[]{ 
						formatoTres.format(rs.getInt("CODCIA")),
						formatoSiete.format(rs.getInt("NROPRO")),
						nomPro,
						nroRif,
						dirPro, 
						"05",
						tlfPro,
						faxPro,
						person1,
						person2,
						tipPer,
						estPro,
						ctaCon, 
						opeReg,
						sisOri,
						email,
						estReg};
				
				/****Se invoca el método que se conecta con la interfaz Bx011 al AS400, con sus respectivos parámetros****/
				valorRetorna = objRunBx011.runI5Program(new BigDecimal(parametros[0]), 
						new BigDecimal(parametros[1]),
						parametros[2],
						parametros[3],
						parametros[4],
						parametros[5],
						parametros[6],
						parametros[7],
						parametros[8],
						parametros[9],
						parametros[10],
						parametros[11],
						parametros[12],
						parametros[13],
						parametros[14],
						parametros[15],
						parametros[16]);
				
				if (valorRetorna.equalsIgnoreCase(ESTREG[0][0])){
					if (rs.getString("syn").equals("N")){
						String sql = "update C_BPartner " +
						 		"set XX_SynchronizationBank = 'Y' " +
						 		"where C_BPartner_ID = " + rs.getInt("C_BPartner_ID");
						DB.executeUpdate(null, sql);
					}
				}
				
				for (int i = 0; i < ESTREG.length; i++){
					if (ESTREG[i][0].equals(valorRetorna)){
						respuesta = ESTREG[i][1];
						System.out.println(respuesta);
						break;
					}
				}
			}
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql_Partner);
			respuesta = "ERROR en la importación de la interfaz.";
			System.out.println(respuesta);
		}finally{
			rs.close();
			pstmt.close();
		}
		return respuesta;
	}

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("Operacion"))
				operacion = element.getInfo();
			else if (name.equals("C_BPartner_ID"))
				idPartner = element.getInfo();
		}
	}
	
	private void updateVendor(int idPartner, String sychronization){
		
	}
	
	/**
	 * Se encarga de buscar los socios de negocios que sean proveedores, que sean válidos, 
	 * y no hayan sido sincronizados con el sistema de banco
	 * @return sql
	 */
	private String queryPartner(int idPartner){
		String sql = "select par.value NROPRO, par.name NOMPRO, par.XX_CI_RIF NRORIF, pal.name DIRPRO, " +
					 "pal.Phone TELPRO, pal.Fax FAXPRO, par.XX_TypePerson TIPPER, pas.value ESTPRO, " +
					 "cli.XX_Value CODCIA, par.IsActive, par.C_BPartner_ID, pal.Phone2 TELPRO2, " +		
					 "par.XX_VENDOREMAIL EMAIL, " +
					 "par.XX_SynchronizationBank syn " +
					 "from C_BPartner par, C_BPartner_Location pal, C_BP_Status pas, AD_Client cli " +
					 "where par.C_BPartner_ID = pal.C_BPartner_ID " +
					 "and par.C_BP_Status_ID = pas.C_BP_Status_ID " +
					 "and par.AD_Client_ID = cli.AD_Client_ID " +
					 "and par.Isvendor = 'Y' " +
					 "and par.XX_IsValid = 'Y' " +
					 "and par.C_BPartner_ID = " + idPartner;
		return sql;
	}
	
	/**
	 * Se encarga de buscar las personas contactos de los proveedores
	 * @param idPartner id del Socio de Negocio
	 * @return usuarios un vector, que contiene los nombre de los contactos
	 */
	private Vector<String> queryPersonContact(int idPartner){
		String sql = "select name from AD_User where C_BPartner_ID = " + idPartner;
		Vector<String> usuarios = new Vector<String>(4);
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery(); 
			while(rs.next()){
				usuarios.add(rs.getString("name"));
			}
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
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
		return usuarios;	
	}

}
