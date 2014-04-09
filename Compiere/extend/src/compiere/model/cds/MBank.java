package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MBank extends org.compiere.model.MBank{
	
	private static final long serialVersionUID = 1L;

	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBank.class);
	
	public MBank(Ctx ctx, int C_Order_ID, Trx trx) {
		super(ctx, C_Order_ID, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MBank(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MBank
	
    /** Get Is Active.
    @return valor de is active */
    public String getisActive()
    {
    	return get_ValueAsString("IsActive");
    }
	
	/**
	 * 	Consulta la tabla C_BANK, devuelve si esta activo el que esta actualmente en la BD
	 */
	private String ConsultaTablaC_BankActive(){
		String aux = "VACIO";
		
		String sql = "SELECT ISACTIVE "
				   + "FROM C_BANK "
				   + "WHERE C_BANK_ID = " + getC_Bank_ID();
		
		try{
			//System.out.println(sql);
			PreparedStatement pstmt = DB.prepareStatement(sql, get_Trx());
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
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return "ERROR";
		}
	}//ConsultaTablaC_BankActive
	
	/**
	 * 	Consulta si tiene algun C_BPartner asociado
	 */
	private String ConsultaProveedoresConBank(String C_Bank_ID){
		String aux = "VACIO";

		String sql = "SELECT COUNT(*) "
				   + "FROM C_BP_BankAccount "
				   + "WHERE C_Bank_ID = '" + C_Bank_ID + "'";

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
			// TODO: handle exception
				return "ERROR";
		}
	}//ConsultaProveedoresConBank	

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{	
		if(super.beforeSave(newRecord)){
			if (!newRecord){

				String CantidadCBPartner = ConsultaProveedoresConBank( ""+getC_Bank_ID() );
				String auxActivoViejo = ConsultaTablaC_BankActive();
				String auxActivoNuevo = getisActive();
	
				if(auxActivoViejo.equals(auxActivoNuevo)){
					/*System.out.println(CantidadCBPartner);
					if (CantidadCBPartner.equals("VACIO")){
						return true;
					}else if (new Integer (CantidadCBPartner).intValue() <= 0 ){
						return true;
					}else
						return false;*/
					return true;
				}else if(auxActivoViejo.equals("Y") && auxActivoNuevo.equals("N")){
					//System.out.println(CantidadCBPartner);
					if (CantidadCBPartner.equals("VACIO")){
						return true;
					}else if (new Integer (CantidadCBPartner).intValue() <= 0 ){
						return true;
					}else{
						log.saveError("Error", Msg.getMsg(getCtx(), "XX_NotDesactive"));
						return false;					
					}
				}else if(auxActivoViejo.equals("N") && auxActivoNuevo.equals("Y")){
					return true;
				} else{
					
					return false;						
				}
			
			}else
				return true;
		}else
			return false;
	}//beforeSave
	
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
}
