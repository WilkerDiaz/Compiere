package compiere.model.suppliesservices;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.MBPartner;

/** Purchase of Supplies and Services
 * Maria Vintimilla Funcion 29 
 * Contract **/
public class MPayContract extends X_XX_PayContract{
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPartner.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param XX_Contract_ID id
	 *	@param trxName transaction
	 */
    public MPayContract(Ctx ctx, int XX_PayContract_ID, Trx trxName) {
		super(ctx, XX_PayContract_ID, trxName);
	}
    
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MPayContract(Ctx ctx, ResultSet rs, Trx trxName)	{
		super(ctx, rs, trxName);
	}//MContract
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord) {
		boolean save = super.beforeSave(newRecord);
		
		X_XX_VCN_ContractInvoice contractInv = new X_XX_VCN_ContractInvoice( Env.getCtx(), get_ValueAsInt("XX_VCN_ContractInvoice_ID"), null);
		X_XX_Contract Contract = new X_XX_Contract(Env.getCtx(), contractInv.getXX_Contract_ID(), null);
		
		if(save){
			// Validaciones de fechas respecto a las fechas definidas en el contrato
			if(getXX_DateFrom() != null && getXX_DateTo() != null ){
				if (getXX_DateTo().before(getXX_DateFrom())){
					log.saveError("SaveError", Msg.translate(Env.getCtx(), "XX_PayDate"));
					return false;
				}
				if (getXX_DateFrom().before(Contract.getXX_DateFrom())){
					log.saveError("SaveError", Msg.translate(Env.getCtx(), "XX_PayDate"));
					return false;
				}
				if (getXX_DateTo().after(Contract.getXX_DateTo())){
					log.saveError("SaveError", Msg.translate(Env.getCtx(), "XX_PayDate"));
					return false;
				}
			}
			else {
				// Deben proporcionarse fechas para los pagos
				if (getXX_DateFrom() == null){
					log.saveError("SaveError", Msg.translate(Env.getCtx(), "XX_PayDateFrom"));
					return false;
				}
				if (getXX_DateTo() == null){
					log.saveError("SaveError", Msg.translate(Env.getCtx(), "XX_PayDateTo"));
					return false;
				}
			}
			
			// Debe existir al menos una forma de pago (% o por monto)
			if(getXX_ContractAmount().compareTo(new BigDecimal(0)) == 0 && 
					getXX_CPercentage().compareTo(new BigDecimal(0)) == 0) {
				log.saveError("SaveError", Msg.translate(Env.getCtx(), "XX_PayTypeDef"));
				return false;
			}
			
			// Se verifica que la recurrencia de pago no esté vacía
			if(getXX_ContractAmount().compareTo(new BigDecimal(0)) > 0  && getXX_PaymentRecurrency1() == null) {
				log.saveError("SaveError", Msg.translate(Env.getCtx(), "XX_PayRecurrency"));
				return false;
			}
			else if(getXX_ContractAmount().compareTo(new BigDecimal(0)) > 0  && getXX_PaymentRecurrency1() != null){
				if(getXX_ContractAmount().compareTo(Contract.getXX_ContractAmount()) == 1 ){
					log.saveError("Error",Msg.translate(Env.getCtx(), 
					"XX_ContractDetailPercentage"));
					return false;
				}
			}
			if(getXX_CPercentage().compareTo(new BigDecimal(0)) > 0 && getXX_PaymentRecorrency2() == null) {
				log.saveError("SaveError", Msg.translate(Env.getCtx(), "XX_PayRecurrency"));
				return false;
			}
			else if(getXX_CPercentage().compareTo(new BigDecimal(0)) > 0 && getXX_PaymentRecorrency2() != null){
				if(getXX_CPercentage().compareTo(new BigDecimal(100)) == 1 ){
					log.saveError("Error",Msg.translate(Env.getCtx(), 
					"XX_ContractDetailPercentage"));
					return false;
				}
			}	
		}// Fin Save
		
		return save;
	}// Fin beforeSave
	
	
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		
		String sql = "select  TRIM(CASE WHEN NVL(a.XX_ContractAmount, 0)=0 THEN a.XX_CPERCENTAGE || ' %' " +
				  "ELSE ' '||TO_CHAR(a.XX_ContractAmount,'999G999G999D99MI') END) " +
				  "||'  - '|| a.XX_DATEFROM ||' - '|| a.XX_DATETO A  " +
				  "FROM XX_PayContract a WHERE a.XX_Paycontract_ID = " + get_ID();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			rs = pstmt.executeQuery();

			if (rs.next()){
				if(rs.getString(1)!=null){
					String update = "UPDATE XX_PayContract SET NAME = '"+ rs.getString(1) +"' where XX_Paycontract_ID = " + get_ID();
					DB.executeUpdate(get_Trx(), update);
				}
				else
					set_ValueNoCheck("Name", "-");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return true;
	}





}// Fin MPayContract
