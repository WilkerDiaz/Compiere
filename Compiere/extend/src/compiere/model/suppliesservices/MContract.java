package compiere.model.suppliesservices;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.compiere.util.Ctx;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.MBPartner;

public class MContract extends X_XX_Contract{
	/** Purchase of Supplies and Services
	 * Maria Vintimilla Funcion 29 
	 * Distribution By Sales (Stores)**/
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MContract.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param XX_Contract_ID id
	 *	@param trxName transaction
	 */
    public MContract(Ctx ctx, int XX_Contract_ID, Trx trxName) {
		super(ctx, XX_Contract_ID, trxName);
		// TODO Auto-generated constructor stub
	}
    
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MContract(Ctx ctx, ResultSet rs, Trx trxName)	{
		super(ctx, rs, trxName);
	}//MContract
    
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord) {

		/****Jessica Mendoza****/
		if (getC_Currency_ID() == 205){
			setXX_ContractType("Nacional");
		}else{
			setXX_ContractType("Importada");
		}
		/****Fin de código - Jessica Mendoza****/
		
		boolean save = super.beforeSave(newRecord);		
		if(save){
			
			// Verify if a Distribution type is selected (Correct: only one)
			if(getXX_ApplicablePercentage() != null && getXX_ApplicablePercentage().equals("W") && getM_Warehouse_ID()==0){
				log.saveError("Error", "Debe indicar una tienda");
				return false;
			}
			// Verify Dates
			if (getXX_DateTo().before(getXX_DateFrom())){
				log.saveError("SaveError", Msg.translate(Env.getCtx(), "XX_PayDate"));
				return false;
			}
			// Verify if a Distribution type is selected (Correct: only one)
			if(isXX_IsDistrbPercentage() && isXX_IsDistrbAmount()){
				log.saveError("Error",Msg.getMsg(Env.getCtx(),"XX_ContractDistrib"));
				return false;
			}
			else if (!isXX_IsDistrbPercentage() && !isXX_IsDistrbAmount()){
				log.saveError("Error",Msg.getMsg(Env.getCtx(),"XX_ContractDistribType"));
				return false;
			}
			// Funcion 108: Revision de contratos
			// Campo mes/anio que permite identificar la fecha de notificacion
			// de renovacion de un contrato
			// Maria Vintimilla (07/06/2012)
			else if(getXX_RenewalNotificationDate() != null){
				int days = 30;
				Date aux;
				String newValue = "";
				SimpleDateFormat simpleDateformat = new SimpleDateFormat("MM/yyyy");
				Calendar newDate = Calendar.getInstance();  // Calendar aux
				newDate.setTime((Timestamp)getXX_RenewalNotificationDate());			
				newDate.add(Calendar.DAY_OF_MONTH,-days);	
				aux = newDate.getTime();
				newValue += simpleDateformat.format(aux);
				setXX_ContractNotifDate(newValue);
				return true;
			}
			else {
				return true;
			}
			
			
		}//Fin Save
		return save;
	}//Fin beforeSave
}// Fin MContract
