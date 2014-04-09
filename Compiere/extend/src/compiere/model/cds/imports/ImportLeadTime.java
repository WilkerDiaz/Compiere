package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_I_XX_VLO_LeadTimes;
import compiere.model.cds.X_XX_VLO_DateLeadTime;
import compiere.model.cds.X_XX_VLO_LeadTimes;

public class ImportLeadTime extends SvrProcess {

	/** Client to be imported to */
	private int s_AD_Client_ID = 0;

	/** Delete old Imported */
	private boolean s_deleteOldImported = false;
	
	
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null);

			else if (name.equals("AD_Client_ID"))
				s_AD_Client_ID = ((BigDecimal) element.getParameter())
						.intValue();
			else if (name.equals("DeleteOldImported"))
				s_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}	
		
	}// end prepare
	
	
	protected String doIt() throws Exception {
		
		
		String bPartner = null;
		String country = null;
		String port = null;
		String dateFrom = null;
		String dateUntil = null;
		/*Integer tei = new Integer(0);
		Integer ten = new Integer(0);
		Integer tli = new Integer(0);
		Integer tnac = new Integer(0);
		Integer trc = new Integer(0);
		Integer tt = new Integer(0);*/
		
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;
		
		
		// **** Prepare **** //

		// Delete Old Imported
/**		if (s_deleteOldImported) {
			sql = new StringBuffer("DELETE FROM I_XX_VLO_LEADTIMES "
					+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}
		
		// Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer("UPDATE I_XX_VLO_LEADTIMES "
				+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(
				s_AD_Client_ID).append(
				")," + " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
				+ " IsActive = COALESCE (IsActive, 'Y'),"
				+ " Created = COALESCE (Created, SysDate),"
				+ " CreatedBy = COALESCE (CreatedBy, 0),"
				+ " Updated = COALESCE (Updated, SysDate),"
				+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
				+ " I_ErrorMsg = NULL," + " I_IsImported = 'N' "
				+ " WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
	
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Reset=" + no);
		
		
		/*String SQL1 = ("select XX_COUNTRYNAME from I_XX_VLO_LEADTIMES ");
		
		try
		{
			PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null); 
		    ResultSet rs1 = pstmt1.executeQuery();
		    
		   while(rs1.next())
		   {
			  
			  bPartner = rs1.getString("XX_COUNTRYNAME");
			  System.out.println(bPartner);
		   }//end while
		   rs1.close();
		   pstmt1.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}*/
		
		
/**		//BPartner		
		sql = new StringBuffer("UPDATE I_XX_VLO_LEADTIMES i " +
				"SET i.C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner " +
				"WHERE value=i.XX_BPARTNERNAME) " +
				"WHERE  " +
				"I_IsImported='N' AND i.XX_BPARTNERNAME IS not NULL");
		
	
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set BPartner ID=" + no);
		
		String ts = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		
		System.out.println("importo el proveedor");
		
		// Country
		sql = new StringBuffer("UPDATE I_XX_VLO_LEADTIMES i "
				+ "SET i.C_Country_ID=(SELECT C_Country_ID FROM C_Country "
				+ "WHERE i.XX_COUNTRYNAME = value) "
				+ "WHERE i.C_Country_ID IS NULL " 
				+ "AND I_IsImported='N' AND i.XX_COUNTRYNAME IS NOT NULL");
	
	
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Country ID=" + no);
	
		String ts1 = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		System.out.println("importo el pais");
		
		// Port
		sql = new StringBuffer("UPDATE I_XX_VLO_LEADTIMES i "
				+ "SET i.XX_VLO_ArrivalPort_ID = (SELECT XX_VLO_ArrivalPort_ID FROM XX_VLO_ArrivalPort "
				+ "WHERE VALUE = i.XX_PORTNAME ) "
				+ "WHERE i.XX_VLO_ArrivalPort_ID IS NULL " 
				+ "AND I_IsImported='N' AND i.XX_PORTNAME IS NOT NULL");
	
		
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set ArrivalPort ID=" + no);
	
		String ts2 = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		System.out.println("importo el puerto");
		
		
		commit();
		*/
		
	//---------------------------------------------------------------------------------------------------------------------
		
		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_VLO_LEADTIMES "
				+ " WHERE I_IsImported='N'");

		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(),get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				X_I_XX_VLO_LeadTimes leadItimes = new X_I_XX_VLO_LeadTimes (getCtx(), rs, get_TrxName());
				log.fine("I_XX_VLO_LeadTimes=" + leadItimes.getI_XX_VLO_LEADTIMES_ID());
				
				X_XX_VLO_LeadTimes leadXtimes = null;
				
				X_XX_VLO_DateLeadTime dateLeadTime = new X_XX_VLO_DateLeadTime(getCtx(), 0, get_TrxName()); 
				
				if(leadItimes.getXX_VLO_LEADTIMES_ID() == 0)
				{
					leadXtimes = new X_XX_VLO_LeadTimes(getCtx(), 0, get_TrxName());
				}
				else
				{
					leadXtimes = new X_XX_VLO_LeadTimes(getCtx(), leadItimes.getXX_VLO_LEADTIMES_ID(), get_TrxName());
				}
				
				String SQL1 = ("select XX_VLO_DATELEADTIME_ID from XX_VLO_DATELEADTIME " +
						"where XX_DATEFROM = '"+leadItimes.getXX_DateFrom()+"' and XX_DATEUNTIL = '"+leadItimes.getXX_DateUntil()+"' and ISACTIVE = 'Y' ");
			
				 
				PreparedStatement pstmt1 = DB.prepareStatement(SQL1,get_TrxName());
				ResultSet rs1 = pstmt1.executeQuery();
				
				if(!rs1.next())
				{
					dateLeadTime.setXX_DateFrom(leadItimes.getXX_DateFrom());
					dateLeadTime.setXX_DateUntil(leadItimes.getXX_DateUntil());
					dateLeadTime.save();
				}
				rs1.close();
				pstmt1.close();
				
				leadXtimes.setXX_DATEH(leadItimes.getXX_DateFrom());
				leadXtimes.setXX_YEARH(leadItimes.getXX_DateUntil());
				leadXtimes.setC_BPartner_ID(leadItimes.getC_BPartner_ID());
				leadXtimes.setC_Country_ID(leadItimes.getC_Country_ID());
				leadXtimes.setXX_VLO_ArrivalPort_ID(leadItimes.getXX_VLO_ArrivalPort_ID());
				leadXtimes.setXX_INTERNACARRIVALTIMETEI(leadItimes.getXX_TEI());
				leadXtimes.setXX_NACARRIVALTIMETEN(leadItimes.getXX_TEN());
				leadXtimes.setXX_NATIONALIZATIONTIMETNAC(leadItimes.getXX_Tnac());
				leadXtimes.setXX_TIMEREGISTCELLATIONTRC(leadItimes.getXX_TRC());
				leadXtimes.setXX_TRANSITTIMETT(leadItimes.getXX_TT());
				leadXtimes.setXX_IMPORTSLOGISTICSTIMETLI(leadItimes.getXX_TLI());
				
				if(leadXtimes.save())
				{
					log.finest("Insert/Update SalesPurchase - "
						+ leadXtimes.getXX_VLO_LEADTIMES_ID());
					
					leadItimes.setXX_VLO_LEADTIMES_ID(leadXtimes.getXX_VLO_LEADTIMES_ID());
					
					
					noInsert++;
					
					leadItimes.setI_IsImported(true);
					leadItimes.setProcessed(true);
					leadItimes.setProcessing(false);
					leadItimes.save();
				}
				else
				{
					rollback();
					noInsert--;
					sql = new StringBuffer("UPDATE I_XX_VLO_LEADTIMES i "
							+ "SET I_IsImported='E', I_ErrorMsg=" +  "|| '")
							.append("Cannot Insert LEADTIMES...").append(
									"' WHERE I_XX_VLO_LEADTIMES=").append(
											leadItimes.getI_XX_VLO_LEADTIMES_ID());
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

		sql = new StringBuffer("UPDATE I_XX_VLO_LEADTIMES "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert), "@XX_VLO_LEADTIMES_ID@: @Inserted@");
		
		return "Proceso Realizado";
	}

	
	

}
