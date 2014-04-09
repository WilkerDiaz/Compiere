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
package org.compiere.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.api.MigrationStepInterface;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Trx;

/**
 *	Populate MStorageDetail with the information from MStorage
 *  
 *  In 3.6.1, M_Storage is replaced with M_StorageDetail to increase the 
 *  concurrency of various document processing flows.
 *  
 *  This migration step will check if M_StorageDetail has been populated. If so,
 *  it will assume that the information has been copied before and will not 
 *  attempt to copy the information again.
 *  
 *  If M_StorageDetail is empty, then the migration step will attempt to 
 *  populate M_StorageDetail with the inventory quantity information in 
 *  M_Storage. 
 *	
 *  @author rthng
 */

public class PopulateStorageDetail implements MigrationStepInterface{

	/**	Logger							*/
	protected static CLogger			log = CLogger.getCLogger (PopulateStorageDetail.class);
	
	@Override
	public String executeStep() {
		// Check if MStorageDetail has been populated
		String sql = "SELECT COUNT(*) FROM M_StorageDetail";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) > 0) {
					log.log(Level.INFO, "MStorageDetail is not empty. Assume that it has been previously populated and skipping this step");
					return null;
				}
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Unable to get MStorageDetail count" + e.toString());
			return "Unable to get MStorageDetail count";
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	
		Trx trx = Trx.get("PopulateStorageDetail");
		
		// Populate MStorageDetail with the inventory quantity information from MStorage
		String returnVal = populateStorageDetail("QtyAllocated","A",trx);
		if (returnVal==null) returnVal = populateStorageDetail ("QtyDedicated","D", trx); 	
		if (returnVal==null) returnVal = populateStorageDetail ("QtyExpected","E", trx); 	
		if (returnVal==null) returnVal = populateStorageDetail ("QtyOnhand","H", trx); 	
		if (returnVal==null) returnVal = populateStorageDetail ("QtyOrdered","O", trx); 	
		if (returnVal==null) returnVal = populateStorageDetail ("QtyReserved","R", trx); 		
		
		// Verify that the copied information is accurate
		String sql1 = "SELECT " +
				"AD_CLIENT_ID,AD_ORG_ID,DATELASTINVENTORY,M_ATTRIBUTESETINSTANCE_ID,M_LOCATOR_ID," +
				"M_PRODUCT_ID,QTYALLOCATED,QTYDEDICATED,QTYEXPECTED,QTYONHAND,QTYORDERED,QTYRESERVED " +
		      "FROM " +
		        "M_STORAGE_V " +
			  "MINUS " +
			  "SELECT " +
				"AD_CLIENT_ID,AD_ORG_ID,DATELASTINVENTORY,M_ATTRIBUTESETINSTANCE_ID,M_LOCATOR_ID," +
				"M_PRODUCT_ID,QTYALLOCATED,QTYDEDICATED,QTYEXPECTED,QTYONHAND,QTYORDERED,QTYRESERVED " +
			  "FROM " +
			    "M_STORAGE";
		String sql2 = "SELECT " +
			    "AD_CLIENT_ID,AD_ORG_ID,DATELASTINVENTORY,M_ATTRIBUTESETINSTANCE_ID,M_LOCATOR_ID," +
		        "M_PRODUCT_ID,QTYALLOCATED,QTYDEDICATED,QTYEXPECTED,QTYONHAND,QTYORDERED,QTYRESERVED " +
              "FROM " +
                "M_STORAGE " +
	          "MINUS " +
	          "SELECT " +
		        "AD_CLIENT_ID,AD_ORG_ID,DATELASTINVENTORY,M_ATTRIBUTESETINSTANCE_ID,M_LOCATOR_ID," +
		        "M_PRODUCT_ID,QTYALLOCATED,QTYDEDICATED,QTYEXPECTED,QTYONHAND,QTYORDERED,QTYRESERVED " +
	          "FROM " +
	            "M_STORAGE_V";

		try {
			pstmt = DB.prepareStatement(sql1, trx);
			rs = pstmt.executeQuery();
			if (rs.next()){
				log.log(Level.SEVERE, "There is a mismatch between the inventory information in MStorageDetail and MStorage");
				trx.rollback();
				return "There is a mismatch between the inventory information in MStorageDetail and MStorage";
			}
			pstmt = DB.prepareStatement(sql2, trx);
			rs = pstmt.executeQuery();
			if (rs.next()){
				log.log(Level.SEVERE, "There is a mismatch between the inventory information in MStorageDetail and MStorage");
				trx.rollback();
				return "There is a mismatch between the inventory information in MStorageDetail and MStorage";
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Unable to verify the inventory information in MStorageDetail" + e.toString());
			return "Unable to verify the inventory information in MStorageDetail";
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// Populated and verified the information in MStorageDetail
		trx.commit();
		return returnVal;
	}
	
	private String populateStorageDetail(String qtyColumn, String qtyType, Trx trx) {
		String sql = "INSERT INTO M_STORAGEDETAIL (" +
				"AD_CLIENT_ID,AD_ORG_ID,CREATED,CREATEDBY,DATELASTINVENTORY,ISACTIVE," +
				"M_ATTRIBUTESETINSTANCE_ID,M_LOCATOR_ID,M_PRODUCT_ID,QTY,QTYTYPE,UPDATED,UPDATEDBY) " +
			  "SELECT AD_CLIENT_ID,AD_ORG_ID,CREATED,CREATEDBY,DATELASTINVENTORY,ISACTIVE," +
				"M_ATTRIBUTESETINSTANCE_ID,M_LOCATOR_ID,M_PRODUCT_ID,"+qtyColumn+",'"+qtyType+"',UPDATED,UPDATEDBY " +
			  "FROM " +
			    "M_STORAGE";
		int count = DB.executeUpdate(trx, sql);
		if (count < 0) {
			log.log(Level.SEVERE, "Unable to copy "+qtyColumn+" from MStorage to MStorageDetail");
			trx.rollback();
			return "Unable to copy "+qtyColumn+" from MStorage to MStorageDetail";
		}
		log.log(Level.INFO, "Copied "+count+" "+qtyColumn+" from MStorage to MStorageDetail");
		return null;
	}
}
