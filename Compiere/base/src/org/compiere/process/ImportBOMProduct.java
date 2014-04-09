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

import java.math.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * Import BOMs from I_BOMProduct
 * 
 * @author ramyav
 *
 */
public class ImportBOMProduct extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		m_DateValue = null;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				m_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		StringBuffer sql = null;
		StringBuffer sql2 = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID= ? ";

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_BOMProduct "
					+ "WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
			log.info("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IaActive, Created/Updated, ProductType
		sql = new StringBuffer ("UPDATE I_BOMProduct "
				+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ?),"
						+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
						+ " IsActive = COALESCE (IsActive, 'Y'),"
						+ " Created = COALESCE (Created, SysDate),"
						+ " CreatedBy = COALESCE (CreatedBy, 0),"
						+ " Updated = COALESCE (Updated, SysDate),"
						+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
						+ " I_ErrorMsg = NULL,"
						+ " I_IsImported = 'N' "
						+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.info("Reset=" + no);

		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly

		// Product 
		sql = new StringBuffer ("UPDATE I_BOMProduct i "
				+ "SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p"
				+ " WHERE i.ProductValue=p.Value AND i.AD_Client_ID=p.AD_Client_ID) "
				+ "WHERE M_Product_ID IS NULL AND ProductValue IS NOT NULL"
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		log.fine("Set Product from ProductKey=" + no);	

		sql = new StringBuffer ("UPDATE I_BOMProduct "
				+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Fill Mandatory - Product, ' "
				+ "WHERE M_Product_ID IS NULL AND ProductValue IS NULL"
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		if (no != 0)
			log.warning ("Fill Mandatory - Product=" + no);	

		// Product Key		
		sql = new StringBuffer ("UPDATE I_BOMProduct i "
				+ "SET ProductValue = (SELECT Value FROM M_Product WHERE M_Product_ID=i.M_Product_ID)"
				+ "WHERE M_Product_ID IS NOT NULL "
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		log.fine("Update ProductKey from Product=" + no);

		sql = new StringBuffer ("UPDATE I_BOMProduct "
				+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Enter a Valid ProductKey (or) Fill Mandatory - Product, ' "
				+ "WHERE M_Product_ID IS NULL AND (ProductValue IS NOT NULL)"
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Product=" + no);

		// Organization	
		sql = new StringBuffer ("UPDATE I_BOMProduct i "
				+ "SET AD_Org_ID = (SELECT AD_Org_ID FROM M_Product WHERE M_Product_ID=i.M_Product_ID)"
				+ "WHERE M_Product_ID IS NOT NULL "
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		log.fine("Update Organization with product's organization=" + no);

		// BOM Name
		sql = new StringBuffer ("UPDATE I_BOMProduct i " 
				+ "SET I_IsImported='E', I_ErrorMsg="+ts+"||'ERR=Fill Mandatory - BOMName, ' " 
				+ "WHERE Name IS NULL"
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		if (no != 0)
			log.warning ("Fill Mandatory - BOM Name=" + no);

		// Component 
		sql = new StringBuffer ("UPDATE I_BOMProduct i "
				+ "SET M_ProductBOM_ID=(SELECT MAX(M_Product_ID) FROM M_Product p"
				+ " WHERE i.ProductName=p.Value AND i.AD_Client_ID=p.AD_Client_ID) "
				+ "WHERE M_ProductBOM_ID IS NULL AND ProductName IS NOT NULL"
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		log.fine("Set Component from Component Key=" + no);	

		sql = new StringBuffer ("UPDATE I_BOMProduct "
				+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Fill Mandatory - Component, ' "
				+ "WHERE M_ProductBOM_ID IS NULL AND ProductName IS NULL"
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		if (no != 0)
			log.warning ("Fill Mandatory - Component=" + no);	

		// Component Key		
		sql = new StringBuffer ("UPDATE I_BOMProduct i "
				+ "SET ProductName = (SELECT Value FROM M_Product WHERE M_Product_ID=i.M_ProductBOM_ID)"
				+ "WHERE M_ProductBOM_ID IS NOT NULL "
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		log.fine("Update ComponentKey from Component=" + no);

		sql = new StringBuffer ("UPDATE I_BOMProduct "
				+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Enter a Valid ComponentKey (or) Fill Mandatory - Component, ' "
				+ "WHERE M_ProductBOM_ID IS NULL AND (ProductName IS NOT NULL)"
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Component=" + no);	

		// Component BOM
		sql = new StringBuffer ("UPDATE I_BOMProduct i "
				+ "SET M_ProductBOMVersion_ID=(SELECT M_BOM_ID FROM M_BOM b"
				+ " WHERE i.Name2=b.Name AND i.M_ProductBOM_ID=b.M_Product_ID) "
				+ "WHERE M_ProductBOMVersion_ID IS NULL AND Name2 IS NOT NULL AND M_ProductBOM_ID IS NOT NULL"
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		log.fine("Set Component BOM from Component BOM Name=" + no);	

		// Component BOM Name		
		sql = new StringBuffer ("UPDATE I_BOMProduct i "
				+ "SET Name2 = (SELECT Name FROM M_BOM WHERE M_BOM_ID=i.M_ProductBOMVersion_ID AND M_Product_ID=i.M_ProductBOM_ID)"
				+ "WHERE M_ProductBOMVersion_ID IS NOT NULL AND M_ProductBOM_ID IS NOT NULL"
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		log.fine("Update ComponentBOMName from ComponentBOM=" + no);

		//Line No
		sql = new StringBuffer ("UPDATE I_BOMProduct "
				+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR= Fill Mandatory - Line No, ' "
				+ "WHERE Line = 0 OR Line IS NULL"
				+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Line No=" + no);	

		commit();

		//	-- New BOM -----------------------------------------------------

		int[] list;		
		list = new int[4]; 		//	list[0] = noInsert;		//	list[1] = noInsertLine;		//	list[2] = noUpdate;		//	list[3] = noUpdateLine;
		//select all records which have ComponentBOM_ID and Component Name populated or which  have Component BOM ID and Name not populated.
		//these are records that can directly be imported.
		sql = new StringBuffer ("SELECT * FROM I_BOMProduct "
				+ "WHERE I_IsImported='N' AND ((M_ProductBOMVersion_ID IS NOT NULL AND Name2 IS NOT NULL) OR " +
		"(M_ProductBOMVersion_ID IS NULL AND Name2 IS NULL))").append (clientCheck)
		.append(" ORDER BY M_ProductBOMVersion_ID DESC, M_Product_ID ");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//	Go through BOM Records w/o
		try
		{
			//BOM
			boolean BOMExists = false;
			//BOM Component
			boolean BOMLineExists = false;						

			MBOM bom = null;			
			MBOMProduct component = null;	

			pstmt = DB.prepareStatement (sql.toString(), get_Trx());
			pstmt.setInt(1, m_AD_Client_ID);
			rs = pstmt.executeQuery ();

			while (rs.next ())
			{
				X_I_BOMProduct imp = new X_I_BOMProduct (getCtx (), rs, get_Trx());	
				// if (imp.getName2()== null)
				if((imp.getM_ProductBOMVersion_ID()== 0 && imp.getName2()== null) ||(imp.getM_ProductBOMVersion_ID()!= 0 && imp.getName2()!= null))
				{
					if(!InsertBOM(imp, sql, bom, component, BOMExists, BOMLineExists, list))
						continue;
				}	
			}

			//Select from the remaining records.
			sql2 = new StringBuffer ("SELECT I_BOMProduct_ID FROM I_BOMProduct "
					+ "WHERE I_IsImported='N' AND ((M_ProductBOMVersion_ID IS NOT NULL AND Name2 IS NULL) OR " +
			"(M_ProductBOMVersion_ID IS NULL AND Name2 IS NOT NULL))").append (clientCheck)
			.append(" ORDER BY M_Product_ID ");
			PreparedStatement pstmt2 = DB.prepareStatement (sql2.toString(), get_Trx());
			pstmt2.setInt(1, m_AD_Client_ID);
			ResultSet rs2 = pstmt2.executeQuery ();
			ArrayList<Integer> ImportIDs = new ArrayList<Integer>();
			while (rs2.next())
			{
				ImportIDs.add(rs2.getInt(1));
			}
			rs2.close();
			pstmt2.close();

			for(Integer ImportID : ImportIDs)
			{
				ArrayList<X_I_BOMProduct> impBOM = new 	ArrayList<X_I_BOMProduct>();
				X_I_BOMProduct imp = new X_I_BOMProduct (getCtx (), ImportID, get_Trx());
				//log.warning(imp.getI_IsImported());
				if (imp.getI_IsImported().equals("Y") || imp.getI_ErrorMsg()!=null) //if it is imported or if it is Set as Error continue.
					continue;				
				impBOM.add(imp);
				int size = 0;				
				//Verify the Component BOM, It either verifies the BOM Completely or setes the error messages.
				if(!VerifyComponentBOM(impBOM, imp, size)) //, sql2, bom, component, BOMExists, BOMLineExists, list))
					continue;
				else
				{
					size  = impBOM.size();
					if(size > 1)
					{
						//Reversing the List, so that the Record without component BOM gets imported first and then its parent and so on.
						ArrayList<X_I_BOMProduct> reverseimp = new 	ArrayList<X_I_BOMProduct>();
						for (int i=0; i<size; i++)
							reverseimp.add(impBOM.get(size - (i+1)));
						for (X_I_BOMProduct impbom: reverseimp)
						{
							if(!InsertBOM(impbom, sql, bom, component, BOMExists, BOMLineExists, list))
								continue;							
						}
					}
					else
						if(!InsertBOM(imp, sql, bom, component, BOMExists, BOMLineExists, list))
							continue;
				}
			}			
		}

		catch (Exception e)
		{
			log.log(Level.SEVERE, "BOM - " + sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//		Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE I_BOMProduct "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString(),m_AD_Client_ID);
		addLog (0, null, new BigDecimal (no), "@Errors@");
		//
		addLog (0, null, new BigDecimal (list[0]), "@M_BOM_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (list[1]), "@M_BOMProduct_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (list[2]), "@M_BOM_ID@: @Updated@");
		addLog (0, null, new BigDecimal (list[3]), "@M_BOMProduct_ID@: @Updated@");
		return "#" + list[0] + "/" + list[1] + "/" + list[2] + "/" + list[3] + "/" + "Please Run VerifyBOM Process to use the Imported BOMs";
	}

	/**
	 * Verify the Component BOM
	 * @param impBOM ArrayList of IDs
	 * @param imp current Record
	 * @param counter counter	 
	 * @return true if Verified.
	 */
	public boolean VerifyComponentBOM(ArrayList<X_I_BOMProduct>  impBOM, X_I_BOMProduct imp, int counter) //StringBuffer sql,MBOM bom, MBOMProduct component, 
			//boolean BOMExists, boolean BOMLineExists, int[] list)
	{
		ArrayList<Integer> impIDs = getrecords();
		int recordexists = 0;
		// Check if there exists a similar BOM Name as that of the Component BOM(of the 1st record)
		// in the records that are to be imported
		MProduct prod = new MProduct(getCtx(), imp.getM_ProductBOM_ID(), get_TrxName());
		if(prod.isBOM())
		{
			MBOM[] boms = MBOM.getOfProduct(getCtx(), prod.getM_Product_ID(), get_TrxName(), null);
			for ( MBOM bomProd: boms)
			{
				if(bomProd.getName().equals(imp.getName2()))
				{
					imp.setM_ProductBOMVersion_ID(bomProd.getM_BOM_ID());
					recordexists++;					
					return true;
				}
			}				
		}
		for(Integer impID : impIDs)
		{					
			X_I_BOMProduct impnew = new X_I_BOMProduct (getCtx (), impID, get_Trx());	
			if(impBOM.get(counter).getName2().equals(impnew.getName()))						
			{
				recordexists++;
				if(impnew.getName2()!= null || impnew.getM_ProductBOMVersion_ID()!= 0)
				{
					impBOM.add(impnew);
					counter++;
					if(!VerifyComponentBOM(impBOM, impnew, counter))// sql ,bom, component, BOMExists, BOMLineExists, list))
						return false;	
				}				
			}
		}
		// If there are no records with the same BOM Name then throw an error
		if(recordexists == 0)
		{
			String msg = "Invalid Component BOM Name";
			ValueNamePair pp = CLogger.retrieveError();
			if (pp != null)
				msg += " - " + pp.toStringX();
			for(X_I_BOMProduct impLoop : impBOM)
			{
				impLoop.setI_ErrorMsg(msg);
				impLoop.save();
			}
			
			//set the error message for all records in the impBOM loop.
		
			return false;
		}
		return true;
	}

	/**
	 * Gets the un imported records.
	 * @return ArrayList<Integer> of un imported records.
	 */
	public ArrayList<Integer> getrecords ()
	{
		ArrayList<Integer> list = new ArrayList<Integer> ();
		StringBuffer sql = new StringBuffer("SELECT I_BOMProduct_ID FROM I_BOMProduct WHERE I_IsImported='N'");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			rs = pstmt.executeQuery();
			while (rs.next())
			{				
				list.add(rs.getInt(1));
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//		
		return list;
	}	//	get records which have Component BOM Name's defined and Component BOM undefined. 

	/**
	 * @param imp Import BOM Record
	 * @param sql Sql
	 * @param bom BOM
	 * @param component  Component
	 * @param BOMExists true if Exists
	 * @param BOMLineExists true if exists
	 * @param list Global List.
	 * @return
	 */
	private boolean InsertBOM(X_I_BOMProduct imp, StringBuffer sql,MBOM bom, MBOMProduct component, 
			boolean BOMExists, boolean BOMLineExists, int[] list)
	{
		//      ----------- Insert/Update BOMs -----------	
		String clientCheck = " AND AD_Client_ID= ? ";
		sql = new StringBuffer ("SELECT * FROM M_BOM "
				+ "WHERE M_Product_ID = ? AND Name = ? ").append (clientCheck)
				.append(" ORDER BY M_Product_ID, Name");
		imp.setAD_Client_ID(m_AD_Client_ID);
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		try{
			pstmt1 = DB.prepareStatement (sql.toString(), get_Trx());
			pstmt1.setInt(1, imp.getM_Product_ID());
			pstmt1.setString(2, imp.getName());
			pstmt1.setInt(3, m_AD_Client_ID);
			rs1 = pstmt1.executeQuery ();

			// If the BOM exists, the same BOM is updated 
			if (rs1.next())
			{
				BOMExists = true;
				bom = new MBOM (getCtx(), rs1, get_Trx());
			}			

			//	New BOM
			else 
			{
				BOMExists = false;
				MProduct product = new MProduct(getCtx(), imp.getM_Product_ID(), get_TrxName());
				if(!product.isBOM())
					product.setIsBOM(true);
				if(!product.save())
				{
					String msg = "Could not save Product";
					ValueNamePair pp = CLogger.retrieveError();
					if (pp != null)
						msg += " - " + pp.toStringX();
					imp.setI_ErrorMsg(msg);
					imp.save();
					return false;
				}
				bom = new MBOM (getCtx(), 0, get_Trx());	
			}

			// Checks whether the selected Product is Valid or not
			// Only products of type Item can have a BOM defined for them.
			// Product should be active

			if (!validateProduct(imp.getM_Product_ID()))
			{
				String msg = "Invalid Product - product should have its product type as Item and should be active";
				ValueNamePair pp = CLogger.retrieveError();
				if (pp != null)
					msg += " - " + pp.toStringX();
				imp.setI_ErrorMsg(msg);
				imp.save();				
				return false;
			}
			// The Product Assembly is not allowed to be as a component for the same
			// i.e., A Product cannot have the same product as a component
			if (imp.getM_ProductBOM_ID() == imp.getM_Product_ID())
			{
				String msg = "Invalid Component - Product is recursively added as a component. ";
				ValueNamePair pp = CLogger.retrieveError();
				if (pp != null)
					msg += " - " + pp.toStringX();
				imp.setI_ErrorMsg(msg);
				imp.save();				
				return false;
			}				

			// Only one Current Active per BOM Use
			if(!BOMExists)
			{
				if (imp.getBOMType().equals(X_M_BOM.BOMTYPE_CurrentActive))
				{
					MBOM[] boms = MBOM.getOfProduct(getCtx(), imp.getM_Product_ID(), get_Trx(),
							"BOMType='A' AND BOMUse='" + imp.getBOMUse() + "' AND IsActive='Y'");
					if (boms.length == 0	//	only one = this 
							|| (boms.length == 1 && boms[0].getM_BOM_ID() == imp.getM_BOM_ID()))
						;
					else
					{
						String msg = "Invalid BOM - Only one Current Active per BOM Use ";
						ValueNamePair pp = CLogger.retrieveError();
						if (pp != null)
							msg += " - " + pp.toStringX();
						imp.setI_ErrorMsg(msg);
						imp.save();				
						return false;
					}
				}
			}

			// Checks whether the selected Component is Valid or not
			// A Component selected should have its product type as Item if BOMUse is Manufacturing
			// Component should be active
			if (!validateComponent(imp.getM_ProductBOM_ID(), imp.getBOMUse()))
			{					
				String msg = "Invalid Component - Select a valid Component ";
				ValueNamePair pp = CLogger.retrieveError();
				if (pp != null)
					msg += " - " + pp.toStringX();
				imp.setI_ErrorMsg(msg);
				imp.save();				
				return false;
			}	

			// setting the fields of BOM 
			bom.setClientOrg (imp.getAD_Client_ID(), imp.getAD_Org_ID());					

			if (imp.getName()!= null)
				bom.setName(imp.getName());
			if (imp.getM_Product_ID()!= 0)
				bom.setM_Product_ID(imp.getM_Product_ID());
			if (imp.getDescription() != null)
				bom.setDescription(imp.getDescription());				
			if (imp.getHelp() != null)
				bom.setHelp(imp.getHelp());
			if (imp.getBOMUse()!= null)
				bom.setBOMUse(imp.getBOMUse());
			if (imp.getBOMType()!= null)
				bom.setBOMType(imp.getBOMType());
			bom.setProcessing(false);				

			if (!bom.save())
			{
				String msg = "Could not save BOM";
				ValueNamePair pp = CLogger.retrieveError();
				if (pp != null)
					msg += " - " + pp.toStringX();
				imp.setI_ErrorMsg(msg);
				imp.save();
				return false;
			}

			if (BOMExists == true)
				list[2]++; 	//noUpdate
			else
				list[0]++;	//noInsert

			imp.setM_BOM_ID(bom.getM_BOM_ID());	

			//  ----------- Insert/Update BOM Components -----------

			// If BOM exists then check whether the Line exists(If exists, Line is updated)
			if(BOMExists == true)
			{
				sql = new StringBuffer ("SELECT * FROM M_BOMProduct "
						+ "WHERE M_BOM_ID = ? AND Line = ? ").append (clientCheck)
						.append(" ORDER BY M_ProductBOM_ID, M_BOM_ID");	
				PreparedStatement pstmtLine = DB.prepareStatement (sql.toString(), get_Trx());
				pstmtLine.setInt(1, imp.getM_BOM_ID());
				pstmtLine.setInt(2, imp.getLine());
				pstmtLine.setInt(3, m_AD_Client_ID);
				ResultSet rsLine = pstmtLine.executeQuery ();
				// If a forecastLine already exists for a forecast, it is updated 
				if (rsLine.next ())
				{
					BOMLineExists = true;
					component = new MBOMProduct(getCtx(), rsLine, get_Trx());
				}// End of Check for Line	

				//	New BOMComponentLine(If a Line is not found, then a new Line is Inserted)
				else 
				{
					BOMLineExists = false;			
					component = new MBOMProduct (bom);	
				}
				rsLine.close();
				pstmtLine.close();
			}
			else
			{
				BOMLineExists = false;			
				component = new MBOMProduct (bom);	
			}				

			if (imp.getM_BOM_ID()!=0)
				component.setM_BOM_ID(imp.getM_BOM_ID());
			if (imp.getM_ProductBOM_ID()!= 0)
				component.setM_ProductBOM_ID(imp.getM_ProductBOM_ID());
			if (imp.getLine()!= 0 )				
				component.setLine( imp.getLine());
			if (imp.getOperationSeqNo()!= 0)
				component.setOperationSeqNo( imp.getOperationSeqNo());
			if (imp.getBOMProductType()!= null)
				component.setBOMProductType(imp.getBOMProductType());
			if (imp.getM_BOMAlternative_ID()!= 0)
				component.setM_BOMAlternative_ID(imp.getM_BOMAlternative_ID());
			if (imp.getDescriptionURL()!=null)
				component.setDescription(imp.getDescriptionURL());
			if (imp.getM_ProductBOMVersion_ID()!= 0)
				component.setM_ProductBOMVersion_ID(imp.getM_ProductBOMVersion_ID());
			if (imp.getSupplyType()!= null)
				component.setSupplyType(imp.getSupplyType());
			component.setBOMQty(imp.getBOMQty());
			component.setIsPhantom(false);

			if (!component.save())
			{
				String msg = "Could not save BOM Component";
				ValueNamePair pp = CLogger.retrieveError();
				if (pp != null)
					msg += " - " + pp.toStringX();
				imp.setI_ErrorMsg(msg);
				imp.save();
				return false;
			}

			//	Update Import
			imp.setM_BOMProduct_ID(component.getM_BOMProduct_ID());
			imp.setI_IsImported(X_I_Forecast.I_ISIMPORTED_Yes);
			imp.setProcessed(true);
			if (imp.save())
				if(BOMLineExists == true)
					list[3]++;	//noUpdateLine
				else
					list[1]++; //noInsertLine
			pstmt1.close();
			rs1.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Forecast - " + sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs1);
			DB.closeStatement(pstmt1);
		}
		return true;
	}	

	/**
	 * Validate Product	 
	 * @param ProductID product
	 * @return true if successful
	 */
	private boolean validateProduct(int ProductID) {
		String sql = "SELECT M_Product_ID FROM M_Product WHERE " +
		"M_Product.IsActive='Y'" +
		" AND M_Product.M_Product_ID = ?"+
		" AND M_Product.ProductType = 'I'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());	
			pstmt.setInt(1, ProductID);
			rs = pstmt.executeQuery ();
			int ProductExists = 0;
			while (rs.next ())
			{
				if(ProductID == rs.getInt(1))
					ProductExists++;			
			}
			if(ProductExists != 1)
			{
				log.warning("Product - Invalid, The selected product should have its product type as Item and should be active.");
				return false;
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return true;
	}   // End of ValidateProduct

	/**
	 * Validate Component
	 * @param ComponentID
	 * @param bomUse
	 * @return
	 */
	private boolean validateComponent(int ComponentID, String bomUse)
	{
		MProduct Component = new MProduct(getCtx(), ComponentID, get_TrxName());
		// If the BOM is of use Manufacturing, only products of type item can be selected as components
		if(bomUse.equals(X_M_BOM.BOMUSE_Manufacturing))
		{
			if(!Component.getProductType().equals(X_M_Product.PRODUCTTYPE_Item))
			{
				log.warning("Component - Invalid, only products of type item can be selected as components for Manufacturing BOM's");
				return false;
			}
		}
		String sql = "SELECT M_Product_ID FROM M_Product WHERE " +
		"M_Product.IsActive='Y'" +
		" AND M_Product.M_Product_ID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());	
			pstmt.setInt(1, ComponentID);
			rs = pstmt.executeQuery ();
			int ProductExists = 0;
			while (rs.next ())
			{				
				if(ComponentID == rs.getInt(1))
					ProductExists++;			
			}
			if(ProductExists != 1)
			{
				log.warning("Component - Invalid, Select an active product as a component");
				return false;
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return true;
	}   // End of ValidateComponent
}
