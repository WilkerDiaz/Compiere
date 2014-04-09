package compiere.model.cds;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.model.X_C_Conversion_Rate;
import org.compiere.model.X_C_Period;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

/**
 *	XX_VSI_KeyNameInfo Model
 *	
 *  @author ghuchet 
 */

public class MVSIKeyNameInfo extends X_XX_VSI_KeyNameInfo {

	public MVSIKeyNameInfo(Ctx ctx, int XX_VSI_KeyNameInfo_ID, Trx trx) {
		super(ctx, XX_VSI_KeyNameInfo_ID, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
		boolean save = super.afterSave(newRecord, success);

		if(save){
			if(get_ValueAsBigDecimal("XX_L_VCN_StorageCharge").compareTo((BigDecimal)get_ValueOld("XX_L_VCN_StorageCharge"))!= 0){
				String msg = "¿Está seguro que quiere actualizar el porcentaje de gasto de almacenaje?. " +
						"\nEste cambio actualizará el factor de reposición de las tasas de cambio de periodo igual o posterior al actual.";
				Boolean ask = ADialog.ask(1, new Container(), msg);
				if(ask){
					BigDecimal percentageIncreaseOld = (BigDecimal)get_ValueOld("XX_L_VCN_StorageCharge"); 
					BigDecimal percentageIncrease = get_ValueAsBigDecimal("XX_L_VCN_StorageCharge"); 
				  	Date time= Utilities.currentServerDate();
				  	Calendar date = Calendar.getInstance();
				  	date.setTime(time);
				  	date.set(Calendar.HOUR,0);
				  	date.set(Calendar.MINUTE,0);
				  	date.set(Calendar.SECOND,0);
				  	date.set(Calendar.MILLISECOND,0);
					BigDecimal multiplyRate = new BigDecimal(0); 
					BigDecimal replacementFactor_aux  = new BigDecimal(0);
					BigDecimal replacementFactor = new BigDecimal(0);
					int conversionRateID = 0; 
					
					DateFormat formatter = new SimpleDateFormat("yyyy-MM");
		          	// Calendar to Date Conversion
		          	int year = date.get(Calendar.YEAR);
		          	int month = date.get(Calendar.MONTH);
		          	int day = date.get(Calendar.DATE);
		          	Date auxDate = new Date((year-1900), month, day);
		          	String fecha=formatter.format(auxDate);
		          	
					String SQL = "Select C.XX_REPLACEMENTFACTOR, C.MULTIPLYRATE, C.C_CONVERSION_RATE_ID, C.XX_PERIOD_ID, P.STARTDATE "+
								"\nFROM C_CONVERSION_RATE C, C_PERIOD P WHERE C.XX_PERIOD_ID =  P.C_PERIOD_ID AND " +
								"\nP.ISACTIVE='Y' AND P.AD_CLIENT_ID = 1000012 AND P.STARTDATE >=  TO_DATE('"+fecha+"','YYYY-MM')";
					System.out.println(SQL);
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try 
					{	
						pstmt = DB.prepareStatement(SQL, null);
						rs = pstmt.executeQuery();
	
						int i = 0;
						while (rs.next()) {	
							multiplyRate = rs.getBigDecimal("MultiplyRate");
							replacementFactor_aux = rs.getBigDecimal("XX_ReplacementFactor");
							conversionRateID = rs.getInt("C_CONVERSION_RATE_ID");
							
							//Si se esta en medio de un período se agrega a la tabla X_XX_VCN_ReplacementFactor el factor de reposición anterior.
							Calendar start = Calendar.getInstance();
							start.setTime(rs.getDate("STARTDATE"));
							if(start.compareTo(date)<=0){
								X_XX_VCN_ReplacementFactor replacementFactorOld = new X_XX_VCN_ReplacementFactor(Env.getCtx(), 0, null);
								replacementFactorOld.setC_Conversion_Rate_ID(conversionRateID );
								replacementFactorOld.setXX_ReplacementFactor(replacementFactor_aux);
								replacementFactorOld.setMultiplyRate(multiplyRate);
								replacementFactorOld.setXX_PercentageIncrease(percentageIncreaseOld);
								replacementFactorOld.save();
								replacementFactorOld.setValidTo(replacementFactorOld.getCreated());
								replacementFactorOld.save();
							}
							// Set Replacement Factor
							X_C_Conversion_Rate convertionRate = new X_C_Conversion_Rate(Env.getCtx(),rs.getInt("C_CONVERSION_RATE_ID"), null);
							replacementFactor = multiplyRate.add((multiplyRate.multiply(percentageIncrease)));
							convertionRate.set_Value("XX_ReplacementFactor", replacementFactor);
							convertionRate.save();
							i++;
						}
	
						if (i > 0 ){
							msg = "Se actualizó el Factor de Reposición de las Tasas de Cambio de periodo igual o posterior al actual.";
							ADialog.info(1, new Container(), msg);
						}
	
					} catch (Exception e) {
						e.printStackTrace();
					} finally
					{
						DB.closeResultSet(rs);
						DB.closeStatement(pstmt);
					}
				}else {
					return false;
				}
			}
		}
		return save;

	}
}
