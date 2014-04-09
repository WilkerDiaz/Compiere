package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MBPartnerLocation extends org.compiere.model.MBPartnerLocation {
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPartnerLocation.class);
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BPartner_ID id
	 *	@param trxName transaction
	 */
	public MBPartnerLocation(Ctx ctx, int C_BPartner_ID, Trx trx) 
	{
		super(ctx, C_BPartner_ID, trx);
	}//MBPartnerLocation

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MBPartnerLocation(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MBPartnerLocation
	
	public MBPartnerLocation(MBPartner bp) {
		super(bp);
	}
	
	/**
	 * Chequea que sea numeros un String
	 */
	private boolean isNumeric(String cadena)
	{	try {
			if (cadena == null){
				return true;
			}
			Long.parseLong(cadena);
			return true;	
		} catch (NumberFormatException nfe){
			System.out.println(nfe);
			return false;	
		}
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord){	
		if (super.beforeSave(newRecord)){
			
			if(getPhone2() == null)
				setPhone2("");

			if(getFax() == null)
				setFax("");
			
			if (getPhone2().equals("") && getFax().equals("")){
				if(!isNumeric(getPhone())){
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_PhoneOnlyNumber"));
					return false;
				}				
			}else if (getPhone2().equals("") && !getFax().equals("")) {
				if(!isNumeric(getPhone()) || !isNumeric(getFax())){
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_PhoneOnlyNumber"));
					return false;
				}
			}else if (!getPhone2().equals("") && getFax().equals("")) {
				if(!isNumeric(getPhone()) || !isNumeric(getPhone2())){
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_PhoneOnlyNumber"));
					return false;
				}
			}else if(!getPhone2().equals("") && !getFax().equals("")){
				if(!isNumeric(getPhone()) || !isNumeric(getPhone2()) || !isNumeric(getFax())){
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_PhoneOnlyNumber"));
					return false;
				}
			}
			
			if (newRecord) {
				String sql = "select count(C_BPartner_Location_ID) "
					   + "from C_BPartner_Location "
					   + "where C_BPartner_ID = "
					   + getC_BPartner_ID()
					   + " and IsActive = 'Y'";

				try{
					//System.out.println(sql);
					PreparedStatement pstmt = DB.prepareStatement(sql, get_Trx());
					ResultSet rs = pstmt.executeQuery();
	
					String aux = "VACIO";
					if(rs.next()){
						aux = rs.getString("count(C_BPartner_Location_ID)");

					}
					rs.close();
					pstmt.close();
					if (aux.equals("VACIO") || aux.equals("0")){
						return true;					
					}
					else{
						log.saveError("Error", Msg.getMsg(getCtx(), "XX_NotSeveralAddress" ));
						return false;
					}
				}
				catch (SQLException e){
					log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					return false;
				}				
			}else {
				return true;
			}
		}else
			return false;
	}//beforeSave

}//MBPartner
