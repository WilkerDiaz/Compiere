package compiere.model.cds.imports;

import java.math.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import compiere.model.cds.X_I_XX_Tabm13;
import compiere.model.cds.X_I_XX_VMR_ConValDptBrand;
import compiere.model.cds.X_I_XX_VendorCountryDis;
import compiere.model.cds.X_XX_VCN_VendorCountryDistri;
import compiere.model.cds.X_XX_VMR_ConceptValDptBrand;
import compiere.model.cds.X_XX_VMR_Line;

/**
 * Import Line from X_I_XX_Tabm13
 * 
 * @author Cadena de Suministros
 */
public class ImportConValDepBrand extends SvrProcess {

	/** Client to be imported to */
	private int s_AD_Client_ID = 0;

	/** Delete old Imported */
	private boolean s_deleteOldImported = false;

	/**
	 * Prepare - e.g., get Parameters.
	 */	
	@Override
	public void prepare() {
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
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;

		// **** Prepare ****
/**
		// Delete Old Imported
		if (s_deleteOldImported) {
			sql = new StringBuffer("DELETE FROM I_XX_VMR_ConValDptBrand "
					+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			log.fine("Delete Old Imported =" + no);
		}
*/
/**		// Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer("UPDATE I_XX_VMR_ConValDptBrand "
				+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(
				s_AD_Client_ID).append(
				")," + " AD_Org_ID = 0,"
				+ " IsActive = COALESCE (IsActive, 'Y'),"
				+ " Created = COALESCE (Created, SysDate),"
				+ " CreatedBy = COALESCE (CreatedBy, 0),"
				+ " Updated = COALESCE (Updated, SysDate),"
				+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
				+ " " + " I_IsImported = 'N' "
				+ " ");
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Reset=" + no);
		
		sql = new StringBuffer ("UPDATE I_XX_VMR_ConValDptBrand i " 
				+ "SET XX_VMR_DEPARTMENT_ID = (SELECT XX_VMR_DEPARTMENT_ID " 
				+ "FROM XX_VMR_DEPARTMENT d WHERE i.XX_CODDEP = d.Value " 
				+ ") WHERE XX_CODDEP IS NOT NULL "
				+ "");
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Department ID=" + no);
		System.out.println(sql);
		
		
		
		sql = new StringBuffer ("UPDATE I_XX_VMR_ConValDptBrand i " 
				+ "SET XX_VMR_Brand_ID = (SELECT XX_VMR_Brand_ID " 
				+ "FROM XX_VMR_Brand d WHERE i.MARCAPRODUCTO = d.Value " 
				+ ") WHERE MARCAPRODUCTO IS NOT NULL "
				+ "");
		
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		
		sql = new StringBuffer ("UPDATE I_XX_VMR_ConValDptBrand i " 
				+ "SET XX_VME_ConceptValue_ID = (select XX_VME_ConceptValue_ID from XX_VME_ConceptValue where name like 'BUENO')" 
				+ " WHERE XX_ValueConcept like 'B'"
				+ "");
		
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		sql = new StringBuffer ("UPDATE I_XX_VMR_ConValDptBrand i " 
				+ "SET XX_VME_ConceptValue_ID = (select XX_VME_ConceptValue_ID from XX_VME_ConceptValue where name like 'MEJOR')" 
				+ " WHERE XX_ValueConcept like 'M'"
				+ "");
		
		no = DB.executeUpdate(sql.toString(), get_TrxName());

		
		sql = new StringBuffer ("UPDATE I_XX_VMR_ConValDptBrand i " 
				+ "SET XX_VME_ConceptValue_ID = (select XX_VME_ConceptValue_ID from XX_VME_ConceptValue where name like 'OPTIMO')" 
				+ " WHERE XX_ValueConcept like 'O'"
				+ "");
		
		no = DB.executeUpdate(sql.toString(), get_TrxName());	
*/

		String ts = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')": "I_ErrorMsg"; 
				// java bug, it could not be used directly
/**		
		//	Reference Error
		sql = new StringBuffer ("UPDATE I_XX_VMR_ConValDptBrand SET I_IsImported='E', "
							+ " I_ErrorMsg='ERR=No se encontro referencia,'||"
							+ ts + " WHERE XX_VMR_DEPARTMENT_ID IS NULL "
							+ " AND I_IsImported<>'Y' ").append(clientCheck); 
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning("Invalid referencia=" + no);
*/
		commit();
		// -----------------------------------------------------------------------------------
		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_VMR_ConValDptBrand "
				+ " ");

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(),
					get_TrxName());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				
				X_I_XX_VMR_ConValDptBrand impIcat = new X_I_XX_VMR_ConValDptBrand(getCtx(), rs, get_TrxName());
				
				X_XX_VMR_ConceptValDptBrand impXcat = null;

				
				if (impIcat.getXX_VMR_ConceptValDptBrand_ID() == 0) {
					
					impXcat = new X_XX_VMR_ConceptValDptBrand(getCtx(), 0, get_TrxName());
					
				} else {
					
					impXcat = new X_XX_VMR_ConceptValDptBrand(getCtx(), impIcat
							.getXX_VMR_ConceptValDptBrand_ID(), get_TrxName());
					
				}

				// get and set
				impXcat.setAD_Org_ID(0);
				impXcat.setXX_VME_ConceptValue_ID(impIcat.getXX_VME_ConceptValue_ID());
				impXcat.setXX_VMR_Department_ID(impIcat.getXX_VMR_Department_ID());	
				impXcat.setXX_VMR_Brand_ID(impIcat.getXX_VMR_Brand_ID());	
				impXcat.setIsActive(impIcat.isActive());
				
				if (impXcat.save()) {
					log.finest("Insert/Update I_XX_VMR_ConValDptBrand - "
							+ impXcat.getXX_VMR_ConceptValDptBrand_ID());
					noInsert++;

					impIcat.setI_IsImported(true);
					impIcat.setProcessed(true);
					impIcat.setProcessing(false);
					impIcat.save();
				} else {
					rollback();
					noInsert--;
/**					sql = new StringBuffer("UPDATE I_XX_Tabm13 i "
							+ "SET I_IsImported='E', I_ErrorMsg=" + ts + " || '")
							.append("Cannot Insert Tabm13...").append(
									"' WHERE I_XX_Tabm13_ID=").append(
									impIcat.getXX_VCN_VENDORCOUNTRYDISTRI_ID());
					DB.executeUpdate(sql.toString(), get_TrxName());
					*/
					// continue;
			}
				commit();
				
			} // end while

			rs.close();
			pstmt.close();

		} catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			rollback();
		}

		// Set Error to indicator to not imported
		sql = new StringBuffer("UPDATE I_XX_VMR_ConValDptBrand "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert), "@XX_VMR_Line_ID@: @Inserted@");
		return "";
		
	}

}
