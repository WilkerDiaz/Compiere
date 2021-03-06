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

import compiere.model.cds.X_I_XX_Tabm12;
import compiere.model.cds.X_XX_VMR_Department;

/**
 * Import Department from X_I_XX_Tabm12
 * 
 * @author Cadena de Suministros
 */
public class ImportVMRDepartment extends SvrProcess {

	/** Client to be imported to */
	private int s_AD_Client_ID = 0;

	/** Delete old Imported */
	private boolean s_deleteOldImported = false;

	/**
	 * Prepare - e.g., get Parameters.
	 */

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
		}// fin for
	}// fin prepare

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
		if (s_deleteOldImported) {
			sql = new StringBuffer("DELETE FROM I_XX_Tabm12 "
					+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			// System.out.println(sql);
			log.fine("Delete Old Imported =" + no);
		}

		// Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer("UPDATE I_XX_Tabm12 "
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
		
		//
		sql = new StringBuffer("UPDATE I_XX_Tabm12 i "
				+ "SET XX_VMR_DEPARTMENT_ID=(SELECT XX_VMR_DEPARTMENT_ID " 
				+ "FROM XX_VMR_DEPARTMENT WHERE i.XX_CODDEP = Value "
				+ "AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.XX_VMR_DEPARTMENT_ID IS NULL " 
				+ "AND i.AD_CLIENT_ID IS NOT NULL AND I_IsImported='N'")
				.append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Department ID=" + no);
		
		//	Updating Category ID
		sql = new StringBuffer ("UPDATE I_XX_Tabm12 i " 
				+ "SET XX_VMR_CATEGORY_ID = (SELECT XX_VMR_CATEGORY_ID " 
				+ "FROM XX_VMR_CATEGORY c WHERE i.XX_CODCAT = c.Value " 
				+ "AND c.IsActive = 'Y') WHERE i.XX_CODCAT IS NOT NULL "
				+ "AND I_IsImported='N' ").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Category ID=" + no);
		
		/**
		 * Agregado por Jorge E. Pires G.
		 * */
		//	Assistant Value al Assistant ID
		sql = new StringBuffer ("UPDATE I_XX_Tabm12 i " 
			+ " SET XX_IdAsis_ID=(SELECT C_BPartner_ID FROM C_BPartner bp "
			+ " WHERE i.XX_IdAsis = bp.XX_AS400USER and bp.IsActive = 'Y') "
			+ " WHERE i.XX_IdAsis_ID IS NULL AND i.XX_IdAsis IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Assistant ID=" + no);
		
		//	Buyer Value al Buyer ID
		sql = new StringBuffer ("UPDATE I_XX_Tabm12 i " 
			+ " SET XX_IdCompra_ID=(SELECT C_BPartner_ID FROM C_BPartner bp "
			+ " WHERE i.XX_IdAsis = bp.XX_AS400USER and bp.IsActive = 'Y') "
			+ " WHERE i.XX_IdCompra_ID IS NULL AND i.XX_IdCompra IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Buyer ID=" + no);		
		
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  
				//java bug, it could not be used directly
		
		//	Reference Error
		sql = new StringBuffer ("UPDATE I_XX_Tabm12 SET I_IsImported='E', "
							+ " I_ErrorMsg='ERR=No se encontro referencia,'||"
							+ ts + " WHERE XX_VMR_CATEGORY_ID IS NULL "
							+ " AND I_IsImported<>'Y' ").append(clientCheck); 
		no = DB.executeUpdate(get_Trx(), sql.toString());
		if (no != 0)
			log.warning("Invalid referencia=" + no);
		
		commit();
		// -----------------------------------------------------------------------------------
		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_Tabm12 "
				+ " WHERE I_IsImported='N'").append(clientCheck);

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				X_I_XX_Tabm12 impIdep = new X_I_XX_Tabm12(getCtx(), rs, get_TrxName());
				log.fine("I_XX_TABM12_ID=" + impIdep.getI_XX_TABM12_ID());

				// *** Create/Update Department *** //
				X_XX_VMR_Department impXdep = null;

				// Pregunto por ID de la tabla compiere que es igual al de la
				// tabla I //
				if (impIdep.getXX_VMR_Department_ID() == 0) {
					
					impXdep = new X_XX_VMR_Department(getCtx(), 0, get_TrxName());
					
				} else {
					
					impXdep = new X_XX_VMR_Department(getCtx(), impIdep
							.getXX_VMR_Department_ID(), get_TrxName());
					
				}

				// Get and Set de los campos
				impXdep.setDescription(impIdep.getXX_DesDep());
				impXdep.setXX_VMR_Category_ID(impIdep.getXX_VMR_Category_ID());				
				impXdep.setXX_UserAssistant_ID(impIdep.getXX_IdAsis_ID());
				impXdep.setXX_UserBuyer_ID(impIdep.getXX_IdCompra_ID());
				Integer value = impIdep.getXX_CodDep();
				impXdep.setValue(value.toString());
				impXdep.setName(impIdep.getXX_DesDep());
				
				if (impXdep.save()) {
					log.finest("Insert/Update Deparment - "
							+ impXdep.getXX_VMR_Department_ID());
					noInsert++;

					impIdep.setI_IsImported(true);
					impIdep.setProcessed(true);
					impIdep.setProcessing(false);
					impIdep.save();
				} // If save
				else {
					rollback();
					noInsert--;
					sql = new StringBuffer("UPDATE I_XX_Tabm12 i "
							+ "SET I_IsImported='E', I_ErrorMsg=" + ts + "|| '")
							.append("Cannot Insert Tabm12...").append(
									"' WHERE I_XX_Tabm12_ID=").append(
									impIdep.getI_XX_TABM12_ID());
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

		sql = new StringBuffer("UPDATE I_XX_Tabm12 "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert), "@XX_VMR_DEPARTMENT_ID@: @Inserted@");
		return "";
	}// doIt

}// Fin