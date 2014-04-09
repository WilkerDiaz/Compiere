package compiere.model.cds;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

/**
 * @author Patricia Ayuso
 *
 */
public class MVCNApplicationNumber extends X_XX_VCN_ApplicationNumber {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MVCNApplicationNumber(Ctx ctx, int XX_VCN_ApplicationNumber_ID,
			Trx trx) {
		super(ctx, XX_VCN_ApplicationNumber_ID, trx);
	}

	public MVCNApplicationNumber(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}
	
	/**
	 * Method that set the consecutive Application number
	 *  
	 * @param DateD
	 * @param ApplNum
	 * @return Application Number ID if is generated satisfactorily
	 * 			if is not it return 0
	 */
	public int generateApplicationNumber(Timestamp DateD, int ApplNum){
		
		// Getting month and year from DateD
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(DateD.getTime());
		Integer numMonth = date.get(Calendar.MONTH);
		numMonth++;
		Integer numYear = date.get(Calendar.YEAR);
		String month = numMonth.toString();
		if(numMonth < 10)
			month = "0".concat(month);
		String year = numYear.toString();
		
		setXX_DATED(year.concat(month));		
		setXX_ApplicationNumber(ApplNum);
		
		if(save())
			return ApplNum;
		else
			return 0;
	}
	
	/**
	 * Method that set the consecutive Application number if the
	 * vendor hasn't one in the date give it. 
	 * Otherwise it generate a consecutive Application number
	 *  
	 * @param DateD
	 * @param ApplNum
	 * @param VendorID
	 * @return Application Number ID if is generated satisfactorily
	 * 			if is not it return 0
	 */
	public int generateApplicationNumber(Timestamp DateD, int OrderID, boolean flag, Trx trans){
		
		// Getting month and year from DateD
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(DateD.getTime());
		Integer numMonth = date.get(Calendar.MONTH)+1;
		Integer numYear = date.get(Calendar.YEAR);
		String month = numMonth.toString();
		if(numMonth < 10)
			month = "0".concat(month);
		String year = numYear.toString();
		String monthYear = (year.concat(month));
		int applicationNum = 0;
		Trx transaction = null;
		
		if (trans != null)
			transaction = trans; //WDIAZCAMBIO
		
		String sql = "SELECT a.XX_APPLICATIONNUMBER, a.XX_VCN_APPLICATIONNUMBER_ID, a.XX_DATED " 
			+ " FROM XX_VCN_APPLICATIONNUMBER a " 
			//+ " JOIN XX_VCN_PURCHASESBOOK p on a.XX_VCN_APPLICATIONNUMBER_ID = p.XX_VCN_APPLICATIONNUMBER_ID "
			//+ " WHERE p.C_ORDER_ID = " + OrderID
			+ " WHERE a.XX_DATED = '" + year + month 
			+ "' GROUP BY a.XX_VCN_APPLICATIONNUMBER_ID, a.XX_APPLICATIONNUMBER, a.XX_DATED";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				applicationNum = rs.getInt(1);				
				
				if(flag)
					return applicationNum;
				
				applicationNum++;
				int applID = rs.getInt(2);
				String dateD = rs.getString(3);
				
				if(dateD.compareTo(monthYear) != 0){
					ADialog.ask(EnvConstants.WINDOW_INFO, new Container(), 
						Msg.getMsg(Env.getCtx(), "Retention Difference Date"));
					return 0;
				}
				
				MVCNApplicationNumber applNumber = new MVCNApplicationNumber(getCtx(), applID, transaction);
				applNumber.setXX_ApplicationNumber(applicationNum);
				applNumber.save();
				
				/*String sql2 = "UPDATE XX_VCN_APPLICATIONNUMBER a "
					+ " SET a.XX_APPLICATIONNUMBER = " + applicationNum
					+ " where a.XX_VCN_APPLICATIONNUMBER_ID = " + applID;
					int no = DB.executeUpdate(sql2, null);
					log.fine("Update Application Number " + no);*/
				
			}else{
				applicationNum = 0000001;
				MVCNApplicationNumber applNumber = new MVCNApplicationNumber(getCtx(), 0, transaction);
				applNumber.setXX_ApplicationNumber(applicationNum);
				applNumber.setXX_DATED(monthYear);
				//applNumber.setC_Order_ID(OrderID);
				applNumber.save();
				
			}
	
		} catch (SQLException e) {
			//log.log(Level.SEVERE, sql, e);
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}

		return applicationNum;
	}

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
	//Verifying if the date is in the actual "Taxable Period"
		if(newRecord){
			Calendar auxCalendar = Calendar.getInstance();
			String dateD = getXX_DATED();
			Integer numMonth = auxCalendar.get(Calendar.MONTH);
			numMonth++;
			String month = numMonth.toString();
			if(numMonth < 10)
				month = "0".concat(month);
			String monthYear = ((Integer)auxCalendar.get(Calendar.YEAR)).toString() + month;
			
			if(dateD.compareTo(monthYear) != 0){
				ADialog.ask(EnvConstants.WINDOW_INFO, new Container(), 
					Msg.getMsg(Env.getCtx(), "Retention Difference Date"));
				return false;
			}
		}
		
		return true;
	}	//	beforeSave

}
