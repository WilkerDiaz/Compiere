package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_I_XX_VLO_CostsPercent;
import compiere.model.cds.X_XX_VLO_CostsPercent;
import compiere.model.cds.X_XX_VLO_DateCostPercent;

public class ImportCostsPercent extends SvrProcess {

	/** Client to be imported to */
	private int s_AD_Client_ID = 0;

	/** Delete old Imported */
	private boolean s_deleteOldImported = false;
	
	@Override
	protected String doIt() throws Exception {
		
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;
		
		// **** Prepare **** //

		// Delete Old Imported
		/*if (s_deleteOldImported) {
			sql = new StringBuffer("DELETE FROM I_XX_VLO_COSTSPERCENT "
					+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			log.fine("Delete Old Imported =" + no);
		}
		
		// Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer("UPDATE I_XX_VLO_COSTSPERCENT "
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
	
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Reset=" + no);	
		
		//BPartner		
		sql = new StringBuffer("UPDATE I_XX_VLO_COSTSPERCENT i "
				+ "SET i.C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner "
				+ "WHERE VALUE = i.XX_VENDORCODE AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.C_BPartner_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N' AND i.XX_VENDORCODE IS NOT NULL");
	
		
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set BPartner ID=" + no);*/
	
		String ts = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		/*
		
		// Country
		sql = new StringBuffer("UPDATE I_XX_VLO_COSTSPERCENT i "
				+ "SET i.C_Country_ID=(SELECT C_Country_ID FROM C_Country "
				+ "WHERE i.XX_COUNTRYCODE = COUNTRYCODE) "
				+ "WHERE i.C_Country_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N' AND i.XX_COUNTRYCODE IS NOT NULL");
	
	
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Country ID=" + no);
	
		String ts1 = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		
		
		// Port
		sql = new StringBuffer("UPDATE I_XX_VLO_COSTSPERCENT i "
				+ "SET i.XX_VLO_ArrivalPort_ID = (SELECT XX_VLO_ArrivalPort_ID FROM XX_VLO_ArrivalPort "
				+ "WHERE VALUE = TO_NUMBER(i.XX_PORTCODE) AND AD_CLIENT_ID=i.AD_CLIENT_ID AND ROWNUM = 1) "
				+ "WHERE i.XX_VLO_ArrivalPort_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N' AND i.XX_PORTCODE IS NOT NULL");
	
		
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set ArrivalPort ID=" + no);
	
		String ts2 = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		
		commit();*/
		
		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_VLO_COSTSPERCENT "
				+ " WHERE I_IsImported='N'");
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(),get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				X_I_XX_VLO_CostsPercent iCostPercent = new X_I_XX_VLO_CostsPercent( getCtx(), rs, get_TrxName());
				log.fine("I_XX_VLO_CostsPercent=" + iCostPercent.getI_XX_VLO_CostsPercent_ID());
				
				X_XX_VLO_CostsPercent xCostPercent = null;
				
				X_XX_VLO_DateCostPercent dateCostPercent = new X_XX_VLO_DateCostPercent(getCtx(), 0, get_TrxName()); 
				
				if(iCostPercent.getXX_VLO_CostsPercent_ID() == 0)
				{
					xCostPercent = new X_XX_VLO_CostsPercent(getCtx(), 0, get_TrxName());
				}
				else
				{
					xCostPercent = new X_XX_VLO_CostsPercent(getCtx(), iCostPercent.getXX_VLO_CostsPercent_ID(), get_TrxName());
				}
				
				String SQL1 = ("select XX_VLO_DateCostPercent_ID from XX_VLO_DateCostPercent " +
						"where XX_DATEFROM = '"+ iCostPercent.getXX_DateFrom()+"' and XX_DATEUNTIL = '"+ iCostPercent.getXX_DateUntil()+"' and ISACTIVE = 'Y' ");
			
				 
				PreparedStatement pstmt1 = DB.prepareStatement(SQL1,get_TrxName());
				ResultSet rs1 = pstmt1.executeQuery();
				
				if(!rs1.next())
				{
					dateCostPercent.setXX_DateFrom(iCostPercent.getXX_DateFrom());
					dateCostPercent.setXX_DateUntil(iCostPercent.getXX_DateUntil());
					dateCostPercent.save();
				}
				rs1.close();
				pstmt1.close();
				
				xCostPercent.setXX_DATEH(iCostPercent.getXX_DateFrom());
				xCostPercent.setXX_YEARH(iCostPercent.getXX_DateUntil());
				xCostPercent.setC_BPartner_ID(iCostPercent.getC_BPartner_ID());
				xCostPercent.setC_Country_ID(iCostPercent.getC_Country_ID());
				xCostPercent.setXX_VLO_ArrivalPort_ID(iCostPercent.getXX_VLO_ArrivalPort_ID());
				xCostPercent.setXX_ESTIMATEDPERTUSAGENT(iCostPercent.getXX_CustomAgent());
				xCostPercent.setXX_INTERFREESTIMATEPERT(iCostPercent.getXX_InternationalFreight());
				xCostPercent.setXX_NACFREESTIMATEPERT(iCostPercent.getXX_NationalFreight());
				
				if(xCostPercent.save())
				{
					log.finest("Insert/Update SalesPurchase - "
						+ xCostPercent.getXX_VLO_CostsPercent_ID());
					
					iCostPercent.setXX_VLO_CostsPercent_ID(xCostPercent.getXX_VLO_CostsPercent_ID());
					
					noInsert++;
					
					iCostPercent.setI_IsImported(true);
					iCostPercent.setProcessed(true);
					iCostPercent.setProcessing(false);
					iCostPercent.save();
				}
				else
				{
					rollback();
					noInsert--;
					sql = new StringBuffer("UPDATE I_XX_VLO_COSTSPERCENT i "
							+ "SET I_IsImported='E', I_ErrorMsg=" + ts + "|| '")
							.append("Cannot Insert COSTSPERCENT...").append(
									"' WHERE I_XX_VLO_COSTSPERCENT=").append(
											iCostPercent.getI_XX_VLO_CostsPercent_ID());
					DB.executeUpdate(get_Trx(), sql.toString());
				}
				
				commit();
			}
			
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			rollback();
			log.log(Level.SEVERE, e.getMessage());
		}
		
		sql = new StringBuffer("UPDATE I_XX_VLO_COSTSPERCENT "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert), "@XX_VLO_COSTPERCENT_ID@: @Inserted@");
		
		return "";
	}

	@Override
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
	}

}
