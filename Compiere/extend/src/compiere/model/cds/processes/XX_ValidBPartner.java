/**
 * 
 */
package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.model.MRole;
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
public class XX_ValidBPartner extends SvrProcess {

	/**
	 * 	Consultar BPartner
	 */
	private Integer ConsultarTablasBPartner(int C_BPartner_ID, String Tabla, String Contacto){
		Integer aux = null;
		String sql = "SELECT COUNT(*) "
				   + "FROM "+ Tabla +" "
				   + "WHERE C_BPARTNER_ID = " + C_BPartner_ID + " ";
		
		if (Contacto != null){
			sql = sql + " and XX_ContactType = "+ Contacto;
		}
		
		sql = MRole.getDefault().addAccessSQL(
				sql, "", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		//System.out.println(sql);
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getInt("COUNT(*)");
				rs.close();
				pstmt.close();
			}
			return aux;
		}
		catch (SQLException e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return aux;
		}
	}//ConsultarTablasBPartner

	
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {
		/* Purchase of Supplies and Services Funcion 001  
		 * Agregado por Maria Vintimilla */
		Integer ContCategories = 0;
		Boolean assets = false;
		Boolean services = false;
		Boolean merchandise = false;
		
		MBPartner Vendor = new MBPartner(Env.getCtx(), getRecord_ID(), null);
		assets = Vendor.getXX_Assets();
		services = Vendor.getXX_Services();
		merchandise = Vendor.getXX_Merchandise();
		
		if (assets || services || merchandise) {
			ContCategories++;
		}
		Integer Location = ConsultarTablasBPartner(Vendor.get_ID(), "C_BPartner_Location", null);
		Integer PaisesDistrib = ConsultarTablasBPartner(Vendor.get_ID(), "XX_VCN_VendorCountryDistri", null);
		/* Purchase of Supplies and Services 
		  Maria Vintimilla de acuerdo a Funcion 001 
		  Proveedores de Bienes y Servicios no tienen asociados departamentos */ 
		Integer DeparAsociado = 0;
		if(merchandise){
			DeparAsociado = ConsultarTablasBPartner(Vendor.get_ID(), "XX_VMR_VendorDepartAssoci", null);
		}
		Integer BankAccount = ConsultarTablasBPartner(Vendor.get_ID(), "C_BP_BankAccount", null);
		Integer ContactosSales = ConsultarTablasBPartner(Vendor.get_ID(), " AD_User", "10000025");
		Integer ContactosAdmin = ConsultarTablasBPartner(Vendor.get_ID(), " AD_User", "10000024");
		String mensaje = null;
		
		if ( Location.intValue() <= 0){
			mensaje = Msg.getMsg(getCtx(), "XX_RecordNeed", new String[] {"Location"});
		} 
		else if ( PaisesDistrib.intValue() <= 0){
			mensaje = Msg.getMsg(getCtx(), "XX_RecordNeed", new String[] {"Distribution Country's"});			
		} 
		  /* Purchase of Supplies and Services 
		  Maria Vintimilla de acuerdo a Funcion 001 
		  Proveedores de Bienes y Servicios no tienen asociados departamentos */ 
		else if ( DeparAsociado.intValue() <= 0 && merchandise){
			mensaje = Msg.getMsg(Env.getCtx(), "XX_RecordNeed", new String[] {"Associate Department's"});
		} 
		else if ( BankAccount.intValue() <= 0){
			mensaje = Msg.getMsg(getCtx(), "XX_RecordNeed", new String[] {"Bank Account"});									
		} 
		else if(BankAccount.intValue() > 0 && !getBankAccount(getRecord_ID())){
			mensaje = Msg.getMsg(getCtx(), "XX_RecordNeed", new String[] {"Principal Bank Acoount"});	
		}
		else if ( ContactosSales.intValue() <= 0){
			mensaje = Msg.getMsg(getCtx(), "XX_RecordNeed", new String[] {"Administrative Contact's"});									
		} 
		else if ( ContactosAdmin.intValue() <= 0){
			mensaje = Msg.getMsg(getCtx(), "XX_RecordNeed", new String[] {"Sales Contact's "});									
		} 
		/* Purchase of Supplies and Services Funcion 001  
		 * Agregado por Maria Vintimilla */
		else if (ContCategories == 0){
			mensaje = Msg.getMsg(Env.getCtx(), "XX_RecordNeed", new String[] {"Vendor Category's "});
		} 
		else {
			Vendor.setXX_IsValid(true);	
			/****Jessica Mendoza****/
			//Actualizar el estado del proveedor
			Vendor.setC_BP_Status_ID(statusVendor());
			/****Fin código - Jessica Mendoza****/
			if(Vendor.save()){
				mensaje = Msg.getMsg(getCtx(), "XX_ValidVendor");						
				ADialog.info(1, new Container(), mensaje);
				return mensaje;				
			}else{
				mensaje = Msg.getMsg(getCtx(), "XX_CheckBPartnerData");						
				ADialog.info(1, new Container(), mensaje);
				return mensaje;					
			}
		}
		
		ADialog.error(1, new Container(), mensaje);
		return mensaje;
	}


	/**
	 * getBankAccount
	 *  Funcion 109: Revision Socio de Negocios
	 * El socio de negocio debe tener al menos una cuenta de banco principal
	 * @author Maria Vintimilla
	 * @since 11-06-2012
	 * @param C_BPartner_ID Identificador del socio de negocio
	 * @param ppalAccount Determina si la cuenta de banco es o no principal
	 */
	private boolean getBankAccount(int C_BPartner_ID){
		boolean ppalAccount = false;
		String accountType = "";
		
		String sql = " SELECT XX_IsPrimary ISPRIM "
				   + " FROM C_BP_BANKACCOUNT "
				   + " WHERE C_BPARTNER_ID = " + C_BPartner_ID;
		
		sql = MRole.getDefault().addAccessSQL(
				sql, "", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		//System.out.println(sql);
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
	
			while(rs.next()){
				accountType = rs.getString("ISPRIM");
				if(accountType.equalsIgnoreCase("Y")){
					ppalAccount = true;
					return ppalAccount;
				}
			} // while
		}
		catch (SQLException e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		return ppalAccount;
	}//Fin getBankAccount

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
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}
	
	/**
	 * Busca el id del estado activo para actualizarlo a los proveedores
	 * @author Jessica Mendoza
	 * @return id
	 */
	public int statusVendor(){
		int id = 0;
		String sql = "select C_BP_Status_ID from C_BP_Status where value = 'ACT' ";
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery(); 
			while(rs.next()){
				id = rs.getInt(1);
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
		return id;
	}

}
