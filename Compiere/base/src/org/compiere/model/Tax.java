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
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Tax Handling
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Tax.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class Tax
{
	/**	Logger							*/
	static private CLogger			log = CLogger.getCLogger (Tax.class);

	
	/**************************************************************************
	 *	Get Tax ID - converts parameters to call Get Tax.
	 *  <pre>
	 *		M_Product_ID/C_Charge_ID	->	C_TaxCategory_ID
	 *		billDate, shipDate			->	billDate, shipDate
	 *		AD_Org_ID					->	billFromC_Location_ID
	 *		M_Warehouse_ID				->	shipFromC_Location_ID
	 *		billC_BPartner_Location_ID  ->	billToC_Location_ID
	 *		shipC_BPartner_Location_ID 	->	shipToC_Location_ID
	 *
	 *  if IsSOTrx is false, bill and ship are reversed
	 *  </pre>
	 * 	@param ctx	context
	 * 	@param M_Product_ID product
	 * 	@param C_Charge_ID product
	 * 	@param billDate invoice date
	 * 	@param shipDate ship date
	 * 	@param AD_Org_ID org
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param billC_BPartner_Location_ID invoice location
	 * 	@param shipC_BPartner_Location_ID ship location
	 * 	@param IsSOTrx is a sales p_trx
	 * 	@return C_Tax_ID
	 *  If error it returns 0 and sets error log (TaxCriteriaNotFound)
	 */
	public static int get (Ctx ctx, int M_Product_ID, int C_Charge_ID,
		Timestamp billDate, Timestamp shipDate,
		int AD_Org_ID, int M_Warehouse_ID,
		int billC_BPartner_Location_ID, int shipC_BPartner_Location_ID,
		boolean IsSOTrx)
	{
		if (M_Product_ID != 0)
			return getProduct (ctx, M_Product_ID, billDate, shipDate, AD_Org_ID, M_Warehouse_ID,
				billC_BPartner_Location_ID, shipC_BPartner_Location_ID, IsSOTrx);
		else if (C_Charge_ID != 0)
			return getCharge (ctx, C_Charge_ID, billDate, shipDate, AD_Org_ID, M_Warehouse_ID,
				billC_BPartner_Location_ID, shipC_BPartner_Location_ID, IsSOTrx);
		else
			return getExemptTax (ctx, AD_Org_ID,IsSOTrx,0);
	}	//	get

	/**
	 *	Get Tax ID - converts parameters to call Get Tax.
	 *  <pre>
	 *		C_Charge_ID					->	C_TaxCategory_ID
	 *		billDate, shipDate			->	billDate, shipDate
	 *		AD_Org_ID					->	billFromC_Location_ID
	 *		M_Warehouse_ID				->	shipFromC_Location_ID
	 *		billC_BPartner_Location_ID  ->	billToC_Location_ID
	 *		shipC_BPartner_Location_ID 	->	shipToC_Location_ID
	 *
	 *  if IsSOTrx is false, bill and ship are reversed
	 *  </pre>
	 * 	@param ctx	context
	 * 	@param C_Charge_ID product
	 * 	@param billDate invoice date
	 * 	@param shipDate ship date
	 * 	@param AD_Org_ID org
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param billC_BPartner_Location_ID invoice location
	 * 	@param shipC_BPartner_Location_ID ship location
	 * 	@param IsSOTrx is a sales p_trx
	 * 	@return C_Tax_ID
	 *  If error it returns 0 and sets error log (TaxCriteriaNotFound)
	 */
	public static int getCharge (Ctx ctx, int C_Charge_ID,
		Timestamp billDate, Timestamp shipDate,
		int AD_Org_ID, int M_Warehouse_ID,
		int billC_BPartner_Location_ID, int shipC_BPartner_Location_ID,
		boolean IsSOTrx)
	{
		if (M_Warehouse_ID == 0)
			M_Warehouse_ID = ctx.getContextAsInt( "M_Warehouse_ID");
		if (M_Warehouse_ID == 0)
		{
			log.warning("No Warehouse - C_Charge_ID=" + C_Charge_ID);
			return 0;
		}
		int C_TaxCategory_ID = 0;
		int shipFromC_Location_ID = 0;
		int shipToC_Location_ID = 0;
		int billFromC_Location_ID = 0;
		int billToC_Location_ID = 0;
		String IsTaxExempt = null;

		//	Get all at once
		String sql = "SELECT c.C_TaxCategory_ID, o.C_Location_ID, il.C_Location_ID, b.IsTaxExempt,"
			 + " w.C_Location_ID, sl.C_Location_ID "
			 + "FROM C_Charge c, AD_OrgInfo o,"
			 + " C_BPartner_Location il INNER JOIN C_BPartner b ON (il.C_BPartner_ID=b.C_BPartner_ID),"
			 + " M_Warehouse w, C_BPartner_Location sl "
			 + "WHERE c.C_Charge_ID=?"
			 + " AND o.AD_Org_ID=?"
			 + " AND il.C_BPartner_Location_ID=?"
			 + " AND w.M_Warehouse_ID=?"
			 + " AND sl.C_BPartner_Location_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, C_Charge_ID);
			pstmt.setInt (2, AD_Org_ID);
			pstmt.setInt (3, billC_BPartner_Location_ID);
			pstmt.setInt (4, M_Warehouse_ID);
			pstmt.setInt (5, shipC_BPartner_Location_ID);
			rs = pstmt.executeQuery ();
			boolean found = false;
			if (rs.next ())
			{
				C_TaxCategory_ID = rs.getInt (1);
				billFromC_Location_ID = rs.getInt (2);
				billToC_Location_ID = rs.getInt (3);
				IsTaxExempt = rs.getString (4);
				shipFromC_Location_ID = rs.getInt (5);
				shipToC_Location_ID = rs.getInt (6);
				found = true;
			}
			//
			if (!found)
			{
				log.warning("Not found for C_Charge_ID=" + C_Charge_ID 
					+ ", AD_Org_ID=" + AD_Org_ID + ", M_Warehouse_ID=" + M_Warehouse_ID
					+ ", C_BPartner_Location_ID=" + billC_BPartner_Location_ID 
					+ "/" + shipC_BPartner_Location_ID);
				return 0;
			}
			else if ("Y".equals (IsTaxExempt))
				return getExemptTax (ctx, AD_Org_ID,IsSOTrx,C_TaxCategory_ID);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
			return 0;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//	Reverese for PO
		if (!IsSOTrx)
		{
			int temp = billFromC_Location_ID;
			billFromC_Location_ID = billToC_Location_ID;
			billToC_Location_ID = temp;
			temp = shipFromC_Location_ID;
			shipFromC_Location_ID = shipToC_Location_ID;
			shipToC_Location_ID = temp;
		}
		//
		log.fine("C_TaxCategory_ID=" + C_TaxCategory_ID
		  + ", billFromC_Location_ID=" + billFromC_Location_ID
		  + ", billToC_Location_ID=" + billToC_Location_ID
		  + ", shipFromC_Location_ID=" + shipFromC_Location_ID
		  + ", shipToC_Location_ID=" + shipToC_Location_ID);
		return get (ctx, C_TaxCategory_ID, IsSOTrx,
		  shipDate, shipFromC_Location_ID, shipToC_Location_ID,
		  billDate, billFromC_Location_ID, billToC_Location_ID);
	}	//	getCharge


	/**
	 *	Get Tax ID - converts parameters to call Get Tax.
	 *  <pre>
	 *		M_Product_ID				->	C_TaxCategory_ID
	 *		billDate, shipDate			->	billDate, shipDate
	 *		AD_Org_ID					->	billFromC_Location_ID
	 *		M_Warehouse_ID				->	shipFromC_Location_ID
	 *		billC_BPartner_Location_ID  ->	billToC_Location_ID
	 *		shipC_BPartner_Location_ID 	->	shipToC_Location_ID
	 *
	 *  if IsSOTrx is false, bill and ship are reversed
	 *  </pre>
	 * 	@param ctx	context
	 * 	@param M_Product_ID product
	 * 	@param billDate invoice date
	 * 	@param shipDate ship date
	 * 	@param AD_Org_ID org
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param billC_BPartner_Location_ID invoice location
	 * 	@param shipC_BPartner_Location_ID ship location
	 * 	@param IsSOTrx is a sales p_trx
	 * 	@return C_Tax_ID
	 *  If error it returns 0 and sets error log (TaxCriteriaNotFound)
	 */
	public static int getProduct (Ctx ctx, int M_Product_ID,
		Timestamp billDate, Timestamp shipDate,
		int AD_Org_ID, int M_Warehouse_ID,
		int billC_BPartner_Location_ID, int shipC_BPartner_Location_ID,
		boolean IsSOTrx)
	{
		String variable = "";
		int C_TaxCategory_ID = 0;
		int shipFromC_Location_ID = 0;
		int shipToC_Location_ID = 0;
		int billFromC_Location_ID = 0;
		int billToC_Location_ID = 0;
		String IsTaxExempt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			//	Get all at once
			String sql = "SELECT p.C_TaxCategory_ID, o.C_Location_ID, il.C_Location_ID, b.IsTaxExempt,"
				+ " w.C_Location_ID, sl.C_Location_ID "
				+ "FROM M_Product p, AD_OrgInfo o,"
				+ " C_BPartner_Location il INNER JOIN C_BPartner b ON (il.C_BPartner_ID=b.C_BPartner_ID),"
				+ " M_Warehouse w, C_BPartner_Location sl "
				+ "WHERE p.M_Product_ID=?"
				+ " AND o.AD_Org_ID=?"
				+ " AND il.C_BPartner_Location_ID=?"
				+ " AND w.M_Warehouse_ID=?"
				+ " AND sl.C_BPartner_Location_ID=?";
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setInt(2, AD_Org_ID);
			pstmt.setInt(3, billC_BPartner_Location_ID);
			pstmt.setInt(4, M_Warehouse_ID);
			pstmt.setInt(5, shipC_BPartner_Location_ID);
			rs = pstmt.executeQuery();
			boolean found = false;
			if (rs.next())
			{
				C_TaxCategory_ID = rs.getInt(1);
				billFromC_Location_ID = rs.getInt(2);
				billToC_Location_ID = rs.getInt(3);
				IsTaxExempt = rs.getString(4);
				shipFromC_Location_ID = rs.getInt(5);
				shipToC_Location_ID = rs.getInt(6);
				found = true;
			}
			rs.close();
			pstmt.close();
			//
			if (found && "Y".equals(IsTaxExempt))
			{
				log.fine("Business Partner is Tax exempt");
				return getExemptTax(ctx, AD_Org_ID,IsSOTrx,C_TaxCategory_ID);
			}
			else if (found)
			{
				if (!IsSOTrx)
				{
					int temp = billFromC_Location_ID;
					billFromC_Location_ID = billToC_Location_ID;
					billToC_Location_ID = temp;
					temp = shipFromC_Location_ID;
					shipFromC_Location_ID = shipToC_Location_ID;
					shipToC_Location_ID = temp;
				}
				log.fine("C_TaxCategory_ID=" + C_TaxCategory_ID
					+ ", billFromC_Location_ID=" + billFromC_Location_ID
					+ ", billToC_Location_ID=" + billToC_Location_ID
					+ ", shipFromC_Location_ID=" + shipFromC_Location_ID
					+ ", shipToC_Location_ID=" + shipToC_Location_ID);
				return get(ctx, C_TaxCategory_ID, IsSOTrx,
					shipDate, shipFromC_Location_ID, shipToC_Location_ID,
					billDate, billFromC_Location_ID, billToC_Location_ID);
			}

			// ----------------------------------------------------------------

			//	Detail for error isolation

		//	M_Product_ID				->	C_TaxCategory_ID
			sql = "SELECT C_TaxCategory_ID FROM M_Product "
				+ "WHERE M_Product_ID=?";
			variable = "M_Product_ID";
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, M_Product_ID);
			rs = pstmt.executeQuery();
			found = false;
			if (rs.next())
			{
				C_TaxCategory_ID = rs.getInt(1);
				found = true;
			}
			rs.close();
			pstmt.close();
			if (C_TaxCategory_ID == 0)
			{
				if(!Ini.isClient())
					log.saveError(Msg.getMsg(ctx, "TaxCriteriaNotFound")+":  "+ Msg.translate(ctx, variable)
							+ (found ? "" : " (Product=" + M_Product_ID + " not found)"),"");
				else
					log.saveError("TaxCriteriaNotFound", Msg.translate(ctx, variable)
							+ (found ? "" : " (Product=" + M_Product_ID + " not found)"));					
				return 0;
			}
			log.fine("C_TaxCategory_ID=" + C_TaxCategory_ID);

		//	AD_Org_ID					->	billFromC_Location_ID
			sql = "SELECT C_Location_ID FROM AD_OrgInfo "
				+ "WHERE AD_Org_ID=?";
			variable = "AD_Org_ID";
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Org_ID);
			rs = pstmt.executeQuery();
			found = false;
			if (rs.next())
			{
				billFromC_Location_ID = rs.getInt (1);
				found = true;
			}
			rs.close();
			pstmt.close();
			if (billFromC_Location_ID == 0)
			{
				if(!Ini.isClient())
					log.saveError(Msg.getMsg(ctx, "TaxCriteriaNotFound")+":  "+ Msg.translate(Env.getAD_Language(ctx), variable)
					  + (found ? "" : " (Info/Org=" + AD_Org_ID + " not found)"),"");
				else
					log.saveError("TaxCriteriaNotFound", Msg.translate(Env.getAD_Language(ctx), variable)
							  + (found ? "" : " (Info/Org=" + AD_Org_ID + " not found)"));

				return 0;
			}

		//	billC_BPartner_Location_ID  ->	billToC_Location_ID
			sql = "SELECT l.C_Location_ID, b.IsTaxExempt "
				+ "FROM C_BPartner_Location l INNER JOIN C_BPartner b ON (l.C_BPartner_ID=b.C_BPartner_ID) "
				+ "WHERE C_BPartner_Location_ID=?";
			variable = "BillTo_ID";
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, billC_BPartner_Location_ID);
			rs = pstmt.executeQuery();
			found = false;
			if (rs.next())
			{
				billToC_Location_ID = rs.getInt(1);
				IsTaxExempt = rs.getString(2);
				found = true;
			}
			rs.close();
			pstmt.close();
			if (billToC_Location_ID == 0)
			{
				if(!Ini.isClient())				
					log.saveError(Msg.getMsg(ctx, "TaxCriteriaNotFound")+":  "+ Msg.translate(Env.getAD_Language(ctx), variable)
						+ (found ? "" : " (BPLocation=" + billC_BPartner_Location_ID + " not found)"),"");
				else
					log.saveError("TaxCriteriaNotFound", Msg.translate(Env.getAD_Language(ctx), variable)
							+ (found ? "" : " (BPLocation=" + billC_BPartner_Location_ID + " not found)"));

				return 0;
			}
			if ("Y".equals(IsTaxExempt))
				return getExemptTax(ctx, AD_Org_ID,IsSOTrx,C_TaxCategory_ID);

			//  Reverse for PO
			if (!IsSOTrx)
			{
				int temp = billFromC_Location_ID;
				billFromC_Location_ID = billToC_Location_ID;
				billToC_Location_ID = temp;
			}
			log.fine("billFromC_Location_ID = " + billFromC_Location_ID);
			log.fine("billToC_Location_ID = " + billToC_Location_ID);

			//-----------------------------------------------------------------

		//	M_Warehouse_ID				->	shipFromC_Location_ID
			sql = "SELECT C_Location_ID FROM M_Warehouse "
				+ "WHERE M_Warehouse_ID=?";
			variable = "M_Warehouse_ID";
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, M_Warehouse_ID);
			rs = pstmt.executeQuery();
			found = false;
			if (rs.next())
			{
				shipFromC_Location_ID = rs.getInt (1);
				found = true;
			}
			rs.close();
			pstmt.close();
			if (shipFromC_Location_ID == 0)
			{
				if(!Ini.isClient())				
					log.saveError(Msg.getMsg(ctx, "TaxCriteriaNotFound") + ":  " + Msg.translate(Env.getAD_Language(ctx), variable)
						+ (found ? "" : " (Warehouse=" + M_Warehouse_ID + " not found)"), "");
				else
					log.saveError("TaxCriteriaNotFound", Msg.translate(Env.getAD_Language(ctx), variable)
							+ (found ? "" : " (Warehouse=" + M_Warehouse_ID + " not found)"));

				return 0;
			}

		//	shipC_BPartner_Location_ID 	->	shipToC_Location_ID
			sql = "SELECT C_Location_ID FROM C_BPartner_Location "
				+ "WHERE C_BPartner_Location_ID=?";
			variable = "C_BPartner_Location_ID";
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, shipC_BPartner_Location_ID);
			rs = pstmt.executeQuery();
			found = false;
			if (rs.next())
			{
				shipToC_Location_ID = rs.getInt (1);
				found = true;
			}
			if (shipToC_Location_ID == 0)
			{
				if(!Ini.isClient())
					log.saveError(Msg.getMsg(ctx, "TaxCriteriaNotFound")+":  "+ Msg.translate(Env.getAD_Language(ctx), variable)
						+ (found ? "" : " (BPLocation=" + shipC_BPartner_Location_ID + " not found)"),"");
				else
					log.saveError("TaxCriteriaNotFound", Msg.translate(Env.getAD_Language(ctx), variable)
							+ (found ? "" : " (BPLocation=" + shipC_BPartner_Location_ID + " not found)"));
				return 0;
			}

			//  Reverse for PO
			if (!IsSOTrx)
			{
				int temp = shipFromC_Location_ID;
				shipFromC_Location_ID = shipToC_Location_ID;
				shipToC_Location_ID = temp;
			}
			log.fine("shipFromC_Location_ID = " + shipFromC_Location_ID);
			log.fine("shipToC_Location_ID = " + shipToC_Location_ID);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "getProduct (" + variable + ")", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return get (ctx, C_TaxCategory_ID, IsSOTrx,
			shipDate, shipFromC_Location_ID, shipToC_Location_ID,
			billDate, billFromC_Location_ID, billToC_Location_ID);
	}	//	getProduct

	/**
	 * 	Get Exempt Tax Code
	 * 	@param ctx context
	 * 	@param AD_Org_ID org to find client
	 * 	@return C_Tax_ID
	 */
	private static int getExemptTax (Ctx ctx, int AD_Org_ID,boolean IsSOTrx, int C_TaxCategory_ID)
	{
		
		int C_Tax_ID = 0;
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT t.C_Tax_ID FROM C_Tax t INNER JOIN AD_Org o ON (t.AD_Client_ID=o.AD_Client_ID) ");
		sql.append("WHERE t.IsTaxExempt='Y' AND t.IsActive = 'Y' AND o.AD_Org_ID=? AND (t.SOPOType = 'B' ");
		if (IsSOTrx)
			sql.append("OR t.SOPOType = 'S') ");
		else
			sql.append("OR t.SOPOType = 'P') ");	
		if (C_TaxCategory_ID != 0)
			sql.append("AND C_TaxCategory_ID = ? ");	
			
			
		sql.append("ORDER BY t.SOPOType DESC, t.rate DESC");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean found = false;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			pstmt.setInt(1, AD_Org_ID);
			if (C_TaxCategory_ID != 0)
				pstmt.setInt(2, C_TaxCategory_ID);
			
						
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				C_Tax_ID = rs.getInt (1);
				found = true;
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		log.fine("TaxExempt=Y - C_Tax_ID=" + C_Tax_ID);
		if (C_Tax_ID == 0)
		{
			if(!Ini.isClient())
				log.saveError(Msg.getMsg(ctx, "TaxCriteriaNotFound") + ":  "+ Msg.getMsg(ctx, "TaxNoExemptFound")
					+ (found ? "" : " (Tax/Org=" + AD_Org_ID + " not found)"),"");
			else
				log.saveError("TaxCriteriaNotFound", Msg.getMsg(ctx, "TaxNoExemptFound")
						+ (found ? "" : " (Tax/Org=" + AD_Org_ID + " not found)"));

		}
		return C_Tax_ID;
	}	//	getExemptTax

	
	/**************************************************************************
	 *	Get Tax ID (Detail).
	 *  If error return 0 and set error log (TaxNotFound)
	 *  @param ctx context
	 *	@param C_TaxCategory_ID tax category
	 * 	@param IsSOTrx Sales Order Trx
	 *	@param shipDate ship date (ignored)
	 *	@param shipFromC_Locction_ID ship from (ignored)
	 *	@param shipToC_Location_ID ship to (ignored)
	 *	@param billDate invoice date
	 *	@param billFromC_Location_ID invoice from
	 *	@param billToC_Location_ID invoice to
	 *	@return C_Tax_ID
	 */
	protected static int get (Ctx ctx,
		int C_TaxCategory_ID, boolean IsSOTrx,
		Timestamp shipDate, int shipFromC_Locction_ID, int shipToC_Location_ID,
		Timestamp billDate, int billFromC_Location_ID, int billToC_Location_ID)
	{
		//	C_TaxCategory contains CommodityCode
		
		//	API to Tax Vendor comes here

		if (CLogMgt.isLevelFine())
		{
			log.info("(Detail) - Category=" + C_TaxCategory_ID 
				+ ", SOTrx=" + IsSOTrx);
			log.config("(Detail) - BillFrom=" + billFromC_Location_ID 
				+ ", BillTo=" + billToC_Location_ID + ", BillDate=" + billDate);
		}

		MTax[] taxes = MTax.getAll (ctx);
		
		MLocation lbillFrom = new MLocation (ctx, billFromC_Location_ID, null); 
		MLocation lbillTo = new MLocation (ctx, billToC_Location_ID, null);
		MLocation lshipFrom = new MLocation (ctx, shipFromC_Locction_ID, null); 
		MLocation lshipTo = new MLocation (ctx, shipToC_Location_ID, null); 

		log.finer("From=" + lbillFrom);
		log.finer("To=" + lbillTo);
		log.finer("From=" + lshipFrom);
		log.finer("To=" + lshipTo);
		
		// This object assigns a priority to a given tax/taxPostal record.
		// Any dual specified field beats any combination of single fields,
		// and single fields are used to decide between otherwise equal dual specifications.
		// Zip > Region > Country
		class MTaxPriority {
			MTax tax;
			int priority = 0;
			
			public MTaxPriority(MTax t, MTaxPostal tp){
				tax = t;
				final int DUAL_ZIP = 32;
				final int DUAL_REGION = 16;
				final int DUAL_COUNTRY = 8;
				final int ZIP = 4;
				final int REGION = 2;
				final int COUNTRY = 1;
				
				if(tp != null){
					if(tp.getPostal_To() == null)
						priority += ZIP;
					else
						priority += DUAL_ZIP;
				}
				
				int countryTotal = ((tax.getC_Country_ID() == 0)?0:1) + ((tax.getTo_Country_ID() == 0)?0:1);
				if(countryTotal == 1)
					priority += COUNTRY;
				if(countryTotal == 2)
					priority += DUAL_COUNTRY;
				
				int regionTotal = ((tax.getC_Region_ID() == 0)?0:1) + ((tax.getTo_Region_ID() == 0)?0:1);
				if(regionTotal == 1)
					priority += REGION;
				if(regionTotal == 2)
					priority += DUAL_REGION;
			}
			
			public int getC_Tax_ID(){
				return tax.getC_Tax_ID();
			}
			
			public Timestamp getValidFrom(){
				return tax.getValidFrom();
			}
			
			// returns true if this MTaxPriority beats the one being compared to
			// The MTaxPriority with higher priority wins.
			// If the priorities are tied, then the one with the more recent date wins. 
			public boolean trumps(MTaxPriority compareTax){
				if(priority > compareTax.priority ||
						(priority == compareTax.priority && getValidFrom().after(compareTax.getValidFrom())))
					return true;
				else 
					return false;
			}
		}

		ArrayList<MTaxPriority> results = new ArrayList<MTaxPriority>();
		for (MTax tax : taxes) {
			if (tax.isTaxExempt()
				|| !tax.isActive() 
				|| tax.getC_TaxCategory_ID() != C_TaxCategory_ID
				|| tax.getParent_Tax_ID() != 0)	//	user parent tax
				continue;
			if (IsSOTrx && X_C_Tax.SOPOTYPE_PurchaseTax.equals(tax.getSOPOType()))
				continue;
			if (!IsSOTrx && X_C_Tax.SOPOTYPE_SalesTax.equals(tax.getSOPOType()))
				continue;
			
			MLocation lFrom = lbillFrom;
			MLocation lTo = lbillTo;
			
			if (tax.isUseShippingAddress()) {
				lFrom = lshipFrom;
				lTo = lshipTo;
			}
				

			if (CLogMgt.isLevelFinest())
			{
				log.finest(tax.toString());
				log.finest("From Country - " + (tax.getC_Country_ID() == lFrom.getC_Country_ID() 
					|| tax.getC_Country_ID() == 0));
				log.finest("From Region - " + (tax.getC_Region_ID() == lFrom.getC_Region_ID() 
					|| tax.getC_Region_ID() == 0));
				log.finest("To Country - " + (tax.getTo_Country_ID() == lTo.getC_Country_ID() 
					|| tax.getTo_Country_ID() == 0));
				log.finest("To Region - " + (tax.getTo_Region_ID() == lTo.getC_Region_ID() 
					|| tax.getTo_Region_ID() == 0));
				log.finest("Date valid - " + (!tax.getValidFrom().after(billDate)));
			}
			
				//	From Country
			if ((tax.getC_Country_ID() == lFrom.getC_Country_ID() 
					|| tax.getC_Country_ID() == 0)
				//	From Region
				&& (tax.getC_Region_ID() == lFrom.getC_Region_ID() 
					|| tax.getC_Region_ID() == 0)
				//	To Country
				&& (tax.getTo_Country_ID() == lTo.getC_Country_ID() 
					|| tax.getTo_Country_ID() == 0)
				//	To Region
				&& (tax.getTo_Region_ID() == lTo.getC_Region_ID() 
					|| tax.getTo_Region_ID() == 0)
				//	Date
				&& !tax.getValidFrom().after(billDate)
				)
			{
				if (!tax.isPostal())
				{
					results.add(new MTaxPriority(tax, null));
					continue;
				}
				//
				MTaxPostal[] postals = tax.getPostals(false);
				for (MTaxPostal postal : postals) {
					if (postal.isActive()
						//	Postal From is mandatory
						&& lFrom.getPostal().startsWith(postal.getPostal())
						//	Postal To is optional
						&& (postal.getPostal_To() == null 
							|| lTo.getPostal().startsWith(postal.getPostal_To()))
						)
					{
						results.add(new MTaxPriority(tax, postal));
						continue;
					}
				}	//	for all postals
			}
		}	//	for all taxes
		
		//	One Result
		if (results.size() == 1)
			return results.get(0).getC_Tax_ID();
		//	Multiple results - different valid from dates
		if (results.size() > 1)
		{
			MTaxPriority latest = null;
			for (int i = 0; i < results.size(); i++)
			{
				MTaxPriority taxPriority = results.get(i);
				if (latest == null 
					|| taxPriority.trumps(latest)){
					latest = taxPriority;
				}
			}
			return latest.getC_Tax_ID();
		}		

		//	Default Tax
		for (MTax tax : taxes) {
			if (!tax.isDefault() || !tax.isActive()
				|| tax.getParent_Tax_ID() != 0)	//	user parent tax
				continue;
			if (IsSOTrx && X_C_Tax.SOPOTYPE_PurchaseTax.equals(tax.getSOPOType()))
				continue;
			if (!IsSOTrx && X_C_Tax.SOPOTYPE_SalesTax.equals(tax.getSOPOType()))
				continue;
			log.fine("(default) - " + tax);
			return tax.getC_Tax_ID();
		}	//	for all taxes
		
		if(!Ini.isClient())
			log.saveError(Msg.getMsg(ctx,"TaxNotFound"), "");
		else
			log.saveError("TaxNotFound", "");
		return 0;
	}	//	get
	
}	//	Tax
