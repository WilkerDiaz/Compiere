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

import compiere.model.cds.X_I_XX_Tabm13;
import compiere.model.cds.X_XX_VMR_Line;

/**
 * Import Line from X_I_XX_Tabm13
 * 
 * @author Cadena de Suministros
 */
public class ImportVMRLine extends SvrProcess {

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
			sql = new StringBuffer("DELETE FROM I_XX_Tabm13 "
					+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}
*/
/**		// Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer("UPDATE I_XX_Tabm13 "
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
		
		//	Updating Department ID
		sql = new StringBuffer ("UPDATE I_XX_Tabm13 i " 
				+ "SET XX_VMR_DEPARTMENT_ID = (SELECT XX_VMR_DEPARTMENT_ID " 
				+ "FROM XX_VMR_DEPARTMENT d WHERE i.XX_CODDEP = d.Value " 
				+ ") WHERE XX_CODDEP IS NOT NULL "
				+ "").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Department ID=" + no);
		
		//	Updating TypeInventory ID
		sql = new StringBuffer ("UPDATE I_XX_Tabm13 i " 
				+ "SET XX_VMR_TypeInventory_ID = (SELECT XX_VMR_TypeInventory_ID " 
				+ "FROM XX_VMR_TypeInventory t WHERE i.XX_TypeInventory = t.Value " 
				+ ") WHERE XX_TypeInventory IS NOT NULL "
				+ "").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Department ID=" + no);

//		Updating IsActive 
		sql = new StringBuffer ("UPDATE I_XX_Tabm13 i " 
				+ "SET IsActive =  XX_EstLin" 
				+ " WHERE XX_EstLin='N' "
				+ "");
		no = DB.executeUpdate(get_Trx(), sql.toString());
*/		
//		Updating value para que tenga el cero
		sql = new StringBuffer ("UPDATE xx_vmr_line SET value=concat('0',value) where length(value)=1"
				+ "");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		
		//	Updating Line ID
		sql = new StringBuffer("UPDATE I_XX_Tabm13 i "
				+ "SET XX_VMR_Line_ID=(SELECT XX_VMR_Line_ID FROM XX_VMR_Line "
				+ "WHERE i.XX_CODLIN=Value AND i.XX_VMR_DEPARTMENT_ID=XX_VMR_DEPARTMENT_ID " 
				+ "AND AD_CLIENT_ID=i.AD_CLIENT_ID) WHERE XX_VMR_Line_ID IS NULL "
				+ "AND i.AD_CLIENT_ID IS NOT NULL "
				+ "AND XX_VMR_DEPARTMENT_ID IS NOT NULL ").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Line ID=" + no);

		String ts = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')": "I_ErrorMsg"; 
				// java bug, it could not be used directly
/**		
		//	Reference Error
		sql = new StringBuffer ("UPDATE I_XX_Tabm13 SET I_IsImported='E', "
							+ " I_ErrorMsg='ERR=No se encontro referencia,'||"
							+ ts + " WHERE XX_VMR_DEPARTMENT_ID IS NULL "
							+ " AND I_IsImported<>'Y' ").append(clientCheck); 
		no = DB.executeUpdate(get_Trx(), sql.toString());
		if (no != 0)
			log.warning("Invalid referencia=" + no);
*/
		commit();
		// -----------------------------------------------------------------------------------
/**		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_Tabm13 "
				+ " WHERE I_IsImported='N'").append(clientCheck);

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(),
					get_TrxName());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				X_I_XX_Tabm13 impIcat = new X_I_XX_Tabm13(getCtx(), rs, get_TrxName());
				log.fine("I_XX_Tabm13_ID=" + impIcat.getI_XX_TABM13_ID());

				// *** Create/Update Solm01 ****
				X_XX_VMR_Line impXcat = null;

				// Pregunto por ID de la tabla compiere que es igual al de la
				// tabla I //
				if (impIcat.getXX_VMR_Line_ID() == 0) {
					
					impXcat = new X_XX_VMR_Line(getCtx(), 0, get_TrxName());
					
				} else {
					
					impXcat = new X_XX_VMR_Line(getCtx(), impIcat
							.getXX_VMR_Line_ID(), get_TrxName());
					
				}

				// get and set
				impXcat.setDescription(impIcat.getXX_DesLin());
				Integer value = impIcat.getXX_CodLin();
				impXcat.setValue(value.toString());
				impXcat.setName(impIcat.getXX_DesLin());
				impXcat.setXX_VMR_Department_ID(impIcat.getXX_VMR_Department_ID());
				impXcat.setXX_VMR_TypeInventory_ID(impIcat.getXX_VMR_TypeInventory_ID());
				impXcat.setIsActive(impIcat.isActive());
				
				if (impXcat.save()) {
					log.finest("Insert/Update Tabm13 - "
							+ impXcat.getXX_VMR_Line_ID());
					noInsert++;

					impIcat.setI_IsImported(true);
					impIcat.setProcessed(true);
					impIcat.setProcessing(false);
					impIcat.save();
				} else {
					rollback();
					noInsert--;
					sql = new StringBuffer("UPDATE I_XX_Tabm13 i "
							+ "SET I_IsImported='E', I_ErrorMsg=" + ts + " || '")
							.append("Cannot Insert Tabm13...").append(
									"' WHERE I_XX_Tabm13_ID=").append(
									impIcat.getI_XX_TABM13_ID());
					DB.executeUpdate(get_Trx(), sql.toString());
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
		sql = new StringBuffer("UPDATE I_XX_Tabm13 "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert), "@XX_VMR_Line_ID@: @Inserted@");
*/		return "";
		
	}

}
