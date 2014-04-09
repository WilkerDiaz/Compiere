package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.MRefList;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVCNTypesBankAccount extends X_XX_VCN_TypesBankAccount {
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param XX_VCN_TypesBankAccount_ID id
	 *	@param trxName transaction
	 */
	public MVCNTypesBankAccount(Ctx ctx, int XX_VCN_TypesBankAccount_ID,	Trx trx) 
	{
		super(ctx, XX_VCN_TypesBankAccount_ID, trx);

	}//MVCNTypesBankAccount

	/** Get Is Active.
    @return valor de is active */
    public String getisActive()
    {
    	return get_ValueAsString("IsActive");
    }
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MVCNTypesBankAccount(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MVCNTypesBankAccount
	
	/**
	 * 	Consulta la lista C_Bank Account Type, devuelve true si esta en la lista, y false en caso contrario
	 */
	private boolean ConsultaListaC_BankAccountType(String Name, String Value){
		String aux = "VACIO";
		
		String sql = "SELECT COUNT(*) "
				   + "FROM AD_REF_LIST "
				   + "WHERE AD_REFERENCE_ID = "
				        + "(SELECT AD_REFERENCE_ID FROM AD_REFERENCE WHERE VALIDATIONTYPE = 'L' AND "
																		+" NAME = 'C_Bank Account Type' )" 
				   + " AND NAME = '" + Name + "'"
				   + " AND VALUE ='" + Value + "'";
		
		try{
			//System.out.println(sql);
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getString("COUNT(*)");

			}
			rs.close();
			pstmt.close();
			if(aux.equals("VACIO")){
				return false;				
			}
			else if(aux.equals("1")){
					return true;				
				 }else
					 return false;
		}
		catch (SQLException e){
			// TODO: handle exception
				return false;
		}
	}//ConsultaListaC_BankAccountType

	/**
	 * 	Consulta la tabla XX_VCN_TypesBankAccount, devuelve el Nombre del Type Bank Account Actual
	 */
	private String ConsultaTablaTypesBankAccountName(){
		String aux = "VACIO";
		
		String sql = "SELECT NAME "
				   + "FROM XX_VCN_TypesBankAccount "
				   + "WHERE XX_VCN_TypesBankAccount_ID = " + getXX_VCN_TYPESBANKACCOUNT_ID();
		
		try{
			//System.out.println(sql);
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getString("NAME");

			}
			rs.close();
			pstmt.close();
			return aux;
		}
		catch (SQLException e){
			// TODO: handle exception
				return "ERROR";
		}
	}//ConsultaTablaTypesBankAccountName

	/**
	 * 	Consulta la tabla XX_VCN_TypesBankAccount, devuelve el Codigo del Type Bank Account Actual
	 */
	private String ConsultaTablaTypesBankAccountCode(){
		String aux = "VACIO";
		
		String sql = "SELECT XX_CodeTypesBankAccount "
				   + "FROM XX_VCN_TypesBankAccount "
				   + "WHERE XX_VCN_TypesBankAccount_ID = " + getXX_VCN_TYPESBANKACCOUNT_ID();
		
		try{
			//System.out.println(sql);
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getString("XX_CodeTypesBankAccount");

			}
			rs.close();
			pstmt.close();
			return aux;
		}
		catch (SQLException e){
			// TODO: handle exception
				return "ERROR";
		}
	}//ConsultaTablaTypesBankAccountCode

	/**
	 * 	Consulta la tabla XX_VCN_TypesBankAccount, devuelve si esta Activo el Type Bank Account Actual
	 */
	private String ConsultaTablaTypesBankAccountActive(){
		String aux = "VACIO";
		
		String sql = "SELECT ISACTIVE "
				   + "FROM XX_VCN_TypesBankAccount "
				   + "WHERE XX_VCN_TypesBankAccount_ID = " + getXX_VCN_TYPESBANKACCOUNT_ID();
		
		try{
			//System.out.println(sql);
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getString("ISACTIVE");

			}
			rs.close();
			pstmt.close();
			return aux;
		}
		catch (SQLException e){
			// TODO: handle exception
				return "ERROR";
		}
	}//ConsultaTablaTypesBankAccountActive

	/**
	 * 	Consultar proveedores con esta forma de pago
	 */
	private String ConsultaProveedoresConTypesBankAccount(String TypesBankAccount){
		String aux = "VACIO";

		String sql = "SELECT COUNT(*) "
				   + "FROM C_BP_BankAccount "
				   + "WHERE BankAccountType = '" + TypesBankAccount + "'";

		try{
			//System.out.println(sql);
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getString("COUNT(*)");

			}
			rs.close();
			pstmt.close();
			return aux;
		}
		catch (SQLException e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return "ERROR";
		}
	}//ConsultaProveedoresConTypesBankAccount

	/**
	 * 	Realiza el Insert en AD_REF_LIST del XX_VCN_TypesBankAccount
	 */
	private boolean InsertAD_REF_LIST(){
		String AuxADRefence = new String();
		String sql = "SELECT AD_REFERENCE_ID FROM AD_REFERENCE WHERE VALIDATIONTYPE = 'L' AND "
			   													  +" NAME = 'C_Bank Account Type'";

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			AuxADRefence = rs.getString("AD_REFERENCE_ID");
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			// TODO: handle exception
			return false;
		}
		
		MRefList AuxMRefList = new MRefList(getCtx(), 0, get_Trx());
		
		
		AuxMRefList.setName(getName());
		AuxMRefList.setAD_Client_ID(0);
		AuxMRefList.setAD_Org_ID(0);
		AuxMRefList.setDescription(getDescription());
		AuxMRefList.setEntityType("VSCH");
		AuxMRefList.setValue(getXX_CodeTypesBankAccount());
		AuxMRefList.setAD_Reference_ID(new Integer(AuxADRefence).intValue() );
		
		if (AuxMRefList.save()){
			return true;			
		}
		else{
			return false;
		}			
	}//InsertAD_REF_LIST
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		String auxActivoViejo = ConsultaTablaTypesBankAccountActive();
		String auxActivoNuevo = getisActive();
		int Activar = -1;
		String numProveedores = ConsultaProveedoresConTypesBankAccount(ConsultaTablaTypesBankAccountCode());
		
		
		if (newRecord){
			/*
			 * aqui inserta en AD_REF_LIST los valores que estan en la tabla
			 */
			if ( !ConsultaListaC_BankAccountType(getName(),getXX_CodeTypesBankAccount()) ){
				return InsertAD_REF_LIST();
			}//if de ConsultarLista C_Bank Account Type
		}else{
			
			/*
			 * aqui se valida que no se pueda desactivar si esta asociado a algun C_BPartner  
			 */
			if(auxActivoViejo.equals("Y") && auxActivoNuevo.equals("N")){
				Activar = 1;
			}else if(auxActivoViejo.equals("N") && auxActivoNuevo.equals("Y")){
				Activar = 2;
			}else if(auxActivoViejo.equals(auxActivoNuevo)){
				Activar = 3;
			}
			
			if ( (!numProveedores.equals("0")) && (Activar == 1) ){ //no puede desactivar xq tiene asociados C_BPartner
				log.saveError("Error", Msg.getMsg(getCtx(), "XX_NotDesactive"));
				return false;
			} else if ( numProveedores.equals("0") && (Activar == 1)){ //puede desactivar xq no tiene asociados C_BPartner				
	 			 String sql = "update AD_REF_LIST set "
								   + "ISACTIVE = 'N' where "
								   + "AD_REFERENCE_ID = (SELECT AD_REFERENCE_ID FROM AD_REFERENCE WHERE VALIDATIONTYPE = 'L' AND NAME = 'C_Bank Account Type') and " 
								   + "NAME = '" + ConsultaTablaTypesBankAccountName() + "' and "
								   + "Value = '" + ConsultaTablaTypesBankAccountCode() + "'";

	 			try{
					//System.out.println(sql);
					PreparedStatement pstmt = DB.prepareStatement(sql, null);
					pstmt.executeQuery();
					return true;
				}//try
				catch (SQLException e){
					// TODO: handle exception
					log.saveError("ErrorSQL", Msg.getMsg(getCtx(), e.getMessage()));
					return false;
				}//catch
			} else if (Activar == 2){
				String sql = "update AD_REF_LIST set "
					   + "ISACTIVE = 'Y' where "
					   + "AD_REFERENCE_ID = (SELECT AD_REFERENCE_ID FROM AD_REFERENCE WHERE VALIDATIONTYPE = 'L' AND NAME = 'C_Bank Account Type') and " 
					   + "NAME = '" + ConsultaTablaTypesBankAccountName() + "' and "
					   + "Value = '" + ConsultaTablaTypesBankAccountName() + "'";

				try{
					//System.out.println(sql);
					PreparedStatement pstmt = DB.prepareStatement(sql, null);
					pstmt.executeQuery();
					return true;
				}//try
				catch (SQLException e){
					// TODO: handle exception
					log.saveError("ErrorSQL", Msg.getMsg(getCtx(), e.getMessage()));
					return false;
				}//catch
			}else if (Activar == 3){
				/*
				 * Aqui actualiza en AD_REF_LIST la lista con los valores que estan en la tabla
				 */
				if ( ConsultaListaC_BankAccountType(ConsultaTablaTypesBankAccountName(),ConsultaTablaTypesBankAccountCode()) ){
					String auxDescripcion = new String();
					if (getDescription() == null)
						auxDescripcion = "null ,";
					else
						auxDescripcion = "'" + getDescription() + "',";
					
		 			 String sql = "update AD_REF_LIST set "
									   + "DESCRIPTION = " + auxDescripcion
									   + "NAME = '" + getName() + "',"
									   + "UPDATED = " + "TO_DATE('" + getUpdated().toString().substring(0, 19) + "','YYYY-MM-DD HH24:MI:SS'),"
									   + "UPDATEDBY = '" + getUpdatedBy() + "',"
									   + "VALUE = '" + getXX_CodeTypesBankAccount() + "' where "
									   + "AD_REFERENCE_ID = (SELECT AD_REFERENCE_ID FROM AD_REFERENCE WHERE VALIDATIONTYPE = 'L' AND NAME = 'C_Bank Account Type') and " 
									   + "NAME = '" + ConsultaTablaTypesBankAccountName() + "' and "
									   + "Value = '" + ConsultaTablaTypesBankAccountCode() + "'";

		 			try{
						//System.out.println(sql);
						PreparedStatement pstmt = DB.prepareStatement(sql, null);
						pstmt.executeQuery();
						return true;
					}//try
					catch (SQLException e){
						// TODO: handle exception
						log.saveError("ErrorSQL", Msg.getMsg(getCtx(), e.getMessage()));
						return false;
					}//catch
				}else {
					/*
					 * aqui inserta en AD_REF_LIST los valores que estan en la tabla
					 */
					return InsertAD_REF_LIST();
				}//else 
				
			}//else if de activar ==3
			
			
		}
		
		return true;
	}//	beforeSave

	/**
	 * 	Before Delete
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeDelete()
	{	
		log.saveError("Error", Msg.getMsg(getCtx(), "XX_NotDelete"));
		return false;
	}//	beforeDelete
	
	/**
	 * 	After Delete
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean afterDelete(boolean newRecord)
	{	
		if ( !ConsultaListaC_BankAccountType(getName(),getXX_CodeTypesBankAccount()) ){
									
			String sql = "Delete from AD_REF_LIST "
				   			  + "where NAME = '" + getName() + "' and "
				   			  		+ "value = '" + getXX_CodeTypesBankAccount() + "'";
			try{
				//System.out.println(sql);
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.executeQuery();
				
				return true;
			}
			catch (SQLException e){
				// TODO: handle exception
				return false;
			}
		}
		return true;
	}//	afterDelete
	
}//M_XX_VCN_PaymentRule
