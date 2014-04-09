/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package compiere.model.cds.imports;

import java.math.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import compiere.model.cds.X_I_XX_Tabm14;
import compiere.model.cds.X_XX_VMR_Section;

/**
 * Import Section from X_I_XX_Tabm14
 * 
 * @author Cadena de Suministros
 */
public class ImportVMRSection extends SvrProcess {
	/** Client to be imported to */
	private int s_AD_Client_ID = 0;

	/** Delete old Imported */
	//private boolean s_deleteOldImported = false;

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

//			else if (name.equals("DeleteOldImported"))
//				s_deleteOldImported = "Y".equals(element.getParameter());

			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}// for

	} // Prepare

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
		// Delete Old Imported
		//if (s_deleteOldImported) {
		//	sql = new StringBuffer("DELETE FROM I_XX_Tabm14 "
		//			+ " WHERE I_IsImported='Y'").append(clientCheck);
		//	no = DB.executeUpdate(get_Trx(), sql.toString());
		//	log.fine("Delete Old Imported =" + no);
		//} // If DeleteOldImported

		// Set Client, Org , IsActive, Created/Updated
		/*sql = new StringBuffer("UPDATE I_XX_Tabm14 "
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
		
		//	Updating Line ID
		sql = new StringBuffer("UPDATE I_XX_Tabm14 i "
				+ "SET XX_VMR_Line_ID=(SELECT XX_VMR_Line_ID " 
				+ "FROM XX_VMR_Line l, XX_VMR_DEPARTMENT d "
				+ "WHERE i.XX_CODLIN = l.Value AND i.XX_CODDEP = d.Value "
				+ "AND l.XX_VMR_DEPARTMENT_ID = d.XX_VMR_DEPARTMENT_ID "
				+ "AND l.AD_CLIENT_ID = i.AD_CLIENT_ID AND d.AD_CLIENT_ID = i.AD_CLIENT_ID) " 
				+ "WHERE XX_VMR_Line_ID IS NULL "
				+ "AND i.AD_CLIENT_ID IS NOT NULL AND I_IsImported='N'")
				.append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Line ID=" + no);
		
		//Updating IsActive 
        sql = new StringBuffer ("UPDATE I_XX_Tabm14 i " 
                + "SET IsActive =  XX_EstSec" 
                + " WHERE XX_EstSec='N' "
                + "");
        no = DB.executeUpdate(get_Trx(), sql.toString());*/
		
		// Updating Section ID
		/*sql = new StringBuffer("UPDATE I_XX_Tabm14 i "
				+ "SET XX_VMR_SECTION_ID=(SELECT XX_VMR_SECTION_ID FROM XX_VMR_Section "
				+ "WHERE i.XX_Codsec = Value AND i.XX_VMR_Line_ID = XX_VMR_Line_ID " 
				+ "AND AD_CLIENT_ID = i.AD_CLIENT_ID) WHERE XX_CODSEC IS NOT NULL "
				+ "AND i.XX_VMR_SECTION_ID IS NULL AND XX_VMR_Line_ID IS NOT NULL " 
				+ "AND i.AD_CLIENT_ID IS NOT NULL AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Found Section=" + no);*/
		
		String ts = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')":"I_ErrorMsg"; 
				// java bug, it could not be used directly
		
		/*//	Reference Error
		sql = new StringBuffer ("UPDATE I_XX_Tabm14 SET I_IsImported='N', "
							+ " I_ErrorMsg='ERR=No se encontro referencia,||"
							+ ts + "' WHERE XX_VMR_LINE_ID IS NULL "
							+ " AND I_IsImported<>'Y' ").append(clientCheck); 
		no = DB.executeUpdate(get_Trx(), sql.toString());
		if (no != 0)
			log.warning("Invalid referencia=" + no);*/
		
		commit();

		// -----------------------------------------------------------------------------------

		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_Tabm14"
				+ " WHERE I_IsImported='N' AND I_ErrorMsg IS NULL ").append(clientCheck);

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(),
					get_TrxName());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				
				X_I_XX_Tabm14 impItab = new X_I_XX_Tabm14(getCtx(), rs, get_TrxName());
				
				log.fine("I_XX_Tabm14_ID=" + impItab.getI_XX_TABM14_ID());

				// **** Create/Update Category ****
				X_XX_VMR_Section impXtab = null;

				// Pregunto por ID de la tabla compiere que es igual al de la
				// tabla I //
				if (impItab.getXX_VMR_Section_ID() == 0) {
					
					impXtab = new X_XX_VMR_Section(getCtx(), 0, get_TrxName());
					
				} else {
					
					impXtab = new X_XX_VMR_Section(getCtx(), impItab.getXX_VMR_Section_ID(), get_TrxName());
					
				}

				// Get and Set de los campos
				impXtab.setDescription(impItab.getXX_DesSec());
				Integer value = impItab.getXX_Codsec();
				impXtab.setValue(value.toString());
				impXtab.setXX_VMR_Line_ID(impItab.getXX_VMR_Line_ID());
				impXtab.setName(impItab.getXX_DesSec());
				
				//
				if (impXtab.save()) {
					log.finest("Insert/Update Section - "
							+ impXtab.getXX_VMR_Section_ID());
					
					impItab.setXX_VMR_Section_ID(impXtab.getXX_VMR_Section_ID());
					
					noInsert++;

					impItab.setI_IsImported(true);
					impItab.setProcessed(true);
					impItab.setProcessing(false);
					impItab.save();
					
				} // If save
				else {
					rollback();
					noInsert--;
					sql = new StringBuffer("UPDATE I_XX_Tamb14  "
							+ "SET I_IsImported='E', I_ErrorMsg=" + ts + "|| '")
							.append("Cannot Insert Tamb14...").append(
									"' WHERE I_XX_Tabm14_ID=").append(
									impItab.getI_XX_TABM14_ID());
					DB.executeUpdate(get_Trx(), sql.toString());
					// continue;
				}// else safe

				commit();
				// 
			} // end while

			rs.close();
			pstmt.close();
		}// try

		catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			rollback();
		}// catch

		// Set Error to indicator to not imported
		sql = new StringBuffer("UPDATE I_XX_Tabm14 "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert),
				"@XX_VMR_Section_ID@: @Inserted@");
		return "";
	}// doIt

}// Fin