package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_I_XX_VMR_BRAND;
import compiere.model.cds.X_XX_VMR_Brand;

/**
 * Import Brand from Excel File
 * 
 * @author Patricia Ayuso
 */
public class ImportVMRBrand extends SvrProcess {

	/** Client to be imported to */
	private int p_AD_Client_ID = 0;
	/** Delete old Imported */
	//private boolean p_deleteOldImported = false;

	/**
	 * Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = ((BigDecimal) element.getParameter())
						.intValue();
			/*
			 * else if (name.equals("DeleteOldImported")) 
			 * p_deleteOldImported = "Y".equals(element.getParameter());
			 */
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	/**
	 * Perform process.
	 * 
	 * @return Message
	 * @throws Exception
	 */
	@Override
	protected String doIt() throws Exception {

		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + p_AD_Client_ID;

		// **** Prepare ****

		// Delete Old Imported
		/*
		 * if (p_deleteOldImported) { sql = new StringBuffer
		 * ("DELETE FROM I_BPartner " +
		 * " WHERE I_IsImported='Y'").append(clientCheck); no =
		 * DB.executeUpdate(get_Trx(), sql.toString());
		 * log.fine("Delete Old Impored =" + no); }
		 */

/**		// Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer("UPDATE I_XX_VMR_Brand "
				+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(
				p_AD_Client_ID).append(
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
		
		
*/
		
		//	Updating Line ID
		sql = new StringBuffer("UPDATE I_XX_VMR_BRAND i "
				+ "SET I_XX_VMR_BRAND_ID=(SELECT XX_VMR_BRAND_ID FROM XX_VMR_BRAND "
				+ "WHERE i.value=Value AND i.name=name " 
				+ "AND AD_CLIENT_ID=i.AD_CLIENT_ID) ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Line ID=" + no);

		
		String ts = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly

		commit();
		// -----------------------------------------------------------------------------------

/**		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_VMR_Brand "
				+ " WHERE I_IsImported<>'Y'").append(clientCheck);

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(),
					get_TrxName());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				X_I_XX_VMR_BRAND impIbrand = new X_I_XX_VMR_BRAND(getCtx(), rs,
						get_TrxName());
				log.fine("X_I_XX_VMR_BRAND_ID=" + impIbrand.getI_XX_VMR_BRAND_ID());

				X_XX_VMR_Brand impXbrand = null;

				// Asking if M table ID is the same I table ID
				impXbrand = new X_XX_VMR_Brand(getCtx(), 0, get_TrxName());
				/*if (impIbrand.getI_XX_VMR_BRAND_ID() == 0) {
					impXbrand = new X_XX_VMR_Brand(getCtx(), 0, get_TrxName());
					
				} else {
					impXbrand = new X_XX_VMR_Brand(getCtx(), impIbrand
							.getI_XX_VMR_BRAND_ID(), get_TrxName());

				}*/
/**				impXbrand.setName(impIbrand.getName());
//				impXbrand.setXX_BrandOwn(impIbrand.isXX_ONWBRAND_VALUE());
				impXbrand.setValue(impIbrand.getValue());
//				impXbrand.setXX_OLDVALUE(impIbrand.getXX_OLDVALUE());
				
				if (impXbrand.save()) {
					log.finest("Insert/Update Brand - "
							+ impXbrand.getXX_VMR_Brand_ID());
					noInsert++;

					impIbrand.setI_IsImported("Y");
					impIbrand.setProcessed(true);
					impIbrand.setProcessing(false);
					impIbrand.save();
				} else {
					rollback();
					noInsert--;
					sql = new StringBuffer("UPDATE I_XX_VMR_Brand i "
							+ "SET I_IsImported='E', I_ErrorMsg=" + ts + "|| '")
							.append("Cannot Insert Brand...").append(
									"' WHERE I_XX_VMR_Brand_ID=").append(
									impIbrand.getI_XX_VMR_BRAND_ID());
					DB.executeUpdate(get_Trx(), sql.toString());
				}

				commit();

			}// end while

			rs.close();
			pstmt.close();

		} // end try

		catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			rollback();
		}

		// Set Error to indicator to not imported
		sql = new StringBuffer("UPDATE I_XX_VMR_Brand "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert),
				"@XX_VMR_BRAND_ID@: @Inserted@");
*/		return "";
	}

}
