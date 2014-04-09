package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_I_XX_VMR_IncreaseFactor;
import compiere.model.cds.X_XX_VMR_IncreaseFactor;

public class ImportIncreaseFactor extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
	
		StringBuffer sql = null;
		int no = 0;

		
		sql = new StringBuffer("UPDATE i_XX_VMR_IncreaseFactor " +
				"SET XX_COUNTRYCODE = (CASE WHEN (SUBSTR(XX_COUNTRYCODE,2) = 'UK') THEN '2GB' " +
				"WHEN (SUBSTR(XX_COUNTRYCODE,2) = 'HO') THEN '2NL' " +
				"WHEN (SUBSTR(XX_COUNTRYCODE,2) = 'VT') THEN '2VN' " +
				"WHEN (SUBSTR(XX_COUNTRYCODE,2) = 'TA') THEN '2TW' " +
				"WHEN (SUBSTR(XX_COUNTRYCODE,2) = 'BU') THEN '2BG' " +
				"WHEN (SUBSTR(XX_COUNTRYCODE,2) = 'PO') THEN '2PT' " +
				"ELSE TO_CHAR(XX_COUNTRYCODE) END) ");
		
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Country ID=" + no);	
				
		
		// Country
	
		sql = new StringBuffer("UPDATE I_XX_VMR_INCREASEFACTOR i "
          + "SET i.C_Country_ID= " 
          + "(SELECT C_Country_ID FROM C_Country "
          + "WHERE SUBSTR(i.XX_COUNTRYCODE,2) = COUNTRYCODE) "
          + "WHERE i.C_Country_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL "
          + "AND I_IsImported='N' AND i.XX_COUNTRYCODE IS NOT NULL ");
		

		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Country ID=" + no);

		String ts1 = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
		: "I_ErrorMsg"; // java bug, it could not be used directly 
		
		// Department
		sql = new StringBuffer(
				"UPDATE I_XX_VMR_INCREASEFACTOR i "
			  + "SET i.XX_VMR_Department_ID= " +
						"(SELECT XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT "
					   + "WHERE TO_NUMBER(i.XX_DEPARTMENTCODE) = value AND ROWNUM = 1) "
			  + "WHERE i.XX_VMR_DEPARTMENT_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL "
		      + "AND I_IsImported='N' AND i.XX_DEPARTMENTCODE IS NOT NULL ");
		
		//System.out.println(sql);


		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Country ID=" + no);

		ts1 = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
		: "I_ErrorMsg"; // java bug, it could not be used directly 
		
		
		// Dispatch Route
		sql = new StringBuffer(
				"UPDATE I_XX_VMR_INCREASEFACTOR i "
			  + "SET i.XX_VLO_DispatchRoute_ID=" +
						"(SELECT XX_VLO_DispatchRoute_ID FROM XX_VLO_DispatchRoute "
					   + "WHERE VALUE = i.XX_DISPATCHROUTECODE AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
			  + "WHERE i.XX_VLO_DispatchRoute_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL "
		      + "AND I_IsImported='N' AND i.XX_DISPATCHROUTECODE IS NOT NULL");
		
		//System.out.println(sql);


		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set DispatchRoute ID=" + no);

		ts1 = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
		: "I_ErrorMsg"; // java bug, it could not be used directly 
		
		
		commit();
		
	//-------------------------------------------------------------------------------------------------------------
		
		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_VMR_INCREASEFACTOR "
				+ " WHERE I_IsImported='N'");
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(),get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				X_I_XX_VMR_IncreaseFactor increaseIfactor = new X_I_XX_VMR_IncreaseFactor(getCtx(), rs, get_TrxName());
				log.fine("I_XX_VMR_IncreaseFactor=" + increaseIfactor.getI_XX_VMR_INCREASEFACTOR_ID());
				
				X_XX_VMR_IncreaseFactor increaseXfactor = null;
				
				if(increaseIfactor.getXX_VMR_IncreaseFactor_ID() == 0)
				{
					increaseXfactor = new X_XX_VMR_IncreaseFactor(getCtx(), 0, get_TrxName()); 
				}
				else
				{
					increaseXfactor = new X_XX_VMR_IncreaseFactor(getCtx(), increaseIfactor.getXX_VMR_IncreaseFactor_ID(), get_TrxName());
				}
					 
				increaseXfactor.setXX_VMR_Department_ID(increaseIfactor.getXX_VMR_Department_ID());
				increaseXfactor.setC_Country_ID(increaseIfactor.getC_Country_ID());
				increaseXfactor.setXX_VLO_DispatchRoute_ID(increaseIfactor.getXX_VLO_DispatchRoute_ID());
				increaseXfactor.setXX_IncreaseFactor(increaseIfactor.getXX_IncreaseFactor());
			
				if(increaseXfactor.save())
				{
					log.finest("Insert/Update SalesPurchase - "
							+ increaseXfactor.getXX_VMR_IncreaseFactor_ID());
						
					//leadItimes.setXX_VLO_LEADTIMES_ID(leadXtimes.getXX_VLO_LEADTIMES_ID());
					increaseIfactor.setXX_VMR_IncreaseFactor_ID(increaseXfactor.getXX_VMR_IncreaseFactor_ID());	
						
					noInsert++;
						
					increaseIfactor.setI_IsImported(true);
					increaseIfactor.setProcessed(true);
					increaseIfactor.setProcessing(false);
					increaseIfactor.save();
				}
				else
				{
					rollback();
					noInsert--;
					sql = new StringBuffer("UPDATE I_XX_VMR_INCREASEFACTOR i "
							+ "SET I_IsImported='E', I_ErrorMsg=" + ts1 + "|| '")
							.append("Cannot Insert SALEPURCHASE...").append(
									"' WHERE I_XX_VLO_LEADTIMES=").append(
											increaseIfactor.getI_XX_VMR_INCREASEFACTOR_ID());
					DB.executeUpdate(get_Trx(), sql.toString());
				}
				
			commit();
			}// end while
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			rollback();
		}

		// Set Error to indicator to not imported

		sql = new StringBuffer("UPDATE I_XX_VMR_INCREASEFACTOR "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert), "@XX_VMR_INCREASEFACTOR_ID@: @Inserted@");
		
		return "Proceso Realizado";
	}

	@Override
	protected void prepare() {
	
	}

}
