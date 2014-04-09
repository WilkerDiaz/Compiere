package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.MRole;
import org.compiere.model.X_C_BP_BankAccount;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MBPBankAccount extends org.compiere.model.MBPBankAccount{
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPBankAccount.class);

	public MBPBankAccount(Ctx ctx, int C_Order_ID, Trx trx) {
		super(ctx, C_Order_ID, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	//Ivan Valdes
	public MBPBankAccount(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MBPBankAccount
	
    /** Set XX_IsPrimary.
    @param XX_IsPrimary */
    public void setXX_IsPrimary (Boolean XX_IsPrimary)
    {
        set_Value ("XX_IsPrimary", XX_IsPrimary);
        
    }
    
    /** Get XX_IsPrimary.
    @return XX_IsPrimary */
    public boolean getXX_IsPrimary() 
    {
    	return get_ValueAsBoolean("XX_IsPrimary");
   
    }
    
    /** Set XX_IsIntermediate.
    @param XX_IsIntermediate */
    public void setXX_IsIntermediate (Boolean XX_IsIntermediate)
    {
        set_Value ("XX_IsIntermediate", XX_IsIntermediate);
        
    }
    
    /** Get XX_IsIntermediate.
    @return XX_IsIntermediate */
    public boolean getXX_IsIntermediate() 
    {
    	return get_ValueAsBoolean("XX_IsIntermediate");
   
    }
   
    
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

			}
			rs.close();
			pstmt.close();
			return aux;
		}
		catch (SQLException e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return aux;
		}
	}//ConsultarTablasBPartner

	/**
	 * 
	 */
	protected boolean beforeDelete() {
		X_C_BP_BankAccount vendorBank = new X_C_BP_BankAccount(getCtx(),get_ID(), null);		
		Integer Bank = ConsultarTablasBPartner(vendorBank.getC_BPartner_ID(), "C_BP_BankAccount", null);
		
		if (Bank == 1){
			String mensaje = Msg.getMsg(getCtx(), "XX_NotDeleteInactiveVendor", new String[] {" Bank Account"});
			log.saveError("Error", mensaje);
			//ADialog.info(1, new Container(), mensaje);
			return false;
		}else{
			return true;
		}
	}//beforeDelete
    
    
    /**
	 * Consulta si hay varias cuentas principales en un C_Bpartner
	 */
	private String ConsultaCuentasPrincipales(int C_BPartner_ID, int C_BP_BankAccount_ID){
		String aux = "VACIO";

		String sql = "SELECT COUNT(*) "
				   + "FROM C_BP_BANKACCOUNT "
				   + "WHERE C_BPartner_ID = " + C_BPartner_ID + " AND "
				   		  +"XX_IsPrimary = 'Y' AND " 
				   		  +"C_BP_BankAccount_ID <> " + C_BP_BankAccount_ID;

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
			return aux;
		}
	}//ConsultaCuentasPrincipales
	

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{	
		if (getXX_IsIntermediate() && getXX_IsPrimary()){
			log.saveError("Error", Msg.getMsg(getCtx(), "XX_Not_Inter_Pri"));
			return false;
		}
		
		if(super.beforeSave(newRecord)){
			String CantidadDepartamento = ConsultaCuentasPrincipales(getC_BPartner_ID(),getC_BP_BankAccount_ID());
			
			if(getXX_IsPrimary()){
				if (CantidadDepartamento.equals("VACIO")){
					return true;
				}else if (new Integer (CantidadDepartamento).intValue() <= 0 ){
					return true;
				}else{
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_PrimaryAccount"));
					return false;
				}				
			}else
				return true;
		}
		else
			return false;
	}//beforeSave
}
