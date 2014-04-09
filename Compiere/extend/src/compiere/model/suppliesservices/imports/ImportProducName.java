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
package compiere.model.suppliesservices.imports;


import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import compiere.model.cds.X_I_XX_VCN_ProductName;
import compiere.model.cds.X_XX_ProductName;


/**
 * Import Category from X_I_XX_ProductName
 * 
 * @author Proyecto Bienes y Servicios - Carmen Capote
 */
public class ImportProducName extends SvrProcess {

	/** Client to be imported to */
	private int s_AD_Client_ID = Env.getCtx().getAD_Client_ID();

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

		// **** Prepare **** //

		// Delete Old Imported
		if (s_deleteOldImported) {
			sql = new StringBuffer("DELETE FROM I_XX_VCN_ProductName "
					+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}

		// Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer("UPDATE I_XX_VCN_ProductName" +
				" "
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
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.fine("Reset=" + no);
		
	/*	sql = new StringBuffer("UPDATE I_XX_ProductName i "
				+ "SET XX_ProductName_ID=(SELECT XX_ProductName_ID FROM XX_ProductName "
				+ "WHERE i.XX_ProductName=Value AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.XX_ProductName_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N'")
				.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set ProductName ID=" + no);
*/
		String ts = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
				
		/**
		 * 
		 * */
		//	Buyer Value al Buyer ID
		/*sql = new StringBuffer ("UPDATE I_XX_ProductName i " 
			+ " SET XX_CodJefCat_ID=(SELECT C_BPartner_ID FROM C_BPartner bp "
			+ " WHERE i.XX_CodJefCat = bp.XX_AS400USER and bp.IsActive = 'Y') "
			+ " WHERE i.XX_CodJefCat_ID IS NULL AND i.XX_CodJefCat IS NOT NULL "
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Assistant ID=" + no);   */
		
		
		commit();
		// -----------------------------------------------------------------------------------
		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_VCN_ProductName "
				+ " WHERE I_IsImported<>'Y'").append(clientCheck);

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(),
					get_TrxName());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				X_I_XX_VCN_ProductName impIprod = new X_I_XX_VCN_ProductName(getCtx(), rs,
						get_TrxName());
				log.fine("X_I_XX_VCN_ProductName_ID=" + impIprod.getI_XX_VCN_PRODUCTNAME_ID());

				X_XX_ProductName impXprod = null;

				// Asking if M table ID is the same I table ID
				impXprod = new X_XX_ProductName(getCtx(), 0, get_TrxName());
				/*if (impIbrand.getI_XX_VMR_BRAND_ID() == 0) {
					impXbrand = new X_XX_VMR_Brand(getCtx(), 0, get_TrxName());
					
				} else {
					impXbrand = new X_XX_VMR_Brand(getCtx(), impIbrand
							.getI_XX_VMR_BRAND_ID(), get_TrxName());

				}*/
				impXprod.setName(impIprod.getXX_ProductName());
				impXprod.setDescription(impIprod.getDescription());
				impXprod.setXX_ProductName(impIprod.getXX_ProductName());
				//impXprod.setValue(impIprod.getValue());
				
				
				if (impXprod.save()) {
					log.finest("Insert/Update Brand - "
							+ impXprod.getXX_ProductName_ID());
					noInsert++;

					impIprod.setI_IsImported("Y");
					impIprod.setProcessed(true);
					impIprod.setProcessing(false);
					impIprod.save();
				} else {
					rollback();
					noInsert--;
					sql = new StringBuffer("UPDATE I_XX_VCN_ProductName i "
							+ "SET I_IsImported='E', I_ErrorMsg=" + ts + "|| '")
							.append("Cannot Insert Nombre Producto...").append(
									"' WHERE I_XX_VCN_ProductName_ID=").append(
											impIprod.getI_XX_VCN_PRODUCTNAME_ID());
					DB.executeUpdate(get_TrxName(), sql.toString());
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
		sql = new StringBuffer("UPDATE I_XX_VCN_ProductName "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert),
				"@XX_VCN_ProductName_ID@: @Inserted@");
		return "";
	}

}
